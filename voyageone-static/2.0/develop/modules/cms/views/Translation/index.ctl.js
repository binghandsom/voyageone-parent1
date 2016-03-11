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
            translateex = $translate;
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
                        lenInfo = res.data.lenSetInfo;

                    }.bind(this), function (res) {
                        this.notify(res.message);
                    }.bind(this))
            },

            // 分发翻译任务.
            assignTasks: function (model) {

                var assignRule = model.distributeRule;
                var assignCount = model.distributeCount;

                if (this.productTranslationBeanList.length > 0){

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
            },

        };

        return translationDetailController
    })());
});

// 检查输入字数
var timeoutID = "";
var lenInfo = null;
var maxLen = 0;
var minLen = 0;
var translateex = null;
function cvtLen(lenType) {
    if (lenType == 1) {
        minLen = lenInfo.long_title.minLen;
        maxLen = lenInfo.long_title.maxLen;
    } else if (lenType == 2) {
        minLen = lenInfo.middle_title.minLen;
        maxLen = lenInfo.middle_title.maxLen;
    } else if (lenType == 3) {
        minLen = lenInfo.short_title.minLen;
        maxLen = lenInfo.short_title.maxLen;
    } else if (lenType == 4) {
        minLen = lenInfo.long_desc.minLen;
        maxLen = lenInfo.long_desc.maxLen;
    } else if (lenType == 5) {
        minLen = lenInfo.short_desc.minLen;
        maxLen = lenInfo.short_desc.maxLen;
    }
}



function chkWordSize(lenType, tobj) {
    cvtLen(lenType);
    clearTimeout(timeoutID);
    timeoutID = setTimeout(function() {
        var curLength = $(tobj).val().length;
        var od = $(tobj.parentNode).find("i#wordsizemsg");
        if (curLength < minLen) {
            od[0].innerHTML = '&nbsp;' + translateex.instant('TXT_MSG_INPUT_WORD_LENLOWLIMIT') + minLen + translateex.instant('TXT_MSG_INPUT_WORD_LENCHK2');
        } else if (curLength >= maxLen) {
            od[0].innerHTML = '&nbsp;' + translateex.instant('TXT_MSG_INPUT_WORD_LENLIMIT');
        } else {
            od[0].innerHTML = '&nbsp;' + translateex.instant('TXT_MSG_INPUT_WORD_LENCHK') + (maxLen - curLength) + translateex.instant('TXT_MSG_INPUT_WORD_LENCHK2');
        }
    },500);
}

function cmtWordSize(lenType, tobj) {
    cvtLen(lenType);
    var od = $(tobj.parentNode).find("i#wordsizemsg");
    $(od[0]).css("visibility", "visible");
    var curLength = $(tobj).val().length;
    if (curLength < minLen) {
        od[0].innerHTML = '&nbsp;' + translateex.instant('TXT_MSG_INPUT_WORD_LENLOWLIMIT') + minLen + translateex.instant('TXT_MSG_INPUT_WORD_LENCHK2');
    } else if (curLength >= maxLen) {
        od[0].innerHTML = '&nbsp;' + translateex.instant('TXT_MSG_INPUT_WORD_LENLIMIT');
    } else {
        od[0].innerHTML = '&nbsp;' + translateex.instant('TXT_MSG_INPUT_WORD_LENCHK') + (maxLen - curLength) + translateex.instant('TXT_MSG_INPUT_WORD_LENCHK2');
    }
}