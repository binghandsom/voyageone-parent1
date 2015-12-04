/**
 * @author sky
 * @date 2015-05-28
 * @version 0.0.2
 */

define([
    "modules/wms/wms.module",
    "components/services/ajax.service"
], function (wms) {
    wms.service("backorderService", [
        'ajaxService',
        'wmsActions',
        function (ajaxService, wmsActions) {

            //获取backorderInfo
            this.doGetBackorderInfo = function (data, scope) {
                return ajaxService.ajaxPost(data, wmsActions.backOrder.search, scope);
            };

            //
            this.doDelRow = function (data, scope) {
                return ajaxService.ajaxPost(data, wmsActions.backOrder.delete, scope);
            };

            //添加sku到backorder
            this.doAddBackorderInfo = function (data, scope) {
                return ajaxService.ajaxPost(data, wmsActions.backOrder.add, scope);
            };

            //添加sku页面初始化
            this.doPopInit = function (data, scope) {
                return ajaxService.ajaxPost(data, wmsActions.backOrder.popInit, scope);
            };

            //backorderList页面初始化
            this.doListInit = function (data, scope) {
                return ajaxService.ajaxPost(data, wmsActions.backOrder.listInit, scope);
            };

        }]);
});