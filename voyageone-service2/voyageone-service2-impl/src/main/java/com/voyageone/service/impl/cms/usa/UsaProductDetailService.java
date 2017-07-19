package com.voyageone.service.impl.cms.usa;

import com.voyageone.base.dao.mongodb.JongoUpdate;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.CmsChannelConfigs;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.Types;
import com.voyageone.common.configs.beans.CmsChannelConfigBean;
import com.voyageone.common.configs.beans.TypeBean;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.masterdate.schema.enums.FieldTypeEnum;
import com.voyageone.common.masterdate.schema.factory.SchemaJsonReader;
import com.voyageone.common.masterdate.schema.factory.SchemaReader;
import com.voyageone.common.masterdate.schema.field.*;
import com.voyageone.common.masterdate.schema.option.Option;
import com.voyageone.common.masterdate.schema.utils.FieldUtil;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.masterdate.schema.value.ComplexValue;
import com.voyageone.common.masterdate.schema.value.Value;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.service.bean.cms.CmsBtTagBean;
import com.voyageone.service.bean.cms.product.CmsMtBrandsMappingBean;
import com.voyageone.service.bean.cms.product.ProductUpdateBean;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.CommonSchemaService;
import com.voyageone.service.impl.cms.PlatformCategoryService;
import com.voyageone.service.impl.cms.PlatformSchemaService;
import com.voyageone.service.impl.cms.TagService;
import com.voyageone.service.impl.cms.prices.IllegalPriceConfigException;
import com.voyageone.service.impl.cms.prices.PriceCalculateException;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.CmsBtTagModel;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategorySchemaModel;
import com.voyageone.service.model.cms.mongo.product.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.voyageone.common.CmsConstants.ChannelConfig.PRICE_CALCULATOR;
import static com.voyageone.common.CmsConstants.ChannelConfig.PRICE_CALCULATOR_FORMULA;

/**
 * Created by james on 2017/7/18.
 */
@Service
public class UsaProductDetailService extends BaseService {

    private final
    ProductService productService;

    private final
    CommonSchemaService commonSchemaService;

    private final
    PlatformCategoryService platformCategoryService;

    private final
    TagService tagService;

    @Autowired
    CmsBtProductDao cmsBtProductDao;

    @Autowired
    public UsaProductDetailService(ProductService productService, CommonSchemaService commonSchemaService, PlatformCategoryService platformCategoryService, TagService tagService) {
        this.productService = productService;
        this.commonSchemaService = commonSchemaService;
        this.platformCategoryService = platformCategoryService;
        this.tagService = tagService;
    }

    /**
     * 取得美国用产品编辑页
     */
    public Map<String, Object> getMastProductInfo(String channelId, Long prodId) {
        Map<String, Object> result = new HashMap<>();

        // 取得产品信息
        CmsBtProductModel cmsBtProduct = productService.getProductById(channelId, prodId);
        // 取得该商品的所在group的其他商品的图片
        List<CmsBtProductModel> cmsBtProductGroup = productService.getProductListByModel(channelId, cmsBtProduct.getCommonNotNull().getFieldsNotNull().getModel());
        List<Map<String, Object>> images = new ArrayList<>();
        cmsBtProductGroup.forEach(product -> {
            if (product != null) {
                Map<String, Object> image = new HashMap<>();
                image.put("productCode", product.getCommonNotNull().getFields().getCode());
                String imageName = "";

                if (!ListUtils.isNull(product.getCommon().getFields().getImages1()) && product.getCommon().getFields().getImages1().get(0).size() > 0) {
                    imageName = (String) product.getCommon().getFields().getImages1().get(0).get("image1");
                }
                if (StringUtil.isEmpty(imageName) && !ListUtils.isNull(product.getCommon().getFields().getImages6()) && product.getCommon().getFields().getImages6().get(0).size() > 0) {
                    imageName = (String) product.getCommon().getFields().getImages6().get(0).get("image6");
                }
                image.put("imageName", imageName);
                image.put("prodId", product.getProdId());
                image.put("qty", product.getCommon().getFields().getQuantity());
                images.add(image);
            }
        });

        List<Field> cmsMtCommonFields = commonSchemaService.getComUsSchemaModel().getFields();
        fillFieldOptions(cmsMtCommonFields, channelId, "en");
        CmsBtProductModel_Common productComm = cmsBtProduct.getCommon();

        String productType = productComm.getFields().getProductType();
        productComm.getFields().setProductType(StringUtil.isEmpty(productType) ? "" : productType.trim());
        String sizeType = productComm.getFields().getSizeType();
        productComm.getFields().setSizeType(StringUtil.isEmpty(sizeType) ? "" : sizeType.trim());

        FieldUtil.setFieldsValueFromMap(cmsMtCommonFields, cmsBtProduct.getCommon().getFields());
        productComm.put("schemaFields", cmsMtCommonFields);


        Map<String, Object> mastData = new HashMap<>();
        mastData.put("images", images);

        result.put("productComm", productComm);
        result.put("mastData", mastData);
        Map<String, Object> plarform = getProductPlatform(channelId, prodId, CartEnums.Cart.SN.getValue());
        result.put("platform",plarform);


        //freeTag
        List<String> tagPathList = cmsBtProduct.getFreeTags();
        if (tagPathList != null && tagPathList.size() > 0) {
            List<CmsBtTagBean> tagModelList = new ArrayList<>();
            List<String> temp = new ArrayList<>();
            for (String tag : tagPathList) {
                temp.add(tag);
            }
            if (temp.size() > 0) {
                List<CmsBtTagBean> ts = tagService.getTagPathNameByTagPath(channelId, temp);
                if (!ListUtils.isNull(ts)) {
                    List<Map<String, String>> freeTags = new ArrayList<>(ts.size());
                    ts.forEach(t->{
                        Map<String, String> item = new HashMap<>();
                        item.put("tagPath", t.getTagPath());
                        item.put("tagPathName", t.getTagPathName());
                        freeTags.add(item);
                    });
                    result.put("freeTag", freeTags);
                }
            }
        }
        return result;
    }

