/**
 * Created by sofia on 7/21/2016.
 */
define([
    'cms'
], function (cms) {
    cms.controller('PlatformBrandSettingController', (function () {
        function PlatformBrandSettingController(context, notify, popups, brandMappingService, $uibModalInstance, confirm, alert) {
            this.platformData = context;
            this.notify = notify;
            this.popups = popups;
            this.brandMappingService = brandMappingService;
            this.$uibModalInstance = $uibModalInstance;
            this.platformList = [];
            this.selectedPlatformlist = [];
            this.confirm = confirm;
            this.alert = alert;
        }

        PlatformBrandSettingController.prototype = {
            init: function () {
                var self = this;
                self.brandMappingService.searchCustBrands({'cartId': self.platformData.cartId}).then(function (res) {
                    self.custBrandList = res.data.custBrandList;
                });
            },
            selectedPlatformBrand: function (item) {
                var self = this;
                self.selectedPlatform = item.brandName;
                self.selectedBrandId = item.brandId;
            },
            refresh: function () {
                var self = this;
                self.brandMappingService.getSynchronizedTime({'cartId': self.platformData.cartId}).then(function (res) {
                    self.synchTime = res.data.synchTime;
                    self.confirm('最近一次“平台品牌获取”启动时间：' + self.synchTime).then(function () {
                        self.brandMappingService.synchronizePlatformBrands({'cartId': self.platformData.cartId}).then(function (res) {
                            if (res.data.success == false) self.alert(res.data.message);
                            self.init();
                        })
                    });
                });
            },
            submitSet: function () {
                var self = this;
                self.selectedPlatformlist = {
                    'cmsBrand': self.platformData.masterName,
                    'selectedPlatform': self.selectedPlatform,
                    'cartId': self.platformData.cartId,
                    'brandId': self.selectedBrandId,
                    'cartName': self.platformData.cartName
                };
                if (!self.selectedPlatformlist.selectedPlatform) {
                    self.notify.warning('TXT_COMPLETE_THE_PLATEFORM_BRAND');
                    return;
                }
                self.popups.openPlatformMappingConfirm(self.selectedPlatformlist).then(function (res) {

                    if (res == true) {
                        self.brandMappingService.addNewBrandMapping({
                            'cmsBrand': self.platformData.masterName,
                            'cartId': self.platformData.cartId,
                            'brandId': self.selectedBrandId
                        });

                        self.$uibModalInstance.close(self.selectedPlatformlist);

                    }
                });
            }
        };
        return PlatformBrandSettingController;
    })())
});