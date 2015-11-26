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

    coreApp.service('userManagerService',['$q', 'ajaxService', 'coreAction',
        function($q, ajaxService, coreAction) {
    	
    		this.HTML_CHECKBOX="<input type ='checkbox' disabled='true' ng-checked={{row.getProperty(col.field)}} />";

		    this.doInit = function() {
		        var defer = $q.defer();
		        ajaxService.ajaxPost(null,coreAction.core_setting_user_doUserInit,null)
		            .then(function(response) {
		                defer.resolve(response.data);
		            });
		        return defer.promise;
		    };
            /**
             * get all announcement.
             * @returns {*}
             */
            this.doGetUserList = function(data) {
                var defer = $q.defer();
                ajaxService.ajaxPost(data,coreAction.core_setting_user_doGetUserList,null)
                    .then(function(response) {
                        defer.resolve(response.data);
                    });
                return defer.promise;
            };
            
            this.doGetUserInfo = function(userId) {
                var defer = $q.defer();
                var data={userId:userId};
                ajaxService.ajaxPost(data,coreAction.core_setting_user_doGetUserInfo,null)
                    .then(function(response) {
                        defer.resolve(response.data);
                    });
                return defer.promise;
            };
            
            this.doUpdateUserInfo = function(data) {
                var defer = $q.defer();
                ajaxService.ajaxPost(data,coreAction.core_setting_user_doUpdateUserInfo,null)
                    .then(function(response) {
                        defer.resolve(response.data);
                    });
                return defer.promise;
            };
            this.doAddUserInfo = function(data) {
                var defer = $q.defer();
                ajaxService.ajaxPost(data,coreAction.core_setting_user_doAddUserInfo,null)
                    .then(function(response) {
                        defer.resolve(response.data);
                    });
                return defer.promise;
            };
            this.doDelUserInfo = function(userId) {
                var defer = $q.defer();
                var data={userId:userId};
                ajaxService.ajaxPost(data,coreAction.core_setting_user_doDelUserInfo,null)
                    .then(function(response) {
                        defer.resolve(response.data);
                    });
                return defer.promise;
            };
            
            this.doAddUserRole = function(data){
                var defer = $q.defer();
                ajaxService.ajaxPost(data,coreAction.core_setting_user_doAddUserRole,null)
                    .then(function(response) {
                        defer.resolve(response.data);
                    });
                return defer.promise;            	
            }
            this.doUpdateUserRole = function(data){
                var defer = $q.defer();
                ajaxService.ajaxPost(data,coreAction.core_setting_user_doUpdateUserRole,null)
                    .then(function(response) {
                        defer.resolve(response.data);
                    });
                return defer.promise;            	
            }
            
            this.doAddUserPermission = function(data){
                var defer = $q.defer();
                ajaxService.ajaxPost(data,coreAction.core_setting_user_doAddUserPermission,null)
                    .then(function(response) {
                        defer.resolve(response.data);
                    });
                return defer.promise;            	
            }
            this.doUpdateUserPermission = function(data){
                var defer = $q.defer();
                ajaxService.ajaxPost(data,coreAction.core_setting_user_doUpdateUserPermission,null)
                    .then(function(response) {
                        defer.resolve(response.data);
                    });
                return defer.promise;            	
            }
        	this.doGetRoleList=function(){
                var defer = $q.defer();
                ajaxService.ajaxPost(null,coreAction.core_setting_role_doGetRoleList,null)
                    .then(function(response) {
                        defer.resolve(response.data);
                    });
                return defer.promise; 
        	}
    }]);
    
    coreApp.service('roleManagerService',['$q', 'ajaxService', 'coreAction',
        function($q, ajaxService, coreAction) {
    	
    	this.doGetRoleList=function(){
            var defer = $q.defer();
            ajaxService.ajaxPost(null,coreAction.core_setting_role_doGetRoleList,null)
                .then(function(response) {
                    defer.resolve(response.data);
                });
            return defer.promise; 
    	};
    	this.doUpdateRole=function(data){
            var defer = $q.defer();
            ajaxService.ajaxPost(data,coreAction.core_setting_role_doUpdateRole,null)
                .then(function(response) {
                    defer.resolve(response.data);
                }, function(response) {
                    defer.reject();
                });
            return defer.promise; 
    	};  
    	this.doAddRole=function(data){
            var defer = $q.defer();
            ajaxService.ajaxPost(data,coreAction.core_setting_role_doAddRole,null)
                .then(function(response) {
                    defer.resolve(response.data);
                }, function(response) {
                    defer.reject();
                });
            return defer.promise; 
    	};       	
    	this.doGetRoleInfoById= function(Id) {
            var defer = $q.defer();
            var data={role_id:Id};
            ajaxService.ajaxPost(data,coreAction.core_setting_role_doGetRoleInfoById,null)
                .then(function(response) {
                    defer.resolve(response.data);
                });
            return defer.promise;
        };
        
        this.doUpdateRolePermission=function(data){
            var defer = $q.defer();
            ajaxService.ajaxPost(data,coreAction.core_setting_role_doUpdateRolePermission,null)
                .then(function(response) {
                    defer.resolve(response.data);
                }, function(response) {
                    defer.reject();
                });
            return defer.promise; 
    	};       	
        this.doAddRolePermission=function(data){
            var defer = $q.defer();
            ajaxService.ajaxPost(data,coreAction.core_setting_role_doAddRolePermission,null)
                .then(function(response) {
                    defer.resolve(response.data);
                }, function(response) {
                    defer.reject();
                });
            return defer.promise; 
    	};  
    }]);
    
    coreApp.service('applicationManagerService',['$q', 'ajaxService', 'coreAction',
	      function($q, ajaxService, coreAction) {
        this.doajaxPost=function(data,url){
            var defer = $q.defer();
            ajaxService.ajaxPost(data,url,null)
                .then(function(response) {
                    defer.resolve(response.data);
                }, function(response) {
                    defer.reject();
                });
            return defer.promise; 
    	}; 
	  }]);
});