package com.voyageone.task2.cms.service;

import com.google.common.base.Joiner;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Channels;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.cnn.enums.CnnConstants;
import com.voyageone.components.cnn.service.CnnWareService;
import com.voyageone.service.bean.cms.product.SxData;
import com.voyageone.service.dao.cms.CmsBtSxCnSkuDao;
import com.voyageone.service.daoext.cms.CmsBtSxCnSkuDaoExt;
import com.voyageone.service.impl.cms.DictService;
import com.voyageone.service.impl.cms.PlatformProductUploadService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.sx.rule_parser.ExpressionParser;
import com.voyageone.service.model.cms.CmsBtSxCnSkuModel;
import com.voyageone.service.model.cms.CmsBtSxWorkloadModel;
import com.voyageone.service.model.cms.CmsMtPlatformDictModel;
import com.voyageone.service.model.cms.mongo.product.*;
import com.voyageone.task2.base.BaseCronTaskService;
import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.base.util.TaskControlUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 新独立域名(Liking)平台产品上新服务
 *
 * @author desmond on 2017/01/04.
 * @version 2.11.0
 * @since 2.11.0
 */
@Service
public class CmsBuildPlatformProductUploadCnnService extends BaseCronTaskService {

    // 新独立域名平台ID(32 Liking)
    private static final int CART_ID_CNN = CartEnums.Cart.LCN.getValue();
    // 分隔符(,)
    private final static String Separtor_Coma = ",";
    // 上新名称
    private final static String UPLOAD_NAME = "新独立域名Liking";
    // 保存每个渠道每个商品的上新结果(成功失败件数信息,key为"channelId_groupId")
    Map<String, Map<String, Object>> resultMap = new ConcurrentHashMap<>();
    // 线程数(synship.tm_task_control中设置的当前job的最大线程数"thread_count", 默认为3)
    private int threadCount;
    // 抽出件数(synship.tm_task_control中设置的当前job的最大线程数"row_count", 默认为500)
    private int rowCount;
    @Autowired
    private PlatformProductUploadService platformProductUploadService;
    @Autowired
    private CnnWareService cnnWareService;
    @Autowired
    private ProductService productService;
    @Autowired
    private SxProductService sxProductService;
    @Autowired
    private DictService dictService;
    @Autowired
    private CmsBtSxCnSkuDao cmsBtSxCnSkuDao;
    @Autowired
    private CmsBtSxCnSkuDaoExt cmsBtSxCnSkuDaoExt;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "CmsBuildPlatformProductUploadCnnJob";
    }

    /**
     * 新独立域名平台产品上新处理
     *
     * @param taskControlList taskcontrol信息
     */
    @Override
    public void onStartup(List<TaskControlBean> taskControlList) throws Exception {
        if (ListUtils.isNull(taskControlList)) {
            String errMsg = String.format("%s处理JOB没有启动! tm_task_control表中没有相应的配置信息", UPLOAD_NAME);
            $warn(errMsg);
            return;
        }

        // 线程数(默认为3)
        threadCount = NumberUtils.toInt(TaskControlUtils.getVal1WithDefVal(taskControlList, TaskControlEnums.Name.thread_count, "3"));
        // 抽出件数(默认为500)
        rowCount = NumberUtils.toInt(TaskControlUtils.getVal1WithDefVal(taskControlList, TaskControlEnums.Name.row_count, "500"));

        // 获取该任务可以运行的销售渠道
        List<String> channelIdList = TaskControlUtils.getVal1List(taskControlList, TaskControlEnums.Name.order_channel_id);

        // 初始化cms_mt_channel_condition_config表的条件表达式(避免多线程时2次初始化)
//        channelConditionConfig = new HashMap<>();
//        if (ListUtils.notNull(channelIdList)) {
//            for (final String orderChannelID : channelIdList) {
//                channelConditionConfig.put(orderChannelID, conditionPropValueRepo.getAllByChannelId(orderChannelID));
//            }
//        }

        // 所有渠道Liking上新开始时间
        long totalStartTime = System.currentTimeMillis();

        // 创建线程池(外面channel级别的线程池，只给2个channel同时上新，防止一个channel上新数据太多，影响其他channel的上新)
        ExecutorService executor = Executors.newFixedThreadPool(2);
        // 根据上新任务列表中的groupid循环上新处理
        for(String channelId : channelIdList) {
            // 启动多线程
            executor.execute(() -> doUploadChannel(channelId, CART_ID_CNN));
        }
        // ExecutorService停止接受任何新的任务且等待已经提交的任务执行完成(已经提交的任务会分两类：一类是已经在执行的，另一类是还没有开始执行的)，
        // 当所有已经提交的任务执行完毕后将会关闭ExecutorService
        executor.shutdown(); // 并不是终止线程的运行，而是禁止在这个Executor中添加新的任务
        try {
            // 阻塞，直到线程池里所有任务结束
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }

        $info("=================" + UPLOAD_NAME + "上新  最终结果=====================");
        for(String channelId : channelIdList) {
            int totalCnt = 0;
            int addOkCnt = 0;
            int updOkCnt = 0;
            int addNgCnt = 0;
            int updNgCnt = 0;

            for (Object obj: resultMap.values()) {
                Map<String, Object> result = (Map<String, Object>)obj;
                if (!channelId.equals(result.get("channelId"))) {
                    continue;
                }
                totalCnt++;
                if (!(boolean)result.get("isUpdate")) {
                    // 新增商品
                    if ((boolean)result.get("result")) {
                        addOkCnt++;
                    } else {
                        addNgCnt++;
                    }
                } else {
                    // 更新商品
                    if ((boolean)result.get("result")) {
                        updOkCnt++;
                    } else {
                        updNgCnt++;
                    }
                }
            }
            OrderChannelBean channel = Channels.getChannel(channelId);
            String strResult = String.format("%s %s%s上新结果: [总件数:%s 新增(成功:%s 失败:%s) 更新(成功:%s 失败:%s)]",
                    channelId, String.format("%1$-15s", channel != null ? channel.getFull_name() : "未知店铺名"),
                    UPLOAD_NAME, totalCnt, addOkCnt, addNgCnt, updOkCnt, updNgCnt);
            $info(strResult);
        }
        resultMap.clear();
        $info("=================" + UPLOAD_NAME + "上新  主线程结束 [总耗时:" + (System.currentTimeMillis() - totalStartTime) + "]====================");
    }

    /**
     * 平台级别上新处理
     *
     * @param channelId  渠道ID
     * @param cartId     平台ID
     */
    public void doUploadChannel(String channelId, int cartId)  {
        // 所有渠道Liking上新开始时间
        long channelStartTime = System.currentTimeMillis();
        $info("当前渠道的%s上新任务开始！[channelId:%s] [cartId:%s]", UPLOAD_NAME, channelId, CART_ID_CNN);

        try{
            // 获取店铺信息
            ShopBean shop = Shops.getShop(channelId, cartId);
            if (shop == null) {
                $error("获取到店铺信息失败(shopProp == null)! [ChannelId:%s] [CartId:%s]", channelId, cartId);
                return;
            }

            // 从上新的任务表中获取该平台及渠道需要上新的任务列表(group by channel_id, cart_id, group_id)
            // 重新的groupId会被group之后取更新时间最晚的取出一条groupId出来，回写回写的时候modified < 现在取出来的最晚的更新时间，所有这一批groupId会一起更新掉
            List<CmsBtSxWorkloadModel> sxWorkloadModels = platformProductUploadService.getSxWorkloadWithChannelIdCartId(rowCount, channelId, cartId);
            if (ListUtils.isNull(sxWorkloadModels)) {
                $error("上新任务表中没有该渠道和平台对应的任务列表信息！[ChannelId:%s] [CartId:%s]", channelId, cartId);
                return;
            }

            if (ListUtils.isNull(sxWorkloadModels)) {
                $info("上新任务表中没有该平台(cartId:%d)对应的任务列表信息！", cartId);
                return;
            }
            $info("从上新任务表中共读取共读取[%d]条%s任务！[channelId:%s] [cartId:%s]", sxWorkloadModels.size(), UPLOAD_NAME, channelId, cartId);

            // 创建线程池
            ExecutorService executor = Executors.newFixedThreadPool(threadCount);
            // 根据上新任务列表中的groupId循环上新处理
            for(CmsBtSxWorkloadModel cmsBtSxWorkloadModel : sxWorkloadModels) {
                // 启动多线程
                executor.execute(() -> doUploadProduct(shop, cmsBtSxWorkloadModel));
            }
            // ExecutorService停止接受任何新的任务且等待已经提交的任务执行完成(已经提交的任务会分两类：一类是已经在执行的，另一类是还没有开始执行的)，
            // 当所有已经提交的任务执行完毕后将会关闭ExecutorService
            executor.shutdown(); // 并不是终止线程的运行，而是禁止在这个Executor中添加新的任务
            try {
                // 阻塞，直到线程池里所有任务结束
                executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }

            $info("当前渠道的%s上新任务执行完毕！[channelId:%s] [cartId:%s] [上新对象group件数:%s] [当前渠道上新总耗时:%s]",
                    UPLOAD_NAME, channelId, cartId, sxWorkloadModels.size(), (System.currentTimeMillis() - channelStartTime));

        } catch (Exception e) {
            $info("当前渠道的%s上新任务执行失败！[channelId:%s] [cartId:%s] [errMsg:%s] [当前渠道上新总耗时:%s]",
                    UPLOAD_NAME, channelId, cartId, e.getMessage(), (System.currentTimeMillis() - channelStartTime));
            return;
        }

    }

    /**
     * 商品级别上新处理
     *
     * @param shop  店铺
     * @param cmsBtSxWorkloadModel     WorkLoad信息
     */
    public void doUploadProduct(ShopBean shop, CmsBtSxWorkloadModel cmsBtSxWorkloadModel)  {

        // 渠道
        String channelId = cmsBtSxWorkloadModel.getChannelId();
        // 平台id
        int cartId = cmsBtSxWorkloadModel.getCartId();
        // 商品groupId
        Long groupId = cmsBtSxWorkloadModel.getGroupId();
        // 商品id
        long cnnWareId = 0;
        // 上新数据
        SxData sxData = null;
        // 是否是更新商品(true:更新商品，false:新增商品)
        boolean isUpdate = false;
        // 新增/更新商品类型
        String updateType = "新增商品";
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
                throw new BusinessException(sxData.getErrorMessage());
            }

            // 主产品等列表取得
            CmsBtProductModel mainProduct = sxData.getMainProduct();
            List<CmsBtProductModel> cmsBtProductList = sxData.getProductList();
            List<BaseMongoMap<String, Object>> skuList = sxData.getSkuList();

            // 用于删除cms_bt_sx_cn_sku表中的库存同步的sku
            List<String> listDelCodes = new ArrayList<>(); // 删除的code列表

            // 没有lock并且已Approved的产品列表为空的时候,中止该产品的上新流程
            if (ListUtils.isNull(cmsBtProductList)) {
                throw new BusinessException("未被锁定,已完成审批且品牌不在黑名单的产品列表为空！");
            }
            // 主产品取得结果判断
            if (mainProduct == null) {
                throw new BusinessException("取得主商品信息失败！");
            }
            // 如果产品没有common信息，数据异常不上新
            if (mainProduct.getCommon() == null || mainProduct.getCommon().getFields() == null) {
                throw new BusinessException("取得主商品common信息失败！");
            }

            // 取得该产品所有skuCode列表,用于后面取得sku级别的库存信息
            List<String> strSkuCodeList = new ArrayList<>();
            skuList.forEach(sku -> strSkuCodeList.add(sku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.skuCode.name())));
            // 如果已Approved产品skuList为空，则中止该产品的上新流程；否则会把库存表里面所有的数据（几万条）数据全部查出来了，很花时间
            if (strSkuCodeList.isEmpty()) {
                throw new BusinessException("已完成审批的产品sku列表为空！");
            }
            // 获取字典表(根据channel_id)上传图片的规格等信息
            List<CmsMtPlatformDictModel> cmsMtPlatformDictModelList = dictService.getModesByChannelCartId(channelId, cartId);
            if (cmsMtPlatformDictModelList == null || cmsMtPlatformDictModelList.size() == 0) {
                throw new BusinessException(String.format("获取字典表数据（图片规格及详情页等）失败 [ChannelId:%s] [CartId:%s]", channelId, cartId));
            }

            // 如果skuList不为空，取得所有sku的库存信息
            // 为了对应MiniMall的场合， 获取库存的时候要求用getOrgChannelId()（其他的场合仍然是用channelId即可）
            // WMS2.0切换 20170526 charis STA
            // 上新对象code
            List<String> listSxCode = null;
            if (ListUtils.notNull(sxData.getProductList())) {
                listSxCode = sxData.getProductList().stream().map(p -> p.getCommonNotNull().getFieldsNotNull().getCode()).collect(Collectors.toList());
            }
            // 库存取得逻辑变为直接用cms的库存
            Map<String, Integer> skuLogicQtyMap = sxProductService.getSaleQuantity(mainProduct.getPlatform(cartId).getSkus());
