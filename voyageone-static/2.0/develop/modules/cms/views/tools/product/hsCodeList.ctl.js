/**
 * Created by sofia on 6/2/2016.
 */
define([
    'cms'
], function (cms) {
    cms.controller('HsCodeController', (function () {
        function HsCodeController(hsCodeInfoService, notify) {
            this.hsCodeInfoService = hsCodeInfoService;
            this.prodPageOption = {curr: 1, total: 0, size: 10, fetch: this.search};
            this.hsCodeList = [];
            this.hsCodeValue = [];
            this.status = false;
            this.notify = notify;
            this.getTaskInfo = {
                curr: this.prodPageOption.curr,
                size: this.prodPageOption.size,
                qty: "",
                order: "",
                code: "",
                hsCodeTaskCnt: 10
            };
            this.searchInfo = {
                curr: this.prodPageOption.curr,
                size: this.prodPageOption.size,
                hsCodeStatus: "0",
                searchCondition: ""
            };
        }

        HsCodeController.prototype = {
            init: function () {
                var self = this;
                var data = this.prodPageOption;
                _.extend(data, {"hsCodeStatus": this.searchInfo.hsCodeStatus});
                _.extend(data, {"searchCondition": this.searchInfo.searchCondition});

                self.hsCodeInfoService.init(data).then(function (res) {
                    self.hsSettedData = res.data;
                    self.hsCodeList = self.hsSettedData.hsCodeList;
                    self.hsCodeValue = self.hsSettedData.hsCodeValue;
                    console.log(self.hsSettedData);
                })
            },
            get: function (page) {
                var self = this;
                self.prodPageOption.curr = !page ? self.prodPageOption.curr : page;
                self.hsCodeInfoService.get(self.getTaskInfo).then(function (res) {
                })
            },
            search: function (page) {
                var self = this;
                self.prodPageOption.curr = !page ? self.prodPageOption.curr : page;
                self.hsCodeInfoService.search(self.searchInfo).then(function (res) {
                })
            },
            save: function (list) {
                var self = this;
                if (list.selectedValue) self.notify.success('TXT_MSG_UPDATE_SUCCESS');
                self.notify.warning('请继续完善税号设置');
            }
        };

        return HsCodeController;
    })())
});