package com.voyageone.task2.cms.service.platform.common;

import com.taobao.api.ApiException;
import com.taobao.api.request.TmallItemSchemaUpdateRequest;
import com.taobao.api.response.TmallItemSchemaUpdateResponse;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.masterdate.schema.enums.FieldTypeEnum;
import com.voyageone.common.masterdate.schema.exception.TopSchemaException;
import com.voyageone.common.masterdate.schema.factory.SchemaReader;
import com.voyageone.common.masterdate.schema.factory.SchemaWriter;
import com.voyageone.common.masterdate.schema.field.*;
import com.voyageone.common.masterdate.schema.value.ComplexValue;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.tmall.service.TbProductService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 批量修改天猫某些属性
 * 先拉天猫上当前商品属性schema，然后set想要修改的属性，再update
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class TmallItemSchemaUpdateTest {

    private final TbApi tbApi = new TbApi();

    // Map<需要改的属性id, 改的值>
    // 目前只做了input和singleCheck类型的逻辑
    private Map<String, Object> changeFieldValue = new HashMap<>();

//    public static void main(String[] args) {
    @Test
    public void main() {

        String channelId = "010";
        int cartId = 23;
        ShopBean shopBean = Shops.getShop(channelId, cartId);

        changeFieldValue.put("customsClearanceWay", "true");
        changeFieldValue.put("global_arrive", "true");

//        // 直接写死xml的场合
//        String xmlData = "";
//        TmallItemSchemaUpdateResponse updateItemResponse = tbApi.updateItem(null, numIId, null, xmlData, shopBean);

//        String numIId = "";
//        execute(numIId, shopBean);

    }

    private void execute(String numIId, ShopBean shopBean) {
        String schema;
        try {
            schema = tbApi.doGetWareInfoItem(numIId, shopBean).getUpdateItemResult();
        } catch (ApiException e) {
            System.out.println("numIId[" + numIId + "] 取得商品信息Api调用失败!");
            return;
        }
        System.out.println("numIId["+ numIId +"]取得:" + schema);

        List<Field> fieldList;
        try {
            fieldList = SchemaReader.readXmlForList(schema);
        } catch (TopSchemaException e) {
            System.out.println("numIId[" + numIId + "] schema转换失败!");
            return;
        }

        for (Field field : fieldList) {
            getField(field);
        }

        try {
            String xmlUpdate = SchemaWriter.writeParamXmlString(fieldList);
            System.out.println("numIId[" + numIId + "]更新:" + xmlUpdate);

            doTmallItemSchemaUpdate(numIId, xmlUpdate, shopBean);
        } catch (TopSchemaException e) {
            System.out.println("numIId[" + numIId + "] schema转换失败!");
        } catch (ApiException e) {
            System.out.println("numIId[" + numIId + "] 更新Api调用失败!");
        }
    }

    private void getField(Field field) {
//        Field result = null;
//        if (changeFieldValue.containsKey(field.getId())) {
////            InputField inputField = (InputField)field;
////            inputField.setValue("其他");
//        } else {
            if (field.getType() == FieldTypeEnum.INPUT) {
                InputField inputField = (InputField)field;
                inputField.setValue(inputField.getDefaultValue());
                if (changeFieldValue.containsKey(field.getId())) {
                    inputField.setValue((String) changeFieldValue.get(field.getId()));
                }
            } else if (field.getType() == FieldTypeEnum.SINGLECHECK) {
                SingleCheckField singleCheckField = (SingleCheckField)field;
                singleCheckField.setValue(singleCheckField.getDefaultValue());
                if (changeFieldValue.containsKey(field.getId())) {
                    singleCheckField.setValue((String) changeFieldValue.get(field.getId()));
                }
            } else if (field.getType() == FieldTypeEnum.MULTICHECK) {
                MultiCheckField multiCheckField = (MultiCheckField)field;

                if (multiCheckField.getDefaultValues() != null) {
                    for (String value : multiCheckField.getDefaultValues()) {
                        multiCheckField.addValue(value);
                    }
                }

            } else if (field.getType() == FieldTypeEnum.COMPLEX) {
                ComplexField complexField = (ComplexField)field;
                ComplexValue complexValue = new ComplexValue();
                if (complexField.getDefaultComplexValue() != null) {
                    for (String fieldId : complexField.getDefaultComplexValue().getFieldKeySet()) {
                        complexValue.put(complexField.getDefaultComplexValue().getValueField(fieldId));
                    }
                    complexField.setComplexValue(complexValue);
                }

            } else if (field.getType() == FieldTypeEnum.MULTICOMPLEX) {
                MultiComplexField multiComplexField = (MultiComplexField)field;

                if (multiComplexField.getDefaultComplexValues() != null) {
                    for (ComplexValue complexValue : multiComplexField.getDefaultComplexValues()) {
                        multiComplexField.addComplexValue(complexValue);
                    }
                }

            } else {
                System.out.println("没有对应: 其他");
            }

//        }

    }

    /**
     * 更新商品 - item
     */
    private void doTmallItemSchemaUpdate(String numIId, String xmlData, ShopBean shopBean) throws ApiException {
        TmallItemSchemaUpdateResponse updateItemResponse = tbApi.updateItem(null, numIId, null, xmlData, shopBean);
        if (updateItemResponse == null) {
            System.out.println("numIId[" + numIId + "]异常:Tmall return null response when update item");
        } else if (updateItemResponse.getErrorCode() != null) {
            System.out.println("numIId[" + numIId + "]异常:errorCode[" + updateItemResponse.getErrorCode() + "]" + updateItemResponse.getSubMsg());
        } else {
            System.out.println("numIId[" + numIId + "]更新成功!");
        }
    }

    private class TbApi extends TbProductService {
        public TmallItemSchemaUpdateResponse updateItem(String productId, String numId, Long categoryId, String xmlData, ShopBean config) throws ApiException {
            TmallItemSchemaUpdateRequest request = new TmallItemSchemaUpdateRequest();
            if (categoryId != null) {
                request.setCategoryId(categoryId);
            }
            if (!StringUtils.isEmpty(productId)) {
                request.setProductId(Long.parseLong(productId));
            }
            request.setItemId(Long.parseLong(numId));
            request.setXmlData(xmlData);

            return reqTaobaoApi(config, request);
        }
    }

}
