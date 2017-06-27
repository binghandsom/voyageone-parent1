package com.voyageone.task2.cms.service;

import com.google.common.base.Joiner;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
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
import com.voyageone.common.masterdate.schema.value.Value;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.ecerp.interfaces.third.koala.KoalaItemService;
import com.voyageone.ecerp.interfaces.third.koala.beans.KoalaApiError;
import com.voyageone.ecerp.interfaces.third.koala.beans.KoalaConfig;
import com.voyageone.ecerp.interfaces.third.koala.beans.request.ItemAddPartRequest;
import com.voyageone.ecerp.interfaces.third.koala.beans.response.ItemAddPartResponse;
import com.voyageone.ecerp.interfaces.third.koala.beans.response.SkuOuterIdResult;
import com.voyageone.ecerp.interfaces.third.koala.support.KoalaApiException;
import com.voyageone.ims.rule_expression.MasterWord;
import com.voyageone.ims.rule_expression.RuleExpression;
import com.voyageone.service.bean.cms.product.SxData;
import com.voyageone.service.dao.cms.CmsBtKlSkuDao;
import com.voyageone.service.dao.cms.CmsMtChannelConditionMappingConfigDao;
import com.voyageone.service.impl.cms.CmsMtPlatformSkusService;
import com.voyageone.service.impl.cms.DictService;
import com.voyageone.service.impl.cms.PlatformCategoryService;
import com.voyageone.service.impl.cms.PlatformProductUploadService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.sx.rule_parser.ExpressionParser;
import com.voyageone.service.model.cms.*;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategorySchemaModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductConstants;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform_Cart;
import com.voyageone.task2.base.BaseCronTaskService;
import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.base.util.TaskControlUtils;
import com.voyageone.task2.cms.model.ConditionPropValueModel;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * 考拉平台产品上新服务
 *
 * @author 2017/6/16.
 * @version 2.1.0
 * @since 2.0.0
 */
//@SuppressWarnings("ALL")
@Service
public class CmsBuildPlatformProductUploadKlService extends BaseCronTaskService {

    // 商品预定义属性值列表
    private final static String Attributes = "attributes";
    // 用户自行输入的属性值串
    private final static String Input_Strs = "input_strs";
    // 前台展示的商家自定义店内分类
//    private final static String Prop_ShopCategory = "seller_cids_";
    // 考拉运费模板
    private final static String Prop_TransportId = "transportid_";
    // SKU属性类型(颜色)
    private final static String AttrType_Color = "c";
    // SKU属性类型(尺寸)
    private final static String AttrType_Size = "s";
    // SKU属性Active
    private final static int AttrType_Active_1 = 1;
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
    // 分隔符(^)
    private final static String Separtor_Caret = "^";
    // 销售属性_颜色和尺码都有
    private final static String SaleProp_Both_Color_Size_1 = "1";
    // 销售属性_只有颜色没有尺码属性
    private final static String SaleProp_Only_Color_2 = "2";
    // 销售属性_只有尺码没有颜色属性
    private final static String SaleProp_Only_Size_3 = "3";
    // 销售属性_颜色和尺码都没有
    private final static String SaleProp_None_Color_Size_4 = "4";
    // 特殊属性Key(七天无理由退货)
//    private final static String Is7ToReturn = "is7ToReturn";

    private static final int CART_ID = CartEnums.Cart.KL.getValue();

    @Autowired
    private DictService dictService;
    @Autowired
    private PlatformProductUploadService platformProductUploadService;
    @Autowired
    private PlatformCategoryService platformCategoryService;
    @Autowired
    private CmsMtPlatformSkusService cmcMtPlatformSkusService;
    @Autowired
    private SxProductService sxProductService;
    @Autowired
    private ProductService productService;
    @Autowired
    private CmsMtChannelConditionMappingConfigDao cmsMtChannelConditionMappingConfigDao;
    @Autowired
    private KoalaItemService koalaItemService;
    @Autowired
    private CmsBtKlSkuDao cmsBtKlSkuDao;

