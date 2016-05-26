package com.voyageone.service.bean.openapi.image;


import com.voyageone.service.bean.openapi.OpenApiResultBean;
import com.voyageone.service.model.cms.CmsMtImageCreateFileModel;

import java.util.List;

public class GetListResultBean extends OpenApiResultBean {

    protected List<CmsMtImageCreateFileModel> cmsMtImageCreateFiles;

    public List<CmsMtImageCreateFileModel> getCmsMtImageCreateFiles() {
        return cmsMtImageCreateFiles;
    }

    public void setCmsMtImageCreateFiles(List<CmsMtImageCreateFileModel> cmsMtImageCreateFiles) {
        this.cmsMtImageCreateFiles = cmsMtImageCreateFiles;
    }
}