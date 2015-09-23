package com.voyageone.batch.ims.modelbean;

import com.voyageone.ims.enums.CmsFieldEnum;

import java.util.List;
import java.util.Map;

/**
 * Created by Leo on 2015/5/29.
 */
public class CmsCodePropBean {
    private Map<String, String> propMap;
    private CmsModelPropBean cmsModelPropBean;
    private List<CmsSkuPropBean> cmsSkuPropBeanList;

    public String getProp(CmsFieldEnum.CmsCodeEnum propKey)
    {
        return propMap.get(propKey.toString());
    }

    public Map<String, String> getPropMap() {
        return propMap;
    }

    public void setPropMap(Map<String, String> propMap) {
        this.propMap = propMap;
    }

    public CmsModelPropBean getCmsModelPropBean() {
        return cmsModelPropBean;
    }

    public void setCmsModelPropBean(CmsModelPropBean cmsModelPropBean) {
        this.cmsModelPropBean = cmsModelPropBean;
    }

    public List<CmsSkuPropBean> getCmsSkuPropBeanList() {
        return cmsSkuPropBeanList;
    }

    public void setCmsSkuPropBeanList(List<CmsSkuPropBean> cmsSkuPropBeanList) {
        this.cmsSkuPropBeanList = cmsSkuPropBeanList;
    }
}
