package com.voyageone.oms.service;

import java.util.List;

import com.voyageone.oms.formbean.InFormServiceSearchSKU;
import com.voyageone.oms.formbean.OutFormServiceSearchSKU;

/**
 * OMS 共通service
 * 
 * @author jacky
 *
 */
public interface OmsCommonService {

	/**
	 * 获得SKU信息
	 * 
	 * @return
	 */
	public List<OutFormServiceSearchSKU> getSKUList(InFormServiceSearchSKU inFormServiceSearchSKU,String type);
	
}	

