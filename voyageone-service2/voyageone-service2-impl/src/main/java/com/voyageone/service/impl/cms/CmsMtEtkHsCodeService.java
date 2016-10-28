package com.voyageone.service.impl.cms;

import com.voyageone.service.daoext.cms.CmsMtEtkHsCodeDaoExt;
import com.voyageone.service.model.cms.CmsMtEtkHsCodeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by james on 2016/10/24.
 */
@Service
public class CmsMtEtkHsCodeService {
    @Autowired
    private CmsMtEtkHsCodeDaoExt cmsMtEtkHsCodeDaoExt;

    public CmsMtEtkHsCodeModel getEdcHsCodeByHsCode(String hsCode){
        return cmsMtEtkHsCodeDaoExt.selectOneByHsCode(hsCode);
    }

}
