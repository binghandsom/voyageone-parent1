package com.voyageone.web2.cms.views.product;

import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.service.model.cms.enums.CartType;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.Channels;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.Types;
import com.voyageone.common.configs.beans.TypeBean;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.masterdate.schema.enums.FieldTypeEnum;
import com.voyageone.common.masterdate.schema.factory.SchemaJsonReader;
import com.voyageone.common.masterdate.schema.field.*;
import com.voyageone.common.masterdate.schema.option.Option;
import com.voyageone.common.masterdate.schema.utils.FieldUtil;
import com.voyageone.common.masterdate.schema.value.ComplexValue;
import com.voyageone.common.masterdate.schema.value.Value;
import com.voyageone.common.util.CommonUtil;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.cms.CmsCategoryInfoBean;
import com.voyageone.service.bean.cms.product.ProductUpdateBean;
import com.voyageone.service.dao.cms.CmsBtFeedCustomPropDao;
import com.voyageone.service.dao.cms.mongo.CmsBtFeedInfoDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.dao.cms.mongo.CmsMtCategorySchemaDao;
import com.voyageone.service.dao.cms.mongo.CmsMtCommonSchemaDao;
import com.voyageone.service.impl.cms.CategorySchemaService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.promotion.PromotionDetailService;
import com.voyageone.service.model.cms.CmsBtFeedCustomPropModel;
import com.voyageone.service.model.cms.CmsBtPromotionCodeModel;
import com.voyageone.service.model.cms.mongo.CmsMtCategorySchemaModel;
import com.voyageone.service.model.cms.mongo.CmsMtCommonSchemaModel;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.service.model.cms.mongo.product.*;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.cms.CmsConstants;
import com.voyageone.web2.cms.bean.CmsProductInfoBean;
import com.voyageone.web2.cms.bean.CustomAttributesBean;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.*;

/**
 * Created by lewis on 15-12-16.
 */
@Service
public class CmsProductDetailService extends BaseAppService {

    @Autowired
    private CmsMtCategorySchemaDao cmsMtCategorySchemaDao;

    @Autowired
    private CmsMtCommonSchemaDao cmsMtCommonSchemaDao;

    @Autowired
    private CmsBtFeedInfoDao cmsBtFeedInfoDao;

    @Autowired
    private CmsBtFeedCustomPropDao cmsBtFeedCustomPropDao;

    @Autowired
    private CmsBtProductDao cmsBtProductDao;

    @Autowired
    private ProductService productService;

    @Autowired
    protected CategorySchemaService categorySchemaService;

    @Autowired
    private PromotionDetailService promotionDetailService;

    private static final String FIELD_SKU_CARTS = "skuCarts";

    private static final String COMPLETE_STATUS = "1";

