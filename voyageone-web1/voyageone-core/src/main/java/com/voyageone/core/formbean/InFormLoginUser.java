package com.voyageone.core.formbean;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.voyageone.core.ajax.AjaxRequestBean;

public class InFormLoginUser extends AjaxRequestBean {
	/**
	 * 用户名
	 */
	private String userName;
	
	/**
	 * 密码
	 */
	private String password;
	
	/**
	 * 时区
	 */
	private int timezone;

	
	/**
	 * @return the timezone
	 */
	public int getTimezone() {
		return timezone;
	}

	/**
	 * @param timezone the timezone to set
	 */
	public void setTimezone(int timezone) {
		this.timezone = timezone;
	}

	/**
	 * @return the userName
	 */
	@NotEmpty
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
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
		return new String[]{"userName", "password"};
	}
}