//            Map<String, Integer> skuLogicQtyMap = new HashMap<>();
//            for (String code : listSxCode) {
//                try {
//                    Map<String, Integer> map = sxProductService.getAvailQuantity(channelId, String.valueOf(cartId), code, null);
//                    for (Map.Entry<String, Integer> e : map.entrySet()) {
//                        skuLogicQtyMap.put(e.getKey(), e.getValue());
//                    }
//                } catch (Exception e) {
//                    String errorMsg = String.format("获取可售库存时发生异常 [channelId:%s] [cartId:%s] [code:%s] [errorMsg:%s]",
//                            channelId, cartId, code, e.getMessage());
//                    throw new Exception(errorMsg);
//                }
//            }
            // WMS2.0切换 20170526 charis END

            // 计算该商品下所有产品所有SKU的逻辑库存之和，新增时如果所有库存为0，报出不能上新错误
            int totalSkusLogicQty = 0;
            for(String skuCode : skuLogicQtyMap.keySet()) {
                totalSkusLogicQty += skuLogicQtyMap.get(skuCode);
            }

            // 判断新增商品还是更新商品 (只要productGroup表中的numIId不为空，则为更新商品)
            if (!StringUtils.isEmpty(sxData.getPlatform().getNumIId())) {
                // 更新商品
                isUpdate = true;
                // 取得更新对象商品id
                cnnWareId = Long.parseLong(sxData.getPlatform().getNumIId());
                updateType = "更新商品";
            }

            // 全部去掉重复之后的颜色和尺寸别名值列表
            Map<String, String> colorMap = new LinkedHashMap<>();
            List<String> sizeList = new ArrayList<>();

            // 设置应用级参数-商品共通级属性
            Map<String, Object> paramCommonFields = getCommonFields(shop, sxData);
            // 设置应用级参数-产品级扩展属性
            Map<String, Object> paramCustomFields = getCustomFields(sxData);
            // 设置应用级参数-该商品的sku列表
            List<Map<String, Object>> paramSkuList = getSkuList(sxData, skuLogicQtyMap, colorMap, sizeList);
            // 设置应用级参数-该商品的sku列表
            List<Map<String, Object>> paramOptionsList = getOptionsList(colorMap, sizeList);

            String result = null;
            if (!isUpdate) {
                // 新增商品时

                // 没有库存时不能上新，如果所有产品所有SKU的库存之和为0时，直接报错
//                if (totalSkusLogicQty == 0) {
//                    throw new BusinessException(String.format("新增商品时所有SKU的总库存为0，不能上新！请添加库存信息之后再做上新. " +
//                            "[ChannelId:%s] [CartId:%s] [GroupId:%s]", channelId, cartId, groupId));
//                }

                // 新增商品到平台
                result = cnnWareService.addProduct(shop, paramCommonFields, paramCustomFields, paramSkuList, paramOptionsList);
            } else {
                // 更新商品时

                // 更新商品到平台
                result = cnnWareService.updateProduct(shop, cnnWareId, paramCommonFields, paramCustomFields, paramSkuList, paramOptionsList);
            }
            $info(String.format("%s调用%s接口API结束！[ChannelId:%s] [CartId:%s] [GroupId:%s] [结果:%s] ",
                    UPLOAD_NAME, updateType, channelId, cartId, groupId, result));

            if (StringUtils.isEmpty(result)) {
                throw new BusinessException(String.format("%s%s失败！调用接口返回值为空! [groupId:%s]", UPLOAD_NAME, updateType, groupId));
            } else {
                Map<String, Object> responseMap = JacksonUtil.jsonToMap(result);
                if (responseMap != null && responseMap.containsKey("code") && responseMap.get("code") != null) {
                    if (CnnConstants.C_CNN_RETURN_SUCCESS_0 == (int)responseMap.get("code")) {
                        // 新增/更新商品成功
                        if (!isUpdate) {
                            // 新增时，取得返回的numIId
                            cnnWareId = NumberUtils.toLong(StringUtils.toString(responseMap.get("numIId")));
                        }
                    } else {
                        // 新增/更新商品失败
                        String errMsg = String.format("%s调用接口API%s失败! [groupId:%s] [errMsg=%s]", UPLOAD_NAME, updateType, groupId, responseMap.get("msg"));
                        throw new BusinessException(errMsg);
                    }
                }
            }

            // 执行商品上架/下架操作(如果SKU总库存为0，直接下架)
            CmsConstants.PlatformStatus platformStatus = null;
            if (totalSkusLogicQty == 0) {
                // 总库存为0,强制下架并回写下架状态，记录上下架履历
                // 上新对象产品Code列表
                List<String> sxCodeList = sxData.getProductList().stream().map(p -> p.getCommonNotNull().getFieldsNotNull().getCode()).collect(Collectors.toList());
                // 强制下架
                boolean blnListingResult = doCnnForceWareListing(shop, cnnWareId, groupId, CmsConstants.PlatformActive.ToInStock, sxCodeList, UPLOAD_NAME + "上新SKU总库存为0时强制下架处理");
                // 下架之后删除cms_bt_sx_cn_sku表中库存同步用sku
                if (blnListingResult) {
                    listDelCodes.addAll(cmsBtProductList.stream().map(p -> p.getCommonNotNull().getFieldsNotNull().getCode()).collect(Collectors.toList()));
                }
            } else {
                // 如果SKU总库存不为0时，根据group表里设置的Action设置上下架状态
                boolean updateCnnWareListing = doCnnWareListing(shop, sxData, cnnWareId);
                // 新增或更新商品，只有在商品上架/下架操作成功之后才回写platformStatus，失败不回写状态(新增商品时除外)
                if (updateCnnWareListing) {
                    // 上架/下架操作成功时
                    // platformActive平台上新状态类型(ToOnSale/ToInStock)
                    if (CmsConstants.PlatformActive.ToOnSale.equals(sxData.getPlatform().getPlatformActive())) {
                        // platformActive是(ToOnSale)时，把platformStatus更新成"OnSale"
                        platformStatus = CmsConstants.PlatformStatus.OnSale;
                        // 上架成功之后，更新cms_bt_sx_cn_sku表，用于刷全量库存
                        updateSxCnnSku(channelId, cartId, sxData);
                    } else {
                        platformStatus = CmsConstants.PlatformStatus.InStock;
                        // 下架之后删除cms_bt_sx_cn_sku表中库存同步用sku
                        listDelCodes.addAll(cmsBtProductList.stream().map(p -> p.getCommonNotNull().getFieldsNotNull().getCode()).collect(Collectors.toList()));
                    }
                } else {
                    // 商品上架/下架失败的时候，新增商品时一律回写成"InStock"(默认),更新商品时不回写状态
                    if (!isUpdate) {
                        // 新增商品之后商品上架/下架失败时，把platformStatus更新成"InStock"(默认)
                        platformStatus = CmsConstants.PlatformStatus.InStock;
                    }
                }
            }

            // 如果有下架后需要删除的库存同步用sku
            if (ListUtils.notNull(listDelCodes)) {
                deleteSxCnSku(channelId, listDelCodes);
            }

            // 上新成功时状态回写操作
            sxProductService.doUploadFinalProc(shop, true, sxData, cmsBtSxWorkloadModel, String.valueOf(cnnWareId), platformStatus, "", getTaskName());

            // 把上新成功状态放入结果map中
            add2ResultMap(channelId, cartId, groupId, isUpdate, true);

            $info(String.format("%s单个商品%s成功！[ChannelId:%s] [CartId:%s] [GroupId:%s] [WareId:%s] [耗时:%s]",
                    UPLOAD_NAME, isUpdate ? "更新" : "上新", channelId, cartId, groupId, cnnWareId, (System.currentTimeMillis() - prodStartTime)));

        } catch (Exception ex) {

            // 异常结束时
            // 把上新失败结果加入到resultMap中
            add2ResultMap(channelId, cartId, groupId, isUpdate, false);

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
                    sxData.setErrorMessage("出现nullpoint不可预知的错误，请跟管理员联系! " + ex.getStackTrace()[0].toString());
                    ex.printStackTrace();
                } else {
                    sxData.setErrorMessage(ex.getMessage());
                }
            }

            if (sxData.getErrorMessage().contains(shop.getShop_name())) {
                sxData.setErrorMessage(sxData.getErrorMessage().replace(shop.getShop_name(), getPreMsg(shop.getShop_name(), "")));
            } else {
                sxData.setErrorMessage(getPreMsg(shop.getShop_name(), "") + sxData.getErrorMessage());
            }

            $error(sxData.getErrorMessage());

            // 上新出错时状态回写操作
            sxProductService.doUploadFinalProc(shop, false, sxData, cmsBtSxWorkloadModel, "", null, "", getTaskName());

            $error(String.format("%s单个商品%s异常结束！[ChannelId:%s] [CartId:%s] [GroupId:%s] [WareId:%s] [errMsg:%s] [耗时:%s]",
                    UPLOAD_NAME, isUpdate ? "更新" : "上新", channelId, cartId, groupId, cnnWareId, Arrays.toString(ex.getStackTrace())), (System.currentTimeMillis() - prodStartTime));
            return;
        }
    }

    /**
     * 设置应用级参数-商品共通级属性
     *
     * @param shop 店铺
     * @param sxData 上新数据
     * @return Map<String, Object> 返回应用级参数-商品共通级属性
     */
    protected Map<String, Object> getCommonFields(ShopBean shop, SxData sxData) {
        // 设置应用级参数-商品共通级属性
        Map<String, Object> paramCommonFields = new HashMap<>();

        // 主产品等列表取得
        CmsBtProductModel mainProduct = sxData.getMainProduct();
        CmsBtProductModel_Common mainProdComm = mainProduct.getCommonNotNull();
        CmsBtProductModel_Field mainProdCommField = mainProdComm.getFieldsNotNull();
        CmsBtProductModel_Platform_Cart mainProdPlatCart = mainProduct.getPlatformNotNull(sxData.getCartId());

        ExpressionParser expressionParser = new ExpressionParser(sxProductService, sxData);

        // 店铺内分类ID(注意是cms端的分类ID) 该商品属于多个分类时，分类ID之间用","分隔
        String categoryId = null;
        List<String> sellerCatIds = new ArrayList<>();
        if (mainProdPlatCart != null && ListUtils.notNull(mainProdPlatCart.getSellerCats())) {
            // 把多个店铺内分子的父子ID用都好连接起来
            List<CmsBtProductModel_SellerCat> sellerCatList = mainProdPlatCart.getSellerCats();
            sellerCatList.forEach(p -> {
                if (ListUtils.notNull(p.getcIds())) {
                    sellerCatIds.addAll(p.getcIds());
                }
            });
            if (ListUtils.notNull(sellerCatList)) {
                categoryId = Joiner.on(Separtor_Coma).join(sellerCatIds);
            }
        }
        if (!StringUtils.isEmpty(categoryId)) paramCommonFields.put("categoryId", categoryId);

        // 商品渠道ID
        paramCommonFields.put("channelId", sxData.getChannelId());
        // 款号
        paramCommonFields.put("model", mainProdCommField.getModel());
        // 品牌名/制造商名
        paramCommonFields.put("brand", mainProdCommField.getBrand());
        // 产品名称(中文) (对应于cms中的originalTitleCn/productNameEn)
        if (mainProdPlatCart != null && !StringUtils.isEmpty(mainProdPlatCart.getFieldsNotNull().getStringAttribute("productTitle"))) {
            paramCommonFields.put("title", mainProdPlatCart.getFieldsNotNull().getStringAttribute("productTitle"));
        } else if (!StringUtils.isEmpty(mainProdCommField.getOriginalTitleCn())) {
            paramCommonFields.put("title", mainProdCommField.getOriginalTitleCn());
        } else {
            paramCommonFields.put("title", mainProdCommField.getProductNameEn());
        }
        // 简短描述(中文)
        String shortDesc = org.apache.commons.lang3.StringUtils.trimToNull(mainProdCommField.getShortDesCn());
        if (shortDesc == null) {
            shortDesc = org.apache.commons.lang3.StringUtils.trimToNull(mainProdCommField.getShortDesEn());
            if (shortDesc == null) {
                paramCommonFields.put("shortDesc", "");
            } else {
                paramCommonFields.put("shortDesc", shortDesc.length() > 1000 ? shortDesc.substring(0, 1000) : shortDesc);
            }
        } else {
            paramCommonFields.put("shortDesc", shortDesc.length() > 1000 ? shortDesc.substring(0, 1000) : shortDesc);

        }
        // 详情描述(中文)
        if (!StringUtils.isEmpty(mainProdCommField.getLongDesCn())) {
            paramCommonFields.put("longDesc", mainProdCommField.getLongDesCn());
        } else {
            paramCommonFields.put("longDesc", mainProdCommField.getLongDesEn());
        }
        // 材质(中文)
        String material = org.apache.commons.lang3.StringUtils.trimToNull(mainProdCommField.getMaterialCn());
        if (material == null) {
            material = org.apache.commons.lang3.StringUtils.trimToNull(mainProdCommField.getMaterialEn());
            if (material == null) {
                paramCommonFields.put("material", "");
            } else {
                paramCommonFields.put("material", material.length() > 1000 ? material.substring(0, 1000) : material);
            }
        } else {
            paramCommonFields.put("material", material.length() > 1000 ? material.substring(0, 1000) : material);
        }
        // 商品特质(颜色/口味/香型等)(中文) (对应于cms中的color/codeDiff)
        if (!StringUtils.isEmpty(mainProdCommField.getColor())) {
            paramCommonFields.put("feature", mainProdCommField.getColor());
        } else {
            paramCommonFields.put("feature", mainProdCommField.getCodeDiff());
        }
        // 产地
        paramCommonFields.put("origin", mainProdCommField.getOrigin());
        // 产品分类
        if (!StringUtils.isEmpty(mainProdCommField.getProductTypeCn())) {
            paramCommonFields.put("productType", mainProdCommField.getProductTypeCn());
        } else {
            paramCommonFields.put("productType", mainProdCommField.getProductType());
        }
        // 适用人群
        if (!StringUtils.isEmpty(mainProdCommField.getSizeTypeCn())) {
            paramCommonFields.put("sizeType", mainProdCommField.getSizeTypeCn());
        } else {
            paramCommonFields.put("sizeType", mainProdCommField.getSizeType());
        }
        // 使用说明(中文)
        if (!StringUtils.isEmpty(mainProdCommField.getUsageCn())) {
            paramCommonFields.put("usage", mainProdCommField.getUsageCn());
        } else {
            paramCommonFields.put("usage", mainProdCommField.getUsageEn());
        }

        if (ListUtils.isNull(mainProdCommField.getImages1())
                || StringUtils.isEmpty((mainProdCommField.getImages1().get(0).getName()))) {
            throw new BusinessException(String.format("主产品的images1图片为空，请追加图片信息之后再上新！[code:%s]",
                    mainProdCommField.getCode()));
        }
        // 商品主图(images1里的第一张图的名字)
        paramCommonFields.put("mainImage", mainProdCommField.getImages1().get(0).getName());
        // 产品图列表(images1里的所有图片)
        String[] images = mainProdCommField.getImages1().stream().map(p -> p.getName()).toArray(String[]::new);
        paramCommonFields.put("images", images);

        // PC端产品页详情内容（直接是html脚本）
        // 根据字典
        String strNotes = "";
        try {
            // 取得描述
//            RuleExpression ruleDetails = new RuleExpression();
//            MasterWord masterWord = new MasterWord("details");
//            ruleDetails.addRuleWord(masterWord);
//            String details = expressionParser.parse(ruleDetails, shop, getTaskName(), null);
//            if (!StringUtils.isEmpty(details)) {
//                strNotes = sxProductService.resolveDict(details, expressionParser, shop, getTaskName(), null);
//                if (StringUtils.isEmpty(strNotes)) {
//                    throw new BusinessException(String.format("详情页描述[%s]在dict表里未设定!", details));
//                }
//            } else {
//                // 解析cms_mt_platform_dict表中配置的"新独立域名详情页描述"
//                strNotes = sxProductService.resolveDict(UPLOAD_NAME + "详情页描述", expressionParser, shop, getTaskName(), null);
//            }

            String strCnnDetailTemplateName = "新独立域名Liking详情页描述";
            if (mainProduct.getChannelId().equals("928")) {
                if (mainProduct.getPlatformNotNull(CartEnums.Cart.LTT.getValue()).getFieldsNotNull().containsKey("details")) {
                    String detailName = mainProduct.getPlatformNotNull(CartEnums.Cart.LTT.getValue()).getFieldsNotNull().getStringAttribute("details");

                    if (StringUtils.isEmpty(detailName)) detailName = "";
                    if (detailName.equals("天猫同购描述-重点商品")) {
                        strCnnDetailTemplateName = "新独立域名Liking详情页描述-重点商品";
                    } else if (detailName.equals("天猫同购描述-无属性图")) {
                        strCnnDetailTemplateName = "新独立域名Liking详情页描述-无属性图";
                    } else if (detailName.equals("天猫同购描述-非重点之英文长描述")) {
                        strCnnDetailTemplateName = "新独立域名Liking详情页描述-非重点之英文长描述";
                    } else if (detailName.equals("天猫同购描述-非重点之中文长描述")) {
                        strCnnDetailTemplateName = "新独立域名Liking详情页描述-非重点之中文长描述";
                    } else if (detailName.equals("天猫同购描述-非重点之中文使用说明")) {
                        strCnnDetailTemplateName = "新独立域名Liking详情页描述-非重点之中文使用说明";
                    } else if (detailName.equals("天猫同购描述-爆款商品")) {
                        strCnnDetailTemplateName = "新独立域名Liking详情页描述-爆款商品";
                    }
                }
            }
            strNotes = sxProductService.resolveDict(strCnnDetailTemplateName, expressionParser, shop, getTaskName(), null);

        } catch (Exception ex) {
            throw new BusinessException(String.format("%s取得详情页描述信息失败！[errMsg:%s]", UPLOAD_NAME, ex.getMessage()));
        }
        paramCommonFields.put("pageDetailPC", strNotes);
        // 移动端产品页详情内容（直接是html脚本）
        paramCommonFields.put("pageDetailM", "pageDetailM");   // TODO
        // searchKey(不设置)
//        paramCommonFields.put("searchKey", "");

        return paramCommonFields;
    }

    /**
     * 设置应用级参数-产品级扩展属性
     *
     * @param sxData 上新数据
     * @return Map<String, Object> 返回应用级参数-产品级扩展属性
     */
    protected Map<String, Object> getCustomFields(SxData sxData) {

        // 设置应用级参数-产品级扩展属性
//        Map<String, Object> paramCustomFields = new HashMap<>();

        return null;
    }

    /**
     * 设置应用级参数-该商品的sku列表
     *
     * @param sxData 上新数据
     * @param skuLogicQtyMap sku级别库存信息
     * @param colorMap 用于保存所有去掉重复的颜色(颜色key:图片名value)(后面getOptionsList方法会用到)
     * @param sizeList  用于保存所有去掉重复的尺寸(后面getOptionsList方法会用到)
     * @return Map<String, Object> 返回应用级参数-该商品的sku列表
     */
    protected List<Map<String, Object>> getSkuList(SxData sxData, Map<String, Integer> skuLogicQtyMap, Map<String, String> colorMap, List<String> sizeList) {
        // 设置应用级参数-该商品的sku列表
        List<Map<String, Object>> paramSkuList = new ArrayList<>();

        String channelId = sxData.getChannelId();
        String cartId = StringUtils.toString(sxData.getCartId());

        List<CmsBtProductModel> cmsBtProductList = sxData.getProductList();

        // SKU列表取得
        List<BaseMongoMap<String, Object>> cmsSkuList = sxData.getSkuList();

        // 取得Redis中缓存cms_mt_channel_config配置表中配置的颜色别名的字段名(KEY:29.color_alias))
        String colorAliasItemName = sxProductService.getRedisChannelConfigValue(channelId, CmsConstants.ChannelConfig.ALIAS,
                cartId + CmsConstants.ChannelConfig.COLOR_ALIAS);

        // 根据product列表循环设置该商品的SKU属性
        for (CmsBtProductModel objProduct : cmsBtProductList) {
            // 颜色别名(有可能是color,也可能是code)
            String colorAlias = null;
            if ("color".equalsIgnoreCase(colorAliasItemName)) {
                colorAlias = objProduct.getCommonNotNull().getFieldsNotNull().getColor();
            }
            if (StringUtils.isEmpty(colorAlias)) {
                colorAlias = objProduct.getCommonNotNull().getFieldsNotNull().getCode();
            }

            if (ListUtils.isNull(objProduct.getCommonNotNull().getFieldsNotNull().getImages1())
                    || StringUtils.isEmpty((objProduct.getCommonNotNull().getFieldsNotNull().getImages1().get(0).getName()))) {
                throw new BusinessException(String.format("当前产品的images1图片为空，请追加图片信息之后再上新！[code:%s]",
                        objProduct.getCommonNotNull().getFieldsNotNull().getCode()));
            }
            // 保存每种颜色对应一张图片信息
            colorMap.put(colorAlias, objProduct.getCommonNotNull().getFieldsNotNull().getImages1().get(0).getName());

            CmsBtProductModel_Platform_Cart objPlatformCart = objProduct.getPlatform(sxData.getCartId());
            if (objPlatformCart == null || ListUtils.isNull(objPlatformCart.getSkus())) continue;
            List<BaseMongoMap<String, Object>> objPlatformSkus = objPlatformCart.getSkus();
            for (BaseMongoMap<String, Object> objSku : objPlatformSkus) {
                // skuCode
                String objSkuCode = objSku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.skuCode.name());
                // 如果没有找到对应skuCode，则继续循环
                if (cmsSkuList.stream().filter(sku -> objSkuCode.equals(sku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.skuCode.name()))).count() == 0) {
                    continue;
                }

                // 取得上新用SKU，设置SKU信息
                BaseMongoMap<String, Object> sxSku = cmsSkuList.stream().filter(sku -> objSkuCode.equals(sku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.skuCode.name()))).findFirst().get();
                Map<String, Object> currSku = new HashMap<>();
                // sku编号(必须)
                currSku.put("skuCode", objSkuCode);
                // 产品编号(对应于cms中的product code)(必须)
                currSku.put("prodCode", objProduct.getCommonNotNull().getFieldsNotNull().getCode());
                // sku名称，可以不设值，页面显示时直接使用'产品名称'   (不设置)
