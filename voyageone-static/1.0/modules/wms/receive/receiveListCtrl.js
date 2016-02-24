/**
 * @User: Jack
 * @Date: 2015-05-19 17:16:50
 * @Version: 0.0.1
 */
define([
    "modules/wms/wms.module",
    "modules/wms/receive/receiveService",
    "components/directives/paging",
    "components/services/printService",
    "../directives/popChangeReservation/popChangeReservation",
    "components/directives/enterClick",
    "components/directives/dialogs/dialogs"
], function (wms) {
    wms.controller("receiveListCtrl", [
        "$scope",
        "receiveService",
        "printService",
        "vConfirm",
        "$filter",
        "notify",
        "$window",
        "wmsConstant",
        "wmsChangeReservation",
        receiveListCtrl
    ]);

    function receiveListCtrl($scope, receiveService, printService, confirm, $filter, notify, $window, wmsConstant, wmsChangeReservation) {

        var vm = $scope.vm = {};

        //检索
        $scope.searchPickup = searchPickup;

        //改变扫描类型
        $scope.changePickup = changePickup;

        //扫描
        $scope.scanPickup = scanPickup;

        //重新打印
        $scope.reLabelPickup = relabelPickup;

        //根据列表每一行的内容设置按钮是否显示
        $scope.setValue = setValue;

        //根据所选种类更新物品
        $scope.changeKind = changeKind;

        //已收货报告下载
        $scope.downloadReportReceived = downloadReportReceived;


        //检索条件初始化
        vm.search = {
            orderNumber:"",
            reservationId: "",
            sku:"",
            status:"",
            fromDate:null,
            toDate:null
        };

        //检索条件初始化
        vm.report = {
            fromDate:null,
            toDate:null
        };

        //扫描条件初始化
        vm.scan = {
            no:"",
            mode:"Scan",
            permit:"",
            labelPrint:"",
            pickupType:"",
            pickupTypeName:"",
            pickupStatus:"",
            pickupPort:"",
            relabelType:"",
            relabelTypeName:"",
            relabelStatus:"",
            relabelPort:"",
            scanNo:"",
            scanMode:"",
            scanType:"",
            scanTypeName:"",
            scanStatus:"",
            scanPort:"",
            store:""
        };


        //分页结构体
        vm.paging = {
            curr: 1,
            size: 20,
            total: 1,
            fetch: reqSearchPickup
        };

        // 物品一览
        $scope.reservationList = [];
        vm.lastPrint = [];

        var liViewCtl = $scope.liViewCtl = {};

        // 初始化处理：取得仓库一览、状态一览并赋予初期值
        receiveService.doInit({},$scope).then(function(response) {
            vm.status = response.data.status;
            vm.stores = response.data.storeList;
            vm.search.status = response.data.defaultStatus;
            vm.search.store =vm.stores[0].store_id;
            vm.search.fromDate = response.data.fromDate;
            vm.search.toDate = response.data.toDate;

            vm.report.store =vm.stores[0].store_id;
            vm.report.fromDate = response.data.reportFromDate;
            vm.report.toDate = response.data.reportToDate;
            console.log(vm.report);
            vm.selectStores = response.data.selectStoreList;
            if (response.data.selectStoreList.length == 1) {
                vm.scan.store = vm.selectStores[0].store_id;
            }

            vm.scan.pickupType = response.data.pickupType;
            vm.scan.pickupTypeName = response.data.pickupTypeName;
            vm.scan.pickupStatus = response.data.pickupStatus;
            vm.scan.pickupPort = response.data.pickupPort;
            vm.scan.relabelType = response.data.relabelType;
            vm.scan.relabelTypeName = response.data.relabelTypeName;
            vm.scan.relabelStatus = response.data.relabelStatus;
            vm.scan.relabelPort = response.data.relabelPort;
            vm.scan.permit = response.data.permit;
            vm.scan.labelPrint = response.data.labelPrint;

            // 扫描项目的初期说明
            $('#scanNo').attr('placeholder',vm.scan.pickupTypeName).focus();

            // 根据初始化条件进行查询
            searchPickup();
        });

        // 根据检索条件查询相关记录
        function searchPickup() {
            vm.paging.curr = 1;
            reqSearchPickup();
        }

        // 分页调用函数
        function reqSearchPickup() {

            var f = "yyyy-MM-dd";
            var dateFilter = $filter("date");
            var format = function(v) {
                return v ? (_.isDate(v) ?dateFilter(v, f) : v) : "";
            };

            // 格式化时间
            var from = format(vm.search.fromDate),
                to = format(vm.search.toDate);

            receiveService.searchPickup(
                vm.search.orderNumber,
                vm.search.reservationId,
                vm.search.sku,
                vm.search.status,
                vm.search.store,
                from,
                to,
                vm.paging.curr,
                vm.paging.size
            )
                .then(function (res) {
                    $scope.reservationList= res.data;
                    vm.paging.total = res.count;
                });
        }

        // 改变扫描类型
        function changePickup() {
            var scanNo = $('#scanNo');

            if (vm.scan.mode == "Scan") {
                scanNo.attr('placeholder',vm.scan.pickupTypeName);
            } else {
                scanNo.attr('placeholder',vm.scan.relabelTypeName);
            }
            vm.scan.no = "";
            scanNo.focus();
        }

        // 扫描并且打印捡货单
        function scanPickup() {

            if (vm.scan.permit != "1") {
                notify({"message": "WMS_RECEIVE_PERMIT","status":"warning"});
                return;
            }

            if (vm.scan.mode == "Scan") {
                vm.scan.scanType = vm.scan.pickupType;
                vm.scan.scanTypeName = vm.scan.pickupTypeName;
                vm.scan.scanStatus = vm.scan.pickupStatus;
                vm.scan.scanPort = vm.scan.pickupPort;

                if (!vm.scan.store) {
                    notify({"message": "WMS_PICKUP_STORE_UN_VALID","status":"warning"});
                    return;
                }

            } else {
                vm.scan.scanType = vm.scan.relabelType;
                vm.scan.scanTypeName = vm.scan.relabelTypeName;
                vm.scan.scanStatus = vm.scan.relabelStatus;
                vm.scan.scanPort = vm.scan.relabelPort;
            }
            vm.scan.scanMode = vm.scan.mode;
            vm.scan.scanNo = vm.scan.no;

            if (!vm.scan.scanNo) {
                notify({"message":{id:"WMS_PICKUP_TYPE_UN_VALID", values:vm.scan},"status":"warning"});
                return;
            }

            if (vm.scan.scanMode == "Scan") {
                pickup();
            } else {
                confirm({id:"WMS_PICKUP_RELABEL", values:vm.scan}).then(pickup);
            }
        }

        // 扫描并且打印捡货单
        function relabelPickup() {

            if (vm.scan.permit != "1") {
                notify({"message": "WMS_RECEIVE_PERMIT","status":"warning"});
                return;
            }

            vm.scan.scanType = vm.scan.relabelType;
            vm.scan.scanTypeName = vm.scan.relabelTypeName;
            vm.scan.scanStatus = vm.scan.relabelStatus;
            vm.scan.scanPort = vm.scan.relabelPort;
            vm.scan.scanMode = "ReLabel";

            if (vm.lastPrint == null || vm.lastPrint.length == 0){
                vm.scan.scanNo = "";
            }
            else if (vm.scan.scanType == '1') {
                vm.scan.scanNo = vm.lastPrint[0].id;
            } else if (vm.scan.scanType == '2') {
                vm.scan.scanNo = vm.lastPrint[0].order_number;
            } else if (vm.scan.scanType == '3') {
                vm.scan.scanNo = vm.lastPrint[0].id;
            }

            if (!vm.scan.scanNo) {
                var op={"message":vm.scan.scanTypeName + " is required !","status":"warning"};
                notify(op);
                return;
            }

            confirm({id:"WMS_PICKUP_RELABEL", values:vm.scan}).then(pickup);
        }

        function pickup() {

            vm.scan.no = "";
            $('#scanNo').focus();

            receiveService.scanPickup(
                vm.scan.scanNo,
                vm.scan.scanMode,
                vm.scan.scanType,
                vm.scan.scanTypeName,
                vm.scan.scanStatus,
                vm.scan.scanPort,
                vm.scan.store
            )
                .then(function (res) {

                    vm.lastPrint = res.data.pickupLabel;

                    if (vm.scan.labelPrint == "1") {
                        printService.doPrint(wmsConstant.print.business.PickUp, wmsConstant.print.hardware_key.Print_PickUp, res.data.printPickupLabel);
                    }

                    vm.scan.no = "";
                    $('#scanNo').focus();

                    reqSearchPickup();
                });
        }

        //将列表中被点击的对象传递到后台
        function setValue(reservationInfo){
            $scope.reservationList.selected = reservationInfo;
            operationButtonShowCtl(reservationInfo);
        }

        //表格操作按钮显示控制
        function operationButtonShowCtl(reservationInfo){
            liViewCtl.cancel = false;
            liViewCtl.backOrder = false;
            liViewCtl.open = false;
            liViewCtl.backOrderConfirmed = false;
            //状态为11(open)的时候才展示【取消】、【超卖】按钮。并且可变更仓库
            if(reservationInfo.res_status_id == '11'){
                liViewCtl.cancel = true;
                liViewCtl.backOrder = true;
            }
            //状态为98(backOrder)的时候才展示【打开】、【确认】按钮
            if(reservationInfo.res_status_id == '98'){
                liViewCtl.open  = true;
                liViewCtl.backOrderConfirmed  = true;
            }

        }

        //根据所选状态更新物品
        function changeKind(changeKind,processContent){
            var reservation = $scope.reservationList.selected;

            reservation.changeKind = changeKind;
            reservation.processContent = processContent;

            wmsChangeReservation($scope,reservation);

        }

        // 已拣货报告下载
        function downloadReportReceived() {
            var f = "yyyy-MM-dd";
            var dateFilter = $filter("date");
            var format = function(v) {
                return v ? (_.isDate(v) ?dateFilter(v, f) : v) : "";
            };

            // 格式化时间
            var from = format(vm.report.fromDate),
                to = format(vm.report.toDate);
            //var checkFrom = getNextDay(from)

            //js日期比较(yyyy-mm-dd)
            //if (checkDate(checkFrom, to) == false){
            //    notify({"message": "WMS_DEL_REPORT_CHECK_DATE","status":"warning"});
            //    return;
            //}
            var downloadUrl = "./wms/receive/report/download?store_id={0}&from={1}&to={2}";
            var realPage = downloadUrl.replace ("{0}",vm.report.store).replace ("{1}",from).replace ("{2}",to);
            $window.open(realPage);

        }



    }
});