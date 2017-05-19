package com.voyageone.web2.cms.views.product;

import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.JongoUpdate;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.*;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.beans.CmsChannelConfigBean;
import com.voyageone.common.configs.beans.TypeBean;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.masterdate.schema.enums.FieldTypeEnum;
import com.voyageone.common.masterdate.schema.factory.SchemaJsonReader;
import com.voyageone.common.masterdate.schema.field.*;
import com.voyageone.common.masterdate.schema.option.Option;
import com.voyageone.common.masterdate.schema.utils.FieldUtil;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.masterdate.schema.value.ComplexValue;
import com.voyageone.common.masterdate.schema.value.Value;
import com.voyageone.common.util.BeanUtils;
import com.voyageone.common.util.CommonUtil;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.service.bean.cms.CmsCategoryInfoBean;
import com.voyageone.service.bean.cms.product.*;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.dao.ims.ImsBtProductDao;
import com.voyageone.service.impl.cms.CategorySchemaService;
import com.voyageone.service.impl.cms.CategoryTreeAllService;
import com.voyageone.service.impl.cms.CommonSchemaService;
import com.voyageone.service.impl.cms.ImageTemplateService;
import com.voyageone.service.impl.cms.feed.FeedCustomPropService;
import com.voyageone.service.impl.cms.feed.FeedInfoService;
import com.voyageone.service.impl.cms.prices.IllegalPriceConfigException;
import com.voyageone.service.impl.cms.prices.PlatformPriceService;
import com.voyageone.service.impl.cms.prices.PriceCalculateException;
import com.voyageone.service.impl.cms.prices.PriceService;
import com.voyageone.service.impl.cms.product.*;
import com.voyageone.service.impl.cms.product.search.CmsSearchInfoBean2;
import com.voyageone.service.impl.cms.sx.PlatformWorkloadAttribute;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;
import com.voyageone.service.impl.cms.vomq.CmsMqSenderService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.CmsBatchSetMainCategoryMQMessageBody;
import com.voyageone.service.impl.com.mq.MqSender;
import com.voyageone.service.impl.wms.InventoryCenterLogicService;
import com.voyageone.service.impl.wms.WmsCodeStoreInvBean;
import com.voyageone.service.model.cms.CmsMtFeedCustomPropModel;
import com.voyageone.service.model.cms.mongo.CmsMtCategorySchemaModel;
import com.voyageone.service.model.cms.mongo.CmsMtCategoryTreeAllModel_Platform;
import com.voyageone.service.model.cms.mongo.CmsMtCommonSchemaModel;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel_Sku;
import com.voyageone.service.model.cms.mongo.product.*;
import com.voyageone.service.model.ims.ImsBtProductModel;
import com.voyageone.web2.base.BaseViewService;
import com.voyageone.web2.cms.bean.CmsProductInfoBean;
import com.voyageone.web2.cms.bean.CmsSessionBean;
import com.voyageone.web2.cms.bean.CustomAttributesBean;
import com.voyageone.web2.cms.views.search.CmsAdvanceSearchService;
import com.voyageone.web2.core.bean.UserSessionBean;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.voyageone.common.CmsConstants.ChannelConfig.PRICE_CALCULATOR;
import static com.voyageone.common.CmsConstants.ChannelConfig.PRICE_CALCULATOR_FORMULA;

/**
 * Created by lewis on 15-12-16.
 */
@Service
public class CmsProductDetailService extends BaseViewService {

    private static final String FIELD_SKU_CARTS = "skuCarts";
    private static final String COMPLETE_STATUS = "1";
    @Autowired
    protected CategorySchemaService categorySchemaService;
    @Autowired
    private CommonSchemaService commonSchemaService;
    @Autowired
    private FeedInfoService feedInfoService;
    @Autowired
    private FeedCustomPropService feedCustomPropService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductGroupService productGroupService;
    @Autowired
    private CmsAdvanceSearchService advanceSearchService;
    @Autowired
    private ImageTemplateService imageTemplateService;
    @Autowired
    private CategoryTreeAllService categoryTreeAllService;
    @Autowired
    private ProductStatusHistoryService productStatusHistoryService;
    @Autowired
    private SxProductService sxProductService;
    @Autowired
    private ImsBtProductDao imsBtProductDao;
    @Autowired
    private CmsBtProductDao cmsBtProductDao;
    @Autowired
    private CmsBtPriceConfirmLogService cmsBtPriceConfirmLogService;
    @Autowired
    private PriceService priceService;
    @Autowired
    private CmsBtPriceLogService cmsBtPriceLogService;
    @Autowired
    private InventoryCenterLogicService inventoryCenterLogicService;
    @Autowired
    private MqSender sender;
    @Autowired
    private PlatformPriceService platformPriceService;
    @Autowired
    private ProductPlatformService productPlatformService;
    @Autowired
    private CmsMqSenderService senderService;

    /**
     * 填充field选项值.
     */
    public static void fillFieldOptions(List<Field> fields, String channelId, String language) {

        for (Field field : fields) {

            if (CmsConstants.OptionConfigType.OPTION_DATA_SOURCE.equals(field.getDataSource())
                    || CmsConstants.OptionConfigType.OPTION_DATA_SOURCE_CHANNEL.equals(field.getDataSource())) {

                FieldTypeEnum type = field.getType();

                switch (type) {
                    case LABEL:
                        break;
                    case INPUT:
                        break;
                    case SINGLECHECK:
                    case MULTICHECK:
                        if (CmsConstants.OptionConfigType.OPTION_DATA_SOURCE.equals(field.getDataSource())) {
                            List<TypeBean> typeBeanList = Types.getTypeList(field.getId(), language);

                            // 替换成field需要的样式
                            List<Option> options = new ArrayList<>();
                            for (TypeBean typeBean : typeBeanList) {
                                Option opt = new Option();
                                opt.setDisplayName(typeBean.getName());
                                opt.setValue(typeBean.getValue());
                                options.add(opt);
                            }

                            OptionsField optionsField = (OptionsField) field;
                            optionsField.setOptions(options);
                        } else if (CmsConstants.OptionConfigType.OPTION_DATA_SOURCE_CHANNEL.equals(field.getDataSource())) {
                            // 获取type channel bean
                            List<TypeChannelBean> typeChannelBeanList;
                            if (FIELD_SKU_CARTS.equals(field.getId())) {
                                typeChannelBeanList = TypeChannels.getTypeListSkuCarts(channelId, Constants.comMtTypeChannel.SKU_CARTS_53_A, language);
                            } else {
                                typeChannelBeanList = TypeChannels.getTypeWithLang(field.getId(), channelId, language);
                            }

                            // 替换成field需要的样式
                            List<Option> options = new ArrayList<>();
                            if (typeChannelBeanList != null) {
                                for (TypeChannelBean typeChannelBean : typeChannelBeanList) {
                                    Option opt = new Option();
                                    opt.setDisplayName(typeChannelBean.getName());
                                    opt.setValue(typeChannelBean.getValue());
                                    options.add(opt);
                                }
                            }
                            OptionsField optionsField = (OptionsField) field;
                            optionsField.setOptions(options);
                        }
                        break;
                    default:
                        break;

                }

            }
        }

    }

    /**
     * 获取类目以及类目属性信息.
     * 1.检查数据已经准备完成，batchField.switchCategory = 1时返回并告知运营正在准备数据，否则正常显示.
     */
    public Map<String, Object> getProductInfo(String channelId, Long prodId, int cartId, String language) throws BusinessException {

        CmsProductInfoBean productInfo = new CmsProductInfoBean();

        //check the product data is ready.
        productInfo.setProductDataIsReady(productService.checkProductDataIsReady(channelId, prodId));

        //自定义属性.
        CustomAttributesBean customAttributes = new CustomAttributesBean();

        // 获取product data.
        CmsBtProductModel productValueModel = getProductModel(channelId, prodId, cartId);

        //商品各种状态.
        CmsProductInfoBean.ProductStatus productStatus = productInfo.getProductStatusInstance();
//        productStatus.setApproveStatus(productValueModel.getFields().getStatus());

        if (COMPLETE_STATUS.equals(productValueModel.getCommon().getFields().getTranslateStatus())) {
            productStatus.setTranslateStatus(true);
        } else {
            productStatus.setTranslateStatus(false);
        }

        // 设置是否approve标签
//        if (CmsConstants.ProductStatus.Approved.name().equals(productValueModel.getFields().getStatus())) {
//            productStatus.setIsApproved(true);
//        } else {
//            productStatus.setIsApproved(false);
//        }

        //获取商品图片信息.
        Map<String, List<CmsBtProductModel_Field_Image>> productImages = new HashMap<>();
        productImages.put("image1", productValueModel.getCommon().getFields().getImages(CmsBtProductConstants.FieldImageType.PRODUCT_IMAGE));
        productImages.put("image2", productValueModel.getCommon().getFields().getImages(CmsBtProductConstants.FieldImageType.PACKAGE_IMAGE));
        productImages.put("image3", productValueModel.getCommon().getFields().getImages(CmsBtProductConstants.FieldImageType.ANGLE_IMAGE));
        productImages.put("image4", productValueModel.getCommon().getFields().getImages(CmsBtProductConstants.FieldImageType.CUSTOM_IMAGE));
        productImages.put("image5", productValueModel.getCommon().getFields().getImages(CmsBtProductConstants.FieldImageType.MOBILE_CUSTOM_IMAGE));
        productImages.put("image6", productValueModel.getCommon().getFields().getImages(CmsBtProductConstants.FieldImageType.CUSTOM_PRODUCT_IMAGE));

        // 获取feed方数据.
        Map<String, String> feedInfoModel = getCmsBtFeedInfoModel(channelId, prodId, productValueModel);


        // 获取共通schema.
        CmsMtCommonSchemaModel comSchemaModel = commonSchemaService.getComSchemaModel();

        List<Field> comSchemaFields = comSchemaModel.getFields();

        fillFieldOptions(comSchemaFields, channelId, language);


        //获取主数据的值.
        Map masterSchemaValue = productValueModel.getCommon().getFields();

        //填充master schema
        FieldUtil.setFieldsValueFromMap(comSchemaFields, masterSchemaValue);

        //没有值的情况下设定complexField、MultiComplexField的默认值.
        setDefaultComplexValues(comSchemaFields);

//        this.fillFieldOptions(subSkuFields, channelId, language);

        // TODO 取得Sku的库存
        Map<String, Integer> skuInventoryList = productService.getProductSkuQty(productValueModel.getOrgChannelId(), productValueModel.getCommon().getFields().getCode());

        //获取sku schemaValue
//        Map<String, Object> skuSchemaValue = buildSkuSchemaValue(productValueModel, categorySchemaModel, skuInventoryList);

        //填充sku schema.
//        FieldUtil.setFieldsValueFromMap(skuSchemaFields, skuSchemaValue);

        //设置feed属性值
        customAttributes.setOrgAtts(productValueModel.getFeed().getOrgAtts());
        customAttributes.setCnAtts(productValueModel.getFeed().getCnAtts());
        customAttributes.setCustomIds(productValueModel.getFeed().getCustomIds());
        customAttributes.setCustomIdsCn(productValueModel.getFeed().getCustomIdsCn());
        customAttributes.setCnAttsShow(getCustomAttributesCnAttsShow(feedInfoModel.get("category"), productValueModel.getFeed(), channelId));

        productInfo.setChannelId(channelId);
        productInfo.setProductId(prodId);
        productInfo.setCustomAttributes(customAttributes);
        productInfo.setFeedInfoModel(feedInfoModel);
        productInfo.setProductImages(productImages);
        productInfo.setProductStatus(productStatus);
        productInfo.setModified(productValueModel.getModified());
        productInfo.setProductCode(productValueModel.getCommon().getFields().getCode());
        productInfo.setOrgChannelId(productValueModel.getOrgChannelId());


        Map<String, Object> infoMap = new HashMap<>();
        infoMap.put("productInfo", productInfo);

        ChannelConfigEnums.Channel channel = ChannelConfigEnums.Channel.valueOfId(productValueModel.getOrgChannelId());
        if (channel == null) {
            infoMap.put("orgChaName", "");
        } else {
            infoMap.put("orgChaName", channel.getFullName());
        }

        // 判断是否是minimall用户
        boolean isMiniMall = Channels.isUsJoi(channelId);
        infoMap.put("isminimall", isMiniMall ? 1 : 0);

        // 判断是否主商品
        boolean isMain = false;

        // 根据产品code找到group
        CmsBtProductGroupModel grpObj = productGroupService.getProductGroupByQuery(channelId, "{'cartId':" + cartId + ",'productCodes':'" + productValueModel.getCommon().getFields().getCode() + "'}");
        if (grpObj != null) {
            if (productValueModel.getCommon().getFields().getCode().equals(grpObj.getMainProductCode())) {
                isMain = true;
            }
        }
        infoMap.put("isMain", isMain ? 1 : 0);

        // 设置默认第一张图片
        String defaultImageUrl = imageTemplateService.getDefaultImageUrl();
        Map<String, Object> defaultImage = productValueModel.getCommon().getFields().getImages1().get(0);
        if (defaultImage.size() > 0)
            infoMap.put("defaultImage", String.format(defaultImageUrl, String.valueOf(defaultImage.get("image1"))));

        return infoMap;
    }

