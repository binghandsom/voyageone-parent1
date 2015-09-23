package com.voyageone.bi.commonutils;

import java.util.List;

import com.voyageone.bi.tranbean.UserInfoBean;

public class DaoUtils {

	public static String getFirstDbName(UserInfoBean userInfoBean) {
		String dbName = "";
		List<String> userChannelDBList = userInfoBean.getUserChannelDBList();
		if (userChannelDBList.size()>0) {
			String channelDB = userChannelDBList.get(0) ;
			if (channelDB != null && !"".equals(channelDB.trim())) {
				dbName = userChannelDBList.get(0) + ".";
			}
		}
		return dbName;
	}
}
