package com.voyageone.common.mail;

import java.util.List;

import com.voyageone.base.exception.SystemException;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.Codes;
import com.voyageone.common.util.CommonUtil;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.StringUtils;

import javax.mail.MessagingException;

/**
 * 邮件发送类
 * <p>
 * Created by Jonas on 4/16/2015.
 *
 * @author Jonas
 * @version 0.0.1
 */
public final class Mail {
    /**
     * 发送一个异常的警告邮件
     *
     * @param receiverName 对应 Code 表中邮件接收人的键
     * @param subject      邮件标题
     * @param e            目标异常
     * @throws MessagingException
     */
    public static void sendAlert(String receiverName, String subject, Exception e) throws MessagingException {
        sendAlert(receiverName, subject, CommonUtil.getExceptionContent(e), false);
    }

    /**
     * 发送一个警告邮件
     *
     * @param receiverName 对应 Code 表中邮件接收人的键
     * @param subject      邮件标题
     * @param content      邮件内容
     * @throws MessagingException
     */
    public static void sendAlert(String receiverName, String subject, String content) throws MessagingException {

        sendAlert(receiverName, subject, content, false);
    }

    /**
     * 发送一个警告邮件
     *
     * @param receiverName 对应 Code 表中邮件接收人的键
     * @param subject      邮件标题
     * @param content      邮件内容
     * @param priority     重要度 TRUE 重要  FALSE 一般
     * @throws MessagingException
     */
    public static void sendAlert(String receiverName, String subject, String content, boolean priority) throws MessagingException {
        sendAlert(receiverName, subject, content, null, priority);
    }

    /**
     * 发送一个警告邮件
     *
     * @param receiverName 对应 Code 表中邮件接收人的键
     * @param subject      邮件标题
     * @param content      邮件内容
     * @param priority     重要度 TRUE 重要  FALSE 一般
     * @throws MessagingException
     */
    public static void sendAlert(String receiverName, String subject, String content, List<String> fileAffix, boolean priority) throws MessagingException {
        String subjectTmp = Constants.MAIL.SUBJECT_REQUIRED + subject;

        String receiver = getReceiver(receiverName);

        send(receiver, subjectTmp, content, fileAffix, priority);
    }

    /**
     * 发送一封邮件
     *
     * @param receiver 接收人
     * @param subject  邮件标题
     * @param content  邮件内容
     * @throws MessagingException
     */
    public static void send(String receiver, String subject, String content) throws MessagingException {
        send(receiver, subject, content, false);
    }

    /**
     * 发送一封邮件
     *
     * @param receiver 接收人
     * @param subject  邮件标题
     * @param content  邮件内容
     * @param priority 重要度 TRUE 重要  FALSE 一般
     * @throws MessagingException
     */
    public static void send(String receiver, String subject, String content, boolean priority) throws MessagingException {
        send(receiver, subject, content, null, priority);
    }
    
    /**
     * 发送一封邮件
     *
     * @param receiverName 接收人配置名
     * @param subject  邮件标题
     * @param content  邮件内容
     * @throws MessagingException
     */
    public static void send2(String receiverName, String subject, String content) throws MessagingException {
    	String receiver = getReceiver(receiverName);
    	
        send(receiver, subject, content, false);
    }

    public static void send(String receiver, String subject, String content, List<String> fileAffix, boolean priority) throws MessagingException {
        MailInfo mail = new MailInfo(receiver);

        mail.setSubject(Constants.MAIL.SUBJECT_PREFIX + subject + DateTimeUtil.getNow());
        mail.setContent(content);
        mail.setPriority(priority);
        mail.setFileAffix(fileAffix);

        mail.send();
    }

    /**
     * 根据 Name 从 Code 中获取一个包含收件人的字符串
     *
     * @param receiverName 对应 tm_code 表 EMAIL_RECEIVER 下的内容
     * @return 一个包含收件人的字符串
     */
    private static String getReceiver(String receiverName) {
        String receiver = Codes.getCodeName(Constants.MAIL.EMAIL_RECEIVER, receiverName);

        if (StringUtils.isEmpty(receiver)) {
            String msg = "邮件发送时 => 没有找到 CodeBean，ID：" + Constants.MAIL.EMAIL_RECEIVER + "，CODE：" + receiverName;
            throw new SystemException(msg);
        }

        return receiver;
    }
}
