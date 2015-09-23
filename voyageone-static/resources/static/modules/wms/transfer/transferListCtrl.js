/**
 * @User: Jonas
 * @Date: 2015-3-26 19:15:17
 * @Version: 0.0.2
 */

define([
    "modules/wms/wms.module",
    "modules/wms/transfer/transferService",
    "components/directives/paging",
    "components/directives/dialogs/dialogs"
], function (wms) {

    wms
        .controller("transferListCtrl", [
            "$scope",
            "transferService",
            "vConfirm",
            "vAlert",
            "$location",
            "$window",
            "$filter",
            transferListCtrl
        ]);

    function transferListCtrl($scope, transferService, confirm, alert, $location, $window, $filter) {

        $scope.pageOption = {
            curr: 1,
            size: 10,
            total: 1,
            fetch: reqSearchTransfer
        };

        // 表格的搜索条件
        $scope.gridOptions = {
            status: "",
            store: "",
            fromDate: null,
            toDate: null,
            po: null,
            selected: -1
        };

        $scope.search = search;
        $scope.editRow = editRow;
        $scope.delRow = delRow;
        $scope.download = download;
        //根据列表每一行的内容设置按钮是否显示
        $scope.setValue = setValue;

        var liViewCtl = $scope.liViewCtl = {};

        // 初始化处理：取得仓库一览、状态一览并赋予初期值
        transferService.doListInit({}, $scope).then(function (response) {

            $scope.byStatus = response.data.status;
            $scope.stores = response.data.storeList;

            $scope.gridOptions.status = $scope.byStatus[0].id;
            $scope.gridOptions.store = $scope.stores[0].store_id;
            $scope.gridOptions.fromDate = response.data.fromDate;
            $scope.gridOptions.toDate = response.data.toDate;

            // 第一次加载默认搜索的第一页
            reqSearchTransfer();
        });

        function search() {
            $scope.pageOption.curr = 1;
            reqSearchTransfer();
        }

        function reqSearchTransfer() {
            var o = $scope.gridOptions;
            var f = "yyyy-MM-dd";
            var dateFilter = $filter("date");
            var format = function (v) {
                return v ? dateFilter(v, f) : "";
            };

            // 格式化时间
            var from = format(o.fromDate),
                to = format(o.toDate);

            transferService

                .searchTransfer(o.po, o.store, o.status, from, to, $scope.pageOption.curr - 1, $scope.pageOption.size)

                .then(function (res) {
                    $scope.pageOption.total = res.rows;
                    $scope.transfers = res.data;
                });
        }

        //将列表中被点击的对象传递到后台
        function setValue(index){
            $scope.gridOptions.selected = index;
            operationButtonShowCtl();
        }

        //表格操作按钮显示控制
        function operationButtonShowCtl(){

            if ($scope.gridOptions.selected < 0) return;

            var index = $scope.gridOptions.selected;
            var selected = $scope.transfers[index];

            liViewCtl.edit = (selected.transfer_status === '0'); // 如果是 closed 则显示“详情”，否则为“编辑”
            liViewCtl.delete = (selected.transfer_status === '0'); // 如果是 closed 不能删除。否则显示可用
            liViewCtl.download = true;

        }

        function editRow() {
            if ($scope.gridOptions.selected < 0) return;

            var index = $scope.gridOptions.selected;
            var selected = $scope.transfers[index];

            $location.path("/wms/transfer/edit/" + selected.transfer_id);
        }

        function delRow() {
            if ($scope.gridOptions.selected < 0) return;

            var index = $scope.gridOptions.selected;
            var selected = $scope.transfers[index];

            confirm({id:"WMS_ALERT_TRANSFER_DELETE", values: {transfer_name:selected.transfer_name}})

                .then(function () {

                    transferService.deleteTransfer(selected.transfer_id, selected.modified)

                        .then(function (isSuccess) {
                            if (isSuccess) {
                                $scope.transfers.splice(index, 1);

                                // 刷新画面上的行总数显示
                                pageOption.total = pageOption.total - 1;

                                notify.success("WMS_NOTIFY_OP_SUCCESS");
                            } else {
                                alert("WMS_ALERT_TRANSFER_DELETE_FAIL");
                            }
                        });

                });
        }

        function download() {
            if ($scope.gridOptions.selected < 0) return;

            var index = $scope.gridOptions.selected;
            var selected = $scope.transfers[index];

            $window.open("./wms/transfer/list/download?transfer_id="+selected.transfer_id, '_blank');

        }
    }
});

