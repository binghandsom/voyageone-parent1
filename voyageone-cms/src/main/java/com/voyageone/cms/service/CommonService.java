package com.voyageone.cms.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.voyageone.cms.formbean.IPriceSetting;
@Service
public interface CommonService {


	
	public Map<String,Object> doGetMasterDataList(String channelId);

	public void doUpdateProductCNPriceInfo(String productId, String channelId, IPriceSetting priceSetting, String modifier) throws Exception;

}
