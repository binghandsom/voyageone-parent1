/**
 * Created by 123 on 2016/4/8.
 */
define([
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {

    angularAMD.controller('popPriceModifyCtl', function ($scope, $routeParams) {
        $scope.model={priceValueType:1,priceType:"1",discount:0,price:0};

        /**
         * 初始化数据.
         */
        $scope.initialize = function () {
            if ($scope.vm == undefined) {
                $scope.vm = {};
            }
            $scope.vm.attsList = $routeParams.attsList;
        };

    });
});