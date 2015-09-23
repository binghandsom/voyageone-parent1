/**
 * @User: Jack
 * @Date: 2015-06-04 15:39:01
 * @Version: 0.0.1
 * @modify sky 20150604
 */

define([
    "modules/wms/wms.module",
    "modules/wms/directives/popChangeReservation/popChangeReservationService",
    "components/directives/dialogs/dialogs"
], function (wms) {

    wms.factory("wmsChangeReservation", [
        "$modal",
        "vAlert",
        "notify",
        "popChangeReservationService",
        function ($modal, alert, notify, popChangeReservationService) {

            //弹出框对象
            var m;

            return function($scope, reservationInfo) {
                m = $modal.open({
                    size: 'dialog',
                    scope: $scope,
                    templateUrl: "modules/wms/directives/popChangeReservation/popChangeReservation.tpl.html",
                    title: "Change Reservation",
                    controller: ["$scope", modalController]
                });

                //弹出框控制器
                function modalController(scope) {

                    //页面变量Map
                    var vm = scope.vm = {};

                    var viewCtl = $scope.viewCtl = {};

                    //检索条件初始化
                    vm.change = {
                        store: "",
                        sku: "",
                        status: "",
                        notes: ""
                    };

                    //关闭窗口
                    scope.close = close;

                    //点击查询按钮
                    scope.doChange = doChange;

                    reservationInfo = stringToObject(reservationInfo);

                    //初始化
                    popChangeReservationService.popInit(reservationInfo.order_channel_id, reservationInfo.store_id).then(function (response) {

                        viewCtl.changeStore = false;
                        viewCtl.updateStatus = false;

                        // 仓库变更的场合，显示仓库行
                        if (reservationInfo.changeKind == "1") {
                            vm.stores = response.storeList;
                            if (response.storeId != '' && !isNaN(response.storeId)) {
                                var store_id = parseInt(response.storeId);
                                vm.change.store = store_id;
                            }

                            viewCtl.changeStore = true;
                        }
                        // 备注入力的场合，显示备注内容
                        else if (reservationInfo.changeKind == "2") {
                            vm.change.notes = reservationInfo.res_note;

                        }
                        // 状态变更的场合，显示状态行
                        else {
                            viewCtl.updateStatus = true;
                            vm.change.sku = reservationInfo.sku;
                            vm.change.status = "[" + reservationInfo.reservation_id + "]'s Status will changed to：" + reservationInfo.processContent + ".";
                        }

                    });

                    // 根据检索条件查询相关记录
                    function doChange() {

                        // 仓库变更的场合，显示仓库行
                        if (reservationInfo.changeKind == "1") {
                            if (!vm.change.store) {
                                alert("Please Select Store.");
                                return;
                            }
                        }

                        if (!vm.change.notes) {
                            alert("Please Input Notes.");
                            return;
                        }

                        popChangeReservationService.change(
                            reservationInfo.reservation_id,
                            reservationInfo.changeKind,
                            vm.change.notes,
                            vm.change.store,
                            reservationInfo.modified
                        )
                            .then(function (res) {
                                $scope.reservationList.selected.store_id = res.data.store_id;
                                $scope.reservationList.selected.store_name = res.data.store_name;
                                $scope.reservationList.selected.res_status_id = res.data.res_status_id;
                                $scope.reservationList.selected.res_status_name = res.data.res_status_name;
                                $scope.reservationList.selected.res_note = res.data.res_note;
                                $scope.reservationList.selected.modified = res.data.modified;

                                var op={"message": reservationInfo.reservation_id + " update success !","status":"success"};
                                notify(op);

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
    ]).directive("popWmsChangeReservation", [
        "wmsChangeReservation",
        popWmsChangeReservation
    ]);

    function popWmsChangeReservation(wmsChangeReservation) {
        return {
            restrict: "A",
            link: link
        };

        function link($scope, elem, attr) {
            //点击插件属性，标记的页面元素时候触发事件
            elem.click(function () {
                wmsChangeReservation($scope, attr.popWmsChangeReservation);
            });
        }
    }

});