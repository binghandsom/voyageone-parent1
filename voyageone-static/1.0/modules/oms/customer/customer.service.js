/**
 * @Name:    searchService.js
 * @Date:    2015/3/26
 *
 * @User:    sky
 * @Version: 1.0.0
 */

define(function (require) {

    var omsApp = require('modules/oms/oms.module');
    //require('components/services/ajaxService');
    require('modules/oms/common/common.service');

    omsApp.service('customerService',['$q', 'omsAction', 'ajaxService',
        function ($q, omsAction, ajaxService) {
			//客户信息初始化
			this.doInit = function (data, scope) {
		        var defer = $q.defer();
		        ajaxService.ajaxPost(data, omsAction.oms_customer_doInit, scope)
		            .then(function (response) {
		                defer.resolve(response.data);
		            });
		        return defer.promise;
		    };
    		//客户查询
    		this.doSearch = function (data, scope) {
                var defer = $q.defer();
                ajaxService.ajaxPost(data, omsAction.oms_customer_search_doSearch, scope)
                    .then(function (response) {
                        defer.resolve(response);
                    });
                return defer.promise;
            };

        }]);
    return omsApp;
});