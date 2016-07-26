/**
 * Created by sofia on 7/21/2016.
 */
define([
    'cms'
], function (cms) {
    cms.controller('PlatformBrandSettingController', (function () {
        function PlatformBrandSettingController(context, notify, popups, brandMappingService, $uibModalInstance) {
            this.platformData = context;
            this.notify = notify;
            this.popups = popups;
            this.brandMappingService = brandMappingService;
            this.$uibModalInstance = $uibModalInstance;
            this.platformList = [];
            this.selectedPlatformlist = [];
        }

        PlatformBrandSettingController.prototype = {
            init: function () {
                var self = this;
                self.refresh();
            },
            selectedPlatformBrand: function (item) {
                var self = this;
                self.selectedPlatform = item.brandName;
                self.selectedBrandId = item.brandId;
            },
            refresh: function () {
                var self = this;
                self.brandMappingService.searchCustBrands({'cartId': self.platformData.cartId}).then(function (res) {
                    self.custBrandList = res.data.custBrandList;
                });
            },
            submitSet: function () {
                var self = this;
                self.selectedPlatformlist = {
                    'masterName': self.platformData.masterName,
                    'selectedPlatform': self.selectedPlatform,
                    'cartId': self.platformData.cartId,
                    'brandId': self.selectedBrandId
                };
                if (!self.selectedPlatformlist.selectedPlatform) {
                    self.notify.warning('TXT_COMPLETE_THE_PLATEFORM_BRAND');
                    return;
                }
                self.popups.openPlatformMappingConfirm(self.selectedPlatformlist).then(function (res) {
                    if (res == true) {
                        self.brandMappingService.addNewBrandMapping({
                            'cartId': self.platformData.cartId,
                            'brandId': self.selectedBrandId
                        });
                        self.$uibModalInstance.close();
                    }
                });
            }
        };
        return PlatformBrandSettingController;
    })())
});