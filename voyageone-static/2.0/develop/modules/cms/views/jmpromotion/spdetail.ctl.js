define(['cms', './sp.edit.directive', './sp.shelf.directive', './sp.data.service'], function (cms) {

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