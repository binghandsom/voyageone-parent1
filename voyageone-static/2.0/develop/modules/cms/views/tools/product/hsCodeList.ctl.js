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
            get: function (page) {
                var self = this;
                self.getTaskInfo = {};
                self.prodPageOption.curr = !page ? self.prodPageOption.curr : page;
                self.getTaskInfo.curr = self.prodPageOption.curr;
                self.getTaskInfo.size = self.prodPageOption.size;
                self.getTaskInfo.qty = self.qty;
                self.getTaskInfo.order = self.order;
                self.getTaskInfo.code = self.code;
                self.getTaskInfo.hsCodeTaskCnt = self.hsCodeTaskCnt;
                self.hsCodeInfoService.get(self.getTaskInfo).then()
            },
            search: function (page) {
                var self = this;
                self.searchInfo = {};
                self.prodPageOption.curr = !page ? self.prodPageOption.curr : page;
                self.searchInfo.curr = self.prodPageOption.curr;
                self.searchInfo.size = self.prodPageOption.size;
                self.searchInfo.hsCodeStatus = self.hsCodeStatus;
                self.searchInfo.searchCondition = self.searchCondition;
                self.hsCodeInfoService.search(self.searchInfo).then(function (res) {

                })
            }
        };

        return HsCodeController;
    })())
});