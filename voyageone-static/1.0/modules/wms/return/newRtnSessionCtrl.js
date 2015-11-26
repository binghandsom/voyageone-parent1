/**
 * @User: sky
 * @Date: 2015-04-09 17:16:50
 * @Version: 0.0.1
 */
define([
	"modules/wms/wms.module",
	"modules/wms/directives/popReturnInfo/popReturnInfo",
	"components/directives/paging",
	"modules/wms/return/returnService",
	"components/directives/getFocus",
	"components/directives/enterClick",
	"components/services/printService",
	"components/directives/dialogs/dialogs"
], function (wms) {
	wms
		.controller("newRtnSessionCtrl", [
			"$scope",
			"$location",
			"returnService",
			"$routeParams",
			"printService",
			"wmsConstant",
			"vAlert",
			"vConfirm",
			"notify",
			newRtnSessionCtrl
		]);

	function newRtnSessionCtrl ($scope, $location, returnService, $routeParams, printService, wmsConstant, alert, confirm, notify) {

		$scope.returnInfo = [];

		$scope.search =[];

		//是否显示选择仓库的下拉框(newSession的时候显示)
		$scope.isShow = isShow;

		//编辑行
		$scope.editRow = editRow;

		//删除行
		$scope.delRow = deleteRow;

		//删除行
		$scope.printRow = printRow;

		//页面变量Map
		var vm = $scope.vm = [];

		//页面初始化
		$scope.initialize = initialize;

		//下拉框值变化的时候根据sessionName查询数据库找到对应的session的内容
		$scope.onSelectChang = onSelectChang;

		//点击coloseSession的时候调用
		$scope.doCloseSession = doCloseSession;

        //扫描后清空输入框
        $scope.scanOrderNumber = scanOrderNumber;

        function scanOrderNumber(){
            $scope.orderNumber = "";
        }

		function initialize(){
			returnService.doNewSessionInit({
					"returnCondition": wmsConstant.return.typeId.returnCondition,
					"returnExpress": wmsConstant.return.typeId.returnExpress,
					"returnReason": wmsConstant.return.typeId.returnReason
				}).then(function(response) {
					$scope.processingSessionList = response.data.processingSessionList;
					var returnCondition = response.data.returnCondition,
						returnExpress = response.data.returnExpress,
						returnReason = response.data.returnReason,
						userStore = response.data.userStore;
					$scope.userStore = userStore;
				    $scope.returnTypeFlg = response.data.returnType;
					if (userStore.length == 1){
						$scope.search.storeId = userStore[0].store_id;
					}
					$scope.returnCondition = returnCondition;
					$scope.returnExpress = returnExpress;
					//快递公司默认无初始值
					//$scope.returnInfo.received_from = returnExpress[0].id;
					$scope.returnReason = returnReason;
					$scope.returnInfo.reason = returnReason[0].id;
					$scope.search.sessionId = $routeParams.sessionId;


				//监控sessionId,当有变化时候，查询数据库找到对应的session内容
				$scope.$watch("search.sessionId", function(value){
					$scope.onSelectChang(value);
					getSessionStore(value);
				});
				});
		}

		function onSelectChang(id){
			returnService.doGetSessionInfo({"id" : id} , $scope).then(function(response){
				$scope.vm.returnList = response.data.returnList;
			});
		}

		function doCloseSession(){
			var sessionId = $scope.search.sessionId;
			if(!sessionId){
				alert("WMS_RETURN_NO_SESSION");
				return;
			}
			confirm("WMS_RETURN_SESSION_CLOSE").then(function(){
				returnService.doCloseSession(sessionId, $scope).then(function(response){
					//response.data.successFlg ? notify(response.data.returnResMsg) : alert(response.data.returnResMsg);
					if(response.data.successFlg){
						notify.success("WMS_NOTIFY_OP_SUCCESS");
					}
					$location.path("/wms/return/sessionList");
				})
			})
		}

		function isShow(){
			return !$scope.search.sessionId;
		}

		function getSessionStore(value){
			var sessionObj = _.find($scope.processingSessionList, function(obj){
				return obj.return_session_id == value;
			});
			if(!!sessionObj){
				if (sessionObj.store_id != null) {
					$scope.search.storeId = sessionObj.store_id;
				}
			}
		}

		function editRow(){
			returnService.setSelectInfo($scope.vm.returnList.selected);
			returnService.setSessionId($scope.search.sessionId);
			$location.path('/wms/return/itemEdit').search({"storeId": $scope.search.storeId});
		}

		function deleteRow(){
			confirm("WMS_RETURN_ROW_DEL").then(function(){
				var res_id = $scope.vm.returnList.selected.res_id;
				returnService.doRemoveReturnInfo(res_id).then(function(response){
					//response.data.successFlg ? notify(response.data.returnResMsg) : alert(response.data.returnResMsg);
					if(response.data.successFlg){
						notify.success("WMS_NOTIFY_OP_SUCCESS");
					}
					returnService.setSessionId($scope.search.sessionId);
					returnService.doGetSessionInfo({"id" : $scope.search.sessionId}, $scope).then(function(response){
						$scope.vm.returnList = response.data.returnList;
						$scope.vm.returnList.count = response.data.returnList.length;
					});
				});
			});
		}

		function printRow(){
			var returnInfo = $scope.vm.returnList.selected;
			var data = [{"codition" : returnInfo.condition_id,
						 "modifier" : returnInfo.modifier,
						 "modified" : returnInfo.modified,
				 	     "location" : returnInfo.location_name,
				   		 "barcode" : returnInfo.barCode,
						 "sku" : returnInfo.sku,
						 "size" : returnInfo.size,
						 "product" : returnInfo.product_name,
						 "notes" : returnInfo.notes,
						 "label_type" : returnInfo.label_type}];
			var jsonData = JSON.stringify(data);
			printService.doPrint(wmsConstant.print.business.ReturnLabel, wmsConstant.print.hardware_key.Print_Return, jsonData);

		}
	}
});
