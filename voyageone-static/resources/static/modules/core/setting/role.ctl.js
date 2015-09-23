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
//    require('modules/core/directives/popUserPermission.directive');
//    require('modules/core/directives/popRole.directive');
    require('modules/core/popup/popRole.ctl');
    require('modules/core/popup/popUserPermission.ctl');

    accountApp.controller('roleController', ['$scope', 'userManagerService','roleManagerService','$location','coreRoute','ngDialog',
        function($scope, userManagerService,roleManagerService,$location,coreRoute,ngDialog) {

            /**
             * get the public and company announcement info.
             */
    		var _ = require('underscore');
    		$scope.currentId=null;						//当前选择的id
    		$scope.grid_selections=[];	
        	$scope.popUserPermission={};
        	$scope.popUserPermission.permission={};
        	$scope.popRole={};
        	$scope.filte={property_id:null,application_id:null};
        	$scope.filterOptions={};
        	$scope.data=null;
            $scope.initialize = function() {
            	roleManagerService.doGetRoleList()
                .then(function(response) {
                	$scope.grid_roleList=response;
                	
                    userManagerService.doInit()
                    .then(function (response) {
                        $scope.popUserPermission.applicationData=response.application;
                        $scope.popUserPermission.propertyData=response.property;
                    });
                    
                    
                });
            };
            
            $scope.creadfilterText = function(type){
            	if(type == 1)
            	{
            		$scope.popUserPermission.moduleData=_.findWhere($scope.popUserPermission.applicationData,{id:$scope.filte.application_id}).children;
            		$scope.popUserPermission.controllerData=[];
            	}else if(type == 2){
            		$scope.popUserPermission.controllerData=_.findWhere($scope.popUserPermission.moduleData,{id:$scope.filte.module_id}).children;
            	}
            	$scope.filterOptions.filterText=""
            	_.mapObject($scope.filte,function(val,key){
            		if(val != '' & val != null)
            		{
            			$scope.filterOptions.filterText+=key+':'+val+';';
            		}
            		
            	})
            }
            //单击一条role数据后触发的事件
            selectionChange =function(rowItem, event) {
            	
            	if(rowItem.selected && $scope.currentId!=$scope.grid_roleList[rowItem.rowIndex].id)
            	{
            		//获取该role的权限数据
	            	$scope.currentId=$scope.grid_roleList[rowItem.rowIndex].id;
	            	roleManagerService.doGetRoleInfoById($scope.grid_roleList[rowItem.rowIndex].id)
	            	 .then(function(response) {
	                	$scope.grid_rolePermissionList=response;
	                	
	                })
                }
            };
            $scope.grid_roleColumnDefines =
                [
                    {field: "role",		displayName:"GRID_TITLE_TXT_USER_MANATER_ROLE",		width: '120px'},
                    {field: "is_admin",	displayName:"TITLE_TXT_ADMIN",	width: '82px', cellTemplate:userManagerService.HTML_CHECKBOX},
                    {field: "active",		displayName:"TITLE_TXT_ACTIVE",									width: '71px', cellTemplate:userManagerService.HTML_CHECKBOX}
                ];
            $scope.gridRoleListOptions = {
            		data: 'grid_roleList',
                    enablePaging: false,
                    enableColumnResize: true,
                    showFooter: false,
                    multiSelect: true,
                    showSelectionCheckbox: false,
                    useExternalSorting: false,
                    columnDefs:'grid_roleColumnDefines',
                    afterSelectionChange:selectionChange,
                    onDblClick:"onEditRole",
                    selectedItems: $scope.grid_selections,
                    multiSelect:false
                };
            $scope.grid_rolePermissionColumnDefines =
                [
                 	{field: "property_id",displayName:"GRID_TITLE_TXT_USER_MANATER_PROPERTY",		width: '92px', visible:false},
                    {field: "property_name",displayName:"GRID_TITLE_TXT_USER_MANATER_PROPERTY",		width: '115px'},
                    {field: "application_id",	displayName:"application_id",						width: '76px', visible:false},
                    {field: "application",	displayName:"TITLE_TXT_APPLICATION",								width: '129px'},
                    {field: "module_id",	displayName:"module_id",								width: '76px',visible:false},
                    {field: "module",		displayName:"TITLE_TXT_MODULE",									width: '108px'},
                    {field: "controller_id",displayName:"GRID_TITLE_TXT_USER_MANATER_CONTROLLER",	width: '94px',visible:false},
                    {field: "controller",	displayName:"GRID_TITLE_TXT_USER_MANATER_CONTROLLER",	width: '140px'},
                    {field: "action_name",	displayName:"GRID_TITLE_TXT_USER_MANATER_ACTION",		width: '92px'},
                    {field: "value",		displayName:"GRID_TITLE_TXT_USER_MANATER_VALUE",		width: '90px', cellType:'checkbox', checkboxDisabled:true},
                    {field: "active",		displayName:"TITLE_TXT_ACTIVE",							width: '90px', cellType:'checkbox', checkboxDisabled:true}
                ];
//            // [上一页，下一页，首页，末页]按钮按下，或者[指定页编号]有变话的场合
//            $scope.$watch('grid_pagingOptions', function (newVal, oldVal) {
//                    //if (newVal !== oldVal && newVal.currentPage !== oldVal.currentPage) {
//                if (newVal !== oldVal) {
//                	var start=(newVal.currentPage-1)*newVal.pageSize;
//                	var end=start+newVal.pageSize;
//                	$scope.grid_rolePermissionList=$scope.data.slice(start, end);
//                }
//            }, true);
            
            $scope.gridRoleInfo = {
            		data: 'grid_rolePermissionList',
                    enablePaging: false,
                    enableColumnResize: true,
                    showFooter: false,
                    multiSelect: true,
                    filterOptions:$scope.filterOptions,
                    totalServerItems: 'grid_total',
                    showSelectionCheckbox: false,
                    useExternalSorting: false,
                    columnDefs:'grid_rolePermissionColumnDefines',
                    onDblClick:"onEditRolePermission",
                    multiSelect:false
                };   
            //双击事件  编辑role数据
            $scope.onEditRole = function (rowItem, source) {
                // 跳转到用户编辑页面
                $scope.popRole=rowItem.entity;
                ngDialog.open ({
                    template: '/VoyageOne/modules/core/popup/popRole.dialog.tpl.html',
                    controller: 'popRoleController',
                    scope: $scope
                });
            };
            //双击事件  编辑Permission数据            
            $scope.onEditRolePermission = function (rowItem, source) {
                // 跳转到用户编辑页面
            	$scope.popUserPermission.moduleData=_.findWhere($scope.popUserPermission.applicationData,{id:rowItem.entity.application_id}).children;
        		$scope.popUserPermission.controllerData=_.findWhere($scope.popUserPermission.moduleData,{id:rowItem.entity.module_id}).children;
        		$scope.popUserPermission.actionData=_.findWhere($scope.popUserPermission.controllerData,{id:rowItem.entity.controller_id}).children;
        		$scope.popUserPermission.permission=rowItem.entity;
        		$scope.popUserPermission.permission.role_id=$scope.currentId;
                ngDialog.open ({
                    template: '/VoyageOne/modules/core/popup/popUserPermission.dialog.tpl.html',
                    controller: 'popUserPermissionController',
                    scope: $scope
                });
            };
            //弹出role追加画面
            $scope.doAddRole = function(){
            	$scope.popRole={};
            	$scope.popRole.active=true;
                ngDialog.open ({
                    template: '/VoyageOne/modules/core/popup/popRole.dialog.tpl.html',
                    controller: 'popRoleController',
                    scope: $scope
                });
            };
            //弹出permission追加画面            
            $scope.doAddPermission = function(){
            	$scope.popUserPermission.permission={};
        		$scope.popUserPermission.permission.role_id=$scope.currentId;
            	$scope.popUserPermission.permission.active=true;
            	$scope.popUserPermission.permission.value=true;
                ngDialog.open ({
                    template: '/VoyageOne/modules/core/popup/popUserPermission.dialog.tpl.html',
                    controller: 'popUserPermissionController',
                    scope: $scope
                });
            };
            //
            $scope.doUpdatePermission = function(){
            	if($scope.popUserPermission.permission.id !== undefined)
            	{
            		roleManagerService.doUpdateRolePermission($scope.popUserPermission.permission)
	           	 	.then(function(response) {
                            window.location.reload();
	               })
            	}else{
            		roleManagerService.doAddRolePermission($scope.popUserPermission.permission)
	           	 	.then(function(response) {
	           	 		window.location.reload();
	               })
            	}
            };
            $scope.doUpdateRole = function(){
            	if($scope.popRole.id !== undefined)
            	{
            		roleManagerService.doUpdateRole($scope.popRole)
	           	 	.then(function(response) {
                            window.location.reload();
	               })
            	}else{
            		roleManagerService.doAddRole($scope.popRole)
	           	 	.then(function(response) {
	           	 		window.location.reload();
	               })
            	}
            };
            
            
        }]);
});
