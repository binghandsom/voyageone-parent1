package com.voyageone.task2.vms.service;

import com.voyageone.service.bean.com.MessageBean;
import com.voyageone.service.daoext.com.MessageDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Message (ct_message_info) 数据访问
 * Created on 16/06/29.
 * @author jeff.duan
 * @version 1.0
 */
@Service
public class MessageService {
    @Autowired
    private MessageDao messageDao;

    private Map<String, MessageBean> cache = new HashMap<>();

    /**
     * 获得消息
     */
    public MessageBean getMessage(String lang, String code) {

        MessageBean message = cache.get(lang + code);

        if (message == null) {
            message = messageDao.selectMessage(lang, code);
            cache.put(lang + code, message);
        }

        return message;
    }
}
