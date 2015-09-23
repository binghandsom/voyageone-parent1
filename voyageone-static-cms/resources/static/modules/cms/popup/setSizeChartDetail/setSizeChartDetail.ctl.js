/**
 * @Name:    popSetSizeChartController
 * @Date:    2015/9/1
 *
 * @User:    Eric
 * @Version: 1.0.0
 */

define (function (require) {
    var cmsApp = require ('modules/cms/cms.module');
    require ('modules/cms/popup/setSizeChartDetail/setSizeChartDetail.service');

    cmsApp.controller ('popSetSizeChartDetailController',['$scope', 'popSetSizeChartDetailService', 'userService', '$modalInstance', 'notify', 'parameters', 'DTOptionsBuilder',
        function ($scope, popSetSizeChartDetailService, userService, $modalInstance, notify, parameters, DTOptionsBuilder) {

            var _ = require ('underscore');
            var channelId = userService.getSelChannel();

            /**
             * 关闭当前对话框.
             */
            $scope.close = function() {
                $modalInstance.dismiss('close');
            };

            /**
             * 初始化
             */
            $scope.initialize = function () {
                //$scope.dtSizeChartList = {
                //    options: DTOptionsBuilder.newOptions()
                //        .withOption('processing', true)
                //        .withOption('scrollY', '400px')
                //        .withOption('scrollX', '100%')
                //        .withOption('scrollCollapse', true)
                //        .withPaginationType('full_numbers')
                //};
                doGetSizeChartModel ();
                doGetSizeChartDetailInfo ();
                //doGetBoundSizeChart ();
            };

            /**
             * 当尺码对照发生变化时.
             */
            $scope.sizeChartDetailChanged = function () {
                $scope.sizeChartDetailIsChanged = !_.isEqual ($scope.sizeChartDetail, $scope.oldSizeChartDetail);
            };

            /**
             * 重新展示被选中的尺码对照模板图片.
             * @param sizeChartModelUrl
             */
            $scope.sizeChartModelChanged = function (sizeChartModelUrl) {
                $scope.sizeChartDetail.sizeChartModelUrl = sizeChartModelUrl;
                $scope.sizeChartDetailChanged();
            };

            /**
             * 清空被选中的尺码对照模板.
             */
            $scope.clearSizeChartModelId = function () {
                $scope.sizeChartDetail.sizeChartModelId = null;
                $scope.sizeChartDetail.sizeChartModelUrl = null;
                $scope.sizeChartDetailChanged ();
            };

            /**
             * 添加一个新的尺码对照.
             */
            $scope.addNewSize = function () {
                $scope.sizeChartDetail.sizeList.push({sizeChartId:parameters.sizeChartId,channelId: channelId,sizeValue: '',sizeCn: '',creater:null,modifier:null})
                $scope.sizeChartDetailChanged();
            };

            /**
             * 删除一个尺码对照.
             * @param sizeInfo
             */
            $scope.deleteSize = function (sizeInfo) {

                if (_.indexOf($scope.sizeChartDetail.sizeList, sizeInfo) > -1) {
                    $scope.sizeChartDetail.sizeList.splice(_.indexOf($scope.sizeChartDetail.sizeList, sizeInfo), 1);
                    $scope.sizeChartDetailChanged();
                }
            };

            //$scope.doSaveSizeChart =  function () {
            //    var data={};
            //
            //    popSetSizeChartDetailService.doSaveSizeChart(data) .then (function (res) {
            //
            //    })
            //};

            /**
             * 更新被修改的尺码对照.
             */
            $scope.doUpdateSizeChartDetail =  function () {
            	var data = $scope.sizeChartDetail;
                popSetSizeChartDetailService.doUpdateSizeChartDetailInfo(data) .then (function (res) {
                	 notify.success("CMS_TXT_MSG_UPDATE_SUCCESS");
                     $modalInstance.close('');

                })

            };
           
            /**
             * 获得该尺码对照已经被绑定的数据信息.
             */
            //function doGetBoundSizeChart () {
            //    var data = {
            //        channelId: channelId,
            //        sizeChartId: parameters.sizeChartId
            //    };
            //
            //    popSetSizeChartDetailService.doGetBoundSizeChart(data) .then (function (data) {
            //        $scope.bindSizeChartList = data;
            //    })
            //}

            /**
             * 取得当前尺码的对照的详细信息.
             */
            function doGetSizeChartDetailInfo () {
                var data={
                    channelId: channelId,
                    sizeChartId: parameters.sizeChartId
                };

                popSetSizeChartDetailService.doGetSizeChartDetailInfo(data) .then (function (data) {
                    $scope.sizeChartDetail = data;
                    $scope.oldSizeChartDetail = angular.copy($scope.sizeChartDetail);
                    $scope.sizeChartDetailIsChangd = false;
                })
            }

            /**
             * 取得尺码对照表的模板数据列表.
             */
            function doGetSizeChartModel () {
                popSetSizeChartDetailService.doGetSizeChartModel(channelId).then(function(data) {
                    $scope.sizeChartModelList = data;
                });
            }

        }])
});
