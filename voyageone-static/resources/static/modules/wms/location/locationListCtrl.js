/**
 * 货架管理
 *
 * @User: Jonas
 * @Date: 2015-4-3 17:04:39
 * @Version: 0.0.2
 */

define([
    "modules/wms/wms.module",
    "../directives/popInventoryInfo/popInventoryInfo",
    "components/directives/dialogs/dialogs",
    "modules/wms/location/location.service",
    "components/services/printService",
    "components/directives/paging"
], function (wms) {

    wms.controller("locationListCtrl", [
        "$scope",
        "vConfirm",
        "userService",
        "locationService",
        "printService",
        "ngDialog",
        "notify",
        "$location",
        "wmsConstant",
        locationListCtrl
    ]);

    function locationListCtrl(scope, confirm, user, locationService, printService, ngDialog, notify, $location, wmsConstant) {
        var vm = scope.vm = {};

        vm.search = {
            name: "",
            store: ""
        };

        vm.paging = {
            curr: 1,
            size: 20,
            total: 1,
            fetch: reqSearchLocation
        };

        vm.add = {
            name: "",
            store: "",
            store_name: ""
        };

        vm.locations = [];

        // 检索货架
        scope.searchLocation = searchLocation;
        // 删除货架
        scope.deleteLocation = deleteLocation;
        // 打印货架
        scope.printLocation = printLocation;
        // 增加货架
        scope.addLocation = addLocation;
        // 绑定货架
        scope.bindLocation = bindLocation;

        // 初始化处理：取得仓库一览、状态一览并赋予初期值
        locationService.doListInit({},scope).then(function(response) {

            vm.stores = response.data.storeList;

            vm.search.store =vm.stores[0].store_id;

            vm.addStores = response.data.addStoreList;
            if (vm.addStores.length == 1 ) {
                vm.add.store =vm.addStores[0].store_id;
                vm.add.store_name =vm.addStores[0].store_name;
            }

            // 根据初始化条件进行查询
            searchLocation();
        });

        function searchLocation() {
            vm.paging.curr = 1;
            reqSearchLocation();
        }

        function deleteLocation() {
            var index = vm.locations.selected;

            var location = vm.locations[index];

            if (!location) {
                notify("WMS_LOCATION_NOT_FOUND");
                return;
            }

            confirm("WMS_LOCATION_DEL").then(function() {
                // if sure
                reqDeleteLocation(location);
            });
        }

        function printLocation() {

            var index = vm.locations.selected;

            var location = vm.locations[index];

            reqPrintLocation(location.store_name,location.location_name);

        }

        function addLocation() {
            ngDialog.openConfirm({
                template: "location.add.html",
                scope: scope
            }).then(reqAddLocation);
        }

        function bindLocation() {
            $location.path("/wms/location/bind");
        }

        function reqSearchLocation() {
            locationService.doSearchLocation(
                vm.search.name,
                vm.search.store,
                vm.paging.curr,
                vm.paging.size
            )
                .then(function (res) {
                    vm.locations = res.data;
                    vm.paging.total = res.count;
                });
        }

        function reqPrintLocation(store_name, location_name) {

            var data = [{"store" : store_name,
                "location" : location_name}];
            var jsonData = JSON.stringify(data);

            printService.doPrint(wmsConstant.print.business.Location, wmsConstant.print.hardware_key.Print_Location, jsonData);
        }

        function reqAddLocation() {
            var store = vm.add.store;
            var name = vm.add.name;
            vm.add.name = "";

            if (!store) {
                notify("WMS_LOCATION_ADD_FAIL");
                return;
            }

            if (!name) {
                notify("WMS_LOCATION_NAME_UN_VALID");
                return;
            }

            locationService.doAddLocation(
                name,
                vm.add.store
            ).then(
                function () {
                    notify({message:"WMS_NOTIFY_OP_SUCCESS", status:"success"});

                    reqPrintLocation(vm.add.store_name, name);

                    searchLocation();
                });
        }

        function reqDeleteLocation(location) {
            locationService.doDeleteLocation(
                location.location_id,
                location.store_id,
                location.modified
            ).then(
                function() {
                    notify({message:"WMS_NOTIFY_OP_SUCCESS", status:"success"});

                    searchLocation();
                }
            )
        }

    }

});