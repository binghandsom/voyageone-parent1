/**
 * Created by 123 on 2016/2/16.
 */
/**
 * @Description:
 *
 * @User: lewis
 * @Version: 1.0.0, 2016/02/18
 */

define([
    'cms',
    'modules/cms/controller/popup.ctl'
], function (cms) {
    return cms.controller('translationDetailController', (function () {

        function TranslationDetailController($routeParams, $translate, translationService, notify, confirm,alert) {

            this.routeParams = $routeParams;
            this.$translate = $translate;
            this.translationService = translationService;
            this.notify = notify;
            this.confirm = confirm;
            this.alert = alert;

            this.searchCondition = "";
            this.tranSts = -1;
            this.sortFieldOptions = [];
            this.lenInfo = {};
            this.getTaskInfo = {
                distributeRule: 1,
                distributeCount: 10,
                sortCondition: "",
                sortRule: ""
            };
        }

        TranslationDetailController.prototype = {

            // 获取初始化数据
            initialize: function () {
                var self =  this;
                this.translationService.getTasks()
                    .then(function (res) {
                        self.taskInfos = res.data.taskInfos;
                        self.sortFieldOptions = res.data.sortFieldOptions;
                        self.lenInfo = res.data.lenSetInfo;
                    }.bind(this), function (res) {
                        this.notify(res.message);
                    }.bind(this))
            },

            // 分发翻译任务.
            assignTasks: function () {
                if (this.taskInfos.productTranslationBeanList.length > 0){
                    this.alert(this.$translate.instant('TXT_MSG_HAVE_UN_TRANSLATED_TASK'));
                } else {
                    var self =  this;
                    this.translationService.assignTasks(self.getTaskInfo)
                        .then(function (res) {
                            self.taskInfos = res.data;
                        }.bind(this))
                }
            },

            // 暂存当前任务
            saveTask: function (productItem,index) {
                var self = this;
                this.translationService.saveTask(productItem)
                    .then(function (res){
                        self.taskInfos.productTranslationBeanList[index].modifiedTime = res.data.modifiedTime;
                        self.taskInfos.totalDoneCount = res.data.totalDoneCount;
                        self.taskInfos.totalUndoneCount = res.data.totalUndoneCount;
                        self.taskInfos.userDoneCount = res.data.userDoneCount;
                        self.notify.success (self.$translate.instant('TXT_MSG_UPDATE_SUCCESS'));
                    }.bind(this))
            },

            // 提交当前任务.
            submitTask: function (productItem,index) {
                var self = this;
                this.translationService.submitTask(productItem)
                    .then(function (res){
                        self.taskInfos.productTranslationBeanList.splice(index,1);
                        self.taskInfos.totalDoneCount = res.data.totalDoneCount;
                        self.taskInfos.totalUndoneCount = res.data.totalUndoneCount;
                        self.taskInfos.userDoneCount = res.data.userDoneCount;
                        self.notify.success (self.$translate.instant('TXT_MSG_UPDATE_SUCCESS'));
                    }.bind(this))
            },

            // 从主产品拷贝翻译信息.
            copyFormMainProduct: function (productItem,index) {
                var self = this;
                this.translationService.copyFormMainProduct(productItem)
                    .then(function (res){
                        self.taskInfos.productTranslationBeanList[index] = res.data;
                    }.bind(this))
            },

            // 查询历史任务.
            searchHistoryTasks: function () {
                var self = this;
                this.translationService.searchHistoryTasks({searchCondition:self.searchCondition,tranSts:self.tranSts})
                    .then(function (res) {
                        self.taskInfos.productTranslationBeanList = res.data.productTranslationBeanList;
                    }.bind(this))
            },

            // 撤销翻译任务.
            cancelTask: function (productItem, index) {
                var self = this;
                this.translationService.cancelTask({searchCondition:productItem.productCode});
            },

            // 清空查询条件.
            clearConditions: function () {
                this.getTaskInfo = {
                    distributeRule: 1,
                    distributeCount: 10,
                    sortCondition: "",
                    sortRule: ""
                };
            }
        };

        return TranslationDetailController;
    })());
});
