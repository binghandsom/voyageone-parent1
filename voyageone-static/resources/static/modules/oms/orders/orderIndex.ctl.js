/**
 * @Name:    ordersIndexController.js
 * @Date:    2015/4/16
 *
 * @User:    Eric
 * @Version: 1.0.0
 */

define (function (require) {
    var omsApp = require ('modules/oms/oms.module');
    require ('modules/oms/orders/orders.service');
    require ('modules/oms/services/orderSearch.service');
    // 检索条件

    omsApp.controller ('orderIndexController', [
        '$scope', '$location', 'omsRoute', 'orderStatus', 'orderIndexService', 'searchOrderService',
        function ($scope, $location, omsRoute, orderStatus, orderIndexService, searchOrderService) {
            // 检索条件结构体
            $scope.search = {};
            $scope.orderIndexInfo ={};
            // 自定义显示的数据类型
            var showType = {
                type_1: 1,
                type_2: 2,
                type_3: 3
            };

            /**
             * 页面初始显示
             */
            $scope.initialize = function () {
                orderIndexService.doInit ()
                    .then (function (data) {
                    $scope.orderIndexInfo.todayCountList = data.orderIndexInfo.todayCount;
                    $scope.orderIndexInfo.reCountList = data.orderIndexInfo.reCount;
                    $scope.orderIndexInfo.uaCountList = data.orderIndexInfo.uaCount;
                    $scope.orderIndexInfo.dateTime = data.orderIndexInfo.dateTime;
                });
            };

            /**
             * 点击页面快捷link使用
             * @param type
             * @param channelId
             */
            $scope.doSaveSearch = function (type, channelId) {

                // 设置search检索页面需要保存的数据
                $scope.search.storeId = [];
                $scope.search.storeId.push (channelId);
                $scope.search.propertySelected = channelId;

                switch (type) {
                    case showType.type_1:
                        $scope.search.orderDateFrom = $scope.orderIndexInfo.dateTime;
                        $scope.search.orderDateTo = $scope.orderIndexInfo.dateTime;
                        $scope.search.quickFilter="04";
                        $scope.search.orderKindOriginal=true;
                        $scope.search.orderKindPriceDifference=true;
                        break;
                    case showType.type_2:
                       /* $scope.search.isRefund = "1";*/
                        $scope.search.quickFilter="01";
                        break;
                    case showType.type_3:
                      /*  $scope.search.approved = "0";*/
                        $scope.search.orderStatus = orderStatus.InProcessing;
                        break;
                    default :
                        break;
                }

                // 设置search页面检索条件
                searchOrderService.setSearchConditionFlag (true);
                searchOrderService.setSearchCondition ($scope.search);

                // 跳转到search页面
                $location.path (omsRoute.oms_orders_search.hash);
            }
        }]);
});
