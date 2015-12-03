package com.voyageone.core.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.voyageone.common.Constants;
import com.voyageone.core.CoreConstants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.voyageone.core.dao.UserDao;
import com.voyageone.core.emum.UserEditEnum;
import com.voyageone.core.formbean.InFormUser;
import com.voyageone.core.modelbean.UserSessionBean;
import com.voyageone.core.service.SettingService;


@Scope(Constants.SCOPE_PROTOTYPE)
@Service
public class SettingServiceImpl implements SettingService {

	@Autowired
	private UserDao userDao;

	@Override
	public List<Map<String, Object>> getUserList(Map<String, Object> data) {

		return userDao.getUserList(data);
	}

	@Override
	public int addUser(InFormUser formUser) {
		return userDao.addUser(formUser);
	}

	@Override
	public int updateUser(InFormUser formUser) {
		return userDao.updateUser(formUser);
	}

	@Override
	public int delUser(int userId) {
		// TODO Auto-generated method stub
		return userDao.delUser(userId);
	}

	/**
	 * 根据用户ID取得该用户的role信息
	 */
	@Override
	public List<Map<String, Object>> getUserRoleInfo(int userId) {
		return userDao.getUserRoleInfoById(userId);
	}

	/**
	 * 根据用户ID取得该用户的权限数据
	 */
	@Override
	public List<Map<String, Object>> getUserPermissionInfo(int userId) {
		return userDao.getUserPermissionInfoById(userId);
	}

	/**
	 * 更新数据（共同）
	 */
	@Override
	public int commonUpdate(Map<String, Object> data, UserEditEnum type) {

		int ret = 0;
		// 更新前先检查一下数据库中是否已存在相应的记录
		List<Map<String, Object>> roleInfo = userDao.selectHandle(data, type);
		// 存在的场合
		if (roleInfo.size() > 0 && !roleInfo.get(0).get("id").toString().equals(data.get("id").toString())) {
			// //删除编辑的那条数据
			// commonDel(Integer.parseInt(data.get("id").toString()),type);
			// //把已存在的那条数据进行更新
			// data.put("id", roleInfo.get(0).get("id").toString());
			return -1;
		}
		ret = userDao.updateHandle(data, type);
		return ret;
	}

	/**
	 * 删除数据（共同）
	 */
	@Override
	public int commonDel(int id, UserEditEnum type) {
		int ret = 0;
		ret = userDao.delHandle(id, type);
		return ret;
	}

	/**
	 * 新增数据（共同）
	 */
	@Override
	public int commonInsert(Map<String, Object> data, UserEditEnum type) {
		int ret = 0;
		if (!data.containsKey("active")) {
			data.put("active", true);
		}
		// 检索改条记录是否存在
		List<Map<String, Object>> roleInfo = userDao.selectHandle(data, type);
		// 存在
		if (roleInfo.size() > 0) {
			// //更新已存在的那条记录
			// data.put("id", roleInfo.get(0).get("id").toString());
			// ret=userDao.updateHandle(data,type);
			ret = -1;
		} else {
			// 插入新数据
			ret = userDao.insertHandle(data, type);
		}
		return ret;
	}

	/**
	 * 查询数据（共同）
	 */
	@Override
	public List<Map<String, Object>> commonSelect(Map<String, Object> data, UserEditEnum type) {
		return userDao.selectHandle(data, type);
	}

	/**
	 * 取得所用的（role,property,company,application,module,controller,action)数据
	 */
	@Override
	public Map<String, Object> getAllRolePermissionName() {
		return userDao.getAllRolePermissionName();
	}

	/**
	 * 最佳指定application下的所有权限
	 */
	@Override
	public int insertRoleAllPermission(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return userDao.insertRoleAllPermission(data);
	}

	@Override
	public int insertUserAllPermission(Map<String, Object> data) {
		// TODO Auto-generated method stub
		return userDao.insertUserAllPermission(data);
	}
	
	@Override
	public void batchAdd(Map<String, Object> requestMap, String userName){
		String temp = (String) requestMap.get("url");
		String bf[] = temp.split("\n");
		String id = null;

		for (String string : bf) {

			if (string.length() > 2) {
				if ("/".equals(string.subSequence(0, 1))) {
					string = string.substring(1);
				}
				String appInfo[] = string.split("/");
				Map<String, Object> req;
				List<Map<String, Object>> r;
				if (appInfo.length > 0) {
					req = new java.util.HashMap<String, Object>();
					req.put("application", appInfo[0]);
					req.put("modifier", userName);
					r = commonSelect(req, UserEditEnum.Application);
					if (r.size() == 0) {
						commonInsert(req, UserEditEnum.Application);
						id = req.get("id").toString();
					} else {
						id = r.get(0).get("id").toString();
					}
				}
				if (appInfo.length > 1) {
					req = new java.util.HashMap<String, Object>();
					req.put("application_id", id);
					req.put("module", appInfo[1]);
					req.put("modifier", userName);
					r = commonSelect(req, UserEditEnum.Module);
					if (r.size() == 0) {
						commonInsert(req, UserEditEnum.Module);
						id = req.get("id").toString();
					} else {
						id = r.get(0).get("id").toString();
					}
				}
				if (appInfo.length > 2) {
					req = new java.util.HashMap<String, Object>();
					req.put("module_id", id);
					req.put("controller", appInfo[2]);
					req.put("modifier", userName);
					r = commonSelect(req, UserEditEnum.Controller);
					if (r.size() == 0) {
						commonInsert(req, UserEditEnum.Controller);
						id = req.get("id").toString();
					} else {
						id = r.get(0).get("id").toString();
					}
				}
				if (appInfo.length > 3) {
					req = new java.util.HashMap<String, Object>();
					req.put("controller_id", id);
					req.put("name", appInfo[3]);
					req.put("modifier", userName);
					r = commonSelect(req, UserEditEnum.Action);
					if (r.size() == 0) {
						commonInsert(req, UserEditEnum.Action);
						id = req.get("id").toString();
					} else {
						id = r.get(0).get("id").toString();
					}
				}
			}
		}
	}

	/**
	 * 更新UserConfig数据
	 * 
	 */
	@Override
	public void updateUserConfig(Map<String, Object> requestMap,UserSessionBean userBean) {
		requestMap.put("userId", userBean.getUserId());
		requestMap.put("modifier", userBean.getUserName());
		
		if(userDao.updateUserConfig(requestMap) == 0){
			userDao.insertUserConfig(requestMap);
		}
		List<String> emptyList = new ArrayList<>();
		if("cms_us_product_attributes".equalsIgnoreCase(requestMap.get("cfgName").toString())){
			userBean.getCmsProductfields().put("cmsUsProductAttributes", requestMap.get("cfgVal1").toString().isEmpty() ? emptyList : requestMap.get("cfgVal1").toString().split(","));
		}else if("cms_cn_product_attributes".equalsIgnoreCase(requestMap.get("cfgName").toString())){
			userBean.getCmsProductfields().put("cmsCnProductAttributes", requestMap.get("cfgVal1").toString().isEmpty() ? emptyList : requestMap.get("cfgVal1").toString().split(","));
		}
		
	}
}
