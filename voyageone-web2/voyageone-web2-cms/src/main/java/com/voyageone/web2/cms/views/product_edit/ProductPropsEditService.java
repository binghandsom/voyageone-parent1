package com.voyageone.web2.cms.views.product_edit;

import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.cms.service.CmsProductService;
import com.voyageone.cms.service.dao.mongodb.CmsBtFeedInfoDao;
import com.voyageone.cms.service.dao.mongodb.CmsMtCategorySchemaDao;
import com.voyageone.cms.service.dao.mongodb.CmsMtCommonSchemaDao;
import com.voyageone.cms.service.model.*;
import com.voyageone.common.configs.TypeChannel;
import com.voyageone.common.masterdate.schema.enums.FieldTypeEnum;
import com.voyageone.common.masterdate.schema.factory.SchemaJsonReader;
import com.voyageone.common.masterdate.schema.field.*;
import com.voyageone.common.masterdate.schema.option.Option;
import com.voyageone.common.masterdate.schema.utils.FieldUtil;
import com.voyageone.common.masterdate.schema.value.ComplexValue;
import com.voyageone.common.masterdate.schema.value.Value;
import com.voyageone.common.util.StringUtils;
import com.voyageone.web2.cms.bean.CustomAttributesBean;
import com.voyageone.web2.cms.bean.ProductInfoBean;
import com.voyageone.web2.sdk.api.VoApiDefaultClient;
import com.voyageone.web2.sdk.api.request.ProductUpdateRequest;
import com.voyageone.web2.sdk.api.service.ProductSdkClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lewis on 15-12-16.
 */
@Service
public class ProductPropsEditService {

    @Autowired
    private CmsMtCategorySchemaDao cmsMtCategorySchemaDao;

    Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private CmsProductService cmsProductService;

    @Autowired
    private CmsMtCommonSchemaDao cmsMtCommonSchemaDao;

    @Autowired
    private CmsBtFeedInfoDao cmsBtFeedInfoDao;

    @Autowired
    protected VoApiDefaultClient voApiClient;

    @Autowired
    protected ProductSdkClient productClient;

    private static final String optionDataSource = "optConfig";

    private static final String completeStatus = "1";

    public ProductInfoBean getProductInfo(String channelId, int prodId) throws BusinessException{

        ProductInfoBean productInfo = new ProductInfoBean();

        //自定义属性.
        CustomAttributesBean customAttributes = new CustomAttributesBean();

        // 获取product data.
        CmsBtProductModel productValueModel = getProductModel(channelId, prodId);

        //商品各种状态.
        ProductInfoBean.ProductStatus productStatus = productInfo.getProductStatusInstance();
        productStatus.setApproveStatus(productValueModel.getFields().getStatus());

        if (completeStatus.equals(productValueModel.getFields().getTranslateStatus())){
            productStatus.setTranslateStatus(true);
        }else {
            productStatus.setTranslateStatus(false);
        }
        if (completeStatus.equals(productValueModel.getFields().getEditStatus())){
            productStatus.setEditStatus(true);
        }else {
            productStatus.setEditStatus(false);
        }

        //获取商品图片信息.
        List<CmsBtProductModel_Field_Image> productImages = productValueModel.getFields().getImages(CmsBtProductConstants.FieldImageType.PRODUCT_IMAGE);

        // 获取feed方数据.
        Map<String,String> feedInfoModel = getCmsBtFeedInfoModel(channelId, prodId, productValueModel);

        // 获取product 对应的 schema
        CmsMtCategorySchemaModel categorySchemaModel = getCmsMtCategorySchemaModel(productValueModel);

        // 获取共通schema.
        CmsMtComSchemaModel comSchemaModel = getComSchemaModel();

        List<Field> comSchemaFields = comSchemaModel.getFields();

        this.fillFieldOptions(comSchemaFields,channelId);

        // 获取master schema.
        List<Field> masterSchemaFields = categorySchemaModel.getFields();

        // 向主数据schema 添加共通schema.
        masterSchemaFields.addAll(comSchemaFields);

        //获取主数据的值.
        Map masterSchemaValue =  productValueModel.getFields();

        //填充master schema
        FieldUtil.setFieldsValueFromMap(masterSchemaFields,masterSchemaValue);

        //没有值的情况下设定complexField、MultiComplexField的默认值.
        setDefaultComplexValues(masterSchemaFields);

        //获取sku schema.
        List<Field> skuSchemaFields = this.buildSkuSchema(categorySchemaModel);

        MultiComplexField skuField = (MultiComplexField)skuSchemaFields.get(0);

        List<Field> subSkuFields = skuField.getFields();

        this.fillFieldOptions(subSkuFields,channelId);

        //获取sku schemaValue
        Map<String, Object> skuSchemaValue = buildSkuSchemaValue(productValueModel, categorySchemaModel);

        //填充sku schema.
        FieldUtil.setFieldsValueFromMap(skuSchemaFields,skuSchemaValue);

        //设置feed属性值
        customAttributes.setOrgAtts(productValueModel.getFeed().getOrgAtts());
        customAttributes.setCnAtts(productValueModel.getFeed().getCnAtts());
        customAttributes.setCustomIds(productValueModel.getFeed().getCustomIds());

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

        return productInfo;
    }



