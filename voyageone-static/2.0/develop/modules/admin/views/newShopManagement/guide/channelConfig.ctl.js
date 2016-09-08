/**
 * Created by sofia on 2016/9/1.
 */
define([
    'admin',
    'modules/admin/controller/popup.ctl'
], function (admin) {
    admin.controller('GuideChannelCogInfoController', (function () {
        function GuideChannelCogInfoController(smsConfigService, selectRowsFactory, popups) {
            this.selectRowsFactory = selectRowsFactory;
            this.popups = popups;
            this.smsConfigService = smsConfigService;
            this.context = JSON.parse(window.sessionStorage.getItem('channelCogInfo'));
        }

        GuideChannelCogInfoController.prototype = {
            init: function () {
                var self = this;
                //SMS 配置
                self.channelSmsList = self.context.sms;
                if (self.tempChannelSmsSelect == null) {
                    self.tempChannelSmsSelect = new self.selectRowsFactory();
                } else {
                    self.tempChannelSmsSelect.clearCurrPageRows();
                    self.tempChannelSmsSelect.clearSelectedList();
                }
                _.forEach(self.channelSmsList, function (Info) {
                    if (Info.updFlg != 8) {
                        self.tempChannelSmsSelect.currPageRows({
                            "id": Info.seq
                        });
                    }
                });
                self.channelSmsSelList = self.tempChannelSmsSelect.selectRowsInfo;

                //第三方配置
                self.channelThirdList = self.context.thirdParty;
                if (self.tempChannelThirdSelect == null) {
                    self.tempChannelThirdSelect = new self.selectRowsFactory();
                } else {
                    self.tempChannelThirdSelect.clearCurrPageRows();
                    self.tempChannelThirdSelect.clearSelectedList();
                }
                _.forEach(self.channelThirdList, function (Info) {
                    if (Info.updFlg != 8) {
                        self.tempChannelThirdSelect.currPageRows({
                            "id": Info.seq
                        });
                    }
                });
                self.channelThirdSelList = self.tempChannelThirdSelect.selectRowsInfo;

                //快递
                self.carrierList = self.context.carrier;
                if (self.tempChannelCarrierSelect == null) {
                    self.tempChannelCarrierSelect = new self.selectRowsFactory();
                } else {
                    self.tempChannelCarrierSelect.clearCurrPageRows();
                    self.tempChannelCarrierSelect.clearSelectedList();
                }
                _.forEach(self.carrierList, function (channelInfo, index) {
                    if (channelInfo.updFlg != 8) {
                        _.extend(channelInfo, {"mainKey": index});
                        self.tempChannelCarrierSelect.currPageRows({
                            "id": channelInfo.mainKey
                        });
                    }
                });
                self.carrierSelList = self.tempChannelCarrierSelect.selectRowsInfo;

                //类型属性信息
                self.channelTypeList = self.context.channelAttr;
                if (self.tempChannelTypeSelect == null) {
                    self.tempChannelTypeSelect = new self.selectRowsFactory();
                } else {
                    self.tempChannelTypeSelect.clearCurrPageRows();
                    self.tempChannelTypeSelect.clearSelectedList();
                }
                _.forEach(self.channelTypeList, function (channelInfo) {
                    if (channelInfo.updFlg != 8) {
                        self.tempChannelTypeSelect.currPageRows({
                            "id": channelInfo.id
                        });
                    }
                });
                self.channelTypeSelList = self.tempChannelTypeSelect.selectRowsInfo;
            },
            edit: function (item) {
                var self = this;
                switch (item.type) {
                    case 'Sms':
                        if (item.kind == 'add') {
                            self.popups.openChannelSms({
                                'kind': 'add',
                                'isReadOnly': true,
                                'orderChannelId': self.channelSmsList[0].orderChannelId,
                                'channelName': self.channelSmsList[0].channelName
                            }).then(function (res) {
                                var list = self.channelSmsList;
                                list.push(res);
                                self.init();
                            });
                        } else {
                            _.forEach(self.channelSmsList, function (Info) {
                                if (Info.seq == self.channelSmsSelList.selList[0].id) {
                                    _.extend(Info, {'isReadOnly': true});
                                    self.popups.openChannelSms(Info).then(function () {
                                        self.init();
                                    });
                                }
                            })
                        }
                        break;
                    case 'Third':
                        if (item.kind == 'add') {
                            self.popups.openChannelThird({
                                'kind': 'add',
                                'isReadOnly': true,
                                'channelId': self.channelSmsList[0].orderChannelId,
                                'channelName': self.channelSmsList[0].channelName
                            }).then(function (res) {
                                var list = self.channelThirdList;
                                list.push(res);
                                self.init();
                            });
                        } else {
                            _.forEach(self.channelThirdList, function (Info) {
                                if (Info.seq == self.channelThirdSelList.selList[0].id) {
                                    _.extend(Info, {'isReadOnly': true});
                                    self.popups.openChannelThird(Info).then(function () {
                                        self.init();
                                    });
                                }
                            })
                        }
                        break;
                    case 'Carrier':
                        if (item.kind == 'add') {
                            self.popups.openChannelCarrier({
                                'kind': 'add',
                                'isReadOnly': true,
                                'orderChannelId': self.channelSmsList[0].orderChannelId,
                                'channelName': self.channelSmsList[0].channelName
                            }).then(function (res) {
                                var list = self.carrierList;
                                list.push(res);
                                self.init();
                            });
                        } else {
                            _.forEach(self.carrierList, function (Info) {
                                if (Info.mainKey == self.carrierSelList.selList[0].id) {
                                    _.extend(Info, {'isReadOnly': true});
                                    self.popups.openChannelCarrier(Info).then(function () {
                                        self.init();
                                    });
                                }
                            })
                        }
                        break;
                    case 'TypeAttr':
                        if (item.kind == 'add') {
                            self.popups.openAddChannelType({
                                    'kind': 'add',
                                    'isReadOnly': true,
                                    'channelId': self.channelSmsList[0].orderChannelId,
                                    'channelName': self.channelSmsList[0].channelName
                                })
                                .then(function (res) {
                                    var list = self.channelTypeList;
                                    list.push(res);
                                    self.init();
                                });
                        } else {
                            _.forEach(self.channelTypeList, function (Info) {
                                if (Info.id == self.channelTypeSelList.selList[0].id) {
                                    _.extend(Info, {'isReadOnly': true});
                                    self.popups.openAddChannelType(Info).then(function () {
                                        self.init();
                                    });
                                }
                            })
                        }
                        break;
                }
            },
            delete: function (item) {
                var self = this;
                var delList = [];
                switch (item.type) {
                    case 'Sms':
                        _.forEach(self.channelSmsSelList.selList, function (delInfo) {
                            delList.push(delInfo.id);
                        });
                        _.forEach(delList, function (item) {
                                var source = self.channelSmsList;
                                var data = _.find(source, function (sItem) {
                                    return sItem.seq == item;
                                });
                                if (source.indexOf(data) > -1) {
                                    source.splice(source.indexOf(data), 1);
                                    self.init();
                                }
                            }
                        );
                        break;
                    case 'Third':
                        _.forEach(self.channelThirdSelList.selList, function (delInfo) {
                            delList.push(delInfo.id);
                        });
                        _.forEach(delList, function (item) {
                                var source = self.channelThirdList;
                                var data = _.find(source, function (sItem) {
                                    return sItem.seq == item;
                                });
                                if (source.indexOf(data) > -1) {
                                    source.splice(source.indexOf(data), 1);
                                    self.init();
                                }
                            }
                        );
                        break;
                    case 'Carrier':
                        _.forEach(self.carrierSelList.selList, function (delInfo) {
                            delList.push(delInfo.id);
                        });
                        _.forEach(delList, function (item) {
                                var source = self.carrierList;
                                var data = _.find(source, function (sItem) {
                                    return sItem.mainKey == item;
                                });
                                if (source.indexOf(data) > -1) {
                                    source.splice(source.indexOf(data), 1);
                                    self.init();
                                }
                            }
                        );
                        break;
                    case 'TypeAttr':
                        _.forEach(self.channelTypeSelList.selList, function (delInfo) {
                            delList.push(delInfo.id);
                        });
                        _.forEach(delList, function (item) {
                                var source = self.channelTypeList;
                                var data = _.find(source, function (sItem) {
                                    return sItem.id == item;
                                });
                                if (source.indexOf(data) > -1) {
                                    source.splice(source.indexOf(data), 1);
                                    self.init();
                                }
                            }
                        );
                        break;
                }

            },
            next: function () {
                var self = this;
                function synchronizeChannelSeries(data) {
                    var channel = data.channel;
                    var callback = function (item) {
                        item.orderChannelId = channel.orderChannelId;
                        item.channelId = channel.orderChannelId;
                        item.channelName = channel.name;
                    };
                    _.forEach(channel.channelConfig, callback);
                    _.forEach(data.sms, callback);
                    _.forEach(data.thirdParty, callback);
                    _.forEach(data.carrier, callback);
                    _.forEach(data.channelAttr, callback);
                    _.forEach(data.store, callback);
                    _.forEach(data.cartShop, callback);
                    _.forEach(data.cartShop.cartShopConfig, callback);
                    _.forEach(data.cartTracking, callback);
                }

                synchronizeChannelSeries(self.context);
                window.sessionStorage.setItem('storeInfo', JSON.stringify(self.context));

                window.location.href = "#/newShop/guide/storeInfo";
            }

        };
        return GuideChannelCogInfoController;
    })())
});