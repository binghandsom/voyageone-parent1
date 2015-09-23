package com.voyageone.core.init;

import java.util.HashMap;
import java.util.Map;

import com.voyageone.common.util.StringUtils;
import com.voyageone.core.modelbean.MessageInfoBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.voyageone.core.MessageConstants;
import com.voyageone.core.dao.MessageDao;

/**
 * @author jacky
 */
public class MessageHelp {

    @Autowired
    private MessageDao messageDao;

    /**
     * 所有消息记录 区别与老的获取方式
     */
    private static Map<String, MessageInfoBean> MSG_EXCEPTION_MAP_ALL = new HashMap<>();

    /**
     * 总的消息MAP
     */
    public static Map<Integer, Map<String, String>> MESSAGE_MAP = new HashMap<>();

    /**
     * 消息MAP初始化
     */
    public void initMessagesMap() {
        /*
         * 输入参数校验失败
         */
        Map<String, String> MSG_VALIDATE_MAP = messageDao.getMessages(MessageConstants.MESSAGE_TYPE_VALIDATE);
        MESSAGE_MAP.put(MessageConstants.MESSAGE_TYPE_VALIDATE, MSG_VALIDATE_MAP);
		
		/*
         * 业务异常
         */
        Map<String, String> MSG_BUSSINESS_EXCEPTION_MAP = messageDao.getMessages(MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
        MESSAGE_MAP.put(MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION, MSG_BUSSINESS_EXCEPTION_MAP);
		
		/*
         * session过期
         */
        Map<String, String> MSG_SESSION_TIMEOUT_MAP = messageDao.getMessages(MessageConstants.MESSAGE_TYPE_SESSION_TIMEOUT);
        MESSAGE_MAP.put(MessageConstants.MESSAGE_TYPE_SESSION_TIMEOUT, MSG_SESSION_TIMEOUT_MAP);
		
		/*
         * 弹出框（URL无权限或token不一致）
         */
        Map<String, String> MSG_DIALOG_MAP = messageDao.getMessages(MessageConstants.MESSAGE_TYPE_DIALOG);
        MESSAGE_MAP.put(MessageConstants.MESSAGE_TYPE_DIALOG, MSG_DIALOG_MAP);
		
		/*
         * 不可恢复异常（系统异常、其他）
         */
        Map<String, String> MSG_EXCEPTION_MAP = messageDao.getMessages(MessageConstants.MESSAGE_TYPE_EXCEPTION);
        MESSAGE_MAP.put(MessageConstants.MESSAGE_TYPE_EXCEPTION, MSG_EXCEPTION_MAP);

        //新的Msg获取对象
        MSG_EXCEPTION_MAP_ALL = messageDao.getMessages();

    }

    /**
     * 获得消息
     */
    public static String getMessage(int msgType, String msgCode) {
        String msg = MessageHelp.MESSAGE_MAP.get(msgType).get(msgCode);
        if (StringUtils.isNullOrBlank2(msg)) {
            msg = "";
        }
        return msg;
    }

    /**
     * 获得消息
     */
    public static MessageInfoBean getMsgObjectByLang(String lang, String msgCode) {

        String key = msgCode + "-" + lang;

        return MessageHelp.MSG_EXCEPTION_MAP_ALL.get(key);
    }

    /**
     * 获得消息
     */
    public static String getMsgByLang(String lang, String msgCode) {

        String key = msgCode + "-" + lang;

        String msg = MessageHelp.MSG_EXCEPTION_MAP_ALL.get(key).getMessage();

        if (StringUtils.isNullOrBlank2(msg)) {
            msg = "";
        }

        return msg;
    }

    /**
     * 获得消息
     */
    public static MessageInfoBean getMsgByLang(String key) {

        return MessageHelp.MSG_EXCEPTION_MAP_ALL.get(key);
    }
}
