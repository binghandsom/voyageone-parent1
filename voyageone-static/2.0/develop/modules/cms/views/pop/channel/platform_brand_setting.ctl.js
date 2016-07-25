/**
 * Created by sofia on 7/21/2016.
 */
define([
    'cms'
], function (cms) {
    cms.controller('PlatformBrandSettingController', (function () {
        function PlatformBrandSettingController(context, notify, popups) {
            this.platformData = context;
            this.notify = notify;
            this.popups = popups;
            this.platformPageOption = {curr: 1, total: 0, size: 10, fetch: this.search};
            this.platformList = [];
            this.selectedPlatformlist = [];
        }

        PlatformBrandSettingController.prototype = {
            init: function () {
                var self = this;
                self.brand = self.platformData;
                self.platformList = [
                    {id: '01', name: "Vans"}, {id: '02', name: "耐克"}, {id: '03', name: "阿迪达斯"},
                    {id: '04', name: "NewBalance"}, {id: '05', name: "Skechers"}, {id: '06', name: "Vansss"},
                    {id: '07', name: "NewBalance3"}, {id: '08', name: "Skechers22"}, {id: '09', name: "NewBalance444"},
                    {id: '010', name: "耐克a"}, {id: '011', name: "阿迪达斯2"}, {id: '012', name: "阿迪达斯43"}
                ];
            },
            selected: function (item) {
                var self = this;
                self.selectedPlatform = item.name;
            },
            submitSet: function () {
                var self = this;
                self.selectedPlatformlist.brand = self.brand;
                self.selectedPlatformlist.selectedPlatform = self.selectedPlatform;
                if (!self.selectedPlatformlist.selectedPlatform) {
                    self.notify.warning('TXT_COMPLETE_THE_PLATEFORM_BRAND');
                    return;
                }
                self.popups.openPlatformMappingConfirm(self.selectedPlatformlist);

            }
        };
        return PlatformBrandSettingController;
    })())
});