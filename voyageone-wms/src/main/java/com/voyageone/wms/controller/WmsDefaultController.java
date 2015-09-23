package com.voyageone.wms.controller;

import com.voyageone.core.ajax.AjaxResponseBean;
import com.voyageone.wms.WmsUrlConstants;
import com.voyageone.wms.service.WmsDefaultService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * WMS默认画面
 * 
 * @author jack
 *
 */
@Controller
@RequestMapping(value = WmsUrlConstants.URL_WMS_DEFAULT_INDEX)
public class WmsDefaultController {
	/**
	 * 日志
	 */
	private static Log logger = LogFactory.getLog(WmsDefaultController.class);

    @Autowired
    private WmsDefaultService wmsDefaultService;

	/**
	 * 初始化（获得欢迎信息）
	 * 
	 * @param response
	 */
	@RequestMapping(value = "/doInit", method = RequestMethod.POST)
	public void doInit(HttpServletRequest request, HttpServletResponse response) {
		
		AjaxResponseBean responseBean = new AjaxResponseBean();
		// 设置返回结果
		responseBean.setResult(true);
		Map<String, String> wmsInfoMap = new HashMap<String, String>();

        // 入出库数
        int transCount = wmsDefaultService.getTransferCount();

        wmsInfoMap.put("transCount", String.valueOf(transCount));
		responseBean.setResultInfo(wmsInfoMap);

		// 结果返回输出流
		responseBean.writeTo(request, response);
		
		// 输出结果出力
		logger.info(responseBean.toString());
		
		return;
	}
	
}
