package com.voyageone.cms.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.voyageone.cms.dao.SystemSettingDao;
import com.voyageone.cms.modelbean.MtType;
import com.voyageone.cms.service.SystemSettingService;

@Service
public class SystemSettingServiceImpl implements SystemSettingService {
      
	@Resource
	private SystemSettingDao systemSettingDao;
	/**
	 * 获取MtType
	 * @param data
	 * @return
	 */
	@Override
	public List<MtType> getCmsMtType (Map<String,Object> data) {
		return systemSettingDao.getCmsMtType(data);
	}
	/**
	 * 更新MtType
	 * @param mtType
	 * @return
	 */
	@Override
	public boolean updateMtType (MtType mtType) {
		return systemSettingDao.updateCmsMtType(mtType);
	}
	/**
	 * 插入MtType
	 * @param mtType
	 * @return
	 */
	@Override
	public boolean insertMtType (MtType mtType) {
		return systemSettingDao.insertCmsMtType(mtType);
	}
	/**
	 * 获取MtType的总条数
	 * @return
	 */
	@Override
	public int getMtTypeCount () {
		return systemSettingDao.getMtTypeCount();
	}
}
