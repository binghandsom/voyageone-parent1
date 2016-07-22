/**
 * Created by sofia on 7/21/2016.
 */
define([
    'cms'
], function (cms) {
    cms.controller('PlatformBrandSettingController', (function () {
        function PlatformBrandSettingController(context, alert, popups) {
            this.platformData = context;
            this.alert = alert;
            this.popups = popups;
            this.platformPageOption = {curr: 1, total: 0, size: 10, fetch: this.search};
            this.platformList = [
                {id: '01', name: "Vans"}, {id: '02', name: "耐克"}, {id: '03', name: "阿迪达斯"},
                {id: '04', name: "NewBalance"}, {id: '05', name: "Skechers"}, {id: '06', name: "Vansss"},
                {id: '07', name: "NewBalance3"}, {id: '08', name: "Skechers22"}, {id: '09', name: "NewBalance444"},
                {id: '010', name: "耐克a"}, {id: '011', name: "阿迪达斯2"}, {id: '012', name: "阿迪达斯43"}
            ];
            this.selectedPlatformlist = [];
            this.key = [];
        }

        PlatformBrandSettingController.prototype = {
            init: function () {
                var self = this;
                self.brand = self.platformData;
            },
            byTagChildrenName: function (arr) {
                var key = this.key;
                return key ? arr.filter(function (item) {
                    return item.catName.indexOf(key) > -1;
                }) : arr;
            },
            selected: function (item) {
                console.log(item);
            },
            submitSet: function () {
                var self = this;
                console.log(self.selectedPlatformlist);
                self.popups.openPlatformMappingConfirm();

            }
        };
        return PlatformBrandSettingController;
    })())
});