    /**
     * 更新product values.
     * @param channelId
     * @param user
     * @param requestMap
     */
    public String updateProductMasterInfo(String channelId, String user, Map requestMap){

        List<Map<String,Object>> masterFieldsList = (List<Map<String,Object>>) requestMap.get("masterFields");

        Map<String,Object> customAttributesValue =(Map<String,Object>) requestMap.get("customAttributes");

        ProductUpdateRequest updateRequest = new ProductUpdateRequest(channelId);
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

        updateRequest.setProductModel(productModel);
        updateRequest.setModifier(user);
        updateRequest.setModified(requestMap.get("modified").toString());

        return productClient.updateProductRetModified(updateRequest);

    }

    /**
     * 更新product values.
     * @param channelId
     * @param user
     * @param categoryId
     * @param productId
     * @param categoryFullPath
     * @param skuFieldMap
     */
    public String updateProductSkuInfo(String channelId,String user,String categoryId,Long productId,String modified,String categoryFullPath, Map skuFieldMap){

        ProductUpdateRequest updateRequest = new ProductUpdateRequest(channelId);

        CmsBtProductModel productModel = new CmsBtProductModel(channelId);

        List<CmsBtProductModel_Sku> skuValues = buildCmsBtProductModel_skus(skuFieldMap);

        productModel.setCatId(categoryId);
        productModel.setProdId(productId);
        productModel.setCatPath(categoryFullPath);
        productModel.setSkus(skuValues);
        productModel.setModified(modified);

        updateRequest.setProductModel(productModel);
        updateRequest.setModifier(user);

        return productClient.updateProductRetModified(updateRequest);

    }

    /**
     * 保存全部产品信息.
     * @param channelId
     * @param userName
     * @param requestMap
     * @return
     */
    public String updateProductAllInfo(String channelId,String userName, Map requestMap){

        String categoryId = requestMap.get("categoryId").toString();
        Long productId = Long.valueOf(requestMap.get("productId").toString());
        String categoryFullPath = requestMap.get("categoryFullPath").toString();
        Map skuMap = (Map) requestMap.get("skuFields");
        String modified = requestMap.get("modified").toString();

        List<Map<String,Object>> masterFieldsList = (List<Map<String,Object>>) requestMap.get("masterFields");
        Map<String,Object> customAttributesValue =(Map<String,Object>) requestMap.get("customAttributes");
        List<CmsBtProductModel_Sku> skuValues = buildCmsBtProductModel_skus(skuMap);

        ProductUpdateRequest updateRequest = new ProductUpdateRequest(channelId);

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

        updateRequest.setProductModel(productModel);
        updateRequest.setModifier(userName);

        return productClient.updateProductRetModified(updateRequest);

    }

