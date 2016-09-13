/**
 * Created by sofia on 2016/9/1.
 */
define([
    'admin',
    'modules/admin/controller/popup.ctl'
], function (admin) {
    admin.controller('GuideCartSetController', (function () {
        function GuideCartSetController(selectRowsFactory, popups, confirm) {
            this.selectRowsFactory = selectRowsFactory;
            this.popups = popups;
            this.confirm = confirm;
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
            config: function (type) {
                var self = this;
                if (self.cartShopSelList.selList.length < 1) {
                    self.popups.openConfig({
                        'configType': type,
                        'isReadOnly': true,
                        'sourceData': self.context.cartShop,
                        'channelInfo': self.context.channel,
                        'orderChannelId': self.context.channel.orderChannelId
                    });
                    return;
                } else {
                    _.forEach(self.cartShopList, function (Info) {
                        if (Info.cartId == self.cartShopSelList.selList[0].id) {
                            _.extend(Info, {'configType': type});
                            self.popups.openConfig(Info);
                        }
                    })
                }
            },
            edit: function (item) {
                var self = this;
                switch (item.type) {
                    case 'cartShop':
                        if (item.kind == 'add') {
                            self.popups.openCartChannelShop({
                                'kind': 'add',
                                'isReadOnly': true,
                                'orderChannelId': self.context.channel.orderChannelId,
                                'channelName': self.context.channel.channelName,
                                'sourceData': self.context.channel
                            }).then(function (res) {
                                var list = self.cartShopList;
                                list.push(res);
                                self.init(1);
                            });
                        } else {
                            _.forEach(self.cartShopList, function (Info) {
                                if (Info.cartId == self.cartShopSelList.selList[0].id) {
                                    _.extend(Info, {'isReadOnly': true,'sourceData': self.context.channel});
                                    self.popups.openCartChannelShop(Info).then(function () {
                                        self.init(1);
                                    });
                                }
                            })
                        }
                        break;
                    case 'cartTracking':
                        if (item.kind == 'add') {
                            self.popups.openCartTrackingInfo({
                                'kind': 'add',
                                'isReadOnly': true,
                                'orderChannelId': self.context.channel.orderChannelId,
                                'channelName': self.context.channel.channelName,
                                'sourceData': self.context.channel
                            }).then(function (res) {
                                var list = self.cartTrackingList;
                                list.push(res);
                                self.init(1);
                            });
                        } else {
                            _.forEach(self.cartTrackingList, function (Info) {
                                if (Info.seq == self.cartTrackingSelList.selList[0].id) {
                                    _.extend(Info, {'isReadOnly': true,'sourceData': self.context.channel});
                                    self.popups.openCartTrackingInfo(Info).then(function () {
                                        self.init(1);
                                    });
                                }
                            })
                        }
                        break;
                }

            },
            delete: function (item) {
                var self = this;
                self.confirm('TXT_CONFIRM_INACTIVE_MSG').then(function () {
                        var delList = [];
                        switch (item.type) {
                            case 'cartShop':
                                _.forEach(self.cartShopSelList.selList, function (delInfo) {
                                    delList.push({'cartId': delInfo.id, 'orderChannelId': delInfo.orderChannelId});
                                });
                                _.forEach(delList, function (item) {
                                        var source = self.cartShopList;
                                        var data = _.find(source, function (sItem) {
                                            return sItem.cartId == item.cartId;
                                        });
                                        if (source.indexOf(data) > -1) {
                                            source.splice(source.indexOf(data), 1);
                                            self.init();
                                        }
                                    }
                                );
                                break;
                            case 'cartTracking':
                                _.forEach(self.cartTrackingSelList.selList, function (delInfo) {
                                    delList.push({'seq': delInfo.id, 'cartId': delInfo.cartId});
                                });
                                _.forEach(delList, function (item) {
                                        var source = self.cartTrackingList;
                                        var data = _.find(source, function (sItem) {
                                            return sItem.seq == item.seq;
                                        });
                                        if (source.indexOf(data) > -1) {
                                            source.splice(source.indexOf(data), 1);
                                            self.init();
                                        }
                                    }
                                );
                                break;
                        }
                    }
                );
            },
            next: function () {
                window.sessionStorage.setItem('valueBean', JSON.stringify(this.context));
                window.location.href = "#/newShop/guide/batchJob";
            }
        };
        return GuideCartSetController;
    })())
});