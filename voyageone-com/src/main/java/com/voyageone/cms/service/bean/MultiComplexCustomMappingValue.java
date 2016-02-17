package com.voyageone.cms.service.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leo on 15-12-30.
 */
public class MultiComplexCustomMappingValue {
    private List<MappingBean> subMappings;

    public MultiComplexCustomMappingValue() {
        subMappings = new ArrayList<>();
    }

    public List<MappingBean> getSubMappings() {
        return subMappings;
    }

    public void setSubMappings(List<MappingBean> subMappings) {
        this.subMappings = subMappings;
    }

    public void addSubMapping(MappingBean mappingBean) {
        this.subMappings.add(mappingBean);
    }
}
