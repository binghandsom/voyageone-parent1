package com.voyageone.oms.formbean;

import java.util.List;

import com.voyageone.core.ajax.AjaxRequestBean;

/**
 * 画面传入Customer检索条件bean
 * 
 * @author jerry
 *
 */
public class InFormAddNewOrderCustomer extends AjaxRequestBean {
	/**
	 * customer_id
	 */
	private String customerId;
	
	/**
	 * orderNumber
	 */
	private String orderNumber;
	
	/**
	 * first_name
	 */
	private String firstName;
	
	/**
	 * last_name
	 */
	private String lastName;
	
	/**
	 * company
	 */
	private String company;

	/**
	 * email
	 */
	private String email;
	
	/**
	 * city
	 */
	private String city;
	
	/**
	 * state
	 */
	private String state;
	
	/**
	 * zip
	 */
	private String zip;
	
	/**
	 * country
	 */
	private String country;
	/**
	 * order_channel_id
	 */
	private List<String> order_channel_id;
	/**
	 * channelIdSelected
	 */
	private String  channelIdSelected;
	/**
	 * phone
	 */
	private String phone;

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * @return the order_channel_id
	 */
	public List<String> getOrder_channel_id() {
		return order_channel_id;
	}

	/**
	 * @param order_channel_id the order_channel_id to set
	 */
	public void setOrder_channel_id(List<String> order_channel_id) {
		this.order_channel_id = order_channel_id;
	}
    
	public String getChannelIdSelected() {
		return channelIdSelected;
	}

	public void setChannelIdSelected(String channelIdSelected) {
		this.channelIdSelected = channelIdSelected;
	}

	@Override
	protected String[] getValidateSorts() {
		return new String[]{"noShippedDays", "customerEmail"};
	}
}
