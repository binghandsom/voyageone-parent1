package com.voyageone.task2.cms.service;

import com.taobao.api.ApiException;
import com.taobao.api.domain.SellerCat;
import com.taobao.top.schema.exception.TopSchemaException;
import com.taobao.top.schema.field.*;
import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.CmsChannelConfigs;
import com.voyageone.common.configs.Enums.PlatFormEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.CmsChannelConfigBean;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.masterdate.schema.factory.SchemaReader;
import com.voyageone.common.masterdate.schema.field.*;
import com.voyageone.common.masterdate.schema.field.ComplexField;
import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.common.masterdate.schema.field.InputField;
import com.voyageone.common.masterdate.schema.field.MultiCheckField;
import com.voyageone.common.masterdate.schema.field.MultiComplexField;
import com.voyageone.common.masterdate.schema.field.MultiInputField;
import com.voyageone.common.masterdate.schema.field.SingleCheckField;
import com.voyageone.common.masterdate.schema.value.ComplexValue;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.MongoUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.tmall.exceptions.GetUpdateSchemaFailException;
import com.voyageone.components.tmall.service.TbItemSchema;
import com.voyageone.components.tmall.service.TbItemService;
import com.voyageone.components.tmall.service.TbProductService;
import com.voyageone.components.tmall.service.TbSellerCatService;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.dao.cms.mongo.CmsMtPlatformCategorySchemaDao;
import com.voyageone.service.impl.cms.PlatformCategoryService;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.com.mq.config.MqRoutingKey;
import com.voyageone.service.model.cms.mongo.CmsBtSellerCatModel;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategorySchemaModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductConstants;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;
import com.voyageone.task2.base.BaseMQCmsService;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.base.util.TaskControlUtils;
import com.voyageone.task2.cms.bean.TmpOldCmsDataBean;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author james.li on 2016/7/11.
 * @version 2.0.0
 */
@Service
@RabbitListener(queues = MqRoutingKey.CMS_BATCH_TMFieldsImportCms2Job)
public class CmsPlatformProductImport2Service extends BaseMQCmsService {

    @Autowired
    private ProductGroupService productGroupService;

    @Autowired
    private TbProductService tbProductService;

    @Autowired
    private CmsBtProductDao cmsBtProductDao;

    @Autowired
    private TbSellerCatService tbSellerCatService;

    @Autowired
    private TbItemService tbItemService;

    @Autowired
    private CmsMtPlatformCategorySchemaDao cmsMtPlatformCategorySchemaDao;

    @Autowired
    private PlatformCategoryService platformCategoryService;

    @Override
    public void onStartup(Map<String, Object> messageMap) throws Exception {

        doMain((String) messageMap.get("channelId"));
    }