    /**
     * 获取类目以及类目属性信息.
     * 1.检查数据已经准备完成，batchField.switchCategory = 1时返回并告知运营正在准备数据，否则正常显示.
     *
     * @param channelId
     * @param prodId
     * @return
     * @throws BusinessException
     */
    public Map getProductInfo(String channelId, Long prodId, int cartId, String language) throws BusinessException {

        CmsProductInfoBean productInfo = new CmsProductInfoBean();

        //check the product data is ready.
        productInfo.setProductDataIsReady(cmsBtProductDao.checkProductDataIsReady(channelId, prodId));

        //自定义属性.
        CustomAttributesBean customAttributes = new CustomAttributesBean();

        // 获取product data.
        CmsBtProductModel productValueModel = getProductModel(channelId, prodId);

        //商品各种状态.
        CmsProductInfoBean.ProductStatus productStatus = productInfo.getProductStatusInstance();
        productStatus.setApproveStatus(productValueModel.getFields().getStatus());

        if (COMPLETE_STATUS.equals(productValueModel.getFields().getTranslateStatus())) {
            productStatus.setTranslateStatus(true);
        } else {
            productStatus.setTranslateStatus(false);
        }

//        if (COMPLETE_STATUS.equals(productValueModel.getFields().getEditStatus())) {
//            productStatus.setEditStatus(true);
//        } else {
//            productStatus.setEditStatus(false);
//        }

        //获取商品图片信息.
        Map<String, List<CmsBtProductModel_Field_Image>> productImages = new HashMap<>();
        productImages.put("image1",productValueModel.getFields().getImages(CmsBtProductConstants.FieldImageType.PRODUCT_IMAGE));
        productImages.put("image2",productValueModel.getFields().getImages(CmsBtProductConstants.FieldImageType.PACKAGE_IMAGE));
        productImages.put("image3",productValueModel.getFields().getImages(CmsBtProductConstants.FieldImageType.ANGLE_IMAGE));
        productImages.put("image4",productValueModel.getFields().getImages(CmsBtProductConstants.FieldImageType.CUSTOM_IMAGE));

        // 获取feed方数据.
        Map<String, String> feedInfoModel = getCmsBtFeedInfoModel(channelId, prodId, productValueModel);

        // 获取product 对应的 schema
        CmsMtCategorySchemaModel categorySchemaModel = getCmsMtCategorySchemaModel(productValueModel.getCatId());

        // 获取共通schema.
        CmsMtCommonSchemaModel comSchemaModel = getComSchemaModel();

        List<Field> comSchemaFields = comSchemaModel.getFields();

        this.fillFieldOptions(comSchemaFields, channelId, language);

        // 获取master schema.
        List<Field> masterSchemaFields = categorySchemaModel.getFields();

        // 向主数据schema 添加共通schema.
        masterSchemaFields.addAll(0, comSchemaFields);

        //获取主数据的值.
        Map masterSchemaValue = productValueModel.getFields();

        //填充master schema
        FieldUtil.setFieldsValueFromMap(masterSchemaFields, masterSchemaValue);

        //没有值的情况下设定complexField、MultiComplexField的默认值.
        setDefaultComplexValues(masterSchemaFields);

        //获取sku schema.
        List<Field> skuSchemaFields = this.buildSkuSchema(categorySchemaModel);

        MultiComplexField skuField = (MultiComplexField) skuSchemaFields.get(0);

        List<Field> subSkuFields = skuField.getFields();

        this.fillFieldOptions(subSkuFields, channelId, language);

        // TODO 取得Sku的库存
        Map<String, Integer> skuInventoryList = productService.getProductSkuQty(channelId, productValueModel.getFields().getCode());

        //获取sku schemaValue
        Map<String, Object> skuSchemaValue = buildSkuSchemaValue(productValueModel, categorySchemaModel, skuInventoryList);

        //填充sku schema.
        FieldUtil.setFieldsValueFromMap(skuSchemaFields, skuSchemaValue);

        //设置feed属性值
        customAttributes.setOrgAtts(productValueModel.getFeed().getOrgAtts());
        customAttributes.setCnAtts(productValueModel.getFeed().getCnAtts());
        customAttributes.setCustomIds(productValueModel.getFeed().getCustomIds());
        customAttributes.setCnAttsShow(getCustomAttributesCnAttsShow((String) feedInfoModel.get("category"), productValueModel.getFeed(), channelId));

        productInfo.setMasterFields(masterSchemaFields);
        productInfo.setChannelId(channelId);
        productInfo.setProductId(prodId);
        productInfo.setCategoryId(categorySchemaModel.getCatId());
        productInfo.setCategoryFullPath(categorySchemaModel.getCatFullPath());
        productInfo.setSkuFields(skuField);
        productInfo.setCustomAttributes(customAttributes);
        productInfo.setFeedInfoModel(feedInfoModel);
        productInfo.setProductImages(productImages);
        productInfo.setProductStatus(productStatus);
        productInfo.setModified(productValueModel.getModified());
        productInfo.setProductCode(productValueModel.getFields().getCode());

        Map infoMap = new HashMap();
        infoMap.put("productInfo", productInfo);

        ChannelConfigEnums.Channel channel = ChannelConfigEnums.Channel.valueOfId(productValueModel.getOrgChannelId());
        if (channel == null) {
            infoMap.put("orgChaName", "");
        } else {
            infoMap.put("orgChaName", channel.getFullName());
        }

        // 判断是否是minimall用户
        boolean isMiniMall = channelId.equals(ChannelConfigEnums.Channel.VOYAGEONE.getId());
        infoMap.put("isminimall", isMiniMall ? 1 : 0);

        boolean isMain = false;
        CmsBtProductModel_Group gpList = productValueModel.getGroups();
        if (gpList != null) {
            List<CmsBtProductModel_Group_Platform> pltList = gpList.getPlatforms();
            if (pltList != null && pltList.size() > 0) {
                for (CmsBtProductModel_Group_Platform pltObj : pltList) {
                    if (pltObj.getCartId() == cartId && pltObj.getIsMain()) {
                        isMain = true;
                        break;
                    }
                }
            }
        }
        infoMap.put("isMain", isMain ? 1 : 0);

        return infoMap;
    }

