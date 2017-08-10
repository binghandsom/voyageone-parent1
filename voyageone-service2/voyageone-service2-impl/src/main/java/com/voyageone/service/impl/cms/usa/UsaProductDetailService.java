package com.voyageone.service.impl.cms.usa;

import com.voyageone.base.dao.mongodb.JongoUpdate;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.Types;
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
import com.voyageone.common.util.BeanUtils;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.service.bean.cms.CmsBtTagBean;
import com.voyageone.service.bean.cms.search.product.CmsProductCodeListBean;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.*;
import com.voyageone.service.impl.cms.prices.PriceService;
import com.voyageone.service.impl.cms.product.CmsBtPriceLogService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.product.ProductTopService;
import com.voyageone.service.impl.cms.product.search.CmsSearchInfoBean2;
import com.voyageone.service.impl.cms.search.product.CmsProductSearchQueryService;
import com.voyageone.service.impl.cms.vomq.CmsMqSenderService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.usa.*;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategorySchemaModel;
import com.voyageone.service.model.cms.mongo.product.*;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by james on 2017/7/18.
 */
@Service
public class UsaProductDetailService extends BaseService {

    private final ProductService productService;

    private final CommonSchemaService commonSchemaService;

    private final PlatformCategoryService platformCategoryService;

    private final TagService tagService;

    private final CmsBtProductDao cmsBtProductDao;
    private final PriceService priceService;
    private final CmsMqSenderService cmsMqSenderService;
    private final CmsProductSearchQueryService cmsProductSearchQueryService;
    private final CmsBtPriceLogService cmsBtPriceLogService;
    private final PlatformProductUploadService platformProductUploadService;
    private final SellerCatService sellerCatService;
    private final ProductTopService productTopService;

