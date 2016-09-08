/**
 * Created by sofia on 2016/9/1.
 */
define([
    'admin',
    'modules/admin/controller/popup.ctl'
], function (admin) {
    admin.controller('GuideChannelInfoController', (function () {
        function GuideChannelInfoController(selectRowsFactory, popups) {
            this.selectRowsFactory = selectRowsFactory;
            this.popups = popups;
            this.context = JSON.parse(window.sessionStorage.getItem('storeInfo'));
            this.storeList = [];
            this.storeSelList = {selList: []};
            this.tempSelect = null;
        }

        GuideChannelInfoController.prototype = {
            init: function () {
                var self = this;
                self.storeList = self.context.store;
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
            config: function (type) {
                var self = this;
                var selectedInfo = {};
                if (self.storeSelList.selList.length > 0) {
                    _.forEach(self.storeList, function (storeInfo) {
                        if (storeInfo.storeId == self.storeSelList.selList[0].id) {
                            selectedInfo = storeInfo;
                            return;
                        }
                    });
                }
                _.extend(selectedInfo, {'configType': type, 'isReadOnly': true, 'sourceData': self.context.store});
                self.popups.openConfig(selectedInfo);
            },
            edit: function (type) {
                var self = this;
                if (type == 'add') {
                    self.popups.openStoreAdd('add').then(function () {
                        self.search(1);
                    });
                } else {
                    if (self.storeSelList.selList.length <= 0) {
                        self.alert('TXT_MSG_NO_ROWS_SELECT');
                        return;
                    } else {
                        _.forEach(self.storeList, function (Info) {
                            if (Info.storeId == self.storeSelList.selList[0].id) {
                                Info['areaId'] = Info['areaId'] + '';
                                var copyData = Info.inventoryHold.split(",");
                                Info.inventoryHold = copyData[0];
                                Info.remainNum = copyData[1];
                                self.popups.openStoreAdd(Info).then(function () {
                                    self.search(1);
                                });
                            }
                        })
                    }
                }

            },
            delete: function () {
                var self = this;
                self.confirm('TXT_CONFIRM_INACTIVE_MSG').then(function () {
                        var delList = [];
                        _.forEach(self.storeSelList.selList, function (delInfo) {
                            delList.push({'orderChannelId': delInfo.orderChannelId, 'storeId': delInfo.id});
                        });
                        self.storeService.deleteStore(delList).then(function (res) {
                            self.search();
                        })
                    }
                );
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