    /**
     * 取得Sku的库存
     */
    public List<Map<String, Object>> getProdSkuCnt(String channelId, Long prodId) {
        CmsBtProductModel prodObj = productService.getProductById(channelId, prodId);
//        if (channelId.equals(ChannelConfigEnums.Channel.VOYAGEONE.getId())) {
        // 如果是mini mall店铺，则需要用原始channelId去检索库存信息
        channelId = StringUtils.isEmpty(prodObj.getOrgChannelId()) ? channelId : prodObj.getOrgChannelId();
//        }
        Map<String, Integer> skuList = productService.getProductSkuQty(channelId, prodObj.getCommon().getFields().getCode());

        List<Map<String, Object>> inventoryList = new ArrayList<>(0);
        prodObj.getCommon().getSkus().forEach(sku -> {
            Map<String, Object> result = new HashMap<>();
            result.put("skucode", sku.getSkuCode());
            result.put("skyqty", skuList.get(sku.getSkuCode()) != null ? skuList.get(sku.getSkuCode()) : 0);
            inventoryList.add(result);
        });

        return inventoryList;
    }

    /**
     * 更新product values.
     */
    public String updateProductMasterInfo(String channelId, String user, Map requestMap) {

        List<Map<String, Object>> masterFieldsList = (List<Map<String, Object>>) requestMap.get("masterFields");

        Map<String, Object> customAttributesValue = (Map<String, Object>) requestMap.get("customAttributes");

        CmsBtProductModel productModel = new CmsBtProductModel(channelId);

        CmsBtProductModel_Feed feedModel = buildCmsBtProductModel_feed(customAttributesValue);

        List<Field> masterFields = buildMasterFields(masterFieldsList);

        CmsBtProductModel_Field masterFieldsValue = buildCmsBtProductModel_field(requestMap, masterFields);

        productModel.getCommon().setCatId(requestMap.get("categoryId").toString());
        productModel.setProdId(Long.valueOf(requestMap.get("productId").toString()));
        productModel.getCommon().setCatPath(requestMap.get("categoryFullPath").toString());
        productModel.getCommon().setFields(masterFieldsValue);
        productModel.setFeed(feedModel);
        productModel.setModified(requestMap.get("modified").toString());

        ProductUpdateBean productUpdateBean = new ProductUpdateBean();
        productUpdateBean.setProductModel(productModel);
        productUpdateBean.setModifier(user);
        String newModified = DateTimeUtil.getNowTimeStamp();
        productUpdateBean.setModified(newModified);

        productService.updateProduct(channelId, productUpdateBean);

        return newModified;
    }

    /**
     * 更新product values.
     */
    public String updateProductSkuInfo(String channelId, String user, String categoryId, Long productId, String modified, String categoryFullPath, Map skuFieldMap) {

//        CmsBtProductModel productModel = new CmsBtProductModel(channelId);
//
//        List<CmsBtProductModel_Sku> skuValues = buildCmsBtProductModel_skus(skuFieldMap);
//
//        productModel.setCatId(categoryId);
//        productModel.setProdId(productId);
//        productModel.setCatPath(categoryFullPath);
//        productModel.setSkus(skuValues);
//        productModel.setModified(modified);
//
//        ProductUpdateBean productUpdateBean = new ProductUpdateBean();
//        productUpdateBean.setProductModel(productModel);
//        productUpdateBean.setModifier(user);
//        String newModified = DateTimeUtil.getNowTimeStamp();
//        productUpdateBean.setModified(newModified);
//
//        productService.updateProduct(channelId, productUpdateBean);

//        return newModified;
        return null;
    }

    /**
     * 保存全部产品信息.
     */
    public Map<String, Object> updateProductAllInfo(String channelId, String userName, Map requestMap) {

//        String categoryId = requestMap.get("categoryId").toString();
//        Long productId = Long.valueOf(requestMap.get("productId").toString());
//        String categoryFullPath = requestMap.get("categoryFullPath").toString();
//        Map skuMap = (Map) requestMap.get("skuFields");
//        String modified = requestMap.get("modified").toString();
//
//        List<Map<String, Object>> masterFieldsList = (List<Map<String, Object>>) requestMap.get("masterFields");
//        Map<String, Object> customAttributesValue = (Map<String, Object>) requestMap.get("customAttributes");
//        List<CmsBtProductModel_Sku> skuValues = buildCmsBtProductModel_skus(skuMap);
//
//        CmsBtProductModel productModel = new CmsBtProductModel(channelId);
//
//        CmsBtProductModel_Feed feedModel = buildCmsBtProductModel_feed(customAttributesValue);
//
//        List<Field> masterFields = buildMasterFields(masterFieldsList);
//
//        CmsBtProductModel_Field masterFieldsValue = buildCmsBtProductModel_field(requestMap, masterFields);
//
//        productModel.setCatId(categoryId);
//        productModel.setProdId(productId);
//        productModel.setCatPath(categoryFullPath);
//        productModel.setFields(masterFieldsValue);
////        productModel.setFeed(feedModel);
//        productModel.setSkus(skuValues);
//        productModel.setModified(modified);
//
//        ProductUpdateBean productUpdateBean = new ProductUpdateBean();
//        productUpdateBean.setProductModel(productModel);
//        productUpdateBean.setModifier(userName);
//        String newModified = DateTimeUtil.getNowTimeStamp();
//        productUpdateBean.setModified(newModified);
//
//        // 更新product数据
//        CmsBtProductModel oldProduct = productService.getProductById(channelId, productId);
//
//        //执行product的carts更新 TODO--这里需要讨论是否要再更新
//        if (productUpdateBean.getProductModel().getFields().getStatus().equals(CmsConstants.ProductStatus.Approved.name())) {
//            // 执行carts更新
//            productUpdateBean.getProductModel().getSkus().forEach(sku -> {
//                List<Integer> newCarts = sku.getSkuCarts().stream().filter(s -> (s == 23 || s == 928 || s == 929 || s == 28 || s == 29)).collect(Collectors.toList());
//                sku.setSkuCarts(newCarts);
//            });
//        }
//
//        productService.updateProduct(channelId, productUpdateBean);
//
//        CmsBtProductModel newProduct = productService.getProductById(channelId, productId);
//
//        //执行product上新
//        if (productUpdateBean.getProductModel().getFields().getStatus().equals(CmsConstants.ProductStatus.Approved.name())) {
//
//            // 插入上新程序
//            productService.insertSxWorkLoad(channelId, newProduct, userName);
//
//            // 插入全店特价宝
//            // todo 插入全店特价宝要修正
////            if (oldProduct.getFields().getPriceSaleEd().compareTo(newProduct.getFields().getPriceSaleEd()) != 0
////                    || oldProduct.getFields().getPriceSaleSt().compareTo(newProduct.getFields().getPriceSaleSt()) != 0) {
////                CmsBtPromotionCodesBean cmsBtPromotionCodesBean = new CmsBtPromotionCodesBean();
////                cmsBtPromotionCodesBean.setProductId(productId);
////                cmsBtPromotionCodesBean.setProductCode(newProduct.getFields().getCode());
////                cmsBtPromotionCodesBean.setPromotionPrice(newProduct.getFields().getPriceSaleEd());
////                cmsBtPromotionCodesBean.setPromotionId(0);
////                cmsBtPromotionCodesBean.setNumIid(oldProduct.getGroups().getNumIId());
////                cmsBtPromotionCodesBean.setChannelId(channelId);
////                cmsBtPromotionCodesBean.setCartId(CartType.TMALLG.getCartId());
////                cmsBtPromotionCodesBean.setModifier(userName);
////                promotionDetailService.teJiaBaoPromotionUpdate(cmsBtPromotionCodesBean);
////            }
//        }
//
//        // Translation状态从完成-》未完成
//        if ("1".equalsIgnoreCase(oldProduct.getFields().getTranslateStatus()) && "0".equalsIgnoreCase(newProduct.getFields().getTranslateStatus())) {
//            Map<String, Object> updObj = new HashMap<>();
//            updObj.put("common.fields.translateStatus", "0");
//            updObj.put("common.fields.translateTime", DateTimeUtil.getNow(DateTimeUtil.DEFAULT_DATETIME_FORMAT));
//            productService.updateTranslation(channelId, newProduct.getFields().getCode(), updObj, userName);
//        }
//
//        // 设置返回值
//        Map<String, Object> result = new HashMap<>();
//        // 设置返回新的时间戳
//        result.put("modified", newModified);
//        // 设置返回approve状态
//        result.put("isApproved", CmsConstants.ProductStatus.Approved.name().equals(newProduct.getFields().getStatus()));
//        // 设置返回status状态
//        result.put("approveStatus", newProduct.getFields().getStatus());
//        return result;
        return null;
    }

