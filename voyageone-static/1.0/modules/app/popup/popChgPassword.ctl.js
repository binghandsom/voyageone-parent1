define(function(require) {
    var mainApp = require('modules/app/app.module');
    require ('modules/app/common/common.service');

    mainApp.controller('popChgPasswordController', ['$scope','commonService', function ($scope,commonService) {																					
    	var _ = require('underscore');

    	$scope.initialize=function(){
    		
    	}

    	$scope.doOk = function() {
        	if($scope.registerForm.$valid)
        	{
        		commonService.doChgPassword($scope.register).then(function(response){
        			$scope.closeThisDialog();
        		},function(response){
        			$scope.message=response.message;
        		});
        		
        	}
        };
																			
    }])	
});
