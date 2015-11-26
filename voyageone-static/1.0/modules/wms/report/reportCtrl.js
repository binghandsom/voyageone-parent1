/**
 * @User: sky
 * @Date:20150720
 * @Version: 1.0.0
 */

define([
    "modules/wms/wms.module",
    "components/directives/dialogs/dialogs",
    "modules/wms/report/reportService"
], function(wms) {
    wms.controller("reportCtrl", [
        "$scope",
        "reportService",
        "$filter",
        listCtrl
    ]);

    function listCtrl($scope, reportService, $filter) {
        $scope.init = init;

        $scope.getParam = getParam;

        //库存详情报表
        var vm = $scope.vm = {};
        var cb = $scope.cb = {};
        function getParam(){
            vm.fromDate = dataf(vm.fromDate);
            vm.toDate = dataf(vm.toDate);
            return vm;
        }

        //初始化
        function init(){
            reportService.doInit().then(function (res){
                //库存详情报表，盘点比对结果报表
                cb.channel = res.data.channel;
                //库存详情报表
               // cb.userStores = res.data.userStore;
                vm.fromDate = res.data.fromDate;
                vm.toDate = res.data.toDate;
                vm.store_sale = true;
                //vm.store_id = res.data.userStore[0].store_id;
                vm.order_channel_id = res.data.channel[0].propertyId;
            });
        }

        //日期格式化
        function dataf(v){
            var dateFilter = $filter("date");
            var f = 'yyyy-MM-dd';
            return v ? (_.isDate(v) ? dateFilter(v, f) : v) : '';
        }
    }
});