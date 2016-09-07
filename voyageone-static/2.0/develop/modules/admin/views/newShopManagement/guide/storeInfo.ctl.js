/**
 * Created by sofia on 2016/9/1.
 */
define([
    'admin',
    'modules/admin/controller/popup.ctl'
], function (admin) {
    admin.controller('GuideChannelInfoController', (function () {
        function GuideChannelInfoController(selectRowsFactory) {
            this.selectRowsFactory = selectRowsFactory;
            this.context = JSON.parse(window.sessionStorage.getItem('storeInfo'));
            this.storeList = [];
            this.storeSelList = {selList: []};
            this.tempSelect = null;
        }

        GuideChannelInfoController.prototype = {
            init: function () {
                var self = this;
                self.storeList = self.context.store;
                // 设置勾选框
                if (self.tempSelect == null) {
                    self.tempSelect = new self.selectRowsFactory();
                } else {
                    self.tempSelect.clearCurrPageRows();
                    self.tempSelect.clearSelectedList();
                }
                _.forEach(self.storeList, function (Info) {
                    if (Info.updFlg != 8) {
                        self.tempSelect.currPageRows({
                            "id": Info.storeId
                        });
                    }
                });
                self.storeSelList = self.tempSelect.selectRowsInfo;
                // End 设置勾选框
            },
            next: function () {
                var self = this;
                window.sessionStorage.setItem('cartInfo', JSON.stringify(self.context));
                window.location.href = "#/newShop/guide/cartSet";
            }
        };
        return GuideChannelInfoController;
    })())
});