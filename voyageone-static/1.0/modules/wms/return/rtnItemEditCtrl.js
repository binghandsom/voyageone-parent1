/**
 * @User: sky
 * @Date: 2015-04-09
 * @Version: 0.0.1
 */
define(["modules/wms/wms.module",
        "modules/wms/return/returnService",
		"components/directives/dialogs/dialogs",
	    "modules/wms/directives/popReturnInfo/popReturnInfoService"
], function (wms) {
    wms.controller("rtnItemEditCtrl", 
    				[
                      "$scope",
                      "returnService",
                      "$location",
					  "vAlert",
				   	  "vConfirm",
					  "notify",
					  "wmsConstant",
					  "$routeParams",
					  "popReturnInfoService",
            function ($scope, returnService, $location, alert, confirm, notify, wmsConstant, $routeParams, popReturnInfoService) {

				$scope.initialize = function() {
					$scope.returnInfo = returnService.getSelectInfo();
					returnService.doItemEditInit({
							//"returnStatus": wmsConstant.return.typeId.returnStatus,
							"returnReason": wmsConstant.return.typeId.returnReason,
							"returnExpress": wmsConstant.return.typeId.returnExpress,
							"returnCondition": wmsConstant.return.typeId.returnCondition
						},
						$scope).then(function(response) {
							//$scope.returnStatus = response.data.returnStatus;
							$scope.returnReason = response.data.returnReason;
							//$scope.returnExpress = response.data.returnExpress;
							$scope.returnCondition = response.data.returnCondition;
							//根据storeID设置快递公司下拉框的值
							popReturnInfoService.getReceivedFromItemByStoreId($routeParams.storeId, $scope).then(function (response){
								$scope.returnExpress = response.data.receivedFromList;
							});
						})
				};
                
                $scope.doSaveReturnInfo = function(){
					confirm("WMS_RETURN_SAVE").then(function(){
						returnService.doSaveReturnInfo($scope.returnInfo).then(function(response){
							//response.data.successFlg ? notify(response.data.returnResMsg) : alert(returnResMsg);
							if(response.data.successFlg){
								notify.success("WMS_NOTIFY_OP_SUCCESS");
							}
							$location.path('wms/return/newSession').search({"sessionId":returnService.getSessionId()});
						});
						}
					)
                };
                
                $scope.doColoseEditeWindow = function(){
                	$location.path('wms/return/newSession').search({"sessionId":returnService.getSessionId()});
                }
            }
        ]);
});
