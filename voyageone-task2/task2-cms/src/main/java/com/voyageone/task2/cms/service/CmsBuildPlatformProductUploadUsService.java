package com.voyageone.task2.cms.service;

import com.taobao.api.domain.Shop;
import com.taobao.top.schema.field.MultiComplexField;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.CmsChannelConfigs;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.masterdate.schema.factory.SchemaReader;
import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.common.masterdate.schema.field.InputField;
import com.voyageone.common.masterdate.schema.field.MultiCheckField;
import com.voyageone.common.masterdate.schema.field.SingleCheckField;
import com.voyageone.common.masterdate.schema.value.Value;
import com.voyageone.common.util.CommonUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.cms.product.SxData;
import com.voyageone.service.impl.cms.PlatformCategoryService;
import com.voyageone.service.impl.cms.PlatformProductUploadService;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.sx.rule_parser.ExpressionParser;
import com.voyageone.service.impl.cms.vomq.CmsMqSenderService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.usa.CmsProductTransferToUsMQMessageBody;
import com.voyageone.service.model.cms.CmsBtUsWorkloadModel;
import com.voyageone.service.model.cms.TransferUsProductModel;
import com.voyageone.service.model.cms.TransferUsProductModel_Sku;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategorySchemaModel;
import com.voyageone.service.model.cms.mongo.product.*;
import com.voyageone.task2.base.BaseCronTaskService;
import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.base.util.TaskControlUtils;
import com.voyageone.task2.cms.model.ConditionPropValueModel;
import com.voyageone.task2.cms.service.putaway.ConditionPropValueRepo;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by Charis on 2017/7/18.
 */
@Service
public class CmsBuildPlatformProductUploadUsService extends BaseCronTaskService {

    @Override
    protected String getTaskName() {
        return "CmsBuildPlatformProductUploadUsJob";
    }

    @Override
    protected SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    private Map<String, Map<String, List<ConditionPropValueModel>>> channelConditionConfig;

    // 默认推送件数
    public static final String DEFAULT_TRANSFER_COUNT = "50";

    @Autowired
    private PlatformProductUploadService platformProductUploadService;

    @Autowired
    private ConditionPropValueRepo conditionPropValueRepo;

    @Autowired
    private SxProductService sxProductService;

    @Autowired
    private CmsMqSenderService cmsMqSenderService;

    @Autowired
    private PlatformCategoryService platformCategoryService;

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        doUploadMain(taskControlList);

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
        int threadCount = NumberUtils.toInt(TaskControlUtils.getVal1WithDefVal(taskControlList, TaskControlEnums.Name.thread_count, "5"));
        // 抽出件数(默认为50)
        int rowCount = NumberUtils.toInt(TaskControlUtils.getVal1WithDefVal(taskControlList, TaskControlEnums.Name.row_count, "500"));

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
                                    doProductUpload(channelId, CartEnums.Cart.SN.getValue(), threadCount, rowCount);
                                    doProductUpload(channelId, CartEnums.Cart.MSN.getValue(), threadCount, rowCount);
                                    doProductUpload(channelId, CartEnums.Cart.military.getValue(), threadCount, rowCount);
                                    doProductUpload(channelId, CartEnums.Cart.Xsneakers.getValue(), threadCount, rowCount);
                                    doProductUpload(channelId, CartEnums.Cart.iKicks.getValue(), threadCount, rowCount);
                                    doProductUpload(channelId, CartEnums.Cart.eBay.getValue(), threadCount, rowCount);
                                    doProductUpload(channelId, CartEnums.Cart.Amazon.getValue(), threadCount, rowCount);
                                    doProductUpload(channelId, CartEnums.Cart.SneakerRx.getValue(), threadCount, rowCount);
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

        // 获取店铺信息
        ShopBean shopProp = Shops.getShop(channelId, cartId);
        if (shopProp == null) {
            $error("获取到店铺信息失败(shopProp == null)! [ChannelId:%s] [CartId:%s]", channelId, cartId);
            return;
        }
        $info("获取店铺信息成功![ChannelId:%s] [CartId:%s]", channelId, cartId);

