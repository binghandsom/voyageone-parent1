package com.voyageone.web2.base.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Message (ct_message_info) 数据访问
 * Created on 11/25/15.
 * @author Jonas
 * @version 2.0
 */
@Service
public class MessageService {
    @Autowired
    private MessageDao messageDao;

    private Map<String, MessageModel> cache = new HashMap<>();

    /**
     * 获得消息
     */
    public MessageModel getMessage(String lang, String code) {

        MessageModel message = cache.get(lang + code);

        if (message == null) {
            message = messageDao.selectMessage(lang, code);
            cache.put(lang + code, message);
        }

        return message;
    }
}
