/**
 * @User: sky
 * @Date: 2015-05-22
 * @Version: 0.0.1
 */
define(["modules/wms/wms.module",
        "modules/wms/stocktake/stocktakeService",
        "components/directives/dialogs/dialogs",
        "components/directives/paging"
    ], function (wms) {
    wms
        .controller("sectionDetailCtrl", [
            "$scope",
            "$routeParams",
            "stocktakeService",
            "vAlert",
            "vConfirm",
            "notify",
            sectionDetailCtrl
        ]);

    function sectionDetailCtrl($scope, $routeParams, stocktakeService, alert, confirm, notify) {

        //变量Map
        var vm = $scope.vm = {
            page: 1,
            rows: 20 //分頁用pageSize
        };

        //分頁结构体
        $scope.pageOption = {
            curr: 1,
            total: 1,
            size: 20,
            fetch: reqSearch
        };

        //初始化
        $scope.init = init;

        //分页调用函数
        function reqSearch(page, rows) {
            vm.page = page;
            $scope.pageOption.curr = page;
            stocktakeService.doSectionDetailInit(vm, $scope).then(function(response){
                vm.itemList = response.data.itemList;
                $scope.pageOption.total = response.data.total;
            });
        }

        function init(){
           var params = JSON.parse($routeParams.params);
            vm.stocktake_id =  params.stocktake_id;
            vm.stocktake_detail_id = params.stocktake_detail_id;
            vm.sessionName = params.sessionName;
            vm.sectionName = params.sectionName;
            reqSearch(1);
        }
    }
});
