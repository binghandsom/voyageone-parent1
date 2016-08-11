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
			
			String SEARCH_CHANNEL = "searchChannel";
			
			String ADD_OR_UPDATE_CHANNEL = "addOrUpdateChannel";
			
			String GET_ALL_CHANNEL = "getAllChannel";

			String GET_ALL_COMPANY = "getAllCompany";
			
		}
		
	}
	
	interface Cart {

		/** Cart信息 */
		interface Self {
			
			String ROOT = "/admin/cart/self";
			
			String GET_ALL_CART = "getAllCart";

			String GET_CART_BY_IDS = "getCartByIds";
		}
		
	}
	
	interface System {
		
		/** 统一属性配置 */
		interface CommonConfig {
			
			String ROOT = "/admin/system/config";
			
			String SEARCH_CONFIG = "searchConfig";
			
		}
		
	}

}
