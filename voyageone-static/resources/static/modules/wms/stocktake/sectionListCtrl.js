/**
 * @User: Tomy
 * @Date: 2015-04-09 17:16:50
 * @Version: 0.0.1
 */
define(["modules/wms/wms.module",
        "modules/wms/stocktake/stocktakeService",
        "components/directives/getFocus",
        "components/directives/enterClick",
        "components/directives/dialogs/dialogs",
        "components/directives/paging"
      ],
    function(wms) {
        wms.controller("sectionlistCtrl",
                       ["$scope",
                        "$routeParams",
                        "stocktakeService",
                        "$location",
                        "vAlert",
                        "vConfirm",
                        "notify",
                        "$location",
                        sectionListCtrl]);

        function sectionListCtrl($scope, $routeParams, stocktakeService, $location, alert, confirm, notify, location) {
            //初始化
            $scope.initialize = init;

            //点击newSection按钮
            $scope.newSection = newSection;

            //点击删除按钮
            $scope.deleteSection = deleteSection;

            //点击表格中的某一行的SectionName链接
            $scope.toSectionDetailPage = toSectionDetailPage;

            //closeSession状态
            $scope.closeSession = closeSession;

            //分頁结构体
            $scope.pageOption = {
                curr: 1,
                total: 1,
                size: 20,
                fetch: reqSearch
            };

            //页面变量map
            var vm = $scope.vm = {};

            var disabled = $scope.disabled = {};

            //查询条件
            var search = $scope.search = {
                page: 1,
                //分頁用
                rows: 20 //分頁用pageSize
            };

            //分页调用函数
            function reqSearch(page) {
                $scope.search.page = page;
                stocktakeService.doSectionListInit(search, $scope).then(function(response){
                    $scope.pageOption.total = response.data.total;
                    $scope.sessionName = response.data.sessionName;
                    $scope.sectionList = response.data.sectionList;
                    vm.stocktake_id = response.data.sessionId;
                    vm.sessionName = response.data.sessionName;
                    vm.sessionStatus = response.data.sessionStatus;
                    vm.sessionStatus == 0 ? vm.disabledFlg = false : vm.disabledFlg = true;
                });
            }

            //跳转到sectionDetail页面
            function toSectionDetailPage(sectionInfo){
                vm.stocktake_detail_id = sectionInfo.stocktake_detail_id;
                vm.sectionName = sectionInfo.location_name;
                location.path("wms/stockTake/sectionDetail").search({"params": JSON.stringify(vm)});
            }

            //将session状态修改为Stock
            function closeSession() {
                if (vm.sessionStatus != "0") {
                    alert("WMS_STOCK_TAKE_SESSION_STATUS_UNVALID");
                } else {
                    confirm("WMS_STOCK_TAKE_TO_STOCK").then(function() {
                        stocktakeService.stockSession(vm.stocktake_id).then(function(response) {
                            //response.data.successFlg ? notify(response.data.returnResMsg) : alert(response.data.returnResMsg);
                            if(response.data.successFlg){
                                notify.success("WMS_NOTIFY_OP_SUCCESS");
                                location.path("wms/stockTake/list");
                            }
                        });
                    });
                }
            }

            //删除Section
            function deleteSection(stocktake_detail_id){
                confirm("WMS_STOCK_TAKE_SECTION_DEL").then(function(){
                    stocktakeService.deleteSection(stocktake_detail_id, $scope).then(function (response){
                        //response.data.successFlg ? notify(response.data.returnResMsg) : alert(response.data.returnResMsg);
                        if(response.data.successFlg){
                            notify.success("WMS_NOTIFY_OP_SUCCESS");
                        }
                        init();
                    });
                });
            }

            //添加section
            function newSection (){
                stocktakeService.newSection(vm, $scope).then(function(response){
                    if(response.data.successFlg){
                        vm.stocktake_detail_id = response.data.stocktake_detail_id;
                        $location.path("/wms/stockTake/inventory").search({"sessionId": vm.stocktake_id, "sectionName": vm.sectionName, "sessionName": vm.sessionName, "stocktake_detail_id": vm.stocktake_detail_id});
                    }else{
                        alert(response.data.returnResMsg);
                    }
                });
            }

            //初始化
            function init(){
                //获取从其他页面跳转过来是通过路由传递的sessionId
                search.stocktake_id = $routeParams.sessionId;
                if($routeParams.viewflag){
                    disabled.flag = true;
                }
                reqSearch(1);
            }
        }

    });
