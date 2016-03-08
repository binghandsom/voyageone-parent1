package com.voyageone.service.bean.cms;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leo on 15-12-30.
 */
public class MultiComplexCustomMappingValue {
    private List<com.voyageone.service.bean.cms.MappingBean> subMappings;

    public MultiComplexCustomMappingValue() {
        subMappings = new ArrayList<>();
    }

    public List<com.voyageone.service.bean.cms.MappingBean> getSubMappings() {
        return subMappings;
    }

    public void setSubMappings(List<com.voyageone.service.bean.cms.MappingBean> subMappings) {
        this.subMappings = subMappings;
    }

    public void addSubMapping(com.voyageone.service.bean.cms.MappingBean mappingBean) {
        this.subMappings.add(mappingBean);
    }
}
