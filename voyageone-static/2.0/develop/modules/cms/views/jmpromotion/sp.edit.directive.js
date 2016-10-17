define([
    'cms',
    'modules/cms/controller/popup.ctl'
], function (cms) {

    function SpEditDirectiveController($routeParams, jmPromotionService, alert, confirm, $translate, $filter) {
        this.$routeParams = $routeParams;
        this.jmPromotionService = jmPromotionService;
        this.alert = alert;
        this.confirm = confirm;
        this.$translate = $translate;
        this.$filter = $filter;
        this.vm = {"jmMasterBrandList": []};
        this.editModel = {model: {}};
        this.datePicker = [];
    }

    SpEditDirectiveController.prototype.init = function () {
        var self = this,
            vm = self.vm,
            routeParams = self.$routeParams,
            editModel = self.editModel,
            jmPromotionService = self.jmPromotionService;
        vm.currentTime = new Date();

        jmPromotionService.init().then(function (res) {
            vm.jmMasterBrandList = res.data.jmMasterBrandList;
        });

        jmPromotionService.getEditModelExt({model: {id: routeParams.jmpromId}}).then(function (res) {
            editModel.model = res.data.model;
            editModel.tagList = res.data.tagList;
            editModel.model.activityStart = formatToDate(editModel.model.activityStart);
            editModel.model.activityEnd = formatToDate(editModel.model.activityEnd);
            editModel.model.prePeriodStart = formatToDate(editModel.model.prePeriodStart);
            editModel.model.signupDeadline = formatToDate(editModel.model.signupDeadline);
            // 准备期是否结束
            vm.isDeadline = false;
            if (editModel.model.signupDeadline < vm.currentTime) {
                vm.isDeadline = true;
            }
            // 预热是否开始
            vm.isBeginPre = false;
            if (editModel.model.prePeriodStart < vm.currentTime) {
                vm.isBeginPre = true;
            }
            // 活动是否开始
            vm.isBegin = false;
            if (editModel.model.activityStart < vm.currentTime) {
                vm.isBegin = true;
            }
            // 活动是否结束
            vm.isEnd = false;
            if (editModel.model.activityEnd < vm.currentTime) {
                vm.isEnd = true;
            }

            if (editModel.model.promotionType) {
                editModel.model.promotionType = editModel.model.promotionType.toString();
            }

            // 转换活动场景的值
            if (editModel.model.promotionScene) {
                var sceneArr = editModel.model.promotionScene.split(",");
                for (var attr in sceneArr) {
                    editModel.model['promotionScene_' + sceneArr[attr]] = true;
                }
            }
        });

    };

    SpEditDirectiveController.prototype.addTag = function () {
        var self = this,
            editModel = self.editModel;

        if (editModel.tagList) {
            editModel.tagList.push({"id": "", "channelId": "", "tagName": "", active: 1});
        } else {
            editModel.tagList = [{"id": "", "channelId": "", "tagName": "", active: 1}];
        }
    };

    SpEditDirectiveController.prototype.getTagList = function () {
        var self = this,
            editModel = self.editModel;

        return _.filter( editModel.tagList, function(tag){ return tag.active==1; }) || [];
    };

    SpEditDirectiveController.prototype.delTag = function () {
        var self = this,
            confirm = self.confirm,
            translate = self.$translate;

        confirm(translate.instant('TXT_MSG_DELETE_ITEM'))
            .then(function () {
                tag.active=0;
            });
    };

    SpEditDirectiveController.prototype.save = function(saveType) {
        var self = this,
            jmPromotionService = self.jmPromotionService;
        var param = {};
        param.tagList= _.filter( self.editModel.tagList, function(tag){ return tag.tagName != "";});
        param.model = angular.copy(self.editModel.model);
        param.extModel = angular.copy(self.editModel.extModel);

        if (param.extModel.displayPlatform_1) {
            param.extModel.displayPlatform = "1";
        }

        param.model.activityStart = formatToStr(param.model.activityStart, self.$filter);
        param.model.activityEnd = formatToStr(param.model.activityEnd, self.$filter);
        param.model.prePeriodStart = formatToStr(param.model.prePeriodStart, self.$filter);
        param.model.prePeriodEnd = param.model.activityEnd;
        param.model.signupDeadline = formatToStr(param.model.signupDeadline, self.$filter);

        param.hasExt = true;
        param.saveType = saveType;
        jmPromotionService.saveModel(param).then(function(res){

        });
    };

    /**
     * @param date 字符串格式为yyyy-MM-dd ss:ss:ss
     * @returns {Date}
     */
    function formatToDate(date) {
        //$filter("date")(new Date(date),"yyyy-MM-dd HH:mm:ss");
        return new Date(date);
    }

    function formatToStr(date,filter){
        return filter("date")(new Date(date),"yyyy-MM-dd HH:mm:ss");
    }

    cms.directive('spEdit', [function spEditDirectiveFactory() {
        return {
            restrict: 'E',
            controller: ['$routeParams', 'jmPromotionService', 'alert', 'confirm', '$translate', '$filter', SpEditDirectiveController],
            controllerAs: 'ctrlEdit',
            templateUrl: '/modules/cms/views/jmpromotion/sp.edit.directive.html'
        }
    }]);
});