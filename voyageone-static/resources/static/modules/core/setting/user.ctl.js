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

    accountApp.controller('userController', ['$scope', 'userManagerService', '$location','coreRoute',
        function($scope, userManagerService,$location,coreRoute) {

    		//当前选择的用户id
        	$scope.currentUserId=null;
    	
        	//初始化
            $scope.initialize = function() {
            	 $scope.initGrid();
            	 $scope.search={};
            	 //初始化  从服务器取得公司和权限的列表
            	 $scope.doInit();
            };
            //初始化Grid
            $scope.initGrid=function(){
            	//初始化 role表
            	$scope.grid_roleColumnDefines =
                    [
                        {field: "role",			displayName:"GRID_TITLE_TXT_USER_MANATER_ROLE",			width: '107px'},
                        {field: "property",		displayName:"GRID_TITLE_TXT_USER_MANATER_PROPERTY",		width: '134px'},
                        {field: "description",	displayName:"GRID_TITLE_TXT_USER_MANATER_DESCRIPTION",	width: '131px'},
                        {field: "active",		displayName:"TITLE_TXT_ACTIVE",							width: '60px' , cellType:'checkbox', checkboxDisabled:true}
                        
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
                        multiSelect:false
                    };
            	//初始化permission表
                $scope.grid_permissionColumnDefines =
                    [
                     {field: "property",displayName:"GRID_TITLE_TXT_USER_MANATER_PROPERTY",		width: '134px'},
                     {field: "application",	displayName:"TITLE_TXT_APPLICATION",								width: '134px'},
                     {field: "module",		displayName:"TITLE_TXT_MODULE",									width: '130px'},
                     {field: "controller",	displayName:"GRID_TITLE_TXT_USER_MANATER_CONTROLLER",	width: '146px'},
                     {field: "action",	displayName:"GRID_TITLE_TXT_USER_MANATER_ACTION",		width: '144px'},
                     {field: "value",		displayName:"GRID_TITLE_TXT_USER_MANATER_VALUE",		width: '60px', cellType:'checkbox', checkboxDisabled:true},
                     {field: "active",		displayName:"TITLE_TXT_ACTIVE",							width: '60px', cellType:'checkbox', checkboxDisabled:true}
                    ];   
                
                $scope.gridUserPermissionOptions = {
                        data: 'grid_userPermissionItems',
                        
                        enablePaging: false,
                        enableColumnResize: true,
                        showFooter: false,
                        multiSelect: true,
                        showSelectionCheckbox: false,
                        useExternalSorting: false,
                        columnDefs:'grid_permissionColumnDefines',
                        multiSelect:false
                    };
            }
            
            $scope.grid_pagingOptions = {
                    pageSizes: [10],
                    pageSize: 10,
                    currentPage: 1
                };
            
            // [上一页，下一页，首页，末页]按钮按下，或者[指定页编号]有变话的场合
            $scope.$watch('grid_pagingOptions', function (newVal, oldVal) {
                    //if (newVal !== oldVal && newVal.currentPage !== oldVal.currentPage) {
                if (newVal !== oldVal) {
                	$scope.doSearch();
                }
            }, true);
            

            // 存放[列]的具体内容
            $scope.grid_userColumnDefines =
                [
                    {field: "username",		displayName:"GRID_TITLE_TXT_USER_MANATER_USERNAME",		width: '150px'},
                    {field: "company",		displayName:"GRID_TITLE_TXT_USER_MANATER_COMPANY",		width: '106px'},
                    {field: "first_name",	displayName:"GRID_TITLE_TXT_USER_MANATER_FIRST_NAME",	width: '138px'},
                    {field: "last_name",	displayName:"GRID_TITLE_TXT_USER_MANATER_LAST_NAME",	width: '138px'},
                    {field: "email",		displayName:"GRID_TITLE_TXT_USER_MANATER_EMAIL",		width: '384px'},
                    {field: "active",		displayName:"TITLE_TXT_ACTIVE",									width: '94px', cellType:'checkbox', checkboxDisabled:true},
                    {field: "is_superuser",	displayName:"GRID_TITLE_TXT_USER_MANATER_SUPERUSER",								width: '127px', cellType:'checkbox', checkboxDisabled:true},
                    {field: "null",			displayName:"",											width: '80px'}
                ];
           
            selectionChange =function(rowItem, event) {
            	
            	if(rowItem.selected && $scope.currentUserId!=$scope.grid_searchListItems[rowItem.rowIndex].user_id)
            	{
	            	$scope.currentUserId=$scope.grid_searchListItems[rowItem.rowIndex].user_id;
	            	userManagerService.doGetUserInfo($scope.grid_searchListItems[rowItem.rowIndex].user_id)
	            	 .then(function(response) {
	            		
	                	$scope.grid_userRoleItems=response.user_role_property;
	                	$scope.grid_userPermissionItems=response.user_permission;
	                	
	                })
                }
            }
            $scope.gridUserListOptions = {
                    data: 'grid_searchListItems',
                    enablePaging: true,
                    enableColumnResize: true,
                    showFooter: true,
                    multiSelect: true,
                    showSelectionCheckbox: false,
                    useExternalSorting: false,
                    columnDefs:'grid_userColumnDefines',
                    totalServerItems: 'grid_totalServerItems',
                    multiSelect:false,
                    pagingOptions: $scope.grid_pagingOptions,
                    filterOptions: $scope.grid_filterOptions,
//                    afterSelectionChange:selectionChange,
                    onDblClick:"onDblClick",
                };
           
            //检索联系人
            $scope.doSearch=function(){
            	//计算开始的位置
            	var offset = $scope.grid_pagingOptions.pageSize*($scope.grid_pagingOptions.currentPage-1);
            	
            	var data={offset:offset,							//起始位置
            			rows:$scope.grid_pagingOptions.pageSize,	//条数
            			like_name:$scope.search.username,			//检索条件 username
            			like_company:$scope.search.company};		//检索条件 company
            	//取得数据
            	userManagerService.doGetUserList(data)
                .then(function(response) {
                	$scope.grid_searchListItems=response.data;
                	$scope.grid_totalServerItems=response.totalCnt;
                });
            };
            $scope.doInit=function(){
            	userManagerService.doInit()
                .then(function(response) {
                	$scope.doSearch();
                	sessionStorage.setItem("all_role_permission", JSON.stringify(response));
                });
            }
            //双击事件 
            $scope.onDblClick = function (rowItem, source) {
                // 跳转到用户编辑页面
            	var aa= JSON.stringify($scope.grid_searchListItems[rowItem.rowIndex]);
            	sessionStorage.setItem("setting_user", JSON.stringify($scope.grid_searchListItems[rowItem.rowIndex]));
                $location.path(coreRoute.core_setting_userdetail.hash);
            }
            //追加用户
            $scope.doAddUser= function(){
            	// 跳转到用户添加页面
            	sessionStorage.removeItem("setting_user");
            	$location.path(coreRoute.core_setting_userdetail.hash);
            }
            //删除用户
            $scope.doDelUser= function(){
            	userManagerService.doDelUserInfo($scope.currentUserId)
           	 .then(function(response) {
           		$scope.initialize();
               })
            }
        }]);
});
