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
                data.tagId = self.tagInfo.tagPath;
                data.tagName = self.tagInfo.tagName;
                data.priceType = self.priceType;
                data.discount = self.hasDiscount ? self.discount / 10 : null;
                self.jmPromotionProductAddService.add(data)
                    .then(function(data) {
                        if(data.data.errlist.length == 0) {
                            self.notify.success(self.$translate.instant('TXT_MSG_UPDATE_SUCCESS'));
                            self.$uibModalInstance.close();
                        }else{
                            var message = "";
                            _.each(data.data.errlist,function(item){
                                message+=item+","
                            })
                            self.alert("以下code没有在聚美上新过不能加入promotion\n"+message);
                        }
                    });
            }
        };

        return popJoinJMCtl;

    })());

});
