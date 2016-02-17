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

        /**
         * 请求服务器，尝试检索 Code 对应的 SKU，并添加指定数量的 Item 到 Package
         * @resolve {TransferItem}
         * @param package_id {string|number}
         * @param code {string}
         * @param num {string|number}
         * @param itemCode {string}
         * @param color {string}
         * @param size {string}
         * @returns {Promise}
         */
        this.change = function (package_id, code, num, itemCode, color, size) {
            if (!_.isNumber(package_id)) package_id = parseInt(package_id);

            if (!_.isNumber(num)) num = parseInt(num);

            return  http.ajaxPost({
                package_id: package_id.toString(),
                barcode: code,
                num: num,
                itemCode: itemCode,
                color: color,
                size: size
            }, addItem).then(function (res) {
                return res.data;
            });
        };

    };
});