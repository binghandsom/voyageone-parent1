/**
 * 
 */
package com.voyageone.core.modelbean;

/**
 * @author jacky
 *
 */
public class MessageInfoBean {

	/**
	 * 消息code
	 */
	private String messageCode;
	/**
	 * 消息内容
	 */
	private String message;
	/**
	 * 消息类型
	 */
	private int messageType;
	
	/**
	 * @return the messageCode
	 */
	public String getMessageCode() {
		return messageCode;
	}
	/**
	 * @param messageCode the messageCode to set
	 */
	public void setMessageCode(String messageCode) {
		this.messageCode = messageCode;
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	/**
	 * @return the messageType
	 */
	public int getMessageType() {
		return messageType;
	}
	/**
	 * @param messageType the messageType to set
	 */
	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}
	
}
