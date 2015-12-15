package com.voyageone.core;

import java.util.Random;

public interface CoreConstants {

	String DAO_READ = "read";
	String DAO_WRITE = "write";
	
	// exception消息分隔符
	String EXCEPTION_MESSAGE_PREFIX = "; cause is ";
	// exception消息描述最大值
	int EXCEPTION_MESSAGE_LENGTH = 200;
	
	// 存放在 session 中用户信息key
	String VOYAGEONE_USER_INFO = "voyageone.userInfo";

	// voyeageone当前用户token的key
	String VOYAGEONE_USER_TOKEN = "Voyageone-User-Token";
	// voyeageone当前用户选择语言
	String VOYAGEONE_USER_LANG = "Voyageone-User-Lang";
	// voyeageone当前用户选择公司
	String VOYAGEONE_USER_COMPANY = "Voyageone-User-Company";
	
	// 随机数
	Random RANDOM = new Random();
	
	// controll的后缀
	String CONTROLLER_SUFFIX = ".html";
	
	// Ajax 请求返回值
	String AJAX_RESULT_OK = "OK";
	String AJAX_RESULT_NG = "NG";
	
	// 公共公告类型
	int ANNOUNCEMENT_PUBLIC = 0;
	
	// 密码加密固定盐值
	String MD5_FIX_SALT = "crypto.voyageone.la";
	// 密码加密散列加密次数
	int MD5_HASHITERATIONS = 4;

	/**
	 * 用户角色
	 */
	final class Role {
		// 管理员
		public final static String ADMIN = "Role_Admin";
		// 客服
		public final static String CS = "Role_CS";
		// 仓库
		public final static String WHS = "Role_WHS";
		// 运营
		public final static String OP = "Role_OP";
	}
}
