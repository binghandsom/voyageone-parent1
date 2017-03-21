package com.voyageone.task2.cms.service;

import com.google.common.base.Joiner;
import com.jd.open.api.sdk.domain.*;
import com.jd.open.api.sdk.domain.ware.Sku;
import com.jd.open.api.sdk.request.ware.ImageWriteUpdateRequest;
import com.jd.open.api.sdk.response.ware.ImageWriteUpdateResponse;
import com.mongodb.BulkWriteResult;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.base.dao.mongodb.JongoUpdate;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.dao.mongodb.model.BulkJongoUpdateList;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.CmsChannelConfigs;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.CmsChannelConfigBean;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.masterdate.schema.factory.SchemaReader;
import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.common.masterdate.schema.field.InputField;
import com.voyageone.common.masterdate.schema.field.MultiCheckField;
import com.voyageone.common.masterdate.schema.field.SingleCheckField;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.masterdate.schema.value.Value;
import com.voyageone.common.util.*;
import com.voyageone.components.jd.JdConstants;
import com.voyageone.components.jd.bean.JdProductBean;
import com.voyageone.components.jd.bean.JdProductNewBean;
import com.voyageone.components.jd.service.JdSaleService;
import com.voyageone.components.jd.service.JdSkuService;
import com.voyageone.components.jd.service.JdWareNewService;
import com.voyageone.components.jd.service.JdWareService;
import com.voyageone.ims.rule_expression.MasterWord;
import com.voyageone.ims.rule_expression.RuleExpression;
import com.voyageone.ims.rule_expression.RuleJsonMapper;
import com.voyageone.service.bean.cms.product.SxData;
import com.voyageone.service.dao.cms.CmsMtChannelConditionMappingConfigDao;
import com.voyageone.service.dao.cms.mongo.CmsBtPlatformActiveLogDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.impl.cms.*;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.sx.SxProductNewService;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.sx.rule_parser.ExpressionParser;
import com.voyageone.service.model.cms.CmsBtSxWorkloadModel;
import com.voyageone.service.model.cms.CmsMtChannelConditionMappingConfigModel;
import com.voyageone.service.model.cms.CmsMtPlatformDictModel;
import com.voyageone.service.model.cms.CmsMtPlatformSkusModel;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategorySchemaModel;
import com.voyageone.service.model.cms.mongo.product.*;
import com.voyageone.task2.base.BaseCronTaskService;
import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.base.util.TaskControlUtils;
import com.voyageone.task2.cms.model.ConditionPropValueModel;
import com.voyageone.task2.cms.service.putaway.ConditionPropValueRepo;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 * 京东平台产品上新服务
 * Product表中产品不存在就向京东平台新增商品，否则就更新商品
 *
 * @author charis on 2017/2/23.
 * @version 2.1.0
 * @since 2.0.0
 */
@SuppressWarnings("ALL")
@Service
public class CmsBuildPlatformProductUploadJdNewService extends BaseCronTaskService {

    // 京东平台的操作类型(在售)
    // charis update
//    private final static String OptioinType_onsale = "onsale";
    private final static int OptioinType_onsale = 8;
    // 京东平台的操作类型(在库)
    // charis update
//    private final static String OptioinType_offsale = "offsale";
    private final static int OptioinType_offsale = 2;
    // 京东平台商品状态（从未上架）
    private  final static int OptionType_neverSale = 1;
    // 价格类型(市场价格)
    //private final static String PriceType_marketprice = "retail_price";
    // 价格类型(京东价格)
    //private final static String PriceType_jdprice = "sale_price";
    // 商品属性列表
    private final static String Attributes = "attributes";
    // 用户自行输入的类目属性ID串
    private final static String Input_Pids = "input_pids";
    // 用户自行输入的属性值串
    private final static String Input_Strs = "input_strs";
    // 前台展示的商家自定义店内分类
//    private final static String Prop_ShopCategory = "seller_cids_";
    // 京东运费模板
    private final static String Prop_TransportId = "transportid_";
    // 京东关联板式
    private final static String Prop_CommonHtmlId = "commonhtml_id_";
    // SKU属性类型(颜色)
    private final static String AttrType_Color = "c";
    // SKU属性类型(尺寸)
    private final static String AttrType_Size = "s";
    // SKU属性Active
    private final static int AttrType_Active_1 = 1;
    // 分隔符(tab)
//    private final static String Separtor_Xor = "^";
    // 分隔符(|)
    private final static String Separtor_Vertical = "|";
    // 分隔符(-)
    private final static String Separtor_Hyphen = "-";
    // 分隔符(:)
    private final static String Separtor_Colon = ":";
    // 分隔符(;)
    private final static String Separtor_Semicolon = ";";
    // 分隔符(,)
    private final static String Separtor_Coma = ",";
    // 商品主图颜色值Id(0000000000)
    private final static String ColorId_MainPic = "0000000000";
    // 销售属性_颜色和尺码都有
    private final static String SaleProp_Both_Color_Size_1 = "1";
    // 销售属性_只有颜色没有尺码属性
    private final static String SaleProp_Only_Color_2 = "2";
    // 销售属性_只有尺码没有颜色属性
    private final static String SaleProp_Only_Size_3 = "3";
    // 销售属性_颜色和尺码都没有
    private final static String SaleProp_None_Color_Size_4 = "4";
    // charis update
    // 特殊属性Key(七天无理由退货)
    private final static String Is7ToReturn = "is7ToReturn";
    @Autowired
    private DictService dictService;
    @Autowired
    private PlatformProductUploadService platformProductUploadService;
    @Autowired
    private PlatformCategoryService platformCategoryService;
    @Autowired
    private ConditionPropValueRepo conditionPropValueRepo;
    @Autowired
    private JdWareNewService jdWareNewService;
    @Autowired
    private JdSaleService jdSaleService;
    @Autowired
    private CmsMtPlatformSkusService cmcMtPlatformSkusService;
    @Autowired
    private SxProductService sxProductService;
    @Autowired
    private SxProductNewService sxProductNewService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductGroupService productGroupService;
    @Autowired
    private CmsBtPlatformActiveLogDao cmsBtPlatformActiveLogDao;
    @Autowired
    private MongoSequenceService sequenceService;
    @Autowired
    private CmsBtProductDao cmsBtProductDao;
    @Autowired
    private JdSkuService jdSkuService;
    @Autowired
    private CmsPlatformTitleTranslateMqService cmsTranslateMqService;
    @Autowired
    private CmsMtChannelConditionMappingConfigDao cmsMtChannelConditionMappingConfigDao;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "CmsBuildPlatformProductUploadJdNewJob";
    }

    private Map<String, Map<String, List<ConditionPropValueModel>>> channelConditionConfig;

    /**
     * 京东平台上新处理
     *
     * @param taskControlList taskcontrol信息
     */
    @Override
    public void onStartup(List<TaskControlBean> taskControlList) throws Exception {

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
                // 京东平台商品信息新增或更新(京东)
                doProductUpload(channelId, CartEnums.Cart.JD.getValue());
                // 京东国际商品信息新增或更新(京东国际)
                doProductUpload(channelId, CartEnums.Cart.JG.getValue());
                // 京东平台商品信息新增或更新(京东国际 匠心界)
                doProductUpload(channelId, CartEnums.Cart.JGJ.getValue());
                // 京东国际商品信息新增或更新(京东国际 悦境)
                doProductUpload(channelId, CartEnums.Cart.JGY.getValue());
            }
        }

//        doUploadMain(taskControlList);

        // 正常结束
        $info("主线程正常结束");
    }

