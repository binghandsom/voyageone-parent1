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
                self.selectedPlatform = item.name;
            },
            refresh: function () {
                var self = this;
                self.brandMappingService.searchCustBrands({'cartId': self.platformData.cartId}).then(function (res) {
                    self.custBrandList = res.data.custBrandList;
                });
                self.platformList = [
                    {id: '01', name: "Vans"}, {id: '02', name: "耐克"}, {id: '03', name: "阿迪达斯"},
                    {id: '04', name: "NewBalance"}, {id: '05', name: "Skechers"}, {id: '06', name: "Vansss"},
                    {id: '07', name: "NewBalance3"}, {id: '08', name: "Skechers22"}, {id: '09', name: "NewBalance444"},
                    {id: '010', name: "耐克a"}, {id: '011', name: "阿迪达斯2"}, {id: '012', name: "阿迪达斯43"}
                ];
            },
            submitSet: function () {
                var self = this;
                self.selectedPlatformlist = {
                    'masterName': self.platformData.masterName,
                    'selectedPlatform': self.selectedPlatform,
                    'cartId': self.platformData.cartId,
                    'brandId': self.custBrandList.brandId
                };
                if (!self.selectedPlatformlist.selectedPlatform) {
                    self.notify.warning('TXT_COMPLETE_THE_PLATEFORM_BRAND');
                    return;
                }
                self.popups.openPlatformMappingConfirm(self.selectedPlatformlist).then(function (res) {
                    if (res == true) self.$uibModalInstance.close();
                });

            }
        };
        return PlatformBrandSettingController;
    })())
});