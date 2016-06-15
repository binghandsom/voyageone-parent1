package com.voyageone.service.daoext.cms;

import com.voyageone.service.model.cms.CmsBtJmMasterBrandModel;
import org.springframework.stereotype.Repository;

@Repository
public interface CmsBtJmMasterBrandDaoExt {
    CmsBtJmMasterBrandModel selectByJmMasterBrandId(Integer jmMasterBrandId);
}
