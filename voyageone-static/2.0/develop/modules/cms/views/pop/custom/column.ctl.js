/**
 * Created by linanbin on 15/12/7.
 */
define([
    'angularAMD'
], function (angularAMD) {

    angularAMD.controller('popCustomColumnCtl', function ($scope, $searchAdvanceService, $modalInstance) {
        // 从后台取得的全部列
        $scope.cus = {
            customProps:[],
            commonProps:[]
        };

        /**
         * 初始化数据.
         */
        $scope.initialize = function () {
            $searchAdvanceService.getCustColumnsInfo().then(function (res) {
                $scope.cus.customProps = res.data.customProps;
                $scope.cus.commonProps = res.data.commonProps;
                $scope.cus.salesTypeList = res.data.salesTypeList;
                _.forEach($scope.cus.customProps, function (data) {
                    data.isChk = _.contains(res.data.custAttrList, data.feed_prop_original);
                });
                _.forEach($scope.cus.commonProps, function (data) {
                    data.isChk = _.contains(res.data.commList, data.propId);
                });
                _.forEach($scope.cus.salesTypeList, function (data) {
                    data.isChk = _.contains(res.data.selSalesTypeList, data.value);
                });
            })
        };

        /**
         * 确定返回，数据传回前端及用户自定义保存
         */
        $scope.ok = function() {
            var customProps = [];
            _.forEach($scope.cus.customProps, function (data) {
                if (data.isChk != undefined && data.isChk) {
                    customProps.push(data.feed_prop_original);
                }
            });
            var commonProps = [];
            _.forEach($scope.cus.commonProps, function (data) {
                if (data.isChk != undefined && data.isChk) {
                    commonProps.push(data.propId);
                }
            });
            var selSalesTypeList = [];
            _.forEach($scope.cus.salesTypeList, function (data) {
                if (data.isChk != undefined && data.isChk) {
                    selSalesTypeList.push(data.value);
                }
            });
            var params = {};
            params.customProps = customProps;
            params.commonProps = commonProps;
            params.selSalesTypeList = selSalesTypeList;
            $searchAdvanceService.saveCustColumnsInfo(params).then(function() {
                $modalInstance.close('');
            });
        };

        $scope.close = function () {
            $modalInstance.dismiss();
        }

    });

});