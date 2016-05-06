/**
 * Created by tony-piao on 2016/5/5.
 */
define([
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {

    angularAMD.controller('popSizeChartCtl', function ($scope, context) {

        $scope.initialize = function () {
            console.log(context);
            if ($scope.vm == undefined) {
                $scope.vm = {};
            }
/*            $scope.vm.imageMain = $routeParams.imageMain;
            $scope.vm.imageList = $routeParams.imageList;*/
        };

    });
});