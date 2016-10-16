define([
    'cms',
    'modules/cms/controller/popup.ctl'
], function (cms) {

    function SpImagesDirectiveController($routeParams, jmPromotionService, popups) {
        this.$routeParams = $routeParams;
        this.jmPromotionService = jmPromotionService;
        this.popups = popups;
    }

    SpImagesDirectiveController.prototype.init = function () {

    };

    SpImagesDirectiveController.prototype.popImageSuit = function(){
        var self = this,
            popups = self.popups;

        popups.openImageSuit({}).then(function(){

        });
    };


    cms.directive('spImages', [function spImagesDirectiveFactory() {
        return {
            restrict: 'E',
            controller: ['$routeParams', 'jmPromotionService', 'popups', SpImagesDirectiveController],
            controllerAs: 'ctrl',
            templateUrl: '/modules/cms/views/jmpromotion/sp.images.directive.html'
        }
    }]);
});