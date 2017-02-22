/**
 * Created by sofia on 7/14/2016.
 */
define([
    'angularAMD',
    'underscore',
    'modules/cms/enums/Carts'
], function (angularAMD, _, Carts) {
    angularAMD.controller('PutOnOffController', function ($scope, context, productHistoryLogService) {

        $scope.vm = {
            logList : [],
            cartId: '',
            context: context,
            pageOption: {curr: 1, total: 0, size: 10, fetch: search}
        };

        $scope.initialize = function () {
            if ($scope.vm.context.cartId == 0) {
                $scope.vm.context.cartList = $scope.vm.context.cartList.sort(function (a, b) {
                    return a.cartId > b.cartId;
                });
            } else {
                $scope.vm.cartId = parseInt($scope.vm.context.cartId);
                var cartInfo = Carts.valueOf($scope.vm.cartId);
                if (cartInfo == null || cartInfo == undefined) {
                    $scope.vm.cartName = '';
                } else {
                    $scope.vm.cartName = cartInfo.name;
                }
            }
            search($scope.vm.context.cartId);
        };

        $scope.refreshData = function () {
            search($scope.vm.cartId);
        };

        /**
         * 查询数据
         */
        function search (selCartId) {
            var data = {};
            data.prodCode = $scope.vm.context.code;
            data.cartId = parseInt(selCartId);
            data.pageNum = $scope.vm.pageOption.curr;
            data.pageSize = $scope.vm.pageOption.size;

            productHistoryLogService.getPutOnOffLogList(data)
                .then(function(res) {
                    if (res.data && res.data.logList && res.data.logList.length > 0) {
                        $scope.vm.logList = res.data.logList;
                        $scope.vm.pageOption.total = res.data.total;
                        _.forEach($scope.vm.logList, function (logItem) {
                            // 日期格式转换
                            var updTime = logItem.modified;
                            if (updTime && updTime.length > 19) {
                                logItem.modified = updTime.substring(0, 19);
                            }
                            if (logItem.result == '等待执行' || logItem.result == '' || logItem.result == undefined || logItem.result == null) {
                                logItem.modified = '';
                            }
                            // 设置平台名
                            var cartInfo = Carts.valueOf(logItem.cartId);
                            if (cartInfo == null || cartInfo == undefined) {
                                logItem.cartName = '';
                            } else {
                                logItem.cartName = cartInfo.name;
                            }
                        });
                    } else {
                        $scope.vm.logList = [];
                        $scope.vm.pageOption.total = 0;
                    }
                });
        }

    });
});