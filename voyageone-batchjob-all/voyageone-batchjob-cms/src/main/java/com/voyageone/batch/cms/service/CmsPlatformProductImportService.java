package com.voyageone.batch.cms.service;

import com.mongodb.BulkWriteResult;
import com.taobao.api.ApiException;
import com.taobao.api.response.TmallItemUpdateSchemaGetResponse;
import com.taobao.top.schema.factory.SchemaReader;
import com.taobao.top.schema.field.*;
import com.taobao.top.schema.value.ComplexValue;
import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.cms.service.bean.ComplexMappingBean;
import com.voyageone.cms.service.bean.MappingBean;
import com.voyageone.cms.service.bean.SimpleMappingBean;
import com.voyageone.cms.service.dao.mongodb.CmsBtProductDao;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.components.tmall.TbProductService;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.StringUtils;
import com.voyageone.ims.rule_expression.MasterWord;
import com.voyageone.ims.rule_expression.RuleExpression;
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
    private CmsBtProductDao cmsBtProductDao;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "CmsPlatformProductJob";
    }

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {


    }

    public Map<String, Object> getPlatformProduct(Long productId, ShopBean shopBean) throws Exception {
        String schema = tbProductService.getProductSchema(productId, shopBean);

        List<Field> fields = SchemaReader.readXmlForList(schema);

        fieldHashMap fieldMap = new fieldHashMap();
        fields.forEach(field -> {
            fields2Map(field, fieldMap);
        });
        return fieldMap;

    }

    public Map<String, Object> getPlatformWareInfoItem(String numIid, ShopBean shopBean) throws Exception {
        String schema = tbProductService.doGetWareInfoItem(numIid, shopBean).getUpdateItemResult();
        List<Field> fields = SchemaReader.readXmlForList(schema);

        fieldHashMap fieldMap = new fieldHashMap();
        fields.forEach(field -> {
            fields2Map(field, fieldMap);
        });

        update2ProductFields("010", "SGM8057AMPE.-7", fieldMap);
        return fieldMap;

    }

    public void update2ProductFields(String channelId, String code, Map<String, Object> fields) {
        List<BulkUpdateModel> bulkList = new ArrayList<>();
        HashMap<String, Object> updateMap = new HashMap<>();
        for(String key:fields.keySet()){
            updateMap.put("fields."+key, fields.get(key));
        }

        HashMap<String, Object> queryMap = new HashMap<>();
        queryMap.put("fields.code", code);
        BulkUpdateModel model = new BulkUpdateModel();
        model.setUpdateMap(updateMap);
        model.setQueryMap(queryMap);
        bulkList.add(model);
        BulkWriteResult result = cmsBtProductDao.bulkUpdateWithMap(channelId, bulkList, getTaskName(), "$set");
        System.out.println("ok");
    }

    public void fields2Map(Field field,fieldHashMap fieldMap) {

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
                List<Map<String,Object>> values = new ArrayList<>();
                if (complexField.getDefaultComplexValue() != null) {
                    for(String fieldId : complexField.getDefaultComplexValue().getFieldKeySet()){
                        values.add(getFieldValue(complexField.getDefaultComplexValue().getValueField(fieldId)));
                    }
                    fieldMap.put(complexField.getId(), values);
                }
                break;
            case MULTICOMPLEX:
                MultiComplexField multiComplexField = (MultiComplexField) field;
                List<Map<String,Object>> multiComplexValues = new ArrayList<>();
                if (multiComplexField.getDefaultComplexValues() != null) {
                    for(ComplexValue item : multiComplexField.getDefaultComplexValues()){
                        for(String fieldId : item.getFieldKeySet()){
                            multiComplexValues.add(getFieldValue(item.getValueField(fieldId)));
                        }
                    }
                }
                fieldMap.put(multiComplexField.getId(), multiComplexValues);
                break;
        }
    }

    public Map<String,Object> getFieldValue(Field field) {
        fieldHashMap map = new fieldHashMap();
        List<String> values;
        switch (field.getType()) {
            case INPUT:
                InputField inputField = (InputField) field;
                map.put(inputField.getId(), inputField.getValue());
                break;
            case MULTIINPUT:
                MultiInputField multiInputField = (MultiInputField) field;
                values = new ArrayList<>();
                multiInputField.getValues().forEach(value -> values.add(value.getValue()));
                map.put(multiInputField.getId(), values);
                break;
            case SINGLECHECK:
                SingleCheckField singleCheckField = (SingleCheckField) field;
                map.put(singleCheckField.getId(), singleCheckField.getValue().getValue());
                break;
            case MULTICHECK:
                MultiCheckField multiCheckField = (MultiCheckField) field;
                values = new ArrayList<>();
                multiCheckField.getValues().forEach(value -> values.add(value.getValue()));
                map.put(multiCheckField.getId(), values);
                break;
            case COMPLEX:
                ComplexField complexField = (ComplexField) field;
                List<Map<String,Object>> valuesMap = new ArrayList<>();
                Map<String, Field> fieldMap = complexField.getFieldMap();
                for (String key : fieldMap.keySet()) {
                    valuesMap.add(getFieldValue(fieldMap.get(key)));
                }
                map.put(complexField.getId(), valuesMap);
                break;
            case MULTICOMPLEX:
                MultiComplexField multiComplexField = (MultiComplexField) field;
                List<Map<String,Object>> multiComplexValues = new ArrayList<>();
                if (multiComplexField.getFieldMap() != null) {
                    for(ComplexValue item : multiComplexField.getComplexValues()){
                        for(String fieldId : item.getFieldKeySet()){
                            multiComplexValues.add(getFieldValue(item.getValueField(fieldId)));
                        }
                    }
                }
                map.put(multiComplexField.getId(), multiComplexValues);
                break;
        }
        return map;
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
