package com.voyageone.batch.ims.job;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taobao.top.schema.exception.TopSchemaException;
import com.taobao.top.schema.factory.SchemaReader;
import com.taobao.top.schema.factory.SchemaWriter;
import com.taobao.top.schema.field.*;
import com.taobao.top.schema.value.ComplexValue;
import com.voyageone.common.util.XmlUtil;
import com.voyageone.ims.rule_expression.RuleExpression;
import com.voyageone.ims.rule_expression.RuleWord;
import com.voyageone.ims.rule_expression.TextWord;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Element;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leo on 15-6-4.
 */
@Component("Test")
public class Test {
    private Log logger = LogFactory.getLog(Test.class);

    public void main()
    {
        logger.info("start.");
        try {
            Thread.sleep(10*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args)
    {
        String f = "http://s7d5.scene7.com/is/image/sneakerhead/Jewelry%%5F20150819%%5Fx380%%5F251x?$380%%2D251$&$text05=%s&$text04=%s&$text03=%s&$text02=%s&$text01=%s";
        //String s = String.format(f, "a", "b", "c", "d", "e");
        String s = null;
        try {
            s = URLEncoder.encode("http://s7d5.scene7.com/is/image/sneakerhead/Jewelry%5F20150819%5Fx380%5F251x?$380%2D251$&$text05=Pendants&$text04=&$text03=Swarovski&$text02=Yellow / White / white / white&$text01=Gold / Swarovski", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println(s);
    }

    public static class TextValue
    {
        private TextWord textWord;
        private RuleExpression ruleExpression;
        private ObjectMapper om;

        public TextValue(String value)
        {
            ruleExpression = new RuleExpression();
            textWord = new TextWord(value);
            /*
            ruleExpression.addValueWord(textWord);
            */
            om = new ObjectMapper();
        }

        @Override
        public String toString()
        {
            try {
                return om.writeValueAsString(ruleExpression);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public static class DictValue
    {
        private RuleWord ruleWord;
        private RuleExpression ruleExpression;
        private ObjectMapper om;

        public DictValue(String value)
        {
            ruleExpression = new RuleExpression();
            /*
            ruleWord = new DictWord(value);
            ruleExpression.addValueWord(ruleWord);
            */
            om = new ObjectMapper();
        }

        @Override
        public String toString()
        {
            try {
                return om.writeValueAsString(ruleExpression);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public static void generateItemValue()
    {
        String channel_id = "006";
        String cart_id = "23";
        String model = "P909915";

        ApplicationContext ctx = new GenericXmlApplicationContext("applicationContext.xml");
        //PropValueDao propValueDao = ctx.getBean(PropValueDao.class);
        String path =  "/home/leo/Documents/xml/item_rule/";
        String xml = XmlUtil.readXml("50013228.xml", path);
        List<Field> fieldList  = null;
        try {
            fieldList = SchemaReader.readXmlForList(xml);
        } catch (TopSchemaException e) {
            e.printStackTrace();
        }

        for (Field field : fieldList) {
            if ("title".equalsIgnoreCase(field.getId())) {
                ((InputField) field).setValue(new TextValue("测试中，请勿购买").toString());
            } else if ("sell_points".equalsIgnoreCase(field.getId())) {
                ComplexField complexField = (ComplexField) field;
                ComplexValue complexValue = new ComplexValue();
                complexValue.setInputFieldValue("sell_point_0", new TextValue("便宜").toString());
                complexValue.setInputFieldValue("sell_point_1", new TextValue("结实").toString());

                complexField.setComplexValue(complexValue);
            } else if ("item_status".equalsIgnoreCase(field.getId())) {
                ((SingleCheckField) field).setValue(new TextValue("2").toString());
            } else if ("item_type".equalsIgnoreCase(field.getId())) {
                ((SingleCheckField) field).setValue(new TextValue("b").toString());
            } else if ("stuff_status".equalsIgnoreCase(field.getId())) {
                ((SingleCheckField) field).setValue(new TextValue("5").toString());
            } else if ("sub_stock".equalsIgnoreCase(field.getId())) {
                ((SingleCheckField) field).setValue(new TextValue("false").toString());
            } else if ("auction_point".equalsIgnoreCase(field.getId())) {
                ((InputField) field).setValue(new TextValue("0.5").toString());
            } else if ("prop_129498239".equalsIgnoreCase(field.getId())) {
                ((SingleCheckField) field).setValue(new TextValue("21959").toString());
            } else if ("channel_type".equalsIgnoreCase(field.getId())) {
                ((SingleCheckField) field).setValue(new TextValue("1").toString());
            } else if ("std_size_group".equalsIgnoreCase(field.getId())) {
                ((SingleCheckField) field).setValue(new TextValue("-1:自定义:-1").toString());
            } else if ("lang".equalsIgnoreCase(field.getId())) {
                ((SingleCheckField) field).setValue(new TextValue("zh_CN").toString());
            } else if ("quantity".equalsIgnoreCase(field.getId())) { //TODO
                ((InputField) field).setValue(new TextValue("1").toString());
            } else if ("price".equalsIgnoreCase(field.getId())) //TODO
            {
                ((InputField) field).setValue(new TextValue("20000").toString());
            } else if ("delivery_way".equalsIgnoreCase(field.getId())) //TODO
            {
                ((MultiCheckField) field).addValue(new TextValue("2").toString());
            } else if ("location".equalsIgnoreCase(field.getId())) //TODO
            {
                ComplexField complexField = (ComplexField) field;
                ComplexValue complexValue = new ComplexValue();
                complexValue.setInputFieldValue("prov", new TextValue("上海").toString());
                complexValue.setInputFieldValue("city", new TextValue("上海").toString());

                complexField.setComplexValue(complexValue);
            } else if ("freight_payer".equalsIgnoreCase(field.getId())) {
                ((SingleCheckField) field).setValue(new TextValue("2").toString());
            } else if ("freight_by_buyer".equalsIgnoreCase(field.getId())) {
                ((SingleCheckField) field).setValue(new TextValue("freight_details").toString());
            } else if ("freight".equalsIgnoreCase(field.getId())) {
                ComplexField complexField = (ComplexField) field;
                ComplexValue complexValue = new ComplexValue();
                complexValue.setInputFieldValue("express_fee", new TextValue("40").toString());
                complexValue.setInputFieldValue("post_fee", new TextValue("30").toString());
                complexValue.setInputFieldValue("ems_fee", new TextValue("20").toString());

                complexField.setComplexValue(complexValue);
            } else if ("seller_cids".equalsIgnoreCase(field.getId())) {
                ((MultiCheckField) field).addValue(new TextValue("1082170191").toString());
            } else if ("has_warranty".equalsIgnoreCase(field.getId())) {
                ((SingleCheckField) field).setValue(new TextValue("false").toString());
            } else if ("has_invoice".equalsIgnoreCase(field.getId())) {
                ((SingleCheckField) field).setValue(new TextValue("false").toString());
            } else if ("description".equalsIgnoreCase(field.getId())) {
                ((InputField) field).setValue(new TextValue("description").toString());
            } else if ("item_images".equalsIgnoreCase(field.getId())) {
                ComplexField complexField = (ComplexField) field;
                ComplexValue complexValue = new ComplexValue();

                complexValue.setInputFieldValue("item_image_0", new TextValue("7g5B5l5mtuE4hhnWKeyBxtrNWGAwHC3K-25").toString());
                //complexValue.setInputFieldValue("item_image_1", "");
                complexField.setComplexValue(complexValue);
            } else if ("valid_thru".equalsIgnoreCase(field.getId())) {
                ((SingleCheckField) field).setValue(new TextValue("7").toString());
            } else if ("service_version".equalsIgnoreCase(field.getId())) {
                ((InputField) field).setValue(new TextValue("11100").toString());
            } else if ("size_measure_image".equalsIgnoreCase(field.getId())) {
                ((SingleCheckField) field).setValue(new TextValue("https://img.alicdn.com/bao/uploaded/i2/TB1XkpJHpXXXXaNXVXXXXXXXXXX_!!0-item_pic.jpg").toString());
            } else if ("item_size_weight".equalsIgnoreCase(field.getId())) {
                ComplexField complexField = (ComplexField) field;
                ComplexValue complexValue = new ComplexValue();
                complexValue.setInputFieldValue("item_size", new TextValue("5").toString());
                complexValue.setInputFieldValue("item_weight", new TextValue("3").toString());
                complexField.setComplexValue(complexValue);
            } else if ("size_model_try".equalsIgnoreCase(field.getId()))
            {
                MultiComplexField multiComplexField = (MultiComplexField) field;
                ComplexValue complexValue = new ComplexValue();
                complexValue.setInputFieldValue("sizeModelTry_modelName", new TextValue("liuyao").toString());
                complexValue.setInputFieldValue("sizeModelTry_shengao", new TextValue("178").toString());
                complexValue.setInputFieldValue("sizeModelTry_tizhong", new TextValue("178").toString());
                complexValue.setInputFieldValue("sizeModelTry_xiongwei", new TextValue("30").toString());
                complexValue.setInputFieldValue("sizeModelTry_yaowei", new TextValue("10").toString());
                complexValue.setInputFieldValue("sizeModelTry_tunwei", new TextValue("20").toString());
                complexValue.setInputFieldValue("sizeModelTry_size", new TextValue("3").toString());
                complexValue.setInputFieldValue("sizeModelTry_result", new TextValue("very good").toString());

                List<ComplexValue> complexValues = new ArrayList<>();
                complexValues.add(complexValue);
                multiComplexField.setComplexValues(complexValues);
            }
            /*
            else if ("size_mapping_template_id".equalsIgnoreCase(field.getId()))
            {
                ((InputField)field).setValue("1");
            }
            */


            Element fieldEle = null;
            try {
                fieldEle = field.toParamElement();
            } catch (TopSchemaException e) {
                e.printStackTrace();
            }
            String fieldValue = fieldEle.asXML();

            //propValueDao.setItemValueByModel(channel_id, cart_id, model, field.getId(), fieldValue);
        }
    }

    public static void generateProductValue()
    {
        String channel_id = "006";
        String cart_id = "23";
        String model = "P909915";

        ApplicationContext ctx = new GenericXmlApplicationContext("applicationContext.xml");
        //PropValueDao propValueDao = ctx.getBean(PropValueDao.class);
        String path =  "/home/leo/Documents/xml/product_rule/";
        String xml = XmlUtil.readXml("50013228.xml", path);
        List<Field> fieldList  = null;
        try {
            fieldList = SchemaReader.readXmlForList(xml);
        } catch (TopSchemaException e) {
            e.printStackTrace();
        }

        for (Field field : fieldList)
        {
            //款号
            if ("prop_13021751".equals(field.getId()))
            {
                ((InputField)field).setValue(new TextValue("111111111111").toString());
            }//品牌
            else if ("prop_20000".equals(field.getId()))
            {
                ((SingleCheckField)field).setValue(new TextValue("5003369").toString());
            } //上市
            else if ("prop_122216347".equals(field.getId()))
            {
                ((SingleCheckField)field).setValue(new TextValue("647672577").toString());
            }//diaopaijia
            else if ("prop_6103476".equals(field.getId()))
            {
                ((InputField)field).setValue(new TextValue("100").toString());
            }//xingbie
            else if ("prop_122216608".equals(field.getId()))
            {
                ((SingleCheckField)field).setValue(new TextValue("20532").toString());
            }

            else if ("prop_20663".equals(field.getId()))
            {
                ((SingleCheckField)field).setValue(new TextValue("29541").toString());
            }
            else if ("prop_122216348".equals(field.getId()))
            {
                ((SingleCheckField)field).setValue(new TextValue("29444").toString());
            }
            else if ("prop_122216586".equals(field.getId()))
            {
                ((SingleCheckField)field).setValue(new TextValue("3600467").toString());
            }
            else if ("prop_20021".equals(field.getId()))
            {
                ((SingleCheckField)field).setValue(new TextValue("105255").toString());
            }
            else if ("prop_122216588".equals(field.getId()))
            {
                ((MultiCheckField)field).addValue(new TextValue("94545").toString());
                ((MultiCheckField)field).addValue(new TextValue("10078610").toString());
                ((MultiCheckField)field).addValue(new TextValue("130568").toString());
            }
            else if ("prop_122216688".equals(field.getId()))
            {
                ((SingleCheckField)field).setValue(new TextValue("3283537").toString());
            }
            else if ("prop_20019".equals(field.getId()))
            {
                ((MultiCheckField)field).addValue(new TextValue("28694121").toString());
                ((MultiCheckField)field).addValue(new TextValue("3229351").toString());
            }
            else if ("prop_123272013".equals(field.getId()))
            {
                ((SingleCheckField)field).setValue(new TextValue("7672268").toString());
            }
            /*
            else if ("prop_124128491".equals(field.getId()))
            {
                ((SingleCheckField)field).setValue("3323086");
            }
            else if ("prop_122216523".equals(field.getId()))
            {
                ((SingleCheckField) field).setValue("103409");
            }
            else if ("prop_122216563".equals(field.getId()))
            {
                ((SingleCheckField)field).setValue("20427273");
            }
            else if ("prop_20019".equals(field.getId()))
            {
                ((MultiCheckField)field).addValue("20435382");
                ((MultiCheckField)field).addValue("4194098");
            }
            else if ("prop_123272013".equals(field.getId()))
            {
                ((SingleCheckField)field).setValue("20642");
            }
            else if ("prop_20490".equals(field.getId()))
            {
                ((SingleCheckField)field).setValue("28102");
            }
            else if ("prop_34272".equals(field.getId()))
            {
                ((SingleCheckField)field).setValue("123658");
            }
            else if ("prop_44686322".equals(field.getId()))
            {
                ((SingleCheckField)field).setValue("21958");
            }
            */
            else if ("product_images".equals(field.getId()))
            {
                ComplexField complexField = (ComplexField) field;
                ComplexValue value = new ComplexValue();
                List<Field> complexFields = ((ComplexField) field).getFieldList();

                for (Field f : complexFields)
                {
                    //only main product image will be uploaded
                    if ("product_image_0".equals(f.getId()))
                    {
                        value.setInputFieldValue(f.getId(), new TextValue("7g5B5l5mtuE4hhnWKeyBxtrNWGAwHC3K-25").toString());
                        value.setInputFieldValue(f.getId(), new DictValue("主产品图片-1").toString());
                    }
                    /*
                    else if ("product_image_1".equals(f.getId()))
                    {
                        value.setInputFieldValue(f.getId(), imageUrl + "eol49vHVGpaAPSV7Vbv0peeBiphal3Vq-25");
                    }
                    else if ("product_image_2".equals(f.getId()))
                    {
                        value.setInputFieldValue(f.getId(), "image2");
                    }
                    else if ("product_image_3".equals(f.getId()))
                    {
                        value.setInputFieldValue(f.getId(), "image3");
                    }
                    else if ("product_image_4".equals(f.getId()))
                    {
                        value.setInputFieldValue(f.getId(), "image4");
                    }
                    */
                }
                complexField.setComplexValue(value);
            }
            else if ("market_price".equals(field.getId()))
            {
                ((InputField)field).setValue(new TextValue("10000").toString());
            }

            Element fieldEle = null;
            try {
                fieldEle = field.toParamElement();
            } catch (TopSchemaException e) {
                e.printStackTrace();
            }
            String fieldValue = fieldEle.asXML();

            //propValueDao.setProductValueByModel(channel_id, cart_id, model, field.getId(), fieldValue);
        }

        try {
            String outXml = SchemaWriter.writeParamXmlString(fieldList);
            System.out.println(outXml);
        } catch (TopSchemaException e) {
            e.printStackTrace();
        }
    }

}
