define([
    'cms',
    'underscore',
    'modules/cms/views/jmpromotion/sort.product.controller'
], function (cms, _) {

    function SpModelDirectiveController(spDataService, notify, popups, $compile, $templateRequest, $document, $scope) {
        var self = this;

        self.spDataService = spDataService;
        self.notify = notify;
        self.popups = popups;
        self.$compile = $compile;
        self.$templateRequest = $templateRequest;
        self.$document = $document;
        self.$scope = $scope;

        $scope.$on('detail.saved', function () {
            self.loadModules();
        });
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

        spDataService.getJmTemplateUrls().then(function (urls) {
            self.barUrl = urls.separatorBar;
        });
    };

    SpModelDirectiveController.prototype.save = function save(isSubmit) {
        var self = this,
            spDataService = self.spDataService;

        if (self.tagsForm.$invalid) {
            self.notify.warning('TXT_SAVE_ERROR');
            return;
        }

        spDataService.saveModules(self.modules, isSubmit).then(function () {
            self.notify.success('TXT_SAVE_SUCCESS');
        });
    };

    SpModelDirectiveController.prototype.submit = function submit() {
        this.save(true);
    };

    cms.directive('spShelf', function spShelfDirectiveFactory() {
        return {
            restrict: 'E',
            controller: ['spDataService', 'notify', 'popups', '$compile', '$templateRequest', '$document', '$scope', SpModelDirectiveController],
            controllerAs: 'ctrl',
            scope: {},
            templateUrl: '/modules/cms/views/jmpromotion/sp.shelf.directive.html'
        }
    });
});