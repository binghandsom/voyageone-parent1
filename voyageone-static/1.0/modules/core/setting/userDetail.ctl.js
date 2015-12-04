/**
 * @Name:    homeController.js
 * @Date:    2015/2/3
 *
 * @User:    Edward
 * @Version: 1.0.0
 */

define(function(require) {
    var accountApp = require('modules/core/core.module');
    require('modules/core/setting/setting.service');
    require('modules/core/popup/popUserRole.ctl');
    require('modules/core/popup/popUserPermission.ctl');
    require('modules/core/popup/popUserPassword.ctl');
    
    accountApp.controller('userDetailController', ['$scope', 'userManagerService','$location','coreRoute', 'ngDialog',
        function($scope, userManagerService,$location,coreRoute,ngDialog) {

            /**
             * get the public and company announcement info.
             */
            $scope.initialize = function() {
            	$scope.initGrid();
            	$scope.user={}; 
            	$scope.popUserPermission={};
            	$scope.popUserPermission.permission={};
            	$scope.popUserRole={};
            	$scope.popUserRole.role={};
            	$scope.popUserPassword={};
            	$scope.userinfo=sessionStorage.getItem("setting_user");
            	
            	$scope.initData();
            	
            	all_role_permission=JSON.parse(sessionStorage.getItem("all_role_permission"));
            	$scope.companyData=all_role_permission.company;
            	
            	$scope.popUserPermission.applicationData=all_role_permission.application;
            	$scope.popUserPermission.propertyData=all_role_permission.property;
            	
            	$scope.popUserRole.roleData=all_role_permission.role;
            	$scope.popUserRole.propertyData=all_role_permission.property;            	
            };
            $scope.initData=function(){
            	if($scope.userinfo != null)
            	{
            		$scope.user=JSON.parse($scope.userinfo);
            		$scope.user.timezone=parseInt($scope.user.timezone);
	            	userManagerService.doGetUserInfo($scope.user.user_id)
	            	 .then(function(response) {
	                	$scope.grid_userRoleItems=response.user_role_property;
	                	$scope.grid_userPermissionItems=response.user_permission;
	                	
	                })
            	}else{
            		//追加用户的场合 active默认为true
            		$scope.user.active=true;
            	}
            }
            $scope.initGrid= function(){
            	$scope.grid_roleColumnDefines =
                    [
                        {field: "role",       displayName:"GRID_TITLE_TXT_USER_MANATER_ROLE",       width: '120px'},
                        {field: "property",     displayName:"GRID_TITLE_TXT_USER_MANATER_PROPERTY",    width: '134px'},
                        {field: "active",     displayName:"TITLE_TXT_ACTIVE",    width: '71px', cellType:'checkbox', checkboxDisabled:true}
                    ];
                $scope.grid_permissionColumnDefines =
                    [
                        {field: "property",       displayName:"GRID_TITLE_TXT_USER_MANATER_PROPERTY",       width: '134px'},
                        {field: "controller",     displayName:"GRID_TITLE_TXT_USER_MANATER_CONTROLLER",    width: '130px'},
                        {field: "action",         displayName:"GRID_TITLE_TXT_USER_MANATER_ACTION",         width: '100px'},
                        {field: "active",     displayName:"TITLE_TXT_ACTIVE",    width: '110px', cellType:'checkbox', checkboxDisabled:true},
                        {field: "value",         displayName:"GRID_TITLE_TXT_USER_MANATER_VALUE",         width: '50px', cellType:'checkbox', checkboxDisabled:true}
                    ];   
                $scope.gridUserRoleOptions = {
                        data: 'grid_userRoleItems',
                        enablePaging: false,
                        enableColumnResize: true,
                        showFooter: false,
                        multiSelect: true,
                        showSelectionCheckbox: false,
                        useExternalSorting: false,
                        columnDefs:'grid_roleColumnDefines',
                        multiSelect:false,
                        onDblClick:"onEditRole"
                    };
                $scope.gridUserPermissionOptions = {
                        data: 'grid_userPermissionItems',
                        enablePaging: false,
                        enableColumnResize: true,
                        showFooter: false,
                        multiSelect: true,
                        showSelectionCheckbox: false,
                        useExternalSorting: false,
                        columnDefs:'grid_permissionColumnDefines',
                        multiSelect:false,
                        onDblClick:"onEditPermission"
                    };
            }
            $scope.update = function(){
            	if($scope.form_user.$valid)
            	{
	            	if($scope.userinfo != null)
	            	{
		            	userManagerService.doUpdateUserInfo($scope.user)
		           	 	.then(function(response) {
		           	 	 $scope.back();
		               })
	            	}else{
	            		
		            	userManagerService.doAddUserInfo($scope.user)
		           	 	.then(function(response) {
		           	 	 $scope.back();
		               })
	            	}
            	}
            };
            $scope.updatePassword = function(){
            	$scope.popUserPassword.isShow=undefined;
            	newpassword={};
            	newpassword.user_id=$scope.popUserPassword.user_id;
            	newpassword.password=$scope.popUserPassword.password;
            	newpassword.username=$scope.user.username;
            	newpassword.active=$scope.user.active;
            	userManagerService.doUpdateUserInfo(newpassword)
           	 	.then(function(response) {
           	 		alert("succeed");
           	 	 //$scope.back();
               })
            };
            /**
             * back to previous page.
             */
            $scope.back = function() {
            	sessionStorage.removeItem("setting_user");
            	$location.path(coreRoute.core_setting_user.hash);
            	
            };
         	//追加Role记录
            $scope.addRole = function() {
            	
            	$scope.popUserRole.role={};
            	$scope.popUserRole.role.user_id=$scope.user.user_id;
            	$scope.popUserRole.role.active=true;
                ngDialog.open ({
                    template: '/VoyageOne/modules/core/popup/popUserRole.dialog.tpl.html',
                    controller: 'popUserRoleController',
                    scope: $scope
                });

            };
         	//修改密码
            $scope.chgPassword = function() {
            	$scope.popUserPassword.user_id=$scope.user.user_id;
            	$scope.popUserPassword.password="";
                ngDialog.open ({
                    template: '/VoyageOne/modules/core/popup/popUserPassword.dialog.tpl.html',
                    controller: 'popUserPasswordController',
                    scope: $scope
                });
            };            
            //追加permission记录
            $scope.addPermission = function() {
            	$scope.popUserPermission.permission={};
            	$scope.popUserPermission.permission.user_id=$scope.user.user_id;
            	$scope.popUserPermission.permission.active=true;
            	$scope.popUserPermission.permission.value=true;
                ngDialog.open ({
                    template: '/VoyageOne/modules/core/popup/popUserPermission.dialog.tpl.html',
                    controller: 'popUserPermissionController',
                    scope: $scope
                });
            }            
            //双击事件 编辑role表
            $scope.onEditRole= function (rowItem, source) {
            	$scope.popUserRole.role=rowItem.entity;
            	$scope.popUserRole.role.user_id=$scope.user.user_id;
                ngDialog.open ({
                    template: '/VoyageOne/modules/core/popup/popUserRole.dialog.tpl.html',
                    controller: 'popUserRoleController',
                    scope: $scope
                });
            }
            //双击事件 编辑 permission表
            $scope.onEditPermission= function (rowItem, source) {
        		
            	$scope.popUserPermission.moduleData=_.findWhere($scope.popUserPermission.applicationData,{id:rowItem.entity.application_id}).children;
        		$scope.popUserPermission.controllerData=_.findWhere($scope.popUserPermission.moduleData,{id:rowItem.entity.module_id}).children;
        		$scope.popUserPermission.actionData=_.findWhere($scope.popUserPermission.controllerData,{id:rowItem.entity.controller_id}).children;
        		
        		$scope.popUserPermission.permission=rowItem.entity;
        		$scope.popUserPermission.permission.user_id=$scope.user.user_id;
                ngDialog.open ({
                    template: '/VoyageOne/modules/core/popup/popUserPermission.dialog.tpl.html',
                    controller: 'popUserPermissionController',
                    scope: $scope
                });
            }
            
            $scope.doUpdateRole = function(){
            	if($scope.popUserRole.role.id !== undefined)
            	{
	            	userManagerService.doUpdateUserRole($scope.popUserRole.role)
	           	 	.then(function(response) {
	           	 	window.location.reload();
	               })
            	}else{
	            	userManagerService.doAddUserRole($scope.popUserRole.role)
	           	 	.then(function(response) {
	           	 	window.location.reload();
	               })
            	}
            };
            
            $scope.doUpdatePermission = function(){
            	if($scope.popUserPermission.permission.id !== undefined)
            	{
	            	userManagerService.doUpdateUserPermission($scope.popUserPermission.permission)
	           	 	.then(function(response) {
//	           	 		$scope.initData();
//                            $scope.refresh();
                            window.location.reload();
	               })
            	}else{
	            	userManagerService.doAddUserPermission($scope.popUserPermission.permission)
	           	 	.then(function(response) {
//	           	 		$scope.initData();
	           	 	window.location.reload();
//                            $scope.refresh();
	               })
            	}
            };
        }]);
});