    /**
     * 获取 feed info model.
     * @param channelId
     * @param prodId
     * @param productValueModel
     * @return
     */
    private Map<String,String> getCmsBtFeedInfoModel(String channelId, int prodId, CmsBtProductModel productValueModel) {

        CmsBtFeedInfoModel feedInfoModel = cmsBtFeedInfoDao.selectProductByCode(channelId,productValueModel.getFields().getCode());

        if (feedInfoModel == null){
            //feed 信息不存在时异常处理.
            String errMsg = "channel id: " + channelId +" product id: " +prodId+" 对应的品牌方信息不存在！";

            logger.warn(errMsg);

        }

        Map<String,String> feedAttributes = new HashMap<>();


        if (!StringUtils.isEmpty(feedInfoModel.getCode())){
            feedAttributes.put("code",feedInfoModel.getCode());
        }

        if (!StringUtils.isEmpty(feedInfoModel.getName())){
            feedAttributes.put("name",feedInfoModel.getName());
        }

        if (!StringUtils.isEmpty(feedInfoModel.getModel())){
            feedAttributes.put("model",feedInfoModel.getModel());
        }

        if (!StringUtils.isEmpty(feedInfoModel.getColor())){
            feedAttributes.put("color",feedInfoModel.getColor());
        }

        if (!StringUtils.isEmpty(feedInfoModel.getOrigin())){
            feedAttributes.put("origin",feedInfoModel.getOrigin());
        }

        if (!StringUtils.isEmpty(feedInfoModel.getSizeType())){
            feedAttributes.put("sizeType",feedInfoModel.getSizeType());
        }

        if (!StringUtils.isEmpty(feedInfoModel.getBrand())){
            feedAttributes.put("brand",feedInfoModel.getBrand());
        }

        if (!StringUtils.isEmpty(feedInfoModel.getWeight())){
            feedAttributes.put("weight",feedInfoModel.getWeight());
        }

        if (!StringUtils.isEmpty(feedInfoModel.getShort_description())){
            feedAttributes.put("short_description",feedInfoModel.getShort_description());
        }

        if (!StringUtils.isEmpty(feedInfoModel.getLong_description())){
            feedAttributes.put("long_description",feedInfoModel.getLong_description());
        }

        if (!StringUtils.isEmpty(String.valueOf(feedInfoModel.getUpdFlg()))){
            feedAttributes.put("updFlg",String.valueOf(feedInfoModel.getUpdFlg()));
        }

        Map<String,List<String>> attributes = feedInfoModel.getAttribute();

        Map<String,String> attributesMap = new HashMap<>();

        for (Map.Entry<String,List<String>> entry : attributes.entrySet()){

            StringBuilder valueStr = new StringBuilder();

            List<String> values = entry.getValue();

            if (values != null){
                for (int i=0;i<values.size();i++){
                    if (i<values.size()-1){
                        valueStr.append(values.get(i)).append("/");
                    }else {
                        valueStr.append(values.get(i));
                    }
                }
            }

            attributesMap.put(entry.getKey(),valueStr.toString());

        }

        feedAttributes.putAll(attributesMap);

        return feedAttributes;
    }

    /**
     * 构建sku schemaValue.
     * @param productValueModel
     * @param categorySchemaModel
     * @return
     */
    private Map<String, Object> buildSkuSchemaValue(CmsBtProductModel productValueModel, CmsMtCategorySchemaModel categorySchemaModel) {
        List<Map<String,Object>> skuValueModel = new ArrayList<>();

        List<CmsBtProductModel_Sku> valueSkus = productValueModel.getSkus();

        for (CmsBtProductModel_Sku model_sku:valueSkus){
            skuValueModel.add(model_sku);
        }

        Map<String,Object> skuSchemaValue = new HashMap<>();

        skuSchemaValue.put(categorySchemaModel.getSku().getId(), skuValueModel);

        return skuSchemaValue;
    }


    /**
     * 构建sku schema.
     * @param categorySchemaModel
     * @return
     */
    private List<Field> buildSkuSchema(CmsMtCategorySchemaModel categorySchemaModel){

        List<Field> skuSchema = new ArrayList<>();
        Field skuField = categorySchemaModel.getSku();
        skuSchema.add(skuField);

        return skuSchema;
    }

    /**
     * 获取 master schema.
     * @param productValueModel
     * @return
     */
    private CmsMtCategorySchemaModel getCmsMtCategorySchemaModel(CmsBtProductModel productValueModel) {

        CmsMtCategorySchemaModel schemaModel = cmsMtCategorySchemaDao.getMasterSchemaModelByCatId(productValueModel.getCatId());

        if (schemaModel == null){
            // product 对应的schema信息不存在时的异常处理.
            String errMsg = "category id: " + productValueModel.getCatId() +"对应的类目信息不存在！";
            logger.error(errMsg);
            throw new BusinessException(errMsg);
        }



        return schemaModel;
    }

    /**
     * 获取product model.
     * @param channelId
     * @param prodId
     * @return
     */
    private CmsBtProductModel getProductModel(String channelId, int prodId) {

        CmsBtProductModel productValueModel = cmsProductService.getProductById(channelId, prodId);

        if (productValueModel == null){

            //product 信息不存在时异常处理.
            String errMsg = "channel id: " + channelId +" product id: " +prodId+" 对应的产品信息不存在！";

            logger.error(errMsg);

            throw new BusinessException(errMsg);
        }

        return productValueModel;
    }

    /**
     * 获取common schema.
     * @return
     */
    private CmsMtComSchemaModel getComSchemaModel() {
        CmsMtComSchemaModel comSchemaModel = cmsMtCommonSchemaDao.getComSchema();

        if (comSchemaModel == null){

            //common schema 不存在时异常处理.
            String errMsg = "共通schema（cms_mt_common_schema）的信息不存在！";

            logger.error(errMsg);

            throw new BusinessException(errMsg);
        }

        return comSchemaModel;
    }

