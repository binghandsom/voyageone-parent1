define(function(require) {
    var omsApp = require('modules/core/core.module');

    omsApp.controller('popUserPasswordController', ['$scope', function ($scope) {																					
    	var _ = require('underscore');

    	$scope.initialize=function(){
    		
    	}

    	$scope.doOk = function() {
        	if($scope.userPasswordForm.$valid)
        	{
        		$scope.updatePassword();
        		$scope.closeThisDialog();
        	}
        };
																			
    }])	
});
