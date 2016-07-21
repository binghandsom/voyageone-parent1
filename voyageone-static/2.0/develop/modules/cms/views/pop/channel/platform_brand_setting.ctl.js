/**
 * Created by sofia on 7/21/2016.
 */
define([
    'cms'
], function (cms) {
    cms.controller('PlatformBrandSettingController', (function () {
        function PlatformBrandSettingController(context) {
            this.platformData = context;
        }

        PlatformBrandSettingController.prototype = {
            init: function () {
                var self = this;
                self.brand = self.platformData;
            }
        };
        return PlatformBrandSettingController;
    })())
});