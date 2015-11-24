define(function(require) {
    var omsApp = require('modules/core/core.module');

    omsApp.controller('popUserPermissionController', ['$scope', function ($scope) {																					
    	var _ = require('underscore');

    	$scope.initialize=function(){
    		
    	}
    	/**
         * 根据下拉列表框选择的内容 给下个一下拉列表框赋值
         */
    	$scope.onSelectChang = function(id,parent){
        	if(parent[0].application !== undefined)
        	{
        		$scope.popUserPermission.permission.module_id=undefined;
        		$scope.popUserPermission.permission.controller_id=undefined;
        		$scope.popUserPermission.permission.action_id=undefined;
        		$scope.popUserPermission.moduleData=_.findWhere(parent,{id:id}).children;
        		if($scope.popUserPermission.moduleData.length>0 && $scope.popUserPermission.permission.id == undefined)
    			{
        			$scope.popUserPermission.moduleData = [{id:"-1",module:"ALL"}].concat($scope.popUserPermission.moduleData);
    			}
        		$scope.popUserPermission.controllerData=[];
        		$scope.popUserPermission.actionData=[];
        	}else if(parent[0].module !== undefined){
        		$scope.popUserPermission.permission.controller_id=undefined;
        		$scope.popUserPermission.permission.action_id=undefined;
        		$scope.popUserPermission.controllerData=_.findWhere(parent,{id:id}).children;
        		if(_.findWhere(parent,{id:id}).children !== undefined && $scope.popUserPermission.controllerData.length>0 && $scope.popUserPermission.permission.id == undefined)
        		{
        			$scope.popUserPermission.controllerData=[{id:"-1",controller:"ALL"}].concat($scope.popUserPermission.controllerData);
        		}
        		$scope.popUserPermission.actionData=[];
        	}else{
        		$scope.popUserPermission.permission.action_id=undefined;
        		$scope.popUserPermission.actionData=_.findWhere(parent,{id:id}).children;
        		if($scope.popUserPermission.actionData.length>0 && $scope.popUserPermission.permission.id == undefined)
        		{
        			$scope.popUserPermission.actionData=[{id:"-1",name:"ALL"}].concat($scope.popUserPermission.actionData);
        		}
        	}
        	
        }
    	$scope.doOk = function() {
        	if($scope.userPermissionForm.$valid)
        	{
        		$scope.doUpdatePermission();
        		$scope.closeThisDialog();
        	}
            
        };
        
        $scope.valid = function(){
        	if(!$scope.userPermissionForm.$valid)
        	{
        		return false;
        	}
        	if($scope.popUserPermission.permission.module_id == "-1" 
        		|| $scope.popUserPermission.permission.controller_id == "-1"
        		|| $scope.popUserPermission.permission.action_id == "-1")
        	{
        		return true;
        	}
        	if($scope.popUserPermission.permission.module_id === undefined
        		|| $scope.popUserPermission.permission.controller_id === undefined
        		|| $scope.popUserPermission.permission.action_id === undefined)
        	{
        		return false;
        	}
        	return true;
        }
																			
    }])	
});
