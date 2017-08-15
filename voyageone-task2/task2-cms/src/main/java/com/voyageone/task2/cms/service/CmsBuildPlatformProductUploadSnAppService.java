package com.voyageone.task2.cms.service;

import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.CmsChannelConfigs;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.CmsChannelConfigBean;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.ListUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.cnn.request.ProductAddRequest;
import com.voyageone.components.cnn.request.ProductListingRequest;
import com.voyageone.components.cnn.request.ProductUpdateRequest;
import com.voyageone.components.cnn.request.bean.ProductInfoBean;
import com.voyageone.components.cnn.response.ProductAddResponse;
import com.voyageone.components.cnn.response.ProductListingResponse;
import com.voyageone.components.cnn.response.ProductUpdateResponse;
import com.voyageone.components.cnn.service.CnnWareNewService;
import com.voyageone.service.bean.cms.product.SxData;
import com.voyageone.service.impl.cms.PlatformCategoryService;
import com.voyageone.service.impl.cms.PlatformProductUploadService;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.sx.rule_parser.ExpressionParser;
import com.voyageone.service.model.cms.CmsBtSxWorkloadModel;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategorySchemaModel;
import com.voyageone.service.model.cms.mongo.product.*;
import com.voyageone.task2.base.BaseCronTaskService;
import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.base.util.TaskControlUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.net.telnet.EchoOptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by Charis on 2017/7/31.
 */
@Service
public class CmsBuildPlatformProductUploadSnAppService extends BaseCronTaskService {

    private static final int CART_ID = CartEnums.Cart.SNAPP.getValue();

    // 分隔符(,)
    private final static String Separtor_Coma = ",";
    // 平台下的标题名称
    private final static String PlatformTitle = "title";
    // 销售属性-color
    private final static String Color = "color";
    // 销售属性-size
    private final static String Size = "size";
    // 销售属性名称-colorName
    private final static String ColorName = "颜色";
    // 销售属性名称-sizeName
    private final static String SizeName = "尺码";

    @Autowired
    private PlatformProductUploadService platformProductUploadService;

    @Autowired
    private SxProductService sxProductService;

    @Autowired
    private CnnWareNewService cnnWareNewService;

    @Override
    protected String getTaskName() {
        return "CmsBuildPlatformProductUploadSnAppJob";
    }
    @Override
    protected SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    /**
     * Sneakerhead APP上新处理
     *
     * @param taskControlList taskcontrol信息
     */
    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        doUploadMain(taskControlList);