    /**
     * 取得Sku的库存
     *
     * @param channelId
     * @param prodId
     * @return
     */
    public List<Map<String, Object>> getProdSkuCnt(String channelId, Long prodId) {
        CmsBtProductModel prodObj = cmsBtProductDao.selectProductById(channelId, prodId);
        if (channelId.equals(ChannelConfigEnums.Channel.VOYAGEONE.getId())) {
            // 如果是mini mall店铺，则需要用原始channelId去检索库存信息
            channelId = prodObj.getOrgChannelId();
        }
        Map<String, Integer> skuList = productService.getProductSkuQty(channelId, prodObj.getFields().getCode());

        List<Map<String, Object>> inventoryList = new ArrayList<Map<String, Object>>(0);
        if (skuList == null || skuList.isEmpty()) {
            $info("当前商品没有Sku信息 prodId=" + prodId);
            return inventoryList;
        }

        for (Map.Entry<String, Integer> skuInv : skuList.entrySet()) {
            Map<String, Object> result = new HashMap<>();
            result.put("skucode", skuInv.getKey());
            result.put("skyqty", skuInv.getValue());
            inventoryList.add(result);
        }

        return inventoryList;
    }

    /**
     * 更新product values.
     *
     * @param channelId
     * @param user
     * @param requestMap
     */
    public String updateProductMasterInfo(String channelId, String user, Map requestMap) {

        List<Map<String, Object>> masterFieldsList = (List<Map<String, Object>>) requestMap.get("masterFields");

        Map<String, Object> customAttributesValue = (Map<String, Object>) requestMap.get("customAttributes");

        CmsBtProductModel productModel = new CmsBtProductModel(channelId);

        CmsBtProductModel_Feed feedModel = buildCmsBtProductModel_feed(customAttributesValue);

        List<Field> masterFields = buildMasterFields(masterFieldsList);

        CmsBtProductModel_Field masterFieldsValue = buildCmsBtProductModel_field(requestMap, masterFields);

        productModel.setCatId(requestMap.get("categoryId").toString());
        productModel.setProdId(Long.valueOf(requestMap.get("productId").toString()));
        productModel.setCatPath(requestMap.get("categoryFullPath").toString());
        productModel.setFields(masterFieldsValue);
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
     *
     * @param channelId
     * @param user
     * @param categoryId
     * @param productId
     * @param categoryFullPath
     * @param skuFieldMap
     */
    public String updateProductSkuInfo(String channelId, String user, String categoryId, Long productId, String modified, String categoryFullPath, Map skuFieldMap) {

        CmsBtProductModel productModel = new CmsBtProductModel(channelId);

        List<CmsBtProductModel_Sku> skuValues = buildCmsBtProductModel_skus(skuFieldMap);

        productModel.setCatId(categoryId);
        productModel.setProdId(productId);
        productModel.setCatPath(categoryFullPath);
        productModel.setSkus(skuValues);
        productModel.setModified(modified);

        ProductUpdateBean productUpdateBean = new ProductUpdateBean();
        productUpdateBean.setProductModel(productModel);
        productUpdateBean.setModifier(user);
        String newModified = DateTimeUtil.getNowTimeStamp();
        productUpdateBean.setModified(newModified);

        productService.updateProduct(channelId, productUpdateBean);

        return newModified;
    }

    /**
     * 保存全部产品信息.
     *
     * @param channelId
     * @param userName
     * @param requestMap
     * @return
     */
    public String updateProductAllInfo(String channelId, String userName, Map requestMap) {

        String categoryId = requestMap.get("categoryId").toString();
        Long productId = Long.valueOf(requestMap.get("productId").toString());
        String categoryFullPath = requestMap.get("categoryFullPath").toString();
        Map skuMap = (Map) requestMap.get("skuFields");
        String modified = requestMap.get("modified").toString();

        List<Map<String, Object>> masterFieldsList = (List<Map<String, Object>>) requestMap.get("masterFields");
        Map<String, Object> customAttributesValue = (Map<String, Object>) requestMap.get("customAttributes");
        List<CmsBtProductModel_Sku> skuValues = buildCmsBtProductModel_skus(skuMap);

        CmsBtProductModel productModel = new CmsBtProductModel(channelId);

        CmsBtProductModel_Feed feedModel = buildCmsBtProductModel_feed(customAttributesValue);

        List<Field> masterFields = buildMasterFields(masterFieldsList);

        CmsBtProductModel_Field masterFieldsValue = buildCmsBtProductModel_field(requestMap, masterFields);

        productModel.setCatId(categoryId);
        productModel.setProdId(productId);
        productModel.setCatPath(categoryFullPath);
        productModel.setFields(masterFieldsValue);
        productModel.setFeed(feedModel);
        productModel.setSkus(skuValues);
        productModel.setModified(modified);

        ProductUpdateBean productUpdateBean = new ProductUpdateBean();
        productUpdateBean.setProductModel(productModel);
        productUpdateBean.setModifier(userName);
        String newModified = DateTimeUtil.getNowTimeStamp();
        productUpdateBean.setModified(newModified);

        CmsBtProductModel oldProduct = productService.getProductById(channelId, productId);
        productService.updateProduct(channelId, productUpdateBean);
        CmsBtProductModel newProduct = productService.getProductById(channelId, productId);

        if (oldProduct.getFields().getPriceSaleEd().compareTo(newProduct.getFields().getPriceSaleEd()) != 0 || oldProduct.getFields().getPriceSaleSt().compareTo(newProduct.getFields().getPriceSaleSt()) != 0) {
            CmsBtPromotionCodeModel cmsBtPromotionCodeModel = new CmsBtPromotionCodeModel();
            cmsBtPromotionCodeModel.setProductId(productId);
            cmsBtPromotionCodeModel.setProductCode(oldProduct.getFields().getCode());
            cmsBtPromotionCodeModel.setPromotionPrice(newProduct.getFields().getPriceSaleEd());
            cmsBtPromotionCodeModel.setPromotionId(0);
            cmsBtPromotionCodeModel.setNumIid(oldProduct.getGroups().getPlatformByCartId(23).getNumIId());
            cmsBtPromotionCodeModel.setChannelId(channelId);
            cmsBtPromotionCodeModel.setCartId(23);
            cmsBtPromotionCodeModel.setModifier(userName);
            promotionDetailService.teJiaBaoPromotionUpdate(cmsBtPromotionCodeModel);
        }

        // Translation状态从完成-》未完成
        if ("1".equalsIgnoreCase(oldProduct.getFields().getTranslateStatus()) && "0".equalsIgnoreCase(newProduct.getFields().getTranslateStatus())) {
            Map<String, Object> updObj = new HashMap<>();
            updObj.put("fields.translateStatus", "0");
            updObj.put("fields.translateTime", DateTimeUtil.getNow(DateTimeUtil.DEFAULT_DATETIME_FORMAT));
            newProduct.getGroups().getPlatforms().forEach(cmsBtProductModel_group_platform -> {
                productService.updateTranslation(channelId, cmsBtProductModel_group_platform.getGroupId(), updObj, userName);
            });
        }

        return newModified;
    }

    /**
     * 获取被切换类目的schema.
     *
     * @param categoryId
     * @return
     * @throws BusinessException
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
     * 1.检查相关产品是否已经上架，如果在架就返回并提醒运营删除对应平台上的产品，否则继续
     *
     * @param requestMap
     * @return
     */
    public Map<String, Object> changeProductCategory(Map requestMap, UserSessionBean userSession, String language) {

        Object catIdObj = requestMap.get("catId");
        Object catPathObj = requestMap.get("catPath");
        Object prodIdObj = requestMap.get("prodIds");

        // check the parameters
        Assert.notEmpty(requestMap);

        Assert.notNull(catIdObj);
        Assert.notNull(catPathObj);
        Assert.notNull(prodIdObj);

        String categoryId = String.valueOf(catIdObj);

        String categoryPath = String.valueOf(catPathObj);

        Set<Long> productIds = new HashSet<Long>(CommonUtil.changeListType((List<Integer>) prodIdObj));

        // 取得products对应的所有的groupIds
        String[] projections = {"feed.orgAtts.modelCode", "groups"};
        List<CmsBtProductModel> products = productService.getList(userSession.getSelChannelId(), productIds, projections);

        // 获取groupId的数据
        List<String> models = new ArrayList<String>();
        Map<String, List<String>> numIids = new HashMap<>();
        for (CmsBtProductModel product : products) {

            // 获取所有model
            String model = product.getFeed().getOrgAtts().get("modelCode").toString();
            if (!models.contains(model))
                models.add(model);

            for (CmsBtProductModel_Group_Platform platform : product.getGroups().getPlatforms()) {
                // 获取已经上新的产品数据
                Integer cartId = Integer.valueOf(platform.getCartId().toString());
                String numIid = platform.getNumIId().toString();
                if (!StringUtils.isEmpty(numIid)) {
                    String cartName = CartType.getCartNameById(cartId, language);
                    if (numIids.get(cartName) != null) {
                        numIids.get(cartName).add(numIid);
                    } else {
                        List<String> tempList = new ArrayList<>();
                        tempList.add(numIid);
                        numIids.put(cartName, tempList);
                    }
                }
            }
        }

        Map<String, Object> resultMap = new HashMap<>();
        // 如果存在已经上新过的产品
        if (numIids.size() > 0) {
            resultMap.put("isChangeCategory", false);
            resultMap.put("publishInfo", numIids);
        }
        // 如果不存在已经上新过的产品
        else {
            Map<String, Object> response = productService.changeProductCategory(userSession.getSelChannelId(), categoryId, categoryPath, models, userSession.getUserName());
            // 获取更新结果
            resultMap.put("isChangeCategory", true);
            resultMap.put("updFeedInfoCount", response.get("updFeedInfoCount"));
            resultMap.put("updProductCount", response.get("updProductCount"));
            resultMap.put("updateCount", response.get("modifiedCount"));
        }

        return resultMap;
    }

    /**
     * 获取 feed info model.
     *
     * @param channelId
     * @param prodId
     * @param productValueModel
     * @return
     */
    private Map<String, String> getCmsBtFeedInfoModel(String channelId, Long prodId, CmsBtProductModel productValueModel) {
        CmsBtFeedInfoModel feedInfoModel = cmsBtFeedInfoDao.selectProductByCode(channelId, productValueModel.getFields().getCode());
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

        if (!StringUtils.isEmpty(feedInfoModel.getShort_description())) {
            feedAttributes.put("short_description", feedInfoModel.getShort_description());
        }

        if (!StringUtils.isEmpty(feedInfoModel.getLong_description())) {
            feedAttributes.put("long_description", feedInfoModel.getLong_description());
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
     *
     * @param productValueModel
     * @param categorySchemaModel
     * @return
     */
    private Map<String, Object> buildSkuSchemaValue(CmsBtProductModel productValueModel, CmsMtCategorySchemaModel categorySchemaModel, Map<String, Integer> inventoryList) {
        List<Map<String, Object>> skuValueModel = new ArrayList<>();

        List<CmsBtProductModel_Sku> valueSkus = productValueModel.getSkus();

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
     *
     * @param categorySchemaModel
     * @return
     */
    private List<Field> buildSkuSchema(CmsMtCategorySchemaModel categorySchemaModel) {

        List<Field> skuSchema = new ArrayList<>();
        Field skuField = categorySchemaModel.getSku();
        skuSchema.add(skuField);

        return skuSchema;
    }

    /**
     * 获取 master schema.
     *
     * @param categoryId
     * @return
     */
    private CmsMtCategorySchemaModel getCmsMtCategorySchemaModel(String categoryId) {

        CmsMtCategorySchemaModel schemaModel = cmsMtCategorySchemaDao.getMasterSchemaModelByCatId(categoryId);

        if (schemaModel == null) {
            // product 对应的schema信息不存在时的异常处理.
            String errMsg = "category id: " + categoryId + "对应的类目信息不存在！";
            $error(errMsg);
            throw new BusinessException(errMsg);
        }

        return schemaModel;
    }

    /**
     * 获取product model.
     *
     * @param channelId
     * @param prodId
     * @return
     */
    private CmsBtProductModel getProductModel(String channelId, Long prodId) {

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
     * 获取common schema.
     *
     * @return
     */
    private CmsMtCommonSchemaModel getComSchemaModel() {

        CmsMtCommonSchemaModel comSchemaModel = cmsMtCommonSchemaDao.getComSchema();

        if (comSchemaModel == null) {

            //common schema 不存在时异常处理.
            String errMsg = "共通schema（cms_mt_common_schema）的信息不存在！";

            $error(errMsg);

            throw new BusinessException(errMsg);
        }

        return comSchemaModel;
    }

    /**
     * 构建CmsBtProductModel_Sku list.
     *
     * @param skuFieldMap
     * @return
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
     *
     * @param masterFieldsList
     * @return
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
     *
     * @param requestMap
     * @param masterFields
     * @return
     */
    private CmsBtProductModel_Field buildCmsBtProductModel_field(Map requestMap, List<Field> masterFields) {
        CmsBtProductModel_Field masterFieldsValue = new CmsBtProductModel_Field();

        if (requestMap.get("productStatus") != null) {
            Map status = (Map) requestMap.get("productStatus");
            masterFieldsValue.setStatus(status.get("approveStatus").toString());
            masterFieldsValue.setTranslateStatus(status.get("translateStatus").toString());
//            masterFieldsValue.setEditStatus(status.get("editStatus").toString());
        }

        Map masterFieldsValueMap = FieldUtil.getFieldsValueToMap(masterFields);
        masterFieldsValue.putAll(masterFieldsValueMap);
        return masterFieldsValue;
    }

    /**
     * 构建 CmsBtProductModel_feed.
     *
     * @param customAttributesValue
     * @return
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

        feedModel.setOrgAtts(orgAtts);
        feedModel.setCnAtts(cnAtts);
        return feedModel;
    }

    /**
     * set complex value.
     *
     * @param fields
     * @param complexValue
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
     *
     * @param fields
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
     *
     * @param fields
     * @param complexValueMap
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
     * 填充field选项值.
     *
     * @param fields
     * @param channelId
     */
    private void fillFieldOptions(List<Field> fields, String channelId, String language) {

        for (Field field : fields) {

            if (CmsConstants.optionConfigType.OPTION_DATA_SOURCE.equals(field.getDataSource())
                    || CmsConstants.optionConfigType.OPTION_DATA_SOURCE_CHANNEL.equals(field.getDataSource())) {

                FieldTypeEnum type = field.getType();

                switch (type) {
                    case LABEL:
                        break;
                    case INPUT:
                        break;
                    case SINGLECHECK:
                    case MULTICHECK:
                        if (CmsConstants.optionConfigType.OPTION_DATA_SOURCE.equals(field.getDataSource())) {
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
                        } else if (CmsConstants.optionConfigType.OPTION_DATA_SOURCE_CHANNEL.equals(field.getDataSource())) {
                            // 获取type channel bean
                            List<TypeChannelBean> typeChannelBeanList = new ArrayList<>();
                            if (FIELD_SKU_CARTS.equals(field.getId())) {
                                typeChannelBeanList = TypeChannels.getTypeListSkuCarts(channelId, Constants.comMtTypeChannel.SKU_CARTS_53_A, language);
                            } else {
                                typeChannelBeanList = TypeChannels.getTypeWithLang(field.getId(), channelId, language);
                            }

                            // 替换成field需要的样式
                            List<Option> options = new ArrayList<>();
                            for (TypeChannelBean typeChannelBean : typeChannelBeanList) {
                                Option opt = new Option();
                                opt.setDisplayName(typeChannelBean.getName());
                                opt.setValue(typeChannelBean.getValue());
                                options.add(opt);
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
     * 取得自定义属性的属性名称的中文翻译
     *
     * @param feedCategory
     * @param feed
     * @param channelId
     * @return
     */
    private Map<String, String[]> getCustomAttributesCnAttsShow(String feedCategory, CmsBtProductModel_Feed feed, String channelId) {

        // 获取
        Map<String, Object> params = new HashMap<>();
        params.put("channelId", channelId);
        params.put("feedCatPath", feedCategory);
        List<CmsBtFeedCustomPropModel> feedPropTranslateList = cmsBtFeedCustomPropDao.selectWithCategory(params);

        // 获取
        Map<String, String[]> result = new HashMap<>();
        for (CmsBtFeedCustomPropModel feedProp : feedPropTranslateList) {
            // 如果自定义属性包含在翻译的内容中
            if (feed.getCustomIds().contains(feedProp.getFeedProp())) {
                String[] cnAttWithTranslate = new String[2];
                cnAttWithTranslate[0] = feedProp.getFeedPropTranslate();
                cnAttWithTranslate[1] = feed.getCnAtts().containsKey(feedProp.getFeedProp()) ? feed.getCnAtts().get(feedProp.getFeedProp()).toString() : "";
                result.put(feedProp.getFeedProp(), cnAttWithTranslate);
            }
        }

        return result;
    }
}
