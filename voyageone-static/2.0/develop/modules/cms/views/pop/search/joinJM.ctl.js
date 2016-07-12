/**
 * Created by 123 on 2016/4/12.
 */
define([
    'underscore',
    'cms',
    'modules/cms/controller/popup.ctl'
], function (_,cms) {

    return cms.controller('popJoinJMCtl', (function () {

        function popJoinJMCtl($translate, jmPromotionProductAddService, notify, context, alert,$uibModalInstance) {

            this.jmPromotionProductAddService = jmPromotionProductAddService;
            this.$uibModalInstance = $uibModalInstance;
            this.notify = notify;
            this.$translate = $translate;{}
            this.promotion = context.promotion;
            this.products = context.products;
            this.selAllFlg = context.isSelAll;
            this.alert = alert;
            this.hasDiscount = true;
            this.priceType = "1";
        }

        popJoinJMCtl.prototype = {
            /**
             * 初始化时,加载必需数据
             */
            init: function () {
                var self = this;
                self.jmPromotionProductAddService.getPromotionTags(self.promotion).then(function (res) {
                    self.subPromotionList = res.data;
                });
            },
            save: function () {
                var self = this;
                var data = {};
                data.promotion = self.promotion;
                data.products = self.products;
                data.isSelAll = self.selAllFlg;
                data.tagId = self.tagInfo.tagPath;
                data.tagName = self.tagInfo.tagName;
                data.priceType = self.priceType;
                data.discount = self.hasDiscount ? self.discount / 10 : null;
                self.jmPromotionProductAddService.add(data)
                    .then(function(res) {
                        if (res.data.ecd == null || res.data.ecd == undefined) {
                            self.alert(self.$translate.instant('TXT_COMMIT_ERROR'));
                            return;
                        }
                        if (res.data.ecd == 1) {
                            // 未选择商品
                            self.alert(self.$translate.instant('未选择商品，请选择后再操作'));
                            return;
                        }
                        if (res.data.ecd == 2) {
                            // 未选择商品
                            self.alert("选择商品的不存在，请重新选择。");
                            return;
                        }
                        if (res.data.ecd == 3) {
                            // 没有更新
                            self.alert("选择的商品都没有在聚美上新过，不能加入聚美活动。");
                            return;
                        }
                        if (res.data.ecd == 4) {
                            self.alert("添加商品到聚美活动失败,请联系IT处理。");
                            return;
                        }
                        if (res.data.ecd == 0) {
                            if (res.data.errlist != null && res.data.errlist.length == 0) {
                                self.notify.success(self.$translate.instant('TXT_MSG_UPDATE_SUCCESS'));
                            } else {
                                var errMsg = '';
                                if (res.data.errlist) {
                                    if (res.data.errlist.length > 10) {
                                        errMsg = res.data.errlist.slice(0, 9).join('， ') + ' ．．．．．．';
                                    } else {
                                        errMsg = res.data.errlist.join('， ');
                                    }
                                }//
                                self.alert("上新过的商品（共" + res.data.cnt + "个）已加入活动，下列商品没有在聚美上新过不能加入活动，以下是未加入活动的商品CODE列表:<br><br>" + errMsg);
                            }
                            self.$uibModalInstance.close();
                        }
                    });
            }
        };

        return popJoinJMCtl;

    })());

});
