package com.voyageone.core.service.impl;

import java.util.*;
import java.util.Map.Entry;

import com.voyageone.common.Constants;
import com.voyageone.common.configs.Enums.StoreConfigEnums;
import com.voyageone.core.CoreConstants;
import com.voyageone.core.modelbean.*;
import com.voyageone.core.utilbean.ConfigConstants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.voyageone.core.dao.LoginDao;
import com.voyageone.core.service.LoginService;

@Scope(Constants.SCOPE_PROTOTYPE)
@Service
public class LoginServiceImpl implements LoginService {
	
	@Autowired
	private LoginDao loginDao;
	
	@Override
	public List<Map<String, Object>> getUserInfoByName(String userName) {
		// 用户名密码检验
		List<Map<String, Object>> userInfo = loginDao.getUserInfoByName(userName);
		
		return userInfo;
	}

	@Override
	public List<Map<String, Object>> getCompanyInfoByName(String userName) {
		// 公司信息
		List<Map<String, Object>> companyInfo = loginDao.getCompanyInfoByName(userName);
		
		return companyInfo;
	}

	@Override
	public List<Map<String, Object>> getCompanyInfoForSuperUser() {
		// 公司信息
		List<Map<String, Object>> companyInfo = loginDao.getCompanyInfoForSuperUser();
		
		return companyInfo;
	}
	
	@Override
	public HashMap<String, PermissionBean> getPermissions(int companyId, String userName) {
		
		// 取得角色定义权限
		HashMap<String, PermissionBean> permissions = getTypePermissions(companyId, userName, 1);
		
		// 取得用户定义权限
		HashMap<String, PermissionBean> userPermission = getTypePermissions(companyId, userName, 2);
		
		// 遍历user permission，将user permission中的权限覆盖role permission
		Iterator<Entry<String, PermissionBean>> iterator = userPermission.entrySet().iterator();
		while(iterator.hasNext()) {
			Entry<String, PermissionBean> entry = (Entry<String, PermissionBean>)iterator.next();
			String key = entry.getKey();
			PermissionBean permissionUnit = entry.getValue();
			// 取得用户级权限定义
			Map<String, Boolean> userPermissionList = permissionUnit.getPermissions();
			if(permissions.containsKey(key)){
				// 如果存在相同的property,则用user permission覆盖
				Iterator<Entry<String, Boolean>> userIterator = userPermissionList.entrySet().iterator();
				while(userIterator.hasNext()) {
					Entry<String, Boolean> line = (Entry<String, Boolean>)userIterator.next();
					String url = line.getKey();
					Boolean value = line.getValue();
					permissions.get(key).getPermissions().put(url, value);
				}
			}else{
				// 如果user permission中有新的permission,则加入role permission
				permissions.put(key, permissionUnit);
			}
		}		
		
		// 取得通用权限
		Map<String, Boolean> commonPermission = getCommonPermissions(userName);
		Iterator<Entry<String, Boolean>> commonIterator = commonPermission.entrySet().iterator();
		while(commonIterator.hasNext()) {
			Entry<String, Boolean> line = (Entry<String, Boolean>)commonIterator.next();
			String url = line.getKey();
			Boolean value = line.getValue();
			Iterator<Entry<String, PermissionBean>> unitIterator = permissions.entrySet().iterator();
			while(unitIterator.hasNext()) {
				Entry<String, PermissionBean> unitLine = (Entry<String, PermissionBean>)unitIterator.next();
				unitLine.getValue().getPermissions().put(url, value);
			}
		}
		
		return permissions;
	}

