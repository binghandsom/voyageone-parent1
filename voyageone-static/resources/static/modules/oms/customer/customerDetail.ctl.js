/**
 * @Name:    customerDetailController.js
 * @Date:    2015/3/31
 * @User:    sky
 * @Version: 1.0.0
 */

define(function (require) {
    var omsApp = require('modules/oms/oms.module');
//    require('modules/oms/directives/tab.directive');
    require('modules/oms/customer/customerDetail.service');
    require('modules/oms/customer/customer.service');
//    require('modules/oms/directives/selectLine.directive');
    require('modules/oms/services/orderDetailInfo.service');
    require('modules/oms/services/addNewOrder.service');
    require('modules/oms/popup/popAddNoteCustomer.ctl');
    
    omsApp.controller('customerDetailController', ['$scope', '$q', '$location', 'ngDialog', 'customerDetailService','customerService','orderDetailInfoService', 'newOrderService', 'omsPopupPages', 'omsRoute',
        function ($scope, $q, $location, ngDialog, customerDetailService,customerService,orderDetailInfoService, newOrderService, omsPopupPages, omsRoute) {
    		$scope.grid_columnDefines1 = [];
    		$scope.grid_columnDefines2 = [];
    		var currentCustomer = "";
    		//防止客户，选中某条notes，再点“添加note”,弹出POPUP后再点击取消，再点击编辑按钮，notesId丢失问题；
    		var currentNoteId = "";
    		
    		//客户相关订单详情双击跳转到订单详细页面
    		$scope.onDblClickRow = function(row) {

				var orderDetailHash = omsRoute.oms_orders_orderdetail.hash.substring (omsRoute.oms_orders_orderdetail.hash.indexOf (':'), omsRoute.oms_orders_orderdetail.hash.length);
				var sourceOrderId = $scope.customerOrderDetailForGrid[row.rowIndex].sourceOrderId;

				orderDetailInfoService.setOrderBeforePageUrl($location.path());

				// 跳转到详细页面
				$location.path (omsRoute.oms_orders_orderdetail.hash.replace(orderDetailHash, sourceOrderId));
        	};
    		
    		// This customer's orders
    		$scope.grid_columnDefines1 = 
                [
                 {field: "orderNumber", displayName:"TXT_ORDERS_SEARCH_SEARCH_ORDER_NUMBER", width: '180px'},
                 {field: "sourceOrderId", displayName:"TXT_ORDERS_SEARCH_SEARCH_WEB_ORDER", width: '200px'},
                 {field: "marketName", displayName:"TXT_ORDERS_SEARCH_SEARCH_CHANNEL", width: '180px'},
                 {field: "orderDate", displayName:"TXT_ORDERS_SEARCH_SEARCH_ORDER_DATE", width: '180px'},
                 {field: "finalGrandTotal", displayName:"TXT_ORDERS_SEARCH_SEARCH_GRAND_TOTAL_AMOUNT", width: '120px'},
                 {field: "balanceDue", displayName:"TXT_ORDERS_SEARCH_BALANCE_DUE", width: '120px'},
             ];
    	
    		// This customer's transactions
    		$scope.grid_columnDefines2 = 
                [
                 {field: "date", displayName:"TXT_ORDERS_SEARCH_SEARCH_ORDER_DATE", width: '200px'},
                 {field: "description", displayName:"TXT_ORDER_DESCRIPTION", width: '300px'},
                 {field: "amount", displayName:"TXT_OMS_AMOUNT", width: '200px'},
                 //{field: "type", displayName:"TXT_ORDER_TYPE", width: '200px'},
                ];
        
    		$scope.gridCustomerOrder = {
    			data: 'customerOrderDetailForGrid',
    			//selectedItems: $scope.orderDetailSelections,
    			multiSelect: false,
    			columnDefs: 'grid_columnDefines1',
    			enableColumnResize: true,
    			showGroupPanel: false,                
    			rowTemplate: '<div ng-dblclick="onDblClickRow(row)" ng-repeat="col in renderedColumns" ng-class="col.colIndex()" class="ngCell {{col.cellClass}}"><div class="ngVerticalBar" ng-style="{height: rowHeight}" ng-class="{ ngVerticalBarVisible: !$last }">&nbsp;</div><div ng-cell></div></div>'
            	};
        
    		$scope.gridCustomerTransaction = {
    			data: 'customerTransactionDetailForGrid',
                //selectedItems: $scope.orderDetailSelections,
                multiSelect: false,
                columnDefs: 'grid_columnDefines2',
                enableColumnResize: true,
                showGroupPanel: false
            };
        
    		$scope.initialize = function () {
    			$scope.isSelected = false;
    			$scope.hidenWenHasNotes = false;
    			customerDetailService.doCustomerDetailSearch().then(function (response) {
    				currentCustomer = response.data.customerInfo[0];
    				//客户详情
    				$scope.customerDetail = currentCustomer;
    				//客户订单详情
    				$scope.customerOrderDetailForGrid = response.data.customerOrderList;
    				//客户交易详情
    				$scope.customerTransactionDetailForGrid = response.data.customerTransactionList;
    				//客户Notes
    				$scope.customerNotesList = response.data.customerNotes;
    				if($scope.customerNotesList.length == 0){
    					$scope.hidenWenHasNotes = true;
    				}
    				//默认显示第一条Notes
    				$scope.notesDetail = response.data.customerNotes[0];
    				//默认将第一条Notes的id和content存入session,方便编辑Notes
    				if (!response.data.customerNotes[0]) {
    					customerDetailService.setNotesId(null);
    					customerDetailService.setNotesContent("");
    					$scope.isEditShow = false;
    				} else {
    					customerDetailService.setNotesId(response.data.customerNotes[0].id);
    					customerDetailService.setNotesContent(response.data.customerNotes[0].notes);
    					$scope.isEditShow = true;
    				}
    				if (!$scope.$$phase) {
    					$scope.$apply();
    				}
    			});
            
    		};
        
    		//单击nots列表展示详细的notes
    		$scope.showNotesDetail = function (notesInfo, index) {
    			$scope.notesDetail = notesInfo;
    			$scope.isSelected = index; 
    			//设置选中Li中的notes信息，为编辑notes做准备
    			customerDetailService.setNotesId(notesInfo.id);
    			customerDetailService.setNotesContent(notesInfo.notes);
    			currentNoteId = notesInfo.id;
    		};
        
    		//notes保存按钮
    		/*$scope.addNotes = function () {
    			var data = {id:customerDetailService.getNotesId(), numericKey: $scope.customerDetail.customer_id, notes:$scope.newNotes};
    			customerDetailService.doSaveNotes(data).then(function (response) {
    				$scope.disShowPopUp ();
    				$scope.initialize();
    			});
    		};*/
        
    		//展示popUp,点击添加备注按钮
    		/*$scope.showPopUp = function () {
    			$scope.isShow = true;
    			$scope.newNotes = "";
    			//清除session中的Id区分是newNotes还是updateNoets
    			customerDetailService.setNotesId(null);
    		};*/
        
    		//隐藏popUp
    		/*$scope.disShowPopUp = function () {
    			$scope.isShow = false;
    		};*/
        
    		//添加订单按钮
    		$scope.addOrder = function () {
				var orderInfo = {};
				orderInfo.customerInfo = currentCustomer;
				orderInfo.orderChannelId = currentCustomer.orderChannelId;
    			newOrderService.setCurrentOrderInfo(orderInfo);
    			newOrderService.setOrderBeforePageUrl ($location.path());
    			$location.path(omsRoute.oms_orders_addneworder.hash);
    		};  
        
    		//编辑备注按钮
    		/*$scope.editNotes = function () {
    			$scope.newNotes = customerDetailService.getNotesContent();
    			if(customerDetailService.getNotesId() == "null" ){
    				customerDetailService.setNotesId(currentNoteId);
    			}
    			$scope.isShow = true;
    		}*/
    		
    		//新建或编辑Notes
    		$scope.popCustomerNotes = function (type) {
    			if(type == 1){
    				//新建备注
        			$scope.newNotes = "";
        			//清除session中的Id区分是newNotes还是updateNoets
        			customerDetailService.setNotesId(null);
    			}else{
    				//编辑备注
        			$scope.newNotes = customerDetailService.getNotesContent();
        			if(customerDetailService.getNotesId() == "null" ){
        				customerDetailService.setNotesId(currentNoteId);
        			}
    			}
    		    ngDialog.open({
    		    	template: omsPopupPages.popAddNoteCustomer.page,
    		    	controller: omsPopupPages.popAddNoteCustomer.controller,
    		    	scope: $scope
    		    });
    		}
    	}]);

});
