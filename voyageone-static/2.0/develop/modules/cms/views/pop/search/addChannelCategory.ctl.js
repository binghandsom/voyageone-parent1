/**
 * Created by sofia on 5/19/2016.
 */
define([
    'cms',
    './addChannelCategory.service.dev'
], function (cms) {
    cms.controller('popAddChannelCategoryCtrl', (function () {
        function PopAddChannelCategoryCtrl(context, addChannelCategoryService) {
            this.code = context.productIds;
            this.channelCategoryList = null;
            this.cartList = [];
            this.cartId = 0;
            this.addChannelCategoryService = addChannelCategoryService;
        }

        PopAddChannelCategoryCtrl.prototype = {
            /**
             * 画面初始化时
             */



            init: function () {
                var self = this;
                self.addChannelCategoryService.init().then(function (res) {
                    self.channelCategoryList = res.data;
                });
                self.cartList=['TM','TG','JD','JG']
            },
            /**
             * 点击保存按钮时
             */
            save: function () {
                var self = this;
                self.addChannelCategoryService.save().then(function () {

                })
            }
        };
        return PopAddChannelCategoryCtrl;
    })());
});




