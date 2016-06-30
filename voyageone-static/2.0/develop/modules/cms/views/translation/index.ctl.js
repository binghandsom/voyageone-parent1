/**
 * Created by lewis on 2016/02/18,
 * Refactored by Vantis on 2016/06/29.
 */
define([
    'cms',
    'modules/cms/controller/popup.ctl'
], function (cms) {
    cms.controller('translationManageController', (function () {
        function TranslationManageController($translate, translationService, notify, confirm, alert) {
            this.translate = $translate;
            this.translationService = translationService;
            this.notify = notify;
            this.confirm = confirm;
            this.alert = alert;
            this.searchBtnClicked = false;
            this.vm = {
                data: {},
                searchInfo: {},
                taskInfos: {},
                prodPageOption: {curr: 1, total: 0, fetch: self.searchHistoryTasks},
                sortFieldOptions: [],
                lenInfo: {},
                assignInfo: {
                    codeOrName: "",
                    priority: "",
                    sort: ""
                }
            };
        }

        TranslationManageController.prototype = {
            init: function () {
                var self = this;
                self.vm.prodPageOption.curr = 1;
                // 获取初始化数据
                self.translationService.init()
                    .then(function (res) {
                        self.vm.data = res.data;
                    })
            },

            // 分发翻译任务.
            assign: function () {
                var self = this;
                self.translationService.assign(self.vm.assignInfo).then(function (res) {
                    self.vm.data = res.data;
                })
            },

            // 暂存当前任务
            save: function () {
                var self = this;
                console.info(self.vm.data.taskDetail);
                self.translationService.save(self.vm.data.taskDetail)
                    .then(function (res) {
                        self.vm.data.taskSummary = res.data.taskSummary;
                    })
            },

            // 提交当前任务.
            submit: function () {
                var self = this;
                if (!(self.vm.data.taskDetail.commonFields.originalTitleCn)) {

                } else {
                    self.translationService.submit(productItem)
                        .then(function (res) {
                            self.vm.taskInfos.productTranslationBeanList.splice(index, 1);
                            self.vm.taskInfos.totalDoneCount = res.data.totalDoneCount;
                            self.vm.taskInfos.totalUndoneCount = res.data.totalUndoneCount;
                            self.vm.taskInfos.userDoneCount = res.data.userDoneCount;
                            self.notify.success(self.translate.instant('TXT_MSG_UPDATE_SUCCESS'));
                            self.vm.prodPageOption.total = self.vm.prodPageOption.total - 1;
                        })
                }
            },

            // 从主产品拷贝翻译信息.
            copyFormMainProduct: function (productItem, index) {
                var self = this;
                self.translationService.copyFormMainProduct(productItem)
                    .then(function (res) {
                        self.vm.taskInfos.productTranslationBeanList[index] = res.data;
                    })
            },

            // 查询历史任务.
            searchHistoryTasks: function (page) {
                var self = this;
                self.btnclick = true;
                self.vm.prodPageOption.curr = !page ? self.vm.prodPageOption.curr : page;
                self.vm.searchInfo.pageNum = self.vm.prodPageOption.curr;
                self.vm.searchInfo.pageSize = self.vm.prodPageOption.size;

                self.translationService.searchHistoryTasks(self.vm.searchInfo)
                    .then(function (res) {
                        self.vm.taskInfos.productTranslationBeanList = res.data.productTranslationBeanList;
                        self.vm.prodPageOption.total = res.data.prodListTotal;
                    })
            },

            // 撤销翻译任务.
            cancelTask: function (productItem, index) {
                var self = this;
                self.translationService.cancelTask({prodCode: productItem.productCode})
                    .then(function (res) {
                        self.vm.taskInfos.totalDoneCount = res.data.totalDoneCount;
                        self.vm.taskInfos.totalUndoneCount = res.data.totalUndoneCount;
                        self.vm.taskInfos.userDoneCount = res.data.userDoneCount;
                        self.notify.success(self.translate.instant('TXT_MSG_UPDATE_SUCCESS'));
                        productItem.tranSts = 0;
                    })
            },

            // 清空查询条件.
            clearConditions: function () {
                var self = this;
                self.vm.searchInfo = {};
            }
        };

        return TranslationManageController;
    })())
});
