package com.voyageone.web2.cms.views.product;

import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.configs.CmsChannelConfigs;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.beans.CmsChannelConfigBean;
import com.voyageone.common.masterdate.schema.enums.FieldTypeEnum;
import com.voyageone.common.masterdate.schema.factory.SchemaJsonReader;
import com.voyageone.common.masterdate.schema.factory.SchemaReader;
import com.voyageone.common.masterdate.schema.field.*;
import com.voyageone.common.masterdate.schema.utils.FieldUtil;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.masterdate.schema.value.ComplexValue;
import com.voyageone.common.masterdate.schema.value.Value;
import com.voyageone.service.dao.cms.CmsMtBrandsMappingDao;
import com.voyageone.service.dao.cms.mongo.CmsMtPlatformCategorySchemaDao;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.CmsMtBrandsMappingModel;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategorySchemaModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform_Cart;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;
import com.voyageone.web2.base.BaseAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private CmsMtPlatformCategorySchemaDao cmsMtPlatformCategorySchemaDao;
    @Autowired
    private CmsMtBrandsMappingDao cmsMtBrandsMappingDao;

    /**
     * 获取产品平台信息
     *
     * @param channelId channelId
     * @param prodId    prodId
     * @param cartId    cartId
     * @return 产品平台信息
     */
    public Map<String, Object> getProductPlatform(String channelId, Long prodId, int cartId) {
        CmsBtProductModel cmsBtProduct = productService.getProductById(channelId, prodId);
        CmsBtProductModel_Platform_Cart platformCart = cmsBtProduct.getPlatform(cartId);

        if (platformCart != null && !StringUtil.isEmpty(platformCart.getpCatId())) {
            platformCart.setCartId(cartId);
            CmsMtPlatformCategorySchemaModel platformCategorySchemaModel;
            // JM的场合schema就一条
            if (cartId == Integer.parseInt(CartEnums.Cart.JM.getId())) {
                platformCategorySchemaModel = cmsMtPlatformCategorySchemaDao.selectPlatformCatSchemaModel("1", cartId);
            } else {
                platformCategorySchemaModel = cmsMtPlatformCategorySchemaDao.selectPlatformCatSchemaModel(platformCart.getpCatId(), cartId);
            }
            List<Field> fields = SchemaReader.readXmlForList(platformCategorySchemaModel.getPropsItem());
            BaseMongoMap<String, Object> fieldsValue = platformCart.getFields();
            if (fieldsValue != null) {
                FieldUtil.setFieldsValueFromMap(fields, fieldsValue);
            }
            platformCart.put("schemaFields", fields);
            // platform 品牌名
            if (StringUtil.isEmpty(platformCart.getpBrandId())) {
                Map<String, Object> parm = new HashMap<>();
                parm.put("channelId", channelId);
                parm.put("cartId", cartId);
                parm.put("cmsBrand", cmsBtProduct.getFields().getBrand());
                parm.put("active", 1);
                CmsMtBrandsMappingModel cmsMtBrandsMappingModel = cmsMtBrandsMappingDao.selectOne(parm);
                if (cmsMtBrandsMappingModel != null) {
                    platformCart.setpBrandId(cmsMtBrandsMappingModel.getBrandId());
                    platformCart.setpBrandName(cmsMtBrandsMappingModel.getCmsBrand());
                }
            }
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
        CmsBtProductGroupModel cmsBtProductGroup = productGroupService.selectProductGroupByCode(channelId, cmsBtProduct.getFields().getCode(), cartId);
        if (cmsBtProductGroup == null) {
            cmsBtProductGroup = productGroupService.selectProductGroupByCode(channelId, cmsBtProduct.getFields().getCode(), 0);
        }
        List<Map<String, Object>> images = new ArrayList<>();
        final CmsBtProductGroupModel finalCmsBtProductGroup = cmsBtProductGroup;
        cmsBtProductGroup.getProductCodes().forEach(s1 -> {
            CmsBtProductModel product = cmsBtProduct.getFields().getCode().equalsIgnoreCase(s1) ? cmsBtProduct : productService.getProductByCode(channelId, s1);
            if (product != null) {
                Map<String, Object> image = new HashMap<String, Object>();
                image.put("productCode", s1);
                image.put("imageName", product.getFields().getImages1().get(0).get("image1"));
                image.put("isMain", finalCmsBtProductGroup.getMainProductCode().equalsIgnoreCase(s1));
                images.add(image);
            }
        });

        mastData.put("productCode", cmsBtProduct.getFields().getCode());
        mastData.put("productName", StringUtil.isEmpty(cmsBtProduct.getFields().getProductNameCn()) ? cmsBtProduct.getFields().getProductNameEn() : cmsBtProduct.getFields().getProductNameCn());
        mastData.put("model", cmsBtProduct.getFields().getModel());
        mastData.put("groupId", cmsBtProductGroup.getGroupId());
        mastData.put("skus", cmsBtProduct.getSkus());
        mastData.put("isMain",finalCmsBtProductGroup.getMainProductCode().equalsIgnoreCase(cmsBtProduct.getFields().getCode()));

        // TODO 取得Sku的库存
        Map<String, Integer> skuInventoryList = productService.getProductSkuQty(channelId, cmsBtProduct.getFields().getCode());
        cmsBtProduct.getSkus().forEach(cmsBtProductModel_sku -> cmsBtProductModel_sku.setQty(skuInventoryList.get(cmsBtProductModel_sku.getSkuCode()) == null?0:skuInventoryList.get(cmsBtProductModel_sku.getSkuCode())));

        if (cmsBtProduct.getCommon().getFields() != null) {
            mastData.put("translateStatus", cmsBtProduct.getFields().getTranslateStatus());
            mastData.put("hsCodeStatus", StringUtil.isEmpty(cmsBtProduct.getFields().getHsCodePrivate())?0:1);
        }
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
    public Map<String, Object> changePlatformCategory(String channelId, Long prodId, int cartId, String catId) {
        CmsBtProductModel cmsBtProduct = productService.getProductById(channelId, prodId);
        CmsBtProductModel_Platform_Cart platformCart = cmsBtProduct.getPlatform(cartId);
        if (platformCart != null) {
            CmsMtPlatformCategorySchemaModel platformCategorySchemaModel;
            if (cartId == Integer.parseInt(CartEnums.Cart.JM.getId())) {
                platformCategorySchemaModel = cmsMtPlatformCategorySchemaDao.selectPlatformCatSchemaModel("1", cartId);
            } else {
                platformCategorySchemaModel = cmsMtPlatformCategorySchemaDao.selectPlatformCatSchemaModel(catId, cartId);
            }
            List<Field> fields = SchemaReader.readXmlForList(platformCategorySchemaModel.getPropsItem());
            BaseMongoMap<String, Object> fieldsValue = platformCart.getFields();
            if (fieldsValue != null) {
                FieldUtil.setFieldsValueFromMap(fields, fieldsValue);
            }
            platformCart.put("schemaFields", fields);
            platformCart.setpCatPath(platformCategorySchemaModel.getCatFullPath());
            platformCart.setpCatId(platformCategorySchemaModel.getCatId());
            // platform 品牌名
            if (StringUtil.isEmpty(platformCart.getpBrandId())) {
                Map<String, Object> parm = new HashMap<>();
                parm.put("channelId", channelId);
                parm.put("cartId", cartId);
                parm.put("cmsBrand", cmsBtProduct.getFields().getBrand());
                parm.put("active", 1);
                CmsMtBrandsMappingModel cmsMtBrandsMappingModel = cmsMtBrandsMappingDao.selectOne(parm);
                if (cmsMtBrandsMappingModel != null) {
                    platformCart.setpBrandId(cmsMtBrandsMappingModel.getBrandId());
                    platformCart.setpBrandName(cmsMtBrandsMappingModel.getCmsBrand());
                }
            }
        } else {
            platformCart = new CmsBtProductModel_Platform_Cart();
            CmsMtPlatformCategorySchemaModel platformCategorySchemaModel;
            if (cartId == Integer.parseInt(CartEnums.Cart.JM.getId())) {
                platformCategorySchemaModel = cmsMtPlatformCategorySchemaDao.selectPlatformCatSchemaModel("1", cartId);
            } else {
                platformCategorySchemaModel = cmsMtPlatformCategorySchemaDao.selectPlatformCatSchemaModel(catId, cartId);
            }
            List<Field> fields = SchemaReader.readXmlForList(platformCategorySchemaModel.getPropsItem());
            platformCart.put("schemaFields", fields);
            Map<String, Object> parm = new HashMap<>();
            parm.put("channelId", channelId);
            parm.put("cartId", cartId);
            parm.put("cmsBrand", cmsBtProduct.getFields().getBrand());
            parm.put("active", 1);
            CmsMtBrandsMappingModel cmsMtBrandsMappingModel = cmsMtBrandsMappingDao.selectOne(parm);
            if (cmsMtBrandsMappingModel != null) {
                platformCart.setpBrandId(cmsMtBrandsMappingModel.getBrandId());
                platformCart.setpBrandName(cmsMtBrandsMappingModel.getCmsBrand());
            }
            platformCart.setpCatPath(platformCategorySchemaModel.getCatFullPath());
            platformCart.setpCatId(catId);
        }
        return platformCart;
    }

    public String updateProductPlatform(String channelId, Long prodId, Map<String, Object> platform) {

        List<Field> masterFields = buildMasterFields((List<Map<String, Object>>) platform.get("schemaFields"));

        platform.put("fields", FieldUtil.getFieldsValueToMap(masterFields));
        platform.remove("schemaFields");
        CmsBtProductModel_Platform_Cart platformModel = new CmsBtProductModel_Platform_Cart(platform);

        return productService.updateProductPlatform(channelId, prodId, platformModel,true);

    }

    public String priceChk(String channelId, Long prodId, Map<String, Object> platform) {

        // 阀值
        CmsChannelConfigBean cmsChannelConfigBean = CmsChannelConfigs.getConfigBeanNoCode(channelId
                , CmsConstants.ChannelConfig.MANDATORY_BREAK_THRESHOLD);

        Double breakThreshold = null;
        if(cmsChannelConfigBean != null){
            breakThreshold = Double.parseDouble(cmsChannelConfigBean.getConfigValue1())/100D+1.0;
        }

        if(platform.get("skus") !=null ) {
            CmsBtProductModel cmsBtProduct = productService.getProductById(channelId, prodId);
            List<CmsBtProductModel_Sku> cmsBtProductModel_skus = cmsBtProduct.getSkus();
            Map<String, Double> comPrice = cmsBtProductModel_skus.stream().
                    collect(Collectors.toMap(CmsBtProductModel_Sku::getSkuCode, CmsBtProductModel_Sku::getPriceRetail));
            for (Map stringObjectBaseMongoMap : (List<Map<String, Object>>) platform.get("skus")) {
                String sku = (String) stringObjectBaseMongoMap.get("skuCode");
                Double newPriceSale = Double.parseDouble(stringObjectBaseMongoMap.get("priceSale").toString());
                if (comPrice.containsKey(sku) && comPrice.get(sku).compareTo(newPriceSale) > 0) {
                    return "4000091";
                }
                if (breakThreshold != null && comPrice.containsKey(sku) && ((Double) (comPrice.get(sku) * breakThreshold)).compareTo(newPriceSale) < 0) {
                    return "4000092";
                }
            }
        }
        return null;
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
}