    private Map<String, Map<String, List<ConditionPropValueModel>>> channelConditionConfig;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "CmsBuildPlatformProductUploadKlJob";
    }

    /**
     * 考拉平台上新处理
     *
     * @param taskControlList taskcontrol信息
     */
    @Override
    public void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        doUploadMain(taskControlList);

        // 正常结束
        $info("主线程正常结束");
    }

    public void doUploadMain(List<TaskControlBean> taskControlList) {
        // 由于这个方法可能会自己调用自己循环很多很多次， 不一定会跳出循环， 但又希望能获取到最新的TaskControl的信息， 所以不使用基类里的这个方法了
        // 为了调试方便， 允许作为参数传入， 但是理想中实际运行中， 基本上还是自主获取的场合比较多
        if (taskControlList == null) {
            taskControlList = taskDao.getTaskControlList(getTaskName());

            if (taskControlList.isEmpty()) {
//                $info("没有找到任何配置。");
                logIssue("没有找到任何配置！！！", getTaskName());
                return;
            }

            // 是否可以运行的判断
            if (!TaskControlUtils.isRunnable(taskControlList)) {
                $info("Runnable is false");
                return;
            }

        }

        // 每个小组， 最多允许的线程数量
        int threadCount = NumberUtils.toInt(TaskControlUtils.getVal1WithDefVal(taskControlList, TaskControlEnums.Name.thread_count, "10"));

        // 获取该任务可以运行的销售渠道
        List<TaskControlBean> taskControlBeanList = TaskControlUtils.getVal1s(taskControlList, TaskControlEnums.Name.order_channel_id);

        // 准备按组分配线程（相同的组， 会共用相同的一组线程通道， 不同的组， 线程通道互不干涉）
        Map<String, List<String>> mapTaskControl = new HashMap<>();
        taskControlBeanList.forEach((l) -> {
            String key = l.getCfg_val2();
            if (StringUtils.isEmpty(key)) {
                key = "0";
            }
            if (mapTaskControl.containsKey(key)) {
                mapTaskControl.get(key).add(l.getCfg_val1());
            } else {
                List<String> channelList = new ArrayList<>();
                channelList.add(l.getCfg_val1());
                mapTaskControl.put(key, channelList);
            }
        });

        Map<String, ExecutorService> mapThread = new HashMap<>();

        while (true) {

            mapTaskControl.forEach((k, v) -> {
                boolean blnCreateThread = false;

                if (mapThread.containsKey(k)) {
                    ExecutorService t = mapThread.get(k);
                    if (t.isTerminated()) {
                        // 可以新做一个线程
                        blnCreateThread = true;
                    }
                } else {
                    // 可以新做一个线程
                    blnCreateThread = true;
                }

                if (blnCreateThread) {
                    ExecutorService t = Executors.newSingleThreadExecutor();

                    List<String> channelIdList = v;
                    if (channelIdList != null) {
                        for (String channelId : channelIdList) {
                            t.execute(() -> {
                                try {
                                    doProductUpload(channelId, CART_ID, threadCount);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            });

                        }
                    }
                    t.shutdown();

                    mapThread.put(k, t);

                }
            });

            boolean blnAllOver = true;
            for (Map.Entry<String, ExecutorService> entry : mapThread.entrySet()) {
                if (!entry.getValue().isTerminated()) {
                    blnAllOver = false;
                    break;
                }
            }
            if (blnAllOver) {
                break;
            }
            try {
                Thread.sleep(1000 * 10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        // TODO: 所有渠道处理总件数为0的场合， 就跳出不继续做了。 以外的场合， 说明可能还有别的未完成的数据， 继续自己调用自己一下
        try {
            Thread.sleep(1000 * 10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        doUploadMain(null);

    }

    /**
     * 平台产品上新主处理
     *
     * @param channelId String 渠道ID
     * @param cartId    String 平台ID
     */
    public void doProductUpload(String channelId, int cartId, int threadPoolCnt) throws Exception {

        // 获取店铺信息
        ShopBean shopProp = Shops.getShop(channelId, cartId);
        if (shopProp == null) {
//            $error("获取到店铺信息失败(shopProp == null)! [ChannelId:%s] [CartId:%s]", channelId, cartId);
            return;
        }
//        $info("获取店铺信息成功![ChannelId:%s] [CartId:%s]", channelId, cartId);

        // 从上新的任务表中获取该平台及渠道需要上新的任务列表(group by channel_id, cart_id, group_id)
        List<CmsBtSxWorkloadModel> sxWorkloadModels = platformProductUploadService.getSxWorkloadWithChannelIdCartId(
                CmsConstants.PUBLISH_PRODUCT_RECORD_COUNT_ONCE_HANDLE, channelId, cartId);
        if (ListUtils.isNull(sxWorkloadModels)) {
//            $error("上新任务表中没有该渠道和平台对应的任务列表信息！[ChannelId:%s] [CartId:%s]", channelId, cartId);
            return;
        }

        // 保存渠道级别(channel)的共通配置项目(从cms_mt_channel_config表中取得的)
        Map<String, String> channelConfigValueMap = new ConcurrentHashMap<>();
        // 取得cms_mt_channel_config表中配置的渠道级别的配置项目值(如：颜色别名等)
        doChannelConfigInit(channelId, cartId, channelConfigValueMap);

        // 从cms_mt_channel_condition_mapping_config表中取得当前渠道的取得产品主类目与天猫平台叶子类目(或者平台一级类目)，以及feed类目id和天猫平台类目之间的mapping关系数据
        Map<String, List<Map<String, String>>> categoryMappingListMap = getCategoryMapping(channelId, cartId);

        // 创建线程池
        ExecutorService executor = Executors.newFixedThreadPool(threadPoolCnt);
        // 根据上新任务列表中的groupid循环上新处理
        for (CmsBtSxWorkloadModel cmsBtSxWorkloadModel : sxWorkloadModels) {
            // 启动多线程
            executor.execute(() -> uploadProduct(cmsBtSxWorkloadModel, shopProp, channelConfigValueMap, categoryMappingListMap));
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
     * 取得产品主类目与天猫平台叶子类目(或者平台一级类目)，以及feed类目id和天猫平台类目之间的mapping关系数据
     *
     * @param channelId 渠道id
     * @param cartId    平台id
     * @return Map<String, List<Map<String, String>>> 表中的配置mapping信息
     */
    protected Map<String, List<Map<String, String>>> getCategoryMapping(String channelId, int cartId) {

        Map<String, List<Map<String, String>>> categoryMapping = new HashMap<>();
        // 取得主类目与平台叶子类目之间的mapping关系数据
        // key:主类目String     value:平台叶子类目String
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
     * @param cartId    平台id
     * @param propName  查询mapping分类(tt_main_category_leaf:主类目与平台叶子类目, tt_main_category:主类目与平台一级类目, tt_category:feed类目与平台一级类目)
     * @return List<CmsMtChannelConditionMappingConfigModel> 表中的配置mapping信息
     */
    protected List<CmsMtChannelConditionMappingConfigModel> getChannelConditionMappingInfo(String channelId, Integer cartId, String propName) {

        // 从cms_mt_channel_condition_mapping_config表中取得该渠道，平台对应的客户过来的类目id和天猫平台一级类目之间的mapping关系数据
        Map<String, String> conditionMappingParamMap = new HashMap<>();
        if (!StringUtils.isEmpty(channelId)) conditionMappingParamMap.put("channelId", channelId);
        if (cartId != null) conditionMappingParamMap.put("cartId", StringUtils.toString(cartId));
        if (!StringUtils.isEmpty(propName)) conditionMappingParamMap.put("propName", propName);   // 查询mapping分类
        return cmsMtChannelConditionMappingConfigDao.selectList(conditionMappingParamMap);
    }

    /**
     * 平台产品上新处理
     *
     * @param cmsBtSxWorkloadModel CmsBtSxWorkloadModel WorkLoad信息
     * @param shopProp             ShopBean 店铺信息
     */
    public void uploadProduct(CmsBtSxWorkloadModel cmsBtSxWorkloadModel, ShopBean shopProp, Map<String, String> channelConfigValueMap, Map<String, List<Map<String, String>>> categoryMappingListMap) {

        // 当前groupid(用于取得产品信息)
        long groupId = cmsBtSxWorkloadModel.getGroupId();
        // 渠道id
        String channelId = shopProp.getOrder_channel_id();
        // 平台id
        int cartId = Integer.parseInt(shopProp.getCart_id());
        // 上新数据
        SxData sxData = null;
//        // 是否是智能上新(根据配置)
//		boolean blnIsSmartSx = sxProductService.isSmartSx(shopProp.getOrder_channel_id(), Integer.parseInt(shopProp.getCart_id()));
//        String sxType = "普通上新";
//        if (blnIsSmartSx) {
//            sxType = "智能上新";
//        }
        boolean blnIsSmartSx = false;

        try {
            // 上新用的商品数据信息取得 // TODO：这段翻译写得不好看， 以后再改 Tom
            sxData = sxProductService.getSxProductDataByGroupId(channelId, groupId);
            if (!StringUtils.isEmpty(sxData.getPlatform().getPlatformPid())) throw new BusinessException("已经上新过,考拉不能更新商品!");
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
            // 上新对象code
            List<String> listSxCode = null;
            if (ListUtils.notNull(sxData.getProductList())) {
                listSxCode = sxData.getProductList().stream().map(p -> p.getCommonNotNull().getFieldsNotNull().getCode()).collect(Collectors.toList());
            }

            // 如果skuList不为空，取得所有sku的库存信息
            // 为了对应MiniMall的场合， 获取库存的时候要求用getOrgChannelId()（其他的场合仍然是用channelId即可）
            // WMS2.0切换 20170526 charis STA
            Map<String, Integer> skuLogicQtyMap = new HashMap<>();
            for (String code : listSxCode) {
                try {
                    Map<String, Integer> map = sxProductService.getAvailQuantity(channelId, String.valueOf(cartId), code, null);
                    for (Map.Entry<String, Integer> e : map.entrySet()) {
                        skuLogicQtyMap.put(e.getKey(), e.getValue());
                    }
                } catch (Exception e) {
                    String errorMsg = String.format("获取可售库存时发生异常 [channelId:%s] [cartId:%s] [code:%s] [errorMsg:%s]",
                            channelId, cartId, code, e.getMessage());
                    throw new Exception(errorMsg);
                }
            }
            // WMS2.0切换 20170526 charis END
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
            for (String skuCode : skuLogicQtyMap.keySet()) {
                totalSkusLogicQty += skuLogicQtyMap.get(skuCode);
            }

//            StringBuffer sbFailCause = new StringBuffer("");
            // delete by desmond 2016/12/26 start 暂时先注释掉，以后有可能还是要删除库存为0的SKU
//            // 考拉上新的时候，以前库存为0的SKU也上，现在改为不上库存为0的SKU不要上新
//            // 无库存的skuId列表（包含之前卖，现在改为不在考拉上售卖的SKU）,只在更新的时候用
//            List<String> skuIdListNoStock = new ArrayList<>();
//            Iterator<BaseMongoMap<String, Object>> skuIter = skuList.iterator();
//            while (skuIter.hasNext()) {
//                BaseMongoMap<String, Object> sku = skuIter.next();
//                // 如果该skuCode对应的库存为0(或库存信息不存在)，则把该sku加到skuIdListNoStock列表中，后面会删除考拉平台上该SKU
//                if (isSkuNoStock(sku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.skuCode.name()), skuLogicQtyMap)) {
//                    // 删除SxData.skuList里面库存为0的SKU，这样后面上新的时候，从这个列表中取得的都是有库存的SKU
//                    skuIter.remove();
//
//                    // 把库存为0的SKU的jdSkuId加到skuIdListNoStock列表中，后面会一起再考拉平台上删除这些库存为0的SKU
//                    if (!StringUtils.isEmpty(sku.getStringAttribute("jdSkuId"))) {
//                        skuIdListNoStock.add(sku.getStringAttribute("jdSkuId"));
//                    } else if (!StringUtils.isEmpty(sku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.skuCode.name()))){
//                        sbFailCause.setLength(0);
//                        // 如果本地数据库中没有jdSkuId,根据skuCode到考拉平台上重新取得考拉skuId
//                        Sku currentSku = jdSkuService.getSkuByOuterId(shopProp,
//                                sku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.skuCode.name()), sbFailCause);
//                        if (currentSku != null) {
//                            skuIdListNoStock.add(StringUtils.toString(currentSku.getSkuId()));
//                        }
//                    }
//                }
//            }
            // delete by desmond 2016/12/26 end

            // 取得主产品考拉平台设置信息(包含SKU等信息)
            CmsBtProductModel_Platform_Cart mainProductPlatformCart = mainProduct.getPlatform(sxData.getCartId());
            if (mainProductPlatformCart == null) {
                $error(String.format("获取主产品考拉平台设置信息(包含SKU，Schema属性值等信息)失败！[ProductCode:%s][CartId:%s]",
                        mainProduct.getCommon().getFields().getCode(), sxData.getCartId()));
                throw new BusinessException("获取主产品考拉平台设置信息(包含SKU，Schema属性值等信息)失败");
            }

            // 取得考拉共通Schema(用于设定考拉商品标题，长宽高重量等共通属性)
            String commCatId = "1";          // 类目schema表中考拉共通属性的catId为"1"
            // 取得平台类目schema信息
            CmsMtPlatformCategorySchemaModel klCommonSchema = platformCategoryService.getPlatformCatSchema(commCatId, cartId);
            if (klCommonSchema == null) {
                $error(String.format("获取考拉共通schema信息失败 [类目Id:%s] [CartId:%s]", commCatId, cartId));
                throw new BusinessException("获取考拉共通schema信息失败");
            }

            // 取得主产品的考拉平台类目(用于取得考拉平台该类目下的Schema信息)
            String platformCategoryId = mainProductPlatformCart.getpCatId();
            // 新增商品的场合， 如果类目没填， 并且是智能上新的话， 想想办法能不能设置一下
            if (StringUtils.isEmpty(platformCategoryId)) {
                // 暂不支持只能上新，先抛个错
                throw new BusinessException("未设置类目!");
//                if (blnIsSmartSx) {
//                    // 先判断一下必要的条件
//                    // 主产品主类目path
//                    String mainCatPath = mainProduct.getCommonNotNull().getCatPathEn();
//                    if (!StringUtils.isEmpty(mainCatPath) && MapUtils.isNotEmpty(categoryMappingListMap)) {
//                        String brand = mainProduct.getCommonNotNull().getFieldsNotNull().getBrand();
//                        String sizeType = mainProduct.getCommonNotNull().getFieldsNotNull().getSizeType();
//
//                        // 匹配优先顺序：
//                        // 1.主类目+品牌+适用人群
//                        // 2.主类目+品牌
//                        // 3.主类目+适用人群
//                        // 4.主类目
//                        String valCategory = getMainCategoryMappingInfo(mainCatPath, brand, sizeType, CmsBuildPlatformProductUploadTmTongGouService.TtPropName.tt_main_category, categoryMappingListMap);
//                        if (StringUtils.isEmpty(valCategory)) {
//                            valCategory = getMainCategoryMappingInfo(mainCatPath, brand, null, CmsBuildPlatformProductUploadTmTongGouService.TtPropName.tt_main_category, categoryMappingListMap);
//                        }
//                        if (StringUtils.isEmpty(valCategory)) {
//                            valCategory = getMainCategoryMappingInfo(mainCatPath, null, sizeType, CmsBuildPlatformProductUploadTmTongGouService.TtPropName.tt_main_category, categoryMappingListMap);
//                        }
//                        if (StringUtils.isEmpty(valCategory)) {
//                            valCategory = getMainCategoryMappingInfo(mainCatPath, null, null, CmsBuildPlatformProductUploadTmTongGouService.TtPropName.tt_main_category, categoryMappingListMap);
//                        }
//
//                        if (!StringUtils.isEmpty(valCategory)) {
//                            platformCategoryId = valCategory;
//                        }
//
//                    }
//
//                    if (StringUtils.isEmpty(platformCategoryId)) {
//                        $error("未设置平台类目， 并且也没有自动匹配上类目");
//                        throw new BusinessException("未设置平台类目， 并且也没有自动匹配上类目");
//                    }
//
//                }
            }
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

            // 编辑考拉共通属性
            ItemAddPartRequest klProductBean = setKlProductCommonInfo(sxData, platformCategoryId, shopProp, klCommonSchema, cmsMtPlatformCategorySchema, blnIsSmartSx);

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

            // 保存sku信息 返回销售属性别名
            doSaveSkus(shopProp, klProductBean, sxData, skuLogicQtyMap, cmsColorList, cmsSizeList, channelConfigValueMap);

            // 新增商品
            // 如果所有产品所有SKU的库存之和为0时，直接报错
//            if (totalSkusLogicQty == 0) {
//                String errMsg = String.format("考拉新增商品时所有SKU的总库存为0，不能上新！请添加库存信息之后再做上新. " +
//                        "[ChannelId:%s] [CartId:%s] [GroupId:%s]", channelId, cartId, groupId);
//                $error(errMsg);
//                throw new BusinessException(errMsg);
//            }

            KoalaConfig koalaConfig = Shops.getShopKoala(channelId, String.valueOf(cartId));
            ItemAddPartResponse response;
            try {
                $info("考拉上新schema[groupId:%s]:%s", groupId, klProductBean.toString());
                response = koalaItemService.addPart(koalaConfig, klProductBean);
            } catch (KoalaApiException e) {
                if (e.hasError()) {
                    if (e.getError().getSubErrors().size() > 0) {
                        StringBuffer sb = new StringBuffer("");
                        for (KoalaApiError.ErrorResponseBean.SubErrorsBean errorsBean : e.getError().getSubErrors()) {
                            sb.append(errorsBean.toString()).append(Separtor_Semicolon);
                        }
                        throw new BusinessException(sb.toString());
                    } else {
                        throw e;
                    }
                } else {
                    throw e;
                }
            }

            String platformPid = response.getKey();
            SkuOuterIdResult[] skuKeys = response.getSkuKeys();

            // 上新成功时状态回写操作
            sxProductService.doUploadFinalProc(shopProp, true, sxData, cmsBtSxWorkloadModel, "", CmsConstants.PlatformStatus.InStock, platformPid, getTaskName());
            // 回写product表的skuKey
            saveProductPlatformSku(sxData, skuKeys);
            // 回写cms_bt_kl_sku
            saveCmsBtKlSku(sxData, "", skuKeys);

            if (ChannelConfigEnums.Channel.SN.getId().equals(channelId)) {
                // Sneakerhead
                try {
                    sxProductService.uploadCnInfo(sxData);
                } catch (IOException io) {
                    throw new BusinessException("上新成功!但在推送给美国数据库时发生异常!" + io.getMessage());
                }
            }
        } catch (Exception ex) {
            // 异常结束时
            String errMsg = String.format("考拉单个商品新增信息异常结束！[ChannelId:%s] [CartId:%s] [GroupId:%s]", channelId, cartId, groupId);
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
                    sxData.setErrorMessage(ex.getMessage());
                }
            }
            // 如果上新数据中的errorMessage为空
            if (StringUtils.isEmpty(sxData.getErrorMessage())) {
                // nullpoint错误的处理
                if (StringUtils.isNullOrBlank2(ex.getMessage())) {
                    sxData.setErrorMessage("出现不可预知的错误，请跟管理员联系! " + ex.getStackTrace()[0].toString());
                    ex.printStackTrace();
                } else {
                    sxData.setErrorMessage(ex.getMessage());
                }
            }

            // 上新出错时状态回写操作
            sxProductService.doUploadFinalProc(shopProp, false, sxData, cmsBtSxWorkloadModel, "", null, "", getTaskName());

            return;
        }

        // 正常结束
        $info(String.format("考拉单个商品新增信息成功！[ChannelId:%s] [CartId:%s] [GroupId:%s]", channelId, cartId, groupId));

    }

    /**
     * 取得主类目到天猫一级类目的匹配结果
     *
     * @param mainCatPath            主类目
     * @param brand                  品牌
     * @param sizeType               适用人群
     * @param ttPropName             匹配方式
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
     * 设置考拉上新产品用共通属性
     * 不包含"sku属性","sku价格","sku库存","自定义属性值别名","SKU外部ID"的值
     *
     * @param sxData             sxData 上新产品对象
     * @param platformCategoryId String     平台类目id
     * @param shopProp           ShopBean             店铺信息
     * @param klCommonSchema     CmsMtPlatformCategorySchemaModel  考拉共通schema数据
     * @param platformSchemaData CmsMtPlatformCategorySchemaModel  主产品类目对应的平台schema数据
     * @return ItemAddPartRequest 考拉上新用bean
     * @throws BusinessException
     */
    private ItemAddPartRequest setKlProductCommonInfo(SxData sxData, String platformCategoryId,
                                                      ShopBean shopProp,
                                                      CmsMtPlatformCategorySchemaModel klCommonSchema,
                                                      CmsMtPlatformCategorySchemaModel platformSchemaData,
                                                      boolean blnIsSmartSx) throws BusinessException {
        CmsBtProductModel mainProduct = sxData.getMainProduct();
        List<BaseMongoMap<String, Object>> skuList = sxData.getSkuList();
        ExpressionParser expressionParser = new ExpressionParser(sxProductService, sxData);

        // 考拉上新产品共通属性设定
        ItemAddPartRequest klAddBean = new ItemAddPartRequest();
        // 如果去掉库存为0的sku之后一个sku都没有时，后面新增时报错/更新时下架，所有共通属性也不用设置了，直接返回空的klAddBean
        if (ListUtils.isNull(skuList)) return klAddBean;

        // 渠道id
        String channelId = sxData.getChannelId();
        // 平台id
        String cartId = sxData.getCartId().toString();

        // 取得考拉共通属性值(包括标题，长宽高，重量等)
        Map<String, String> klCommonInfoMap = getKlCommonInfo(klCommonSchema, shopProp, expressionParser, blnIsSmartSx);

        // 类目id(必须)
        klAddBean.setCategoryId(Long.parseLong(platformCategoryId));
        // 商品标题(必须)
        String title = klCommonInfoMap.get("name");
        if (StringUtils.isEmpty(title)) {
            title = mainProduct.getCommonNotNull().getFieldsNotNull().getLongDesCn();
            if (StringUtils.isEmpty(title)) {
                title = mainProduct.getCommonNotNull().getFieldsNotNull().getLongDesEn();
            }
        }
        klAddBean.setName(title); // 商品名称
        klAddBean.setSubTitle(klCommonInfoMap.get("subTitle")); // 副标题
        klAddBean.setShortTitle(klCommonInfoMap.get("shortTitle")); // 短标题
        klAddBean.setTenWordsDesc(klCommonInfoMap.get("tenWordsDesc")); // 十字描述
        // 商品货号
        String itemNO = null;
        if (mainProduct.getPlatform(sxData.getCartId()) != null) {
            if (mainProduct.getPlatform(sxData.getCartId()).getFields() != null) {
                itemNO = mainProduct.getPlatform(sxData.getCartId()).getFields().getStringAttribute("item_NO");
            }
        }
        if (StringUtils.isEmpty(itemNO)) {
            // 默认使用model来设置
            // TODO:暂时一个group一个code,改成code
//            klAddBean.setItemNO(mainProduct.getCommonNotNull().getFieldsNotNull().getModel());
            klAddBean.setItemNO(mainProduct.getCommonNotNull().getFieldsNotNull().getCode());
        } else {
            // 如果有填了的话, 那就用运营自己填写的来设置
            klAddBean.setItemNO(itemNO);
        }
        // 商品外键id
        String itemOuterId = null;
        if (mainProduct.getPlatform(sxData.getCartId()) != null) {
            if (mainProduct.getPlatform(sxData.getCartId()).getFields() != null) {
                itemOuterId = mainProduct.getPlatform(sxData.getCartId()).getFields().getStringAttribute("ItemOuterId");
            }
        }
        if (StringUtils.isEmpty(itemOuterId)) {
            // 默认使用model来设置
            klAddBean.setItemOuterId(mainProduct.getCommonNotNull().getFieldsNotNull().getModel());
        } else {
            // 如果有填了的话, 那就用运营自己填写的来设置
            klAddBean.setItemOuterId(itemOuterId);
        }
        klAddBean.setBrandId(Long.valueOf(sxData.getBrandCode())); // 品牌id
        klAddBean.setOriginalCountryCodeId(klCommonInfoMap.get("originalCountryCodeId")); // 原产国id

        // HsCode，暂时不需要
//        String propValue = sxData.getMainProduct().getCommon().getFields().getHsCodePrivate(); // "0410004300, 戒指 ,对" 或者  "0410004300, 戒指 ,只"
//        // added by morse.lu 2017/01/03 start
//        // 通过配置表(cms_mt_channel_config)来决定用hsCodeCross，还是hsCodePrivate，默认用hsCodePrivate
//        CmsChannelConfigBean hscodeConfig = CmsChannelConfigs.getConfigBean(sxData.getChannelId(),
//                CmsConstants.ChannelConfig.HSCODE,
//                String.valueOf(sxData.getCartId()) + CmsConstants.ChannelConfig.SX_HSCODE);
//        if (hscodeConfig != null) {
//            String hscodePropName = hscodeConfig.getConfigValue1(); // 目前配置的是hsCodeCross或者hsCodePrivate
//            if (!StringUtils.isEmpty(hscodePropName)) {
//                String val = sxData.getMainProduct().getCommon().getFields().getStringAttribute(hscodePropName);
//                if (!StringUtils.isEmpty(val)) {
//                    propValue = val;
//                }
//            }
//        }
//        klAddBean.setHsCode(propValue.split(",")[0]);

        // 行邮税号tax_code，暂时不需要
//        klAddBean.setTaxCode("");
        // 度量单位unit_code，暂时不需要
//        klAddBean.setUnitCode("");

        // TODO : 运费

        // 取得一下product默认属性
        Map<String, Field> productSchemaFields = SchemaReader.readXmlForMap(klCommonSchema.getPropsProduct());

        // 重量(单位:kg)(必须)
        String weight = klCommonInfoMap.get("grossWeight");
        if (StringUtils.isEmpty(weight)) {
            weight = String.valueOf(mainProduct.getCommonNotNull().getFieldsNotNull().getWeightKG());
            if (StringUtils.isEmpty(weight)) {
                InputField f = (InputField) productSchemaFields.get("grossWeight");
                weight = f.getDefaultValue();
            }
        }
        if (StringUtils.isEmpty(weight) || BigDecimal.ZERO.compareTo(new BigDecimal(weight)) == 0) weight = "1";
        klAddBean.setGrossWeight(weight);

        // 详情描述
        String strNotes = "";
        try {
            // 取得描述
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
                strNotes = sxProductService.resolveDict("考拉详情页描述", expressionParser, shopProp, getTaskName(), null);
            }
        } catch (Exception ex) {
            String errMsg = String.format("考拉取得详情页描述信息失败！[errMsg:%s]", ex.getMessage());
            $error(errMsg);
            throw new BusinessException(errMsg);
        }
        klAddBean.setDescription(strNotes);

        // 图片信息（图片尺寸为800*800，单张大小不超过 2048K）(必须)
        // 商品5张图片名称列表
        // TODO:先这边写着，回头做到schema里，custom表里追加IMAGE属性，这样可以用共通代码解析处理
        Set<String> listPicNameUrl = new HashSet<>();
        // 获取url
        for (int i = 1; i <= 5; i++) {
            try {
                // 取得图片url
                String picUrl = sxProductService.resolveDict("商品图片-" + i, expressionParser, shopProp, getTaskName(), null);
                if (!StringUtils.isEmpty(picUrl)) listPicNameUrl.add(picUrl);

            } catch (Exception ex) {
                String errMsg = String.format("考拉取得商品主图信息失败![ChannelId:%s] [CartId:%s] [GroupId:%s] [PlatformCategoryId:%s] [PicName:%s]",
                        channelId, cartId, sxData.getGroupId(), platformCategoryId, "商品图片-" + i);
                $error(errMsg, ex);
            }
        }
        if (listPicNameUrl.isEmpty()) {
            throw new BusinessException("考拉取得商品主图信息失败!");
        }
        // 上传
        Map<String, String> mapUrls;
        try {
            mapUrls = sxProductService.uploadImage(sxData.getChannelId(), sxData.getCartId(), Long.toString(sxData.getGroupId()), shopProp, listPicNameUrl, getTaskName());
        } catch (Exception ex) {
            String errMsg = String.format("考拉上传图片失败![ChannelId:%s] [CartId:%s] [GroupId:%s] [PlatformCategoryId:%s]",
                    channelId, cartId, sxData.getGroupId(), platformCategoryId);
            $error(errMsg, ex);
            throw new BusinessException("考拉上传图片失败!" + ex.getMessage());
        }
        String strUrls = mapUrls.values().stream().map(url -> url + Separtor_Caret + "1").collect(Collectors.joining(Separtor_Vertical)) // 商品图片
                + Separtor_Vertical
                + mapUrls.values().stream().map(url -> url + Separtor_Caret + "2").collect(Collectors.joining(Separtor_Vertical)); // App图片
        klAddBean.setImageUrls(strUrls);

        // 调用共通函数取得商品属性列表，用户自行输入的类目属性ID和用户自行输入的属性值Map
        Map<String, String> klProductAttrMap = getKlProductAttributes(platformSchemaData, shopProp, expressionParser, blnIsSmartSx);
        klAddBean.setPropertyValueIdList(klProductAttrMap.get(Attributes));
        klAddBean.setTextPropertyNameId(klProductAttrMap.get(Input_Strs));

        return klAddBean;
    }

    /**
     * 取得考拉商品共通属性值
     *
     * @param klCommonSchema   CmsMtPlatformCategorySchemaModel  考拉共通schema数据
     * @param shopBean         ShopBean  店铺信息
     * @param expressionParser ExpressionParser  解析子
     * @param blnIsSmartSx     是否强制使用智能上新
     * @return Map<String, String> 考拉商品共通属性
     */
    private Map<String, String> getKlCommonInfo(CmsMtPlatformCategorySchemaModel klCommonSchema,
                                                ShopBean shopBean, ExpressionParser expressionParser,
                                                boolean blnIsSmartSx) {
        Map<String, String> retAttrMap = new HashMap<>();

        // 取得考拉共通schema数据中的propsItem(XML字符串)
        String propsItem = klCommonSchema.getPropsProduct();
        List<Field> itemFieldList = null;
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
            String errMsg = String.format("取得考拉共通Schema所有Field对应的属性值失败！[ChannelId:%s] [CartId:%s] [PlatformCategoryId:%s]",
                    shopBean.getOrder_channel_id(), shopBean.getCart_id(), klCommonSchema.getCatId());
            $error(errMsg, ex);
            throw new BusinessException("取得考拉平台类目共通属性值失败！" + ex.getMessage());
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
//                    $info("没找到该fieldId对应的属性值！ [FieldId:%s]", fieldId);
                    continue;
                }

                // 根据输入类型分别设置3个属性值
                switch (fieldValue.getType()) {
                    case SINGLECHECK: {
                        // 输入类型input_type为1(单选)的时候
                        retAttrMap.put(fieldId, ((SingleCheckField) fieldValue).getValue().getValue());
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
                        $error("复杂类型[" + field.getType() + "]不能作为考拉共通属性值来使用！");
                }
            }
        }

        return retAttrMap;
    }

    /**
     * 取得考拉商品属性值
     *
     * @param platformSchemaData CmsMtPlatformCategorySchemaModel  主产品类目对应的平台schema数据
     * @param shopBean           ShopBean  店铺信息
     * @param expressionParser   ExpressionParser  解析子
     * @param blnIsSmartSx       是否强制使用智能上新
     * @return Map<String, String> 考拉类目属性
     */
    private Map<String, String> getKlProductAttributes(CmsMtPlatformCategorySchemaModel platformSchemaData,
                                                       ShopBean shopBean, ExpressionParser expressionParser,
                                                       boolean blnIsSmartSx) {
        Map<String, String> retAttrMap = new HashMap<>();

        // 取得schema数据中的propsItem(XML字符串)
        String propsItem = platformSchemaData.getPropsItem();
        List<Field> itemFieldList = null;
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
            String errMsg = String.format("取得考拉平台Schema所有Field对应的属性值失败！[ChannelId:%s] [CartId:%s] [PlatformCategoryId:%s]",
                    shopBean.getOrder_channel_id(), shopBean.getCart_id(), platformSchemaData.getCatId());
            $error(errMsg, ex);
            throw new BusinessException("取得考拉平台商品类目Schema属性值失败！" + ex.getMessage());
        }

        // 商品属性列表,多组之间用|分隔，格式:vid1|vid2
        StringBuffer sbAttributes = new StringBuffer();
        // 用户自行输入的属性值,结构:‘aid1^输入值1|aid2^输入值2’
        StringBuffer sbInputStrs = new StringBuffer();

        // 如果list为空说明没有mappingg过，不用设置
        if (attrMap != null && attrMap.size() > 0 && itemFieldList != null) {
            // 遍历fieldList
            for (Field field : itemFieldList) {
                // 属性值id(field_id)
                String fieldId = field.getId();

                // 从属性值map里面取得当前fieldId对应的Field
                Field fieldValue = attrMap.get(fieldId);
                if (fieldValue == null) {
//                    $info("没找到该fieldId对应的属性值！ [FieldId:%s]", fieldId);
                    continue;
                }

                // 根据输入类型分别设置3个属性值
                switch (fieldValue.getType()) {
                    case SINGLECHECK: {
                        // 输入类型input_type为1(单选)的时候,设置【商品属性列表】
                        // 属性值设置(如果有多个，则用|分隔 "属性值1|属性值2|属性值3")
                        sbAttributes.append(((SingleCheckField) fieldValue).getValue().getValue()); // 属性值id
                        sbAttributes.append(Separtor_Vertical);                // "|"
                        break;
                    }
                    case MULTICHECK: {
                        // 多选的时候，属性值多个，则用|分隔 "属性值1|属性值2|属性值3")
                        List<Value> valueList = ((MultiCheckField) fieldValue).getValues();
                        for (Value value : valueList) {
                            // 输入类型input_type为2(多选)的时候,设置【商品属性列表】
                            sbAttributes.append(value.getValue());      // 第N个属性值
                            sbAttributes.append(Separtor_Vertical);   // "|"
                        }
                        break;
                    }
                    case INPUT: {
                        // 输入类型input_type为3(可输入)的时候
                        // 设置用户自行输入的属性值,结构:‘aid1^输入值1|aid2^输入值2’
                        sbInputStrs.append(fieldId);                          // 属性id(fieldId)
                        sbInputStrs.append(Separtor_Caret);                   // "^"
                        sbInputStrs.append(((InputField) fieldValue).getValue()); // 属性值id
                        sbInputStrs.append(Separtor_Vertical);                // "|"
                        break;
                    }
                    default:
                        $error("复杂类型[" + field.getType() + "]不能作为属性值来使用！");
                }
            }
        }

        // 移除商品预定义属性值最后的"|"
        if (sbAttributes.length() > 0) {
            sbAttributes.deleteCharAt(sbAttributes.length() - 1);
        }

        // 移除用户自行输入的属性值最后的"|"
        if (sbInputStrs.length() > 0) {
            sbInputStrs.deleteCharAt(sbInputStrs.length() - 1);
        }

        // 把取得的字符串值放进返回map中
        retAttrMap.put(Attributes, sbAttributes.toString());
        retAttrMap.put(Input_Strs, sbInputStrs.toString());

        return retAttrMap;
    }

    /**
     * 取得指定SKU的价格
     *
     * @param cmsBtProductModelSku BaseMongoMap<String, Object> SKU对象
     * @param channelId            String 渠道id
     * @param cartId               String 平台id
     * @param priceCode            String 价格类型 ("sale_price"(考拉价))
     * @return double SKU价格
     */
    private double getSkuPrice(BaseMongoMap<String, Object> cmsBtProductModelSku, String channelId, String cartId, String priceKey, String priceCode) {
        // 价格有可能是用priceSale, 也有可能用priceMsrp, 所以需要判断一下
        // SKU价格类型应该用"sale_price"(考拉价)
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
     * 取得当前平台主类目对应的销售属性状况
     * 1:颜色和尺寸属性都有 2:只有颜色没有尺寸属性 3:没有颜色只有尺寸属性 4:没有颜色没有尺寸属性
     *
     * @param cmsColorList List<CmsMtPlatformSkusModel> 颜色对象列表
     * @param cmsSizeList  List<CmsMtPlatformSkusModel> 颜色对象列表
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
     * 取得cms_mt_channel_config配置表中配置的值集合
     *
     * @param channelId             String 渠道id
     * @param cartId                int 平台id
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
     * @param channelId  String 渠道id
     * @param configKey  CmsConstants.ChannelConfig ConfigKey
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
     * 全量保存考拉商品的SKU属性
     *
     * @param shopProp              ShopBean 店铺信息
     * @param klProductBean         ItemAddPartRequest   产品对象
     * @param sxData                SxData 产品对象
     * @param skuLogicQtyMap        Map<String, Integer> 所有SKU的逻辑库存列表
     * @param cmsColorList          List<CmsMtPlatformSkusModel> 该类目对应的颜色SKU列表
     * @param cmsSizeList           List<CmsMtPlatformSkusModel> 该类目对应的尺寸SKU列表
     * @param channelConfigValueMap cms_mt_channel_config配置表中配置的值集合
     */
    protected void doSaveSkus(ShopBean shopProp, ItemAddPartRequest klProductBean, SxData sxData,
                              Map<String, Integer> skuLogicQtyMap, List<CmsMtPlatformSkusModel> cmsColorList,
                              List<CmsMtPlatformSkusModel> cmsSizeList,
                              Map<String, String> channelConfigValueMap) {

        List<CmsBtProductModel> productList = sxData.getProductList();
        List<BaseMongoMap<String, Object>> skuList = sxData.getSkuList();
        ExpressionParser expressionParser = new ExpressionParser(sxProductService, sxData);

        // 当前平台主类目对应的销售属性状况(1:颜色和尺寸属性都有 2:只有颜色没有尺寸属性 3:没有颜色只有尺寸属性 4:没有颜色没有尺寸属性)
        String salePropStatus = getSalePropStatus(cmsColorList, cmsSizeList, sxData.getMainProduct().getOrgChannelId());

        // 产品和颜色的Mapping表(因为后面上传SKU图片的时候也要用到，所以从外面传进来)
        // SKU尺寸和尺寸值的Mapping表(Map<上新用尺码, 平台取下来的尺码值value>)
        Map<String, CmsMtPlatformSkusModel> skuSizeMap = new HashMap<>();

        // Sku市场价，多个sku的市场价用|分隔,支持2位小数，单位:元，格式:100|200.12|300.22
        StringBuffer sbMarketPrices = new StringBuffer();
        // Sku销售价，多个sku的销售价用|分隔,支持2位小数，单位:元，格式:100|200.12|300.22
        StringBuffer sbSalePrices = new StringBuffer();
        // Sku条形码，多个sku的条形码用|分隔，格式:13123|234234|324234
        StringBuffer sbBarcode = new StringBuffer();
        // Sku库存，整数，多个Sku的库存用|分隔，格式:100|200|300
        StringBuffer sbStock = new StringBuffer();
        // 录入格式：(属性项id:属性值id:属性项中文:图片url|属性项id:-1:自定义属性值:图片url) 同一个sku不同的属性之间用;分隔，不同的sku属性之间用|分隔，只有颜色属性会有图片url
        // 格式:12949:1237795:110cm;12948:1237794:红色:http://pop.nosdn.127.net/b2f6b452-ca53-4821-b0cf-2b0fe26f969a|12949:-1:100cm;12948:1237793:白色:http://pop.nosdn.127.net/b2f6b452-ca53-4821-b0cf-2b0fe26f969a
        StringBuffer sbPropertyValue = new StringBuffer();
        // Sku外键id，不同的sku属性之间用|分隔，格式:13123|234234|324234
        // 我们设置skuCode
        StringBuffer sbOuterId = new StringBuffer();

        // TODO:没有颜色没有尺码怎么上新，还不知道，碰到了再说

        // 根据product列表循环设置该商品的SKU属性
        for (CmsBtProductModel objProduct : productList) {
            // 取得颜色别名的值
            String colorAlias = getColorAlias(objProduct, StringUtils.toString(sxData.getCartId()), channelConfigValueMap);

            // 20170620 优先匹配考拉给的颜色列表 charis STA
            String attrColorValueId = cmsColorList.get(0).getAttrValue().split(Separtor_Colon)[0];
            CmsMtPlatformSkusModel colorModel = cmsColorList.stream()
                    .filter(c -> c.getAttrName().equals(colorAlias))
                    .findFirst()
                    .orElse(null);
            // 20170620 优先匹配考拉给的颜色列表 charis END

            String productCode = objProduct.getCommon().getFields().getCode();
//            CmsMtPlatformSkusModel colorModel = cmsColorList.get(0);
//            cmsColorList.remove(0);

            String picUrl;
            // 属性图片
            try {
                String srcPicUrl = sxProductService.resolveDict("属性图片模板", expressionParser, shopProp, getTaskName(), null);
                srcPicUrl = String.format(srcPicUrl, sxProductService.getProductImages(objProduct, CmsBtProductConstants.FieldImageType.PRODUCT_IMAGE, sxData.getCartId()).get(0).getName());
                Set<String> setUrl = new HashSet<>();
                setUrl.add(srcPicUrl);
                picUrl = sxProductService.uploadImage(sxData.getChannelId(), sxData.getCartId(), Long.toString(sxData.getGroupId()), shopProp, setUrl, getTaskName()).get(srcPicUrl);
            } catch (Exception e) {
                $error(e);
                throw new BusinessException("考拉取得属性图片模板失败!" + e.getMessage());
            }

            CmsBtProductModel_Platform_Cart platformCart = objProduct.getPlatform(sxData.getCartId());
            if (platformCart == null || ListUtils.isNull(platformCart.getSkus())) continue;
            List<BaseMongoMap<String, Object>> platformSkuList = platformCart.getSkus();
            for (BaseMongoMap<String, Object> pSku : platformSkuList) {
                String pSkuCode = pSku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.skuCode.name());
                // 在skuList中找到对应sku信息，然后设置需要的属性
                for (BaseMongoMap<String, Object> objSku : skuList) {
                    // 如果没有找到对应skuCode，则继续循环
                    if (!pSkuCode.equals(objSku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.skuCode.name()))) {
                        continue;
                    }

                    // 上新用尺码别名
                    String sizeSx = objSku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.sizeSx.name());
//                    CmsMtPlatformSkusModel sizeModel = skuSizeMap.get(sizeSx);

                    // 20170620 优先匹配考拉给的尺码列表 charis STA
                    String attrSizeValueId = cmsSizeList.get(0).getAttrValue().split(Separtor_Colon)[0];
                    CmsMtPlatformSkusModel sizeModel = cmsSizeList.stream()
                            .filter(c -> c.getAttrName().equals(sizeSx))
                            .findFirst()
                            .orElse(null);
                    // 20170620 优先匹配考拉给的尺码列表 charis END

//                    if (sizeModel == null) {
//                        sizeModel = cmsSizeList.get(0);
//                        cmsSizeList.remove(0);
//                        skuSizeMap.put(sizeSx, sizeModel);
//                    }
//                    String sizeVal = sizeModel.getAttrValue();

                    // 设置SKU颜色和尺码销售属性
                    switch (salePropStatus) {
                        case SaleProp_Both_Color_Size_1:
                            // 颜色和尺码属性都有时
                            if (colorModel == null) {
                                sbPropertyValue.append(attrColorValueId).append(Separtor_Colon).append("-1").append(Separtor_Colon).append(productCode).append(Separtor_Colon).append(picUrl); // 自定义颜色
                            } else {
                                sbPropertyValue.append(colorModel.getAttrValue()).append(Separtor_Colon).append(colorAlias).append(Separtor_Colon).append(picUrl); // 颜色
                            }
                            sbPropertyValue.append(Separtor_Semicolon);
                            if (sizeModel == null) {
                                sbPropertyValue.append(attrSizeValueId).append(Separtor_Colon).append("-1").append(Separtor_Colon).append(sizeSx); // 自定义尺码
                            } else {
                                sbPropertyValue.append(sizeModel.getAttrValue()).append(Separtor_Colon).append(sizeSx); // 尺码
                            }
                            sbPropertyValue.append(Separtor_Vertical);
                            break;
                        case SaleProp_Only_Color_2:
                            // 只有颜色属性时
                            if (colorModel == null) {
                                sbPropertyValue.append(attrColorValueId).append(Separtor_Colon).append("-1").append(Separtor_Colon).append(productCode).append(Separtor_Colon).append(picUrl); // 自定义颜色
                            } else {
                                sbPropertyValue.append(colorModel.getAttrValue()).append(Separtor_Colon).append(colorAlias).append(Separtor_Colon).append(picUrl); // 颜色
                            }
                            sbPropertyValue.append(Separtor_Vertical);
                            break;
                        case SaleProp_Only_Size_3:
                            // 只有尺码属性时
                            if (sizeModel == null) {
                                sbPropertyValue.append(attrSizeValueId).append(Separtor_Colon).append("-1").append(Separtor_Colon).append(sizeSx); // 自定义尺码
                            } else {
                                sbPropertyValue.append(sizeModel.getAttrValue()).append(Separtor_Colon).append(sizeSx); // 尺码
                            }
                            sbPropertyValue.append(Separtor_Vertical);
                            break;
                    }

                    // 市场价
                    sbMarketPrices.append(objSku.getDoubleAttribute(CmsBtProductConstants.Platform_SKU_COM.priceMsrp.name())).append(Separtor_Vertical);

                    // 销售价
                    Double skuPrice = getSkuPrice(objSku, shopProp.getOrder_channel_id(), shopProp.getCart_id(), CmsConstants.ChannelConfig.PRICE_SALE_KEY, CmsConstants.ChannelConfig.PRICE_SALE_PRICE_CODE);
                    sbSalePrices.append(Double.toString(skuPrice)).append(Separtor_Vertical);

//                    sbBarcode.append(objSku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.barcode.name())).append(Separtor_Vertical);
                    sbBarcode.append(pSkuCode).append(Separtor_Vertical);
                    // SKU库存
                    Integer qty = skuLogicQtyMap.get(pSkuCode);
                    if (qty == null || qty.intValue() < 0) qty = 0;
                    sbStock.append(qty).append(Separtor_Vertical);
                    // SKU外部ID(skuCode)
                    sbOuterId.append(pSkuCode).append(Separtor_Vertical);
                }
            }
        }

        // 去掉最后一位|
        sbMarketPrices.deleteCharAt(sbMarketPrices.length() - 1);
        sbSalePrices.deleteCharAt(sbSalePrices.length() - 1);
        sbBarcode.deleteCharAt(sbBarcode.length() - 1);
        sbStock.deleteCharAt(sbStock.length() - 1);
        sbPropertyValue.deleteCharAt(sbPropertyValue.length() - 1);
        sbOuterId.deleteCharAt(sbOuterId.length() - 1);

        klProductBean.setSkuMarketPrices(sbMarketPrices.toString());
        klProductBean.setSkuSalePrices(sbSalePrices.toString());
        klProductBean.setSkuBarcode(sbBarcode.toString());
        klProductBean.setSkuStock(sbStock.toString());
        klProductBean.setSkuPropertyValue(sbPropertyValue.toString());
        klProductBean.setSkuOuterId(sbOuterId.toString());

    }

    /**
     * 取得当前产品code对应的颜色别名
     *
     * @param objProduct            产品产品
     * @param cartId                平台id
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

    protected void saveCmsBtKlSku(SxData sxData, String numIId, SkuOuterIdResult[] skuKeys) {
        String channelId = sxData.getChannelId();
        int cartId = sxData.getCartId();
        String skuKey;
        List<BaseMongoMap<String, Object>> skuList = sxData.getSkuList();
        for (CmsBtProductModel productModel : sxData.getProductList()) {
            // 取得产品信息
            if (productModel == null
                    || productModel.getPlatform(cartId) == null
                    || ListUtils.isNull(productModel.getPlatform(cartId).getSkus())) continue;

            // 只处理这次上新的，之前上过这次不上的，不会清除skuKey的记录，因为暂时考拉也不支持更新，等以后有了再看怎么处理
            List<BaseMongoMap<String, Object>> pSkus = productModel.getPlatform(cartId).getSkus();
            for (BaseMongoMap<String, Object> pSku : pSkus) {
                String pSkuCode = pSku.getStringAttribute("skuCode");
                SkuOuterIdResult result = Stream.of(skuKeys).filter(sku -> pSkuCode.equals(sku.getSkuOuterId())).findFirst().orElse(null);
                if (result == null) {
                    skuKey = ""; // 理论上不会
                } else {
                    skuKey = result.getSkuKey();
                }
                BaseMongoMap<String, Object> sku = skuList.stream().filter(s -> pSkuCode.equals(s.getStringAttribute("skuCode"))).findFirst().orElse(null);
                if (MapUtils.isEmpty(sku)) continue;

                CmsBtKlSkuModel cmsBtKlSkuModel = fillCmsBtKlSkuModel(channelId, productModel.getCommonNotNull().getFields().getCode(), sku, skuKey, numIId, productModel.getOrgChannelId());

                // 回写mysql的cms_bt_jm_sku表中(存在时更新，不存在时新增)
                insertOrUpdateOrDeleteCmsBtKlSku(cmsBtKlSkuModel);
            }
        }

    }

    protected CmsBtKlSkuModel fillCmsBtKlSkuModel(String channelId, String productCode, BaseMongoMap<String, Object> klSku,
                                                  String skuKey, String numIId, String orgChannelId) {
        CmsBtKlSkuModel cmsBtKlSkuModel = new CmsBtKlSkuModel();

        cmsBtKlSkuModel.setChannelId(channelId);
        cmsBtKlSkuModel.setOrgChannelId(orgChannelId);
        cmsBtKlSkuModel.setKlNumiid(numIId);
        cmsBtKlSkuModel.setProductCode(productCode);
        cmsBtKlSkuModel.setSku(klSku.getStringAttribute("skuCode"));
        cmsBtKlSkuModel.setKlSkuKey(skuKey);
        cmsBtKlSkuModel.setUpc(klSku.getStringAttribute("barcode"));

        cmsBtKlSkuModel.setModified(DateTimeUtil.getDate());
        cmsBtKlSkuModel.setCreated(DateTimeUtil.getDate());
        cmsBtKlSkuModel.setModifier(getTaskName());
        cmsBtKlSkuModel.setCreater(getTaskName());

        return cmsBtKlSkuModel;
    }

    protected void insertOrUpdateOrDeleteCmsBtKlSku(CmsBtKlSkuModel skuModel) {
        String skuKey = skuModel.getKlSkuKey();
        CmsBtKlSkuModel cmsBtKlSkuModel = getCmsBtKlSkuModel(skuModel.getChannelId(), skuModel.getOrgChannelId(), skuModel.getProductCode(), skuModel.getSku());
        if (cmsBtKlSkuModel == null) {
            if (!StringUtils.isEmpty(skuKey)) {
                cmsBtKlSkuDao.insert(skuModel);
            }
        } else {
            if (StringUtils.isEmpty(skuKey)) {
                cmsBtKlSkuDao.delete(cmsBtKlSkuModel.getId());
            } else {
                if (!skuKey.equals(cmsBtKlSkuModel.getKlSkuKey())) {
                    cmsBtKlSkuModel.setKlSkuKey(skuKey);
                    cmsBtKlSkuModel.setModifier(getTaskName());
                    cmsBtKlSkuModel.setModified(new Date());
                    cmsBtKlSkuDao.update(cmsBtKlSkuModel);
                }
            }
        }
    }

    protected CmsBtKlSkuModel getCmsBtKlSkuModel(String channelId, String orgChannelId, String code, String sku) {

        Map<String, Object> map = new HashMap<>();
        map.put("orgChannelId", orgChannelId);
        map.put("channelId", channelId);
        map.put("productCode", code);
        map.put("sku", sku);
        CmsBtKlSkuModel skuModel = cmsBtKlSkuDao.selectOne(map);

        return skuModel;
    }

    protected void saveProductPlatformSku(SxData sxData, SkuOuterIdResult[] skuKeys) {
        String channelId = sxData.getChannelId();
        int cartId = sxData.getCartId();
        List<BulkUpdateModel> bulkList = new ArrayList<>();
        for (CmsBtProductModel productModel : sxData.getProductList()) {
            // 只处理这次上新的，之前上过这次不上的，不会清除skuKey的记录，因为暂时考拉也不支持更新，等以后有了再看怎么处理
            List<BaseMongoMap<String, Object>> klSkus = productModel.getPlatform(cartId).getSkus();

            for (BaseMongoMap<String, Object> sku : klSkus) {
                String skuCode = sku.getStringAttribute("skuCode");
                SkuOuterIdResult result = Stream.of(skuKeys).filter(skuKey -> skuCode.equals(skuKey.getSkuOuterId())).findFirst().orElse(null);
                if (result != null) {
                    // 更新条件
                    HashMap<String, Object> queryMap = new HashMap<>();
                    queryMap.put("platforms.P" + cartId + ".skus.skuCode", skuCode);
                    // 更新内容
                    HashMap<String, Object> updateMap = new HashMap<>();
                    updateMap.put("platforms.P" + cartId + ".skus.$.skuKey", result.getSkuKey());

                    BulkUpdateModel model = new BulkUpdateModel();
                    model.setUpdateMap(updateMap);
                    model.setQueryMap(queryMap);
                    bulkList.add(model);
                }
            }
            if (ListUtils.notNull(bulkList)) {
                // 批量更新P34.skus里面的属性
                productService.bulkUpdateWithMap(channelId, bulkList, getTaskName(), "$set");
                bulkList.clear();
                $info("保存product的考拉skuKey成功！[ProductId:%s], [ChannelId:%s], [CartId:%s]", productModel.getProdId(), channelId, cartId);
            }
        }
    }

}
