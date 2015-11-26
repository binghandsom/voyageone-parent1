/**
 * @Name:    menuService.js
 * @Date:    2015/2/3
 *
 * @User:    Edward
 * @Version: 1.0.0
 */

define(function(require) {
    var coreApp = require('modules/core/core.module');
    //require('components/services/ajaxService');

    coreApp.service('batchJobService',['$q', 'ajaxService', 'coreAction',
        function($q, ajaxService, coreAction) {
    	
            this.doGetBatchJobList = function(){
                var defer = $q.defer();
                ajaxService.ajaxPost('','/core/manager/batchJob/doGetBatchJobList',null)
                    .then(function(response) {
                        defer.resolve(response.data);
                    });
                return defer.promise;            	
            }
            this.doUpdateJobRun = function(data){
                var defer = $q.defer();
                ajaxService.ajaxPost(data,'/core/manager/batchJob/doUpdateJobRun',null)
                    .then(function(response) {
                        defer.resolve(response.data);
                    });
                return defer.promise;            	
            }
            this.doinsertBatchJob = function(data){
                var defer = $q.defer();
                ajaxService.ajaxPost(data,'/core/manager/batchJob/doInsertBatchJob',null)
                    .then(function(response) {
                        defer.resolve(response.data);
                    });
                return defer.promise;            	
            }
    }]);
});