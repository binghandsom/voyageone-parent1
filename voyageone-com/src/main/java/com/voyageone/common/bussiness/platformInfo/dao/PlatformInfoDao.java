package com.voyageone.common.bussiness.platformInfo.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.common.Constants;
import com.voyageone.common.bussiness.platformInfo.model.PlatformInfoModel;
@Repository
public class PlatformInfoDao extends BaseDao {

	public List<PlatformInfoModel> getPlatformInfo(int categoryId) {
		
		Map<String, Integer> parms = new HashMap<>();
		
		parms.put("categoryId", categoryId);
		
		return super.selectList(Constants.DAO_NAME_SPACE_COMMON+"com_platform_info", parms);
	}
	
}
