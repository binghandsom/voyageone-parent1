/**
 * Created by Tester on 5/25/2015.
 *
 * @date 2015-05-25 19:16:35
 * @version 0.0.1
 */
define([
    "modules/wms/wms.module",
    "components/services/ajax.service"
], function (wms) {

    wms.service("upcService", [
        'ajaxService',
        'wmsActions',
        upcService]);

    function upcService(http, actions) {

        this.doGetProduct = function (order_channel_id, code) {
            return http.ajaxPost({
                    order_channel_id: order_channel_id,
                    code: code
                },
                actions.upc.GET_PRODUCT)

                .then(function (res) {
                    return res.data;
                });
        };

        this.doSaveProduct = function (bean) {
            return http.ajaxPost(
                bean,
                actions.upc.SAVE_PRODUCT)

                .then(function (res) {
                    return res.data;
                });
        };

        this.doGetAllSize = function (order_channel_id, product_type_id) {
            return http.ajaxPost({
                    order_channel_id: order_channel_id,
                    product_type_id: product_type_id
                },
                actions.upc.GET_ALL_SIZE)

                .then(function (res) {
                    return res.data;
                });
        };

        this.doSaveItemDetail = function (bean) {
            return http.ajaxPost(
                bean,
                actions.upc.SAVE_ITEM_DETAIL)

                .then(function (res) {
                    return res.data;
                });
        };

        this.doGetAllProductTypes = function(order_channel_id) {
            return http.ajaxPost({
                    order_channel_id: order_channel_id
                },
                actions.upc.GET_ALL_PRODUCT_TYPE)

                .then(function (res) {
                    return res.data;
                });
        };

    }
});