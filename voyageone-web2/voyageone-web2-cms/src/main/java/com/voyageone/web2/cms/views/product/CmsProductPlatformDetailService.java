package com.voyageone.web2.cms.views.product;

import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.configs.CmsChannelConfigs;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.beans.CmsChannelConfigBean;
import com.voyageone.common.masterdate.schema.enums.FieldTypeEnum;
import com.voyageone.common.masterdate.schema.factory.SchemaJsonReader;
import com.voyageone.common.masterdate.schema.field.*;
import com.voyageone.common.masterdate.schema.utils.FieldUtil;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.masterdate.schema.value.ComplexValue;
import com.voyageone.common.masterdate.schema.value.Value;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.cms.product.CmsMtBrandsMappingBean;
import com.voyageone.service.impl.cms.CmsMtBrandService;
import com.voyageone.service.impl.cms.PlatformCategoryService;
import com.voyageone.service.impl.cms.PlatformSchemaService;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.tools.PlatformMappingService;
import com.voyageone.service.model.cms.CmsMtBrandsMappingModel;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategorySchemaModel;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategoryTreeModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform_Cart;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author james.li on 2016/6/3.
 * @version 2.0.0
 */
@Service
public class CmsProductPlatformDetailService extends BaseAppService {
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductGroupService productGroupService;
    @Autowired
    private PlatformSchemaService platformSchemaService;
    @Autowired
    private PlatformMappingService platformMappingService;
    @Autowired
    private PlatformCategoryService platformCategoryService;
    @Autowired
    private CmsMtBrandService cmsMtBrandService;
    @Autowired
    private SxProductService sxProductService;

    /**
     * 获取产品平台信息
     *
     * @param channelId channelId
     * @param prodId    prodId
     * @param cartId    cartId
     * @return 产品平台信息
     */
    public Map<String, Object> getProductPlatform(String channelId, Long prodId, int cartId, String language) {
        CmsBtProductModel cmsBtProduct = productService.getProductById(channelId, prodId);
        CmsBtProductModel_Platform_Cart platformCart = cmsBtProduct.getPlatform(cartId);
        if (platformCart == null) {
            platformCart = new CmsBtProductModel_Platform_Cart();
            platformCart.setCartId(cartId);
        }

        // platform 品牌名
//        if (StringUtil.isEmpty(platformCart.getpBrandId()) || StringUtil.isEmpty(platformCart.getpBrandName())) {
            if (cartId != CartEnums.Cart.USJGJ.getValue()
                    && cartId != CartEnums.Cart.USJGY.getValue()
                    && cartId != CartEnums.Cart.USJGT.getValue()) {
                Map<String, Object> parm = new HashMap<>();
                parm.put("channelId", channelId);
                parm.put("cartId", cartId);
                parm.put("cmsBrand", cmsBtProduct.getCommon().getFields().getBrand());
                parm.put("active", 1);
                CmsMtBrandsMappingBean cmsMtBrandsMappingModel = cmsMtBrandService.getModelByMap(parm);
                if (cmsMtBrandsMappingModel != null) {
                    platformCart.setpBrandId(cmsMtBrandsMappingModel.getBrandId());
                    platformCart.setpBrandName(cmsMtBrandsMappingModel.getpBrand());
                }
            }

        if (cartId != CartEnums.Cart.USJGJ.getValue()
                && cartId != CartEnums.Cart.USJGY.getValue()
                && cartId != CartEnums.Cart.USJGT.getValue()) {
            // 非主商品的平台类目跟这个主商品走
            if (platformCart.getpIsMain() != 1 && cartId != CartEnums.Cart.JM.getValue()) {
                CmsBtProductGroupModel cmsBtProductGroup = productGroupService.selectProductGroupByCode(channelId, cmsBtProduct.getCommon().getFields().getCode(), cartId);
                if (cmsBtProductGroup == null) {
                    throw new BusinessException(CartEnums.Cart.getValueByID(cartId + "") + "该商品的没有设置主商品，请先设置主商品：" + cmsBtProduct.getCommon().getFields().getCode());
                }
                CmsBtProductModel mainProduct = productService.getProductByCode(channelId, cmsBtProductGroup.getMainProductCode());
                CmsBtProductModel_Platform_Cart mainPlatform = mainProduct.getPlatform(cartId);
                if (mainPlatform == null || StringUtil.isEmpty(mainPlatform.getpCatId())) {
                    throw new BusinessException(CartEnums.Cart.getValueByID(cartId + "") + "该商品的主商品类目没有设置，请先设置主商品：" + mainProduct.getCommon().getFields().getCode());
                }
                platformCart.setpCatPath(mainPlatform.getpCatPath());
                platformCart.setpCatId(mainPlatform.getpCatId());
            }

            if(platformCart.getFields() == null) platformCart.setFields(new BaseMongoMap<>());
            platformCart.put("schemaFields", getSchemaFields(platformCart.getFields(), platformCart.getpCatId(), channelId, cartId, prodId, language,null));
        }
        return platformCart;
    }

