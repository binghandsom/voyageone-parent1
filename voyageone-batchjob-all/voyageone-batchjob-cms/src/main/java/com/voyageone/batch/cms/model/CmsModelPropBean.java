package com.voyageone.batch.cms.model;

import com.voyageone.ims.enums.CmsFieldEnum;

import java.util.List;
import java.util.Map;

/**
 * Created by Leo on 15-6-2.
 */
public class CmsModelPropBean implements Cloneable{
    private Map<String, String> propMap;
    private List<CmsCodePropBean> cmsCodePropBeanList;


    public String getProp(CmsFieldEnum.CmsModelEnum propKey)
    {
        return propMap.get(propKey.toString());
    }

    public List<CmsCodePropBean> getCmsCodePropBeanList() {
        return cmsCodePropBeanList;
    }

    public void setCmsCodePropBeanList(List<CmsCodePropBean> cmsCodePropBeanList) {
        this.cmsCodePropBeanList = cmsCodePropBeanList;
    }

    public Map<String, String> getPropMap() {
        return propMap;
    }

    public void setPropMap(Map<String, String> propMap) {
        this.propMap = propMap;
    }
}
