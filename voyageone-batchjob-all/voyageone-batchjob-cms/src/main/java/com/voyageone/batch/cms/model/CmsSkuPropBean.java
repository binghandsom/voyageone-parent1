package com.voyageone.batch.cms.model;

import com.voyageone.ims.enums.CmsFieldEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Leo on 15-6-5.
 */
public class CmsSkuPropBean {

    private Map<String, String> propMap;
    private CmsCodePropBean cmsCodePropBean;

    public void setProp(CmsFieldEnum.CmsSkuEnum propEnum, String value)
    {
        if (propMap == null)
            propMap = new HashMap<>();
        propMap.put(propEnum.toString(), value);
    }

    public String getProp(CmsFieldEnum.CmsSkuEnum propKey)
    {
        return propMap.get(propKey.toString());
    }

    public Map<String, String> getPropMap() {
        return propMap;
    }

    public void setPropMap(Map<String, String> propMap) {
        this.propMap = propMap;
    }

    public CmsCodePropBean getCmsCodePropBean() {
        return cmsCodePropBean;
    }

    public void setCmsCodePropBean(CmsCodePropBean cmsCodePropBean) {
        this.cmsCodePropBean = cmsCodePropBean;
    }
}
