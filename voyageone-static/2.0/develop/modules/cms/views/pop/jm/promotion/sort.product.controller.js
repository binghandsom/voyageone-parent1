define(['cms'], function (cms) {
    function JmSortProductPopupController(spDataService, context) {
        this.tagId = context.tagId;
        this.spDataService = spDataService;
        this.loadProduct();
    }

    JmSortProductPopupController.prototype.loadProduct = function () {
        var self = this,
            spDataService = self.spDataService;

        spDataService.getPromotionProducts(self.tagId).then(function (productList) {
            self.productList = productList;
        });
    };

    cms.controller('JmSortProductPopupController', ['spDataService', 'context', JmSortProductPopupController]);
});