define([
    'cms',
    'modules/cms/controller/popup.ctl'
], function (cms) {

    function SpEditDirectiveController($routeParams, jmPromotionService, spDataService, alert, confirm, $translate, $filter, $scope) {
        this.$routeParams = $routeParams;
        this.jmPromotionService = jmPromotionService;
        this.spDataService = spDataService;
        this.alert = alert;
        this.confirm = confirm;
        this.$translate = $translate;
        this.$filter = $filter;
        this.vm = { "brandEnName": '', "mainChannelAb": '', "isFromBox": false };
        this.editModel = {model: {}};
        this.datePicker = [];

        this.$fire = function (eventName, context) {
            $scope.$parent.$broadcast(eventName, context);
        };

        $scope.$watch('ctrlEdit.editModel.model.activityEnd', function (newValue, oldValue, scope) {
            if (newValue && scope.ctrlEdit.vm.isFromBox) {
                scope.ctrlEdit.vm.isFromBox = false;
                scope.ctrlEdit.editModel.model.activityEnd = new Date(newValue.toString().substr(0, 11) + "23:59:59");
            }
        });
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
            editModel.tagListOrg = angular.copy(res.data.tagList);
            editModel.model.activityStart = formatToDate(editModel.model.activityStart);
            editModel.model.activityEnd = formatToDate(editModel.model.activityEnd);
            editModel.model.prePeriodStart = formatToDate(editModel.model.prePeriodStart);
            editModel.model.signupDeadline = formatToDate(editModel.model.signupDeadline);
            // 准备期是否结束
            vm.isDeadline = false;
            if (editModel.model.signupDeadline && editModel.model.signupDeadline < vm.currentTime) {
                vm.isDeadline = true;
            }
            // 预热是否开始
            vm.isBeginPre = false;
            if (editModel.model.prePeriodStart && editModel.model.prePeriodStart < vm.currentTime) {
                vm.isBeginPre = true;
            }
            // 活动是否开始
            vm.isBegin = false;
            if (editModel.model.activityStart && editModel.model.activityStart < vm.currentTime) {
                vm.isBegin = true;
            }
            // 活动是否结束
            vm.isEnd = false;
            if (editModel.model.activityEnd && editModel.model.activityEnd < vm.currentTime) {
                vm.isEnd = true;
            }

            if (editModel.model.promotionType) {
                editModel.model.promotionType = editModel.model.promotionType.toString();
            }
            if (editModel.extModel == undefined || editModel.extModel == null || editModel.extModel == '') {
                editModel.extModel = {};
            }
            if (editModel.extModel.directmailType == undefined || editModel.extModel.directmailType == null || editModel.extModel.directmailType == '') {
                editModel.extModel.directmailType = '1';
            }

            // 转换并设置checkbox
            editModel.extModel.syncMobile = editModel.extModel.syncMobile == '1' ? true : false;
            editModel.extModel.showHiddenDeal = editModel.extModel.showHiddenDeal == '1' ? true : false;
            editModel.extModel.showSoldOutDeal = editModel.extModel.showSoldOutDeal == '1' ? true : false;
            editModel.extModel.showMobile = editModel.extModel.showMobile == '1' ? true : false;
            editModel.extModel.isPromotionFullMinus = editModel.extModel.isPromotionFullMinus == '1' ? true : false;
            editModel.extModel.isPromotionEachfullMinus = editModel.extModel.isPromotionEachfullMinus == '1' ? true : false;
            editModel.extModel.jmCoupons = editModel.extModel.jmCoupons == '1' ? true : false;
            editModel.extModel.voCoupons = editModel.extModel.voCoupons == '1' ? true : false;

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
            if (editModel.extModel.displayPlatform) {
                var sceneArr = JSON.parse(editModel.extModel.displayPlatform);
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

            jmPromotionService.init({hasExt:true}).then(function (res) {
                vm.metaData = res.data;
                // 记住主品牌初始值
                _getJmBrandEnName(self, editModel.model.cmsBtJmMasterBrandId);
            });
            // 记住主频道英文缩写
            _getJmMainChannelAb(self, editModel.extModel.mainChannel);
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

    SpEditDirectiveController.prototype.delTag = function (tag, index) {
        var self = this,
            confirm = self.confirm,
            translate = self.$translate,
            editModel = self.editModel;

        confirm(translate.instant('TXT_MSG_DELETE_ITEM'))
            .then(function () {
                tag.active = 0;
                editModel.tagList.splice(index, 1);
                if (tag.id) {
                    // 找到原始保存的taglist,将其'active'设为0
                    var tagObj = _.find(editModel.tagListOrg, function(item) { return item.id == tag.id; });
                    tagObj.active = 0;
                }
            });
    };

    // 检查checkbox是否有输入
    SpEditDirectiveController.prototype.checkboxVal = function (inputArr) {
        var inputObj = _.find(inputArr, function(item) { return item == true; });
        if (inputObj) {
            return false;
        } else {
            return true;
        }
    };

    // 只用于监控活动结束时间发生变化
    SpEditDirectiveController.prototype.onDateChange = function () {
        var self = this;
        self.vm.isFromBox = true;

        if (self.vm.datePicker2) {
            self.vm.datePicker2 = false;
        } else {
            self.vm.datePicker2 = true;
        }
    };

    // 当专场主品牌改变时，记住所选值，用于创建页面标识(主要是品牌英文名，并且要简单处理，去空格和特殊符号)
    SpEditDirectiveController.prototype.onJmBrandChange = function () {
        var self = this;
        _getJmBrandEnName(self, self.editModel.model.cmsBtJmMasterBrandId);
        self.createActId();
    };

    // 当主频道改变时，记住所选值（缩写），用于创建页面标识
    SpEditDirectiveController.prototype.onMainChannelChange = function () {
        var self = this;
        _getJmMainChannelAb(self, self.editModel.extModel.mainChannel);
        self.createActId();
    };

    // 创建页面标识
    SpEditDirectiveController.prototype.createActId = function () {
        var self = this;
        var idDate = self.$filter("date")(new Date(),"yyyyMMdd");
        var idTime = self.$filter("date")(new Date(),"HH-mm-ss-sss").replace(/-/g, "");
        idTime = parseInt(idTime).toString(36);
        var jmBrandId = self.editModel.model.cmsBtJmMasterBrandId;

        var mainChannel = self.editModel.extModel.mainChannel;
        if (mainChannel == undefined || mainChannel == null) {
            mainChannel = '';
        }

        if (self.editModel.model.promotionType == '3') {
            if (self.editModel.extModel.promotionProductType == null || self.editModel.extModel.promotionProductType == undefined) {
                self.editModel.extModel.promotionProductType = '';
            }
            var pageId = idDate + mainChannel + '_' + self.editModel.extModel.promotionProductType + '_' + jmBrandId + '_' + idTime;
            self.editModel.extModel.pcPageId = pageId + '_pc';
            self.editModel.extModel.appPageId = pageId + '_app';
        } else {
            var pageId = self.vm.mainChannelAb + '_' + self.vm.brandEnName + '_' + idDate + '_' + idTime;
            self.editModel.extModel.pcPageId = pageId + '_pc';
            self.editModel.extModel.appPageId = pageId + '_app';
        }
    };

    // 保存修改  saveType：0->暂存  1->提交
    // 只有在'提交'时才检查输入项目
    SpEditDirectiveController.prototype.save = function(saveType) {
        var self = this,
            alert = self.alert,
            jmPromotionService = self.jmPromotionService,
            spDataService = self.spDataService,
            editModel = self.editModel;

        var model = editModel.model;
        var extModel = editModel.extModel;
        if (saveType) {
            // 在'提交'时检查输入项目
            var start = new Date(model.activityStart);
            var end = new Date(model.activityEnd);

            if (model.activityStart > model.activityEnd) {
                alert("活动时间检查：请输入结束时间>开始时间，最小间隔为30分钟。");
                return;
            }

            if (end.getTime() - start.getTime() < 30 * 60 * 1000) {
                alert("活动时间检查：最小间隔为30分钟。");
                return;
            }

            if (model.prePeriodStart > model.prePeriodEnd) {
                alert("预热时间检查：请输入结束时间>开始时间。");
                return;
            }

            if (model.prePeriodStart > model.activityStart) {
                alert("预热开始时间不能晚于活动开始时间");
                return;
            }

            if (model.signupDeadline > model.prePeriodStart) {
                alert("报名截止日期不能晚于预热开始时间");
                return;
            }

            if (editModel.tagList.length === 0) {
                alert("请至少添加一个标签");
                return;
            }
            var hasTag = _.every(editModel.tagList, function (element) {
                return element.tagName;
            });
            if (!hasTag)
                return;

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
        param.tagList = _.filter( editModel.tagList, function(tag){ return tag.tagName != "";});
        // 活动标签设置，找出已被删除的tag，添加到现有taglist的最后
        _.each(editModel.tagListOrg, function(element) {
            if (element.active == 0) {
                param.tagList.push(element);
            }
        });

        param.model = angular.copy(model);
        param.extModel = angular.copy(extModel);

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

        // 转换并设置checkbox
        param.extModel.syncMobile = param.extModel.syncMobile ? 1 : 0;
        param.extModel.showHiddenDeal = param.extModel.showHiddenDeal ? 1 : 0;
        param.extModel.showSoldOutDeal = param.extModel.showSoldOutDeal ? 1 : 0;
        param.extModel.showMobile = param.extModel.showMobile ? 1 : 0;
        param.extModel.isPromotionFullMinus = param.extModel.isPromotionFullMinus ? 1 : 0;
        param.extModel.isPromotionEachfullMinus = param.extModel.isPromotionEachfullMinus ? 1 : 0;
        param.extModel.jmCoupons = param.extModel.jmCoupons ? 1 : 0;
        param.extModel.voCoupons = param.extModel.voCoupons ? 1 : 0;

        param.model.activityStart = formatToStr(param.model.activityStart, self.$filter);
        param.model.activityEnd = formatToStr(param.model.activityEnd, self.$filter);
        param.model.prePeriodStart = formatToStr(param.model.prePeriodStart, self.$filter);
        param.model.prePeriodEnd = param.model.activityEnd;
        param.model.signupDeadline = formatToStr(param.model.signupDeadline, self.$filter);

        param.extModel.shareTitle = param.extModel.marketingTitle;
        if (param.extModel.marketingCopywriter == undefined || param.extModel.marketingCopywriter == null) {
            param.extModel.marketingCopywriter = '';
        }
        if (param.extModel.promotionalCopy == undefined || param.extModel.promotionalCopy == null) {
            param.extModel.promotionalCopy = '';
        }
        param.extModel.shareContent = param.extModel.marketingCopywriter + '' + param.extModel.promotionalCopy;

        param.hasExt = true;
        param.saveType = saveType;
        param.extModel.promotionId = self.$routeParams.promId;
        param.extModel.jmpromotionId = self.$routeParams.jmpromId;
        spDataService.jmPromotionObj.detailStatus = 2;

        jmPromotionService.saveModel(param).then(function() {
            if (saveType == 1) {
                spDataService.jmPromotionObj.detailStatus = 1;
            }
            // 保存之后如果标签被修改过就需要重新刷新标签缓存
            spDataService.getPromotionModules(true).then(function (tagModels) {
                // 刷新标签编辑部分
                editModel.tagList = tagModels.map(function (tagModelItem) {
                    return tagModelItem.tag;
                });
                // 之后触发事件，促使模块刷新
                self.$fire('detail.saved');
            });
        });
    };

    /**
     * @param date 字符串格式为yyyy-MM-dd ss:ss:ss
     * @returns {Date}
     */
    function formatToDate(date) {
        //$filter("date")(new Date(date),"yyyy-MM-dd HH:mm:ss");
        if (date) {
            return new Date(date);
        } else {
            return null;
        }
    }

    function formatToStr(date, filter) {
        if (date) {
            return filter("date")(new Date(date),"yyyy-MM-dd HH:mm:ss");
        } else {
            return null;
        }
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
        var brandObj = _.find(self.vm.metaData.jmMasterBrandList, function(item) { return item.id == jmBrandId; });
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

    // 取得主频道英文缩写
    function _getJmMainChannelAb(self, mainChannelName) {
        if (mainChannelName == undefined || mainChannelName == null) {
            self.vm.mainChannelAb = '';
            return;
        }
        // TODO 这里数据先固定，将来要改从数据库取
        if ('luxuryglobal' == mainChannelName) {
            self.vm.mainChannelAb = 'ppt';
        } else if ('food' == mainChannelName) {
            self.vm.mainChannelAb = 'vtm';
        }
    };

    cms.directive('spEdit', function spEditDirectiveFactory() {
        return {
            restrict: 'E',
            scope: {},
            controller: ['$routeParams', 'jmPromotionService', 'spDataService', 'alert', 'confirm', '$translate', '$filter', '$scope', SpEditDirectiveController],
            controllerAs: 'ctrlEdit',
            templateUrl: '/modules/cms/views/jmpromotion/sp.edit.directive.html'
        }
    });
});