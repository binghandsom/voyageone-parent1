/**
 * @Name:    itemLocationInfoService.js
 * @Date:    2016/2/22
 * @User:    jerry
 * @Version: 1.0.0
 */

define(function (require) {

    var wmsApp = require('modules/wms/wms.module');
    require('components/services/ajax.service');
    wmsApp.service('itemLocationService',['$q', 'wmsActions', 'ajaxService',
        function ($q, wmsActions, ajaxService) {
    		this.doSearch = function (data, scope) {
                var defer = $q.defer();
                ajaxService.ajaxPost(data, wmsActions.location.bind.searchByLocationId, scope)
                    .then(function (response) {
                        defer.resolve(response);
                    });
                return defer.promise;
            };

        }]);
    return wmsApp;
});