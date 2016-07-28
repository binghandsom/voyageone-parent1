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
            this.reportYearMonthList = [];
            this.reportYearMonth = "";
            this.canConfirmReport = false;
        }

        FinancialReportController.prototype = {
            init: function () {
                var main = this;
                main.financialReportService.init().then(function (res) {
                    main.reportYearMonthList = res.reportYearMonthList;
                    main.search();
                })
            },
            search: function () {
                var main = this;
                main.financialReportService.search({
                    "reportYearMonth": main.reportYearMonth
                }).then(function (res) {
                    main.financialReportList = res.financialReportList;
                    main.canConfirmReport = res.canConfirmReport;
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
                $.download.post('/vms/report/financial_report/downloadFinancialReport', {"reportFileName": reportFileName}, this.afterDownload, main);
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