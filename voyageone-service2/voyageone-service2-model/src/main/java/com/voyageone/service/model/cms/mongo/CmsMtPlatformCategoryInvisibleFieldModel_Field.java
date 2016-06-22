package com.voyageone.service.model.cms.mongo;

import com.voyageone.common.masterdate.schema.field.Field;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 不想显示在画面上的属性的表
 * 保存例：
 * 1：一级属性1   表示一级属性1一级下层的所有23级等等的属性都不显示，无需全部添加进表
 * 2：一级属性2>二级属性2   表示一级属性2下面的二级属性2以及二级属性2下面的所有属性不显示，一级属性以及二级属性1等等还是显示
 * 3：一级属性3>二级属性3>三级属性1
 *
 * @author morse.lu 2016/06/21
 * @version 2.1.0
 * @since 2.1.0
 */
@Document
public class CmsMtPlatformCategoryInvisibleFieldModel_Field {
    public static final String SEPARATOR  = ">";

    private String fieldId;
    private String fieldName;

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

}
