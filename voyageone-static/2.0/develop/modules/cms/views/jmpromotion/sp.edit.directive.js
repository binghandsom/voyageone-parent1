define([
    'cms',
    'modules/cms/controller/popup.ctl'
], function (cms) {

    function SpEditDirectiveController($routeParams, jmPromotionService, spDataService, alert, confirm, $translate, $filter, $scope, notify) {
        this.$routeParams = $routeParams;
        this.jmPromotionService = jmPromotionService;
        this.spDataService = spDataService;
        this.alert = alert;
        this.confirm = confirm;
        this.notify = notify;
        this.$translate = $translate;
        this.$filter = $filter;
        this.vm = {"brandEnName": '', "mainChannelAb": '', "isFromBox": false};
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
            editModel.model.activityStart = formatToDate(editModel.model.activityStart);
            editModel.model.activityEnd = formatToDate(editModel.model.activityEnd);
            editModel.model.prePeriodStart = formatToDate(editModel.model.prePeriodStart);
            editModel.model.signupDeadline = formatToDate(editModel.model.signupDeadline);


            // 准备期是否结束
            vm.isDeadline = editModel.model.signupDeadline && editModel.model.signupDeadline < vm.currentTime;
            // 预热是否开始
            vm.isBeginPre = editModel.model.prePeriodStart && editModel.model.prePeriodStart < vm.currentTime;
            // 活动是否开始
            vm.isBegin = editModel.model.activityStart && editModel.model.activityStart < vm.currentTime;
            // 活动是否结束
            vm.isEnd = editModel.model.activityEnd && editModel.model.activityEnd < vm.currentTime;

            if (editModel.model.promotionType) {
                editModel.model.promotionType = editModel.model.promotionType.toString();
            }
            if (editModel.extModel == undefined || editModel.extModel == null || editModel.extModel == '') {
                editModel.extModel = {};
            }
            if (editModel.extModel.directmailType == undefined || editModel.extModel.directmailType == null || editModel.extModel.directmailType == '') {
                editModel.extModel.directmailType = '1';
            }
            if (editModel.extModel.mainChannel == undefined || editModel.extModel.mainChannel == null || editModel.extModel.mainChannel == '') {
                editModel.extModel.mainChannel = 'luxuryglobal';
            }

            // 转换并设置checkbox
            editModel.extModel.syncMobile = editModel.extModel.syncMobile == '1';
            editModel.extModel.showHiddenDeal = editModel.extModel.showHiddenDeal == '1';
            editModel.extModel.showSoldOutDeal = editModel.extModel.showSoldOutDeal == '1';
            editModel.extModel.showMobile = editModel.extModel.showMobile == '1';
            editModel.extModel.isPromotionFullMinus = editModel.extModel.isPromotionFullMinus == '1';
            editModel.extModel.isPromotionEachfullMinus = editModel.extModel.isPromotionEachfullMinus == '1';
            editModel.extModel.jmCoupons = editModel.extModel.jmCoupons == '1';
            editModel.extModel.voCoupons = editModel.extModel.voCoupons == '1';
            // 设置默认值
            if (editModel.model.detailStatus == 0) {
                // 表示第一次进入详情画面，未保存过
                editModel.extModel.syncMobile = true;
                editModel.extModel.showHiddenDeal = true;
                editModel.extModel.showMobile = true;
            }

            var sceneArr;

            // 转换活动场景的值
            if (editModel.model.promotionScene) {
                sceneArr = JSON.parse(editModel.model.promotionScene);
                editModel.model.promotionScene = [];
                angular.forEach(sceneArr, function (element) {
                    editModel.model.promotionScene[element] = true;
                });
            } else {
                editModel.model.promotionScene = [];
            }
            // 转换展示平台的值
            if (editModel.extModel.displayPlatform) {
                sceneArr = JSON.parse(editModel.extModel.displayPlatform);
                editModel.extModel.displayPlatform = [];
                angular.forEach(sceneArr, function (element) {
                    editModel.extModel.displayPlatform[element] = true;
                });
            } else {
                editModel.extModel.displayPlatform = [];
            }
            // 转换预展示频道的值
            if (editModel.extModel.preDisplayChannel) {
                sceneArr = JSON.parse(editModel.extModel.preDisplayChannel);
                editModel.extModel.preDisplayChannel = [];
                angular.forEach(sceneArr, function (element) {
                    editModel.extModel.preDisplayChannel[element] = true;
                });
            } else {
                editModel.extModel.preDisplayChannel = [];
            }

            jmPromotionService.init({hasExt: true}).then(function (res) {
                vm.metaData = res.data;
                // 记住主品牌初始值
                _getJmBrandEnName(self, editModel.model.masterBrandName);
                // 记住主频道英文缩写
                _getJmMainChannelAb(self, editModel.extModel.mainChannel);
            });

            self.spDataService.editModel = editModel;
        });
    };

    SpEditDirectiveController.prototype.addTag = function (tag) {
        var self = this,
            tagList = self.editModel.tagList,
            newTag;

        if (!tagList)
            self.editModel.tagList = tagList = [];

        newTag = {
            model: {id: "", channelId: "", tagName: "", active: 1},
            featured: false
        };

        if (tag)
            newTag = angular.merge(newTag, tag);

        tagList.push(newTag);

        return newTag;
    };

    SpEditDirectiveController.prototype.delTag = function (tag, index) {
        var self = this,
            confirm = self.confirm,
            editModel = self.editModel;

        confirm('TXT_MSG_DELETE_ITEM').then(function () {
            tag.model.active = 0;
            // 没有数据，说明后台不存在，就物理删除
            if (!tag.model.id)
                editModel.tagList.splice(index, 1);
        });
    };

    // 检查checkbox是否有输入
    SpEditDirectiveController.prototype.checkboxVal = function (inputArr) {
        var inputObj = _.find(inputArr, function (item) {
            return item == true;
        });
        return !inputObj;
    };

    // 只用于监控活动结束时间发生变化
    SpEditDirectiveController.prototype.onDateChange = function () {
        var self = this;
        self.vm.isFromBox = true;
        self.vm.datePicker2 = !self.vm.datePicker2;
    };

    // 当专场主品牌改变时，记住所选值，用于创建页面标识(主要是品牌英文名，并且要简单处理，去空格和特殊符号)
    SpEditDirectiveController.prototype.onJmBrandChange = function () {
        var self = this;
        _getJmBrandEnName(self, self.editModel.model.masterBrandName);
        self.createActId();

        self.editModel.model.brand = '';
        self.editModel.model.cmsBtJmMasterBrandId = '';

        if (self.editModel.model.masterBrandName) {
            var inputObj = _.find(self.vm.metaData.jmMasterBrandList, function (item) {
                return item.value == self.editModel.model.masterBrandName;
            });
            if (inputObj) {
                self.editModel.model.brand = inputObj.value;
                self.editModel.model.cmsBtJmMasterBrandId = inputObj.name;
            }
        }
    };

    // 当主频道改变时，记住所选值（缩写），用于创建页面标识
    SpEditDirectiveController.prototype.onMainChannelChange = function () {
        var self = this;
        _getJmMainChannelAb(self, self.editModel.extModel.mainChannel);
        self.createActId();
    };

    // 创建页面标识
    SpEditDirectiveController.prototype.createActId = function (fieldName) {
        var self = this;
        var idDate = self.$filter("date")(new Date(self.editModel.model.activityStart),"yyyyMMdd");

        var jmBrandId = self.editModel.model.cmsBtJmMasterBrandId;

        var mainChannel = self.editModel.extModel.mainChannel;
        if (mainChannel == undefined || mainChannel == null) {
            mainChannel = '';
        }

        var tempPromotionProductType = '';
        if (self.editModel.extModel.promotionProductType == null || self.editModel.extModel.promotionProductType == undefined || self.editModel.extModel.promotionProductType == '') {
            self.editModel.extModel.promotionProductType = '';
            tempPromotionProductType = '';
        } else {
            tempPromotionProductType = "_" + self.editModel.extModel.promotionProductType
        }

        if (self.spDataService.jmPromotionObj.detailStatus != 1) {
            if (self.editModel.model.promotionType == '3') {
                var idTime = self.$filter("date")(new Date(),"HH-mm-ss-sss").replace(/-/g, "");
                idTime = parseInt(idTime).toString(36);
                var pageId = idDate + mainChannel + tempPromotionProductType + '_' + jmBrandId + '_' + idTime;
                self.editModel.extModel.pcPageId = pageId + '_pc';
                self.editModel.extModel.appPageId = pageId + '_app';
            } else {
                //if (fieldName == 'promotionProductType') {
                //    // 其他专场时，活动主要商品品类的输入无效
                //    return;
                //}
                var idTime = self.$filter("date")(new Date(),"HH-mm-ss-sss").replace(/-/g, "");
                idTime = parseInt(idTime).toString(36);
                var pageId = self.vm.mainChannelAb + '_' + self.vm.brandEnName + tempPromotionProductType + '_' + idDate + '_' + idTime;
                self.editModel.extModel.pcPageId = pageId + '_pc';
                self.editModel.extModel.appPageId = pageId + '_app';
            }
        }
    };

    // 保存修改  saveType：0->暂存  1->提交
    // 只有在'提交'时才检查输入项目
    SpEditDirectiveController.prototype.save = function (saveType) {
        var self = this,
            alert = self.alert,
            notify = self.notify,
            jmPromotionService = self.jmPromotionService,
            spDataService = self.spDataService,
            editModel = self.editModel;

        var model = editModel.model;
        var extModel = editModel.extModel;
        if (saveType) {
            // 在'提交'时检查输入项目
            if (model.cmsBtJmMasterBrandId == undefined || model.cmsBtJmMasterBrandId == null || model.cmsBtJmMasterBrandId == '') {
                alert("聚美品牌未匹配，请重新选择。");
                return;
            }

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
                return element.model.tagName;
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
        param.tagList = _.filter(editModel.tagList, function (tag) {
            return tag.model.tagName != "";
        });

        param.model = angular.copy(model);
        param.extModel = angular.copy(extModel);

        param.model.promotionScene = JSON.stringify(_returnKey(param.model.promotionScene));
        param.extModel.displayPlatform = JSON.stringify(_returnKey(param.extModel.displayPlatform));
        param.extModel.preDisplayChannel = JSON.stringify(_returnKey(param.extModel.preDisplayChannel));

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

        jmPromotionService.saveModel(param).then(function () {
            notify.success('TXT_SAVE_SUCCESS');
            if (saveType == 1) {
                spDataService.jmPromotionObj.detailStatus = 1;
                model.isFstSave = 1;
            } else {
                spDataService.jmPromotionObj.detailStatus = 2;
            }
            // 保存之后如果标签被修改过就需要重新刷新标签缓存
            spDataService.getPromotionModules(true).then(function (tagModels) {
                // 刷新标签编辑部分
                editModel.tagList = tagModels.map(function (tagModelItem) {
                    return {
                        model: tagModelItem.tag,
                        featured: tagModelItem.module.featured
                    };
                });
                // 之后触发事件，促使模块刷新
                self.$fire('detail.saved');
            });
        });
    };

    /**
     * 判断标签是否重复
     * @param model
     */
    SpEditDirectiveController.prototype.validTagName = function(model){
        var self = this,
            tagList = self.editModel.tagList,
            mark,
            alert = self.alert;

        if(!tagList || tagList.length === 0)
            return;

        mark = _.some(tagList,function(ele){
            return ele.model != model && ele.model.tagName === model.tagName
        });

        if(mark){
            alert('标签不可以重复！');
            model.tagName = '';
        }


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
            return filter("date")(new Date(date), "yyyy-MM-dd HH:mm:ss");
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
            .map(function (value, key) {
                return value ? key : null;
            })
            .filter(function (value) {
                return value;
            })
            .value();
    }

    // 取得专场主品牌的品牌英文名，并且要简单处理，去空格和特殊符号
    function _getJmBrandEnName(self, jmBrandName) {
        if (jmBrandName == null || jmBrandName == undefined || jmBrandName == '') {
            self.vm.brandEnName = "";
            return;
        }
        var nameVal = '';
        for (var i = 0; i < jmBrandName.length; i++) {
            var charVal = jmBrandName.charAt(i);
            if (('0' <= charVal && charVal <= '9') || ('a' <= charVal && charVal <= 'z') || ('A' <= charVal && charVal <= 'Z')) {
                nameVal = nameVal.concat(charVal);
            }
        }
        self.vm.brandEnName = nameVal;
    }

    // 取得主频道英文缩写
    function _getJmMainChannelAb(self, mainChannelName) {
        self.vm.mainChannelAb = '';
        if (mainChannelName == undefined || mainChannelName == null) {
            return;
        }
        var channelObj = _.find(self.vm.metaData.mainChannelList, function (item) {
            return item.value == mainChannelName;
        });
        if (channelObj) {
            self.vm.mainChannelAb = channelObj.abbr;
        }
    }

    cms.directive('spEdit', function spEditDirectiveFactory() {
        return {
            restrict: 'E',
            scope: {},
            controller: ['$routeParams', 'jmPromotionService', 'spDataService', 'alert', 'confirm', '$translate', '$filter', '$scope', 'notify', SpEditDirectiveController],
            controllerAs: 'ctrlEdit',
            templateUrl: '/modules/cms/views/jmpromotion/sp.edit.directive.html'
        }
    });
});