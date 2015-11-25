/**
 * @Name:    homeController.js
 * @Date:    2015/2/3
 *
 * @User:    Edward
 * @Version: 1.0.0
 */

define(function (require) {
    var accountApp = require('modules/core/core.module');
    require('modules/core/setting/setting.service');
    require('modules/core/popup/popApp.ctl');
    accountApp.controller('applicationController', ['$scope', 'applicationManagerService', 'userManagerService', '$location', 'coreAction','ngDialog',
        function ($scope, applicationManagerService, userManagerService, $location, coreAction,ngDialog) {

    	
    		var _ = require('underscore');
    		//当前选择的module
            $scope.currentModuleItem = null;
            //当前选择的controller
            $scope.currentController = null;
            //当前选择的action
            $scope.currentAction = null;
            $scope.currentItem=null;
            //pop画面里的内容初始化
            $scope.popInfo = {};
            //pop画面关闭 0：关闭 1：app追加 2：module追加 3：controller追加 4：action追加
            $scope.popInfo.isShow = 0;
            url = [['', ''],
                [coreAction.core_setting_app_doAddApp, coreAction.core_setting_app_doUpdateApp],
                [coreAction.core_setting_app_doAddModule, coreAction.core_setting_app_doUpdateModule],
                [coreAction.core_setting_app_doAddController, coreAction.core_setting_app_doUpdateController],
                [coreAction.core_setting_app_doAddAction, coreAction.core_setting_app_doUpdateAction]];
            /**
             * 初始化
             */
            $scope.initialize = function () {

                $scope.doInit();
            };
            /**
             * 初始化界面 取得application到action的关系数据
             */
            $scope.doInit = function () {
            	$scope.batchAppShow=false;
                userManagerService.doInit()
                    .then(function (response) {
                        $scope.application = response.application;
                    });
            }
            /**
             * application单击事件
             * @param rowItem
             * @param event
             */
            selectionAppChange = function (rowItem, event) {
            	//选择的AppID发生变化的场合
                if (rowItem.selected && $scope.currentAppId != $scope.application[rowItem.rowIndex].id) {
                	//把之前module数据清空
                    if ($scope.currentModuleItem != null) {
                        $scope.currentModuleItem.selectionProvider.setSelection($scope.currentModuleItem, false);
                        $scope.currentModuleItem = null;
                        $scope.currentModuleId = -1;
                    }
                    if ($scope.currentController != null) {
                        $scope.currentController.selectionProvider.setSelection($scope.currentController, false);
                        $scope.currentController = null;
                        $scope.currentControllerId = -1;
                    }
                    
                    $scope.currentAppId = $scope.application[rowItem.rowIndex].id;
                    //根据新选择的Appid给module赋值
                    $scope.grid_moduleList = rowItem.entity.children;
                    $scope.grid_controllerList = [];
                    $scope.grid_actionList = [];
                    checkboxcheck();
                }
            };
            /**
             * Module单击事件
             * @param rowItem
             * @param event
             */
            selectionModuleChange = function (rowItem, event) {
            	//选择的ModuleID发生变化的场合
                if (rowItem.selected && $scope.currentModuleId != $scope.grid_moduleList[rowItem.rowIndex].id) {
                	//把之前controller数据清空
                    if ($scope.currentController != null) {
                        $scope.currentController.selectionProvider.setSelection($scope.currentController, false);
                        $scope.currentController = null;
                        $scope.currentControllerId = -1;
                    }

                    $scope.currentModuleItem = rowItem;
                    $scope.currentModuleId = $scope.grid_moduleList[rowItem.rowIndex].id;
                    //根据新选择的Module给controller赋值
                    $scope.grid_controllerList = rowItem.entity.children;
                    $scope.grid_actionList = [];
                    checkboxcheck();
                }
            };
            selectionControllerChange = function (rowItem, event) {
                if (rowItem.selected && $scope.currentControllerId != $scope.grid_controllerList[rowItem.rowIndex].id) {
                    if ($scope.currentAction != null) {
                        $scope.currentAction.selectionProvider.setSelection($scope.currentAction, false);
                        $scope.currentAction = null;
                    }

                    $scope.currentController = rowItem;
                    $scope.currentControllerId = $scope.grid_controllerList[rowItem.rowIndex].id;
                    $scope.grid_actionList = rowItem.entity.children;
                    checkboxcheck();
                }
            };

            selectionActionChange = function (rowItem, event) {
                if (rowItem.selected) {
                    $scope.currentAction = rowItem;
                }
            };
            $scope.grid_applicationColumnDefines =
                [
                    {field: "application", displayName: "TITLE_TXT_APPLICATION", width: '131px'},
                    {
                        field: "active",
                        displayName: "TITLE_TXT_ACTIVE",
                        width: '71px',
                        cellType: 'checkbox',
                        checkboxDisabled: true
                    }
                ];
            $scope.gridApplicationInfo = {
                data: 'application',
                enablePaging: false,
                enableColumnResize: true,
                showFooter: false,
                multiSelect: true,
                showSelectionCheckbox: false,
                useExternalSorting: false,
                columnDefs: 'grid_applicationColumnDefines',
                afterSelectionChange: selectionAppChange,
                onDblClick: "doUpdateApp",
                multiSelect: false
            };
            $scope.grid_moduleColumnDefines =
                [
                    {field: "module", displayName: "TITLE_TXT_MODULE", width: '100px'},
                    {
                        field: "active",
                        displayName: "TITLE_TXT_ACTIVE",
                        width: '71px',
                        cellType: 'checkbox',
                        checkboxDisabled: true
                    }
                ];

            $scope.gridModuleInfo = {
                data: 'grid_moduleList',
                enablePaging: false,
                enableColumnResize: true,
                showFooter: false,
                multiSelect: true,
                showSelectionCheckbox: false,
                useExternalSorting: false,
                columnDefs: 'grid_moduleColumnDefines',
                afterSelectionChange: selectionModuleChange,
                onDblClick: "doUpdateModule",
                multiSelect: false
            };
            $scope.grid_controllerColumnDefines =
                [
                    {field: "controller", displayName: "GRID_TITLE_TXT_USER_MANATER_CONTROLLER", width: '141px'},
                    {
                        field: "active",
                        displayName: "TITLE_TXT_ACTIVE",
                        width: '71px',
                        cellType: 'checkbox',
                        checkboxDisabled: true
                    }
                ];

            $scope.grid_controller = {
                data: 'grid_controllerList',
                enablePaging: false,
                enableColumnResize: true,
                showFooter: false,
                multiSelect: true,
                showSelectionCheckbox: false,
                useExternalSorting: false,
                columnDefs: 'grid_controllerColumnDefines',
                onDblClick: "doUpdateController",
                afterSelectionChange: selectionControllerChange,
                multiSelect: false
            };
            $scope.grid_actionColumnDefines =
                [
                    {field: "name", displayName: "GRID_TITLE_TXT_USER_MANATER_ACTION", width: '100px'},
                    {
                        field: "active",
                        displayName: "TITLE_TXT_ACTIVE",
                        width: '71px',
                        cellType: 'checkbox',
                        checkboxDisabled: true
                    }
                ];

            $scope.grid_action = {
                data: 'grid_actionList',
                enablePaging: false,
                enableColumnResize: true,
                showFooter: false,
                multiSelect: true,
                showSelectionCheckbox: false,
                useExternalSorting: false,
                columnDefs: 'grid_actionColumnDefines',
                onDblClick: "doUpdateAction",
                afterSelectionChange: selectionActionChange,
                multiSelect: false
            };

            /**
             * 追加app
             */
            $scope.doAddApp = function () {
                $scope.popInfo = {};
                $scope.popInfo.active = true;
                $scope.popInfo.isShow = 1;
                ngDialog.open ({
                    template: '/VoyageOne/modules/core/popup/popApp.dialog.tpl.html',
                    controller: 'popAppController',
                    scope: $scope
                });
            };
            /**
             * 追加module
             */
            $scope.doAddModule = function () {
                $scope.popInfo = {};
                $scope.popInfo.active = true;
                $scope.popInfo.application_id = $scope.currentAppId;
                $scope.popInfo.isShow = 2;
                ngDialog.open ({
                    template: '/VoyageOne/modules/core/popup/popApp.dialog.tpl.html',
                    controller: 'popAppController',
                    scope: $scope
                });
            };
            /**
             * 追加controller
             */
            $scope.doAddController = function () {
                $scope.popInfo = {};
                $scope.popInfo.active = true;
                $scope.popInfo.module_id = $scope.currentModuleId;
                $scope.popInfo.isShow = 3;
                ngDialog.open ({
                    template: '/VoyageOne/modules/core/popup/popApp.dialog.tpl.html',
                    controller: 'popAppController',
                    scope: $scope
                });
            };
            /**
             * 追加Action
             */
            $scope.doAddAction = function () {
            	checkboxcheck();
            	$scope.popInfo = {};
                $scope.popInfo.active = true;
                $scope.popInfo.controller_id = $scope.currentControllerId;
                $scope.popInfo.isShow = 4;
                ngDialog.open ({
                    template: '/VoyageOne/modules/core/popup/popApp.dialog.tpl.html',
                    controller: 'popAppController',
                    scope: $scope
                });
                
            };

            /**
             * 弹出更新app画面
             */
            $scope.doUpdateApp = function (rowItem) {
            	$.extend($scope.popInfo, rowItem.entity);
                $scope.popInfo.isShow = 1;
                $scope.currentItem=rowItem.entity;
                ngDialog.open ({
                    template: '/VoyageOne/modules/core/popup/popApp.dialog.tpl.html',
                    controller: 'popAppController',
                    scope: $scope
                });
            };
            /**
             * 弹出更新module画面
             */
            $scope.doUpdateModule = function (rowItem) {
            	$.extend( $scope.popInfo, rowItem.entity);
                $scope.popInfo.isShow = 2;
                $scope.currentItem=rowItem.entity;
                ngDialog.open ({
                    template: '/VoyageOne/modules/core/popup/popApp.dialog.tpl.html',
                    controller: 'popAppController',
                    scope: $scope
                });
            };
            /**
             * 弹出更新controller画面
             */
            $scope.doUpdateController = function (rowItem) {
            	$.extend( $scope.popInfo, rowItem.entity);
                $scope.popInfo.isShow = 3;
                $scope.currentItem=rowItem.entity;
                ngDialog.open ({
                    template: '/VoyageOne/modules/core/popup/popApp.dialog.tpl.html',
                    controller: 'popAppController',
                    scope: $scope
                });
            };
            /**
             * 弹出更新action画面
             */
            $scope.doUpdateAction = function (rowItem) {
            	$.extend($scope.popInfo, rowItem.entity);
                $scope.popInfo.isShow = 4;
                $scope.currentItem=rowItem.entity;
                ngDialog.open ({
                    template: '/VoyageOne/modules/core/popup/popApp.dialog.tpl.html',
                    controller: 'popAppController',
                    scope: $scope
                });
            };

            /**
             * 更新操作
             */
            $scope.doUpdate = function () {
                if ($scope.popInfo.isShow > 0) {
                	//追加的场合
                    if ($scope.popInfo.id === undefined) {
                    	//调用服务器的接口
                        applicationManagerService.doajaxPost($scope.popInfo, url[$scope.popInfo.isShow][0])
                            .then(function (response) {
                            	response.children=[];
                                switch ($scope.popInfo.isShow) {
                                    case 1:
                                        $scope.application.push(response);
                                        break;
                                    case 2:
                                        $scope.grid_moduleList.push(response);
                                        break;
                                    case 3:
                                        $scope.grid_controllerList.push(response);
                                        break;
                                    case 4:
                                        $scope.grid_actionList.push(response);
                                        break;
                                    default:
                                        break;
                                }
                                $scope.popInfo.isShow = 0;
                                checkboxcheck();
                            },function(i){
                            	$scope.popInfo.isShow = 0;
                            });
                    } else {
                        applicationManagerService.doajaxPost(_.omit($scope.popInfo,"children"), url[$scope.popInfo.isShow][1])
                            .then(function (response) {
                                $scope.popInfo.isShow = 0;
                                _.extend($scope.currentItem, $scope.popInfo);
                                checkboxcheck();
                            },function(i){
                            	$scope.popInfo.isShow = 0;
                            });
                    }
                }
            }
           
            $scope.batchAppshow= function () {
            	$scope.batchAppShow=!$scope.batchAppShow;
//            	$scope.batchApp = true;
            }
            $scope.batchApp = function() {
            	var data = {};
            	data.url=$scope.batchUrl;
            	 applicationManagerService.doajaxPost(data, coreAction.core_setting_app_doBatchApp)
                 .then(function (response) {
                	 window.location.reload();
                 },function(i){
                 	
                 });
            }
            function checkboxcheck(){
            	setTimeout(function(){
            		$("input:checkbox").each(function(){
                	    if($(this).attr("ng-checked")==="true"){
                	    	this.checked=true;
                	    }else if($(this).attr("ng-checked")==="false")
                	    {
                	    	this.checked=false;
                	    }
                	})
            	},5);
            }
        }]);
});
