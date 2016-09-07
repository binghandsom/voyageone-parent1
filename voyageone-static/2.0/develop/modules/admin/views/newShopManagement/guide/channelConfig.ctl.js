/**
 * Created by sofia on 2016/9/1.
 */
define([
    'admin',
    'modules/admin/controller/popup.ctl'
], function (admin) {
    admin.controller('GuideChannelCogInfoController', (function () {
        function GuideChannelCogInfoController(smsConfigService, selectRowsFactory) {
            this.selectRowsFactory = selectRowsFactory;
            this.smsConfigService = smsConfigService;
            this.context = JSON.parse(window.sessionStorage.getItem('channelCogInfo'));
        }

        GuideChannelCogInfoController.prototype = {
            init: function () {
                var self = this;
                //SMS 配置
                self.channelSmsList = self.context.sms;
                // 设置勾选框
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
                // 设置勾选框
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
                // 设置勾选框
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

                // 设置勾选框
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
            next: function () {
                var self = this;
                window.sessionStorage.setItem('storeInfo', JSON.stringify(self.context));
                window.location.href = "#/newShop/guide/storeInfo";
            }

        };
        return GuideChannelCogInfoController;
    })())
});