        // 正常结束
        $info("主线程正常结束");
    }
    private void doUploadMain(List<TaskControlBean> taskControlList) {
        // 由于这个方法可能会自己调用自己循环很多很多次， 不一定会跳出循环， 但又希望能获取到最新的TaskControl的信息， 所以不使用基类里的这个方法了
        // 为了调试方便， 允许作为参数传入， 但是理想中实际运行中， 基本上还是自主获取的场合比较多
        if (taskControlList == null) {
            taskControlList = taskDao.getTaskControlList(getTaskName());

            if (taskControlList.isEmpty()) {
                logIssue("没有找到配置！！！", getTaskName());
                return;
            }

            // 是否可以运行的判断
            if (!TaskControlUtils.isRunnable(taskControlList)) {
                $info("Runnable is false");
                return;
            }

        }

        // 每个小组， 最多允许的线程数量
        int threadCount = NumberUtils.toInt(TaskControlUtils.getVal1WithDefVal(taskControlList, TaskControlEnums.Name.thread_count, "5"));

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
                List<String> channels = new ArrayList<>();
                channels.add(l.getCfg_val1());
                mapTaskControl.put(key, channels);
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
                    ExecutorService s = Executors.newSingleThreadExecutor();

                    List<String> channelIdList = v;
                    if (channelIdList != null) {
                        for (String channelId : channelIdList) {
                            s.execute(() -> {
                                try {
                                    doUploadProduct(channelId, CART_ID, threadCount);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            });
                        }
                    }
                    s.shutdown();
                    mapThread.put(k, s);

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
    public void doUploadProduct(String channelId, int cartId, int threadPoolCnt) throws Exception {

        // 获取店铺信息
        ShopBean shopProp = Shops.getShop(channelId, cartId);
        if (shopProp == null) return;

        // 从上新的任务表中获取该平台及渠道需要上新的任务列表(group by channel_id, cart_id, group_id)
        List<CmsBtSxWorkloadModel> sxWorkloadModels = platformProductUploadService.getSxWorkloadWithChannelIdCartId(
                CmsConstants.PUBLISH_PRODUCT_RECORD_COUNT_ONCE_HANDLE, channelId, cartId);
        if (ListUtils.isNull(sxWorkloadModels)) return;


        // 创建线程池
        ExecutorService executor = Executors.newFixedThreadPool(threadPoolCnt);
        // 根据上新任务列表中的groupId循环上新处理
        for (CmsBtSxWorkloadModel cmsBtSxWorkloadModel : sxWorkloadModels) {
            // 启动多线程
            executor.execute(() -> uploadProduct(cmsBtSxWorkloadModel, shopProp));
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
     * @param shopProp             ShopBean 店铺信息
     */
    public void uploadProduct(CmsBtSxWorkloadModel cmsBtSxWorkloadModel, ShopBean shopProp) {
        // 当前groupId(用于取得产品信息)
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

            sxData = sxProductService.getSxProductDataByGroupId(channelId, groupId);
            if (sxData == null) {
                throw new BusinessException("取得上新用的商品数据信息失败！请向管理员确认 [sxData=null]");
            }
            // 如果取得上新对象商品信息出错时，报错
            if (!StringUtils.isEmpty(sxData.getErrorMessage())) {
                sxData.setCartId(cartId);
                String errorMsg = sxData.getErrorMessage();
                sxData.setErrorMessage(""); // 这里设为空之后，异常捕捉到之后msg前面会加上店铺名称
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
            if (strSkuCodeList.isEmpty()) {
                String errMsg = "已完成审批的产品sku列表为空";
                $error(errMsg);
                throw new BusinessException(errMsg);
            }

            // 取得主产品SN App设置信息(包含SKU等信息)
            CmsBtProductModel_Platform_Cart mainProductPlatformCart = mainProduct.getPlatform(sxData.getCartId());
            if (mainProductPlatformCart == null) {
                String error = String.format("获取主产品SN App平台设置信息(包含SKU，Schema属性值等信息)失败！[ProductCode:%s][CartId:%s]",
                        mainProduct.getCommon().getFields().getCode(), sxData.getCartId());
                $error(error);
                throw new BusinessException(error);
            }

            // 从cms_mt_channel_config表中取得上新用价格配置项目名
            String priceConfigValue = getPriceConfigValue(sxData.getChannelId(), StringUtils.toString(cartId), CmsConstants.ChannelConfig.PRICE_SX_KEY,
                    CmsConstants.ChannelConfig.PRICE_SX_PRICE_CODE);
            if (StringUtils.isEmpty(priceConfigValue)) {
                String errMsg = String.format("从cms_mt_channel_config表中未能取得该店铺设置的上新用价格配置项目！ [config_key:%s]",
                        StringUtils.toString(cartId) + CmsConstants.ChannelConfig.PRICE_SX_PRICE_CODE);
                $error(errMsg);
                throw new BusinessException(errMsg);
            }
            if (!StringUtils.isEmpty(sxData.getPlatform().getNumIId())) {
                updateWare = true;
                // 取得更新对象商品id
                numIId = sxData.getPlatform().getNumIId();
            }

            // 填充 ProductInfoBean
            ProductInfoBean productInfoBean = fillProductInfoBean(sxData, shopProp, mainProductPlatformCart, priceConfigValue);
            if (updateWare) {
                // 更新
                ProductUpdateRequest productUpdateRequest = new ProductUpdateRequest();
                productUpdateRequest.setProductInfoBean(productInfoBean);
                productUpdateRequest.setNumIId(Long.parseLong(numIId));

                ProductUpdateResponse response;
                try {
                    response = cnnWareNewService.updateProduct(shopProp, productUpdateRequest);
                    if (response.isSuccess()) {
                        String errMsg = String.format("Sn App更新出现异常 [mainCode:%s] [groupId:%s] [error:%s]",
                                mainProduct.getCommon().getFields().getCode(), sxData.getGroupId(), response.getMsg());
                        throw new Exception(errMsg);
                    }
                } catch (Exception e) {
                    String errMsg = String.format("Sn App更新出现异常 [mainCode:%s] [groupId:%s] [error:%s]",
                            mainProduct.getCommon().getFields().getCode(), sxData.getGroupId(), e.getMessage());
                    $error(errMsg);
                    throw new Exception(errMsg);
                }
            } else {
                // 上新
                ProductAddRequest productAddRequest = new ProductAddRequest();
                productAddRequest.setProductInfoBean(productInfoBean);

                ProductAddResponse response;
                try {
                    response = cnnWareNewService.addProduct(shopProp, productAddRequest);
                    if (response.isSuccess()) {
                        numIId = String.valueOf(response.getData().getNumIId());
                    } else {
                        String errMsg = String.format("Sn App上新出现异常 [mainCode:%s] [groupId:%s] [error:%s]",
                                mainProduct.getCommon().getFields().getCode(), sxData.getGroupId(), response.getMsg());
                        throw new Exception(errMsg);
                    }

                } catch (Exception e) {
                    String errMsg = String.format("Sn App上新出现异常 [mainCode:%s] [groupId:%s] [error:%s]",
                            mainProduct.getCommon().getFields().getCode(), sxData.getGroupId(), e.getMessage());
                    $error(errMsg);
                    throw new Exception(errMsg);
                }
            }

            // 默认在库
            CmsConstants.PlatformStatus platformStatus = CmsConstants.PlatformStatus.InStock;
            // 商品上下架
            CmsConstants.PlatformActive platformActive = sxData.getPlatform().getPlatformActive();
            if (!updateWare) {
                // 新增的场合， 看一下配置
                platformActive = sxProductService.getDefaultPlatformActiveConfigByChannelCart(sxData.getChannelId(), String.valueOf(sxData.getCartId()));
            }
            if (platformActive == CmsConstants.PlatformActive.ToOnSale) {
                // 调用商品上架api
                ProductListingRequest request = new ProductListingRequest();
                request.setNumIId(numIId);
                ProductListingResponse response;
                try {
                    response = cnnWareNewService.doProductListing(shopProp, request);
                    if (response.isSuccess()) {
                        platformStatus = CmsConstants.PlatformStatus.OnSale;
                    }
                } catch (Exception e) {
                    $error("上架失败！[error:%s]", e.getMessage());
                }
            }
            // 回写ims_bt_product表(numIId)
            sxProductService.updateImsBtProduct(sxData, getTaskName());
            // 上新成功状态回写操作
            sxProductService.doUploadFinalProc(shopProp, true, sxData, cmsBtSxWorkloadModel, numIId,
                    platformStatus, "", getTaskName(), null);


        } catch (Exception ex) {
            if (sxData == null) {
                // 回写详细错误信息表(cms_bt_business_log)用
                sxData = new SxData();
                sxData.setChannelId(channelId);
                sxData.setCartId(cartId);
                sxData.setGroupId(groupId);
                sxData.setErrorMessage(shopProp.getShop_name() + " Sn App取得上新用的商品数据信息异常,请跟管理员联系! [上新数据为null]");
            }
            // 如果上新数据中的errorMessage为空
            if (StringUtils.isEmpty(sxData.getErrorMessage())) {
                // nullPoint错误的处理
                if(StringUtils.isNullOrBlank2(ex.getMessage())) {
                    ex.printStackTrace();
                    sxData.setErrorMessage(shopProp.getShop_name() + " Sn App上新时出现不可预知的错误，请跟管理员联系! "
                            + ex.getStackTrace()[0].toString());
                } else {
                    sxData.setErrorMessage(shopProp.getShop_name() + " " +ex.getMessage());
                }
            }
            // 上新出错时状态回写操作
            sxProductService.doUploadFinalProc(shopProp, false, sxData, cmsBtSxWorkloadModel, "", null, "", getTaskName());
            // 异常结束
            $error(String.format("Sn App单个商品%s失败！[ChannelId:%s] [CartId:%s] [GroupId:%s] [NumIId:%s] [errMsg:%s]",
                    updateWare ? "更新" : "上新", channelId, cartId, groupId, numIId, sxData.getErrorMessage()));
        }

    }

    /**
     * 填充productAddRequest
     * @param sxData 上新主数据
     * @param shop 店铺
     * @param platformCart 平台信息
     * @param priceConfigValue 价格配置
     * @return 商品属性信息
     * @throws Exception
     */
    private ProductInfoBean fillProductInfoBean(SxData sxData, ShopBean shop, CmsBtProductModel_Platform_Cart platformCart,
                                                String priceConfigValue) throws Exception{

//        ExpressionParser expressionParser = new ExpressionParser(sxProductService, sxData);
        ProductInfoBean productInfoBean = new ProductInfoBean();
        // 填充共通级属性
        fillCommonFields(platformCart, sxData, shop, productInfoBean);

        // 填充sku
        fillSkuFields(platformCart, sxData, productInfoBean, priceConfigValue);

        // 填充optionItem
        fillOptionItems(productInfoBean, sxData);

        return productInfoBean;
    }

    /**
     * 填充OptionItem
     * @param productInfoBean 商品属性信息
     * @param sxData 上新主数据
     * @throws Exception
     */
    private void fillOptionItems(ProductInfoBean productInfoBean, SxData sxData) throws Exception {
        // 颜色所有值的列表
        List<String> colorList = new ArrayList<>();
        List<String> urlList = new ArrayList<>();
        for (CmsBtProductModel product : sxData.getProductList()) {
            String color = sxProductService.getSxColorAlias(sxData.getChannelId(), sxData.getCartId(), product, 50);
            colorList.add(color);
            // 属性图片
            String srcPicUrl = sxProductService.getProductImages(product, CmsBtProductConstants.FieldImageType.PRODUCT_IMAGE, sxData.getCartId()).get(0).getName();
            urlList.add(srcPicUrl);
        }
        // 尺码所有值的列表（排序）
        ArrayList<String> result = new ArrayList<>();
        List<String> sizeList = sxData.getSkuList().stream()
                .map(sku -> sku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.sizeSx.name()))
                .collect(Collectors.toList());

        sizeList.stream()
                .filter(size -> Collections.frequency(result, size) < 1)
                .forEach(result::add);

        // 对optionItem统一设值处理
        ProductInfoBean.OptionItem optionItem;
        // colorOption
        {
            optionItem = productInfoBean.createAddOptionItem();
            optionItem.setKey(Color);
            optionItem.setName(ColorName);
            optionItem.setOrder(1);
            optionItem.setValueList(colorList);
            optionItem.setUrlList(urlList);
        }
        // sizeOption
        {
            optionItem = productInfoBean.createAddOptionItem();
            optionItem.setKey(Size);
            optionItem.setName(SizeName);
            optionItem.setOrder(2);
            optionItem.setValueList(result);
        }
    }

    /**
     * 填充productAddRequest中CommonFields结构体
     * @param platformCart 平台信息
     * @param sxData 上新主数据
     * @param shop 店铺
     * @param productInfoBean 商品属性信息
     * @throws Exception
     */
    private void fillCommonFields(CmsBtProductModel_Platform_Cart platformCart, SxData sxData, ShopBean shop,
                                  ProductInfoBean productInfoBean) throws Exception{
        CmsBtProductModel_Field cmsCommonFields = sxData.getMainProduct().getCommon().getFieldsNotNull();

        ProductInfoBean.CommonFields commonFields = productInfoBean.getCommonFields();
        commonFields.setChannelId(shop.getOrder_channel_id());
        commonFields.setModel(cmsCommonFields.getModel());
        commonFields.setBrand(cmsCommonFields.getBrand());
        commonFields.setShortDesc(cmsCommonFields.getShortDesCn());
        commonFields.setLongDesc(cmsCommonFields.getLongDesCn());
        commonFields.setMaterial(cmsCommonFields.getMaterialCn());
        commonFields.setOrigin(cmsCommonFields.getOrigin());
        commonFields.setProductType(cmsCommonFields.getProductType());
        commonFields.setSizeType(cmsCommonFields.getSizeType());
        // TODO: 2017/8/1
        commonFields.setUsage("使用说明");
        // TODO: 2017/8/1
        commonFields.setPageDetailM("http://image.sneakerhead.com/is/image/sneakerhead/tmall-800d?$800$&$img=air-jordan-6-91-tshirt-833933010-1;http://image.sneakerhead.com/is/image/sneakerhead/tmall-800d?$800$&$img=air-jordan-6-91-tshirt-833933010-2");
        // TODO: 2017/8/1
        commonFields.setPageDetailPC("http://image.sneakerhead.com/is/image/sneakerhead/tmall-800d?$800$&$img=air-jordan-6-91-tshirt-833933010-1;http://image.sneakerhead.com/is/image/sneakerhead/tmall-800d?$800$&$img=air-jordan-6-91-tshirt-833933010-2");
        // 店铺内分类ID
        if (ListUtils.notNull(platformCart.getSellerCats())) {
            List<String> categoryIds = platformCart.getSellerCats().stream()
                    .map(CmsBtProductModel_SellerCat::getcIds)
                    .collect(ArrayList::new, ArrayList::addAll, ArrayList::addAll);
            if (ListUtils.notNull(categoryIds)) commonFields.setCategoryId(StringUtils.join(categoryIds, Separtor_Coma));
        }

        // 产品名称
        String title = platformCart.getFieldsNotNull().getStringAttribute(PlatformTitle);
        if (StringUtils.isNullOrBlank2(title)) title = cmsCommonFields.getOriginalTitleCn();
        if (StringUtils.isNullOrBlank2(title)) title = cmsCommonFields.getProductNameEn();
        commonFields.setTitle(title);

        // 商品特质(颜色/口味/香型等)
        String color = cmsCommonFields.getColor();
        if (StringUtils.isNullOrBlank2(color)) color = cmsCommonFields.getCodeDiff();
        commonFields.setFeature(color);

        // 商品主图
        String valMainImages = sxProductService.getProductImages(sxData.getMainProduct(), CmsBtProductConstants.FieldImageType.PRODUCT_IMAGE, sxData.getCartId()).get(0).getName();
        commonFields.setMainImage(valMainImages);

        // 产品图列表
        List<String> listPicNameUrl = new ArrayList<>();
        for (CmsBtProductModel productModel : sxData.getProductList()) {
            List<String> imageList = sxProductService.getProductImages(productModel, CmsBtProductConstants.FieldImageType.PRODUCT_IMAGE, sxData.getCartId())
                    .stream()
                    .map(CmsBtProductModel_Field_Image::getName)
                    .collect(Collectors.toList());

            listPicNameUrl.addAll(imageList);
        }
        commonFields.setImages(listPicNameUrl);
    }

    /**
     * 填充productAddRequest中skuList结构体
     * @param platformCart 平台信息
     * @param sxData 上新主数据
     * @param productInfoBean 商品属性信息
     * @param priceConfigValue 价格配置
     * @throws Exception
     */
    private void fillSkuFields(CmsBtProductModel_Platform_Cart platformCart, SxData sxData, ProductInfoBean productInfoBean,
                               String priceConfigValue) throws Exception{

        for (CmsBtProductModel product : sxData.getProductList()) {
            String code = product.getCommon().getFields().getCode();
            String color = sxProductService.getSxColorAlias(sxData.getChannelId(), sxData.getCartId(), product, 50);

            for (BaseMongoMap<String, Object> pSku : platformCart.getSkus()) {
                // 获取sku
                String pSkuCode = pSku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.skuCode.name());
                // 根据skuCode从skuList中取得common.sku和PXX.sku合并之后的sku
                BaseMongoMap<String, Object> mergedSku = sxData.getSkuList().stream()
                        .filter(s -> s.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.skuCode.name()).equals(pSkuCode))
                        .findFirst()
                        .get();
                // 上新用的尺码
                String sizeSx = mergedSku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.sizeSx.name());

                Map<String, Object> skuOptionMap = new HashMap<>();
                skuOptionMap.put(Color, color);
                skuOptionMap.put(Size, sizeSx);

                // 填充skuItem
                ProductInfoBean.SkuItem skuItem = productInfoBean.createAddSkuIem();
                skuItem.setSkuCode(pSkuCode);
                skuItem.setProdCode(code);
                skuItem.setInventory(pSku.getIntAttribute(CmsBtProductConstants.Platform_SKU_COM.qty.name()));
                skuItem.setMsrpPrice(pSku.getDoubleAttribute(CmsBtProductConstants.Platform_SKU_COM.priceMsrp.name()));
                skuItem.setSalePrice(pSku.getDoubleAttribute(priceConfigValue));
                skuItem.setTax(0d);
                skuItem.setSkuOptions(skuOptionMap);
            }
        }
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
        CmsChannelConfigBean priceConfig = CmsChannelConfigs.getConfigBean(channelId,priceKey, cartId + priceCode);
        String priceConfigValue = "";
        if (priceConfig != null) {
            // 取得价格对应的configValue名
            priceConfigValue = priceConfig.getConfigValue1();
        }
        return priceConfigValue;
    }
}
