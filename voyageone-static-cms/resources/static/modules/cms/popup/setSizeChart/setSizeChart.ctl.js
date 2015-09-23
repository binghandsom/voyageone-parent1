/**
 * @Name:    popSetSizeChartController
 * @Date:    2015/9/1
 *
 * @User:    Eric
 * @Version: 1.0.0
 */

define (function (require) {
    var cmsApp = require ('modules/cms/cms.module');
    require ('modules/cms/popup/setSizeChart/setSizeChart.service');
    require ('modules/cms/popup/setSizeChartDetail/setSizeChartDetail.ctl');
    require ('modules/cms/popup/newSizeChartDetail/newSizeChartDetail.ctl');
    cmsApp.controller ('popSetSizeChartController', ['$scope', 'popSetSizeChartService', 'userService', '$modalInstance', '$modal', 'notify', 'cmsPopupPages', 'parameters', 'DTOptionsBuilder',
        function ($scope, popSetSizeChartService, userService, $modalInstance, $modal, notify, cmsPopupPages, parameters, DTOptionsBuilder) {

            var channelId = userService.getSelChannel();
            $scope.searchInfo = {};
            $scope.searchInfo.size = null;
            $scope.searchInfo.cnSize = null;
//            $scope.usModelInfo.productTypeId = null;
//            $scope.usModelInfo.brandId = null;
//            $scope.usModelInfo.sizeTypeId = null;
            /**
             * 初始化操作.
             */
            $scope.initialize = function () {
                $scope.currentSizeChartId = parameters.sizeChartId;
                $scope.dtSizeChartList = {
                    options: DTOptionsBuilder.newOptions()
                        .withOption('processing', true)
                        .withOption('scrollY', '400px')
                        .withOption('scrollX', '100%')
                        .withOption('scrollCollapse', true)
                        .withPaginationType('full_numbers')
                };
            };

            /**
             * 关闭窗口，并初始化该页面输入内容.
             */
            $scope.close = function () {
                $modalInstance.dismiss('close');
            };

            /**
             * 检索出已经被绑定的尺码对照.
             */
            $scope.doGetBoundSizeChartList =  function () {
                var data = {
                    channelId: channelId,
                    productTypeId: $scope.searchInfo.productTypeId,
                    brandId: $scope.searchInfo.brandId,
                    sizeTypeId: $scope.searchInfo.sizeTypeId
                };

                popSetSizeChartService.doGetBoundSizeChartList(data).then (function (res) {
                    $scope.bindSizeList=res.data;
                })
            };

            /**
             * 检索出其他尺码对照表.
             */
            $scope.doGetOtherSizeChartList =  function () {
                var data = {
                    channelId: channelId,
                    sizeValue: $scope.searchInfo.size,
                    sizeCn: $scope.searchInfo.cnSize
                };

                popSetSizeChartService.doGetOtherSizeChartList(data).then (function (res) {
                    $scope.otherSizeList = res.data;
                })
            };

            /**
             * 保存已经选中的
             */
            $scope.doSaveSizeChart = function () {
                var data = {
                    channelId: channelId
                };

                switch (parameters.type) {
                    // 更新category的尺码对照
                    case '1':
                        data.categoryId = parameters.id;
                        data.channelId = channelId;
                        data.sizeChartId = $scope.currentSizeChartId;
                        popSetSizeChartService.doSaveCategorySizeChart(data) .then (function () {
                            notify.success("CMS_TXT_MSG_UPDATE_SUCCESS");
                            $modalInstance.close('');
                        });
                        break;
                    // 更新model的尺码对照
                    case '2':
                        data.modelId = parameters.id;
                        data.channelId = channelId;
                        data.sizeChartId = $scope.currentSizeChartId;
                        data.productTypeId = parameters.modelInfo.productTypeId;
                        data.brandId = parameters.modelInfo.brandId;
                        data.sizeTypeId = parameters.modelInfo.sizeTypeId;
                        popSetSizeChartService.doSaveModelSizeChart(data) .then (function () {
                            notify.success("CMS_TXT_MSG_UPDATE_SUCCESS");
                            $modalInstance.close('');
                        });
                        break;
                }
            };
            
            /**
             * 该页面只能选中一个尺码对照.
             * @param sizeChartId
             */
            $scope.selSizeChartId = function (sizeChartId) {

                if (_.isEqual($scope.currentSizeChartId, sizeChartId)) {
                    $scope.currentSizeChartId = null;
                } else {
                    $scope.currentSizeChartId = sizeChartId;
                }
            };

            $scope.vm = {};
            $scope.vm.refreshSizeChart = function (sizeChartId) {
                $scope.currentSizeChartId = sizeChartId;
                $scope.doSaveSizeChart();
            }

        }])
});
