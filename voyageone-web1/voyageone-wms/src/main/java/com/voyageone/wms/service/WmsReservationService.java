package com.voyageone.wms.service;

import java.util.List;
import java.util.Map;

import com.voyageone.core.modelbean.UserSessionBean;
import com.voyageone.wms.formbean.FormReservation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Simple to Introduction  
 * @Package      [com.voyageone.wms.service]  
 * @ClassName    [WmsReservationService]   
 * @Description  [reservation接口]   
 * @Author       [sky]   
 * @CreateDate   [20150421]   
 * @UpdateUser   [${user}]   
 * @UpdateDate   [${date} ${time}]   
 * @UpdateRemark [说明本次修改内容]  
 * @Version      [v1.0] 
 */
public interface WmsReservationService {

	/**
	 *获取reservationLog
	 */
	void getReservationLogList(FormReservation formReservation, UserSessionBean user, HttpServletResponse response);
	
	/**
	 *获取reservationList
	 */
	void getReservationList(HttpServletRequest request, HttpServletResponse response, FormReservation formReservation, UserSessionBean user);
	
	/**
	 *【reservationList页面】 初始化
	 */
	void doReservationListInit(HttpServletRequest request, HttpServletResponse response, Map<String, String> paramMap, UserSessionBean user);

	/**
	 *【popInventory页面】 初始化
	 */
	void popInventoryInit(HttpServletRequest request, HttpServletResponse response, Object object, UserSessionBean user);

	/**
	 *【popInventory页面】 获取库存分配
	 */
	void getInventoryInfo(HttpServletRequest request, HttpServletResponse response, Object object, UserSessionBean user);

	/**
	 *【Inventory页面】 根据条件删除逻辑库存，重新同步库存至各平台
	 */
	void resetInventoryInfo(Map<String, Object> paramMap, HttpServletResponse response);

	/**
	 *【popSkuHisList页面】 获取库存变更详情
	 */
	void getSkuHisList(HttpServletRequest request, HttpServletResponse response, Object object, UserSessionBean user);

	/**
	 * 【reservationList页面】 更新状态
	 * @param paramMap 参数
	 * @param user 用户登录信息
	 * @return FormReservation 物品记录
	 */
	FormReservation doPopChangeReservationChange(Map<String, Object> paramMap, UserSessionBean user);

	/**
	 * 【reservationList页面】 Reservation画面初期化
	 * @param paramMap 参数
	 * @return Map 画面初期化内容
	 */
	Map<String, Object> popChangeReservationInit(Map<String, Object> paramMap);

	/**
	 * 【reservationList页面】 SKUHIT画面初始化
	 * @param user 登陆用户信息
	 */
	void initSkuHisList(HttpServletRequest request, HttpServletResponse response, UserSessionBean user);
}
