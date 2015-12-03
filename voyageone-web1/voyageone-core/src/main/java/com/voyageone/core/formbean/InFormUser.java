package com.voyageone.core.formbean;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.voyageone.core.ajax.AjaxRequestBean;

public class InFormUser extends AjaxRequestBean {
	
	/**
	 * id
	 */
	private int user_id;
	
	/**
	 * 用户名
	 */
	private String username;
	
	/**
	 * 密码
	 */
	private String password;
	
	/**
	 * 姓
	 */	
	private String first_name;
	
	/**
	 * 名
	 */	
	private String last_name;
	
	/**
	 * 邮箱
	 */	
	private String email;
	
	/**
	 * 公司id
	 */	
	private String company_id;
	/**
	 * 公司名字
	 */		
	private String company;
	/**
	 * 删除标记
	 */
	private boolean active;	
	
	private String modifier;
	
	private String timezone;
	
	/**
	 * @return the timezone
	 */
	public String getTimezone() {
		return timezone;
	}

	/**
	 * @param timezone the timezone to set
	 */
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	/**
	 * @return the modifier
	 */
	public String getModifier() {
		return modifier;
	}

	/**
	 * @param modifier the modifier to set
	 */
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	/**
	 * 是否超级管理员
	 */
	private boolean is_superuser;
	
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int userId) {
		this.user_id = userId;
	}
	
	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCompany_id() {
		return company_id;
	}

	public void setCompany_id(String company_id) {
		this.company_id = company_id;
	}

	public boolean isIs_superuser() {
		return is_superuser;
	}

	public void setIs_superuser(boolean is_superuser) {
		this.is_superuser = is_superuser;
	}

	/**
	 * @return the userName
	 */
	@NotEmpty
	public String getUsername() {
		return username;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	@NotEmpty
	@Length(min=6, max=50)
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		
		this.password = password;
	}

	@Override
	protected String[] getValidateSorts() {
		return new String[]{"username", "password"};
	}
}
