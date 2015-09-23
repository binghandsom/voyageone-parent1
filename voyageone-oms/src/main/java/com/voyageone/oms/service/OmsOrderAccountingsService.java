package com.voyageone.oms.service;

import com.voyageone.core.ajax.AjaxResponseBean;
import com.voyageone.core.modelbean.UserSessionBean;
import com.voyageone.oms.formbean.*;
import com.voyageone.oms.modelbean.CustomerBean;
import com.voyageone.oms.modelbean.OrderDetailsBean;
import com.voyageone.oms.modelbean.OrdersBean;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * OMS 订单明细检索service
 * 
 * @author jacky
 *
 */
public interface OmsOrderAccountingsService {
	
	/**
	 * 保存账务文件
	 *
	 * @param file 结算文件
	 * @param user 当前用户
	 * @return
	 */
	public void saveSettlementFile(MultipartFile file, AjaxResponseBean result, UserSessionBean user);
	

}
