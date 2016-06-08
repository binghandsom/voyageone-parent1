/**
 * Created by sofia on 6/2/2016.
 */
define([
    'cms'
], function (cms) {
    cms.controller('HsCodeController', (function () {
        function HsCodeController(hsCodeInfoService) {
            this.hsCodeInfoService = hsCodeInfoService;
            this.prodPageOption = {curr: 1, total: 0, fetch: this.search};
            this.searchInfo = {};
            this.totalHsCodeCnt = 0;
            this.hsCodeTaskCnt = "";
            this.hsCodeStatus = "0";
            this.hsCodeList = [];
            this.productCode = "";
            this.productName = "";
            this.productMaterial = "";
            this.hsCodeValue = [];
        }

        HsCodeController.prototype = {
            init: function () {
                var self = this;
                self.hsCodeInfoService.init({hsCodeStatus: self.hsCodeStatus}).then(function (res) {
                    self.totalHsCodeCnt = res.data.totalHsCodeCnt;
                    self.hsCodeTaskCnt = res.data.hsCodeTaskCnt;
                    self.hsCodeList = res.data.hsCodeList;
                    self.productCode = res.data.productCode;
                    self.productName = res.data.productName;
                    self.productMaterial = res.data.productMaterial;
                    self.hsCodeValue = res.data.hsCodeValue;
                })
            },
            search: function (page) {
                var self = this;
                self.prodPageOption.curr = !page ? self.prodPageOption.curr : page;
                self.searchInfo.pageNum = self.prodPageOption.curr;
                self.searchInfo.pageSize = self.prodPageOption.size;
                self.hsCodeInfoService.search(self.searchInfo).then(function (res) {

                })
            },
            get: function () {
                var self = this;
                self.hsCodeInfoService.get().then()
            }
        };

        return HsCodeController;
    })())
});