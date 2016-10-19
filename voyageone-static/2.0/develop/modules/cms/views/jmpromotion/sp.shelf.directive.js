define([
    'cms',
    'underscore',
    'modules/cms/views/jmpromotion/sort.product.controller'
], function (cms, _) {

    function SpModelDirectiveController(spDataService, notify, popups, $compile, $templateRequest, $document, $scope) {
        this.spDataService = spDataService;
        this.notify = notify;
        this.popups = popups;
        this.$compile = $compile;
        this.$templateRequest = $templateRequest;
        this.$document = $document;
        this.$scope = $scope;
    }

    SpModelDirectiveController.prototype.productsSortBy = {
        '按销量降序': 1,
        '填写顺序': 2
    };

    SpModelDirectiveController.prototype.openProductSortPopup = function (tagId) {
        var self = this,
            $compile = self.$compile,
            $document = self.$document,
            $templateRequest = self.$templateRequest,
            $scope = self.$scope,
            body = $document[0].body;

        $templateRequest('/modules/cms/views/jmpromotion/sort.product.html').then(function (html) {
            var modal = $(html);
            var modalChildScope = $scope.$new();

            modalChildScope.tagId = tagId;

            modal.appendTo(body);
            $compile(modal)(modalChildScope);
        });
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
            controller: ['spDataService', 'notify', 'popups', '$compile', '$templateRequest', '$document', '$scope', SpModelDirectiveController],
            controllerAs: 'ctrl',
            scope: {},
            templateUrl: '/modules/cms/views/jmpromotion/sp.shelf.directive.html'
        }
    }]);
});