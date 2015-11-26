/**
 * Created by dell on 2015/4/9. modify by sky at 2015/04/21
 */

define(["modules/wms/wms.module",
        "modules/wms/directives/popReservationLog/popReservationLogService"],
    function (wms) {
        wms.directive("reservationLog", ["$modal", "reservationLogService",
            function ($modal, reservationLogService) {

                return {
                    restrict: "A",
                    link: link
                };

                function link($scope, elem, attr) {
                    //reservationId
                    var id;

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
                        id = attr.reservationLog;
                        createModal();
                        vm.reservation_id = id;
                        $scope.search = doSearch;
                        doSearch(1);
                    });

                    //检索reservationLog
                    function doSearch(page) {
                        vm.page = page;
                        id = vm.reservation_id;
                        reservationLogService.doSearch(vm).then(function (response) {
                            $scope.reservationLogList = response.data.reservationList;
                            //$scope.reservationLogList.count= response.data.reservationList.length;
                            $scope.pageOperation.total = response.data.total;
                            if (response.data.reservationList.length > 0 ){
                                vm.sku = response.data.reservationList[0].sku;
                                vm.productName =  response.data.reservationList[0].product;
                            } else {
                                vm.sku = "";
                                vm.productName =  "";
                            }
                        });
                    }

                    //创建弹出框
                    function createModal() {
                        var m;
                        return m = $modal.open({
                            size: 'lg',
                            scope: $scope,
                            templateUrl: "modules/wms/directives/popReservationLog/popReservationLog.tpl.html",
                            title: " ReservationLog",
                            controller: ["$scope", function (scope) {
                                scope.close = close;
                                function close() {
                                    m.close();
                                }
                            }]
                        });
                    }
                }
            }]);
    });