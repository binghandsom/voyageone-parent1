define(['cms',
    './sp.edit.directive',
    './sp.shelf.directive',
    './sp.images.directive',
    './sp.data.service',
    './sp.product-list.directive',
    './sp.import-list.directivel',
    './sp.export-list.directivel',
    './bay.window.directive'
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

    SpDetailPageController.prototype.downloadSpecialImageZip = function () {
        var self = this,
            spDataService = self.spDataService;

        spDataService.downloadSpecialImageZip().then(function () {

        });
    };

    SpDetailPageController.prototype.downloadWaresImageZip = function () {
        var self = this,
            spDataService = self.spDataService;

        spDataService.downloadWaresImageZip().then(function () {

        });
    };
    cms.controller('SpDetailPageController', SpDetailPageController);
});