    private void doMain(String channelId) throws Exception {
        JomgoQuery queryObject = new JomgoQuery();
        queryObject.setQuery("{cartId:23,numIId:{$nin:[\"\",null]}}");
        Long cnt = productGroupService.countByQuery(queryObject.getQuery(), channelId);
        List<CmsBtProductGroupModel> cmsBtProductGroupModels = productGroupService.getList(channelId, queryObject);
        ShopBean shopBean = Shops.getShop(channelId, 23);
//        shopBean.setApp_url("http://gw.api.taobao.com/router/rest");
//        shopBean.setAppKey("21008948");
//        shopBean.setAppSecret("0a16bd08019790b269322e000e52a19f");
//        shopBean.setSessionKey("620230429acceg4103a72932e22e4d53856b145a192140b2854639042");
//        shopBean.setShop_name("Target海外旗舰店");

        List<CmsBtSellerCatModel> sellerCat = new ArrayList<>();

        List<SellerCat> sellerCatList = tbSellerCatService.getSellerCat(shopBean);
        sellerCat = formatTMModel(sellerCatList, channelId, 23, getTaskName());
        convert2Tree(sellerCat);

        final List<CmsBtSellerCatModel> finalSellerCat = sellerCat;
        for (int i = 0; i < cmsBtProductGroupModels.size(); i++) {
            CmsBtProductGroupModel item = cmsBtProductGroupModels.get(i);
            try {
                $info(String.format("%s-%s天猫属性取得 %d/%d", channelId, item.getNumIId(), i+1, cnt));
                doSetProduct(shopBean, item, channelId, finalSellerCat);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 将TM店铺自定义分类Model转换成CmsBtSellerCatModel
     */
    private List<CmsBtSellerCatModel> formatTMModel(List<SellerCat> list, String channelId, int cartId, String creator) {
        List<CmsBtSellerCatModel> result = new ArrayList<>();

        for (SellerCat model : list) {
            CmsBtSellerCatModel cmsBtSellerCatModel = new CmsBtSellerCatModel();
            cmsBtSellerCatModel.setCatId(String.valueOf(model.getCid()));
            cmsBtSellerCatModel.setCatName(model.getName());
            cmsBtSellerCatModel.setParentCatId(String.valueOf(model.getParentCid()));
            cmsBtSellerCatModel.setChannelId(channelId);
            cmsBtSellerCatModel.setCartId(cartId);
            String now = DateTimeUtil.getNow();
            cmsBtSellerCatModel.setCreated(now);
            cmsBtSellerCatModel.setModified(now);
            cmsBtSellerCatModel.setCreater(creator);
            cmsBtSellerCatModel.setModifier(creator);
            result.add(cmsBtSellerCatModel);
        }


        return result;
    }

    private List<Map<String, Object>> doSetSeller(ShopBean shopBean, Long nummIId, List<CmsBtSellerCatModel> sellerCat) throws Exception {
        TbItemSchema schema = tbItemService.getUpdateSchema(shopBean, nummIId);

        List<com.taobao.top.schema.field.Field> fields = schema.getFields();

        List<String> cIds = new ArrayList<>();

        for (com.taobao.top.schema.field.Field field : fields) {
            if ("seller_cids".equals(field.getId())) {
                List<String> values = ((com.taobao.top.schema.field.MultiCheckField) field).getDefaultValues();

                for (String value : values) {
                    cIds.add(value);
                }
            }
        }


        List<Map<String, Object>> sellerCats = new ArrayList<>();

        for (String pCId : cIds) {
            CmsBtSellerCatModel leaf = sellerCat.stream().filter(w -> pCId.equals(w.getCatId())).findFirst().get();
            Map<String, Object> model = new HashMap<>();
            model.put("cId", leaf.getCatId());
            model.put("cName", leaf.getCatPath());
            model.put("cIds", leaf.getFullCatId().split("-"));
            model.put("cNames", leaf.getCatPath().split(">"));

            sellerCats.add(model);
        }
        return sellerCats;
    }

    private void doSetProduct(ShopBean shopBean, CmsBtProductGroupModel cmsBtProductGroup, String channelId, List<CmsBtSellerCatModel> sellerCat) throws Exception {
        Map<String, Object> fieldMap = new HashMap<>();
        if (PlatFormEnums.PlatForm.TM.getId().equals(shopBean.getPlatform_id())) {
            // 只有天猫系才会更新fields字段
            fieldMap.putAll(getPlatformProduct(cmsBtProductGroup.getPlatformPid(), shopBean));
            fieldMap.putAll(getPlatformWareInfoItem(cmsBtProductGroup.getNumIId(), shopBean));
        }
        List<Map<String, Object>> sellerCats = doSetSeller(shopBean, Long.parseLong(cmsBtProductGroup.getNumIId()), sellerCat);
        upProductPlatform(fieldMap, cmsBtProductGroup, channelId, sellerCats);
    }

    private void upProductPlatform(Map<String, Object> fieldMap, CmsBtProductGroupModel cmsBtProductGroup, String channelId, List<Map<String, Object>> sellerCats) {
        List<BulkUpdateModel> bulkList = new ArrayList<>();
        cmsBtProductGroup.getProductCodes().forEach(s -> {
            HashMap<String, Object> queryMap = new HashMap<>();
            queryMap.put("common.fields.code", s);
            // added by morse.lu 2016/07/18 start
            CmsBtProductModel product = cmsBtProductDao.selectByCode(s, channelId);
            List<String> listSkuCode = product.getCommon().getSkus().stream().map(CmsBtProductModel_Sku::getSkuCode).collect(Collectors.toList());
            // added by morse.lu 2016/07/18 end
            HashMap<String, Object> updateMap = new HashMap<>();
            updateMap.put("platforms.P23.modified", DateTimeUtil.getNowTimeStamp());
            updateMap.put("platforms.P23.sellerCats", sellerCats);
            final boolean[] hasPublishSku = {false, false}; // 第一个表示是否是sku级的，第二个表示本code是否有sku上新过
            fieldMap.forEach((s1, o) -> {
                // added by morse.lu 2016/07/18 start
                if ("sku".equals(s1)) {
                    hasPublishSku[0] = true;
                    List<Map<String, Object>> upValSku = new ArrayList<>();
                    List<Map<String, Object>> listVal = (List) o;
                    listVal.forEach(skuVal -> {
                        if (listSkuCode.contains(skuVal.get("sku_outerId"))) {
                            upValSku.add(skuVal);
                            hasPublishSku[1] = true;
                        }
                    });
                    updateMap.put("platforms.P23.fields." + s1, upValSku);
//                } else if ("prop_13021751".equals(s1)) {
                    // 不要回写,model是主字段,会影响别的逻辑,改上新逻辑,货号优先去platform.P23.prop_13021751里取，取不到，再用model
//                    // 货号，回写进主商品common.fields.model
//                    updateMap.put("common.fields.model", o);
                } else {
                    // added by morse.lu 2016/07/18 end
                    updateMap.put("platforms.P23.fields." + s1, o);
                }
            });
            // added by morse.lu 2016/07/18 start
            String catId = (String) fieldMap.get("cat_id"); // 类目ID
            if (!StringUtils.isEmpty(catId)) {
                // 取到了再回写
                updateMap.put("platforms.P23.pCatId", catId);
//                CmsMtPlatformCategorySchemaModel cmsMtPlatformCategorySchemaModel = cmsMtPlatformCategorySchemaDao.selectPlatformCatSchemaModel(catId, 23);
                CmsMtPlatformCategorySchemaModel cmsMtPlatformCategorySchemaModel = platformCategoryService.getPlatformCatSchemaTm(catId, channelId, 23);
                if (cmsMtPlatformCategorySchemaModel != null) {
                    updateMap.put("platforms.P23.pCatPath", cmsMtPlatformCategorySchemaModel.getCatFullPath());
                } else {
                    updateMap.put("platforms.P23.pCatPath", "");
                }
            } else{
                // 产品id错了，取不到产品信息
                $warn(String.format("PlatformPid[%s] numIid=[%s] 天猫上不存在!group表PlatformPid已经清除,product表的平台类目pCatId需要重新选择填写!", cmsBtProductGroup.getPlatformPid(), cmsBtProductGroup.getNumIId()));
                cmsBtProductGroup.setPlatformPid("");
            }

            if (!hasPublishSku[0] || hasPublishSku[1]) {
                // product级 或者 本code有sku上新过
                updateMap.put("platforms.P23.pProductId", cmsBtProductGroup.getPlatformPid());
                updateMap.put("platforms.P23.pNumIId", cmsBtProductGroup.getNumIId());
                String item_status = (String) fieldMap.get("item_status"); // 商品状态
                if ("0".equals(item_status)) {
                    // 出售中
                    updateMap.put("platforms.P23.pStatus", CmsConstants.PlatformStatus.OnSale.name());
                } else {
                    // 仓库中
                    updateMap.put("platforms.P23.pStatus", CmsConstants.PlatformStatus.InStock.name());
                }
            }
            // added by morse.lu 2016/07/18 end
            BulkUpdateModel model = new BulkUpdateModel();
            model.setUpdateMap(updateMap);
            model.setQueryMap(queryMap);
            bulkList.add(model);
        });

        cmsBtProductDao.bulkUpdateWithMap(cmsBtProductGroup.getChannelId(), bulkList, getTaskName(), "$set");

//        {
//            // price 回写进common.skus.size和platforms.P23.skus下的priceMsrp或priceSale
//            // size 先不回写
//            // 该group下的所有code
//            List<String> productCodeList = cmsBtProductGroup.getProductCodes();
//            String[] codeArr = new String[productCodeList.size()];
//            codeArr = productCodeList.toArray(codeArr);
//            List<CmsBtProductModel> productModelList = cmsBtProductDao.select("{" + MongoUtils.splicingValue("common.fields.code", codeArr, "$in") + "}", channelId);
//
//            String pricePropName = getPricePropName(channelId);
//
//            List<Map<String, Object>> listSkus = (List) fieldMap.get("sku");
//            // Map<skuCode, Map<String, Object>>
//            Map<String, Map<String, Object>> mapSkus = listSkus.stream().collect(Collectors.toMap((p) -> (String) p.get("sku_outerId"), (p) -> p));
//
//            productModelList.forEach(model-> {
//                model.getPlatform(23).getSkus().forEach(sku -> {
//                    String skuCode = sku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.skuCode.name());
//                    if (mapSkus.get(skuCode) != null) {
//                        String price = (String) mapSkus.get(skuCode).get("sku_price");
//                        sku.setStringAttribute(pricePropName, price);
//                    }
//                });
//
//                try {
//                    // ★★★★★此更新方法已经被干掉了，需要的话，本地打开★★★★★
//                    cmsBtProductDao.updateByModel(model);
//                } catch (BusinessException ex) {
//                    $warn("product表更新关闭!");
//                }
//            });
//        }

        // added by morse.lu 2016/07/18 start
        String item_status = (String) fieldMap.get("item_status"); // 商品状态
        if ("0".equals(item_status)) {
            // 出售中
            cmsBtProductGroup.setPlatformStatus(CmsConstants.PlatformStatus.OnSale);
            cmsBtProductGroup.setPlatformActive(CmsConstants.PlatformActive.ToOnSale);
        } else {
            // 仓库中
            cmsBtProductGroup.setPlatformStatus(CmsConstants.PlatformStatus.InStock);
            cmsBtProductGroup.setPlatformActive(CmsConstants.PlatformActive.ToInStock);
        }
        productGroupService.update(cmsBtProductGroup);
        // added by morse.lu 2016/07/18 end
    }

    private String getPricePropName(String channelId) {
        CmsChannelConfigBean sxPriceConfig = CmsChannelConfigs.getConfigBean(channelId
                                    , CmsConstants.ChannelConfig.PRICE
                                    , String.valueOf(23) + CmsConstants.ChannelConfig.PRICE_SX_PRICE);

        if (sxPriceConfig == null) {
            return null;
        } else {
            return sxPriceConfig.getConfigValue1();
        }
    }

    private Map<String, Object> getPlatformProduct(String productId, ShopBean shopBean) throws Exception {
        fieldHashMap fieldMap = new fieldHashMap();
        if(StringUtils.isEmpty(productId)) return fieldMap;
        String schema = tbProductService.getProductSchema(Long.parseLong(productId), shopBean);
        if (schema != null) {
            List<Field> fields = SchemaReader.readXmlForList(schema);


            fields.forEach(field -> {
                fields2Map(field, fieldMap);
            });
        }
        return fieldMap;

    }

    private Map<String, Object> getPlatformWareInfoItem(String numIid, ShopBean shopBean) throws Exception {
        fieldHashMap fieldMap = new fieldHashMap();
        String schema = tbProductService.doGetWareInfoItem(numIid, shopBean).getUpdateItemResult();
        if (schema != null) {
            List<Field> fields = SchemaReader.readXmlForList(schema);
            fields.forEach(field -> {
                fields2Map(field, fieldMap);
            });
        }
        return fieldMap;

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
                fieldHashMap values = new fieldHashMap();
                if (complexField.getDefaultComplexValue() != null) {
                    for (String fieldId : complexField.getDefaultComplexValue().getFieldKeySet()) {
                        values.put(fieldId, getFieldValue(complexField.getDefaultComplexValue().getValueField(fieldId)));
                    }
                }
                fieldMap.put(field.getId(), values);
                break;
            case MULTICOMPLEX:
                MultiComplexField multiComplexField = (MultiComplexField) field;
                List<Map<String, Object>> multiComplexValues = new ArrayList<>();
                if (multiComplexField.getDefaultComplexValues() != null) {
                    for (ComplexValue item : multiComplexField.getDefaultComplexValues()) {
                        fieldHashMap obj = new fieldHashMap();
                        for (String fieldId : item.getFieldKeySet()) {
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
                fieldHashMap complexValues = new fieldHashMap();
                for (String key : fieldMap.keySet()) {
                    complexValues.put(key, getFieldValue(fieldMap.get(key)));
                }
                return complexValues;

            case MULTICOMPLEX:
                MultiComplexField multiComplexField = (MultiComplexField) field;
                List<Object> multiComplexValues = new ArrayList<>();
                if (multiComplexField.getFieldMap() != null) {
                    for (ComplexValue item : multiComplexField.getComplexValues()) {
                        for (String fieldId : item.getFieldKeySet()) {
                            multiComplexValues.add(getFieldValue(item.getValueField(fieldId)));
                        }
                    }
                }
                return multiComplexValues;
        }

        return null;
    }

    class fieldHashMap extends HashMap<String, Object> {
        @Override
        public Object put(String key, Object value) {
            if (value == null) {
                return value;
            }
            return super.put(StringUtils.replaceDot(key), value);
        }
    }

    /**
     * 将店铺自定义分类列转成一组树
     */
    private List<CmsBtSellerCatModel> convert2Tree(List<CmsBtSellerCatModel> sellCatList) {
        List<CmsBtSellerCatModel> roots = findRoots(sellCatList);
        List<CmsBtSellerCatModel> notRoots = (List<CmsBtSellerCatModel>) CollectionUtils.subtract(sellCatList, roots);
        for (CmsBtSellerCatModel root : roots) {
            List<CmsBtSellerCatModel> children = findChildren(root, notRoots);
            root.setChildren(children);
        }
        return roots;

    }

    /**
     * 查找所有子节点
     */
    private List<CmsBtSellerCatModel> findChildren(CmsBtSellerCatModel root, List<CmsBtSellerCatModel> allNodes) {
        List<CmsBtSellerCatModel> children = new ArrayList<>();

        for (CmsBtSellerCatModel comparedOne : allNodes) {
            if (comparedOne.getParentCatId().equals(root.getCatId())) {
                children.add(comparedOne);
                comparedOne.setCatPath(root.getCatPath() + ">" + comparedOne.getCatName());
                comparedOne.setFullCatId(root.getFullCatId() + "-" + comparedOne.getCatId());
            }
        }
        root.setChildren(children);
        if (!children.isEmpty()) {
            root.setIsParent(1);
        } else {
            root.setIsParent(0);
        }

        List<CmsBtSellerCatModel> notChildren = (List<CmsBtSellerCatModel>) CollectionUtils.subtract(allNodes, children);

        for (CmsBtSellerCatModel child : children) {
            List<CmsBtSellerCatModel> tmpChildren = findChildren(child, notChildren);

            child.setChildren(tmpChildren);
        }

        return children;
    }

    /**
     * 查找所有根节点
     */
    private List<CmsBtSellerCatModel> findRoots(List<CmsBtSellerCatModel> allNodes) {
        List<CmsBtSellerCatModel> results = new ArrayList<>();
        for (CmsBtSellerCatModel node : allNodes) {
            if ("0".equals(node.getParentCatId())) {
                results.add(node);
                node.setCatPath(node.getCatName());
                node.setFullCatId(node.getCatId());
            }
        }
        return results;
    }
}