	@Override
	public HashMap<String, PermissionBean> getTypePermissions(int companyId, String userName, int type) {

		// 权限列表
		HashMap<String, PermissionBean> permissionList = new HashMap<String, PermissionBean>();
		List<Map<String, Object>> result = null;
		if(type == 0){
			// 超级用户权限获取
			result = loginDao.getSuperUserPermissions(companyId);
		}else if(type == 1){
			// role权限
			result = loginDao.getRolePermissions(companyId, userName);
		}else if(type == 2){
			// user权限
			result = loginDao.getUserPermissions(companyId, userName); 
		}else{
			result = loginDao.getRolePermissions(companyId, userName);
		}
		
		for(int i = 0;i < result.size();i++){
			Map<String, Object> line = result.get(i);
			String propertyId = (String)line.get("property_id");
			// 拼接URL
			String url = "/" + line.get("application").toString()
					   + "/" + line.get("module").toString()
					   + "/" + line.get("controller").toString()
					   + "/" + line.get("action").toString();
			Boolean value = (Boolean)line.get("value");
			// 如果property在权限列表中是否已存在
			if(permissionList.containsKey(propertyId)){
				PermissionBean permission = permissionList.get(propertyId);
				ArrayList<String> roles = permission.getPropertyRoles();
				// 获取Role权限时，记录用户Role
				if(type == 1 && line.get("role") != null){
					// 如果Role没有包含，那么加入该Role
					if(!roles.contains(line.get("role").toString())){
						roles.add(line.get("role").toString());
						permission.setPropertyRoles(roles);
					}
				}
				// 加入权限定义
				Map<String, Boolean> urlList = permission.getPermissions();
				urlList.put(url, value);
				permission.setPermissions(urlList);
				permissionList.put(propertyId, permission);
			}else{
				PermissionBean permission = new PermissionBean();
				permission.setPropertyId(propertyId);
				permission.setPropertyName(line.get("property_name").toString());
				Map<String, Boolean> urlList = new HashMap<String, Boolean>();
				urlList.put(url, value);
				ArrayList<String> roles = new ArrayList<String>();
				// 获取Role权限时，记录用户Role
				if(type == 1 && line.get("role") != null){
					// 加role
					roles.add(line.get("role").toString());
				}
				permission.setPropertyRoles(roles);
				permission.setPermissions(urlList);
				permissionList.put(propertyId, permission);
			}	
		}		
		return permissionList;
	}

	@Override
	public Map<String, Boolean> getCommonPermissions(String userName) {
		
		Map<String, Boolean> commonPermissions = new HashMap<String, Boolean>();
		// 权限列表
		List<Map<String, Object>> permissions = loginDao.getCommonPermissions(userName);
		// 遍历通用权限
		for(int i = 0;i < permissions.size();i++){
			Map<String, Object> line = permissions.get(i);
			// 拼接URL
			String url = "/" + line.get("application").toString()
					   + "/" + line.get("module").toString()
					   + "/" + line.get("controller").toString()
					   + "/" + line.get("action").toString();
			Boolean value = (Boolean)line.get("value");
			commonPermissions.put(url, value);
		}		
		
		return commonPermissions;
	}

	@Override
	public List<Map<String, Object>> getApplicationMenuInfo() {
		// 取得菜单
		List<Map<String, Object>> menuInfoList = loginDao.getApplicationMenuInfo();
		
		return menuInfoList;
	}

	@Override
	public List<Map<String, Object>> getModuleMenuInfo(int applicationId) {
		// 取得菜单
		List<Map<String, Object>> menuInfoList = loginDao.getModuleMenuInfo(applicationId);
		
		return menuInfoList;
	}

	@Override
	public List<Map<String, Object>> getHomeMenuInfo() {
		// 取得菜单
		List<Map<String, Object>> homeMenuInfo = loginDao.getHomeMenuInfo();
		
		return homeMenuInfo;
	}

	@Override
	public List<Map<String, Object>> getControllerMenuInfo(int moduleId) {
		// 取得菜单
		List<Map<String, Object>> menuInfoList = loginDao.getControllerMenuInfo(moduleId);
		
		return menuInfoList;
	}

