package com.voyageone.core.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.voyageone.common.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.voyageone.core.CoreConstants;
import com.voyageone.core.dao.MasterInfoDao;
import com.voyageone.core.modelbean.MasterInfoBean;
import com.voyageone.core.service.MasterInfoService;

@Scope(Constants.SCOPE_PROTOTYPE)
@Service
public class MasterInfoServiceImpl implements MasterInfoService {

	@Autowired
	@Qualifier("CoreMasterInfoDao")
	private MasterInfoDao masterInfoDao;
	
	@Override
	public List<MasterInfoBean> getMasterInfoFromId(int type, boolean showBlank) {

		
		List<MasterInfoBean> masterInfoList = masterInfoDao.getMasterInfoFromId(type);
		if (masterInfoList == null) {
			masterInfoList = new ArrayList<MasterInfoBean>();
		}
		
		if (showBlank) {
			// default 项目追加
			MasterInfoBean defaultItem = new MasterInfoBean();
			masterInfoList.add(0, defaultItem);
		}
		
		return masterInfoList;
	}
}
