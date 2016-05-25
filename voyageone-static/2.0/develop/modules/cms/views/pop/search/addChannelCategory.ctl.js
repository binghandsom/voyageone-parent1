/**
 * Created by sofia on 5/19/2016.
 */
define([
    'cms'
], function (cms) {
    cms.controller('popAddChannelCategoryCtrl', (function () {
        function PopAddChannelCategoryCtrl(context, $addChannelCategoryService) {
            this.code = context.productIds;
            this.channelCategoryList = null;
            this.cartList = [];
            this.isSelectCid = [];
            this.cartId = 20;
            this.addChannelCategoryService = $addChannelCategoryService;
            this.cid = null;
            this.cids = [];
        }

        PopAddChannelCategoryCtrl.prototype = {
            /**
             * 画面初始化时
             */
            init: function () {
                var self = this;
                self.addChannelCategoryService.init({"code": self.code, "cartId": self.cartId}).then(function (res) {
                    self.channelCategoryList = res.data.channelCategoryList;
                    self.cartList = res.data.cartList;
                    self.isSelectCid =  [{
                        "cId": "101"
                    }, {
                        "cid": "201"
                    }, {
                        "cid": "202"
                    }]
                });
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




