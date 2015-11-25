/**
 * @Name:    omsIndexController.js
 * @Date:    2015/3/2
 *
 * @User:    Tom
 * @Version: 1.0.0
 */

define(function(require) {
    //var defaultApp = require('oms/default/defaultModule');
    var defaultApp = require('modules/oms/oms.module');

    require('modules/oms/default/omsIndex.service');
    //require('common/config');

    defaultApp.controller('omsIndexController', ['$scope', 'omsIndexService',
        function($scope, omsIndexService) {

            $scope.initialize = function(){
                omsIndexService.doInit()
                    .then(function(response) {
                        $scope.omsInfoList = response.data;
                    });
            };
        }]);
    return defaultApp;
});
