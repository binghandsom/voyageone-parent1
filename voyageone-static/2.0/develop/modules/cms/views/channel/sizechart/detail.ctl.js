/**
 * Created by tony-piao on 2016/5/5.
 */
define([
    'cms',
    'modules/cms/controller/popup.ctl'
], function (cms) {

    cms.controller('sizeChartDetailCtl',function($scope, $routeParams, sizeChartService, sizeChartDetailService, alert, notify, $translate, popups) {

        $scope.vm = {
            originCondition: [],    //保存初始状态
            saveInfo: [],
            importList: [],
            brandNameList: [],
            productTypeList: [],
            sizeTypeList: [],
            sizeChartId:Number($routeParams.sizeChartId)
        };

        $scope.initialize = function () {
            getItemById();
            init();
        };

        function getItemById() {
            sizeChartDetailService.init({sizeChartId: $scope.vm.sizeChartId}).then(function (resp) {
                var item = $scope.vm.saveInfo = resp.data.sizeChartList[0];
                $scope.vm.originCondition = angular.copy(resp.data.sizeChartList[0]);
                $scope.vm.importList = _.map(item.sizeMap == null ? [] : item.sizeMap, function (item) {
                    item.usual = item.usual != "0";
                    return item;
                });

                $scope.vm.canUsual = _.every($scope.vm.importList,function(item){
                    return item.usual;
                })
            });
        }

        function init() {
            var vm = $scope.vm;
            sizeChartService.init().then(function (resp) {
                vm.brandNameList = resp.data.brandNameList == null ? [] : resp.data.brandNameList;
                vm.productTypeList = resp.data.productTypeList == null ? [] : resp.data.productTypeList;
                vm.sizeTypeList = resp.data.sizeTypeList == null ? [] : resp.data.sizeTypeList;
            });
        }

        /**
         * 添加尺码表
         */
        $scope.addSize = function () {
            $scope.vm.importList.push({usual: true});
        };
        /**
         * 重置
         */
        $scope.reset = function () {
            $scope.vm.saveInfo = angular.copy($scope.vm.originCondition);
        };

        /**
         * 保存修改后的尺码表
         */
        $scope.saveFinish = function () {
            var upEntity = $scope.vm.saveInfo;

            if ($scope.vm.saveInfo.sizeChartName == "") {
                alert($translate.instant('TXT_SIZE_CHART_NOTICE_REQUIED'));
                return;
            }

            sizeChartDetailService.detailSave({
                sizeChartId: upEntity.sizeChartId,
                sizeChartName: upEntity.sizeChartName,
                finishFlag: upEntity.finish,
                brandNameList: upEntity.brandName,
                productTypeList: upEntity.productType,
                sizeTypeList: upEntity.sizeType
            }).then(function () {
                notify.success($translate.instant('TXT_MSG_UPDATE_SUCCESS'));
                getItemById();
            });
        };

        /**尺码修改popup*/
        $scope.openSizeDetails = function () {
            var self = this;

            self.vm.from = "detail";

            popups.openSizeChartAdd(self.vm).then(function () {
                $scope.init();
            });
        };

        /**
         * 保存导入（输入）的尺码表
         */
        $scope.saveSize = function () {
            var upEntity = $scope.vm.saveInfo,
                sizeMaps = angular.copy($scope.vm.importList),
                tmpOriginalSize = "";

            if ($scope.vm.saveInfo.sizeChartName == "") {
                alert($translate.instant('TXT_SIZE_CHART_NOTICE_REQUIED'));
                return;
            }

            sizeMaps = _.filter(sizeMaps, function (item) {
                return item.originalSize != null || item.adjustSize != null;
            });

            _.map(sizeMaps, function (item) {
                return item.usual = item.usual ? "1" : "0";
            });

            for (var i = 0, length = sizeMaps.length; i < length; i++) {
                if (sizeMaps[i].originalSize == null || sizeMaps[i].adjustSize == null) {
                    alert($translate.instant('TXT_SIZE_CHART_NOTICE_NULL'));
                    return;
                }
                if (sizeMaps[i].originalSize == tmpOriginalSize) {
                    alert($translate.instant('TXT_SIZE_CHART_NOTICE_REPEAT'));
                    return;
                } else {
                    tmpOriginalSize = sizeMaps[i].originalSize;
                }
            }

            sizeChartDetailService.detailSizeMapSave({
                sizeChartId: upEntity.sizeChartId,
                sizeMap: sizeMaps
            }).then(function () {
                notify.success($translate.instant('TXT_MSG_UPDATE_SUCCESS'));
                getItemById();
            });
        };

        /**
         * 删除添加项
         * @param index
         */
        $scope.delSize = function (index) {
            $scope.vm.importList.splice(index, 1);
        };

        /**
         * 处理导入数组
         * @param result
         */
        $scope.import = function (result) {
            $scope.vm.importList = [];
            var tmp = 0, count = 0, obj = {};
            angular.forEach(result, function (data) {
                if (tmp == data.index) {
                    switch (count) {
                        case 0:
                            obj.originalSize = data.value;
                            break;
                        case 1:
                            obj.adjustSize = data.value;
                            break;
                        case 2:
                            obj.usual = data.value == 1;
                            break;
                        default:
                            break;
                    }
                    count++;
                } else {
                    $scope.vm.importList.push(obj);
                    obj = {};
                    obj.originalSize = data.value;
                    tmp = data.index;
                    count = 1;
                }
            });
            $scope.vm.importList.push(obj);
        };

        /**刷新操作*/
        $scope.refresh = function () {
            init();
        };

        /**
         * 全选操作
         */
        $scope.checkUsual = function(){
            var vm =  $scope.vm;

            _.each(vm.importList,function(ele){
                ele.usual = vm.canUsual;
            });
        }

    });

});