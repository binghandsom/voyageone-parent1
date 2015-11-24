/**
 * @Name:    returnService.js
 * @Date:    2015/04/28
 * @User:    sky
 * @Version: 1.0.0
 */

define(function (require) {

    var wmsApp = require('modules/wms/wms.module');
    require('components/services/ajax.service');

    wmsApp.service('returnService',['$q', 'wmsActions', 'ajaxService',
        function ($q, wmsActions, ajaxService) {
           
    		var RETURNINFO = "selectInfo";
    		var RETURNSESSIONID = "sessionId";
    		
    		//将编辑时候选中的对象存入session
			this.setSelectInfo = function (value) {
				sessionStorage.setItem(RETURNINFO, JSON.stringify(value));
			};

			this.getSelectInfo = function () {
				return JSON.parse(sessionStorage.getItem(RETURNINFO));
			};
			
			this.setSessionId = function (value) {
				sessionStorage.setItem(RETURNSESSIONID, value);
			};

			this.getSessionId = function () {
				return sessionStorage.getItem(RETURNSESSIONID);
			};
		
    		//【returnList页面】控件初始化
			this.doInit = function (data, scope) {
				return ajaxService.ajaxPost(data, wmsActions.ordReturn.returnList.init, scope);
			};
        
    		//【returnList页面】结果集查询
    		this.doSearch = function (data, scope) {
                return ajaxService.ajaxPost(data, wmsActions.ordReturn.returnList.search, scope);
            };
            
    		//【returnList页面】點擊refunded按鈕更新return狀態
    		this.doChangeRecToRef = function (data, scope) {
                return ajaxService.ajaxPost(data, wmsActions.ordReturn.returnList.changeStatus, scope);
            };
            
			//【newSession页面】控件初始化
			this.doNewSessionInit = function (data) {
				return ajaxService.ajaxPost(data, wmsActions.ordReturn.newSession.init);
			};
			
			//【newSession页面】根据returnSessionId获取对应的session实例
			this.doGetSessionInfo = function (data, scope) {
				return ajaxService.ajaxPost(data, wmsActions.ordReturn.newSession.getSessionInfo, scope);
			};
		
			//【newSession页面】点击closeSession操作
			this.doCloseSession = function (data, scope) {
				return ajaxService.ajaxPost(data, wmsActions.ordReturn.newSession.closeReturnSession, scope);
			};
			
			//【itemedit页面】页面初始化
			this.doItemEditInit = function (data, scope) {
				return ajaxService.ajaxPost(data, wmsActions.ordReturn.itemEdit.init, scope);
			};
			
			//【itemedit页面】保存编辑信息
			this.doSaveReturnInfo = function (data, scope) {
				return ajaxService.ajaxPost(data, wmsActions.ordReturn.itemEdit.save, scope);
			};
			
			//【itemedit页面】删除return记录
			this.doRemoveReturnInfo = function (data, scope) {
				return ajaxService.ajaxPost(data, wmsActions.ordReturn.itemEdit.remove, scope);
			};
			
			//【sessionList页面】页面初始化
			this.doSessionListInit = function (data, scope) {
				return ajaxService.ajaxPost(data, wmsActions.ordReturn.sessionList.init, scope);
			};
			
			//【sessionList页面】sessionList查询
			this.doSessionListSearch = function (data, scope) {
				return ajaxService.ajaxPost(data, wmsActions.ordReturn.sessionList.search, scope);
			};
			
			//【sessionDetail页面】初始化(sessionInfo)查询
			this.doSessionDetailSearch = function (data, scope) {
				return ajaxService.ajaxPost(data, wmsActions.ordReturn.sessionDetail.init, scope);
			};
			
        }]);
    return wmsApp;
});