    public Map<String, Object> updateProductFeedInfo(String channelId, String userName, Map requestMap) {

        Long productId = Long.valueOf(requestMap.get("productId").toString());
        String modified = requestMap.get("modified").toString();

        Map<String, Object> customAttributesValue = (Map<String, Object>) requestMap.get("customAttributes");

        CmsBtProductModel productModel = new CmsBtProductModel(channelId);

        CmsBtProductModel_Feed feedModel = buildCmsBtProductModel_feed(customAttributesValue);

        productModel.setProdId(productId);
        productModel.setFeed(feedModel);
        productModel.setModified(modified);
        productModel.setModifier(userName);

        ProductUpdateBean productUpdateBean = new ProductUpdateBean();
        productUpdateBean.setProductModel(productModel);
        productUpdateBean.setModifier(userName);
        String newModified = DateTimeUtil.getNowTimeStamp();
        productUpdateBean.setModified(newModified);

        productService.updateProduct(channelId, productUpdateBean);

        CmsBtProductModel newProduct = productService.getProductById(channelId, productId);

        //执行product上新
//        if (newProduct.getFields().getStatus().equals(CmsConstants.ProductStatus.Approved.name())) {

        // 插入上新程序
        sxProductService.insertSxWorkLoad(newProduct, userName);

//        }

        // 设置返回值
        Map<String, Object> result = new HashMap<>();
        // 设置返回新的时间戳
        result.put("modified", newModified);
//        // 设置返回approve状态
//        result.put("isApproved", CmsConstants.ProductStatus.Approved.name().equals(newProduct.getFields().getStatus()));
//        // 设置返回status状态
//        result.put("approveStatus", newProduct.getFields().getStatus());
        return result;
    }

    /**
     * 获取被切换类目的schema.
     */
    public CmsCategoryInfoBean getCategoryInfo(String categoryId) throws BusinessException {

        // TODO 将来应该是调用/product/group/numiid/delete直接删除，不需要报异常处理.
        // 现在的方案，先报错，让运营手动删除，然后删除cms对应数据.
        // 先是否已在售？
        // 提醒运营人员去天猫后台删除对应产品信息
        // 删除cms系统中对应的产品id.
        return categorySchemaService.getCategorySchemaByCatId(categoryId);
    }

    /**
     * 确认切换类目.
     * 更新选中商品的主类目，对应的平台的属性已经编写pAttributeStatus = 1，则不更新平台类目，如果=0则根据主类目对应的平台类目设置
     */
    public Map<String, Object> changeProductCategory(Map requestMap, UserSessionBean userInfo, CmsSessionBean cmsSession) {
        // 获取参数
        String mCatId = StringUtils.trimToNull((String) requestMap.get("catId"));
        String mCatPath = StringUtils.trimToNull((String) requestMap.get("catPath"));
//        List<Map> pCatList = (List) requestMap.get("pCatList");

        Map<String, Object> resultMap = new HashMap<>();
        if (mCatId == null || mCatPath == null) {
            $warn("切换类目 缺少参数 params=" + requestMap.toString());
            resultMap.put("isChangeCategory", false);
            return resultMap;
        }

        Integer isSelAll = (Integer) requestMap.get("isSelAll");
        if (isSelAll == null) {
            isSelAll = 0;
        }
        List<String> prodCodes = null;
        if (isSelAll == 1) {
            // 从高级检索重新取得查询结果（根据session中保存的查询条件）
            prodCodes = advanceSearchService.getProductCodeList(userInfo.getSelChannelId(), (Map<String, Object>) requestMap.get("searchInfo"));
        } else {
            prodCodes = (List<String>) requestMap.get("prodIds");
        }
        if (prodCodes == null || prodCodes.isEmpty()) {
            $error("切换类目 没有prod id条件 params=" + requestMap.toString());
            resultMap.put("isChangeCategory", false);
            return resultMap;
        }

        requestMap.put("productCodes", prodCodes);
        requestMap.put("userName", userInfo.getUserName());
        requestMap.put("channelId", userInfo.getSelChannelId());

        CmsBatchSetMainCategoryMQMessageBody messageBody = BeanUtils.toModel(requestMap, CmsBatchSetMainCategoryMQMessageBody.class);
        messageBody.setSender(userInfo.getUserName());
        senderService.sendMessage(messageBody);

        // 获取更新结果
        resultMap.put("isChangeCategory", true);
        return resultMap;
    }

    public Map<String, Object> refreshProductCategory(Map requestMap, UserSessionBean userInfo) {

        Map<String, Object> resultMap = new HashMap<>();

        Integer isSelAll = (Integer) requestMap.get("isSelAll");
        if (isSelAll == null) {
            isSelAll = 0;
        }
        List<String> prodCodes = null;
        if (isSelAll == 1) {
            // 从高级检索重新取得查询结果（根据session中保存的查询条件）
            prodCodes = advanceSearchService.getProductCodeList(userInfo.getSelChannelId(), (Map<String, Object>) requestMap.get("searchInfo"));
        } else {
            prodCodes = (List<String>) requestMap.get("prodIds");
        }
        if (prodCodes == null || prodCodes.isEmpty()) {
            $error("切换类目 没有prod id条件 params=" + requestMap.toString());
            resultMap.put("isChangeCategory", false);
            return resultMap;
        }
        requestMap.put("userName", userInfo.getUserName());
        requestMap.put("channelId", userInfo.getSelChannelId());
        List<List<String>> splitCodes = CommonUtil.splitList(prodCodes, 100);
        splitCodes.forEach(codes -> {
            requestMap.put("codeList", codes);
            sender.sendMessage(CmsMqRoutingKey.CMS_BATCH_CmsBatchRefreshMainCategoryJob, requestMap);
        });

        // 获取更新结果
        resultMap.put("isChangeCategory", true);
        return resultMap;
    }

    public Map<String, Object> getMastProductInfo(String channelId, Long prodId, String lang) {
        Map<String, Object> result = new HashMap<>();

        // 取得产品信息
        CmsBtProductModel cmsBtProduct = productService.getProductById(channelId, prodId);
        // 取得该商品的所在group的其他商品的图片
        CmsBtProductGroupModel cmsBtProductGroup = productGroupService.selectProductGroupByCode(channelId, cmsBtProduct.getCommon().getFields().getCode(), 0);
        List<Map<String, Object>> images = new ArrayList<>();
        final CmsBtProductGroupModel finalCmsBtProductGroup = cmsBtProductGroup;
        cmsBtProductGroup.getProductCodes().forEach(s1 -> {
            CmsBtProductModel product = cmsBtProduct.getCommon().getFields().getCode().equalsIgnoreCase(s1) ? cmsBtProduct : productService.getProductByCode(channelId, s1);
            if (product != null) {
                Map<String, Object> image = new HashMap<String, Object>();
                image.put("productCode", s1);
                String imageName = "";

                if (!ListUtils.isNull(product.getCommon().getFields().getImages1()) && product.getCommon().getFields().getImages1().get(0).size() > 0) {
                    imageName = (String) product.getCommon().getFields().getImages1().get(0).get("image1");
                }
                if (StringUtil.isEmpty(imageName) && !ListUtils.isNull(product.getCommon().getFields().getImages6()) && product.getCommon().getFields().getImages6().get(0).size() > 0) {
                    imageName = (String) product.getCommon().getFields().getImages6().get(0).get("image6");
                }
                image.put("imageName", imageName);
                image.put("isMain", finalCmsBtProductGroup.getMainProductCode().equalsIgnoreCase(s1));
                image.put("prodId", product.getProdId());
                image.put("qty", product.getCommon().getFields().getQuantity());
                images.add(image);
            }
        });

        List<Field> cmsMtCommonFields = commonSchemaService.getComSchemaModel().getFields();
        fillFieldOptions(cmsMtCommonFields, channelId, lang);
        CmsBtProductModel_Common productComm = cmsBtProduct.getCommon();

        String productType = productComm.getFields().getProductType();
        productComm.getFields().setProductType(StringUtil.isEmpty(productType) ? "" : productType.trim());
        String sizeType = productComm.getFields().getSizeType();
        productComm.getFields().setSizeType(StringUtil.isEmpty(sizeType) ? "" : sizeType.trim());

        if (productComm != null) {
            FieldUtil.setFieldsValueFromMap(cmsMtCommonFields, cmsBtProduct.getCommon().getFields());
            productComm.put("schemaFields", cmsMtCommonFields);
        }


        Map<String, Object> mastData = new HashMap<>();
        mastData.put("images", images);
        mastData.put("lock", cmsBtProduct.getLock());
        mastData.put("appSwitch", productComm.getFields().getAppSwitch());
        mastData.put("translateStatus", productComm.getFields().getTranslateStatus());
        mastData.put("isMain", cmsBtProductGroup.getMainProductCode().equalsIgnoreCase(cmsBtProduct.getCommon().getFields().getCode()));

        // 获取各个平台的状态
        List<Map<String, Object>> platformList = new ArrayList<>();
        if (cmsBtProduct.getPlatforms() != null) {
            cmsBtProduct.getPlatforms().forEach((s, platformInfo) -> {
                if (platformInfo.getCartId() == null || platformInfo.getCartId() == 0) {
                    return;
                }
                Map<String, Object> platformStatus = new HashMap<String, Object>();
                platformStatus.put("cartId", platformInfo.getCartId());
                platformStatus.put("pStatus", platformInfo.getpStatus());
                platformStatus.put("status", platformInfo.getStatus());
                platformStatus.put("pPublishError", platformInfo.getpPublishError());
                platformStatus.put("pNumIId", platformInfo.getpNumIId());
                platformStatus.put("cartName", CartEnums.Cart.getValueByID(platformInfo.getCartId() + ""));
                platformStatus.put("pReallyStatus", platformInfo.getpReallyStatus());
                platformStatus.put("pIsMain", platformInfo.getpIsMain());
                platformStatus.put("pPlatformMallId", platformInfo.getpPlatformMallId());
                platformList.add(platformStatus);
            });
        }
        mastData.put("platformList", platformList);

        mastData.put("feedInfo", productService.getCustomProp(cmsBtProduct));
        mastData.put("productCustomIsDisp", cmsBtProduct.getFeed().getProductCustomIsDisp());

        List<Map<String, Object>> inventoryList = getProdSkuCnt(channelId, prodId);
        mastData.put("inventoryList", inventoryList);

        result.put("productComm", productComm);
        result.put("mastData", mastData);
        return result;
    }

