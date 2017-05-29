package com.voyageone.task2.cms.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Joiner;
import com.taobao.api.ApiException;
import com.taobao.api.domain.ScItem;
import com.taobao.top.schema.exception.TopSchemaException;
import com.taobao.top.schema.factory.SchemaWriter;
import com.taobao.top.schema.field.Field;
import com.taobao.top.schema.field.InputField;
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
import com.voyageone.common.util.*;
import com.voyageone.components.tmall.exceptions.GetUpdateSchemaFailException;
import com.voyageone.components.tmall.service.TbItemSchema;
import com.voyageone.components.tmall.service.TbProductService;
import com.voyageone.components.tmall.service.TbScItemService;
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
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.dao.cms.mongo.CmsMtPlatformCategorySchemaTmDao;
import com.voyageone.service.impl.cms.DictService;
import com.voyageone.service.impl.cms.PlatformProductUploadService;
import com.voyageone.service.impl.cms.TaobaoScItemService;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.promotion.PromotionDetailService;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.sx.rule_parser.ExpressionParser;
import com.voyageone.service.impl.com.mq.MqSender;
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
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
    public enum TtPropName {
        tt_main_category_leaf,   // 主类目与平台叶子类目匹配关系
        tt_main_category,        // 主类目与平台一级类目匹配关系
        tt_category              // feed类目与平台一级类目匹配关系
    }
    // 主类目与天猫平台类目的匹配JSON项目名称
    public enum TtItemName {
        t_key_category,          // key:主类目(或feed类目)
        t_key_brand,             // key:品牌
        t_key_sizeType,          // key:适用人群
        t_value_category         // value:天猫平台类目(天猫叶子类目，天猫一级类目)
    }

    @Autowired
    private ProductGroupService productGroupService;
    @Autowired
    private CmsBtProductDao cmsBtProductDao;
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
    @Autowired
    private TbScItemService tbScItemService;
    @Autowired
    private MqSender sender;
    @Autowired
    private TbProductService tbProductService;
    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "CmsBuildPlatformProductUploadTmTongGouJob";
    }

    private Map<String, Map<String, List<ConditionPropValueModel>>> channelConditionConfig;

    // 保存每个渠道每个平台上每个商品的上新结果(成功失败件数信息,key为"channelId_cartId_groupId")
    Map<String, Map<String, Object>> resultMap = new ConcurrentHashMap<>();

    /**
     * 天猫国际官网同购上新处理
     *
     * @param taskControlList taskcontrol信息
     */
    @Override
    public void onStartup(List<TaskControlBean> taskControlList) throws Exception {
//
////        // 清除缓存（这样在cms_mt_channel_config表中刚追加的价格计算公式等配置就能立刻生效了）
////        CacheHelper.delete(CacheKeyEnums.KeyEnum.ConfigData_CmsChannelConfigs.toString());
//
//        // 线程数(默认为5)
//        threadCount = NumberUtils.toInt(TaskControlUtils.getVal1WithDefVal(taskControlList, TaskControlEnums.Name.thread_count, "5"));
//        // 抽出件数(默认为500)
//        rowCount = NumberUtils.toInt(TaskControlUtils.getVal1WithDefVal(taskControlList, TaskControlEnums.Name.row_count, "500"));
//
//        // 获取该任务可以运行的销售渠道
//        List<String> channelIdList = TaskControlUtils.getVal1List(taskControlList, TaskControlEnums.Name.order_channel_id);
//
//        // 初始化cms_mt_channel_condition_config表的条件表达式(避免多线程时2次初始化)
//        channelConditionConfig = new HashMap<>();
//        if (ListUtils.notNull(channelIdList)) {
//            for (final String orderChannelID : channelIdList) {
//                channelConditionConfig.put(orderChannelID, conditionPropValueRepo.getAllByChannelId(orderChannelID));
//            }
//        }
//
//        List<Map<String, Object>> channelCartMapList = new ArrayList<>();
//
//        // 循环所有销售渠道
//        if (ListUtils.notNull(channelIdList)) {
//            for (String channelId : channelIdList) {
//                if (ChannelConfigEnums.Channel.USJGJ.getId().equals(channelId)) {
//                    // 商品上传(USJOI天猫国际官网同购)
//                    doProductUpload(channelId, CartEnums.Cart.LTT.getValue(), threadCount, rowCount);
//                    sxProductService.add2ChannelCartMapList(channelCartMapList, channelId, CartEnums.Cart.LTT.getValue());
//                } else {
//                    // 商品上传(天猫国际官网同购)
//                    doProductUpload(channelId, CartEnums.Cart.TT.getValue(), threadCount, rowCount);
//                    sxProductService.add2ChannelCartMapList(channelCartMapList, channelId, CartEnums.Cart.TT.getValue());
//                }
//            }
//        }
//
//        // 输出最终结果
//        sxProductService.doPrintResultMap(resultMap, "天猫官网同购上新", channelCartMapList);

        // WMS切换临时测试用 START
//        doUploadMain(taskControlList); // 临时先注释掉不用
        Map<String, Integer> map = sxProductService.getAvailQuantity("017", "30", "105030", null);
        map.entrySet().forEach((ky)->{
            $info("TOM:" + ky.getKey() + ":" + ky.getValue() + ";");
            List<Map<String, Object>> lst = new ArrayList<>();
            Map<String, Object> messageMap = new HashMap<>();
            messageMap.put("channelId", "017");
            messageMap.put("cartId", "30");
            messageMap.put("sku", ky.getKey());
            sender.sendMessage("ewms_mq_stock_sync_platform_stock_017", messageMap);

        });
        {
            Date nowTime  = new Date();
            Date changeTime = null;
            try {
                changeTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2017-05-28 00:00:00");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Map<String, Integer> skuLogicQtyMap = new HashMap<>();
            if (changeTime.before(nowTime)) {
                $info("TOM-2:Y");
            } else {
                $info("TOM-2:N");
            }

        }
        // WMS切换临时测试用 END

        // 正常结束
        $info("天猫国际官网同购主线程正常结束");
    }


    public void doUploadMain(List<TaskControlBean> taskControlList) {
        // 初始化cms_mt_channel_condition_config表的条件表达式(避免多线程时2次初始化)
        channelConditionConfig = new HashMap<>();

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

        // 线程数(默认为5)
        threadCount = NumberUtils.toInt(TaskControlUtils.getVal1WithDefVal(taskControlList, TaskControlEnums.Name.thread_count, "5"));
        // 抽出件数(默认为500)
        rowCount = NumberUtils.toInt(TaskControlUtils.getVal1WithDefVal(taskControlList, TaskControlEnums.Name.row_count, "500"));

        // 获取该任务可以运行的销售渠道
        List<TaskControlBean> taskControlBeanList = TaskControlUtils.getVal1s(taskControlList, TaskControlEnums.Name.order_channel_id);

        // 准备按组分配线程（相同的组， 会共用相同的一组线程通道， 不同的组， 线程通道互不干涉）
        Map<String, List<String>> mapTaskControl = new HashMap<>();
        taskControlBeanList.forEach((l)->{
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
            // 再次获取一下配置项
            channelConditionConfig.put(l.getCfg_val1(), conditionPropValueRepo.getAllByChannelId(l.getCfg_val1()));
        });

        Map<String, ExecutorService> mapThread = new HashMap<>();

        while (true) {

            mapTaskControl.forEach((k, v)->{
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
                                    if (ChannelConfigEnums.Channel.USJGJ.getId().equals(channelId)) {
                                        doProductUpload(channelId, CartEnums.Cart.LTT.getValue(), threadCount, rowCount);
                                    } else {
                                        doProductUpload(channelId, CartEnums.Cart.TT.getValue(), threadCount, rowCount);
                                    }
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
        Map<String, List<Map<String, String>>> categoryMappingListMap = getCategoryMapping(channelId, cartId);

        // 创建线程池
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        // 根据上新任务列表中的groupid循环上新处理
        for (CmsBtSxWorkloadModel cmsBtSxWorkloadModel : sxWorkloadModels) {
            // 启动多线程
            executor.execute(() -> uploadProduct(cmsBtSxWorkloadModel, shopProp, tmTonggouFeedAttrList, categoryMappingListMap));
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

        $info("当前渠道的天猫官网同购上新任务执行完毕！[channelId:%s] [cartId:%s] [上新对象group件数:%s] ", channelId, cartId, sxWorkloadModels.size());
    }

    /**
     * 平台产品上新处理
     *
     * @param cmsBtSxWorkloadModel CmsBtSxWorkloadModel WorkLoad信息
     * @param shopProp ShopBean 店铺信息
     * @param tmTonggouFeedAttrList List<String> 当前渠道和平台设置的可以天猫官网同购上传的feed attribute列表
     * @param categoryMappingListMap 当前渠道和平台设置类目和天猫平台类目(叶子类目或平台一级类目)匹配信息列表map
     */
    public void uploadProduct(CmsBtSxWorkloadModel cmsBtSxWorkloadModel, ShopBean shopProp,
                              List<String> tmTonggouFeedAttrList, Map<String, List<Map<String, String>>> categoryMappingListMap) {

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
        // 开始时间
        long prodStartTime = System.currentTimeMillis();

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

            // 20170417 全链路库存改造 charis STA
            // 判断一下是否需要做货品绑定
            String storeCode = taobaoScItemService.doGetLikingStoreCode(shopProp, sxData.getMainProduct().getOrgChannelId());
            if (!StringUtils.isEmpty(storeCode) && !channelId.equals("928")) {
                String title = getTitleForTongGou(mainProduct, sxData, shopProp);
                Map<String, ScItem> scItemMap = new HashMap<>();
                for (String sku_outerId : strSkuCodeList) {
                    ScItem scItem;
                    // 检查是否发布过仓储商品
                    try {
                        scItem = tbScItemService.getScItemByOuterCode(shopProp, sku_outerId);
                    } catch (ApiException e) {
                        String errMsg = String.format("自动设置天猫商品全链路库存管理:检查是否发布过仓储商品:{outerId: %s, err_msg: %s}", sku_outerId, e.toString());
                        throw new BusinessException(errMsg);
                    }
                    if (scItem == null) {
                        // 没有发布过仓储商品的场合， 发布仓储商品
                        try {
                            scItem = tbScItemService.addScItemSimple(shopProp, title, sku_outerId);
                        } catch (ApiException e) {
                            String errMsg = String.format("自动设置天猫商品全链路库存管理:发布仓储商品:{outerId: %s, err_msg: %s}", sku_outerId, e.toString());
                            throw new BusinessException(errMsg);
                        }
                    }
                    scItemMap.put(sku_outerId, scItem);
                }
                sxData.setScItemMap(scItemMap);
            }
            // 20170417 全链路库存改造 charis END

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
            // WMS2.0切换 20170526 charis STA
            // 上新对象code
            List<String> listSxCode = null;
            if (ListUtils.notNull(sxData.getProductList())) {
                listSxCode = sxData.getProductList().stream().map(p -> p.getCommonNotNull().getFieldsNotNull().getCode()).collect(Collectors.toList());
            }
            Date nowTime  = new Date();
            Date changeTime = null;
            try {
                changeTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2017-05-28 00:00:00");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Map<String, Integer> skuLogicQtyMap = new HashMap<>();
            if (changeTime.before(nowTime)) {
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
            } else {
                skuLogicQtyMap = productService.getLogicQty(mainProduct.getOrgChannelId(), strSkuCodeList);
            }
            // WMS2.0切换 20170526 charis END

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
            TbItemSchema tbItemSchema = null;
            if (!StringUtils.isEmpty(sxData.getPlatform().getNumIId())) {
                // 更新商品
                updateWare = true;
                // 取得更新对象商品id
                numIId = sxData.getPlatform().getNumIId();
                // 获取商品页面信息
                tbItemSchema = tbSimpleItemService.getSimpleItem(shopProp, Long.parseLong(numIId));
            }

            // 编辑天猫国际官网同购共通属性
            BaseMongoMap<String, String> productInfoMap = getProductInfo(sxData, shopProp, priceConfigValue,
                    skuLogicQtyMap, tmTonggouFeedAttrList, categoryMappingListMap, updateWare, tbItemSchema);

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
                if (result.contains("商品类目未授权")) {
                    errMsg += "（使用的类目是：" + productInfoMap.get("category") + "）";
                }
                $error(errMsg);
                throw new BusinessException(errMsg);
            } else {
                // 天猫官网同购新增/更新商品成功时
                if (!updateWare) {
                    numIId = result;

                    // 设置共通属性
                    sxData.getPlatform().setNumIId(numIId);
                    // 更新cms_bt_product_groups表
                    sxData.getPlatform().setModified(DateTimeUtil.getNow());
                    productGroupService.update(sxData.getPlatform());


                    // 批量更新产品的平台状态.
                    List<BulkUpdateModel> bulkList = new ArrayList<>();
                    for (String code : listSxCode) {
                        // modified by morse.lu 2016/08/08 end
                        // 设置批量更新条件
                        HashMap<String, Object> bulkQueryMap = new HashMap<>();
                        bulkQueryMap.put("common.fields.code", code);
                        // 设置更新值
                        HashMap<String, Object> bulkUpdateMap = new HashMap<>();
                        bulkUpdateMap.put("platforms.P" + cartId + ".pNumIId", numIId);

                        // 设定批量更新条件和值
                        if (!bulkUpdateMap.isEmpty()) {
                            BulkUpdateModel bulkUpdateModel = new BulkUpdateModel();
                            bulkUpdateModel.setUpdateMap(bulkUpdateMap);
                            bulkUpdateModel.setQueryMap(bulkQueryMap);
                            bulkList.add(bulkUpdateModel);
                        }
                    }
                    // 批量更新product表
                    if (!bulkList.isEmpty()) {
                        // 因为是回写产品状态，找不到产品时也不插入新错误的记录
                        cmsBtProductDao.bulkUpdateWithMap(channelId, bulkList, null, "$set", false);
                    }
                }
            }

            // 这些Liking的商品， 仍然需要绑定货品
			boolean blnDoScitemMap = false;
            if (channelId.equals("928") && cartId == 31) {
				List<String> allowDoScitemNumIIdList = new ArrayList<>();
//				allowDoScitemNumIIdList.add("544342030946");allowDoScitemNumIIdList.add("544333633529");
//				allowDoScitemNumIIdList.add("544343354326");allowDoScitemNumIIdList.add("544309896081");
//				allowDoScitemNumIIdList.add("544344586861");allowDoScitemNumIIdList.add("544344538928");
//				allowDoScitemNumIIdList.add("544336748554");allowDoScitemNumIIdList.add("544363981461");
//				allowDoScitemNumIIdList.add("544338864090");allowDoScitemNumIIdList.add("544345292489");
//				allowDoScitemNumIIdList.add("544362496881");allowDoScitemNumIIdList.add("544362596746");
//				allowDoScitemNumIIdList.add("544417499407");allowDoScitemNumIIdList.add("544418555388");
//				allowDoScitemNumIIdList.add("544419007438");allowDoScitemNumIIdList.add("544402062832");
//				allowDoScitemNumIIdList.add("544403094363");allowDoScitemNumIIdList.add("544396981402");
//				allowDoScitemNumIIdList.add("544409262197");allowDoScitemNumIIdList.add("544399409951");
//				allowDoScitemNumIIdList.add("544409666167");allowDoScitemNumIIdList.add("544373780524");
//				allowDoScitemNumIIdList.add("544429243103");allowDoScitemNumIIdList.add("544432587723");
//				allowDoScitemNumIIdList.add("544406761330");allowDoScitemNumIIdList.add("544380568866");
//				allowDoScitemNumIIdList.add("544384908810");allowDoScitemNumIIdList.add("544440231827");
//				allowDoScitemNumIIdList.add("544422638918");allowDoScitemNumIIdList.add("544423610086");
//				allowDoScitemNumIIdList.add("544424226511");allowDoScitemNumIIdList.add("544429353036");
//				allowDoScitemNumIIdList.add("544438418181");allowDoScitemNumIIdList.add("544442870033");
//				allowDoScitemNumIIdList.add("544464583860");allowDoScitemNumIIdList.add("544449646056");
//				allowDoScitemNumIIdList.add("544459488076");allowDoScitemNumIIdList.add("544498205483");
//				allowDoScitemNumIIdList.add("544499721489");allowDoScitemNumIIdList.add("544530591413");
//				allowDoScitemNumIIdList.add("544526766496");allowDoScitemNumIIdList.add("544490984273");
//				allowDoScitemNumIIdList.add("544516749956");allowDoScitemNumIIdList.add("544545263613");
//				allowDoScitemNumIIdList.add("544547539056");allowDoScitemNumIIdList.add("544531850678");
//				allowDoScitemNumIIdList.add("544542006694");allowDoScitemNumIIdList.add("544531677203");
//				allowDoScitemNumIIdList.add("544560023247");allowDoScitemNumIIdList.add("544542454180");
//				allowDoScitemNumIIdList.add("544542546179");allowDoScitemNumIIdList.add("544531945199");
//				allowDoScitemNumIIdList.add("544505868390");allowDoScitemNumIIdList.add("544506288120");
//				allowDoScitemNumIIdList.add("544560543554");allowDoScitemNumIIdList.add("544569375028");
//				allowDoScitemNumIIdList.add("544683050054");allowDoScitemNumIIdList.add("544677773993");
//				allowDoScitemNumIIdList.add("544681609113");allowDoScitemNumIIdList.add("544708931758");
//				allowDoScitemNumIIdList.add("544653812912");allowDoScitemNumIIdList.add("544711099181");
//				allowDoScitemNumIIdList.add("544688209035");allowDoScitemNumIIdList.add("544696382979");
//				allowDoScitemNumIIdList.add("544660628133");allowDoScitemNumIIdList.add("544428181455");
//				allowDoScitemNumIIdList.add("544661060394");allowDoScitemNumIIdList.add("544689865870");
//				allowDoScitemNumIIdList.add("544690037926");allowDoScitemNumIIdList.add("544717543505");
//				allowDoScitemNumIIdList.add("544662420101");allowDoScitemNumIIdList.add("544700742175");
//				allowDoScitemNumIIdList.add("544701330314");allowDoScitemNumIIdList.add("544720919359");
//				allowDoScitemNumIIdList.add("544666292926");allowDoScitemNumIIdList.add("544667600008");
//				allowDoScitemNumIIdList.add("544668324179");allowDoScitemNumIIdList.add("544670352317");
//				allowDoScitemNumIIdList.add("544674264636");allowDoScitemNumIIdList.add("544730523887");
//				allowDoScitemNumIIdList.add("544731591602");allowDoScitemNumIIdList.add("544713066788");
//				allowDoScitemNumIIdList.add("544732759231");allowDoScitemNumIIdList.add("544678004561");
//				allowDoScitemNumIIdList.add("544736591709");allowDoScitemNumIIdList.add("544713697278");
//				allowDoScitemNumIIdList.add("544725110169");allowDoScitemNumIIdList.add("544726738039");
//				allowDoScitemNumIIdList.add("544867692832");

                allowDoScitemNumIIdList.add("544429243103");
                allowDoScitemNumIIdList.add("544696382979");
                allowDoScitemNumIIdList.add("544730523887");
                allowDoScitemNumIIdList.add("544725110169");

                if (allowDoScitemNumIIdList.contains(numIId)) {
					blnDoScitemMap = true;
				}
			}
            if (!StringUtils.isEmpty(storeCode) && !channelId.equals("928")) {
                blnDoScitemMap = true;
            }
            if (blnDoScitemMap) {
                // TODO: Liking因为效率问题， 不准备绑定货品了， 暂时注释掉， 以后可能要恢复的
                // 获取skuId
                List<Map<String, Object>> skuMapList = null;
                // 获取商品页面信息
                tbItemSchema = tbSimpleItemService.getSimpleItem(shopProp, Long.parseLong(numIId));
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

                // TODO: Liking因为效率问题， 不准备绑定货品了， 暂时注释掉， 以后可能要恢复的
                // 关联货品
                if (skuMapList != null) {

                    try {
                        for (Map<String, Object> skuMap : skuMapList) {
//                        skuMap: outer_id, price, quantity, sku_id
                            skuMap.put("scProductId",
                                    taobaoScItemService.doSetLikingScItem(
                                            shopProp, sxData,
                                            Long.parseLong(numIId), skuMap));
                        }
                    } catch (Exception e) {
                        String error = "";
//                        if (e.getMessage().contains("创建关联失败") && !updateWare) {
//                            try {
//                                tbProductService.delItem(shopProp, String.valueOf(numIId));
//                            } catch (ApiException e1) {
//                                error = "商品上新的场合,关联货品失败后做删除商品的动作时失败了！";
//                            }
//                        }
                        throw new Exception(e.getMessage() + " " + error);
                    }

                    saveCmsBtTmScItem_Liking(sxData, cartId, skuMapList);
                }


                // 回写数据库
                // TODO: 目前这个channelId传入的是原始channelId， 2017年4月份左右新wms上新前， 要改为928自己的channelId
//                saveCmsBtTmScItem_Liking(channelId, cartId, skuMapList);
                // TODO: Liking因为效率问题， 不准备绑定货品了， 暂时注释掉， 以后可能要恢复的

            }

            // 20170413 tom 在上新的时候已经判断过是否上架了， 所以这里只需要用之前的那个判断结果就行了 START
//            // 看看总库存
//            int qty = 0;
//            List<CmsBtProductModel> productModelList = sxData.getProductList();
//            for (CmsBtProductModel productModel : productModelList) {
//                List<BaseMongoMap<String, Object>> productPlatformSku = productModel.getPlatformNotNull(sxData.getCartId()).getSkus();
//
//                if (productPlatformSku != null) {
//                    for (BaseMongoMap<String, Object> sku : productPlatformSku) {
//                        if (Boolean.parseBoolean(sku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.isSale.name()))) {
//                            String skucode = sku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.skuCode.name());
//                            if (skuLogicQtyMap.containsKey(skucode)) {
//                                qty = qty + skuLogicQtyMap.get(skucode);
//                            }
//                        }
//                    }
//                }
//            }
//
//            // 调用淘宝商品上下架操作(新增的时候默认为下架，只有更新的时候才根据group里面platformActive调用上下架操作)
//            // 回写用商品上下架状态(OnSale/InStock)
//            CmsConstants.PlatformStatus platformStatus = null;
//            CmsConstants.PlatformActive platformActive = sxData.getPlatform().getPlatformActive();
//            // 更新商品并且PlatformActive=ToOnSale时,执行商品上架；新增商品或PlatformActive=ToInStock时，执行下架功能
//            // 库存大于0才能上架， 否则自动设为在库
//            if (updateWare && platformActive == CmsConstants.PlatformActive.ToOnSale && qty > 0) {
//                platformStatus = CmsConstants.PlatformStatus.OnSale;   // 上架
//            } else {
//                platformStatus = CmsConstants.PlatformStatus.InStock;   // 在库
//            }

            // status：0（上架）， status：2（下架）
            CmsConstants.PlatformStatus platformStatus = CmsConstants.PlatformStatus.InStock;
            if ("0".equals(productInfoMap.get("status"))) {
                // 商品上架
                platformStatus = CmsConstants.PlatformStatus.OnSale;
            }

            // 20170413 tom 在上新的时候已经判断过是否上架了， 所以这里只需要用之前的那个判断结果就行了 END

            if (changeTime.before(nowTime)) {
                // 20170526 调用新的更新库存接口同步库存 STA
                for (String sku : strSkuCodeList) {
                    Map<String, Object> messageMap = new HashMap<>();
                    messageMap.put("channelId", channelId);
                    messageMap.put("cartId", cartId);
                    messageMap.put("sku", sku);
                    sender.sendMessage("ewms_mq_stock_sync_platform_stock", messageMap);
                }
                // 20170526 调用新的更新库存接口同步库存 END
            } else {
                // 20170417 调用更新库存接口同步库存 STA
                sxProductService.synInventoryToPlatform(channelId, String.valueOf(cartId), null, strSkuCodeList);
                // 20170417 调用更新库存接口同步库存 END
            }

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

            // 更新特价宝
            sxData.getPlatform().setNumIId(numIId);
            updateTeJiaBaoPromotion(sxData);

            // added by morse.lu 2016/12/08 start
            if (ChannelConfigEnums.Channel.SN.getId().equals(channelId)) {
                // Sneakerhead
                try {
                    sxProductService.uploadCnInfo(sxData);
                } catch (IOException io) {
                    throw new BusinessException("上新成功!但在推送给美国数据库时发生异常!"+ io.getMessage());
                }
            }
            // added by morse.lu 2016/12/08 end

            // 把上新成功状态放入结果map中
            sxProductService.add2ResultMap(resultMap, channelId, cartId, groupId, updateWare, true);

            // 正常结束
            $info(String.format("天猫官网同购单个商品%s成功！[ChannelId:%s] [CartId:%s] [GroupId:%s] [NumIId:%s] [耗时:%s]",
                    updateWare ? "更新" : "上新", channelId, cartId, groupId, numIId, (System.currentTimeMillis() - prodStartTime)));
        } catch (Exception ex) {
            // 异常结束时
            String errMsg = String.format("天猫官网同购上新异常结束，开始记录异常状态！[ChannelId:%s] [CartId:%s] [GroupId:%s] [NumIId:%s]",
                    channelId, cartId, groupId, numIId);
            $error(errMsg);

            // 把上新失败结果加入到resultMap中
            sxProductService.add2ResultMap(resultMap, channelId, cartId, groupId, updateWare, false);

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

            // 异常结束
            $error(String.format("天猫官网同购单个商品%s失败！[ChannelId:%s] [CartId:%s] [GroupId:%s] [NumIId:%s] [耗时:%s] [errMsg:%s]",
                    updateWare ? "更新" : "上新", channelId, cartId, groupId, numIId, (System.currentTimeMillis() - prodStartTime), sxData.getErrorMessage()));
        }
    }

    // 注意： 本函数Liking专用（code无所谓， 随便瞎填的）
    private void saveCmsBtTmScItem_Liking(SxData sxData, int cartId, List<Map<String, Object>> skuMapList) {
        for(Map<String, Object> skuMap : skuMapList) {
            String code = "I_LIKING_IT";
            String skuCode = String.valueOf(skuMap.get("outer_id"));
            Map<String, Object> searchParam = new HashMap<>();
            searchParam.put("channelId", sxData.getChannelId());
            searchParam.put("orgChannelId", sxData.getMainProduct().getOrgChannelId());
            searchParam.put("cartId", cartId);
            searchParam.put("code", code);
            searchParam.put("sku", skuCode);
            CmsBtTmScItemModel scItemModel = cmsBtTmScItemDao.selectOne(searchParam);

            String scProductId = null;
            if (skuMap.containsKey("scProductId") && skuMap.get("scProductId") != null) {
                scProductId = String.valueOf(skuMap.get("scProductId"));
            }
            if (StringUtils.isEmpty(scProductId)) {
                // delete
                if (scItemModel != null) {
                    cmsBtTmScItemDao.delete(scItemModel.getId());
                }
            } else {
                if (scItemModel == null) {
                    // add
                    scItemModel = new CmsBtTmScItemModel();
                    scItemModel.setChannelId(sxData.getChannelId());
                    scItemModel.setOrgChannelId(sxData.getMainProduct().getOrgChannelId());
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
     * @param categoryMappingListMap 当前渠道和平台设置类目和天猫平台类目(叶子类目或平台一级类目)匹配信息列表map
     * @param updateWare 新增/更新flg(false:新增 true:更新)
     * @return JdProductBean 京东上新用bean
     * @throws BusinessException
     */
    private BaseMongoMap<String, String> getProductInfo(SxData sxData, ShopBean shopProp, String priceConfigValue,
                                                        Map<String, Integer> skuLogicQtyMap, List<String> tmTonggouFeedAttrList,
                                                        Map<String, List<Map<String, String>>> categoryMappingListMap,
                                                        boolean updateWare, TbItemSchema tbItemSchema) throws BusinessException {
        // 上新产品信息保存map
        BaseMongoMap<String, String> productInfoMap = new BaseMongoMap<>();

        CmsBtProductModel mainProduct = sxData.getMainProduct();
        CmsBtFeedInfoModel feedInfo = sxData.getCmsBtFeedInfoModel();
        List<CmsBtProductModel> productList = sxData.getProductList();
        List<BaseMongoMap<String, Object>> skuList = sxData.getSkuList();
        ExpressionParser expressionParser = new ExpressionParser(sxProductService, sxData);

        // 天猫国际官网同购平台（或USJOI天猫国际官网同购平台）
        CmsBtProductModel_Platform_Cart mainProductPlatformCart = mainProduct.getPlatform(sxData.getCartId());

        // 先临时这样处理
        String notAllowList = getConditionPropValue(sxData, "notAllowTitleList", shopProp);

        String valTitle = getTitleForTongGou(mainProduct, sxData, shopProp);
        // 店铺级标题禁用词 20161216 tom END
        // 官网同购是会自动把超长的字符截掉的， 为了提示运营， 报个错吧 20170509 tom START
        if (!StringUtils.isEmpty(valTitle)
                && ChannelConfigEnums.Channel.Coty.equals(shopProp.getOrder_channel_id()) ) {
            int titleLength = 0;

            try {
                titleLength = valTitle.getBytes("GB2312").length;
            } catch (UnsupportedEncodingException ignored) {
            }

            if (titleLength > 60) {
                throw new BusinessException(String.format("标题超长:%s", valTitle));
            }

        }
        // 官网同购是会自动把超长的字符截掉的， 为了提示运营， 报个错吧 20170509 tom END
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
        }

        // 20170523 临时用一下 tom START
        {
            // 如果曾经上新过， 但是cms里并没有保存着类目id， 说明类目id被人删掉了， 需要从天猫上拉回来

            if (updateWare && StringUtils.isEmpty(valCategory)) {
                // 回写PXX.pCatId, PXX.pCatPath等信息
                Map<String, String> pCatInfoMap = null;
                try {
                    String numIId = sxData.getPlatform().getNumIId();
                    pCatInfoMap = getSimpleItemCatInfo(shopProp, numIId);
                } catch (ApiException | TopSchemaException | GetUpdateSchemaFailException ignored) {
                }
                if (pCatInfoMap != null && pCatInfoMap.size() > 0) {
                    // 如果拉到了的话
                    if (pCatInfoMap.containsKey("pCatId")) {
                        if (!StringUtils.isEmpty(pCatInfoMap.get("pCatId"))) {
                            valCategory = pCatInfoMap.get("pCatId");
                        }
                    }
                }
                if (StringUtils.isEmpty(valCategory)) {
                    throw new BusinessException("如果曾经上新过， 但是cms里并没有保存着类目id， 说明类目id被人删掉了， 需要从天猫上拉回来， 但是拉取失败了， 这种可能性很小， 如果遇到了， 请联系IT。");
                }
            }
        }
        // 20170523 临时用一下 tom END

//        else if (feedInfo != null && !StringUtils.isEmpty(feedInfo.getCategory())) {
        // 使用商家自有系统类目路径
        // feed_info表的category（将中杠【-】替换为：【&gt;】(>)） (格式：<value>man&gt;sports&gt;socks</value>)

        if (StringUtils.isEmpty(valCategory)) {
            if (ChannelConfigEnums.Channel.LUCKY_VITAMIN.getId().equals(sxData.getChannelId())) {
                // LuckyVitamin默认固定
                Map<String, Object> paramCategory = new HashMap<>();
                paramCategory.put("cat_id", "50050237"); // 保健食品/膳食营养补充食品>海外膳食营养补充食品>其他膳食营养补充食品>其他膳食营养补充剂
                valCategory = JacksonUtil.bean2Json(paramCategory);
            } else if (ChannelConfigEnums.Channel.KitBag.getId().equals(sxData.getChannelId())) {
                // Kitbag默认固定
                valCategory = "运动/瑜伽/健身/球迷用品";
            } else {
                // 主产品主类目path
                String mainCatPath = mainProduct.getCommonNotNull().getCatPathEn();
                // 类目匹配优先顺序："主类目到天猫叶子类目" > "主类目到天猫一级类目" > "feed类目到天猫一级类目"
                // 主类目到天猫叶子类目匹配
                if (!StringUtils.isEmpty(mainCatPath) && MapUtils.isNotEmpty(categoryMappingListMap)) {
                    // 如果设置过主类目，并且cms_mt_channel_condition_mapping_config表中配置过主类目到天猫叶子类目的匹配关系
                    String leafCategory = getMainCategoryMappingInfo(mainCatPath, null, null, TtPropName.tt_main_category_leaf, categoryMappingListMap);
                    if (!StringUtils.isEmpty(leafCategory)) {
                        // 如果匹配到天猫叶子类目，需要设置匹配到天猫叶子类目id的json串(只有匹配到叶子类目才需要配置到id的json串)
                        Map<String, Object> paramCategory = new HashMap<>();
                        paramCategory.put("cat_id", leafCategory);
                        valCategory = JacksonUtil.bean2Json(paramCategory);
                    }
                }

                // 如果没有匹配到主类目对应的天猫叶子类目，则匹配主类目到天猫一级类目
                if (StringUtils.isEmpty(valCategory)
                        && !StringUtils.isEmpty(mainCatPath)
                        && MapUtils.isNotEmpty(categoryMappingListMap)) {
                    String brand = mainProduct.getCommonNotNull().getFieldsNotNull().getBrand();
                    String sizeType = mainProduct.getCommonNotNull().getFieldsNotNull().getSizeType();
                    // 匹配优先顺序：
                    // 1.主类目+品牌+适用人群
                    // 2.主类目+品牌
                    // 3.主类目+适用人群
                    // 4.主类目
                    valCategory = getMainCategoryMappingInfo(mainCatPath, brand, sizeType, TtPropName.tt_main_category, categoryMappingListMap);
                    if (StringUtils.isEmpty(valCategory)) {
                        valCategory = getMainCategoryMappingInfo(mainCatPath, brand, null, TtPropName.tt_main_category, categoryMappingListMap);
                    }
                    if (StringUtils.isEmpty(valCategory)) {
                        valCategory = getMainCategoryMappingInfo(mainCatPath, null, sizeType, TtPropName.tt_main_category, categoryMappingListMap);
                    }
                    if (StringUtils.isEmpty(valCategory)) {
                        valCategory = getMainCategoryMappingInfo(mainCatPath, null, null, TtPropName.tt_main_category, categoryMappingListMap);
                    }
                }
            }
        }

        // 如果根据主类目没有匹配到平台类目的时候，再根据feed类目来匹配
        if (StringUtils.isEmpty(valCategory) && MapUtils.isNotEmpty(categoryMappingListMap)
                && feedInfo != null && !StringUtils.isEmpty(feedInfo.getCategory())) {

            // 普通的获取类目的方式(只能匹配到cms_mt_channel_condition_mapping_config表中配置过配置过天猫一级类目)
            String feedCategory = getValueFromPageOrCondition("tmall_category_key", "", mainProductPlatformCart, sxData, shopProp);
            if (StringUtils.isEmpty(feedCategory)) {
                String errMsg = String.format("没有取到cms_mt_channel_condition_config表中\"tmall_category_key\"配置的" +
                        "类目对象项目对应的feed类目信息,不能根据feed类目取得对应的天猫平台一级类目信息，中止上新！");
                $error(errMsg);
                throw new BusinessException(errMsg);
            }

            // 如果设置过主类目，并且cms_mt_channel_condition_mapping_config表中配置过主类目到天猫叶子类目的匹配关系
            valCategory = getMainCategoryMappingInfo(feedCategory, null, null, TtPropName.tt_category, categoryMappingListMap);
        }

        // 如果最终没有匹配到天猫平台类目，则报出错误
        if (StringUtils.isEmpty(valCategory)) {
            String errMsg = String.format("从cms_mt_channel_condition_mapping_config表中没有匹配到天猫平台叶子类目或一级类目信息，中止上新！");
            $error(errMsg);
            throw new BusinessException(errMsg);
        }

        // 防止母婴类目 START            注意：这段逻辑一定要放在类目匹配最后来做
        // 如果标题包含下列关键字,再检查一下有没有设置错类目,防止天猫报错
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

            // 天猫平台类目(valCategory有可能为catId的json串，或者天猫叶子类目，天猫一级类目)
            String strCategoryPath = valCategory;
            if (valCategory.startsWith("{")) {
                // 获取到该产品上已经设置好的或者前面匹配到的天猫平台id
                Map<String, Object> categoryMap = JacksonUtil.json2Bean(valCategory, HashMap.class);
                String pCatId = categoryMap.containsKey("cat_id") ? (String)categoryMap.get("cat_id") : null;
                if (!StringUtils.isEmpty(pCatId)) {
                    // 取得设置好的或者前面匹配到的天猫平台id对应的pCatPath
                    strCategoryPath = getTongGouCatFullPathByCatId(shopProp, pCatId);
                }
            }

            // 如果不是下面这些类目，统一设置成"50026470"
            if (!StringUtils.isEmpty(strCategoryPath)
                    && !strCategoryPath.startsWith("孕妇装/孕产妇用品/营养")
                    && !strCategoryPath.startsWith("童鞋/婴儿鞋/亲子鞋")
                    && !strCategoryPath.startsWith("奶粉/辅食/营养品/零食")) {
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
                try {
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
        // 店铺级标题禁用词 20161216 tom START
        // 先临时这样处理
        if (!StringUtils.isEmpty(notAllowList)) {
            if (!StringUtils.isEmpty(valDescription)) {
                String[] splitWord = notAllowList.split(",");
                for (String notAllow : splitWord) {
                    // 直接删掉违禁词
                    valDescription = valDescription.replaceAll(notAllow, "");
                }
            }
        }
        // 店铺级标题禁用词 20161216 tom END
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
            // 如果当前店铺在cms_mt_channel_config表中配置成了运营自己在天猫后台管理无线端共通模块时
            CmsChannelConfigBean config = CmsChannelConfigs.getConfigBean(shopProp.getOrder_channel_id(),
                    CmsConstants.ChannelConfig.TMALL_WIRELESS_COMMON_MODULE_BY_USER, shopProp.getCart_id());
            if (config != null && "1".equals(config.getConfigValue1()) && updateWare) {
                // 如果设置成"1：运营自己天猫后台管理"时,用天猫平台上取下来的运营自己后台设置的值设置schema无线端共通模块相关属性

                String defaultValue = ((InputField)tbItemSchema.getFieldMap().get("wireless_desc")).getDefaultValue();
                // 店铺活动(json)
                valWirelessDetails = updateDefaultValue(valWirelessDetails, "shop_discount", defaultValue);
                // 文字说明(json)
                valWirelessDetails = updateDefaultValue(valWirelessDetails, "item_text", defaultValue);
                // 优惠(json)
                valWirelessDetails = updateDefaultValue(valWirelessDetails, "coupon", defaultValue);
                // 同店推荐(json)
                valWirelessDetails = updateDefaultValue(valWirelessDetails, "hot_recommanded", defaultValue);
            }
            productInfoMap.put("wireless_desc", valWirelessDetails);
        }

        // 看看总库存
        int qty = 0;
        List<CmsBtProductModel> productModelList = sxData.getProductList();
        for (CmsBtProductModel productModel : productModelList) {
            List<BaseMongoMap<String, Object>> productPlatformSku = productModel.getPlatformNotNull(sxData.getCartId()).getSkus();

            if (productPlatformSku != null) {
                for (BaseMongoMap<String, Object> sku : productPlatformSku) {
                    if (Boolean.parseBoolean(sku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.isSale.name()))) {
                        String skucode = sku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.skuCode.name());
                        if (skuLogicQtyMap.containsKey(skucode)) {
                            qty = qty + skuLogicQtyMap.get(skucode);
                        }
                    }
                }
            }
        }

        // 透明素材图
        try {
            String url = sxProductService.resolveDict("透明图", expressionParser, shopProp, getTaskName(), null);
            if (!StringUtils.isEmpty(url)) {
				String white_bg_image = sxProductService.uploadTransparentPictureToTm(sxData.getChannelId(), sxData.getCartId(), Long.toString(sxData.getGroupId()), shopProp, url, getTaskName());
				if (!StringUtils.isEmpty(white_bg_image)) {
					productInfoMap.put("white_bg_image", white_bg_image);
				}
            }
        } catch (Exception e) {
            // 出错了就算了， 不设置透明图碰碰运气
        }


        // 商品上下架
        CmsConstants.PlatformActive platformActive = sxData.getPlatform().getPlatformActive();

        // 20170413 tom 如果是新建的场合， 需要根据配置来设置上下架状态 START
        if (!updateWare) {
            // 新增的场合， 看一下配置
            platformActive = sxProductService.getDefaultPlatformActiveConfigByChannelCart(sxData.getChannelId(), String.valueOf(sxData.getCartId()));
        }
        if (qty == 0) {
            // 库存为0则自动设置为下架
            platformActive = CmsConstants.PlatformActive.ToInStock;
        }
        // 20170413 tom 如果是新建的场合， 需要根据配置来设置上下架状态 END

        // 更新商品并且PlatformActive=ToOnSale时,执行商品上架；新增商品或PlatformActive=ToInStock时，执行下架功能
        // 库存大于0才能上架， 否则自动设为在库
        if (platformActive == CmsConstants.PlatformActive.ToOnSale) {
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

        // 如果根据channelid和platformPropId取得的条件表达式为空
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
                String propValue = product.getCommon().getFields().getHsCodePrivate();

                // 20170427 bug修正 START
                // 通过配置表(cms_mt_channel_config)来决定用hsCodeCross，还是hsCodePrivate，默认用hsCodePrivate
                CmsChannelConfigBean hscodeConfig = CmsChannelConfigs.getConfigBean(expressionParser.getSxData().getChannelId(),
                        CmsConstants.ChannelConfig.HSCODE,
                        String.valueOf(expressionParser.getSxData().getCartId()) + CmsConstants.ChannelConfig.SX_HSCODE);
                if (hscodeConfig != null) {
                    String hscodePropName = hscodeConfig.getConfigValue1(); // 目前配置的是code或者color或者codeDiff
                    if (!StringUtils.isEmpty(hscodePropName)) {
                        String val = expressionParser.getSxData().getMainProduct().getCommon().getFields().getStringAttribute(hscodePropName);
                        if (!StringUtils.isEmpty(val)) {
                            propValue = val;
                        }
                    }
                }
                // 20170427 bug修正 END

                if (!StringUtils.isEmpty(propValue)) {
                    if (propValue.contains(Separtor_Coma)) {
                        hscode = propValue.substring(0, propValue.indexOf(Separtor_Coma));
                    } else {
                        hscode = propValue;
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
                saleProp.put("颜色", color);
                // 根据skuCode从skuList中取得common.sku和PXX.sku合并之后的sku
                BaseMongoMap<String, Object> mergedSku = skuList.stream()
                        .filter(s -> s.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.skuCode.name()).equals(skuCode))
                        .findFirst()
                        .get();
                // 尺寸
                String size = mergedSku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.sizeSx.name());
                if (!StringUtils.isEmpty(size)) {
                    size = size.replaceAll(",", "，");
                    size = size.replaceAll(":", "：");
                    size = size.replaceAll(";", "；");
                    size = size.replaceAll("&", "＆");
                    size = size.replaceAll("/", "／");
                }
                saleProp.put("规格", size);
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
                String propImage = expressionParser.getSxProductService().getProductImages(product, CmsBtProductConstants.FieldImageType.PRODUCT_IMAGE, cartId).get(0).getName();
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

        // 取得天猫官网同购pCatId对应的pCatPath
        String pCatPath = getTongGouCatFullPathByCatId(shopProp, pCatId);
        if (!StringUtils.isEmpty(pCatPath)) {
            pCatInfoMap.put("pCatPath", pCatPath);
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

        try {
            // 从cms_mt_platform_category_schema_tm_cXXX(channelId)表中取得pCatId对应的pCatPath
            CmsMtPlatformCategorySchemaTmModel categorySchemaTm = platformCategorySchemaDao.selectPlatformCatSchemaTmModel(
                    pCatdId, shopProp.getOrder_channel_id(), NumberUtils.toInt(shopProp.getCart_id()));
            if (categorySchemaTm != null) {
                pCatPath = categorySchemaTm.getCatFullPath();
            }
        } catch(Exception e) {
            e.printStackTrace();
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
     * @return Map<String, List<Map<String, String>>> 表中的配置mapping信息
     */
    protected Map<String, List<Map<String, String>>> getCategoryMapping(String channelId, int cartId) {

        Map<String, List<Map<String, String>>> categoryMapping = new HashMap<>();
        // 取得主类目与天猫平台叶子类目之间的mapping关系数据
        // key:主类目String     value:天猫平台叶子类目String
        List<CmsMtChannelConditionMappingConfigModel> mainLeafList = getChannelConditionMappingInfo(channelId, cartId, TtPropName.tt_main_category_leaf.name());
        if (ListUtils.notNull(mainLeafList)) {
            List<Map<String, String>> conditionMappingMapList = new ArrayList<>();
            mainLeafList.forEach(p -> {
                Map<String, String> leafMap = new LinkedHashMap<>();
                leafMap.put(TtItemName.t_key_category.name(), p.getMapKey());
                leafMap.put(TtItemName.t_value_category.name(), p.getMapValue());
                conditionMappingMapList.add(leafMap);
            });
            categoryMapping.put(TtPropName.tt_main_category_leaf.name(), conditionMappingMapList);
        }

        // 取得主类目与天猫平台一级类目之间的mapping关系数据
        // key:包含主类目，品牌，适用人群等的json     value:天猫平台一级类目String
        List<CmsMtChannelConditionMappingConfigModel> mainCategoryList = getChannelConditionMappingInfo(channelId, cartId, TtPropName.tt_main_category.name());
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
                    categoryMap.put(TtItemName.t_value_category.name(), p.getMapValue());
                    conditionMappingMapList.add(categoryMap);
                }
            });
            if (ListUtils.notNull(errKeyList)) {
                String errMsg = String.format("从cms_mt_channel_condition_mapping_config表中取得的%s条主类目到天猫一级类目" +
                                "的匹配(%s)关系的匹配key项目不是json格式的，请把数据线修改成json格式之后再次重试！[错误key:%s]",
                        errKeyList.size(), TtPropName.tt_main_category.name(), Joiner.on(",").join(errKeyList));
                $error(errMsg);
                throw new BusinessException(errMsg);
            }
            categoryMapping.put(TtPropName.tt_main_category.name(), conditionMappingMapList);
        }

        // 取得feed类目与天猫平台一级类目之间的mapping关系数据
        List<CmsMtChannelConditionMappingConfigModel> feedCategoryList = getChannelConditionMappingInfo(channelId, cartId, TtPropName.tt_category.name());
        if (ListUtils.notNull(feedCategoryList)) {
            List<Map<String, String>> conditionMappingMapList = new ArrayList<>();
            feedCategoryList.forEach(p -> {
                Map<String, String> feedMap = new LinkedHashMap<>();
                feedMap.put(TtItemName.t_key_category.name(), p.getMapKey());
                feedMap.put(TtItemName.t_value_category.name(), p.getMapValue());
                conditionMappingMapList.add(feedMap);
            });
            categoryMapping.put(TtPropName.tt_category.name(), conditionMappingMapList);
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
                                                TtPropName ttPropName, Map<String, List<Map<String, String>>> categoryMappingListMap) {
        if (mainCatPath == null
                || ttPropName == null
                || MapUtils.isEmpty(categoryMappingListMap)
                || !categoryMappingListMap.containsKey(ttPropName.name())
                || ListUtils.isNull(categoryMappingListMap.get(ttPropName.name()))) return null;

        Map<String, String> resultMap = categoryMappingListMap.get(ttPropName.name()).stream()
                .filter(m -> mainCatPath.equalsIgnoreCase(m.get(TtItemName.t_key_category.name())))
                .filter(m -> (StringUtils.isEmpty(brand) || brand.equalsIgnoreCase(m.get(TtItemName.t_key_brand.name()))))
                .filter(m -> (StringUtils.isEmpty(sizeType) || sizeType.equalsIgnoreCase(m.get(TtItemName.t_key_sizeType.name()))))
                .findFirst()
                .orElse(null);

        if (MapUtils.isNotEmpty(resultMap)) {
            return resultMap.get(TtItemName.t_value_category.name());
        }

        return null;
    }

    public String getTitleForTongGou(CmsBtProductModel mainProduct, SxData sxData, ShopBean shopProp) {
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

        // 店铺级标题禁用词 20161216 tom START
        // 先临时这样处理
        String notAllowList = getConditionPropValue(sxData, "notAllowTitleList", shopProp);
        if (!StringUtils.isEmpty(notAllowList)) {
            if (!StringUtils.isEmpty(valTitle)) {
                String[] splitWord = notAllowList.split(",");
                for (String notAllow : splitWord) {
                    // 直接删掉违禁词
                    valTitle = valTitle.replaceAll(notAllow, "");
                }
            }
        }
        return valTitle;
    }

    public String updateDefaultValue(String valWirelessDetails, String fieldName, String defaultValue) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> defaultMap = JacksonUtil.jsonToMap(defaultValue);
        Map<String, Object> wirelessMap = JacksonUtil.jsonToMap(valWirelessDetails);
        if (defaultMap != null && defaultMap.get(fieldName) != null) {
            if ("item_picture".equals(fieldName)) {
                ((Map<String,Object>)(wirelessMap.get(fieldName))).put("order", ((Map<String,Object>)(defaultMap.get(fieldName))).get("order"));
            } else {
                wirelessMap.put(fieldName, defaultMap.get(fieldName));
            }
        }
        try {
            valWirelessDetails = objectMapper.writeValueAsString(wirelessMap);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return valWirelessDetails;
    }

}
