/**
 * @Name:    homeController.js
 * @Date:    2015/2/3
 *
 * @User:    Edward
 * @Version: 1.0.0
 */

define(function(require) {
    var accountApp = require('modules/core/core.module');
    require('modules/core/manager/manager.service');
    require('modules/core/popup/popAddBatch.ctl');

    accountApp.controller('batchJobController', ['$scope', 'batchJobService', '$location','coreRoute','notify','ngDialog',
        function($scope, batchJobService,$location,coreRoute,notify,ngDialog) {

    	
        	//初始化
            $scope.initialize = function() {
            	 //初始化  从服务器取得公司和权限的列表
            	batchJobService.doGetBatchJobList().then(function(response) {
            		$scope.batchList=response;
            	});
            };
            
            $scope.update = function(job){
            	batchJobService.doUpdateJobRun(job).then(function(response) {
            		var op={"message":"success","status":"success"};
            		notify(op);
            	},function(){
            		var op={"message":"fail","status":"danger"};
            		notify(op);
            	});
            };
            
            $scope.doAdd = function(){
                ngDialog.open ({
                    template: '/VoyageOne/modules/core/popup/popAddBatch.dialog.tpl.html',
                    controller: 'popBatchController',
                    scope: $scope
                });
            }
           
        }]);
});
