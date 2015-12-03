package com.voyageone.cms.formbean;

import java.util.List;

/**
 * Created by lewis on 2015/10/22.
 */
public class MasterCatPropBatchUpdateBean {

    /**
     * 主属性.
     */
    MasterCategoryPropBean masterProperty;

    /**
     * 从属性列表.
     */
    List<MasterCategoryPropBean> properties;

    /**
     * 需要删除的从属性列表.
     */
    List<MasterCategoryPropBean> delProperties;

    /**
     * 需要更新model列表.
     */
    List<String> tarModelList;


    public MasterCategoryPropBean getMasterProperty() {
        return masterProperty;
    }

    public void setMasterProperty(MasterCategoryPropBean masterProperty) {
        this.masterProperty = masterProperty;
    }

    public List<MasterCategoryPropBean> getProperties() {
        return properties;
    }

    public void setProperties(List<MasterCategoryPropBean> properties) {
        this.properties = properties;
    }

    public List<String> getTarModelList() {
        return tarModelList;
    }

    public void setTarModelList(List<String> tarModelList) {
        this.tarModelList = tarModelList;
    }

    public List<MasterCategoryPropBean> getDelProperties() {
        return delProperties;
    }

    public void setDelProperties(List<MasterCategoryPropBean> delProperties) {
        this.delProperties = delProperties;
    }
}
