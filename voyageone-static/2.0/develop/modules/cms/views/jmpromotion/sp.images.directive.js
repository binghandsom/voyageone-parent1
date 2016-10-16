define([
    'cms',
    'modules/cms/controller/popup.ctl'
], function (cms) {

    function SpImagesDirectiveController(spDataService, popups) {
        this.spDataService = spDataService;
        this.popups = popups;
    }

    SpImagesDirectiveController.prototype.init = function () {

    };

    SpImagesDirectiveController.prototype.popImageSuit = function () {
        var self = this,
            popups = self.popups;

        popups.openImageSuit({}).then(function () {

        });
    };


    cms.directive('spImages', [function spImagesDirectiveFactory() {
        return {
            restrict: 'E',
            controller: ['spDataService', 'popups', SpImagesDirectiveController],
            controllerAs: 'ctrl',
            templateUrl: '/modules/cms/views/jmpromotion/sp.images.directive.html'
        }
    }]);
});