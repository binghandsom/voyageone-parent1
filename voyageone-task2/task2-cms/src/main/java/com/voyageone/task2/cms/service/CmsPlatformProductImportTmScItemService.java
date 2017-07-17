package com.voyageone.task2.cms.service;

import com.taobao.api.ApiException;
import com.taobao.api.domain.ScItem;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.masterdate.schema.factory.SchemaReader;
import com.voyageone.common.masterdate.schema.field.*;
import com.voyageone.common.masterdate.schema.value.ComplexValue;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.tmall.service.TbProductService;
import com.voyageone.components.tmall.service.TbScItemService;
import com.voyageone.service.dao.cms.CmsBtPlatformNumiidDao;
import com.voyageone.service.dao.cms.CmsBtTmScItemDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.daoext.cms.CmsBtPlatformNumiidDaoExt;
import com.voyageone.service.impl.cms.TaobaoScItemService;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;
import com.voyageone.service.model.cms.CmsBtPlatformNumiidModel;
import com.voyageone.service.model.cms.CmsBtTmScItemModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductConstants;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.task2.base.BaseMQCmsService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author morse.lu on 2017/7/14.
 */
@Service
@RabbitListener(queues = CmsMqRoutingKey.CMS_BATCH_TMScItemImportCms2Job)
public class CmsPlatformProductImportTmScItemService extends BaseMQCmsService {

    @Autowired
    private ProductGroupService productGroupService;
    @Autowired
    private SxProductService sxProductService;

    @Autowired
    private TbProductService tbProductService;
    @Autowired
    private TaobaoScItemService taobaoScItemService;
    @Autowired
    private TbScItemService tbScItemService;

    @Autowired
    private CmsBtProductDao cmsBtProductDao;
    @Autowired
    private CmsBtTmScItemDao cmsBtTmScItemDao;

    @Autowired
    private CmsBtPlatformNumiidDao cmsBtPlatformNumiidDao;
    @Autowired
    private CmsBtPlatformNumiidDaoExt cmsBtPlatformNumiidDaoExt;


    @Override
    public void onStartup(Map<String, Object> messageMap) throws Exception {

        String channelId = null;
        if (messageMap.containsKey("channelId")) {
            channelId = String.valueOf(messageMap.get("channelId"));
        }
        String cartId = null;
        if (messageMap.containsKey("cartId")) {
            cartId = String.valueOf(messageMap.get("cartId"));
            if (!CartEnums.Cart.isTmSeries(CartEnums.Cart.getValueByID(cartId))) {
                $error("入参的平台id不是天猫系!");
                return;
            }
        }
        String numIId = null;
        if (messageMap.containsKey("numIId")) {
            numIId = String.valueOf(messageMap.get("numIId"));
        }
        String code = null;
        if (messageMap.containsKey("code")) {
            code = String.valueOf(messageMap.get("code"));
        }

        String runType = null; // runType=2 从cms_bt_platform_numiid表里抽出numIId去做
        if (messageMap.containsKey("runType")) {
            runType = String.valueOf(messageMap.get("runType"));
        }

        doMain(channelId, cartId, numIId, code, runType);
    }

