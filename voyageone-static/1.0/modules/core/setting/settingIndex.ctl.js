/**
 * @Name:    settingIndexController.js
 * @Date:    2015/4/16
 *
 * @User:    Eric
 * @Version: 1.0.0
 */

define(function(require) {
 	 var accountApp = require('modules/core/core.module');
	    require('modules/core/setting/settingIndex.service');
  

	    accountApp.controller('settingIndexController', ['$scope', 'settingIndexService',
        function($scope, settingIndexService) {

            $scope.initialize = function(){
            	settingIndexService.doInit()
                    .then(function(response) {
                      
                    });
            };
        }]);
    return accountApp;
});