    public Map<String, Object> getMastProductSkuInfo(String channelId, Long prodId, String lang) {
        Map<String, Object> result = new HashMap<>();

        CmsBtProductModel cmsBtProduct = null;
        CmsBtFeedInfoModel feedInfoModel = null;

        // 取得产品信息
        cmsBtProduct = productService.getProductById(channelId, prodId);

        // 取得Feed信息
        if (cmsBtProduct != null) {
            feedInfoModel = feedInfoService.getProductByCode(channelId, StringUtils.isEmpty(cmsBtProduct.getCommon().getFields().getOriginalCode()) ? cmsBtProduct.getCommon().getFields().getCode() : cmsBtProduct.getCommon().getFields().getOriginalCode());
        }

        Map<String, Integer> skuinvs = null;
        WmsCodeStoreInvBean stockDetail = inventoryCenterLogicService.getCodeStockDetails(cmsBtProduct.getOrgChannelId(), cmsBtProduct.getCommon().getFields().getCode());
        if (stockDetail != null && !ListUtils.isNull(stockDetail.getStocks())) {
            skuinvs = stockDetail.getStocks().stream().map(WmsCodeStoreInvBean.StocksBean::getBase)
                    .collect(Collectors.toMap(sku -> channelId.equals("001") ? sku.getSku().toLowerCase() : sku.getSku(), WmsCodeStoreInvBean.StocksBean.BaseBean::getTotal));
        }

        List<Map<String, Object>> skuList = new ArrayList<>();
        for (CmsBtProductModel_Sku skuModel : cmsBtProduct.getCommon().getSkus()) {
            Map<String, Object> skuInfo = new HashMap<>();
            skuInfo.put("skuCode", skuModel.getSkuCode());
            skuInfo.put("qty", skuinvs == null || skuinvs.get(skuModel.getSkuCode()) == null ? 0 : skuinvs.get(skuModel.getSkuCode()));
            skuInfo.put("size", skuModel.getSize());
            skuInfo.put("barcode", skuModel.getBarcode());
            // 取得FeedInfo中的原始图片
            String imageUrl = "";
            if (feedInfoModel != null) {
                for (CmsBtFeedInfoModel_Sku feedSkuModel : feedInfoModel.getSkus()) {
                    if (feedSkuModel.getSku().equals(skuModel.getSkuCode())) {
                        // 如果图片在sku上，那么取得sku里的第一张图片
                        if (feedSkuModel.getImage() != null && feedSkuModel.getImage().size() > 0) {
                            imageUrl = feedSkuModel.getImage().get(0);
                        } else {
                            // 否则取得Code中的第一站图片
                            if (feedInfoModel.getImage() != null && feedInfoModel.getImage().size() > 0) {
                                imageUrl = feedInfoModel.getImage().get(0);
                            }
                        }
                        break;
                    }
                }
            }
            skuInfo.put("imageUrl", imageUrl);
            skuList.add(skuInfo);
        }


        result.put("skuList", skuList);
        return result;
    }

    /**
     * 更新产品共通属性
     *
     * @param channelId
     * @param prodId
     * @param commInfo
     * @param modifier
     * @return
     */
    public Map<String, Object> updateCommonProductInfo(String channelId, Long prodId, Map<String, Object> commInfo, String modifier) {

        List<Field> masterFields = buildMasterFields((List<Map<String, Object>>) commInfo.get("schemaFields"));

        commInfo.remove("schemaFields");
        CmsBtProductModel_Common commonModel = new CmsBtProductModel_Common(commInfo);
        commonModel.put("fields", FieldUtil.getFieldsValueToMap(masterFields));
        CmsBtProductModel oldProduct = productService.getProductById(channelId, prodId);

        //编辑前后主类目发生变化后重新触发feed->mast导入
        if (oldProduct.getCommon().getCatId() == null) oldProduct.getCommon().setCatId("");
        if (commonModel.getCatId() == null) commonModel.setCatId("");
        if (!oldProduct.getCommon().getCatId().equalsIgnoreCase(commonModel.getCatId())) {
            changeMastCategory(commonModel, oldProduct, modifier);

//            // 更新 feedinfo表中的updFlg 重新出发 feed->mast
//            HashMap<String, Object> paraMap = new HashMap<>(1);
//            paraMap.put("code", oldProduct.getCommon().getFields().getCode());
//            HashMap<String, Object> valueMap = new HashMap<>(1);
//            valueMap.put("updFlg", 0);
//            feedInfoService.updateFeedInfo(channelId, paraMap, valueMap);

        }
        //产品编辑页翻译状态从0-》1的场合 翻译时间 和人 设置
        if (!oldProduct.getCommon().getFields().getTranslateStatus().equalsIgnoreCase(commonModel.getFields().getTranslateStatus()) && "1".equalsIgnoreCase(commonModel.getFields().getTranslateStatus())) {
            commonModel.getFields().setTranslator(modifier);
            commonModel.getFields().setTranslateTime(DateTimeUtil.getNowTimeStamp());
        }

        /**保存时未设置翻译状态，询问后设置翻译状态补全翻译实践 added by piao */
        if ("1".equalsIgnoreCase(commonModel.getFields().getTranslateStatus())
                && commonModel.getFields().getTranslator().isEmpty()
                && commonModel.getFields().getTranslateTime().isEmpty()) {
            commonModel.getFields().setTranslator(modifier);
            commonModel.getFields().setTranslateTime(DateTimeUtil.getNowTimeStamp());
        }

        // 更新产品的共通属性
        Map<String, Object> result = productService.updateProductCommon(channelId, prodId, commonModel, modifier, true);

        CmsBtProductModel newProduct = productService.getProductById(channelId, prodId);

        String newHsCodePrivate = commonModel.getFields().getHsCodePrivate();
        String newHsCodeCross = commonModel.getFields().getHsCodeCross();
        String oldHsCodePrivate = oldProduct.getCommon().getFields().getHsCodePrivate();
        String oldHsCodeCross = oldProduct.getCommon().getFields().getHsCodeCross();

        // 如果税号发生变化,更新各平台价格
        if (!productService.compareHsCode(newHsCodePrivate, oldHsCodePrivate)
                || !productService.compareHsCode(newHsCodeCross, oldHsCodeCross)) {

            // 处理各平台价格
            newProduct.getPlatforms().forEach((s, platform) -> {
                Integer cartId = platform.getCartId();

                if (cartId < CmsConstants.ACTIVE_CARTID_MIN)
                    return;

                // 如果该平台使用的FORMULA计算价格,则跳过通过voRate的价格计算处理
                CmsChannelConfigBean priceCalculatorConfig = CmsChannelConfigs.getConfigBeanWithDefault(channelId, PRICE_CALCULATOR, cartId.toString());
                if (priceCalculatorConfig == null || PRICE_CALCULATOR_FORMULA.equals(priceCalculatorConfig.getConfigValue1()))
                    return;

                // 计算指导价
                try {
                    String msg = String.format("产品编辑页面-税号变更.变更前: %s, %s;变更后:%s,%s", oldHsCodePrivate, oldHsCodeCross, newHsCodePrivate, newHsCodeCross);
                    platformPriceService.updateProductPlatformPrice(newProduct, cartId, false, modifier, msg);
                    // 保存计算结果
                    JongoUpdate updObj2 = new JongoUpdate();
                    updObj2.setQuery("{'common.fields.code':#}");
                    updObj2.setQueryParameters(newProduct.getCommon().getFields().getCode());
                    updObj2.setUpdate("{$set:{'platforms.P#.skus':#}}");
                    updObj2.setUpdateParameters(cartId, newProduct.getPlatform(cartId).getSkus());
                    productService.updateFirstProduct(updObj2, channelId);
                } catch (PriceCalculateException e) {
                    throw new BusinessException("价格计算错误,cartId:" + cartId + e.getMessage());
                } catch (IllegalPriceConfigException e) {
                    throw new BusinessException("价格公式配置错误,价格计算失败cartId:" + cartId + e.getMessage());
                } catch (Throwable e) {
                    throw new BusinessException("各平台价格更新失败cartId:" + cartId + e.getMessage());
                }
            });

        }

//        if (!productService.compareHsCode(commonModel.getFields().getHsCodePrivate(), oldProduct.getCommon().getFields().getHsCodePrivate())
//                || !productService.compareHsCode(commonModel.getFields().getHsCodeCross(), oldProduct.getCommon().getFields().getHsCodeCross())) {
//            try {
//                // 税号从无到有的场后同步最终售价
//                if (StringUtil.isEmpty(oldProduct.getCommon().getFields().getHsCodePrivate())
//                        || StringUtil.isEmpty(oldProduct.getCommon().getFields().getHsCodeCross())){
//                    priceService.setPrice(newProduct, true);
//                } else{
//                    priceService.setPrice(newProduct, false);
//                }
//            } catch (PriceCalculateException e) {
//                throw new BusinessException("价格计算错误" + e.getMessage());
//            } catch (IllegalPriceConfigException e) {
//                // TODO 当捕获配置错误异常时, 需要停止渠道级别的计算
//                e.printStackTrace();
//                throw new BusinessException("价格公式配置错误,价格计算失败" + e.getMessage());
//            }
//
//            // 更新各平台的价格属性
//            newProduct.getPlatforms().forEach((s, platform) -> {
//                if (platform.getCartId() != 0) {
//
//                    if (platform.getSkus() != null
//                            && platform.getSkus().size() > 0
//                            && !platform.getSkus().get(0).getStringAttribute(priceRetail.name()).equalsIgnoreCase(platform.getSkus().get(0).getStringAttribute(confPriceRetail.name()))) {
//                        platform.getSkus().forEach(sku -> {
//                            sku.setAttribute(confPriceRetail.name(), sku.getDoubleAttribute(priceRetail.name()));
//                        });
//                        cmsBtPriceConfirmLogService.addConfirmed(channelId, newProduct.getCommon().getFields().getCode(), platform, modifier);
//                    }
//                    productService.updateProductPlatform(channelId, prodId, platform, modifier, false, EnumProductOperationType.WebEdit, "税号变更",true);
//                }
//            });
//
//        }
        return result;
    }

