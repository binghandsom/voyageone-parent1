/**
 * Created by sofia on 6/2/2016.
 */
define([
    'cms'
], function (cms) {
    cms.controller('HsCodeController', (function () {
        function HsCodeController(hsCodeInfoService) {
            this.hsCodeInfoService = hsCodeInfoService;
            this.prodPageOption = {curr: 1, total: 0, size: 10, fetch: this.search};
            this.searchCondition = "";
            this.totalHsCodeCnt = 0;
            this.hsCodeTaskCnt = 10;
            this.hsCodeStatus = "0";
            this.hsCodeList = [];
            this.hsCodeValue = [];
            this.status = false;
            this.qty = "";
            this.order = "";
            this.code = "";
        }

        HsCodeController.prototype = {
            init: function () {
                var self = this;
                var data = this.prodPageOption;
                _.extend(data, {"hsCodeStatus": this.hsCodeStatus});
                _.extend(data, {"searchCondition": this.searchCondition});
                self.hsCodeInfoService.init(data).then(function (res) {
                    self.notAssignedTotalHsCodeCnt = res.data.notAssignedTotalHsCodeCnt;
                    self.alreadyAssignedTotalHsCodeCnt = res.data.alreadyAssignedTotalHsCodeCnt;
                    self.setChannelTotalHsCodeCnt = res.data.setChannelTotalHsCodeCnt;
                    self.setPersonalTotalHsCodeCnt = res.data.setPersonalTotalHsCodeCnt;

                    self.hsCodeList = res.data.hsCodeList;
                    self.hsCodeValue = res.data.hsCodeValue;
                })
            },
            get: function () {
                var self = this;
                self.hsCodeInfoService.get({"qty": self.qty, "order": self.order, "code":self.code, "hsCodeTaskCnt":self.hsCodeTaskCnt}).then()
            },
            search: function (page) {
                var self = this;
                self.searchInfo = {};
                self.prodPageOption.curr = !page ? self.prodPageOption.curr : page;
                self.searchInfo.pageNum = self.prodPageOption.curr;
                self.searchInfo.pageSize = self.prodPageOption.size;
                self.searchInfo.hsCodeStatus = self.hsCodeStatus;
                self.searchInfo.searchCondition = self.searchCondition;
                self.hsCodeInfoService.search(self.searchInfo).then(function (res) {

                })
            }
        };

        return HsCodeController;
    })())
});