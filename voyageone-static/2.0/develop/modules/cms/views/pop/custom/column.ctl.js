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
        $scope.initialize = initialize;
        /**
         * 初始化数据.
         */
        function initialize () {
            $searchAdvanceService.getCustColumnsInfo()
                .then(function (res) {
                $scope.cus.customProps = res.data.customProps;
                $scope.cus.commonProps = res.data.commonProps;
            })
        }

    });

});