    private void changeMastCategory(CmsBtProductModel_Common commonModel, CmsBtProductModel oldProduct, String modifier) {
        List<CmsMtCategoryTreeAllModel_Platform> platformCategory = categoryTreeAllService.getCategoryByCatPath(commonModel.getCatPath()).getPlatformCategory();
        if (platformCategory == null || platformCategory.size() == 0) return;
        if (oldProduct.getPlatforms() == null) {
            return;
        }
        oldProduct.getPlatforms().forEach((cartId, platform) -> {
            if (platform.getCartId() != 0 && (platform.getFields() == null || platform.getFields().size() == 0) && platform.getCartId() != null) {
                List<CmsMtCategoryTreeAllModel_Platform> temp = platformCategory.stream().filter(item -> item.getPlatformId().equalsIgnoreCase(Carts.getCart(platform.getCartId()).getPlatform_id())).collect(Collectors.toList());
                if (temp != null && temp.size() > 0 && !StringUtil.isEmpty(temp.get(0).getCatId())) {
                    platform.setpCatId(temp.get(0).getCatId());
                    platform.setpCatPath(temp.get(0).getCatPath());
                    productPlatformService.updateProductPlatformWithSx(oldProduct.getChannelId(), oldProduct.getProdId(), platform, modifier, "变更主类目-关联平台类目变更", false, 1);
                }
            }
        });
    }

    /**
     * 获取 feed info model.
     */
    private Map<String, String> getCmsBtFeedInfoModel(String channelId, Long prodId, CmsBtProductModel productValueModel) {
        CmsBtFeedInfoModel feedInfoModel = feedInfoService.getProductByCode(channelId, productValueModel.getCommon().getFields().getOriginalCode() == null ? productValueModel.getCommon().getFields().getCode() : productValueModel.getCommon().getFields().getOriginalCode());
        Map<String, String> feedAttributes = new HashMap<>();
        if (feedInfoModel == null) {
            //feed 信息不存在时异常处理.
            String errMsg = "channel id: " + channelId + " product id: " + prodId + " 对应的品牌方信息不存在！";
            $warn(errMsg);
            return feedAttributes;
        }

        if (!StringUtils.isEmpty(feedInfoModel.getCategory())) {
            feedAttributes.put("category", feedInfoModel.getCategory());
        }

        if (!StringUtils.isEmpty(feedInfoModel.getCode())) {
            feedAttributes.put("code", feedInfoModel.getCode());
        }

        if (!StringUtils.isEmpty(feedInfoModel.getName())) {
            feedAttributes.put("name", feedInfoModel.getName());
        }

        if (!StringUtils.isEmpty(feedInfoModel.getModel())) {
            feedAttributes.put("model", feedInfoModel.getModel());
        }

        if (!StringUtils.isEmpty(feedInfoModel.getColor())) {
            feedAttributes.put("color", feedInfoModel.getColor());
        }

        if (!StringUtils.isEmpty(feedInfoModel.getOrigin())) {
            feedAttributes.put("origin", feedInfoModel.getOrigin());
        }

        if (!StringUtils.isEmpty(feedInfoModel.getSizeType())) {
            feedAttributes.put("sizeType", feedInfoModel.getSizeType());
        }

        if (!StringUtils.isEmpty(feedInfoModel.getBrand())) {
            feedAttributes.put("brand", feedInfoModel.getBrand());
        }

        if (!StringUtils.isEmpty(feedInfoModel.getWeight())) {
            feedAttributes.put("weight", feedInfoModel.getWeight());
        }

        if (!StringUtils.isEmpty(feedInfoModel.getShortDescription())) {
            feedAttributes.put("shortDescription", feedInfoModel.getShortDescription());
        }

        if (!StringUtils.isEmpty(feedInfoModel.getLongDescription())) {
            feedAttributes.put("longDescription", feedInfoModel.getLongDescription());
        }

        if (!StringUtils.isEmpty(String.valueOf(feedInfoModel.getUpdFlg()))) {
            feedAttributes.put("updFlg", String.valueOf(feedInfoModel.getUpdFlg()));
        }

        Map<String, List<String>> attributes = feedInfoModel.getAttribute();

        Map<String, String> attributesMap = new HashMap<>();

        for (Map.Entry<String, List<String>> entry : attributes.entrySet()) {

            StringBuilder valueStr = new StringBuilder();

            List<String> values = entry.getValue();

            if (values != null) {
                for (int i = 0; i < values.size(); i++) {
                    if (i < values.size() - 1) {
                        valueStr.append(values.get(i)).append("/");
                    } else {
                        valueStr.append(values.get(i));
                    }
                }
            }

            attributesMap.put(entry.getKey(), valueStr.toString());

        }

        feedAttributes.putAll(attributesMap);

        return feedAttributes;
    }

    /**
     * 构建sku schemaValue.
     */
    private Map<String, Object> buildSkuSchemaValue(CmsBtProductModel productValueModel, CmsMtCategorySchemaModel categorySchemaModel, Map<String, Integer> inventoryList) {
        List<Map<String, Object>> skuValueModel = new ArrayList<>();

        List<CmsBtProductModel_Sku> valueSkus = productValueModel.getCommon().getSkus();

        for (CmsBtProductModel_Sku model_sku : valueSkus) {
            model_sku.setQty(inventoryList.get(model_sku.getSkuCode()) == null ? 0 : inventoryList.get(model_sku.getSkuCode()));
            skuValueModel.add(model_sku);
        }

        Map<String, Object> skuSchemaValue = new HashMap<>();

        skuSchemaValue.put(categorySchemaModel.getSku().getId(), skuValueModel);

        return skuSchemaValue;
    }

    /**
     * 构建sku schema.
     */
    private List<Field> buildSkuSchema(CmsMtCategorySchemaModel categorySchemaModel) {

        List<Field> skuSchema = new ArrayList<>();
        Field skuField = categorySchemaModel.getSku();
        skuSchema.add(skuField);

        return skuSchema;
    }

    /**
     * 获取 master schema.
     */
    private CmsMtCategorySchemaModel getCmsMtCategorySchemaModel(String categoryId) {
        return categorySchemaService.getCmsMtCategorySchema(categoryId);
    }

    /**
     * 获取product model.
     */
    private CmsBtProductModel getProductModel(String channelId, Long prodId, int cartId) {
        CmsBtProductModel productValueModel = productService.getProductById(channelId, prodId);
        if (productValueModel == null) {
            //product 信息不存在时异常处理.
            String errMsg = "channel id: " + channelId + " product id: " + prodId + " 对应的产品信息不存在！";
            $error(errMsg);
            throw new BusinessException(errMsg);
        }
        return productValueModel;
    }

    /**
     * 构建CmsBtProductModel_Sku list.
     */
    private List<CmsBtProductModel_Sku> buildCmsBtProductModel_skus(Map skuFieldMap) {
        Field skuField = SchemaJsonReader.mapToField(skuFieldMap);

        Map<String, Object> skuFieldValueMap = new HashMap<>();

        skuField.getFieldValueToMap(skuFieldValueMap);

        List<Map> skuValuesMap = (List<Map>) skuFieldValueMap.get("sku");

        List<CmsBtProductModel_Sku> skuValues = new ArrayList<>();

        for (Map skuMap : skuValuesMap) {
            CmsBtProductModel_Sku skuModel = new CmsBtProductModel_Sku(skuMap);
            // 特殊处理qty不更新到数据库
            skuModel.remove("qty");
            skuValues.add(skuModel);
        }
        return skuValues;
    }

    /**
     * 构建masterFields.
     */
    private List<Field> buildMasterFields(List<Map<String, Object>> masterFieldsList) {

        List<Field> masterFields = SchemaJsonReader.readJsonForList(masterFieldsList);

        // setComplexValue
        for (Field field : masterFields) {

            if (field instanceof ComplexField) {
                ComplexField complexField = (ComplexField) field;
                List<Field> complexFields = complexField.getFields();
                ComplexValue complexValue = complexField.getComplexValue();
                setComplexValue(complexFields, complexValue);
            }

        }

        return masterFields;
    }

    /**
     * 构建 CmsBtProductModel_Field
     */
    private CmsBtProductModel_Field buildCmsBtProductModel_field(Map requestMap, List<Field> masterFields) {
        CmsBtProductModel_Field masterFieldsValue = new CmsBtProductModel_Field();

        if (requestMap.get("productStatus") != null) {
            Map status = (Map) requestMap.get("productStatus");
//            masterFieldsValue.setStatus(status.get("approveStatus").toString());
            masterFieldsValue.setTranslateStatus(status.get("translateStatus").toString());
//            masterFieldsValue.setEditStatus(status.get("editStatus").toString());
        }

        Map masterFieldsValueMap = FieldUtil.getFieldsValueToMap(masterFields);
        masterFieldsValue.putAll(masterFieldsValueMap);
        return masterFieldsValue;
    }

