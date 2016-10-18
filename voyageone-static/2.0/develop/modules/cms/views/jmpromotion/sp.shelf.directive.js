define([
    'cms',
    'underscore',
    'modules/cms/controller/popup.ctl'
], function (cms, _) {

    function SpModelDirectiveController(spDataService, notify, popups) {
        this.spDataService = spDataService;
        this.notify = notify;
        this.popups = popups;
    }

    SpModelDirectiveController.prototype.productsSortBy = {
        '按销量降序': 1,
        '填写顺序': 2
    };

    SpModelDirectiveController.prototype.openProductSortPopup = function (tagId) {
        var self = this,
            popups = self.popups;

        popups.popSortProduct({tagId: tagId});
    };

    SpModelDirectiveController.prototype.loadModules = function () {
        var self = this,
            spDataService = self.spDataService;

        spDataService.getPromotionModules().then(function (modules) {
            self.modules = modules;
        });
    };

    SpModelDirectiveController.prototype.saveAll = function () {
        var self = this,
            spDataService = self.spDataService;

        spDataService.saveModules(self.modules).then(function () {
            self.notify.success('已保存');
        });
    };

    cms.directive('spShelf', [function spModelDirectivefactory() {
        return {
            restrict: 'E',
            controller: ['spDataService', 'notify', 'popups', SpModelDirectiveController],
            controllerAs: 'ctrl',
            scope: {},
            templateUrl: '/modules/cms/views/jmpromotion/sp.shelf.directive.html'
        }
    }]);
});