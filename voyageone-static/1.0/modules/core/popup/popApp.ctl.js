
define(function(require) {
    var omsApp = require('modules/core/core.module');

    omsApp.controller('popAppController', ['$scope', function ($scope) {																					
    	var _ = require('underscore');

    	$scope.initialize=function(){
    		
    	}

    	$scope.doOk = function() {
    		//app变更或追加的场合
        	if($scope.popInfo.isShow==1)
        	{
            	if($scope.appForm.$valid)
            	{
            		$scope.doUpdate();
            		$scope.closeThisDialog();
            	}
        	}
        	//module变更或追加的场合
        	if($scope.popInfo.isShow==2)
        	{
            	if($scope.moduleForm.$valid)
            	{
            		$scope.doUpdate();
            		$scope.closeThisDialog();
            	}
        	} 
        	//controller变更或追加的场合
        	if($scope.popInfo.isShow==3)
        	{
            	if($scope.controllerForm.$valid)
            	{
            		$scope.doUpdate();
            		$scope.closeThisDialog();
            	}
        	} 
        	//action变更或追加的场合
        	if($scope.popInfo.isShow==4)
        	{
            	if($scope.actionForm.$valid)
            	{
            		$scope.doUpdate();
            		$scope.closeThisDialog();
            	}
        	}    
        };
																			
    }])	
});