/**
 * @User: sky
 * @Date: 2015-06-16
 * @Version: 0.0.1
 */

define([
    "modules/wms/wms.module",
    "modules/wms/directives/popInventoryInfo/popInventoryService",
    "components/directives/dialogs/dialogs",
    "modules/wms/directives/popSkuHisList/popSkuHisList"
], function (wms) {

    wms.controller("inventoryCtrl", [
        "$scope",
        "$modal",
        "popInventoryService",
        "vConfirm",
        "notify",
        "vAlert",
        "$window",
        inventoryCtrl
    ]);

    function inventoryCtrl($scope, $modal, popInventoryService, confirm, notify, alert, $window) {

        //页面变量Map
        var vm = $scope.vm = {
            page: 1,   //分頁用
            rows: 20 //分頁用pageSize
        };

        var cb = $scope.cb = {};

        //点击查询按钮
        $scope.doSearch = doSearch;

        $scope.init = init;

        $scope.doDownload = doDownload;

        //同步对应行的库存
        $scope.syncRow = syncRow;

        //分頁結構體
        $scope.pageOption = {
            curr: 1,
            total: 1,
            size: 20,
            fetch: reqSearch
        };

        function doSearch(page) {
            reqSearch($scope.pageOption.curr = page);
        }

        //初始化
        function init() {
            popInventoryService.popInit(vm).then(function (response) {
                cb.channel = response.data.channel;
                if (response.data.channel.length > 0) {
                    vm.order_channel_id = response.data.channel[0].propertyId;
                }
                doSearch(1);
            });
        }

        function reqSearch(page) {
            $scope.vm.page = page;
            popInventoryService.doSearch(vm).then(function (response) {
                $scope.storeList = response.data.storeList;
                $scope.inventoryList = response.data.inventoryList;
                $scope.pageOption.total = response.data.total;
            })
        }

        function stringToObject(reservationInfo) {
            if (!_.isObject(reservationInfo)) {
                return JSON.parse(reservationInfo)
            } else {
                return reservationInfo;
            }
        }

        function syncRow() {
            confirm("WMS_INVENTORY_SYNC").then(function () {
                var order_channel_id = $scope.inventoryList.selected.order_channel_id;
                var sku = $scope.inventoryList.selected.sku;
                popInventoryService.doReset(order_channel_id, sku).then(function (response) {
                    if (response.data.successFlg) {
                        notify.success("WMS_NOTIFY_OP_SUCCESS");
                    }
                });
            });
        }

        function doDownload() {
            $window.open("./wms/reservation/popInventory/doPopInventoryDownload?param="+ JSON.stringify($scope.vm));
        }
    }
});