/**
 * @User: Jonas
 * @Date: 2015-4-7 10:02:33
 * @Version: 0.0.1
 */

define([
    "modules/wms/wms.module",
    "components/directives/dialogs/dialogs",
    "modules/wms/location/location.service"
], function (wms) {

    wms.controller("locationBindCtrl", [
        "$scope",
        "notify",
        "locationService",
        "userService",
        locationBindCtrl
    ]);

    function locationBindCtrl($scope, notify, locationService, user) {
        var vm = $scope.vm = {};

        // 搜索条件
        vm.search = {
            store: "",
            code: ""
        };

        // 当前的编辑条件
        vm.curr = {
            store: null,
            code: "",
            store_name: ""
        };

        // 添加时绑定的输出变量
        vm.add = {
            location: ""
        };

        // 绑定的列表数据源
        vm.itemLocations = [];

        // 绑定的日志数据源
        vm.itemLocationLogs = [];

        $scope.searchItemLocation = searchItemLocation;
        $scope.deleteItemLocation = deleteItemLocation;
        $scope.addItemLocation = addItemLocation;

        // 初始化处理：取得仓库一览、状态一览并赋予初期值
        locationService.doBindInit({},$scope).then(function(response) {

            vm.stores = response.data.storeList;

            vm.search.store =vm.stores[0].store_id;

        });

        function searchItemLocation() {

            if (!vm.search.code) {
                notify("WMS_LOCATION_CODE_UN_VALID");
                return;
            }

            // 根据条件搜索
            locationService.doSearchItemLocation(
                vm.search.code,
                vm.search.store
            ).then(
                function (res) {
                    // 搜索成功之后，将搜索的条件保存到非公开的变量上
                    vm.curr.code = res.code;
                    vm.curr.store = res.store_id;
                    vm.curr.store_name = res.storeName;

                    vm.itemLocations = res.itemLocations;
                    vm.itemLocationLogs = res.itemLocationLogs;
                });
        }

        function deleteItemLocation(item_location, index) {
            locationService.doDeleteItemLocation(
                item_location.item_location_id,
                item_location.modified
            ).then(
                function(itemLocationLog) {
                    if (itemLocationLog) {
                        vm.itemLocations.splice(index, 1);
                        vm.itemLocationLogs.unshift(itemLocationLog);
                    }
                }
            )
        }

        function addItemLocation() {
            var location_name = vm.add.location;

            vm.add.location = "";

            if (!vm.curr.code) {
                notify("WMS_LOCATION_SEARCH");
                return;
            }

            if (!location_name) {
                notify("WMS_LOCATION_NAME_UN_VALID");
                return;
            }

            locationService.doAddItemLocation(
                location_name,
                vm.curr.code,
                vm.curr.store
            ).then(
                function(res) {
                    vm.itemLocations.push(res.itemLocation);
                    vm.itemLocationLogs.unshift(res.itemLocationLog);
                }
            );
        }
    }

});