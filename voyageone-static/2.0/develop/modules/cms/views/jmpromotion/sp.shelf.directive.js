define([
    'cms',
    'underscore',
    'modules/cms/controller/popup.ctl'
], function (cms, _) {

    function SpModelDirectiveController(spDataService) {
        this.spDataService = spDataService;
    }

    SpModelDirectiveController.prototype.loadModules = function () {
        var self = this,
            spDataService = self.spDataService;

        spDataService.getPromotionModules().then(function (modules) {
            this.modules = modules;
        });
    };

    cms.directive('spShelf', [function spModelDirectivefactory() {
        return {
            restrict: 'E',
            controller: ['spDataService', SpModelDirectiveController],
            controllerAs: 'ctrl',
            templateUrl: '/modules/cms/views/jmpromotion/sp.shelf.directive.html'
        }
    }]);
});