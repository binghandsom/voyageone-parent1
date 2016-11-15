package com.voyageone.service.bean.cms;

import com.voyageone.service.model.cms.CmsBtShelvesModel;
import com.voyageone.service.model.cms.CmsBtShelvesProductModel;

import java.util.List;

/**
 * Created by james on 2016/11/15.
 */
public class CmsBtShelvesInfoBean {
    private CmsBtShelvesModel shelvesModel;

    private List<CmsBtShelvesProductBean> shelvesProductModels;

    public CmsBtShelvesModel getShelvesModel() {
        return shelvesModel;
    }

    public void setShelvesModel(CmsBtShelvesModel shelvesModel) {
        this.shelvesModel = shelvesModel;
    }

    public List<CmsBtShelvesProductBean> getShelvesProductModels() {
        return shelvesProductModels;
    }

    public void setShelvesProductModels(List<CmsBtShelvesProductBean> shelvesProductModels) {
        this.shelvesProductModels = shelvesProductModels;
    }
}