//            currSku.put("name", "");
                // 库存数(必须)
                currSku.put("inventory", skuLogicQtyMap.get(objSkuCode));
                // 指导价,单位:元(必须)
                Double retailPrice = sxProductService.getSkuPrice(objSku, channelId, cartId,
                        CmsConstants.ChannelConfig.PRICE_RETAIL_KEY, CmsConstants.ChannelConfig.PRICE_RETAIL_PRICE_CODE);
                currSku.put("retailPrice", retailPrice);
                // 最终销售价,单位:元(必须)
                Double salePrice = sxProductService.getSkuPrice(objSku, channelId, cartId,
                        CmsConstants.ChannelConfig.PRICE_SALE_KEY, CmsConstants.ChannelConfig.PRICE_SALE_PRICE_CODE);
                currSku.put("salePrice", salePrice);
                // 税额(任意)  (不设置)
//            currSku.put("tax", tax);
                // sku区分选项(键值对形式)，根据其值可以唯一确定商品里的sku
                // 例如：{ 'color':'黑色', 'size':'L' }
                // 其中的键名和值的设置请参照"应用级参数-optionItem"
                // 只有单个sku的情形时，不需要此项，其属性设置在"skuFields"中
                Map<String, String> skuSalePropMap = new HashMap<>();
                skuSalePropMap.put("color", colorAlias);
                String sizeSx = sxSku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.sizeSx.name());
                skuSalePropMap.put("size", sizeSx);
                currSku.put("skuOptions", skuSalePropMap);
                // 追加尺码信息
                if (!sizeList.contains(sizeSx)) sizeList.add(sizeSx);
                // sku个别扩展属性(键值对形式) (任意)  (不设置)
