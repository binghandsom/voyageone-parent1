/**
 * Created by linanbin on 15/12/7.
 */
define([
    'angularAMD'
], function (angularAMD) {

    angularAMD.controller('popCustomColumnCtl', function ($scope, $searchAdvanceService) {
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
                _.forEach($scope.cus.customProps, function (data) {
                    data.isChk = _.contains(res.data.custAttrList, data.feed_prop_original);
                });
                _.forEach($scope.cus.commonProps, function (data) {
                    data.isChk = _.contains(res.data.commList, data.propId);
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
            var params = {};
            params.customProps = customProps;
            params.commonProps = commonProps;
            $searchAdvanceService.saveCustColumnsInfo(params);
            $scope.$close();
            console.log($scope.cusData);
        };


    });

});