	@Override
	public UserSessionBean getUserInfo(UserSessionBean user) {

		// action单位权限
		List<String> actionPermission = new ArrayList<String>();
		// 页面单位权限
		List<String> pagePermission = new ArrayList<String>();
		// 用户菜单列表
		List<MenuBean> userMenuList = new ArrayList<MenuBean>();
		
		// 取得用户权限信息
		HashMap<String, PermissionBean> permissions = new HashMap<String, PermissionBean>();
		
		if(user.isSuperUser()){
			permissions = getTypePermissions(user.getSelectCompany().getCompanyId(), user.getUserName(), 0);
		}else{
			permissions = getPermissions(user.getSelectCompany().getCompanyId(), user.getUserName());
		}
		
		user.setPropertyPermissions(permissions);

		// 取得3级页面权限
		Iterator<Entry<String, PermissionBean>> iterator = permissions.entrySet().iterator();
		while(iterator.hasNext()) {
			Entry<String, PermissionBean> entry = (Entry<String, PermissionBean>)iterator.next();
			PermissionBean permissionUnit = entry.getValue();
			// 取得用户级权限定义
			Map<String, Boolean> unitPermissionList = permissionUnit.getPermissions();
			Iterator<Entry<String, Boolean>> unitIterator = unitPermissionList.entrySet().iterator();
			while(unitIterator.hasNext()){
				Entry<String, Boolean> permission = (Entry<String, Boolean>)unitIterator.next();
				if(permission.getValue()){
					String url = permission.getKey();
					if(!actionPermission.contains(url)){
						actionPermission.add(url);
					}
					String pageUrl = url.substring(0, url.lastIndexOf("/"));
					if(!pagePermission.contains(pageUrl)){
						pagePermission.add(pageUrl);
					}
				}
			}
		}
		// 导航列表
		List<PageNaviBean> pageNaviList = new ArrayList<PageNaviBean>();
		
		// Home导航项
		// 取得Home的显示名和URL
		List<Map<String, Object>> homeMenuInfo = getHomeMenuInfo();
		NaviBean homeNavi = new NaviBean();
		homeNavi.setMenuName(homeMenuInfo.get(0).get("menu_title").toString());
		homeNavi.setMenuUrl(homeMenuInfo.get(0).get("default_url").toString());
		List<NaviBean> homepageNaviList = new ArrayList<NaviBean>();

		// Home导航项结构为：HOME
		PageNaviBean homepage = new PageNaviBean();
		homepage.setPermissionUrl(homeMenuInfo.get(0).get("default_url").toString());
		homepageNaviList.add(homeNavi);
		homepage.setNavigationList(homepageNaviList);
		// 加入Home的导航项
		pageNaviList.add(homepage);
		
		// 取得系统定义所有Application级菜单
		List<Map<String, Object>> applicationMenuList = getApplicationMenuInfo();
		for(int i = 0;i < applicationMenuList.size();i++){
			Map<String, Object> line = applicationMenuList.get(i);
			String url = line.get("default_url").toString();
			String applicationTitle = line.get("menu_title").toString();
						
			// 判断用户是否有一级菜单项URL的权限，如果有则加入该菜单项
			if(pagePermission.contains(url)){
				MenuBean menu = new MenuBean();
				menu.setMenuName(line.get("menu_title").toString());
				menu.setMenuUrl(url);
								
				// 加入application导航项
				NaviBean applicationNavi = new NaviBean();
				applicationNavi.setMenuName(applicationTitle);
				applicationNavi.setMenuUrl(url);			
				List<NaviBean> applicationNaviList = new ArrayList<NaviBean>();
				
				// application导航项结构为：HOME -> OMS
				PageNaviBean applicationPage = new PageNaviBean();
				applicationPage.setPermissionUrl(url);
				// 加入Home的导航项
				applicationNaviList.add(homeNavi);
				// 加入application的导航项
				applicationNaviList.add(applicationNavi);
				applicationPage.setNavigationList(applicationNaviList);
				// 加入导航列表
				pageNaviList.add(applicationPage);				
				
				Integer applicationId = Integer.parseInt(line.get("id").toString());
				// 取得该Application下所有Module菜单项
				List<Map<String, Object>> moduleMenuList = getModuleMenuInfo(applicationId);
				List<MenuBean> moduleList = new ArrayList<MenuBean>();
				for(int j = 0;j < moduleMenuList.size();j++){
					Map<String, Object> moduleLine = moduleMenuList.get(j);
					String moduleUrl = moduleLine.get("default_url").toString();
					// 判断用户是否有Module菜单项URL的权限，如果有则加入该菜单项
					if(pagePermission.contains(moduleUrl)){
						MenuBean moduleMenu = new MenuBean();
						moduleMenu.setMenuName(moduleLine.get("menu_title").toString());
						moduleMenu.setMenuUrl(moduleUrl);
						
						// 加入Module导航项
						NaviBean moduleNavi = new NaviBean();
						moduleNavi.setMenuName(moduleLine.get("menu_title").toString());
						moduleNavi.setMenuUrl(moduleUrl);	
						List<NaviBean> moduleNaviList = new ArrayList<NaviBean>();
						
						// module导航项结构为：HOME -> OMS -> Orders
						PageNaviBean modulePage = new PageNaviBean();
						modulePage.setPermissionUrl(moduleUrl);
						// 加入Home的导航项
						moduleNaviList.add(homeNavi);
						// 加入application的导航项
						moduleNaviList.add(applicationNavi);
						// 加入module的导航项
						moduleNaviList.add(moduleNavi);
						modulePage.setNavigationList(moduleNaviList);
						// 加入导航列表
						pageNaviList.add(modulePage);
						
						
						Integer moduleId = Integer.parseInt(moduleLine.get("id").toString());
						// 取得该Module下所有Controller菜单项
						List<Map<String, Object>> controllerMenuList = getControllerMenuInfo(moduleId);
						List<MenuBean> controllerList = new ArrayList<MenuBean>();
						for(int k = 0;k < controllerMenuList.size();k++){
							Map<String, Object> controllerLine = controllerMenuList.get(k);
							String controllerUrl = controllerLine.get("default_url").toString();
							// 判断用户是否有Controller菜单项URL的权限，如果有则加入该菜单项
							if(pagePermission.contains(controllerUrl)){
								MenuBean controllerMenu = new MenuBean();
								controllerMenu.setMenuName(controllerLine.get("menu_title").toString());
								controllerMenu.setMenuUrl(controllerUrl);
								controllerList.add(controllerMenu);

								// 加入Page导航项
								NaviBean controllerNavi = new NaviBean();
								controllerNavi.setMenuName(controllerLine.get("menu_title").toString());
								controllerNavi.setMenuUrl(controllerUrl);	
								List<NaviBean> controllerNaviList = new ArrayList<NaviBean>();
								// Page导航项结构为：HOME -> OMS -> Orders -> Search
								PageNaviBean controllerPage = new PageNaviBean();
								controllerPage.setPermissionUrl(controllerUrl);
								// 加入Home的导航项
								controllerNaviList.add(homeNavi);
								// 加入application的导航项
								controllerNaviList.add(applicationNavi);
								// 加入module的导航项
								controllerNaviList.add(moduleNavi);
								// 加入Page的导航项
								controllerNaviList.add(controllerNavi);
								controllerPage.setNavigationList(controllerNaviList);
								// 加入导航列表
								pageNaviList.add(controllerPage);
							}
						}
						moduleMenu.setSubMenuList(controllerList);
						moduleList.add(moduleMenu);
					}					
				}
				menu.setSubMenuList(moduleList);
				userMenuList.add(menu);
			}
		}
	
		user.setActionPermission(actionPermission);
		user.setPagePermission(pagePermission);
		user.setUserMenuList(userMenuList);
		user.setPageNavi(pageNaviList);

		// 取得用户当前选择公司的渠道值
		List<String> channelList = user.getChannelList();

		// 判断用户角色
		List<String> propertyRoles = new ArrayList<>();
		for (String channel : channelList) {
			propertyRoles.addAll(user.getPropertyPermissions().get(channel).getPropertyRoles());
		}

		// 获取相关仓库
		List<ChannelStoreBean> storeList = new ArrayList<>();

		// 用户角色中有仓库时，取得用户表中设定的仓库
		if (propertyRoles.contains(CoreConstants.Role.WHS)) {
			storeList = getUserStoreList(user.getUserId());
		}
		// 管理者或客服或运营时，取得相关渠道下的仓库
		else if (propertyRoles.contains(CoreConstants.Role.ADMIN) || propertyRoles.contains(CoreConstants.Role.CS) || propertyRoles.contains(CoreConstants.Role.OP)){
			storeList = getChannelStoreList(channelList);
		}

        user.setStoreList(storeList);
		// 获取用户 选择公司的所属仓库
		List<ChannelStoreBean> companyStoreList = new ArrayList<>();
		List<ChannelStoreBean> companyRealStoreList = new ArrayList<>();
		if(storeList != null) {
			for (ChannelStoreBean channelStoreBean : storeList) {
				if (channelList.contains(channelStoreBean.getOrder_channel_id())) {
					companyStoreList.add(channelStoreBean);
					// 真实仓库才抽出
					if (StoreConfigEnums.Kind.REAL.getId().equals(channelStoreBean.getStore_kind())) {
						companyRealStoreList.add(channelStoreBean);
					}
				}
			}
		}
		user.setCompanyStoreList(companyStoreList);
		user.setCompanyRealStoreList(companyRealStoreList);

		// 获取用户 所有店铺
        List<ChannelShopBean> shopList = getUserShopList(user.getUserId());
        user.setShopList(shopList);
		// 获取用户 选择公司的所属店铺
		List<ChannelShopBean> companyShopList = new ArrayList<>();
		if(shopList != null) {
			for (ChannelShopBean channelShopBean : shopList) {
				if (channelList.contains(channelShopBean.getOrder_channel_id())) {
					companyShopList.add(channelShopBean);
				}
			}
		}
		user.setCompanyShopList(companyShopList);

        // 获取用户 配置信息
        HashMap<String , ArrayList<UserConfigBean>> userConfig = getUserConfig(user.getUserId());
        user.setUserConfig(userConfig);
		// 获取用户 选择公司的所属退货类型
		ArrayList<UserConfigBean> userConfigList = userConfig.get(ConfigConstants.RETURN_TYPE);
		String companyReturnType = "";
		if(userConfigList != null) {
			for (UserConfigBean userConfigBean : userConfigList) {
				if (channelList.contains(userConfigBean.getCfg_val2())) {
					companyReturnType = userConfigBean.getCfg_val1();
				}
			}
		}
		user.setCompanyReturnType(companyReturnType);
		
		// cms 用户自定义列 us
		ArrayList<UserConfigBean> userUsProduct = userConfig.get(ConfigConstants.CMS_US_PRODUCT_ATTRIBUTES);
		String cmsUsProductAttributes="";
		if(userUsProduct != null && userUsProduct.size()>0){
			cmsUsProductAttributes = userUsProduct.get(0).getCfg_val1();
		}
		
		// cms 用户自定义列 cn
		ArrayList<UserConfigBean> userCnProduct = userConfig.get(ConfigConstants.CMS_CN_PRODUCT_ATTRIBUTES);
		String cmsCnProductAttributes="";
		if(userCnProduct != null && userCnProduct.size()>0){
			cmsCnProductAttributes = userCnProduct.get(0).getCfg_val1();
		}
		Map<String,Object> cmsProductFields = new HashMap<String, Object>();
		List<String> emptyAttribute = new ArrayList<>();
		cmsProductFields.put("cmsUsProductAttributes", cmsUsProductAttributes.isEmpty() ? emptyAttribute : cmsUsProductAttributes.split(","));
		cmsProductFields.put("cmsCnProductAttributes", cmsCnProductAttributes.isEmpty() ? emptyAttribute : cmsCnProductAttributes.split(","));
		user.setCmsProductfields(cmsProductFields);
		
        // 获取用户 移库对象
        List<ChannelStoreBean> setStoreList = getUserStoreToList(user.getUserId());
        user.setStoreToList(setStoreList);
		// 获取用户 选择公司的所属TO仓库
		List<ChannelStoreBean> companyStoreToList = new ArrayList<>();
		List<ChannelStoreBean> companyRealStoreToList = new ArrayList<>();
		if(setStoreList != null) {
			for (ChannelStoreBean channelStore : setStoreList) {
				if (channelList.contains(channelStore.getOrder_channel_id())) {
					companyStoreToList.add(channelStore);
					// 真实仓库才抽出
					if (StoreConfigEnums.Kind.REAL.getId().equals(channelStore.getStore_kind())) {
						companyRealStoreToList.add(channelStore);
					}
				}
			}
		}
		user.setCompanyStoreToList(companyStoreToList);
		user.setCompanyRealStoreToList(companyRealStoreToList);


		// 获取用户硬件信息
        List<UserHardwareConfigBean> hardwareConfig = getUserHardwareConfigList(user.getUserId());
        user.setHardwareConfig(hardwareConfig);

//        // 获取时区
//        if (userConfig.size() == 0 || userConfig.get(ConfigConstants.TIME_ZONE) == null){
//            user.setTimeZone(8); //如果没有这个属性，那么设置为默认值 东八区
//        } else {
//            user.setTimeZone(Integer.valueOf(userConfig.get(ConfigConstants.TIME_ZONE).get(0).getCfg_val1()));
//        }


		return user;
	}
    //获取用户 所有仓库
    public List<ChannelStoreBean> getUserStoreList(int userId) {
        // 权限列表
        List<ChannelStoreBean> result = new ArrayList<>();
        // 查询 ct_user_config 关联 wms_mt_store 获取仓库的 id 与 name
        result = loginDao.getUserStoreList(userId);

        return result;

    }
	//获取渠道 所有仓库
	public List<ChannelStoreBean> getChannelStoreList(List<String> channelList) {
		// 权限列表
		List<ChannelStoreBean> result = new ArrayList<>();
		// 查询 ct_user_config 关联 wms_mt_store 获取仓库的 id 与 name
		if (channelList !=null || channelList.size() > 0) {
			result = loginDao.getChannelStoreList(channelList);
		}



		return result;

	}
    //获取用户 所有移库对象
    public List<ChannelStoreBean> getUserStoreToList(int userId) {
        // 权限列表
        List<ChannelStoreBean> result = new ArrayList<ChannelStoreBean>();
        // 查询 ct_user_config 关联 wms_mt_store 获取仓库的 id 与 name
        result = loginDao.getUserStoreToList(userId);

        return result;

    }
    // 获取用户 所有店铺
    public List<ChannelShopBean> getUserShopList(int userId) {
        // 权限列表
        List<ChannelShopBean> result = new ArrayList<ChannelShopBean>();
        // 查询 ct_user_role_property 关联 tm_channel_shop 获取仓库的 id 与 name
        result = loginDao.getUserShopList(userId);

        return result;
    }

