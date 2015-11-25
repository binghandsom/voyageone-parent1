/**
 * @User: sky
 * @Date: 2015-06-03
 * @Version: 0.0.1
 */

define([
    "../../wms.module.js",
    "components/directives/paging",
    "modules/wms/directives/popSkuHisList/popSkuHisListService",
    "components/directives/dialogs/dialogs"
], function (wms) {

    wms.directive("popSkuHisList", [
        "$modal",
        "popSkuHisListService",
        "$filter",
        "vAlert",
        function ($modal, popSkuHisListService, $filter, alert) {
            return {
                restrict: "A",
                link: function($scope, elem, attr) {

                    var searchCondition = attr.popSkuHisList;

                    searchCondition = stringToObject(searchCondition);

                    //弹出框对象
                    var m;

                    //单击加入这个插件的页面元素触发事件
                    elem.click(function() {
                        m = createModal();
                    });

                    //创建弹出框
                    function createModal(){
                        return $modal.open({
                            size: 'lg',
                            scope: $scope,
                            templateUrl: "modules/wms/directives/popSkuHisList/popSkuHisList.tpl.html",
                            title: "Inventory history",
                            controller: ["$scope", modalController]
                        });
                    }

                    //弹出框controller
                    function modalController(scope){

                        //分頁結構體
                        scope.pageCtrl = {
                            curr: 1,
                            total: 1,
                            size: 10,
                            fetch: reqSearch
                        };

                        //页面对象Map
                        var vm = scope.vm = {};

                        //关闭窗口
                        scope.close = close;

                        //检索
                        scope.doSearch = doSearch;

                        //打开日期控件(FROM)
                        scope.openDateFormWin = openDateFormWin;

                        //打开日期控件(TO)
                        scope.openDateToWin = openDateToWin;

                        //初始化
                        doInit();

                        function reqSearch(page){
                            doSearch(page);
                            scope.pageCtrl.curr = page;
                        }

                        function doInit(){
                            vm.sku = searchCondition.sku;
                            popSkuHisListService.doInit().then(function (response){
                                vm.createTime_s = response.data.fromDate;
                                vm.createTime_e = response.data.toDate;
                                doSearch(1);
                            });
                        }

                        function doSearch(page){
                            if(!vm.sku){
                                alert("Please input sku!");
                                return;
                            }
                            searchCondition.sku = vm.sku;
                            var dateFilter = $filter("date");
                            searchCondition.fromDate = dateFilter(vm.createTime_s, 'yyyy-MM-dd');
                            searchCondition.toDate = dateFilter(vm.createTime_e, 'yyyy-MM-dd');
                            searchCondition.page = page;
                            searchCondition.rows = 10;
                            popSkuHisListService.doSearch(searchCondition).then(function (response){
                                vm.curQty = response.data.curQty;
                                scope.skuHisList = response.data.skuHisList;
                                scope.skuHisList.count = response.data.skuHisList.length;
                                scope.pageCtrl.total = response.data.total;
                            });
                        }

                        function close(){
                            m.close();
                        }

                        function openDateFormWin(){
                            scope.fromOpened = true;
                        }

                        function openDateToWin(){
                            scope.toOpened = true;
                        }
                    }

                    function stringToObject(searchCondition){
                        if(!_.isObject(searchCondition)){
                            return JSON.parse(searchCondition);
                        }else{
                            return searchCondition;
                        }
                    }
                }
            };
        }
    ]);

});