    /**
     * 获取产品平台信息
     *
     * @param channelId channelId
     * @param prodId    prodId
     * @param cartId    cartId
     * @return 产品平台信息
     */
    public Map<String, Object> getProductPlatform(String channelId, Long prodId, Integer cartId) {
        CmsBtProductModel cmsBtProduct = productService.getProductById(channelId, prodId);
        CmsBtProductModel_Platform_Cart platformCart = cmsBtProduct.getUsPlatform(cartId);
        if (platformCart == null) {
            platformCart = new CmsBtProductModel_Platform_Cart();
            platformCart.setCartId(cartId);
        }

        if (platformCart.getFields() == null) platformCart.setFields(new BaseMongoMap<>());

        CmsMtPlatformCategorySchemaModel platformCatSchemaModel = platformCategoryService.getPlatformCatSchema("1", cartId);

        List<Field> listItemField = SchemaReader.readXmlForList(platformCatSchemaModel.getPropsItem());

        FieldUtil.setFieldsValueFromMap(listItemField, platformCart.getFields());
        // added by morse.lu 2016/09/13 end
        platformCart.put("platformFields", listItemField);

        return platformCart;
    }


    // 更新共同属性
    public void updateCommonProductInfo(String channelId, Long prodId, Map<String, Object> commInfo, String modifier) {

        List<Field> masterFields = buildMasterFields((List<Map<String, Object>>) commInfo.get("schemaFields"));
        commInfo.remove("schemaFields");
        CmsBtProductModel_Common commonModel = new CmsBtProductModel_Common(commInfo);
        commonModel.put("fields", FieldUtil.getFieldsValueToMap(masterFields));

        HashMap<String, Object> queryMap = new HashMap<>();
        queryMap.put("prodId", prodId);
        List<BulkUpdateModel> bulkList = new ArrayList<>();
        HashMap<String, Object> updateMap = new HashMap<>();
        updateMap.put("common", commonModel);
        BulkUpdateModel model = new BulkUpdateModel();
        model.setUpdateMap(updateMap);
        model.setQueryMap(queryMap);
        bulkList.add(model);
        cmsBtProductDao.bulkUpdateWithMap(channelId, bulkList, null, "$set");
    }

    // 更新自由标签
    public void updateFreeTag(String channelId, Long prodId, List<Map<String, String>> freeTag) {

        List<String> usFreeTags = freeTag.stream().map(item->item.get("tagPath")).collect(Collectors.toList());
        HashMap<String, Object> queryMap = new HashMap<>();
        queryMap.put("prodId", prodId);
        List<BulkUpdateModel> bulkList = new ArrayList<>();
        HashMap<String, Object> updateMap = new HashMap<>();
        updateMap.put("usFreeTags", usFreeTags);
        BulkUpdateModel model = new BulkUpdateModel();
        model.setUpdateMap(updateMap);
        model.setQueryMap(queryMap);
        bulkList.add(model);
        cmsBtProductDao.bulkUpdateWithMap(channelId, bulkList, null, "$set");
    }

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
    /**
     * 填充field选项值.
     */
    private static void fillFieldOptions(List<Field> fields, String channelId, String language) {

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
                            typeChannelBeanList = TypeChannels.getTypeWithLang(field.getId(), channelId, language);

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
}
