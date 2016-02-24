package com.voyageone.wms.service;

import com.voyageone.core.modelbean.UserSessionBean;
import com.voyageone.wms.formbean.FormReturn;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;
/**   
 * Simple to Introduction  
 * @Package      [com.voyageone.wms.service]  
 * @ClassName    [WmsReturnService]   
 * @Description  [return服务类接口]   
 * @Author       [sky]   
 * @CreateDate   [20150427]   
 * @UpdateUser   [${user}]   
 * @UpdateDate   [${date} ${time}]   
 * @UpdateRemark [说明本次修改内容]  
 * @Version      [v1.0] 
 */
public interface WmsReturnService {

//	/**
//	 * @description 点击【Refunded】按钮将received状态修改为refunded
//	 * @param request HttpServletRequest
//	 * @param response HttpServletResponse
//	 * @param string String
//	 * @return boolean
//	 */
//	void changeStatus(HttpServletRequest request, HttpServletResponse response, String string);

	/**
	 * @description 根据orderNo获取orderInfo
	 * @param orderNumber 订单号
	 * @return list
	 */
	void getOrderInfoByOrdNo(HttpServletRequest request, HttpServletResponse response, String orderNumber, UserSessionBean user);

	/**
	 * @description 将return记录插入到wms_bt_return表
	 * @param object Object
	 * @return boolean
	 */
	void insertReturnInfo(HttpServletRequest request, HttpServletResponse response, Object object, UserSessionBean user);

	/**
	 * @description 创建returnSession
	 * @param object Object
	 * @param userInfo 用户信息
	 * @return string
	 */
	void createReturnSession(HttpServletRequest request, HttpServletResponse response, Object object, UserSessionBean userInfo);

	/**
	 * @description 关闭returnSession
	 * @param sessionId returnSessionId
	 * @param userInfo 用户信息
	 */
	void closeReturnSession(HttpServletRequest request, HttpServletResponse response, String sessionId, UserSessionBean userInfo, HttpSession session);

	/**
	 * @description 保存ItemEdit信息
	 * @param object Object
	 * @param user 用户信息
	 * @return boolean
	 */
	void saveItemEdit(HttpServletRequest request, HttpServletResponse response, Object object, UserSessionBean user);

	/**
	 * @description newSession页面移除returnInfo
	 * @param resId reservationId
	 * @return boolean
	 */
	void removeReturnInfo(HttpServletRequest request, HttpServletResponse response, String resId);

	/**
	 * @description SessionList页面查询session
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param session HttpSession
	 */
	void doSessionListSearch(HttpServletRequest request, HttpServletResponse response, Object object, HttpSession session, UserSessionBean user);

	/**
	 * @description SessionList 初始化
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param session HttpSession
	 */
	void SessionListInit(HttpServletRequest request, HttpServletResponse response, Map<String, String> paramMap, HttpSession session, UserSessionBean user);

	/**
	 * @description doReturnListSearch();scription SessionDetail 初始化
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param sessionId returnSessionId
	 * @param session HttpSession
	 */
	void getSessionInfo(HttpServletRequest request, HttpServletResponse response, String sessionId, HttpSession session, UserSessionBean user);

	/**
	 * @description returnList页面查询
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param formReturn bean
	 * @param user 用户信息
	 */
	void doReturnListSearch(HttpServletRequest request, HttpServletResponse response, FormReturn formReturn, UserSessionBean user);

	/**
	 * @description newSession
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param paramMap 参数 map
	 * @param user 用户信息
	 */
	void doNewSessionInit(HttpServletRequest request, HttpServletResponse response, Map<String, String> paramMap, UserSessionBean user);

	/**
	 * @description newSessionPopUp 根据storeId获取
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param storeId 仓库Id
	 * @param user 用户信息
	 */
	void doGetReceivedFromItemByStoreId(HttpServletRequest request, HttpServletResponse response, int storeId, UserSessionBean user);

	/**
	 * @description 根据sessionId获取session的属性
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param user 用户信息
	 */
	void getReturnType(HttpServletRequest request, HttpServletResponse response, UserSessionBean user);

	/**
	 * @description Return 更新
	 * @param paramMap 参数 map
	 * @param user 用户信息
	 */
	FormReturn doChange(Map<String, Object> paramMap, UserSessionBean user);
}