    @Autowired
    public UsaProductDetailService(ProductService productService, CommonSchemaService commonSchemaService, PlatformCategoryService platformCategoryService, TagService tagService, CmsBtProductDao cmsBtProductDao, PriceService priceService, CmsMqSenderService cmsMqSenderService, CmsProductSearchQueryService cmsProductSearchQueryService, CmsBtPriceLogService cmsBtPriceLogService, PlatformProductUploadService platformProductUploadService, SellerCatService sellerCatService, ProductTopService productTopService) {
        this.productService = productService;
        this.commonSchemaService = commonSchemaService;
        this.platformCategoryService = platformCategoryService;
        this.tagService = tagService;
        this.cmsBtProductDao = cmsBtProductDao;
        this.priceService = priceService;
        this.cmsMqSenderService = cmsMqSenderService;
        this.cmsProductSearchQueryService = cmsProductSearchQueryService;
        this.cmsBtPriceLogService = cmsBtPriceLogService;
        this.platformProductUploadService = platformProductUploadService;
        this.sellerCatService = sellerCatService;
        this.productTopService = productTopService;
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
        fillFieldOptions(cmsMtCommonFields, channelId);
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
        Map<String, Object> plarform = getProductPlatform(channelId, prodId, CartEnums.Cart.SNKRHDp.getValue());
        result.put("platform", plarform);


        //freeTag
        List<String> tagPathList = cmsBtProduct.getUsFreeTags();
        if (tagPathList != null && tagPathList.size() > 0) {
            List<String> temp = new ArrayList<>();
            for (String tag : tagPathList) {
                temp.add(tag);
            }
            if (temp.size() > 0) {
                List<CmsBtTagBean> ts = tagService.getTagPathNameByTagPath(channelId, temp);
                if (!ListUtils.isNull(ts)) {
                    List<Map<String, String>> freeTags = new ArrayList<>(ts.size());
                    ts.forEach(t -> {
                        Map<String, String> item = new HashMap<>();
                        item.put("tagPath", t.getTagPath());
                        item.put("tagPathName", t.getTagPathName());
                        freeTags.add(item);
                    });
                    result.put("freeTagList", freeTags);
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
        Map<String, Object> result = new HashMap<>();
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
        result.put("platform", platformCart);
        result.put("productComm", cmsBtProduct.getCommon());

        List<Map<String, String>> sellers = new ArrayList<>();
        String pCatId = platformCart.getpCatId();
        if (platformCart.getSellerCats() != null) {
            platformCart.getSellerCats().forEach(seller -> {
                if (!seller.getcId().equals(pCatId)) {
                    Map<String, String> item = new HashMap<String, String>();
                    item.put("catId", seller.getcIds().stream().collect(Collectors.joining(">")));
                    item.put("catPath", seller.getcNames().stream().collect(Collectors.joining(">")));
                    sellers.add(item);
                }
            });
        }
        result.put("sellerCarts", sellers);

        return result;
    }


    // 更新共同属性
    public void updateCommonProductInfo(String channelId, Long prodId, Map<String, Object> commInfo, String modifier) {

        List<Field> masterFields = buildMasterFields((List<Map<String, Object>>) commInfo.get("schemaFields"));
        commInfo.remove("schemaFields");
        CmsBtProductModel_Common commonModel = new CmsBtProductModel_Common(commInfo);
        Map<String, Object> fields = FieldUtil.getFieldsValueToMap(masterFields);
        fields.put("images1", ((Map<String, Object>) commInfo.get("fields")).get("images1"));
        fields.put("images2", ((Map<String, Object>) commInfo.get("fields")).get("images2"));
        commonModel.put("fields", fields);

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
    public void updateFreeTag(String channelId, Long prodId, List<String> freeTags) {
        HashMap<String, Object> queryMap = new HashMap<>();
        queryMap.put("prodId", prodId);
        List<BulkUpdateModel> bulkList = new ArrayList<>();
        HashMap<String, Object> updateMap = new HashMap<>();
        updateMap.put("usFreeTags", freeTags);
        BulkUpdateModel model = new BulkUpdateModel();
        model.setUpdateMap(updateMap);
        model.setQueryMap(queryMap);
        bulkList.add(model);
        cmsBtProductDao.bulkUpdateWithMap(channelId, bulkList, null, "$set");
    }

    // 更新自由标签
    public void updateProductPlatform(String channelId, Long prodId, Map<String, Object> platform, List<Map<String, String>> sellers, String modifier) {

        CmsBtProductModel oldProduct = productService.getProductById(channelId, prodId);

        /* 保存类型 */
        if (platform.get("platformFields") != null) {
            List<Field> masterFields = buildMasterFields((List<Map<String, Object>>) platform.get("platformFields"));

            platform.put("fields", FieldUtil.getFieldsValueToMap(masterFields));
            platform.remove("platformFields");
        }
        CmsBtProductModel_Platform_Cart platformModel = new CmsBtProductModel_Platform_Cart(platform);
        if (ListUtils.isNull(platformModel.getSkus())) {
            return;
        }

        // 店铺内分类设置
        List<String> sellerPaths = new ArrayList<>();
        if (!StringUtil.isEmpty(platformModel.getpCatPath())) {
            sellerPaths.add(platformModel.getpCatPath());
        }

        if (sellers == null) {
            sellers = new ArrayList<>();
        }
        if (!sellers.isEmpty()) {
            for (Map<String, String> item : sellers) {
                sellerPaths.add(item.get("catPath"));
            }
        }
        sellerPaths = sellerPaths.stream().distinct().collect(Collectors.toList());
        List<CmsBtProductModel_SellerCat> sellerCats = new ArrayList<>();
        sellerPaths.forEach(cat -> {
            CmsBtProductModel_SellerCat sellerCat = sellerCatService.getSellerCat(channelId, platformModel.getCartId(), cat);
            if (sellerCat != null) {
                sellerCats.add(sellerCat);
            }
        });
        platformModel.setSellerCats(sellerCats);

        // 价格类型处理,String -> Double
        for (BaseMongoMap<String, Object> sku : platformModel.getSkus()) {
            sku.setAttribute("clientMsrpPrice", sku.getDoubleAttribute("clientMsrpPrice"));
            sku.setAttribute("clientNetPrice", sku.getDoubleAttribute("clientNetPrice"));
            sku.setAttribute("clientRetailPrice", sku.getDoubleAttribute("clientRetailPrice"));
        }
        updateUsPlatformMinAndMaxPrice(platformModel);

        HashMap<String, Object> queryMap = new HashMap<>();
        queryMap.put("prodId", prodId);

        List<BulkUpdateModel> bulkList = new ArrayList<>();
        HashMap<String, Object> updateMap = new HashMap<>();

        platformModel.setModified(DateTimeUtil.getNowTimeStamp());
        updateMap.put("usPlatforms.P" + platformModel.getCartId(), platformModel);
        BulkUpdateModel model = new BulkUpdateModel();
        model.setUpdateMap(updateMap);
        model.setQueryMap(queryMap);
        bulkList.add(model);

        cmsBtProductDao.bulkUpdateWithMap(channelId, bulkList, modifier, "$set");

        CmsBtProductModel cmsBtProductModel = productService.getProductById(channelId, prodId);

        if (CmsConstants.ProductStatus.Approved.name().equals(platformModel.getStatus())) {
            platformProductUploadService.saveCmsBtUsWorkloadModel(channelId, platformModel.getCartId(), cmsBtProductModel.getCommon().getFields().getCode(), null, 0, modifier);
        }


        if (Objects.equals(platformModel.getCartId(), CartEnums.Cart.SNKRHDp.getValue())) {
            List<CmsBtProductModel_SellerCat> oSeller = null;
            if (oldProduct.getUsPlatform(platformModel.getCartId()) != null) {
                oSeller = oldProduct.getUsPlatform(platformModel.getCartId()).getSellerCats();
            }
            Map<String, List<String>> diff = sellerCatService.sellerCompare(oSeller, platformModel.getSellerCats());


            if (ListUtils.notNull(diff.get("del"))) {

                diff.get("del").forEach(catId -> productTopService.removeTop50(channelId, catId, cmsBtProductModel.getCommon().getFields().getCode(), platformModel.getCartId()));
            }

            // 重新推送类目的产品数据
            if (ListUtils.notNull(diff.get("delCatPath"))) {
                CmsCategoryReceiveMQMessageBody cmsCategoryReceiveMQMessageBody = new CmsCategoryReceiveMQMessageBody();
                cmsCategoryReceiveMQMessageBody.setChannelId(channelId);
                cmsCategoryReceiveMQMessageBody.setCartId(platformModel.getCartId() + "");
                cmsCategoryReceiveMQMessageBody.setFullCatIds(diff.get("delCatPath"));
                cmsCategoryReceiveMQMessageBody.setSender(modifier);
                cmsMqSenderService.sendMessage(cmsCategoryReceiveMQMessageBody);
            }

            if (ListUtils.notNull(diff.get("addCatPath"))) {
                CmsCategoryReceiveMQMessageBody cmsCategoryReceiveMQMessageBody = new CmsCategoryReceiveMQMessageBody();
                cmsCategoryReceiveMQMessageBody.setChannelId(channelId);
                cmsCategoryReceiveMQMessageBody.setCartId(platformModel.getCartId() + "");
                cmsCategoryReceiveMQMessageBody.setFullCatIds(diff.get("addCatPath"));
                cmsCategoryReceiveMQMessageBody.setSender(modifier);
                cmsMqSenderService.sendMessage(cmsCategoryReceiveMQMessageBody);
            }
        }
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
    private static void fillFieldOptions(List<Field> fields, String channelId) {

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
                            List<TypeBean> typeBeanList = Types.getTypeList(field.getId(), "en");
                            if (typeBeanList == null) return;
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
                            typeChannelBeanList = TypeChannels.getTypeWithLang(field.getId(), channelId, "en");

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

    public String updatePrice(Map<String, Object> paraMap, String channelId, String userName) {
        CmsBtProductUpdatePriceMQMessageBody mqMap = new CmsBtProductUpdatePriceMQMessageBody();
        if (paraMap != null) {
            String selAll = (String) paraMap.get("selAll");
            List<String> codeList = (List<String>) paraMap.get("codeList");
            Integer cartId = Integer.parseInt((String) paraMap.get("cartId"));
            mqMap.setCartId(cartId);
            HashMap<String, Object> params = new HashMap<>();

            String changedPriceType = (String) paraMap.get("changedPriceType");
            String basePriceType = (String) paraMap.get("basePriceType");
            String optionType = (String) paraMap.get("optionType");
            String value = (String) paraMap.get("value");
            //"1":取整,"0":不取整
            String flag = (String) paraMap.get("flag");
            params.put("changedPriceType", changedPriceType);
            params.put("basePriceType", basePriceType);
            params.put("optionType", optionType);
            params.put("value", value);
            params.put("flag", flag);
            mqMap.setParams(params);
            if ("true".equals(selAll)) {
                //勾选了全部,需要通过检索条件,查询出所有信息
                Map<String, Object> map = (Map<String, Object>) paraMap.get("queryMap");
                CmsSearchInfoBean2 queryParams = BeanUtils.toModel(map, CmsSearchInfoBean2.class);
                CmsProductCodeListBean cmsProductCodeListBean = cmsProductSearchQueryService.getProductCodeList(queryParams, channelId);
                long productListTotal = cmsProductCodeListBean.getTotalCount();
                //要根据查询出来的总页数设置分页
                long pageNumber = getPageNumber(productListTotal);

                for (int i = 0; i < pageNumber; i++) {
                    queryParams.setProductPageSize(100);
                    queryParams.setProductPageNum(i + 1);
                    CmsProductCodeListBean cmsProductCodeListBean1 = cmsProductSearchQueryService.getProductCodeList(queryParams, channelId);
                    List<String> productCodeList = cmsProductCodeListBean1.getProductCodeList();
                    mqMap.setProductCodes(productCodeList);
                    mqMap.setChannelId(channelId);
                    mqMap.setSender(userName);
                    cmsMqSenderService.sendMessage(mqMap);
                }
            } else {
                //未勾选全部
                mqMap.setProductCodes(codeList);
                mqMap.setChannelId(channelId);
                mqMap.setSender(userName);

                String s = JacksonUtil.bean2Json(mqMap);

                cmsMqSenderService.sendMessage(mqMap);

            }
        }
        return null;
    }

    //批量进行上下架操作
    public String listOrDelist(Map<String, Object> paraMap, String channelId, String userName) {
        CmsBtProductUpdateListDelistStatusMQMessageBody mqMap = new CmsBtProductUpdateListDelistStatusMQMessageBody();
        if (paraMap != null) {
            String selAll = (String) paraMap.get("selAll");
            List<String> codeList = (List<String>) paraMap.get("codeList");
            Integer cartId = Integer.parseInt((String) paraMap.get("cartId"));
            mqMap.setCartId(cartId);
            String activeStatus = (String) paraMap.get("activeStatus");
            mqMap.setActiveStatus(activeStatus);
            Integer days = Integer.parseInt((String) paraMap.get("days"));
            mqMap.setDays(days);
            mqMap.setChannelId(channelId);
            mqMap.setSender(userName);
            if ("true".equals(selAll)) {
                //勾选了全部,需要通过检索条件,查询出所有信息
                Map<String, Object> map = (Map<String, Object>) paraMap.get("queryMap");
                CmsSearchInfoBean2 queryParams = BeanUtils.toModel(map, CmsSearchInfoBean2.class);
                CmsProductCodeListBean cmsProductCodeListBean = cmsProductSearchQueryService.getProductCodeList(queryParams, channelId);
                long productListTotal = cmsProductCodeListBean.getTotalCount();
                //要根据查询出来的总页数设置分页
                long pageNumber = getPageNumber(productListTotal);
                for (int i = 0; i < pageNumber; i++) {
                    queryParams.setProductPageSize(100);
                    queryParams.setProductPageNum(i + 1);
                    CmsProductCodeListBean cmsProductCodeListBean1 = cmsProductSearchQueryService.getProductCodeList(queryParams, channelId);
                    List<String> productCodeList = cmsProductCodeListBean1.getProductCodeList();
                    mqMap.setProductCodes(productCodeList);
                    cmsMqSenderService.sendMessage(mqMap);
                }
            } else {
                //未勾选全部
                mqMap.setProductCodes(codeList);
                String s = JacksonUtil.bean2Json(mqMap);

                cmsMqSenderService.sendMessage(mqMap);
            }
        }
        return null;
    }

    private long getPageNumber(long productListTotal) {
        long pageNumber = 0;
        if (productListTotal % 100 == 0) {
            //整除
            pageNumber = productListTotal / 100;
        } else {
            //不整除
            pageNumber = (productListTotal / 100) + 1;
        }
        return pageNumber;
    }

    //修改单条价格
    public String updateOnePrice(List<Map<String, Object>> lists, String channelId, String userName) {
        CmsBtProductModel cmsBtProductModel = null;

        if (ListUtils.notNull(lists)) for (Map<String, Object> paraMap : lists) {

            //同事传入portId更新价格履历
            Long prodId = Long.parseLong((String) paraMap.get("prodId"));
            //平台id
            Integer cartId = Integer.parseInt((String) paraMap.get("cartId"));
            String isSale = (String) paraMap.get("isSale");
            Double clientMsrpPrice = null;
            Double clientRetailPrice = null;
            if (StringUtils.isNotEmpty((String) paraMap.get("clientMsrpPrice"))) {
                clientMsrpPrice = Double.parseDouble((String) paraMap.get("clientMsrpPrice"));
            }
            if (StringUtils.isNotEmpty((String) paraMap.get("clientRetailPrice"))) {
                clientRetailPrice = Double.parseDouble((String) paraMap.get("clientRetailPrice"));
            }
            if (cmsBtProductModel == null) {
                cmsBtProductModel = productService.getProductById(channelId, prodId);
            }
            String productCode = cmsBtProductModel.getCommon().getFields().getCode();
            //获取商品code
            String code = cmsBtProductModel.getCommonNotNull().getFieldsNotNull().getCode();

            //设置滞后发布日期
            Integer days = (Integer) paraMap.get("days");
            if (days != null) {
                if (days != 0) {
                    Date date = new Date();
                    Calendar calendar = new GregorianCalendar();
                    calendar.setTime(date);
                    calendar.add(calendar.DATE, days);
                    date = calendar.getTime();
                    platformProductUploadService.saveCmsBtUsWorkloadModel(channelId, cartId, productCode, date, 0, userName);
                } else {
                    platformProductUploadService.saveCmsBtUsWorkloadModel(channelId, cartId, productCode, null, 0, userName);
                }
            }

            if (cartId < 20) {
                //修改美国平台价格
                Map<String, CmsBtProductModel_Platform_Cart> usPlatforms = cmsBtProductModel.getUsPlatforms();
                CmsBtProductModel_Platform_Cart usPlatform = usPlatforms.get("P" + cartId);
                if (usPlatform != null) {
                    List<BulkUpdateModel> bulkList = new ArrayList<>();
                    List<BulkUpdateModel> minMaxPrice = new ArrayList<>();
                    List<BaseMongoMap<String, Object>> skus = usPlatform.getSkus();

                    if (skus != null) {
                        for (BaseMongoMap<String, Object> sku : skus) {
                            //获取到对应的skuCode
                            String skuCode = (String) sku.get("skuCode");
                            //修改最大值最小值
                            HashMap<String, Object> minMaxPriceQueryMap = new HashMap<>();
                            //设置查询条件,通过productCode进行定位
                            minMaxPriceQueryMap.put("common.fields.code", code);
                            HashMap<String, Object> minMaxPriceUpdateMap = new HashMap<>();
                            HashMap<String, Object> updateMap = new HashMap<>();
                            if (clientMsrpPrice != null) {
                                updateMap.put("usPlatforms.P" + cartId + ".skus.$.clientMsrpPrice", clientMsrpPrice);
                                //修改最大值最小值
                                minMaxPriceUpdateMap.put("usPlatforms.P" + cartId + ".pPriceMsrpSt", clientMsrpPrice);
                                minMaxPriceUpdateMap.put("usPlatforms.P" + cartId + ".pPriceMsrpEd", clientMsrpPrice);
                            }
                            if (clientRetailPrice != null) {
                                updateMap.put("usPlatforms.P" + cartId + ".skus.$.clientRetailPrice", clientRetailPrice);
                                updateMap.put("usPlatforms.P" + cartId + ".skus.$.clientNetPrice", clientRetailPrice);
                                //修改最大值最小值
                                minMaxPriceUpdateMap.put("usPlatforms.P" + cartId + ".pPriceSaleSt", clientRetailPrice);
                                minMaxPriceUpdateMap.put("usPlatforms.P" + cartId + ".pPriceSaleEd", clientRetailPrice);

                                minMaxPriceUpdateMap.put("usPlatforms.P" + cartId + ".pPriceRetailSt", clientRetailPrice);
                                minMaxPriceUpdateMap.put("usPlatforms.P" + cartId + ".pPriceRetailEd", clientRetailPrice);
                            }
                            //修改销售状态
                            if (StringUtils.isNotEmpty(isSale)) {
                                minMaxPriceUpdateMap.put("usPlatforms.P" + cartId + ".isSale", isSale);
                            }
                            HashMap<String, Object> queryMap = new HashMap<>();
                            //设置查询条件
                            queryMap.put("usPlatforms.P" + cartId + ".skus.skuCode", skuCode);
                            BulkUpdateModel model = new BulkUpdateModel();
                            model.setUpdateMap(updateMap);
                            model.setQueryMap(queryMap);
                            bulkList.add(model);
                            //修改最大值最小值
                            BulkUpdateModel minMaxPriceModel = new BulkUpdateModel();
                            minMaxPriceModel.setUpdateMap(minMaxPriceUpdateMap);
                            minMaxPriceModel.setQueryMap(minMaxPriceQueryMap);
                            minMaxPrice.add(minMaxPriceModel);
                        }
                    }
                    if (ListUtils.notNull(bulkList) && ListUtils.notNull(minMaxPrice)) {
                        productService.bulkUpdateWithMap(channelId, bulkList, userName, "$set");
                        productService.bulkUpdateWithMap(channelId, minMaxPrice, userName, "$set");
                    }

                    if (CmsConstants.ProductStatus.Approved.name().equals(usPlatform.getStatus()) && (CmsConstants.PlatformStatus.OnSale == usPlatform.getpStatus() || CmsConstants.PlatformStatus.InStock == usPlatform.getpStatus() )) {
                        platformProductUploadService.saveCmsBtUsWorkloadModel(channelId, cartId, productCode, null, 0, userName);
                    }
                }
            } else {
                //修改中国平台价格
                CmsBtProductModel_Platform_Cart platform = cmsBtProductModel.getPlatform(cartId);
                if (platform != null) {
                    List<BulkUpdateModel> bulkList1 = new ArrayList<>();
                    List<BulkUpdateModel> minMaxPrice1 = new ArrayList<>();
                    List<BaseMongoMap<String, Object>> skus = platform.getSkus();
                    if (skus != null) {
                        for (BaseMongoMap<String, Object> sku : skus) {
                            //获取到对应的skuCode
                            String skuCode = (String) sku.get("skuCode");
                            //修改最大值最小值
                            HashMap<String, Object> minMaxPriceQueryMap = new HashMap<>();
                            HashMap<String, Object> updateMap = new HashMap<>();
                            //设置查询条件,通过productCode进行定位
                            minMaxPriceQueryMap.put("common.fields.code", code);
                            HashMap<String, Object> minMaxPriceUpdateMap = new HashMap<>();

                            if (clientMsrpPrice != null) {
                                updateMap.put("platforms.P" + cartId + ".skus.$.priceMsrp", clientMsrpPrice);
                                //修改最大值最小值
                                minMaxPriceUpdateMap.put("platforms.P" + cartId + ".pPriceMsrpSt", clientMsrpPrice);
                                minMaxPriceUpdateMap.put("platforms.P" + cartId + ".pPriceMsrpEd", clientMsrpPrice);
                            }
                            if (clientRetailPrice != null) {
                                updateMap.put("platforms.P" + cartId + ".skus.$.priceRetail", clientRetailPrice);
                                //修改priceDiffFlg
                                //调用接口计算priceDiffFlg的值
                                sku.setAttribute("priceRetail", clientRetailPrice);
                                String priceDiffFlg = priceService.getPriceDiffFlg(channelId, sku, cartId);
                                updateMap.put("platforms.P" + cartId + ".skus.$.priceDiffFlg", priceDiffFlg);

                                //修改最大值最小值
                                minMaxPriceUpdateMap.put("platforms.P" + cartId + ".pPriceRetailSt", clientRetailPrice);
                                minMaxPriceUpdateMap.put("platforms.P" + cartId + ".pPriceRetailEd", clientRetailPrice);
                            }
                            //修改销售状态
                            if (StringUtils.isNotEmpty(isSale)) {
                                minMaxPriceUpdateMap.put("platforms.P" + cartId + ".isSale", isSale);
                            }
                            HashMap<String, Object> queryMap = new HashMap<>();
                            //设置查询条件
                            queryMap.put("platforms.P" + cartId + ".skus.skuCode", skuCode);
                            BulkUpdateModel model = new BulkUpdateModel();
                            model.setUpdateMap(updateMap);
                            model.setQueryMap(queryMap);
                            bulkList1.add(model);
                            //修改最大值最小值
                            BulkUpdateModel minMaxPriceModel = new BulkUpdateModel();
                            minMaxPriceModel.setUpdateMap(minMaxPriceUpdateMap);
                            minMaxPriceModel.setQueryMap(minMaxPriceQueryMap);
                            minMaxPrice1.add(minMaxPriceModel);
                        }
                        if (ListUtils.notNull(bulkList1) && ListUtils.notNull(minMaxPrice1)) {
                            productService.bulkUpdateWithMap(channelId, bulkList1, userName, "$set");
                            productService.bulkUpdateWithMap(channelId, minMaxPrice1, userName, "$set");
                        }
                        if (CmsConstants.ProductStatus.Approved.name().equals(platform.getStatus()) && (CmsConstants.PlatformStatus.OnSale == platform.getpStatus() || CmsConstants.PlatformStatus.InStock == platform.getpStatus() )) {
                            platformProductUploadService.saveCmsBtUsWorkloadModel(channelId, cartId, productCode, null, 0, userName);
                        }
                        //更新价格履历
                        List<String> skus1 = new ArrayList<>();
                        skus.forEach(sku -> skus1.add(sku.getStringAttribute("skuCode")));
                        cmsBtPriceLogService.addLogForSkuListAndCallSyncPriceJob(skus1, channelId, prodId, cartId, userName, "新版CMS,美国平台,产品编辑页面,手动修改价格");
                    }
                }
            }
        }
        return null;
    }


    //根据productCode获取中国和美国的平台价格信息
    public HashMap<String, Object> getAllPlatformsPrice(Long id, String channelId) {
        HashMap<String, Object> data = new HashMap<>();

        List<TypeChannelBean> platformsNames = TypeChannels.getTypeWithLang(Constants.comMtTypeChannel.SKU_CARTS_53, channelId, "en");

        HashMap<String, String> namesMap = new HashMap<>();
        if (ListUtils.notNull(platformsNames)) {
            for (TypeChannelBean platformsName : platformsNames) {

                if (Integer.parseInt(platformsName.getValue()) > 0 && Integer.parseInt(platformsName.getValue()) < 20) {
                    namesMap.put(platformsName.getValue(), platformsName.getName());
                } else {
                    namesMap.put(platformsName.getValue(), platformsName.getAdd_name2());
                }

            }
        }
        if (id != null) {
            CmsBtProductModel cmsBtProductModel = productService.getProductById(channelId, id);
            if (cmsBtProductModel != null) {
                //封装返回的价格map,cartId作为key,对应平台下价格的最大值最小值作为值

                Map<String, CmsBtProductModel_Platform_Cart> platforms = cmsBtProductModel.getPlatforms();
                Map<String, CmsBtProductModel_Platform_Cart> usPlatforms = cmsBtProductModel.getUsPlatforms();
                //美国平台参数
                if (MapUtils.isNotEmpty(usPlatforms)) {
                    HashMap<String, Map<String, Object>> allUsPriceList = new HashMap<>();
                    for (Map.Entry<String, CmsBtProductModel_Platform_Cart> entry : usPlatforms.entrySet()) {
                        String key = entry.getKey();
                        CmsBtProductModel_Platform_Cart value = entry.getValue();

                        HashMap<String, Object> priceMap = new HashMap<>();
                        priceMap.put("priceMsrpSt", value.getpPriceMsrpSt());
                        priceMap.put("priceMsrpEd", value.getpPriceMsrpEd());
                        priceMap.put("priceRetailSt", value.getpPriceSaleSt());
                        priceMap.put("priceRetailEd", value.getpPriceSaleEd());
                        priceMap.put(value.getCartId().toString(), namesMap.get(value.getCartId().toString()));

                        priceMap.put("cartName", namesMap.get(value.getCartId().toString()));
                        priceMap.put("cartId", value.getCartId().toString());
                        priceMap.put("status", value.getStatus());
                        priceMap.put("pStatus", value.getpStatus() != null ? value.getpStatus().name() : null);
                        priceMap.put("pReallyStatus", value.getpReallyStatus());
                        priceMap.put("pPublishError", value.getpPublishError());
                        priceMap.put("isSale", value.getIsSale());
                        allUsPriceList.put(value.getCartId().toString(), priceMap);
                    }
                    data.put("allUsPriceList", allUsPriceList);
                }
                //中国平台参数
                if (MapUtils.isNotEmpty(platforms)) {
                    HashMap<String, Map<String, Object>> allPriceList = new HashMap<>();
                    for (Map.Entry<String, CmsBtProductModel_Platform_Cart> entry : platforms.entrySet()) {
                        String key = entry.getKey();
                        CmsBtProductModel_Platform_Cart value = entry.getValue();
                        if (!"0".equals(value.getCartId().toString())) {
                            HashMap<String, Object> priceMap = new HashMap<>();
                            priceMap.put("priceMsrpSt", value.getpPriceMsrpSt());
                            priceMap.put("priceMsrpEd", value.getpPriceMsrpEd());
                            priceMap.put("priceRetailSt", value.getpPriceRetailSt());
                            priceMap.put("priceRetailEd", value.getpPriceRetailEd());

                            priceMap.put("priceSaleSt", value.getpPriceSaleSt());
                            priceMap.put("priceSaleEd", value.getpPriceSaleEd());

                            priceMap.put(value.getCartId().toString(), namesMap.get(value.getCartId().toString()));

                            priceMap.put("cartName", namesMap.get(value.getCartId().toString()));
                            priceMap.put("cartId", value.getCartId().toString());
                            priceMap.put("status", value.getStatus());
                            priceMap.put("pStatus", value.getpStatus() != null ? value.getpStatus().name() : null);
                            priceMap.put("pReallyStatus", value.getpReallyStatus());
                            priceMap.put("pPublishError", value.getpPublishError());
                            priceMap.put("isSale", value.getIsSale());

                            allPriceList.put(value.getCartId().toString(), priceMap);
                        }
                    }
                    data.put("allPriceList", allPriceList);
                }
            }
        }
        return data;
    }

    //同步美国平台最大最小值
    public void updateUsPlatformMinAndMaxPrice(CmsBtProductModel_Platform_Cart usPlatform) {

        List<BaseMongoMap<String, Object>> skus = usPlatform.getSkus();

        if (ListUtils.notNull(skus)) {
            Double msrpMin = Double.MAX_VALUE;
            Double msrpMax = Double.MIN_VALUE;
            Double retailMin = Double.MAX_VALUE;
            Double retailMax = Double.MIN_VALUE;

            for (BaseMongoMap<String, Object> sku : skus) {
                Double clientMsrpPrice = (Double) sku.get("clientMsrpPrice");
                Double clientRetailPrice = (Double) sku.get("clientRetailPrice");
                msrpMin = Double.min(msrpMin, clientMsrpPrice);
                msrpMax = Double.max(msrpMax, clientMsrpPrice);
                retailMin = Double.min(retailMin, clientRetailPrice);
                retailMax = Double.max(retailMax, clientRetailPrice);
            }
            usPlatform.setpPriceMsrpSt(msrpMin);
            usPlatform.setpPriceMsrpEd(msrpMax);
            usPlatform.setpPriceRetailSt(retailMin);
            usPlatform.setpPriceRetailEd(retailMax);
            usPlatform.setpPriceSaleSt(retailMin);
            usPlatform.setpPriceSaleEd(retailMax);
        }
    }

    public List<Map<String, Object>> changeModel(String channelId, Long prodId, String model) {

        JongoUpdate jongoUpdate = new JongoUpdate();

        jongoUpdate.setQuery("{\"prodId\":#}");
        jongoUpdate.setQueryParameters(prodId);
        jongoUpdate.setUpdate("{$set:{\"common.fields.model\":#}}");
        jongoUpdate.setUpdateParameters(model);
        cmsBtProductDao.updateFirst(jongoUpdate, channelId);

        List<CmsBtProductModel> cmsBtProductGroup = productService.getProductListByModel(channelId, model);
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
        return images;
    }

    //updatePrimaryCategory
    public String updatePrimaryCategory(Map<String, Object> paraMap, String channelId, String userName) {

        CmsUsaPlatformCategoryUpdateOneMQMessageBody body = new CmsUsaPlatformCategoryUpdateOneMQMessageBody();
        String selAll = (String) paraMap.get("selAll");
        List<String> codeList = (List<String>) paraMap.get("codeList");

        Integer cartId = Integer.parseInt((String) paraMap.get("cartId"));
        Map<String, Object> mapping = (Map<String, Object>) paraMap.get("mapping");
        String pCatPath = (String) paraMap.get("pCatPath");
        String pCatId = (String) paraMap.get("pCatId");
        Map<String, Object> searchInfo = (Map<String, Object>) paraMap.get("searchInfo");
        Boolean flag = (Boolean) paraMap.get("flag");

        body.setCartId(cartId);
        body.setChannelId(channelId);
        body.setFlag(flag);
        body.setMapping(mapping);
        body.setpCatId(pCatId);
        body.setpCatPath(pCatPath);
        body.setSender(userName);

        if ("true".equals(selAll)) {
            //勾选了全部,需要通过检索条件,查询出所有信息
            CmsSearchInfoBean2 queryParams = BeanUtils.toModel(searchInfo, CmsSearchInfoBean2.class);
            CmsProductCodeListBean cmsProductCodeListBean = cmsProductSearchQueryService.getProductCodeList(queryParams, channelId);
            if (cmsProductCodeListBean != null) {
                //获取分页页数,默认pagesize为100
                long pageNumber = getPageNumber(cmsProductCodeListBean.getTotalCount());
                for (int i = 0; i < pageNumber; i++) {
                    queryParams.setProductPageSize(100);
                    queryParams.setProductPageNum(i + 1);
                    CmsProductCodeListBean cmsProductCodeListBean1 = cmsProductSearchQueryService.getProductCodeList(queryParams, channelId);
                    List<String> productCodeList = cmsProductCodeListBean1.getProductCodeList();
                    body.setProductCodes(productCodeList);
                    cmsMqSenderService.sendMessage(body);
                }
            }
        } else {
            //未勾选全部
            body.setProductCodes(codeList);
           // String s = JacksonUtil.bean2Json(body);
            cmsMqSenderService.sendMessage(body);
        }
        return null;
    }

    public String updateOtherCategory(Map<String, Object> paraMap, String channelId, String userName) {

        CmsUsaPlatformCategoryUpdateManyMQMessageBody body = new CmsUsaPlatformCategoryUpdateManyMQMessageBody();
        String selAll = (String) paraMap.get("selAll");
        List<String> codeList = (List<String>) paraMap.get("codeList");

        Integer cartId = Integer.parseInt((String) paraMap.get("cartId"));
        Map<String, Object> searchInfo = (Map<String, Object>) paraMap.get("searchInfo");
        Boolean statue = (Boolean) paraMap.get("statue");
        List<String> pCatPaths = (List<String>) paraMap.get("pCatPaths");

        body.setCartId(cartId);
        body.setChannelId(channelId);
        body.setSender(userName);
        body.setStatue(statue);
        body.setpCatPath(pCatPaths);

        if ("true".equals(selAll)) {
            //勾选了全部,需要通过检索条件,查询出所有信息
            CmsSearchInfoBean2 queryParams = BeanUtils.toModel(searchInfo, CmsSearchInfoBean2.class);
            CmsProductCodeListBean cmsProductCodeListBean = cmsProductSearchQueryService.getProductCodeList(queryParams, channelId);
            if (cmsProductCodeListBean != null) {
                long pageNumber = getPageNumber(cmsProductCodeListBean.getTotalCount());
                for (int i = 0; i < pageNumber; i++) {
                    queryParams.setProductPageSize(100);
                    queryParams.setProductPageNum(i + 1);
                    CmsProductCodeListBean cmsProductCodeListBean1 = cmsProductSearchQueryService.getProductCodeList(queryParams, channelId);
                    List<String> productCodeList = cmsProductCodeListBean1.getProductCodeList();
                    body.setProductCodes(productCodeList);
                    cmsMqSenderService.sendMessage(body);
                }
            }
        } else {
            //未勾选全部
            body.setProductCodes(codeList);
            //String s = JacksonUtil.bean2Json(body);

            cmsMqSenderService.sendMessage(body);
        }
        return null;
    }
}
