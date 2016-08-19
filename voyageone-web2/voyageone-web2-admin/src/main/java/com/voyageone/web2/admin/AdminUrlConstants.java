package com.voyageone.web2.admin;

/**
 * 系统的请求地址统一常量定义，与前端的actions.json相对应。
 * @author Wangtd
 *  @since 2.0.0 2016/8/9
 */
public interface AdminUrlConstants {
	
	interface Channel {
		
		/** 渠道信息 */
		interface Self {
			
			String ROOT = "/admin/channel/self";
			
			String SEARCH_CHANNEL_BY_PAGE = "searchChannelByPage";
			
			String ADD_CHANNEL = "addChannel";

			String UPDATE_CHANNEL = "updateChannel";
			
			String GET_ALL_CHANNEL = "getAllChannel";

			String GET_ALL_COMPANY = "getAllCompany";

			String GENERATE_SECRET_KEY = "generateSecretKey";

			String GENERATE_SESSION_KEY = "generateSessionKey";

			String DELETE_CHANNEL = "deleteChannel";

			String SEARCH_CHANNEL_CONFIG_BY_PAGE = "searchChannelConfigByPage";

			String ADD_CHANNEL_CONFIG = "addChannelConfig";

			String UPDATE_CHANNEL_CONFIG = "updateChannelConfig";

			String DELETE_CHANNEL_CONFIG = "deleteChannelConfig";
			
		}
		
		/** 渠道属性信息 */
		interface Attribute {
			
			String ROOT = "/admin/channel/attribute";
			
			String SEARCH_CHANNEL_ATTRIBUTE_BY_PAGE = "searchChannelAttributeByPage";

			String ADD_CHANNEL_ATTRIBUTE = "addChannelAttribute";

			String UPDATE_CHANNEL_ATTRIBUTE = "updateChannelAttribute";

			String DELETE_CHANNEL_ATTRIBUTE = "deleteChannelAttribute";
			
		}
		
		interface Carrier {
			
			String ROOT = "/admin/channel/carrier";
			
			String SEARCH_CARRIER_CONFIG_BY_PAGE = "searchCarrierConfigByPage";

			String ADD_CARRIER_CONFIG = "addCarrierConfig";

			String UPDATE_CARRIER_CONFIG = "updateCarrierConfig";

			String DELETE_CARRIER_CONFIG = "deleteCarrierConfig";

			String GET_ALL_CARRIER = "getAllCarrier";
			
		}
		
		/** 第三方配置信息 */
		interface ThirdParty {
			
			String ROOT = "/admin/channel/thirdParty";
			
			String SEARCH_THIRD_PARTY_CONFIG_BY_PAGE = "searchThirdPartyConfigByPage";

			String ADD_THIRD_PARTY_CONFIG = "addThirdPartyConfig";

			String UPDATE_THIRD_PARTY_CONFIG = "updateThirdPartyConfig";

			String DELETE_THIRD_PARTY_CONFIG = "deleteThirdPartyConfig";
			
		}

		/** 短信配置信息 */
		interface Sms {
			
			String ROOT = "/admin/channel/sms";

			String SEARCH_SMS_CONFIG_BY_PAGE = "searchSmsConfigByPage";
			
			String ADD_SMS_CONFIG = "addSmsConfig";

			String UPDATE_SMS_CONFIG = "updateSmsConfig";

			String DELETE_SMS_CONFIG = "deleteSmsConfig";

		}

	}
	
	interface Cart {

		/** Cart信息 */
		interface Self {
			
			String ROOT = "/admin/cart/self";
			
			String GET_ALL_CART = "getAllCart";

			String GET_CART_BY_IDS = "getCartByIds";

			String SEARCH_CART_BY_PAGE = "searchCartByPage";

			String GET_ALL_PLATFORM = "getAllPlatform";

			String ADD_CART = "addCart";

			String UPDATE_CART = "updateCart";

			String DELETE_CART = "deleteCart";
		}
		
		/** 商店信息 */
		interface Shop {
			
			String ROOT = "/admin/cart/shop";
			