//    public void doUploadMain(List<TaskControlBean> taskControlList) {
//        // 由于这个方法可能会自己调用自己循环很多很多次， 不一定会跳出循环， 但又希望能获取到最新的TaskControl的信息， 所以不使用基类里的这个方法了
//        // 为了调试方便， 允许作为参数传入， 但是理想中实际运行中， 基本上还是自主获取的场合比较多
//        if (taskControlList == null) {
//            taskControlList = taskDao.getTaskControlList(getTaskName());
//
//            if (taskControlList.isEmpty()) {
//                $info("没有找到任何配置。");
//                logIssue("没有找到任何配置！！！", getTaskName());
//                return;
//            }
//
//            // 是否可以运行的判断
//            if (!TaskControlUtils.isRunnable(taskControlList)) {
//                return;
//            }
//
//        }
//
//        // 获取该任务可以运行的销售渠道
//        List<TaskControlBean> taskControlBeanList = TaskControlUtils.getVal1s(taskControlList, TaskControlEnums.Name.order_channel_id);
//
//        // 准备按组分配线程（相同的组， 会共用相同的一组线程通道， 不同的组， 线程通道互不干涉）
//        Map<String, List<String>> mapTaskControl = new HashMap<>();
//        taskControlBeanList.forEach((l)->{
//            String key = l.getCfg_val2();
//            if (StringUtils.isEmpty(key)) {
//                key = "0";
//            }
//            if (mapTaskControl.containsKey(key)) {
//                mapTaskControl.get(key).add(l.getCfg_val1());
//            } else {
//                List<String> channelList = new ArrayList<>();
//                channelList.add(l.getCfg_val1());
//                mapTaskControl.put(key, channelList);
//            }
//        });
//
//        // 总共最多5个组同时运行
//        int threadPoolCnt = 5;
////        ExecutorService executor = Executors.newFixedThreadPool(threadPoolCnt);
//        Map<String, ExecutorService> mapThread = new HashMap<>();
//
//        while (true) {
//            boolean blnAllOver = true;
//            for (Map.Entry<String, ExecutorService> entry : mapThread.entrySet()) {
//                if (!entry.getValue().isTerminated()) {
//                    blnAllOver = false;
//                    break;
//                }
//            }
//            if (blnAllOver) {
//                break;
//            }
//
//            mapTaskControl.forEach((k, v)->{
//                boolean blnCreateThread = false;
//
//                if (mapThread.containsKey(k)) {
//                    ExecutorService t = mapThread.get(k);
//                    if (t.isTerminated()) {
//                        // 可以新做一个线程
//                        blnCreateThread = true;
//                    }
//                } else {
//                    // 可以新做一个线程
//                    blnCreateThread = true;
//                }
//
//                if (blnCreateThread) {
//                    ExecutorService t = Executors.newFixedThreadPool(threadPoolCnt);
//
//                    List<String> channelIdList = v;
//                    if (channelIdList != null) {
//                        for (String channelId : channelIdList) {
//                            t.execute(() -> {
//                                try {
//                                    doProductUpload(channelId, CartEnums.Cart.JD.getValue());
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            });
//                            t.execute(() -> {
//                                try {
//                                    doProductUpload(channelId, CartEnums.Cart.JG.getValue());
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            });
//                            t.execute(() -> {
//                                try {
//                                    doProductUpload(channelId, CartEnums.Cart.JGJ.getValue());
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            });
//                            t.execute(() -> {
//                                try {
//                                    doProductUpload(channelId, CartEnums.Cart.JGY.getValue());
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            });
//
//                        }
//                    }
//
//                    mapThread.put(k, t);
//
//                }
//            });
//
//        }
//
////        executor.shutdown(); //并不是终止线程的运行，而是禁止在这个Executor中添加新的任务
////        try {
////            // 阻塞，直到线程池里所有任务结束
////            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
////        } catch (InterruptedException ie) {
////            ie.printStackTrace();
////        }
//
//        // TODO: 所有渠道处理总件数为0的场合， 就跳出不继续做了。 以外的场合， 说明可能还有别的未完成的数据， 继续自己调用自己一下
//        doUploadMain(null);
//
//    }

    /**
     * 平台产品上新主处理
     *
     * @param channelId String 渠道ID
     * @param cartId String 平台ID
     */
    public void doProductUpload(String channelId, int cartId) throws Exception {

        // 默认线程池最大线程数
        int threadPoolCnt = 5;

        // 获取店铺信息
        ShopBean shopProp = Shops.getShop(channelId, cartId);
        if (shopProp == null) {
            $error("获取到店铺信息失败(shopProp == null)! [ChannelId:%s] [CartId:%s]", channelId, cartId);
            return;
        }
        $info("获取店铺信息成功![ChannelId:%s] [CartId:%s]", channelId, cartId);

        // 从上新的任务表中获取该平台及渠道需要上新的任务列表(group by channel_id, cart_id, group_id)
        List<CmsBtSxWorkloadModel> sxWorkloadModels = platformProductUploadService.getSxWorkloadWithChannelIdCartId(
                CmsConstants.PUBLISH_PRODUCT_RECORD_COUNT_ONCE_HANDLE, channelId, cartId);
        if (ListUtils.isNull(sxWorkloadModels)) {
            $error("上新任务表中没有该渠道和平台对应的任务列表信息！[ChannelId:%s] [CartId:%s]", channelId, cartId);
            return;
        }

        // 保存渠道级别(channel)的共通配置项目(从cms_mt_channel_config表中取得的)
        Map<String, String> channelConfigValueMap = new ConcurrentHashMap<>();
        // 取得cms_mt_channel_config表中配置的渠道级别的配置项目值(如：颜色别名等)
        doChannelConfigInit(channelId, cartId, channelConfigValueMap);

		$info("TOM-01:" + sxWorkloadModels.size() + "Channel:" + channelId + ": CartId:" + cartId);
        // 从cms_mt_channel_condition_mapping_config表中取得当前渠道的取得产品主类目与天猫平台叶子类目(或者平台一级类目)，以及feed类目id和天猫平台类目之间的mapping关系数据
        Map<String, List<Map<String, String>>> categoryMappingListMap = getCategoryMapping(channelId, cartId);
		$info("TOM-02");

        // 创建线程池
        ExecutorService executor = Executors.newFixedThreadPool(threadPoolCnt);
        // 根据上新任务列表中的groupid循环上新处理
        for(CmsBtSxWorkloadModel cmsBtSxWorkloadModel:sxWorkloadModels) {
            // 启动多线程
            executor.execute(() -> uploadProduct(cmsBtSxWorkloadModel, shopProp, channelConfigValueMap, categoryMappingListMap));
        }
        // ExecutorService停止接受任何新的任务且等待已经提交的任务执行完成(已经提交的任务会分两类：一类是已经在执行的，另一类是还没有开始执行的)，
        // 当所有已经提交的任务执行完毕后将会关闭ExecutorService。
        executor.shutdown(); // 并不是终止线程的运行，而是禁止在这个Executor中添加新的任务
        try {
            // 阻塞，直到线程池里所有任务结束
			$info("TOM-03");
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
			$info("TOM-04");
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }


    /**
     * 取得产品主类目与天猫平台叶子类目(或者平台一级类目)，以及feed类目id和天猫平台类目之间的mapping关系数据
     *
     * @param channelId 渠道id
     * @param cartId 平台id
     * @return Map<String, List<Map<String, String>>> 表中的配置mapping信息
     */
    protected Map<String, List<Map<String, String>>> getCategoryMapping(String channelId, int cartId) {

        Map<String, List<Map<String, String>>> categoryMapping = new HashMap<>();
        // 取得主类目与天猫平台叶子类目之间的mapping关系数据
        // key:主类目String     value:天猫平台叶子类目String
        List<CmsMtChannelConditionMappingConfigModel> mainLeafList = getChannelConditionMappingInfo(channelId, cartId, CmsBuildPlatformProductUploadTmTongGouService.TtPropName.tt_main_category_leaf.name());
        if (ListUtils.notNull(mainLeafList)) {
            List<Map<String, String>> conditionMappingMapList = new ArrayList<>();
            mainLeafList.forEach(p -> {
                Map<String, String> leafMap = new LinkedHashMap<>();
                leafMap.put(CmsBuildPlatformProductUploadTmTongGouService.TtItemName.t_key_category.name(), p.getMapKey());
                leafMap.put(CmsBuildPlatformProductUploadTmTongGouService.TtItemName.t_value_category.name(), p.getMapValue());
                conditionMappingMapList.add(leafMap);
            });
            categoryMapping.put(CmsBuildPlatformProductUploadTmTongGouService.TtPropName.tt_main_category_leaf.name(), conditionMappingMapList);
        }

        // 取得主类目与天猫平台一级类目之间的mapping关系数据
        // key:包含主类目，品牌，适用人群等的json     value:天猫平台一级类目String
        List<CmsMtChannelConditionMappingConfigModel> mainCategoryList = getChannelConditionMappingInfo(channelId, cartId, CmsBuildPlatformProductUploadTmTongGouService.TtPropName.tt_main_category.name());
        if (ListUtils.notNull(mainCategoryList)) {
            List<Map<String, String>> conditionMappingMapList = new ArrayList<>();
            List<String> errKeyList = new ArrayList<>();
            mainCategoryList.forEach(p -> {
                // 如果匹配key项目值为空或者不是json,报出错误
                if (StringUtils.isEmpty(p.getMapKey())
                        || !(p.getMapKey().startsWith("{") && p.getMapKey().endsWith("}"))) {
                    errKeyList.add(p.getMapKey());
                } else {
                    Map<String, String> categoryMap = new LinkedHashMap<>();
                    // 如果匹配key项目值是json格式的时候，解析出json中配置的多个项目值并分别加到map中
                    Map<String, String> keyMap = JacksonUtil.json2Bean(p.getMapKey(), HashMap.class);
                    categoryMap.putAll(keyMap);   // json中解析出来的项目名称应该跟TtItemName中定义的一致
                    // 再把匹配value项目值也放进map中
                    categoryMap.put(CmsBuildPlatformProductUploadTmTongGouService.TtItemName.t_value_category.name(), p.getMapValue());
                    conditionMappingMapList.add(categoryMap);
                }
            });
            if (ListUtils.notNull(errKeyList)) {
                String errMsg = String.format("从cms_mt_channel_condition_mapping_config表中取得的%s条主类目到天猫一级类目" +
                                "的匹配(%s)关系的匹配key项目不是json格式的，请把数据线修改成json格式之后再次重试！[错误key:%s]",
                        errKeyList.size(), CmsBuildPlatformProductUploadTmTongGouService.TtPropName.tt_main_category.name(), Joiner.on(",").join(errKeyList));
                $error(errMsg);
                throw new BusinessException(errMsg);
            }
            categoryMapping.put(CmsBuildPlatformProductUploadTmTongGouService.TtPropName.tt_main_category.name(), conditionMappingMapList);
        }

        // 取得feed类目与天猫平台一级类目之间的mapping关系数据
        List<CmsMtChannelConditionMappingConfigModel> feedCategoryList = getChannelConditionMappingInfo(channelId, cartId, CmsBuildPlatformProductUploadTmTongGouService.TtPropName.tt_category.name());
        if (ListUtils.notNull(feedCategoryList)) {
            List<Map<String, String>> conditionMappingMapList = new ArrayList<>();
            feedCategoryList.forEach(p -> {
                Map<String, String> feedMap = new LinkedHashMap<>();
                feedMap.put(CmsBuildPlatformProductUploadTmTongGouService.TtItemName.t_key_category.name(), p.getMapKey());
                feedMap.put(CmsBuildPlatformProductUploadTmTongGouService.TtItemName.t_value_category.name(), p.getMapValue());
                conditionMappingMapList.add(feedMap);
            });
            categoryMapping.put(CmsBuildPlatformProductUploadTmTongGouService.TtPropName.tt_category.name(), conditionMappingMapList);
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
    protected List<CmsMtChannelConditionMappingConfigModel> getChannelConditionMappingInfo(String channelId, Integer cartId, String propName) {

        // 从cms_mt_channel_condition_mapping_config表中取得该渠道，平台对应的客户过来的类目id和天猫平台一级类目之间的mapping关系数据
        Map<String, String> conditionMappingParamMap = new HashMap<>();
        if (!StringUtils.isEmpty(channelId)) conditionMappingParamMap.put("channelId", channelId);
        if (cartId != null) conditionMappingParamMap.put("cartId", StringUtils.toString(cartId));
        if (!StringUtils.isEmpty(propName))  conditionMappingParamMap.put("propName", propName);   // 查询mapping分类
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
     * 平台产品上新处理
     *
     * @param cmsBtSxWorkloadModel CmsBtSxWorkloadModel WorkLoad信息
     * @param shopProp ShopBean 店铺信息
     */
    public void uploadProduct(CmsBtSxWorkloadModel cmsBtSxWorkloadModel, ShopBean shopProp, Map<String, String> channelConfigValueMap
                , Map<String, List<Map<String, String>>> categoryMappingListMap) {

        // 当前groupid(用于取得产品信息)
        long groupId = cmsBtSxWorkloadModel.getGroupId();
        // 渠道id
        String channelId = shopProp.getOrder_channel_id();
        // 平台id
        int cartId = Integer.parseInt(shopProp.getCart_id());
        // 商品id
        long jdWareId = 0;
        // 上新数据
        SxData sxData = null;
        // 新增或更新商品标志
        boolean updateWare = false;
        // 是否是智能上新(根据配置)
        boolean blnIsSmartSx = sxProductService.isSmartSx(sxData.getChannelId(), sxData.getCartId());
        String sxType = "普通上新";
        if (blnIsSmartSx) {
            sxType = "智能上新";
        }

        try {
            // 上新用的商品数据信息取得 // TODO：这段翻译写得不好看， 以后再改
            sxData = sxProductService.getSxProductDataByGroupId(channelId, groupId);
            cmsTranslateMqService.executeSingleCode(channelId, 0, sxData.getMainProduct().getCommon().getFields().getCode(), "0");
            sxData = sxProductService.getSxProductDataByGroupId(channelId, groupId);
            if (sxData == null) {
                throw new BusinessException("取得上新用的商品数据信息失败！请向管理员确认 [sxData=null]");
            }
            // 如果取得上新对象商品信息出错时，报错
            if (!StringUtils.isEmpty(sxData.getErrorMessage())) {
                String errorMsg = sxData.getErrorMessage();
                sxData.setErrorMessage(""); // 这里设为空之后，异常捕捉到之后msg前面会加上店铺名称
                // 取得上新数据出错时，cartId有可能没有设置
                sxData.setCartId(cartId);
                // 有错误的时候，直接报错
                throw new BusinessException(errorMsg);
            }

            // 如果一个产品的类目要求至少5张图片，但运营部愿意自己去补足图片导致大量图片上新错误，只好在这里手动给每个产品补足5张图片(用第一张图片补)
            // 但这里补足的图片不会回写到mongoDB的产品中，如果在京东平台上展示出来的效果运营不满意，让他们自己去补足图片
            {
                // 京东默认补满image1的5张图片
                if (ListUtils.notNull(sxData.getMainProduct().getCommon().getFields().getImages1())) {
                    int cnt = sxData.getMainProduct().getCommon().getFields().getImages1().size();
                    for (int i = cnt; i < 5; i++) {
                        sxData.getMainProduct().getCommon().getFields().getImages1().add(sxData.getMainProduct().getCommon().getFields().getImages1().get(0));
                    }
                }
                // 补满每个产品image1的5张图片
                for (CmsBtProductModel cmsBtProduct : sxData.getProductList()) {
                    if (ListUtils.notNull(cmsBtProduct.getCommon().getFields().getImages1())) {
                        int cnt = cmsBtProduct.getCommon().getFields().getImages1().size();
                        for (int i = cnt; i < 5; i++) {
                            cmsBtProduct.getCommon().getFields().getImages1().add(cmsBtProduct.getCommon().getFields().getImages1().get(0));
                        }
                    }
                }
            }

            // 主产品等列表取得
            CmsBtProductModel mainProduct = sxData.getMainProduct();
            List<CmsBtProductModel> cmsBtProductList = sxData.getProductList();
            List<BaseMongoMap<String, Object>> skuList = sxData.getSkuList();

            // 没有lock并且已Approved的产品列表为空的时候,中止该产品的上新流程
            if (ListUtils.isNull(cmsBtProductList)) {
                String errMsg = "未被锁定,已完成审批且品牌不在黑名单的产品列表为空";
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

            // 构造该产品所有SKUCODE的字符串列表
            List<String> strSkuCodeList = new ArrayList<>();
            skuList.forEach(sku -> strSkuCodeList.add(sku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.skuCode.name())));
            // 如果已Approved产品skuList为空，则中止该产品的上新流程；否则会把库存表里面所有的数据（几万条）数据全部查出来了，很花时间
            if (strSkuCodeList.isEmpty()) {
                String errMsg = "已完成审批的产品sku列表为空";
                $error(errMsg);
                throw new BusinessException(errMsg);
            }

            // 判断新增商品还是更新商品
            // 只要numIId不为空，则为更新商品
            if (!StringUtils.isEmpty(sxData.getPlatform().getNumIId())) {
                // 更新商品
                updateWare = true;
                // 取得更新对象商品id
                jdWareId = Long.parseLong(sxData.getPlatform().getNumIId());
            }

            // 如果skuList不为空，取得所有sku的库存信息
            // 为了对应MiniMall的场合， 获取库存的时候要求用getOrgChannelId()（其他的场合仍然是用channelId即可）
            Map<String, Integer> skuLogicQtyMap = productService.getLogicQty(mainProduct.getOrgChannelId(), strSkuCodeList);

            // delete by desmond 2016/12/26 start 暂时先注释掉，以后有可能还是要删除库存为0的SKU
//            // 删除主产品的common.skus中库存为0的SKU
//            if (ListUtils.notNull(sxData.getMainProduct().getCommonNotNull().getSkus())) {
//                deleteNoStockCommonSku(sxData.getMainProduct().getCommonNotNull().getSkus(), skuLogicQtyMap);
//            }
//            // 删除主产品的PXX.skus中库存为0的SKU
//            if (ListUtils.notNull(sxData.getMainProduct().getPlatformNotNull(cartId).getSkus())) {
//                deleteNoStockPlatformSku(sxData.getMainProduct().getPlatformNotNull(cartId).getSkus(), skuLogicQtyMap);
//            }
//            // 删除每个产品库存为0的sku
//            for (CmsBtProductModel cmsBtProduct : sxData.getProductList()) {
//                if (ListUtils.notNull(cmsBtProduct.getCommonNotNull().getSkus())) {
//                    deleteNoStockCommonSku(cmsBtProduct.getCommonNotNull().getSkus(), skuLogicQtyMap);
//                }
//                if (ListUtils.notNull(cmsBtProduct.getPlatformNotNull(cartId).getSkus())) {
//                    deleteNoStockPlatformSku(cmsBtProduct.getPlatformNotNull(cartId).getSkus(), skuLogicQtyMap);
//                }
//            }
            // delete by desmond 2016/12/26 end

            // 计算该商品下所有产品所有SKU的逻辑库存之和，新增时如果所有库存为0，报出不能上新错误
            int totalSkusLogicQty = 0;
            for(String skuCode : skuLogicQtyMap.keySet()) {
                totalSkusLogicQty += skuLogicQtyMap.get(skuCode);
            }

//            StringBuilder sbFailCause = new StringBuilder("");
            // delete by desmond 2016/12/26 start 暂时先注释掉，以后有可能还是要删除库存为0的SKU
//            // 京东上新的时候，以前库存为0的SKU也上，现在改为不上库存为0的SKU不要上新
//            // 无库存的skuId列表（包含之前卖，现在改为不在京东上售卖的SKU）,只在更新的时候用
//            List<String> skuIdListNoStock = new ArrayList<>();
//            Iterator<BaseMongoMap<String, Object>> skuIter = skuList.iterator();
//            while (skuIter.hasNext()) {
//                BaseMongoMap<String, Object> sku = skuIter.next();
//                // 如果该skuCode对应的库存为0(或库存信息不存在)，则把该sku加到skuIdListNoStock列表中，后面会删除京东平台上该SKU
//                if (isSkuNoStock(sku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.skuCode.name()), skuLogicQtyMap)) {
//                    // 删除SxData.skuList里面库存为0的SKU，这样后面上新的时候，从这个列表中取得的都是有库存的SKU
//                    skuIter.remove();
//
//                    // 把库存为0的SKU的jdSkuId加到skuIdListNoStock列表中，后面会一起再京东平台上删除这些库存为0的SKU
//                    if (!StringUtils.isEmpty(sku.getStringAttribute("jdSkuId"))) {
//                        skuIdListNoStock.add(sku.getStringAttribute("jdSkuId"));
//                    } else if (!StringUtils.isEmpty(sku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.skuCode.name()))){
//                        sbFailCause.setLength(0);
//                        // 如果本地数据库中没有jdSkuId,根据skuCode到京东平台上重新取得京东skuId
//                        Sku currentSku = jdSkuService.getSkuByOuterId(shopProp,
//                                sku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.skuCode.name()), sbFailCause);
//                        if (currentSku != null) {
//                            skuIdListNoStock.add(StringUtils.toString(currentSku.getSkuId()));
//                        }
//                    }
//                }
//            }
            // delete by desmond 2016/12/26 end

            // 取得主产品京东平台设置信息(包含SKU等信息)
            CmsBtProductModel_Platform_Cart mainProductPlatformCart = mainProduct.getPlatform(sxData.getCartId());
            if (mainProductPlatformCart == null) {
                $error(String.format("获取主产品京东平台设置信息(包含SKU，Schema属性值等信息)失败！[ProductCode:%s][CartId:%s]",
                        mainProduct.getCommon().getFields().getCode(), sxData.getCartId()));
                throw new BusinessException("获取主产品京东平台设置信息(包含SKU，Schema属性值等信息)失败");
            }

            // 取得京东共通Schema(用于设定京东商品标题，长宽高重量等共通属性)
            String catId = "1";          // 类目shema表中京东共通属性的catId为"1"
            // 取得平台类目schema信息
            CmsMtPlatformCategorySchemaModel jdCommonSchema = platformCategoryService.getPlatformCatSchema(catId, cartId);
            if (jdCommonSchema == null) {
                $error(String.format("获取京东共通schema信息失败 [类目Id:%s] [CartId:%s]", catId, cartId));
                throw new BusinessException("获取京东共通schema信息失败 [类目(" + catId + ":京东共通)]");
            }

            // 取得主产品的京东平台类目(用于取得京东平台该类目下的Schema信息)
            String platformCategoryId = mainProductPlatformCart.getpCatId();
            if (updateWare) {
                // added by morse.lu 2016/09/02 start
                // 取一下最新的jd上的商品信息，可能catId被jd的管理员改掉了，就不能上了，如果改了，回写下表(pCatId,pCatPath)
                String jdCatId = jdWareNewService.getJdProductCatId(shopProp, String.valueOf(jdWareId));
                if (!StringUtils.isEmpty(jdCatId)) {
                    if (!jdCatId.equals(platformCategoryId)) {
                        // 不一样
                        platformCategoryId = jdCatId;
                        // 回写
                        // 这一版回写完还是会直接尝试上新
                        updateCategory(channelId, cartId, mainProduct.getCommon().getFields().getCode(), jdCatId);
                    }
                } else {
                    // 有错误，没取到
                    // 先不管，继续做下去吧
                }
                // added by morse.lu 2016/09/02 end
            } else {
                // 新增商品的场合， 如果类目没填， 并且是智能上新的话， 想想办法能不能设置一下
                if (StringUtils.isEmpty(platformCategoryId)) {
                    if (blnIsSmartSx) {
                        // 先判断一下必要的条件
                        // 主产品主类目path
                        String mainCatPath = mainProduct.getCommonNotNull().getCatPath();
                        if (!StringUtils.isEmpty(mainCatPath) && MapUtils.isNotEmpty(categoryMappingListMap)) {
                            String brand = mainProduct.getCommonNotNull().getFieldsNotNull().getBrand();
                            String sizeType = mainProduct.getCommonNotNull().getFieldsNotNull().getSizeType();

                            // 匹配优先顺序：
                            // 1.主类目+品牌+适用人群
                            // 2.主类目+品牌
                            // 3.主类目+适用人群
                            // 4.主类目
                            String valCategory = getMainCategoryMappingInfo(mainCatPath, brand, sizeType, CmsBuildPlatformProductUploadTmTongGouService.TtPropName.tt_main_category, categoryMappingListMap);
                            if (StringUtils.isEmpty(valCategory)) {
                                valCategory = getMainCategoryMappingInfo(mainCatPath, brand, null, CmsBuildPlatformProductUploadTmTongGouService.TtPropName.tt_main_category, categoryMappingListMap);
                            }
                            if (StringUtils.isEmpty(valCategory)) {
                                valCategory = getMainCategoryMappingInfo(mainCatPath, null, sizeType, CmsBuildPlatformProductUploadTmTongGouService.TtPropName.tt_main_category, categoryMappingListMap);
                            }
                            if (StringUtils.isEmpty(valCategory)) {
                                valCategory = getMainCategoryMappingInfo(mainCatPath, null, null, CmsBuildPlatformProductUploadTmTongGouService.TtPropName.tt_main_category, categoryMappingListMap);
                            }

                            if (!StringUtils.isEmpty(valCategory)) {
                                platformCategoryId = valCategory;
                            }

                        }

                        if (StringUtils.isEmpty(platformCategoryId)) {
                            $error("未设置平台类目， 并且也没有自动匹配上类目");
                            throw new BusinessException("未设置平台类目， 并且也没有自动匹配上类目");
                        }

                    }
                }
            }
            $info("主产品的京东平台类目:" + platformCategoryId);
            // 取得平台类目schema信息
            CmsMtPlatformCategorySchemaModel cmsMtPlatformCategorySchema = platformCategoryService.getPlatformCatSchema(platformCategoryId, cartId);
            if (cmsMtPlatformCategorySchema == null) {
                $error(String.format("获取平台类目schema信息失败 [平台类目Id:%s] [CartId:%s]", platformCategoryId, cartId));
                throw new BusinessException("获取平台类目schema信息失败 [平台类目(" + platformCategoryId +
                        ":" + mainProductPlatformCart.getpCatPath() + ")]");
            }

            // 获取cms_mt_platform_skus表里渠道指定类目对应的所有颜色和尺寸信息列表
            List<CmsMtPlatformSkusModel> cmsMtPlatformSkusList = cmcMtPlatformSkusService.getModesByAttrType(channelId, cartId, platformCategoryId, AttrType_Active_1);
            if (ListUtils.isNull(cmsMtPlatformSkusList)) {
                $error(String.format("平台skus表中该商品类目对应的颜色和尺寸信息不存在 [ChannelId:%s] [CartId:%s] [PlatformCategoryId:%s] [PlatformCategoryPath:%s]",
                        channelId, cartId, platformCategoryId, mainProductPlatformCart.getpCatPath()));
                throw new BusinessException("平台skus表中该商品类目对应的颜色和尺寸信息不存在 [平台类目(" + platformCategoryId +
                        ":" + mainProductPlatformCart.getpCatPath() + ")]");
            }

            // 获取字典表(根据channel_id)上传图片的规格等信息
            List<CmsMtPlatformDictModel> cmsMtPlatformDictModelList = dictService.getModesByChannelCartId(channelId, cartId);
            if (cmsMtPlatformDictModelList == null || cmsMtPlatformDictModelList.size() == 0) {
                $error(String.format("获取字典表数据（图片规格及详情页等）失败 [ChannelId:%s] [CartId:%s]", channelId, cartId));
                throw new BusinessException("获取字典表数据（图片规格及详情页等）失败");
            }

            // 编辑京东共通属性
            JdProductNewBean jdProductBean = setJdProductCommonInfo(sxData, platformCategoryId, groupId, shopProp,
                    jdCommonSchema, cmsMtPlatformCategorySchema, skuLogicQtyMap, blnIsSmartSx);
            // 更新时设置商品id
            if (!StringUtils.isEmpty(sxData.getPlatform().getNumIId())) {
                jdProductBean.setWareId(Long.parseLong(sxData.getPlatform().getNumIId()));
            }

            // 取得cms_mt_platform_skus表里平台类目id对应的颜色信息列表(按idx升序排列)
            List<CmsMtPlatformSkusModel> cmsColorList = cmsMtPlatformSkusList
                    .stream()
                    .filter(s -> AttrType_Color.equals(s.getAttrType()))
                    .sorted(Comparator.comparing(s -> s.getIdx()))
                    .collect(Collectors.toList());

            // 取得cms_mt_platform_skus表里平台类目id对应的尺寸信息列表(按idx升序排列)
            List<CmsMtPlatformSkusModel> cmsSizeList = cmsMtPlatformSkusList
                    .stream()
                    .filter(s -> AttrType_Size.equals(s.getAttrType()))
                    .sorted(Comparator.comparing(s -> s.getIdx()))
                    .collect(Collectors.toList());

            // 当前平台主类目对应的销售属性状况(1:颜色和尺寸属性都有 2:只有颜色没有尺寸属性 3:没有颜色只有尺寸属性 4:没有颜色没有尺寸属性)
            String salePropStatus = getSalePropStatus(cmsColorList, cmsSizeList, mainProduct.getOrgChannelId());

            // 产品和颜色值的Mapping关系表(设置SKU属性时填入值，上传SKU图片时也会用到)
            Map<String, Object> productColorMap = new HashMap<>();

            // charis update
            // 保存sku信息 返回销售属性别名
            Set<Prop> allSkuSaleProps = doSaveWareSkus(shopProp, jdProductBean, sxData, productColorMap, skuLogicQtyMap,
                    cmsColorList, cmsSizeList, salePropStatus, channelConfigValueMap);

            // 图片信息（图片尺寸为800*800，单张大小不超过 1024K）(必须)
            //charis update


        // 产品5张图片名称列表(TODO 暂时为固定值，以后会改成从表中读取)
        List<String> mainPicNameList = new ArrayList<>();
        mainPicNameList.add("京东产品图片-1");
        mainPicNameList.add("京东产品图片-2");
        mainPicNameList.add("京东产品图片-3");
        mainPicNameList.add("京东产品图片-4");
        mainPicNameList.add("京东产品图片-5");
        String getMainPicErr = "";
        // 循环取得5张图片的url并分别上传到京东
        String picUrl = "";
        ExpressionParser expressionParser = new ExpressionParser(sxProductService, sxData);
        for (String picName : mainPicNameList) {
            try {
                // 取得图片url
                picUrl = sxProductService.resolveDict(picName, expressionParser, shopProp, getTaskName(), null);
                if (StringUtils.isEmpty(picUrl)) continue;
                break;
            } catch (Exception ex) {
                String errMsg = String.format("京东取得商品主图信息失败！[ChannelId:%s] [CartId:%s] [GroupId:%s] [PlatformCategoryId:%s] [PicName:%s]",
                        channelId, cartId, groupId, platformCategoryId, picName);
                $error(errMsg, ex);
                getMainPicErr = "京东取得商品主图信息失败 [图片URL:" + picUrl + "]";
                // 继续取下一张图片
            }

        }
        //charis update
        List<Image> imageList = new ArrayList<>();
        Set<String> picSet = new HashSet<>();
        picSet.add(picUrl);
        Map<String,String> imageMap = sxProductNewService.uploadImage(shopProp.getOrder_channel_id(), Integer.parseInt(shopProp.getCart_id()),
                String.valueOf(sxData.getGroupId()), shopProp, picSet, getTaskName());
        for (int i=0;i<5;i++) {
            Image image = new Image();
            image.setColorId("0000000000");
            image.setImgIndex(i + 1);
            image.setImgUrl(imageMap.get(picUrl));
            imageList.add(image);
        }
        // 新增所有产品的属性图片
        List<Image> skuImageList = uploadJdProductAddPics(shopProp, sxData, productColorMap, salePropStatus, jdProductBean);
        if (skuImageList == null) {
            String errMsg = String.format("新增商品的每种颜色5张非主图片均获取失败! [GroupId:%s]", sxData.getGroupId());
            $error(errMsg);
            // 如果上新数据中的errorMessage为空
            throw new BusinessException(errMsg);
        }
        imageList.addAll(skuImageList);
        jdProductBean.setImages(imageList);
        // 图片信息（图片尺寸为800*800，单张大小不超过 1024K）(必须)
//        if (bytes == null) {
//            throw new BusinessException(getMainPicErr);
//        }
//        jdProductBean.setWareImage(bytes);
//        jdProductBean.setImages();
//            // 上传商品主图
//            List<String> mainPicNameList = new ArrayList<>();
//            mainPicNameList.add("京东产品图片-2");  // TODO 暂时用固定值，以后改为从表里取
//            mainPicNameList.add("京东产品图片-3");
//            mainPicNameList.add("京东产品图片-4");
//            mainPicNameList.add("京东产品图片-5");
//
//            // 取得图片URL参数
//            ExpressionParser expressionParser = new ExpressionParser(sxProductService, sxData);
//            // 京东要求图片必须是5张，商品主图的第一张已经在前面的共通属性里面设置了，这里最多只需要设置4张非主图
//            for (String picName : mainPicNameList) {
//                String picUrl = "";
//                try {
//                    // 取得图片URL
//                    picUrl = sxProductService.resolveDict(picName, expressionParser, shopProp, getTaskName(), null);
//                    if (StringUtils.isEmpty(picUrl)) continue;
//                    // 上传主产品的其余4张非主图片
//                    jdWareNewService.addWarePropimg(shopProp, String.valueOf(jdWareId), ColorId_MainPic, picUrl, picName, false);
//                } catch (Exception ex) {
//                    // 有可能是因为新增商品时，如果该类目最少5张图片，京东自动的把上传的第一张图片复制成5张图，
//                    // 则传后续4张图时只有第2张能正常上传，后面都会报超过6张图片的错误
//                    $error("京东上传主商品非主图失败！[WareId:%s] [ColorId:%s] [PicName:%s] [PicUrl:%s]", jdWareId, ColorId_MainPic, picName, picUrl);
//                    $error(ex);
//                    // 继续上传下一张图片
//                }
//            }



            // 新增或更新商品主处理
            if (!updateWare) {
                // 新增商品的时候
                // 京东没有库存时不能上新，如果所有产品所有SKU的库存之和为0时，直接报错
                if (totalSkusLogicQty == 0) {
                    String errMsg = String.format("京东新增商品时所有SKU的总库存为0，不能上新！请添加库存信息之后再做上新. " +
                            "[ChannelId:%s] [CartId:%s] [GroupId:%s]", channelId, cartId, groupId);
                    $error(errMsg);
                    throw new BusinessException(errMsg);
                }
                // 新增商品时将商品状态设置为从未上架 charis
                jdProductBean.setWareStatus(OptionType_neverSale);
                // 调用京东新增商品API(360buy.ware.add)
                // charis update 发布商品api变更为 jingdong.ware.write.add
                jdWareId = jdWareNewService.addProduct(shopProp, jdProductBean);
                // 返回的商品id是0的时候，新增商品失败
                if (0 == jdWareId) {
                    $error(String.format("京东新增商品失败！[ChannelId:%s] [CartId:%s] [GroupId:%s] [PlatformCategoryId:%s]",
                            channelId, cartId, groupId, platformCategoryId));
                    throw new BusinessException("京东新增商品失败 [返回商品id=0]");
                }

                // 回写商品id(wareId->numIId)
                // 为了避免商品上传成功，但后续处理出现异常时，表中没有numIId信息，所以要先回写numIId，后面上新失败再改为空
                updateProductGroupNumIId(sxData, String.valueOf(jdWareId));

//                // 上传商品主图
//                List<String> mainPicNameList = new ArrayList<>();
//                mainPicNameList.add("京东产品图片-2");  // TODO 暂时用固定值，以后改为从表里取
//                mainPicNameList.add("京东产品图片-3");
//                mainPicNameList.add("京东产品图片-4");
//                mainPicNameList.add("京东产品图片-5");
//
//                // 取得图片URL参数
//                ExpressionParser expressionParser = new ExpressionParser(sxProductService, sxData);
//                // 京东要求图片必须是5张，商品主图的第一张已经在前面的共通属性里面设置了，这里最多只需要设置4张非主图
//                for (String picName : mainPicNameList) {
//                    String picUrl = "";
//                    try {
//                        // 取得图片URL
//                        picUrl = sxProductService.resolveDict(picName, expressionParser, shopProp, getTaskName(), null);
//                        if (StringUtils.isEmpty(picUrl)) continue;
//                        // 上传主产品的其余4张非主图片
//                        jdWareNewService.addWarePropimg(shopProp, String.valueOf(jdWareId), ColorId_MainPic, picUrl, picName, false);
//                    } catch (Exception ex) {
//                        // 有可能是因为新增商品时，如果该类目最少5张图片，京东自动的把上传的第一张图片复制成5张图，
//                        // 则传后续4张图时只有第2张能正常上传，后面都会报超过6张图片的错误
//                        $error("京东上传主商品非主图失败！[WareId:%s] [ColorId:%s] [PicName:%s] [PicUrl:%s]", jdWareId, ColorId_MainPic, picName, picUrl);
//                        $error(ex);
//                        // 继续上传下一张图片
//                    }
//                }

                try {
//                    // 新增商品成功后，调用京东全量保存SKU接口设置SKU属性(包括颜色和尺码别名)
//                    boolean saveWareSkusResult = doSaveWareSkus(shopProp, jdWareId, sxData, productColorMap, skuLogicQtyMap,
//                            cmsColorList, cmsSizeList, salePropStatus, channelConfigValueMap);
//                    // 新增之后更新商品SKU信息成功
//                    if (!saveWareSkusResult) {
//                        String errMsg = String.format("京东新增商品时，调用京东全量保存SKU接口设置SKU属性失败! [ChannelId:%s] [CartId:%s] [GroupId:%s] [WareId:%s]",
//                                channelId, cartId, groupId, jdWareId);
//                        $error(errMsg);
//                        throw new BusinessException(errMsg);
//                    }
                    //  更新商品维度的销售属性值别名
                    StringBuilder failCause = new StringBuilder();
                    failCause.setLength(0);
                    boolean blnResult = jdWareNewService.updateWareSaleAttrvalueAlias(shopProp, jdWareId, allSkuSaleProps, failCause);
                    if (!blnResult || !StringUtils.isEmpty(failCause.toString())) {
                        $warn(failCause.toString());
                    }

//                    // 新增商品SKU信息成功之后，上传该商品下所有产品的图片
//                    boolean retStatus = uploadJdProductAddPics(shopProp, jdWareId, sxData, productColorMap, salePropStatus);
//                    if (!retStatus) {
//                        String errMsg = String.format("新增商品的每种颜色5张非主图片均上传失败! [WareId:%s]", jdWareId);
//                        $error(errMsg);
//                        // 如果上新数据中的errorMessage为空
//                        throw new BusinessException(errMsg);
//                    }
                } catch (Exception e) {
                    // 设置异常信息
                    sxData.setErrorMessage(e.getMessage());

                    // 新增之后更新商品SKU信息失败之后，删除京东平台上的该商品
                    // 参数：1.ware_id 2.trade_no(流水号：现在时刻)
                    jdWareNewService.deleteWare(shopProp, String.valueOf(jdWareId), DateTimeUtil.getNowTimeStamp());

                    // 删除group表中的商品id(numIId)
                    updateProductGroupNumIId(sxData, "");

                    String errMsg = String.format("京东新增单个商品成功之后，全量保存SKU信息或商品每种颜色上传5张非主图片失败！[ChannelId:%s] [CartId:%s] [GroupId:%s] [WareId:%s]",
                        channelId, cartId, groupId, jdWareId);
                    $error(errMsg);
                    throw new BusinessException(errMsg);
                }
            } else {
                // 更新商品时
                // 如果所有产品所有SKU的总库存为0时，先全量保存SKU信息，再直接做下架处理，不报错

                // 更新商品时，调用京东商品更新API，更新SKU属性之外的一些共通属性,返回更新时间
                boolean result = jdWareNewService.updateProduct(shopProp, jdProductBean);
                if (!result) {
                    // 更新失败时抛出异常
                    String errMsg = String.format("京东更新商品共通信息(不包含SKU信息)失败! [ChannelId:%s] [CartId:%s] [GroupId:%s] [WareId:%s]",
                            channelId, cartId, groupId, jdWareId);
                    $error(errMsg);
                    throw new BusinessException(errMsg);
                }

                try {
//                调用京东全量保存SKU接口，更新当前商品的SKU信息
                    StringBuilder failCause = new StringBuilder();
                    List<Sku> resultSkus = jdWareNewService.saveWareSkus(shopProp, jdWareId, jdProductBean.getSkus(), failCause);
                    if (ListUtils.isNull(resultSkus) || !StringUtils.isEmpty(failCause.toString())) {
                        $error(failCause.toString());
                        throw new BusinessException(failCause.toString());
                    }
//                    // 更新商品共通信息成功后，调用京东全量保存SKU接口设置SKU属性(包括颜色和尺码别名)
//                    boolean saveWareSkusResult = doSaveWareSkus(shopProp, jdWareId, sxData, productColorMap, skuLogicQtyMap,
//                            cmsColorList, cmsSizeList, salePropStatus, channelConfigValueMap);
                    // 新增之后更新商品SKU信息成功
//                    if (!saveWareSkusResult) {
//                        String errMsg = String.format("京东更新商品时，调用京东全量保存SKU接口设置SKU属性失败! [ChannelId:%s] [CartId:%s] [GroupId:%s] [WareId:%s]",
//                                channelId, cartId, groupId, jdWareId);
//                        throw new BusinessException(errMsg);
//                    }
                } catch (Exception e) {
                    // 设置异常信息
                    sxData.setErrorMessage(e.getMessage());

                    String errMsg = String.format("京东更新单个商品共通信息成功之后，全量保存SKU信息失败！[ChannelId:%s] [CartId:%s] [GroupId:%s] [WareId:%s]",
                            channelId, cartId, groupId, jdWareId);
                    $error(errMsg);
                    throw new BusinessException(errMsg);
                }

                // 更新商品是否成功
                // 更新商品成功后，更新该商品下所有产品的图片
                boolean retStatus = uploadJdProductUpdatePics(shopProp, jdWareId, jdProductBean, salePropStatus);
                if (!retStatus) {
                    String errMsg = String.format("京东更新商品的产品5张非主图失败! [WareId:%s]", jdWareId);
                    $error(errMsg);
                    throw new BusinessException(errMsg);
                }
            }

            // 新增/更新商品没有报异常的时候，给商品打标（设置"7天无理由退货"等特殊features属性）,出错报异常让运营感知到异常
            // charis update
//            doMergeWareFeatures(shopProp, jdProductBean, jdWareId);

            // 设置京东运费模板
            // charis update
//            updateJdWareTransportId(shopProp, sxData, jdWareId);
            // 设置京东关联板式
            // charis update
//            updateJdWareLayoutId(shopProp, sxData, jdWareId);

            // 执行商品上架/下架操作(如果SKU总库存为0，直接下架)
            CmsConstants.PlatformStatus platformStatus = null;
            if (totalSkusLogicQty == 0) {
                // 总库存为0,强制下架并回写下架状态，记录上下架履历
                // 上新对象产品Code列表
                List<String> sxCodeList = sxData.getProductList().stream().map(p -> p.getCommon().getFields().getCode()).collect(Collectors.toList());
                doJdForceWareListing(shopProp, jdWareId, groupId, CmsConstants.PlatformActive.ToInStock, sxCodeList, updateWare, "京东上新SKU总库存为0时强制下架处理");
            } else {
                // 如果SKU总库存不为0时，根据group表里设置的Action设置上下架状态
                boolean updateJdWareListing = updateJdWareListing(shopProp, sxData, jdWareId, updateWare);
                // 新增或更新商品，只有在商品上架/下架操作成功之后才回写platformStatus，失败不回写状态(新增商品时除外)
                if (updateJdWareListing) {
                    // 上架/下架操作成功时
                    // platformActive平台上新状态类型(ToOnSale/ToInStock)
                    if (CmsConstants.PlatformActive.ToOnSale.equals(sxData.getPlatform().getPlatformActive())) {
                        // platformActive是(ToOnSale)时，把platformStatus更新成"OnSale"
                        platformStatus = CmsConstants.PlatformStatus.OnSale;
                    } else {
                        platformStatus = CmsConstants.PlatformStatus.InStock;
                    }
                } else {
                    // 商品上架/下架失败的时候，新增商品时一律回写成"InStock"(默认),更新商品时不回写状态
                    if (!updateWare) {
                        // 新增商品之后商品上架/下架失败时，把platformStatus更新成"InStock"(默认)
                        platformStatus = CmsConstants.PlatformStatus.InStock;
                    }
                }
            }

            // 上新成功时状态回写操作
            sxProductService.doUploadFinalProc(shopProp, true, sxData, cmsBtSxWorkloadModel, String.valueOf(jdWareId), platformStatus, "", getTaskName());

            // 上新成功之后回写jdSkuId操作
            updateSkuIds(shopProp, StringUtils.toString(jdWareId), updateWare);

            // added by morse.lu 2016/12/08 start
            if (ChannelConfigEnums.Channel.SN.getId().equals(channelId)) {
                // Sneakerhead
                try {
                    sxProductService.uploadCnInfo(sxData);
                } catch (IOException io) {
                    throw new BusinessException("上新成功!但在推送给美国数据库时发生异常!"+ io.getMessage());
                }
            }
        } catch (Exception ex) {
            // 异常结束时
            String errMsg = String.format("京东单个商品新增或更新信息异常结束！[ChannelId:%s] [CartId:%s] [GroupId:%s] [WareId:%s]",
                    channelId, cartId, groupId, jdWareId);
            $error(errMsg);

            if (sxData == null) {
                // 回写详细错误信息表(cms_bt_business_log)用
                sxData = new SxData();
                sxData.setChannelId(channelId);
                sxData.setCartId(cartId);
                sxData.setGroupId(groupId);
            }
            if (ex instanceof BusinessException) {
                if (StringUtils.isEmpty(sxData.getErrorMessage())) {
                    sxData.setErrorMessage(((BusinessException)ex).getMessage());
                }
            }
            // 如果上新数据中的errorMessage为空
            if (StringUtils.isEmpty(sxData.getErrorMessage())) {
                // nullpoint错误的处理
                if(StringUtils.isNullOrBlank2(ex.getMessage())) {
                    sxData.setErrorMessage("出现不可预知的错误，请跟管理员联系! " + ex.getStackTrace()[0].toString());
                    ex.printStackTrace();
                } else {
                    sxData.setErrorMessage(ex.getMessage());
                }
            }

            if (sxData.getErrorMessage().contains(shopProp.getShop_name())) {
                sxData.setErrorMessage(sxData.getErrorMessage().replace(shopProp.getShop_name(), getPreMsg(shopProp.getShop_name(), sxType)));
            } else {
                sxData.setErrorMessage(getPreMsg(shopProp.getShop_name(), sxType) + sxData.getErrorMessage());
            }

            // 上新出错时状态回写操作
            sxProductService.doUploadFinalProc(shopProp, false, sxData, cmsBtSxWorkloadModel, "", null, "", getTaskName());

            // 更新商品出错时，也要设置运费模板和关联板式
            if (updateWare && !(sxData.getErrorMessage().contains("11200014:操作非法：商品已经删除"))) {
                try {
                    // 设置京东运费模板
                    updateJdWareTransportId(shopProp, sxData, jdWareId);
                    // 设置京东关联板式
                    updateJdWareLayoutId(shopProp, sxData, jdWareId);
                } catch (Exception e) {
                    // 什么都不做，保证不要报异常
                }
            }
            return;
        }

        // 正常结束
        if (!updateWare) {
            $info(String.format("%s京东单个商品新增信息成功！[ChannelId:%s] [CartId:%s] [GroupId:%s] [WareId:%s]",
                    getPreMsg(shopProp.getShop_name(), sxType), channelId, cartId, groupId, jdWareId));
        } else {
            $info(String.format("%s京东单个商品更新信息成功！[ChannelId:%s] [CartId:%s] [GroupId:%s] [WareId:%s]",
                    getPreMsg(shopProp.getShop_name(), sxType), channelId, cartId, groupId, jdWareId));
        }

    }

    /**
     * 取得主类目到天猫一级类目的匹配结果
     *
     * @param mainCatPath 主类目
     * @param brand 品牌
     * @param sizeType 适用人群
     * @param ttPropName 匹配方式
     * @param categoryMappingListMap 当前渠道和平台设置类目和天猫平台类目(叶子类目或平台一级类目)匹配信息列表map
     * @return List<CmsMtChannelConditionMappingConfigModel> 表中的配置mapping信息
     */
    protected String getMainCategoryMappingInfo(String mainCatPath, String brand, String sizeType,
                                                CmsBuildPlatformProductUploadTmTongGouService.TtPropName ttPropName, Map<String, List<Map<String, String>>> categoryMappingListMap) {
        if (mainCatPath == null
                || ttPropName == null
                || MapUtils.isEmpty(categoryMappingListMap)
                || !categoryMappingListMap.containsKey(ttPropName.name())
                || ListUtils.isNull(categoryMappingListMap.get(ttPropName.name()))) return null;

        Map<String, String> resultMap = categoryMappingListMap.get(ttPropName.name()).stream()
                .filter(m -> mainCatPath.equalsIgnoreCase(m.get(CmsBuildPlatformProductUploadTmTongGouService.TtItemName.t_key_category.name())))
                .filter(m -> (StringUtils.isEmpty(brand) || brand.equalsIgnoreCase(m.get(CmsBuildPlatformProductUploadTmTongGouService.TtItemName.t_key_brand.name()))))
                .filter(m -> (StringUtils.isEmpty(sizeType) || sizeType.equalsIgnoreCase(m.get(CmsBuildPlatformProductUploadTmTongGouService.TtItemName.t_key_sizeType.name()))))
                .findFirst()
                .orElse(null);

        if (MapUtils.isNotEmpty(resultMap)) {
            return resultMap.get(CmsBuildPlatformProductUploadTmTongGouService.TtItemName.t_value_category.name());
        }

        return null;
    }

    /**
     * 设置京东上新产品用共通属性
     * 不包含"sku属性","sku价格","sku库存","自定义属性值别名","SKU外部ID"的值
     *
     * @param sxData sxData 上新产品对象
     * @param platformCategoryId String     平台类目id
     * @param groupId long                  groupid
     * @param shopProp ShopBean             店铺信息
     * @param jdCommonSchema CmsMtPlatformCategorySchemaModel  京东共通schema数据
     * @param platformSchemaData CmsMtPlatformCategorySchemaModel  主产品类目对应的平台schema数据
     * @param skuLogicQtyMap Map<String, Integer>  SKU逻辑库存
     * @param blnIsSmartSx 是否强制使用智能上新
     * @return JdProductBean 京东上新用bean
     * @throws BusinessException
     */
    private JdProductNewBean setJdProductCommonInfo(SxData sxData, String platformCategoryId,
                                                 long groupId, ShopBean shopProp,
                                                 CmsMtPlatformCategorySchemaModel jdCommonSchema,
                                                 CmsMtPlatformCategorySchemaModel platformSchemaData,
                                                 Map<String, Integer> skuLogicQtyMap,
                                                 boolean blnIsSmartSx) throws BusinessException {
        CmsBtProductModel mainProduct = sxData.getMainProduct();
        List<BaseMongoMap<String, Object>> skuList = sxData.getSkuList();
        ExpressionParser expressionParser = new ExpressionParser(sxProductService, sxData);

        // 京东上新产品共通属性设定
        JdProductNewBean jdProductBean = new JdProductNewBean();
        // 如果去掉库存为0的sku之后一个sku都没有时，后面新增时报错/更新时下架，所有共通属性也不用设置了，直接返回空的JdProductBean
        if (ListUtils.isNull(skuList)) return jdProductBean;

        // 渠道id
        String channelId = sxData.getChannelId();
        // 平台id
        String cartId = sxData.getCartId().toString();

        // 取得京东共通属性值(包括标题，长宽高，重量等)
        Map<String, String> jdCommonInfoMap = getJdCommonInfo(jdCommonSchema, shopProp, expressionParser, blnIsSmartSx);

        // 流水号(非必须)
//        jdProductBean.setTradeNo(mainProduct.getXXX());                  // 不使用
        // 产地(非必须)
//        jdProductBean.setWareLocation(mainProduct.getCommon().getFields().getOrigin());
        // 类目id(必须)
        //charis update
        jdProductBean.setCategoryId(Long.parseLong(platformCategoryId));
        // 自定义店内分类(非必须)
        //charis update
        try {
            Set<Long> shopCagetory = this.getShopCategory(shopProp, sxData);
            jdProductBean.setShopCategorys(shopCagetory);
        } catch (Exception ex) {
            String errMsg = String.format("京东取得自定义店内分类失败！[ChannelId:%s] [CartId:%s] [GroupId:%s] [PlatformCategoryId:%s]",
                    channelId, cartId, groupId, platformCategoryId);
            $error(errMsg, ex);
        }
        // 商品标题(必须)
        jdProductBean.setTitle(jdCommonInfoMap.get("productTitle"));
        if (StringUtils.isEmpty(jdProductBean.getTitle()) || jdProductBean.getTitle().length() > 45) {
            String tmpBrand = sxData.getMainProduct().getCommonNotNull().getFieldsNotNull().getBrand();
            String tmpSizeType = sxData.getMainProduct().getCommonNotNull().getFieldsNotNull().getSizeTypeCn();
            if (StringUtils.isEmpty(tmpSizeType)) {
                tmpSizeType = sxData.getMainProduct().getCommonNotNull().getFieldsNotNull().getSizeType();
            }
            String tmpProductType = sxData.getMainProduct().getCommonNotNull().getFieldsNotNull().getProductTypeCn();
            if (StringUtils.isEmpty(tmpProductType)) {
                tmpProductType = sxData.getMainProduct().getCommonNotNull().getFieldsNotNull().getProductType();
            }
            jdProductBean.setTitle(tmpBrand + " " + tmpSizeType + " " + tmpProductType);

            if (StringUtils.isEmpty(jdProductBean.getTitle()) || jdProductBean.getTitle().length() > 45) {
                jdProductBean.setTitle(tmpBrand + " " + tmpProductType);
            }
        }
        // 7天无理由退货 1为支持，0为不支持 (非必须,不设置京东会默认为"支持")
//        jdProductBean.setIs7ToReturn(jdCommonInfoMap.get("productIs7ToReturn"));
        //charis update
        Set<Feature> featureSet = new HashSet<>();
        Feature feature = new Feature();
        feature.setKey(Is7ToReturn);
        feature.setValue(jdCommonInfoMap.get("productIs7ToReturn"));
        featureSet.add(feature);
        jdProductBean.setFeatures(featureSet);
        // UPC编码(非必须)
//        jdProductBean.setUpcCode(mainProduct.getXXX());                 // 不使用
        // 操作类型 现只支持：offsale 或onsale,默认为下架状态 (非必须)
        // 如果这里设成OnSale，新增商品时会直接上架，在售商品不能删除
        // charis update
        // 商品状态 1:从未上架 2:自主下架 4:系统下架 8:上架 513:从未上架待审 514:自主下架待审 516:系统下架待审 520:上架待审核 1028:系统下架审核失败
        jdProductBean.setWareStatus(OptioinType_offsale);
        // 外部商品编号，对应商家后台货号(非必须)
        String productModel = null;
        if (mainProduct.getPlatform(sxData.getCartId()) != null) {
            if (mainProduct.getPlatform(sxData.getCartId()).getFields() != null) {
                productModel = mainProduct.getPlatform(sxData.getCartId()).getFields().getStringAttribute("productModel");
            }
        }
        if (StringUtils.isEmpty(productModel)) {
            // 默认使用model来设置
            jdProductBean.setItemNum(mainProduct.getCommon().getFields().getModel());
        } else {
            // 如果有填了的话, 那就用运营自己填写的来设置
            jdProductBean.setItemNum(productModel);
        }
        // 库存(必须)
        // 计算该产品所有SKU的逻辑库存之和
        Long skuTotalLogicQty = 0L;
        for(String skuCode : skuLogicQtyMap.keySet()) {
            skuTotalLogicQty += skuLogicQtyMap.get(skuCode);
        }
        jdProductBean.setStockNum(skuTotalLogicQty);
        // 生产厂商(非必须)
//        jdProductBean.setProducter(mainProduct.getXXX());                // 不使用
        // 包装规格 (非必须)
//        jdProductBean.setWrap(mainProduct.getXXX());                     // 不使用

		// 取得一下product默认属性
		Map<String, Field> productSchemaFields = SchemaReader.readXmlForMap(jdCommonSchema.getPropsProduct());
        //charis update
        // 长(单位:mm)(必须)
        if (StringUtils.isEmpty(jdCommonInfoMap.get("productLengthMm"))) {
        	InputField f = (InputField) productSchemaFields.get("productLengthMm");
            if(!StringUtils.isEmpty(f.getDefaultValue()))
                jdProductBean.setLength(Integer.parseInt(f.getDefaultValue()));
		}else {
            jdProductBean.setLength(Integer.parseInt(jdCommonInfoMap.get("productLengthMm")));
        }
        // 宽(单位:mm)(必须)
        //charis update
		if (StringUtils.isEmpty(jdCommonInfoMap.get("productWideMm"))) {
			InputField f = (InputField) productSchemaFields.get("productWideMm");
            if(!StringUtils.isEmpty(f.getDefaultValue()))
			    jdProductBean.setWidth(Integer.parseInt(f.getDefaultValue()));
		}else {
            jdProductBean.setWidth(Integer.parseInt(jdCommonInfoMap.get("productWideMm")));
        }
        // 高(单位:mm)(必须)
        //charis update
		if (StringUtils.isEmpty(jdCommonInfoMap.get("productHighMm"))) {
			InputField f = (InputField) productSchemaFields.get("productHighMm");
            if(!StringUtils.isEmpty(f.getDefaultValue()))
			    jdProductBean.setHeight(Integer.parseInt(f.getDefaultValue()));
		}else {
            jdProductBean.setHeight(Integer.parseInt(jdCommonInfoMap.get("productHighMm")));
        }
        // 重量(单位:kg)(必须)
//        Object objfieldItemValue = null;
//        String strWeight = "1";  // 默认为1kg
//        objfieldItemValue = sxProductService.getPropValue(mainProduct.getPlatform(sxData.getCartId()).getFields(), "productWeightKg");
//         // 取得值为null不设置，空字符串的时候还是要设置（可能是更新时特意把某个属性的值改为空）
//        if (objfieldItemValue != null && objfieldItemValue instanceof String) {
//            strWeight = String.valueOf(objfieldItemValue);
//        }
        //charis update
        if (StringUtils.isEmpty(jdCommonInfoMap.get("productWeightKg"))) {
            InputField f = (InputField) productSchemaFields.get("productWeightKg");
            if(!StringUtils.isEmpty(f.getDefaultValue()))
                jdProductBean.setWeight(Float.parseFloat(f.getDefaultValue()));
        }else {
            jdProductBean.setWeight(Float.parseFloat(jdCommonInfoMap.get("productWeightKg")));
            if(jdProductBean.getWeight() == 0)
                jdProductBean.setWeight(1F);
        }
        // 进货价,精确到2位小数，单位:元(非必须)
//        jdProductBean.setCostPrice(String.valueOf(jdPrice));     // 不使用
        // 市场价, 精确到2位小数，单位:元(必须)
        Double marketPrice = getItemPrice(skuList, channelId, cartId, CmsConstants.ChannelConfig.PRICE_RETAIL_KEY, CmsConstants.ChannelConfig.PRICE_RETAIL_PRICE_CODE);
        //charis update
        jdProductBean.setMarketPrice(BigDecimal.valueOf(marketPrice));
        // 京东价,精确到2位小数，单位:元(必须)
        Double jdPrice = getItemPrice(skuList, channelId, cartId, CmsConstants.ChannelConfig.PRICE_SALE_KEY, CmsConstants.ChannelConfig.PRICE_SALE_PRICE_CODE);
        sxData.setMaxPrice(jdPrice);
        //charis update
        jdProductBean.setJdPrice(BigDecimal.valueOf(jdPrice));

        // 描述（最多支持3万个英文字符(必须)
        String strNotes = "";
        try {
            // 取得描述
            // added by morse.lu 2016/08/16 start
            RuleExpression ruleDetails = new RuleExpression();
            MasterWord masterWord = new MasterWord("details");
            ruleDetails.addRuleWord(masterWord);
            String details = expressionParser.parse(ruleDetails, shopProp, getTaskName(), null);
            if (!StringUtils.isEmpty(details)) {
                strNotes = sxProductService.resolveDict(details, expressionParser, shopProp, getTaskName(), null);
                if (StringUtils.isEmpty(strNotes)) {
                    String errorMsg = String.format("详情页描述[%s]在dict表里未设定!", details);
                    sxData.setErrorMessage(errorMsg);
                    throw new BusinessException(errorMsg);
                }
            } else {
                // added by morse.lu 2016/08/16 end
                strNotes = sxProductService.resolveDict("京东详情页描述", expressionParser, shopProp, getTaskName(), null);
            }
        } catch (Exception ex) {
            String errMsg = String.format("京东取得详情页描述信息失败！[errMsg:%s]", ex.getMessage());
            $error(errMsg);
            throw new BusinessException(errMsg);
        }
        //charis update
        jdProductBean.setIntroduction(strNotes);

        // 图片信息（图片尺寸为800*800，单张大小不超过 1024K）(必须)
        //charis update


//        // 产品5张图片名称列表(TODO 暂时为固定值，以后会改成从表中读取)
//        List<String> mainPicNameList = new ArrayList<>();
//        mainPicNameList.add("京东产品图片-1");
//        mainPicNameList.add("京东产品图片-2");
//        mainPicNameList.add("京东产品图片-3");
//        mainPicNameList.add("京东产品图片-4");
//        mainPicNameList.add("京东产品图片-5");
//        byte[] bytes = null;
//
//        String getMainPicErr = "";
//        // 循环取得5张图片的url并分别上传到京东
//        for (String picName : mainPicNameList) {
//            String picUrl = "";
//            try {
//                // 取得图片url
//                picUrl = sxProductService.resolveDict(picName, expressionParser, shopProp, getTaskName(), null);
//                if (StringUtils.isEmpty(picUrl)) continue;
//                //charis update
//                Set<String> picSet = new HashSet<>();
//                picSet.add(picUrl);
//                Map<String,String> imageMap = sxProductNewService.uploadImage(shopProp.getOrder_channel_id(), Integer.parseInt(shopProp.getCart_id()),
//                        String.valueOf(sxData.getGroupId()), shopProp, picSet, getTaskName());
//                List<Image> imageList = new ArrayList<>();
//                Image image = new Image();
//                image.setColorId();
//
//                // 读取图片
////                InputStream inputStream = jdWareNewService.getImgInputStream(picUrl, 3);
////                bytes = IOUtils.toByteArray(inputStream);
//
//                // 取得图片就退出循环
//                break;
//            } catch (Exception ex) {
//                String errMsg = String.format("京东取得商品主图信息失败！[ChannelId:%s] [CartId:%s] [GroupId:%s] [PlatformCategoryId:%s] [PicName:%s]",
//                        channelId, cartId, groupId, platformCategoryId, picName);
//                $error(errMsg, ex);
//                getMainPicErr = "京东取得商品主图信息失败 [图片URL:" + picUrl + "]";
//                // 继续取下一张图片
//            }
//        }
//        // 图片信息（图片尺寸为800*800，单张大小不超过 1024K）(必须)
//        if (bytes == null) {
//            throw new BusinessException(getMainPicErr);
//        }
//        jdProductBean.setWareImage(bytes);
//        jdProductBean.setImages();
        // 包装清单(非必须)
//        jdProductBean.setPackListing(mainProduct.getXXX());               // 不使用
        // 售后服务(非必须)
//        jdProductBean.setService(mainProduct.getXXX());                   // 不使用

        // 调用共通函数取得商品属性列表，用户自行输入的类目属性ID和用户自行输入的属性值Map
        Map<String, String> jdProductAttrMap = getJdProductAttributes(platformSchemaData, shopProp, expressionParser, blnIsSmartSx);
        // 商品属性列表,多组之间用|分隔，格式:aid:vid 或 aid:vid|aid1:vid1 或 aid1:vid1(必须)
        // 如输入类型input_type为1或2，则attributes为必填属性；如输入类型input_type为3，则用字段input_str填入属性的值

        //charis update
        Set<Prop> propSet = new HashSet<Prop>();
        for(String attr : jdProductAttrMap.get(Attributes).split("\\|")) {
            String attrValue = attr.split(":")[1];
            if ("null".equals(attrValue)) {
                continue;
            }
            Prop prop = new Prop();
            prop.setAttrId(attr.split(":")[0]);
            prop.setAttrValues(attr.split(":")[1].split(","));
            propSet.add(prop);
        }
        jdProductBean.setProps(propSet);

        // 是否先款后货,false为否，true为是 (非必须)
        //charis update
//        jdProductBean.setPayFirst(jdCommonInfoMap.get("productIsPayFirst"));
        // 发票限制：非必须输入，true为限制，false为不限制开增值税发票，FBP、LBP、SOPL、SOP类型商品均可输入(非必须)
        //charis update
//        jdProductBean.setCanVAT(jdCommonInfoMap.get("productIsCanVat"));
        // 是否进口商品：非必须输入，false为否，true为是，FBP类型商品可输入(非必须)
//        jdProductBean.setImported(mainProduct.getXXX());                   // 不使用
        // 是否保健品：非必须输入，false为否，true为是，FBP类型商品可输入  (非必须)
//        jdProductBean.setHealthProduct(mainProduct.getXXX());              // 不使用
        // 是否保质期管理商品, false为否，true为是(非必须)
//        jdProductBean.setShelfLife(mainProduct.getXXX());                  // 不使用
        // 保质期：非必须输入，0-99999范围区间，FBP类型商品可输入(非必须)
//        jdProductBean.setShelfLifeDays(mainProduct.getXXX());              // 不使用
        // 是否序列号管理：非必须输入，false为否，true为是，FBP类型商品可输入(非必须)
//        jdProductBean.setSerialNo(mainProduct.getXXX());                   // 不使用
        // 大家电购物卡：非必须输入，false为否，true为是，FBP类型商品可输入  (非必须)
//        jdProductBean.setAppliancesCard(mainProduct.getXXX());             // 不使用
        // 是否特殊液体：非必须输入，false为否，true为是，FBP、LBP、SOPL类型商品可输入(非必须)
//        jdProductBean.setSpecialWet(mainProduct.getXXX());                // 不使用
        // 商品件型：FBP类型商品必须输入，非FBP类型商品可输入非必填，0免费、1超大件、2超大件半件、3大件、4大件半件、5中件、6中件半件、7小件、8超小件(必须)
//        jdProductBean.setWareBigSmallModel(mainProduct.getXXX());         // 不使用
        // 商品包装：FBP类型商品必须输入，非FBP类型商品可输入非必填，1普通商品、2易碎品、3裸瓶液体、4带包装液体、5按原包装出库(必须)
//        jdProductBean.setWarePackType(mainProduct.getXXX());              // 不使用
        // 用户自行输入的类目属性ID串结构：‘pid1|pid2|pid3’,属性的pid调用360buy.ware.get.attribute取得, 输入类型input_type=3即输入(非必须)
        //charis update
//        jdProductBean.setInputPids(jdProductAttrMap.get(Input_Pids));
        // 用户自行输入的属性值,结构:‘输入值|输入值2|输入值3’图书品类输入值规则： ISBN：数字、字母格式 出版时间：日期格式“yyyy-mm-dd”
        // 版次：数字格式 印刷时间：日期格式“yyyy-mm-dd” 印次：数字格式 页数：数字格式 字数：数字格式 套装数量：数字格式 附件数量：数字格式(非必须)
        //charis update
//        jdProductBean.setInputStrs(jdProductAttrMap.get(Input_Strs));
        // 是否输入验证码 true:是;false:否  (非必须)
        //charis update
//        jdProductBean.setHasCheckCode("false");
        // 商品标语(广告词)内容最大支持45个字符(非必须)
        //charis update
        AdWords adwords = new AdWords();
        if (StringUtils.isEmpty(jdCommonInfoMap.get("productAdContent"))) {
            adwords.setWords("");
        } else {
            adwords.setWords(jdCommonInfoMap.get("productAdContent"));
        }
        jdProductBean.setAdWords(adwords);
        // 定时上架时间 时间格式：yyyy-MM-dd HH:mm:ss;规则是大于当前时间，10天内。(非必须)
//        jdProductBean.setListTime(mainProduct.getXXX());                   // 不使用
        // 品牌id
        //charis update
        if (sxData.getBrandCode() != null)
            jdProductBean.setBrandId(Long.parseLong(sxData.getBrandCode()));

        // charis update
        // 京东运费模板id
        String wareTransportId = "";
        try {
            // 取得京东运费模板id
            wareTransportId = getConditionPropValue(shopProp, sxData, Prop_TransportId);
        } catch (Exception ex) {
            String errMsg = String.format("取得京东运费模板id失败！[ChannelId:%s] [CartId:%s] [Prop_TransportId:%s]",
                    shopProp.getOrder_channel_id(), shopProp.getCart_id(), Prop_TransportId);
            $info(errMsg, ex);
        }
        // 20160315 sneakerhead比较优秀， 写死判断 START
        if (ChannelConfigEnums.Channel.SN.getId().equals(channelId) && CartEnums.Cart.JD.getId().equals(cartId)) {
            if (mainProduct != null
                    && mainProduct.getCommonNotNull().getFieldsNotNull().getProductType() != null
                    && "Shoes".equals(mainProduct.getCommonNotNull().getFieldsNotNull().getProductType())) {
                wareTransportId = "39227"; // 鞋子
            } else {
                wareTransportId = "39060"; // 鞋子以外
            }
        } else if (ChannelConfigEnums.Channel.SN.getId().equals(channelId) && CartEnums.Cart.JG.getId().equals(cartId)) {
            if (mainProduct != null
                    && mainProduct.getCommonNotNull().getFieldsNotNull().getProductType() != null
                    && "Shoes".equals(mainProduct.getCommonNotNull().getFieldsNotNull().getProductType())) {
                wareTransportId = "231866"; // 鞋子
            } else {
                wareTransportId = "235210"; // 鞋子以外
            }
        }
        // 20160315 sneakerhead比较优秀， 写死判断 END
        if(!StringUtil.isEmpty(wareTransportId)) {
            jdProductBean.setTransportId(Long.parseLong(wareTransportId));
        }
//           throw new BusinessException("京东运费模板id为空！[ChannelId:%s] [CartId:%s] [Prop_TransportId:%s]",
//                   shopProp.getOrder_channel_id(), shopProp.getCart_id(), Prop_TransportId);

        // charis update
        // 京东关联板式id
        String commonHtml_Id = "";
        try {
            // 取得京东关联板式id
            commonHtml_Id = getConditionPropValue(shopProp, sxData, Prop_CommonHtmlId);
        } catch (Exception ex) {
            String errMsg = String.format("取得京东关联板式id失败！[ChannelId:%s] [CartId:%s] [Prop_CommonHtmlId:%s]",
                    shopProp.getOrder_channel_id(), shopProp.getCart_id(), Prop_CommonHtmlId);
            $info(errMsg, ex);
        }
        if(!StringUtil.isEmpty(commonHtml_Id)) {
            jdProductBean.setTemplateId(Long.parseLong(commonHtml_Id));
        }
//            throw new BusinessException("京东关联板式id为空！[ChannelId:%s] [CartId:%s] [Prop_CommonHtmlId:%s]",
//                    shopProp.getOrder_channel_id(), shopProp.getCart_id(), Prop_CommonHtmlId);

        return jdProductBean;
    }

    /**
     * 取得京东商品共通属性值
     *
     * @param jdCommonSchema CmsMtPlatformCategorySchemaModel  京东共通schema数据
     * @param shopBean ShopBean  店铺信息
     * @param expressionParser ExpressionParser  解析子
     * @param blnIsSmartSx 是否强制使用智能上新
     * @return Map<String, String> 京东商品共通属性
     */
    private Map<String, String> getJdCommonInfo(CmsMtPlatformCategorySchemaModel jdCommonSchema,
                                                ShopBean shopBean, ExpressionParser expressionParser,
                                                boolean blnIsSmartSx) {
        Map<String, String> retAttrMap = new HashMap<>();

        // 取得京东共通schema数据中的propsItem(XML字符串)
        String propsItem = jdCommonSchema.getPropsProduct();
        List<Field> itemFieldList =null;
        if (!StringUtils.isEmpty(propsItem)) {
            // 将取出的propsItem转换为字段列表
            itemFieldList = SchemaReader.readXmlForList(propsItem);
        }

        // 根据field列表取得属性值mapping数据
        Map<String, Field> attrMap = null;

        try {
            // 取得平台Schema所有field对应的属性值（不使用platform_mapping，直接从mainProduct中取得fieldId对应的值）
            attrMap = sxProductService.constructPlatformProps(itemFieldList, shopBean, expressionParser, blnIsSmartSx);
        } catch (Exception ex) {
            String errMsg = String.format("取得京东共通Schema所有Field对应的属性值失败！[ChannelId:%s] [CartId:%s] [PlatformCategoryId:%s]",
                    shopBean.getOrder_channel_id(), shopBean.getCart_id(), jdCommonSchema.getCatId());
            $error(errMsg, ex);
            throw new BusinessException("取得京东平台类目共通属性值失败！" + ex.getMessage());
        }

        // 如果list为空说明没有设置商品共通属性，不用设置
        if (attrMap != null && attrMap.size() > 0 && itemFieldList != null) {
            // 遍历fieldList
            for (Field field : itemFieldList) {
                // 属性值id(field_id)
                String fieldId = field.getId();

                // 从属性值map里面取得当前fieldId对应的Field
                Field fieldValue = attrMap.get(fieldId);
                if (fieldValue == null) {
                    $info("没找到该fieldId对应的属性值！ [FieldId:%s]", fieldId);
                    continue;
                }

                // 根据输入类型分别设置3个属性值
                switch (fieldValue.getType()) {
                    case SINGLECHECK: {
                        // 输入类型input_type为1(单选)的时候
                        retAttrMap.put(fieldId, ((SingleCheckField)fieldValue).getValue().getValue());
                        break;
                    }
                    case MULTICHECK: {
                        // 多选的时候，属性值多个，则用逗号分隔 "属性值1，属性值2，属性值3")
                        List<Value> valueList = ((MultiCheckField) fieldValue).getValues();
                        if (ListUtils.notNull(valueList)) {
                            retAttrMap.put(fieldId, valueList.stream().map(Value::getValue).collect(Collectors.joining(Separtor_Coma)));
                        } else {
                            retAttrMap.put(fieldId, "");
                        }
                        break;
                    }
                    case INPUT: {
                        // 输入类型input_type为3(可输入)的时候
                        retAttrMap.put(fieldId, ((InputField) fieldValue).getValue());
                        break;
                    }
                    default:
                        $error("复杂类型[" + field.getType() + "]不能作为京东共通属性值来使用！");
                }
            }
        }

        return retAttrMap;
    }

    /**
     * 取得京东商品属性值
     *
     * @param platformSchemaData CmsMtPlatformCategorySchemaModel  主产品类目对应的平台schema数据
     * @param shopBean ShopBean  店铺信息
     * @param expressionParser ExpressionParser  解析子
     * @param blnIsSmartSx 是否强制使用智能上新
     * @return Map<String, String> 京东类目属性
     */
    private Map<String, String> getJdProductAttributes(CmsMtPlatformCategorySchemaModel platformSchemaData,
                                                       ShopBean shopBean, ExpressionParser expressionParser,
                                                       boolean blnIsSmartSx) {
        Map<String, String> retAttrMap = new HashMap<>();

        // 取得schema数据中的propsItem(XML字符串)
        String propsItem = platformSchemaData.getPropsItem();
        List<Field> itemFieldList =null;
        if (!StringUtils.isEmpty(propsItem)) {
            // 将取出的propsItem转换为字段列表
            itemFieldList = SchemaReader.readXmlForList(propsItem);
        }

        // 根据field列表取得属性值mapping数据
        Map<String, Field> attrMap = null;

        try {
            // 取得平台Schema所有field对应的属性值（不使用platform_mapping，直接从mainProduct中取得fieldId对应的值）
            attrMap = sxProductService.constructPlatformProps(itemFieldList, shopBean, expressionParser, blnIsSmartSx);
        } catch (Exception ex) {
            String errMsg = String.format("取得京东平台Schema所有Field对应的属性值失败！[ChannelId:%s] [CartId:%s] [PlatformCategoryId:%s]",
                    shopBean.getOrder_channel_id(), shopBean.getCart_id(), platformSchemaData.getCatId());
            $error(errMsg, ex);
            throw new BusinessException("取得京东平台商品类目Schema属性值失败！" + ex.getMessage());
        }

        // 商品属性列表,多组之间用|分隔，格式:aid:vid 或 aid:vid|aid1:vid1 或 aid1:vid1（需要从类目服务接口获取）
        // 如输入类型input_type为1或2，则attributes为必填属性；如输入类型input_type为3，则用字段input_str填入属性的值 
        StringBuilder sbAttributes = new StringBuilder();
        // 用户自行输入的类目属性ID串结构：‘pid1|pid2|pid3’,属性的pid调用360buy.ware.get.attribute取得, 输入类型input_type=3即输入 
        StringBuilder sbInputPids = new StringBuilder();
        // 用户自行输入的属性值,结构:‘输入值|输入值2|输入值3’
        // 图书品类输入值规则：ISBN：数字、字母格式 出版时间：日期格式“yyyy-mm-dd” 版次：数字格式 印刷时间：日期格式“yyyy-mm-dd” 印次：数字格式 页数：数字格式 字数：数字格式 套装数量：数字格式 附件数量：数字格式 
        StringBuilder sbInputStrs = new StringBuilder();

        // 如果list为空说明没有mappingg过，不用设置
        if (attrMap != null && attrMap.size() > 0 && itemFieldList != null) {
            // 遍历fieldList
            for (Field field : itemFieldList) {
                // 属性值id(field_id)
                String fieldId = field.getId();

                // 从属性值map里面取得当前fieldId对应的Field
                Field fieldValue = attrMap.get(fieldId);
                if (fieldValue == null) {
                    $info("没找到该fieldId对应的属性值！ [FieldId:%s]", fieldId);
                    continue;
                }

                // 根据输入类型分别设置3个属性值
                switch (fieldValue.getType()) {
                    case SINGLECHECK: {
                        // 输入类型input_type为1(单选)的时候,设置【商品属性列表】
                        // 设置商品属性列表,多组之间用|分隔，格式:aid:vid 或 aid:vid|aid1:vid1
                        sbAttributes.append(fieldId);                          // 属性id(fieldId)
                        sbAttributes.append(Separtor_Colon);                   // ":"
                        // 属性值设置(如果有多个，则用逗号分隔 "属性值1，属性值2，属性值3")
                        sbAttributes.append(((SingleCheckField)fieldValue).getValue().getValue()); // 属性值id
                        sbAttributes.append(Separtor_Vertical);                // "|"
                        break;
                    }
                    case MULTICHECK: {
                        // 多选的时候，属性值多个，则用逗号分隔 "属性值1，属性值2，属性值3")
//                        String multiAttrValue = "";
                        List<Value> valueList = ((MultiCheckField) fieldValue).getValues();
//                        if (ListUtils.notNull(valueList)) {
//                            multiAttrValue = valueList.stream()
//                                    .map(Value::getValue)
//                                    .collect(Collectors.joining(Separtor_Coma));
//                        }
                        for (Value value : valueList) {
                            // 输入类型input_type为2(多选)的时候,设置【商品属性列表】
                            // 设置商品属性列表,多组之间用|分隔，格式:aid:vid 或 aid1:vid1|aid2:vid21|aid2:vid22
                            sbAttributes.append(fieldId);             // 属性id(fieldId)
                            sbAttributes.append(Separtor_Colon);      // ":"
                            // 属性值设置(如果有多个，以前用逗号分隔 "属性值1，属性值2，属性值3" -> 现在分别设置属性:属性值)
                            sbAttributes.append(value.getValue());      // 第N个属性值
                            sbAttributes.append(Separtor_Vertical);   // "|"
                        }
                        break;
                    }
                    case INPUT: {
                        // 输入类型input_type为3(可输入)的时候,设置【用户自行输入的类目属性ID串】和【用户自行输入的属性值】
                        // 设置用户自行输入的类目属性ID串结构：‘pid1|pid2|pid3’
                        sbInputPids.append(fieldId);                         // 属性id(fieldId)
                        sbInputPids.append(Separtor_Vertical);               // "|"

                        // 设置用户自行输入的属性值,结构:‘输入值|输入值2|输入值3’
                        sbInputStrs.append(((InputField) fieldValue).getValue()); // 属性值id
                        sbInputStrs.append(Separtor_Vertical);                // "|"
                        break;
                    }
                    default:
                        $error("复杂类型[" + field.getType() + "]不能作为属性值来使用！");
                }
            }
        }

        // 移除用户自行输入的类目属性ID最后的"|"
        if (sbAttributes.length() > 0) {
            sbAttributes.deleteCharAt(sbAttributes.length() - 1);
        }

        // 移除用户自行输入的类目属性ID最后的"|"
        if (sbInputPids.length() > 0) {
            sbInputPids.deleteCharAt(sbInputPids.length() - 1);
        }

        // 移除用户自行输入的属性值最后的"|"
        if (sbInputStrs.length() > 0) {
            sbInputStrs.deleteCharAt(sbInputStrs.length() - 1);
        }

        // 把取得的字符串值放进返回map中
        retAttrMap.put(Attributes, sbAttributes.toString());
        retAttrMap.put(Input_Pids, sbInputPids.toString());
        retAttrMap.put(Input_Strs, sbInputStrs.toString());

        return retAttrMap;
    }

    /**
     * 新增商品时设置该商品每个产品的图片
     * 遍历当前group里的所有的product列表，每个product上传5张图片
     * 第一张图设为主图true，其他的都设置为false
     *
     * @param wareId long 商品id
     * @param sxData SxData 产品对象
     * @param productColorMap Map<String Object> 产品和颜色值Mapping关系表
     * @param salePropStatus String 当前平台主类目对应的销售属性状况
     * @return boolean 新增商品上传SKU图片是否成功
     */
    private List<Image> uploadJdProductAddPics(ShopBean shopProp, SxData sxData,
                                           Map<String, Object> productColorMap, String salePropStatus, JdProductNewBean jdProductBean) {
        List<Image> imageList = new ArrayList<>();
        // 如果该平台类目没有颜色，只有尺寸的时候，不用上传图片，直接返回true
        // 当前平台主类目对应的销售属性状况(1:颜色和尺寸属性都有 2:只有颜色没有尺寸属性 3:没有颜色只有尺寸属性 4:没有颜色没有尺寸属性)
        if (SaleProp_Only_Size_3.equals(salePropStatus) || SaleProp_None_Color_Size_4.equals(salePropStatus)) {
            return null;
        }

        boolean retUploadAddPics = true;
        List<CmsBtProductModel> productList = sxData.getProductList();
        ExpressionParser expressionParser = new ExpressionParser(sxProductService, sxData);
        List<BaseMongoMap<String, Object>> skuList = sxData.getSkuList();

        // 产品5张图片名称列表
        List<String> productPicNameList = new ArrayList<>();
        productPicNameList.add("京东产品图片-1");
        productPicNameList.add("京东产品图片-2");
        productPicNameList.add("京东产品图片-3");
        productPicNameList.add("京东产品图片-4");
        productPicNameList.add("京东产品图片-5");

        // 增加图片  调用API【根据商品Id，销售属性值Id增加图片】
        // 每个颜色要传五张图，第一张图设为主图，其他的都设置为非主图
        for (CmsBtProductModel product : productList) {
            String productCode = product.getCommon().getFields().getCode();
            if (StringUtils.isEmpty(productCode)) continue;
            // 颜色值id取得
            // 新增或更新商品的时候，从产品和颜色值Mapping关系表中取得该产品对应的颜色值
            List<String> colorIds = new ArrayList<>();
            if (SaleProp_Both_Color_Size_1.equals(salePropStatus)) {
                // 该平台类目颜色和尺寸都有的时候，根据productCode去查找对应的颜色
                if (productColorMap.containsKey(productCode)) {
                    if (!StringUtil.isEmpty(String.valueOf(productColorMap.get(productCode)))) {
                        String[] colorIdArray = productColorMap.get(product.getCommon().getFields().getCode()).toString().split(Separtor_Colon);
                        if (colorIdArray.length >= 2) {
                            // 产品颜色值Mapping关系表里面取得的颜色值为"1000021641:1523005913",取得后面的颜色值"1523005913"
                            colorIds.add(colorIdArray[1]);
                        }
                    }
                }
            } else if (SaleProp_Only_Color_2.equals(salePropStatus)) {
                // 如果类目只有颜色没有尺寸的时候，每种尺寸都作为一种颜色,用productCode_sizeSx去查找对应的颜色
                // (如果没有颜色只有尺寸的时候不上传图片，因为图片用的颜色id）
                CmsBtProductModel_Platform_Cart platformCart = product.getPlatform(sxData.getCartId());
                if (platformCart == null || ListUtils.isNull(platformCart.getSkus())) continue;
                List<BaseMongoMap<String, Object>> platformSkuList = platformCart.getSkus();
                for (BaseMongoMap<String, Object> pSku : platformSkuList) {
                    String pSkuCode = pSku.getStringAttribute("skuCode");
                    for (BaseMongoMap<String, Object> objSku : skuList) {
                        // 如果没有找到对应skuCode，则继续循环
                        if (!pSkuCode.equals(objSku.getStringAttribute("skuCode"))) {
                            continue;
                        }

                        // productColorMap中的key为productCode_sizeSx
                        String colorKey = product.getCommon().getFields().getCode() + "_" +
                                objSku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.sizeSx.name());
                        // 从产品颜色map中取得一个颜色id
                        if (productColorMap.containsKey(colorKey)) {
                            String[] colorIdArray = productColorMap.get(colorKey).toString().split(Separtor_Colon);
                            if (colorIdArray.length >= 2) {
                                // 产品颜色值Mapping关系表里面取得的颜色值为"1000021641:1523005913",取得后面的颜色值"1523005913"
                                colorIds.add(colorIdArray[1]);
                            }
                        }
                    }
                }
            }

            // 如果没有取得颜色id，则不上传图片
            if (ListUtils.isNull(colorIds)) {
                continue;
            }

            // 如果一个产品有多个颜色id循环设置图片（如果类目只有颜色没有尺寸时，会把每种SKU都作为一种颜色）
            for (String colorId : colorIds) {
                // 主图不上传
                if (StringUtils.isEmpty(colorId) || ColorId_MainPic.equals(colorId)) {
                    continue;
                }

                // 根据商品Id，销售属性值Id增加5张产品图片
                // SKU图片上传是否成功(只要5张图片有一张上传成功则认为成功)
                boolean uploadProductPicResult = false;
                // 取得图片URL参数
                String[] extParameter = {product.getCommon().getFields().getCode()};   // 产品code

                // 循环取得5张图片的url并分别上传到京东
                for (int i=0;i<productPicNameList.size();i++) {
                    String picUrl = "";
                    try {
                        // 取得图片URL
                        picUrl = sxProductService.resolveDict(productPicNameList.get(i), expressionParser, shopProp, getTaskName(), extParameter);
                        if (StringUtils.isEmpty(picUrl)) continue;
                        Set<String> picSet = new HashSet<>();
                        picSet.add(picUrl);
                        Map<String,String> imageMap = sxProductNewService.uploadImage(shopProp.getOrder_channel_id(), Integer.parseInt(shopProp.getCart_id()),
                                String.valueOf(sxData.getGroupId()), shopProp, picSet, getTaskName());
                        Image image = new Image();
                        image.setColorId(colorId);
                        image.setImgIndex(i + 1);
                        image.setImgUrl(imageMap.get(picUrl));
                        imageList.add(image);
//                        // 如果之前没有一张图片上传成功则本次上传对象图片设置为主图，如果之前已经有图片上传成功，则本次设为非主图
//                        boolean skuPicResult = jdWareNewService.addWarePropimg(shopProp, String.valueOf(wareId), colorId, picUrl, picName, !uploadProductPicResult);

                        // 5张图片只有曾经有一张上传成功就认为SKU图片上传成功
//                        uploadProductPicResult = uploadProductPicResult || skuPicResult;
                    } catch (Exception ex) {
                        // 如果5张图片里面有一张上传成功的时候
                        if (imageList == null) {
                            $debug("京东根据商品Id销售属性值Id获取图片失败！[WareId:%s] [ColorId:%s] [PicName:%s] [PicUrl:%s]", jdProductBean.getWareId(), colorId, productPicNameList.get(i), picUrl);
                        }
//                        else {
//                            $debug("京东根据商品Id销售属性值Id上传产品主图失败！[WareId:%s] [ColorId:%s] [PicName:%s] [PicUrl:%s]", wareId, colorId, picName, picUrl);
//                        }
                        $error(ex);
                        // 即使5张图片中的某张上传出错，也继续循环上传后面的图片
                    }
                }

                // 该产品5张图片全部上传失败的时候
                if (imageList == null) {
                    $error("新增商品时该颜色图片全部上传失败！[WareId:%s] [ProductCode:%s]", jdProductBean.getWareId(), product.getCommon().getFields().getCode());
                }
//                else {
//                    $debug("新增商品时该颜色图片至少有1张上传成功！[WareId:%s] [ProductCode:%s]", wareId, product.getCommon().getFields().getCode());
//                }

                // 图片上传返回状态判断(该商品下所有产品的图片均上传成功时，才返回成功，否则返回失败)
//                retUploadAddPics = retUploadAddPics && uploadProductPicResult;
            }

            // 只要有一个产品（颜色）的5张图片都上传失败，则认为失败，退出循环，后面会删除该商品
//            if (!retUploadAddPics) {
//                break;
//            }
        }

        return imageList;
    }

    /**
     * 更新商品时更新该商品每个产品的图片（不包括主图）
     * 遍历当前group里的所有的product，更新每个product的5张图片
     * 每个颜色先上传一张，再删除一张，最后删除多余的更新前图片
     *
     * @param wareId long 商品id
     * @param sxData SxData 产品对象
     * @param productColorMap Map<String Object> 产品和颜色值Mapping关系表
     * @param salePropStatus String 当前平台主类目对应的销售属性状况
     * @return boolean 上传指定商品SKU图片是否成功
     */
    private boolean uploadJdProductUpdatePics(ShopBean shopProp, long wareId, JdProductNewBean jdProductBean, String salePropStatus) {

        // 如果该平台类目没有颜色，只有尺寸的时候，不用上传图片，直接返回true
        // 当前平台主类目对应的销售属性状况(1:颜色和尺寸属性都有 2:只有颜色没有尺寸属性 3:没有颜色只有尺寸属性 4:没有颜色没有尺寸属性)
        if (SaleProp_Only_Size_3.equals(salePropStatus) || SaleProp_None_Color_Size_4.equals(salePropStatus)) {
            return true;
        }

        // ---------------------------------------------
        // charis update 20170228
        // 京东更新图片的api变更为 jingdong.image.write.update
        // ---------------------------------------------
        List<String> colorList = jdProductBean.getImages().stream()
                .map(i -> i.getColorId())
                .collect(Collectors.toList());
        List<String> urlList = jdProductBean.getImages().stream()
                .map(i -> i.getImgUrl())
                .collect(Collectors.toList());
        List<String> indexList = jdProductBean.getImages().stream()
                .map(i -> i.getImgIndex().toString())
                .collect(Collectors.toList());

        ImageWriteUpdateResponse imageWriteUpdateResponse = jdWareNewService.imageWriteUpdate(shopProp, wareId,
                colorList, urlList, indexList);

        try {
            if (imageWriteUpdateResponse != null) {
                // 京东返回正常的场合
                if (JdConstants.C_JD_RETURN_SUCCESS_OK.equals(imageWriteUpdateResponse.getCode())) {
                    return imageWriteUpdateResponse.getSuccess();
                } else {
                    // 京东返回失败的场合
                    throw new BusinessException(imageWriteUpdateResponse.getMsg());
                }
            } else {
                // response = null（https://api.jd.com/routerjson）不能访问的可能原因是服务器禁掉了https端口
                // 或app_url,app_key等不正确
                throw new BusinessException("京东更新商品图片API返回为空(response = null)");
            }
        }catch (Exception e) {
            String errMsg = String.format(shopProp.getShop_name() + "调用京东更新商品维度的销售属性值别名API失败! [channelId:%s] [cartId:%s] [jdSkuId:%s] " +
                    "[errMsg:%s]", shopProp.getOrder_channel_id(), shopProp.getCart_id(), StringUtils.toString(wareId), e.getMessage());
            logger.error(errMsg);
            throw new BusinessException(errMsg);
        }
//        return false;


//        List<CmsBtProductModel> productList = sxData.getProductList();
//        List<BaseMongoMap<String, Object>> skuList = sxData.getSkuList();
//
//        ExpressionParser expressionParser = new ExpressionParser(sxProductService, sxData);
//
//        // 产品5张图片名称列表(TODO 暂时为固定值，以后会改成从表中读取)
//        List<String> productPicNameList = new ArrayList<>();
//        productPicNameList.add("京东产品图片-1");
//        productPicNameList.add("京东产品图片-2");
//        productPicNameList.add("京东产品图片-3");
//        productPicNameList.add("京东产品图片-4");
//        productPicNameList.add("京东产品图片-5");

//        // 1.检索 取得图片更新前京东平台上每种颜色的图片张数（可能范围：0~5张）
//        // 更新的时候，保存更新之前的颜色和图片INDEX列表用（不包含颜色值Id为0000000000主图）
//        Map<String,List<String>> jdColorIndexesMap = new HashMap<>();
//        // 调用京东API根据商品Id检索指定商品Id对应的商品图片列表（包含颜色值Id为0000000000）
//        List<Image> wareIdPics = jdWareNewService.getImagesByWareId(shopProp, wareId);
//        // 取得图片更新前京东平台上每种颜色的图片张数
//        for (Image img : wareIdPics) {
//            // 过滤掉主图的颜色值Id0000000000
//            if (ColorId_MainPic.equals(img.getColorId())) {
//                continue;
//            }
//
//            // 如果对象MAP中没有这个颜色
//            if (!jdColorIndexesMap.containsKey(img.getColorId())) {
//                jdColorIndexesMap.put(img.getColorId(), new ArrayList<>());
//            }
//
//            // 将图片index追加到列表中
//            if (jdColorIndexesMap.containsKey(img.getColorId())){
//                jdColorIndexesMap.get(img.getColorId()).add(img.getImgIndex().toString());
//            }
//        }

        // 根据商品Id，销售属性值Id增加5张产品图片
        // SKU图片上传是否成功(只要有一张上传成功则认为成功)
//        boolean uploadAllPicsResult = false;
//        String productPicErr = "";

        // 2.循环颜色（产品）列表更新图片
//         每个颜色先上传一张，再删除一张，最后删除多余的更新前图片
//        for (CmsBtProductModel product:productList) {
//            String productCode = product.getCommon().getFields().getCode();
//            if (StringUtils.isEmpty(productCode)) continue;
//            // 颜色值id取得
//            // 新增或更新商品的时候，从产品和颜色值Mapping关系表中取得该产品对应的颜色值
////            String colorId = "";
//            List<String> colorIds = new ArrayList<>();
//            if (SaleProp_Both_Color_Size_1.equals(salePropStatus)) {
//                // 该平台类目颜色和尺寸都有的时候，根据productCode去查找对应的颜色
//                if (productColorMap.containsKey(productCode)) {
//                    if (!StringUtil.isEmpty(String.valueOf(productColorMap.get(productCode)))) {
//                        String[] colorIdArray = productColorMap.get(product.getCommon().getFields().getCode()).toString().split(Separtor_Colon);
//                        if (colorIdArray.length >= 2) {
//                            // 产品颜色值Mapping关系表里面取得的颜色值为"1000021641:1523005913",取得后面的颜色值"1523005913"
//                            colorIds.add(colorIdArray[1]);
//                        }
//                    }
//                }
//            } else if (SaleProp_Only_Color_2.equals(salePropStatus)) {
//                // 如果类目只有颜色没有尺寸的时候，每种尺寸都作为一种颜色,用sizeSx去查找对应的颜色
//                // (如果没有颜色只有尺寸的时候不上传图片，因为图片用的颜色id）
//                CmsBtProductModel_Platform_Cart platformCart = product.getPlatform(sxData.getCartId());
//                if (platformCart == null || ListUtils.isNull(platformCart.getSkus())) continue;
//                List<BaseMongoMap<String, Object>> platformSkuList = platformCart.getSkus();
//                for (BaseMongoMap<String, Object> pSku : platformSkuList) {
//                    String pSkuCode = pSku.getStringAttribute("skuCode");
//                    for (BaseMongoMap<String, Object> objSku : skuList) {
//                        // 如果没有找到对应skuCode，则继续循环
//                        if (!pSkuCode.equals(objSku.getStringAttribute("skuCode"))) {
//                            continue;
//                        }
//
//                        // productColorMap或productSizeMap中的key为productCode_sizeSx
//                        String colorKey = product.getCommon().getFields().getCode() + "_" +
//                                objSku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.sizeSx.name());
//                        // 从产品颜色map中取得一个颜色id
//                        if (productColorMap.containsKey(colorKey)) {
//                            String[] colorIdArray = productColorMap.get(colorKey).toString().split(Separtor_Colon);
//                            if (colorIdArray.length >= 2) {
//                                // 产品颜色值Mapping关系表里面取得的颜色值为"1000021641:1523005913",取得后面的颜色值"1523005913"
//                                colorIds.add(colorIdArray[1]);
//                            }
//                        }
//                    }
//                }
//            }
//
//            // 如果没有取得颜色id，则不上传图片
//            if (ListUtils.isNull(colorIds)) {
//                continue;
//            }
//
//            // 如果一个产品有多个颜色id循环设置图片（如果类目只有颜色没有尺寸时，会把每种尺寸都作为一种颜色）
//            for (String colorId : colorIds) {
//                // 主图不上传
//                if (StringUtils.isEmpty(colorId) || ColorId_MainPic.equals(colorId)) {
//                    continue;
//                }
//
//                // 根据商品Id，销售属性值Id增加5张产品图片
//                // SKU图片上传是否成功(只要5张图片有一张上传成功则认为成功,会被当成主图，后面上传的图片就是非主图)
//                boolean uploadCurrentColorPicResult = false;
//                // 取得图片URL参数
//                String[] extParameter = {productCode};   // 产品code
//                // 京东平台上该颜色对应的更新前图片张数
//                int intOldImageCnt = 0;
//                if (!ListUtils.isNull(jdColorIndexesMap.get(colorId))) {
//                    intOldImageCnt = jdColorIndexesMap.get(colorId).size();
//                }
//                // 要更新的产品图片张数
////            int intNewImageCnt = product.getCommon().getFields().getImages1().size();
//                // 京东平台上该颜色已经删除的图片件数
//                int delImageCnt = 0;
//
//                // 循环取得5张图片的url并分别上传到京东
//                for (String picName : productPicNameList) {
//                    String picUrl = "";
//                    try {
//                        // 取得图片URL(这里的解析字典不能知道现在到底是哪个颜色产品的图片，目前每个商品都只有一个product，暂时没问题)
//                        picUrl = sxProductService.resolveDict(picName, expressionParser, shopProp, getTaskName(), extParameter);
//                        if (StringUtils.isEmpty(picUrl)) continue;
//                        // 如果之前没有一张图片上传成功则本次上传对象图片设置为主图，如果之前已经有图片上传成功，则本次设为非主图
//                        boolean skuPicResult = jdWareNewService.addWarePropimg(shopProp, String.valueOf(wareId), colorId, picUrl, picName, !uploadCurrentColorPicResult);
//
//                        // 5张图片只有曾经有一张上传成功就认为SKU图片上传成功，并被当成主图，后面的图片设为非主图
//                        uploadCurrentColorPicResult = uploadCurrentColorPicResult || skuPicResult;
//                    } catch (Exception ex) {
//                        $info("京东根据商品Id销售属性值Id上传产品颜色图片失败！[WareId:%s] [ColorId:%s] [PicName:%s] [PicUrl:%s]", wareId, colorId, picName, picUrl);
//                        productPicErr = ex.getMessage();
//                        // 即使5张图片中的某张上传出错，也继续循环上传后面的图片
//                    }
//
//                    // 删除第一张图片(及时上传失败，也要删除老的图片，如果报必须大于5张/3张/1张(每个类目要求图片张数不一样)的错误，只能让运营把图片改好)
//                    if (delImageCnt < intOldImageCnt) {
//                        try {
//                            // 调用API【删除商品图片】批量删除该商品该颜色的第一张图片
//                            boolean delPicResult = jdWareNewService.deleteImagesByWareId(shopProp, wareId, colorId, "1");
//                            // 京东平台上该颜色已经删除的图片件数
//                            if (delPicResult) delImageCnt++;
//                        } catch (Exception ex) {
//                            // 如果报"图片张数必须多余N张"的异常，则继续上传下一张图片，否则抛出异常
//                            if (ex.getMessage().contains("图片张数必须多于")) {
//                                delImageCnt++;
//                                continue;
//                            } else {
//                                throw new BusinessException(ex.getMessage());
//                            }
//                        }
//                    }
//                }
//
//                // 删除剩余的图片
//                for (int i = delImageCnt; i < intOldImageCnt; i++) {
//                    // 调用删除图片API（指定删除第一张）
//                    // 如果报"图片张数必须多余N张"的异常，则写到log表里，让运营在CMS里面添加图片
//                    try {
//                        jdWareNewService.deleteImagesByWareId(shopProp, wareId, colorId, "1");
//                    } catch (Exception ex) {
//                        String errMsg = String.format("调用京东API上传商品颜色图片失败 [ErrorMsg:%s] [WareId:%s] " +
//                                "[ProductCode:%s] [ColorId:]", ex.getMessage(), wareId, productCode, colorId);
//                        $error(errMsg);
//                        throw new BusinessException(errMsg);
//                    }
//                }
//
//                // 图片上传返回状态判断
//                uploadAllPicsResult = uploadAllPicsResult || uploadCurrentColorPicResult;
//
//                $debug("商品颜色图片上传成功！[WareId:%s] [ProductCode:%s] [ColorId:%s]", wareId, product.getCommon().getFields().getCode(), colorId);
//            }
//        }
//
//        // 该产品颜色图片全部上传失败的时候
//        if (!uploadAllPicsResult) {
//            $error("商品所有颜色图片全部上传失败！[WareId:%s]", wareId);
//            sxData.setErrorMessage(shopProp.getShop_name() + " 商品所有颜色图片全部上传失败 [WareId:" + wareId + "] [ErrMsg:" + productPicErr + "]");
//        }

//        return uploadAllPicsResult;
    }

    /**
     * 取得前台展示的商家自定义店内分类
     * 如果cms_bt_condition_prop_value表存在条件表达式，优先使用这个条件表达式；
     * 不存在的话，通过360buy.sellercats.get获取店铺分类的parent_id及cid，
     * 按“parent_id-cid"格式传入，同时设置多个以分号（;）分隔即可。
     * 店内分类，格式:206-208;207-208 206(一级)-208(二级);207(一级)-207(一级)
     *
     * @param shop ShopBean 店铺对象
     * @param sxData SxData 产品对象
     * @return String 前台展示的商家自定义店内分类
     */
    private Set<Long> getShopCategory(ShopBean shop, SxData sxData) throws Exception {

        // 多个条件表达式用分号分隔用
//        StringBuilder sbbuilder = new StringBuilder();
        Set<Long> shopCategorysSet = new HashSet<>();
        // delete by desmond 2016/08/10 店铺内分类字典的解析和设置改为在feed->master导入来做了 start
//        // 条件表达式表platform_prop_id字段的检索条件为"seller_cids_"加cartId
//        String platformPropId = Prop_ShopCategory + shop.getCart_id();
//
//        ExpressionParser expressionParser = new ExpressionParser(sxProductService, sxData);
//
//        // 根据channelid和platformPropId取得cms_bt_condition_prop_value表的条件表达式
//        List<ConditionPropValueModel> conditionPropValueModels = conditionPropValueRepo.get(shop.getOrder_channel_id(), platformPropId);
//
//        // 优先使用条件表达式
//        if (conditionPropValueModels != null && !conditionPropValueModels.isEmpty()) {
//            RuleJsonMapper ruleJsonMapper = new RuleJsonMapper();
//            for (ConditionPropValueModel conditionPropValueModel : conditionPropValueModels) {
//                String conditionExpressionStr = conditionPropValueModel.getCondition_expression();
//                RuleExpression conditionExpression = ruleJsonMapper.deserializeRuleExpression(conditionExpressionStr);
//                String propValue = expressionParser.parse(conditionExpression, shop, getTaskName(), null);
//
//                // 多个表达式(2392231-4345291格式)用分号分隔
//                if (!StringUtils.isEmpty(propValue)) {
//                    sbbuilder.append(propValue);
//                    sbbuilder.append(Separtor_Semicolon);   // 用分号(";")分隔
//                }
//            }
//        }
        // delete by desmond 2016/08/10 end

//        if (StringUtils.isEmpty(sbbuilder.toString())) {
            // 获取京东平台前台展示的商家自定义店内分类(从分平台信息里面取得sellerCats)
            // added by morse.lu 2016/11/18 start
            String newArrivalSellerCat = sxProductService.getNewArrivalSellerCat(sxData.getChannelId(), sxData.getCartId(), sxData.getPlatform().getPublishTime()); // 新品类目id
            if (!StringUtils.isEmpty(newArrivalSellerCat)) {
                // 需要添加新品类目
//                sbbuilder.append(newArrivalSellerCat);
//                sbbuilder.append(Separtor_Semicolon); // 用分号(";")分隔

                shopCategorysSet.add(Long.parseLong(newArrivalSellerCat.split("-")[1]));
            }
            // added by morse.lu 2016/11/18 end
            CmsBtProductModel_Platform_Cart productPlatformCart = sxData.getMainProduct().getPlatform(sxData.getCartId());
            if (productPlatformCart != null && ListUtils.notNull(productPlatformCart.getSellerCats())) {
                // 取得
                List<CmsBtProductModel_SellerCat> sellerCatList = productPlatformCart.getSellerCats();
                // 里面的数据是“1233797770-1233809821;1233797771-1233809822”样式的
                for (CmsBtProductModel_SellerCat sellerCat : sellerCatList ) {
                    if (sellerCat == null || ListUtils.isNull(sellerCat.getcIds())) {
                        continue;
                    }
                    // 用连字符("-")连接 (1233797770-1233809821)
                    // modified by morse.lu 2016/11/18 start
//                    sbbuilder.append(sellerCat.getcIds().stream().collect(Collectors.joining(Separtor_Hyphen)));
//                    sbbuilder.append(Separtor_Semicolon);    // 用分号(";")分隔
//                    String cids = sellerCat.getcIds().stream().collect(Collectors.joining(Separtor_Hyphen));
                String cid = sellerCat.getcId();
                if (!cid.equals(newArrivalSellerCat)) {
//                        sbbuilder.append(cids);
//                        sbbuilder.append(Separtor_Semicolon);    // 用分号(";")分隔
                    shopCategorysSet.add(Long.parseLong(cid));
                }
                // modified by morse.lu 2016/11/18 end
            }
        }
        // 直接从Product中取得店铺内分类，不用从京东去取了
//            List<ShopCategory> shopCategoryList = jdShopService.getShopCategoryList(shop);
//            if (shopCategoryList != null && !shopCategoryList.isEmpty()) {
//                for (ShopCategory shopCategory : shopCategoryList) {
//                    // 如果不是父类目的话，加到店内分类里，用分号分隔
//                    if (!shopCategory.getParent()) {
//                        // 转换成“parent_id-cid"格式，同时设置多个以分号（;）分隔
//                        sbbuilder.append(String.valueOf(shopCategory.getParentId()));
//                        sbbuilder.append(Separtor_Hyphen);       // 用连字符("-")连接
//                        sbbuilder.append(String.valueOf(shopCategory.getCid()));
//                        sbbuilder.append(Separtor_Semicolon);    // 用分号(";")分隔
//                    }
//                }
//            }
//        }
        // 移除最后的分号
//        if (sbbuilder.length() > 0) {
//            sbbuilder.deleteCharAt(sbbuilder.length() - 1);
//        }

        // 店铺种类
        return shopCategorysSet;
    }

    /**
     * 取得运费模板id或关联版式id
     *
     * @param shop ShopBean 店铺对象
     * @param sxData SxData 产品对象
     * @param prePropId String 条件表达式前缀(运费模板:transportid_ 关联版式:commonhtml_id_)
     * @return String 运费模板id或关联版式id
     */
    private String getConditionPropValue(ShopBean shop, SxData sxData, String prePropId) throws Exception {

        // 运费模板id或关联版式id返回用
        String  retStr = "";
        // 条件表达式前缀(运费模板:transportid_ 关联版式:commonhtml_id_)
        // 条件表达式表platform_prop_id字段的检索条件为条件表达式前缀加cartId
        String platformPropId = prePropId + shop.getCart_id();

        ExpressionParser expressionParser = new ExpressionParser(sxProductService, sxData);

        // 根据channelid和platformPropId取得cms_mt_channel_condition_config表的条件表达式
        List<ConditionPropValueModel> conditionPropValueModels = null;
        if (channelConditionConfig != null && channelConditionConfig.containsKey(shop.getOrder_channel_id())) {
            if (channelConditionConfig.get(shop.getOrder_channel_id()).containsKey(platformPropId)) {
                conditionPropValueModels = channelConditionConfig.get(shop.getOrder_channel_id()).get(platformPropId);
            }
        }

        // 使用运费模板或关联版式条件表达式
        if (conditionPropValueModels != null && !conditionPropValueModels.isEmpty()) {
            RuleJsonMapper ruleJsonMapper = new RuleJsonMapper();
            for (ConditionPropValueModel conditionPropValueModel : conditionPropValueModels) {
                String conditionExpressionStr = conditionPropValueModel.getCondition_expression();
                RuleExpression conditionExpression = ruleJsonMapper.deserializeRuleExpression(conditionExpressionStr);
                String propValue = expressionParser.parse(conditionExpression, shop, getTaskName(), null);

                // 找到运费模板或关联版式表达式则跳出循环
                if (!StringUtils.isEmpty(propValue)) {
                    retStr = propValue;
                    break;
                }
            }
        }

        // 运费模板id或关联版式id
        return retStr;
    }

    /**
     * 取得操作类型
     * 现只支持：offsale 或onsale,默认为下架状态
     *
     * @param productGroup CmsBtProductGroupModel 商品Model Group Channel
     * @param groupId long groupid
     * @return String 前台操作类型（offsale或onsale）
     */
    private String getOptionType(CmsBtProductGroupModel productGroup, long groupId) {
        String retOptionType;
        CmsConstants.PlatformActive platformActive = productGroup.getPlatformActive();

        if (platformActive == CmsConstants.PlatformActive.ToOnSale) {
            // 如果是ToOnSale， 那么onsale
            retOptionType = String.valueOf(OptioinType_onsale);
        } else {
            // 如果是ToInStock， 那么offsale（默认状态为offsale）
            retOptionType = String.valueOf(OptioinType_offsale);
        }

        return retOptionType;
    }

    /**
     * 取得所有SKU价格的最高价格
     *
     * @param skuList List<BaseMongoMap<String, Object>> 所有sku对象列表
     * @param channelId String 渠道id
     * @param cartId String 平台id
     * @param priceKey
     * @param priceCode String 价格类型
     * @return double 所有产品全部SKU的最高价格
     */
    private Double getItemPrice(List<BaseMongoMap<String, Object>> skuList, String channelId, String cartId, String priceKey, String priceCode) {
        // 价格有可能是用priceSale, 也有可能用priceMsrp, 所以需要判断一下
        // priceType:"retail_price"(市场价)  "sale_price"(京东价)
        CmsChannelConfigBean sxPriceConfig = CmsChannelConfigs.getConfigBean(channelId, priceKey, cartId + priceCode);

        // 检查一下
        String sxPricePropName;
        if (sxPriceConfig == null) {
            return 0.0;
        } else {
            // 取得价格属性名
            sxPricePropName = sxPriceConfig.getConfigValue1();
            if (StringUtils.isEmpty(sxPricePropName)) {
                return 0.0;
            }
        }

        Double resultPrice = 0.0;
        // 如果skuList为空（可能所有的sku都没有库存），直接返回0.0,否则后面会报"No value present"错误
        if (ListUtils.isNull(skuList) || skuList.stream().mapToDouble(p -> p.getDoubleAttribute(sxPricePropName)).count() == 0) {
            return 0.0;
        }

        if (CmsConstants.ChannelConfig.PRICE_SALE_PRICE_CODE.equals(priceCode)) {//PriceType_jdprice
            resultPrice = skuList.parallelStream().mapToDouble(p -> p.getDoubleAttribute(sxPricePropName)).max().getAsDouble();
        } else if (CmsConstants.ChannelConfig.PRICE_RETAIL_PRICE_CODE.equals(priceCode)) {//PriceType_marketprice
            // 如果是市场价"retail_price"，则取个平台相应的售价(platform.P29.sku.priceMsrp)
            resultPrice = skuList.parallelStream().mapToDouble(p -> p.getDoubleAttribute(sxPricePropName)).max().getAsDouble();
        } else {
            $warn("取得所有SKU价格的最高价格时传入的priceType不正确 [priceType:%s]" + priceCode);
        }

        return resultPrice;
    }

    /**
     * 取得指定SKU的价格
     *
     * @param cmsBtProductModelSku BaseMongoMap<String, Object> SKU对象
     * @param channelId String 渠道id
     * @param cartId String 平台id
     * @param priceCode String 价格类型 ("sale_price"(京东价))
     * @return double SKU价格
     */
    private double getSkuPrice(BaseMongoMap<String, Object> cmsBtProductModelSku, String channelId, String cartId, String priceKey, String priceCode) {
        // 价格有可能是用priceSale, 也有可能用priceMsrp, 所以需要判断一下
        // SKU价格类型应该用"sale_price"(京东价)
        CmsChannelConfigBean sxPriceConfig = CmsChannelConfigs.getConfigBean(channelId, priceKey, cartId + priceCode);

        // 检查一下
        String sxPricePropName;
        if (sxPriceConfig == null) {
            return 0.0d;
        } else {
            // 取得价格属性名
            sxPricePropName = sxPriceConfig.getConfigValue1();
            if (StringUtils.isEmpty(sxPricePropName)) {
                return 0.0d;
            }
        }

        // 取得该SKU的价格
        return cmsBtProductModelSku.getDoubleAttribute(sxPricePropName);
    }

    /**
     * 回写产品Group表里的商品id
     *
     * @param sxData SxData 上新数据
     * @param numIId String 商品id
     */
    private void updateProductGroupNumIId(SxData sxData, String numIId) {

        // 回写商品id(wareId->numIId) 删除的时候设为空("")即可
        sxData.getPlatform().setNumIId(numIId);
        // 更新者
        sxData.getPlatform().setModifier(getTaskName());
        // 更新ProductGroup表
        productGroupService.update(sxData.getPlatform());
    }

    /**
     * 设置京东运费模板
     *
     * @param shop ShopBean 店铺对象
     * @param sxData SxData 上新数据
     * @param wareId long 商品id
     */
    private void updateJdWareTransportId(ShopBean shop, SxData sxData, long wareId) {
        // 京东运费模板id
        String wareTransportId = "";

        try {
            // 取得京东运费模板id
            wareTransportId = getConditionPropValue(shop, sxData, Prop_TransportId);
        } catch (Exception ex) {
            String errMsg = String.format("取得京东运费模板id失败！[ChannelId:%s] [CartId:%s] [Prop_TransportId:%s]",
                    shop.getOrder_channel_id(), shop.getCart_id(), Prop_TransportId);
            $info(errMsg, ex);
        }

        // 调用京东API设置运费模板
        if (!StringUtils.isEmpty(wareTransportId)) {
            jdWareNewService.updateWareTransportId(shop, wareId, Long.parseLong(wareTransportId));
        }
    }

    /**
     * 设置京东关联板式
     *
     * @param shop ShopBean 店铺对象
     * @param sxData SxData 上新数据
     * @param wareId long 商品id
     */
    private void updateJdWareLayoutId(ShopBean shop, SxData sxData, long wareId) {
        // 京东关联板式id
        String commonHtml_Id = "";

        try {
            // 取得京东关联板式id
            commonHtml_Id = getConditionPropValue(shop, sxData, Prop_CommonHtmlId);
        } catch (Exception ex) {
            String errMsg = String.format("取得京东关联板式id失败！[ChannelId:%s] [CartId:%s] [Prop_CommonHtmlId:%s]",
                    shop.getOrder_channel_id(), shop.getCart_id(), Prop_CommonHtmlId);
            $info(errMsg, ex);
            return;
        }

        // 调用京东API设置关联板式（取消商品关联版式时，请将commonHtml_Id值设置为空）
        jdWareNewService.updateWareLayoutId(shop, String.valueOf(wareId), commonHtml_Id);
    }

    /**
     * 商品上架/下架处理
     *
     * @param shop ShopBean 店铺对象
     * @param sxData SxData 上新数据
     * @param wareId long 商品id
     * @param updateFlg boolean 新增/更新商品flg
     */
    private boolean updateJdWareListing(ShopBean shop, SxData sxData, long wareId, boolean updateFlg) {
        // 商品上架/下架结果
        boolean updateListingResult = false;

        // platformActive平台上新状态类型(ToOnSale/ToInStock)
        if (CmsConstants.PlatformActive.ToOnSale.equals(sxData.getPlatform().getPlatformActive())) {
            // platformActive是(ToOnSale)时，执行商品上架操作
            updateListingResult = jdSaleService.doWareUpdateListing(shop, wareId, updateFlg);
        } else {
            // platformActive是(ToInStock)时，执行商品下架操作
            updateListingResult = jdSaleService.doWareUpdateDelisting(shop, wareId, updateFlg);
        }

        // 插入mongoDB上下架履历表cms_bt_platform_active_log_cXXX
        insertPlatformActiveLog(sxData, wareId, updateListingResult);

        return updateListingResult;
    }

    /**
     * 记录商品上架/下架处理履历表cms_bt_platform_active_log_cXXX
     *
     * @param sxData SxData 上新数据
     * @param wareId long 商品id
     * @param updateListingResult boolean 上下架是否成功
     */
    public void insertPlatformActiveLog(SxData sxData, long wareId, boolean updateListingResult) {
        CmsBtPlatformActiveLogModel platformActiveLogModel = new CmsBtPlatformActiveLogModel();

        platformActiveLogModel.setChannelId(sxData.getChannelId());
        platformActiveLogModel.setGroupId(sxData.getGroupId());
        platformActiveLogModel.setCartId(sxData.getCartId());
        platformActiveLogModel.setNumIId(Long.toString(wareId));
        platformActiveLogModel.setProdCode(sxData.getMainProduct().getCommon().getFields().getCode());
        platformActiveLogModel.setResult(updateListingResult ? "1" : "2"); // 1:上/下架成功 2:上/下架失败 3:不满足上下架条件
        String strComment = "";
        String strFailComment = "";
        String platformStatus = "";
        // platformActive平台上新状态类型(ToOnSale/ToInStock)
        if (CmsConstants.PlatformActive.ToInStock.equals(sxData.getPlatform().getPlatformActive())) {
            strComment = "下架处理";
            strFailComment = "调用京东下架API失败";
            platformStatus = CmsConstants.PlatformStatus.InStock.name();
        } else if (CmsConstants.PlatformActive.ToOnSale.equals(sxData.getPlatform().getPlatformActive())) {
            strComment = "上架处理";
            strFailComment = "调用京东上架API失败";
            platformStatus = CmsConstants.PlatformStatus.OnSale.name();
        }
        if (!updateListingResult) {
            // 上下架失败的时候，设为老的状态
            platformStatus = sxData.getPlatform().getPlatformStatus() != null ?
                    sxData.getPlatform().getPlatformStatus().name() : "";
        }
        platformActiveLogModel.setComment("京东上新结束之后商品" + strComment);
        platformActiveLogModel.setFailedComment(updateListingResult ? "" : strFailComment);
        platformActiveLogModel.setPlatformStatus(platformStatus);
        platformActiveLogModel.setActiveStatus(sxData.getPlatform().getPlatformActive().name());
        platformActiveLogModel.setMainProdCode(sxData.getMainProduct().getCommon().getFields().getCode());
        long batchNo = sequenceService.getNextSequence(MongoSequenceService.CommSequenceName.CMS_BT_PRODUCT_PLATFORMACTIVEJOB_ID);
        platformActiveLogModel.setBatchNo(batchNo);
        platformActiveLogModel.setCreater(getTaskName());
        platformActiveLogModel.setModifier(getTaskName());

        cmsBtPlatformActiveLogDao.insert(platformActiveLogModel);
    }

    /**
     * 商品强制上架/下架并回写状态记录历史处理
     *
     * @param shop ShopBean 店铺对象
     * @param wareId long 商品id
     * @param updateFlg boolean 新增/更新商品flg
     */
    protected void doJdForceWareListing(ShopBean shop, Long wareId, Long groupId, CmsConstants.PlatformActive platformActive, List<String> codeList, boolean updateFlg, String modifier) {
        if (shop == null || wareId == null || platformActive == null) return;

        // 商品上架/下架结果
        boolean updateListingResult = false;

        // platformActive平台上新状态类型(ToOnSale/ToInStock)
        if (CmsConstants.PlatformActive.ToOnSale.name().equals(platformActive.name())) {
            // platformActive是(ToOnSale)时，执行商品上架操作
            updateListingResult = jdSaleService.doWareUpdateListing(shop, wareId, updateFlg);
        } else if (CmsConstants.PlatformActive.ToInStock.name().equals(platformActive.name())) {
            // platformActive是(ToInStock)时，执行商品下架操作
            updateListingResult = jdSaleService.doWareUpdateDelisting(shop, wareId, updateFlg);
        }

        // 回写上下架状态到product和productGroup表，并插入mongoDB上下架履历表cms_bt_platform_active_log_cXXX
        sxProductService.updateListingStatus(shop.getOrder_channel_id(), shop.getCart_id(), groupId, codeList, platformActive, updateListingResult, "", modifier);

    }

    /**
     * 取得当前平台主类目对应的销售属性状况
     * 1:颜色和尺寸属性都有 2:只有颜色没有尺寸属性 3:没有颜色只有尺寸属性 4:没有颜色没有尺寸属性
     *
     * @param cmsColorList List<CmsMtPlatformSkusModel> 颜色对象列表
     * @param cmsSizeList List<CmsMtPlatformSkusModel> 颜色对象列表
     * @param orgChannelId String 原始channelId(子店channelId)
     * @return String 当前平台主类目对应的销售属性状况
     */
    private String getSalePropStatus(List<CmsMtPlatformSkusModel> cmsColorList, List<CmsMtPlatformSkusModel> cmsSizeList, String orgChannelId) {

        // 当前平台主类目对应的销售属性状况(默认为4:没有颜色没有尺寸属性)
        String salePropStatus = SaleProp_None_Color_Size_4;

        if (ChannelConfigEnums.Channel.LUCKY_VITAMIN.getId().equals(orgChannelId)) {
            // 017:LuckyVitamin店一个SKU是一个CODE，所以他们希望全店都只有颜色，不要尺码
            // 2:只有颜色没有尺寸属性
            salePropStatus = SaleProp_Only_Color_2;
        } else {
            // platformActive平台上新状态类型(ToOnSale/ToInStock)
            if (ListUtils.notNull(cmsColorList) && ListUtils.notNull(cmsSizeList)) {
                // 1:颜色和尺寸属性都有
                salePropStatus = SaleProp_Both_Color_Size_1;
            } else if (ListUtils.notNull(cmsColorList) && ListUtils.isNull(cmsSizeList)) {
                // 2:只有颜色没有尺寸属性
                salePropStatus = SaleProp_Only_Color_2;
            } else if (ListUtils.isNull(cmsColorList) && ListUtils.notNull(cmsSizeList)) {
                // 3:没有颜色只有尺寸属性
                salePropStatus = SaleProp_Only_Size_3;
            }
        }

        return salePropStatus;
    }

    /**
     * 回写pCatId,pCatPath
     */
    public void updateCategory(String channelId, int cartId, String code, String catId) {
        CmsMtPlatformCategorySchemaModel cmsMtPlatformCategorySchema = platformCategoryService.getPlatformCatSchema(catId, cartId);
        if (cmsMtPlatformCategorySchema == null) {
            throw new BusinessException(String.format("获取平台类目信息失败 [平台类目Id:%s] [CartId:%s]", catId, cartId));
        }

        JongoUpdate updateProductQuery = new JongoUpdate();
        updateProductQuery.setQuery("{\"common.fields.code\": #}");
        updateProductQuery.setQueryParameters(code);

        updateProductQuery.setUpdate("{$set:{\"platforms.P"+ cartId +".pCatId\": #, \"platforms.P"+ cartId +".pCatPath\": #}}");
        updateProductQuery.setUpdateParameters(catId, cmsMtPlatformCategorySchema.getCatFullPath());

        cmsBtProductDao.updateFirst(updateProductQuery, channelId);

    }

    /**
     * 取得cms_mt_channel_config配置表中配置的值集合
     *
     * @param channelId String 渠道id
     * @param cartId int 平台id
     * @param channelConfigValueMap 返回cms_mt_channel_config配置表中配置的值集合用
     */
    public void doChannelConfigInit(String channelId, int cartId, Map<String, String> channelConfigValueMap) {

        // 从配置表(cms_mt_channel_config)表中取得颜色别名(ALIAS_29.color_alias)
        String colorAliasKey = CmsConstants.ChannelConfig.ALIAS + "_" + cartId + CmsConstants.ChannelConfig.COLOR_ALIAS;
        String colorAliasValue1 = getChannelConfigValue(channelId, CmsConstants.ChannelConfig.ALIAS,
                cartId + CmsConstants.ChannelConfig.COLOR_ALIAS);
        channelConfigValueMap.put(colorAliasKey, colorAliasValue1);
    }

    /**
     * 取得cms_mt_channel_config配置表中配置的值
     *
     * @param channelId String 渠道id
     * @param configKey CmsConstants.ChannelConfig ConfigKey
     * @param configCode String ConfigCode
     * @return String cms_mt_channel_config配置表中配置的值
     */
    public String getChannelConfigValue(String channelId, String configKey, String configCode) {
        if (StringUtils.isEmpty(channelId) || StringUtils.isEmpty(configKey)) return "";

        // 配置表(cms_mt_channel_config)表中ConfigCode的默认值为0
        String strConfigCode = "0";
        if (!StringUtils.isEmpty(configCode)) {
            strConfigCode = configCode;
        }

        String strConfigValue = "";
        // 通过配置表(cms_mt_channel_config)取得Configykey和ConfigCode对应的配置值(config_value1)
        CmsChannelConfigBean channelConfig = CmsChannelConfigs.getConfigBean(channelId, configKey, strConfigCode);
        if (channelConfig != null) {
            strConfigValue = channelConfig.getConfigValue1();
        }

        return strConfigValue;
    }

    /**
     * 给京东商品打标，设置一些特殊的属性(如：7天无理由退货等feature特殊属性)
     *
     * @param shop String 渠道id
     * @param jdProductBean JdProductBean 京东产品对象(目前只需要取得is7ToReturn,以后可能还会取得其他属性值)
     * @param jdWareId Long 京东商品id
     * @return String cms_mt_channel_config配置表中配置的值
     */
    public boolean doMergeWareFeatures(ShopBean shop, JdProductBean jdProductBean, Long jdWareId) {
        boolean result = false;
        if (shop == null || jdProductBean == null || jdWareId == null || jdWareId == 0) return result;

        // 特殊标key(最大20个，用逗号分隔)
        StringBuilder sbFeatureKey = new StringBuilder();
        // 特殊标值(最大20个，用逗号分隔)
        StringBuilder sbFeatureValue  = new StringBuilder();

        // 设置7天无理由退货(1为支持，0为不支持)
        if ("0".equals(jdProductBean.getIs7ToReturn()) || "1".equals(jdProductBean.getIs7ToReturn())) {
            sbFeatureKey.append("is7ToReturn");
            sbFeatureValue.append(jdProductBean.getIs7ToReturn());
            result = jdWareNewService.mergeWareFeatures(shop, jdWareId, sbFeatureKey.toString(), sbFeatureValue.toString());
        }

        return result;
    }

    /**
     * 从京东平台取得skuId回写到mongDB的product中
     */
    public void updateSkuIds(ShopBean shop, String wareId, boolean updateFlg) {
        if (shop == null || StringUtils.isEmpty(wareId)) return;

        String channelId = shop.getOrder_channel_id();
        String cartId = shop.getCart_id();
        StringBuilder failCause = new StringBuilder("");
        List<Sku> skus;

        try {
            // 先睡一会儿， 不然可能还没创建完成
            Thread.sleep(5000);
            // 根据京东商品id取得京东平台上的sku信息列表(即使出错也不报出来，算上新成功，只是回写出错，以后再回写也可以)
            skus = jdSkuService.getSkusByWareId(shop, wareId, failCause);

            if (ListUtils.isNull(skus)) return;

            // 循环取得的sku信息列表，把jdSkuId批量更新到product中去
            BulkJongoUpdateList bulkList = new BulkJongoUpdateList(1000, cmsBtProductDao, channelId);
            BulkWriteResult rs;
            for (Sku sku : skus) {
                JongoUpdate updObj = new JongoUpdate();
                updObj.setQuery("{'platforms.P"+ cartId +".skus.skuCode':#}");
                updObj.setQueryParameters(sku.getOuterId());
                updObj.setUpdate("{$set:{'platforms.P"+ cartId +".skus.$.jdSkuId':#,'modified':#,'modifier':#}}");
                updObj.setUpdateParameters(StringUtils.toString(sku.getSkuId()), DateTimeUtil.getNowTimeStamp(), getTaskName());
                rs = bulkList.addBulkJongo(updObj);
                if (rs != null) {
                    $debug("京东上新成功之后回写jdSkuId处理 channelId=%s, cartId=%s, wareId=%s, skuCode=%s, skuId=%s, jdSkuId更新结果=%s",
                            channelId, cartId, wareId, sku.getOuterId(), StringUtils.toString(sku.getSkuId()), rs.toString());
                }
            }

            rs = bulkList.execute();
            if (rs != null) {
                $debug("京东上新成功之后回写jdSkuId处理 channelId=%s, cartId=%s, wareId=%s, jdSkuId更新结果=%s", channelId, cartId, wareId, rs.toString());
            }

            // 如果是更新商品且取得skuId没有出错时，看看wareId对应的jdSkuId非空sku中，哪些sku在京东平台上没有,把删除的sku的jdSkuId清空
            if (updateFlg && StringUtils.isEmpty(failCause.toString())) {
                // 先取得wareId对应的所有产品有jdSkuId的platforms.PXX.skus.skuCode
                JongoQuery queryObj = new JongoQuery();
                queryObj.setQuery("{'platforms.P"+ cartId +".pNumIId':#}");
                queryObj.setParameters(wareId);
                queryObj.setProjectionExt("platforms.P"+ cartId +".skus");
                List<CmsBtProductModel> productList = cmsBtProductDao.select(queryObj, channelId);
                if (ListUtils.notNull(productList)) {
                    BulkJongoUpdateList bulkList2 = new BulkJongoUpdateList(1000, cmsBtProductDao, channelId);
                    BulkWriteResult rs2;
                    for (CmsBtProductModel product : productList) {
                        List<BaseMongoMap<String, Object>> prodPlatformSkus = product.getPlatformNotNull(NumberUtils.toInt(cartId)).getSkus();
                        if (ListUtils.isNull(prodPlatformSkus)) continue;
                        for (BaseMongoMap<String, Object> currentSku : prodPlatformSkus) {
                            // 如果当前sku的jdSkuId不为空，并且在京东平台上取得的sku列表中不存在的时候，需要把数据库中的jdSkuId清空
                            if (!StringUtils.isEmpty(currentSku.getStringAttribute("jdSkuId"))
                                    && skus.stream().filter(p -> currentSku.getStringAttribute("skuCode").equals(p.getOuterId())).count() == 0) {
                                // 构造更新语句
                                JongoUpdate updObj = new JongoUpdate();
                                updObj.setQuery("{'platforms.P"+ cartId +".skus.skuCode':#}");
                                updObj.setQueryParameters(currentSku.getStringAttribute("skuCode"));
                                updObj.setUpdate("{$set:{'platforms.P"+ cartId +".skus.$.jdSkuId':#,'modified':#,'modifier':#}}");
                                updObj.setUpdateParameters("", DateTimeUtil.getNowTimeStamp(), getTaskName());
                                rs2 = bulkList2.addBulkJongo(updObj);
                                if (rs2 != null) {
                                    $debug("京东上新回写jdSkuId之后，清空平台上已被删除的jdSkuId处理 channelId=%s, cartId=%s, wareId=%s, skuCode=%s, jdSkuId清空更新结果=%s",
                                            channelId, cartId, wareId, currentSku.getStringAttribute("skuCode"), rs.toString());
                                }
                            }
                        }
                    }
                    rs2 = bulkList2.execute();
                    if (rs2 != null) {
                        $debug("京东上新回写jdSkuId之后，清空平台上已被删除的jdSkuId处理 channelId=%s, cartId=%s, wareId=%s, jdSkuId清空更新结果=%s", channelId, cartId, wareId, rs2.toString());
                    }
                }
            }
        } catch (Exception e) {
            $warn(String.format("京东上新成功之后，回写jdSkuId时出错！[wareId:%s] [errMsg:%s]", wareId, failCause.toString()));
        }
    }

//    /**
//     * 判断指定sku的库存是否为0
//     *
//     * @param skuCode String skuCode
//     * @param skuLogicQtyMap sku级别的逻辑库存map
//     * @return boolean 该sku库存为0时返回true,否则返回false
//     */
//    public boolean isSkuNoStock(String skuCode, Map<String, Integer> skuLogicQtyMap) {
//        // 如果对象skuCode在库存信息map中不存在的时候，直接返回true
//        if (!skuLogicQtyMap.keySet().contains(skuCode)) return true;
//        return skuLogicQtyMap.entrySet().stream().filter(s -> skuCode.equals(s.getKey())).findFirst().get().getValue() == 0;
//    }
//
//    /**
//     * 删除产品中库存为0的common.sku
//     *
//     * @param skus List<CmsBtProductModel_Sku> common.sku列表
//     * @param skuLogicQtyMap sku级别的逻辑库存map
//     */
//    public void deleteNoStockCommonSku(List<CmsBtProductModel_Sku> skus, Map<String, Integer> skuLogicQtyMap) {
//        Iterator<CmsBtProductModel_Sku> skuIter = skus.iterator();
//        while (skuIter.hasNext()) {
//            CmsBtProductModel_Sku sku = skuIter.next();
//            // 如果该skuCode对应的库存为0(或库存信息不存在)，则把该sku加到skuListNoStock列表中，并在sxData.skuList中删除这个sku
//            if (isSkuNoStock(sku.getSkuCode(), skuLogicQtyMap)) {
//                // 已经Mapping过的颜色值从颜色列表中删除
//                skuIter.remove();
//            }
//        }
//    }
//
//    /**
//     * 删除产品中库存为0的pXX.sku
//     *
//     * @param skus List<BaseMongoMap<String, Object>> sku列表
//     * @param skuLogicQtyMap sku级别的逻辑库存map
//     */
//    public void deleteNoStockPlatformSku(List<BaseMongoMap<String, Object>> skus, Map<String, Integer> skuLogicQtyMap) {
//        Iterator<BaseMongoMap<String, Object>> skuIter = skus.iterator();
//        while (skuIter.hasNext()) {
//            BaseMongoMap<String, Object> sku = skuIter.next();
//            // 如果该skuCode对应的库存为0(或库存信息不存在)，则把该sku加到skuListNoStock列表中，并在sxData.skuList中删除这个sku
//            if (isSkuNoStock(sku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.skuCode.name()), skuLogicQtyMap)) {
//                // 已经Mapping过的颜色值从颜色列表中删除
//                skuIter.remove();
//            }
//        }
//    }
//
//    /**
//     * 删除一条京东平台上本地库存为0的SKU
//     *
//     * @param shop 店铺信息
//     * @param jdSkuId 库存为0的京东skuId
//     * @return String 如果平台上的SKU全部要删除时，返回最后一个不能删除的skuId;没有的报这个错的时候，返回null
//     */
//    public String deleteJdPlatformSku(ShopBean shop, String jdSkuId) {
//        List<String> skuIdListNoStock = new ArrayList<>();
//        skuIdListNoStock.add(jdSkuId);
//
//        return deleteJdPlatformSku(shop, skuIdListNoStock);
//    }

    /**
     * 删除京东平台上本地库存为0的SKU(不能删除平台上剩下的最后一个SKU)
     * 这里先删除库存为0的SKU，后面再更新库存不为0的SKU信息到京东平台；如果后面不再更新一下的话，不能达到库存为0的SKU显示但不能选的效果
     *
     * @param shop 店铺信息
     * @param skuIdListNoStock List<String> 库存为0的skuId列表
     * @return String 如果平台上的SKU全部要删除时，返回最后一个不能删除的skuId;没有的报这个错的时候，返回null
     */
    public String deleteJdPlatformSku(ShopBean shop, List<String> skuIdListNoStock) {
        if (shop == null || ListUtils.isNull(skuIdListNoStock)) return null;

        String lastSkuId = null;
        StringBuilder sbFailCause = new StringBuilder("");
        // 删除京东平台上库存为0的SKU
        for (String jdSkuId : skuIdListNoStock) {
            sbFailCause.setLength(0);
            // 根据jdSkuId删除京东平台商品的SKU信息
            jdSkuService.deleteSkuBySkuId(shop, jdSkuId, sbFailCause);
            if (!StringUtils.isEmpty(sbFailCause.toString())) {
                // 删除库存为0的SKU失败输入错误消息，但不需要抛出异常
                $debug(sbFailCause.toString());
                // 京东删除商品最后一个SKU的时候，会报"此SKU为商品最后sku，不允许删除"错误
                if (sbFailCause.toString().contains("此SKU为商品最后sku")) {
                    lastSkuId = jdSkuId;
                    break;
                }
            }
        }

        return lastSkuId;
    }

    /**
     * 获得log头部信息
     *
     * @param shopName 店铺名称
     * @param sxType   上新类型
     * @return String  log头部信息
     */
    private String getPreMsg(String shopName, String sxType) {
        return shopName + "[" + sxType + "] ";
    }


    /**
     * 全量保存京东商品的SKU属性
     *
//     * @param targetProductBean JdProductBean   产品对象
     * @param shop ShopBean 店铺信息
     * @param wareId Long   京东商品id
     * @param sxData SxData 产品对象
     * @param productColorMap Map<String, Object> 产品和颜色值Mapping关系表(填入值，后面上传颜色图片时会用到)
     * @param skuLogicQtyMap Map<String, Integer> 所有SKU的逻辑库存列表
     * @param cmsColorList List<CmsMtPlatformSkusModel> 该类目对应的颜色SKU列表
     * @param cmsSizeList List<CmsMtPlatformSkusModel> 该类目对应的尺寸SKU列表
     * @param salePropStatus String 当前平台主类目对应的销售属性状况
     * @param channelConfigValueMap cms_mt_channel_config配置表中配置的值集合
     * @return JdProductBean 京东上新用bean
     * @throws BusinessException
     */
    protected Set<Prop> doSaveWareSkus(ShopBean shop, JdProductNewBean jdProductBean, SxData sxData, Map<String, Object> productColorMap,
                                     Map<String, Integer> skuLogicQtyMap, List<CmsMtPlatformSkusModel> cmsColorList,
                                     List<CmsMtPlatformSkusModel> cmsSizeList, String salePropStatus,
                                     Map<String, String> channelConfigValueMap) {

        List<CmsBtProductModel> productList = sxData.getProductList();
        List<BaseMongoMap<String, Object>> skuList = sxData.getSkuList();

        // 产品和颜色的Mapping表(因为后面上传SKU图片的时候也要用到，所以从外面传进来)
        // SKU尺寸和尺寸值的Mapping表(Map<上新用尺码, 平台取下来的尺码值value>)
        Map<String, Object> skuSizeMap = new HashMap<>();

        // 根据类目的销售属性从cms_mt_platform_skus表中相应颜色和尺寸的值的对应关系，存放在productColorMap和skuSizeList表中
        doSetColorSizeMappingInfo(sxData, cmsColorList, cmsSizeList, salePropStatus, productColorMap, skuSizeMap);

        // 全量保存SKU接口用对象SKU列表
        List<com.jd.open.api.sdk.domain.Sku> salePropSkuList = new ArrayList<>();
        // 更新商品维度的销售属性值别名用总的别名列表
        Set<Prop> allSkuSaleProps = new HashSet<>();

        // 根据product列表循环设置该商品的SKU属性
        for (CmsBtProductModel objProduct : productList) {
            // 取得颜色别名的值
            String colorAlias = getColorAlias(objProduct, StringUtils.toString(sxData.getCartId()), channelConfigValueMap);
            String productCode = objProduct.getCommon().getFields().getCode();

            CmsBtProductModel_Platform_Cart platformCart = objProduct.getPlatform(sxData.getCartId());
            if (platformCart == null || ListUtils.isNull(platformCart.getSkus())) continue;
            List<BaseMongoMap<String, Object>> platformSkuList = platformCart.getSkus();
            for (BaseMongoMap<String, Object> pSku : platformSkuList) {
                String pSkuCode = pSku.getStringAttribute("skuCode");
                // 在skuList中找到对应sku信息，然后设置需要的属性
                for (BaseMongoMap<String, Object> objSku : skuList) {
                    // 如果没有找到对应skuCode，则继续循环
                    if (!pSkuCode.equals(objSku.getStringAttribute("skuCode"))) {
                        continue;
                    }

                    // 上新用尺码别名
                    String sizeSx = objSku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.sizeSx.name());

                    // 全量保存用SKU对象
                    com.jd.open.api.sdk.domain.Sku sku = new com.jd.open.api.sdk.domain.Sku();
                    String colorKey = null;
                    String sizeKey = null;
                    Set<Prop> saleProp = null;
                    String[] colorAttrValues = null;
                    String[] sizeAttrValues = null;
                    Prop colorProp = null;
                    Prop sizeProp = null;

                    // 设置SKU颜色和尺码销售属性
                    switch (salePropStatus) {
                        case SaleProp_Both_Color_Size_1:
                            // 颜色和尺码属性都有时(颜色1^尺码1)
                            colorKey = productCode;
                            sizeKey = sizeSx;
                            saleProp = new HashSet<>();
                            // 将当前code对应的颜色属性值(来自cms_mt_platform_skus表)解析成颜色属性id和颜色属性值id
                            colorAttrValues = getColorAttrValueId(productCode, colorKey, productColorMap, platformCart.getpCatId(), platformCart.getpCatPath());
                            // 当前SKU的颜色销售属性
                            colorProp = getSaleProp(colorAttrValues[0], colorAttrValues[1], colorAlias);
                            saleProp.add(colorProp);
                            allSkuSaleProps.add(colorProp);   // 后面更新销售属性别名用
                            // 将当前sku对应的尺码属性值(来自cms_mt_platform_skus表)解析成尺码属性id和尺码属性值id
                            sizeAttrValues = getSizeAttrValueId(productCode, pSkuCode, sizeSx, sizeKey, skuSizeMap, platformCart.getpCatId(), platformCart.getpCatPath());
                            // 当前SKU的尺码销售属性
                            sizeProp = getSaleProp(sizeAttrValues[0], sizeAttrValues[1], sizeKey);
                            saleProp.add(sizeProp);
                            allSkuSaleProps.add(sizeProp);    // 后面更新销售属性别名用
                            sku.setSaleAttrs(saleProp);
                            break;
                        case SaleProp_Only_Color_2:
                            // 只有颜色属性时(颜色1^颜色2)
                            // 如果平台类目只有颜色没有尺寸信息时，productColorMap中的key为productCode_sizeSx
                            colorKey = productCode + "_" + sizeSx;
                            saleProp = new HashSet<>();
                            // 将当前code对应的颜色属性值(来自cms_mt_platform_skus表)解析成颜色属性id和颜色属性值id
                            colorAttrValues = getColorAttrValueId(productCode, colorKey, productColorMap, platformCart.getpCatId(), platformCart.getpCatPath());
                            // 当前SKU的颜色销售属性
                            colorProp = getSaleProp(colorAttrValues[0], colorAttrValues[1], colorAlias);
                            saleProp.add(colorProp);
                            allSkuSaleProps.add(colorProp);   // 后面更新销售属性别名用
                            sku.setSaleAttrs(saleProp);
                            break;
                        case SaleProp_Only_Size_3:
                            // 只有尺码属性时(尺码1^尺码2)
                            /// 如果平台类目没有颜色只有尺寸信息时，skuSizeMap中的key为productCode_sizeSx
                            sizeKey = productCode + "_" + sizeSx;
                            saleProp = new HashSet<>();
                            // 将当前sku对应的尺码属性值(来自cms_mt_platform_skus表)解析成尺码属性id和尺码属性值id
                            sizeAttrValues = getSizeAttrValueId(productCode, pSkuCode, sizeSx, sizeKey, skuSizeMap, platformCart.getpCatId(), platformCart.getpCatPath());
                            // 当前SKU的尺码销售属性
                            sizeProp = getSaleProp(sizeAttrValues[0], sizeAttrValues[1], sizeKey);
                            saleProp.add(sizeProp);
                            allSkuSaleProps.add(sizeProp);    // 后面更新销售属性别名用
                            sku.setSaleAttrs(saleProp);
                            break;
                    }

                    // 设置SKU京东价，库存，外部Code等属性
                    // SKU京东价(100.0)
                    Double skuPrice = getSkuPrice(objSku, shop.getOrder_channel_id(), shop.getCart_id(), CmsConstants.ChannelConfig.PRICE_SALE_KEY, CmsConstants.ChannelConfig.PRICE_SALE_PRICE_CODE);//PriceType_jdprice
                    sku.setJdPrice(new BigDecimal(skuPrice));
                    // SKU库存(3)
                    sku.setStockNum(LongUtils.parseLong(skuLogicQtyMap.get(pSkuCode)));
                    // SKU外部ID(skuCode)
                    sku.setOuterId(pSkuCode);
                    salePropSkuList.add(sku);
                }
            }
        }

        // 以防万一，check一下
        if (ListUtils.isNull(salePropSkuList)) {
            String errMsg = String.format("全量保存SKU接口用对象SKU列表为空，不能全量更新该商品的SKU信息！[wareId:%s]", StringUtils.toString(jdProductBean.getWareId()));
            $error(errMsg);
            throw new BusinessException(errMsg);
        }
        //charis update 将sku信息设到bean里
        jdProductBean.setSkus(salePropSkuList);


//         调用京东全量保存SKU接口，更新当前商品的SKU信息
//        List<Sku> resultSkus = jdWareNewService.saveWareSkus(shop, wareId, salePropSkuList, failCause);
//        if (ListUtils.isNull(resultSkus) || !StringUtils.isEmpty(failCause.toString())) {
//            $error(failCause.toString());
//            throw new BusinessException(failCause.toString());
//        }



        return allSkuSaleProps;
    }

    /**
     * 取得当前产品code对应的颜色别名
     *
     * @param objProduct  产品产品
     * @param cartId 平台id
     * @param channelConfigValueMap cms_mt_channel_config配置表中配置的值集合
     * @return String 颜色别名值
     */
    protected String getColorAlias(CmsBtProductModel objProduct, String cartId, Map<String, String> channelConfigValueMap) {
        // 如果该店铺在cms_mt_channel_config表中配置了使用哪个字段作为颜色别名就用配置的字段(如:color),否则默认为用code作为颜色别名
        String colorAlias;
        // 颜色别名配置key(ALIAS_29.color_alias)
        String colorAliasKey = CmsConstants.ChannelConfig.ALIAS + "_" + cartId + CmsConstants.ChannelConfig.COLOR_ALIAS;
        // 颜色别名
        if ("color".equalsIgnoreCase(channelConfigValueMap.get(colorAliasKey))) {
            colorAlias = objProduct.getCommon().getFields().getColor();
        } else {
            colorAlias = objProduct.getCommon().getFields().getCode();
            if (colorAlias.length() > 25) {
                colorAlias = objProduct.getCommon().getFields().getColor();
            }
        }

        return colorAlias;
    }

    /**
     * 取得颜色属性id和属性值id列表
     *
     * @param productCode  产品code
     * @param productColorMap   颜色属性值mapping信息
     * @param catId        类目id
     * @param catPath      类目Path
     * @return String[] 颜色属性id和属性值id列表
     */
    protected String[] getColorAttrValueId(String productCode, String colorKey, Map<String, Object> productColorMap, String catId, String catPath) {
        if (!productColorMap.containsKey(colorKey)) {
            // 该group下产品件数比京东平台上该类目的颜色属性件数多，强制上新会报"参数错误.销售属性维度不一致"的异常
            String errMsg = String.format("产品(%s)没找到对应的平台类目(%s:%s)的颜色属性值,原因是本group下面的" +
                            "产品或SKU件数比京东平台上该类目的颜色属性件数(%s件)多，强制上新会报\"参数错误.销售属性维度不一致\"的异常.",
                    productCode, catId, catPath, productColorMap.size());
            $error(errMsg);
            throw new BusinessException(errMsg);
        }
        // 将当前code对应的颜色属性值(来自cms_mt_platform_skus表)解析成颜色属性id和颜色属性值id
        String[] colorAttrValues = StringUtils.toString(productColorMap.get(colorKey)).split(Separtor_Colon);
        if (colorAttrValues == null || colorAttrValues.length < 2) {
            String errMsg = StringUtils.format("cms_mt_platform_skus表中的属性值格式(\"属性id:属性值id\")不正确. " +
                    "[属性值:%s]", StringUtils.toString(productColorMap.get(colorKey)));
            $error(errMsg);
            throw new BusinessException(errMsg);
        }

        return colorAttrValues;
    }

    /**
     * 取得尺码属性id和属性值id列表
     *
     * @param productCode   产品code
     * @param skuCode       SKU code
     * @param sizeSx       上信用尺码别名
     * @param sizeKey       尺码mapping key
     * @param skuSizeMap   尺码属性值mapping信息
     * @param catId        类目id
     * @param catPath      类目Path
     * @return String[] 颜色属性id和属性值id列表
     */
    protected String[] getSizeAttrValueId(String productCode, String skuCode, String sizeSx, String sizeKey, Map<String, Object> skuSizeMap, String catId, String catPath) {
        if (!skuSizeMap.containsKey(sizeKey)) {
            // 该产品上新用尺码件数(<=sku件数)比京东平台上该类目的尺码属性件数多，强制上新会报"参数错误.销售属性维度不一致"的异常
            String errMsg = String.format("产品(%s)的sku(%s)的上新用尺码(%s)没找到对应的平台类目(%s:%s)的尺码属性值，" +
                            "原因是该产品上新用SKU尺码件数比京东平台上该类目的尺码属性件数(%s件)多，强制上新" +
                            "会报\"参数错误.销售属性维度不一致\"的异常.", productCode, skuCode, sizeSx, catId,
                            catPath, skuSizeMap.size());
            $error(errMsg);
            throw new BusinessException(errMsg);
        }
        // 将当前sku对应的尺码属性值(来自cms_mt_platform_skus表)解析成尺码属性id和尺码属性值id
        String[] sizeAttrValues = StringUtils.toString(skuSizeMap.get(sizeKey)).split(Separtor_Colon);
        if (sizeAttrValues == null || sizeAttrValues.length < 2) {
            String errMsg = StringUtils.format("cms_mt_platform_skus表中的属性值格式(\"属性id:属性值id\")不正确. " +
                    "[属性值:%s]", StringUtils.toString(skuSizeMap.get(sizeKey)));
            $error(errMsg);
            throw new BusinessException(errMsg);
        }

        return sizeAttrValues;
    }

    /**
     * 取得SKU的颜色或尺码销售对象
     *
     * @param salePropAttrId  当前类目的颜色或者尺码属性id
     * @param attrValue       属性值(颜色或者尺码的属性值)
     * @param attrValueAlias  别名(颜色或者尺码别名)
     * @return Prop 单个SKU颜色或者尺码销售属性
     */
    protected Prop getSaleProp(String salePropAttrId, String attrValue, String attrValueAlias) {

        Prop prop = new Prop();
        prop.setAttrId(salePropAttrId);                 // 当前类目的颜色或者尺码属性id(如：1000000051)
        String[] arrAttrValue = {attrValue};            // 当前code对应的颜色属性值1（如：1565522969）
        prop.setAttrValues(arrAttrValue);
        String[] arrAttrValueAlias = {attrValueAlias};  // 颜色别名或者尺码别名（如：红色）
        prop.setAttrValueAlias(arrAttrValueAlias);

        return prop;
    }

    /**
     * 根据当前类目销售属性类型设置取得颜色和尺码的值
     *
     * @param sxData SxData 产品对象
     * @param cmsColorList List<CmsMtPlatformSkusModel> 该类目对应的颜色SKU列表
     * @param cmsSizeList List<CmsMtPlatformSkusModel> 该类目对应的尺寸SKU列表
     * @param salePropStatus String 当前平台主类目对应的销售属性状况
     * @param productColorMap Map<String, Object> 产品和颜色值Mapping关系表(填入值，后面上传颜色图片时会用到)
     * @param skuSizeMap  Map<String, Object> SKU和尺码值Mapping关系表
     */
    protected void doSetColorSizeMappingInfo(SxData sxData,
                                             List<CmsMtPlatformSkusModel> cmsColorList,
                                             List<CmsMtPlatformSkusModel> cmsSizeList,
                                             String salePropStatus,
                                             Map<String, Object> productColorMap,
                                             Map<String, Object> skuSizeMap) {
        List<CmsBtProductModel> productList = sxData.getProductList();
        List<BaseMongoMap<String, Object>> skuList = sxData.getSkuList();

        // 当前平台主类目对应的销售属性状况(1:颜色和尺寸属性都有 2:只有颜色没有尺寸属性 3:没有颜色只有尺寸属性 4:没有颜色没有尺寸属性)
        switch (salePropStatus) {
            case SaleProp_Both_Color_Size_1:
                // 如果该平台类目颜色属性和尺寸信息都有的时候，则把每个product作为一种颜色
                // 根据product列表取得要上新的产品颜色Mapping关系
                for (CmsBtProductModel product : productList) {
                    // 取得颜色值列表中的第一个颜色值
                    if (cmsColorList.size() > 0) {
                        // "产品code":"颜色值Id" Mapping追加
                        productColorMap.put(product.getCommon().getFields().getCode(), cmsColorList.get(0).getAttrValue());
                        // 已经Mapping过的颜色值从颜色列表中删除
                        cmsColorList.remove(0);
                    } else {
                        String errMsg = String.format("该商品的product总件数比cms_mt_platform_skus表中颜色值件数多，该产品(%s)未" +
                                "找到对应的颜色值！[salePropStatus:%s(颜色和尺寸属性都有)]", product.getCommon().getFields().getCode(), salePropStatus);
                        $error(errMsg);
                        throw new BusinessException(errMsg);
                    }
                }

                // 根据sku列表(根据sizeSx排序)取得要上新的产品尺寸Mapping关系
                for (BaseMongoMap<String, Object> sku : skuList) {
                    // SKU和尺寸的Mapping表中不存在的话，追加进去(已存在不要再追加)
                    // skuSizeMap<上新用尺码, 平台取下来的尺码值value> 直接用共通方法里面转换后的上新用尺码作为尺码别名上新
                    // 上新用尺码(sizeSx)的设置顺序：sizeNick（特殊尺码转换信息） > 尺码转换表（共通尺码转换信息） > size (转换前尺码)
                    if (!skuSizeMap.containsKey(sku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.sizeSx.name()))) {
                        // 取得尺寸列表中的第一个尺寸值
                        if (cmsSizeList.size() > 0) {
                            // "SKU尺寸(3,3.5等)":"尺寸值Id" Mapping追加
                            skuSizeMap.put(sku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.sizeSx.name()), cmsSizeList.get(0).getAttrValue());
                            // 已经Mapping过的尺寸值从尺寸列表中删除
                            cmsSizeList.remove(0);
                        } else {
                            String errMsg = String.format("SKU尺寸件数比cms_mt_platform_skus表中尺寸值件数多，该尺寸(sizeSx:%s)" +
                                            "未找到对应的尺寸值! [salePropStatus:%s(颜色和尺寸属性都有)]",
                                    sku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.sizeSx.name()), salePropStatus);
                            $error(errMsg);
                            throw new BusinessException(errMsg);
                        }
                    }
                }
                break;
            case SaleProp_Only_Color_2:
                // 如果该平台类目只有颜色属性，没有尺寸信息，则把product下面的每个sku作为一种颜色
                // 根据product列表取得要上新的产品颜色Mapping关系
                for (CmsBtProductModel product : productList) {
                    CmsBtProductModel_Platform_Cart platformCart = product.getPlatform(sxData.getCartId());
                    if (platformCart == null || ListUtils.isNull(platformCart.getSkus())) continue;
                    List<BaseMongoMap<String, Object>> platformSkuList = platformCart.getSkus();
                    for (BaseMongoMap<String, Object> pSku : platformSkuList) {
                        String pSkuCode = pSku.getStringAttribute("skuCode");
                        for (BaseMongoMap<String, Object> objSku : skuList) {
                            // 如果没有找到对应skuCode，则继续循环
                            if (!pSkuCode.equals(objSku.getStringAttribute("skuCode"))) {
                                continue;
                            }

                            // productMap中的key为productCode_sizeSx
                            String colorKey = product.getCommon().getFields().getCode() + "_" +
                                    objSku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.sizeSx.name());

                            // 取得颜色值列表中的第一个颜色值
                            if (cmsColorList.size() > 0) {
                                // "产品code":"颜色值Id" Mapping追加
                                productColorMap.put(colorKey, cmsColorList.get(0).getAttrValue());
                                // 已经Mapping过的颜色值从颜色列表中删除
                                cmsColorList.remove(0);
                            } else {
                                String errMsg = String.format("该商品的产品sku总件数比cms_mt_platform_skus表中颜色值件数多，该SKU未" +
                                                "找到对应的颜色值！[code:%s] [skuCode:%s] [sizeSx:%s] [salePropStatus:%s(2:只有颜色没有尺寸," +
                                                "把product下面的每个sku作为一种颜色)]", product.getCommon().getFields().getCode(), pSkuCode,
                                        objSku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.sizeSx.name()), salePropStatus);
                                $error(errMsg);
                                throw new BusinessException(errMsg);
                            }
                        }
                    }
                }
                break;
            case SaleProp_Only_Size_3:
                // 如果该平台类目没有颜色属性，只有尺寸信息，则把product下面的每个sku作为一种尺寸
                // 根据product列表取得要上新的产品尺寸Mapping关系
                for (CmsBtProductModel product : productList) {
                    CmsBtProductModel_Platform_Cart platformCart = product.getPlatform(sxData.getCartId());
                    if (platformCart == null || ListUtils.isNull(platformCart.getSkus())) continue;
                    List<BaseMongoMap<String, Object>> platformSkuList = platformCart.getSkus();
                    for (BaseMongoMap<String, Object> pSku : platformSkuList) {
                        String pSkuCode = pSku.getStringAttribute("skuCode");
                        for (BaseMongoMap<String, Object> objSku : skuList) {
                            // 如果没有找到对应skuCode，则继续循环
                            if (!pSkuCode.equals(objSku.getStringAttribute("skuCode"))) {
                                continue;
                            }

                            // productSizeMap中的key为productCode_sizeSx
                            String sizeKey = product.getCommon().getFields().getCode() + "_" +
                                    objSku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.sizeSx.name());

                            // 上新用尺码(sizeSx)的设置顺序：sizeNick（特殊尺码转换信息） > 尺码转换表（共通尺码转换信息） > size (转换前尺码)
                            if (!skuSizeMap.containsKey(objSku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.sizeSx.name()))) {
                                // 取得尺寸列表中的第一个尺寸值
                                if (cmsSizeList.size() > 0) {
                                    // "SKU尺寸(3,3.5等)":"尺寸值Id" Mapping追加
                                    skuSizeMap.put(sizeKey, cmsSizeList.get(0).getAttrValue());
                                    // 已经Mapping过的尺寸值从尺寸列表中删除
                                    cmsSizeList.remove(0);
                                } else {
                                    String errMsg = String.format("该商品SKU总件数比cms_mt_platform_skus表中尺寸值件数多，该SKU未找到对应的尺寸值!" +
                                                    "[code:%s] [skuCode:%s] [sizeSx:%s] [salePropStatus:%s (没有颜色只有尺寸,把product下面的每个sku作为一种尺寸)]",
                                            product.getCommon().getFields().getCode(), pSkuCode,
                                            objSku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.sizeSx.name()), salePropStatus);
                                    $error(errMsg);
                                    throw new BusinessException(errMsg);
                                }
                            }
                        }
                    }
                }
                break;
        }
    }


}
