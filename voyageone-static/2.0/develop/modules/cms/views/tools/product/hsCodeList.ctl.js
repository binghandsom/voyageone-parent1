/**
 * Created by sofia on 6/2/2016.
 */
define([
    'cms',
    'modules/cms/controller/popup.ctl'
], function (cms) {
    cms.controller('HsCodeController', (function () {
        function HsCodeController(hsCodeInfoService, notify, popups, $feedSearchService) {
            this.hsCodeInfoService = hsCodeInfoService;
            this.$feedSearchService = $feedSearchService;
            this.prodPageOption = {curr: 1, total: 0, size: 10, fetch: this.search.bind(this)};
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
                code: ""
            };
            this.searchInfo = {
                curr: this.prodPageOption.curr,
                size: this.prodPageOption.size,
                hsCodeStatus: "1",
                searchCondition: ""
            };
            this.selected = false;
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
            get: function () {
                var self = this;
                if (!self.getTaskInfo.qty) self.getTaskInfo.order = "";
                if (self.hsCodeTaskCnt > self.max) return;
                if (!self.hsCodeTaskCnt && !self.getTaskInfo.code) return;
                else {
                    if (!self.hsCodeTaskCnt)self.getTaskInfo.hsCodeTaskCnt = 1;
                    else self.getTaskInfo.hsCodeTaskCnt = self.hsCodeTaskCnt;
                    self.hsCodeInfoService.get(self.getTaskInfo).then(function (res) {
                        self.hsSettedData = res.data.taskSummary;
                        self.hsCodeList = res.data.hsCodeList;
                        self.hsCodeValue = res.data.hsCodeValue;
                    })
                }
            },
            search: function (page, flg) {
                var self = this;
                if (flg === 10)  self.searchInfo.size = 10;
                if (flg === 20)  self.searchInfo.size = 20;
                if (flg === 50)  self.searchInfo.size = 50;
                if (flg === 100)  self.searchInfo.size = 100;
                self.searchInfo.curr = !page ? self.searchInfo.curr : page;

                self.hsCodeInfoService.search(self.searchInfo).then(function (res) {
                    self.max = self.hsCodeTaskCnt = res.data.hsCodeTaskCnt;
                    self.hsSettedData = res.data.taskSummary;
                    self.hsCodeList = res.data.hsCodeList;
                    self.prodPageOption.total = res.data.total;
                    self.hsCodeValue = res.data.hsCodeValue;
                });
            },
            clear: function () {
                var self = this;
                self.searchInfo.searchCondition = "";
            },
            save: function (list) {
                var self = this;
                if (list.common.fields.hsCodePrivate) {
                    self.selected = true;
                    self.notify.success('TXT_MSG_UPDATE_SUCCESS');
                    self.hsCodeInfoService.save({
                        "code": list.common.fields.code,
                        "hsCodePrivate": list.common.fields.hsCodePrivate
                    }).then(function (res) {
                        self.hsSettedData = res.data.taskSummary;
                    })
                }
                else {
                    self.notify.warning('TXT_CARRY_ON_THE_CURRENT_SETTING');
                }
            },
            openHsCodeImagedetail: function (item) {
                if (item.common == undefined || item.common.fields == undefined) return;
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
            openHsCodeCodeDetail: function (list) {
                var self = this;
                self.searchDetail = {};
                self.searchDetail.isAll = false;
                self.searchDetail.codeList = list.common.fields.code;
                self.searchDetail.pageNum = self.searchInfo.curr;
                self.searchDetail.pageSize = self.searchInfo.size;
                self.$feedSearchService.search(self.searchDetail).then(function (res) {
                    console.log(res.data.feedList);
                    self.feedInfo = res.data.feedList;
                    // 统计属性数
                    var attsMap = self.feedInfo[0].attribute;
                    var attsList = [];
                    if (attsMap != undefined) {
                        var d = attsMap['StoneColor'];
                        _.forEach(attsMap, function (value, key) {
                            var attsObj = {'aKey': key, 'aValue': value.join('; ')};
                            attsList.push(attsObj);
                        });
                    }
                    self.feedInfo[0].attsList = attsList;
                    if (self.feedInfo[0].attsList.length == 0) return;
                    self.popups.openCodeDetail({'attsList': self.feedInfo[0].attsList});
                })
            }
        };
        return HsCodeController;
    })())
});