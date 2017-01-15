package com.voyageone.task2.cms.service;

import com.google.common.base.Joiner;
import com.taobao.api.ApiException;
import com.taobao.top.schema.exception.TopSchemaException;
import com.taobao.top.schema.factory.SchemaWriter;
import com.taobao.top.schema.field.Field;
import com.taobao.top.schema.field.InputField;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.CmsChannelConfigs;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.CmsChannelConfigBean;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.tmall.exceptions.GetUpdateSchemaFailException;
import com.voyageone.components.tmall.service.TbItemSchema;
import com.voyageone.components.tmall.service.TbSimpleItemService;
import com.voyageone.ims.rule_expression.DictWord;
import com.voyageone.ims.rule_expression.MasterWord;
import com.voyageone.ims.rule_expression.RuleExpression;
import com.voyageone.ims.rule_expression.RuleJsonMapper;
import com.voyageone.service.bean.cms.CmsBtPromotionCodesBean;
import com.voyageone.service.bean.cms.CmsBtPromotionSkuBean;
import com.voyageone.service.bean.cms.product.SxData;
import com.voyageone.service.dao.cms.CmsBtTmScItemDao;
import com.voyageone.service.dao.cms.CmsBtTmTonggouFeedAttrDao;
import com.voyageone.service.dao.cms.CmsMtChannelConditionMappingConfigDao;
import com.voyageone.service.dao.cms.mongo.CmsMtPlatformCategorySchemaTmDao;
import com.voyageone.service.impl.cms.DictService;
import com.voyageone.service.impl.cms.PlatformProductUploadService;
import com.voyageone.service.impl.cms.TaobaoScItemService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.promotion.PromotionDetailService;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.sx.rule_parser.ExpressionParser;
import com.voyageone.service.model.cms.*;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategorySchemaTmModel;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.service.model.cms.mongo.product.*;
import com.voyageone.task2.base.BaseCronTaskService;
import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.base.util.TaskControlUtils;
import com.voyageone.task2.cms.model.ConditionPropValueModel;
import com.voyageone.task2.cms.service.putaway.ConditionPropValueRepo;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 天猫国际官网同购（官网直供）产品上新服务
 * 按照天猫国际要求的数据格式，将官网商品发布到天猫国际平台
 *
 * @author desmond on 2016/8/23.
 * @version 2.5.0
 * @since 2.5.0
 */
@Service
public class CmsBuildPlatformProductUploadTmTongGouService extends BaseCronTaskService {

    // 分隔符(,)
    private final static String Separtor_Coma = ",";
    // 线程数(synship.tm_task_control中设置的当前job的最大线程数"thread_count", 默认为5)
    private int threadCount = 5;
    // 抽出件数(synship.tm_task_control中设置的当前job的最大线程数"row_count", 默认为500)
    private int rowCount = 500;
    // 产品类目与主类目的匹配关系查询分类名称
    private enum TtPropName {
        tt_main_category_leaf,   // 主类目与平台叶子类目匹配关系
        tt_main_category,        // 主类目与平台一级类目匹配关系
        tt_category              // feed类目与平台一级类目匹配关系
    }

