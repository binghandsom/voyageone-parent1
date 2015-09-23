define(function(require) {
    var omsApp = require('modules/core/core.module');
    require('modules/core/manager/manager.service');
    
    omsApp.controller('popBatchController', ['$scope','batchJobService', function ($scope,batchJobService) {																					
    	var _ = require('underscore');

    	$scope.initialize=function(){
    		
    	}

    	$scope.doOk = function() {
    		
    		if(!isExist())
    		{
    			batchJobService.doinsertBatchJob($scope.pop).then(function(response) {
    				response.cfg_val1=1;
    				$scope.batchList.push(response);
    				$scope.closeThisDialog();
            	},function(){
            		alert("fail");
            	});
    			
    		}else{
    			alert("已存在");
    		}
        };
        
        function isExist(){
        	var isExist=false
        	_.each($scope.batchList, function(item){
        		if(_.isEqual(item.task,$scope.pop.task)){
        			isExist= true;
        		}
        	});
        	return isExist
        }
																			
    }])	
});
