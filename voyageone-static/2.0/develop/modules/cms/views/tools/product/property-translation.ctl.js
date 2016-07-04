/**
 * Created by lewis on 2016/02/18,
 * Refactored by Vantis on 2016/06/29.
 */
define([
    'cms',
    'angular',
    'modules/cms/controller/popup.ctl'
], function (cms, angular) {
    cms.controller('translationManageController', (function () {
        function TranslationManageController(translationService, notify, confirm, alert, popups) {
            this.translationService = translationService;
            this.notify = notify;
            this.confirm = confirm;
            this.alert = alert;
            this.popups = popups;
            this.searchBtnClicked = false;
            this.searchPageSizeOption = [
                {name: "10行", value: 10},
                {name: "20行", value: 20},
                {name: "50行", value: 50},
                {name: "100行", value: 100}
            ];
            this.searchPageSettings = {
                curr: 1,
                total: 0,
                size: 10,
                keyWord: "",
                translateStatus: "",
                fetch: this.searchPage.bind(this)
            };
            this.assignInfo = {
                codeOrName: "",
                priority: "",
                sort: ""
            };
            this.vm = {
                taskInfo: {},
                taskList: [],
                taskDetail: {},
                sortFieldOptions: []
            };
        }

        TranslationManageController.prototype = {
            init: function () {
                var self = this;
                self.translationService.init().then(function (res) {
                    self.vm.taskInfo = res.data.taskInfo;
                    self.vm.taskDetail = res.data.taskDetail;
                    self.vm.sortFieldOptions = res.data.sortFieldOptions;
                    self.vm.taskSummary = res.data.taskSummary;
                    self.assignInfo = {
                        codeOrName: "",
                        sort: "desc"
                    };
                    self.assignInfo.priority = self.vm.sortFieldOptions[0].value;
                })
            },

            // 获取任务.
            assign: function () {
                var self = this;
                self.searchBtnClicked = false;
                self.translationService.assign(self.assignInfo).then(function (res) {
                    self.vm.taskDetail = res.data.taskDetail;
                    self.vm.taskSummary = res.data.taskSummary;
                })
            },

            // 暂存当前任务
            save: function () {
                var self = this;
                var req = angular.copy(self.vm.taskDetail);
                req.customProps = req.customProps.filter(function (customProp) {
                    return (customProp.feedAttrCn);
                });
                self.translationService.save(req).then(function (res) {
                    self.vm.taskSummary = res.data.taskSummary;
                    self.vm.taskDetail = res.data.taskDetail;
                    self.notify.success("保存成功.");
                });
            },

            // 提交当前任务.
            submit: function () {
                var self = this;
                var req = angular.copy(self.vm.taskDetail);
                req.customProps = req.customProps.filter(function (customProp) {
                    return (customProp.feedAttrCn);
                });
                self.translationService.submit(req).then(function (res) {
                    self.vm.taskInfo = res.data.taskInfo;
                    self.vm.taskSummary = res.data.taskSummary;
                    self.notify.success("提交成功");
                })
            },

            // 查询历史任务.
            search: function () {
                var self = this;
                var searchInfo = {
                    keyWord: self.searchPageSettings.keyWord,
                    translateStatus: self.searchPageSettings.translateStatus,
                    pageSize: self.searchPageSettings.size,
                    pageNum: self.searchPageSettings.curr
                };
                self.searchBtnClicked = true;
                self.translationService.search(searchInfo).then(function (res) {
                    self.vm.taskList = res.data.taskList;
                    self.searchPageSettings.total = res.data.total;
                })
            },

            // 查询页数跳转
            searchPage: function (page) {
                var self = this;
                self.searchPageSettings.curr = !page ? self.searchPageSettings.curr : page;
                var searchInfo = {
                    keyWord: self.searchPageSettings.keyWord,
                    translateStatus: self.searchPageSettings.translateStatus,
                    pageSize: self.searchPageSettings.size,
                    pageNum: self.searchPageSettings.curr
                };
                self.translationService.search(searchInfo).then(function (res) {
                    self.vm.taskList = res.data.taskList;
                    self.searchPageSettings.total = res.data.total;
                })
            },

            // 跳转Code编辑页面.
            get: function (task) {
                var self = this;
                self.translationService.get(task).then(function (res) {
                    self.searchBtnClicked = false;
                    self.vm.taskDetail = res.data.taskDetail;
                })
            },

            // 清空查询条件.
            clearConditions: function () {
                var self = this;
                self.searchPageSettings.curr = 1;
                self.searchPageSettings.size = 10;
                self.searchPageSettings.keyWord = "";
                self.searchPageSettings.translateStatus = "";
            },

            // 图片popup
            popUpImages: function (item) {
                if (item == undefined || item.commonFields == undefined) {
                    return;
                }
                var picList = [];
                if (item.commonFields) {
                    for (var attr in item.commonFields) {
                        if (attr.indexOf("images1") >= 0) {
                            var image = _.map(item.commonFields[attr], function (entity) {
                                var imageKeyName = "image" + attr.substring(6, 7);
                                return entity[imageKeyName] != null ? entity[imageKeyName] : "";
                            });
                            picList.push(image);
                        }
                    }
                } else if (item.image1) {
                    picList.push(item.image1);
                }
                this.popups.openImagedetail({'mainPic': picList[0][0], 'picList': picList});
            },

            // 翻译状态转换
            getStatusNameFromStatusValue: function (status) {
                if (status == 0)
                    return "未翻译";
                else if (status == 1)
                    return "已翻译";
                else return "未知";
            },

            // 时间戳转换
            getDateTimeFromTimeStamp: function (timeStamp) {
                var date = new Date(timeStamp);
                return date;
            },

            // 跳转链接
            openUrl: function (url) {
                window.open(url);
            }
        };

        return TranslationManageController;
    })())
});
