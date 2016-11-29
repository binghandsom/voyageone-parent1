/**
 * Created by dell on 2016/11/8.
 */
define([
    "cms",
],function (cms) {
    cms.controller("channelConfigAddController", (function () {
        function ChannelConfigDetailController($scope, channelConfigService, context){
            $scope.channelId = context.channelId;
            $scope.channelName = context.channelName;
            $scope.modelBean = {};
            $scope.addSubmit = function () {
                var _modelBean = angular.copy($scope.modelBean);
                _modelBean.channelId = $scope.channelId;
                channelConfigService.addChannelConfig(_modelBean).then(function () {
                    $scope.$close();
                });
            }
        }
        return ChannelConfigDetailController;
    })());
});
