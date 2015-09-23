package com.voyageone.wsdl.core;

public interface Constants {
	public static final String SCOPE_PROTOTYPE = "prototype";
	
	// exception消息分隔符
	public static final String EXCEPTION_MESSAGE_PREFIX = "; cause is ";
	// exception消息描述最大值
	public static final int EXCEPTION_MESSAGE_LENGTH = 200;
	
	public static final String DAO_NAME_SPACE_CORE = "com.voyageone.wsdl.core.sql.";
	public static final String DAO_NAME_SPACE_OMS = "com.voyageone.wsdl.oms.sql.";
	
	// 空字符串
	public static final String EMPTY_STR = "";
	// 空格
	public static final String BLANK_STR = " ";
	
	public static final String TIME_STAMP_CHECK = "timeStamp";
	
	public static final String SIGNATURE_CHECK = "signature";
	
	public static final String MESSAGE_ACCESS_DENIED = "非法访问";
}
