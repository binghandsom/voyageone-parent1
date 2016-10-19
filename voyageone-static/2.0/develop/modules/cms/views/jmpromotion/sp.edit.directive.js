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

        jmPromotionService.getEditModelExt({model: {id: routeParams.jmpromId}, hasExt: true}).then(function (res) {
            editModel.model = res.data.model;
            editModel.extModel = res.data.extModel;
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
            if (editModel.extModel.directmailType == undefined || editModel.extModel.directmailType == null || editModel.extModel.directmailType == '') {
                editModel.extModel.directmailType = '1';
            }

            // 转换活动场景的值
            if (editModel.model.promotionScene) {
                var sceneArr = JSON.parse(editModel.model.promotionScene);
                editModel.model.promotionScene = [];
                angular.forEach(sceneArr, function(element) {
                    editModel.model.promotionScene[element] = true;
                });
            } else {
                editModel.model.promotionScene = [];
            }
            // 转换展示平台的值
            if (editModel.extModel.extModel) {
                var sceneArr = JSON.parse(editModel.extModel.extModel);
                editModel.extModel.displayPlatform = [];
                angular.forEach(sceneArr, function(element) {
                    editModel.extModel.displayPlatform[element] = true;
                });
            } else {
                editModel.extModel.displayPlatform = [];
            }
            // 转换预展示频道的值
            if (editModel.extModel.preDisplayChannel) {
                var sceneArr = JSON.parse(editModel.extModel.preDisplayChannel);
                editModel.extModel.preDisplayChannel = [];
                angular.forEach(sceneArr, function(element) {
                    editModel.extModel.preDisplayChannel[element] = true;
                });
            } else {
                editModel.extModel.preDisplayChannel = [];
            }

            jmPromotionService.init().then(function (res) {
                vm.jmMasterBrandList = res.data.jmMasterBrandList;
                // 记住主品牌初始值
                _getJmBrandEnName(self, editModel.model.cmsBtJmMasterBrandId);
            });
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

    SpEditDirectiveController.prototype.delTag = function () {
        var self = this,
            confirm = self.confirm,
            translate = self.$translate;

        confirm(translate.instant('TXT_MSG_DELETE_ITEM'))
            .then(function () {
                tag.active=0;
            });
    };

    // 当专场主品牌改变时，记住所选值，用于创建页面标识(主要是品牌英文名，并且要简单处理，去空格和特殊符号)
    SpEditDirectiveController.prototype.onJmBrandChange = function () {
        var self = this;
        _getJmBrandEnName(self, self.editModel.model.cmsBtJmMasterBrandId);
        self.createActId();
    };

    // 创建页面标识
    SpEditDirectiveController.prototype.createActId = function () {
        var self = this;
        var idDate = self.$filter("date")(new Date(),"yyyyMMdd");
        var idTime = self.$filter("date")(new Date(),"HH-mm-ss-sss").replace(/-/g, "");
        idTime = parseInt(idTime).toString(36);
        var jmBrandId = self.editModel.model.cmsBtJmMasterBrandId;
        var jmBrandName = self.vm.brandEnName;
        if (jmBrandName == null || jmBrandName == undefined) {
            jmBrandName = '';
        }
        var mainChannel = '';
        var mainChannel2 = '';
        if (self.editModel.extModel.mainChannel) {
            mainChannel = self.editModel.extModel.mainChannel.substring(0, self.editModel.extModel.mainChannel.indexOf(':'));
            mainChannel2 = self.editModel.extModel.mainChannel.substring(self.editModel.extModel.mainChannel.indexOf(':') + 1);
        }

        if (self.editModel.model.promotionType == '3') {
            if (self.editModel.extModel.productType == null || self.editModel.extModel.productType == undefined) {
                self.editModel.extModel.productType = '';
            }
            var pageId = idDate + mainChannel + '_' + self.editModel.extModel.productType + '_' + jmBrandId + '_' + idTime;
            self.editModel.extModel.pcPageId = pageId + '_pc';
            self.editModel.extModel.appPageId = pageId + '_app';
        } else {
            var pageId = mainChannel2 + '_' + jmBrandName + '_' + idDate + '_' + idTime;
            self.editModel.extModel.pcPageId = pageId + '_pc';
            self.editModel.extModel.appPageId = pageId + '_app';
        }
    };

    // 保存修改  saveType：0->暂存  1->提交
    // 只有在'提交'时才检查输入项目
    SpEditDirectiveController.prototype.save = function(saveType) {
        var self = this,
            alert = self.alert,
            jmPromotionService = self.jmPromotionService;

        if (saveType) {
            var start = new Date(self.editModel.model.activityStart);
            var end = new Date(self.editModel.model.activityEnd);

            if (self.editModel.model.activityStart > self.editModel.model.activityEnd) {
                alert("活动时间检查：请输入结束时间>开始时间，最小间隔为30分钟。");
                return;
            }

            if (end.getTime() - start.getTime() < 30 * 60 * 1000) {
                alert("活动时间检查：最小间隔为30分钟。");
                return;
            }

            if (self.editModel.model.prePeriodStart > self.editModel.model.prePeriodEnd) {
                alert("预热时间检查：请输入结束时间>开始时间。");
                return;
            }

            if (self.editModel.model.prePeriodStart > self.editModel.model.activityStart) {
                alert("预热开始时间不能晚于活动开始时间");
                return;
            }

            if (self.editModel.model.signupDeadline > self.editModel.model.prePeriodStart) {
                alert("报名截止日期不能晚于预热开始时间");
                return;
            }

            if (self.editModel.tagList.length === 0) {
                alert("请至少添加一个标签");
                return;
            }
            var hasTag = _.every(self.editModel.tagList, function (element) {
                return element.tagName;
            });
            if (!hasTag)
                return;

            var extModel = self.editModel.extModel;
            if (extModel.isPromotionFullMinus && (extModel.promotionFullAmount == null || extModel.promotionFullAmount == '' || extModel.promotionFullAmount == undefined
                || extModel.promotionMinusAmount == null || extModel.promotionMinusAmount == '' || extModel.promotionMinusAmount == undefined)) {
                alert("当设置满减优惠时，必须填写优惠限额及优惠金额");
                return;
            }
            if (extModel.isPromotionEachfullMinus && (extModel.promotionEachfullAmount == null || extModel.promotionEachfullAmount == '' || extModel.promotionEachfullAmount == undefined
                || extModel.promotionEachminusAmount == null || extModel.promotionEachminusAmount == '' || extModel.promotionEachminusAmount == undefined)) {
                alert("当设置每满减优惠时，必须填写优惠限额及优惠金额");
                return;
            }
            if (extModel.jmCoupons && (extModel.jmCouponsMoney == null || extModel.jmCouponsMoney == '' || extModel.jmCouponsMoney == undefined)) {
                alert("当设置聚美承担优惠券时，必须填写优惠券金额");
                return;
            }
            if (extModel.voCoupons && (extModel.voCouponsMoney == null || extModel.voCouponsMoney == '' || extModel.voCouponsMoney == undefined)) {
                alert("当设置VO承担优惠券时，必须填写优惠券金额");
                return;
            }
        }

        var param = {};
        param.tagList= _.filter( self.editModel.tagList, function(tag){ return tag.tagName != "";});
        param.model = angular.copy(self.editModel.model);
        param.extModel = angular.copy(self.editModel.extModel);

        param.extModel.mainChannel = param.extModel.mainChannel.substring(0, param.extModel.mainChannel.indexOf(':') + 1);

        param.model.promotionScene = JSON.stringify(_returnKey (param.model.promotionScene));
        param.extModel.displayPlatform = JSON.stringify(_returnKey (param.extModel.displayPlatform));
        param.extModel.preDisplayChannel = JSON.stringify(_returnKey (param.extModel.preDisplayChannel));

        if (!param.extModel.isPromotionFullMinus) {
            param.extModel.promotionFullAmount = null;
            param.extModel.promotionMinusAmount = null;
        }
        if (!param.extModel.isPromotionEachfullMinus) {
            param.extModel.promotionEachfullAmount = null;
            param.extModel.promotionEachminusAmount = null;
        }
        if (!param.extModel.jmCoupons) {
            param.extModel.jmCouponsMoney = null;
        }
        if (!param.extModel.voCoupons) {
            param.extModel.voCouponsMoney = null;
        }

        param.model.activityStart = formatToStr(param.model.activityStart, self.$filter);
        param.model.activityEnd = formatToStr(param.model.activityEnd, self.$filter);
        param.model.prePeriodStart = formatToStr(param.model.prePeriodStart, self.$filter);
        param.model.prePeriodEnd = param.model.activityEnd;
        param.model.signupDeadline = formatToStr(param.model.signupDeadline, self.$filter);
        param.extModel.shareTitle = param.extModel.marketingTitle;
        param.extModel.shareContent = param.extModel.marketingCopywriter + '' + param.extModel.promotionalCopy;

        param.hasExt = true;
        param.saveType = saveType;
        param.extModel.promotionId = self.$routeParams.promId;
        param.extModel.jmpromotionId = self.$routeParams.jmpromId;

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

    /**
     * 如果checkbox被选中,返回被选中的value.
     * eg.[{new: true, pending: false, approved: true}] -> [new, approved]
     * @param object
     * @returns {*}
     */
    function _returnKey(object) {
        return _.chain(object)
            .map(function(value, key) { return value ? key : null;})
            .filter(function(value) { return value;})
            .value();
    }

    // 取得专场主品牌的品牌英文名，并且要简单处理，去空格和特殊符号
    function _getJmBrandEnName(self, jmBrandId) {
        var brandObj = _.find(self.vm.jmMasterBrandList, function(item) { return item.id == jmBrandId; });
        if (brandObj == null || brandObj == undefined) {
            self.vm.brandEnName = "";
            return;
        }
        var enName = brandObj.enName;
        if (enName == null || enName == undefined || enName == '') {
            self.vm.brandEnName = "";
            return;
        }
        var nameVal = '';
        for (var i = 0; i < enName.length; i ++) {
            var charVal = enName.charAt(i);
            if (('0' <= charVal && charVal <= '9') || ('a' <= charVal && charVal <= 'z') || ('A' <= charVal && charVal <= 'Z')) {
                nameVal = nameVal.concat(charVal);
            }
        }
        self.vm.brandEnName = nameVal;
    };

    cms.directive('spEdit', [function spEditDirectiveFactory() {
        return {
            restrict: 'E',
            controller: ['$routeParams', 'jmPromotionService', 'alert', 'confirm', '$translate', '$filter', SpEditDirectiveController],
            controllerAs: 'ctrlEdit',
            templateUrl: '/modules/cms/views/jmpromotion/sp.edit.directive.html'
        }
    }]);
});