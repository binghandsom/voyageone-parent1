package com.voyageone.core.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.common.Constants;
import com.voyageone.core.modelbean.ChannelShopBean;
import com.voyageone.core.modelbean.ChannelStoreBean;
import com.voyageone.core.modelbean.CompanyBean;
import com.voyageone.core.modelbean.UserConfigBean;
import com.voyageone.core.modelbean.UserHardwareConfigBean;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class LoginDao extends BaseDao {
	
	/**
	 * 登录检验
	 * @param userName
	 * @return
	 */
	public List<Map<String, Object>> getUserInfoByName(String userName) {
		List<Map<String, Object>> userInfoList = 
				(List) selectList(Constants.DAO_NAME_SPACE_CORE + "ct_user_getUserByUserName", userName);
		
		if (userInfoList == null || userInfoList.size() == 0) {
			userInfoList = new ArrayList<Map<String, Object>>();
		}
		return userInfoList;
	}
	
	/**
	 * 公司信息
	 * @param userName
	 * @return
	 */
	public List<Map<String, Object>> getCompanyInfoByName(String userName) {
		List<Map<String, Object>> companyInfoList = 
				(List) selectList(Constants.DAO_NAME_SPACE_CORE + "ct_user_getCompanyByUserName", userName);
		
		if (companyInfoList == null || companyInfoList.size() == 0) {
			companyInfoList = new ArrayList<Map<String, Object>>();
		}
		
		return companyInfoList;
	}

	/**
	 * 公司信息
	 * @param userName
	 * @return
	 */
	public List<Map<String, Object>> getCompanyInfoForSuperUser() {
		List<Map<String, Object>> companyInfoList = 
				(List) selectList(Constants.DAO_NAME_SPACE_CORE + "ct_user_getCompanyForSuperUser");
		
		if (companyInfoList == null || companyInfoList.size() == 0) {
			companyInfoList = new ArrayList<Map<String, Object>>();
		}
		
		return companyInfoList;
	}

	/**
	 * 当前公司用户角色权限信息
	 * @param companyId
	 * @param userName
	 * @return
	 */
	public List<Map<String, Object>> getRolePermissions(int companyId, String userName) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("companyId", companyId);
		params.put("userName", userName);
		
		List<Map<String, Object>> permissionList = 
				(List) selectList(Constants.DAO_NAME_SPACE_CORE + "ct_role_permission_getPermissionByRole", params);
		
		if (permissionList == null || permissionList.size() == 0) {
			permissionList = new ArrayList<Map<String, Object>>();
		}
		
		return permissionList;
	}

	/**
	 * 当前超级用户权限信息
	 * @param companyId
	 * @return
	 */
	public List<Map<String, Object>> getSuperUserPermissions(int companyId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("companyId", companyId);
		
		List<Map<String, Object>> permissionList = 
				(List) selectList(Constants.DAO_NAME_SPACE_CORE + "ct_role_permission_getPermissionForSuperUser", params);
		
		if (permissionList == null || permissionList.size() == 0) {
			permissionList = new ArrayList<Map<String, Object>>();
		}
		
		return permissionList;
	}

	/**
	 * 当前公司用户权限信息
	 * @param companyId
	 * @param userName
	 * @return
	 */
	public List<Map<String, Object>> getUserPermissions(int companyId, String userName) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("companyId", companyId);
		params.put("userName", userName);
		
		List<Map<String, Object>> permissionList = 
				(List) selectList(Constants.DAO_NAME_SPACE_CORE + "ct_user_permission_getPermissionByUser", params);
		
		if (permissionList == null || permissionList.size() == 0) {
			permissionList = new ArrayList<Map<String, Object>>();
		}
		
		return permissionList;
	}

	/**
	 * 通用权限信息
	 * @param companyId
	 * @param userName
	 * @return
	 */
	public List<Map<String, Object>> getCommonPermissions(String userName) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userName", userName);

		List<Map<String, Object>> permissionList = 
				(List) selectList(Constants.DAO_NAME_SPACE_CORE + "ct_role_permission_getCommonPermission", params);
		
		if (permissionList == null || permissionList.size() == 0) {
			permissionList = new ArrayList<Map<String, Object>>();
		}
		
		return permissionList;
	}

	/**
	 * 取得Home菜单信息
	 * @return
	 */
	public List<Map<String, Object>> getHomeMenuInfo() {
		List<Map<String, Object>> menuInfoList = 
				(List) selectList(Constants.DAO_NAME_SPACE_CORE + "ct_controller_getHomeMenuInfo");
		
		if (menuInfoList == null || menuInfoList.size() == 0) {
			menuInfoList = new ArrayList<Map<String, Object>>();
		}
		return menuInfoList;
	}

	/**
	 * 取得菜单信息
	 * @return
	 */
	public List<Map<String, Object>> getApplicationMenuInfo() {
		List<Map<String, Object>> menuInfoList = 
				(List) selectList(Constants.DAO_NAME_SPACE_CORE + "ct_application_getMenuInfo");
		
		if (menuInfoList == null || menuInfoList.size() == 0) {
			menuInfoList = new ArrayList<Map<String, Object>>();
		}
		return menuInfoList;
	}

	/**
	 * 取得菜单信息
	 * @return
	 */
	public List<Map<String, Object>> getModuleMenuInfo(int applicationId) {
		List<Map<String, Object>> menuInfoList = 
				(List) selectList(Constants.DAO_NAME_SPACE_CORE + "ct_module_getMenuInfo", applicationId);
		
		if (menuInfoList == null || menuInfoList.size() == 0) {
			menuInfoList = new ArrayList<Map<String, Object>>();
		}
		return menuInfoList;
	}

	/**
	 * 取得菜单信息
	 * @return
	 */
	public List<Map<String, Object>> getControllerMenuInfo(int moduleId) {
		List<Map<String, Object>> menuInfoList = 
				(List) selectList(Constants.DAO_NAME_SPACE_CORE + "ct_controller_getMenuInfo", moduleId);
		
		if (menuInfoList == null || menuInfoList.size() == 0) {
			menuInfoList = new ArrayList<Map<String, Object>>();
		}
		return menuInfoList;
	}

	/**
	 * 取得配置信息
	 * @return
	 */
	public List<UserConfigBean> getUserConfig(int userId) {
		List<UserConfigBean> configInfoList =
				 selectList(Constants.DAO_NAME_SPACE_CORE + "ct_user_config_getConfigInfo", userId);
		
		if (configInfoList == null || configInfoList.size() == 0) {
			configInfoList = new ArrayList<UserConfigBean>();

		}
		return configInfoList;
	}

    /**
     * 取得配置信息
     * @return
     */
    public List<ChannelShopBean> getUserShopList(int userId) {
        List<ChannelShopBean> shopList =
                selectList(Constants.DAO_NAME_SPACE_CORE + "ct_user_config_getUserShopList", userId);

        if (shopList == null || shopList.size() == 0) {
            shopList = new ArrayList<ChannelShopBean>();
        }
        return shopList;
    }

    /**
     * 获取用户 所属记录
     * @return
     */
    public List<ChannelStoreBean> getUserStoreList(int userId) {
        List<ChannelStoreBean> storeList =
                selectList(Constants.DAO_NAME_SPACE_CORE + "ct_user_config_getUserStoreList", userId);

        if (storeList == null || storeList.size() == 0) {
            storeList = new ArrayList<ChannelStoreBean>();
        }
        return storeList;
    }

	/**
	 * 获取相关渠道 所属记录
	 * @return
	 */
	public List<ChannelStoreBean> getChannelStoreList(List<String> channelList) {

		Map<String, Object> params = new HashMap<>();

		params.put("channelList", channelList);

		List<ChannelStoreBean> storeList =
				selectList(Constants.DAO_NAME_SPACE_CORE + "ct_user_config_getChannelStoreList", params);

		if (storeList == null || storeList.size() == 0) {
			storeList = new ArrayList<ChannelStoreBean>();
		}
		return storeList;
	}

    /**
     * 获取用户 所有移库对象
     * @return
     */
    public List<ChannelStoreBean> getUserStoreToList(int userId) {
        List<ChannelStoreBean> storeList =
                selectList(Constants.DAO_NAME_SPACE_CORE + "ct_user_config_getUserStoreToList", userId);

        if (storeList == null || storeList.size() == 0) {
            storeList = new ArrayList<ChannelStoreBean>();
        }
        return storeList;
    }

    /**
     * 获取用户 硬件配置信息
     * @return
     */
    public List<UserHardwareConfigBean> getUserHardwareConfigList(int userId) {
        List<UserHardwareConfigBean> userHardwareConfigList =
                selectList(Constants.DAO_NAME_SPACE_CORE + "ct_user_hardware_config_getConfig", userId);

        if (userHardwareConfigList == null || userHardwareConfigList.size() == 0) {
            userHardwareConfigList = new ArrayList<UserHardwareConfigBean>();
        }
        return userHardwareConfigList;
    }
    
    public List<CompanyBean> getUserCompanyPermissionMap(String userName){
    	return selectList(Constants.DAO_NAME_SPACE_CORE + "ct_user_company_permission", userName);
    }
}
