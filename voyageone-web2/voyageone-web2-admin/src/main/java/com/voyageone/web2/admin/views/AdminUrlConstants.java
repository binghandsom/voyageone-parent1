package com.voyageone.web2.admin.views;

/**
 * 系统的请求地址统一常量定义，与前端的actions.json相对应。
 * @author Wangtd
 *  @since 2.0.0 2016/8/9
 */
public interface AdminUrlConstants {
	
	interface Channel {
		
		String ROOT = "/admin/channel";
		
		/** 渠道信息 */
		interface Self {
			
			String ROOT = Channel.ROOT + "/self";
			
			String SEARCH_CHANNEL = "searchChannel";
			
			String ADD_OR_UPDATE_CHANNEL = "addOrUpdateChannel";
			
		}
		
	}
	
	interface System {
		
		String ROOT = "/admin/system";
		
		/** 统一属性配置 */
		interface CommonConfig {
			
			String ROOT = System.ROOT + "/config";
			
			String SEARCH_CONFIG = "searchConfig";
			
		}
	}

}
