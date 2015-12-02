package com.voyageone.wms.controller;

import com.voyageone.common.util.JsonUtil;
import com.voyageone.core.ajax.AjaxResponseBean;
import com.voyageone.base.BaseController;
import com.voyageone.wms.WmsUrlConstants.ReservationUrls;
import com.voyageone.wms.formbean.FormReservation;
import com.voyageone.wms.service.WmsReservationService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Simple to Introduction  
 * @Package      [com.voyageone.wms.controller]
 * @ClassName    [WmsReservationController]
 *  [ReservationLog 控制类]   
 * @Author       [sky]
 * @CreateDate   [20150421]
 * @UpdateUser   [${user}]   
 * @UpdateDate   [20150529]
 * @UpdateRemark [说明本次修改内容]
 * @Version      [v1.0]
 */

@Controller
@RequestMapping(method = RequestMethod.POST)
public class WmsReservationController extends BaseController{

	@Autowired
	private WmsReservationService wmsReservationService;
	private static Log logger = LogFactory.getLog(WmsReservationController.class);

	/**
	 * 检索reservationLog信息
	 * @param response HttpServletResponse
	 * @param obj 参数Map
	 * @create 20150421
	 */
	@RequestMapping(ReservationUrls.ReservationLog.SEARCH)
	public void doReservationLogSearch(HttpServletResponse response, @RequestBody Object obj) {
		FormReservation formReservation = JsonUtil.jsonToBean(JsonUtil.getJsonString(obj), FormReservation.class);
		wmsReservationService.getReservationLogList(formReservation, getUser(), response);
	}

	/**
	 * 检索reservation信息
	 * @param response HttpServletResponse
	 * @param formReservation bean
	 * @create 20150423
	 */
	@RequestMapping(ReservationUrls.ReservationList.SEARCH)
	public void doReservationListSearch(HttpServletResponse response, @RequestBody FormReservation formReservation) {
		wmsReservationService.getReservationList(getRequest(), response, formReservation, getUser());
	}

	/**
	 * 初始化页面，获取下拉框内容
	 * @param response 返回数据
	 * @param paramMap 参数Map
	 * @create 20150424
	 */
	@RequestMapping(ReservationUrls.ReservationList.INIT)
	public void doInit(HttpServletResponse response, @RequestBody Map<String,String> paramMap) {
		wmsReservationService.doReservationListInit(getRequest(), response, paramMap, getUser());
	}

	/**
	 * popInventory 初始化
	 * @param response HttpServletResponse
	 * @param object bean
	 * @create 20150602
	 */
	@RequestMapping(ReservationUrls.PopInventory.INIT)
	public void doPopInventoryInit(HttpServletResponse response, @RequestBody Object object) {
		wmsReservationService.popInventoryInit(getRequest(), response, object, getUser());
	}

	/**
	 * popInventory 根据itemCode和orderChannelId获取对应产品的库存分配
	 * @param response HttpServletResponse
	 * @param object bean
	 * @create 20150602
	 */
	@RequestMapping(ReservationUrls.PopInventory.SEARCH)
	public void doPopInventorySearch(HttpServletResponse response, @RequestBody Object object) {
		wmsReservationService.getInventoryInfo(getRequest(), response, object, getUser());
	}

	/**
	 * 根据条件删除逻辑库存，重新同步库存至各平台
	 *
	 * @param paramMap   id/status
	 * @param response data
	 */
	@RequestMapping(ReservationUrls.PopInventory.RESET)
	public void doPopInventoryReset(@RequestBody Map<String, Object> paramMap, HttpServletResponse response) {
		wmsReservationService.resetInventoryInfo(paramMap, response);
	}

	/**
	 * popSkuHis页面，初始化
	 * @param response HttpServletResponse
	 * @create 20150603
	 */
	@RequestMapping(ReservationUrls.PopSkuHisList.INIT)
	public void doPopSkuHisInit(HttpServletResponse response) {
		wmsReservationService.initSkuHisList(getRequest(), response, getUser());
	}

	/**
	 * popSkuHis页面，根据起止日期和sku查询对应的库存变化详情
	 * @param response HttpServletResponse
	 * @param object bean
	 * @create 20150603
	 */
	@RequestMapping(ReservationUrls.PopSkuHisList.SEARCH)
	public void doPopSkuHisSearch(HttpServletResponse response, @RequestBody Object object) {
		wmsReservationService.getSkuHisList(getRequest(), response, object, getUser());
	}

	/**
	 * popChangeReservation 初始化
	 * @param response HttpServletResponse
	 * @param paramMap bean
	 * @create 20150604
	 */
	@RequestMapping(ReservationUrls.PopChangeReservation.INIT)
	public void doPopChangeReservationInit(HttpServletResponse response, @RequestBody Map<String, Object> paramMap) {

		Map<String, Object> resultMap = wmsReservationService.popChangeReservationInit(paramMap);

		// 设置返回画面的值
		AjaxResponseBean
				.newResult(true)
				.setResultInfo(resultMap)
				.writeTo(getRequest(), response);

	}

	/**
	 * 更新状态
	 *
	 * @param paramMap   id/status
	 * @param response data
	 */
	@RequestMapping(ReservationUrls.PopChangeReservation.CHANGE)
	public void doPopChangeReservationChange(@RequestBody Map<String, Object> paramMap, HttpServletResponse response) {

		// 根据检索条件取得抽出记录
		FormReservation formReservation = wmsReservationService.doPopChangeReservationChange(paramMap, getUser());

		// 设置返回画面的值
		JsonObj result = new JsonObj()
				.add("data", formReservation);

		AjaxResponseBean
				.newResult(true)
				.setResultInfo(result)
				.writeTo(getRequest(), response);
	}
}
