package com.voyageone.cms.service;

import java.util.List;

import com.voyageone.cms.formbean.CmsCategoryBean;
import com.voyageone.cms.modelbean.CmsCategoryModel;
import com.voyageone.common.bussiness.platformInfo.model.PlatformInfoModel;

public interface MasterCategoryMatchService {
	
//	public List<CmsCategoryBean> getCmsCategoryList(String channelId);
	
	public List<PlatformInfoModel> getPlatformInfo(int categoryId);

	public List<CmsCategoryModel> getCmsCategoryModelList(String channelId);
}
