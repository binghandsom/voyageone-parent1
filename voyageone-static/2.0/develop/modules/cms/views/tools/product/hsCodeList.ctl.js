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
            this.hsCodeTaskCnt = "";
            this.hsCodeStatus = "0";
            this.hsCodeList = [];
            this.hsCodeValue = [];
            this.status = false;
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

                    self.totalHsCodeCnt = res.data.totalHsCodeCnt;
                    self.hsCodeTaskCnt = res.data.hsCodeTaskCnt;
                    self.hsCodeList = res.data.hsCodeList;
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