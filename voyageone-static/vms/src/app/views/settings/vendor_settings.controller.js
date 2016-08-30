/**
 * Created by vantis on 16-8-30.
 */
define([
    'vms'
], function (vms) {
    vms.controller('VendorSettingsController', (function () {

        function VendorSettingsController(alert, notify, confirm, vendorSettingsService) {
            this.alert = alert;
            this.notify = notify;
            this.confirm = confirm;
            this.vendorSettingsService = vendorSettingsService;
            this.channelConfig = {};
            this.deliveryCompanyList = [];
        }

        VendorSettingsController.prototype.init = function () {
            var self = this;
            self.vendorSettingsService.init().then(function (data) {
                self.channelConfig = data.channelConfig;
                self.deliveryCompanyList = data.deliveryCompanyList;
            });
        };

        return VendorSettingsController;
    }()))
});