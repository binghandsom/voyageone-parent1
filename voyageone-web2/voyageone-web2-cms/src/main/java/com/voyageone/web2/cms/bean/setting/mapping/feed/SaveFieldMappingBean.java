package com.voyageone.web2.cms.bean.setting.mapping.feed;

import com.voyageone.cms.service.model.feed.mapping.Prop;

import java.util.List;

/**
 * 保存某主类目的属性匹配时的请求参数
 *
 * @author Jonas, 12/28/15.
 * @version 2.0.0
 * @since 2.0.0
 */
public class SaveFieldMappingBean {

    private String mappingId;

    private List<String> fieldPath;

    private Prop propMapping;

    public String getMappingId() {
        return mappingId;
    }

    public void setMappingId(String mappingId) {
        this.mappingId = mappingId;
    }

    public List<String> getFieldPath() {
        return fieldPath;
    }

    public void setFieldPath(List<String> fieldPath) {
        this.fieldPath = fieldPath;
    }

    public Prop getPropMapping() {
        return propMapping;
    }

    public void setPropMapping(Prop propMapping) {
        this.propMapping = propMapping;
    }
}
