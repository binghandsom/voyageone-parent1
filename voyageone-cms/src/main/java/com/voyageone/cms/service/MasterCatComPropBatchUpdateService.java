package com.voyageone.cms.service;

import com.voyageone.cms.formbean.MasterCatPropBatchUpdateBean;
import com.voyageone.cms.modelbean.CmsCategoryModel;
import com.voyageone.common.bussiness.platformInfo.model.PlatformInfoModel;
import com.voyageone.core.modelbean.UserSessionBean;

import java.util.List;
import java.util.Map;

public interface MasterCatComPropBatchUpdateService {
	
	public List<Map<String,String>> getPropOptions(String PropName);
	public boolean batchUpdate(MasterCatPropBatchUpdateBean formData,UserSessionBean userSession);
}
