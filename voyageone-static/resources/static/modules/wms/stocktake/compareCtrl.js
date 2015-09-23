/**
 * @User: sky
 * @Date: 2015-05-25
 * @Version: 0.0.1
 */
define(["modules/wms/wms.module",
        "modules/wms/stocktake/stocktakeService",
        "components/directives/paging",
        "components/directives/getFocus",
        "components/directives/enterClick",
        "components/directives/dialogs/dialogs"],
    function(wms) {
        wms.controller("compareCtrl",
            ["$scope",
            "$routeParams",
            "stocktakeService",
            "vAlert",
            "vConfirm",
            "notify",
            "$location",
             compareCtrl]);

        function compareCtrl($scope, $routeParams, stocktakeService, alert, confirm, notify, $location) {

            var vm = $scope.vm = {};

            //查询条件
            var search = $scope.search = {
                page: 1,
                //分頁用
                rows: 20 //分頁用pageSize
            };

            //分頁结构体
            $scope.pageOption = {
                curr: 1,
                total: 1,
                size: 20,
                fetch: reqSearch
            };

            $scope.init = init();

            //checkBox值改变出发事件
            $scope.doCheckBoxValueChange = doCheckBoxValueChange;

            //fixed按钮事件，修改session状态
            $scope.fixed = fixed;

            function fixed(){
                confirm("WMS_STOCK_TAKE_SESSION_FIX").then(function(){
                    stocktakeService.doSessionDone(search.stocktake_id, $scope).then(function (response){
                        //response.data.successFlg ? notify(response.data.returnResMsg) : alert(response.data.returnResMsg);
                        if(response.data.successFlg){
                            notify.success("WMS_NOTIFY_OP_SUCCESS");
                        }
                        //init();
                        //跳转到一级页面
                        $location.path("/wms/stockTake/list");
                    });
                })
            }

            function init(){
                search.stocktake_id = $routeParams.sessionId;
                var sessionname = $routeParams.sessionName;
                vm.sessionName = sessionname;
                vm.stocktake_id = $routeParams.sessionId;
                checkSessionStatus(search.stocktake_id);
            }

            function checkSessionStatus(stocktake_id){
                stocktakeService.doCheckSessionStatus(stocktake_id, $scope).then(function (response){
                    if(response.data.sessionInfo.stocktake_status == '3' ){
                        vm.disableFlg = true;
                    }
                    searchList(1);
                });
            }

            //分页调用函数
            function reqSearch(page, rows) {
                $scope.search.page = page;
                stocktakeService.doCompareInit(search, $scope).then(function (response){
                    $scope.compareInfoList = response.data.compareInfoList;
                    $scope.pageOption.total = response.data.total;
                });
            }

            //页面结果集查询
            function searchList(page) {
                reqSearch($scope.pageOption.curr = page);
            }

            //将目前页面选中的选项保存到数据库
            function doCheckBoxValueChange(compareInfo){
                stocktakeService.doCheckBoxValueChange(compareInfo, $scope);
            }
        }
    });
