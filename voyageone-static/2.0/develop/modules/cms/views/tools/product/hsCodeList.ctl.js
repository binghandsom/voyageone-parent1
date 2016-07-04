/**
 * Created by sofia on 6/2/2016.
 */
define([
    'cms',
    'modules/cms/controller/popup.ctl'
], function (cms) {
    cms.controller('HsCodeController', (function () {
        function HsCodeController(hsCodeInfoService, notify, popups) {
            this.hsCodeInfoService = hsCodeInfoService;
            this.prodPageOption = {curr: 1, total: 0, size: 10, fetch: this.search};
            this.hsCodeList = [];
            this.hsCodeValue = [];
            this.status = false;
            this.notify = notify;
            this.popups = popups;
            this.getTaskInfo = {
                curr: this.prodPageOption.curr,
                size: this.prodPageOption.size,
                qty: "1",
                order: "-1",
                code: "",
                hsCodeTaskCnt: 10
            };
            this.searchInfo = {
                curr: this.prodPageOption.curr,
                size: this.prodPageOption.size,
                hsCodeStatus: "1",
                searchCondition: ""
            };
        }

        HsCodeController.prototype = {
            init: function () {
                var self = this;
                var data = this.prodPageOption;
                _.extend(data, {"hsCodeStatus": this.searchInfo.hsCodeStatus});
                _.extend(data, {"searchCondition": this.searchInfo.searchCondition});

                self.hsCodeInfoService.init(data).then(function () {
                    self.search();
                })
            },
            get: function (page) {
                var self = this;
                self.prodPageOption.curr = !page ? self.prodPageOption.curr : page;
                if (!self.getTaskInfo.qty) self.getTaskInfo.order = "";
                self.hsCodeInfoService.get(self.getTaskInfo).then(function (res) {
                })
            },
            search: function (page) {
                var self = this;
                self.prodPageOption.curr = !page ? self.prodPageOption.curr : page;
                self.hsCodeInfoService.search(self.searchInfo).then(function (res) {
                    self.hsSettedData = res.data.taskSummary;
                    self.hsCodeList = res.data.hsCodeList;
                    self.hsCodeValue = res.data.hsCodeValue;
                })
            },
            save: function (list) {
                var self = this;
                if (list.selectedValue) self.notify.success('TXT_MSG_UPDATE_SUCCESS');
                else {
                    self.notify.warning('请继续完善税号设置');
                }
            },
            openHsCodeImagedetail: function (item) {
                if (item.common == undefined || item.common.fields == undefined) {
                    return;
                }
                var picList = [];
                for (var attr in item.common.fields) {
                    if (attr.indexOf("images1") >= 0) {
                        var image = _.map(item.common.fields[attr], function (entity) {
                            var imageKeyName = "image" + attr.substring(6, 7);
                            return entity[imageKeyName] != null ? entity[imageKeyName] : "";
                        });
                        picList.push(image);
                    }
                }
                this.popups.openImagedetail({'mainPic': picList[0][0], 'picList': picList});
            },
            openHsCodeCodeDetail: function (item) {
                // var self = this;
                // var feedObj = self.hsCodeList[item];
                // if (feedObj.commonNotNull.fields.length == 0) {
                //     return;
                // }
                // this.openCodeDetail({'attsList': feedObj.attsList});

            }
        };

        return HsCodeController;
    })())
});