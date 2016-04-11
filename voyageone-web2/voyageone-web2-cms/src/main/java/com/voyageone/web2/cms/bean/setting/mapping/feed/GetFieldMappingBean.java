package com.voyageone.web2.cms.bean.setting.mapping.feed;

import com.voyageone.service.model.cms.enums.MappingPropType;

/**
 * 获取具体到字段的 Mapping 设定的请求参数
 *
 * @author Jonas, 12/26/15.
 * @version 2.0.0
 * @since 2.0.0
 */
public class GetFieldMappingBean {

    private String mappingId;

    private String fieldId;

    private MappingPropType fieldType;

    public String getMappingId() {
        return mappingId;
    }

    public void setMappingId(String mappingId) {
        this.mappingId = mappingId;
    }

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    public MappingPropType getFieldType() {
        return fieldType;
    }

    public void setFieldType(MappingPropType fieldType) {
        this.fieldType = fieldType;
    }
}
