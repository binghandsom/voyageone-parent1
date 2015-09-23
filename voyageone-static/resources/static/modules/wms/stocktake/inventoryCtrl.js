/**
 * @User: sky
 * @Date: 2015-05-21
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
        wms.controller("inventoryCtrl",
            ["$scope",
            "$routeParams",
            "stocktakeService",
            "vAlert",
            "vConfirm",
            "notify",
            "$location",
            inventoryCtrl]);

        function inventoryCtrl($scope, routeParams, stocktakeService, alert, confirm, notify, $location) {

            //变量Map
            var vm = $scope.vm = {
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

            //按钮状态控制
            var btnCtrl =$scope.btnCtrl = {};

            //初始化
            $scope.init = init;

            //扫描Upc 【scan按钮】
            $scope.scanUpc = scanUpc;

            //提交section 【submit按钮】
            $scope.submit = submit;

            //撤销已经扫描的Item 【ReScan按钮】
            $scope.reScan = reScan;

            //分页调用函数
            function reqSearch(page, rows) {
                vm.page = page;
                $scope.pageOption.curr = page;
                stocktakeService.doInventoryInit(vm, $scope).then(function (response){
                    $scope.pageOption.total = response.data.total;
                    $scope.itemList = response.data.itemList;
                    //submit按钮禁用控制
                    stocktakeService.doCheckSectionStatus(vm, $scope).then(function (response){
                        var sectionInfo = _.find(response.data.sectionList, function(obj){
                            return obj.location_name == vm.location_name;
                        });

                        btnCtrl.disabledFlg = sectionInfo.sectionStatus != '0';
                    })
                });
            }

            function reScan() {
                confirm("WMS_STOCK_TAKE_SECTION_RESCAN").then(function() {
                    stocktakeService.doReScan(vm.stocktake_detail_id, $scope).then(function(response) {
                        //response.data.successFlg ? notify(response.data.returnResMsg) : alert(response.data.returnResMsg);
                        if(response.data.successFlg){
                            notify.success("WMS_NOTIFY_OP_SUCCESS");
                        }
                        init();
                    });
                });
            }

            function submit(){
                confirm("WMS_STOCK_TAKE_SECTION_CLOSE").then(function(){
                    stocktakeService.doCloseSection(vm, $scope).then(function(response) {
                        //response.data.successFlg ? notify(response.data.returnResMsg) : alert(response.data.returnResMsg);
                        if(response.data.successFlg){
                            notify.success("WMS_NOTIFY_OP_SUCCESS");
                        }
                        $location.path("/wms/stockTake/sectionList");
                    });
                });
            }

            function scanUpc(){
                stocktakeService.doUpcScan(vm, $scope).then(function(response){
                    //response.data.successFlg ? notify(response.data.returnResMsg) : alert(response.data.returnResMsg);
                    if(response.data.successFlg){
                        notify.success("WMS_NOTIFY_OP_SUCCESS");
                    }
                    init();
                    //扫描完成之后清空barcode、sku
                    vm.upc = "";
                    vm.sku = "";
                });
            }

            function init() {
                vm.stocktake_id = routeParams.sessionId;
                vm.location_name = routeParams.sectionName;
                vm.sessionName = routeParams.sessionName;
                vm.stocktake_detail_id = routeParams.stocktake_detail_id;
                //默认扫描数量为1，可手工输入数量
                vm.stocktake_qty = 1;
                reqSearch(1);
            }
        }
    });
