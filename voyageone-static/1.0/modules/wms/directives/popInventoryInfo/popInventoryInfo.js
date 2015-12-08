/**
 * @User: Jonas
 * @Date: 2015-04-08 15:39:01
 * @Version: 0.0.1
 * @modify sky 20150601
 */

define([
    "modules/wms/wms.module",
    "modules/wms/directives/popInventoryInfo/popInventoryService",
    "components/directives/dialogs/dialogs",
    "modules/wms/directives/popSkuHisList/popSkuHisList"
], function (wms) {

    wms.directive("popWmsInventoryInfo", [
        "$modal",
        "popInventoryService",
        "vAlert",
        function ($modal, popInventoryService, alert) {
            return {
                restrict: "A",
                link: link};

                function link ($scope, elem, attr) {

                    //获取页面传递过来的sku
                    var reservationInfo;

                    //弹出框对象
                    var m;

                    //点击插件属性，标记的页面元素时候触发事件
                    elem.click(function() {
                        reservationInfo = attr.popWmsInventoryInfo;
                        m = createModal();
                    });

                    //创建弹出框
                    function createModal() {
                        return $modal.open({
                            size: 'lg',
                            scope: $scope,
                            templateUrl: "modules/wms/directives/popInventoryInfo/popInventoryInfo.tpl.html",
                            title: "Inventory Info",
                            controller: ["$scope", modalController]
                        });
                    }

                    //弹出框控制器
                    function modalController(scope){

                        //页面变量Map
                        var vm = scope.vm = {};

                        //关闭窗口
                        scope.close = close;

                        //点击查询按钮
                        scope.doSearch = doSearch;

                        //初始化
                        popInventoryService.popInit(reservationInfo).then(function (response){
                            if(!response.data.successFlg){
                                m.close();
                                alert(response.data.returnResMsg);
                            }
                            vm.itemCode = response.data.productInfo.itemCode;
                            vm.productName = response.data.productInfo.product;
                            doSearch();
                        });

                        function doSearch(){
                            reservationInfo = stringToObject(reservationInfo);
                            reservationInfo.itemCode = vm.itemCode;
                            reservationInfo.sku = null;
                            popInventoryService.doSearch(reservationInfo).then(function (response){
                                scope.storeList = response.data.storeList;
                                scope.inventoryList = response.data.inventoryList;
                                scope.inventoryList.count = response.data.inventoryList.length;
                                vm.image_path = response.data.inventoryList[0].image_path;
                                vm.productName = response.data.inventoryList[0].product;
                            })
                        }

                        function close() {
                            m.close();
                        }

                        function stringToObject(reservationInfo){
                            if(!_.isObject(reservationInfo)){
                                return JSON.parse(reservationInfo)
                            }else{
                                return reservationInfo;
                            }
                        }
                    }
                }
        }
    ]);

});