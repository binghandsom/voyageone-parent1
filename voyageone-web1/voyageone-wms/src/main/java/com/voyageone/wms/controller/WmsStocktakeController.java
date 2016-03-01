package com.voyageone.wms.controller;

import com.voyageone.base.BaseController;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.core.ajax.AjaxResponseBean;
import com.voyageone.wms.WmsConstants;
import com.voyageone.wms.WmsUrlConstants;
import com.voyageone.wms.WmsUrlConstants.StockTakeUrls;
import com.voyageone.wms.formbean.FormStocktake;
import com.voyageone.wms.service.WmsStocktakeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.voyageone.wms.WmsConstants.ReportItems.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**   
 * Simple to Introduction  
 * @Package      [com.voyageone.wms.controller]  
 * @ClassName    [WmsStocktakeController]
 *  [盘点处理的控制类]
 * @Author       [sky]   
 * @CreateDate   [20150518]
 * @UpdateUser   [${user}]   
 * @UpdateDate   [${date} ${time}]   
 * @UpdateRemark [说明本次修改内容]  
 * @Version      [v1.0] 
 */
@Controller
@RequestMapping(method = RequestMethod.POST)
public class WmsStocktakeController extends BaseController {

	@Autowired
	private WmsStocktakeService wmsStocktakeService;

	/**
	 * @param response data
	 * @param paramMap 需要获取的下拉框控件的option名称和对应的typeId {name: typeId}
	 * 【sessionList页面】初始化，获取下拉框内容
	 * @create 20150518
	 */
	@RequestMapping(StockTakeUrls.SessionList.INIT)
	public void sessionListInit(HttpServletResponse response, @RequestBody Map<String, String> paramMap) {
		wmsStocktakeService.sessionListInit(getRequest(), response, paramMap, getUser(), getSession());
	}

	/**
	 * @param response HttpServletResponse
	 * @param object Object
	 * 【sessionList页面】检索returnList页面訂單的信息
	 * @create 20150518
	 */
	@RequestMapping(StockTakeUrls.SessionList.SEARCH)
	public void doSessionListSearch(HttpServletResponse response, @RequestBody Object object) {
		wmsStocktakeService.getStocktakeSessionList(getRequest(), response, object, getUser());
	}

	/**
	 * @param response HttpServletResponse
	 * @param object Object
	 * 【sessionList页面】新建session
	 * @create 20150519
	 */
	@RequestMapping(StockTakeUrls.SessionList.NEWSESSION)
	public void doNewSession(HttpServletResponse response, @RequestBody Object object) {
		wmsStocktakeService.newSession(getRequest(), response, object, getUser());
	}

	/**
	 * @param response HttpServletResponse
	 * @param sessionId Object
	 * 【sessionList页面】删除session
	 * @create 20150519
	 */
	@RequestMapping(StockTakeUrls.SessionList.DELETE)
	public void doSessionDelete(HttpServletResponse response, @RequestBody String sessionId) {
		wmsStocktakeService.sessionDelete(getRequest(), response, sessionId);
	}

	/**
	 * @param response HttpServletResponse
	 * @param sessionId Object
	 * 【sessionList页面】关闭session
	 * @create 20150520
	 */
	@RequestMapping(StockTakeUrls.SessionList.CLOSE)
	public void doSessionStock(HttpServletResponse response, @RequestBody String sessionId) {
		wmsStocktakeService.sessionStock(getRequest(), response, sessionId, getUser());
	}

	/**
	 * @param response HttpServletResponse
	 * @param object Object
	 * 【sectionList页面】初始化
	 * @create 20150519
	 */
	@RequestMapping(StockTakeUrls.SectionList.INIT)
	public void sectionListInit(HttpServletResponse response, @RequestBody Object object) {
		wmsStocktakeService.sectionListInit(getRequest(), response, object, getUser());
	}

	/**
	 * @param response HttpServletResponse
	 * @param object Object
	 * 【sectionList页面】newSection按钮
	 * @create 20150520
	 */
	@RequestMapping(StockTakeUrls.SectionList.NEWSECTION)
	public void doNewSection(HttpServletResponse response, @RequestBody Object object) {
		wmsStocktakeService.newSection(getRequest(), response, object, getUser());
	}

	/**
	 * @param response HttpServletResponse
	 * @param stocktake_detail_id sectionId
	 * 【sectionList页面】Delete Section
	 * @create 20150521
	 */
	@RequestMapping(StockTakeUrls.SectionList.DELETESECTION)
	public void doDeleteSection(HttpServletResponse response, @RequestBody int stocktake_detail_id) {
		wmsStocktakeService.deleteSection(getRequest(), response, stocktake_detail_id);
	}

	/**
	 * @param response HttpServletResponse
	 * @param object Object
	 * 【inventory 页面】init
	 * @create 20150521
	 */
	@RequestMapping(StockTakeUrls.Inventory.INIT)
	public void doInventoryInit(HttpServletResponse response, @RequestBody Object object) {
		wmsStocktakeService.inventoryInit(getRequest(), response, object, getUser(), getSession());
	}

	/**
	 * 【inventory 页面】upcScan
	 * @param response HttpServletResponse
	 * @param object Object
	 * @create 20150521
	 */
	@RequestMapping(StockTakeUrls.Inventory.SCAN)
	public void doUpcScan(HttpServletResponse response, @RequestBody Object object) {
		wmsStocktakeService.upcScan(getRequest(), response, object, getUser(), getSession());
	}

