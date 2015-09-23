package com.voyageone.cms.modelbean;

import java.util.Date;

public class CnCategory  {
	
	private int categoryId;
	
	private String channelId;
	
    private String cnName;

    private String cnHeaderTitle;

    private boolean cnIsEnableFilter;
	
	private boolean cnIsVisibleOnMenu;
	
	private boolean cnIsPublished; 

    private String created;

    private String creater;

    private String  modified;

    private String modifier;

    
  
	public int getCategoryId() {
		return categoryId;
	}

	
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	
	public String getChannelId() {
		return channelId;
	}

	
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getCnName() {
        return cnName;
    }

    public void setCnName(String cnName) {
        this.cnName = cnName == null ? null : cnName.trim();
    }

    public String getCnHeaderTitle() {
        return cnHeaderTitle;
    }

    public void setCnHeaderTitle(String cnHeaderTitle) {
        this.cnHeaderTitle = cnHeaderTitle == null ? null : cnHeaderTitle.trim();
    }

    public boolean isCnIsEnableFilter() {
		return cnIsEnableFilter;
	}


	public void setCnIsEnableFilter(boolean cnIsEnableFilter) {
		this.cnIsEnableFilter = cnIsEnableFilter;
	}


	public boolean isCnIsVisibleOnMenu() {
		return cnIsVisibleOnMenu;
	}


	public void setCnIsVisibleOnMenu(boolean cnIsVisibleOnMenu) {
		this.cnIsVisibleOnMenu = cnIsVisibleOnMenu;
	}


	public boolean isCnIsPublished() {
		return cnIsPublished;
	}


	public void setCnIsPublished(boolean cnIsPublished) {
		this.cnIsPublished = cnIsPublished;
	}

    public String getCreated() {
		return created;
	}


	public void setCreated(String created) {
		this.created = created;
	}


	public String getCreater() {
		return creater;
	}


	public void setCreater(String creater) {
		this.creater = creater;
	}


	public String getModified() {
		return modified;
	}


	public void setModified(String modified) {
		this.modified = modified;
	}


	public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier == null ? null : modifier.trim();
    }
}