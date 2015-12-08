/**
 * @User: sky
 * @Date: 2015-05-18
 * @Version: 0.0.1
 */
define(["modules/wms/wms.module",
        "modules/wms/stocktake/stocktakeService",
        "components/directives/paging",
        "components/directives/dialogs/dialogs"
], function (wms) {
    wms.controller("stocktakeListCtrl", [
            "$scope",
            "stocktakeService",
            "wmsConstant",
            "vAlert",
            "$location",
            "ngDialog",
            "vConfirm",
            "notify",
            "$filter",
            sessionListCtrl
        ]);

    function sessionListCtrl($scope, stocktakeService, wmsConstant, alert, $location, ngDialog, confirm, notify, $filter) {

        //初始化
        $scope.initialize = init;

        //列表查询
        $scope.searchList = searchList;

        //newSession
        $scope.newSession = newSession;

        //根据列表每一行的内容设置按钮是否显示
        $scope.setValue = setValue;

        //表格操作
        $scope.viewSession = viewSession;
        $scope.editSession = editSession;
        $scope.deleteSession = deleteSession;
        $scope.compare = compare;

        //查询条件
        var search = $scope.search = {
            page: 1,
            //分頁用
            rows: 20 //分頁用pageSize
        };

        var liViewCtl = $scope.liViewCtl = {};

        //viewSession实现页面跳转
        function viewSession(){
            var sessionInfo = $scope.sessionList.selected;
            $location.path("/wms/stockTake/sectionList").search({"sessionId": sessionInfo.stocktake_id, "viewflag": true });
        }

        //将列表中被点击的对象传递到后台
        function setValue(sessionInfo){
            $scope.sessionList.selected = sessionInfo;
            oprationButtonShowCtl(sessionInfo);
        }

        //表格操作按钮显示控制
        function oprationButtonShowCtl(sessionInfo){
            //状态为0(processing)的时候才展示【编辑】、【删除】、【查看】按钮
            if(sessionInfo.stocktake_status == 0){
                liViewCtl.edit = true;
                liViewCtl.delete = true;
            }else{
                liViewCtl.edit = false;
                liViewCtl.delete = false;
            }
            //只有状态为2（compar),或者3(done)的时候显示Compar按钮
            liViewCtl.compare = !!(sessionInfo.stocktake_status == 2 || sessionInfo.stocktake_status == 3);
        }

        //页面初始化
        function init() {
            stocktakeService.doSessionListInit({"sessionStatus": wmsConstant.stockTake.typeId.sessionStatus},
                $scope).then(function(response) {
                    $scope.sessionStatus = response.data.sessionStatus;
                    $scope.userStores = response.data.storeList;
                    search.store_id = response.data.storeList[0].store_id;
                    //设置默认时间
                    $scope.search.modifiedTime_s = response.data.fromDate;
                    $scope.search.modifiedTime_e = response.data.toDate;
                    searchList(1);
                });
        }

        //页面结果集查询
        function searchList(page) {
            reqSearch($scope.pageOption.curr = page);
        }

        //分页调用函数
        function reqSearch(page, rows) {
            $scope.search.page = page;
            var dateFilter = $filter("date");
            search.modifiedTime_s = dateFilter(search.modifiedTime_s, 'yyyy-MM-dd');
            search.modifiedTime_e = dateFilter(search.modifiedTime_e, 'yyyy-MM-dd');
            stocktakeService.doSessionListSearch(search, $scope).then(function(response) {
                $scope.sessionList = response.data.sessionList;
                $scope.pageOption.total = response.data.total;
            });
        }

        //分頁结构体
        $scope.pageOption = {
            curr: 1,
            total: 1,
            size: 20,
            fetch: reqSearch
        };

        //编辑Session
        function editSession() {
            var sessionInfo = $scope.sessionList.selected;
            if(sessionInfo.stocktake_status != "0"){
                alert("WMS_STOCK_TAKE_SESSION_STATUS_UNVALID");
                return;
            }
            $location.path("/wms/stockTake/sectionList").search("sessionId", sessionInfo.stocktake_id);
        }

        //跳转到compare页面
        function compare(){
            var sessionId = $scope.sessionList.selected.stocktake_id;
            var sessionname = $scope.sessionList.selected.stocktake_name;
            $location.path("/wms/stockTake/compare").search({"sessionId": sessionId, "sessionName": sessionname});
        }

        //删除session
        function deleteSession() {
            var sessionId = $scope.sessionList.selected.stocktake_id;
            confirm("WMS_STOCK_TAKE_SESSION_DEL").then(function(){
                stocktakeService.deleteSession(sessionId).then(function(response) {
                    //response.data.successFlg ? notify(response.data.returnResMsg) : alert(response.data.returnResMsg);
                    if(response.data.successFlg){
                        notify.success("WMS_NOTIFY_OP_SUCCESS");
                    }
                    //重新刷新页面
                    searchList($scope.pageOption.curr);
                });
            });
        }

        //新建session
        function newSession() {
            ngDialog.openConfirm({
                template: "store.choose.html",
                controller: ["$scope", chooseStore]
            });
        }

        //创建session的时候选择仓库
        function chooseStore(scope) {
            var setValue = scope.setValue = {};
            //点击OK按钮处理
            scope.createSession = createSession;

            //弹出框初始化
            stocktakeService.doSessionListInit({"stocktakeType": wmsConstant.stockTake.typeId.stockTakeType+";false"}, $scope).then(function(response) {
                scope.userStores = response.data.storeList;
                if (response.data.storeList.length == 1) {
                    setValue.store_id = response.data.storeList[0].store_id;
                }
                scope.stocktakeType = response.data.stocktakeType;
                setValue.stocktakeType = response.data.stocktakeType[0].id;
            });

            //新建session
            function createSession(){
                if (!setValue.store_id) {
                    alert("WMS_STOCK_TAKE_NEW_FAIL");
                    return;
                }
                stocktakeService.newSession(setValue, scope).then(function(response){
                    $location.path("/wms/stockTake/sectionList").search("sessionId",  response.data.newSessionId);
                    scope.confirm();
                });
            }
        }

    }
});
