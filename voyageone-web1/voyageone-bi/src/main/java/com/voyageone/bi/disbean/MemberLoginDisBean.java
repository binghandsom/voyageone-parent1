package com.voyageone.bi.disbean;


public class MemberLoginDisBean {
	
//	@Length(min=6,max=6)
    private String username;
	
//	@Length(min=6,max=6)
    private String password;
    
//	@Length(min=4,max=4)
    private String identifyCode;
    
    private String isRemember;
	
	public String getIsRemember() {
		return isRemember;
	}
	public void setIsRemember(String isRemember) {
		this.isRemember = isRemember;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getIdentifyCode() {
		return identifyCode;
	}
	public void setIdentifyCode(String identifyCode) {
		this.identifyCode = identifyCode;
	}
    
    
}