package com.voyageone.wms.controller;

import com.voyageone.base.BaseController;
import com.voyageone.wms.WmsUrlConstants.BackOrderUrls;
import com.voyageone.wms.service.WmsBackorderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;

/**
 * Simple to Introduction  
 * @Package      [com.voyageone.wms.controller]
 * @ClassName    [WmsBackorderController]
 *  [超卖管理处理的控制类]
 * @Author       [sky]   
 * @CreateDate   [20150528]
 * @UpdateUser   [${user}]   
 * @UpdateDate   [${date} ${time}]   
 * @UpdateRemark [说明本次修改内容]
 * @Version      [v1.0] 
 */
@Controller
@RequestMapping(method = RequestMethod.POST)
public class WmsBackorderController extends BaseController {

	@Autowired
	private WmsBackorderService wmsBackorderService;

	/**
	 * 【backOrderList页面】获取backOrderList
	 * @param response 返还数据
	 * @param object 页面输入的参数
	 * @create 20150528
	 */
	@RequestMapping(BackOrderUrls.SEARCH)
	public void doGetBackOrderInfoList(HttpServletResponse response, @RequestBody Object object) {
		wmsBackorderService.getBackorderInfoList(getRequest(), response, object, getUser());
	}

	/**
	 * 【backOrderList页面】添加backOrder
	 * @param response 返还数据
	 * @param object 页面输入的参数
	 * @create 20150528
	 */
	@RequestMapping(BackOrderUrls.ADD)
	public void doAddBackOrderInfo(HttpServletResponse response, @RequestBody Object object) {
		wmsBackorderService.addBackorderInfo(getRequest(), response, object, getUser());
	}

	/**
	 * 【backOrderList页面】逻辑删除backOrderInfo
	 * @param response 返还数据
	 * @param object 页面输入的参数
	 * @create 20150528
	 */
	@RequestMapping(BackOrderUrls.DELETE)
	public void doDelBackOrderInfo(HttpServletResponse response, @RequestBody Object object) {
		wmsBackorderService.delBackorderInfo(getRequest(), response, object, getUser());
	}

	/**
	 * 【backorderList 弹出框页面】点击add a item to backorder按钮弹出框页面初始化
	 * @param response 返还数据
	 * @create 20150608
	 */
	@RequestMapping(BackOrderUrls.POPINIT)
	public void doPopInit(HttpServletResponse response) {
		wmsBackorderService.popInit(getRequest(), response, getUser());
	}

	/**
	 * 【backOrderList 弹出框页面】点击add a item to backOrder按钮弹出框页面初始化
	 * @param response 返还数据
	 * @create 20150608
	 */
	@RequestMapping(BackOrderUrls.LISTINIT)
	public void doListInit(HttpServletResponse response) {
		wmsBackorderService.listInit(getRequest(), response, getUser());
	}

}
