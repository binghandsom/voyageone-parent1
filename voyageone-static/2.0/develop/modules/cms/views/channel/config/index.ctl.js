/**
 * Created by dell on 2016/11/7.
 */

define([
    "cms",
    'modules/cms/controller/popup.ctl'
], function (cms) {
    cms.controller("channelConfigController", (function () {
        function ChannelConfigController($scope, channelConfigService, popups, confirm) {
            $scope.vm = {
                channels: [],
                configs: []
            };

            $scope.initialize = function () {
                init();
            };
            function init() {
                channelConfigService.init().then(function (resp) {
                    $scope.vm.channels = resp.data.channels == null ? [] : resp.data.channels;
                    $scope.selectChannel = _.find($scope.vm.channels, function (item) {
                        return item.channelId == resp.data.channelId;
                    });
                    $scope.vm.configs = resp.data.configs == null ? [] : resp.data.configs;
                });
            }

            $scope.changeChannel = function () {
                channelConfigService.loadByChannel({
                    "channelId": $scope.selectChannel.channelId
                }).then(function (resp) {
                    $scope.vm.configs = resp.data.configs == null ? [] : resp.data.configs;
                });
            }

            $scope.popNewChannelConfig = function () {
                popups.newChannelConfig($scope.selectChannel).then(function () {
                    channelConfigService.loadByChannel({
                        "channelId": $scope.selectChannel.channelId
                    }).then(function (resp) {
                        $scope.vm.configs = resp.data.configs == null ? [] : resp.data.configs;
                    });
                });
            }

            $scope.popEditChannelConfig = function (config) {
                var upEntity = angular.copy($scope.selectChannel);
                popups.editChannelConfig(_.extend(upEntity, config)).then(function () {
                    channelConfigService.loadByChannel({
                        "channelId": $scope.selectChannel.channelId
                    }).then(function (resp) {
                        $scope.vm.configs = resp.data.configs == null ? [] : resp.data.configs;
                    });
                });
            }

            $scope.delChannelConfig = function (config) {
                confirm("确定要删除吗？").then(function () {
                    channelConfigService.delChannelConfig({
                        "channelConfigId":config.id
                    }).then(function (resp) {
                        channelConfigService.loadByChannel({
                            "channelId": $scope.selectChannel.channelId
                        }).then(function (resp) {
                            $scope.vm.configs = resp.data.configs == null ? [] : resp.data.configs;
                        });
                    });
                });
            }
        }

        return ChannelConfigController;
    })())
});

