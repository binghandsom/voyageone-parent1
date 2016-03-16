/**
 * Created by linanbin on 15/12/7.
 */
define([
    'angularAMD'
], function (angularAMD) {

    angularAMD.controller('popCustomColumnCtl', function ($scope,$searchAdvanceService) {
        $scope.cus = {
            customProps:[],
            commonProps:[]
        };

        /**
         * 初始化数据.
         */
        $scope.initialize = function () {
            $searchAdvanceService.getCustColumnsInfo()
                .then(function (res) {
                $scope.cus.customProps = res.data.customProps;
                $scope.cus.commonProps = res.data.commonProps;
            })
        }

        /**
         * 提交属性追加
         */
        $scope.ok = function () {

            $scope.$close();

        };
    });

});