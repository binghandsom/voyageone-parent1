package com.voyageone.oms.service;

import com.voyageone.core.ajax.AjaxResponseBean;
import com.voyageone.core.modelbean.UserSessionBean;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * OMS 订单明细检索service
 * 
 * @author jacky
 *
 */
public interface OmsOrderRateService {
	
	/**
	 * 保存账务文件
	 *
	 * @param storeId 渠道ID
	 * @param channelId cartID
	 * @param rate 汇率
	 * @param currency 币种
	 * @param rateTime 汇率日期
	 * @param user 当前用户
	 * @return
	 */
	public List<Object> saveRate(String storeId, String channelId, String rate, String currency, String rateTime, String calculationError, UserSessionBean user);
	

}
