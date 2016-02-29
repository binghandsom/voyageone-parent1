package com.voyageone.wms.service;

import com.taobao.api.domain.User;
import com.voyageone.core.modelbean.UserSessionBean;
import com.voyageone.wms.formbean.FormStocktake;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Simple to Introduction  
 * @Package      [com.voyageone.wms.service]  
 * @ClassName    [WmsStocktakeService]
 * @Description  [return服务类接口]   
 * @Author       [sky]   
 * @CreateDate   [20150518]
 * @UpdateUser   [${user}]   
 * @UpdateDate   [${date} ${time}]   
 * @UpdateRemark [说明本次修改内容]  
 * @Version      [v1.0] 
 */
public interface WmsStocktakeService {

	/**
	 * @description 【stocktakeList页面】初始化
	 * @param request;
	 * @param response HttpServletResponse
	 * @param paramMap 参数
	 * @param user 用户信息
	 */
	void sessionListInit(HttpServletRequest request, HttpServletResponse response, Map<String, String> paramMap, UserSessionBean user, HttpSession session);

	/**
	 * @description 【stocktakeList页面】获取列表结果集
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param object 参数对象
	 * @param user 用户信息
	 */
	void getStocktakeSessionList(HttpServletRequest request, HttpServletResponse response, Object object, UserSessionBean user);

	/**
	 * @description 【stocktakeList页面】newSession
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param object 参数对象
	 * @param user 用户信息
	 */
	void newSession(HttpServletRequest request, HttpServletResponse response, Object object, UserSessionBean user);

	/**
	 * @description 【sectionList 页面】初始化
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param object 参数对象
	 * @param user 用户信息
	 */
	void sectionListInit(HttpServletRequest request, HttpServletResponse response, Object object, UserSessionBean user);

	/**
	 * @description 【stocktakeList 页面】 删除Session
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param sessionId stocktakeId
	 */
	void sessionDelete(HttpServletRequest request, HttpServletResponse response, String sessionId);

	/**
	 * @description 【stocktakeList 页面】将session的状态由processing修改为Stock
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param sessionId stocktakeId
	 */
	void sessionStock(HttpServletRequest request, HttpServletResponse response, String sessionId, UserSessionBean user);

	/**
	 * @description 【sectionList 页面】newSection
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param object 参数对象
	 * @param user 用户信息
	 */
	void newSection(HttpServletRequest request, HttpServletResponse response, Object object, UserSessionBean user);

	/**
	 * @description 【sectionList 页面】deleteSection
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param stocktake_detail_id sectionId
	 */
	void deleteSection(HttpServletRequest request, HttpServletResponse response, int stocktake_detail_id);

	/**
	 * @description 【sectionList 页面】初始化
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param object 参数对象
	 * @param user 用户信息
	 * @param session HttpSession
	 */
	void inventoryInit(HttpServletRequest request, HttpServletResponse response, Object object, UserSessionBean user, HttpSession session);

	/**
	 * @description 【sectionList 页面】upc(barcode)扫描
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param object 参数对象
	 * @param user 用户信息
	 * @param session HttpSession
	 */
	void upcScan(HttpServletRequest request, HttpServletResponse response, Object object, UserSessionBean user, HttpSession session);

	/**
	 * @description 【sectionDetail 页面】初始化
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param object 参数对象
	 * @param user 用户信息
	 */
	void sectionDetailInit(HttpServletRequest request, HttpServletResponse response, Object object, UserSessionBean user);

	/**
	 * @description 【sectionDetail 页面】关闭section
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param object 参数对象
	 * @param user 用户信息
	 */
	void closeSection(HttpServletRequest request, HttpServletResponse response, Object object, UserSessionBean user);

	/**
	 * @description 【sectionDetail 页面】关闭section
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param stocktake_detail_id sectionId
	 */
	void reScan(HttpServletRequest request, HttpServletResponse response, int stocktake_detail_id);

	/**
	 * @description 【compare 页面】初始化
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param object 参数对象
	 */
	void compareInit(HttpServletRequest request, HttpServletResponse response, Object object, UserSessionBean user);

	/**
	 * @description 【compare 页面】复选框状态变化
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param object 参数对象
	 * @param user 用户信息
	 */
	void checkBoxValueChange(HttpServletRequest request, HttpServletResponse response, Object object, UserSessionBean user);

	/**
	 * @description 【compare 页面】检查session状态控制按钮是否禁用
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param stocktake_id sessionId
	 */
	void checkSessionStatus(HttpServletRequest request, HttpServletResponse response, int stocktake_id);

	/**
	 * @description 【compare 页面】 点击fixed按钮将状态修改为 Done
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param stocktake_id sesssionId
	 *  @param user 用户信息
	 */
	void doSessionDone(HttpServletRequest request, HttpServletResponse response, int stocktake_id, UserSessionBean user);

	/**
	 * @description 【inventory 页面】 根据section状态判断按钮是否禁用
	 * @param request HtrvletRequest
	 * @param response HttpServletResponse
	 * @param object 参数对象
	 * @param user 用户信息
	 */
	void checkSectionStatus(HttpServletRequest request, HttpServletResponse response, Object object, UserSessionBean user);

	/**
	 * @description 【compaer 页面】 比对结果报告修改
	 * @param param 参数
	 * @param user 用户信息
	 */
	byte[] downloadCompResReport(String param, UserSessionBean user);

	/**
	 * @description 【inventory 页面】 获取SKU信息
	 */
	Map<String, Object> getSku(FormStocktake formStocktake, String barcode, String sku);

	Map<String,Object> deleteItem(long stocktake_id, long stocktake_detail_id, String sku);
}