//            currSku.put("skuFields", skuFieldsMap);

                paramSkuList.add(currSku);
            }
        }

        return paramSkuList;
    }

    /**
     * 设置应用级参数-sku区分选项名称及值的一览
     *
     * @param colorMap 所有去掉重复的颜色(颜色key:图片名value)
     * @param sizeList  所有去掉重复的尺寸
     * @return List<Map<String, Object>> 返回应用级参数-sku区分选项名称及值的一览
     */
    protected List<Map<String, Object>> getOptionsList(Map<String, String> colorMap, List<String> sizeList) {

        if (MapUtils.isEmpty(colorMap) && ListUtils.isNull(sizeList)) return null;

        // 设置应用级参数-该商品的sku列表
        List<Map<String, Object>> paramOptionsList = new ArrayList<>();

        int order = 1;

        // 设置颜色属性值
        if (MapUtils.isNotEmpty(colorMap)) {
            Map<String, Object> optionItemColor = new HashMap<>();
            optionItemColor.put("key", "color");
            optionItemColor.put("name", "颜色");
            optionItemColor.put("order", order++);
            // 图片名称(图片保存在单独的图片服务器上)
            // 该值现在只有在键名是'color'的情况下有效，商品颜色用一个图片（小图）表示
            // 该值与"valueList"中的项目、按顺序一一对应
            // 若键名是'color'，而该值没有设置，则直接显示该颜色名称，而不是显示色块
            // 图片名称(例：{ "basketball-shoes342222002-1", "342222-061-1", "air-jordan-phly-legend-basketball-shoes342222101-1"})
            String[] picUrlArray = colorMap.values().stream().toArray(String[]::new);
            optionItemColor.put("urlList", picUrlArray);
            // 颜色数组(例：{ "white", "blue"})
            String[] colorValueArray = colorMap.keySet().stream().toArray(String[]::new);
            optionItemColor.put("valueList", colorValueArray);
            paramOptionsList.add(optionItemColor);
        }

        // 设置尺寸属性值
        if (ListUtils.notNull(sizeList)) {
            Map<String, Object> optionItemSize = new HashMap<>();
            optionItemSize.put("key", "size");
            optionItemSize.put("name", "尺寸");
            optionItemSize.put("order", order++);
            // 尺寸数组(例：{ "4", "8", "16"})
            String[] sizeValueArray = sizeList.stream().toArray(String[]::new);
            optionItemSize.put("valueList", sizeValueArray);
            paramOptionsList.add(optionItemSize);
        }

        return paramOptionsList;
    }

    /**
     * 商品强制上架/下架并回写状态记录历史处理
     *
     * @param shop ShopBean 店铺对象
     * @param wareId long 商品id
     */
    protected boolean doCnnForceWareListing(ShopBean shop, Long wareId, Long groupId, CmsConstants.PlatformActive platformActive, List<String> codeList, String modifier) {
        if (shop == null || wareId == null || platformActive == null) return false;

        // 商品上架/下架结果
        boolean updateListingResult = false;
        String errMsg = null;

        try {
            String result = null;
            // platformActive平台上新状态类型(ToOnSale/ToInStock)
            if (CmsConstants.PlatformActive.ToOnSale.name().equals(platformActive.name())) {
                // platformActive是(ToOnSale)时，执行商品上架操作
                result = cnnWareService.doWareUpdateListing(shop, wareId);
            } else if (CmsConstants.PlatformActive.ToInStock.name().equals(platformActive.name())) {
                // platformActive是(ToInStock)时，执行商品下架操作
                result = cnnWareService.doWareUpdateDelisting(shop, wareId);
            } else {
                return false;
            }
            $info(String.format("调用%s平台上下架API [channelId=%s] [cartId=%s] [numIId=%s] [platformActive:%s] [结果=%s]",
                    UPLOAD_NAME, shop.getOrder_channel_id(), shop.getCart_id(), wareId, platformActive.name(), result));
            if (!StringUtils.isEmpty(result)) {
                Map<String, Object> responseMap = JacksonUtil.jsonToMap(result);
                if (responseMap != null && responseMap.containsKey("code") && responseMap.get("code") != null) {
                    if (CnnConstants.C_CNN_RETURN_SUCCESS_0 == (int) responseMap.get("code")) {
                        updateListingResult = true;
                    } else {
                        errMsg = String.format("调用%s平台上下架失败! [platformActive:%s] [errMsg=%s]", UPLOAD_NAME, platformActive.name(), responseMap.get("msg"));
                        $error(errMsg);
                    }
                }
            }

        } catch (Exception e) {
            errMsg = String.format("商品强制上/下架处理发生异常! [wareId:%s] [groupId:%s] [platformActive:%s] [errMsg:%s]",
                    wareId, groupId, platformActive.name(), e.getMessage());
            $error(errMsg);
        }

        if (updateListingResult) {
            // 回写上下架状态到product和productGroup表，并插入mongoDB上下架履历表cms_bt_platform_active_log_cXXX
            sxProductService.updateListingStatus(shop.getOrder_channel_id(), shop.getCart_id(), groupId, codeList, platformActive, updateListingResult, errMsg, modifier);
        }

        return updateListingResult;
    }

    /**
     * 商品上架/下架处理
     *
     * @param shop ShopBean 店铺对象
     * @param sxData SxData 上新数据
     * @param wareId long 商品id
     */
    protected boolean doCnnWareListing(ShopBean shop, SxData sxData, long wareId) {

        // 商品上架/下架结果
        boolean updateListingResult = false;
        String errMsg = null;

        try {
            String result = null;
            // platformActive平台上新状态类型(ToOnSale/ToInStock)
            if (CmsConstants.PlatformActive.ToOnSale.equals(sxData.getPlatform().getPlatformActive())) {
                // platformActive是(ToOnSale)时，执行商品上架操作
                result = cnnWareService.doWareUpdateListing(shop, wareId);
            } else {
                // platformActive是(ToInStock)时，执行商品下架操作
                result = cnnWareService.doWareUpdateDelisting(shop, wareId);
            }
            $info(String.format("调用%s平台上下架API [channelId=%s] [cartId=%s] [numIId=%s] [platformActive:%s] [结果=%s]",
                    UPLOAD_NAME, shop.getOrder_channel_id(), shop.getCart_id(), wareId, sxData.getPlatform().getPlatformActive().name(), result));
            if (!StringUtils.isEmpty(result)) {
                Map<String, Object> responseMap = JacksonUtil.jsonToMap(result);
                if (responseMap != null && responseMap.containsKey("code") && responseMap.get("code") != null) {
                    if (CnnConstants.C_CNN_RETURN_SUCCESS_0 == (int)responseMap.get("code")) {
                        updateListingResult = true;
                    } else {
                        errMsg = String.format("调用%s平台上下架失败! [platformActive:%s] [errMsg=%s]",
                                UPLOAD_NAME, sxData.getPlatform().getPlatformActive().name(), responseMap.get("msg"));
                        $error(errMsg);
                    }
                }
            }

        } catch (Exception e) {
            errMsg = String.format("商品强制上/下架处理发生异常! [wareId:%s] [platformActive:%s] [errMsg:%s]",
                    wareId, sxData.getPlatform().getPlatformActive().name(), e.getMessage());
            $error(errMsg);
        }

        return updateListingResult;
    }

    /**
     * 将上新状态添加到结果map里面
     *
     * @param channelId  渠道id
     * @param cartId     平台id
     * @param isUpdate   是否更新商品(true:更新商品, false:新增商品)
     * @param result     上新结果(true:成功, false:失败)
     */
    protected void add2ResultMap(String channelId, int cartId, Long groupId, boolean isUpdate, boolean result) {
        Map<String, Object> currResult = new HashMap<>();
        currResult.put("channelId", channelId);
        currResult.put("cartId", cartId);
        currResult.put("groupId", groupId);
        currResult.put("isUpdate", isUpdate);
        currResult.put("result", result);
        resultMap.put(channelId + "_" + groupId, currResult);
    }

    /**
     * 获得log头部信息
     *
     * @param shopName 店铺名称
     * @param sxType   上新类型
     * @return String  log头部信息
     */
    private String getPreMsg(String shopName, String sxType) {
        return StringUtils.isEmpty(sxType) ? (shopName + " ") : (shopName + "[" + sxType + "] ");
    }

    /**
     * 回写Liking官网SKU信息表(cms_bt_sx_cn_sku)
     *
     * @param channelId 渠道id
     * @param cartId 平台id
     * @param channelId 渠道id
     */
    protected int updateSxCnnSku(String channelId, int cartId, SxData sxData) {
        int cnt = 0;

        // 主产品等列表取得
        List<CmsBtProductModel> cmsBtProductList = sxData.getProductList();
        List<BaseMongoMap<String, Object>> skuList = sxData.getSkuList();

        for (CmsBtProductModel prodObj : cmsBtProductList) {
            CmsBtProductModel_Platform_Cart platform = prodObj.getPlatformNotNull(cartId);
            if (MapUtils.isEmpty(platform)) continue;

            // 产品code
            String code = prodObj.getCommonNotNull().getFieldsNotNull().getCode();

            List<BaseMongoMap<String, Object>> skus = platform.getSkus();
            for (BaseMongoMap<String, Object> sku : skus) {
                CmsBtSxCnSkuModel cnnSkuModel = new CmsBtSxCnSkuModel();
                cnnSkuModel.setChannelId(channelId);
                cnnSkuModel.setOrgChannelId(prodObj.getOrgChannelId());
                cnnSkuModel.setCode(code);
                cnnSkuModel.setSku(sku.getStringAttribute("skuCode"));
                String size = "未知commonSize";
                for (CmsBtProductModel_Sku commSkuObj : prodObj.getCommonNotNull().getSkus()) {
                    if (sku.getStringAttribute("skuCode").equals(commSkuObj.getSkuCode())) {
                        size = commSkuObj.getSize();
                    }
                }
                cnnSkuModel.setSize(size);
                String sizeSx = size;
                for (BaseMongoMap<String, Object> skuObj : skuList) {
                    if (sku.getStringAttribute("skuCode").equals(skuObj.getStringAttribute("skuCode"))) {
                        sizeSx = skuObj.getStringAttribute("sizeSx");
                    }
                }
                cnnSkuModel.setShowSize(sizeSx);
                String categoryIds = "未知categoryIds";
                if (!StringUtils.isEmpty(platform.getpCatId())) {
                    categoryIds = platform.getpCatId();
                }
                cnnSkuModel.setCategoryIds(categoryIds);
                cnnSkuModel.setCreater(getTaskName());
                cnnSkuModel.setCreated(DateTimeUtil.getDate());
                cnnSkuModel.setModifier(getTaskName());
                cnnSkuModel.setModified(DateTimeUtil.getDate());

                // 查询mySql表中的sku列表(一个产品查询一次，如果每个sku更新/新增的时候都去查的话，效率太低了)
                Map<String, String> query = new HashMap<>();
                query.put("channelId", channelId);
                query.put("code", code);
                query.put("sku", sku.getStringAttribute("skuCode"));
                CmsBtSxCnSkuModel searchModel = cmsBtSxCnSkuDao.selectOne(query);

                if (searchModel == null) {
                    // 不存在，新增
                    cnt += cmsBtSxCnSkuDao.insert(cnnSkuModel);
                } else {
                    // 存在，更新
                    cnnSkuModel.setId(searchModel.getId());
                    cnt += cmsBtSxCnSkuDao.update(cnnSkuModel);
                }
            }
            $info("%s回写库存同步用cms_bt_sx_cn_sku表数据，新增或更新了%d件!", UPLOAD_NAME, cnt);
        }

        return cnt;
    }

    /**
     * 删除库存同步用cms_bt_sx_cn_sku表
     */
    private int deleteSxCnSku(String channelId, List<String> listDelCodes) {
        int delCnt = cmsBtSxCnSkuDaoExt.deleteByListCodes(channelId, listDelCodes);
        $info("%s回写库存同步用cms_bt_sx_cn_sku表数据，删除了%d件,code列表:" + listDelCodes, UPLOAD_NAME, delCnt);
        return delCnt;
    }

}