    /**
     * 构建 CmsBtProductModel_feed.
     */
    private CmsBtProductModel_Feed buildCmsBtProductModel_feed(Map<String, Object> customAttributesValue) {
        CmsBtProductModel_Feed feedModel = new CmsBtProductModel_Feed();
        BaseMongoMap<String, Object> orgAtts = new BaseMongoMap<>();
        BaseMongoMap<String, Object> cnAtts = new BaseMongoMap<>();

        Map<String, String> orgAttsList = (Map<String, String>) customAttributesValue.get("orgAtts");

        for (Map.Entry orgAtt : orgAttsList.entrySet()) {
            orgAtts.put(orgAtt.getKey().toString(), orgAtt.getValue());
        }

        Map<String, Object> cnAttsList = (Map<String, Object>) customAttributesValue.get("cnAtts");
        for (Map.Entry cnAtt : cnAttsList.entrySet()) {
            cnAtts.put(cnAtt.getKey().toString(), cnAtt.getValue());
        }

        List<String> customIds = (List<String>) customAttributesValue.get("customIds");

        List<String> customIdsCn = (List<String>) customAttributesValue.get("customIdsCn");

        feedModel.setCustomIds(customIds);
        feedModel.setCustomIdsCn(customIdsCn);
        feedModel.setOrgAtts(orgAtts);
        feedModel.setCnAtts(cnAtts);
        return feedModel;
    }

    /**
     * set complex value.
     */
    private void setComplexValue(List<Field> fields, ComplexValue complexValue) {

        for (Field fieldItem : fields) {

            complexValue.put(fieldItem);

            FieldTypeEnum fieldType = fieldItem.getType();

            switch (fieldType) {
                case INPUT:
                    InputField inputField = (InputField) fieldItem;
                    String inputValue = inputField.getValue();
                    complexValue.setInputFieldValue(inputField.getId(), inputValue);
                    break;
                case SINGLECHECK:
                    SingleCheckField singleCheckField = (SingleCheckField) fieldItem;
                    Value checkValue = singleCheckField.getValue();
                    complexValue.setSingleCheckFieldValue(singleCheckField.getId(), checkValue);
                    break;
                case MULTICHECK:
                    MultiCheckField multiCheckField = (MultiCheckField) fieldItem;
                    List<Value> checkValues = multiCheckField.getValues();
                    complexValue.setMultiCheckFieldValues(multiCheckField.getId(), checkValues);
                    break;
                case MULTIINPUT:
                    MultiInputField multiInputField = (MultiInputField) fieldItem;
                    List<String> inputValues = multiInputField.getStringValues();
                    complexValue.setMultiInputFieldValues(multiInputField.getId(), inputValues);
                    break;
                case COMPLEX:
                    ComplexField complexField = (ComplexField) fieldItem;
                    List<Field> subFields = complexField.getFields();
                    ComplexValue subComplexValue = complexField.getComplexValue();
                    setComplexValue(subFields, subComplexValue);
                    break;
                case MULTICOMPLEX:
                    MultiComplexField multiComplexField = (MultiComplexField) fieldItem;
                    List<ComplexValue> complexValueList = multiComplexField.getComplexValues();
                    complexValue.setMultiComplexFieldValues(multiComplexField.getId(), complexValueList);
                    break;

                default:
                    break;
            }

        }

    }

    /**
     * complex field值为空时设定默认值.
     */
    private void setDefaultComplexValues(List<Field> fields) {

        for (Field fieldItem : fields) {

            FieldTypeEnum fieldType = fieldItem.getType();

            switch (fieldType) {
                case COMPLEX:
                    ComplexField complexField = (ComplexField) fieldItem;
                    if (complexField.getComplexValue().getFieldMap().isEmpty() && complexField.getDefaultComplexValue().getFieldMap().isEmpty()) {

                        ComplexValue defComplexValue = new ComplexValue();
                        Map<String, Field> complexValueMap = new HashMap<>();
                        List<Field> complexFields = complexField.getFields();
                        setDefaultValueFieldMap(complexFields, complexValueMap);
                        defComplexValue.setFieldMap(complexValueMap);
                        complexField.setDefaultValue(defComplexValue);

                    }
                    break;
                case MULTICOMPLEX:
                    MultiComplexField multiComplexField = (MultiComplexField) fieldItem;
                    if (multiComplexField.getComplexValues().isEmpty() && multiComplexField.getDefaultComplexValues().isEmpty()) {
                        List<Field> complexFields = multiComplexField.getFields();
                        ComplexValue defComplexValue = new ComplexValue();
                        Map<String, Field> complexValueMap = new HashMap<>();
                        setDefaultValueFieldMap(complexFields, complexValueMap);
                        defComplexValue.setFieldMap(complexValueMap);
                        multiComplexField.addDefaultComplexValue(defComplexValue);
                    }
                    break;

                default:
                    break;
            }

        }

    }

    /**
     * 设定Field 的valueFieldMap.
     */
    private void setDefaultValueFieldMap(List<Field> fields, Map<String, Field> complexValueMap) {

        for (Field field : fields) {
            FieldTypeEnum type = field.getType();
            switch (type) {
                case INPUT:
                case SINGLECHECK:
                case MULTICHECK:
                case MULTIINPUT:
                    complexValueMap.put(field.getId(), field);
                    break;
                case COMPLEX:

                    ComplexField complexField = (ComplexField) field;

                    if (complexField.getComplexValue().getFieldMap().isEmpty() && complexField.getDefaultComplexValue().getFieldMap().isEmpty()) {

                        ComplexValue complexValue = new ComplexValue();

                        Map<String, Field> subComplexValueMap = new HashMap<>();

                        List<Field> subFields = complexField.getFields();

                        setDefaultValueFieldMap(subFields, subComplexValueMap);

                        complexValue.setFieldMap(subComplexValueMap);

                        complexField.setDefaultValue(complexValue);

                        complexValueMap.put(complexField.getId(), complexField);
                    }
                    break;
                case MULTICOMPLEX:
                    MultiComplexField multiComplexField = (MultiComplexField) field;
                    if (multiComplexField.getComplexValues().isEmpty() && multiComplexField.getDefaultComplexValues().isEmpty()) {
                        ComplexValue complexValue = new ComplexValue();
                        Map<String, Field> subComplexValueMap = new HashMap<>();
                        List<Field> subFields = multiComplexField.getFields();

                        setDefaultValueFieldMap(subFields, subComplexValueMap);

                        multiComplexField.addDefaultComplexValue(complexValue);
                        complexValueMap.put(multiComplexField.getId(), multiComplexField);

                    }
                    break;
                default:
                    break;
            }

        }
    }

    /**
     * 取得自定义属性的属性名称的中文翻译
     */
    private Map<String, String[]> getCustomAttributesCnAttsShow(String feedCategory, CmsBtProductModel_Feed feed, String channelId) {
        // 获取
        List<CmsMtFeedCustomPropModel> feedPropTranslateList = feedCustomPropService.getFeedCustomPropWithCategory(channelId, feedCategory);

        // 获取
        Map<String, String[]> result = new HashMap<>();
        for (CmsMtFeedCustomPropModel feedProp : feedPropTranslateList) {
            // 如果自定义属性包含在翻译的内容中
            if (feed.getCustomIds().contains(feedProp.getFeedPropOriginal())) {
                String[] cnAttWithTranslate = new String[2];
                cnAttWithTranslate[0] = feedProp.getFeedPropTranslation();
                cnAttWithTranslate[1] = feed.getCnAtts().containsKey(feedProp.getFeedPropOriginal()) ? feed.getCnAtts().get(feedProp.getFeedPropOriginal()).toString() : "";
                result.put(feedProp.getFeedPropOriginal(), cnAttWithTranslate);
            }
        }

        return result;
    }

    //获取切换主商品  的显示信息
    public Map<String, Object> getChangeMastProductInfo(GetChangeMastProductInfoParameter parameter) {
        Map<String, Object> result = new HashMap<>();
        CmsBtProductGroupModel cmsBtProductGroup = productGroupService.selectProductGroupByCode(parameter.getChannelId(), parameter.getProductCode(), parameter.getCartId());
        if (cmsBtProductGroup == null) return result;
        List<Map<String, Object>> productInfoList = new ArrayList<>();
        cmsBtProductGroup.getProductCodes().forEach(s1 -> {
            CmsBtProductModel product = productService.getProductByCode(parameter.getChannelId(), s1);
            if (product != null) {
                Map<String, Object> productInfo = new HashMap<String, Object>();
                productInfo.put("productCode", s1);

                String imageName = "";

                if (!ListUtils.isNull(product.getCommon().getFields().getImages1()) && product.getCommon().getFields().getImages1().get(0).size() > 0) {
                    imageName = (String) product.getCommon().getFields().getImages1().get(0).get("image1");
                }
                if (StringUtil.isEmpty(imageName) && !ListUtils.isNull(product.getCommon().getFields().getImages6()) && product.getCommon().getFields().getImages6().get(0).size() > 0) {
                    imageName = (String) product.getCommon().getFields().getImages6().get(0).get("image6");
                }
                productInfo.put("imageName", imageName);
                productInfo.put("isMain", cmsBtProductGroup.getMainProductCode().equalsIgnoreCase(s1));//common.fields.quantity   platforms.pXX.status
                productInfo.put("quantity", product.getCommon().getFields().getQuantity());
                productInfo.put("numIId", cmsBtProductGroup.getNumIId());
                CmsBtProductModel_Platform_Cart platForm = product.getPlatform(parameter.getCartId());
                if (platForm != null) {
                    productInfo.put("platFormStatus", platForm.getStatus());
                }
                productInfoList.add(productInfo);
            }
        });
        result.put("productInfoList", productInfoList);
        return result;
    }

