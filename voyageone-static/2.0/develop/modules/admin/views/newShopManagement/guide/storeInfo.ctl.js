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
            this.context = JSON.parse(window.sessionStorage.getItem('valueBean'));
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
                    self.popups.openStoreAdd({
                            'kind': 'add', 'isReadOnly': true,
                            'orderChannelId': self.storeList[0].orderChannelId,
                            'channelName': self.storeList[0].channelName
                        })
                        .then(function (res) {
                            var list = self.storeList;
                            list.push(res);
                            self.init();
                        });
                } else {
                    _.forEach(self.storeList, function (Info) {
                        if (Info.storeId == self.storeSelList.selList[0].id) {
                            Info['areaId'] = Info['areaId'] + '';
                            var copyData = Info.inventoryHold.split(",");
                            Info.inventoryHold = copyData[0];
                            Info.remainNum = copyData[1];
                            Info.isReadOnly = true;
                            self.popups.openStoreAdd(Info).then(function () {
                                self.init();
                            });
                        }
                    })
                }

            },
            delete: function () {
                var self = this;
                var delList = [];
                _.forEach(self.storeSelList.selList, function (delInfo) {
                    delList.push({'orderChannelId': self.storeList[0].orderChannelId, 'storeId': delInfo.id});
                });
                _.forEach(delList, function (item) {
                        var source = self.storeList;
                        var data = _.find(source, function (sItem) {
                            return sItem.storeId == item.storeId;
                        });
                        if (source.indexOf(data) > -1) {
                            source.splice(source.indexOf(data), 1);
                            self.init();
                        }
                    }
                );
            },
            next: function () {
                window.location.href = "#/newShop/guide/cartSet";
            }
        };
        return GuideChannelInfoController;
    })())
});