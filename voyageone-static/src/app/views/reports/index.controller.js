/**
 * Created by sofia on 7/6/2016.
 */
define([
    'vms'
], function (vms) {
    vms.controller('FinancialReportController', (function () {

        function FinancialReportController(financialReportService) {
            this.financialReportService = financialReportService;
            this.financialReportList = [];
            this.yearMonthList = [];
            this.yearMonth = "";
            this.canConfirm = false;
        }

        FinancialReportController.prototype = {
            init: function () {
                var main = this;
                main.financialReportService.init().then(function (res) {
                    main.financialReportList = res.financialReportList;
                    main.search();
                })
            },
            search: function () {
                var main = this;
                main.financialReportService.search({
                    "yearMonth": main.yearMonth
                }).then(function (res) {
                    main.financialReportList = res.financialReportList;
                    main.canConfirm = res.canConfirm;
                })
            },

            confirm: function (id) {
                var main = this;
                main.financialReportService.confirm({
                    "id": id
                }).then(function (res) {
                    main.search();
                })
            },

            download: function (reportFileName) {
                var main = this;
                $.download.post('/vms/feed/financial_report/downloadFinancialReport', {"reportFileName": reportFileName}, this.afterDownload, main);
            },

            afterDownload:function (responseContent, param, context) {
                var res = JSON.parse(responseContent);
                if (res.message != '') {
                    context.alert(res.message);
                }
            }
        }
        return FinancialReportController;

    }()));
});