    @Autowired
    private DictService dictService;
    @Autowired
    private PlatformProductUploadService platformProductUploadService;
    @Autowired
    private ConditionPropValueRepo conditionPropValueRepo;
    @Autowired
    private SxProductService sxProductService;
    @Autowired
    private ProductService productService;
    @Autowired
    private TbSimpleItemService tbSimpleItemService;
    @Autowired
    private CmsBtTmTonggouFeedAttrDao cmsBtTmTonggouFeedAttrDao;
    @Autowired
    private CmsMtPlatformCategorySchemaTmDao platformCategorySchemaDao;
    @Autowired
    private CmsMtChannelConditionMappingConfigDao cmsMtChannelConditionMappingConfigDao;
    @Autowired
    private PromotionDetailService promotionDetailService;
    @Autowired
    private CmsBtTmScItemDao cmsBtTmScItemDao;
    @Autowired
    private TaobaoScItemService taobaoScItemService;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "CmsBuildPlatformProductUploadTmTongGouJob";
    }

    private Map<String, Map<String, List<ConditionPropValueModel>>> channelConditionConfig;

    // 主类目到平台类目的匹配
    private static List<Map<String, String>> mainCategoryMapList = new ArrayList() {{
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>Vitamins & Supplements>维生素多种维生素>维生素B>维生素 B1");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>减肥产品");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>减肥产品>利尿剂");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>减肥产品>补充品");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>减肥产品>补充品>丙酮酸");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>减肥产品>补充品>共轭亚油酸 (CLA)");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>减肥产品>补充品>厚朴黄蘖提取物补充品");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>减肥产品>食欲控制食欲压抑剂");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品> 多酶系");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>多种维生素矿物质补充品");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>抗氧化剂");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>抗氧化剂>α-硫辛酸");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>抗氧化剂>β—胡萝卜素");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>抗氧化剂>叶黄素");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>抗氧化剂>抗氧化配方");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>抗氧化剂>松树皮萃取物");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>抗氧化剂>果多酚络合物");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>抗氧化剂>番茄红素");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>抗氧化剂>碧萝芷");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>抗氧化剂>红酒萃取物");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>抗氧化剂>超氧化物歧化酶");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>抗氧化剂>辅酶Q10");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>混合必需脂肪酸");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>矿物质");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>矿物质>依普黄酮");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>矿物质>多种矿物质");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>矿物质>山羊乳清矿物质");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>矿物质>微量元素");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>矿物质>硅");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>矿物质>硒");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>矿物质>硼");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>矿物质>碘");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>矿物质>碘化钾");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>矿物质>磷酸吡哆醛 （维生素B6）(P-5-P)");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>矿物质>胶体金");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>矿物质>胶体银");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>矿物质>胶原蛋白");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>矿物质>钙");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>矿物质>钙>抗坏血酸钙");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>矿物质>钙>珊瑚钙");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>矿物质>钠");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>矿物质>钾");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>矿物质>铁");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>矿物质>铜");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>矿物质>铬");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>矿物质>锌");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>矿物质>锗");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>矿物质>锰");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>矿物质>镁");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>纤维补充品");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>维生素多种维生素");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>维生素多种维生素>多种维生素");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>维生素多种维生素>多种维生素>女士维生素");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>维生素多种维生素>多种维生素>男士维生素");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>维生素多种维生素>维他命");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>维生素多种维生素>维生素 C");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>维生素多种维生素>维生素 D");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>维生素多种维生素>维生素 E");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>维生素多种维生素>维生素 K1");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>维生素多种维生素>维生素 O");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>维生素多种维生素>维生素A");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>维生素多种维生素>维生素B");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>维生素多种维生素>维生素B>复合维生素B");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>维生素多种维生素>维生素B>生物素（维生素H）");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>维生素多种维生素>维生素B>维生素 B12");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>维生素多种维生素>维生素B>维生素 B2");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>维生素多种维生素>维生素B>维生素 B3");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>维生素多种维生素>维生素B>维生素 B5");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>维生素多种维生素>维生素B>维生素 B6");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>维生素多种维生素>维生素B>维生素 B8");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>维生素多种维生素>钾-镁");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>腺体萃取物");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>腺体萃取物>甲状腺萃取物");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>腺体萃取物>肝脏萃取物");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>腺体萃取物>肾上腺萃取物");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>腺体萃取物>胸腺萃取物");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>腺体萃取物>脾脏萃取物");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以A开头营养补充剂");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以A开头营养补充剂>余甘子");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以A开头营养补充剂>南非醉茄");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以A开头营养补充剂>天门冬属");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以A开头营养补充剂>山金车");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以A开头营养补充剂>白蜡树");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以A开头营养补充剂>芦荟");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以A开头营养补充剂>苜蓿");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以A开头营养补充剂>茴芹");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以A开头营养补充剂>菜蓟");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以A开头营养补充剂>虾青素");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以A开头营养补充剂>阿拉伯半乳聚糖");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以A开头营养补充剂>阿江榄仁树");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以A开头营养补充剂>鳄梨");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以A开头营养补充剂>黄芪属");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以B开头营养补充剂");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以B开头营养补充剂>乳香脂酸或乳香属");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以B开头营养补充剂>伏牛花");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以B开头营养补充剂>假马齿苋属");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以B开头营养补充剂>兰草");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以B开头营养补充剂>合香叶");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以B开头营养补充剂>地笋属");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以B开头营养补充剂>墨角藻");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以B开头营养补充剂>大麦草");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以B开头营养补充剂>布里格姆茶");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以B开头营养补充剂>旋花类植物");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以B开头营养补充剂>月桂果实");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以B开头营养补充剂>柴胡");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以B开头营养补充剂>款冬");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以B开头营养补充剂>水飞蓟");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以B开头营养补充剂>波耳多叶");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以B开头营养补充剂>灰胡桃");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以B开头营养补充剂>牛蒡");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以B开头营养补充剂>紫草科");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以B开头营养补充剂>红千层");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以B开头营养补充剂>罗勒");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以B开头营养补充剂>花椰菜芽");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以B开头营养补充剂>苦瓜素");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以B开头营养补充剂>荚莲");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以B开头营养补充剂>蓝升麻");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以B开头营养补充剂>蓝色马鞭草");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以B开头营养补充剂>蓝莓");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以B开头营养补充剂>蓝鸢尾");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以B开头营养补充剂>血根草");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以B开头营养补充剂>越桔");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以B开头营养补充剂>酸橙");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以B开头营养补充剂>金雀花");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以B开头营养补充剂>黄杨");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以B开头营养补充剂>黑刺莓浆果");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以B开头营养补充剂>黑升麻");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以B开头营养补充剂>黑接骨木");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以B开头营养补充剂>黑胡桃");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以B开头营养补充剂>黑醋栗");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以B开头营养补充剂>鼠李");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以C开头营养补充剂");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以C开头营养补充剂>丁香");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以C开头营养补充剂>仙人掌属");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以C开头营养补充剂>假荆芥");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以C开头营养补充剂>圣洁莓");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以C开头营养补充剂>樱桃");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以C开头营养补充剂>欧洲荚蒾树皮");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以C开头营养补充剂>款冬");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以C开头营养补充剂>洋甘菊");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以C开头营养补充剂>活性碳 ");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以C开头营养补充剂>灌木丛萃取物");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以C开头营养补充剂>猪殃殃");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以C开头营养补充剂>猫爪草");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以C开头营养补充剂>玉米须");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以C开头营养补充剂>矢车菊");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以C开头营养补充剂>穿心莲");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以C开头营养补充剂>类胡萝卜素");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以C开头营养补充剂>紫堇属块茎");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以C开头营养补充剂>紫草科植物");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以C开头营养补充剂>繁缕");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以C开头营养补充剂>老鹳草");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以C开头营养补充剂>肉桂");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以C开头营养补充剂>花菱草");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以C开头营养补充剂>芹菜籽");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以C开头营养补充剂>茅根");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以C开头营养补充剂>葛缕子");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以C开头营养补充剂>蔓越橘");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以C开头营养补充剂>角豆树");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以C开头营养补充剂>辣椒粉");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以C开头营养补充剂>锦紫苏");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以C开头营养补充剂>鼠李皮");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以D开头营养补充剂");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以D开头营养补充剂>南非钩麻");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以D开头营养补充剂>山茱萸");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以D开头营养补充剂>当归");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以D开头营养补充剂>恶魔果");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以D开头营养补充剂>红皮藻");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以D开头营养补充剂>蒲公英根");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以D开头营养补充剂>达米阿那");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以D开头营养补充剂>麻黄");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以E开头营养补充剂");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以E开头营养补充剂>小米草");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以E开头营养补充剂>护士茶");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以E开头营养补充剂>接骨木");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以E开头营养补充剂>月见草");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以E开头营养补充剂>桉树");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以E开头营养补充剂>紫锥花");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以F开头营养补充剂");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以F开头营养补充剂>False Unicorn");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以F开头营养补充剂>亚麻籽");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以F开头营养补充剂>何首乌");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以F开头营养补充剂>流苏树");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以F开头营养补充剂>白菊花");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以F开头营养补充剂>茴香");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以F开头营养补充剂>葫芦巴");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以G开头营养补充剂");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以G开头营养补充剂>人参");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以G开头营养补充剂>刺番荔枝");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以G开头营养补充剂>匙羹藤");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以G开头营养补充剂>印度古蒿");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以G开头营养补充剂>大蒜");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以G开头营养补充剂>天竺葵");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以G开头营养补充剂>姜");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以G开头营养补充剂>瓜拉那");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以G开头营养补充剂>白屈菜");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以G开头营养补充剂>白毛茛");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以G开头营养补充剂>秋麒麟草");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以G开头营养补充剂>紫苞佩兰");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以G开头营养补充剂>绿茶");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以G开头营养补充剂>葡萄柚");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以G开头营养补充剂>葡萄籽萃取物");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以G开头营养补充剂>藤黄果");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以G开头营养补充剂>银杏");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以G开头营养补充剂>雷公根");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以G开头营养补充剂>龙胆");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以H开头营养补充剂");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以H开头营养补充剂>七叶树");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以H开头营养补充剂>啤酒花");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以H开头营养补充剂>圣罗勒");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以H开头营养补充剂>夏至草");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以H开头营养补充剂>山楂");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以H开头营养补充剂>山葵");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以H开头营养补充剂>海索草");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以H开头营养补充剂>淫羊藿");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以H开头营养补充剂>石杉碱甲");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以H开头营养补充剂>绣球花");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以H开头营养补充剂>金丝桃属植物");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以I开头营养补充剂");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以I开头营养补充剂>吐根");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以I开头营养补充剂>常春藤叶");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以J开头营养补充剂");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以J开头营养补充剂>杜松");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以J开头营养补充剂>绞股蓝");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以K开头营养补充剂");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以K开头营养补充剂>卡瓦胡椒");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以K开头营养补充剂>可乐果");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以K开头营养补充剂>海藻");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以K开头营养补充剂>菜豆豆荚");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以K开头营养补充剂>葛藤");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以L开头营养补充剂");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以L开头营养补充剂>东革阿里");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以L开头营养补充剂>斗篷草");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以L开头营养补充剂>椴树");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以L开头营养补充剂>熏衣草");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以L开头营养补充剂>狭缝芹属");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以L开头营养补充剂>甘草根");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以L开头营养补充剂>蜜蜂花");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以M开头营养补充剂");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以M开头营养补充剂> 玛卡");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以M开头营养补充剂>万寿菊");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以M开头营养补充剂>巴西榥榥木");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以M开头营养补充剂>槲寄生");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以M开头营养补充剂>毛蕊花");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以M开头营养补充剂>水飞蓟");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以M开头营养补充剂>没药");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以M开头营养补充剂>益母草");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以M开头营养补充剂>绣线菊");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以M开头营养补充剂>艾蒿");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以M开头营养补充剂>药属葵");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以M开头营养补充剂>蘑菇");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以M开头营养补充剂>锦葵");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以N开头营养补充剂");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以N开头营养补充剂>胭脂仙人掌");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以N开头营养补充剂>苦楝树");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以N开头营养补充剂>荨麻");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以N开头营养补充剂>诺丽");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以O开头营养补充剂");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以O开头营养补充剂>Osha");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以O开头营养补充剂>俄勒冈葡萄根");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以O开头营养补充剂>橄榄叶");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以O开头营养补充剂>橡树");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以O开头营养补充剂>洋葱");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以O开头营养补充剂>牛至");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以P开头营养补充剂");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以P开头营养补充剂>保哥果");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以P开头营养补充剂>南瓜");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以P开头营养补充剂>块根马利筋");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以P开头营养补充剂>小长春花");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以P开头营养补充剂>木瓜蛋白酶");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以P开头营养补充剂>梅笠草");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以P开头营养补充剂>欧芹");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以P开头营养补充剂>罂粟籽");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以P开头营养补充剂>美洲花椒");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以P开头营养补充剂>芍药");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以P开头营养补充剂>薄荷");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以P开头营养补充剂>薄荷油");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以P开头营养补充剂>西番莲");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以P开头营养补充剂>车前子");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以P开头营养补充剂>车前草");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以R开头营养补充剂");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以R开头营养补充剂>厚朴黄蘖提取物");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以R开头营养补充剂>新泽西茶树");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以R开头营养补充剂>玫瑰果");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以R开头营养补充剂>红三叶草");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以R开头营养补充剂>红景天");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以R开头营养补充剂>红曲米");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以R开头营养补充剂>覆盆子");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以R开头营养补充剂>迷迭香");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以S开头营养补充剂");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以S开头营养补充剂>二蕊紫苏");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以S开头营养补充剂>五味子");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以S开头营养补充剂>北印地安蔓草");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以S开头营养补充剂>向日葵");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以S开头营养补充剂>圣约翰草");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以S开头营养补充剂>塞润榈");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以S开头营养补充剂>巴西人参根");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以S开头营养补充剂>檀香");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以S开头营养补充剂>檫木");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以S开头营养补充剂>滑榆");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以S开头营养补充剂>甜叶菊");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以S开头营养补充剂>番泻叶");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以S开头营养补充剂>艾属");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以S开头营养补充剂>荠菜");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以S开头营养补充剂>菝葜");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以S开头营养补充剂>酸模");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以S开头营养补充剂>金钮扣");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以S开头营养补充剂>黄芩属植物");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以S开头营养补充剂>鼠尾草");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以T开头营养补充剂");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以T开头营养补充剂>三果宝");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以T开头营养补充剂>北美香柏");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以T开头营养补充剂>姜黄");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以T开头营养补充剂>琼崖海棠");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以T开头营养补充剂>百里香");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以T开头营养补充剂>茶树");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以T开头营养补充剂>蒺藜属");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以U开头营养补充剂");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以U开头营养补充剂>松萝");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以U开头营养补充剂>熊果 ");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以V开头营养补充剂");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以V开头营养补充剂>缬草");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以V开头营养补充剂>长春西汀");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以V开头营养补充剂>马鞭草");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以W开头营养补充剂");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以W开头营养补充剂> 胡桃");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以W开头营养补充剂>北美靛蓝属");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以W开头营养补充剂>柳树");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以W开头营养补充剂>水苏");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以W开头营养补充剂>白松");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以W开头营养补充剂>白栎");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以W开头营养补充剂>白樟树");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以W开头营养补充剂>苦艾");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以W开头营养补充剂>野山药");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以W开头营养补充剂>野樱皮");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以W开头营养补充剂>野燕麦");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以W开头营养补充剂>金缕梅");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以Y开头营养补充剂");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以Y开头营养补充剂>丝兰");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以Y开头营养补充剂>洋蕺菜根");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以Y开头营养补充剂>皱叶酸模");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以Y开头营养补充剂>育亨宾树");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以Y开头营养补充剂>蓍草");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>草药补充品>英文名以Y开头营养补充剂>北美圣草");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>酶");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>酶>乳糖酶");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>维生素补充品>酶>脂肪酶");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>营养棒与营养饮料");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>营养棒与营养饮料>营养凝胶");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>营养棒与营养饮料>营养棒");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>营养棒与营养饮料>营养饮料营养奶昔");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>营养棒与营养饮料>营养饮料营养奶昔>营养奶昔");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>营养棒与营养饮料>营养饮料营养奶昔>营养饮料");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","健康与个人护理>饮食与营养>运动补充品");put("t_category","保健食品/膳食营养补充食品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表");put("t_brand","");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表");put("t_brand","a_line");put("t_category","手表/瑞士腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表");put("t_brand","Accutron by Bulova");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表");put("t_brand","Akribos XXIV");put("t_category","手表/日韩腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表");put("t_brand","Armani Exchange");put("t_category","手表/瑞士腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表");put("t_brand","August Steiner");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表");put("t_brand","Balmain");put("t_category","手表/瑞士腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表");put("t_brand","Bebe");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表");put("t_brand","Ben & Sons");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表");put("t_brand","Bulova Accutron II");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表");put("t_brand","Bulova");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表");put("t_brand","Burgi");put("t_category","手表/瑞士腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表");put("t_brand","Cabochon");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表");put("t_brand","Calvin Klein");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表");put("t_brand","Carrera");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表");put("t_brand","Christian Bernard");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表");put("t_brand","Christian Lacroix");put("t_category","手表/瑞士腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表");put("t_brand","Clyda");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表");put("t_brand","Columbia");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表");put("t_brand","David Yurman");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表");put("t_brand","Diesel");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表");put("t_brand","Ebel");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表");put("t_brand","Elini Barokas");put("t_category","手表/瑞士腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表");put("t_brand","Emilio Pucci");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表");put("t_brand","Eterna");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表");put("t_brand","Frederique Constant");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表");put("t_brand","Glam Rock");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表");put("t_brand","Gucci");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表");put("t_brand","Guess");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表");put("t_brand","Jorg Gray");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表");put("t_brand","Joshua & Sons");put("t_category","手表/瑞士腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表");put("t_brand","Just Cavalli");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表");put("t_brand","Kenneth Cole Reaction");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表");put("t_brand","Kenneth Jay Lane");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表");put("t_brand","Kenzo");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表");put("t_brand","Lacoste");put("t_category","手表/瑞士腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表");put("t_brand","Lancaster Italy");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表");put("t_brand","Lucien Piccard");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表");put("t_brand","Luminox");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表");put("t_brand","Marc by Marc Jacobs");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表");put("t_brand","Maurice Lacroix");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表");put("t_brand","Michael By Michael Kors");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表");put("t_brand","Michael Kors");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表");put("t_brand","Momo Design");put("t_category","手表/瑞士腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表");put("t_brand","Movado");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表");put("t_brand","Nicole Miller");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表");put("t_brand","Nike");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表");put("t_brand","Nina Ricci");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表");put("t_brand","Nixon");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表");put("t_brand","Oris");put("t_category","手表/瑞士腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表");put("t_brand","Puma");put("t_category","手表/瑞士腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表");put("t_brand","Raymond Weil");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表");put("t_brand","Red Line");put("t_category","手表/日韩腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表");put("t_brand","Salvatore Ferragamo");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表");put("t_brand","Sector");put("t_category","手表/瑞士腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表");put("t_brand","Seiko");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表");put("t_brand","Swiss Legend");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表");put("t_brand","Ted Lapidus");put("t_category","手表/瑞士腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表");put("t_brand","Tod's");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表");put("t_brand","TW Steel");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>休闲手表");put("t_brand","");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>休闲手表");put("t_brand","a_line");put("t_category","手表/瑞士腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>休闲手表");put("t_brand","Accutron by Bulova");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>休闲手表");put("t_brand","Akribos XXIV");put("t_category","手表/日韩腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>休闲手表");put("t_brand","Armani Exchange");put("t_category","手表/瑞士腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>休闲手表");put("t_brand","August Steiner");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>休闲手表");put("t_brand","Balmain");put("t_category","手表/瑞士腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>休闲手表");put("t_brand","Bebe");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>休闲手表");put("t_brand","Ben & Sons");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>休闲手表");put("t_brand","Bulova Accutron II");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>休闲手表");put("t_brand","Bulova");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>休闲手表");put("t_brand","Burgi");put("t_category","手表/瑞士腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>休闲手表");put("t_brand","Cabochon");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>休闲手表");put("t_brand","Calvin Klein");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>休闲手表");put("t_brand","Carrera");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>休闲手表");put("t_brand","Christian Bernard");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>休闲手表");put("t_brand","Christian Lacroix");put("t_category","手表/瑞士腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>休闲手表");put("t_brand","Clyda");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>休闲手表");put("t_brand","Columbia");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>休闲手表");put("t_brand","David Yurman");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>休闲手表");put("t_brand","Diesel");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>休闲手表");put("t_brand","Ebel");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>休闲手表");put("t_brand","Elini Barokas");put("t_category","手表/瑞士腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>休闲手表");put("t_brand","Emilio Pucci");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>休闲手表");put("t_brand","Eterna");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>休闲手表");put("t_brand","Frederique Constant");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>休闲手表");put("t_brand","Glam Rock");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>休闲手表");put("t_brand","Gucci");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>休闲手表");put("t_brand","Guess");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>休闲手表");put("t_brand","Jorg Gray");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>休闲手表");put("t_brand","Joshua & Sons");put("t_category","手表/瑞士腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>休闲手表");put("t_brand","Just Cavalli");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>休闲手表");put("t_brand","Kenneth Cole Reaction");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>休闲手表");put("t_brand","Kenneth Jay Lane");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>休闲手表");put("t_brand","Kenzo");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>休闲手表");put("t_brand","Lacoste");put("t_category","手表/瑞士腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>休闲手表");put("t_brand","Lancaster Italy");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>休闲手表");put("t_brand","Lucien Piccard");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>休闲手表");put("t_brand","Luminox");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>休闲手表");put("t_brand","Marc by Marc Jacobs");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>休闲手表");put("t_brand","Maurice Lacroix");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>休闲手表");put("t_brand","Michael By Michael Kors");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>休闲手表");put("t_brand","Michael Kors");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>休闲手表");put("t_brand","Momo Design");put("t_category","手表/瑞士腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>休闲手表");put("t_brand","Movado");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>休闲手表");put("t_brand","Nicole Miller");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>休闲手表");put("t_brand","Nike");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>休闲手表");put("t_brand","Nina Ricci");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>休闲手表");put("t_brand","Nixon");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>休闲手表");put("t_brand","Oris");put("t_category","手表/瑞士腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>休闲手表");put("t_brand","Puma");put("t_category","手表/瑞士腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>休闲手表");put("t_brand","Raymond Weil");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>休闲手表");put("t_brand","Red Line");put("t_category","手表/日韩腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>休闲手表");put("t_brand","Salvatore Ferragamo");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>休闲手表");put("t_brand","Sector");put("t_category","手表/瑞士腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>休闲手表");put("t_brand","Seiko");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>休闲手表");put("t_brand","Swiss Legend");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>休闲手表");put("t_brand","Ted Lapidus");put("t_category","手表/瑞士腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>休闲手表");put("t_brand","Tod's");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>休闲手表");put("t_brand","TW Steel");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>怀表");put("t_category","手表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>手表带");put("t_category","手表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>手表配件");put("t_category","手表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>手表配件>怀表表链");put("t_category","手表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>手表配件>手表上弦器");put("t_category","手表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>手表配件>手表柜>表壳");put("t_category","手表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>手表配件>手表电池");put("t_category","手表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>手表配件>手表维修工具");put("t_category","手表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>时装表");put("t_brand","a_line");put("t_category","手表/瑞士腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>时装表");put("t_brand","Accutron by Bulova");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>时装表");put("t_brand","Akribos XXIV");put("t_category","手表/日韩腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>时装表");put("t_brand","Armani Exchange");put("t_category","手表/瑞士腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>时装表");put("t_brand","August Steiner");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>时装表");put("t_brand","Balmain");put("t_category","手表/瑞士腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>时装表");put("t_brand","Bebe");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>时装表");put("t_brand","Ben & Sons");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>时装表");put("t_brand","Bulova Accutron II");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>时装表");put("t_brand","Bulova");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>时装表");put("t_brand","Burgi");put("t_category","手表/瑞士腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>时装表");put("t_brand","Cabochon");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>时装表");put("t_brand","Calvin Klein");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>时装表");put("t_brand","Carrera");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>时装表");put("t_brand","Christian Bernard");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>时装表");put("t_brand","Christian Lacroix");put("t_category","手表/瑞士腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>时装表");put("t_brand","Clyda");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>时装表");put("t_brand","Columbia");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>时装表");put("t_brand","David Yurman");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>时装表");put("t_brand","Diesel");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>时装表");put("t_brand","Ebel");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>时装表");put("t_brand","Elini Barokas");put("t_category","手表/瑞士腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>时装表");put("t_brand","Emilio Pucci");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>时装表");put("t_brand","Eterna");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>时装表");put("t_brand","Frederique Constant");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>时装表");put("t_brand","Glam Rock");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>时装表");put("t_brand","Gucci");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>时装表");put("t_brand","Guess");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>时装表");put("t_brand","Jorg Gray");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>时装表");put("t_brand","Joshua & Sons");put("t_category","手表/瑞士腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>时装表");put("t_brand","Just Cavalli");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>时装表");put("t_brand","Kenneth Cole Reaction");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>时装表");put("t_brand","Kenneth Jay Lane");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>时装表");put("t_brand","Kenzo");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>时装表");put("t_brand","Lacoste");put("t_category","手表/瑞士腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>时装表");put("t_brand","Lancaster Italy");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>时装表");put("t_brand","Lucien Piccard");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>时装表");put("t_brand","Luminox");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>时装表");put("t_brand","Marc by Marc Jacobs");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>时装表");put("t_brand","Maurice Lacroix");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>时装表");put("t_brand","Michael By Michael Kors");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>时装表");put("t_brand","Michael Kors");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>时装表");put("t_brand","Momo Design");put("t_category","手表/瑞士腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>时装表");put("t_brand","Movado");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>时装表");put("t_brand","Nicole Miller");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>时装表");put("t_brand","Nike");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>时装表");put("t_brand","Nina Ricci");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>时装表");put("t_brand","Nixon");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>时装表");put("t_brand","Oris");put("t_category","手表/瑞士腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>时装表");put("t_brand","Perrelet");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>时装表");put("t_brand","Puma");put("t_category","手表/瑞士腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>时装表");put("t_brand","Raymond Weil");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>时装表");put("t_brand","Red Line");put("t_category","手表/日韩腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>时装表");put("t_brand","Salvatore Ferragamo");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>时装表");put("t_brand","Sector");put("t_category","手表/瑞士腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>时装表");put("t_brand","Seiko");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>时装表");put("t_brand","Swiss Legend");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>时装表");put("t_brand","Ted Lapidus");put("t_category","手表/瑞士腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>时装表");put("t_brand","Tod's");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>时装表");put("t_brand","TW Steel");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>时装表");put("t_brand","Versus");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动手表");put("t_brand","");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动手表");put("t_brand","a_line");put("t_category","手表/瑞士腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动手表");put("t_brand","Accutron by Bulova");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动手表");put("t_brand","Akribos XXIV");put("t_category","手表/日韩腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动手表");put("t_brand","Armani Exchange");put("t_category","手表/瑞士腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动手表");put("t_brand","August Steiner");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动手表");put("t_brand","Balmain");put("t_category","手表/瑞士腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动手表");put("t_brand","Bebe");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动手表");put("t_brand","Ben & Sons");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动手表");put("t_brand","Bulova Accutron II");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动手表");put("t_brand","Bulova");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动手表");put("t_brand","Burgi");put("t_category","手表/瑞士腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动手表");put("t_brand","Cabochon");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动手表");put("t_brand","Calvin Klein");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动手表");put("t_brand","Carrera");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动手表");put("t_brand","Christian Bernard");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动手表");put("t_brand","Christian Lacroix");put("t_category","手表/瑞士腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动手表");put("t_brand","Clyda");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动手表");put("t_brand","Columbia");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动手表");put("t_brand","David Yurman");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动手表");put("t_brand","Diesel");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动手表");put("t_brand","Ebel");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动手表");put("t_brand","Elini Barokas");put("t_category","手表/瑞士腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动手表");put("t_brand","Emilio Pucci");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动手表");put("t_brand","Eterna");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动手表");put("t_brand","Frederique Constant");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动手表");put("t_brand","Glam Rock");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动手表");put("t_brand","Gucci");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动手表");put("t_brand","Guess");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动手表");put("t_brand","Jorg Gray");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动手表");put("t_brand","Joshua & Sons");put("t_category","手表/瑞士腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动手表");put("t_brand","Just Cavalli");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动手表");put("t_brand","Kenneth Cole Reaction");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动手表");put("t_brand","Kenneth Jay Lane");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动手表");put("t_brand","Kenzo");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动手表");put("t_brand","Lacoste");put("t_category","手表/瑞士腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动手表");put("t_brand","Lancaster Italy");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动手表");put("t_brand","Lucien Piccard");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动手表");put("t_brand","Luminox");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动手表");put("t_brand","Marc by Marc Jacobs");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动手表");put("t_brand","Maurice Lacroix");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动手表");put("t_brand","Michael By Michael Kors");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动手表");put("t_brand","Michael Kors");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动手表");put("t_brand","Momo Design");put("t_category","手表/瑞士腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动手表");put("t_brand","Movado");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动手表");put("t_brand","Nicole Miller");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动手表");put("t_brand","Nike");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动手表");put("t_brand","Nina Ricci");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动手表");put("t_brand","Nixon");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动手表");put("t_brand","Oris");put("t_category","手表/瑞士腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动手表");put("t_brand","Puma");put("t_category","手表/瑞士腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动手表");put("t_brand","Raymond Weil");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动手表");put("t_brand","Red Line");put("t_category","手表/日韩腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动手表");put("t_brand","Salvatore Ferragamo");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动手表");put("t_brand","Sector");put("t_category","手表/瑞士腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动手表");put("t_brand","Seiko");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动手表");put("t_brand","Swiss Legend");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动手表");put("t_brand","Ted Lapidus");put("t_category","手表/瑞士腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动手表");put("t_brand","Tod's");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动手表");put("t_brand","TW Steel");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动手表");put("t_brand","Versus");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动迷衍生手表");put("t_brand","a_line");put("t_category","手表/瑞士腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动迷衍生手表");put("t_brand","Accutron by Bulova");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动迷衍生手表");put("t_brand","Akribos XXIV");put("t_category","手表/日韩腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动迷衍生手表");put("t_brand","Armani Exchange");put("t_category","手表/瑞士腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动迷衍生手表");put("t_brand","August Steiner");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动迷衍生手表");put("t_brand","Balmain");put("t_category","手表/瑞士腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动迷衍生手表");put("t_brand","Bebe");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动迷衍生手表");put("t_brand","Ben & Sons");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动迷衍生手表");put("t_brand","Bulova Accutron II");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动迷衍生手表");put("t_brand","Bulova");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动迷衍生手表");put("t_brand","Burgi");put("t_category","手表/瑞士腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动迷衍生手表");put("t_brand","Cabochon");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动迷衍生手表");put("t_brand","Calvin Klein");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动迷衍生手表");put("t_brand","Carrera");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动迷衍生手表");put("t_brand","Christian Bernard");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动迷衍生手表");put("t_brand","Christian Lacroix");put("t_category","手表/瑞士腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动迷衍生手表");put("t_brand","Clyda");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动迷衍生手表");put("t_brand","Columbia");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动迷衍生手表");put("t_brand","David Yurman");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动迷衍生手表");put("t_brand","Diesel");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动迷衍生手表");put("t_brand","Ebel");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动迷衍生手表");put("t_brand","Elini Barokas");put("t_category","手表/瑞士腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动迷衍生手表");put("t_brand","Emilio Pucci");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动迷衍生手表");put("t_brand","Eterna");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动迷衍生手表");put("t_brand","Frederique Constant");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动迷衍生手表");put("t_brand","Glam Rock");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动迷衍生手表");put("t_brand","Gucci");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动迷衍生手表");put("t_brand","Guess");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动迷衍生手表");put("t_brand","Jorg Gray");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动迷衍生手表");put("t_brand","Joshua & Sons");put("t_category","手表/瑞士腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动迷衍生手表");put("t_brand","Just Cavalli");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动迷衍生手表");put("t_brand","Kenneth Cole Reaction");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动迷衍生手表");put("t_brand","Kenneth Jay Lane");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动迷衍生手表");put("t_brand","Kenzo");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动迷衍生手表");put("t_brand","Lacoste");put("t_category","手表/瑞士腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动迷衍生手表");put("t_brand","Lancaster Italy");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动迷衍生手表");put("t_brand","Lucien Piccard");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动迷衍生手表");put("t_brand","Luminox");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动迷衍生手表");put("t_brand","Marc by Marc Jacobs");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动迷衍生手表");put("t_brand","Maurice Lacroix");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动迷衍生手表");put("t_brand","Michael By Michael Kors");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动迷衍生手表");put("t_brand","Michael Kors");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动迷衍生手表");put("t_brand","Momo Design");put("t_category","手表/瑞士腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动迷衍生手表");put("t_brand","Movado");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动迷衍生手表");put("t_brand","Nicole Miller");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动迷衍生手表");put("t_brand","Nike");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动迷衍生手表");put("t_brand","Nina Ricci");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动迷衍生手表");put("t_brand","Nixon");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动迷衍生手表");put("t_brand","Oris");put("t_category","手表/瑞士腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动迷衍生手表");put("t_brand","Perrelet");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动迷衍生手表");put("t_brand","Puma");put("t_category","手表/瑞士腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动迷衍生手表");put("t_brand","Raymond Weil");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动迷衍生手表");put("t_brand","Red Line");put("t_category","手表/日韩腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动迷衍生手表");put("t_brand","Salvatore Ferragamo");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动迷衍生手表");put("t_brand","Sector");put("t_category","手表/瑞士腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动迷衍生手表");put("t_brand","Seiko");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动迷衍生手表");put("t_brand","Swiss Legend");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动迷衍生手表");put("t_brand","Ted Lapidus");put("t_category","手表/瑞士腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动迷衍生手表");put("t_brand","Tod's");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动迷衍生手表");put("t_brand","TW Steel");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","手表>运动迷衍生手表");put("t_brand","Versus");put("t_category","手表/欧美腕表");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装");put("t_sizeType","Women");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>上装T恤");put("t_sizeType","Women");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>上装T恤>Polo衫");put("t_sizeType","Women");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>上装T恤>背心吊带");put("t_sizeType","Women");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>上装T恤>背心吊带>吊带衬衣");put("t_sizeType","Women");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>上装T恤>背心吊带>背心吊带");put("t_sizeType","Women");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>上装T恤>衬衫扣角领衬衫");put("t_sizeType","Women");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>上装T恤>衬衫扣角领衬衫>扣角领衬衫");put("t_sizeType","Women");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>上装T恤>衬衫扣角领衬衫>衬衫");put("t_sizeType","Women");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>上装T恤>针织衫T恤");put("t_sizeType","Women");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>休闲运动服饰>衬衫T恤>背心");put("t_sizeType","Women");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>休闲运动服饰>衬衫T恤>骑行服");put("t_sizeType","Women");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>内衣");put("t_sizeType","Women");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>内衣>三角裤");put("t_sizeType","Women");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>内衣>三角裤>丁字裤");put("t_sizeType","Women");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>内衣>三角裤>三角裤");put("t_sizeType","Women");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>内衣>三角裤>平口裤");put("t_sizeType","Women");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>内衣>三角裤>比基尼泳装");put("t_sizeType","Women");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>内衣>吊袜带");put("t_sizeType","Women");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>内衣>塑腹胸衣");put("t_sizeType","Women");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>内衣>塑身内衣");put("t_sizeType","Women");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>内衣>塑身内衣>情趣睡衣");put("t_sizeType","Women");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>内衣>塑身内衣>无袖衬衣");put("t_sizeType","Women");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>内衣>塑身内衣>束身裤");put("t_sizeType","Women");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>内衣>塑身内衣>美体裤");put("t_sizeType","Women");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>内衣>塑身内衣>腰封");put("t_sizeType","Women");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>内衣>塑身内衣>连身衣");put("t_sizeType","Women");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>内衣>性感内衣套装");put("t_sizeType","Women");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>内衣>情趣睡衣");put("t_sizeType","Women");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>内衣>情趣睡衣>半身情趣睡衣");put("t_sizeType","Women");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>内衣>情趣睡衣>长款情趣睡衣");put("t_sizeType","Women");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>内衣>文胸");put("t_sizeType","Women");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>内衣>文胸>义乳文胸");put("t_sizeType","Women");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>内衣>文胸>半罩杯文胸");put("t_sizeType","Women");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>内衣>文胸>性感文胸");put("t_sizeType","Women");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>内衣>文胸>抹胸");put("t_sizeType","Women");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>内衣>文胸>聚拢文胸");put("t_sizeType","Women");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>内衣>文胸>裹胸");put("t_sizeType","Women");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>内衣>文胸>调整型文胸");put("t_sizeType","Women");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>内衣>文胸>运动文胸");put("t_sizeType","Women");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>内衣>无袖短裙连衫衬裤睡衣");put("t_sizeType","Women");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>内衣>无袖衬衣");put("t_sizeType","Women");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>内衣>服饰配件");put("t_sizeType","Women");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>内衣>服饰配件>内衣收纳包");put("t_sizeType","Women");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>内衣>服饰配件>文胸延长扣");put("t_sizeType","Women");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>内衣>服饰配件>硅胶隐形文胸");put("t_sizeType","Women");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>内衣>服饰配件>肩带");put("t_sizeType","Women");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>内衣>服饰配件>胸托");put("t_sizeType","Women");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>内衣>服饰配件>胸贴");put("t_sizeType","Women");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>内衣>服饰配件>防走光贴");put("t_sizeType","Women");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>半身裙");put("t_sizeType","Women");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>国家特色服饰");put("t_sizeType","Women");put("t_category","孕妇装/孕产妇用品/营养");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>外套夹克衫");put("t_sizeType","Women");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>外套夹克衫>大衣");put("t_sizeType","Women");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>外套夹克衫>夹克衫");put("t_sizeType","Women");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>外套夹克衫>皮质外套");put("t_sizeType","Women");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>外套夹克衫>羊毛外套");put("t_sizeType","Women");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>外套夹克衫>风衣");put("t_sizeType","Women");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>套装");put("t_sizeType","Women");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>套装>裙子套装");put("t_sizeType","Women");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>套装>西装");put("t_sizeType","Women");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>套装>连衣裙");put("t_sizeType","Women");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>套装>长裤套装");put("t_sizeType","Women");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>孕妇装");put("t_sizeType","Women");put("t_category","孕妇装/孕产妇用品/营养");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>孕妇装>上装");put("t_sizeType","Women");put("t_category","孕妇装/孕产妇用品/营养");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>孕妇装>上装>背心");put("t_sizeType","Women");put("t_category","孕妇装/孕产妇用品/营养");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>孕妇装>上装>衬衫");put("t_sizeType","Women");put("t_category","孕妇装/孕产妇用品/营养");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>孕妇装>上装>衬衫>哺乳衬衫");put("t_sizeType","Women");put("t_category","孕妇装/孕产妇用品/营养");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>孕妇装>上装>衬衫>孕妇衬衫");put("t_sizeType","Women");put("t_category","孕妇装/孕产妇用品/营养");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>孕妇装>上装>针织衫T恤");put("t_sizeType","Women");put("t_category","孕妇装/孕产妇用品/营养");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>孕妇装>内衣");put("t_sizeType","Women");put("t_category","孕妇装/孕产妇用品/营养");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>孕妇装>内衣>三角裤");put("t_sizeType","Women");put("t_category","孕妇装/孕产妇用品/营养");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>孕妇装>内衣>哺乳孕妇文胸");put("t_sizeType","Women");put("t_category","孕妇装/孕产妇用品/营养");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>孕妇装>内衣>孕妇托腹带");put("t_sizeType","Women");put("t_category","孕妇装/孕产妇用品/营养");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>孕妇装>内衣>收腹带");put("t_sizeType","Women");put("t_category","孕妇装/孕产妇用品/营养");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>孕妇装>内衣>文胸延长扣");put("t_sizeType","Women");put("t_category","孕妇装/孕产妇用品/营养");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>孕妇装>内衣>无袖衬衣");put("t_sizeType","Women");put("t_category","孕妇装/孕产妇用品/营养");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>孕妇装>外套夹克");put("t_sizeType","Women");put("t_category","孕妇装/孕产妇用品/营养");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>孕妇装>套装");put("t_sizeType","Women");put("t_category","孕妇装/孕产妇用品/营养");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>孕妇装>孕妇短裤");put("t_sizeType","Women");put("t_category","孕妇装/孕产妇用品/营养");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>孕妇装>孕妇短裤>休闲短裤");put("t_sizeType","Women");put("t_category","孕妇装/孕产妇用品/营养");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>孕妇装>孕妇短裤>孕妇短裙裤");put("t_sizeType","Women");put("t_category","孕妇装/孕产妇用品/营养");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>孕妇装>孕妇短裤>牛仔短裤");put("t_sizeType","Women");put("t_category","孕妇装/孕产妇用品/营养");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>孕妇装>孕妇裙");put("t_sizeType","Women");put("t_category","孕妇装/孕产妇用品/营养");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>孕妇装>孕妇连衣裙");put("t_sizeType","Women");put("t_category","孕妇装/孕产妇用品/营养");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>孕妇装>孕妇长裤");put("t_sizeType","Women");put("t_category","孕妇装/孕产妇用品/营养");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>孕妇装>毛衣");put("t_sizeType","Women");put("t_category","孕妇装/孕产妇用品/营养");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>孕妇装>泳装");put("t_sizeType","Women");put("t_category","孕妇装/孕产妇用品/营养");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>孕妇装>泳装>分体式泳装");put("t_sizeType","Women");put("t_category","孕妇装/孕产妇用品/营养");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>孕妇装>泳装>泳裙");put("t_sizeType","Women");put("t_category","孕妇装/孕产妇用品/营养");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>孕妇装>泳装>筒式泳装");put("t_sizeType","Women");put("t_category","孕妇装/孕产妇用品/营养");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>孕妇装>泳装>连体式泳装");put("t_sizeType","Women");put("t_category","孕妇装/孕产妇用品/营养");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>孕妇装>牛仔裤");put("t_sizeType","Women");put("t_category","孕妇装/孕产妇用品/营养");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>孕妇装>睡衣");put("t_sizeType","Women");put("t_category","孕妇装/孕产妇用品/营养");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>孕妇装>睡衣>上装");put("t_sizeType","Women");put("t_category","孕妇装/孕产妇用品/营养");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>孕妇装>睡衣>下装");put("t_sizeType","Women");put("t_category","孕妇装/孕产妇用品/营养");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>孕妇装>睡衣>套装");put("t_sizeType","Women");put("t_category","孕妇装/孕产妇用品/营养");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>孕妇装>紧身服饰");put("t_sizeType","Women");put("t_category","孕妇装/孕产妇用品/营养");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>孕妇装>紧身服饰>紧身衣");put("t_sizeType","Women");put("t_category","孕妇装/孕产妇用品/营养");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>孕妇装>紧身服饰>针织服饰");put("t_sizeType","Women");put("t_category","孕妇装/孕产妇用品/营养");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>帽衫家居服");put("t_sizeType","Women");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>帽衫家居服>套装");put("t_sizeType","Women");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>帽衫家居服>长裤");put("t_sizeType","Women");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>时尚帽衫");put("t_sizeType","Women");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>毛衣");put("t_sizeType","Women");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>毛衣>V领衫");put("t_sizeType","Women");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>毛衣>两件套");put("t_sizeType","Women");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>毛衣>圆领衫");put("t_sizeType","Women");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>毛衣>套头衫");put("t_sizeType","Women");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>毛衣>披肩");put("t_sizeType","Women");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>毛衣>羊毛衫");put("t_sizeType","Women");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>毛衣>背心马甲");put("t_sizeType","Women");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>毛衣>高领衫");put("t_sizeType","Women");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>泳装");put("t_sizeType","Women");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>泳装>分体式泳装");put("t_sizeType","Women");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>泳装>比基尼泳装");put("t_sizeType","Women");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>泳装>比赛训练泳装");put("t_sizeType","Women");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>泳装>沙滩裤");put("t_sizeType","Women");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>泳装>泳裙");put("t_sizeType","Women");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>泳装>筒式泳装");put("t_sizeType","Women");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>泳装>连体式泳装");put("t_sizeType","Women");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>泳装>防晒衣");put("t_sizeType","Women");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>牛仔裤");put("t_sizeType","Women");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>睡衣睡袍");put("t_sizeType","Women");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>睡衣睡袍>睡衣");put("t_sizeType","Women");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>睡衣睡袍>睡衣>睡衣下装");put("t_sizeType","Women");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>睡衣睡袍>睡衣>睡衣套装");put("t_sizeType","Women");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>睡衣睡袍>睡袍");put("t_sizeType","Women");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>睡衣睡袍>睡裙");put("t_sizeType","Women");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>短裤");put("t_sizeType","Women");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>短裤> 裙式短裤");put("t_sizeType","Women");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>短裤>休闲短裤");put("t_sizeType","Women");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>短裤>卡其短裤");put("t_sizeType","Women");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>短裤>牛仔短裤");put("t_sizeType","Women");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>短裤>百慕达短裤");put("t_sizeType","Women");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>短裤>短裙裤");put("t_sizeType","Women");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>短裤>短裤");put("t_sizeType","Women");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>短裤>裙裤");put("t_sizeType","Women");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>短裤>针织松紧腰短裤");put("t_sizeType","Women");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>背心马甲");put("t_sizeType","Women");put("t_category","孕妇装/孕产妇用品/营养");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>衬衫Polo衫");put("t_sizeType","Women");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>衬衫Polo衫>套头衫");put("t_sizeType","Women");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>衬衫Polo衫>扣角领牛津衬衫");put("t_sizeType","Women");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>衬衫Polo衫>衬衫");put("t_sizeType","Women");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>衬衫Polo衫>高领衫");put("t_sizeType","Women");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>袜子");put("t_sizeType","Women");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>袜子>女士袜子");put("t_sizeType","Women");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>袜子>女士袜子>短袜");put("t_sizeType","Women");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>袜子>女士袜子>船袜");put("t_sizeType","Women");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>袜子>女士袜子>袜裤长筒袜");put("t_sizeType","Women");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>袜子>女士袜子>靴袜");put("t_sizeType","Women");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>袜子>连裤袜");put("t_sizeType","Women");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>袜子>连裤袜>丝袜");put("t_sizeType","Women");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>袜子>连裤袜>哑光袜子");put("t_sizeType","Women");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>袜子>连裤袜>提花袜");put("t_sizeType","Women");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>袜子>连裤袜>网眼袜");put("t_sizeType","Women");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>袜子>连裤袜>美体塑形袜");put("t_sizeType","Women");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>裙装");put("t_sizeType","Women");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>裙装>太阳裙");put("t_sizeType","Women");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>裙装>娃娃裙");put("t_sizeType","Women");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>裙装>正式特殊场合礼服");put("t_sizeType","Women");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>裙装>毛料连衣裙");put("t_sizeType","Women");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>裙装>衬衫式裙装");put("t_sizeType","Women");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>裙装>裹身裙");put("t_sizeType","Women");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>裙装>连衣裙");put("t_sizeType","Women");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>裙装>连衫裙");put("t_sizeType","Women");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>裙装>连衫裤");put("t_sizeType","Women");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>运动服饰");put("t_sizeType","Women");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>运动服饰>内衣");put("t_sizeType","Women");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>运动服饰>基础内衣");put("t_sizeType","Women");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>运动服饰>基础内衣>内衣上装");put("t_sizeType","Women");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>运动服饰>基础内衣>内衣下装");put("t_sizeType","Women");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>运动服饰>基础内衣>内衣套装");put("t_sizeType","Women");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>运动服饰>夹克衫");put("t_sizeType","Women");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>运动服饰>套装");put("t_sizeType","Women");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>运动服饰>套装>田径服");put("t_sizeType","Women");put("t_brand","");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>运动服饰>套装>运动服");put("t_sizeType","Women");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>运动服饰>帽衫");put("t_sizeType","Women");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>运动服饰>打底裤");put("t_sizeType","Women");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>运动服饰>毛衣");put("t_sizeType","Women");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>运动服饰>田径服运动夹克");put("t_sizeType","Women");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>运动服饰>短裤");put("t_sizeType","Women");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>运动服饰>短裤>攀岩运动短裤");put("t_sizeType","Women");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>运动服饰>短裤>瑜伽短裤");put("t_sizeType","Women");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>运动服饰>短裤>篮球短裤");put("t_sizeType","Women");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>运动服饰>短裤>网球短裤");put("t_sizeType","Women");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>运动服饰>短裤>足球短裤");put("t_sizeType","Women");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>运动服饰>短裤>运动休闲短裤");put("t_sizeType","Women");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>运动服饰>短裤>运动健身短裤");put("t_sizeType","Women");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>运动服饰>短裤>运动短裤");put("t_sizeType","Women");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>运动服饰>短裤>骑行短裤");put("t_sizeType","Women");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>运动服饰>短裤>高尔夫球短裤");put("t_sizeType","Women");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>运动服饰>背心马甲");put("t_sizeType","Women");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>运动服饰>衬衫T恤");put("t_sizeType","Women");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>运动服饰>衬衫T恤>保龄球运动服");put("t_sizeType","Women");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>运动服饰>衬衫T恤>篮球服");put("t_sizeType","Women");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>运动服饰>衬衫T恤>足球服");put("t_sizeType","Women");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>运动服饰>衬衫T恤>运动健身服");put("t_sizeType","Women");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>运动服饰>衬衫T恤>高尔夫球服");put("t_sizeType","Women");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>运动服饰>裙式短裤");put("t_sizeType","Women");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>运动服饰>裙装");put("t_sizeType","Women");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>运动服饰>运动文胸");put("t_sizeType","Women");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>运动服饰>运动衫");put("t_sizeType","Women");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>运动服饰>运动衫>圆领");put("t_sizeType","Women");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>运动服饰>运动衫>拉链衫");put("t_sizeType","Women");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>运动服饰>运动衫裤子");put("t_sizeType","Women");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>运动服饰>运动袜");put("t_sizeType","Women");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>运动服饰>长裤");put("t_sizeType","Women");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>运动服饰>长裤>七分裤");put("t_sizeType","Women");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>运动服饰>长裤>健身运动裤装");put("t_sizeType","Women");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>运动服饰>长裤>攀岩运动裤");put("t_sizeType","Women");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>运动服饰>长裤>瑜伽裤");put("t_sizeType","Women");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>运动服饰>长裤>田径运动裤");put("t_sizeType","Women");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>运动服饰>长裤>运动裤");put("t_sizeType","Women");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>运动服饰>长裤>骑行裤装");put("t_sizeType","Women");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>运动迷衍生服饰");put("t_sizeType","Women");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>运动迷衍生服饰>T恤");put("t_sizeType","Women");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>运动迷衍生服饰>内衣");put("t_sizeType","Women");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>运动迷衍生服饰>夹克衫");put("t_sizeType","Women");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>运动迷衍生服饰>运动球衣");put("t_sizeType","Women");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>运动迷衍生服饰>运动衫");put("t_sizeType","Women");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>长裤");put("t_sizeType","Women");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>长裤>七分裤");put("t_sizeType","Women");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>长裤>休闲裤");put("t_sizeType","Women");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>长裤>工装裤");put("t_sizeType","Women");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>长裤>打底裤");put("t_sizeType","Women");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>长裤>正装裤");put("t_sizeType","Women");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>长裤>灯芯绒裤");put("t_sizeType","Women");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>长裤>针织裤");put("t_sizeType","Women");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>防寒服饰配件");put("t_sizeType","Women");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>防寒服饰配件>围巾披肩");put("t_sizeType","Women");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>防寒服饰配件>围脖");put("t_sizeType","Women");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>防寒服饰配件>帽子");put("t_sizeType","Women");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>防寒服饰配件>手套");put("t_sizeType","Women");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>防寒服饰配件>无指手套");put("t_sizeType","Women");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>防寒服饰配件>无指手套及手闷子");put("t_sizeType","Women");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>防寒服饰配件>耳罩");put("t_sizeType","Women");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>防寒服饰配件>袖筒");put("t_sizeType","Women");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>女装>防寒服饰配件>防寒服饰配件套装");put("t_sizeType","Women");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>女童装（0～24个月）");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>女童装（0～24个月）>内衣");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>女童装（0～24个月）>半身裙");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>女童装（0～24个月）>外套夹克");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>女童装（0～24个月）>婴儿全套服装");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>女童装（0～24个月）>婴儿套装");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>女童装（0～24个月）>婴儿连体冬装");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>女童装（0～24个月）>服饰套装");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>女童装（0～24个月）>毛衣");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>女童装（0～24个月）>泳装");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>女童装（0～24个月）>泳装>分体式泳装");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>女童装（0～24个月）>泳装>沙滩装");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>女童装（0～24个月）>泳装>游泳尿布裤");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>女童装（0～24个月）>泳装>连体泳装");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>女童装（0～24个月）>泳装>防晒衣防紫外线泳装");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>女童装（0～24个月）>洗礼袍");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>女童装（0～24个月）>灯笼裤");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>女童装（0～24个月）>睡衣");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>女童装（0～24个月）>短裤");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>女童装（0～24个月）>衬衫");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>女童装（0～24个月）>袜子连裤袜");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>女童装（0～24个月）>袜子连裤袜>袜子");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>女童装（0～24个月）>袜子连裤袜>连裤袜");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>女童装（0～24个月）>运动衫");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>女童装（0～24个月）>运动裤");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>女童装（0～24个月）>运动迷衍生服饰");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>女童装（0～24个月）>连体裤");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>女童装（0～24个月）>连衣裙连衫裤童装");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>女童装（0～24个月）>连裤童装");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>女童装（0～24个月）>长裤");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>T恤");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>内衣");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>内衣>三角裤");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>内衣>情趣睡衣");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>内衣>文胸");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>内衣>无袖衬衣");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>半身裙蓬蓬裙裙式裤");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>半身裙蓬蓬裙裙式裤>半身裙");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>半身裙蓬蓬裙裙式裤>蓬蓬裙");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>半身裙蓬蓬裙裙式裤>裙式裤");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>外套夹克");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>外套夹克>大衣");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>外套夹克>牛仔夹克");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>外套夹克>背心马甲");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>外套夹克>风衣");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>帽衫");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>服饰套装");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>服饰套装>短裤套装");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>服饰套装>裙子套装");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>服饰套装>裤子套装");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>毛衣");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>毛衣>套头毛衣");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>毛衣>羊毛衫两件套");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>毛衣>长外衣");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>泳装");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>泳装>分体式泳装");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>泳装>沙滩装");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>泳装>沙滩裤");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>泳装>连体泳装");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>泳装>防晒衣防紫外线泳装");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>牛仔裤");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>睡衣睡袍");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>睡衣睡袍>睡衣");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>睡衣睡袍>睡衣套装");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>睡衣睡袍>睡袍");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>睡衣睡袍>睡裙");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>睡衣睡袍>睡裤");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>短袜连裤袜");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>短袜连裤袜>短筒袜");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>短袜连裤袜>连裤袜");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>短袜连裤袜>长袜");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>短裤");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>衬衫");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>衬衫>Polo衫");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>衬衫>套头衫");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>衬衫>女装衬衫");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>衬衫>背心");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>衬衫>高领套衫");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>裙装");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>裙装>太阳裙");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>裙装>正式特殊场合礼服");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>裙装>连衫裙");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>裙装>连衫裤");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>运动服饰");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>运动服饰>半身裙");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>运动服饰>毛衣");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>运动服饰>短裤");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>运动服饰>紧身衣");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>运动服饰>衬衫");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>运动服饰>运动衫");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>运动服饰>运动裤");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>运动服饰>长裤");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>运动迷衍生服饰>T恤");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>运动迷衍生服饰>夹克衫");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>运动迷衍生服饰>帽子");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>运动迷衍生服饰>球衣");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>运动迷衍生服饰>运动衫");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>长裤");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>长裤>七分裤");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>长裤>休闲裤");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>长裤>工装裤");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>长裤>灯芯绒裤");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>长裤>针织裤打底裤");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>长裤套装>正装裤");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>防寒服饰配件");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>防寒服饰配件>帽子");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>防寒服饰配件>手套");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>防寒服饰配件>连指手套及手闷子");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>防寒服饰配件>防寒服饰配件套装");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>雨具雪具用品");put("t_category","居家日用");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>雨具雪具用品>滑雪裤装");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少女装(2-16岁)>雨具雪具用品>雨衣");put("t_category","居家日用");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少年装 (2-20岁)>T恤背心");put("t_category","男装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少年装 (2-20岁)>T恤背心吊带>T恤");put("t_category","男装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少年装 (2-20岁)>T恤背心吊带>背心");put("t_category","男装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少年装 (2-20岁)>内衣");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少年装 (2-20岁)>内衣>T恤");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少年装 (2-20岁)>内衣>三角裤");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少年装 (2-20岁)>内衣>保暖内衣");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少年装 (2-20岁)>内衣>四角裤");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少年装 (2-20岁)>内衣>平角裤");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少年装 (2-20岁)>内衣>背心");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少年装 (2-20岁)>外套夹克");put("t_category","男装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少年装 (2-20岁)>外套夹克>外套");put("t_category","男装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少年装 (2-20岁)>外套夹克>大衣");put("t_category","男装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少年装 (2-20岁)>外套夹克>牛仔夹克");put("t_category","男装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少年装 (2-20岁)>外套夹克>背心马甲");put("t_category","男装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少年装 (2-20岁)>外套夹克>风衣");put("t_category","男装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少年装 (2-20岁)>帽衫");put("t_category","男装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少年装 (2-20岁)>服饰套装");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少年装 (2-20岁)>服饰套装>短裤套装");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少年装 (2-20岁)>服饰套装>裤装套装");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少年装 (2-20岁)>毛衣");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少年装 (2-20岁)>毛衣>套头毛衣");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少年装 (2-20岁)>毛衣>羊毛衫");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少年装 (2-20岁)>毛衣>高领毛衣");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少年装 (2-20岁)>泳装");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少年装 (2-20岁)>泳装>沙滩裤");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少年装 (2-20岁)>泳装>泳裤");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少年装 (2-20岁)>泳装>防晒服防紫外线泳装");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少年装 (2-20岁)>牛仔裤");put("t_category","男装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少年装 (2-20岁)>睡衣睡袍");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少年装 (2-20岁)>睡衣睡袍>睡衣");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少年装 (2-20岁)>睡衣睡袍>睡衣套装");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少年装 (2-20岁)>睡衣睡袍>睡袍");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少年装 (2-20岁)>睡衣睡袍>睡裤");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少年装 (2-20岁)>短裤");put("t_category","男装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少年装 (2-20岁)>短裤>卡其短裤");put("t_category","男装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少年装 (2-20岁)>短裤>工装短裤");put("t_category","男装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少年装 (2-20岁)>短裤>牛仔短裤");put("t_category","男装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少年装 (2-20岁)>衬衫");put("t_category","男装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少年装 (2-20岁)>衬衫>Henley衫橄榄球衫");put("t_category","男装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少年装 (2-20岁)>衬衫>Polo衫");put("t_category","男装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少年装 (2-20岁)>衬衫>扣角领牛津衬衫");put("t_category","男装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少年装 (2-20岁)>衬衫>高领衬衫");put("t_category","男装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少年装 (2-20岁)>袜子");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少年装 (2-20岁)>运动外套");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少年装 (2-20岁)>运动外套>套装");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少年装 (2-20岁)>运动外套>运动服");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少年装 (2-20岁)>运动服饰");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少年装 (2-20岁)>运动服饰>毛衣");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少年装 (2-20岁)>运动服饰>短裤");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少年装 (2-20岁)>运动服饰>衬衫");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少年装 (2-20岁)>运动服饰>运动衫");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少年装 (2-20岁)>运动服饰>运动裤");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少年装 (2-20岁)>运动服饰>长裤");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少年装 (2-20岁)>运动迷衍生服饰");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少年装 (2-20岁)>运动迷衍生服饰>T恤");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少年装 (2-20岁)>运动迷衍生服饰>夹克衫");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少年装 (2-20岁)>运动迷衍生服饰>帽子");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少年装 (2-20岁)>运动迷衍生服饰>运动衫");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少年装 (2-20岁)>长裤");put("t_category","男装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少年装 (2-20岁)>长裤>休闲裤");put("t_category","男装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少年装 (2-20岁)>长裤>工装裤");put("t_category","男装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少年装 (2-20岁)>长裤>正装裤");put("t_category","男装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少年装 (2-20岁)>长裤>灯芯绒裤");put("t_category","男装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少年装 (2-20岁)>防寒服饰配件");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少年装 (2-20岁)>防寒服饰配件>帽子");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少年装 (2-20岁)>防寒服饰配件>手套");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少年装 (2-20岁)>防寒服饰配件>连指手套及手闷子");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少年装 (2-20岁)>防寒服饰配件>防寒服饰配件套装");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少年装 (2-20岁)>雨具雪具用品");put("t_category","居家日用");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少年装 (2-20岁)>雨具雪具用品>滑雪裤装");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>少年装 (2-20岁)>雨具雪具用品>雨衣");put("t_category","居家日用");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>男童装（0～24个月）");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>男童装（0～24个月）>内衣");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>男童装（0～24个月）>外套夹克");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>男童装（0～24个月）>套装");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>男童装（0～24个月）>婴儿全套服装");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>男童装（0～24个月）>婴儿套装");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>男童装（0～24个月）>婴儿连体冬装");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>男童装（0～24个月）>服饰套装");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>男童装（0～24个月）>毛衣");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>男童装（0～24个月）>泳装");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>男童装（0～24个月）>泳装>游泳尿布裤");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>男童装（0～24个月）>泳装>游泳短裤");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>男童装（0～24个月）>泳装>防晒衣防紫外线泳装");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>男童装（0～24个月）>灯笼裤");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>男童装（0～24个月）>睡衣");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>男童装（0～24个月）>短裤");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>男童装（0～24个月）>背带裤");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>男童装（0～24个月）>衬衫");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>男童装（0～24个月）>袜子");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>男童装（0～24个月）>运动衫");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>男童装（0～24个月）>运动裤");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>男童装（0～24个月）>运动迷衍生服饰");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>男童装（0～24个月）>连裤童装");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>少男少女及儿童服饰>男童装（0～24个月）>长裤");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件");put("t_sizeType","Men");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件");put("t_sizeType","Women");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>围巾披肩");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>围巾披肩");put("t_sizeType","Men");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>围巾披肩");put("t_sizeType","Women");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>围巾披肩>丝制围巾披肩");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>围巾披肩>丝制围巾披肩");put("t_sizeType","Men");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>围巾披肩>丝制围巾披肩");put("t_sizeType","Women");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>围巾披肩>印花围巾披肩");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>围巾披肩>印花围巾披肩");put("t_sizeType","Men");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>围巾披肩>印花围巾披肩");put("t_sizeType","Women");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>围巾披肩>山羊绒围巾披肩");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>围巾披肩>山羊绒围巾披肩");put("t_sizeType","Men");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>围巾披肩>山羊绒围巾披肩");put("t_sizeType","Women");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>围巾披肩>方形围巾披肩");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>围巾披肩>方形围巾披肩");put("t_sizeType","Men");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>围巾披肩>方形围巾披肩");put("t_sizeType","Women");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>围巾披肩>矩形围巾披肩");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>围巾披肩>矩形围巾披肩");put("t_sizeType","Men");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>围巾披肩>矩形围巾披肩");put("t_sizeType","Women");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>围巾披肩>羊毛围巾披肩");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>围巾披肩>羊毛围巾披肩");put("t_sizeType","Men");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>围巾披肩>羊毛围巾披肩");put("t_sizeType","Women");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>围巾披肩>针织围巾披肩");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>围巾披肩>针织围巾披肩");put("t_sizeType","Men");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>围巾披肩>针织围巾披肩");put("t_sizeType","Women");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>外衣披肩");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>外衣披肩");put("t_sizeType","Men");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>外衣披肩");put("t_sizeType","Women");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>妈咪包");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>妈咪包");put("t_sizeType","Men");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>妈咪包");put("t_sizeType","Women");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>帽子");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>帽子");put("t_sizeType","Men");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>帽子");put("t_sizeType","Women");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>帽子>圆顶礼帽");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>帽子>圆顶礼帽");put("t_sizeType","Men");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>帽子>圆顶礼帽");put("t_sizeType","Women");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>帽子>太阳帽");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>帽子>太阳帽");put("t_sizeType","Men");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>帽子>太阳帽");put("t_sizeType","Women");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>帽子>常礼帽");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>帽子>常礼帽");put("t_sizeType","Men");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>帽子>常礼帽");put("t_sizeType","Women");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>帽子>报童帽");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>帽子>报童帽");put("t_sizeType","Men");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>帽子>报童帽");put("t_sizeType","Women");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>帽子>棒球帽");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>帽子>棒球帽");put("t_sizeType","Men");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>帽子>棒球帽");put("t_sizeType","Women");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>帽子>牛仔帽");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>帽子>牛仔帽");put("t_sizeType","Men");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>帽子>牛仔帽");put("t_sizeType","Women");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>帽子>草帽");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>帽子>草帽");put("t_sizeType","Men");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>帽子>草帽");put("t_sizeType","Women");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>帽子>贝雷帽");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>帽子>贝雷帽");put("t_sizeType","Men");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>帽子>贝雷帽");put("t_sizeType","Women");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>帽子>软呢帽");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>帽子>软呢帽");put("t_sizeType","Men");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>帽子>软呢帽");put("t_sizeType","Women");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>帽子>针织帽无檐帽");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>帽子>针织帽无檐帽");put("t_sizeType","Men");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>帽子>针织帽无檐帽");put("t_sizeType","Women");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>帽子>鸭舌帽");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>帽子>鸭舌帽");put("t_sizeType","Men");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>帽子>鸭舌帽");put("t_sizeType","Women");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>手套");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>手套");put("t_sizeType","Men");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>手套");put("t_sizeType","Women");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>手帕");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>手帕");put("t_sizeType","Men");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>手帕");put("t_sizeType","Women");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>手提包袋");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>手提包袋");put("t_sizeType","Men");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>手提包袋");put("t_sizeType","Women");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>手提包袋>保龄球包");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>手提包袋>保龄球包");put("t_sizeType","Men");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>手提包袋>保龄球包");put("t_sizeType","Women");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>手提包袋>手包");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>手提包袋>手包");put("t_sizeType","Men");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>手提包袋>手包");put("t_sizeType","Women");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>手提包袋>手拿包");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>手提包袋>手拿包");put("t_sizeType","Men");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>手提包袋>手拿包");put("t_sizeType","Women");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>手提包袋>手提包");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>手提包袋>手提包");put("t_sizeType","Men");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>手提包袋>手提包");put("t_sizeType","Women");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>手提包袋>托特包");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>手提包袋>托特包");put("t_sizeType","Men");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>手提包袋>托特包");put("t_sizeType","Women");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>手提包袋>晚宴包");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>手提包袋>晚宴包");put("t_sizeType","Men");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>手提包袋>晚宴包");put("t_sizeType","Women");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>手提包袋>法式长棍包");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>手提包袋>法式长棍包");put("t_sizeType","Men");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>手提包袋>法式长棍包");put("t_sizeType","Women");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>电脑包");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>电脑包");put("t_sizeType","Men");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>电脑包");put("t_sizeType","Women");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>电脑包>便携式电脑包");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>电脑包>便携式电脑包");put("t_sizeType","Men");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>电脑包>便携式电脑包");put("t_sizeType","Women");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>电脑包>女士电脑包");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>电脑包>女士电脑包");put("t_sizeType","Men");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>电脑包>女士电脑包");put("t_sizeType","Women");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>电脑包>投影仪包");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>电脑包>投影仪包");put("t_sizeType","Men");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>电脑包>投影仪包");put("t_sizeType","Women");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>电脑包>滚轮电脑包");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>电脑包>滚轮电脑包");put("t_sizeType","Men");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>电脑包>滚轮电脑包");put("t_sizeType","Women");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>电脑包>笔记本双肩包");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>电脑包>笔记本双肩包");put("t_sizeType","Men");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>电脑包>笔记本双肩包");put("t_sizeType","Women");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>电脑包>笔记本箱包");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>电脑包>笔记本箱包");put("t_sizeType","Men");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>电脑包>笔记本箱包");put("t_sizeType","Women");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>电脑包>笔记本邮差包");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>电脑包>笔记本邮差包");put("t_sizeType","Men");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>电脑包>笔记本邮差包");put("t_sizeType","Women");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>电脑包及其配件>硬盘盒");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>电脑包及其配件>硬盘盒");put("t_sizeType","Men");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>电脑包及其配件>硬盘盒");put("t_sizeType","Women");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>电脑包及其配件>笔记本套");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>电脑包及其配件>笔记本套");put("t_sizeType","Men");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>电脑包及其配件>笔记本套");put("t_sizeType","Women");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>眼镜及其配件");put("t_category","ZIPPO/瑞士军刀/眼镜");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>眼镜及其配件");put("t_sizeType","Men");put("t_category","ZIPPO/瑞士军刀/眼镜");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>眼镜及其配件");put("t_sizeType","Women");put("t_category","ZIPPO/瑞士军刀/眼镜");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>眼镜及其配件>光学镜架");put("t_category","ZIPPO/瑞士军刀/眼镜");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>眼镜及其配件>光学镜架");put("t_sizeType","Men");put("t_category","ZIPPO/瑞士军刀/眼镜");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>眼镜及其配件>光学镜架");put("t_sizeType","Women");put("t_category","ZIPPO/瑞士军刀/眼镜");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>眼镜及其配件>太阳镜");put("t_category","ZIPPO/瑞士军刀/眼镜");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>眼镜及其配件>太阳镜");put("t_sizeType","Men");put("t_category","ZIPPO/瑞士军刀/眼镜");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>眼镜及其配件>太阳镜");put("t_sizeType","Women");put("t_category","ZIPPO/瑞士军刀/眼镜");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>眼镜及其配件>眼镜盒");put("t_category","ZIPPO/瑞士军刀/眼镜");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>眼镜及其配件>眼镜盒");put("t_sizeType","Men");put("t_category","ZIPPO/瑞士军刀/眼镜");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>眼镜及其配件>眼镜盒");put("t_sizeType","Women");put("t_category","ZIPPO/瑞士军刀/眼镜");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>眼镜及其配件>眼镜链及眼镜吊带");put("t_category","ZIPPO/瑞士军刀/眼镜");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>眼镜及其配件>眼镜链及眼镜吊带");put("t_sizeType","Men");put("t_category","ZIPPO/瑞士军刀/眼镜");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>眼镜及其配件>眼镜链及眼镜吊带");put("t_sizeType","Women");put("t_category","ZIPPO/瑞士军刀/眼镜");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>箱包");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>箱包");put("t_sizeType","Men");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>箱包");put("t_sizeType","Women");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>箱包>公文包");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>箱包>公文包");put("t_sizeType","Men");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>箱包>公文包");put("t_sizeType","Women");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>箱包>手提包及背包");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>箱包>手提包及背包");put("t_sizeType","Men");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>箱包>手提包及背包");put("t_sizeType","Women");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>箱包>手提包及背包>双肩包");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>箱包>手提包及背包>双肩包");put("t_sizeType","Men");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>箱包>手提包及背包>双肩包");put("t_sizeType","Women");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>箱包>手提包及背包>行李包");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>箱包>手提包及背包>行李包");put("t_sizeType","Men");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>箱包>手提包及背包>行李包");put("t_sizeType","Women");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>箱包>手提包及背包>运动包");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>箱包>手提包及背包>运动包");put("t_sizeType","Men");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>箱包>手提包及背包>运动包");put("t_sizeType","Women");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>箱包>手提包及背包>邮差包");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>箱包>手提包及背包>邮差包");put("t_sizeType","Men");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>箱包>手提包及背包>邮差包");put("t_sizeType","Women");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>箱包>旅行包");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>箱包>旅行包");put("t_sizeType","Men");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>箱包>旅行包");put("t_sizeType","Women");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>箱包>旅行包>包装盒");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>箱包>旅行包>包装盒");put("t_sizeType","Men");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>箱包>旅行包>包装盒");put("t_sizeType","Women");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>箱包>旅行包>文件包");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>箱包>旅行包>文件包");put("t_sizeType","Men");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>箱包>旅行包>文件包");put("t_sizeType","Women");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>箱包>旅行装备>行李箱挂牌");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>箱包>旅行装备>行李箱挂牌");put("t_sizeType","Men");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>箱包>旅行装备>行李箱挂牌");put("t_sizeType","Women");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>箱包>旅行装备>鞋子收纳袋");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>箱包>旅行装备>鞋子收纳袋");put("t_sizeType","Men");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>箱包>旅行装备>鞋子收纳袋");put("t_sizeType","Women");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>箱包>箱包");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>箱包>箱包");put("t_sizeType","Men");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>箱包>箱包");put("t_sizeType","Women");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>箱包>箱包>便携式行李包");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>箱包>箱包>便携式行李包");put("t_sizeType","Men");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>箱包>箱包>便携式行李包");put("t_sizeType","Women");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>箱包>箱包>托特包");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>箱包>箱包>托特包");put("t_sizeType","Men");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>箱包>箱包>托特包");put("t_sizeType","Women");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>箱包>箱包>折叠式旅行袋");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>箱包>箱包>折叠式旅行袋");put("t_sizeType","Men");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>箱包>箱包>折叠式旅行袋");put("t_sizeType","Women");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>箱包>箱包>滚轮式行李箱");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>箱包>箱包>滚轮式行李箱");put("t_sizeType","Men");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>箱包>箱包>滚轮式行李箱");put("t_sizeType","Women");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>箱包>箱包>硬壳行李箱");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>箱包>箱包>硬壳行李箱");put("t_sizeType","Men");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>箱包>箱包>硬壳行李箱");put("t_sizeType","Women");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>箱包>箱包>箱包套件");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>箱包>箱包>箱包套件");put("t_sizeType","Men");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>箱包>箱包>箱包套件");put("t_sizeType","Women");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>背带");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>背带");put("t_sizeType","Men");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>背带");put("t_sizeType","Women");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>腰带");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>腰带");put("t_sizeType","Men");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>腰带");put("t_sizeType","Women");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>运动迷衍生服饰配件");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>运动迷衍生服饰配件");put("t_sizeType","Men");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>运动迷衍生服饰配件");put("t_sizeType","Women");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>运动迷衍生服饰配件>太阳镜");put("t_category","ZIPPO/瑞士军刀/眼镜");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>运动迷衍生服饰配件>太阳镜");put("t_sizeType","Men");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>运动迷衍生服饰配件>太阳镜");put("t_sizeType","Women");put("t_category","ZIPPO/瑞士军刀/眼镜");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>运动迷衍生服饰配件>帽子");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>运动迷衍生服饰配件>帽子");put("t_sizeType","Men");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>运动迷衍生服饰配件>帽子");put("t_sizeType","Women");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>运动迷衍生服饰配件>帽子>太阳帽");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>运动迷衍生服饰配件>帽子>太阳帽");put("t_sizeType","Men");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>运动迷衍生服饰配件>帽子>太阳帽");put("t_sizeType","Women");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>运动迷衍生服饰配件>帽子>安全帽");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>运动迷衍生服饰配件>帽子>安全帽");put("t_sizeType","Men");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>运动迷衍生服饰配件>帽子>安全帽");put("t_sizeType","Women");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>运动迷衍生服饰配件>帽子>棒球帽");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>运动迷衍生服饰配件>帽子>棒球帽");put("t_sizeType","Men");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>运动迷衍生服饰配件>帽子>棒球帽");put("t_sizeType","Women");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>运动迷衍生服饰配件>帽子>针织帽");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>运动迷衍生服饰配件>帽子>针织帽");put("t_sizeType","Men");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>运动迷衍生服饰配件>帽子>针织帽");put("t_sizeType","Women");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>运动迷衍生服饰配件>手提包袋");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>运动迷衍生服饰配件>手提包袋");put("t_sizeType","Men");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>运动迷衍生服饰配件>手提包袋");put("t_sizeType","Women");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>运动迷衍生服饰配件>背包背袋");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>运动迷衍生服饰配件>背包背袋");put("t_sizeType","Men");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>运动迷衍生服饰配件>背包背袋");put("t_sizeType","Women");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>运动迷衍生服饰配件>背包背袋>托特包");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>运动迷衍生服饰配件>背包背袋>托特包");put("t_sizeType","Men");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>运动迷衍生服饰配件>背包背袋>托特包");put("t_sizeType","Women");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>运动迷衍生服饰配件>背包背袋>旅行包");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>运动迷衍生服饰配件>背包背袋>旅行包");put("t_sizeType","Men");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>运动迷衍生服饰配件>背包背袋>旅行包");put("t_sizeType","Women");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>运动迷衍生服饰配件>背包背袋>背包");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>运动迷衍生服饰配件>背包背袋>背包");put("t_sizeType","Men");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>运动迷衍生服饰配件>背包背袋>背包");put("t_sizeType","Women");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>运动迷衍生服饰配件>腰带");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>运动迷衍生服饰配件>腰带");put("t_sizeType","Men");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>运动迷衍生服饰配件>腰带");put("t_sizeType","Women");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>运动迷衍生服饰配件>钥匙扣");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>运动迷衍生服饰配件>钥匙扣");put("t_sizeType","Men");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>运动迷衍生服饰配件>钥匙扣");put("t_sizeType","Women");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>运动迷衍生服饰配件>钱包");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>运动迷衍生服饰配件>钱包");put("t_sizeType","Men");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>运动迷衍生服饰配件>钱包");put("t_sizeType","Women");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>运动迷衍生服饰配件>防寒服饰配件");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>运动迷衍生服饰配件>防寒服饰配件");put("t_sizeType","Men");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>运动迷衍生服饰配件>防寒服饰配件");put("t_sizeType","Women");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>运动迷衍生服饰配件>防寒服饰配件>围巾披肩");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>运动迷衍生服饰配件>防寒服饰配件>围巾披肩");put("t_sizeType","Men");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>运动迷衍生服饰配件>防寒服饰配件>围巾披肩");put("t_sizeType","Women");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>运动迷衍生服饰配件>防寒服饰配件>帽子");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>运动迷衍生服饰配件>防寒服饰配件>帽子");put("t_sizeType","Men");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>运动迷衍生服饰配件>防寒服饰配件>帽子");put("t_sizeType","Women");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>运动迷衍生服饰配件>防寒服饰配件>手套");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>运动迷衍生服饰配件>防寒服饰配件>手套");put("t_sizeType","Men");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>运动迷衍生服饰配件>防寒服饰配件>手套");put("t_sizeType","Women");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>运动迷衍生服饰配件>雨伞");put("t_category","居家日用");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>运动迷衍生服饰配件>雨伞");put("t_sizeType","Men");put("t_category","居家日用");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>运动迷衍生服饰配件>雨伞");put("t_sizeType","Women");put("t_category","居家日用");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>运动迷衍生服饰配件>领带");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>运动迷衍生服饰配件>领带");put("t_sizeType","Men");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>运动迷衍生服饰配件>领带");put("t_sizeType","Women");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>钱包卡包钥匙包");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>钱包卡包钥匙包");put("t_sizeType","Men");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>钱包卡包钥匙包");put("t_sizeType","Women");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>钱包卡包钥匙包>卡包");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>钱包卡包钥匙包>卡包");put("t_sizeType","Men");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>钱包卡包钥匙包>卡包");put("t_sizeType","Women");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>钱包卡包钥匙包>名片夹");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>钱包卡包钥匙包>名片夹");put("t_sizeType","Men");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>钱包卡包钥匙包>名片夹");put("t_sizeType","Women");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>钱包卡包钥匙包>护照夹");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>钱包卡包钥匙包>护照夹");put("t_sizeType","Men");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>钱包卡包钥匙包>护照夹");put("t_sizeType","Women");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>钱包卡包钥匙包>支票夹");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>钱包卡包钥匙包>支票夹");put("t_sizeType","Men");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>钱包卡包钥匙包>支票夹");put("t_sizeType","Women");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>钱包卡包钥匙包>皮夹钱包");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>钱包卡包钥匙包>皮夹钱包");put("t_sizeType","Men");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>钱包卡包钥匙包>皮夹钱包");put("t_sizeType","Women");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>钱包卡包钥匙包>皮夹钱包>三折钱夹");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>钱包卡包钥匙包>皮夹钱包>三折钱夹");put("t_sizeType","Men");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>钱包卡包钥匙包>皮夹钱包>三折钱夹");put("t_sizeType","Women");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>钱包卡包钥匙包>皮夹钱包>卡包");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>钱包卡包钥匙包>皮夹钱包>卡包");put("t_sizeType","Men");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>钱包卡包钥匙包>皮夹钱包>卡包");put("t_sizeType","Women");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>钱包卡包钥匙包>皮夹钱包>双折钱包");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>钱包卡包钥匙包>皮夹钱包>双折钱包");put("t_sizeType","Men");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>钱包卡包钥匙包>皮夹钱包>双折钱包");put("t_sizeType","Women");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>钱包卡包钥匙包>皮夹钱包>钱夹");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>钱包卡包钥匙包>皮夹钱包>钱夹");put("t_sizeType","Men");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>钱包卡包钥匙包>皮夹钱包>钱夹");put("t_sizeType","Women");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>钱包卡包钥匙包>钥匙圈或钥匙链包");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>钱包卡包钥匙包>钥匙圈或钥匙链包");put("t_sizeType","Men");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>钱包卡包钥匙包>钥匙圈或钥匙链包");put("t_sizeType","Women");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>钱包卡包钥匙包>钱包");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>钱包卡包钥匙包>钱包");put("t_sizeType","Men");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>钱包卡包钥匙包>钱包");put("t_sizeType","Women");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>钱包卡包钥匙包>钱夹");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>钱包卡包钥匙包>钱夹");put("t_sizeType","Men");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>钱包卡包钥匙包>钱夹");put("t_sizeType","Women");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>钱包卡包钥匙包>零钱包");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>钱包卡包钥匙包>零钱包");put("t_sizeType","Men");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>钱包卡包钥匙包>零钱包");put("t_sizeType","Women");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>防寒服饰配件");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>防寒服饰配件");put("t_sizeType","Men");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>防寒服饰配件");put("t_sizeType","Women");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>防寒服饰配件>围巾披肩");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>防寒服饰配件>围巾披肩");put("t_sizeType","Men");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>防寒服饰配件>围巾披肩");put("t_sizeType","Women");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>防寒服饰配件>围脖");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>防寒服饰配件>围脖");put("t_sizeType","Men");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>防寒服饰配件>围脖");put("t_sizeType","Women");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>防寒服饰配件>帽子");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>防寒服饰配件>帽子");put("t_sizeType","Men");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>防寒服饰配件>帽子");put("t_sizeType","Women");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>防寒服饰配件>手套");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>防寒服饰配件>手套");put("t_sizeType","Men");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>防寒服饰配件>手套");put("t_sizeType","Women");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>防寒服饰配件>耳罩");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>防寒服饰配件>耳罩");put("t_sizeType","Men");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>防寒服饰配件>耳罩");put("t_sizeType","Women");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>防寒服饰配件>袖筒");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>防寒服饰配件>袖筒");put("t_sizeType","Men");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>防寒服饰配件>袖筒");put("t_sizeType","Women");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>防寒服饰配件>连指手套及手闷子");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>防寒服饰配件>连指手套及手闷子");put("t_sizeType","Men");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>防寒服饰配件>连指手套及手闷子");put("t_sizeType","Women");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>防寒服饰配件>防寒服饰配件套装");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>防寒服饰配件>防寒服饰配件套装");put("t_sizeType","Men");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>防寒服饰配件>防寒服饰配件套装");put("t_sizeType","Women");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>防寒服饰配件>露指手套");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>防寒服饰配件>露指手套");put("t_sizeType","Men");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>防寒服饰配件>露指手套");put("t_sizeType","Women");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>雨伞");put("t_category","居家日用");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>雨伞");put("t_sizeType","Men");put("t_category","居家日用");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>雨伞");put("t_sizeType","Women");put("t_category","居家日用");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>领带装饰带");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>领带装饰带");put("t_sizeType","Men");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>领带装饰带");put("t_sizeType","Women");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>领带装饰带>装饰带");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>领带装饰带>装饰带");put("t_sizeType","Men");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>领带装饰带>装饰带");put("t_sizeType","Women");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>领带装饰带>领带");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>领带装饰带>领带");put("t_sizeType","Men");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>领带装饰带>领带");put("t_sizeType","Women");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>领带装饰带>领结");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>领带装饰带>领结");put("t_sizeType","Men");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>服饰配件>领带装饰带>领结");put("t_sizeType","Women");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>军用服装");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>工作服制服");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>工作服制服>医护制服");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>工作服制服>医护制服>医师帽");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>工作服制服>医护制服>医护夹克衫");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>工作服制服>医护制服>医护工作套装");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>工作服制服>医护制服>医护工作衬衫");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>工作服制服>医护制服>医护工作裤");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>工作服制服>厨师制服");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>工作服制服>厨师制服>厨师帽");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>工作服制服>厨师制服>厨师服");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>工作服制服>厨师制服>厨师裤");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>工作服制服>学生装");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>工作服制服>工装制服");put("t_category","女装/女士精品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>情趣服装");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>情趣服装> 塑腹胸衣");put("t_sizeType","Women");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>情趣服装>T恤");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>情趣服装>三角裤");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>情趣服装>吊袜带");put("t_sizeType","Women");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>情趣服装>性感内衣套装");put("t_sizeType","Women");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>情趣服装>文胸");put("t_sizeType","Women");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>情趣服装>无袖短裙连衫衬裤睡衣");put("t_sizeType","Women");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>情趣服装>无袖短裙连衫衬裤睡衣>无袖短裙");put("t_sizeType","Women");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>情趣服装>无袖短裙连衫衬裤睡衣>睡衣");put("t_sizeType","Women");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>情趣服装>无袖短裙连衫衬裤睡衣>连衫衬裤");put("t_sizeType","Women");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>情趣服装>无袖衬衣");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>情趣服装>服装");put("t_sizeType","Women");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>情趣服装>服饰配件");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>情趣服装>泳装");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>情趣服装>男士情趣服装");put("t_sizeType","Men");put("t_category","男装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>情趣服装>睡衣睡袍");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>情趣服装>腰带扣");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>情趣服装>衬裙");put("t_sizeType","Women");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>情趣服装>袜子");put("t_category","运动包/户外包/配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>情趣服装>裙装");put("t_sizeType","Women");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>情趣服装>运动衫");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>服装及其配件");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>服装及其配件>假发");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>服装及其配件>假发>儿童与婴儿");put("t_sizeType","Baby");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>服装及其配件>假发>女士");put("t_sizeType","Women");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>服装及其配件>假发>男士");put("t_sizeType","Men");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>服装及其配件>口罩");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>服装及其配件>口罩>儿童与婴儿");put("t_sizeType","Baby");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>服装及其配件>口罩>女士口罩");put("t_sizeType","Women");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>服装及其配件>口罩>男士口罩");put("t_sizeType","Men");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>服装及其配件>帽子");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>服装及其配件>帽子>儿童与婴儿");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>服装及其配件>帽子>女帽");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>服装及其配件>帽子>男帽");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>服装及其配件>更多服饰配件");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>服装及其配件>更多服饰配件>儿童与婴儿");put("t_sizeType","Baby");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>服装及其配件>更多服饰配件>女士");put("t_sizeType","Women");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>服装及其配件>更多服饰配件>男士");put("t_sizeType","Men");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>服装及其配件>鞋子>儿童与婴儿");put("t_sizeType","Baby");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>服装及其配件>鞋子>女鞋");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>服装及其配件>鞋子>男鞋");put("t_sizeType","Men");put("t_category","男鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>保龄球球运动服饰");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>保龄球球运动服饰>夹克衫");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>保龄球运动服饰>运动衬衫");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>冰球运动服饰");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>单板滑雪运动服饰");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>单板滑雪运动服饰>单板滑雪袜");put("t_category","运动/瑜伽/健身/球迷用品/网球/运动袜");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>单板滑雪运动服饰>夹克衫");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>单板滑雪运动服饰>运动休闲裤");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>徒步运动服饰");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>徒步运动服饰>徒步袜");put("t_category","运动/瑜伽/健身/球迷用品/网球/运动袜");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>徒步运动服饰>运动休闲裤");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>徒步运动服饰>运动衬衫");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>拳击服");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>拳击服>拳击战袍");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>拳击服>球衣");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>拳击服>运动短裤");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>排球运动服饰");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>排球运动服饰>排球袜");put("t_category","运动/瑜伽/健身/球迷用品/网球/运动袜");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>排球运动服饰>球衣");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>排球运动服饰>运动短裤");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>棒球垒球运动服饰");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>棒球垒球运动服饰>棒球垒球袜");put("t_category","运动/瑜伽/健身/球迷用品/网球/运动袜");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>棒球垒球运动服饰>球衣");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>棒球垒球运动服饰>运动短裤");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>橄榄球运动服饰");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>橄榄球运动服饰>夹克衫");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>橄榄球运动服饰>橄榄球袜");put("t_category","运动/瑜伽/健身/球迷用品/网球/运动袜");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>橄榄球运动服饰>球衣");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>橄榄球运动服饰>运动短裤");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>游泳运动服饰");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>游泳运动服饰>分体泳装");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>游泳运动服饰>泳装");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>游泳运动服饰>游泳裤");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>游泳运动服饰>运动服饰套装");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>游泳运动服饰>连体泳装");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>滑板运动服饰");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>滑板运动服饰>T恤");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>滑板运动服饰>运动帽衫");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>滑板运动服饰>运动短裤");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>滑雪运动服饰");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>滑雪运动服饰>夹克衫");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>滑雪运动服饰>滑雪袜");put("t_category","运动/瑜伽/健身/球迷用品/网球/运动袜");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>滑雪运动服饰>背带滑雪裤");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>滑雪运动服饰>运动休闲裤");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>瑜伽运动服饰");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>瑜伽运动服饰>瑜伽袜");put("t_category","运动/瑜伽/健身/球迷用品/网球/运动袜");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>瑜伽运动服饰>运动休闲裤");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>瑜伽运动服饰>运动短裤");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>瑜伽运动服饰>运动衬衫");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>登山运动服饰");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>登山运动服饰>夹克衫");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>登山运动服饰>登山袜");put("t_category","运动/瑜伽/健身/球迷用品/网球/运动袜");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>登山运动服饰>运动休闲裤");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>登山运动服饰>运动短裤");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>登山运动服饰>运动衬衫");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>篮球运动服饰");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>篮球运动服饰>球衣");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>篮球运动服饰>篮球袜");put("t_category","运动/瑜伽/健身/球迷用品/网球/运动袜");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>篮球运动服饰>运动短裤");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>网球运动服饰");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>网球运动服饰>夹克衫");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>网球运动服饰>网球袜");put("t_category","运动/瑜伽/健身/球迷用品/网球/运动袜");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>网球运动服饰>网球裙");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>网球运动服饰>裙装");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>网球运动服饰>运动休闲裤");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>网球运动服饰>运动短裤");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>网球运动服饰>运动衬衫");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>网球运动服饰>运动裙式短裤");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>英式足球或橄榄球运动服饰");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>英式足球或橄榄球运动服饰>夹克衫");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>英式足球或橄榄球运动服饰>球衣");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>英式足球或橄榄球运动服饰>英式足球或橄榄球袜");put("t_category","运动/瑜伽/健身/球迷用品/网球/运动袜");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>足球运动服饰");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>足球运动服饰>球衣");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>足球运动服饰>足球袜");put("t_category","运动/瑜伽/健身/球迷用品/网球/运动袜");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>足球运动服饰>运动短裤");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>跑步运动服饰");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>跑步运动服饰>夹克衫");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>跑步运动服饰>跑步袜");put("t_category","运动/瑜伽/健身/球迷用品/网球/运动袜");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>跑步运动服饰>运动休闲裤");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>跑步运动服饰>运动短裤");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>跑步运动服饰>运动紧身裤");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>骑行运动服饰");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>骑行运动服饰>夹克衫");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>骑行运动服饰>球衣");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>骑行运动服饰>背心马甲");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>骑行运动服饰>运动休闲裤");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>骑行运动服饰>运动短裤");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>骑行运动服饰>骑行紧身衣");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>骑行运动服饰>骑行背带短裤");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>骑行运动服饰>骑行袜");put("t_category","运动/瑜伽/健身/球迷用品/网球/运动袜");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>高尔夫运动服饰");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>高尔夫运动服饰>夹克衫");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>高尔夫运动服饰>运动休闲裤");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>高尔夫运动服饰>运动短裤");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>高尔夫运动服饰>运动衬衫");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>运动服饰>高尔夫运动服饰>高尔夫袜");put("t_category","运动/瑜伽/健身/球迷用品/网球/运动袜");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>音乐衍生服饰");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>音乐衍生服饰>T恤");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>音乐衍生服饰>服饰配件");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>特殊服饰>音乐衍生服饰>运动衫");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装");put("t_sizeType","Men");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>T恤");put("t_sizeType","Men");put("t_category","男装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>保暖内衣");put("t_sizeType","Men");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>保暖内衣>上装");put("t_sizeType","Men");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>保暖内衣>下装");put("t_sizeType","Men");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>保暖内衣>套装");put("t_sizeType","Men");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>内衣");put("t_sizeType","Men");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>内衣>丁字裤");put("t_sizeType","Men");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>内衣>三角裤");put("t_sizeType","Men");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>内衣>内裤");put("t_sizeType","Men");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>内衣>四角裤");put("t_sizeType","Men");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>外套夹克衫");put("t_sizeType","Men");put("t_category","男装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>外套夹克衫>外套");put("t_sizeType","Men");put("t_category","男装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>外套夹克衫>夹克衫");put("t_sizeType","Men");put("t_category","男装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>外套夹克衫>微纤维外套");put("t_sizeType","Men");put("t_category","男装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>外套夹克衫>皮质外套");put("t_sizeType","Men");put("t_category","男装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>外套夹克衫>羊毛外套");put("t_sizeType","Men");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>外套夹克衫>羊绒衫");put("t_sizeType","Men");put("t_category","男装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>外套夹克衫>羽绒服");put("t_sizeType","Men");put("t_category","男装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>外套夹克衫>风衣");put("t_sizeType","Men");put("t_category","男装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>帽衫运动夹克衫");put("t_sizeType","Men");put("t_category","男装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>帽衫运动夹克衫>帽衫");put("t_sizeType","Men");put("t_category","男装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>帽衫运动夹克衫>运动衫");put("t_sizeType","Men");put("t_category","男装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>毛衣");put("t_sizeType","Men");put("t_category","男装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>毛衣>Polo衫");put("t_sizeType","Men");put("t_category","男装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>毛衣>V领衫");put("t_sizeType","Men");put("t_category","男装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>毛衣>圆领衫");put("t_sizeType","Men");put("t_category","男装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>毛衣>羊毛衫");put("t_sizeType","Men");put("t_category","男装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>毛衣>背心马甲");put("t_sizeType","Men");put("t_category","男装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>毛衣>高领衫");put("t_sizeType","Men");put("t_category","男装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>泳装");put("t_sizeType","Men");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>泳装>三角裤");put("t_sizeType","Men");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>泳装>比赛训练泳装");put("t_sizeType","Men");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>泳装>沙滩裤");put("t_sizeType","Men");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>泳装>游泳裤");put("t_sizeType","Men");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>泳装>防晒衣");put("t_sizeType","Men");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>牛仔裤");put("t_sizeType","Men");put("t_category","男装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>睡衣睡袍");put("t_sizeType","Men");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>睡衣睡袍>睡衣");put("t_sizeType","Men");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>睡衣睡袍>睡衣>睡衣上装");put("t_sizeType","Men");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>睡衣睡袍>睡衣>睡衣下装");put("t_sizeType","Men");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>睡衣睡袍>睡衣>睡衣套装");put("t_sizeType","Men");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>睡衣睡袍>睡袍");put("t_sizeType","Men");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>短裤");put("t_sizeType","Men");put("t_category","男装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>背心马甲");put("t_sizeType","Men");put("t_category","男装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>衬衫Polo衫");put("t_sizeType","Men");put("t_category","男装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>衬衫Polo衫>Henley衫橄榄球衫");put("t_sizeType","Men");put("t_category","男装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>衬衫Polo衫>Polo衫");put("t_sizeType","Men");put("t_category","男装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>衬衫Polo衫>扣角领牛津衬衫");put("t_sizeType","Men");put("t_category","男装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>衬衫Polo衫>衬衫");put("t_sizeType","Men");put("t_category","男装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>衬衫Polo衫>高领衫");put("t_sizeType","Men");put("t_category","男装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>袜子");put("t_sizeType","Men");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>袜子>商务男袜");put("t_sizeType","Men");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>袜子>短筒袜");put("t_sizeType","Men");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>裤装");put("t_sizeType","Men");put("t_category","男装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>裤装>休闲裤");put("t_sizeType","Men");put("t_category","男装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>裤装>加绒裤");put("t_sizeType","Men");put("t_category","男装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>裤装>工装裤");put("t_sizeType","Men");put("t_category","男装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>裤装>正装裤");put("t_sizeType","Men");put("t_category","男装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>西装运动外套");put("t_sizeType","Men");put("t_category","男装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>西装运动外套>西装");put("t_sizeType","Men");put("t_category","男装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>西装运动外套>运动外套西装");put("t_sizeType","Men");put("t_category","男装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>贴身内衣");put("t_sizeType","Men");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>贴身内衣>T恤");put("t_sizeType","Men");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>贴身内衣>背心");put("t_sizeType","Men");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>运动服饰");put("t_sizeType","Men");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>运动服饰>内衣");put("t_sizeType","Men");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>运动服饰>基础内衣");put("t_sizeType","Men");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>运动服饰>基础内衣>上装");put("t_sizeType","Men");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>运动服饰>基础内衣>下装");put("t_sizeType","Men");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>运动服饰>基础内衣>套装");put("t_sizeType","Men");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>运动服饰>夹克衫");put("t_sizeType","Men");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>运动服饰>打底裤");put("t_sizeType","Men");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>运动服饰>毛衣");put("t_sizeType","Men");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>运动服饰>短裤");put("t_sizeType","Men");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>运动服饰>短裤>徒步短裤");put("t_sizeType","Men");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>运动服饰>短裤>攀岩短裤");put("t_sizeType","Men");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>运动服饰>短裤>瑜伽短裤");put("t_sizeType","Men");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>运动服饰>短裤>篮球短裤");put("t_sizeType","Men");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>运动服饰>短裤>网球短裤");put("t_sizeType","Men");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>运动服饰>短裤>足球短裤");put("t_sizeType","Men");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>运动服饰>短裤>跑步短裤");put("t_sizeType","Men");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>运动服饰>短裤>运动健身短裤");put("t_sizeType","Men");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>运动服饰>短裤>骑行裤短裤");put("t_sizeType","Men");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>运动服饰>短裤>高尔夫短裤");put("t_sizeType","Men");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>运动服饰>背心马甲");put("t_sizeType","Men");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>运动服饰>衬衫");put("t_sizeType","Men");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>运动服饰>衬衫>篮球衫");put("t_sizeType","Men");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>运动服饰>衬衫>足球服");put("t_sizeType","Men");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>运动服饰>衬衫>运动健身服");put("t_sizeType","Men");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>运动服饰>衬衫>运动衫");put("t_sizeType","Men");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>运动服饰>衬衫>骑行服");put("t_sizeType","Men");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>运动服饰>衬衫>高尔夫球服");put("t_sizeType","Men");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>运动服饰>运动夹克衫");put("t_sizeType","Men");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>运动服饰>运动服套装");put("t_sizeType","Men");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>运动服饰>运动服饰套装");put("t_sizeType","Men");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>运动服饰>运动服饰套装>运动服饰套装");put("t_sizeType","Men");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>运动服饰>运动服饰套装>运动衫");put("t_sizeType","Men");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>运动服饰>运动服饰套装>运动裤");put("t_sizeType","Men");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>运动服饰>长裤");put("t_sizeType","Men");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>运动服饰>长裤>健身运动裤装");put("t_sizeType","Men");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>运动服饰>长裤>攀岩运动裤");put("t_sizeType","Men");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>运动服饰>长裤>田径运动裤");put("t_sizeType","Men");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>运动服饰>长裤>运动裤");put("t_sizeType","Men");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>运动服饰>长裤>骑行裤装");put("t_sizeType","Men");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>运动迷衍生服饰");put("t_sizeType","Men");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>运动迷衍生服饰>T恤");put("t_sizeType","Men");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>运动迷衍生服饰>内衣");put("t_sizeType","Men");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>运动迷衍生服饰>夹克衫");put("t_sizeType","Men");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>运动迷衍生服饰>运动衫");put("t_sizeType","Men");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>防寒服饰配件");put("t_sizeType","Men");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>防寒服饰配件>围巾披肩");put("t_sizeType","Men");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>防寒服饰配件>围脖");put("t_sizeType","Men");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>防寒服饰配件>帽子");put("t_sizeType","Men");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>防寒服饰配件>手套");put("t_sizeType","Men");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>防寒服饰配件>无指手套");put("t_sizeType","Men");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>防寒服饰配件>无指手套及手闷子");put("t_sizeType","Men");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>防寒服饰配件>耳罩");put("t_sizeType","Men");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>防寒服饰配件>袖筒");put("t_sizeType","Men");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>男装>防寒服饰配件>防寒服饰配件套装");put("t_sizeType","Men");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>青少年服饰");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>青少年服饰>T恤");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>青少年服饰>半身裙");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>青少年服饰>外套夹克");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>青少年服饰>帽衫家居服");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>青少年服饰>帽衫家居服>帽衫");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>青少年服饰>帽衫家居服>长裤");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>青少年服饰>毛衣");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>青少年服饰>泳装");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>青少年服饰>泳装>分体式泳装");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>青少年服饰>泳装>比基尼泳装");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>青少年服饰>泳装>比赛训练泳装");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>青少年服饰>泳装>沙滩裤");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>青少年服饰>泳装>泳裙");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>青少年服饰>泳装>筒式泳装");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>青少年服饰>泳装>连体式泳装");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>青少年服饰>泳装>防晒衣");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>青少年服饰>牛仔裤");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>青少年服饰>睡衣睡袍");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>青少年服饰>睡衣睡袍>睡衣");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>青少年服饰>睡衣睡袍>睡衣>睡衣套装");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>青少年服饰>睡衣睡袍>睡衣>睡裤");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>青少年服饰>睡衣睡袍>睡袍");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>青少年服饰>短裤");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>青少年服饰>背心马甲");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>青少年服饰>衬衫Polo衫");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>青少年服饰>衬衫Polo衫>Polo衫");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>青少年服饰>衬衫Polo衫>衬衫");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>青少年服饰>西装");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>青少年服饰>运动服饰");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>青少年服饰>运动服饰>上装");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>青少年服饰>运动服饰>短裤");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>青少年服饰>运动服饰>裙装裙裤");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>青少年服饰>运动服饰>运动服饰套装");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>青少年服饰>连衣裙");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>青少年服饰>连衣裙>太阳裙");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>青少年服饰>连衣裙>娃娃裙");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>青少年服饰>连衣裙>舞会礼服");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>青少年服饰>连衣裙>连衫裙");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>青少年服饰>长裤");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>青少年服饰>长裤>七分裤");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>青少年服饰>长裤>休闲裤");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>青年装");put("t_sizeType","Men");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>青年装>T恤");put("t_sizeType","Men");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>青年装>内衣");put("t_sizeType","Men");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>青年装>外套夹克");put("t_sizeType","Men");put("t_category","男装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>青年装>毛衣");put("t_sizeType","Men");put("t_category","男装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>青年装>泳装");put("t_sizeType","Men");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>青年装>泳装>三角裤");put("t_sizeType","Men");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>青年装>泳装>比赛训练泳装");put("t_sizeType","Men");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>青年装>泳装>沙滩裤");put("t_sizeType","Men");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>青年装>泳装>游泳裤");put("t_sizeType","Men");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>青年装>泳装>防晒衣");put("t_sizeType","Men");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>青年装>牛仔裤");put("t_sizeType","Men");put("t_category","男装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>青年装>短裤");put("t_sizeType","Men");put("t_category","男装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>青年装>背心马甲");put("t_sizeType","Men");put("t_category","女士内衣/男士内衣/家居服");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>青年装>衬衫Polo衫");put("t_sizeType","Men");put("t_category","男装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>青年装>衬衫Polo衫>Polo衫");put("t_sizeType","Men");put("t_category","男装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>青年装>衬衫Polo衫>扣角领牛津衬衫");put("t_sizeType","Men");put("t_category","男装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>青年装>袜子");put("t_sizeType","Men");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>青年装>运动夹克");put("t_sizeType","Men");put("t_category","男装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>青年装>运动服饰");put("t_sizeType","Men");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>青年装>运动服饰>短裤");put("t_sizeType","Men");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>青年装>运动服饰>衬衫");put("t_sizeType","Men");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>青年装>运动服饰>运动服饰套装");put("t_sizeType","Men");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>青年装>运动服饰>长裤");put("t_sizeType","Men");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>青年装>运动迷衍生服饰");put("t_sizeType","Men");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","服饰>青年装>长裤");put("t_sizeType","Men");put("t_category","男装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>休闲运动与游戏");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>休闲运动与游戏>保龄球装备");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>休闲运动与游戏>保龄球装备>保龄球");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>休闲运动与游戏>保龄球装备>保龄球包");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>休闲运动与游戏>保龄球装备>保龄球包>保龄球手提包");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>休闲运动与游戏>保龄球装备>保龄球包>保龄球袋");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>休闲运动与游戏>保龄球装备>保龄球套装");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>休闲运动与游戏>保龄球装备>保龄球手套");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>休闲运动与游戏>保龄球装备>保龄球抛光设备");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>休闲运动与游戏>保龄球装备>保龄球护具用品");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>休闲运动与游戏>保龄球装备>保龄球球柱");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>休闲运动与游戏>保龄球装备>保龄球运动服饰");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>休闲运动与游戏>保龄球装备>保龄球运动服饰>保龄球运动夹克衫");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>休闲运动与游戏>保龄球装备>保龄球运动服饰>保龄球运动衬衫");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>休闲运动与游戏>保龄球装备>保龄球配件");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>休闲运动与游戏>保龄球装备>保龄球配件>保龄球指孔贴");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>休闲运动与游戏>保龄球装备>保龄球配件>保龄球训练辅助器材");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>休闲运动与游戏>保龄球装备>保龄球配件>保龄球鞋套");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>休闲运动与游戏>保龄球装备>保龄球鞋");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>休闲运动与游戏>保龄球装备>保龄球鞋>女士保龄球");put("t_sizeType","Women");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>休闲运动与游戏>保龄球装备>保龄球鞋>男士保龄球");put("t_sizeType","Men");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>休闲运动与游戏>游戏厅>乒乓球");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>休闲运动与游戏>游戏厅>乒乓球>乒乓球");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>休闲运动与游戏>游戏厅>乒乓球>乒乓球套装");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>休闲运动与游戏>游戏厅>乒乓球>投手板");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>休闲运动与游戏>游戏厅>乒乓球>桌子");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>休闲运动与游戏>游戏厅>乒乓球>球拍");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>休闲运动与游戏>游戏厅>乒乓球>球网球柱");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>休闲运动与游戏>游戏厅>乒乓球>装备包");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>休闲运动与游戏>游戏厅>桌球台球");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>休闲运动与游戏>游戏厅>桌球台球>台布");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>休闲运动与游戏>游戏厅>桌球台球>台球口袋");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>休闲运动与游戏>游戏厅>桌球台球>台球杆头");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>休闲运动与游戏>游戏厅>桌球台球>杆筒");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>休闲运动与游戏>游戏厅>桌球台球>架杆器");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>休闲运动与游戏>游戏厅>桌球台球>架杆头");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>休闲运动与游戏>游戏厅>桌球台球>桌球");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>休闲运动与游戏>游戏厅>桌球台球>桌球布");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>休闲运动与游戏>游戏厅>桌球台球>桌球杆");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>休闲运动与游戏>游戏厅>桌球台球>滑石粉");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>休闲运动与游戏>游戏厅>桌球台球>球杆架");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>休闲运动与游戏>游戏厅>桌球台球>球杆滑石粉");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>休闲运动与游戏>游戏厅>桌球台球>球架");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>休闲运动与游戏>游戏厅>桌球台球>球桌");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>休闲运动与游戏>游戏厅>桌面游戏");put("t_category","玩具/童车/益智/积木/模型");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>休闲运动与游戏>游戏厅>桌面游戏>台球桌");put("t_category","玩具/童车/益智/积木/模型");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>休闲运动与游戏>游戏厅>桌面游戏>家用滚球槽");put("t_category","玩具/童车/益智/积木/模型");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>休闲运动与游戏>游戏厅>桌面游戏>桌上冰球");put("t_category","玩具/童车/益智/积木/模型");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>休闲运动与游戏>游戏厅>桌面游戏>桌上足球");put("t_category","玩具/童车/益智/积木/模型");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>休闲运动与游戏>游戏厅>桌面游戏>沙壶球");put("t_category","玩具/童车/益智/积木/模型");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>休闲运动与游戏>游戏厅>桌面游戏>滚球");put("t_category","玩具/童车/益智/积木/模型");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>休闲运动与游戏>游戏厅>桌面游戏>电子篮球");put("t_category","玩具/童车/益智/积木/模型");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>休闲运动与游戏>游戏厅>桌面游戏>组合桌");put("t_category","玩具/童车/益智/积木/模型");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>休闲运动与游戏>游戏厅>飞镖与圆靶>镖叶");put("t_category","玩具/童车/益智/积木/模型");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>休闲运动与游戏>游戏厅>飞镖与圆靶>镖尾>V翼镖尾");put("t_category","玩具/童车/益智/积木/模型");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>休闲运动与游戏>游戏厅>飞镖与圆靶>镖尾>标准镖尾");put("t_category","玩具/童车/益智/积木/模型");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>休闲运动与游戏>游戏厅>飞镖与圆靶>镖尾>梨形镖尾");put("t_category","玩具/童车/益智/积木/模型");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>休闲运动与游戏>游戏厅>飞镖与圆靶>镖尾>纤细镖尾");put("t_category","玩具/童车/益智/积木/模型");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>休闲运动与游戏>游戏厅>飞镖与圆靶>飞镖");put("t_category","玩具/童车/益智/积木/模型");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>休闲运动与游戏>游戏厅>飞镖与圆靶>飞镖尖");put("t_category","玩具/童车/益智/积木/模型");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>休闲运动与游戏>游戏厅>飞镖与圆靶>飞镖杆");put("t_category","玩具/童车/益智/积木/模型");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>休闲运动与游戏>飞盘运动");put("t_category","玩具/童车/益智/积木/模型");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>休闲运动与游戏>飞盘运动>终极飞盘");put("t_category","玩具/童车/益智/积木/模型");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>休闲运动与游戏>飞盘运动>终极飞盘>入门套件");put("t_category","玩具/童车/益智/积木/模型");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>休闲运动与游戏>飞盘运动>终极飞盘>飞盘");put("t_category","玩具/童车/益智/积木/模型");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>休闲运动与游戏>飞盘运动>飞盘");put("t_category","玩具/童车/益智/积木/模型");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>休闲运动与游戏>飞盘运动>飞盘高尔夫");put("t_category","玩具/童车/益智/积木/模型");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>休闲运动与游戏>飞盘运动>飞盘高尔夫>飞盘袋");put("t_category","玩具/童车/益智/积木/模型");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>休闲运动与游戏>飞盘运动>飞盘高尔夫>飞盘高尔夫篮");put("t_category","玩具/童车/益智/积木/模型");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>休闲运动与游戏>飞盘运动>飞盘高尔夫>高尔夫飞盘");put("t_category","玩具/童车/益智/积木/模型");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>休闲运动与游戏>飞盘运动>飞盘高尔夫>高尔夫飞盘>中距离飞盘");put("t_category","玩具/童车/益智/积木/模型");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>休闲运动与游戏>飞盘运动>飞盘高尔夫>高尔夫飞盘>开盘");put("t_category","玩具/童车/益智/积木/模型");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>休闲运动与游戏>飞盘运动>飞盘高尔夫>高尔夫飞盘>推进盘");put("t_category","玩具/童车/益智/积木/模型");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>体操装备");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>体操装备>体操专用地板");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>体操装备>体操专用垫");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>体操装备>体操训练工具");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>体操装备>垫子");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>体操装备>垫子>体操垫");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>体操装备>垫子>训练垫");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>体操装备>垫子>运动垫");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>体操装备>握力器");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>体操装备>比赛装备");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>体操装备>比赛装备>单杠");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>体操装备>比赛装备>双杠");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>体操装备>比赛装备>吊环");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>体操装备>比赛装备>平衡木");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>体操装备>比赛装备>鞍马");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>体操装备>比赛装备>高低杠");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>体操装备>滑石粉");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>体操装备>装备袋");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>体操装备>降落伞");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>冰壶运动装备");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>冰壶运动装备>冰壶用刷");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>冰壶运动装备>冰壶石");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>冰壶运动装备>冰壶运动手套");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>击剑");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>击剑>击剑护具");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>击剑>击剑护具>击剑手套");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>击剑>击剑护具>夹克衫");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>击剑>击剑护具>护胸");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>击剑>击剑护具>胸铠");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>击剑>击剑护具>裤子");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>击剑>击剑护具>防护用具");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>击剑>击剑护具>面罩");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>击剑>击剑用具");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>击剑>武器");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>击剑>武器>军刀");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>击剑>武器>军刀>完整武器");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>击剑>武器>军刀>零部件");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>击剑>武器>重剑");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>击剑>武器>重剑>完整武器");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>击剑>武器>重剑>零部件");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>击剑>武器>钝头剑");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>击剑>武器>钝头剑>完整武器");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>击剑>武器>钝头剑>零部件");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>帆板运动装备");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>帆板运动装备>安全带");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>帆板运动装备>帆板");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>帆板运动装备>桅杆");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>帆板运动装备>车载冲浪板架");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>帆板运动装备>风帆");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>帆板运动装备>鳍板");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>手球装备");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>手球装备>手球");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>手球装备>手球手套");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>拳击运动装备>吊袋");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>拳击运动装备>吊袋>沙袋");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>拳击运动装备>吊袋>负重沙袋");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>拳击运动装备>吊袋配件");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>拳击运动装备>吊袋配件>吊袋架");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>拳击运动装备>吊袋配件>沙袋固定装置");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>拳击运动装备>吊袋配件>沙袋支架");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>拳击运动装备>吊袋配件>沙袋放置台");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>拳击运动装备>拳击手套");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>拳击运动装备>拳击手套>专业运动员手套");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>拳击运动装备>拳击手套>业余运动员手套");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>拳击运动装备>拳击手套>沙包手套");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>拳击运动装备>拳击手套>训练手套");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>拳击运动装备>拳击护具");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>拳击运动装备>拳击护具>护头头套");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>拳击运动装备>拳击护具>护手用具");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>拳击运动装备>拳击护具>护牙托");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>拳击运动装备>拳击护具>护盾");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>拳击运动装备>拳击护具>护胸护腰");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>拳击运动装备>拳击护具>护脚用具");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>拳击运动装备>拳击护具>护腿");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>拳击运动装备>拳击护具>护臂");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>拳击运动装备>拳击护具>防护用具");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>拳击运动装备>拳击运动服饰");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>拳击运动装备>拳击运动服饰>拳击战袍");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>拳击运动装备>拳击运动服饰>拳击运动球衣");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>拳击运动装备>拳击运动服饰>运动短裤");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>拳击运动装备>拳击运动装备");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>拳击运动装备>拳击防护垫");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>旱冰冰球");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>旱冰冰球>冰刀");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>旱冰冰球>冰球");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>旱冰冰球>溜冰鞋");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>旱冰冰球>球网");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>板球运动装备");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>板球运动装备>板球");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>板球运动装备>板球三柱门");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>板球运动装备>板球三柱门套");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>板球运动装备>板球头盔");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>板球运动装备>板球套装");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>板球运动装备>板球护具用品");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>板球运动装备>板球护具用品>板球守门员护垫");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>板球运动装备>板球护具用品>板球球拍保护垫");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>板球运动装备>板球拍");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>板球运动装备>板球球拍手套");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>板球运动装备>板球装备包");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>板网球装备");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>板网球装备>板网球手套");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>板网球装备>板网球球拍");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>板网球装备>板网球用球");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>桨球装备");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>桨网球装备");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>武术装备");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>武术装备>护具用品");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>武术装备>护具用品>护头头套");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>武术装备>护具用品>护手用具");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>武术装备>护具用品>护盾");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>武术装备>护具用品>护胸护腰");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>武术装备>护具用品>护脚用具");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>武术装备>护具用品>护腹带");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>武术装备>护具用品>护腿");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>武术装备>护具用品>护臂");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>武术装备>护具用品>护齿牙托");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>武术装备>武器固定架");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>武术装备>武器箱");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>武术装备>武器装备");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>武术装备>武器装备>三股叉");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>武术装备>武器装备>三节棍");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>武术装备>武器装备>刀具");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>武术装备>武器装备>剑");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>武术装备>武器装备>忍者武器");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>武术装备>武器装备>木刀");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>武术装备>武器装备>木棍");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>武术装备>武器装备>棍棒");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>武术装备>武器装备>武术棍");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>武术装备>武器装备>武术用刀");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>武术装备>武器装备>滚珠轴承双节棍");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>武术装备>武器装备>绳索双节棍");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>武术装备>武器装备>警棍");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>武术装备>武器装备>训练剑");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>武术装备>武器装备>链条");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>武术装备>武术服");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>武术装备>武术装备袋");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>武术装备>武术训练工具");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>武术装备>武术训练工具>武术手套");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>武术装备>武术训练工具>武术训练棍");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>武术装备>武术训练工具>武术防护垫");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>武术装备>武术训练工具>训练吊袋");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>武术装备>武术训练工具>训练吊袋>沙袋");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>武术装备>武术训练工具>训练吊袋>负重沙袋");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>武术装备>武术训练工具>训练手套");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>武术装备>武术训练工具>训练沙包拳套");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>武术装备>武术训练工具>训练脚靶");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>武术装备>武术训练工具>靶子");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>武术装备>腰带");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>武术装备>腰带扣");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>狗雪橇装备");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>狗雪橇装备>安全带");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>狗雪橇装备>雪橇");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>狗雪橇装备>雪橇绳");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>竞技表演");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>舞蹈用品");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>舞蹈用品>舞蹈鞋");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>舞蹈用品>舞蹈鞋>女士舞蹈鞋");put("t_sizeType","Women");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>舞蹈用品>舞蹈鞋>男士舞蹈鞋");put("t_sizeType","Men");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>舞蹈用品>芭蕾舞用品");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>舞蹈用品>跳舞专用地板");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>舞蹈用品>踢踏舞用品");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>赛艇运动装备");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>赛艇运动装备>护壳罩");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>赛艇运动装备>护壳零件");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>赛艇运动装备>索具");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>赛艇运动装备>船桨");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>赛艇运动装备>船桨零件");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>赛艇运动装备>船艇护壳");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>跳伞运动");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>雪地摩托运动装备");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>雪地摩托运动装备>修理工具");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>雪地摩托运动装备>升降机");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>雪地摩托运动装备>垫板");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>雪地摩托运动装备>拖车配件");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>雪地摩托运动装备>推车套装");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>雪地摩托运动装备>滑雪护具");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>雪地摩托运动装备>牵引");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>雪地摩托运动装备>袋子支架");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>雪地摩托运动装备>雪地摩托车");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>雪地摩托运动装备>雪地摩托车罩");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>雪地摩托运动装备>雪橇滑梯");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>雪地摩托运动装备>风挡");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>马术运动装备>护具用品");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>马术运动装备>鞍辔马具 ");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>马术运动装备>鞍辔马具 >缰绳");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>马术运动装备>鞍辔马具 >肚带");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>马术运动装备>鞍辔马具 >胸铠马颔缰");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>马术运动装备>鞍辔马具 >鞍座配件");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>马术运动装备>鞍辔马具 >鞍辔马具箱");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>马术运动装备>鞍辔马具 >鞍辔马具配件");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>马术运动装备>鞍辔马具 >马衔");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>马术运动装备>鞍辔马具 >马镫");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>马术运动装备>鞍辔马具 >马鞍");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>马术运动装备>鞍辔马具 >马鞍垫");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>马术运动装备>鞭子鞭把");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>马术运动装备>马匹保健装备");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>马术运动装备>马匹保健装备>修剪工具");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>马术运动装备>马匹保健装备>护蹄");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>马术运动装备>马匹保健装备>擦剂");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>马术运动装备>马匹保健装备>毛发护理用品");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>马术运动装备>马匹保健装备>美容用品");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>马术运动装备>马匹保健装备>营养补充品及药物");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>马术运动装备>马匹保健装备>马毯床单");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>马术运动装备>马匹保健装备>马用鞋靴");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>马术运动装备>马匹保健装备>驱虫用品");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>马术运动装备>马匹保健装备>驱虫药");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>马术运动装备>马术专用拖车");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>马术运动装备>马术手套");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>马术运动装备>马术服");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>马术运动装备>马术服>马术衬衫");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>马术运动装备>马术服>马裤");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>马术运动装备>马术运动头盔");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>马术运动装备>马术运动靴子");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>马术运动装备>骑马装备");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>马球");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>马球>护具用品");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>马球>护具用品>头盔");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>马球>护具用品>护膝");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>马球>护具用品>马球手套");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>马球>肚带");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>马球>胸铠马颔缰");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>马球>马刺");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>马球>马球");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>马球>马球杆");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>马球>马鞍");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>马球>马鞍垫");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>马球>马鞍配件");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>其他运动装备>马球>马鞭");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>冰雪运动装备");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>冰雪运动装备>单板滑雪");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>冰雪运动装备>单板滑雪>其他滑雪装备");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>冰雪运动装备>单板滑雪>单板");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>冰雪运动装备>单板滑雪>单板>自由滑行滑雪板");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>冰雪运动装备>单板滑雪>单板滑雪头盔");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>冰雪运动装备>单板滑雪>单板滑雪服");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>冰雪运动装备>单板滑雪>单板滑雪服>单板滑雪夹克衫");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>冰雪运动装备>单板滑雪>单板滑雪服>单板滑雪袜子");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>冰雪运动装备>单板滑雪>单板滑雪服>单板滑雪裤子");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>冰雪运动装备>单板滑雪>单板滑雪靴子");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>冰雪运动装备>单板滑雪>单板滑雪靴子>女士滑雪靴");put("t_sizeType","Women");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>冰雪运动装备>单板滑雪>单板滑雪靴子>男士滑雪靴");put("t_sizeType","Men");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>冰雪运动装备>单板滑雪>单板滑雪靴袋子");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>冰雪运动装备>单板滑雪>固定器");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>冰雪运动装备>单板滑雪>室内滑雪板储物柜");put("t_category","住宅家具");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>冰雪运动装备>单板滑雪>护目镜");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>冰雪运动装备>单板滑雪>滑板包");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>冰雪运动装备>单板滑雪>滑雪垫");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>冰雪运动装备>单板滑雪>滑雪板架");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>冰雪运动装备>滑雪橇");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>冰雪运动装备>滑雪橇>平底雪橇");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>冰雪运动装备>滑雪橇>滑雪管");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>冰雪运动装备>滑雪橇>雪橇");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>冰雪运动装备>滑雪装备");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>冰雪运动装备>滑雪装备>其他滑雪装备");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>冰雪运动装备>滑雪装备>室内滑雪板储物柜");put("t_category","住宅家具");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>冰雪运动装备>滑雪装备>护目镜");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>冰雪运动装备>滑雪装备>滑雪头盔");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>冰雪运动装备>滑雪装备>滑雪服饰");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>冰雪运动装备>滑雪装备>滑雪服饰>滑雪夹克衫");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>冰雪运动装备>滑雪装备>滑雪服饰>滑雪脖套");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>冰雪运动装备>滑雪装备>滑雪服饰>滑雪袜");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>冰雪运动装备>滑雪装备>滑雪服饰>滑雪裤");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>冰雪运动装备>滑雪装备>滑雪板架");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>冰雪运动装备>滑雪装备>滑雪蜡");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>冰雪运动装备>滑雪装备>滑雪装备包");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>冰雪运动装备>滑雪装备>滑雪靴袋子");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>冰雪运动装备>滑雪装备>特里马滑雪");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>冰雪运动装备>滑雪装备>特里马滑雪>固定器");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>冰雪运动装备>滑雪装备>特里马滑雪>滑雪板");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>冰雪运动装备>滑雪装备>特里马滑雪>滑雪靴");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>冰雪运动装备>滑雪装备>越野滑雪");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>冰雪运动装备>滑雪装备>越野滑雪>固定器");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>冰雪运动装备>滑雪装备>越野滑雪>滑雪板");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>冰雪运动装备>滑雪装备>越野滑雪>滑雪靴");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>冰雪运动装备>滑雪装备>越野滑雪>雪仗");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>冰雪运动装备>滑雪装备>野外滑雪装备");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>冰雪运动装备>滑雪装备>野外滑雪装备>信号浮标收发器");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>冰雪运动装备>滑雪装备>野外滑雪装备>雪地探测器");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>冰雪运动装备>滑雪装备>野外滑雪装备>雪铲");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>冰雪运动装备>滑雪装备>高山滑雪");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>冰雪运动装备>滑雪装备>高山滑雪>固定器");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>冰雪运动装备>滑雪装备>高山滑雪>滑雪板");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>冰雪运动装备>滑雪装备>高山滑雪>滑雪靴");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>冰雪运动装备>滑雪装备>高山滑雪>滑雪靴>女士滑雪靴");put("t_sizeType","Women");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>冰雪运动装备>滑雪装备>高山滑雪>滑雪靴>男士滑雪靴");put("t_sizeType","Men");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>冰雪运动装备>滑雪装备>高山滑雪>雪仗");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>冰雪运动装备>雪地鞋");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>冰雪运动装备>雪地鞋>固定器");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>冰雪运动装备>雪地鞋>雪仗");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>冰雪运动装备>雪地鞋>雪地靴子");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>冰雪运动装备>雪地鞋>雪地鞋");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>冰球装备");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>冰球装备>冰球");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>冰球装备>冰球头盔");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>冰球装备>冰球服");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>冰球装备>冰球棍");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>冰球装备>冰球装备配件");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>冰球装备>冰球装备配件>冰刀");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>冰球装备>冰球装备配件>冰球");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>冰球装备>冰球装备配件>冰球包");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>冰球装备>冰球装备配件>冰球杆");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>冰球装备>冰球装备配件>握把套缠带");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>冰球装备>冰球装备配件>旱冰球冰球");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>冰球装备>冰球装备配件>溜冰配件");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>冰球装备>冰球装备配件>球网");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>冰球装备>冰球装备配件>球门");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>冰球装备>冰球装备配件>训练辅助器材");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>冰球装备>冰鞋");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>冰球装备>守门员装备");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>冰球装备>守门员装备>守门员冰鞋");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>冰球装备>守门员装备>守门员护具");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>冰球装备>守门员装备>守门员捕捉器");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>冰球装备>守门员装备>守门员球棍");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>冰球装备>守门员装备>守门员球棒");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>冰球装备>守门员装备>守门员面罩");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>冰球装备>护具用品");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>冰球装备>护具用品>头盔");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>冰球装备>护具用品>头盔面罩");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>冰球装备>护具用品>护肘");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>冰球装备>护具用品>护肩");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>冰球装备>护具用品>护腿");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>冰球装备>护具用品>护腿垫");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>冰球装备>护具用品>护颈");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>冰球装备>护具用品>运动员手套");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>冰球装备>护具用品>面罩护盾");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>冰球装备>握把套缠带");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>冰球装备>溜冰鞋");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>冰球装备>滑冰配件");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>冰球装备>球杆");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>冰球装备>球棍");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>冰球装备>球门");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>冰球装备>训练装备");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>冰球装备>设备袋");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>垒球");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>垒球>垒球");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>垒球>垒球>快速间距垒球");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>垒球>垒球>慢速间距垒球");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>垒球>垒球服");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>垒球>垒球服>垒球球衣");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>垒球>垒球服>垒球短裤");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>垒球>垒球服>垒球袜子");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>垒球>垒球鞋");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>垒球>垒球鞋>女士垒球鞋");put("t_sizeType","Women");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>垒球>垒球鞋>男士垒球鞋");put("t_sizeType","Men");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>垒球>手套及连指手套");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>垒球>手套及连指手套>一垒手手套");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>垒球>手套及连指手套>内场手手套");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>垒球>手套及连指手套>外场手手套");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>垒球>手套及连指手套>接球手手套");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>垒球>护具用品");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>垒球>护具用品>击球员头盔");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>垒球>护具用品>护膝");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>垒球>护具用品>接球手头盔");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>垒球>护具用品>接球手护胸");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>垒球>护具用品>接球手护腿");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>垒球>护具用品>接球手面罩");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>垒球>护具用品>滑垒垫");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>垒球>护具用品>脸部护具");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>垒球>护具用品>裁判员护具用品");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>垒球>球棒");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>垒球>球棒>快速间距垒球棒");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>垒球>球棒>慢速间距垒球棒");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>垒球>球棒手套");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>垒球>训练装备");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>垒球>训练装备>弹力网");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>垒球>训练装备>投球机器");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>垒球>训练装备>练习网");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>垒球>设备袋");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>垒球>设备袋>球棒袋");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>垒球>设备袋>球袋");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>垒球>设备袋>装备袋");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>垒球运动>垒球服饰");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>垒球运动>垒球鞋>女士垒球鞋");put("t_sizeType","Women");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>垒球运动>垒球鞋>男士垒球鞋");put("t_sizeType","Men");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>拉拉队");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>拉拉队>手摇花");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>拉拉队>拉拉队制服");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>拉拉队>拉拉队服");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>拉拉队>话筒");put("t_category","影音电器");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>拉拉队>迷你扩音器");put("t_category","影音电器");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>排球");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>排球>制服");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>排球>室内排球");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>排球>室内排球网");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>排球>户外排球");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>排球>户外排球网");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>排球>护具用品");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>排球>护具用品>护膝");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>排球>护具用品>护踝");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>排球>护具用品>短裤");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>排球>排球");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>排球>排球>室内排球");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>排球>排球>室外排球");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>排球>排球服");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>排球>排球服>排球球衣");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>排球>排球服>排球短裤");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>排球>排球服>排球袜子");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>排球>排球装备");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>排球>排球配件");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>排球>排球配件>其他排球配件");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>排球>排球配件>排球推车");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>排球>排球配件>排球柱护套");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>排球>排球配件>排球网线");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>排球>排球配件>排球袋");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>排球>排球配件>界标");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>排球>排球配件>训练器材");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>排球>排球配件>铝撑竿套装");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>排球>排球鞋");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>排球>排球鞋>女士排球鞋");put("t_sizeType","Women");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>排球>排球鞋>男士排球鞋");put("t_sizeType","Men");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>排球>推球车");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>排球>球网");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>排球>网柱套装");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>排球>训练装备");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>排球>设备袋");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>摔跤运动装备");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>摔跤运动装备>吊带摔跤服");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>摔跤运动装备>护具用品");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>摔跤运动装备>护耳装备");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>摔跤运动装备>护膝");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>摔跤运动装备>摔跤垫");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>摔跤运动装备>摔跤帽子");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>摔跤运动装备>摔跤鞋");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>摔跤运动装备>摔跤鞋>男士摔跤鞋");put("t_sizeType","Men");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>摔跤运动装备>背心吊带");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>曲棍球");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>曲棍球>护具用品");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>曲棍球>护具用品>护目镜");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>曲棍球>曲棍球");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>曲棍球>曲棍球手套");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>曲棍球>球棍");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>曲棍球>球门");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>曲棍球>训练装备");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>曲棍球>设备袋");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>棒球装备");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>棒球装备>棒球");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>棒球装备>棒球击球练习挡网");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>棒球装备>棒球制服");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>棒球装备>棒球发球机设备");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>棒球装备>棒球发球机设备>棒球发球机用T形棒球棒");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>棒球装备>棒球发球机设备>棒球发球机用塑料球");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>棒球装备>棒球发球机设备>棒球发球机用塑料球棒");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>棒球装备>棒球场地维护设备");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>棒球装备>棒球手套");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>棒球装备>棒球手套及连指手套");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>棒球装备>棒球手套及连指手套>棒球一垒手手套");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>棒球装备>棒球手套及连指手套>棒球内场手手套");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>棒球装备>棒球手套及连指手套>棒球外场手手套");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>棒球装备>棒球手套及连指手套>棒球接球手手套");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>棒球装备>棒球手套配件");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>棒球装备>棒球护具用品");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>棒球装备>棒球护具用品>棒球头盔");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>棒球装备>棒球护具用品>棒球护膝");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>棒球装备>棒球护具用品>棒球接球手头盔");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>棒球装备>棒球护具用品>棒球接球手护胸");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>棒球装备>棒球护具用品>棒球接球手护腿");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>棒球装备>棒球护具用品>棒球接球手面罩");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>棒球装备>棒球护具用品>棒球滑垒垫");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>棒球装备>棒球护具用品>棒球脸部护具");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>棒球装备>棒球护具用品>棒球裁判员护具用品");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>棒球装备>棒球服饰");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>棒球装备>棒球服饰>棒球球衣");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>棒球装备>棒球服饰>棒球球袜");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>棒球装备>棒球服饰>棒球短裤");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>棒球装备>棒球棍配件");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>棒球装备>棒球棍配件>棒球加重环");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>棒球装备>棒球棍配件>棒球握把套缠带");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>棒球装备>棒球棍配件>棒球松焦油");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>棒球装备>棒球棍配件>棒球棒套筒");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>棒球装备>棒球棍配件>棒球棒架");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>棒球装备>棒球球场装备");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>棒球装备>棒球球场装备>棒球场地装备");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>棒球装备>棒球球场装备>棒球打击训练器");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>棒球装备>棒球球场装备>棒球挡网");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>棒球装备>棒球球场装备>棒球本垒板");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>棒球装备>棒球球场装备>棒球练习网");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>棒球装备>棒球装备包");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>棒球装备>棒球装备包>棒球包");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>棒球装备>棒球装备包>棒球球拍包");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>棒球装备>棒球装备包>棒球装备包");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>棒球装备>棒球训练工具");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>棒球装备>棒球训练工具>击球训练机");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>棒球装备>棒球训练工具>发球机");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>棒球装备>棒球训练工具>发球训练器");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>棒球装备>棒球训练工具>弹力网");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>棒球装备>棒球训练工具>球棒");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>棒球装备>棒球训练工具>训练网");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>棒球装备>棒球鞋");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>棒球装备>棒球鞋>女士棒球鞋");put("t_sizeType","Women");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>棒球装备>棒球鞋>男士棒球鞋");put("t_sizeType","Men");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>橄榄球运动装备");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>橄榄球运动装备>护具用品");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>橄榄球运动装备>护具用品>头盔");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>橄榄球运动装备>护具用品>弹力运动紧身衣");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>橄榄球运动装备>护具用品>护具");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>橄榄球运动装备>护具用品>护肩");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>橄榄球运动装备>橄榄球");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>橄榄球运动装备>橄榄球>微型球");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>橄榄球运动装备>橄榄球>比赛橄榄球");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>橄榄球运动装备>橄榄球>训练球");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>橄榄球运动装备>橄榄球头盔");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>橄榄球运动装备>橄榄球服");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>橄榄球运动装备>橄榄球服>橄榄球夹克衫");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>橄榄球运动装备>橄榄球服>橄榄球球衣");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>橄榄球运动装备>橄榄球服>橄榄球短裤");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>橄榄球运动装备>橄榄球服>橄榄球袜子");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>橄榄球运动装备>橄榄球运动装备配件");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>橄榄球运动装备>设备袋");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>水球装备");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>水球装备>水球");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>水球装备>水球帽");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>水球装备>球门");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>田径运动");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>田径运动>会场装备");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>田径运动>会场装备>Pit Rakes");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>田径运动>会场装备>号码牌");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>田径运动>会场装备>接力棒");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>田径运动>会场装备>标示旗");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>田径运动>会场装备>计圈器");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>田径运动>会场装备>记分器");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>田径运动>会场装备>运动场地桩 ");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>田径运动>会场装备>风速计");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>田径运动>投掷装备");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>田径运动>投掷装备>健身实心球");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>田径运动>投掷装备>投掷圈");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>田径运动>投掷装备>抵趾板");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>田径运动>投掷装备>标枪");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>田径运动>投掷装备>标枪训练球");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>田径运动>投掷装备>铁饼");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>田径运动>投掷装备>铁饼护笼");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>田径运动>投掷装备>铅球");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>田径运动>投掷装备>链球铅球");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>田径运动>推车装备");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>田径运动>测量计时用具");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>田径运动>测量计时用具>画线装置");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>田径运动>测量计时用具>计时器");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>田径运动>测量计时用具>计时工具");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>田径运动>测量计时用具>跳高丈量尺");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>田径运动>田径场地辅助用具");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>田径运动>田径场地辅助用具>助跳板");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>田径运动>田径场地辅助用具>起跑器");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>田径运动>跑道用品");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>田径运动>跑道用品>发令员护耳器");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>田径运动>跑道用品>发令枪");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>田径运动>跑道用品>发令枪子弹");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>田径运动>跑道用品>发令枪枪套");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>田径运动>跑道用品>发令枪清洁用品");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>田径运动>跑道用品>起跑器");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>田径运动>跑道用品>跨栏架");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>田径运动>跑道用品>跨栏训练垫");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>田径运动>跳跃装备");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>田径运动>跳跃装备>撑杆");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>田径运动>跳跃装备>撑杆立柱");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>田径运动>跳跃装备>撑杆跳基础保护用具");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>田径运动>跳跃装备>横杆");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>田径运动>跳跃装备>沙坑");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>田径运动>跳跃装备>着地垫");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>田径运动>跳跃装备>着地垫配件");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>田径运动>跳跃装备>跳高立柱");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>篮球");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>篮球>球场配件");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>篮球>篮板");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>篮球>篮板>便携式篮板");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>篮球>篮板>固定篮板");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>篮球>篮板>挂式篮板");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>篮球>篮板>篮板");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>篮球>篮板配件");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>篮球>篮板配件>篮板护条");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>篮球>篮板配件>篮框");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>篮球>篮板配件>篮球架保护套");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>篮球>篮板配件>篮网");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>篮球>篮球");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>篮球>篮球储物筐");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>篮球>篮球服");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>篮球>篮球服>篮球球衣");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>篮球>篮球服>篮球短裤");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>篮球>篮球服>篮球袜子");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>篮球>篮球鞋");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>篮球>篮球鞋>女鞋");put("t_sizeType","Women");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>篮球>篮球鞋>女鞋>女士低帮鞋");put("t_sizeType","Women");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>篮球>篮球鞋>女鞋>女士高帮鞋");put("t_sizeType","Women");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>篮球>篮球鞋>男鞋");put("t_sizeType","Men");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>篮球>篮球鞋>男鞋>男士低帮鞋");put("t_sizeType","Men");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>篮球>篮球鞋>男鞋>男士高帮鞋");put("t_sizeType","Men");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>篮球>计分板计时器");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>篮球>训练装备");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>篮球>设备袋");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>袋棍球");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>袋棍球>发球装备");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>袋棍球>头盔");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>袋棍球>守门员手套");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>袋棍球>护具用品");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>袋棍球>护具用品>头盔");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>袋棍球>护具用品>守门员护腹垫");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>袋棍球>护具用品>弹力运动紧身衣");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>袋棍球>护具用品>护目镜");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>袋棍球>护具用品>护肘");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>袋棍球>护具用品>护肩");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>袋棍球>护具用品>护胸");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>袋棍球>护具用品>护腰垫");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>袋棍球>护具用品>护臂");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>袋棍球>护具用品>防护裤");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>袋棍球>护头用品");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>袋棍球>球杆");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>袋棍球>球杆>主攻手球杆");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>袋棍球>球杆>守门员球杆");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>袋棍球>球杆>防守员球杆");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>袋棍球>球棍");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>袋棍球>球网");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>袋棍球>球门");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>袋棍球>袋棍球");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>袋棍球>袋棍球>比赛用球");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>袋棍球>袋棍球>训练球");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>袋棍球>袋棍球手套");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>袋棍球>袋棍球训练工具");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>袋棍球>袋棍球训练装备");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>袋棍球>袋棍球配件");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>袋棍球>袋棍球配件>发球装备");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>袋棍球>袋棍球配件>弹力网");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>袋棍球>袋棍球配件>护牙垫");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>袋棍球>袋棍球配件>球棍袋");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>袋棍球>袋棍球配件>袋棍球缠带");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>袋棍球>袋棍球配件>袋棍球网");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>袋棍球>袋棍球配件>袋棍球装备包");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>袋棍球>装备袋");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>袋棍球>运动员手套");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>袋棍球>运动员护头用具");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>足球运动装备");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>足球运动装备>固定装置");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>足球运动装备>地桩");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>足球运动装备>守门员手套");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>足球运动装备>守门员装备");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>足球运动装备>守门员装备>守门员手套");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>足球运动装备>守门员装备>守门员球衣");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>足球运动装备>守门员装备>守门员短裤");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>足球运动装备>守门员装备>守门员长裤");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>足球运动装备>庭院画线装置");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>足球运动装备>护具用品");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>足球运动装备>护具用品>头盔");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>足球运动装备>护具用品>护具手套");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>足球运动装备>护具用品>护具球衣");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>足球运动装备>护具用品>护具短裤");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>足球运动装备>护具用品>护牙托");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>足球运动装备>护具用品>护肩");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>足球运动装备>护具用品>护腰");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>足球运动装备>护具用品>护腿");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>足球运动装备>护具用品>护腿护膝垫");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>足球运动装备>护具用品>护臂垫");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>足球运动装备>护具用品>护颈");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>足球运动装备>护具用品>束腰带");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>足球运动装备>护具用品>腰胯垫");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>足球运动装备>护具用品>防护衣");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>足球运动装备>护具用品>面罩");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>足球运动装备>护具用品>颏带");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>足球运动装备>护腿");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>足球运动装备>服饰>足球袜子");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>足球运动装备>球架球柜");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>足球运动装备>球网");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>足球运动装备>球门");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>足球运动装备>球门>球门锚");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>足球运动装备>球门>直插式足球门");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>足球运动装备>球门>足球门");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>足球运动装备>球门柱");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>足球运动装备>装备包");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>足球运动装备>训练辅助器材");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>足球运动装备>训练辅助器材>模型装备");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>足球运动装备>训练辅助器材>训练球");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>足球运动装备>训练辅助器材>足球机");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>足球运动装备>训练辅助器材>足球训练网");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>足球运动装备>训练辅助器材>阻力训练装备");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>足球运动装备>设备袋");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>足球运动装备>足球");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>足球运动装备>足球>休闲娱乐足球");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>足球运动装备>足球>室内五人足球");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>足球运动装备>足球>比赛用球");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>足球运动装备>足球>练习用球");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>足球运动装备>足球>足球包");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>足球运动装备>足球>青年足球");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>足球运动装备>足球制服");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>足球运动装备>足球头盔");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>足球运动装备>足球服");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>足球运动装备>足球服>足球夹克衫");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>足球运动装备>足球服>足球球衣");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>足球运动装备>足球服>足球短裤");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>足球运动装备>足球服>足球袜子");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>足球运动装备>足球球网及配件");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>足球运动装备>足球球网及配件>球网");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>足球运动装备>足球球网及配件>球网附件");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>足球运动装备>足球球网及配件>网钩");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>足球运动装备>足球腰带");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>足球运动装备>足球训练装备");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>足球运动装备>足球训练装备>模型装备");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>足球运动装备>足球训练装备>训练球");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>足球运动装备>足球训练装备>训练装备");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>足球运动装备>足球训练装备>足球机");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>足球运动装备>足球训练装备>足球训练网");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>足球运动装备>足球训练装备>阻力训练装备");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>足球运动装备>足球运动服饰");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>足球运动装备>足球运动装备配件");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>足球运动装备>足球运动装备配件>弹力网");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>足球运动装备>足球运动装备配件>足球袋");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>足球运动装备>足球鞋");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>足球运动装备>足球鞋>女士足球鞋");put("t_sizeType","Women");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>足球运动装备>足球鞋>男士足球鞋");put("t_sizeType","Men");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>足球运动装备>足球鞋>男鞋");put("t_sizeType","Men");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>团体运动>足球运动装备>足球鞋>男鞋>男士足球球鞋");put("t_sizeType","Men");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>攀岩");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>攀岩>五金配件");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>攀岩>五金配件>上升器");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>攀岩>五金配件>岩钉救援装备");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>攀岩>五金配件>护具用品");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>攀岩>五金配件>滑轮");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>攀岩>五金配件>绳索下降装备");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>攀岩>安全带");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>攀岩>攀冰装备");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>攀岩>攀冰装备>冰斧");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>攀岩>攀冰装备>冰爪");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>攀岩>攀冰装备>冰爪配件");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>攀岩>攀冰装备>攀冰工具");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>攀岩>攀冰装备>攀冰工具配件");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>攀岩>攀岩固定器");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>攀岩>攀岩头盔");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>攀岩>攀岩绳包");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>攀岩>攀岩鞋");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>攀岩>攀岩鞋>女士攀岩鞋");put("t_sizeType","Women");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>攀岩>攀岩鞋>男士攀岩鞋");put("t_sizeType","Men");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>攀岩>攀爬手套");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>攀岩>滑石粉");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>攀岩>滑石粉袋");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>攀岩>登山扣");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>攀岩>登山扣>快挂");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>攀岩>登山扣>无缝登山连接扣");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>攀岩>登山扣>登山连接扣");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>攀岩>登山服");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>攀岩>登山服>登山夹克衫");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>攀岩>登山服>登山短裤");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>攀岩>登山服>登山衬衫");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>攀岩>登山服>登山袜子");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>攀岩>登山服>登山长裤");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>攀岩>绳子绳索带子");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>攀岩>绳子绳索带子>多功能绳索");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>攀岩>绳子绳索带子>带子");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>攀岩>绳子绳索带子>绳子");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>攀岩>绳子绳索带子>肩带");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>攀岩>绳子绳索带子>菊绳");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>攀岩>防撞垫");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>GPS装置");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>个人护理用品");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>个人护理用品>卫生用品");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>个人护理用品>杀虫剂");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>个人护理用品>毛巾");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>个人护理用品>淋浴设备");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>个人护理用品>肥皂洗发水");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>刀具");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>刀具>多功能工具");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>刀具>手锯");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>刀具>折叠刀");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>刀具>斧头");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>刀具>直刀");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>刀具>铲子");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>包双肩包");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>包双肩包>内架型双肩包");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>包双肩包>外架型双肩包");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>包双肩包>宠物户外登山背包");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>包双肩包>徒步背包");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>包双肩包>徒步腰包");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>包双肩包>水袋");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>包双肩包>滑雪包滑雪板包");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>地形图");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>安全救生装备>急救包");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>安全救生装备>急救取暖用品");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>安全救生装备>急救毯");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>安全救生装备>求生哨");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>安全救生装备>防熊喷雾");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>帐篷");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>帐篷配件");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>帐篷配件>地钉");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>帐篷配件>帐篷桩");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>帐篷配件>帐篷防水布");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>指南针");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>暖手器暖脚器");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>暖手器暖脚器>暖手器");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>暖手器暖脚器>暖脚器");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>水处理设备");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>水处理设备>军用水壶");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>水处理设备>净水器");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>水处理设备>水处理设备");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>水处理设备>滤水器");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>水处理设备>烧瓶");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>灯具");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>灯具>头灯");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>灯具>手电筒");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>灯具>手电筒>前照灯");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>灯具>手电筒>迷你手电筒");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>灯具>提灯");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>灯具>提灯配件");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>登山杖");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>睡袋");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>背包配件");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>背包配件>储水袋");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>背包配件>口袋包");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>背包配件>背包防水罩");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>露营厨房用具");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>露营厨房用具>便携式炉具");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>露营厨房用具>冷却器配件");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>露营厨房用具>冷却机");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>露营厨房用具>冻干食品");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>露营厨房用具>厨具");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>露营厨房用具>咖啡壶茶壶");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>露营厨房用具>炉具配件");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>露营厨房用具>炉具配件>代用燃料");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>露营厨房用具>炉具配件>点火器");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>露营厨房用具>炉具配件>烤架");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>露营厨房用具>炉具配件>空燃料瓶");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>露营厨房用具>点火器");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>露营厨房用具>篝火炊具");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>露营厨房用具>锅");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>露营厨房用具>露营炉具");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>露营厨房用具>餐具");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>露营厨房用具>餐具>杯子");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>露营厨房用具>餐具>盘子");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>露营厨房用具>餐具>碗");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>露营厨房用具>餐具>野战炊具");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>露营厨房用具>餐具>餐具");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>露营家具用品");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>露营家具用品>凳子");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>露营家具用品>桌子");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>露营家具用品>椅子");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>露营家具用品>简易床吊床");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>露营帐篷");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>露营帐篷>观景帐篷");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>露营帐篷>防晒帐篷");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>露营床上用品");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>露营床上用品>充气垫");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>露营床上用品>毯子");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>露营床上用品>睡垫");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>露营床上用品>睡袋配件");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>露营床上用品>睡袋配件>充气袋");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>露营床上用品>睡袋配件>压缩袋");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>露营床上用品>睡袋配件>收纳袋");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>露营床上用品>睡袋配件>衬层");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>露营床上用品>露营枕头");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>露营徒步服饰");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>露营徒步服饰>露营徒步短裤");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>露营徒步服饰>露营徒步衬衫");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>露营徒步服饰>露营徒步袜子");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>露营徒步服饰>露营徒步长裤");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>露营徒步鞋");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>露营徒步鞋>女鞋");put("t_sizeType","Women");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>露营徒步鞋>女鞋>女士跑鞋");put("t_sizeType","Women");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>露营徒步鞋>女鞋>女士防水鞋");put("t_sizeType","Women");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>露营徒步鞋>女鞋>女士鞋子");put("t_sizeType","Women");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>露营徒步鞋>男鞋");put("t_sizeType","Men");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>露营徒步鞋>男鞋>男士登山硬壳鞋");put("t_sizeType","Men");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>露营徒步鞋>男鞋>男士跑鞋");put("t_sizeType","Men");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>露营徒步鞋>男鞋>男士防水鞋");put("t_sizeType","Men");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>露营徒步鞋>男鞋>男士靴子");put("t_sizeType","Men");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>露营徒步鞋>男鞋>男士鞋子");put("t_sizeType","Men");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>户外休闲装备>露营徒步装备>露营袋");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>水上运动装备>快艇装备");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>水上运动装备>快艇装备>快艇鞋子>女士快艇鞋子");put("t_sizeType","Women");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>水上运动装备>快艇装备>快艇鞋子>男士快艇鞋子");put("t_sizeType","Men");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>水上运动装备>快艇装备>比基尼上装");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>水上运动装备>游泳装备>泳帽");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>水上运动装备>游泳装备>泳袜");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>水上运动装备>游泳装备>泳装");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>水上运动装备>游泳装备>泳装>分体泳装");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>水上运动装备>游泳装备>泳装>泳装");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>水上运动装备>游泳装备>泳装>泳裤");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>水上运动装备>游泳装备>泳装>游泳三角裤");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>水上运动装备>游泳装备>泳装>连体泳装");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>水上运动装备>游泳装备>泳镜");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>水上运动装备>游泳装备>游泳耳塞");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>水上运动装备>游泳装备>游泳装备袋");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>水上运动装备>游泳装备>游泳鼻夹");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>水上运动装备>游泳装备>脚蹼");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>水上运动装备>游泳装备>训练装备");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>水上运动装备>游泳装备>训练装备>手蹼");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>水上运动装备>游泳装备>训练装备>救生衣");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>水上运动装备>游泳装备>训练装备>浮带");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>水上运动装备>游泳装备>训练装备>浮板");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>水上运动装备>游泳装备>训练装备>训练脚蹼");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>水上运动装备>游泳装备>训练装备>训练船桨");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>水上运动装备>滑水运动装备>潜水服");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>水上运动装备>潜水装备");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>水上运动装备>潜水装备>浮力调整器(BC)");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>水上运动装备>潜水装备>潜水刀");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>水上运动装备>潜水装备>潜水包");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>水上运动装备>潜水装备>潜水头盔");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>水上运动装备>潜水装备>潜水帽");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>水上运动装备>潜水装备>潜水手套");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>水上运动装备>潜水装备>潜水服");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>水上运动装备>潜水装备>潜水服>干式潜水服");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>水上运动装备>潜水装备>潜水服>湿式潜水服");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>水上运动装备>潜水装备>潜水服>潜水衣");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>水上运动装备>潜水装备>潜水背包");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>水上运动装备>潜水装备>潜水背心");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>水上运动装备>潜水装备>潜水面罩");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>水上运动装备>潜水装备>空气压力监控器");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>水上运动装备>潜水装备>脚蹼");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>水上运动装备>潜水装备>鼻夹");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>水上运动装备>皮艇运动装备>皮划艇");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>水上运动装备>皮艇运动装备>船桨");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>水上运动装备>皮艇运动装备>船桨配件");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>水上运动装备>风筝冲浪装备");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>漆弹游戏与软弹气枪");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>漆弹游戏与软弹气枪>软弹气枪");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>漆弹游戏与软弹气枪>软弹气枪>BB弹");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>漆弹游戏与软弹气枪>软弹气枪>子弹");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>漆弹游戏与软弹气枪>软弹气枪>弹盒");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>漆弹游戏与软弹气枪>软弹气枪>护具用品");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>漆弹游戏与软弹气枪>软弹气枪>护具用品>头盔");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>漆弹游戏与软弹气枪>软弹气枪>护具用品>护目镜");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>漆弹游戏与软弹气枪>软弹气枪>护具用品>护胸");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>漆弹游戏与软弹气枪>软弹气枪>护具用品>面罩");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>漆弹游戏与软弹气枪>软弹气枪>枪套");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>漆弹游戏与软弹气枪>软弹气枪>枪榴弹");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>漆弹游戏与软弹气枪>软弹气枪>枪步枪");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>漆弹游戏与软弹气枪>软弹气枪>枪步枪>手枪");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>漆弹游戏与软弹气枪>软弹气枪>枪步枪>散弹枪");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>漆弹游戏与软弹气枪>软弹气枪>枪步枪>步枪");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>漆弹游戏与软弹气枪>软弹气枪>枪盒");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>漆弹游戏与软弹气枪>软弹气枪>枪膛");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>漆弹游戏与软弹气枪>软弹气枪>气枪修理包");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>漆弹游戏与软弹气枪>软弹气枪>激光器");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>漆弹游戏与软弹气枪>软弹气枪>电池");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>漆弹游戏与软弹气枪>软弹气枪>瞄准器");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>漆弹游戏与软弹气枪>软弹气枪>瞄准镜");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>漆弹游戏与软弹气枪>软弹气枪>瞄准镜架");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>漆弹游戏与软弹气枪>软弹气枪>装弹器");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>冰钓");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>冰钓>冰叉装备");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>冰钓>卷线器");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>冰钓>帐篷");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>冰钓>折叠座椅");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>冰钓>钻冰器");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>冰钓>鱼竿");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>冰钓>鱼竿轴线组合");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>冰钓>鱼线");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>卷线器");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>卷线器>密闭式卷线器");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>卷线器>海钓卷线器");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>卷线器>绕丝轮");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>卷线器>船钓轮");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>卷线器>路亚卷线器");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>卷线器>近海卷线器");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>垂钓装备配件");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>垂钓装备配件>刀具");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>垂钓装备配件>勾刀");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>垂钓装备配件>卷盘护理配件");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>垂钓装备配件>卷线轴配件");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>垂钓装备配件>图表地图");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>垂钓装备配件>垂钓杆");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>垂钓装备配件>垂钓筒杆套装");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>垂钓装备配件>安全护具");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>垂钓装备配件>捕鱼网");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>垂钓装备配件>探鱼器");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>垂钓装备配件>支架");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>垂钓装备配件>浮标");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>垂钓装备配件>渔具盒");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>垂钓装备配件>量鱼尺");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>垂钓装备配件>钓具包鱼饵包");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>垂钓装备配件>钓具存储盘");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>垂钓装备配件>钓具存储袋");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>垂钓装备配件>钓鱼带");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>垂钓装备配件>钓鱼手套");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>垂钓装备配件>钳子");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>垂钓装备配件>饵料盒");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>垂钓装备配件>鱼叉");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>垂钓装备配件>鱼夹");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>垂钓装备配件>鱼网");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>垂钓靴子防水裤");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>渔具配件");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>渔具配件>接钩绳");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>渔具配件>接钩绳装备");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>渔具配件>浮标");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>渔具配件>渔具天平");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>渔具配件>轴承别针");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>渔具配件>钓鱼亮片");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>渔具配件>铅坠");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>渔具配件>鱼钩");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>渔船");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>背心马甲");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>诱饵钓饵");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>诱饵钓饵>人工鱼饵");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>诱饵钓饵>假饵");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>诱饵钓饵>发光假饵");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>诱饵钓饵>发光鱼饵");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>诱饵钓饵>悬浮型假饵");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>诱饵钓饵>振动和噪音型假饵");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>诱饵钓饵>深潜浮水型假饵");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>诱饵钓饵>缓沉型假饵");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>诱饵钓饵>羽毛假饵");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>诱饵钓饵>诱饵装备");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>诱饵钓饵>软塑料假饵");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>诱饵钓饵>金属鱼饵（Spoons系列）");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>诱饵钓饵>鱼饵");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>诱饵钓饵>鱼饵>干鱼饵");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>诱饵钓饵>鱼饵>湿鱼饵");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>飞钓");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>飞钓>卷线器");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>飞钓>飞钓配件");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>飞钓>飞钓配件>假饵钓鱼材料");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>飞钓>飞钓配件>假饵钓鱼装备");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>飞钓>飞钓配件>飞钓工具");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>飞钓>飞钓配件>飞钓杆盒飞钓杆包");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>飞钓>飞钓配件>饵料盒");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>飞钓>飞钓配件>鱼线材料");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>飞钓>鱼竿");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>飞钓>鱼竿轴线组合");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>飞钓>鱼线");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>飞钓>鱼饵");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>飞钓>鱼饵>干鱼饵");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>飞钓>鱼饵>湿鱼饵");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>鱼漂管");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>鱼竿");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>鱼竿>密闭式鱼竿");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>鱼竿>拖钓竿");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>鱼竿>海钓鱼竿");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>鱼竿>绕丝轮鱼竿");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>鱼竿>路亚鱼竿");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>鱼竿>近海鱼竿");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>鱼竿卷线器组合");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>鱼竿卷线器组合>密闭式鱼竿卷线器组合");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>鱼竿卷线器组合>海钓鱼竿卷线器组合");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>鱼竿卷线器组合>绕丝轮鱼竿卷线器组合");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>鱼竿卷线器组合>船钓轮鱼竿卷线器组合");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>鱼竿卷线器组合>路亚鱼竿卷线器组合");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>鱼竿卷线器组合>近海鱼竿卷线器组合");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>鱼线");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>鱼线>单丝线");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>鱼线>碳氟线");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>鱼线>编织线");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>鱼线>铅芯线");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>鱼饵");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>鱼饵>引诱剂");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>鱼饵>拟饵");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>垂钓装备>鱼饵>鸡蛋鱼饵");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>狩猎装备>鞋子>女鞋");put("t_sizeType","Women");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>狩猎与垂钓>狩猎装备>鞋子>男鞋");put("t_sizeType","Men");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>球拍运动装备");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>球拍运动装备>壁球装备");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>球拍运动装备>壁球装备>壁球");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>球拍运动装备>壁球装备>壁球护目镜");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>球拍运动装备>壁球装备>壁球拍");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>球拍运动装备>壁球装备>壁球球拍把套");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>球拍运动装备>壁球装备>壁球设备袋");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>球拍运动装备>网球");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>球拍运动装备>网球>发球机");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>球拍运动装备>网球>发球机发球工具");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>球拍运动装备>网球>球拍把套");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>球拍运动装备>网球>球拍线");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>球拍运动装备>网球>网球");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>球拍运动装备>网球>网球包");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>球拍运动装备>网球>网球拍");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>球拍运动装备>网球>网球拍包");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>球拍运动装备>网球>网球服");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>球拍运动装备>网球>网球服>网球半身裙");put("t_sizeType","Women");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>球拍运动装备>网球>网球服>网球夹克衫");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>球拍运动装备>网球>网球服>网球短裤");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>球拍运动装备>网球>网球服>网球衬衫");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>球拍运动装备>网球>网球服>网球袜子");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>球拍运动装备>网球>网球服>网球裙式裤");put("t_sizeType","Women");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>球拍运动装备>网球>网球服>网球连衣裙");put("t_sizeType","Women");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>球拍运动装备>网球>网球服>网球长裤");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>球拍运动装备>网球>网球筐");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>球拍运动装备>网球>网球网");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>球拍运动装备>网球>网球训练用品");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>球拍运动装备>网球>网球鞋");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>球拍运动装备>网球>网球鞋>女士网球鞋");put("t_sizeType","Women");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>球拍运动装备>网球>网球鞋>男士网球鞋");put("t_sizeType","Men");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>球拍运动装备>网球>避震工具");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>球拍运动装备>美式壁球");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>球拍运动装备>美式壁球>护目镜");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>球拍运动装备>美式壁球>球拍");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>球拍运动装备>美式壁球>美式壁球");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>球拍运动装备>美式壁球>美式壁球手套");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>球拍运动装备>美式壁球>美式壁球鞋");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>球拍运动装备>美式壁球>美式壁球鞋>女士壁球鞋");put("t_sizeType","Women");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>球拍运动装备>美式壁球>美式壁球鞋>男士壁球鞋");put("t_sizeType","Men");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>球拍运动装备>羽毛球");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>球拍运动装备>羽毛球>完整套装");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>球拍运动装备>羽毛球>球网");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>球拍运动装备>羽毛球>羽毛球");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>球拍运动装备>羽毛球>羽毛球包");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>球拍运动装备>羽毛球>羽毛球拍");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>BMX 装备");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>BMX 装备>BMX 头盔");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>BMX 装备>护具用品");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>BMX 装备>自行车");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>BMX 装备>自行车>公路自行车");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>BMX 装备>自行车>特技自行车");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>BMX 装备>车架");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>BMX 装备>零部件");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>BMX 装备>零部件>中轴");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>BMX 装备>零部件>刹车部件");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>BMX 装备>零部件>前叉");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>BMX 装备>零部件>变速器");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>BMX 装备>零部件>座垫");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>BMX 装备>零部件>座管");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>BMX 装备>零部件>指拨变速器");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>BMX 装备>零部件>牙盘");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>BMX 装备>零部件>牙盘曲柄组");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>BMX 装备>零部件>缆线");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>BMX 装备>零部件>脚踏板");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>BMX 装备>零部件>自行车刹车装置");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>BMX 装备>零部件>自行车把立");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>BMX 装备>零部件>自行车车头碗");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>BMX 装备>零部件>自行车链条");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>BMX 装备>零部件>花鼓");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>BMX 装备>零部件>车圈");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>BMX 装备>零部件>车把");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>BMX 装备>零部件>车把套");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>BMX 装备>零部件>车轮");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>BMX 装备>零部件>轮胎");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>BMX 装备>零部件>飞轮");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车>BMX 小轮车");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车>BMX 小轮车>公路自行车");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车>BMX 小轮车>特技自行车");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车>公路自行车");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车>双人自行车");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车>山地自行车");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车>山地自行车>前避震山地自行车");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车>山地自行车>双避震山地自行车");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车>折叠自行车");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车>斜躺自行车");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车>混合动力自行车");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车>电动平衡车");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车>电动自行车");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车>鞍座");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车修车工具设备");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车修车工具设备>多功能工具");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车修车工具设备>工作梯架");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车修车工具设备>工具箱");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车修车工具设备>油");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车修车工具设备>润滑脂");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车修车工具设备>清洁剂");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车修车工具设备>线剪钳");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车配件");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车配件>GPS装置");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车配件>儿童座椅");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车配件>心率监测器");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车配件>快拆座管束销");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车配件>挡泥板");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车配件>气嘴帽");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车配件>水壶");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车配件>水壶架");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车配件>自行车包");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车配件>自行车后视镜");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车配件>自行车尾包");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车配件>自行车尾架");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车配件>自行车打气筒");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车配件>自行车打气筒>含CO2打气筒");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车配件>自行车打气筒>打气筒");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车配件>自行车打气筒>落地式打气筒");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车配件>自行车挂架");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车配件>自行车教练用品");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车配件>自行车教练用品配件");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车配件>自行车水袋背包");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车配件>自行车灯饰零件配件");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车配件>自行车码表");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车配件>自行车筐");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车配件>自行车脚踏板");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车配件>自行车车锁");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车配件>自行车车锁>自行车U形锁");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车配件>自行车车锁>自行车线缆锁");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车配件>自行车车锁>自行车链条锁");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车配件>自行车铃");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车配件>车把包");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车配件>车胎修理工具箱");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车配件>辅助轮");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车配件>骑行手套");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车零部件");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车零部件>中轴");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车零部件>内胎");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车零部件>刹车线");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车零部件>刹车装置");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车零部件>刹车部件");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车零部件>刹车部件>刹车卡钳");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车零部件>刹车部件>刹车块");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车零部件>刹车部件>刹车块固定座");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车零部件>刹车部件>刹车软管");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车零部件>刹车部件>撬胎棒");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车零部件>刹车部件>碟片");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车零部件>前叉");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车零部件>变速器");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车零部件>座位调节装置");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车零部件>座垫");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车零部件>座管");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车零部件>指拨变速器");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车零部件>牙盘");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车零部件>牙盘曲柄组");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车零部件>脚踏板");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车零部件>自行车把立");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车零部件>自行车车头碗");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车零部件>自行车车胎");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车零部件>自行车辐条");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车零部件>自行车链条");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车零部件>花鼓");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车零部件>车圈");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车零部件>车头碗组垫片");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车零部件>车把");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车零部件>车把套");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车零部件>车架");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车零部件>车轮");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车零部件>避震装置");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车零部件>链盒");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车零部件>防卷链器");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车零部件>防掉链器");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车零部件>防缠绕电缆");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车零部件>飞轮");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车零部件>飞轮齿轮");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车骑行头盔");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车骑行鞋");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车骑行鞋>女鞋");put("t_sizeType","Women");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车骑行鞋>女鞋>女士公路车锁鞋");put("t_sizeType","Women");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车骑行鞋>女鞋>女士球鞋");put("t_sizeType","Women");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车骑行鞋>女鞋>女士登山鞋");put("t_sizeType","Women");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车骑行鞋>男鞋");put("t_sizeType","Men");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车骑行鞋>男鞋>男士公路车锁鞋");put("t_sizeType","Men");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车骑行鞋>男鞋>男士球鞋");put("t_sizeType","Men");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>自行车骑行鞋>男鞋>男士登山鞋");put("t_sizeType","Men");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>车架");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>车灯及反光镜");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>车灯及反光镜>反光镜");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>车灯及反光镜>尾灯");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>车灯及反光镜>车头灯");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>车灯及反光镜>车头灯尾灯组合");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>骑行服");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>骑行服>骑行夹克衫");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>骑行服>骑行球衣");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>骑行服>骑行短裤");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>骑行服>骑行紧身衣");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>骑行服>骑行背心马甲");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>骑行服>骑行袜子");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>骑行服>骑行连身车裤");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>自行车及摩托车运动装备>自行车及其配件>骑行服>骑行长裤");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>力量训练器械");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>力量训练器械>俯卧撑支架");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>力量训练器械>健腹器");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>力量训练器械>健身椅");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>力量训练器械>史密斯综合训练机器");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>力量训练器械>哑铃");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>力量训练器械>壶铃");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>力量训练器械>引体向上单杆");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>力量训练器械>拉伸装备");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>力量训练器械>握力器");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>力量训练器械>放置架");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>力量训练器械>放置架>哑铃架");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>力量训练器械>放置架>器械放置架");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>力量训练器械>综合训练器");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>力量训练器械>背肌训练器");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>力量训练器械>脚踝负重沙袋");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>力量训练器械>腕力器");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>力量训练器械>腿部训练器");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>力量训练器械>臂力器");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>力量训练器械>负重护腕");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>力量训练器械>负重背心");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>力量训练器械>负重装备");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>力量训练器械>运动支架");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>力量训练器械>飞碟");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>平衡练习器");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>平衡练习器>健身球");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>平衡练习器>平衡板");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>平衡练习器>瑜伽柱");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>普拉提装备");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>普拉提装备>健身滚轴");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>普拉提装备>垫子");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>普拉提装备>弹性带");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>普拉提装备>普拉提健身球");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>普拉提装备>普拉提圈");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>普拉提装备>普拉提床");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>有氧训练装备");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>有氧训练装备>健身器");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>有氧训练装备>健身自行车");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>有氧训练装备>抖抖机");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>有氧训练装备>滑雪机");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>有氧训练装备>训练器");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>有氧训练装备>跑步机");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>有氧训练装备>踏步机");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>瑜伽");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>瑜伽>丝制眼枕");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>瑜伽>伸展带");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>瑜伽>瑜伽伸展器");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>瑜伽>瑜伽垫");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>瑜伽>瑜伽垫袋子");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>瑜伽>瑜伽服");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>瑜伽>瑜伽服饰>瑜伽短裤");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>瑜伽>瑜伽服饰>瑜伽衬衫");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>瑜伽>瑜伽服饰>瑜伽袜子");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>瑜伽>瑜伽服饰>瑜伽裤");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>瑜伽>瑜伽砖");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>瑜伽>瑜伽训练手套");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>瑜伽>瑜伽辅助工具");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>跑步装备");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>跑步装备>腰包");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>跑步装备>臂包");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>跑步装备>跑步服饰");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>跑步装备>跑步服饰>跑步夹克衫");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>跑步装备>跑步服饰>跑步短裤");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>跑步装备>跑步服饰>跑步紧身裤");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>跑步装备>跑步服饰>跑步袜子");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>跑步装备>跑步服饰>跑步长裤");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>跑步装备>跑步鞋");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>跑步装备>跑步鞋>女鞋");put("t_sizeType","Women");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>跑步装备>跑步鞋>女鞋>女士健行鞋");put("t_sizeType","Women");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>跑步装备>跑步鞋>女鞋>女士跑鞋");put("t_sizeType","Women");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>跑步装备>跑步鞋>男鞋");put("t_sizeType","Men");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>跑步装备>跑步鞋>男鞋>男士健行鞋");put("t_sizeType","Men");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>跑步装备>跑步鞋>男鞋>男士训练鞋");put("t_sizeType","Men");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>跑步装备>跑步鞋>男鞋>男士跑鞋");put("t_sizeType","Men");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>跑步装备>跑步鞋>男鞋>男士钉鞋");put("t_sizeType","Men");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>运动健身服");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>运动健身服>女士运动服装");put("t_sizeType","Women");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>运动健身服>女士运动服装>女士田径服");put("t_sizeType","Women");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>运动健身服>女士运动服装>女士运动上装");put("t_sizeType","Women");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>运动健身服>女士运动服装>女士运动内衣");put("t_sizeType","Women");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>运动健身服>女士运动服装>女士运动半身裙");put("t_sizeType","Women");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>运动健身服>女士运动服装>女士运动基础内衣");put("t_sizeType","Women");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>运动健身服>女士运动服装>女士运动基础内衣>女士运动内衣上装");put("t_sizeType","Women");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>运动健身服>女士运动服装>女士运动基础内衣>女士运动内衣下装");put("t_sizeType","Women");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>运动健身服>女士运动服装>女士运动基础内衣>女士运动内衣套装");put("t_sizeType","Women");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>运动健身服>女士运动服装>女士运动夹克");put("t_sizeType","Women");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>运动健身服>女士运动服装>女士运动文胸");put("t_sizeType","Women");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>运动健身服>女士运动服装>女士运动服");put("t_sizeType","Women");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>运动健身服>女士运动服装>女士运动毛衣");put("t_sizeType","Women");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>运动健身服>女士运动服装>女士运动短裤");put("t_sizeType","Women");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>运动健身服>女士运动服装>女士运动紧身裤");put("t_sizeType","Women");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>运动健身服>女士运动服装>女士运动衫");put("t_sizeType","Women");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>运动健身服>女士运动服装>女士运动衫>女士圆领运动衫");put("t_sizeType","Women");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>运动健身服>女士运动服装>女士运动衫>女士拉链运动衫");put("t_sizeType","Women");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>运动健身服>女士运动服装>女士运动袜子");put("t_sizeType","Women");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>运动健身服>女士运动服装>女士运动裙裤");put("t_sizeType","Women");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>运动健身服>女士运动服装>女士运动裤");put("t_sizeType","Women");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>运动健身服>女士运动服装>女士运动连衣裙");put("t_sizeType","Women");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>运动健身服>女士运动服装>女士运动长裤");put("t_sizeType","Women");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>运动健身服>男士运动服装");put("t_sizeType","Men");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>运动健身服>男士运动服装>男士田径服");put("t_sizeType","Men");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>运动健身服>男士运动服装>男士运动内衣");put("t_sizeType","Men");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>运动健身服>男士运动服装>男士运动内裤");put("t_sizeType","Men");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>运动健身服>男士运动服装>男士运动基础内衣");put("t_sizeType","Men");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>运动健身服>男士运动服装>男士运动基础内衣>男士运动内衣上装");put("t_sizeType","Men");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>运动健身服>男士运动服装>男士运动基础内衣>男士运动内衣下装");put("t_sizeType","Men");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>运动健身服>男士运动服装>男士运动基础内衣>男士运动内衣套装");put("t_sizeType","Men");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>运动健身服>男士运动服装>男士运动夹克");put("t_sizeType","Men");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>运动健身服>男士运动服装>男士运动服");put("t_sizeType","Men");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>运动健身服>男士运动服装>男士运动毛衣");put("t_sizeType","Men");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>运动健身服>男士运动服装>男士运动短裤");put("t_sizeType","Men");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>运动健身服>男士运动服装>男士运动紧身裤");put("t_sizeType","Men");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>运动健身服>男士运动服装>男士运动衫");put("t_sizeType","Men");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>运动健身服>男士运动服装>男士运动衬衫");put("t_sizeType","Men");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>运动健身服>男士运动服装>男士运动袜子");put("t_sizeType","Men");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>运动健身服>男士运动服装>男士运动裤");put("t_sizeType","Men");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>运动健身服>男士运动服装>男士运动长裤");put("t_sizeType","Men");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>运动健身配件");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>运动健身配件>健身备忘本");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>运动健身配件>健身带");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>运动健身配件>健身球");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>运动健身配件>健身球及其配件");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>运动健身配件>健身踏板");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>运动健身配件>弹跳训练器");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>运动健身配件>束腰带");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>运动健身配件>水上健身器材");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>运动健身配件>瘦身腰带");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>运动健身配件>跳绳");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>运动健身配件>跳跳床");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>运动健身配件>运动健身手套");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>运动健身配件>运动垫");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>运动健身配件>运动项圈");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>运动健身配件>防滑地垫");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>运动装备配件>跑步机润滑油");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>运动装备配件>跑步机马达");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>运动视频");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>铁人三项");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>铁人三项>铁人三项紧身衣");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>鞋子");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>鞋子>女鞋");put("t_sizeType","Women");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动健身>鞋子>男鞋");put("t_sizeType","Men");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>充气设备及其配件");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>发带");put("t_category","运动包/户外包/配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>奖杯奖牌奖项");put("t_category","家居饰品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>奖杯奖牌奖项>奖杯");put("t_category","家居饰品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>奖杯奖牌奖项>奖牌");put("t_category","家居饰品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>奖杯奖牌奖项>奖章");put("t_category","家居饰品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>奖杯奖牌奖项>荣誉证书封皮");put("t_category","家居饰品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>教练裁判用品>口哨");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>教练裁判用品>教练裁判制服");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>教练裁判用品>秒表");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>教练裁判用品>计分牌及计时工具");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>汽车运动用品车载架");put("t_category","汽车/用品/配件/改装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>汽车运动用品车载架>车载冲浪板架");put("t_category","汽车/用品/配件/改装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>汽车运动用品车载架>车载帆板架");put("t_category","汽车/用品/配件/改装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>汽车运动用品车载架>车载滑雪板架");put("t_category","汽车/用品/配件/改装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>汽车运动用品车载架>车载皮划艇独木舟架");put("t_category","汽车/用品/配件/改装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>汽车运动用品车载架>车载自行车架");put("t_category","汽车/用品/配件/改装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>电子产品及小配件>GPS装置");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>电子产品及小配件>GPS装置>徒步GPS装置");put("t_category","运动包/户外包/配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>电子产品及小配件>GPS装置>自行车GPS装置");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>电子产品及小配件>GPS装置>跑步GPS装置");put("t_category","运动包/户外包/配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>电子产品及小配件>倾斜仪");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>电子产品及小配件>卡路里计数器");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>电子产品及小配件>心率监测器");put("t_category","运动包/户外包/配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>电子产品及小配件>指南针");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>电子产品及小配件>测高计");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>电子产品及小配件>温度监测器");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>电子产品及小配件>秒表");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>电子产品及小配件>自行车码表");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>电子产品及小配件>计步器");put("t_category","运动包/户外包/配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>电子产品及小配件>车轮转数记录器");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>电子产品及小配件>速度表");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>纪念品展示盒存储盒");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>纪念品展示盒存储盒>卡套");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>纪念品展示盒存储盒>卡片册");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>纪念品展示盒存储盒>卡片盒");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>运动包");put("t_category","运动包/户外包/配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>运动医疗");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>运动医疗>冷热疗法用品");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>运动医疗>急诊工具箱");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>运动医疗>护具用品");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>运动医疗>绷带敷料");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>运动医疗>绷带敷料>创可贴");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>运动医疗>绷带敷料>医用手指套");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>运动医疗>绷带敷料>急救胶带");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>运动医疗>绷带敷料>纱布药棉");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>运动医疗>绷带敷料>纱布药棉>包扎药棉");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>运动医疗>绷带敷料>纱布药棉>纱布");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>运动医疗>绷带敷料>防水绷带");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>运动医疗>运动健身护具");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>运动医疗>运动健身护具>保护带");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>运动医疗>运动健身护具>保护带>夹板");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>运动医疗>运动健身护具>保护带>护肘");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>运动医疗>运动健身护具>保护带>护肩带");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>运动医疗>运动健身护具>保护带>护腕手套");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>运动医疗>运动健身护具>保护带>护腰带");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>运动医疗>运动健身护具>保护带>护腿");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>运动医疗>运动健身护具>保护带>护膝");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>运动医疗>运动健身护具>保护带>护踝");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>运动医疗>运动健身护具>保护带>护颈带");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>运动医疗>运动健身护具>夹板");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>运动医疗>运动健身护具>护肩带");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>运动医疗>运动健身护具>护腿长袜");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>运动医疗>运动健身护具>矫正带");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>运动场装备>划线器");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>运动场装备>场地标记工具");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>运动场装备>标志锥筒");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>运动场装备>标示旗");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>运动场装备>运动场座椅及靠垫");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>运动户外服饰>女士运动户外服饰");put("t_sizeType","Women");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>运动户外服饰>女士运动户外服饰>女士基础内衣");put("t_sizeType","Women");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>运动户外服饰>女士运动户外服饰>女士基础内衣>女士基础内衣上装");put("t_sizeType","Women");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>运动户外服饰>女士运动户外服饰>女士基础内衣>女士基础内衣下装");put("t_sizeType","Women");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>运动户外服饰>女士运动户外服饰>女士基础内衣>女士基础内衣套装");put("t_sizeType","Women");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>运动户外服饰>女士运动户外服饰>女士田径服");put("t_sizeType","Women");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>运动户外服饰>女士运动户外服饰>女士运动夹克");put("t_sizeType","Women");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>运动户外服饰>女士运动户外服饰>女士运动户外上装");put("t_sizeType","Women");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>运动户外服饰>女士运动户外服饰>女士运动户外内衣");put("t_sizeType","Women");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>运动户外服饰>女士运动户外服饰>女士运动户外半身裙");put("t_sizeType","Women");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>运动户外服饰>女士运动户外服饰>女士运动户外毛衣");put("t_sizeType","Women");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>运动户外服饰>女士运动户外服饰>女士运动户外短裤");put("t_sizeType","Women");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>运动户外服饰>女士运动户外服饰>女士运动户外紧身裤");put("t_sizeType","Women");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>运动户外服饰>女士运动户外服饰>女士运动户外袜子");put("t_sizeType","Women");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>运动户外服饰>女士运动户外服饰>女士运动户外裙裤");put("t_sizeType","Women");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>运动户外服饰>女士运动户外服饰>女士运动户外连衣裙");put("t_sizeType","Women");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>运动户外服饰>女士运动户外服饰>女士运动户外长裤");put("t_sizeType","Women");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>运动户外服饰>女士运动户外服饰>女士运动文胸");put("t_sizeType","Women");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>运动户外服饰>女士运动户外服饰>女士运动服");put("t_sizeType","Women");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>运动户外服饰>女士运动户外服饰>女士运动衫");put("t_sizeType","Women");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>运动户外服饰>女士运动户外服饰>女士运动衫>女士圆领运动衫");put("t_sizeType","Women");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>运动户外服饰>女士运动户外服饰>女士运动衫>女士拉链衫");put("t_sizeType","Women");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>运动户外服饰>女士运动户外服饰>女士运动裤");put("t_sizeType","Women");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>运动户外服饰>男士运动户外服饰");put("t_sizeType","Men");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>运动户外服饰>男士运动户外服饰>男士基础内衣");put("t_sizeType","Men");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>运动户外服饰>男士运动户外服饰>男士基础内衣>男士基础内衣上装");put("t_sizeType","Men");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>运动户外服饰>男士运动户外服饰>男士基础内衣>男士基础内衣下装");put("t_sizeType","Men");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>运动户外服饰>男士运动户外服饰>男士基础内衣>男士基础内衣套装");put("t_sizeType","Men");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>运动户外服饰>男士运动户外服饰>男士田径服");put("t_sizeType","Men");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>运动户外服饰>男士运动户外服饰>男士运动夹克");put("t_sizeType","Men");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>运动户外服饰>男士运动户外服饰>男士运动户外内衣");put("t_sizeType","Men");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>运动户外服饰>男士运动户外服饰>男士运动户外内裤");put("t_sizeType","Men");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>运动户外服饰>男士运动户外服饰>男士运动户外紧身裤");put("t_sizeType","Men");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>运动户外服饰>男士运动户外服饰>男士运动户毛衣");put("t_sizeType","Men");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>运动户外服饰>男士运动户外服饰>男士运动户短裤");put("t_sizeType","Men");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>运动户外服饰>男士运动户外服饰>男士运动户衬衫");put("t_sizeType","Men");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>运动户外服饰>男士运动户外服饰>男士运动户袜子");put("t_sizeType","Men");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>运动户外服饰>男士运动户外服饰>男士运动户长裤");put("t_sizeType","Men");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>运动户外服饰>男士运动户外服饰>男士运动服");put("t_sizeType","Men");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>运动户外服饰>男士运动户外服饰>男士运动衫");put("t_sizeType","Men");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>运动户外服饰>男士运动户外服饰>男士运动裤");put("t_sizeType","Men");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>运动手表");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>运动水瓶");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动户外配件>配件盒");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动类用品");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动类用品>滑冰");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动类用品>滑冰>溜冰鞋包");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动类用品>滑冰>滑冰鞋");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动类用品>滑板运动");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动类用品>滑板运动>护具用品");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动类用品>滑板运动>护具用品>护肘");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动类用品>滑板运动>护具用品>护腕");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动类用品>滑板运动>护具用品>护膝");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动类用品>滑板运动>护具用品>防摔裤");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动类用品>滑板运动>滑板用坡道栏杆");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动类用品>滑板运动>滑板蜡");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动类用品>滑板运动>滑板运动头盔");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动类用品>滑板运动>滑板运动工具");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动类用品>滑板运动>滑板运动装备包");put("t_category","运动包/户外包/配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动类用品>滑板运动>滑板运动配件");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动类用品>滑板运动>滑板运动配件>冒口垫");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动类用品>滑板运动>滑板运动配件>套管");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动类用品>滑板运动>滑板运动配件>滑板");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动类用品>滑板运动>滑板运动配件>滑板五金配件");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动类用品>滑板运动>滑板运动配件>滑板砂纸");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动类用品>滑板运动>滑板运动配件>滑板车");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动类用品>滑板运动>滑板运动配件>滑轮");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动类用品>滑板运动>滑板运动配件>轴承");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动类用品>滑板运动>滑板鞋");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动类用品>滑板运动>滑板鞋>女士滑板鞋");put("t_sizeType","Women");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动类用品>滑板运动>滑板鞋>男士滑板鞋");put("t_sizeType","Men");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动类用品>滑板运动>速滑服");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动类用品>滑板运动>速滑服>速滑T恤");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动类用品>滑板运动>速滑服>速滑帽衫");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动类用品>滑板运动>速滑服>速滑短裤");put("t_category","运动服/休闲服装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动类用品>轮滑");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动类用品>轮滑>双排轮配件");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动类用品>轮滑>护具用品");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动类用品>轮滑>护具用品>护肘");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动类用品>轮滑>护具用品>护腕");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动类用品>轮滑>护具用品>护膝");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动类用品>轮滑>护具用品>防摔裤");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动类用品>轮滑>溜冰鞋");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动类用品>轮滑>直排轮");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动类用品>轮滑>直排轮配件");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动类用品>轮滑>直排轮配件>轮子");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动类用品>轮滑>直排轮配件>轴承");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>运动类用品>轮滑>轮滑头盔");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>GPS 装置");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>高尔夫服饰");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>高尔夫服饰>高尔夫夹克衫");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>高尔夫服饰>高尔夫短裤");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>高尔夫服饰>高尔夫衬衫");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>高尔夫服饰>高尔夫袜子");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>高尔夫服饰>高尔夫长裤");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>高尔夫球");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>高尔夫球>X-Out高尔夫球");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>高尔夫球>创意高尔夫");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>高尔夫球>女士高尔夫球");put("t_sizeType","Women");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>高尔夫球>标准球");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>高尔夫球>浮球");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>高尔夫球>练习球");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>高尔夫球>高尔夫球");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>高尔夫球场配件");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>高尔夫球场配件>果岭叉");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>高尔夫球场配件>毛巾");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>高尔夫球场配件>高尔夫伞");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>高尔夫球场配件>高尔夫拾球器");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>高尔夫球场配件>高尔夫球T恤");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>高尔夫球场配件>高尔夫球标记工具");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>高尔夫球手套");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>高尔夫球杆");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>高尔夫球杆>挖起杆多功能杆");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>高尔夫球杆>挖起杆多功能杆>中继挖起杆");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>高尔夫球杆>挖起杆多功能杆>劈起杆");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>高尔夫球杆>挖起杆多功能杆>多功能杆");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>高尔夫球杆>挖起杆多功能杆>沙坑杆");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>高尔夫球杆>挖起杆多功能杆>进攻果岭挖起杆");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>高尔夫球杆>挖起杆多功能杆>高抛挖起杆");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>高尔夫球杆>木杆");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>高尔夫球杆>木杆>木杆");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>高尔夫球杆>木杆>球道木杆");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>高尔夫球杆>木杆>球道木杆>11号木杆");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>高尔夫球杆>木杆>球道木杆>13号木杆");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>高尔夫球杆>木杆>球道木杆>15号木杆");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>高尔夫球杆>木杆>球道木杆>2号木杆");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>高尔夫球杆>木杆>球道木杆>3号木杆");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>高尔夫球杆>木杆>球道木杆>4号木杆");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>高尔夫球杆>木杆>球道木杆>5号木杆");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>高尔夫球杆>木杆>球道木杆>7号木杆");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>高尔夫球杆>木杆>球道木杆>9号木杆");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>高尔夫球杆>铁木杆");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>高尔夫球杆>铁杆");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>高尔夫球杆>铁杆>单差点");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>高尔夫球杆>铁杆>铁杆套装");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>高尔夫球杆>高尔夫完整套装");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>高尔夫球杆>高尔夫推杆");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>高尔夫球杆包");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>高尔夫球杆包>高尔夫手提包");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>高尔夫球杆包>高尔夫球包");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>高尔夫球杆包配件");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>高尔夫球杆部件");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>高尔夫球杆部件>握把");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>高尔夫球杆部件>杆身");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>高尔夫球杆部件>高尔夫球头");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>高尔夫球车");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>高尔夫球车配件");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>高尔夫球鞋");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>高尔夫球鞋>女士高尔夫球鞋");put("t_sizeType","Women");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>高尔夫球鞋>男士高尔夫球鞋");put("t_sizeType","Men");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>高尔夫训练装备");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>高尔夫训练装备>力量训练器");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>高尔夫训练装备>打击垫");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>高尔夫训练装备>挥杆练习器");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>高尔夫训练装备>推杆练习器");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>高尔夫训练装备>高尔夫室内练习器");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>高尔夫训练装备>高尔夫练习网");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>高尔夫配件");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>高尔夫配件>GPS 装置");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>高尔夫配件>手提包袋");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>高尔夫配件>握把修理装备");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>高尔夫配件>旅行袋");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>高尔夫配件>测距仪");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>高尔夫配件>球杆套");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>高尔夫配件>高尔夫扳手");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>高尔夫配件>高尔夫推拉车");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>高尔夫配件>高尔夫衣物包");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>高尔夫配件>高尔夫软鞋钉");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","运动户外>高尔夫装备>高尔夫配件>高尔夫鞋包");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女童鞋靴");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女童鞋靴>婴幼儿鞋靴");put("t_sizeType","Baby");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女童鞋靴>婴幼儿鞋靴>乐福鞋一脚蹬");put("t_sizeType","Baby");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女童鞋靴>婴幼儿鞋靴>凉鞋");put("t_sizeType","Baby");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女童鞋靴>婴幼儿鞋靴>凉鞋>人字拖");put("t_sizeType","Baby");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女童鞋靴>婴幼儿鞋靴>凉鞋>凉拖");put("t_sizeType","Baby");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女童鞋靴>婴幼儿鞋靴>凉鞋>后扣带凉鞋");put("t_sizeType","Baby");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女童鞋靴>婴幼儿鞋靴>凉鞋>渔夫鞋");put("t_sizeType","Baby");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女童鞋靴>婴幼儿鞋靴>凉鞋>踝扣带凉鞋");put("t_sizeType","Baby");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女童鞋靴>婴幼儿鞋靴>凉鞋>运动凉鞋");put("t_sizeType","Baby");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女童鞋靴>婴幼儿鞋靴>凉鞋>针织鞋");put("t_sizeType","Baby");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女童鞋靴>婴幼儿鞋靴>婴儿鞋");put("t_sizeType","Baby");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女童鞋靴>婴幼儿鞋靴>学步鞋");put("t_sizeType","Baby");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女童鞋靴>婴幼儿鞋靴>平底鞋");put("t_sizeType","Baby");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女童鞋靴>婴幼儿鞋靴>平底鞋>开口");put("t_sizeType","Baby");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女童鞋靴>婴幼儿鞋靴>平底鞋>拖鞋木底鞋");put("t_sizeType","Baby");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女童鞋靴>婴幼儿鞋靴>平底鞋>沙滩鞋");put("t_sizeType","Baby");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女童鞋靴>婴幼儿鞋靴>平底鞋>玛丽珍鞋");put("t_sizeType","Baby");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女童鞋靴>婴幼儿鞋靴>平底鞋>舞蹈鞋");put("t_sizeType","Baby");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女童鞋靴>婴幼儿鞋靴>拖鞋");put("t_sizeType","Baby");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女童鞋靴>婴幼儿鞋靴>时尚运动鞋");put("t_sizeType","Baby");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女童鞋靴>婴幼儿鞋靴>系带");put("t_sizeType","Baby");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女童鞋靴>婴幼儿鞋靴>荧光鞋");put("t_sizeType","Baby");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女童鞋靴>婴幼儿鞋靴>针织鞋");put("t_sizeType","Baby");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女童鞋靴>婴幼儿鞋靴>靴子");put("t_sizeType","Baby");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女童鞋靴>婴幼儿鞋靴>靴子>套头靴");put("t_sizeType","Baby");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女童鞋靴>婴幼儿鞋靴>靴子>拉链靴");put("t_sizeType","Baby");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女童鞋靴>婴幼儿鞋靴>靴子>牛仔靴");put("t_sizeType","Baby");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女童鞋靴>婴幼儿鞋靴>靴子>系带靴子");put("t_sizeType","Baby");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女童鞋靴>婴幼儿鞋靴>靴子>羊毛靴");put("t_sizeType","Baby");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女童鞋靴>婴幼儿鞋靴>靴子>防寒靴子");put("t_sizeType","Baby");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女童鞋靴>婴幼儿鞋靴>靴子>雨靴");put("t_sizeType","Baby");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女童鞋靴>小童&大童鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女童鞋靴>小童&大童鞋>乐福鞋一脚蹬");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女童鞋靴>小童&大童鞋>凉鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女童鞋靴>小童&大童鞋>凉鞋>凉拖");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女童鞋靴>小童&大童鞋>凉鞋>后扣带凉鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女童鞋靴>小童&大童鞋>凉鞋>夹趾凉鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女童鞋靴>小童&大童鞋>凉鞋>渔夫鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女童鞋靴>小童&大童鞋>凉鞋>踝扣带凉鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女童鞋靴>小童&大童鞋>凉鞋>运动凉鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女童鞋靴>小童&大童鞋>凉鞋>针织鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女童鞋靴>小童&大童鞋>平底鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女童鞋靴>小童&大童鞋>平底鞋>开口鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女童鞋靴>小童&大童鞋>平底鞋>拖鞋木底鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女童鞋靴>小童&大童鞋>平底鞋>沙滩鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女童鞋靴>小童&大童鞋>平底鞋>玛丽珍鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女童鞋靴>小童&大童鞋>平底鞋>舞蹈鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女童鞋靴>小童&大童鞋>拖鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女童鞋靴>小童&大童鞋>时尚休闲鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女童鞋靴>小童&大童鞋>童鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女童鞋靴>小童&大童鞋>系带");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女童鞋靴>小童&大童鞋>荧光鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女童鞋靴>小童&大童鞋>运动户外鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女童鞋靴>小童&大童鞋>运动户外鞋>板鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女童鞋靴>小童&大童鞋>运动户外鞋>滑板鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女童鞋靴>小童&大童鞋>运动户外鞋>球鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女童鞋靴>小童&大童鞋>运动户外鞋>登山鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女童鞋靴>小童&大童鞋>运动户外鞋>篮球鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女童鞋靴>小童&大童鞋>运动户外鞋>网球鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女童鞋靴>小童&大童鞋>运动户外鞋>舞蹈鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女童鞋靴>小童&大童鞋>运动户外鞋>足球鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女童鞋靴>小童&大童鞋>运动户外鞋>跑鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女童鞋靴>小童&大童鞋>运动户外鞋>雨鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女童鞋靴>小童&大童鞋>针织鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女童鞋靴>小童&大童鞋>靴子");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女童鞋靴>小童&大童鞋>靴子>套头靴");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女童鞋靴>小童&大童鞋>靴子>拉链靴");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女童鞋靴>小童&大童鞋>靴子>牛仔靴");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女童鞋靴>小童&大童鞋>靴子>系带靴子");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女童鞋靴>小童&大童鞋>靴子>羊毛靴");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女童鞋靴>小童&大童鞋>靴子>防寒靴子");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女童鞋靴>小童&大童鞋>靴子>雨靴");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋> 草编鞋>后空凉鞋");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋> 草编鞋>草编罗马鞋");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>乐福鞋一脚蹬");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>乐福鞋一脚蹬>乐福鞋");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>乐福鞋一脚蹬>孟克鞋");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>乐福鞋一脚蹬>帆船鞋");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>乐福鞋一脚蹬>驾车鞋");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>乐福鞋一脚蹬>麂皮鞋");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>凉鞋 ");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>凉鞋>人字拖");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>凉鞋>凉拖");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>凉鞋>后空凉鞋");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>凉鞋>坡跟凉鞋");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>凉鞋>沙滩鞋");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>凉鞋>渔夫鞋");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>凉鞋>绑带");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>凉鞋>运动户外鞋");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>凉鞋>运动户外鞋>人字拖");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>凉鞋>运动户外鞋>凉拖");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>凉鞋>运动户外鞋>后扣带凉鞋");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>凉鞋>防水台凉鞋");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>工作鞋安全鞋");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>工作鞋安全鞋>靴子");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>工作鞋安全鞋>鞋子");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>帆船鞋");put("t_sizeType","Women");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>平底鞋");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>平底鞋>后空凉鞋");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>平底鞋>拖鞋");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>平底鞋>沙滩鞋");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>平底鞋>玛丽珍鞋");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>平底鞋>舞蹈鞋");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>户外鞋");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>户外鞋>休闲鞋");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>户外鞋>凉鞋");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>户外鞋>凉鞋>人字拖");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>户外鞋>凉鞋>后扣带凉鞋");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>户外鞋>凉鞋>拖鞋");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>户外鞋>猎手鞋");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>户外鞋>登山鞋");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>户外鞋>登山鞋>登山鞋");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>户外鞋>登山鞋>跑鞋");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>户外鞋>登山鞋>雨鞋");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>户外鞋>登山鞋>靴子");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>户外鞋>登山鞋>鞋");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>户外鞋>跑鞋");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>户外鞋>跑鞋>跑鞋");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>户外鞋>雨鞋");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>户外鞋>雪板鞋");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>户外鞋>靴子");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>户外鞋>靴子>登山鞋");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>户外鞋>靴子>防寒靴子");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>户外鞋>靴子>雨靴");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>户外鞋>骑行鞋");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>户外鞋>骑行鞋>公路车锁鞋");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>户外鞋>骑行鞋>球鞋");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>户外鞋>骑行鞋>登山鞋");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>拖鞋");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>拖鞋>人字拖");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>拖鞋>凉拖");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>拖鞋>家居鞋");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>拖鞋>羊毛拖鞋");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>拖鞋>软底棉拖鞋");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>拖鞋>运动休闲拖鞋");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>拖鞋>麂皮鞋");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>拖鞋木底鞋");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>拖鞋木底鞋>坡跟鞋");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>拖鞋木底鞋>拖鞋");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>拖鞋木底鞋>木底鞋");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>时尚休闲鞋");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>时尚休闲鞋>一脚蹬");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>时尚休闲鞋>后空凉鞋");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>时尚休闲鞋>坡跟鞋");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>时尚休闲鞋>拖鞋");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>时尚休闲鞋>玛丽珍鞋");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>时尚休闲鞋>系带");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>时尚休闲鞋>针织鞋");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>系带");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>草编鞋");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>运动迷衍生鞋");put("t_sizeType","Women");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>运动迷衍生鞋>休闲鞋");put("t_sizeType","Women");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>运动迷衍生鞋>凉鞋");put("t_sizeType","Women");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>运动迷衍生鞋>拖鞋");put("t_sizeType","Women");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>运动鞋");put("t_sizeType","Women");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>运动鞋>休闲鞋");put("t_sizeType","Women");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>运动鞋>保龄球鞋");put("t_sizeType","Women");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>运动鞋>帆船鞋");put("t_sizeType","Women");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>运动鞋>排球鞋");put("t_sizeType","Women");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>运动鞋>板鞋");put("t_sizeType","Women");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>运动鞋>滑板鞋");put("t_sizeType","Women");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>运动鞋>球鞋");put("t_sizeType","Women");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>运动鞋>篮球鞋");put("t_sizeType","Women");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>运动鞋>篮球鞋>低帮鞋");put("t_sizeType","Women");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>运动鞋>篮球鞋>高帮鞋");put("t_sizeType","Women");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>运动鞋>网球鞋");put("t_sizeType","Women");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>运动鞋>舞蹈鞋");put("t_sizeType","Women");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>运动鞋>训练鞋");put("t_sizeType","Women");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>运动鞋>足球鞋");put("t_sizeType","Women");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>运动鞋>跑鞋");put("t_sizeType","Women");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>运动鞋>跑鞋>跑鞋");put("t_sizeType","Women");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>运动鞋>运动迷衍生鞋");put("t_sizeType","Women");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>运动鞋>运动迷衍生鞋>凉鞋");put("t_sizeType","Women");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>运动鞋>运动迷衍生鞋>拖鞋");put("t_sizeType","Women");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>运动鞋>运动迷衍生鞋>运动鞋");put("t_sizeType","Women");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>运动鞋>骑行鞋");put("t_sizeType","Women");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>运动鞋>骑行鞋>公路车锁鞋");put("t_sizeType","Women");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>运动鞋>骑行鞋>球鞋");put("t_sizeType","Women");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>运动鞋>骑行鞋>登山鞋");put("t_sizeType","Women");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>运动鞋>高尔夫球鞋");put("t_sizeType","Women");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>靴子");put("t_sizeType","Women");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>靴子>坡跟靴");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>靴子>套头靴");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>靴子>工作鞋安全鞋");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>靴子>平底鞋");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>靴子>拉链靴");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>靴子>摩托赛车靴");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>靴子>牛仔靴");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>靴子>系带鞋靴");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>靴子>羊毛靴");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>靴子>运动户外鞋");put("t_sizeType","Women");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>靴子>运动户外鞋>登山鞋");put("t_sizeType","Women");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>靴子>运动户外鞋>防寒靴子");put("t_sizeType","Women");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>靴子>运动户外鞋>雨靴");put("t_sizeType","Women");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>靴子>防水台靴子");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>靴子>雨靴");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>靴子>马靴");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>高跟鞋");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>高跟鞋>压花");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>高跟鞋>双色调");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>高跟鞋>后空凉鞋");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>高跟鞋>坡跟鞋");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>高跟鞋>多尔塞高跟鞋");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>高跟鞋>拖鞋");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>高跟鞋>沙滩鞋");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>高跟鞋>玛丽珍鞋");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>高跟鞋>系带高跟鞋");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>高跟鞋>绑带");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>女鞋>高跟鞋>防水台高跟鞋");put("t_sizeType","Women");put("t_category","女鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>手提包");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>手提包>保龄球袋");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>手提包>手包");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>手提包>手拿包");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>手提包>托特包");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>手提包>旅行包");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>手提包>晚宴包");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>手提包>长条形包");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男童鞋靴>婴幼儿鞋靴");put("t_sizeType","Baby");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男童鞋靴>婴幼儿鞋靴>乐福鞋一脚蹬");put("t_sizeType","Baby");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男童鞋靴>婴幼儿鞋靴>凉鞋");put("t_sizeType","Baby");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男童鞋靴>婴幼儿鞋靴>凉鞋>人字拖");put("t_sizeType","Baby");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男童鞋靴>婴幼儿鞋靴>凉鞋>凉拖");put("t_sizeType","Baby");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男童鞋靴>婴幼儿鞋靴>凉鞋>后扣带凉鞋");put("t_sizeType","Baby");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男童鞋靴>婴幼儿鞋靴>凉鞋>渔夫鞋");put("t_sizeType","Baby");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男童鞋靴>婴幼儿鞋靴>凉鞋>踝扣带凉鞋");put("t_sizeType","Baby");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男童鞋靴>婴幼儿鞋靴>凉鞋>运动凉鞋");put("t_sizeType","Baby");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男童鞋靴>婴幼儿鞋靴>凉鞋>针织鞋");put("t_sizeType","Baby");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男童鞋靴>婴幼儿鞋靴>婴儿鞋");put("t_sizeType","Baby");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男童鞋靴>婴幼儿鞋靴>学步鞋");put("t_sizeType","Baby");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男童鞋靴>婴幼儿鞋靴>拖鞋");put("t_sizeType","Baby");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男童鞋靴>婴幼儿鞋靴>时尚休闲鞋");put("t_sizeType","Baby");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男童鞋靴>婴幼儿鞋靴>系带鞋靴");put("t_sizeType","Baby");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男童鞋靴>婴幼儿鞋靴>荧光鞋");put("t_sizeType","Baby");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男童鞋靴>婴幼儿鞋靴>针织鞋");put("t_sizeType","Baby");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男童鞋靴>婴幼儿鞋靴>靴子");put("t_sizeType","Baby");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男童鞋靴>婴幼儿鞋靴>靴子>套头靴");put("t_sizeType","Baby");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男童鞋靴>婴幼儿鞋靴>靴子>拉链靴");put("t_sizeType","Baby");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男童鞋靴>婴幼儿鞋靴>靴子>牛仔靴");put("t_sizeType","Baby");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男童鞋靴>婴幼儿鞋靴>靴子>系带靴子");put("t_sizeType","Baby");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男童鞋靴>婴幼儿鞋靴>靴子>羊皮靴");put("t_sizeType","Baby");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男童鞋靴>婴幼儿鞋靴>靴子>防寒靴子");put("t_sizeType","Baby");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男童鞋靴>婴幼儿鞋靴>靴子>雨靴");put("t_sizeType","Baby");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男童鞋靴>小童&大童鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男童鞋靴>小童&大童鞋>乐福鞋一脚蹬");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男童鞋靴>小童&大童鞋>凉鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男童鞋靴>小童&大童鞋>凉鞋>人字拖");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男童鞋靴>小童&大童鞋>凉鞋>凉拖");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男童鞋靴>小童&大童鞋>凉鞋>后扣带凉鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男童鞋靴>小童&大童鞋>凉鞋>渔夫鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男童鞋靴>小童&大童鞋>凉鞋>踝扣带凉鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男童鞋靴>小童&大童鞋>凉鞋>运动凉鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男童鞋靴>小童&大童鞋>凉鞋>针织鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男童鞋靴>小童&大童鞋>拖鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男童鞋靴>小童&大童鞋>时尚休闲鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男童鞋靴>小童&大童鞋>童鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男童鞋靴>小童&大童鞋>系带");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男童鞋靴>小童&大童鞋>荧光鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男童鞋靴>小童&大童鞋>运动户外鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男童鞋靴>小童&大童鞋>运动户外鞋>板鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男童鞋靴>小童&大童鞋>运动户外鞋>滑板鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男童鞋靴>小童&大童鞋>运动户外鞋>球鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男童鞋靴>小童&大童鞋>运动户外鞋>登山鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男童鞋靴>小童&大童鞋>运动户外鞋>篮球鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男童鞋靴>小童&大童鞋>运动户外鞋>网球鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男童鞋靴>小童&大童鞋>运动户外鞋>舞蹈鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男童鞋靴>小童&大童鞋>运动户外鞋>足球鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男童鞋靴>小童&大童鞋>运动户外鞋>跑鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男童鞋靴>小童&大童鞋>运动户外鞋>雨鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男童鞋靴>小童&大童鞋>针织鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男童鞋靴>小童&大童鞋>靴子");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男童鞋靴>小童&大童鞋>靴子>套头靴");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男童鞋靴>小童&大童鞋>靴子>拉链靴");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男童鞋靴>小童&大童鞋>靴子>牛仔靴");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男童鞋靴>小童&大童鞋>靴子>系带靴子");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男童鞋靴>小童&大童鞋>靴子>羊皮靴");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男童鞋靴>小童&大童鞋>靴子>防寒靴子");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男童鞋靴>小童&大童鞋>靴子>雨靴");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋");put("t_sizeType","Men");put("t_category","男鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>乐福鞋一脚蹬");put("t_sizeType","Men");put("t_category","流行男鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>乐福鞋一脚蹬>乐福鞋");put("t_sizeType","Men");put("t_category","流行男鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>乐福鞋一脚蹬>孟克鞋");put("t_sizeType","Men");put("t_category","流行男鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>乐福鞋一脚蹬>木底鞋");put("t_sizeType","Men");put("t_category","流行男鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>乐福鞋一脚蹬>流苏鞋");put("t_sizeType","Men");put("t_category","流行男鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>乐福鞋一脚蹬>驾车鞋");put("t_sizeType","Men");put("t_category","流行男鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>乐福鞋一脚蹬>麂皮鞋");put("t_sizeType","Men");put("t_category","流行男鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>凉鞋");put("t_sizeType","Men");put("t_category","流行男鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>凉鞋>人字拖");put("t_sizeType","Men");put("t_category","流行男鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>凉鞋>凉拖");put("t_sizeType","Men");put("t_category","流行男鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>凉鞋>渔夫鞋");put("t_sizeType","Men");put("t_category","流行男鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>凉鞋>运动凉鞋");put("t_sizeType","Men");put("t_category","流行男鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>凉鞋>运动凉鞋>人字拖");put("t_sizeType","Men");put("t_category","流行男鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>凉鞋>运动凉鞋>凉拖");put("t_sizeType","Men");put("t_category","流行男鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>凉鞋>运动凉鞋>后扣带凉鞋");put("t_sizeType","Men");put("t_category","流行男鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>工装鞋");put("t_sizeType","Men");put("t_category","流行男鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>工装鞋>靴子");put("t_sizeType","Men");put("t_category","流行男鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>工装鞋>鞋子");put("t_sizeType","Men");put("t_category","流行男鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>帆船鞋");put("t_sizeType","Men");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>户外鞋");put("t_sizeType","Men");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>户外鞋>休闲鞋");put("t_sizeType","Men");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>户外鞋>凉鞋");put("t_sizeType","Men");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>户外鞋>凉鞋>人字拖");put("t_sizeType","Men");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>户外鞋>凉鞋>凉拖");put("t_sizeType","Men");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>户外鞋>凉鞋>后扣带凉鞋");put("t_sizeType","Men");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>户外鞋>滑雪靴");put("t_sizeType","Men");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>户外鞋>猎手鞋");put("t_sizeType","Men");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>户外鞋>登山鞋");put("t_sizeType","Men");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>户外鞋>登山鞋>登山鞋");put("t_sizeType","Men");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>户外鞋>登山鞋>跑鞋");put("t_sizeType","Men");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>户外鞋>登山鞋>靴子");put("t_sizeType","Men");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>户外鞋>跑鞋");put("t_sizeType","Men");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>户外鞋>跑鞋>训练鞋");put("t_sizeType","Men");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>户外鞋>跑鞋>跑鞋");put("t_sizeType","Men");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>户外鞋>跑鞋>钉鞋");put("t_sizeType","Men");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>户外鞋>雨鞋");put("t_sizeType","Men");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>户外鞋>雪板鞋");put("t_sizeType","Men");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>户外鞋>靴子");put("t_sizeType","Men");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>户外鞋>靴子>登山鞋");put("t_sizeType","Men");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>户外鞋>靴子>防寒靴子");put("t_sizeType","Men");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>户外鞋>靴子>雨靴");put("t_sizeType","Men");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>户外鞋>骑行鞋");put("t_sizeType","Men");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>户外鞋>骑行鞋>公路车锁鞋");put("t_sizeType","Men");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>户外鞋>骑行鞋>球鞋");put("t_sizeType","Men");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>户外鞋>骑行鞋>登山鞋");put("t_sizeType","Men");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>拖鞋");put("t_sizeType","Men");put("t_category","流行男鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>拖鞋>家居鞋");put("t_sizeType","Men");put("t_category","流行男鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>拖鞋>户外");put("t_sizeType","Men");put("t_category","流行男鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>拖鞋>羊毛拖鞋");put("t_sizeType","Men");put("t_category","流行男鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>拖鞋>靴子");put("t_sizeType","Men");put("t_category","流行男鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>拖鞋>麂皮鞋");put("t_sizeType","Men");put("t_category","流行男鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>正装鞋");put("t_sizeType","Men");put("t_category","流行男鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>系带");put("t_sizeType","Men");put("t_category","流行男鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>经典时尚休闲鞋");put("t_sizeType","Men");put("t_category","流行男鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>经典时尚休闲鞋>时尚休闲鞋");put("t_sizeType","Men");put("t_category","流行男鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>运动迷衍生鞋");put("t_sizeType","Men");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>运动迷衍生鞋>休闲鞋");put("t_sizeType","Men");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>运动迷衍生鞋>凉鞋");put("t_sizeType","Men");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>运动迷衍生鞋>拖鞋");put("t_sizeType","Men");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>运动鞋");put("t_sizeType","Men");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>运动鞋>休闲鞋");put("t_sizeType","Men");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>运动鞋>保龄球鞋");put("t_sizeType","Men");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>运动鞋>健身运动鞋与 综合训练鞋");put("t_sizeType","Men");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>运动鞋>帆船鞋");put("t_sizeType","Men");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>运动鞋>排球鞋");put("t_sizeType","Men");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>运动鞋>摔跤鞋");put("t_sizeType","Men");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>运动鞋>板鞋");put("t_sizeType","Men");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>运动鞋>棒球鞋");put("t_sizeType","Men");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>运动鞋>滑板鞋");put("t_sizeType","Men");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>运动鞋>球鞋");put("t_sizeType","Men");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>运动鞋>篮球鞋");put("t_sizeType","Men");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>运动鞋>篮球鞋>低帮鞋");put("t_sizeType","Men");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>运动鞋>篮球鞋>高帮鞋");put("t_sizeType","Men");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>运动鞋>网球鞋");put("t_sizeType","Men");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>运动鞋>舞蹈鞋");put("t_sizeType","Men");put("t_category","男鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>运动鞋>足球鞋");put("t_sizeType","Men");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>运动鞋>足球鞋>球鞋");put("t_sizeType","Men");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>运动鞋>跑鞋");put("t_sizeType","Men");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>运动鞋>跑鞋>健行鞋");put("t_sizeType","Men");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>运动鞋>跑鞋>训练鞋");put("t_sizeType","Men");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>运动鞋>跑鞋>跑鞋");put("t_sizeType","Men");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>运动鞋>跑鞋>钉鞋");put("t_sizeType","Men");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>运动鞋>运动休闲鞋");put("t_sizeType","Men");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>运动鞋>运动休闲鞋>休闲鞋");put("t_sizeType","Men");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>运动鞋>运动休闲鞋>凉鞋");put("t_sizeType","Men");put("t_category","男鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>运动鞋>运动休闲鞋>拖鞋");put("t_sizeType","Men");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>运动鞋>骑行鞋");put("t_sizeType","Men");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>运动鞋>骑行鞋>公路车锁鞋");put("t_sizeType","Men");put("t_category","自行车/骑行装备/零配件");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>运动鞋>骑行鞋>球鞋");put("t_sizeType","Men");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>运动鞋>骑行鞋>登山鞋");put("t_sizeType","Men");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>运动鞋>高尔夫球鞋");put("t_sizeType","Men");put("t_category","运动鞋new");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>靴子>工装鞋");put("t_sizeType","Men");put("t_category","流行男鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>靴子>拉链靴");put("t_sizeType","Men");put("t_category","流行男鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>靴子>摩托赛车靴");put("t_sizeType","Men");put("t_category","流行男鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>靴子>正装鞋");put("t_sizeType","Men");put("t_category","流行男鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>靴子>牛仔靴");put("t_sizeType","Men");put("t_category","流行男鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>靴子>系带");put("t_sizeType","Men");put("t_category","流行男鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>靴子>运动户外鞋");put("t_sizeType","Men");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>靴子>运动户外鞋>登山鞋");put("t_sizeType","Men");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>靴子>运动户外鞋>防寒靴子");put("t_sizeType","Men");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>靴子>运动户外鞋>雨靴");put("t_sizeType","Men");put("t_category","户外/登山/野营/旅行用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>男鞋>靴子>马靴");put("t_sizeType","Men");put("t_category","流行男鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>青少年鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>青少年鞋> 草编鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>青少年鞋> 草编鞋>草编罗马鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>青少年鞋> 草编鞋>露跟鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>青少年鞋>乐福鞋一脚蹬");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>青少年鞋>乐福鞋一脚蹬>乐福鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>青少年鞋>乐福鞋一脚蹬>便鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>青少年鞋>乐福鞋一脚蹬>孟克鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>青少年鞋>乐福鞋一脚蹬>帆船鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>青少年鞋>乐福鞋一脚蹬>驾车鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>青少年鞋>凉鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>青少年鞋>凉鞋>人字拖");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>青少年鞋>凉鞋>凉拖");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>青少年鞋>凉鞋>坡跟凉鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>青少年鞋>凉鞋>沙滩鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>青少年鞋>凉鞋>渔夫鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>青少年鞋>凉鞋>绑带");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>青少年鞋>凉鞋>防水台凉鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>青少年鞋>凉鞋>露跟鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>青少年鞋>平底鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>青少年鞋>平底鞋>拖鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>青少年鞋>平底鞋>沙滩鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>青少年鞋>平底鞋>玛丽珍鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>青少年鞋>平底鞋>芭蕾舞鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>青少年鞋>平底鞋>露跟鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>青少年鞋>拖鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>青少年鞋>拖鞋>家居鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>青少年鞋>拖鞋>拖鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>青少年鞋>拖鞋>短靴");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>青少年鞋>拖鞋>羊毛拖鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>青少年鞋>拖鞋>麂皮鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>青少年鞋>拖鞋木底鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>青少年鞋>拖鞋木底鞋>拖鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>青少年鞋>拖鞋木底鞋>木底鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>青少年鞋>时尚休闲鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>青少年鞋>时尚休闲鞋>一脚蹬");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>青少年鞋>时尚休闲鞋>坡跟鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>青少年鞋>时尚休闲鞋>拖鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>青少年鞋>时尚休闲鞋>玛丽珍鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>青少年鞋>时尚休闲鞋>系带");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>青少年鞋>时尚休闲鞋>针织鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>青少年鞋>时尚休闲鞋>露跟鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>青少年鞋>系带");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>青少年鞋>靴子");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>青少年鞋>靴子>坡跟靴");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>青少年鞋>靴子>套头靴");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>青少年鞋>靴子>平底靴");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>青少年鞋>靴子>摩托赛车靴");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>青少年鞋>靴子>牛仔靴");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>青少年鞋>靴子>系带靴子");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>青少年鞋>靴子>羊皮靴");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>青少年鞋>靴子>防水台靴子");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>青少年鞋>靴子>雨靴");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>青少年鞋>靴子>马靴");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>青少年鞋>高跟鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>青少年鞋>高跟鞋>压花");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>青少年鞋>高跟鞋>双色调");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>青少年鞋>高跟鞋>坡跟鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>青少年鞋>高跟鞋>多尔塞高跟鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>青少年鞋>高跟鞋>拖鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>青少年鞋>高跟鞋>沙滩鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>青少年鞋>高跟鞋>玛丽珍鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>青少年鞋>高跟鞋>绑带");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>青少年鞋>高跟鞋>防水台高跟鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>青少年鞋>高跟鞋>露跟鞋");put("t_category","童鞋/婴儿鞋/亲子鞋");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>鞋靴配件>包及双肩包");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>鞋靴配件>包及双肩包>双肩包");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>鞋靴配件>包及双肩包>挎包");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>鞋靴配件>包及双肩包>旅行包");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>鞋靴配件>包及双肩包>运动休闲包");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>鞋靴配件>皮带");put("t_category","服饰配件/皮带/帽子/围巾");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>鞋靴配件>袜子");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>鞋靴配件>袜子>女童袜");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>鞋靴配件>袜子>女童袜>婴幼儿女童袜连裤袜");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>鞋靴配件>袜子>女童袜>婴幼儿女童袜连裤袜>及膝袜子");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>鞋靴配件>袜子>女童袜>婴幼儿女童袜连裤袜>短筒袜");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>鞋靴配件>袜子>女童袜>婴幼儿女童袜连裤袜>袜子");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>鞋靴配件>袜子>女童袜>婴幼儿女童袜连裤袜>连裤袜");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>鞋靴配件>袜子>女袜");put("t_sizeType","Women");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>鞋靴配件>袜子>女袜>短袜");put("t_sizeType","Women");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>鞋靴配件>袜子>女袜>船袜");put("t_sizeType","Women");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>鞋靴配件>袜子>女袜>裤袜长筒袜");put("t_sizeType","Women");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>鞋靴配件>袜子>女袜>长筒袜");put("t_sizeType","Women");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>鞋靴配件>袜子>男童袜");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>鞋靴配件>袜子>男童袜>婴幼儿男童袜");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>鞋靴配件>袜子>男童袜>男童袜");put("t_category","童装/婴儿装/亲子装");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>鞋靴配件>袜子>男袜");put("t_sizeType","Men");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>鞋靴配件>袜子>男袜>正装袜");put("t_sizeType","Men");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>鞋靴配件>袜子>男袜>短筒袜");put("t_sizeType","Men");put("t_category","运动/瑜伽/健身/球迷用品");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>鞋靴配件>钱包卡包钥匙包");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>鞋靴配件>钱包卡包钥匙包>卡片夹");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>鞋靴配件>钱包卡包钥匙包>护照夹");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>鞋靴配件>钱包卡包钥匙包>支票簿");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>鞋靴配件>钱包卡包钥匙包>皮夹钱包");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>鞋靴配件>钱包卡包钥匙包>皮夹钱包>三层钱夹");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>鞋靴配件>钱包卡包钥匙包>皮夹钱包>名片夹");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>鞋靴配件>钱包卡包钥匙包>皮夹钱包>皮夹");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>鞋靴配件>钱包卡包钥匙包>皮夹钱包>钱夹");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>鞋靴配件>钱包卡包钥匙包>证件夹");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>鞋靴配件>钱包卡包钥匙包>钥匙扣钥匙链");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>鞋靴配件>钱包卡包钥匙包>钱包");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>鞋靴配件>钱包卡包钥匙包>钱夹");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>鞋靴配件>钱包卡包钥匙包>零钱包");put("t_category","箱包皮具/热销女包/男包");}});
        add(new HashMap<String, String>(){{put("t_mainCategory","鞋靴>鞋靴配件>雨伞");put("t_category","居家日用");}});
    }};

    /**
     * 天猫国际官网同购上新处理
     *
     * @param taskControlList taskcontrol信息
     */
    @Override
    public void onStartup(List<TaskControlBean> taskControlList) throws Exception {

//        // 清除缓存（这样在cms_mt_channel_config表中刚追加的价格计算公式等配置就能立刻生效了）
//        CacheHelper.delete(CacheKeyEnums.KeyEnum.ConfigData_CmsChannelConfigs.toString());

        // 线程数(默认为5)
        threadCount = NumberUtils.toInt(TaskControlUtils.getVal1WithDefVal(taskControlList, TaskControlEnums.Name.thread_count, "5"));
        // 抽出件数(默认为500)
        rowCount = NumberUtils.toInt(TaskControlUtils.getVal1WithDefVal(taskControlList, TaskControlEnums.Name.row_count, "500"));

        // 获取该任务可以运行的销售渠道
        List<String> channelIdList = TaskControlUtils.getVal1List(taskControlList, TaskControlEnums.Name.order_channel_id);

        // 初始化cms_mt_channel_condition_config表的条件表达式(避免多线程时2次初始化)
        channelConditionConfig = new HashMap<>();
        if (ListUtils.notNull(channelIdList)) {
            for (final String orderChannelID : channelIdList) {
                channelConditionConfig.put(orderChannelID, conditionPropValueRepo.getAllByChannelId(orderChannelID));
            }
        }

        // 循环所有销售渠道
        if (ListUtils.notNull(channelIdList)) {
            for (String channelId : channelIdList) {
                if (ChannelConfigEnums.Channel.USJGJ.getId().equals(channelId)) {
                    // 商品上传(USJOI天猫国际官网同购)
                    doProductUpload(channelId, CartEnums.Cart.USTT.getValue(), threadCount, rowCount);
                } else {
                    // 商品上传(天猫国际官网同购)
                    doProductUpload(channelId, CartEnums.Cart.TT.getValue(), threadCount, rowCount);
                }
            }
        }

        // 正常结束
        $info("天猫国际官网同购主线程正常结束");
    }

    /**
     * 平台产品上新主处理
     *
     * @param channelId String 渠道ID
     * @param cartId String 平台ID
     * @param threadCount int 线程数
     * @param rowCount int 每个渠道最大抽出件数
     */
    public void doProductUpload(String channelId, int cartId, int threadCount, int rowCount) throws Exception {

        // 默认线程池最大线程数
//        int threadPoolCnt = 5;

        // 获取店铺信息
        ShopBean shopProp = Shops.getShop(channelId, cartId);
        if (shopProp == null) {
            $error("获取到店铺信息失败(shopProp == null)! [ChannelId:%s] [CartId:%s]", channelId, cartId);
            return;
        }
        $info("获取店铺信息成功![ChannelId:%s] [CartId:%s]", channelId, cartId);

        // 从cms_bt_tm_tonggou_feed_attr表中取得该渠道，平台对应的天猫官网同购允许上传的feed attribute属性，如果为空则全部上传
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("channelId", channelId);
        paramMap.put("cartId", StringUtils.toString(cartId));
        List<CmsBtTmTonggouFeedAttrModel> tmTonggouFeedAttrModelList = cmsBtTmTonggouFeedAttrDao.selectList(paramMap);
        List<String> tmTonggouFeedAttrList = new ArrayList<>();
        if (ListUtils.notNull(tmTonggouFeedAttrModelList)) {
            // 如果表中有该渠道和平台对应的feed attribute属性，则将每个attribute加到列表中
            tmTonggouFeedAttrModelList.forEach(p -> tmTonggouFeedAttrList.add(p.getFeedAttr()));
        }

        // 从上新的任务表中获取该平台及渠道需要上新的任务列表(group by channel_id, cart_id, group_id)
        List<CmsBtSxWorkloadModel> sxWorkloadModels = platformProductUploadService.getSxWorkloadWithChannelIdCartId(
                rowCount, channelId, cartId);
        if (ListUtils.isNull(sxWorkloadModels)) {
            $error("上新任务表中没有该渠道和平台对应的任务列表信息！[ChannelId:%s] [CartId:%s]", channelId, cartId);
            return;
        }

        // 从cms_mt_channel_condition_mapping_config表中取得当前渠道的取得产品主类目与天猫平台叶子类目(或者平台一级类目)，以及feed类目id和天猫平台类目之间的mapping关系数据
        Map<String, Map<String, String>> categoryMappingMap = getCategoryMapping(channelId, cartId);

        // 创建线程池
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        // 根据上新任务列表中的groupid循环上新处理
        for (CmsBtSxWorkloadModel cmsBtSxWorkloadModel : sxWorkloadModels) {
            // 启动多线程
            executor.execute(() -> uploadProduct(cmsBtSxWorkloadModel, shopProp, tmTonggouFeedAttrList, categoryMappingMap));
        }
        // ExecutorService停止接受任何新的任务且等待已经提交的任务执行完成(已经提交的任务会分两类：一类是已经在执行的，另一类是还没有开始执行的)，
        // 当所有已经提交的任务执行完毕后将会关闭ExecutorService。
        executor.shutdown(); // 并不是终止线程的运行，而是禁止在这个Executor中添加新的任务
        try {
            // 阻塞，直到线程池里所有任务结束
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }

    /**
     * 平台产品上新处理
     *
     * @param cmsBtSxWorkloadModel CmsBtSxWorkloadModel WorkLoad信息
     * @param shopProp ShopBean 店铺信息
     * @param tmTonggouFeedAttrList List<String> 当前渠道和平台设置的可以天猫官网同购上传的feed attribute列表
     * @param categoryMappingMap 当前渠道和平台设置类目和天猫平台类目(叶子类目或平台一级类目)匹配信息map
     */
    public void uploadProduct(CmsBtSxWorkloadModel cmsBtSxWorkloadModel, ShopBean shopProp,
                              List<String> tmTonggouFeedAttrList, Map<String, Map<String, String>> categoryMappingMap) {

        // 当前groupid(用于取得产品信息)
        long groupId = cmsBtSxWorkloadModel.getGroupId();
        // 渠道id
        String channelId = shopProp.getOrder_channel_id();
        // 平台id
        int cartId = Integer.parseInt(shopProp.getCart_id());
        // 上新数据
        SxData sxData = null;
        // 商品id
        String numIId = "";
        // 新增或更新商品标志
        boolean updateWare = false;
        
        try {
            // 上新用的商品数据信息取得
            sxData = sxProductService.getSxProductDataByGroupId(channelId, groupId);
            if (sxData == null) {
                throw new BusinessException("取得上新用的商品数据信息失败！请向管理员确认 [sxData=null]");
            }
            // 如果取得上新对象商品信息出错时，报错
            if (!StringUtils.isEmpty(sxData.getErrorMessage())) {
                String errorMsg = sxData.getErrorMessage();
//                sxData.setErrorMessage(""); // 这里设为空之后，异常捕捉到之后msg前面会加上店铺名称
                // 取得上新数据出错时，cartId有可能没有设置
                sxData.setCartId(cartId);
                // 有错误的时候，直接报错
                throw new BusinessException(errorMsg);
            }

            // 主产品等列表取得
            CmsBtProductModel mainProduct = sxData.getMainProduct();
            List<CmsBtProductModel> cmsBtProductList = sxData.getProductList();
            List<BaseMongoMap<String, Object>> skuList = sxData.getSkuList();

            // 没有lock并且已Approved的产品列表为空的时候,中止该产品的上新流程
            if (ListUtils.isNull(cmsBtProductList)) {
                String errMsg = "未被锁定且已完成审批的产品列表为空";
                $error(errMsg);
                throw new BusinessException(errMsg);
            }

            // 主产品取得结果判断
            if (mainProduct == null) {
                String errMsg = "取得主商品信息失败 [mainProduct=null]";
                $error(errMsg);
                throw new BusinessException(errMsg);
            }

            // 如果产品没有common信息，数据异常不上新
            if (mainProduct.getCommon() == null || mainProduct.getCommon().getFields() == null) {
                String errMsg = "取得主商品common信息失败";
                $error(errMsg);
                throw new BusinessException(errMsg);
            }

            // 店铺级标题禁用词 20161216 tom START
            // 先临时这样处理
            String notAllowList = getConditionPropValue(sxData, "notAllowTitleList_30", shopProp);
            if (!StringUtils.isEmpty(notAllowList)) {
                if (!StringUtils.isEmpty(mainProduct.getCommon().getFields().getOriginalTitleCn())) {
                    String[] splitWord = notAllowList.split(",");
                    for (String notAllow : splitWord) {
                        if (mainProduct.getCommon().getFields().getOriginalTitleCn().contains(notAllow)) {
                            String errMsg = "标题中含有禁用词：【" + notAllow + "】， 禁止上新。";
                            $error(errMsg);
                            throw new BusinessException(errMsg);
                        }
                    }
                }
            }
            // 店铺级标题禁用词 20161216 tom END

            // 构造该产品所有SKUCODE的字符串列表
            List<String> strSkuCodeList = new ArrayList<>();
            skuList.forEach(sku -> strSkuCodeList.add(sku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.skuCode.name())));

            // 如果已Approved产品skuList为空，则中止该产品的上新流程
            // 如果已Approved产品skuList为空，则把库存表里面所有的数据（几万条）数据全部查出来了，很花时间
            if (ListUtils.isNull(strSkuCodeList)) {
                String errMsg = "已完成审批的产品sku列表为空";
                $error(errMsg);
                throw new BusinessException(errMsg);
            }

            // 从cms_mt_channel_config表中取得上新用价格配置项目名(例：31.sx_price对应的价格项目，有可能priceRetail, 有可能是priceMsrp)
            String priceConfigValue = getPriceConfigValue(sxData.getChannelId(), StringUtils.toString(cartId),CmsConstants.ChannelConfig.PRICE_SX_KEY,
                    CmsConstants.ChannelConfig.PRICE_SX_PRICE_CODE);
            if (StringUtils.isEmpty(priceConfigValue)) {
                String errMsg = String.format("从cms_mt_channel_config表中未能取得该店铺设置的上新用价格配置项目！ [config_key:%s]",
                        StringUtils.toString(cartId) + CmsConstants.ChannelConfig.PRICE_SX_PRICE_CODE);
                $error(errMsg);
                throw new BusinessException(errMsg);
            }

            // 如果skuList不为空，取得所有sku的库存信息
            // 为了对应MiniMall的场合， 获取库存的时候要求用getOrgChannelId()（其他的场合仍然是用channelId即可）
            Map<String, Integer> skuLogicQtyMap = productService.getLogicQty(mainProduct.getOrgChannelId(), strSkuCodeList);

            // 取得主产品天猫同购平台设置信息(包含SKU等信息)
            CmsBtProductModel_Platform_Cart mainProductPlatformCart = mainProduct.getPlatform(cartId);
            if (mainProductPlatformCart == null) {
                $error(String.format("获取主产品天猫同购平台设置信息(包含SKU，Schema属性值等信息)失败！[ProductCode:%s][CartId:%s]",
                        mainProduct.getCommon().getFields().getCode(), cartId));
                throw new BusinessException("获取主产品天猫同购平台设置信息(包含SKU，Schema属性值等信息)失败");
            }

            // 获取字典表cms_mt_platform_dict(根据channel_id)上传图片的规格等信息
            List<CmsMtPlatformDictModel> cmsMtPlatformDictModelList = dictService.getModesByChannelCartId(channelId, cartId);
            if (cmsMtPlatformDictModelList == null || cmsMtPlatformDictModelList.size() == 0) {
                $error(String.format("获取cms_mt_platform_dict字典表数据（属性图片模板等）失败 [ChannelId:%s] [CartId:%s]", channelId, cartId));
                throw new BusinessException("获取cms_mt_platform_dict字典表数据（属性图片模板等）失败");
            }

            // 判断新增商品还是更新商品
            // 只要numIId不为空，则为更新商品
            if (!StringUtils.isEmpty(sxData.getPlatform().getNumIId())) {
                // 更新商品
                updateWare = true;
                // 取得更新对象商品id
                numIId = sxData.getPlatform().getNumIId();
            }

            // 编辑天猫国际官网同购共通属性
            BaseMongoMap<String, String> productInfoMap = getProductInfo(sxData, shopProp, priceConfigValue,
                    skuLogicQtyMap, tmTonggouFeedAttrList, categoryMappingMap, updateWare);

            // 构造Field列表
            List<Field> itemFieldList = new ArrayList<>();
            productInfoMap.entrySet().forEach(p -> {
                InputField inputField = new InputField();
                inputField.setId(p.getKey());
                inputField.setValue(p.getValue());
//                inputField.setType(FieldTypeEnum.INPUT);
                itemFieldList.add(inputField);
            });

            // 转换成XML格式(root元素为<itemRule>)
            String productInfoXml = "";
            if (ListUtils.notNull(itemFieldList))
                productInfoXml = SchemaWriter.writeParamXmlString(itemFieldList);

            // 测试用输入XML内容
            $debug(productInfoXml);

            String result;
            // 新增或更新商品主处理
            if (!updateWare) {
                // 新增商品的时候
                result = tbSimpleItemService.addSimpleItem(shopProp, productInfoXml);
            } else {
                // 更新商品的时候
                result = tbSimpleItemService.updateSimpleItem(shopProp, NumberUtils.toLong(numIId), productInfoXml);
            }

            // sku模式 or product模式（默认s模式， 如果没有颜色的话， 就是p模式）
            sxData.setHasSku(true);
            if ("ERROR:15:isv.invalid-parameter::该类目没有颜色销售属性,不能上传图片".equals(result)) {
                // 用simple的那个sku， 覆盖到原来的那个sku上
                int idxOrg = -1;
                String strSimpleSkuValue = null;
                for (int i = 0; i < itemFieldList.size(); i++) {
                    Field field = itemFieldList.get(i);
                    if (field.getId().equals("skus_simple")) {
                        InputField inputField = (InputField) field;
                        if (!StringUtils.isEmpty(inputField.getValue())) {
                            strSimpleSkuValue = inputField.getValue();
                        }
                    } else if (field.getId().equals("skus")) {
                        idxOrg = i;
                    }
                }
                if (!StringUtils.isEmpty(strSimpleSkuValue) && idxOrg != -1) {
                    // 找到如果设置过值的话， 再给一次机会尝试一下
                    // 如果没设置过值的话， 就说明前面已经判断过， 这个商品有多个sku， 没办法使用这种方式
                    ((InputField)itemFieldList.get(idxOrg)).setValue(strSimpleSkuValue);

                    productInfoXml = SchemaWriter.writeParamXmlString(itemFieldList);

                    // 换为p(roduct)模式
                    sxData.setHasSku(false);
                    if (!updateWare) {
                        // 新增商品的时候
                        result = tbSimpleItemService.addSimpleItem(shopProp, productInfoXml);
                    } else {
                        // 更新商品的时候
                        result = tbSimpleItemService.updateSimpleItem(shopProp, NumberUtils.toLong(numIId), productInfoXml);
                    }
                }
            }

            if (!StringUtils.isEmpty(result) && result.startsWith("ERROR:")) {
                // 天猫官网同购新增/更新商品失败时
                String errMsg = "天猫官网同购新增商品时出现错误! ";
                if (updateWare) {
                    errMsg = "天猫官网同购更新商品时出现错误! ";
                }
                errMsg += result;
                $error(errMsg);
                throw new BusinessException(errMsg);
            } else {
                // 天猫官网同购新增/更新商品成功时
                if (!updateWare) numIId = result;
            }

            {
                // 获取skuId
                List<Map<String, Object>> skuMapList = null;
                TbItemSchema tbItemSchema = tbSimpleItemService.getSimpleItem(shopProp, Long.parseLong(numIId));
                if (tbItemSchema != null) {
                    Map<String, Field> mapField = tbItemSchema.getFieldMap();
                    if (mapField != null) {
                        if (mapField.containsKey("skus")) {
                            Field fieldSkus = mapField.get("skus");
                            if (fieldSkus != null) {
                                skuMapList = JacksonUtil.jsonToMapList(((InputField)tbItemSchema.getFieldMap().get("skus")).getDefaultValue());
                            }
                        }
                    }
                }

                // 关联货品
                if (skuMapList != null) {
                    for (Map<String, Object> skuMap : skuMapList) {
//                        skuMap: outer_id, price, quantity, sku_id

                        skuMap.put("scProductId",
                                taobaoScItemService.doSetLikingScItem(
                                    shopProp, sxData.getMainProduct().getOrgChannelId(),
                                    Long.parseLong(numIId),
                                    productInfoMap.get("title"), skuMap));
                    }
                }


                // 回写数据库
                // TODO: 目前这个channelId传入的是原始channelId， 2017年4月份左右新wms上新前， 要改为928自己的channelId
//                saveCmsBtTmScItem_Liking(channelId, cartId, skuMapList);
                saveCmsBtTmScItem_Liking(sxData.getMainProduct().getOrgChannelId(), cartId, skuMapList);
            }

            // 调用淘宝商品上下架操作(新增的时候默认为下架，只有更新的时候才根据group里面platformActive调用上下架操作)
            // 回写用商品上下架状态(OnSale/InStock)
            CmsConstants.PlatformStatus platformStatus = null;
            CmsConstants.PlatformActive platformActive = sxData.getPlatform().getPlatformActive();
            // 更新商品并且PlatformActive=ToOnSale时,执行商品上架；新增商品或PlatformActive=ToInStock时，执行下架功能
            if (updateWare && platformActive == CmsConstants.PlatformActive.ToOnSale) {
                platformStatus = CmsConstants.PlatformStatus.OnSale;   // 上架
            } else {
                platformStatus = CmsConstants.PlatformStatus.InStock;   // 在库
            }

            // 更新特价宝
            sxData.getPlatform().setNumIId(numIId);
            updateTeJiaBaoPromotion(sxData);

            // 回写PXX.pCatId, PXX.pCatPath等信息
            Map<String, String> pCatInfoMap = getSimpleItemCatInfo(shopProp, numIId);
            if (pCatInfoMap != null && pCatInfoMap.size() > 0) {
                // 上新成功且成功取得平台类目信息时状态回写操作(默认为在库)
                sxProductService.doUploadFinalProc(shopProp, true, sxData, cmsBtSxWorkloadModel, numIId,
                        platformStatus, "", getTaskName(), pCatInfoMap);
            } else {
                // 上新成功时但未取得平台类目信息状态回写操作(默认为在库)
                sxProductService.doUploadFinalProc(shopProp, true, sxData, cmsBtSxWorkloadModel, numIId,
                        platformStatus, "", getTaskName());
            }

            // added by morse.lu 2016/12/08 start
            if (ChannelConfigEnums.Channel.SN.equals(channelId)) {
                // Sneakerhead
                try {
                    sxProductService.uploadCnInfo(sxData);
                } catch (IOException io) {
                    throw new BusinessException("上新成功!但在推送给美国数据库时发生异常!"+ io.getMessage());
                }
            }
            // added by morse.lu 2016/12/08 end

            // 正常结束
            $info(String.format("天猫官网同购商品上新成功！[ChannelId:%s] [CartId:%s] [GroupId:%s] [NumIId:%s]",
                    channelId, cartId, groupId, numIId));
        } catch (Exception ex) {
            // 异常结束时
            String errMsg = String.format(" 天猫官网同购上新异常结束！[ChannelId:%s] [CartId:%s] [GroupId:%s] [NumIId:%s]",
                    channelId, cartId, groupId, numIId);
            $error(errMsg);

            if (sxData == null) {
                // 回写详细错误信息表(cms_bt_business_log)用
                sxData = new SxData();
                sxData.setChannelId(channelId);
                sxData.setCartId(cartId);
                sxData.setGroupId(groupId);
                sxData.setErrorMessage(shopProp.getShop_name() + " 天猫同购取得上新用的商品数据信息异常,请跟管理员联系! [上新数据为null]");
            }
            // 如果上新数据中的errorMessage为空
            if (StringUtils.isEmpty(sxData.getErrorMessage())) {
                // nullpoint错误的处理
                if(StringUtils.isNullOrBlank2(ex.getMessage())) {
                    ex.printStackTrace();
                    sxData.setErrorMessage(shopProp.getShop_name() + " 天猫同购上新时出现不可预知的错误，请跟管理员联系! "
                            + ex.getStackTrace()[0].toString());
                } else {
                    sxData.setErrorMessage(shopProp.getShop_name() + " " +ex.getMessage());
                }
            }

            // 上新出错时状态回写操作
            sxProductService.doUploadFinalProc(shopProp, false, sxData, cmsBtSxWorkloadModel, "", null, "", getTaskName());
        }
    }

    // 注意： 本函数Liking专用（code无所谓， 随便瞎填的）
    private void saveCmsBtTmScItem_Liking(String channelId, int cartId, List<Map<String, Object>> skuMapList) {
        for(Map<String, Object> skuMap : skuMapList) {
            String code = "I_LIKING_IT";
            String skuCode = String.valueOf(skuMap.get("outer_id"));
            Map<String, Object> searchParam = new HashMap<>();
            searchParam.put("channelId", channelId);
            searchParam.put("cartId", cartId);
            searchParam.put("code", code);
            searchParam.put("sku", skuCode);
            CmsBtTmScItemModel scItemModel = cmsBtTmScItemDao.selectOne(searchParam);

            String scProductId = String.valueOf(skuMap.get("scProductId"));
            if (StringUtils.isEmpty(scProductId)) {
                // delete
                if (scItemModel != null) {
                    cmsBtTmScItemDao.delete(scItemModel.getId());
                }
            } else {
                if (scItemModel == null) {
                    // add
                    scItemModel = new CmsBtTmScItemModel();
                    scItemModel.setChannelId(channelId);
                    scItemModel.setCartId(cartId);
                    scItemModel.setCode(code);
                    scItemModel.setSku(skuCode);
                    scItemModel.setScProductId(scProductId);
                    scItemModel.setCreater(getTaskName());
                    cmsBtTmScItemDao.insert(scItemModel);
                } else {
                    // update
                    if (!scProductId.equals(scItemModel.getScProductId())) {
                        scItemModel.setScProductId(scProductId);
                        scItemModel.setModifier(getTaskName());
                        scItemModel.setModified(DateTimeUtil.getDate());
                        cmsBtTmScItemDao.update(scItemModel);
                    }
                }
            }
        }
    }

    /**
     * 设置天猫同购上新产品用共通属性
     *
     * @param sxData sxData 上新产品对象
     * @param shopProp ShopBean  店铺信息
     * @param priceConfigValue String 该店铺上新用项目名
     * @param skuLogicQtyMap Map<String, Integer>  SKU逻辑库存
     * @param tmTonggouFeedAttrList List<String> 当前渠道和平台设置的可以天猫官网同购上传的feed attribute列表
     * @param categoryMappingMap 当前渠道和平台设置类目和天猫平台类目(叶子类目或平台一级类目)匹配信息map
     * @param updateWare 新增/更新flg(false:新增 true:更新)
     * @return JdProductBean 京东上新用bean
     * @throws BusinessException
     */
    private BaseMongoMap<String, String> getProductInfo(SxData sxData, ShopBean shopProp, String priceConfigValue,
                                                 Map<String, Integer> skuLogicQtyMap, List<String> tmTonggouFeedAttrList,
                                                 Map<String, Map<String, String>> categoryMappingMap,
                                                 boolean updateWare) throws BusinessException {
        // 上新产品信息保存map
        BaseMongoMap<String, String> productInfoMap = new BaseMongoMap<>();

        CmsBtProductModel mainProduct = sxData.getMainProduct();
        CmsBtFeedInfoModel feedInfo = sxData.getCmsBtFeedInfoModel();
        List<CmsBtProductModel> productList = sxData.getProductList();
        List<BaseMongoMap<String, Object>> skuList = sxData.getSkuList();
        ExpressionParser expressionParser = new ExpressionParser(sxProductService, sxData);

        // 天猫国际官网同购平台（或USJOI天猫国际官网同购平台）
        CmsBtProductModel_Platform_Cart mainProductPlatformCart = mainProduct.getPlatform(sxData.getCartId());

        // 标题(必填)
        // 商品标题支持英文到中文，韩文到中文的自动翻译，可以在extends字段里面进行设置是否需要翻译
        // 注意：使用测试账号的APPKEY测试时，标题应包含中文"测试请不要拍"
        String valTitle = "";
        if (mainProductPlatformCart != null && mainProductPlatformCart.getFields() != null
                && !StringUtils.isEmpty(mainProductPlatformCart.getFields().getStringAttribute("title"))) {
            // 画面上输入的platform的fields中的标题 (格式：<value>测试请不要拍 title</value>)
            valTitle = mainProductPlatformCart.getFields().getStringAttribute("title");
        } else if (!StringUtils.isEmpty(mainProduct.getCommon().getFields().getStringAttribute("originalTitleCn"))) {
            // common中文长标题
            valTitle = mainProduct.getCommon().getFields().getStringAttribute("originalTitleCn");
        } else if (!StringUtils.isEmpty(mainProduct.getCommon().getFields().getStringAttribute("productNameEn"))) {
            // common英文长标题
            valTitle = mainProduct.getCommon().getFields().getStringAttribute("productNameEn");
        }
//        productInfoMap.put("title", "测试请不要拍 " + valTitle);
        productInfoMap.put("title", valTitle);

        // 子标题(卖点)(非必填)
        String valSubTitle = "";
        if (mainProductPlatformCart != null && mainProductPlatformCart.getFields() != null
                && !StringUtils.isEmpty(mainProductPlatformCart.getFields().getStringAttribute("sub_title"))) {
            // 画面上输入的platform的fields中的子标题(卖点)
            valSubTitle = mainProductPlatformCart.getFields().getStringAttribute("sub_title");
        }
        productInfoMap.put("sub_title", valSubTitle);

        // 类目(必填)
        // 注意：使用天猫授权类目ID发布时，必须使用叶子类目的ID
        // 注意：使用商家自有系统类目路径发布时，不同层级的类目，使用&gt;进行分隔；使用系统匹配时，会有一定的badcase,
        //      商品上架前，建议商家做自查，查看商品是否被匹配到了正确的类目
        String valCategory = "";
        if (mainProductPlatformCart != null
                && !StringUtils.isEmpty(mainProductPlatformCart.getpCatId())) {
            // 画面上输入的platform获得授权的叶子类目ID (格式：<value>{"cat_id":"50012036"}</value>)
            Map<String, Object> paramCategory = new HashMap<>();
            paramCategory.put("cat_id", mainProductPlatformCart.getpCatId());
            valCategory = JacksonUtil.bean2Json(paramCategory);
        } else if (feedInfo != null && !StringUtils.isEmpty(feedInfo.getCategory())) {
            // 使用商家自有系统类目路径
            // feed_info表的category（将中杠【-】替换为：【&gt;】(>)） (格式：<value>man&gt;sports&gt;socks</value>)

            // TODO 测试代码, 这里需要改进
//            valCategory = feedInfo.getCategory().replaceAll("-", "&gt;");
//            valCategory = "居家布艺";


            if (ChannelConfigEnums.Channel.LUCKY_VITAMIN.getId().equals(sxData.getChannelId())) {
                // LuckyVitamin默认固定
                Map<String, Object> paramCategory = new HashMap<>();
                paramCategory.put("cat_id", "50050237"); // 保健食品/膳食营养补充食品>海外膳食营养补充食品>其他膳食营养补充食品>其他膳食营养补充剂
                valCategory = JacksonUtil.bean2Json(paramCategory);
            } else {
                // 主产品主类目path
                String mainCatPath = mainProduct.getCommonNotNull().getCatPath();
                // 类目匹配优先顺序："主类目到天猫叶子类目" > "主类目到天猫一级类目" > "feed类目到天猫一级类目"
                if (!StringUtils.isEmpty(mainCatPath) && categoryMappingMap != null ) {
                    // 如果设置过主类目，并且cms_mt_channel_condition_mapping_config表中配置过主类目到天猫叶子类目的匹配关系
                    if (categoryMappingMap.containsKey(TtPropName.tt_main_category_leaf.name())
                            && categoryMappingMap.get(TtPropName.tt_main_category_leaf.name()) != null
                            && categoryMappingMap.get(TtPropName.tt_main_category_leaf.name()).containsKey(mainCatPath)) {
                        // 直接匹配到天猫叶子类目id(叶子类目需要配置到id)
                        Map<String, Object> paramCategory = new HashMap<>();
                        paramCategory.put("cat_id", categoryMappingMap.get(TtPropName.tt_main_category_leaf.name()).get(mainCatPath));
                        valCategory = JacksonUtil.bean2Json(paramCategory);
                    }
                }

                // 如果没有匹配到主类目对应的天猫叶子类目，则匹配主类目到天猫一级类目
                if (StringUtils.isEmpty(valCategory) && !StringUtils.isEmpty(mainCatPath)) {
                    String brand = mainProduct.getCommonNotNull().getFieldsNotNull().getBrand();
                    String sizeType = mainProduct.getCommonNotNull().getFieldsNotNull().getSizeType();
                    // 匹配优先顺序：
                    // 1.主类目+品牌+适用人群
                    // 2.主类目+品牌
                    // 3.主类目+适用人群
                    // 4.主类目
                    valCategory = getMainCategoryMappingInfo(mainCatPath, brand, sizeType);
                    if (StringUtils.isEmpty(valCategory)) {
                        valCategory = getMainCategoryMappingInfo(mainCatPath, brand, null);
                    }
                    if (StringUtils.isEmpty(valCategory)) {
                        valCategory = getMainCategoryMappingInfo(mainCatPath, null, sizeType);
                    }
                    if (StringUtils.isEmpty(valCategory)) {
                        valCategory = getMainCategoryMappingInfo(mainCatPath, null, null);
                    }
                }

                // 如果根据主类目没有匹配到平台类目的时候，再根据feed类目来匹配
                if (StringUtils.isEmpty(valCategory)) {
                    // 普通的获取类目的方式(只能匹配到cms_mt_channel_condition_mapping_config表中配置过配置过天猫一级类目)
                    String feedCategory = getValueFromPageOrCondition("tmall_category_key", "", mainProductPlatformCart, sxData, shopProp);
                    if (!StringUtils.isEmpty(feedCategory)) {
                        if (categoryMappingMap != null
                                && categoryMappingMap.containsKey(TtPropName.tt_category.name())
                                && categoryMappingMap.get(TtPropName.tt_category.name()) != null
                                && categoryMappingMap.get(TtPropName.tt_category.name()).containsKey(feedCategory)) {
                            // 匹配feed类目到天猫一级类目
                            valCategory = categoryMappingMap.get(TtPropName.tt_category.name()).get(feedCategory);
                        } else {
                            String errMsg = String.format("从cms_mt_channel_condition_mapping_config表中没有取到客户feed类目对应的天猫" +
                                    "平台一级类目信息，中止上新！[feedCategory:%s]", feedCategory);
                            $error(errMsg);
                            throw new BusinessException(errMsg);
                        }
                    } else {
                        String errMsg = String.format("没有取到cms_mt_channel_condition_config表中\"tmall_category_key\"配置的" +
                                "类目对象项目对应的feed类目值,不能根据feed类目取得对应的天猫平台一级类目信息，中止上新！");
                        $error(errMsg);
                        throw new BusinessException(errMsg);
                    }
                }
            }

        }

        // 防止母婴类目 START
        if (valTitle.contains("孕妇")
                || valTitle.contains("婴幼儿")
                || valTitle.contains("儿童")
                || valTitle.contains("产前")
                || valTitle.contains("婴儿")
                || valTitle.contains("幼儿")
                || valTitle.contains("孩子")
                || valTitle.contains("宝宝")
                || valTitle.contains("母婴")
                ) {

            if (mainProductPlatformCart == null
                    || StringUtils.isEmpty(mainProductPlatformCart.getpCatPath())
                    || (!mainProductPlatformCart.getpCatPath().startsWith("孕妇装/孕产妇用品/营养>"))
                    ) {
                Map<String, Object> paramCategory = new HashMap<>();
                paramCategory.put("cat_id", "50026470"); // 孕妇装/孕产妇用品/营养>孕产妇营养品>其它
                valCategory = JacksonUtil.bean2Json(paramCategory);
            }
        }
        // 防止母婴类目 END

        productInfoMap.put("category", valCategory);

        // 商品属性(非必填)
        // 商品的一些属性，可以通过property字段来进行填写，该字段为非必填字段，如果商家有对商品的更为具体的属性信息，
        // 可以都进行填写。格式为"Key":"Value"
        // 注意：所有的属性字段，天猫国际系统会进行自动匹配，如果是天猫对应的该类目的标准属性，则会显示在商品的detail页面
        String valProperty = "";
        if (feedInfo != null && feedInfo.getAttribute() != null && feedInfo.getAttribute().size() > 0) {
            // 画面上输入的platform获得授权的叶子类目ID (格式：<value>{"material":"cotton","gender":"men","color":"grey"}</value>)
            Map<String, Object> paramProperty = new HashMap<>();

            Map<String, List<String>> feedAttribute = feedInfo.getAttribute();
            // 误 -> 如果cms_bt_tm_tonggou_feed_attr表中没有配置当前渠道和平台可以上传的feed attribute属性，
            // 误 -> 则认为可以上传全部feed attribute属性
            // 没有配置就不要设置
            if (ListUtils.isNull(tmTonggouFeedAttrList)) {
//                feedAttribute.entrySet().forEach(p -> {
//                    List<String> attrValueList = p.getValue();
//                    // feed.Attrivute里面的value是一个List，有多个值，用逗号分隔
//                    String value = Joiner.on(Separtor_Coma).join(attrValueList);
//                    paramProperty.put(p.getKey(), value);
//                });
            } else {
                // 如果cms_bt_tm_tonggou_feed_attr表中配置了当前渠道和平台可以上传的feed attribute属性，
                // 则只上传表中配置过的feed attribute属性
                feedAttribute.entrySet().forEach(p -> {
                    // 只追加在表中配置过的eed attribute属性
                    if (tmTonggouFeedAttrList.contains(p.getKey())) {
                        List<String> attrValueList = p.getValue();
                        // feed.Attrivute里面的value是一个List，有多个值，用逗号分隔
                        String value = Joiner.on(Separtor_Coma).join(attrValueList);
                        paramProperty.put(p.getKey(), value);
                    } else {
                        $debug("在cms_bt_tm_tonggou_feed_attr表中没有配置了该feed attribute属性，不上传该feed属性. " +
                                "[feed attribute:" + p.getKey() + "]");
                    }
                });
            }

            valProperty = JacksonUtil.bean2Json(paramProperty);
        }
        productInfoMap.put("property", valProperty);

        // 品牌(必填)
        // 商品品牌的值只支持英文，中文
        // 注意：天猫国际系统会进行品牌的匹配，部分品牌因在天猫品牌库中不存在，因为不一定全部品牌都能成功匹配。
        //      匹配成功的品牌，会出现在商品详情页面;
        //      如果无法匹配，且商家希望显示品牌的，建议商家通过商家后台申请新品牌。
        String valBrand = "";
        // 优先使用cms_mt_brands_mapping表中的匹配过的天猫平台上已有的品牌id(格式:<value>"{\"brand_id\":\"93764828\"}"</value>)
        if (!StringUtils.isEmpty(sxData.getBrandCode())) {
            valBrand = "{\"brand_id\":\"" + sxData.getBrandCode() + "\"}";
        } else {
            // 如果没找到匹配过的天猫平台品牌id，就用feed中带过来的brand，让平台自己去匹配
            if (mainProduct.getCommon() != null && mainProduct.getCommon().getFields() != null
                    && !StringUtils.isEmpty(mainProduct.getCommon().getFields().getStringAttribute("brand"))) {
                // common中的品牌 (格式：<value>nike</value>)
                valBrand = mainProduct.getCommon().getFields().getStringAttribute("brand");
            }
        }
        productInfoMap.put("brand", valBrand);

        // 为什么要这段内容呢， 因为发生了一件很奇怪的事情， 曾经上新成功的商品， 更新的时候提示说【id:xxx还没有成为品牌】
        // 所以使用之前上过的品牌
        // TODO:目前好像只有024这家店有这个问题， 明天再查一下
        if (sxData.getChannelId().equals("024")) {
            // 如果已经上新过了的话， 使用曾经上新过的品牌
            if (!StringUtils.isEmpty(sxData.getPlatform().getNumIId())) {
                String numIId = sxData.getPlatform().getNumIId();
                // 取得更新对象商品id
                TbItemSchema tbItemSchema = null;
                try {
                    tbItemSchema = tbSimpleItemService.getSimpleItem(shopProp, NumberUtils.toLong(numIId));
                    if (tbItemSchema != null && !ListUtils.isNull(tbItemSchema.getFields())) {
                        InputField inputFieldBrand = (InputField)tbItemSchema.getFieldMap().get("brand");
                        if (!StringUtils.isEmpty(inputFieldBrand.getDefaultValue())) {
                            productInfoMap.put("brand", inputFieldBrand.getDefaultValue());
                        }
                    }
                } catch (Exception e) {
                }

            }
        }

        // 主图(必填)
        // 最少1张，最多5张。多张图片之间，使用英文的逗号进行分割。需要使用alicdn的图片地址。建议尺寸为800*800像素。
        // 格式：<value>http://img.alicdn.com/imgextra/i1/2640015666/TB2PTFYkXXXXXaUXpXXXXXXXXXX_!!2640015666.jpg,
        //      http://img.alicdn.com/imgextra/~~</value>
        String valMainImages = "";
        // 解析cms_mt_platform_dict表中的数据字典
        String mainPicUrls = getValueByDict("天猫同购商品主图5张", expressionParser, shopProp);
        if (!StringUtils.isNullOrBlank2(mainPicUrls)) {
            // 去掉末尾的逗号
            valMainImages = mainPicUrls.substring(0, mainPicUrls.lastIndexOf(Separtor_Coma));
        }
        productInfoMap.put("main_images", valMainImages);

        // 描述(必填)
        // 商品描述支持HTML格式，但是需要将内容变成XML格式。
        // 为了更好的用户体验，建议全部使用图片来做描述内容。描述的图片宽度不超过800像素.
        // 格式：<value>&lt;img align="middle" src="http://img.alicdn.com/imgextra/i1/2640015666/TB2islBkXXXXXXBXFXXXXXXXXXX_!!2640015666.jpg"
        //      /&gt; &lt;br&gt;&lt;img align="middle" src="http://img.alicdn.com/imgextra/i1/2640015666/~~</value>
        // 解析cms_mt_platform_dict表中的数据字典
        // modified by morse.lu 2016/12/23 start
        // 画面上可以选
//        String valDescription = getValueByDict("天猫同购描述", expressionParser, shopProp);
        String valDescription;
        RuleExpression ruleDetails = new RuleExpression();
        MasterWord masterWord = new MasterWord("details");
        ruleDetails.addRuleWord(masterWord);
        String details = null;
        try {
            details = expressionParser.parse(ruleDetails, shopProp, getTaskName(), null);
        } catch (Exception e) {
        }
        if (!StringUtils.isEmpty(details)) {
            valDescription = getValueByDict(details, expressionParser, shopProp);
        } else {
            valDescription = getValueByDict("天猫同购描述", expressionParser, shopProp);
        }
        // modified by morse.lu 2016/12/23 end
        productInfoMap.put("description", valDescription);

        // 物流信息(必填)
        // 物流字段：weight值为重量，用于计算运费，单位是千克;
        //         volumn值为体积，由于运费都是基于重量，这个值可以随便填写;
        //         templete_id为物流模板;
        //         province&city值，非港澳台的地区，直接填写中文的国家即可;
        //         start_from为货源地;
        // 格式:<value>{"weight":"1.5","volume":"0.0001","template_id":"243170100","province":"美国","city":"美国"}</value>
        Map<String, Object> paramLogistics = new HashMap<>();
        // 物流重量
        paramLogistics.put("weight", getValueFromPageOrCondition("logistics_weight", "1", mainProductPlatformCart, sxData, shopProp));
        // 物流体积
        paramLogistics.put("volume", getValueFromPageOrCondition("logistics_volume", "", mainProductPlatformCart, sxData, shopProp));
        // 物流模板ID
        paramLogistics.put("template_id", getValueFromPageOrCondition("logistics_template_id", "", mainProductPlatformCart, sxData, shopProp));
        // 物流模板是否是包邮模板(只能整个店铺共通设置一个，画面上不需要,不用加在共通schema里面)
        String shipFlag = getConditionPropValue(sxData, "logistics_template_ship_flag", shopProp);
        if ("true".equalsIgnoreCase(shipFlag)) {
            // 是否包邮设置为包邮("2":包邮)
            // 参加活动天猫给付费广告位的话，还需要商品收藏数达到一定量才可以.
            paramLogistics.put("ship", "2");
        }
        // 省(国家)
        paramLogistics.put("province", getValueFromPageOrCondition("logistics_province", "", mainProductPlatformCart, sxData, shopProp));
        // 城市
        paramLogistics.put("city", getValueFromPageOrCondition("logistics_city", "", mainProductPlatformCart, sxData, shopProp));
        // 货源地
        paramLogistics.put("start_from", getValueFromPageOrCondition("logistics_start_from", "", mainProductPlatformCart, sxData, shopProp));

        productInfoMap.put("logistics", JacksonUtil.bean2Json(paramLogistics));

        // skus(必填)
        // cms_mt_channel_condition_config表中入关方式(cross_border_report)
        // 设置成true跨境申报后，商品的每个SKU必须带有HSCODE才可以上架，
        // 如果设置成false邮关申报后，商品不需要设置HSCODE
        String crossBorderRreportFlg = getValueFromPageOrCondition("extends_cross_border_report", "", mainProductPlatformCart, sxData, shopProp);
        // 取得天猫同购上新用skus列表
        List<BaseMongoMap<String, Object>> targetSkuList_0 = getSkus(0, sxData.getCartId(), productList, skuList,
                priceConfigValue, skuLogicQtyMap, expressionParser, shopProp, crossBorderRreportFlg);

        productInfoMap.put("skus", JacksonUtil.bean2Json(targetSkuList_0));
        if (skuList.size() == 1) {
            // 只有一个sku的场合， 万一天猫自动匹配的类目只允许一个sku的时候， 可以用上
            List<BaseMongoMap<String, Object>> targetSkuList_1 = getSkus(1, sxData.getCartId(), productList, skuList,
                    priceConfigValue, skuLogicQtyMap, expressionParser, shopProp, crossBorderRreportFlg);
            productInfoMap.put("skus_simple", JacksonUtil.bean2Json(targetSkuList_1));
        } else {
            // 多个sku的场合， 万一天猫自动匹配的类目只允许一个sku的时候， 就上新不了了
            productInfoMap.put("skus_simple", null);
        }

        // 扩展(部分必填)
        // 该字段主要控制商品的部分备注信息等，其中部分字段是必须填写的，非必填的字段部分可以完全不用填写。
        // 且其中的部分字段，可以做好统一配置，做好配置的，不需要每个商品发布时都提交.
        Map<String, Object> paramExtends = new HashMap<>();
        // 官网来源代码(必填)   商品详情中会显示来源的国家旗帜
        // 主要国家代码：美国/US 英国/UK 澳大利亚/AU 加拿大/CA 德国/DE 西班牙/ES 法国/FR 香港/HK 意大利/IT 日本/JP
        //             韩国/KR 荷兰/NL 新西兰/NZ 台湾/TW 新加坡/SG
        paramExtends.put("nationality", getValueFromPageOrCondition("extends_nationality", "", mainProductPlatformCart, sxData, shopProp));
        // 币种代码(必填)      用于区分币种，选择后会根据price值和币种，根据支付宝的汇率计算人民币价格
        // 主要币种代码：人民币/CNY 港币/HKD 新台币/TWD 美元/USD 英镑/GBP 日元/JPY 韩元/KRW 欧元/EUR 加拿大元/CAD
        //             澳元/AUD 新西兰元/NZD
        paramExtends.put("currency_type", getValueFromPageOrCondition("extends_currency_type", "", mainProductPlatformCart, sxData, shopProp));
        // 是否需要自动翻译(必填)  (如果有配置优先使用配置项目，没有配置的时候如果标题是中文，那么就是false，否则就是true)
        String extends_translate = "";
        // 优先使用画面选择的是否自动翻译，再解析cms_mt_channel_condition_config表中的数据字典取得"项目名_XX"(XX为cartId)对应的值
        extends_translate = getValueFromPageOrCondition("extends_translate", "", mainProductPlatformCart, sxData, shopProp);
        if (!"true".equalsIgnoreCase(extends_translate) && !"false".equalsIgnoreCase(extends_translate)) {
            // cms_mt_channel_condition_config表中配置的"extends_translate"的值不是"true"或"false"时
            if (mainProductPlatformCart != null && mainProductPlatformCart.getFields() != null
                    && !StringUtils.isEmpty(mainProductPlatformCart.getFields().getStringAttribute("title"))) {
                extends_translate = "false";
            } else if (!StringUtils.isEmpty(mainProduct.getCommon().getFields().getStringAttribute("originalTitleCn"))) {
                extends_translate = "false";
            } else if (StringUtils.isEmpty(StringUtils.toString(productInfoMap.get("title")))) {
                extends_translate = "false";
            } else {
                extends_translate = "true";
            }
        }
        paramExtends.put("translate", extends_translate);
        // 商品原始语言(必填)
        // 主要语言代码为：中文/zh 中文繁体/zt 英文/en 韩文/ko
        paramExtends.put("source_language", getValueFromPageOrCondition("extends_source_language", "", mainProductPlatformCart, sxData, shopProp));
        // 官网名称(必填)     网站的名称，如果美国某某网站，可以做配置，配置后，可以不需要发布的时候填写
        paramExtends.put("website_name", getValueFromPageOrCondition("extends_website_name", "", mainProductPlatformCart, sxData, shopProp));
        // 官网商品地址(必填)  商品在海外网址的地址，如果无法确保一一对应，可以先填写网站url
        paramExtends.put("website_url", getValueFromPageOrCondition("extends_website_url", "", mainProductPlatformCart, sxData, shopProp));
        // 参考价格(非必填)    商品的参考价格，如果大于现在的价格，则填写
//        paramExtends.put("foreign_origin_price", getValueFromPageOrCondition("extends_foreign_origin_price", "", mainProductPlatformCart, sxData, shopProp));
        // 是否使用原标题(false自动插入商品关键词）(非必填)  填写true表示使用原始标题，false表示需要插入关键词，不填写默认为不需要插入关键词
        paramExtends.put("original_title", getValueFromPageOrCondition("extends_original_title", "", mainProductPlatformCart, sxData, shopProp));
        // 店铺内分类id(非必填)  格式："shop_cats":"111111,222222,333333"
        String extends_shop_cats = "";
        if (mainProductPlatformCart != null
                && ListUtils.notNull(mainProductPlatformCart.getSellerCats())) {
            List<String> sellerCatIdList = new ArrayList<>();
            for (CmsBtProductModel_SellerCat sellerCat : mainProductPlatformCart.getSellerCats()) {
                if (!StringUtils.isEmpty(sellerCat.getcId())) {
                    sellerCatIdList.add(sellerCat.getcId());
                }
            }
            if (ListUtils.notNull(sellerCatIdList)) {
                extends_shop_cats = Joiner.on(Separtor_Coma).join(sellerCatIdList);
            }
        }
        paramExtends.put("shop_cats", extends_shop_cats);
        // 是否包税(非必填)     标识是否要报税，true表示报税，false表示不报税，不填写默认为不报税
        // 注意：跨境申报的商品，是不允许报税的，即便设置了报税，商品在交易的过程中，也需要支付税费
        paramExtends.put("tax_free", getValueFromPageOrCondition("extends_tax_free", "", mainProductPlatformCart, sxData, shopProp));
        // 尺码表图片(非必填)    暂不设置
//        paramExtends.put("international_size_table", getValueFromPageOrCondition("extends_international_size_table", "", mainProductPlatformCart, sxData, shopProp));
        // 是否单独发货(非必填)   true表示单独发货，false表示需要不做单独发货，不填写默认为不单独发货
        paramExtends.put("delivery_separate", getValueFromPageOrCondition("extends_delivery_separate", "", mainProductPlatformCart, sxData, shopProp));
        // 是否支持退货
        paramExtends.put("support_refund", getValueFromPageOrCondition("extends_support_refund", "", mainProductPlatformCart, sxData, shopProp));
        // 官网是否热卖(非必填)    表示是否在官网是热卖商品，true表示热卖，false表示非热卖，不填写默认为非热卖
        paramExtends.put("hot_sale", getValueFromPageOrCondition("extends_hot_sale", "", mainProductPlatformCart, sxData, shopProp));
        // 在官网是否是新品(非必填) 表示是否在官网是新品，true表示新品，false表示非新品，不填写默认为非新品
        paramExtends.put("new_goods", getValueFromPageOrCondition("extends_new_goods", "", mainProductPlatformCart, sxData, shopProp));
        // 入关方式 跨境申报/邮关(必填)     true表示跨境申报，false表示邮关申报
        // 根据中国海关4月8日的最新规定，天猫国际的商品必须设置入关方式，入关方式有2种：
        // 1.跨境申报，即每单交易都向海关申报，单单交税；
        // 2.邮关申报，即通过万国邮联的方式快递包裹，有几率抽检；
        // 说明：设置成跨境申报后，商品的每个SKU必须带有HSCODE才可以上架，设置成邮关申报后，商品不需要设置HSCODE
        paramExtends.put("cross_border_report", getValueFromPageOrCondition("extends_cross_border_report", "", mainProductPlatformCart, sxData, shopProp));

        productInfoMap.put("extends", JacksonUtil.bean2Json(paramExtends));

        // 无线描述(选填)
        // 解析cms_mt_platform_dict表中的数据字典
        if (mainProduct.getCommon().getFields().getAppSwitch() != null &&
                mainProduct.getCommon().getFields().getAppSwitch() == 1) {

            String valWirelessDetails;
            RuleExpression ruleWirelessDetails = new RuleExpression();
            MasterWord masterWordWirelessDetails = new MasterWord("wirelessDetails");
            ruleWirelessDetails.addRuleWord(masterWordWirelessDetails);
            String wirelessDetails = null;
            try {
                wirelessDetails = expressionParser.parse(ruleWirelessDetails, shopProp, getTaskName(), null);
            } catch (Exception e) {
            }
            if (!StringUtils.isEmpty(wirelessDetails)) {
                valWirelessDetails = getValueByDict(wirelessDetails, expressionParser, shopProp);
            } else {
                valWirelessDetails = getValueByDict("天猫同购无线描述", expressionParser, shopProp);
            }

            productInfoMap.put("wireless_desc", valWirelessDetails);
        }

        // 商品上下架
        CmsConstants.PlatformActive platformActive = sxData.getPlatform().getPlatformActive();
        // 更新商品并且PlatformActive=ToOnSale时,执行商品上架；新增商品或PlatformActive=ToInStock时，执行下架功能
        if (updateWare && platformActive == CmsConstants.PlatformActive.ToOnSale) {
            productInfoMap.put("status", "0");   // 商品上架
        } else {
            productInfoMap.put("status", "2");   // 商品在库
        }

        return productInfoMap;
    }

    /**
     * 取得画面上输入的项目对应的值
     *
     * @param itemName 项目名字(如："logistics_weight")
     * @param defaultValue 如果未取到对应的值时，返回的默认值
     * @param mainProductPlatformCart 产品中的分平台信息
     * @return 项目对应的值
     * @throws Exception
     */
    private String getValueFromPage(String itemName, String defaultValue, CmsBtProductModel_Platform_Cart mainProductPlatformCart)  {
        String value = "";
        if (mainProductPlatformCart != null && mainProductPlatformCart.getFields() != null
                && !StringUtils.isEmpty(mainProductPlatformCart.getFields().getStringAttribute(itemName))) {
            // 画面上输入的platform的fields中的项目值
            value = mainProductPlatformCart.getFields().getStringAttribute(itemName);
        }

        return StringUtils.isEmpty(value) ? defaultValue : value;
    }

    /**
     * 取得画面上输入的项目对应的值，如果未取到则读取cms_mt_channel_condition_config表配置的值
     *
     * @param itemName 项目名字(如："logistics_weight")
     * @param defaultValue 如果未取到对应的值时，返回的默认值
     * @param mainProductPlatformCart 产品中的分平台信息
     * @param sxData 上新数据
     * @param shopProp 店铺信息
     * @return 项目对应的值
     */
    private String getValueFromPageOrCondition(String itemName, String defaultValue,
                                               CmsBtProductModel_Platform_Cart mainProductPlatformCart,
                                               SxData sxData, ShopBean shopProp)  {
        String value = "";
        if (mainProductPlatformCart != null && mainProductPlatformCart.getFields() != null
                && !StringUtils.isEmpty(mainProductPlatformCart.getFields().getStringAttribute(itemName))) {
            // 画面上输入的platform的fields中的项目值
            value = mainProductPlatformCart.getFields().getStringAttribute(itemName);
        } else {
            // 解析cms_mt_channel_condition_config表中的数据字典取得"项目名_XX"(XX为cartId)对应的值
            value = getConditionPropValue(sxData, itemName, shopProp);
        }

        return StringUtils.isEmpty(value) ? defaultValue : value;
    }


    /**
     * 读取字典对应的值，如果返回空字符串则抛出异常
     *
     * @param dictName 字典名字(如："商品主图5张")
     * @param expressionParser 解析子
     * @param shopProp 店铺信息
     * @return 解析出来值（如果为多张图片URL，用逗号分隔)
     * @throws Exception
     */
    private String getValueByDict(String dictName, ExpressionParser expressionParser, ShopBean shopProp)  {
        String result = "";
        try {
            // 解析字典，取得对应的值
            result = sxProductService.resolveDict(dictName, expressionParser, shopProp, getTaskName(), null);
            if(StringUtils.isNullOrBlank2(result))
            {
                String errorMsg = String.format("字典解析的结果为空! (猜测有可能是字典不存在或者素材管理里的共通图片没有一张图片成功上传到平台) " +
                        "[dictName:%s]", dictName);
                $error(errorMsg);
                throw new BusinessException(errorMsg);
            }
        } catch (Exception e) {
            String errorMsg = "";
            // 如果字典解析异常的errorMessage为空
            if (StringUtils.isNullOrBlank2(e.getMessage())) {
                // nullpoint错误的处理
                errorMsg = "天猫同购上新字典解析时出现不可预知的错误，请跟管理员联系. " + e.getStackTrace()[0].toString();
                e.printStackTrace();
            } else {
                errorMsg = e.getMessage();
            }
            throw new BusinessException(errorMsg);
        }

        return result;
    }

    /**
     * 从cms_mt_channel_condition_config表中取得指定类型的值
     *
     * @param shop ShopBean 店铺对象
     * @param sxData SxData 产品对象
     * @param prePropId String 条件表达式前缀(运费模板:transportid_ 关联版式:commonhtml_id_)
     * @return String 指定类型(如：运费模板id或关联版式id等)对应的值
     */
    private String getConditionPropValue(SxData sxData, String prePropId, ShopBean shop) {

        // 运费模板id或关联版式id返回用
        String  resultStr = "";
        // 条件表达式前缀(运费模板:transportid_ 关联版式:commonhtml_id_)
        // 条件表达式表platform_prop_id字段的检索条件为条件表达式前缀加cartId
        String platformPropId = prePropId + "_" + sxData.getCartId();

        ExpressionParser expressionParser = new ExpressionParser(sxProductService, sxData);

        // 根据channelid和platformPropId取得cms_mt_channel_condition_config表的条件表达式
        List<ConditionPropValueModel> conditionPropValueModels = null;
        if (channelConditionConfig != null && channelConditionConfig.containsKey(sxData.getChannelId())) {
            if (channelConditionConfig.get(sxData.getChannelId()).containsKey(platformPropId)) {
                conditionPropValueModels = channelConditionConfig.get(sxData.getChannelId()).get(platformPropId);
            }
        }

        // 使用运费模板或关联版式条件表达式
        if (ListUtils.isNull(conditionPropValueModels))
            return resultStr;

        try {
            RuleJsonMapper ruleJsonMapper = new RuleJsonMapper();
            for (ConditionPropValueModel conditionPropValueModel : conditionPropValueModels) {
                String conditionExpressionStr = conditionPropValueModel.getCondition_expression().trim();
                RuleExpression conditionExpression;
                String propValue;

                // 带名字字典解析
                if (conditionExpressionStr.startsWith("{\"type\":\"DICT\"")) {
                    DictWord conditionDictWord = (DictWord) ruleJsonMapper.deserializeRuleWord(conditionExpressionStr);
                    conditionExpression = conditionDictWord.getExpression();
                } else if (conditionExpressionStr.startsWith("{\"ruleWordList\"")) {
                    // 不带名字，只有字典表达式字典解析
                    conditionExpression = ruleJsonMapper.deserializeRuleExpression(conditionExpressionStr);
                } else {
                    String errMsg = String.format("cms_mt_channel_condition_config表中数据字典的格式不对 [ChannelId:%s]" +
                            " [CartId:%s] [DictName:%s]", sxData.getChannelId(), sxData.getCartId(), prePropId);
//                        logIssue(getTaskName(), errMsg);
                    $info(errMsg);
                    continue;
                }

                // 解析出字典对应的值
                if (shop != null) {
                    propValue = expressionParser.parse(conditionExpression, shop, getTaskName(), null);
                } else {
                    propValue = expressionParser.parse(conditionExpression, null, getTaskName(), null);
                }

                // 找到字典对应的值则跳出循环
                if (!StringUtils.isEmpty(propValue)) {
                    resultStr = propValue;
                    break;
                }
            }
        } catch (Exception e) {
            String errMsg = String.format("cms_mt_channel_condition_config表中数据字典解析出错 [ChannelId:%s]" +
                            " [CartId:%s] [DictName:%s] [errMsg:%s]", sxData.getChannelId(), sxData.getCartId(), prePropId,
                    StringUtils.isEmpty(e.getMessage()) ? "出现不可预知的错误，请跟管理员联系" : e.getMessage());
            // 如果上新数据中的errorMessage为空
            if (StringUtils.isNullOrBlank2(sxData.getErrorMessage())) {
                e.printStackTrace();
            }
            $info(errMsg);
            throw new BusinessException(errMsg);
        }

        // 指定类型(如：运费模板id或关联版式id等)对应的值
        return resultStr;
    }

    /**
     * 从cms_mt_channel_config表中取得价格对应的配置项目值
     *
     * @param channelId String 渠道id
     * @param cartId String 平台id
     * @param priceKey String 价格类型 (".sx_price",".tejiabao_open",".tejiabao_price")
     * @return double SKU价格
     */
    public String getPriceConfigValue(String channelId, String cartId,String priceKey ,String priceCode) {
        // 价格有可能是用priceSale, 也有可能用priceMsrp
        CmsChannelConfigBean priceConfig = CmsChannelConfigs.getConfigBean(channelId,priceKey,
                cartId + priceCode);

        String priceConfigValue = "";
        if (priceConfig != null) {
            // 取得价格对应的configValue名
            priceConfigValue = priceConfig.getConfigValue1();
        }

        return priceConfigValue;
    }

    /**
     * 取得天猫同购上新用skus列表
     *
     * @param type 0: 适合颜色尺码都有的类目， 1：适合单sku的类目
     * @param cartId String 平台id
     * @param productList List<BaseMongoMap<String, Object>> 上新产品列表
     * @param skuList List<BaseMongoMap<String, Object>> 上新合并后sku列表
     * @param priceConfigValue String 价格取得项目名 ("cartId.sx_price")配置的项目名
     * @param skuLogicQtyMap Map<String, Integer> SKU逻辑库存
     * @param expressionParser 解析子
     * @param shopProp ShopBean 店铺信息
     * @param crossBorderRreportFlg String 入关方式(true表示跨境申报，false表示邮关申报)
     * @return List<BaseMongoMap<String, Object>> 天猫同购上新用skus列表
     */
    private List<BaseMongoMap<String, Object>> getSkus(int type, Integer cartId, List<CmsBtProductModel> productList,
                                                       List<BaseMongoMap<String, Object>> skuList,
                                                       String priceConfigValue, Map<String, Integer> skuLogicQtyMap,
                                                       ExpressionParser expressionParser,
                                                       ShopBean shopProp, String crossBorderRreportFlg) {

        // 官网同购， 上新时候的价格， 统一用所有sku里的最高价
        Double priceMax = 0d;
        for (CmsBtProductModel product : productList) {
            if (product.getCommon() == null
                    || product.getCommon().getFields() == null
                    || product.getPlatform(cartId) == null
                    || ListUtils.isNull(product.getPlatform(cartId).getSkus())) {
                continue;
            }
            for (BaseMongoMap<String, Object> sku : product.getPlatform(cartId).getSkus()) {
                String skuCode = sku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.skuCode.name());

                // 根据skuCode从skuList中取得common.sku和PXX.sku合并之后的sku
                BaseMongoMap<String, Object> mergedSku = skuList.stream()
                        .filter(s -> s.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.skuCode.name()).equals(skuCode))
                        .findFirst()
                        .get();
                // 价格(根据cms_mt_channel_config表中的配置有可能是从priceRetail或者priceMsrp中取得价格)
                if (priceMax.compareTo(Double.parseDouble(mergedSku.getStringAttribute(priceConfigValue))) < 0) {
                    priceMax = Double.parseDouble(mergedSku.getStringAttribute(priceConfigValue));
                }

            }

        }


        // 具体设置属性的逻辑
        List<BaseMongoMap<String, Object>> targetSkuList = new ArrayList<>();
        // 循环productList设置颜色和尺码等信息到sku列表
        for (CmsBtProductModel product : productList) {
            if (product.getCommon() == null
                    || product.getCommon().getFields() == null
                    || product.getPlatform(cartId) == null
                    || ListUtils.isNull(product.getPlatform(cartId).getSkus())) {
                continue;
            }

            // 取得海关报关税号code(10位数字)  (例："9404909000,变形枕,个")
            String hscode = "";
            // 只有当入关方式(true表示跨境申报)时，才需要设置海关报关税号hscode;false表示邮关申报时，不需要设置海关报关税号hscode
            if ("true".equalsIgnoreCase(crossBorderRreportFlg)) {
                String hsCodePrivate = product.getCommon().getFields().getHsCodePrivate();
                if (!StringUtils.isEmpty(hsCodePrivate)) {
                    if (hsCodePrivate.contains(Separtor_Coma)) {
                        hscode = hsCodePrivate.substring(0, hsCodePrivate.indexOf(Separtor_Coma));
                    } else {
                        hscode = hsCodePrivate;
                    }
                }
            }

            // 采用Ⅲ有SKU,且有不同图案，颜色的设置方式
            // 根据cms_mt_channel_condition_config表中配置的取得对象项目，取得当前product对应的商品特质英文（code 或 颜色/口味/香型等）的设置项目 (根据配置决定是用code还是codeDiff，默认为code)
            SxData sxData = expressionParser.getSxData();
            // 由于字典解析方式只能取得mainProduct里面字段的值，但color需要取得每个product里面字段的值，所以另加一个新的方法取得color值
            // modified by morse.lu 2016/12/29 start
            // 配置表和天猫统一
//            String color = getColorCondition(sxData.getChannelId(), sxData.getCartId(), product, "color_code_codediff", shopProp);
//            // 如果根据配置(code或者codeDiff)取出来的值大于30位，则采用color字段的值
//            if (!StringUtils.isEmpty(color) && color.length() > 30) {
//                color = product.getCommon().getFields().getColor();
//            }
            String color = sxProductService.getSxColorAlias(sxData.getChannelId(), sxData.getCartId(), product, 30);
            // modified by morse.lu 2016/12/29 end

            // 在根据skuCode循环
            for (BaseMongoMap<String, Object> sku : product.getPlatform(cartId).getSkus()) {
                String skuCode = sku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.skuCode.name());
                BaseMongoMap<String, Object> skuMap = new BaseMongoMap<>();
                // 销售属性map(颜色，尺寸)
                BaseMongoMap<String, Object> saleProp = new BaseMongoMap<>();
                // 商品特质英文（颜色/口味/香型等）(根据配置决定是用code还是codeDiff，默认为code)
                saleProp.put("color", color);
                // 根据skuCode从skuList中取得common.sku和PXX.sku合并之后的sku
                BaseMongoMap<String, Object> mergedSku = skuList.stream()
                        .filter(s -> s.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.skuCode.name()).equals(skuCode))
                        .findFirst()
                        .get();
                // 尺寸
                saleProp.put("size", mergedSku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.sizeSx.name()));
                // 追加销售属性
                if (type == 0) { // 只有在type是0的场合（有多个颜色尺码的场合）才需要sale_prop这个属性
                    skuMap.put("sale_prop", saleProp);
                }

                // 价格(根据cms_mt_channel_config表中的配置有可能是从priceRetail或者priceMsrp中取得价格)
