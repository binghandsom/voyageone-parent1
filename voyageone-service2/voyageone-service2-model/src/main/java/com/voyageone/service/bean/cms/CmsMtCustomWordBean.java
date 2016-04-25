package com.voyageone.service.bean.cms;


import com.voyageone.service.model.cms.CmsMtCustomWordModel;
import com.voyageone.service.model.cms.CmsMtCustomWordParamModel;

import java.util.List;

public class CmsMtCustomWordBean extends CmsMtCustomWordModel {


    private List<CmsMtCustomWordParamModel> params;

    public List<CmsMtCustomWordParamModel> getParams() {
        return params;
    }

    public void setParams(List<CmsMtCustomWordParamModel> params) {
        this.params = params;
    }

}