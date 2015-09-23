package com.voyageone.wsdl.core.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.voyageone.base.dao.BaseDao;
import org.springframework.stereotype.Repository;

import com.voyageone.wsdl.core.Constants;
import com.voyageone.wsdl.core.modelbean.MessageInfoBean;

@Repository
public class MessageDao extends BaseDao {
	
	/**
	 * 根据消息类型获得类型消息Map
	 * 
	 * @param messageType
	 * @return
	 */
	public Map<String, String> getMessages(int messageType) {
		List<MessageInfoBean> messageList = 
				(List) selectList(Constants.DAO_NAME_SPACE_CORE + "ct_message_info_getMessages", messageType);
		
		Map<String, String> messageMap = new HashMap<String, String>();
		
		if (messageList != null && messageList.size() > 0) {
			for (MessageInfoBean messageInfoBean : messageList) {
				messageMap.put(messageInfoBean.getMessageCode(), messageInfoBean.getMessage());
			}
		}
		
		return messageMap;
	}
	
}