//                skuMap.put("price", mergedSku.getStringAttribute(priceConfigValue));
                skuMap.put("price", String.valueOf(priceMax));
                // outer_id
                skuMap.put("outer_id", skuCode);
                // 库存
                skuMap.put("quantity", skuLogicQtyMap.get(skuCode));
                // 与颜色尺寸这个销售属性关联的图片
                String imageTemplate = getValueByDict("属性图片模板", expressionParser, shopProp);
                String propImage = expressionParser.getSxProductService().getProductImages(product, CmsBtProductConstants.FieldImageType.PRODUCT_IMAGE).get(0).getName();
                String srcImage = String.format(imageTemplate, propImage);
                Set<String> url = new HashSet<>();
                url.add(srcImage);
                try {
                    Map<String, String> map = sxProductService.uploadImage(shopProp.getOrder_channel_id(), cartId, expressionParser.getSxData().getGroupId().toString(), shopProp, url, getTaskName());
                    skuMap.put("image", map.get(srcImage));
                } catch (Exception e) {
                    logger.warn("官网同购sku颜色图片取得失败, groupId: " + expressionParser.getSxData().getGroupId());
                }
                // 只有当入关方式(true表示跨境申报)时，才需要设置海关报关税号hscode;false表示邮关申报时，不需要设置海关报关税号hscode
                if ("true".equalsIgnoreCase(crossBorderRreportFlg)) {
                    // 海关报关的税号
                    skuMap.put("hscode", hscode);
                }

                targetSkuList.add(skuMap);
            }
        }

        return targetSkuList;
    }