    private void doMain(String channelId, String cartId, String numIId, String code, String runType) throws Exception {
        JongoQuery queryObject = new JongoQuery();

        String query = "cartId:" + cartId;
        List<String> listAllNumiid = null;
        List<String> listSuccessNumiid = new ArrayList<>();
        List<String> listErrorNumiid = new ArrayList<>();
        if ("2".equals(runType)) {
            // 从cms_bt_platform_numiid表里抽出numIId去做
            Map<String, Object> seachParam = new HashMap<>();
            seachParam.put("channelId", channelId);
            seachParam.put("cartId", cartId);
            seachParam.put("status", "0");
            List<CmsBtPlatformNumiidModel> listModel = cmsBtPlatformNumiidDao.selectList(seachParam);
            if (ListUtils.isNull(listModel)) {
                $warn("cms_bt_platform_numiid表未找到符合的数据!");
                return;
            }
            listAllNumiid = listModel.stream().map(CmsBtPlatformNumiidModel::getNumIid).collect(Collectors.toList());
            // 表的数据都是自己临时加的，一次处理多少件自己决定，因此暂时不分批处理了，尽量别一次处理太多，不然sql可能撑不住
            query = query + "," + "numIId:{$in:[\"" + listModel.stream().map(CmsBtPlatformNumiidModel::getNumIid).collect(Collectors.joining("\",\"")) + "\"]}";
        } else {
            if (!StringUtils.isEmpty(numIId)) {
                query = query + "," + "numIId:\"" + numIId + "\"";
            } else {
                query = query + ",numIId:{$nin:[\"\",null]}";
            }
            if (!StringUtils.isEmpty(code)) {
                query = query + "," + "productCodes:\"" + code + "\"";
            }
        }
        queryObject.setQuery("{" + query + "}");
        List<CmsBtProductGroupModel> cmsBtProductGroupModels = productGroupService.getList(channelId, queryObject);
        ShopBean shopBean = Shops.getShop(channelId, cartId);

        for (int i = 0; i < cmsBtProductGroupModels.size(); i++) {
            CmsBtProductGroupModel item = cmsBtProductGroupModels.get(i);
            if ("2".equals(runType)) {
                listAllNumiid.remove(item.getNumIId());
            }
            try {
                $info(String.format("%s-%s-%s天猫货品id回写 %d/%d", channelId, cartId, item.getNumIId(), i + 1, cmsBtProductGroupModels.size()));
                doSaveScItem(shopBean, item, channelId, Integer.valueOf(cartId));
                listSuccessNumiid.add(item.getNumIId());
                $info(String.format("channelId:%s, cartId:%s, numIId:%s 货品id回写成功!", channelId, cartId, item.getNumIId()));
            } catch (Exception e) {
                listErrorNumiid.add(item.getNumIId());
                if (e instanceof BusinessException) {
                    $error(String.format("channelId:%s, cartId:%s, numIId:%s 货品id回写失败!" + e.getMessage(), channelId, cartId, item.getNumIId()));
                } else {
                    $error(String.format("channelId:%s, cartId:%s, numIId:%s 货品id回写失败!", channelId, cartId, item.getNumIId()));
                    e.printStackTrace();
                }
            }

            if ((i + 1) % 300 == 0) {
                // 怕中途断掉,300一更新
                if ("2".equals(runType)) {
                    updateCmsBtPlatformNumiid(channelId, cartId, listSuccessNumiid, listErrorNumiid);
                }
                $info(String.format("天猫属性取得,成功%d个,失败%d个!", listSuccessNumiid.size(), listErrorNumiid.size()));
                listSuccessNumiid.clear();
                listErrorNumiid.clear();
            }
        }

        $info(String.format("天猫属性取得,成功%d个,失败%d个!", listSuccessNumiid.size(), listErrorNumiid.size()));
        if ("2".equals(runType)) {
            updateCmsBtPlatformNumiid(channelId, cartId, listSuccessNumiid, listErrorNumiid);
            if (ListUtils.notNull(listAllNumiid)) {
                // 存在没有搜到的numIId
                cmsBtPlatformNumiidDaoExt.updateStatusByNumiids(channelId, Integer.valueOf(cartId), "3", getTaskName(), listAllNumiid);
            }
        }
    }

    private void updateCmsBtPlatformNumiid(String channelId, String cartId, List<String> listSuccessNumiid, List<String> listErrorNumiid) {
        if (listSuccessNumiid.size() > 0) {
            cmsBtPlatformNumiidDaoExt.updateStatusByNumiids(channelId, Integer.valueOf(cartId), "1", getTaskName(), listSuccessNumiid);
        }
        if (listErrorNumiid.size() > 0) {
            cmsBtPlatformNumiidDaoExt.updateStatusByNumiids(channelId, Integer.valueOf(cartId), "2", getTaskName(), listErrorNumiid);
        }
    }

