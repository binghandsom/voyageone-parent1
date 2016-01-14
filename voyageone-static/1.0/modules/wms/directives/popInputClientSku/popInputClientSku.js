/**
 * @User: Jack
 * @Date: 2015-06-04 15:39:01
 * @Version: 0.0.1
 * @modify sky 20150604
 */

define([
    "modules/wms/wms.module",
    "modules/wms/directives/popInputClientSku/popInputClientSkuService",
    "components/directives/dialogs/dialogs"
], function (wms) {

    wms.factory("wmsInputClientSku", [
        "$modal",
        "vAlert",
        "notify",
        "popInputClientSkuService",
        function ($modal, alert, notify, popInputClientSkuService) {

            //弹出框对象
            var m;

            return function($scope, transfer_package_id, code, num) {
                m = $modal.open({
                    size: 'dialog',
                    scope: $scope,
                    templateUrl: "modules/wms/directives/popInputClientSku/popInputClientSku.tpl.html",
                    title: "Input Client Sku Info",
                    controller: ["$scope", modalController]
                });

                //弹出框控制器
                function modalController(scope) {

                    //页面变量Map
                    var vm = scope.vm = {};
                    vm.transfer_package_id = transfer_package_id;
                    vm.code = code;
                    vm.num = num;

                    //关闭窗口
                    scope.close = close;

                    //点击查询按钮
                    scope.doChange = doChange;

                    // 根据检索条件查询相关记录
                    function doChange() {
                        // Code 空检查
                        if (!vm.itemCode) {
                            alert("Please Input Item Code.");
                            return;
                        }

                        // Color 空检查
                        if (!vm.color) {
                            alert("Please Input Color.");
                            return;
                        }

                        // Size 空检查
                        if (!vm.size) {
                            alert("Please Input Size.");
                            return;
                        }

                        popInputClientSkuService.change(
                            vm.transfer_package_id,
                            vm.code,
                            vm.num,
                            vm.itemCode,
                            vm.color,
                            vm.size
                            )
                            .then(function (sku) {
                                if(sku == 'repeatInput') {
                                    alert("Input ItemCode and size is exist!");
                                    return;
                                }

                                // 如果返回内容，有值。那么就反映到画面中
                                $scope.vm.packageItems.unshift({
                                    transfer_sku: sku,
                                    transfer_barcode: vm.code,
                                    transfer_qty: vm.num
                                });

                                m.close();
                            });
                    }

                    function close() {
                        m.close();
                    }

                    function stringToObject(reservationInfo) {
                        if (!_.isObject(reservationInfo)) {
                            return JSON.parse(reservationInfo)
                        } else {
                            return reservationInfo;
                        }
                    }
                }
            };
        }
    ]).directive("popWmsInputClientSku", [
        "wmsInputClientSku",
        popWmsInputClientSku
    ]);

    function popWmsInputClientSku(wmsInputClientSku) {
        return {
            restrict: "A",
            link: link
        };

        function link($scope, elem, attr) {
            //点击插件属性，标记的页面元素时候触发事件
            elem.click(function () {
                wmsInputClientSku($scope, attr.popWmsInputClientSku);
            });
        }
    }

});