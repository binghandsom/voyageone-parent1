/**
 * @User: Tomy
 * @Date: 2015-04-09 17:16:50
 * @Version: 0.0.1
 */
define(["modules/wms/wms.module",
        "modules/wms/return/returnService",
		"components/directives/paging"
        ], function (wms) {
    wms.controller("rtnSessionListCtrl", [
            "$scope",
            "returnService",
            "$location",
			"wmsConstant",
			"$filter",
            function ($scope, returnService, $location, wmsConstant, $filter) {
                // 检索条件结构体
                $scope.search = {
                	page: 1,   //分頁用
        			rows: 20 //分頁用pageSize
                };

				//分頁結構體
				$scope.pageOption = {
					curr: 1,
					total: 1,
					size: 20,
					fetch: reqSearch
				};

				//查詢returnList
				$scope.doSearch = doSearch;
                
				//初始化
				$scope.initialize = function() {
					returnService.doSessionListInit(
						{
							"sessionStatus": wmsConstant.return.typeId.sessionStatus
						},
						$scope).then(function(response) {
							$scope.sessionStatus = response.data.sessionStatus;
							$scope.search.return_session_status = response.data.sessionStatus[0].id;
							$scope.search.createTime_s = response.data.fromDate;
							$scope.search.createTime_e = response.data.toDate;
							doSearch(1);
						});
				};

				//跳转到sessionDetail页面
				$scope.doSearchSessionDetail = doSearchSessionDetail;

				function doSearch(page){
            		reqSearch($scope.pageOption.curr = page);
            	}

				function doSearchSessionDetail(returnSessionId){
               		$location.path('/wms/return/sessionDetail').search({"sessionId" : returnSessionId});
            	}

				function reqSearch(page, rows){
					$scope.search.page = page;
					//查询条件格式化为本地时间
					var dateFilter = $filter("date");
					$scope.search.createTime_s = dateFilter($scope.search.createTime_s, 'yyyy-MM-dd');
					$scope.search.createTime_e = dateFilter($scope.search.createTime_e, 'yyyy-MM-dd');
					returnService.doSessionListSearch($scope.search, $scope).then(function(response){
						$scope.sessionInfoList = response.data.sessionInfoList;
						$scope.pageOption.total = response.data.total;
					});
				}
            	
            }
        ]);
});
