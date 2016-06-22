package com.voyageone.service.model.cms.mongo;

import com.voyageone.common.masterdate.schema.field.Field;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 想追加显示在画面上的属性的表
 * 保存例：
 * 1: null        想要追加在根属性下
 * 2：一级属性1   想要追加在一级属性1下
 * 3：一级属性2>二级属性2   想要追加在一级属性2>二级属性2下
 *
 * @author morse.lu 2016/06/21
 * @version 2.1.0
 * @since 2.1.0
 */
@Document
public class CmsMtPlatformCategoryExtendFieldModel_Field {
    public static final String SEPARATOR = ">";

    private String parentFieldId;
    private String parentFieldName;
    private Field field;

    public String getParentFieldId() {
        return parentFieldId;
    }

    public void setParentFieldId(String parentFieldId) {
        this.parentFieldId = parentFieldId;
    }

    public String getParentFieldName() {
        return parentFieldName;
    }

    public void setParentFieldName(String parentFieldName) {
        this.parentFieldName = parentFieldName;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }
}
