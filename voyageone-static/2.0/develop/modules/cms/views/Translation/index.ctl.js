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
], function (cms, Status) {
    return cms.controller('translationDetailController', (function () {

        function translationDetailController($routeParams, $translate, translationService, notify, confirm,alert) {

            this.routeParams = $routeParams;
            this.translate = $translate;
            this.translationService = translationService;
            this.notify = notify;
            this.confirm = confirm;
            this.alert = alert;
        }

        translationDetailController.prototype = {

            // 获取初始化数据
            initialize: function () {
                this.translationService.getTasks()
                    .then(function (res) {
                        this.taskInfos = res.data.taskInfo;
                        this.totalDoneCount = this.taskInfos.totalDoneCount;
                        this.totalUndoneCount = this.taskInfos.totalUndoneCount;
                        this.userDoneCount = this.taskInfos.userDoneCount;
                        this.searchCondition = this.taskInfos.searchCondition;
                        this.distributeRule = this.taskInfos.distributeRule;
                        this.distributeCount = 10;
                        this.productTranslationBeanList = res.data.taskInfo.productTranslationBeanList;
                        this.sortFieldOptions = [];
                        this.sortRule = "true";
                        var options = res.data.sortFieldOptions;
                        for (var key in options){
                            var item = {value:key,name:options[key]}
                            this.sortFieldOptions.push(item);
                        }
                        this.lenInfo = res.data.lenSetInfo;

                    }.bind(this), function (res) {
                        this.notify(res.message);
                    }.bind(this))
            },

            // 分发翻译任务.
            assignTasks: function (model) {

                var assignRule = model.distributeRule;
                var assignCount = model.distributeCount;

                if (this.productTranslationBeanList && this.productTranslationBeanList.length > 0) {

                    this.alert("您尚有未完成任务，请先完成所有任务！")

                }else if (assignCount>20){

                    //alert(this.translate.instant(''))
                    this.alert("最多不能超过20个任务!")

                } else if(assignCount > this.totalUndoneCount){

                    this.alert("获取任务数量不能超过剩余任务数量！")

                } else {
                    var assignParms = {};

                    if (model.sortCondition != null && model.sortCondition != ''){

                        assignParms = {"sortCondition":model.sortCondition,"sortRule":model.sortRule,"distributeRule":assignRule,"distributeCount":assignCount}
                    }else {
                        assignParms = {"distributeRule":assignRule,"distributeCount":assignCount}
                    }

                    this.translationService.assignTasks(assignParms)
                        .then(function (res) {

                            this.taskInfos = res.data.taskInfo;
                            this.totalDoneCount = this.taskInfos.totalDoneCount;
                            this.totalUndoneCount = this.taskInfos.totalUndoneCount;
                            this.userDoneCount = this.taskInfos.userDoneCount;
                            this.productTranslationBeanList = res.data.taskInfo.productTranslationBeanList;

                        }.bind(this))
                }

            },

            // 暂存当前任务
            saveTask: function (productItem,index) {
                this.index = index;
                this.translationService.saveTask(productItem)
                    .then(function (res){
                        this.productTranslationBeanList[this.index].modifiedTime = res.data.taskInfo.modifiedTime;
                        this.totalDoneCount = res.data.taskInfo.totalDoneCount;
                        this.totalUndoneCount = res.data.taskInfo.totalUndoneCount;
                        this.userDoneCount = res.data.taskInfo.userDoneCount;
                        this.notify.success (this.translate.instant('TXT_MSG_UPDATE_SUCCESS'));
                    }.bind(this))
            },

            // 提交当前任务.
            submitTask: function (productItem,index) {
                this.index = index;
                this.translationService.submitTask(productItem,index)
                    .then(function (res){
                        this.productTranslationBeanList.splice(this.index,1);
                        this.totalDoneCount = res.data.taskInfo.totalDoneCount;
                        this.totalUndoneCount = res.data.taskInfo.totalUndoneCount;
                        this.userDoneCount = res.data.taskInfo.userDoneCount;
                        this.notify.success (this.translate.instant('TXT_MSG_UPDATE_SUCCESS'));
                    }.bind(this))
            },

            // 从主产品拷贝翻译信息.
            copyFormMainProduct: function (productItem,index) {
                this.index = index;
                this.translationService.copyFormMainProduct(productItem)
                    .then(function (res){
                        this.productTranslationBeanList[this.index] = res.data.translationInfo;
                    }.bind(this))
            },

            // 查询历史任务.
            searchHistoryTasks: function (taskInfos) {
                var parms = {"searchCondition":taskInfos.searchCondition}
                this.translationService.searchHistoryTasks(parms)
                    .then(function (res) {
                        this.productTranslationBeanList = res.data.taskInfo.productTranslationBeanList;
                    }.bind(this))
            },

            // 清空查询条件.
            clearConditions: function () {
                this.searchCondition = "";
                this.distributeCount = "";
            }
        };

        return translationDetailController
    })());
});