    //设置主商品
    public void setMastProduct(SetMastProductParameter parameter, String modifier) {
//        CallResult result=new CallResult();
        CmsBtProductGroupModel cmsBtProductGroup = productGroupService.selectProductGroupByCode(parameter.getChannelId(), parameter.getProductCode(), parameter.getCartId());
        if (cmsBtProductGroup.getMainProductCode().equals(parameter.getProductCode())) return;

        List<String> codes = cmsBtProductGroup.getProductCodes().stream().filter(code -> !code.equalsIgnoreCase(cmsBtProductGroup.getMainProductCode()) && !code.equalsIgnoreCase(parameter.getProductCode())).collect(Collectors.toList());

        CmsBtProductModel cmsBtProductModel = productService.getProductByCode(parameter.getChannelId(), cmsBtProductGroup.getMainProductCode());
        CmsBtProductModel newCmsBtProductModel = productService.getProductByCode(parameter.getChannelId(), parameter.getProductCode());

        CmsBtProductModel_Platform_Cart platForm = cmsBtProductModel.getPlatform(parameter.getCartId());
        CmsBtProductModel_Platform_Cart newPlatForm = newCmsBtProductModel.getPlatform(parameter.getCartId());
        if (CmsConstants.ProductStatus.Approved.toString().equalsIgnoreCase(platForm.getStatus()) && !CmsConstants.ProductStatus.Approved.toString().equalsIgnoreCase(newPlatForm.getStatus())) {
            throw new BusinessException("只能设置状态为Approve的商品");
        }
        platForm.setpIsMain(0);// 把mainProduct的所对应的product表中对应的平台的pIsMain设0
        platForm.setMainProductCode(parameter.getProductCode());
        newPlatForm.setpIsMain(1);//把productCode的所对应的product表中对应的平台的pIsMain设1
        newPlatForm.setMainProductCode(parameter.getProductCode());
        cmsBtProductGroup.setMainProductCode(parameter.getProductCode());//把group表中的mainProduct替换成productCode
        cmsBtProductGroup.setModifier(modifier);
        cmsBtProductGroup.setModified(DateTimeUtil.getNowTimeStamp());
        productPlatformService.updateProductPlatformWithSx(parameter.getChannelId(), cmsBtProductModel.getProdId(), platForm, modifier, "切换主商品", false);
        productPlatformService.updateProductPlatformWithSx(parameter.getChannelId(), newCmsBtProductModel.getProdId(), newPlatForm, modifier, "切换主商品", false);

        codes.forEach(code -> {
            CmsBtProductModel product = productService.getProductByCode(parameter.getChannelId(), code);
            CmsBtProductModel_Platform_Cart pform = product.getPlatform(parameter.getCartId());
            pform.setMainProductCode(parameter.getProductCode());
            productPlatformService.updateProductPlatformWithSx(parameter.getChannelId(), product.getProdId(), pform, modifier, "切换主商品", false);
            String comment = "主商品发生变化 主商品：" + parameter.getProductCode();
            productStatusHistoryService.insert(parameter.getChannelId(), product.getCommon().getFields().getCode(), pform.getStatus(), parameter.getCartId(), EnumProductOperationType.ChangeMastProduct, comment, modifier);

        });


        productGroupService.update(cmsBtProductGroup);
        String comment = "取消主商品 主商品：" + parameter.getProductCode();
        productStatusHistoryService.insert(parameter.getChannelId(), cmsBtProductModel.getCommon().getFields().getCode(), platForm.getStatus(), parameter.getCartId(), EnumProductOperationType.ChangeMastProduct, comment, modifier);

        String newComment = "设置为主商品 主商品：" + parameter.getProductCode();
        productStatusHistoryService.insert(parameter.getChannelId(), newCmsBtProductModel.getCommon().getFields().getCode(), newPlatForm.getStatus(), parameter.getCartId(), EnumProductOperationType.ChangeMastProduct, newComment, modifier);
//        productService.updateProductPlatform()
//        1.1 根据 cartId和productCode找到对应的group
//        1.2 检查mainProduct和productCode是否一致
//        1.2.1 一致的场合 return
//                1.2.2 不一致的场合
//        1.2.2.1 判断 mainProduct的状态【status】
//        1.2.2.1.1 【status】= Approved时 productCode的状态必须也是Approved
//        1.2.2.1.2 【status】 != Approved时 productCode的状态不受限制
//        1.2.2.2 把mainProduct的所对应的product表中对应的平台的pIsMain设0 把productCode的所对应的product表中对应的平台的pIsMain设1
//        1.2.3 把group表中的mainProduct替换成productCode
//        1.2.4 调用插入workload表的共同方法
//
//        common.fields.image1
//        common.fields.quantity
//        platforms.pXX.status

    }

    //单品下架
    public void delisting(DelistingParameter parameter, String modifier) {
        CmsBtProductModel cmsBtProductModel = productService.getProductByCode(parameter.getChannelId(), parameter.getProductCode());
        CmsBtProductModel_Platform_Cart platForm = cmsBtProductModel.getPlatform(parameter.getCartId());
        if (platForm.getpIsMain() == 1) { //	是主商品的场合  抛出BusinessException  【该商品是主商品不能单一产品下线请切换主商品或者点击【平台商品删除】按钮
            new BusinessException("该商品是主商品不能单一产品下线请切换主商品或者点击【平台商品删除】按钮");
        }

        //  2.1.2	不是主商品的场合 把该商品所在的平台状态【status】=Ready  【pProductId】【pNumIId】【pStatus】清空
        platForm.setStatus(CmsConstants.ProductStatus.Ready.name());
        platForm.setpProductId("");
        platForm.setpNumIId("");
        // platForm.setpStatus(CmsConstants.PlatformStatus.);
        platForm.remove("pStatus");
        productPlatformService.updateProductPlatformWithSx(parameter.getChannelId(), cmsBtProductModel.getProdId(), platForm, modifier, "单品下线", false);
        String comment = parameter.getComment();
        productStatusHistoryService.insert(parameter.getChannelId(), cmsBtProductModel.getCommon().getFields().getCode(), platForm.getStatus(), parameter.getCartId(), EnumProductOperationType.Delisting, comment, modifier);

        //2.1.3	Voyageone_ims. ims_bt_product(mysql) 根据 channel cartId 和code找到对应的记录 把 numIId字段设为0
        ImsBtProductModel imsBtProductModel = imsBtProductDao.selectImsBtProductByChannelCartCode(parameter.getChannelId(), parameter.getCartId(), parameter.getProductCode());
        if (imsBtProductModel != null) {
            imsBtProductModel.setNumIid("");
            imsBtProductDao.updateImsBtProductBySeq(imsBtProductModel, modifier);
        }
        //    2	单一商品下线
//    2.1	根据 cartId和productCode检查该商品是否是主商品
//    2.1.1	是主商品的场合  抛出BusinessException  【该商品是主商品不能单一产品下线请切换主商品或者点击【平台商品删除】按钮
//    2.1.2	不是主商品的场合 把该商品所在的平台状态【status】=Ready  【pProductId】【pNumIId】【pStatus】清空
//    2.1.3	Voyageone_ims. ims_bt_product(mysql) 根据 channel cartId 和code找到对应的记录 把 numIId字段设为0
//    2.1.4	调用插入workload表的共同方法
    }

    //下架
    public void delistinGroup(DelistingParameter paramr, String modifier) {

        CmsBtProductGroupModel cmsBtProductGroup = productGroupService.selectProductGroupByCode(paramr.getChannelId(), paramr.getProductCode(), paramr.getCartId());
        String numIID = cmsBtProductGroup.getNumIId();
        if (paramr.getCartId() == 27)//jm不处理
        {
            return;
        }
        // 3.2 调用平台的删除商品的API
        productPlatformService.delPlatfromProduct(paramr.getChannelId(), paramr.getCartId(), numIID);
        //3.4 遍历group中的productCodes中的所有的code
        List<String> codes = cmsBtProductGroup.getProductCodes();
        codes.forEach(code -> {
            delistingCode(paramr, modifier, code);//下架单个code  3.4.1 Code【status】如果是Approve的场合改成Ready 【pProductId】【pNumIId】【pStatus】清空
        });
        cmsBtProductGroup.setNumIId("");
        cmsBtProductGroup.setPlatformPid("");
        cmsBtProductGroup.setPublishTime("");
        cmsBtProductGroup.setOnSaleTime("");
        cmsBtProductGroup.setInStockTime("");
        cmsBtProductGroup.setPlatformStatus(null);
        productGroupService.update(cmsBtProductGroup);//3.4.2 Group表中的【numIId】【platformPid】【publishTime】【onSaleTime】【inStockTime】清空
        //    3 平台商品删除 （只有在京东和天猫的平台才）
//    3.1 根据 cartId和productCode找到对应pNumIId
//    3.2 调用平台的删除商品的API
//    3.3 API调用成功 找到对应的group数据
//    3.4 遍历group中的productCodes中的所有的code
//    3.4.1 Code【status】如果是Approve的场合改成Ready 【pProductId】【pNumIId】【pStatus】清空
//    3.4.2 Group表中的【numIId】【platformPid】【publishTime】【onSaleTime】【inStockTime】清空
    }

    private void delistingCode(DelistingParameter paramr, String modifier, String code) {
        CmsBtProductModel cmsBtProductModel = productService.getProductByCode(paramr.getChannelId(), code);
        if (cmsBtProductModel != null) {
            CmsBtProductModel_Platform_Cart platForm = cmsBtProductModel.getPlatform(paramr.getCartId());
            String comment = String.format("NumIID【%s】，人工下线。备注:%s", platForm.getpNumIId(), paramr.getComment());
            platForm.setStatus(CmsConstants.ProductStatus.Ready.name());
            platForm.setpProductId("");
            platForm.setpNumIId("");
            platForm.remove("pStatus");
            productPlatformService.updateProductPlatformWithSx(paramr.getChannelId(), cmsBtProductModel.getProdId(), platForm, modifier, "group下线", false);
            productStatusHistoryService.insert(paramr.getChannelId(), cmsBtProductModel.getCommon().getFields().getCode(), platForm.getStatus(), paramr.getCartId(), EnumProductOperationType.DelistinGroup, comment, modifier);
            ImsBtProductModel imsBtProductModel = imsBtProductDao.selectImsBtProductByChannelCartCode(paramr.getChannelId(), paramr.getCartId(), code);
            if (imsBtProductModel != null) {
                imsBtProductModel.setNumIid("");
                imsBtProductDao.updateImsBtProductBySeq(imsBtProductModel, modifier);
            }
        }
    }

