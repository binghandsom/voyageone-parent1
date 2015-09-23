package com.voyageone.oms.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.voyageone.common.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.voyageone.core.modelbean.MasterInfoBean;
import com.voyageone.oms.dao.MasterInfoDao;
import com.voyageone.oms.service.OmsMasterInfoService;

@Scope(Constants.SCOPE_PROTOTYPE)
@Service
public class OmsMasterInfoServiceImpl implements OmsMasterInfoService {

	@Autowired
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
//			defaultItem.setId("-1");
			defaultItem.setName("Please select...");
			masterInfoList.add(0, defaultItem);
		}
		
		return masterInfoList;
	}
}
