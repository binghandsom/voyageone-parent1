package com.voyageone.web2.admin.bean.channel;

import com.voyageone.web2.admin.bean.AdminFormBean;

/**
 * @author Wangtd
 * @since 2.0.0 2016/8/15
 */
public class SmsConfigFormBean extends AdminFormBean {
	
	private Integer seq;
	
	private String orderChannelId;
	
	private String smsType;
	
	private String smsCode;
	
	private String smsCode1;
	
	private String smsCode2;
	
	private String content;
	
	private String describe;
	
	private String delFlag;
	
	private Boolean active;

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public String getOrderChannelId() {
		return orderChannelId;
	}

	public void setOrderChannelId(String orderChannelId) {
		this.orderChannelId = orderChannelId;
	}

	public String getSmsType() {
		return smsType;
	}

	public void setSmsType(String smsType) {
		this.smsType = smsType;
	}
	
	public String getSmsCode() {
		return smsCode;
	}

	public void setSmsCode(String smsCode) {
		this.smsCode = smsCode;
	}

	public String getSmsCode1() {
		return smsCode1;
	}

	public void setSmsCode1(String smsCode1) {
		this.smsCode1 = smsCode1;
	}

	public String getSmsCode2() {
		return smsCode2;
	}

	public void setSmsCode2(String smsCode2) {
		this.smsCode2 = smsCode2;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
	
}
