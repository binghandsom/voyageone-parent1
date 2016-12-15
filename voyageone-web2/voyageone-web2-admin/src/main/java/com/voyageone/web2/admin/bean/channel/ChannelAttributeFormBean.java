package com.voyageone.web2.admin.bean.channel;

import com.voyageone.web2.admin.bean.AdminFormBean;

/**
 * @author Wangtd
 * @since 2.0.0 2016/8/16
 */
public class ChannelAttributeFormBean extends AdminFormBean {
	
	private Integer id;
	
	private Integer typeId;
	
	private String channelId;
	
	private String value;
	
	private String name;
	
	private String addName1;
	
	private String addName2;
	
	private String langId;
	
	private Integer displayOrder;
	
	private Boolean active;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddName1() {
		return addName1;
	}

	public void setAddName1(String addName1) {
		this.addName1 = addName1;
	}

	public String getAddName2() {
		return addName2;
	}

	public void setAddName2(String addName2) {
		this.addName2 = addName2;
	}

	public String getLangId() {
		return langId;
	}

	public void setLangId(String langId) {
		this.langId = langId;
	}

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

}
