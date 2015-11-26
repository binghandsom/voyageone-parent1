/**
 * @Name:    popSkuHisListService.js
 * @Date:    2015/06/03
 * @User:    sky
 * @Version: 1.0.0
 */

define(function (require) {

    var wmsApp = require('modules/wms/wms.module');
    require('components/services/ajax.service');
    wmsApp.service('popSkuHisListService',['$q', 'wmsActions', 'ajaxService',
        function ($q, wmsActions, ajaxService) {

            this.doSearch = function (data, scope) {
                return ajaxService.ajaxPost(data, wmsActions.reservation.popSkuHisList.search, scope);
            };

            this.doInit = function (data, scope) {
                return ajaxService.ajaxPost(data, wmsActions.reservation.popSkuHisList.init, scope);
            };
        }]);
    return wmsApp;
});