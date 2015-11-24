
define(function(require) {
    var omsApp = require('modules/core/core.module');

    omsApp.controller('popUserRoleController', ['$scope', function ($scope) {																					
    	var _ = require('underscore');

    	$scope.initialize=function(){
    		
    	}
    	$scope.doOk = function() {
        	if($scope.userRoleForm.$valid)
        	{
        		$scope.doUpdateRole();
        		$scope.closeThisDialog();
        	}
            
        };
																			
    }])	
});