    /**
     * 构建CmsBtProductModel_Sku list.
     * @param skuFieldMap
     * @return
     */
    private List<CmsBtProductModel_Sku> buildCmsBtProductModel_skus(Map skuFieldMap) {
        Field skuField = SchemaJsonReader.mapToField(skuFieldMap);

        Map<String,Object> skuFieldValueMap = new HashMap<>();

        skuField.getFieldValueToMap(skuFieldValueMap);

        List<Map> skuValuesMap = (List<Map>) skuFieldValueMap.get("sku");

        List<CmsBtProductModel_Sku> skuValues = new ArrayList<>();

        for (Map skuMap:skuValuesMap){
            CmsBtProductModel_Sku skuModel = new CmsBtProductModel_Sku(skuMap);
            skuValues.add(skuModel);
        }
        return skuValues;
    }

    /**
     * 构建masterFields.
     * @param masterFieldsList
     * @return
     */
    private List<Field> buildMasterFields(List<Map<String, Object>> masterFieldsList) {

        List<Field> masterFields = SchemaJsonReader.readJsonForList(masterFieldsList);

        // setComplexValue
        for (Field field:masterFields){

            if (field instanceof ComplexField){
                ComplexField complexField = (ComplexField)field;
                List<Field> complexFields = complexField.getFields();
                ComplexValue complexValue = complexField.getComplexValue();
                setComplexValue(complexFields,complexValue);
            }

        }

        return masterFields;
    }

    /**
     * 构建 CmsBtProductModel_Field
     * @param requestMap
     * @param masterFields
     * @return
     */
    private CmsBtProductModel_Field buildCmsBtProductModel_field(Map requestMap, List<Field> masterFields) {
        CmsBtProductModel_Field masterFieldsValue = new CmsBtProductModel_Field();

        if (requestMap.get("productStatus") != null){
            Map status = (Map) requestMap.get("productStatus");
            masterFieldsValue.setStatus(status.get("approveStatus").toString());
            masterFieldsValue.setTranslateStatus(status.get("translateStatus").toString());
            masterFieldsValue.setEditStatus(status.get("editStatus").toString());
        }

        Map masterFieldsValueMap = FieldUtil.getFieldsValueToMap(masterFields);
        masterFieldsValue.putAll(masterFieldsValueMap);
        return masterFieldsValue;
    }

    /**
     * 构建 CmsBtProductModel_feed.
     * @param customAttributesValue
     * @return
     */
    private CmsBtProductModel_Feed buildCmsBtProductModel_feed(Map<String, Object> customAttributesValue) {
        CmsBtProductModel_Feed feedModel = new CmsBtProductModel_Feed();
        BaseMongoMap<String, Object> orgAtts = new BaseMongoMap<>();
        BaseMongoMap<String, Object> cnAtts = new BaseMongoMap<>();

        List<String> customIds = new ArrayList<>();

        List<Map<String, String>> orgAttsList =(List<Map<String, String>>) customAttributesValue.get("orgAtts");
        for (Map<String, String> orgAttMap : orgAttsList) {

            orgAtts.put(orgAttMap.get("key"), orgAttMap.get("value"));

            Object selected = orgAttMap.get("selected");

            Boolean isSelected = (Boolean)selected;

            if (isSelected){
                customIds.add(orgAttMap.get("key"));
            }
        }

        List<Map<String, String>> cnAttsList =(List<Map<String, String>>) customAttributesValue.get("cnAtts");
        for (Map<String, String> cnAttsMap : cnAttsList) {
            cnAtts.put(cnAttsMap.get("key"), cnAttsMap.get("value"));
        }

        feedModel.setOrgAtts(orgAtts);
        feedModel.setCnAtts(cnAtts);
        feedModel.setCustomIds(customIds);
        return feedModel;
    }

