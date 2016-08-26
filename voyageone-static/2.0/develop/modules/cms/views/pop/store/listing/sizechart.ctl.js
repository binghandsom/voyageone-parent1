/**
 * Created by tony-piao on 2016/5/5.
 */
define([
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {

    angularAMD.controller('popSizeChartCtl', function ($scope, context, sizeChartService, imageGroupService) {
        $scope.vm = {
            saveInfo: {sizeChartName: "", brandNameList: [], productTypeList: [], sizeTypeList: []}
        };

        $scope.dropdown = context;

        $scope.initialize = function () {
            if ($scope.vm == undefined) {
                $scope.vm = {};
            }

            /**获取未匹配的尺码图组*/
            imageGroupService.getNoMatchSizeImageGroupList().then(function (res) {
                $scope.noMathOpt = res.data;
            });
        };

        $scope.save = function () {
            var _sizeChart = $scope.vm.sizeChart,
                upEntity;

            if (_.isObject(_sizeChart))
                upEntity = _.extend($scope.vm.saveInfo, _sizeChart);
            else
                upEntity = _.extend($scope.vm.saveInfo, {imageGroupName: _sizeChart});

            sizeChartService.editSave(upEntity).then(function () {
                $scope.$close();
                context.search();
            });
        }

    });
});