/**
 * Created by tony-piao on 2016/5/5.
 */
define([
    'angularAMD',
    'modules/cms/enums/Carts'
], function (angularAMD, carts) {

    angularAMD.controller('popSizeChartCtl', function ($scope, context, sizeChartService, imageGroupService, $uibModalInstance) {
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

                $scope.carts = carts;

                $scope.noMathOpt = _.map(res.data, function (value, key) {
                    return {cartId: +key, imgList: value};
                });

                if ($scope.dropdown.from === 'detail') {
                    var saveInfo = $scope.vm.saveInfo,
                        dropdown = $scope.dropdown;

                    saveInfo.sizeChartId = dropdown.saveInfo.sizeChartId;
                    saveInfo.sizeChartName = dropdown.saveInfo.sizeChartName;
                    saveInfo.brandNameList = dropdown.saveInfo.brandName;
                    saveInfo.productTypeList = dropdown.saveInfo.productType;
                    saveInfo.sizeTypeList = dropdown.saveInfo.sizeType;

                    sizeChartService.getListImageGroupBySizeChartId({sizeChartId: dropdown.saveInfo.sizeChartId}).then(function (res) {
                        if (res.data.length > 0) {
                            $scope.chartType = "match";
                            $scope.vm.sizeChart = {};

                            angular.forEach(res.data, function (element) {
                                _.find($scope.noMathOpt, function (item) {
                                    return item.cartId == element.cartId;
                                }).imgList.push(element);

                                $scope.vm.sizeChart[+element.cartId] = element;
                            });
                        }else{
                            $scope.chartType = "setting";
                        }
                    });
                }
            });

        };

        $scope.radioChange = function(){

            if($scope.vm.sizeChart)
                $scope.chartOrg = $scope.vm.sizeChart;

            switch($scope.chartType){
                case 'setting':
                    $scope.vm.sizeChart = null;
                    break;
                case 'match':
                    $scope.vm.sizeChart = $scope.chartOrg;
                    break;
                case 'new':
                    $scope.vm.sizeChart = "";
                    break;
            }
        };

        $scope.save = function () {
            var _sizeChart = $scope.vm.sizeChart,
                listImageGroup = [],
                upEntity;

            if (_sizeChart) {
                if (_.isObject(_sizeChart)) {
                    listImageGroup = _.map(_sizeChart, function (value) {
                        return value;
                    });
                } else {
                    angular.forEach( $scope.noMathOpt,function (element) {
                        listImageGroup.push({cartId:element.cartId,imageGroupName: _sizeChart, imageGroupId: 0});
                    });
                }
            }

            upEntity = _.extend($scope.vm.saveInfo, {listImageGroup: listImageGroup});

            sizeChartService.editSave(upEntity).then(function () {
                $scope.$close();
                context.search();
            });
        }

    });
});