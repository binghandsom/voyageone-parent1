package com.voyageone.batch.cms.model;

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
    private List<Map<String,Object>> attributeNameValueList;

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

    public List<Map<String, Object>> getAttributeNameValueList() {
        return attributeNameValueList;
    }

    public void setAttributeNameValueList(List<Map<String, Object>> attributeNameValueList) {
        this.attributeNameValueList = attributeNameValueList;
    }
}