    /**
     * 获取产品的基础数据给平台展示用
     *
     * @param channelId
     * @param prodId
     * @param cartId
     * @return
     */
    public Map<String, Object> getProductMastData(String channelId, Long prodId, int cartId) {
        Map<String, Object> mastData = new HashMap<>();
        // 取得产品信息
        CmsBtProductModel cmsBtProduct = productService.getProductById(channelId, prodId);
        // 取得该商品的所在group的其他商品的图片
        CmsBtProductGroupModel cmsBtProductGroup = productGroupService.selectProductGroupByCode(channelId, cmsBtProduct.getCommon().getFields().getCode(), cartId);
        if (cmsBtProductGroup == null) {
            cmsBtProductGroup = productGroupService.selectProductGroupByCode(channelId, cmsBtProduct.getCommon().getFields().getCode(), 0);
        }
        List<Map<String, Object>> images = new ArrayList<>();
        final CmsBtProductGroupModel finalCmsBtProductGroup = cmsBtProductGroup;
        cmsBtProductGroup.getProductCodes().forEach(s1 -> {
            CmsBtProductModel product = cmsBtProduct.getCommon().getFields().getCode().equalsIgnoreCase(s1) ? cmsBtProduct : productService.getProductByCode(channelId, s1);
            if (product != null) {
                Map<String, Object> image = new HashMap<String, Object>();
                image.put("productCode", s1);
                image.put("imageName", product.getCommon().getFields().getImages1().get(0).get("image1"));
                image.put("isMain", finalCmsBtProductGroup.getMainProductCode().equalsIgnoreCase(s1));
                image.put("prodId", product.getProdId());
                images.add(image);
            }
        });

        mastData.put("productCode", cmsBtProduct.getCommon().getFields().getCode());
        mastData.put("productName", StringUtil.isEmpty(cmsBtProduct.getCommon().getFields().getProductNameEn()) ? cmsBtProduct.getCommon().getFields().getProductNameEn() : cmsBtProduct.getCommon().getFields().getOriginalTitleCn());
        mastData.put("model", cmsBtProduct.getCommon().getFields().getModel());
        mastData.put("groupId", cmsBtProductGroup.getGroupId());
        mastData.put("skus", cmsBtProduct.getCommon().getSkus());
        mastData.put("isMain", finalCmsBtProductGroup.getMainProductCode().equalsIgnoreCase(cmsBtProduct.getCommon().getFields().getCode()));
        Map<String, String> sizeMap = sxProductService.getSizeMap(channelId, cmsBtProduct.getCommon().getFields().getBrand(), cmsBtProduct.getCommon().getFields().getProductType(), cmsBtProduct.getCommon().getFields().getSizeType());
        if (sizeMap != null && sizeMap.size() > 0) {
            cmsBtProduct.getCommon().getSkus().forEach(sku -> {
                sku.setAttribute("platformSize",sizeMap.get(sku.getSize()));
            });
        }

        // TODO 取得Sku的库存
        String skuChannelId = StringUtils.isEmpty(cmsBtProduct.getOrgChannelId()) ? channelId : cmsBtProduct.getOrgChannelId();
        Map<String, Integer> skuInventoryList = productService.getProductSkuQty(skuChannelId, cmsBtProduct.getCommon().getFields().getCode());
        cmsBtProduct.getCommon().getSkus().forEach(cmsBtProductModel_sku -> cmsBtProductModel_sku.setQty(skuInventoryList.get(cmsBtProductModel_sku.getSkuCode()) == null ? 0 : skuInventoryList.get(cmsBtProductModel_sku.getSkuCode())));

//        if (cmsBtProduct.getCommon().getFields() != null) {
//            mastData.put("translateStatus", cmsBtProduct.getCommon().getFields().getTranslateStatus());
//            mastData.put("hsCodeStatus", StringUtil.isEmpty(cmsBtProduct.getCommon().getFields().getHsCodePrivate()) ? 0 : 1);
//        }
        mastData.put("images", images);
        return mastData;
    }

