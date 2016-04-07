/**
 * Created by 123 on 2016/4/1.
 */
define([
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {

    angularAMD.controller('popCodeDetailCtl', function ($scope, $routeParams) {

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