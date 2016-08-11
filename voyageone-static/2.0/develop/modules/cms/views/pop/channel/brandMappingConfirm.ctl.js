/**
 * Created by sofia on 7/22/2016.
 */
define([
    'cms'
], function (cms) {
    cms.controller('BrandMappingConfirmController', (function () {
        function BrandMappingConfirmController(context, $uibModalInstance, brandMappingService) {
            this.platformData = context;
            this.$uibModalInstance = $uibModalInstance;
            this.brandMappingService = brandMappingService;
            this.result = false;
        }

        BrandMappingConfirmController.prototype = {
            init: function () {
                var self = this;
                self.brandMappingService.searchMatchedBrands({
                    'cartId': self.platformData.cartId, 'brandId': self.platformData.brandId
                }).then(function (res) {
                    self.matchedBrandList = res.data.matchedBrandList;
                });
            },
            confirm: function () {
                var self = this;
                var confirmResult={
                    result : true,
                    brandId : self.platformData.brandId
                };
                self.$uibModalInstance.close(confirmResult);
            }
        };
        return BrandMappingConfirmController;
    })())
});