    /**
     * 平台类目变更
     *
     * @param channelId
     * @param prodId
     * @param cartId
     * @param catId
     * @return
     */
    public Map<String, Object> changePlatformCategory(String channelId, Long prodId, int cartId, String catId, String catPath, String language) {
        CmsBtProductModel cmsBtProduct = productService.getProductById(channelId, prodId);
        CmsBtProductModel_Platform_Cart platformCart = cmsBtProduct.getPlatform(cartId);
        if (platformCart != null) {
            if(platformCart.getFields() == null) platformCart.setFields(new BaseMongoMap<>());
            platformCart.put("schemaFields", getSchemaFields(platformCart.getFields(), catId, channelId, cartId, prodId, language, catPath));
            platformCart.setpCatId(catId);
            // platform 品牌名
//            if (StringUtil.isEmpty(platformCart.getpBrandId()) || StringUtil.isEmpty(platformCart.getpBrandName())) {
                Map<String, Object> parm = new HashMap<>();
                parm.put("channelId", channelId);
                parm.put("cartId", cartId);
                parm.put("cmsBrand", cmsBtProduct.getCommon().getFields().getBrand());
                parm.put("active", 1);
                CmsMtBrandsMappingBean cmsMtBrandsMappingModel = cmsMtBrandService.getModelByMap(parm);
                if (cmsMtBrandsMappingModel != null) {
                    platformCart.setpBrandId(cmsMtBrandsMappingModel.getBrandId());
                    platformCart.setpBrandName(cmsMtBrandsMappingModel.getpBrand());
                }
//            }
        } else {
            platformCart = new CmsBtProductModel_Platform_Cart();
            if(platformCart.getFields() == null) platformCart.setFields(new BaseMongoMap<>());
            platformCart.put("schemaFields", getSchemaFields(platformCart.getFields(), catId, channelId, cartId, prodId, language, catPath));

            Map<String, Object> parm = new HashMap<>();
            parm.put("channelId", channelId);
            parm.put("cartId", cartId);
            parm.put("cmsBrand", cmsBtProduct.getCommon().getFields().getBrand());
            parm.put("active", 1);
            CmsMtBrandsMappingBean cmsMtBrandsMappingModel = cmsMtBrandService.getModelByMap(parm);
            if (cmsMtBrandsMappingModel != null) {
                platformCart.setpBrandId(cmsMtBrandsMappingModel.getBrandId());
                platformCart.setpBrandName(cmsMtBrandsMappingModel.getpBrand());
            }
            platformCart.setpCatId(catId);
        }
        return platformCart;
    }

