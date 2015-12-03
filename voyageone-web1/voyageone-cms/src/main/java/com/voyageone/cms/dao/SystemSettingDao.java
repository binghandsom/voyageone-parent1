package com.voyageone.cms.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.voyageone.cms.modelbean.MtType;
import com.voyageone.common.Constants;

@Repository
public class SystemSettingDao extends CmsBaseDao {
	/**
	 * 获取MtType
	 * @param data
	 * @return
	 */
	public List<MtType> getCmsMtType(Map<String, Object> data) {
		List<MtType> info = (List) selectList(Constants.DAO_NAME_SPACE_CMS + "cms_get_CmsMtType", data);
		return info;
	}
	/**
	 * 获取MtType的总条数
	 * @return
	 */
	public int getMtTypeCount () {
		return selectOne(Constants.DAO_NAME_SPACE_CMS + "cms_get_CmsMtType");

	}
	/**
	 * 更新MtType
	 * @param mtType
	 * @return
	 */
	public boolean updateCmsMtType (MtType mtType) {
		boolean isSuccess = false ; 
		int count = update(Constants.DAO_NAME_SPACE_CMS + "cms_get_CmsMtType", mtType);
		if(count > 0 ) {
			isSuccess = true;
		}
		return isSuccess;
	}
	/**
	 * 插入MtType
	 * @param mtType
	 * @return
	 */
	public boolean insertCmsMtType (MtType mtType) {
		boolean isSuccess = false;
		int count = insert(Constants.DAO_NAME_SPACE_CMS + "cms_update_CmsMtType", mtType);
		if(count > 0 ) {
			isSuccess = true;
		}
		return isSuccess;
		
	}
}
