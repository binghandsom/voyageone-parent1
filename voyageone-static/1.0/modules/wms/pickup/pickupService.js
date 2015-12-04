/**
 * @author Jack
 * @date 2015-05-20 11:44:59
 * @version 0.0.1
 */

define([
    "modules/wms/wms.module",
    "components/services/ajax.service"
], function (wms) {

    wms.service("pickupService", [
        'ajaxService',
        'wmsActions',
        pickupService]);

    function pickupService(http, wmsActions) {

        var pickup = wmsActions.pickup;

        /**
         * 页面初始化
         */
        this.doInit = function (data, scope) {
            return http.ajaxPost(data, pickup.list.init, scope);
        };

        /**
         * 根据条件搜索 pickup
         * @param orderNumber orderNumber
         * @param reservationID reservationID
         * @param sku SKU
         * @param status 物品状态
         * @param store 仓库
         * @param from 开始日期
         * @param to 结束日期
         * @param page 当前页
         * @param size 行数
         */
        this.searchPickup = function (orderNumber, reservationID, sku, status, store, from, to, page, size) {
            return  http.ajaxPost({
                order_number : orderNumber,
                id: reservationID,
                sku: sku,
                status: status,
                store_id: store,
                from: from,
                to: to,
                page: page,
                size: size
            }, pickup.list.search).then(function (res) {
                return res.data;
            });
        };

        /**
         * 根据输入的内容搜索
         * @param scanNo 扫描输入的值
         * @param scanMode 扫描模式
         * @param scanType 扫描类型
         * @param scanTypeName 扫描类型名
         * @param scanStatus 扫描状态
         * @param scanPort 扫描港口
         */
        this.scanPickup = function (scanNo, scanMode, scanType, scanTypeName, scanStatus, scanPort, scanStore ) {
            return  http.ajaxPost({
                scanNo : scanNo.toString(),
                scanMode : scanMode,
                scanType : scanType,
                scanTypeName : scanTypeName,
                scanStatus : scanStatus,
                scanPort : scanPort,
                scanStore: scanStore.toString()
            }, pickup.list.scan).then(function (res) {
                return res;
            });
        };

    };
});