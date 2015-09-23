/**
 * @Name:    popSetSizeChartController
 * @Date:    2015/9/1
 *
 * @User:    Eric
 * @Version: 1.0.0
 */

define (function (require) {
    var cmsApp = require ('modules/cms/cms.module');
    require ('modules/cms/popup/newSizeChartDetail/newSizeChartDetail.service');

    cmsApp.controller ('popNewSizeChartDetailController',['$scope', 'popNewSizeChartDetailService', 'userService', '$modalInstance', 'notify', 
        function ($scope, popNewSizeChartDetailService, userService, $modalInstance, notify) {

            var _ = require ('underscore');
            var channelId = userService.getSelChannel();

            /**
             * 初始化取得数据.
             */
            $scope.initialize = function () {
                $scope.sizeChartDetail = {};
                $scope.sizeChartDetail.sizeList = [];
                $scope.sizeChartModelUrl = null;
                doGetSizeChartModel ();
            };

            /**
             * 关闭当前对话框.
             */
            $scope.close = function() {
                $modalInstance.dismiss('close');
            };
           
            /**
             * 重新展示被选中的尺码对照模板图片.
             * @param sizeChartModelUrl
             */
            $scope.sizeChartModelChanged = function (sizeChartModelUrl) {
                $scope.sizeChartModelUrl = sizeChartModelUrl;
            };

            /**
             * 清空被选中的尺码对照模板.
             */
            $scope.clearSizeChartModelId = function () {
                $scope.sizeChartDetail.sizeChartModelId = null;
                $scope.sizeChartModelUrl = null;
            };

            /**
             * 添加一个新的尺码对照.
             */
            $scope.addNewSize = function () {
                $scope.sizeChartDetail.sizeList.push({sizeChartId:null, channelId: channelId, sizeValue: '', sizeCn: '', creater:null, modifier:null})
            };

            /**
             * 删除一个尺码对照.
             * @param sizeInfo
             */
            $scope.deleteSize = function (sizeInfo) {

                if (_.indexOf($scope.sizeChartDetail.sizeList, sizeInfo) > -1) {
                    $scope.sizeChartDetail.sizeList.splice(_.indexOf($scope.sizeChartDetail.sizeList, sizeInfo), 1);
                }
            };

            /**
             * 新建尺码对照表数据.
             */
            $scope.doSaveSizeChartDetail =  function () {
            	$scope.sizeChartDetail.channelId = channelId;
                var data = $scope.sizeChartDetail;
                
                popNewSizeChartDetailService.doSaveSizeChartDetail(data) .then (function (res) {
                    $modalInstance.close(res);
                	notify.success("CMS_TXT_MSG_SAVE_SUCCESS");
                })
            };

            /**
             * 取得尺码对照表的模板数据列表.
             */
            function doGetSizeChartModel () {
                popNewSizeChartDetailService.doGetSizeChartModel(channelId).then(function(data) {
                    $scope.sizeChartModelList = data;
                });
            }
        }])
});
