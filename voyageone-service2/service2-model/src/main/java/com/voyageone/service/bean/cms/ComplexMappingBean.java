package com.voyageone.service.bean.cms;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leo on 15-12-9.
 */
public class ComplexMappingBean extends com.voyageone.service.bean.cms.MappingBean {
    private List<com.voyageone.service.bean.cms.MappingBean> subMappings;
    private String masterPropId;

    public ComplexMappingBean() {
        mappingType = MAPPING_COMPLEX;
        subMappings = new ArrayList<>();
    }

    public List<com.voyageone.service.bean.cms.MappingBean> getSubMappings() {
        return subMappings;
    }

    public void setSubMappings(List<com.voyageone.service.bean.cms.MappingBean> subMappings) {
        this.subMappings = subMappings;
    }

    public String getMasterPropId() {
        return masterPropId;
    }

    public void setMasterPropId(String masterPropId) {
        this.masterPropId = masterPropId;
    }

    public void addSubMapping(com.voyageone.service.bean.cms.MappingBean subMapping) {
        subMappings.add(subMapping);
    }
}
