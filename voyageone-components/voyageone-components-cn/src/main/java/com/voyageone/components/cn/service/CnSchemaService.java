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
    private static final String XML_UPDATETYPE = "<root updateType=\"%d\">";

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
     * xml生成
     */
    private String writeXmlString(List<List<Field>> multiFields, CnUpdateType updateType, String rootName, String multiPropName) {
        StringBuffer sb = new StringBuffer("");
        sb.append(XML_HEADER);
        sb.append(String.format(XML_UPDATETYPE, updateType.val())); // <root>

        Element root = XmlUtils.createRootElement(rootName);
        for (List<Field> listField : multiFields) {
            Element multiProp = XmlUtils.appendElement(root, multiPropName);

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
        sb.append(XmlUtils.nodeToString(root));

        sb.append("</root>"); // </root>

        return sb.toString();
    }

}