    public String updateProductPlatform(String channelId, Long prodId, Map<String, Object> platform, String modifier) {

        if (platform.get("schemaFields") != null) {
            List<Field> masterFields = buildMasterFields((Map<String, Object>) platform.get("schemaFields"));

            platform.put("fields", FieldUtil.getFieldsValueToMap(masterFields));
            platform.remove("schemaFields");
        }
        CmsBtProductModel_Platform_Cart platformModel = new CmsBtProductModel_Platform_Cart(platform);

        return productService.updateProductPlatform(channelId, prodId, platformModel, modifier, true);

    }

    public String priceChk(String channelId, Long prodId, Map<String, Object> platform) {

        // 阀值
        CmsChannelConfigBean cmsChannelConfigBean = CmsChannelConfigs.getConfigBeanNoCode(channelId
                , CmsConstants.ChannelConfig.MANDATORY_BREAK_THRESHOLD);

        Double breakThreshold = null;
        if (cmsChannelConfigBean != null) {
            breakThreshold = Double.parseDouble(cmsChannelConfigBean.getConfigValue1()) / 100D + 1.0;
        }

        if (platform.get("skus") != null) {
            CmsBtProductModel cmsBtProduct = productService.getProductById(channelId, prodId);
            List<BaseMongoMap<String, Object>> cmsBtProductModel_skus = cmsBtProduct.getPlatform((int) platform.get("cartId")).getSkus();

            Map<String, Double> comPrice = new HashMap<>();
            cmsBtProductModel_skus.forEach(item -> comPrice.put(item.getStringAttribute("skuCode"), item.getDoubleAttribute("priceRetail")));

            for (Map stringObjectBaseMongoMap : (List<Map<String, Object>>) platform.get("skus")) {
                String sku = (String) stringObjectBaseMongoMap.get("skuCode");
                if (stringObjectBaseMongoMap.get("priceSale") == null || StringUtil.isEmpty(stringObjectBaseMongoMap.get("priceSale").toString())) {
                    throw new BusinessException("价格不能为空");
                }
                Double newPriceSale = Double.parseDouble(stringObjectBaseMongoMap.get("priceSale").toString());
                if (breakThreshold != null && comPrice.containsKey(sku) && ((Double) (newPriceSale / (2 - breakThreshold))).compareTo(comPrice.get(sku)) < 0) {
                    throw new BusinessException("4000094", ((Double) Math.ceil(comPrice.get(sku) * (2 - breakThreshold))).intValue());
                }

                if (comPrice.containsKey(sku) && comPrice.get(sku).compareTo(newPriceSale) > 0) {
                    throw new BusinessException("4000091");
                }
                if (breakThreshold != null && comPrice.containsKey(sku) && ((Double) (comPrice.get(sku) * breakThreshold)).compareTo(newPriceSale) < 0) {
                    throw new BusinessException("4000092");
                }
            }
        }
        return null;
    }