    private void doSaveScItem(ShopBean shopBean, CmsBtProductGroupModel cmsBtProductGroup, String channelId, int cartId) throws Exception {
        String numIId = cmsBtProductGroup.getNumIId();
        Map<String, Object> fieldMap = getPlatformWareInfoItem(numIId, shopBean);
        Object obj = fieldMap.get("sku");
        if (obj == null) obj = fieldMap.get("darwin_sku");

        boolean hasErr = false;
        for (String code : cmsBtProductGroup.getProductCodes()) {
            boolean isPublish = false; // 这个code是否上新过
            CmsBtProductModel product = cmsBtProductDao.selectByCode(code, channelId);
            String orgChannelId = product.getOrgChannelId();
            if (StringUtils.isEmpty(taobaoScItemService.doGetStoreCode(channelId, cartId, orgChannelId))) {
                $warn(String.format("channelId:%s, cartId:%s, numIId:%s, code:%s 设定不需要绑定关联货品!", channelId, cartId, numIId, code));
                continue;
            }
            // 全小写比较skuCode
            Map<String, BaseMongoMap<String, Object>> mapSkus = product.getPlatform(cartId).getSkus().stream()
                    .collect(Collectors.toMap(
                            sku -> sku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.skuCode.name()).toLowerCase(),
                            sku -> sku)
                    );

            if (obj != null) {
                // sku级
                List<Map<String, Object>> listVal = (List) obj;
                for (Map<String, Object> skuVal : listVal) {
                    if (skuVal.get("sku_outerId") == null || "".equals(skuVal.get("sku_outerId").toString())) {
                        hasErr = true;
                    } else {
                        BaseMongoMap<String, Object> skuInfo = mapSkus.get(skuVal.get("sku_outerId").toString().toLowerCase());
                        if (skuInfo != null) {
                            isPublish = true;
                            String sku = skuInfo.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.skuCode.name());
                            String itemId = skuVal.get("sku_scProductId") == null ? null : skuVal.get("sku_scProductId").toString();
                            if (StringUtils.isEmpty(itemId)) {
                                $warn(String.format("channelId:%s, cartId:%s, numIId:%s, code:%s, sku:%s 天猫上没有绑定关联货品!", channelId, cartId, numIId, code, sku));
                                continue;
                            }
                            saveCmsBtTmScItem(shopBean, channelId, orgChannelId, cartId, code, sku, Long.parseLong(itemId), getTaskName());
                        }
                    }
                }
            } else {
                // product级
                if (fieldMap.get("outer_id") == null || "".equals(fieldMap.get("outer_id").toString())) {
                    hasErr = true;
                } else {
                    BaseMongoMap<String, Object> skuInfo = mapSkus.get(StringUtils.null2Space((String) fieldMap.get("outer_id")).toLowerCase());
                    if (skuInfo != null) {
                        isPublish = true;
                        String sku = skuInfo.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.skuCode.name());
                        String itemId = fieldMap.get("item_sc_product_id") == null ? null : fieldMap.get("item_sc_product_id").toString();
                        if (StringUtils.isEmpty(itemId)) {
                            $warn(String.format("channelId:%s, cartId:%s, numIId:%s, code:%s, sku:%s 天猫上没有绑定关联货品!", channelId, cartId, numIId, code, sku));
                            continue;
                        }
                        saveCmsBtTmScItem(shopBean, channelId, orgChannelId, cartId, code, sku, Long.parseLong(itemId), getTaskName());
                    } else {
                        $warn(String.format("channelId:%s, cartId:%s, numIId:%s, code:%s, outer_id:%s 此numIId下的outer_id在cms的这个code里不存在对应的sku!", channelId, cartId, numIId, code, fieldMap.get("outer_id")));
                    }
                }
            }

            if (isPublish) {
                sxProductService.updateImsBtProduct(channelId, orgChannelId, cartId, new ArrayList<String>(){{add(code);}}, numIId, obj != null, getTaskName());
            }
        }

        if (hasErr) {
            $warn(String.format("channelId:%s, cartId:%s, numIId:%s 存在outer_id为空的sku!", channelId, cartId, numIId));
        }
    }

    public Map<String, Object> getPlatformWareInfoItem(String numIid, ShopBean shopBean) throws Exception {
        fieldHashMap fieldMap = new fieldHashMap();
        String schema = tbProductService.doGetWareInfoItem(numIid, shopBean).getUpdateItemResult();
        $debug("取得天猫商品信息schema:" + schema);
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

    protected void saveCmsBtTmScItem(ShopBean shopBean, String channelId, String orgChannelId, int cartId, String code, String sku, Long itemId, String modifier) {
        Map<String, Object> searchParam = new HashMap<>();
        searchParam.put("channelId", channelId);
        searchParam.put("cartId", cartId);
        searchParam.put("sku", sku);
        CmsBtTmScItemModel scItemModel = cmsBtTmScItemDao.selectOne(searchParam);
        String scCode;
        try {
            StringBuffer failCause = new StringBuffer("");
            ScItem scItem = tbScItemService.getScitemByItemId(shopBean, itemId, failCause);
            if (scItem == null) {
                $warn(String.format("channelId:%s, cartId:%s, code:%s, sku:%s, itemId:%s 获取货品信息API,返回错误!" + failCause.toString(), channelId, cartId, code, sku, itemId));
                return;
            }
            scCode = scItem.getOuterCode(); // 商家编码
        } catch (ApiException e) {
            $warn(String.format("channelId:%s, cartId:%s, code:%s, sku:%s, itemId:%s 获取货品信息API调用失败!", channelId, cartId, code, sku, itemId));
            return;
        }

        if (scItemModel == null) {
            // add
            scItemModel = new CmsBtTmScItemModel();
            scItemModel.setChannelId(channelId);
            scItemModel.setOrgChannelId(orgChannelId);
            scItemModel.setCartId(cartId);
            scItemModel.setCode(code);
            scItemModel.setSku(sku);
            scItemModel.setScProductId(itemId.toString());
            scItemModel.setScCode(scCode);
            scItemModel.setCreater(modifier);
            cmsBtTmScItemDao.insert(scItemModel);
        } else {
            // update
            if (!itemId.toString().equals(scItemModel.getScProductId())
                    || !scCode.equals(scItemModel.getScCode())) {
                scItemModel.setScProductId(itemId.toString());
                scItemModel.setScCode(scCode);
                scItemModel.setModifier(modifier);
                scItemModel.setModified(DateTimeUtil.getDate());
                cmsBtTmScItemDao.update(scItemModel);
            }
        }
    }

}
