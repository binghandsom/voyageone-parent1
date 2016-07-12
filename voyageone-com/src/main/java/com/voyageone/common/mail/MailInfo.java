package com.voyageone.common.mail;

import com.voyageone.common.Constants;
import com.voyageone.common.configs.Codes;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;

import java.util.List;
import java.util.Properties;

/**
 * 邮件发送的实体类
 * Created by Jonas on 4/16/2015.
 *
 * @author Jonas
 * @version 0.0.1
 */
public final class MailInfo {
    private static final String contentType = "text/html;charset=GB2312";

    private static final String meta = "<meta http-equiv=Context-Type context=text/html;charset=gb2312>";

    private static final String key_host = "mail.smtp.host";
    private static final String key_timeout = "mail.smtp.timeout";
    private static final String key_auth = "mail.smtp.auth";

    private static Properties properties;

    private static String host;

    private static String password;

    private static String userName;

    private String from;

    private String receiver;

    private String subject;

    private String content;
    
    private boolean priority;

    private List<String> fileAffix;


	/**
	 * @return the priority
	 */
	public boolean isPriority() {
		return priority;
	}

	/**
	 * @param priority the priority to set
	 */
	public void setPriority(boolean priority) {
		this.priority = priority;
	}

	private Properties getProperties() {
        if (properties == null) {
            properties = System.getProperties();
        }
        return properties;
    }

    public MailInfo(String receiver) {
        if (host == null)
            host = Codes.getCodeName(Constants.MAIL.EMAIL_CONFIG, Constants.MAIL.SMTP_HOST);

        if (password == null)
            password = Codes.getCodeName(Constants.MAIL.EMAIL_CONFIG, Constants.MAIL.SENDER_PWD);

        if (userName == null)
            userName = Codes.getCodeName(Constants.MAIL.EMAIL_CONFIG, Constants.MAIL.SENDER_ACCOUNT);

        if (from == null)
            from = Codes.getCodeName(Constants.MAIL.EMAIL_CONFIG, Constants.MAIL.SENDER_ACCOUNT);

        this.receiver = receiver;

        getProperties().put(key_host, host);
        getProperties().put(key_timeout, "10000");
        getProperties().put(key_auth, "true");
    }

    /**
     * 邮件标题
     *
     * @param subject 邮件标题
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * 邮件内容
     *
     * @param content 邮件内容
     */
    public void setContent(String content) {
        this.content = content;
    }

    private InternetAddress[] getReceivers() throws AddressException {
        String[] receiverArr = receiver.split(";");
        InternetAddress[] addresses = new InternetAddress[receiverArr.length];

        for (int i = 0; i < receiverArr.length; i++) {
            addresses[i] = new InternetAddress(receiverArr[i]);
        }

        return addresses;
    }

    /**
     * 发送邮件
     *
     * @throws MessagingException
     */
    public void send() throws MessagingException {
        // 创建邮件内容
        BodyPart body = new MimeBodyPart();
        body.setContent(meta + content, contentType);

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(body);
        //添加附件
		if (fileAffix != null) {
			for (String file : fileAffix) {
				BodyPart bp = new MimeBodyPart();
				FileDataSource fileds = new FileDataSource(file);
				bp.setDataHandler(new DataHandler(fileds));
				bp.setFileName(fileds.getName());
				multipart.addBodyPart(bp);
			}
		}
        // 根据配置创建 Session
        Session session = Session.getInstance(getProperties(), null);

        // 创建邮件主体
        MimeMessage mimeMsg = new MimeMessage(session);
        
        // 设置重点重要度
		if (isPriority()) {
			mimeMsg.setHeader("X-Priority", "1");
		}

        // From
        mimeMsg.setFrom(new InternetAddress(from));

        // To
        mimeMsg.setRecipients(Message.RecipientType.TO, getReceivers());

        // Title
        mimeMsg.setSubject(subject);

        // Content
        mimeMsg.setContent(multipart);

        // 保存
        mimeMsg.saveChanges();

        // 发送
        Transport transport = session.getTransport("smtp");
        transport.connect(userName, password);
        transport.sendMessage(mimeMsg, mimeMsg.getRecipients(Message.RecipientType.TO));
        transport.close();
    }

	/**
	 * @return the fileAffix
	 */
	public List<String> getFileAffix() {
		return fileAffix;
	}

	/**
	 * @param fileAffix the fileAffix to set
	 */
	public void setFileAffix(List<String> fileAffix) {
		this.fileAffix = fileAffix;
	}
    
    

}
