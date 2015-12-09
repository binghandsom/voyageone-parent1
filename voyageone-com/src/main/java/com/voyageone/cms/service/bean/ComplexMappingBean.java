package com.voyageone.cms.service.bean;

import java.util.List;

/**
 * Created by Leo on 15-12-9.
 */
public class ComplexMappingBean extends MappingBean {
    private List<MappingBean> subMappings;
    private String masterPropId;

    public ComplexMappingBean() {
        mappingType = MAPPING_COMPLEX;
    }

    public List<MappingBean> getSubMappings() {
        return subMappings;
    }

    public void setSubMappings(List<MappingBean> subMappings) {
        this.subMappings = subMappings;
    }

    public String getMasterPropId() {
        return masterPropId;
    }

    public void setMasterPropId(String masterPropId) {
        this.masterPropId = masterPropId;
    }
}
