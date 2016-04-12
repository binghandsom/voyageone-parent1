package com.voyageone.components.eexpress.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("eExpress_Login")
public class ExpressLoginReq {
	
	// 标记为节点属性  
    @XStreamAsAttribute  
    protected String xmlns = "http://tempuri.org/";  
    
	private String loginID;
	private String pwd;
	private String url;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getLoginID() {
		return loginID;
	}
	public void setLoginID(String loginID) {
		this.loginID = loginID;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	
	
}
