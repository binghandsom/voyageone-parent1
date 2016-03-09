package com.voyageone.task2.cms.service;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.service.dao.cms.mongo.CmsMtCategorySchemaDao;
import com.voyageone.service.dao.cms.mongo.CmsMtCommonSchemaDao;
import com.voyageone.service.model.cms.mongo.CmsMtCategorySchemaModel;
import com.voyageone.service.model.cms.mongo.CmsMtCommonSchemaModel;
import com.voyageone.service.model.cms.mongo.product.*;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.cms.bean.TmpOldCmsDataBean;
import com.voyageone.task2.cms.dao.TmpOldCmsDataDao;
import com.voyageone.cms.CmsConstants;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.components.tmall.TbProductService;
import com.voyageone.common.configs.Enums.PlatFormEnums;
import com.voyageone.common.configs.ShopConfigs;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.masterdate.schema.factory.SchemaReader;
import com.voyageone.common.masterdate.schema.field.*;
import com.voyageone.common.masterdate.schema.value.ComplexValue;
import com.voyageone.common.util.MD5;
import com.voyageone.common.util.StringUtils;
import com.voyageone.web2.sdk.api.VoApiDefaultClient;
import com.voyageone.web2.sdk.api.request.ProductGetRequest;
import com.voyageone.web2.sdk.api.request.ProductGroupsPutRequest;
import com.voyageone.web2.sdk.api.request.ProductUpdateRequest;
import com.voyageone.web2.sdk.api.response.ProductGetResponse;
import com.voyageone.web2.sdk.api.response.ProductGroupsPutResponse;
import com.voyageone.web2.sdk.api.response.ProductUpdateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author james.li on 2016/1/21.
 * @version 2.0.0
 */
@Service
public class CmsPlatformProductImportService extends BaseTaskService {

    @Autowired
    private TbProductService tbProductService;

