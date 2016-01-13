/**
 * @Name:    popInventoryService.js
 * @Date:    2015/06/02
 * @User:    sky
 * @Version: 1.0.0
 */

define([
    "modules/wms/wms.module",
    "components/services/ajax.service"
], function (wms) {

    wms.service("popInputClientSkuService", [
        'ajaxService',
        'wmsActions',
        popInputClientSkuService]);

    function popInputClientSkuService(http, wmsActions) {

        var addItem = wmsActions.transfer.package.item.add;

        //this.change = function (reservationID, changeKind, notes, store, modified) {
        //    return  http.ajaxPost({
        //        reservationID : reservationID.toString(),
        //        changeKind : changeKind.toString(),
        //        notes : notes,
        //        store : store.toString(),
        //        modified : modified
        //    }, popChangeReservation.change).then(function (res) {
        //        return res.data;
        //    });
        //};

        /**
         * 请求服务器，尝试检索 Code 对应的 SKU，并添加指定数量的 Item 到 Package
         * @resolve {TransferItem}
         * @param package_id {string|number}
         * @param code {string}
         * @param num {string|number}
         * @returns {Promise}
         */
        this.change = function (package_id, code, num, itemCode, size) {
            if (!_.isNumber(package_id)) package_id = parseInt(package_id);

            if (!_.isNumber(num)) num = parseInt(num);

            return  http.ajaxPost({
                package_id: package_id,
                barcode: code,
                num: num,
                itemCode: itemCode,
                size: size
            }, addItem).then(function (res) {
                return res.data;
            });
        };

    };
});