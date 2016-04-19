package com.voyageone.service.daoext.jumei;

import com.voyageone.service.model.jumei.CmsBtJmMasterBrandModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CmsBtJmMasterBrandDaoExt {
   public   CmsBtJmMasterBrandModel  getByJmMasterBrandId(Integer jmMasterBrandId);
    }
