/**
 * @User: sky
 * @Date: 2015-04-29
 * @Version: 0.0.1
 */
define([
    "modules/wms/wms.module",
    "components/directives/paging",
    "modules/wms/return/returnService",
	"components/services/printService",
	"components/directives/dialogs/dialogs"
], function (wms) {
    wms
        .controller("returnListCtrl", [
            "$scope",
            "returnService",
			"vAlert",
			"printService",
			"wmsConstant",
			"vConfirm",
			"notify",
			"$filter",
			"$location",
            function ($scope, returnService, alert, printService, wmsConstant, confirm, notify, $filter, $location) {
            	
                //检索条件结构体
                $scope.search = {
                	page:1,   //分頁用
        			rows: 20 //分頁用pageSize
                };

				//分頁結構體
				$scope.pageOption = {
					curr: 1,
					total: 1,
					size: 20,
					fetch: reqSearch
				};

				//查询
				$scope.doSearch = doSearch;

				//初始化頁面
				$scope.initialize = initialize;

                //點擊refunded按鈕更新return狀態
                $scope.changeStatus = changeStatus;

				//跳转到newSession页面
				$scope.changeToNewEditSessionPage = changeToNewEditSessionPage;

                //跳转到sessionList页面
                $scope.changeToSessionListPage = changeToSessionListPage;

				//打印行
				$scope.printRow = printRow;

                function changeToSessionListPage(){
                    $location.path("wms/return/sessionList");
                }

				function changeToNewEditSessionPage(){
					$location.path("wms/return/newSession");
				}

				function initialize(){
            		returnService.doInit({ "returnStatus" : wmsConstant.return.typeId.returnStatus}, $scope)
            			.then(function(response){
            				$scope.returnStatus = response.data.returnStatus;
							//默认查询条件为目前日期往前推一个月
							$scope.search.updateTime_s  = response.data.fromDate;
							$scope.search.updateTime_e  = response.data.toDate;
							//按默认条件展示数据
							doSearch(1);
						})
            	}

				function changeStatus(return_id) {
					confirm("WMS_RETURN_TO_REFUNDED").then(function() {
						returnService.doChangeRecToRef(return_id, $scope).then(function(response) {
                            //response.data.successFlg ? notify(response.data.returnResMsg) : alert(response.data.returnResMsg);
							if(response.data.successFlg){
								notify.success("WMS_NOTIFY_OP_SUCCESS");
							}
							$scope.doSearch(1);
						});
					});
				}

            	function doSearch(page){
            		reqSearch($scope.pageOption.curr = page);
            	}

            	function reqSearch(page){
            		$scope.search.page = page;
					$scope.search.updateTime_s = formatDate($scope.search.updateTime_s);
					$scope.search.updateTime_e = formatDate($scope.search.updateTime_e);
            		returnService.doSearch($scope.search, $scope).then(function(response){
                    	$scope.returnList = response.data.returnList;
                    	$scope.pageOption.total = response.data.total;
            		});
            	}

                function formatDate(v){
                    var f = 'yyyy-MM-dd';
                    var dateFilter = $filter("date");
                    return v ? (_.isDate(v) ? dateFilter(v, f) : v) : "";
                }

				function printRow(){
					var returnInfo = $scope.returnList.selected;
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
        ]);
});
