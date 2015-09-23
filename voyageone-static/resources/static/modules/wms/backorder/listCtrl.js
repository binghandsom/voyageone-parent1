/**
 * @User: sky
 * @Date:20150528
 * @Version: 1.0.0
 */

define([
    "modules/wms/wms.module",
    "components/directives/paging",
    "components/directives/dialogs/dialogs",
    "modules/wms/backorder/backorderService"
], function(wms) {
    wms.controller("listCtrl", [
        "$scope",
        "ngDialog",
        "backorderService",
        "vConfirm",
        "notify",
        "vAlert",
        listCtrl
    ]);

    function listCtrl($scope, ngDialog, backorderService, confirm, notify, alert) {

        //页面combox
        var pcb = $scope.pcb = {};

        //变量Map
        var pvm = $scope.pvm = {
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

        //【Add a item to BackOrder】 按钮，弹出框，填写sku,点击【add】按钮添加数据到backOrder
        $scope.addBackOrder = addBackOrder;

        //【初始化】
        $scope.init = init;

        //列表中删除按钮
        $scope.delRow = delRow;

        //【查询】按钮
        $scope.search = search;

        //列表中的删除按钮
        function delRow(backorderInfo){
            confirm({id:"WMS_BACK_DEL", values:backorderInfo}).then(function (){
                backorderService.doDelRow(backorderInfo).then(function (response){
                    //response.data.successFlg ? notify(response.data.returnResMsg) : alert(response.data.returnResMsg);
                    if(response.data.successFlg){
                        notify.success("WMS_NOTIFY_OP_SUCCESS");
                    }
                    search($scope.pageOption.curr);
                })
            });
        }

        //分页调用函数
        function reqSearch(page, rows) {
            $scope.pvm.page = page;
            backorderService.doGetBackorderInfo(pvm, $scope).then(function (response){
                $scope.backorderList = response.data.backorderList;
                $scope.pageOption.total = response.data.total;
            });
        }

        //页面结果集查询
        function search(page){
            reqSearch($scope.pageOption.curr = page);
        }

        //页面初始化
        function init(){
            backorderService.doListInit().then(function (res){
                pcb.userStores = res.data.userStore;
                pvm.store_id = res.data.userStore[0].store_id;
                search(1);
            });
        }

        //添加backorderItem
        function addBackOrder(){
            ngDialog.openConfirm({
                template: "backOrder.add.html",
                controller: ["$scope", addBackOrderDialogCtr]
            });

            function addBackOrderDialogCtr(scope){

                //弹出框变量Map
                var vm = scope.vm = {store: 1};

                //下拉框
                var cb = scope.cb = {};

                //点击弹出框的add按钮
                scope.add = add;

                //弹出框初始化（获取Store下拉框的值）
                doPopInit();

                function doPopInit(){
                    backorderService.doPopInit().then(function (res){
                        cb.userStores = res.data.userStore;
                        if (res.data.userStore.length == 1) {
                            vm.store_id = res.data.userStore[0].store_id;
                        }

                    });
                }

                function add(){
                    if (!vm.store_id) {
                        notify("WMS_BACK_STORE_UA");
                        scope.closeThisDialog();
                    }

                    else if (!vm.sku) {
                        notify("WMS_BACK_SKU_UA");
                        scope.closeThisDialog();
                    }

                    else {
                        backorderService.doAddBackorderInfo(vm).then(function (response){
                            scope.closeThisDialog();
                            if(response.data.successFlg){
                                notify.success("WMS_NOTIFY_OP_SUCCESS");
                                search(1);
                            }
                        })
                    }
                }
            }
        }
    }
});