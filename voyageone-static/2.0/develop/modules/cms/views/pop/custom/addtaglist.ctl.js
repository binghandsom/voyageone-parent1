/**
 * Created by 123 on 2016/4/18.
 */

define([
    'cms',
    'modules/cms/controller/popup.ctl'
], function (cms) {

    cms.controller('popAddTagListCtl', function ($scope, channelTagService, context, alert) {

        //定义常量
        $scope.vm = {
            tagInfo: null,
            tagPath: null,
            tagPathName: null,
            tagSelectObject: null,
            first: null
        };

        //页面初始化
        $scope.initialize = function () {

            var vm = $scope.vm;
            vm.tagInfo = context.tagInfo;
            vm.first = context.first;

            if (context.tagSelectObject) {
                //页面有数据的情况
                if (context.first)
                    vm.tagPath = "";
                else
                    vm.tagPath = context.tagSelectObject.tagPathName;

            } else {
                //页面没有数据的情况
                if (context.first)
                    vm.tagPath = "";
                else {
                    $scope.$dismiss();
                    alert('TXT_MSG_TAG');
                }
            }
        };

        /**
         * 新增标签
         */
        $scope.save = function () {
            $scope.vm.tagSelectObject = context.tagSelectObject;

            channelTagService.save($scope.vm).then(function (res) {
                //$scope.source = $scope.vm.tagTree = res.data.tagInfo.tagTree;
                $scope.$close();
            })
        }
    });
});