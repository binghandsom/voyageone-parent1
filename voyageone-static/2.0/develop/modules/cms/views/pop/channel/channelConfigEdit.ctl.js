/**
 * Created by dell on 2016/11/8.
 */
define([
    "cms",
],function (cms) {
    cms.controller("channelConfigEditController", (function () {
        function ChannelConfigDetailController($scope, channelConfigService, context){
            $scope.channelName = context.channelName;
            $scope.modelBean = {};
            $scope.initialize = function () {
                init();
            };
            function init(){
                channelConfigService.loadChannelConfigDetail({
                    "channelConfigId":context.id
                }).then(function (resp) {
                    $scope.modelBean = resp.data == null ? {} : resp.data;
                });
            }
            $scope.editSubmit = function () {
                var _modelBean = angular.copy($scope.modelBean);
                channelConfigService.editChannelConfig(_modelBean).then(function () {
                    $scope.$close();
                });
            }
        }
        return ChannelConfigDetailController;
    })());
});
