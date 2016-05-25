/**
 * Created by sofia on 5/19/2016.
 */
define([
    'cms'
], function (cms) {
    cms.controller('popAddChannelCategoryCtrl', (function () {
        function PopAddChannelCategoryCtrl(context, $addChannelCategoryService, selectRowsFactory) {
            this.code = context.productIds;
            this.channelCategoryList = null;
            this.cartList = [];
            this.isSelectCid = [];
            this.cartId = "20";
            this.addChannelCategoryService = $addChannelCategoryService;
            this.catPath = null;
            this.fullCatCId = null;
            this.categorySelList = { selList: []};
            this.tempCategorySelect = new selectRowsFactory();
        }

        PopAddChannelCategoryCtrl.prototype = {
            /**
             * 画面初始化时
             */
            init: function () {
                var self = this;
                self.addChannelCategoryService.init({"code": self.code, "cartId": self.cartId}).then(function (res) {
                    self.cartList = res.data.cartList;
                    self.channelCategoryList = res.data.channelCategoryList;
                    self.isSelectCid = res.data.isSelectCid;
                });
            },

            /**
             * 点击保存按钮时
             */
            save: function () {
                var self = this;
                // 重新初始化选中标签
                self.tempCategorySelect = new selectRowsFactory();
                self.addChannelCategoryService.save({"cartId": self.cartId, "cIds": self.cIds, "cNames": self.cNames,"fullCNames": self.catPath, "fullCIds": self.fullCIds}).then(function () {
                    return self.cartId;
                });
                self.categorySelList = tempCategorySelect.selectRowsInfo;
                if(self.categorySelList.selList.length>0){
                   return function () {
                            var parameter = [];
                            _.forEach(self.categorySelList.selList, function (object) {
                                parameter.push(object.data);
                            });
                        };
                }
            }
        };
        return PopAddChannelCategoryCtrl;
    })());
});




