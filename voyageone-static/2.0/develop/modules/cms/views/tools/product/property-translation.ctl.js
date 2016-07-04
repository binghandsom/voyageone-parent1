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

            /**
             * 搜索按钮点击检测 控制下方子页面内容显示
             * @type {boolean}
             */
            this.searchBtnClicked = false;

            /**
             * 历史任务搜索结果每页行数选项
             * @type {*[]}
             */
            this.searchPageSizeOption = [
                {name: "10行", value: 10},
                {name: "20行", value: 20},
                {name: "50行", value: 50},
                {name: "100行", value: 100}
            ];

            /**
             * 历史任务搜索设定
             * @type {{curr: number, total: number, size: number, keyWord: string, translateStatus: string, fetch: (function(this:TranslationManageController))}}
             */
            this.searchPageSettings = {
                curr: 1,
                total: 0,
                size: 10,
                keyWord: "",
                translateStatus: "",
                fetch: this.searchPage.bind(this)
            };

            /**
             * 获取任务设定
             * @type {{codeOrName: string, priority: string, sort: string}}
             */
            this.assignInfo = {
                codeOrName: "",
                priority: "",
                sort: ""
            };

            /**
             * 后端内容
             * @type {{taskInfo: {}, taskList: Array, taskDetail: {}, sortFieldOptions: Array}}
             */
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

            /**
             * 获取任务
             */
            assign: function () {
                var self = this;
                self.searchBtnClicked = false;
                self.translationService.assign(self.assignInfo).then(function (res) {
                    self.vm.taskDetail = res.data.taskDetail;
                    self.vm.taskSummary = res.data.taskSummary;
                })
            },

            /**
             * 暂存当前任务
             */
            save: function () {
                var self = this;
                var req = angular.copy(self.vm.taskDetail);
                req.customProps = req.customProps.filter(function (customProp) {
                    return (customProp.feedAttrCn);
                });
                self.translationService.save(req).then(function (res) {
                    self.vm.taskSummary = res.data.taskSummary;
                    self.vm.taskDetail = res.data.taskDetail;
                    self.notify.success('TXT_SAVE_SUCCESS');
                });
            },

            /**
             * 提交当前任务
             */
            submit: function () {
                var self = this;
                var req = angular.copy(self.vm.taskDetail);
                req.customProps = req.customProps.filter(function (customProp) {
                    return (customProp.feedAttrCn);
                });
                self.translationService.submit(req).then(function (res) {
                    self.vm.taskInfo = res.data.taskInfo;
                    self.vm.taskSummary = res.data.taskSummary;
                    self.notify.success('TXT_SUBMIT_SUCCESS');
                })
            },

            /**
             * 查询历史任务
             */
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

            /**
             * 变更搜索结果页页数
             * @param page
             */
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

            /**
             * 跳转Code的翻译页面
             * @param task 对应列表中的行内容
             */
            get: function (task) {
                var self = this;
                self.translationService.get(task).then(function (res) {
                    self.searchBtnClicked = false;
                    self.vm.taskDetail = res.data.taskDetail;
                })
            },

            /**
             * 清空查询条件
             */
            clearConditions: function () {
                var self = this;
                self.searchPageSettings.curr = 1;
                self.searchPageSettings.size = 10;
                self.searchPageSettings.keyWord = "";
                self.searchPageSettings.translateStatus = "";
            },

            /**
             * 图片popUp
             * @param item
             */
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

            /**
             * 翻译状态转换
             * @param status
             * @returns {*}
             */
            getStatusNameFromStatusValue: function (status) {
                if (status == 0)
                    return 'TXT_TRANSLATION_SEARCH_SELECT_NOT_TRANSLATED';
                else if (status == 1)
                    return 'TXT_TRANSLATION_SEARCH_SELECT_TRANSLATED';
                else return 'TXT_UNKNOWN';
            },

            /**
             * 时间戳转换
             * @param timeStamp Unix时间戳
             * @returns {Date}
             */
            getDateTimeFromTimeStamp: function (timeStamp) {
                var date = new Date(timeStamp * 1000);
                return date;
            },

            /**
             * 跳转链接
             * @param url
             */
            openUrl: function (url) {
                window.open(url);
            }
        };

        return TranslationManageController;
    })())
});