	/**
	 * 【compare 页面】 点击 fixed按钮将 session的状态修改为 Done
	 * @param response HttpServletResponse
	 * @param object Object
	 * @create 20150526
	 */
	@RequestMapping(StockTakeUrls.Inventory.CHECKSECTIONSTATUS)
	public void doCheckSectionStatus(HttpServletResponse response, @RequestBody Object object) {
		wmsStocktakeService.checkSectionStatus(getRequest(), response, object, getUser());
	}

	/**
	 * 【sectionDetail 页面】init
	 * @param response HttpServletResponse
	 * @param object Object
	 * @create 20150522
	 */
	@RequestMapping(StockTakeUrls.SectionDetail.INIT)
	public void doSectionDetailInit(HttpServletResponse response, @RequestBody Object object) {
		wmsStocktakeService.sectionDetailInit(getRequest(), response, object, getUser());
	}

	/**
	 * 【sectionDetail 页面】关闭section
	 * @param response HttpServletResponse
	 * @param object Object
	 * @create 20150522
	 */
	@RequestMapping(StockTakeUrls.SectionDetail.CLOSESECTION)
	public void doCloseSection(HttpServletResponse response, @RequestBody Object object) {
		wmsStocktakeService.closeSection(getRequest(), response, object, getUser());
	}

	/**
	 * 【inventory 页面】ReScan按钮，删除目前Section中所有的项
	 * @param response HttpServletResponse
	 * @param stocktake_detail_id sectionId
	 * @create 20150522
	 */
	@RequestMapping(StockTakeUrls.SectionDetail.RESCAN)
	public void doReScan(HttpServletResponse response, @RequestBody int stocktake_detail_id) {
		wmsStocktakeService.reScan(getRequest(), response, stocktake_detail_id);
	}

	/**
	 * 【sessionList 页面】列表中的按钮 “compare"，跳转至compare页面并且初始化
	 * @param response HttpServletResponse
	 * @param object Object
	 * @create 20150525
	 */
	@RequestMapping(StockTakeUrls.Compare.INIT)
	public void doCompareInit(HttpServletResponse response, @RequestBody Object object) {
		wmsStocktakeService.compareInit(getRequest(), response, object, getUser());
	}

	/**
	 * 【compare 页面】列表中的复选框状态变化出发事件
	 * @param response HttpServletResponse
	 * @param object Object
	 * @create 20150525
	 */
	@RequestMapping(StockTakeUrls.Compare.CHECKBOXVALUECHANGE)
	public void doCheckBoxValueChange(HttpServletResponse response, @RequestBody Object object) {
		wmsStocktakeService.checkBoxValueChange(getRequest(), response, object, getUser());
	}

	/**
	 * 【compare 页面】检查session状态
	 * @param response HttpServletResponse
	 * @param stocktake_id sessionId
	 * @create 20150525
	 */
	@RequestMapping(StockTakeUrls.Compare.CHECKSESSIONSTATUS)
	public void doCheckSessionStatus(HttpServletResponse response, @RequestBody int stocktake_id) {
		wmsStocktakeService.checkSessionStatus(getRequest(), response, stocktake_id);
	}

	/**
	 * 【compare 页面】 点击 fixed按钮将 session的状态修改为 Done
	 * @param response HttpServletResponse
	 * @param stocktake_id sessionId
	 * @create 20150526
	 */
	@RequestMapping(StockTakeUrls.Compare.SESSIONDONE)
	public void doSessionDone(HttpServletResponse response, @RequestBody int stocktake_id) {
		wmsStocktakeService.doSessionDone(getRequest(), response, stocktake_id, getUser());
	}

	/**
	 * 【compare 页面】盘点比对结果报表下载
	 * @param param 获取报表数据参数
	 * @create 20150810
	 */
	@RequestMapping(value = WmsUrlConstants.StockTakeUrls.Compare.COMPRESDOWNLOAD, method = RequestMethod.GET)
	public ResponseEntity<byte[]> downloadCompResReport(String param) throws IOException {
		byte[] bytes = wmsStocktakeService.downloadCompResReport(param, getUser());
		String outFile = StocktakeCompResRpt.RPT_NAME + "_" + DateTimeUtil.getNow() + WmsConstants.ReportItems.StocktakeCompResRpt.RPT_SUFFIX;
		return  genResponseEntityFromBytes(outFile, bytes);
	}

	/**
	 * 获取SKU信息
	 */
	@RequestMapping(WmsUrlConstants.StockTakeUrls.Inventory.GETSKU)
	public void getSku(@RequestBody Map<String, Object> params, HttpServletResponse response) {

		int stocktake_id = Integer.valueOf((String) params.get("stocktake_id"));
		String barcode = (String) params.get("barcode");
		String sku = (String) params.get("sku");

		FormStocktake formStocktake = new FormStocktake();
		formStocktake.setStocktake_id(stocktake_id);

		Map<String, Object> result = wmsStocktakeService.getSku(formStocktake,barcode,sku );

		AjaxResponseBean
				.newResult(true)
				.setResultInfo(result)
				.writeTo(getRequest(), response);
	}

	/**
	 * 获取SKU信息
	 */
	@RequestMapping(WmsUrlConstants.StockTakeUrls.Inventory.DELETEITEM)
	public void deleteItem(@RequestBody Map<String, Object> params, HttpServletResponse response) {

		long stocktake_id = Long.valueOf((String) params.get("stocktake_id"));
		long stocktake_detail_id = Long.valueOf((String) params.get("stocktake_detail_id"));
		String sku = (String) params.get("sku");


		Map<String, Object> result = wmsStocktakeService.deleteItem(stocktake_id,stocktake_detail_id, sku);

		AjaxResponseBean
				.newResult(true)
				.setResultInfo(result)
				.writeTo(getRequest(), response);
	}

}