    public Map<Integer, Map<String, List<Double>>> hsCodeChg(String channelId, Long prodId, String hsCode) {

        CmsBtProductModel cmsBtProductModel = productService.getProductById(channelId, prodId);
        Map<Integer, Map<String, List<Double>>> prices = new HashMap<>();
        cmsBtProductModel.getPlatforms().forEach((s, platform) -> {
            if (platform.getCartId() != 0) {
                Map<String, List<Double>> price = new HashMap<String, List<Double>>();
                List<Double> priceRetail = new ArrayList<Double>();
                priceRetail.add(platform.getSkus().get(0).getDoubleAttribute("priceRetail"));
                price.put(platform.getSkus().get(0).getStringAttribute("skuCode"), priceRetail);
                prices.put(platform.getCartId(), price);
            }
        });
        cmsBtProductModel.getCommon().getFields().setHsCodePrivate(hsCode);
        try {
            priceService.setPrice(cmsBtProductModel, false);
        } catch (PriceCalculateException e) {
            // 当捕获计算错误时, 可以继续 code 级别的计算
            throw new BusinessException("价格计算错误" + e.getMessage());
        } catch (IllegalPriceConfigException e) {
            // TODO 当捕获配置错误异常时, 需要停止 code 级别的计算
            e.printStackTrace();
            // 当捕获计算错误时, 可以继续 code 级别的计算
            throw new BusinessException("价格计算错误" + e.getMessage());
        }
        cmsBtProductModel.getPlatforms().forEach((s, platform) -> {
            if (platform.getCartId() != 0 && platform.getCartId() != CartEnums.Cart.USJGJ.getValue()) {
                prices.get(platform.getCartId()).get(platform.getSkus().get(0).getStringAttribute("skuCode")).add(platform.getSkus().get(0).getDoubleAttribute("priceRetail"));

//                for (BaseMongoMap<String, Object> sku : platform.getSkus()) {
//                    if ("5".equalsIgnoreCase(sku.getStringAttribute("priceDiffFlg"))) {
//                        throw new BusinessException("税号修改后导致 中国最终售价低于指导价阀值请先修改最终销售价格！hscode调整后 指导价是：" + sku.getDoubleAttribute("priceRetail"));
//                    }
//                }
            }

        });
        return prices;
    }

    public Map<String, Object> copyPropertyFromMainProduct(String channelId, Long prodId, String lang) {
        CmsBtProductModel cmsBtProductModel = productService.getProductById(channelId, prodId);
        CmsBtProductModel_Common common = cmsBtProductModel.getCommon();

        CmsBtProductModel mainProduct = productService.getProductByCode(channelId, cmsBtProductModel.getPlatform(0).getMainProductCode());
        CmsBtProductModel_Common mainCommon = mainProduct.getCommon();


        if (StringUtil.isEmpty(common.getCatId())) {
            common.setCatId(mainCommon.getCatId());
        }

        if (StringUtil.isEmpty(common.getCatPath())) {
            common.setCatPath(mainCommon.getCatPath());
        }

        mainCommon.getFields().forEach((s, o) -> {
            if (common.getFields().containsKey(s)) {
                if (StringUtils.isEmpty(common.getFields().get(s).toString())) {
                    common.getFields().put(s, o);
                }
            } else {
                common.getFields().put(s, o);
            }
        });
        if ("1".equalsIgnoreCase(mainCommon.getFields().getHsCodeStatus())) {
            common.getFields().setHsCodeStatus("1");
        }
        if ("1".equalsIgnoreCase(mainCommon.getFields().getTranslateStatus())) {
            common.getFields().setTranslateStatus("1");
        }
        if ("1".equalsIgnoreCase(mainCommon.getFields().getCategoryStatus())) {
            common.getFields().setCategoryStatus("1");
        }
        List<Field> cmsMtCommonFields = commonSchemaService.getComSchemaModel().getFields();
        fillFieldOptions(cmsMtCommonFields, channelId, lang);
        FieldUtil.setFieldsValueFromMap(cmsMtCommonFields, common.getFields());
        common.put("schemaFields", cmsMtCommonFields);

        return common;
    }

    /**
     * 只更新平台价格,触发平台价格刷新.
     *
     * @param channelId
     * @param cartId
     * @param prodId
     * @param userName
     * @param platform
     * @throws Exception
     */
    public void updateSkuPrice(String channelId, Integer cartId, Long prodId, String userName, CmsBtProductModel_Platform_Cart platform) throws BusinessException {

        // 根据中国最终售价来判断 中国建议售价是否需要自动提高价格
        try {
            priceService.unifySkuPriceMsrp(platform.getSkus(), channelId, platform.getCartId());
        } catch (Exception e) {
            throw new BusinessException(String.format("修改平台商品价格　调用priceService.unifySkuPriceMsrp失败 channelId=%s, cartId=%s", channelId, platform.getCartId()), e);
        }

        //更新mongo数据
        HashMap<String, Object> queryMap = new HashMap<>();
        queryMap.put("prodId", prodId);

        List<BulkUpdateModel> bulkList = new ArrayList<>();
        HashMap<String, Object> updateMap = new HashMap<>();

        updateMap.put("platforms.P" + platform.getCartId() + ".modified", DateTimeUtil.getNowTimeStamp());
        updateMap.put("platforms.P" + platform.getCartId() + ".skus", platform.getSkus());
        BulkUpdateModel model = new BulkUpdateModel();
        model.setUpdateMap(updateMap);
        model.setQueryMap(queryMap);
        bulkList.add(model);
        cmsBtProductDao.bulkUpdateWithMap(channelId, bulkList, userName, "$set");

        // 刷新平台价格
        CmsBtProductModel newProduct = productService.getProductById(channelId, prodId);
        platformPriceService.publishPlatFormPrice(channelId, 2, newProduct, cartId, userName, true, false);

        //更新价格履历
        List<String> skus = new ArrayList<>();
        platform.getSkus().forEach(sku -> skus.add(sku.getStringAttribute("skuCode")));
        cmsBtPriceLogService.addLogForSkuListAndCallSyncPriceJob(skus, channelId, prodId, cartId, userName, "产品编辑页面,手动修改价格");
    }

    /**
     * 修改产品共通属性中的图片
     *
     * @param imageType 图片类型
     * @param images    图片集合
     * @return 系统当前时间
     */
    public String restoreImg(String channelId, Long prodId, String imageType, List<String> images, String modifier) {

        String modified = DateTimeUtil.getNowTimeStamp();
        HashMap<String, Object> queryMap = new HashMap<>();
        queryMap.put("prodId", prodId);

        List<BulkUpdateModel> bulkList = new ArrayList<>();
        HashMap<String, Object> updateMap = new HashMap<>();
        updateMap.put(String.format("common.fields.images%s", imageType.substring(imageType.length() - 1, imageType.length())), images);
        updateMap.put("common.modifier", modifier);

        BulkUpdateModel model = new BulkUpdateModel();
        model.setUpdateMap(updateMap);
        model.setQueryMap(queryMap);
        bulkList.add(model);

        cmsBtProductDao.bulkUpdateWithMap(channelId, bulkList, null, "$set");

        return modified;
    }

    /**
     * 修改产品PlateForm属性中的图片
     *
     * @param cartId    平台类型
     * @param imageType 图片类型
     * @param images    图片集合
     * @return 系统当前时间
     */
    public String restorePlatformImg(String channelId, Long prodId, String imageType, List<String> images, String modifier, Integer cartId) {

        String modified = DateTimeUtil.getNowTimeStamp();
        HashMap<String, Object> queryMap = new HashMap<>();
        queryMap.put("prodId", prodId);

        List<BulkUpdateModel> bulkList = new ArrayList<>();
        HashMap<String, Object> updateMap = new HashMap<>();
        updateMap.put(String.format("platforms.P%s.images%s", cartId, imageType.substring(imageType.length() - 1, imageType.length())), images);
        updateMap.put("common.modifier", modifier);

        BulkUpdateModel model = new BulkUpdateModel();
        model.setUpdateMap(updateMap);
        model.setQueryMap(queryMap);
        bulkList.add(model);

        cmsBtProductDao.bulkUpdateWithMap(channelId, bulkList, null, "$set");

        return modified;
    }

    /**
     * 修改产品originalTitleCn
     *
     * @param channelId       渠道ID
     * @param prodId          产品prodId
     * @param originalTitleCn 更新的originalTitleCn
     * @param username        操作人
     */
    public void updateOriginalTitleCn(String channelId, Long prodId, String originalTitleCn, String username) {

        CmsBtProductModel productModel = productService.getProductById(channelId, prodId);
        if (productModel == null) {
            throw new BusinessException(String.format("产品(%d)不存在", prodId));
        }

        JongoUpdate jongoUpdate = new JongoUpdate();
        jongoUpdate.setQuery("{\"prodId\": #}");
        jongoUpdate.setQueryParameters(productModel.getProdId());

        jongoUpdate.setUpdate("{$set: {\"common.fields.originalTitleCn\": #,\"modifier\":#,\"modified\":#}}");
        jongoUpdate.setUpdateParameters(originalTitleCn, username, DateTimeUtil.getNowTimeStamp());

        WriteResult writeResult = cmsBtProductDao.updateFirst(jongoUpdate, channelId);

        $info(String.format("(%s)更新产品(prodId=%d)originalTitleCn信息，更新结果:%s", username, prodId, JacksonUtil.bean2Json(writeResult)));

        String msg = "高级检索 单商品originalTitleCn设置" ;
        List<String> prodCodeList = new ArrayList<>();
        prodCodeList.add(productModel.getCommon().getFields().getCode());
        productStatusHistoryService.insertList(channelId, prodCodeList, -1, EnumProductOperationType.SingleProdSetOriginalTitleCn, msg, username);

        String code = productModel.getCommon().getFields().getCode();
        for (CmsBtProductModel_Platform_Cart platformCart : productModel.getPlatforms().values()) {
            Integer cartId = platformCart.getCartId();
            if (cartId.intValue() < 900 && CmsConstants.ProductStatus.Approved.name().equals(platformCart.getStatus())) {
                //取得approved的code插入
                $debug(String.format("商品(channel=%s, code=%s)Title修改，平台(cartId=%d)Approved需重新上新", channelId, code, platformCart.getCartId()));
                sxProductService.insertPlatformWorkload(channelId, platformCart.getCartId(), PlatformWorkloadAttribute.TITLE, Arrays.asList(code), username);
            }
        }
    }
}
