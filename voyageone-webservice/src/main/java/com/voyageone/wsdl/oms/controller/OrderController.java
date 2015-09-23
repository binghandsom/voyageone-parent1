package com.voyageone.wsdl.oms.controller;

import java.util.List;
import java.util.Map;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.voyageone.wsdl.core.Constants;
import com.voyageone.wsdl.core.response.WsdlResponseBean;
import com.voyageone.wsdl.core.util.JsonUtil;
import com.voyageone.wsdl.core.util.Verification;
import com.voyageone.wsdl.oms.service.OrderService;

@Scope(Constants.SCOPE_PROTOTYPE)
@Controller
@Path("/order")
public class OrderController {
	private Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	private OrderService orderService;
	
	@Path("/newOrderInfoImport")
	@POST
	public String newOrderInfoImport(String param) {
		logger.debug(param);
		logger.debug("newOrderInfoImport is start");
		
		// 设置返回数据
		WsdlResponseBean responseBean = new WsdlResponseBean();
		
		// 接受json字符串参数转换成MAP
		Map<String, Object> map = JsonUtil.jsonToMap(param);
		
		// 安全校验
		String timeStamp = String.valueOf(map.get(Constants.TIME_STAMP_CHECK));
		String signature = String.valueOf(map.get(Constants.SIGNATURE_CHECK));
		if (Verification.isVerification4IdCard(timeStamp, signature)) {
			// 插入订单json信息至中间表
			List<String> successOrderList = orderService.newOrderInfoImport(map);
			
			responseBean.setResult(true);
			responseBean.setResultInfo(successOrderList);
			
		// 校验失败
		} else {
			logger.info("校验失败");
			
			responseBean.setResult(false);
			responseBean.setMessage(Constants.MESSAGE_ACCESS_DENIED);
		}
		
		logger.debug("newOrderInfoImport is end");
		
		return responseBean.toString();
	}
	
	@Path("/changedOrderInfoImport")
	@POST
	public String changedOrderInfoImport(String param) {
		logger.debug(param);
		logger.debug("changedOrderInfoImport is start");
		
		// 设置返回数据
		WsdlResponseBean responseBean = new WsdlResponseBean();
		
		// 接受json字符串参数转换成MAP
		Map<String, Object> map = JsonUtil.jsonToMap(param);
		
		// 安全校验
		String timeStamp = String.valueOf(map.get(Constants.TIME_STAMP_CHECK));
		String signature = String.valueOf(map.get(Constants.SIGNATURE_CHECK));
		if (Verification.isVerification4IdCard(timeStamp, signature)) {
			// 插入订单json信息至中间表
			List<String> successOrderList = orderService.changedOrderInfoImport(map);
			
			responseBean.setResult(true);
			responseBean.setResultInfo(successOrderList);
			
		// 校验失败
		} else {
			logger.info("校验失败");
			
			responseBean.setResult(false);
			responseBean.setMessage(Constants.MESSAGE_ACCESS_DENIED);
		}
		
		logger.debug("changedOrderInfoImport is end");
		
		return responseBean.toString();
		
	}
}
