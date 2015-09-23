package com.voyageone.core.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.voyageone.core.modelbean.CompanyBean;
import com.voyageone.core.modelbean.PermissionBean;
import com.voyageone.core.modelbean.UserSessionBean;


public interface LoginService {

	/**
	 * 取得用户信息
	 * 
	 * @param userName 用户名
	 */
	public List<Map<String, Object>> getUserInfoByName(String userName);

	/**
	 * 取得公司信息
	 * 
	 * @param userName 用户名
	 */
	public List<Map<String, Object>> getCompanyInfoByName(String userName);	

	/**
	 * 取得超级用户公司信息
	 * 
	 * @param userName 用户名
	 */
	public List<Map<String, Object>> getCompanyInfoForSuperUser();	

	/**
	 * 取得用户权限
	 * 
	 * @param companyId 公司ID
	 * @param userName 用户名
	 */
	public HashMap<String, PermissionBean> getPermissions(int companyId, String userName);
	
	/**
	 * 取得类型权限
	 * 
	 * @param companyId 所选公司ID
	 * @param userName 用户名
	 * @param type 权限类型：0 -> superuser,1 -> role, 2 -> user
	 */
	public HashMap<String, PermissionBean> getTypePermissions(int companyId, String userName, int type);

	/**
	 * 取得通用权限
	 * property_id设置为0的权限，代表了只和role相关，与property及公司都无关的权限
	 * 
	 * @param userName 用户名
	 */
	public Map<String, Boolean> getCommonPermissions(String userName);

	/**
	 * 取得菜单
	 * 
	 */	
	public List<Map<String, Object>> getApplicationMenuInfo();
	
	/**
	 * 取得菜单
	 * 
	 * @param applicationId
	 */
	public List<Map<String, Object>> getModuleMenuInfo(int applicationId);

	/**
	 * 取得菜单
	 * 
	 * @param moduleId
	 */
	public List<Map<String, Object>> getControllerMenuInfo(int moduleId);

	/**
	 * 取得用户信息
	 * 
	 * @param user
	 */
	public UserSessionBean getUserInfo(UserSessionBean user);

	/**
	 * 取得HOME菜单
	 * 
	 */
	public List<Map<String, Object>> getHomeMenuInfo();

	public List<CompanyBean> getUserCompanyPermissionMap(String userName);
}