			String SEARCH_CART_SHOP_BY_PAGE = "searchCartShopByPage";

			String ADD_CART_SHOP = "addCartShop";

			String UPDATE_CART_SHOP = "updateCartShop";

			String DELETE_CART_SHOP = "deleteCartShop";

			String SEARCH_CART_SHOP_CONFIG_BY_PAGE = "searchCartShopConfigByPage";

			String ADD_CART_SHOP_CONFIG = "addCartShopConfig";

			String UPDATE_CART_SHOP_CONFIG = "updateCartShopConfig";

			String DELETE_CART_SHOP_CONFIG = "deleteCartShopConfig";
			
		}
		
	}
	
	interface Store {
		
		/** 仓库信息 */
		interface Self {
			
			String ROOT = "/admin/store/self";
			
			String SEARCH_STORE_BY_PAGE = "searchStoreByPage";

			String ADD_STORE = "addStore";

			String GET_ALL_STORE = "getAllStore";

			String UPDATE_STORE = "updateStore";

			String DELETE_STORE = "deleteStore";

			String SEARCH_STORE_CONFIG_BY_PAGE = "searchStoreConfigByPage";

			String ADD_STORE_CONFIG = "addStoreConfig";

			String UPDATE_STORE_CONFIG = "updateStoreConfig";

			String DELETE_STORE_CONFIG = "deleteStoreConfig";
			
		}
	}
	
	interface Task {
		
		/** 任务信息 */
		interface Self {
			
			String ROOT = "/admin/task/self";
			
			String SEARCH_TASK_BY_PAGE = "searchTaskByPage";

			String ADD_TASK = "addTask";

			String UPDATE_TASK = "updateTask";

			String DELETE_TASK = "deleteTask";

			String SEARCH_TASK_CONFIG_BY_PAGE = "searchTaskConfigByPage";

			String DELETE_TASK_CONFIG = "deleteTaskConfig";

			String ADD_TASK_CONFIG = "addTaskConfig";
			
		}
		
	}
	
	interface System {
		
		/** 统一属性配置 */
		interface Type {
			
			String ROOT = "/admin/system/type";
			
			String SEARCH_TYPE_BY_PAGE = "searchTypeByPage";

			String ADD_TYPE = "addType";

			String UPDATE_TYPE = "updateType";

			String DELETE_TYPE = "deleteType";

			String GET_ALL_TYPE = "getAllType";
			
		}
		
	}

	interface User {

		/** 用户信息 */
		interface Self {

			String ROOT = "/admin/user/self";

			String SEARCH_USER = "searchUser";

			String INIT = "init";

			String ADD_USER = "addUser";

			String UPDATE_USER = "updateUser";

			String DELETE_USER = "deleteUser";

			String SHOW_AUTH = "showAuth";
		}

		/** 角色信息 */
		interface Role {

			String ROOT = "/admin/user/role";

			String SEARCH_ROLE = "searchRole";

			String INIT = "init";

			String ADD_ROLE = "addRole";

			String UPDATE_ROLE = "updateRole";

			String DELETE_ROLE = "deleteRole";

			String SET_AUTH = "setAuth";

			String ADD_AUTH = "addAuth";

			String REMOVE_AUTH = "removeAuth";

			//for dropdown list options
			String GET_ALL_ROLE = "getAllRole";
		}

		/** 组织信息 */
		interface Org {

			String ROOT = "/admin/user/org";

			String SEARCH_ORG = "searchOrg";

			String INIT = "init";

			String ADD_ORG = "addOrg";

			String UPDATE_ORG = "updateOrg";

			String DELETE_ORG = "deleteOrg";

			//for dropdown list options
			String GET_ALL_ORG = "getAllOrg";
		}

		/** 资源信息 */
		interface Res {

			String ROOT = "/admin/user/res";

			String SEARCH_RES = "searchRes";

			String INIT = "init";

			String ADD_RES = "addRes";

			String UPDATE_RES = "updateRes";

			String DELETE_RES = "deleteRes";

		}

	}

}
