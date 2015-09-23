/**
 * @Name:    popInventoryService.js
 * @Date:    2015/06/02
 * @User:    sky
 * @Version: 1.0.0
 */

define(function (require) {

    var wmsApp = require('modules/wms/wms.module');
    require('components/services/ajax.service');
    wmsApp.service('popInventoryService',['$q', 'wmsActions', 'ajaxService',
        function ($q, wmsActions, ajaxService) {

            this.popInit = function (data, scope) {
                return ajaxService.ajaxPost(data, wmsActions.reservation.popInventory.init, scope);
            };

            this.doSearch = function (data, scope) {
                return ajaxService.ajaxPost(data, wmsActions.reservation.popInventory.search, scope);
            };

            this.doReset = function (order_channel_id,sku) {
                return ajaxService.ajaxPost({order_channel_id:order_channel_id,sku:sku}, wmsActions.reservation.popInventory.reset);
            };
        }]);
    return wmsApp;
});