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
                self.brandMappingService.searchMatchedBrands({ 'cartId': self.platformData.cartId, 'brandId': self.platformData.brandId
                }).then(function(res){
                	self.matchedBrandList=res.data.matchedBrandList;
                    console.log(res);
                });
            },
            confirm: function () {
                var self = this;
                self.result = true;
                self.$uibModalInstance.close(self.result);
            }
        };
        return BrandMappingConfirmController;
    })())
});