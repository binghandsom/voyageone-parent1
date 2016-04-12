/**
 * Created by 123 on 2016/4/12.
 */
define([
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {

    angularAMD.controller('popJoinJMCtl', function ($scope, $routeParams) {

        /**
         * 初始化数据.
         */
        $scope.initialize = function () {
            if ($scope.vm == undefined) {
                $scope.vm = {};
            }
            $scope.vm.imageMain = $routeParams.imageMain;
            $scope.vm.imageList = $routeParams.imageList;
        };

    });
});