    /**
     * set complex value.
     * @param fields
     * @param complexValue
     */
    private void setComplexValue(List<Field> fields, ComplexValue complexValue){

        for (Field fieldItem:fields){

            complexValue.put(fieldItem);

            FieldTypeEnum fieldType = fieldItem.getType();

            switch (fieldType){
                case INPUT:
                    InputField inputField = (InputField)fieldItem;
                    String inputValue = inputField.getValue();
                    complexValue.setInputFieldValue(inputField.getId(),inputValue);
                    break;
                case SINGLECHECK:
                    SingleCheckField singleCheckField = (SingleCheckField)fieldItem;
                    Value checkValue = singleCheckField.getValue();
                    complexValue.setSingleCheckFieldValue(singleCheckField.getId(),checkValue);
                    break;
                case MULTICHECK:
                    MultiCheckField multiCheckField = (MultiCheckField)fieldItem;
                    List<Value> checkValues = multiCheckField.getValues();
                    complexValue.setMultiCheckFieldValues(multiCheckField.getId(),checkValues);
                    break;
                case MULTIINPUT:
                    MultiInputField multiInputField = (MultiInputField)fieldItem;
                    List<String> inputValues = multiInputField.getStringValues();
                    complexValue.setMultiInputFieldValues(multiInputField.getId(),inputValues);
                    break;
                case COMPLEX:
                    ComplexField complexField = (ComplexField)fieldItem;
                    List<Field> subFields = complexField.getFields();
                    ComplexValue subComplexValue = complexField.getComplexValue();
                    setComplexValue(subFields,subComplexValue);
                    break;
                case MULTICOMPLEX:
                    MultiComplexField multiComplexField = (MultiComplexField)fieldItem;
                    List<ComplexValue> complexValueList = multiComplexField.getComplexValues();
                    complexValue.setMultiComplexFieldValues(multiComplexField.getId(),complexValueList);
                    break;

                default:
                    break;
            }

        }

    }

    /**
     * complex field值为空时设定默认值.
     * @param fields
     */
    private void setDefaultComplexValues(List<Field> fields){

        for (Field fieldItem:fields){

            FieldTypeEnum fieldType = fieldItem.getType();

            switch (fieldType){
                case COMPLEX:
                    ComplexField complexField = (ComplexField)fieldItem;
                    if (complexField.getComplexValue().getFieldMap().isEmpty() && complexField.getDefaultComplexValue().getFieldMap().isEmpty()){

                        ComplexValue defComplexValue = new ComplexValue();
                        Map<String,Field> complexValueMap = new HashMap<>();
                        List<Field> complexFields = complexField.getFields();
                        setDefaultValueFieldMap(complexFields,complexValueMap);
                        defComplexValue.setFieldMap(complexValueMap);
                        complexField.setDefaultValue(defComplexValue);

                    }
                    break;
                case MULTICOMPLEX:
                    MultiComplexField multiComplexField = (MultiComplexField)fieldItem;
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
     * @param fields
     * @param complexValueMap
     */
    private void setDefaultValueFieldMap(List<Field> fields,Map<String,Field> complexValueMap){

        for (Field field:fields){
            FieldTypeEnum type = field.getType();
            switch (type){
                case INPUT:
                case SINGLECHECK:
                case MULTICHECK:
                case MULTIINPUT:
                    complexValueMap.put(field.getId(),field);
                    break;
                case COMPLEX:

                    ComplexField complexField = (ComplexField)field;

                    if (complexField.getComplexValue().getFieldMap().isEmpty() && complexField.getDefaultComplexValue().getFieldMap().isEmpty()){

                        ComplexValue complexValue = new ComplexValue();

                        Map<String,Field> subComplexValueMap = new HashMap<>();

                        List<Field> subFields = complexField.getFields();

                        setDefaultValueFieldMap(subFields,subComplexValueMap);

                        complexValue.setFieldMap(subComplexValueMap);

                        complexField.setDefaultValue(complexValue);

                        complexValueMap.put(complexField.getId(),complexField);
                    }
                    break;
                case MULTICOMPLEX:
                    MultiComplexField multiComplexField = (MultiComplexField)field;
                    if (multiComplexField.getComplexValues().isEmpty() && multiComplexField.getDefaultComplexValues().isEmpty()){
                        ComplexValue complexValue = new ComplexValue();
                        Map<String,Field> subComplexValueMap = new HashMap<>();
                        List<Field> subFields = multiComplexField.getFields();

                        setDefaultValueFieldMap(subFields,subComplexValueMap);

                        multiComplexField.addDefaultComplexValue(complexValue);
                        complexValueMap.put(multiComplexField.getId(),multiComplexField);

                    }
                    break;
                default:
                    break;
            }

        }
    }

    /**
     * 填充field选项值.
     * @param fields
     * @param channelId
     */
    private void fillFieldOptions(List<Field> fields,String channelId){

        for (Field field : fields) {

            if (optionDataSource.equals(field.getDataSource())) {

                FieldTypeEnum type = field.getType();

                switch (type){
                    case LABEL:
                        break;
                    case INPUT:
                        break;
                    case SINGLECHECK:
                    case MULTICHECK:
                        List<Option> options = TypeChannel.getOptions(field.getId(), channelId);
                        OptionsField optionsField = (OptionsField) field;
                        optionsField.setOptions(options);
                        break;
                    default:
                        break;

                }

            }
        }

    }
}
