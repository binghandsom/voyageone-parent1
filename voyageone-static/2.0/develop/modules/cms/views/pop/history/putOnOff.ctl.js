/**
 * Created by sofia on 7/14/2016.
 */
define([
    'angularAMD',
    'underscore',
    'modules/cms/controller/popup.ctl',
    'modules/cms/enums/Carts'
], function (angularAMD, _, Carts) {
    angularAMD.controller('PutOnOffController', function ($scope, context, productHistoryLogService) {

        $scope.vm = {
            logList : [],
            context: context,
            pageOption: {curr: 1, total: 0, size: 10, fetch: search}
        };

        $scope.initialize = function () {
            search();
        };

        $scope.refreshData = function () {
            search();
        };

        /**
         * 查询数据
         */
        function search () {
            var data = {};
            data.prodCode = '51A0HC13E1-00LCNB0';//self.params.code;
            data.cartId = 0;
            data.pageNum = $scope.vm.pageOption.curr;
            data.pageSize = $scope.vm.pageOption.size;

            productHistoryLogService.getPutOnOffLogList(data)
                .then(function(res) {
                    if (res.data && res.data.logList && res.data.logList.length > 0) {
                        $scope.vm.logList = res.data.logList;
                        $scope.vm.pageOption.total = res.data.total;
                        _.forEach($scope.vm.logList, function (logItem) {
                            var cartInfo = Carts.valueOf(logItem.cartId);
                            if (cartInfo == null || cartInfo == undefined) {
                                logItem.cartName = '';
                            } else {
                                logItem.cartName = cartInfo.name;
                            }
                        });
                    }
                });
        }

    });
});