    /**
     * 构建masterFields.
     */
    private List<Field> buildMasterFields(Map<String, Object> masterFieldsList) {

        List<Map<String, Object>> item = new ArrayList<>();
        if (masterFieldsList.get(PlatformSchemaService.KEY_ITEM) != null) {
            item.addAll((Collection<? extends Map<String, Object>>) masterFieldsList.get(PlatformSchemaService.KEY_ITEM));
        }
        if (masterFieldsList.get(PlatformSchemaService.KEY_PRODUCT) != null) {
            item.addAll((Collection<? extends Map<String, Object>>) masterFieldsList.get(PlatformSchemaService.KEY_PRODUCT));
        }
        List<Field> masterFields = SchemaJsonReader.readJsonForList(item);

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

    private Map<String, List<Field>> getSchemaFields(BaseMongoMap<String, Object> fieldsValue, String catId, String channelId, Integer cartId, Long productId, String language, String catPath) {
        Map<String, List<Field>> fields = null;

        // 从mapping 来的默认值合并到商品属性中
        Map<String, Object> mppingFields = platformMappingService.getValueMap(channelId, productId, cartId, catPath);

        setDefaultValue(fieldsValue, mppingFields);

        // JM的场合schema就一条
        if (cartId == Integer.parseInt(CartEnums.Cart.JM.getId())) {
            if (!StringUtil.isEmpty(catId)) {
                fields = platformSchemaService.getFieldForProductImage("1", channelId, cartId, language);
            }
        } else {
            fields = platformSchemaService.getFieldForProductImage(catId, channelId, cartId, language);
        }
        if (fieldsValue != null && fields != null && fields.get(PlatformSchemaService.KEY_ITEM) != null) {
            FieldUtil.setFieldsValueFromMap(fields.get(PlatformSchemaService.KEY_ITEM), fieldsValue);
        }
        if (fieldsValue != null && fields != null && fields.get(PlatformSchemaService.KEY_PRODUCT) != null) {
            FieldUtil.setFieldsValueFromMap(fields.get(PlatformSchemaService.KEY_PRODUCT), fieldsValue);
        }
        return fields;
    }

    public Map<String, Object> copyPropertyFromMainProduct(String channelId, Long prodId, Integer cartId, String language) {
        CmsBtProductModel cmsBtProductModel = productService.getProductById(channelId, prodId);
        CmsBtProductModel_Platform_Cart platform = cmsBtProductModel.getPlatform(cartId);

        CmsBtProductModel mainProduct = productService.getProductByCode(channelId, platform.getMainProductCode());
        CmsBtProductModel_Platform_Cart mainPlatform = mainProduct.getPlatform(cartId);

        if (CmsConstants.ProductStatus.Pending.toString().equalsIgnoreCase(mainPlatform.getStatus())) {
            throw new BusinessException("主商品没有编辑完成 请先编辑主商品");
        }

        platform.setpCatId(mainPlatform.getpCatId());
        platform.setpCatPath(mainPlatform.getpCatPath());
        if(platform.getFields() == null) platform.setFields(new BaseMongoMap<>());
        mainPlatform.getFields().forEach((s, o) -> {
            if (platform.getFields().containsKey(s)) {
                if (StringUtils.isEmpty(platform.getFields().get(s).toString())) {
                    // 天猫的场合 属性ID是 sku darwin_sku不复制
                    if (cartId == CartEnums.Cart.TG.getValue() && !"sku".equalsIgnoreCase(s) && !"darwin_sku".equalsIgnoreCase(s)) {
                        platform.getFields().put(s, o);
                    }
                }
            } else {
                platform.getFields().put(s, o);
            }
        });

        platform.put("schemaFields", getSchemaFields(platform.getFields(), platform.getpCatId(), channelId, cartId, prodId, language, null));

        return platform;
    }

    /**
     * 获取平台所有类目
     *
     * @param user   用户配置
     * @param cartId 平台 ID
     * @return 类目集合
     */
    List<CmsMtPlatformCategoryTreeModel> getPlatformCategories(UserSessionBean user, Integer cartId) {
        return platformCategoryService.getPlatformCategories(user.getSelChannelId(), cartId);
    }

    /**
     * 从共同属性mapping来的属性合并
     *
     * @param fieldMap
     * @param valueMap
     */
    public void setDefaultValue(Map<String, Object> fieldMap, Map<String, Object> valueMap) {
        if (valueMap == null || valueMap.size() == 0) return;
        valueMap.forEach((s, v) -> {
            Object o = fieldMap.get(s);
            if (o == null) {
                fieldMap.put(s, v);
            } else if (o instanceof List) {
                if (((List) o).size() == 0) {
                    fieldMap.put(s, v);
                }
            } else if (o instanceof Map) {
                setDefaultValue((Map<String, Object>) o, (Map<String, Object>) v);
            } else if (StringUtil.isEmpty((String) o)) {
                fieldMap.put(s, v);
            }
        });
    }
}
