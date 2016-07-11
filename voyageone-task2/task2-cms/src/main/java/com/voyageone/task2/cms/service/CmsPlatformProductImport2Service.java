package com.voyageone.task2.cms.service;

import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Enums.PlatFormEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.masterdate.schema.factory.SchemaReader;
import com.voyageone.common.masterdate.schema.field.*;
import com.voyageone.common.masterdate.schema.value.ComplexValue;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.tmall.service.TbProductService;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.com.mq.config.MqRoutingKey;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.task2.base.BaseMQCmsService;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.base.util.TaskControlUtils;
import com.voyageone.task2.cms.bean.TmpOldCmsDataBean;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    @Override
    public void onStartup(Map<String, Object> messageMap) throws Exception {

        doMain((String) messageMap.get("channelId"));
    }

    private void doMain(String channelId){
        JomgoQuery queryObject = new JomgoQuery();
        queryObject.setQuery("{cartId:23,numIId:{$ne:\"\"}}");
        Long cnt = productGroupService.countByQuery(queryObject.getQuery(),channelId);
        List<CmsBtProductGroupModel> cmsBtProductGroupModels = productGroupService.getList(channelId, queryObject);
        ShopBean shopBean = Shops.getShop(channelId, 23);
//        shopBean.setApp_url("http://gw.api.taobao.com/router/rest");
//        shopBean.setAppKey("21008948");
//        shopBean.setAppSecret("0a16bd08019790b269322e000e52a19f");
//        shopBean.setSessionKey("6201d2770dbfa1a88af5acfd330fd334fb4ZZa8ff26a40b2641101981");
        final Long[] i = {1L};
        cmsBtProductGroupModels.forEach(item -> {
            try {
                $info(String.format("%s-%s天猫属性取得 %d/%d", channelId, item.getNumIId(), i[0],cnt));
                doSetProduct(shopBean, item);
                i[0]++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void doSetProduct(ShopBean shopBean, CmsBtProductGroupModel cmsBtProductGroup) throws Exception {
        Map<String, Object> fieldMap = new HashMap<>();
        if (PlatFormEnums.PlatForm.TM.getId().equals(shopBean.getPlatform_id())) {
            // 只有天猫系才会更新fields字段
            fieldMap.putAll(getPlatformProduct(cmsBtProductGroup.getPlatformPid(), shopBean));
            fieldMap.putAll(getPlatformWareInfoItem(cmsBtProductGroup.getNumIId(), shopBean));
        }
        upProductPlatform(fieldMap,cmsBtProductGroup);
    }

    private void upProductPlatform(Map<String, Object> fieldMap, CmsBtProductGroupModel cmsBtProductGroup) {
        List<BulkUpdateModel> bulkList = new ArrayList<>();
        cmsBtProductGroup.getProductCodes().forEach(s -> {
            HashMap<String, Object> queryMap = new HashMap<>();
            queryMap.put("common.fields.code", s);
            HashMap<String, Object> updateMap = new HashMap<>();
            updateMap.put("platforms.P23.modified", DateTimeUtil.getNowTimeStamp());
            fieldMap.forEach((s1, o) -> {
                updateMap.put("platforms.P23.fields."+s1, o);
            });
            BulkUpdateModel model = new BulkUpdateModel();
            model.setUpdateMap(updateMap);
            model.setQueryMap(queryMap);
            bulkList.add(model);
        });

        cmsBtProductDao.bulkUpdateWithMap(cmsBtProductGroup.getChannelId(), bulkList, getTaskName(), "$set");
    }

    private Map<String, Object> getPlatformProduct(String productId, ShopBean shopBean) throws Exception {
        fieldHashMap fieldMap = new fieldHashMap();
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
                Map<String, Object> values = new HashMap<>();
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
                        Map<String, Object> obj = new HashMap<>();
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
                Map<String, Object> complexValues = new HashMap<>();
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
}
