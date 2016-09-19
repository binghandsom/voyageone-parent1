package com.voyageone.components.cn.service;

import com.voyageone.common.masterdate.schema.enums.FieldTypeEnum;
import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.common.masterdate.schema.field.InputField;
import com.voyageone.common.masterdate.schema.field.SingleCheckField;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.masterdate.schema.utils.XmlUtils;
import com.voyageone.components.ComponentBase;
import com.voyageone.components.cn.enums.CnConstants;
import com.voyageone.components.cn.enums.CnUpdateType;
import org.dom4j.Element;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 独立域名转换xml用
 *
 * @author morse on 2016/8/23
 * @version 2.5.0
 */
@Service
public class CnSchemaService extends ComponentBase {
    private static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

    /**
     * 全量库存更新
     */
    public String writeInventoryFullXmlString(List<List<Field>> multiFields) {
        return writeXmlString(multiFields, CnUpdateType.INVENTORY_FULL, CnConstants.ROOT_SIMPLE, CnConstants.MULTIPROP_PRODUCT);
    }

    /**
     * 用到的库存更新
     */
    public String writeInventoryXmlString(List<List<Field>> multiFields) {
        return writeXmlString(multiFields, CnUpdateType.INCREMENT, CnConstants.ROOT_SIMPLE, CnConstants.MULTIPROP_PRODUCT);
    }

    /**
     * 产品的新增或更新
     */
    public String writeProductXmlString(List<List<Field>> multiFields) {
        return writeXmlString(multiFields, CnUpdateType.PRODUCT, CnConstants.ROOT_CONFIGURABLE, CnConstants.MULTIPROP_PRODUCT);
    }

    /**
     * 商品的新增或更新
     */
    public String writeSkuXmlString(List<List<Field>> multiFields) {
        return writeXmlString(multiFields, CnUpdateType.PRODUCT, CnConstants.ROOT_SIMPLE, CnConstants.MULTIPROP_PRODUCT);
    }

    /**
     * 设置类目里的商品的排序
     */
    public String writeCategoryProductXmlString(List<List<Field>> multiFields) {
        return writeXmlString(multiFields, CnUpdateType.CATEGORY_PRODUCT, CnConstants.ROOT_CATEGORIES, CnConstants.MULTIPROP_CATEGORY);
    }

    /**
     * category的创建或更新
     */
    public String writeCategoryXmlString(List<List<Field>> multiFields) {
        return writeXmlString(multiFields, CnUpdateType.CATEGORY, CnConstants.ROOT_CATEGORIES, CnConstants.MULTIPROP_CATEGORY);
    }

    /**
     * 产品xml读取
     */
    public List<List<Field>> readProductXmlString(String xml) {
        return readXmlToList(xml, CnConstants.ROOT_CONFIGURABLE, CnConstants.MULTIPROP_PRODUCT);
    }

    /**
     * 商品xml读取
     */
    public List<List<Field>> readSkuXmlString(String xml) {
        return readXmlToList(xml, CnConstants.ROOT_SIMPLE, CnConstants.MULTIPROP_PRODUCT);
    }

    /**
     * xml生成
     */
    private String writeXmlString(List<List<Field>> multiFields, CnUpdateType updateType, String rootName, String multiPropName) {
        StringBuffer sb = new StringBuffer("");
        sb.append(XML_HEADER);

        Element type = XmlUtils.createRootElement("root"); // <root updateType="%d">
        type.addAttribute("updateType", String.valueOf(updateType.val()));

        Element root = XmlUtils.appendElement(type, rootName); // 例：<Categories>，<Configurable>，<Simple>
        for (List<Field> listField : multiFields) {
            Element multiProp = XmlUtils.appendElement(root, multiPropName); // 例：<Category>，<Product>

            for (Field field : listField) {
                Element valueNode = XmlUtils.appendElement(multiProp, field.getId());

                String value = null;
                FieldTypeEnum fieldType = field.getType();
                if (fieldType == FieldTypeEnum.INPUT) {
                    value = ((InputField) field).getValue();
                } else if (fieldType == FieldTypeEnum.SINGLECHECK) {
                    value = ((SingleCheckField) field).getValue().getValue();
                } else {
                    logger.warn("独立域名不支持input,singleCheck以外的类型!");
                }
                if(!StringUtil.isEmpty(value)) {
                    valueNode.setText(value);
                }
            }
        }
        sb.append(XmlUtils.nodeToString(type));

        return sb.toString();
    }

    /**
     * 注意：生成的Field都是InputField，仅仅为了读取值，或者拼接Fields用
     */
    private List<List<Field>> readXmlToList(String xml, String rootName, String multiPropName) {
        StringBuffer sb = new StringBuffer(xml);
        if (xml.startsWith(XML_HEADER)) {
            sb.replace(0, XML_HEADER.length(), "");
        }

        Element type = XmlUtils.getRootElementFromString(sb.toString()); // <root updateType="%d">
        Element root = XmlUtils.getChildElement(type, rootName); // 例：<Categories>，<Configurable>，<Simple>
        List<Element> allElements = XmlUtils.getChildElements(root, multiPropName); // 例：<Category>，<Product>

        List<List<Field>> multiFields = new ArrayList<>();
        for (Element allElement : allElements) {
            List<Field> listField = new ArrayList<>();
            multiFields.add(listField);

            List<Element> eachElements = XmlUtils.getElements(allElement, null);
            for (Element eachElement : eachElements) {
                InputField field = (InputField) FieldTypeEnum.createField(FieldTypeEnum.INPUT);
                listField.add(field);
                field.setId(eachElement.getName());
                field.setValue(XmlUtils.getElementValue(eachElement));
            }
        }

        return multiFields;
    }

}
