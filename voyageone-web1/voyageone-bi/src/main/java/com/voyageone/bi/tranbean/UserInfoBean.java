package com.voyageone.bi.tranbean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.voyageone.bi.bean.UserMenuBean;

public class UserInfoBean implements Serializable {
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -1384413166219084539L;
	
	private String uid;
	private String email;
	private String pwd;
	private String company;
	private String companyName;
	private String phone;
	private String user_kind;
	
	private List<UserShopBean> userShopList;
	private List<String> userChannelDBList;
	private Map<String, List<UserMenuBean>> userMenuMap;
	
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getUser_kind() {
		return user_kind;
	}
	public void setUser_kind(String user_kind) {
		this.user_kind = user_kind;
	}
	
	public List<UserShopBean> getUserShopList() {
		return userShopList;
	}
	public void setUserShopList(List<UserShopBean> userShopList) {
		this.userShopList = userShopList;
	}
	
	public List<String> getUserChannelDBList() {
		return userChannelDBList;
	}
	public void setUserChannelDBList(List<String> userChannelDBList) {
		this.userChannelDBList = userChannelDBList;
	}

	public Map<String, List<UserMenuBean>> getUserMenuMap() {
		return userMenuMap;
	}
	public void setUserMenuMap(Map<String, List<UserMenuBean>> userMenuMap) {
		this.userMenuMap = userMenuMap;
	}

}