    @Autowired
    private CmsMtCategorySchemaDao cmsMtCategorySchemaDao; // DAO: 主类目属性结构
    @Autowired
    private CmsMtCommonSchemaDao cmsMtCommonSchemaDao; // DAO: 共通属性结构
    @Autowired
    private TmpOldCmsDataDao tmpOldCmsDataDao; // DAO: 旧cms数据
    @Autowired
    private VoApiDefaultClient voApiClient; // VoyageOne共通API

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "CmsPlatformProductImportService";
    }

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        doMain();

    }

    public void doMain() throws Exception {

        // 获取cms_tmp_old_cms_data的数据 ==========================================================================================
        List<TmpOldCmsDataBean> oldCmsDataBeenList = tmpOldCmsDataDao.getList();

        // 遍历, 并设置主数据
        for (TmpOldCmsDataBean oldCmsDataBean : oldCmsDataBeenList) {
            doSetProduct(oldCmsDataBean);
        }

    }

    private void doSetProduct(TmpOldCmsDataBean oldCmsDataBean) throws Exception {
        ShopBean shopBean = ShopConfigs.getShop(oldCmsDataBean.getChannel_id(), oldCmsDataBean.getCart_id());
        if (shopBean == null) {
            // 不存在的shop, 跳过
            return;
        }

        // tom 20160129 测试代码, 正式发布时要删除的 START
        shopBean.setApp_url("http://gw.api.taobao.com/router/rest");
        shopBean.setAppKey("21008948");
        shopBean.setSessionKey("6201d2770dbfa1a88af5acfd330fd334fb4ZZa8ff26a40b2641101981");
        shopBean.setAppSecret("0a16bd08019790b269322e000e52a19f");
        shopBean.setOrder_channel_id("010");
        // tom 20160129 测试代码, 正式发布时要删除的 END

        // 属性名字列表
        List<String> schemaFieldList = new ArrayList<>();
        List<String> schemaFieldSkuList = new ArrayList<>(); // sku级

        // 获取共通schema数据 ==========================================================================================
        CmsMtCommonSchemaModel comSchemaModel = getComSchemaModel();
        for (Field field : comSchemaModel.getFields()) {
            schemaFieldList.add(field.getId());
        }

        // 获取主数据当前类目的schema数据 ==========================================================================================
        CmsMtCategorySchemaModel schemaModel = cmsMtCategorySchemaDao.getMasterSchemaModelByCatId(MD5.getMD5(oldCmsDataBean.getCategory_path()));
        if (schemaModel == null) {
            // 指定的category在主数据里没有的话, 跳过
            return;
        }
        for (Field field : schemaModel.getFields()) {
            schemaFieldList.add(field.getId());
        }
        MultiComplexField multiComplexField = (MultiComplexField)schemaModel.getSku();
        for (Field field : multiComplexField.getFields()) {
            schemaFieldSkuList.add(field.getId());
        }

        // 获取product表的数据 ==========================================================================================
        CmsBtProductModel cmsProduct = getCmsProduct(oldCmsDataBean.getChannel_id(), oldCmsDataBean.getCode());
        if (cmsProduct == null) {
            // product表里没这个code的场合, 跳过当前记录
            return;
        }

        // 获取天猫上的数据 ==========================================================================================
        Map<String, Object> fieldMap = new HashMap<>();
        if (PlatFormEnums.PlatForm.TM.getId().equals(shopBean.getPlatform_id())) {
            // 只有天猫系才会更新fields字段
            fieldMap.putAll(getPlatformProduct(oldCmsDataBean.getProduct_id(), shopBean));
            fieldMap.putAll(getPlatformWareInfoItem(oldCmsDataBean.getNum_iid(), shopBean));
        }

        // 保存到product表 ==========================================================================================
        update2ProductFields(oldCmsDataBean, fieldMap, cmsProduct, schemaFieldList, schemaFieldSkuList);

    }

    /**
     * 获取common schema.
     * @return
     */
    private CmsMtCommonSchemaModel getComSchemaModel() {
        CmsMtCommonSchemaModel comSchemaModel = cmsMtCommonSchemaDao.getComSchema();

        if (comSchemaModel == null){

            //common schema 不存在时异常处理.
            String errMsg = "共通schema（cms_mt_common_schema）的信息不存在！";

            logger.error(errMsg);

            throw new BusinessException(errMsg);
        }

        return comSchemaModel;
    }

    private CmsBtProductModel getCmsProduct(String channelId, String code) {

        ProductGetRequest productGetRequest = new ProductGetRequest(channelId);
        productGetRequest.setProductCode(code);
        ProductGetResponse productGetResponse = voApiClient.execute(productGetRequest);

        return productGetResponse.getProduct();

    }

    private Map<String, Object> getPlatformProduct(String productId, ShopBean shopBean) throws Exception {
        String schema = tbProductService.getProductSchema(Long.parseLong(productId), shopBean);

        List<Field> fields = SchemaReader.readXmlForList(schema);

        fieldHashMap fieldMap = new fieldHashMap();
        fields.forEach(field -> {
            fields2Map(field, fieldMap);
        });
        return fieldMap;

    }

    private Map<String, Object> getPlatformWareInfoItem(String numIid, ShopBean shopBean) throws Exception {
        String schema = tbProductService.doGetWareInfoItem(numIid, shopBean).getUpdateItemResult();
        List<Field> fields = SchemaReader.readXmlForList(schema);

        fieldHashMap fieldMap = new fieldHashMap();
        fields.forEach(field -> {
            fields2Map(field, fieldMap);
        });

        return fieldMap;

    }

    private void update2ProductFields(
            TmpOldCmsDataBean oldCmsDataBean,
            Map<String, Object> fields,
            CmsBtProductModel cmsProduct,
            List<String> schemaFieldList,
            List<String> schemaFieldSkuList) {

        ShopBean shopBean = ShopConfigs.getShop(oldCmsDataBean.getChannel_id(), oldCmsDataBean.getCart_id());
        if (shopBean == null) {
            return;
        }
        if (PlatFormEnums.PlatForm.TM.getId().equals(shopBean.getPlatform_id())) {
            // 只有天猫系才会更新fields字段

            CmsBtProductModel_Field cmsFields = new CmsBtProductModel_Field();
            List<CmsBtProductModel_Sku> skuList = new ArrayList<>();

            // 天猫取得的字段设定 ==========================================================================================
            for (String key : fields.keySet()) {
                // 看看schema里是否存在
                if (!schemaFieldList.contains(key) && !"sku".equals(key)) {
                    // schema里没有的字段, 无需设置
                    continue;
                }

                // 看看是否是属于不想设置的字段
                if ("brand".equals(key)
                        || "code".equals(key)
                        || "model".equals(key)
                        || "priceMsrpEd".equals(key)
                        || "priceMsrpSt".equals(key)
                        || "priceRetailEd".equals(key)
                        || "priceRetailSt".equals(key)
                        || "productNameEn".equals(key)
                        || "status".equals(key)
                        ) {
                    // 不想设置的字段, 就跳过
                    continue;
                }

                // 设定
                if (schemaFieldList.contains(key)) {
                    // 商品信息
                    cmsFields.setAttribute(key, fields.get(key));
                } else if ("sku".equals(key)) {
                    // sku级别信息
                    List<Map<String, Object>> tmallSkuList = (List<Map<String, Object>>)fields.get(key);
                    for (Map<String, Object> tmallSku : tmallSkuList) {

                        CmsBtProductModel_Sku sku = new CmsBtProductModel_Sku();

                        tmallSku.forEach((k,v)->{
                            // 去除不想设置的字段
                            if ("sku_outerId".equals(k)
                                    // 天猫上拉下来的字段
                                    || "sku_price".equals(k)
                                    || "sku_id".equals(k)
                                    || "sku_quantity".equals(k)
                                    || "sku_barcode".equals(k)

                                    // 万一有遇到与主数据的字段名称一样的, 那也不需要更新
                                    || "skuCode".equals(k)
                                    || "size".equals(k)
                                    || "qty".equals(k)
                                    || "priceMsrp".equals(k)
                                    || "priceRetail".equals(k)
                                    || "priceSale".equals(k)
                                    || "skuCarts".equals(k)
                                    || "barcode".equals(k)

                                    ) {
                                // 不想设置的字段, 就跳过

                            } else {
                                // 看看schema里是否存在
                                if (!schemaFieldSkuList.contains(k)) {
                                    // schema里没有的字段, 无需设置
                                } else {
                                    // 设定
                                    cmsProduct.getSkus().forEach((item)->{
                                        if (item.getSkuCode().equals(tmallSku.get("sku_outerId"))) {
                                            item.put(k, v);
                                        }
                                    });
                                }

                            }

                        });

                    }

                    System.out.println(oldCmsDataBean.getCode() + ":" + key + ":" + fields.get(key));
                }
            }

            // 固定字段设定 ==========================================================================================
            // product状态: 因为已经上了第三方平台, 所以默认设置为Approved
            cmsFields.setStatus(CmsConstants.ProductStatus.Approved);
            // 货号
            cmsFields.setAttribute("prop_13021751", oldCmsDataBean.getModel());
            // 英文标题
            cmsFields.setProductNameEn(oldCmsDataBean.getTitle_en());
            // 中文标题
            cmsFields.setProductNameCn(oldCmsDataBean.getTitle_cn());
            cmsFields.setLongTitle(oldCmsDataBean.getTitle_cn());
            // 英文描述
            cmsFields.setLongDesEn(oldCmsDataBean.getDescription_en());
            // 中文描述
            cmsFields.setLongDesCn(oldCmsDataBean.getDescription_cn());
            // 图片1
            List<String> imgListString = oldCmsDataBean.getImageList(oldCmsDataBean.getImg1());
            List<CmsBtProductModel_Field_Image> imgList1 = new ArrayList<>();
            imgListString.forEach(img->imgList1.add(new CmsBtProductModel_Field_Image("image1", img)));
            cmsFields.setImages1(imgList1);
            // 图片2
            imgListString = oldCmsDataBean.getImageList(oldCmsDataBean.getImg2());
            List<CmsBtProductModel_Field_Image> imgList2 = new ArrayList<>();
            imgListString.forEach(img->imgList2.add(new CmsBtProductModel_Field_Image("image2", img)));
            cmsFields.setImages2(imgList2);
            // 图片3
            imgListString = oldCmsDataBean.getImageList(oldCmsDataBean.getImg3());
            List<CmsBtProductModel_Field_Image> imgList3 = new ArrayList<>();
            imgListString.forEach(img->imgList3.add(new CmsBtProductModel_Field_Image("image3", img)));
            cmsFields.setImages3(imgList3);
            // 图片4
            imgListString = oldCmsDataBean.getImageList(oldCmsDataBean.getImg4());
            List<CmsBtProductModel_Field_Image> imgList4 = new ArrayList<>();
            imgListString.forEach(img->imgList4.add(new CmsBtProductModel_Field_Image("image4", img)));
            cmsFields.setImages4(imgList4);
            // 英文颜色
            cmsFields.setColor(oldCmsDataBean.getColor_en());
            // hs_code_pu 个人行邮税号
            cmsFields.setHsCodePrivate(oldCmsDataBean.getHs_code_pu());

            // product ==========================================================================================
            cmsProduct.setFields(cmsFields);
            cmsProduct.setCatPath(oldCmsDataBean.getCategory_path());
            cmsProduct.setCatId(MD5.getMD5(oldCmsDataBean.getCategory_path()));

        }

        // 设置platform信息
        List<CmsBtProductModel_Group_Platform> platformList = cmsProduct.getGroups().getPlatforms();
        for (CmsBtProductModel_Group_Platform platform : platformList) {

            if (platform.getCartId() == Integer.parseInt(oldCmsDataBean.getCart_id())) {
                platform.setNumIId(oldCmsDataBean.getNum_iid());
                platform.setProductId(oldCmsDataBean.getProduct_id());

                String status = fields.get("item_status").toString();
                switch (status) {
                    case "0": // 出售中
                        platform.setPlatformStatus(CmsConstants.PlatformStatus.Onsale);
                        break;
                    default: // 定时上架 或者 仓库中
                        platform.setPlatformStatus(CmsConstants.PlatformStatus.Instock);
                }

                // 更新group
                ProductGroupsPutRequest productGroupsPutRequest = new ProductGroupsPutRequest();
                productGroupsPutRequest.setChannelId(oldCmsDataBean.getChannel_id());
                productGroupsPutRequest.setPlatform(platform);
                Set<Long> lngSet = new HashSet<>();
                lngSet.add(cmsProduct.getProdId());
                productGroupsPutRequest.setProductIds(lngSet);
                ProductGroupsPutResponse productGroupsPutResponse = voApiClient.execute(productGroupsPutRequest);
                logger.info(String.format("从天猫获取product数据到cms:group:[code:%s, 结果:%s, 错误内容:%s]", oldCmsDataBean.getCode(), productGroupsPutResponse.getCode(), productGroupsPutResponse.getMessage()));

                break;
            }
        }

        // 提交到product表 ==========================================================================================
        ProductUpdateRequest productUpdateRequest = new ProductUpdateRequest(oldCmsDataBean.getChannel_id());
        productUpdateRequest.setProductModel(cmsProduct);
        productUpdateRequest.setModifier(getTaskName());
        productUpdateRequest.setIsCheckModifed(false); // 不做最新修改时间ｃｈｅｃｋ

        ProductUpdateResponse response = voApiClient.execute(productUpdateRequest);

        // 更新cms_tmp_old_cms_data表
        tmpOldCmsDataDao.setFinish(oldCmsDataBean.getChannel_id(), oldCmsDataBean.getCart_id(), oldCmsDataBean.getCode());

        logger.info(String.format("从天猫获取product数据到cms:[code:%s, 结果:%s, 错误内容:%s]", oldCmsDataBean.getCode(), response.getCode(), response.getMessage()));

        System.out.println("ok");
    }

    private void fields2Map(Field field, fieldHashMap fieldMap) {

        switch (field.getType()) {
            case INPUT:
                InputField inputField = (InputField) field;
                fieldMap.put(inputField.getId(), inputField.getDefaultValue());
                break;
            case MULTIINPUT:
                MultiInputField multiInputField = (MultiInputField) field;
                fieldMap.put(multiInputField.getId(), multiInputField.getDefaultValues());
                break;
            case LABEL:
                return;
            case SINGLECHECK:
                SingleCheckField singleCheckField = (SingleCheckField) field;
                fieldMap.put(singleCheckField.getId(), singleCheckField.getDefaultValue());
                break;
            case MULTICHECK:
                MultiCheckField multiCheckField = (MultiCheckField) field;
                fieldMap.put(multiCheckField.getId(), multiCheckField.getDefaultValues());
                break;
            case COMPLEX:
                ComplexField complexField = (ComplexField) field;
                Map<String,Object> values = new HashMap<>();
                if (complexField.getDefaultComplexValue() != null) {
                    for(String fieldId : complexField.getDefaultComplexValue().getFieldKeySet()){
                        values.put(fieldId, getFieldValue(complexField.getDefaultComplexValue().getValueField(fieldId)));
                    }
                }
                fieldMap.put(field.getId(), values);
                break;
            case MULTICOMPLEX:
                MultiComplexField multiComplexField = (MultiComplexField) field;
                List<Map<String, Object>> multiComplexValues = new ArrayList<>();
                if (multiComplexField.getDefaultComplexValues() != null) {
                    for(ComplexValue item : multiComplexField.getDefaultComplexValues()){
                        Map<String, Object> obj = new HashMap<>();
                        for(String fieldId : item.getFieldKeySet()){
                            obj.put(fieldId, getFieldValue(item.getValueField(fieldId)));
                        }
                        multiComplexValues.add(obj);
                    }
                }
                fieldMap.put(multiComplexField.getId(), multiComplexValues);
                break;
        }
    }

    private Object getFieldValue(Field field) {
        List<String> values;
        switch (field.getType()) {
            case INPUT:
                InputField inputField = (InputField) field;
                return inputField.getValue();

            case MULTIINPUT:
                MultiInputField multiInputField = (MultiInputField) field;
                values = new ArrayList<>();
                multiInputField.getValues().forEach(value -> values.add(value.getValue()));
                return values;

            case SINGLECHECK:
                SingleCheckField singleCheckField = (SingleCheckField) field;
                return singleCheckField.getValue().getValue();

            case MULTICHECK:
                MultiCheckField multiCheckField = (MultiCheckField) field;
                values = new ArrayList<>();
                multiCheckField.getValues().forEach(value -> values.add(value.getValue()));
                return values;

            case COMPLEX:
                ComplexField complexField = (ComplexField) field;
                Map<String, Field> fieldMap = complexField.getFieldMap();
                Map<String,Object> complexValues = new HashMap<>();
                for (String key : fieldMap.keySet()) {
                    complexValues.put(key, getFieldValue(fieldMap.get(key)));
                }
                return complexValues;

            case MULTICOMPLEX:
                MultiComplexField multiComplexField = (MultiComplexField) field;
                List<Object> multiComplexValues = new ArrayList<>();
                if (multiComplexField.getFieldMap() != null) {
                    for(ComplexValue item : multiComplexField.getComplexValues()){
                        for(String fieldId : item.getFieldKeySet()){
                            multiComplexValues.add(getFieldValue(item.getValueField(fieldId)));
                        }
                    }
                }
                return multiComplexValues;
        }

        return null;
    }

    class fieldHashMap extends HashMap<String,Object>{
        @Override
        public Object put(String key, Object value){
            if(value == null){
                return value;
            }
            return  super.put(StringUtils.replaceDot(key),value);
        }
    }
}
