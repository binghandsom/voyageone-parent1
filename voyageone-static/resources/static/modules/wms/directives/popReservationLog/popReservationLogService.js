/**
 * @Name:    reservationLogService.js
 * @Date:    2015/4/21
 * @User:    sky
 * @Version: 1.0.0
 */

define(function (require) {

    var wmsApp = require('modules/wms/wms.module');
    require('components/services/ajax.service');
    wmsApp.service('reservationLogService',['$q', 'wmsActions', 'ajaxService',
        function ($q, wmsActions, ajaxService) {
    		this.doSearch = function (data, scope) {
                var defer = $q.defer();
                ajaxService.ajaxPost(data, wmsActions.reservation.reservationLog.search, scope)
                    .then(function (response) {
                        defer.resolve(response);
                    });
                return defer.promise;
            };

        }]);
    return wmsApp;
});