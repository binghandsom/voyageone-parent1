package com.voyageone.web2.admin;

/**
 * @author Wangtd
 * @since 2.0.0 2016/8/10
 */
public interface AdminConstants {
	
	/** 配置类型 */
	enum ConfigType {
		Channel, ChannelCart, Port, Store, Task;
	}
	
	/** 状态值 */
	interface Active {
		/** 可用 */
		int ENABLED = 1;
		/** 禁用 */
		int DESABLED = 0;
	}

}
