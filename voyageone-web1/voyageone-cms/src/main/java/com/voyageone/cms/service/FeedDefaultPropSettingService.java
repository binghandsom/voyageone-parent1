package com.voyageone.cms.service;

import java.util.List;

import com.voyageone.cms.formbean.FeedDefaultPropBean;
import com.voyageone.core.modelbean.UserSessionBean;

public interface FeedDefaultPropSettingService {
	
	public List<FeedDefaultPropBean> getFeedProps(String channelId);
	
	public int submit(List<FeedDefaultPropBean> feedDefaultPropBeans,UserSessionBean userSession);
	
	public boolean isUpdate();
}
