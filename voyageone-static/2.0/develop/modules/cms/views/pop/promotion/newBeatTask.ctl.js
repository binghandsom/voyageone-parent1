define([
    'cms',
    'underscore',
    'modules/cms/enums/Carts',
], function (cms, _, carts) {
    cms.controller('popNewBeatCtl', (function () {

        function PopNewBeatCtl(context, $filter, $uibModalInstance, taskBeatService,
                               taskJiagepiluService, $location) {

            var self = this;
            //var task = null;
            if (context.task) {
                self.taskBean = angular.copy(context.task);
                self.oldTaskBean = angular.copy(context.task);
                self.isEdit = true;
            }

            self.$filter = $filter;
            self.$uibModalInstance = $uibModalInstance;
            self.taskBeatService = taskBeatService;
            self.taskJiagepiluService = taskJiagepiluService;
            self.$location = $location;
            self.platformSxImageTemplates = [];

            if (context.platformTypeList && _.size(context.platformTypeList) > 0) {
                self.platformTypeList = context.platformTypeList;
                _.forEach(self.platformTypeList, function (platform) {
                    var cartIdVal = platform.value;
                    platform.value = parseInt(cartIdVal);
                });
            } else {
                if (self.taskBean) {
                    self.platformTypeList =
                        [{value: self.taskBean.cartId, name: carts.valueOf(self.taskBean.cartId).desc}];
                }
            }
            // 将字符串日期转换为 Date 日期。
            // 因为 input type=date 后, ng-model 只接受 Date 类型
            // 否则会报错


            if (self.taskBean) {
                self.taskBean.activityStart = self.formatDate(self.taskBean.activityStart);
                self.taskBean.activityEnd = self.formatDate(self.taskBean.activityEnd);
                //self.taskBean = task;
                if (_.isString(self.taskBean.config)) {
                    self.taskBean.config = JSON.parse(self.taskBean.config);
                }
                self.selectCart();
                self.taskBean.testPrice = 9999;
                self.changeBeatTemplate();
                self.selectRevertTemplate();
            } else {
                self.taskBean = {
                    taskName: '',
                    cartId: '',
                    testPrice: 9999,
                    need_vimage: false,
                    selectTemplate: null,
                    testVteImageUrl: null,
                    // activityStart: new Date(promotion.activityStart),
                    // activityEnd: new Date(promotion.activityEnd),
                    config: {
                        need_vimage: false,
                        beat_template: null,
                        revert_template: null,
                        beat_vtemplate: null,
                        revert_vtemplate: null
                    }
                };
            }
        }

        PopNewBeatCtl.prototype = {

            formatDate: function (date) {
                return this.$filter('date')(date, 'yyyy-MM-dd HH:mm:ss');
            },

            init: function () {
                // var self = this;
                // self.taskJiagepiluService.getJiagepiluCarts().then(function (resp) {
                //     if (resp.data) {
                //         self.carts = resp.data;
                //         _.each(self.carts, function (cartObj) {
                //             var cartId = parseInt(cartObj.cart_id);
                //             _.extend(cartObj, {cartId: cartId, desc: cart.valueOf(cartId).desc})
                //         });
                //     }
                // });

            },

            ok: function () {

                var self = this;
                var task = angular.copy(self.taskBean);
                var start = task.activityStart;
                var end = task.activityEnd;

                // 确保日期格式正确
                // 如果日期控件不选择的话, 则输出的将是 Date 格式
                if (_.isDate(start)) {
                    task.activityStart = self.formatDate(start);
                }

                if (_.isDate(end)) {
                    task.activityEnd = self.formatDate(end);
                }

                if (self.isEdit) {
                    task.update = true;
                }

                self.taskBeatService.create(task).then(function (res) {
                    if (res.data) {
                        var newBean = res.data;
                        self.$uibModalInstance.close(newBean);
                    }
                });
            },

            cancel: function () {
                this.$uibModalInstance.dismiss(self.oldTaskBean);
            },

            changeBeatTemplate: function () {
                var self = this;
                if (self.taskBean.config.beat_template && self.taskBean.testPrice) {
                    var beatTemplate = angular.copy(self.taskBean.config.beat_template);
                    beatTemplate = beatTemplate.replace("{key}", "adidas-samba-lifestyle-shoesg17102-1");
                    beatTemplate = beatTemplate.replace("{price}", self.taskBean.testPrice);
                    _.extend(self.taskBean, {testImageUrl: beatTemplate});
                }

                if (self.taskBean.config.need_vimage && self.taskBean.config.beat_vtemplate && self.taskBean.testPrice) {
                    var beatTemplate = angular.copy(self.taskBean.config.beat_vtemplate);
                    beatTemplate = beatTemplate.replace("{key}", "adidas-samba-lifestyle-shoesg17102-1");
                    beatTemplate = beatTemplate.replace("{price}", self.taskBean.testPrice);
                    _.extend(self.taskBean, {testVteImageUrl: beatTemplate});
                }
            },

            selectCart: function () {
                var self = this;
                if (self.taskBean && self.taskBean.cartId) {

                    var cartObj = carts.valueOf(parseInt(self.taskBean.cartId));
                    if (cartObj && cartObj.platformId == '2') {
                        _.extend(self.taskBean, {isJdseries: true})
                    } else {
                        _.extend(self.taskBean, {isJdseries: false})
                    }

                    self.taskJiagepiluService.getPlatformSxTemplate({cartId: self.taskBean.cartId}).then(function (resp) {
                        self.platformSxImageTemplates = resp.data;
                    });
                } else {
                    self.platformSxImageTemplates = [];
                }
            },

            selectRevertTemplate: function () {
                var self = this;
                if (self.taskBean && self.taskBean.config.revert_template) {
                    self.taskBean.selectTemplate = self.taskBean.config.revert_template.replace("%s", "adidas-samba-lifestyle-shoesg17102-1");
                }

                if (self.taskBean.config.need_vimage && self.taskBean && self.taskBean.config.revert_vtemplate) {
                    self.taskBean.selectVteTemplateUrl = self.taskBean.config.revert_vtemplate.replace("%s", "adidas-samba-lifestyle-shoesg17102-1");
                }
            },
            changeNeedVimage: function () {
                var self = this;
                if (!self.taskBean.config.need_vimage) {
                    self.taskBean.config.beat_vtemplate = null;
                    self.taskBean.config.revert_vtemplate = null;
                }
            }

        };

        return PopNewBeatCtl;
    })());
});