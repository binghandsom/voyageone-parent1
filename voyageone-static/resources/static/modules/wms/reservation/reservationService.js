/**
 * @Name:    reservationService.js
 * @Date:    2015/04/22
 * @User:    sky
 * @Version: 1.0.0
 */

define(function (require) {

    var omsApp = require('modules/wms/wms.module');
    require('components/services/ajax.service');

    omsApp.service('reservationService',['$q', 'wmsActions', 'ajaxService',
        function ($q, wmsActions, ajaxService) {
           
			//页面控件初始化
			this.doInit = function (data, scope) {
				var defer = $q.defer();
				ajaxService.ajaxPost(data, wmsActions.reservation.reservationList.init, scope)
                	.then(function (response) {
                		defer.resolve(response);
                	});
				return defer.promise;
			};
        
    		//reservationList查询
    		this.doSearch = function (data, scope) {
                var defer = $q.defer();
                ajaxService.ajaxPost(data, wmsActions.reservation.reservationList.search, scope)
                    .then(function (response) {
                        defer.resolve(response);
                    });
                return defer.promise;
            };

        }]);
    return omsApp;
});