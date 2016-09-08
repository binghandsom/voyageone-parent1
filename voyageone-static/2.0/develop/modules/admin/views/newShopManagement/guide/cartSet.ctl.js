/**
 * Created by sofia on 2016/9/1.
 */
define([
    'admin',
    'modules/admin/controller/popup.ctl'
], function (admin) {
    admin.controller('GuideCartSetController', (function () {
        function GuideCartSetController(selectRowsFactory) {
            this.selectRowsFactory = selectRowsFactory;
            this.cartShopSelList = {selList: []};
            this.cartTrackingSelList = {selList: []};
            this.tempShopSelect = null;
            this.tempTrackingSelect = null;
            this.context = JSON.parse(window.sessionStorage.getItem('valueBean'));

        }

        GuideCartSetController.prototype = {
            init: function () {
                var self = this;
                self.cartShopList = self.context.cartShop;

                // 设置勾选框
                if (self.tempShopSelect == null) {
                    self.tempShopSelect = new self.selectRowsFactory();
                } else {
                    self.tempShopSelect.clearCurrPageRows();
                    self.tempShopSelect.clearSelectedList();
                }
                _.forEach(self.cartShopList, function (Info) {
                    if (Info.updFlg != 8) {
                        self.tempShopSelect.currPageRows({
                            "id": Info.cartId,
                            "code": Info.name,
                            "orderChannelId": Info.orderChannelId
                        });
                    }
                });
                self.cartShopSelList = self.tempShopSelect.selectRowsInfo;
                // End 设置勾选框

                self.cartTrackingList = self.context.cartTracking;

                // 设置勾选框
                if (self.tempTrackingSelect == null) {
                    self.tempTrackingSelect = new self.selectRowsFactory();
                } else {
                    self.tempTrackingSelect.clearCurrPageRows();
                    self.tempTrackingSelect.clearSelectedList();
                }
                _.forEach(self.cartTrackingList, function (Info) {
                    if (Info.updFlg != 8) {
                        self.tempTrackingSelect.currPageRows({
                            "id": Info.seq
                        });
                    }
                });
                self.cartTrackingSelList = self.tempTrackingSelect.selectRowsInfo;
                // End 设置勾选框
            },
            next: function () {
                window.location.href = "#/newShop/guide/batchJob";
            }
        };
        return GuideCartSetController;
    })())
});