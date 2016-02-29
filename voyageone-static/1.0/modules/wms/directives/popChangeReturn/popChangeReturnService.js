/**
 * @Name:    popChangeReturnService.js
 * @Date:    2016/02/24
 * @User:    jerry
 * @Version: 1.0.0
 */

define([
    "modules/wms/wms.module",
    "components/services/ajax.service"
], function (wms) {

    wms.service("popChangeReturnService", [
        'ajaxService',
        'wmsActions',
        popChangeReturnService]);

    function popChangeReturnService(http, wmsActions) {

        var popChangeReturn = wmsActions.ordReturn.popChangeReturn;

        this.change = function (returnID, changeKind, notes, store, modified) {
            return  http.ajaxPost({
                returnID : returnID.toString(),
                changeKind : changeKind.toString(),
                notes : notes
            }, popChangeReturn.change).then(function (res) {
                return res.data;
            });
        };

    };
});