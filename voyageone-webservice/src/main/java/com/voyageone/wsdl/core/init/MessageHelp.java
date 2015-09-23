/**
 * 
 */
package com.voyageone.wsdl.core.init;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.voyageone.wsdl.core.dao.MessageDao;
import com.voyageone.wsdl.core.util.StringUtils;

/**
 * @author jacky
 *
 */
public class MessageHelp {
	
	@Autowired
	private MessageDao messageDao;

	
	/**
	 * 总的消息MAP
	 */
	public static Map<Integer, Map<String , String>> MESSAGE_MAP = new HashMap<Integer, Map<String , String>>();
	
	/**
	 * 消息MAP初始化
	 */
	public void initMessagesMap() {
		
	}
	
	/**
	 * 获得消息
	 * 
	 * @param msgType
	 * @param msgCode
	 * @return
	 */
	public static String getMessage(int msgType, String msgCode) {
		String msg = MessageHelp.MESSAGE_MAP.get(msgType).get(msgCode);
		if (StringUtils.isNullOrBlank2(msg)) {
			msg = "";
		}
		return msg;
	}
}