//    /**
//     * 从cms_mt_channel_condition_config表中取得商品特质英文(CodeDiff或Code)的值
//     * 由于通过字典解析取值，只能取得mainProduct里面的字段的值，而商品特质英文需要取得每个product中的字段的值，所以不能用原来的字典解析
//     *
//     * @param channelId String 渠道id
//     * @param cartId String 平台id
//     * @param prePropId String 条件表达式前缀(运费模板:transportid_ 关联版式:commonhtml_id_)
//     * @return String 指定类型(如：运费模板id或关联版式id等)对应的值
//     */
//    protected String getColorCondition(String channelId, Integer cartId, CmsBtProductModel product, String prePropId, ShopBean shop) {
//        if (product == null) return "";
//
//        // 返回值用(默认为code)
//        String resultStr = product.getCommon().getFields().getCode();
//
//        // 条件表达式前缀(商品特质英文(CodeDiff或Code):color_code_codediff)
//        // 条件表达式表platform_prop_id字段的检索条件为条件表达式前缀加cartId
//        String platformPropId = prePropId + "_" + StringUtils.toString(cartId);
//        // 根据channelid和platformPropId取得cms_mt_channel_condition_config表的条件表达式
//        List<ConditionPropValueModel> conditionPropValueModels = null;
//        if (channelConditionConfig.containsKey(channelId)) {
//            if (channelConditionConfig.get(channelId).containsKey(platformPropId)) {
//                conditionPropValueModels = channelConditionConfig.get(channelId).get(platformPropId);
//            }
//        }
//
//        // 使用运费模板或关联版式条件表达式
//        if (ListUtils.notNull(conditionPropValueModels)) {
//            for (ConditionPropValueModel conditionPropValueModel : conditionPropValueModels) {
//                String conditionExpressionStr = conditionPropValueModel.getCondition_expression().trim();
//
//                String propValue = null;
//                // 带名字字典解析
//                if (conditionExpressionStr.contains("color")) {
//                    propValue = product.getCommon().getFields().getColor();
//                } else if (conditionExpressionStr.contains("codeDiff")) {
//                    propValue = product.getCommon().getFields().getCodeDiff();
//                } else if (conditionExpressionStr.contains("code")) {
//                    propValue = product.getCommon().getFields().getCode();
//                }
//
//                // 找到字典对应的值则跳出循环
//                if (!StringUtils.isEmpty(propValue)) {
//                    resultStr = propValue;
//                    break;
//                }
//            }
//        }
//
//        // 如果cms_mt_channel_condition_config表里有配置项，就返回配置的字段值；
//        // 如果没有配置加这个配置项目，直接反应该产品的code
//        return resultStr;
//    }

    /**
     * 根据天猫官网同购返回的NumIId取得平台上的类目id和类目path
     *
     * @param shopProp 店铺信息
     * @param numIId 天猫官网同购上新成功之后返回的numIId
     * @return Map<String, String> 天猫官网同购平台类目信息
     */
    protected Map<String, String> getSimpleItemCatInfo(ShopBean shopProp, String numIId) throws ApiException, GetUpdateSchemaFailException, TopSchemaException {
        // 调用官网同购编辑商品的get接口取得商品信息
        TbItemSchema tbItemSchema = tbSimpleItemService.getSimpleItem(shopProp, NumberUtils.toLong(numIId));
        if (tbItemSchema == null || ListUtils.isNull(tbItemSchema.getFields())) {
            // 天猫官网同购取得商品信息失败
            String errMsg = "天猫官网同购取得商品信息失败! ";
            $error(errMsg);
            throw new BusinessException(errMsg);
        }

        String pCatId = null;
        // 取得类目对应的Field
        InputField inputFieldCategory = (InputField)tbItemSchema.getFieldMap().get("category");
        if (!StringUtils.isEmpty(inputFieldCategory.getDefaultValue())
                && inputFieldCategory.getDefaultValue().contains(":")) {
            // 取得平台上返回的catId
            String[] strings = inputFieldCategory.getDefaultValue().split(":");
            pCatId = strings[1].replaceAll("}|\"", "");   // 删除里面多余的大括号(})和双引号(")
        }

        if (StringUtils.isEmpty(pCatId)) {
            return null;
        }

        Map<String, String> pCatInfoMap = new HashMap<>(2, 1f);
        pCatInfoMap.put("pCatId", pCatId);

        try {
            // 取得天猫官网同购pCatId对应的pCatPath
            String pCatPath = getTongGouCatFullPathByCatId(shopProp, pCatId);
            if (!StringUtils.isEmpty(pCatPath)) {
                pCatInfoMap.put("pCatPath", pCatPath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pCatInfoMap;
    }

    /**
     * 根据天猫官网同购返回的NumIId取得平台上的类目id和类目path
     *
     * @param shopProp 店铺信息
     * @param pCatdId 天猫官网同购商品中取得的catId
     * @return String 天猫官网同购catId对应的平台catPath
     */
    protected String getTongGouCatFullPathByCatId(ShopBean shopProp, String pCatdId) {
        String pCatPath = null;

        // 从cms_mt_platform_category_schema_tm_cXXX(channelId)表中取得pCatId对应的pCatPath
        CmsMtPlatformCategorySchemaTmModel categorySchemaTm = platformCategorySchemaDao.selectPlatformCatSchemaTmModel(
                pCatdId, shopProp.getOrder_channel_id(), NumberUtils.toInt(shopProp.getCart_id()));
        if (categorySchemaTm != null) {
            pCatPath = categorySchemaTm.getCatFullPath();
        }

        return pCatPath;
    }

    /**
     * 特价宝的调用
     *
     * @param sxData            SxData 上新数据
     */
    private void updateTeJiaBaoPromotion(SxData sxData) {
        // 特价宝的调用
        // 价格有可能是用priceSale, 也有可能用priceMsrp, 所以需要判断一下
        CmsChannelConfigBean tejiabaoOpenConfig = CmsChannelConfigs.getConfigBean(sxData.getChannelId()
                , CmsConstants.ChannelConfig.PRICE_TEJIABAO_IS_OPEN_KEY
                , String.valueOf(sxData.getCartId()) + CmsConstants.ChannelConfig.PRICE_TEJIABAO_IS_OPEN_CODE);
        CmsChannelConfigBean tejiabaoPriceConfig = CmsChannelConfigs.getConfigBean(sxData.getChannelId()
                , CmsConstants.ChannelConfig.PRICE_TEJIABAO_KEY
                , String.valueOf(sxData.getCartId()) + CmsConstants.ChannelConfig.PRICE_TEJIABAO_PRICE_CODE);

        // 检查一下
        String tejiabaoOpenFlag = null;
        String tejiabaoPricePropName = null;

        if (tejiabaoOpenConfig != null && !StringUtils.isEmpty(tejiabaoOpenConfig.getConfigValue1())) {
            if ("0".equals(tejiabaoOpenConfig.getConfigValue1()) || "1".equals(tejiabaoOpenConfig.getConfigValue1())) {
                tejiabaoOpenFlag = tejiabaoOpenConfig.getConfigValue1();
            }
        }
        if (tejiabaoPriceConfig != null && !StringUtils.isEmpty(tejiabaoPriceConfig.getConfigValue1())) {
            tejiabaoPricePropName = tejiabaoPriceConfig.getConfigValue1();
        }

        if (tejiabaoOpenFlag != null && "1".equals(tejiabaoOpenFlag)) {
            for (CmsBtProductModel sxProductModel : sxData.getProductList()) {
                // 获取价格
                if (sxProductModel.getCommon().getSkus() == null || sxProductModel.getCommon().getSkus().size() == 0) {
                    // 没有sku的code, 跳过
                    continue;
                }

                List<CmsBtPromotionSkuBean> skus = new ArrayList<>();
                for (BaseMongoMap<String, Object> sku : sxProductModel.getPlatform(sxData.getCartId()).getSkus()) {
                    CmsBtPromotionSkuBean skuBean = new CmsBtPromotionSkuBean();
                    skuBean.setProductSku(sku.getAttribute("skuCode"));
                    Double dblPriceSku = Double.parseDouble(sku.getAttribute(tejiabaoPricePropName).toString());
                    skuBean.setPromotionPrice(new BigDecimal(dblPriceSku));
                    skus.add(skuBean);
                }

                Double dblPrice = Double.parseDouble(sxProductModel.getPlatform(sxData.getCartId()).getSkus().get(0).getAttribute(tejiabaoPricePropName).toString());

                // 设置特价宝
                CmsBtPromotionCodesBean cmsBtPromotionCodesBean = new CmsBtPromotionCodesBean();
                cmsBtPromotionCodesBean.setPromotionId(0); // 设置为0的场合,李俊代码里会去处理
                cmsBtPromotionCodesBean.setChannelId(sxData.getChannelId());
                cmsBtPromotionCodesBean.setCartId(sxData.getCartId());
                // product表结构变化
                cmsBtPromotionCodesBean.setProductCode(sxProductModel.getCommon().getFields().getCode());
                cmsBtPromotionCodesBean.setProductId(sxProductModel.getProdId());
                cmsBtPromotionCodesBean.setPromotionPrice(dblPrice); // 真实售价
                cmsBtPromotionCodesBean.setNumIid(sxData.getPlatform().getNumIId());
                cmsBtPromotionCodesBean.setModifier(getTaskName());
                cmsBtPromotionCodesBean.setSkus(skus);
                // 这里只需要调用更新接口就可以了, 里面会有判断如果没有的话就插入
                promotionDetailService.teJiaBaoPromotionUpdate(cmsBtPromotionCodesBean);

            }
        }

    }

    /**
     * 取得产品主类目与天猫平台叶子类目(或者平台一级类目)，以及feed类目id和天猫平台类目之间的mapping关系数据
     *
     * @param channelId 渠道id
     * @param cartId 平台id
     * @return  Map<String, Map<String, String>>表中的配置mapping信息
     */
    protected Map<String, Map<String, String>> getCategoryMapping(String channelId, int cartId) {

        Map<String, Map<String, String>> categoryMapping = new HashMap<>();
        // 取得主类目与天猫平台叶子类目之间的mapping关系数据
        List<CmsMtChannelConditionMappingConfigModel> mainLeafList = getChannelConditionMappingInfo(channelId, cartId, TtPropName.tt_main_category_leaf.name());
        if (ListUtils.notNull(mainLeafList)) {
            Map<String, String> conditionMappingMap = new HashMap<>();
            mainLeafList.forEach(p -> conditionMappingMap.put(p.getMapKey(), p.getMapValue()));
            categoryMapping.put(TtPropName.tt_main_category_leaf.name(), conditionMappingMap);
        }

        // 取得主类目与天猫平台叶子类目之间的mapping关系数据
        List<CmsMtChannelConditionMappingConfigModel> mainCategoryList = getChannelConditionMappingInfo(channelId, cartId, TtPropName.tt_main_category.name());
        if (ListUtils.notNull(mainCategoryList)) {
            Map<String, String> conditionMappingMap = new HashMap<>();
            mainCategoryList.forEach(p -> conditionMappingMap.put(p.getMapKey(), p.getMapValue()));
            categoryMapping.put(TtPropName.tt_main_category.name(), conditionMappingMap);
        }

        // 取得feed类目与天猫平台叶子类目之间的mapping关系数据
        List<CmsMtChannelConditionMappingConfigModel> feedCategoryList = getChannelConditionMappingInfo(channelId, cartId, TtPropName.tt_category.name());
        if (ListUtils.notNull(feedCategoryList)) {
            Map<String, String> conditionMappingMap = new HashMap<>();
            feedCategoryList.forEach(p -> conditionMappingMap.put(p.getMapKey(), p.getMapValue()));
            categoryMapping.put(TtPropName.tt_category.name(), conditionMappingMap);
        }

        return categoryMapping;
    }

    /**
     * 从cms_mt_channel_condition_mapping_config表中取得该渠道，平台对应的客户过来的类目id和天猫平台类目之间的mapping关系数据
     *
     * @param channelId 渠道id
     * @param cartId 平台id
     * @param propName 查询mapping分类(tt_main_category_leaf:主类目与平台叶子类目, tt_main_category:主类目与平台一级类目, tt_category:feed类目与平台一级类目)
     * @return List<CmsMtChannelConditionMappingConfigModel> 表中的配置mapping信息
     */
    protected List<CmsMtChannelConditionMappingConfigModel> getChannelConditionMappingInfo(String channelId, int cartId, String propName) {

        // 从cms_mt_channel_condition_mapping_config表中取得该渠道，平台对应的客户过来的类目id和天猫平台一级类目之间的mapping关系数据
        Map<String, String> conditionMappingParamMap = new HashMap<>();
        conditionMappingParamMap.put("channelId", channelId);
        conditionMappingParamMap.put("cartId", StringUtils.toString(cartId));
        conditionMappingParamMap.put("propName", propName);   // 天猫同购一级类目匹配
        List<CmsMtChannelConditionMappingConfigModel> conditionMappingConfigModels =
                cmsMtChannelConditionMappingConfigDao.selectList(conditionMappingParamMap);
        if (ListUtils.isNull(conditionMappingConfigModels)) {
            $warn("cms_mt_channel_condition_mapping_config表中没有该渠道和平台对应的天猫平台类目匹配信息！[ChannelId:%s] " +
                    "[CartId:%s] [propName:%s]", channelId, cartId, propName);
            return null;
        }

        return conditionMappingConfigModels;
    }

    /**
     * 取得主类目到天猫一级类目的匹配结果
     *
     * @param mainCatPath 主类目
     * @param brand 品牌
     * @param sizeType 适用人群
     * @return List<CmsMtChannelConditionMappingConfigModel> 表中的配置mapping信息
     */
    protected String getMainCategoryMappingInfo(String mainCatPath, String brand, String sizeType) {

        Map<String, String> resultMap = mainCategoryMapList.stream()
                .filter(m -> mainCatPath.equalsIgnoreCase(m.get("t_mainCategory")))
                .filter(m -> (StringUtils.isEmpty(brand) || brand.equalsIgnoreCase(m.get("t_brand"))))
                .filter(m -> (StringUtils.isEmpty(sizeType) || sizeType.equalsIgnoreCase(m.get("t_sizeType"))))
                .findFirst()
                .orElse(null);

        if (MapUtils.isNotEmpty(resultMap)) {
            return resultMap.get("t_category");
        }

        return null;
    }

}
