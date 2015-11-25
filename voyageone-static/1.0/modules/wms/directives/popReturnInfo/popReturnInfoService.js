/**
 * @Name:    reservationLogService.js
 * @Date:    2015/4/21
 * @User:    sky
 * @Version: 1.0.0
 */

define(function (require) {

    var wmsApp = require('modules/wms/wms.module');
    require('components/services/ajax.service');
    wmsApp.service('popReturnInfoService',['$q', 'wmsActions', 'ajaxService',
        function ($q, wmsActions, ajaxService) {
    	
			this.insertReturnInfo = function (data, scope) {
				return ajaxService.ajaxPost(data, wmsActions.ordReturn.newSession.insertReturnInfo, scope);
			};
			
			this.createReturnSession = function (data, scope){
				return ajaxService.ajaxPost(data, wmsActions.ordReturn.newSession.createReturnSession, scope);
			};
    
    		this.getOrderInfoByOrdNo = function (data, scope) {
                return ajaxService.ajaxPost(data, wmsActions.ordReturn.newSession.getOrderInfoByOrdNo, scope);
            };

            this.getReceivedFromItemByStoreId = function (data, scope) {
                return ajaxService.ajaxPost(data, wmsActions.ordReturn.newSession.getReceivedFromItemByStoreId, scope);
            };

            this.getReturnType = function(data, scope){
                return ajaxService.ajaxPost(data, wmsActions.ordReturn.newSession.getReturnType, scope);
            }
        }]);
    return wmsApp;
});