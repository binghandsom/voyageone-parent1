/**
 * @Name:    coreIndexController.js
 * @Date:    2015/4/16
 *
 * @User:    Eric
 * @Version: 1.0.0
 */

define(function(require) {
    //var defaultApp = require('oms/default/defaultModule');
    var defaultApp = require('modules/core/core.module');

    require('modules/core/default/coreIndex.service');
    //require('common/config');

    defaultApp.controller('coreIndexController', ['$scope', 'coreIndexService',
        function($scope, coreIndexService) {

            $scope.initialize = function(){
                coreIndexService.doInit()
                    .then(function(response) {
                        $scope.omsInfoList = response.data;
                    });
            };
        }]);
    return defaultApp;
});
