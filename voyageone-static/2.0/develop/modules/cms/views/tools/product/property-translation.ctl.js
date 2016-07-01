/**
 * Created by lewis on 2016/02/18,
 * Refactored by Vantis on 2016/06/29.
 */
define([
    'cms',
    'angular'
], function (cms, angular) {
    cms.controller('translationManageController', (function () {
        function TranslationManageController($translate, translationService, notify, confirm, alert) {
            this.translate = $translate;
            this.translationService = translationService;
            this.notify = notify;
            this.confirm = confirm;
            this.alert = alert;
            this.searchBtnClicked = false;
            this.vm = {
                taskInfo: {},
                taskList: [],
                taskDetail: {},
                searchInfo: {
                    pageSize: 20
                },
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
                self.translationService.init().then(function (res) {
                    self.vm = res.data;
                    self.vm.taskDetail = {};
                    if (self.vm.taskDetail == {})
                        alert(self.vm.taskDetail);
                })
            },

            // 分发翻译任务.
            assign: function () {
                var self = this;
                self.translationService.assign(self.vm.assignInfo).then(function (res) {
                    self.vm = res.data;
                })
            },

            // 暂存当前任务
            save: function () {
                var self = this;
                var req = angular.copy(self.vm.taskDetail);
                req.customProps = req.customProps.filter(function (customProp) {
                    return (customProp.feedAttr && customProp.feedAttrCn);
                });
                self.translationService.save(req).then(function (res) {
                    self.vm.taskSummary = res.data.taskSummary;
                    self.vm.taskDetail = res.data.taskDetail;
                    self.alert("保存成功.");
                });
            },

            // 提交当前任务.
            submit: function () {
                var self = this;
                var req = angular.copy(self.vm.taskDetail);
                req.customProps = req.customProps.filter(function (customProp) {
                    return (customProp.feedAttr && customProp.feedAttrCn);
                });
                self.translationService.submit(req).then(function (res) {
                    self.vm.taskInfo.productTranslationBeanList.splice(index, 1);
                    self.vm.taskInfo.totalDoneCount = res.data.totalDoneCount;
                    self.vm.taskInfo.totalUndoneCount = res.data.totalUndoneCount;
                    self.vm.taskInfo.userDoneCount = res.data.userDoneCount;
                    self.notify.success(self.translate.instant('TXT_MSG_UPDATE_SUCCESS'));
                    self.vm.prodPageOption.total = self.vm.prodPageOption.total - 1;
                })
            },

            // 查询历史任务.
            search: function () {
                var self = this;
                self.searchBtnClicked = true;
                self.vm.taskList = [
                    {
                        "prodId": 5954,
                        "translateStatus": 1,
                        "imgUrl": "http://XXX/XXXX/XXX.jpg",
                        "category": "Bracelets-Anklets-No Stone",
                        "code": "1FMA3324Y11",
                        "name": "Just Gold Beaded Anklet in 14K Two-Tone Gold",
                        "totalDistributeUndoneCount": 907,
                        "MainCategory": "Bracelets-Anklets-No Stone",
                        "translator": "XXX",
                        "translateTime": "2016-06-27 12:07:33"
                    },
                    {
                        "prodId": 5955,
                        "translateStatus": 0,
                        "imgUrl": "http://XXX/XXXX/XXX.jpg",
                        "category": "Bracelets-Anklets-No Stone",
                        "code": "1FMA3324Y11",
                        "name": "Just Gold Beaded Anklet in 14K Two-Tone Gold",
                        "totalDistributeUndoneCount": 907,
                        "MainCategory": "Bracelets-Anklets-No Stone",
                        "translator": "XXX",
                        "translateTime": "2016-06-27 12:07:33"
                    }
                ]
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
