/**
 * @Name:    customerIndexController.js
 * @Date:    2015/4/16
 *
 * @User:    Eric
 * @Version: 1.0.0
 */

define(function(require) {
    //var defaultApp = require('oms/default/defaultModule');
    var defaultApp = require('modules/oms/oms.module');

    require('modules/oms/customer/customerIndex.service');
    //require('common/config');

    defaultApp.controller('customerIndexController', ['$scope', 'customerIndexService',
        function($scope, customerIndexService) {

            $scope.initialize = function(){
            	customerIndexService.doInit()
                    .then(function(response) {
                       
                    });
            };
        }]);
    return defaultApp;
});
