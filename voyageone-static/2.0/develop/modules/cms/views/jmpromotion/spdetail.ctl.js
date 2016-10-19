define(['cms',
    './sp.edit.directive',
    './sp.shelf.directive',
    './sp.images.directive',
    './sp.data.service',
    './sp.product-list.directive',
    './sp.import-list.directivel',
    './sp.export-list.directivel'
], function (cms) {

    function SpDetailPageController(spDataService) {
        this.spDataService = spDataService;
    }

    SpDetailPageController.prototype.loadPromotion = function () {
        var self = this,
            spDataService = self.spDataService;

        spDataService.getPromotion().then(function (promotion) {
            self.promotion = promotion;
        });
    };

    cms.controller('SpDetailPageController', SpDetailPageController);
});