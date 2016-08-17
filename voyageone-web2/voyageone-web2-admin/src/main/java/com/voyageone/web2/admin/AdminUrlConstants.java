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

			String SEARCH_CHANNEL_CONFIG = "searchChannelConfigByPage";

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
		
		/** 第三方配置信息 */
		interface ThirdParty {
			
			String ROOT = "/admin/channel/thirdParty";
			
			String SEARCH_THIRD_PARTY_CONFIG_BY_PAGE = "searchThirdPartyConfigByPage";

			String ADD_THIRD_PARTY_CONFIG = "addThirdPartyConfig";

			String UPDATE_THIRD_PARTY_CONFIG = "updateThirdPartyConfig";
			
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
	
	interface System {
		
		/** 统一属性配置 */
		interface CommonConfig {
			
			String ROOT = "/admin/system/config";
			
		}
		
	}

	interface User {

		/** 用户信息 */
		interface Self {

			String ROOT = "/admin/user/self";

			String SEARCH_USER_BY_PAGE = "searchUserByPage";

			String INIT = "init";

			String ADD_USER = "addUser";

			String UPDATE_USER = "updateUser";

			String DELETE_USER = "deleteUser";

			String SHOW_AUTH = "showAuth";
		}

	}

}