        Date publishTime = new Date();
        // 从上新的任务表中获取该平台及渠道需要上新的任务列表(group by channel_id, cart_id, publish_time)
        List<CmsBtUsWorkloadModel> sxWorkloadModels = platformProductUploadService.getListUsWorkload(rowCount, channelId, cartId, publishTime);
        if (ListUtils.isNull(sxWorkloadModels)) {
            $error("上新任务表中没有该渠道和平台对应的任务列表信息！[ChannelId:%s] [CartId:%s]", channelId, cartId);
            return;
        }
        // 读取channel_config表关于推送个数的配置
        String transferCount = sxProductService.getTransferCount(shopProp);
        if (StringUtils.isNullOrBlank2(transferCount)) {
            transferCount = DEFAULT_TRANSFER_COUNT;
        }

        // 创建线程池
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        for (List<CmsBtUsWorkloadModel> cmsBtUsWorkloadModels : CommonUtil.splitList(sxWorkloadModels, Integer.parseInt(transferCount))) {
            // 启动多线程
            executor.execute(() -> uploadProduct(cmsBtUsWorkloadModels, shopProp));
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

    public void uploadProduct(List<CmsBtUsWorkloadModel> cmsBtUsWorkloadModels, ShopBean shopBean) {

        List<TransferUsProductModel> productModels = new ArrayList<>();
        for (CmsBtUsWorkloadModel cmsBtUsWorkloadModel : cmsBtUsWorkloadModels) {
            // 渠道id
            String channelId = shopBean.getOrder_channel_id();
            // 平台id
            int cartId = Integer.parseInt(shopBean.getCart_id());
            // code
            String code = cmsBtUsWorkloadModel.getCode();
            // 上新数据
            SxData sxData = null;
            try {
                sxData = sxProductService.getSxProductDataByCode(channelId, cartId, code);
                if (sxData == null) {
                    throw new BusinessException("取得上新用的商品数据信息失败！请向管理员确认 [sxData=null]");
                }
                if (!StringUtils.isEmpty(sxData.getErrorMessage())) {
                    String errorMsg = sxData.getErrorMessage();
                    sxData.setCartId(cartId);
                    throw new BusinessException(errorMsg);
                }
                TransferUsProductModel productInfo = getProductInfoForUs(sxData, channelId, cartId, shopBean);

                productModels.add(productInfo);

                // 上新成功时状态回写操作
                sxProductService.doUploadFinalProcForUs(true, sxData, cmsBtUsWorkloadModel, getTaskName());
                // 回写ims_bt_product表
                sxProductService.updateImsBtProductForUs(sxData, getTaskName());
                // 回写workload表   (为了知道字段是哪个画面更新的，上新程序不更新workload表的modifier)
                sxProductService.updateUsWorkload(cmsBtUsWorkloadModel, CmsConstants.SxWorkloadPublishStatusNum.okNum,
                        StringUtils.isEmpty(cmsBtUsWorkloadModel.getModifier()) ? getTaskName() : cmsBtUsWorkloadModel.getModifier());

            } catch (Exception e) {
                if (sxData == null) {
                    sxData = new SxData();
                    sxData.setChannelId(channelId);
                    sxData.setCartId(cartId);
                }
                if (e instanceof BusinessException) {
                    if (StringUtils.isEmpty(sxData.getErrorMessage())) {
                        sxData.setErrorMessage(e.getMessage());
                    }
                }
                // 如果上新数据中的errorMessage为空
                if (StringUtils.isEmpty(sxData.getErrorMessage())) {
                    if(StringUtils.isNullOrBlank2(e.getMessage())) {
                        sxData.setErrorMessage("出现不可预知的错误，请跟管理员联系! " + e.getStackTrace()[0].toString());
                        e.printStackTrace();
                    } else {
                        sxData.setErrorMessage(e.getMessage());
                    }
                }
                // 上新出错时状态回写操作
                sxProductService.doUploadFinalProcForUs(false, sxData, cmsBtUsWorkloadModel, getTaskName());
                // 出错的时候将错误信息回写到cms_bt_business_log表
                sxProductService.insertBusinessLog(sxData, getTaskName(), true);
                // 回写workload表   (为了知道字段是哪个画面更新的，上新程序不更新workload表的modifier)
                sxProductService.updateUsWorkload(cmsBtUsWorkloadModel, CmsConstants.SxWorkloadPublishStatusNum.errorNum,
                        StringUtils.isEmpty(cmsBtUsWorkloadModel.getModifier()) ? getTaskName() : cmsBtUsWorkloadModel.getModifier());

            }
        }

        if (ListUtils.notNull(productModels)) {
            // 推送数据给美国
            CmsProductTransferToUsMQMessageBody messageBody = new CmsProductTransferToUsMQMessageBody();
            messageBody.setChannelId(shopBean.getOrder_channel_id());
            messageBody.setProductModels(productModels);
            messageBody.setSender(getTaskName());
            cmsMqSenderService.sendMessage(messageBody);
        }
        $info(String.format("本次推送数据 [%s] 条！", productModels.size()));
    }



    private TransferUsProductModel getProductInfoForUs(SxData sxData, String channelId, int cartId, ShopBean shop) {

        CmsBtProductModel mainProduct = sxData.getMainProduct();
        CmsBtProductModel_Platform_Cart platformInfo = sxData.getMainProduct().getUsPlatform(cartId);
        ExpressionParser expressionParser = new ExpressionParser(sxProductService, sxData);

        // 取得平台类目schema信息
        // catId == 1 (平台下共通schema)
        CmsMtPlatformCategorySchemaModel cmsMtPlatformCategorySchema = platformCategoryService.getPlatformCatSchema("1", cartId);
        if (cmsMtPlatformCategorySchema == null) {
            String error = String.format("获取平台类目schema信息失败 [平台类目Id:%s] [平台类目:%s] [CartId:%s]", platformInfo.getpCatId(), platformInfo.getpCatPath(), cartId);
            $error(error);
            throw new BusinessException(error);
        }

        TransferUsProductModel productModel = new TransferUsProductModel();
        productModel.setChannelId(channelId);
        productModel.setStoreId(cartId);

        // 设置商品共通数据
        setCommonInfo(productModel, mainProduct, platformInfo);

        // 获取sku数据
        List<TransferUsProductModel_Sku> skus = getSkuInfo(platformInfo, sxData);
        productModel.setItems(skus);

        // 获取商品平台数据
        Map<String, Object> platformInfoMap = getUsProductAttributes(cmsMtPlatformCategorySchema, shop, expressionParser);
        productModel.setStoreAttributes(platformInfoMap);

        return productModel;
    }

    private void setCommonInfo(TransferUsProductModel productModel, CmsBtProductModel mainProduct, CmsBtProductModel_Platform_Cart platformInfo) {
        CmsBtProductModel_Field commonFields = mainProduct.getCommon().getFields();
        String amazonCatPath = "";
        if (mainProduct.getUsPlatform(CartEnums.Cart.Amazon.getValue()) != null) {
            amazonCatPath = mainProduct.getUsPlatform(CartEnums.Cart.Amazon.getValue()).getpCatPath();
            productModel.setAmazonBrowseTree(amazonCatPath);
        }
        productModel.setSizeType(commonFields.getSizeType());
        productModel.setName(commonFields.getProductNameEn());
        productModel.setDescription(commonFields.getLongDesEn());
        productModel.setShortDescription(commonFields.getShortDesEn());
        productModel.setCode(commonFields.getCode());
        productModel.setAbstractInfo(commonFields.getAbstract());
        productModel.setAccessory(commonFields.getAccessory());
        productModel.setColor(commonFields.getCodeDiff());
        productModel.setUrlKey(commonFields.getUrlKey());
        productModel.setCreated(mainProduct.getCreated());
        productModel.setModel(commonFields.getModel());
        productModel.setImages(commonFields.getImages1());
        productModel.setBoxImages(commonFields.getImages2());
        productModel.setMarketingImages(null);// TODO: 2017/7/20
        productModel.setBrand(commonFields.getBrand());
        productModel.setColorMap(commonFields.getColorMap());
        productModel.setMaterial(commonFields.getMaterialEn());
        productModel.setOrigin(commonFields.getOrigin());
        productModel.setGoogleCategory(commonFields.getGoogleCategory());
        productModel.setGoogleDepartment(commonFields.getGoogleDepartment());
        productModel.setPriceGrabberCategory(commonFields.getPriceGrabberCategory());
        productModel.setStatus(platformInfo.getpStatus().name());
        if (StringUtils.isNullOrBlank2(commonFields.getTaxable()) || "0".equals(commonFields.getTaxable())) {
            productModel.setTaxable(false);
        } else {
            productModel.setTaxable(true);
        }

    }

    private List<TransferUsProductModel_Sku> getSkuInfo(CmsBtProductModel_Platform_Cart platformInfo, SxData sxData) {
        List<TransferUsProductModel_Sku> transferUsProductModelSkus = new ArrayList<>();

        for (BaseMongoMap<String, Object> sku : platformInfo.getSkus()) {
            TransferUsProductModel_Sku skuInfo = new TransferUsProductModel_Sku();
            String skuCode = sku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.skuCode.name());
            if (!StringUtils.isEmpty(skuCode)) skuInfo.setSku(skuCode);
            // 根据skuCode从skuList中取得common.sku和PXX.sku合并之后的sku
            BaseMongoMap<String, Object> mergedSku = sxData.getSkuList().stream()
                    .filter(s -> s.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.skuCode.name()).equals(skuCode))
                    .findFirst()
                    .get();

            skuInfo.setMsrp(sku.getDoubleAttribute(CmsBtProductConstants.Platform_SKU_COM.priceMsrp.name()));
            skuInfo.setPrice(sku.getDoubleAttribute(CmsBtProductConstants.Platform_SKU_COM.priceSale.name()));
            skuInfo.setQty(sku.getIntAttribute(CmsBtProductConstants.Platform_SKU_COM.qty.name()));
            skuInfo.setSize(mergedSku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.size.name()));
            skuInfo.setUpc(mergedSku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.barcode.name()));
            skuInfo.setWeight(mergedSku.getDoubleAttribute(CmsBtProductConstants.Platform_SKU_COM.weight.name()));
            skuInfo.setWeightUnit(mergedSku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.weightUnit.name()));


            transferUsProductModelSkus.add(skuInfo);
        }

        return transferUsProductModelSkus;
    }

    private Map<String, Object> getUsProductAttributes(CmsMtPlatformCategorySchemaModel cmsMtPlatformCategorySchema,
                                                 ShopBean shop, ExpressionParser expressionParser) {
        Map<String, Object> platformInfoMap = new HashMap<>();

        // 取得schema数据中的propsItem(XML字符串)
        String propsItem = cmsMtPlatformCategorySchema.getPropsItem();
        List<Field> itemFieldList =null;
        if (!StringUtils.isEmpty(propsItem)) {
            // 将取出的propsItem转换为字段列表
            itemFieldList = SchemaReader.readXmlForList(propsItem);
        }
        // 根据field列表取得属性值mapping数据
        Map<String, Field> attrMap;

        try {
            // 取得平台Schema所有field对应的属性值（不使用platform_mapping，直接从mainProduct中取得fieldId对应的值）
            attrMap = sxProductService.constructPlatformProps(itemFieldList, shop, expressionParser, false, true);
        } catch (Exception ex) {
            String errMsg = String.format("取得平台Schema所有Field对应的属性值失败！[ChannelId:%s] [CartId:%s] [PlatformCategoryId:%s]! ",
                    shop.getOrder_channel_id(), shop.getCart_id(), cmsMtPlatformCategorySchema.getCatId());
            $error(errMsg, ex);
            throw new BusinessException(errMsg + ex.getMessage());
        }

        if (attrMap != null && attrMap.size() > 0 && itemFieldList != null) {
            for (Field field : itemFieldList) {
                String fieldId = field.getId();
                Field fieldValue = attrMap.get(fieldId);
                if (fieldValue == null) {
                    continue;
                }

                // 根据输入类型分别设置3个属性值
                switch (fieldValue.getType()) {
                    case SINGLECHECK: {
                        platformInfoMap.put(fieldId, ((SingleCheckField)fieldValue).getValue().getValue());
                        break;
                    }
                    case MULTICHECK: {
                        List<String> valueList = ((MultiCheckField) fieldValue).getValues().stream()
                                    .map(Value::getValue)
                                    .collect(Collectors.toList());
                        platformInfoMap.put(fieldId, valueList);
                        break;
                    }
                    case INPUT: {
                        platformInfoMap.put(fieldId, ((InputField)fieldValue).getValue());
                        break;
                    }
                    default:
                        $error("复杂类型[" + field.getType() + "]不能作为属性值来使用！");
                }
            }

        }

        return platformInfoMap;
    }



}