    // 获取用户 硬件配置信息
    public List<UserHardwareConfigBean> getUserHardwareConfigList(int userId) {
        // 权限列表
        List<UserHardwareConfigBean> result = new ArrayList<UserHardwareConfigBean>();
        // 查询 用户配置表
        result = loginDao.getUserHardwareConfigList(userId);

        return result;
    }

    // 获取用户 配置信息
    public HashMap<String , ArrayList<UserConfigBean>> getUserConfig(int userId) {
        // 权限列表
        HashMap<String , ArrayList<UserConfigBean>> result = new HashMap<String , ArrayList<UserConfigBean>>();
        // 查询 ct_user_config 获取所有该用户的配置信息
        // 如果一个属性有多件的话，就放入List
        // 也就是说 HASHMAP里的每一个属性，是一个集合，这个集合可能是一件，也可能是多件
        // 时区和所属仓库是常用属性，所以独立出来，但是这里也会取到
        List<UserConfigBean> ret = loginDao.getUserConfig(userId);
        // 配置项名称
        String cfgName = "";
        ArrayList<UserConfigBean> cfgVal = new ArrayList<UserConfigBean>();
		for(int i = 0;i < ret.size();i++){
			// 取得该用户的配置信息
            UserConfigBean line = ret.get(i);
			String cfgNameNew = line.getCfg_name();
			// 创建用户配置信息Bean
			UserConfigBean userConfig = new UserConfigBean();
			userConfig.setUser_id(userId);
			userConfig.setCfg_name(cfgNameNew);
			userConfig.setCfg_val1(line.getCfg_val1());
			userConfig.setCfg_val2(line.getCfg_val2());
			userConfig.setComment(line.getComment());
			// 判断是否是同一配置项
			if(cfgName.equalsIgnoreCase(cfgNameNew)){
				cfgVal.add(userConfig);
			}else{
				// 如果配置项发生变化，则先输出前配置项
				// 但是初始值除外
				if(!"".equalsIgnoreCase(cfgName)){
					result.put(cfgName, cfgVal);
				}
				cfgName = cfgNameNew;
				cfgVal = new ArrayList<UserConfigBean>();
				cfgVal.add(userConfig);
			}
		}
		// 设置最后一个配置项
		if(!"".equalsIgnoreCase(cfgName)){
			result.put(cfgName, cfgVal);
		}

        return result;
    }
    
    @Override
    public List<CompanyBean> getUserCompanyPermissionMap(String userName){
    	return loginDao.getUserCompanyPermissionMap(userName);
    }
}
