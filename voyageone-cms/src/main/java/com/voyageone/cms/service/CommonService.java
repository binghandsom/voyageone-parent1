package com.voyageone.cms.service;

import java.util.List;
import java.util.Map;

import com.voyageone.common.configs.beans.ImsCategoryBean;
import org.springframework.stereotype.Service;

import com.voyageone.cms.formbean.IPriceSetting;
@Service
public interface CommonService {


	
	public Map<String,Object> doGetMasterDataList(String channelId);

	public void doUpdateProductCNPriceInfo(String productId, String channelId, IPriceSetting priceSetting, String modifier) throws Exception;

	public List<ImsCategoryBean> getChannelCategories(String channelId);

}
