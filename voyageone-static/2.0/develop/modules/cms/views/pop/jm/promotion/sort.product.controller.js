define(['cms'], function (cms) {
    function JmSortProductPopupController(spDataService) {
        this.spDataService = spDataService;
        this.loadProduct();
    }

    JmSortProductPopupController.prototype.loadProduct = function () {
        var self = this,
            spDataService = self.spDataService;
    };

    cms.controller('JmSortProductPopupController', ['spDataService', JmSortProductPopupController]);
});