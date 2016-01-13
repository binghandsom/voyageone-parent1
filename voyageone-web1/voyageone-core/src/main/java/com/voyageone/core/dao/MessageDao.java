package com.voyageone.core.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.common.Constants;
import com.voyageone.core.modelbean.MessageInfoBean;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class MessageDao extends BaseDao {
	
	/**
	 * 根据消息类型获得类型消息Map
	 */
	public Map<String, String> getMessages(int messageType) {
		List<MessageInfoBean> messageList = 
				(List) selectList(Constants.DAO_NAME_SPACE_CORE + "ct_message_info_getMessages", messageType);
		
		Map<String, String> messageMap = new HashMap<>();
		
		if (messageList != null && messageList.size() > 0) {
			for (MessageInfoBean messageInfoBean : messageList) {
				messageMap.put(messageInfoBean.getMessageCode(), messageInfoBean.getMessage());
			}
		}
		
		return messageMap;
	}

    /**
     * 部分类型获取所有错误信息
     */
    public Map<String, MessageInfoBean> getMessages() {
        List<MessageInfoBean> messageList =
                (List) selectList(Constants.DAO_NAME_SPACE_CORE + "ct_message_info_getMsg");

        Map<String, MessageInfoBean> messageMap = new HashMap<>();

        if (messageList != null && messageList.size() > 0) {
            for (MessageInfoBean messageInfoBean : messageList) {
                messageMap.put(messageInfoBean.getMessageCode(), messageInfoBean);
            }
        }

        return messageMap;
    }
}
