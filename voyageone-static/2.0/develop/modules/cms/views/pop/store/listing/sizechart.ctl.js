/**
 * Created by tony-piao on 2016/5/5.
 */
define([
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {

    angularAMD.controller('popSizeChartCtl', function ($scope, context, sizeChartService, imageGroupService,$uibModalInstance) {
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
                $scope.noMathOpt = _.map(res.data,function(value,key){
                    return {cartName:key,imgList:value};
                });
            });

            if ($scope.dropdown.from === 'detail') {
                var saveInfo = $scope.vm.saveInfo,
                    dropdown = $scope.dropdown;

                saveInfo.sizeChartName = dropdown.saveInfo.sizeChartName;
                saveInfo.brandNameList = dropdown.saveInfo.brandName;
                saveInfo.productTypeList = dropdown.saveInfo.productType;
                saveInfo.sizeTypeList = dropdown.saveInfo.sizeType;
            }
        };

        $scope.save = function () {
            var _sizeChart = $scope.vm.sizeChart,
                upEntity;

/*            if (_.isObject(_sizeChart))
                upEntity = _.extend($scope.vm.saveInfo, _sizeChart);
            else
                upEntity = _.extend($scope.vm.saveInfo, {imageGroupName: _sizeChart});

            if($scope.dropdown.from === 'detail'){
                $uibModalInstance.close(upEntity);
                return;
            }

            sizeChartService.editSave(upEntity).then(function () {
                $scope.$close();
                context.search();
            });*/
        }

    });
});