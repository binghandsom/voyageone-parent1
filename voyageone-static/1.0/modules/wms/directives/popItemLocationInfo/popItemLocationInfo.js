/**
 * Created by jerry on 2016/2/22.
 */

define(["modules/wms/wms.module",
        "modules/wms/directives/popItemLocationInfo/popItemLocationInfoService",
        "modules/wms/location/location.service"],
    function (wms) {
        wms.directive("itemlocationInfo", ["$modal", "itemLocationService","locationService","notify",
            function ($modal, itemLocationService, locationService, notify) {
                return {
                    restrict: "A",
                    scope: true,
                    link: link
                };

                function link($scope, elem, attr) {

                    $scope.deleteItemLocation = deleteItemLocation;
                    $scope.addItemLocation = addItemLocation;

                    //页面参数Map
                    var vm = $scope.vm = {
                        page:1,   //分頁用
                        rows: 5 //分頁用pageSize
                    };

                    //分頁結構體
                    $scope.pageOperation = {
                        curr: 1,
                        total: 1,
                        size: 5,
                        fetch: reqSearch
                    };

                    function reqSearch(page) {
                        doSearch(page);
                        $scope.pageOperation.curr = page;
                    }

                    //点击控件所在的行的时候触发事件
                    elem.click(function () {
                        var itemlocationInfo = analyzeItemLocationInfo(attr.itemlocationInfo);
                        vm.store_id = itemlocationInfo[0];
                        vm.store_name = itemlocationInfo[1];
                        vm.location_id = itemlocationInfo[2];
                        vm.location_name = itemlocationInfo[3];

                        createModal();

                        $scope.search = doSearch;
                        doSearch(1);
                    });

                    //检索reservationLog
                    function doSearch(page) {
                        vm.page = page;

                        itemLocationService.doSearch(vm).then(function (response) {
                            $scope.itemLocationList = response.data.itemLocations;
                            //$scope.pageOperation.total = response.data.total;
                            $scope.total = response.data.total;
                        });
                    }

                    //创建弹出框
                    function createModal() {
                        var m;
                        return m = $modal.open({
                            size: 'lg',
                            scope: $scope,
                            templateUrl: "modules/wms/directives/popItemLocationInfo/popItemLocationInfo.tpl.html",
                            title: " ItemLocationInfo",
                            controller: ["$scope", function (scope) {
                                scope.close = close;
                                function close() {
                                    m.close();
                                }
                            }]
                        });
                    }

                    // {{i.store_id}},{{i.store_name}},{{i.location_id}},{{i.location_name}}
                    function analyzeItemLocationInfo(itemLocationInfo) {
                        return itemLocationInfo.split(",");
                    }

                    // 解除绑定
                    function deleteItemLocation(item_location, index) {
                        locationService.doDeleteItemLocation(
                            item_location.item_location_id,
                            item_location.modified
                        ).then(
                            function(itemLocationLog) {
                                if (itemLocationLog) {
                                    $scope.itemLocationList.splice(index, 1);
                                    $scope.total = $scope.total - 1;
                                }
                            }
                        )
                    }

                    // 绑定追加
                    function addItemLocation() {
                        var location_name = vm.location_name;
                        var code = vm.code;
                        var sku = vm.sku;

                        var code_input = $('#location-bind-code');
                        var sku_input = $('#location-bind-sku');

                        if (!vm.code && !vm.sku) {
                            notify("WMS_LOCATION_INPUT");
                            return;
                        }

                        if (vm.code) {
                            vm.code = "";
                            code_input.focus();
                        }
                        if (vm.sku) {
                            vm.sku = "";
                            sku_input.focus();
                        }


                        locationService.doAddItemLocation(
                            location_name,
                            code,
                            sku,
                            vm.store_id,
                            '1'
                        ).then(
                            function(res) {
                                $scope.itemLocationList.unshift(res.itemLocation);
                                $scope.total = $scope.total + 1;

                                vm.code = "";
                                vm.sku = "";
                            }
                        );
                    }
                }
            }]);
    });