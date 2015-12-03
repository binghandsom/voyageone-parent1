package com.voyageone.wms.controller;

import com.voyageone.base.BaseController;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.wms.WmsConstants.ReportItems;
import com.voyageone.wms.WmsUrlConstants.ReportUrls;
import com.voyageone.wms.service.WmsReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Simple to Introduction  
 * @Package      [com.voyageone.wms.controller]
 * @ClassName    [WmsReportController]
 *  [报告管理处理的控制类]
 * @Author       [sky]   
 * @CreateDate   [20150720]
 * @UpdateUser   [${user}]   
 * @UpdateDate   [${date} ${time}]   
 * @UpdateRemark [说明本次修改内容]
 * @Version      [v1.0] 
 */
@Controller
@RequestMapping(method = RequestMethod.POST)
public class WmsReportController extends BaseController {

	@Autowired
	private WmsReportService wmsReportService;

	/**
	 * 库存变更详情报表初始化
	 */
	@RequestMapping(ReportUrls.INIT)
	public void doInit(HttpServletResponse response) {
		wmsReportService.init(getRequest(), response, getUser());
	}

	/**
	 * 库存变更详情报表下载
	 */
	@RequestMapping(value = ReportUrls.DOWNLOAD, method = RequestMethod.GET)
	public ResponseEntity<byte[]> downloadInvDelReport(String param) throws IOException {
		byte[] bytes = wmsReportService.downloadInvDelReport(param, getUser());
		String outFile = ReportItems.InvDelRpt.RPT_NAME + "_" + DateTimeUtil.getNow() + ReportItems.InvDelRpt.RPT_SUFFIX;
		return  genResponseEntityFromBytes(outFile, bytes);
	}
}
