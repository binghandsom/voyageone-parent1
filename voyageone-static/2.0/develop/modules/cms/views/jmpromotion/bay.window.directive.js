define(['cms'], function (cms) {
    function BayWindowComponentController(spDataService) {
        var self = this;
        self.fixedWindows = [];
        self.linkWindows = [];
        self.spDataService = spDataService;
        self.loadTemplates().then(function () {
            self.loadBayWindow();
        })
    }

    BayWindowComponentController.prototype.loadTemplates = function loadTemplates() {
        var self = this,
            spDataService = self.spDataService;

        return spDataService.getJmTemplateUrls().then(function (urls) {
            self.bayWindowTemplateUrls = urls.bayWindowTemplateUrls;
        });
    };

    BayWindowComponentController.prototype.loadBayWindow = function loadBayWindow() {
        var self = this,
            spDataService = self.spDataService;

        spDataService.getBayWindow().then(function (bayWindow) {
            var bayWindows = bayWindow.bayWindows;
            self.bayWindow = bayWindow;

            self.initBayWindows().then(function (_bayWindows) {

                self.fixedWindows = _bayWindows;
                self.linkWindows = (!bayWindows || !bayWindows.length) ? _bayWindows.map(function (item) {
                    return angular.copy(item);
                }) : bayWindows;

                self.switchPreview();
            });
        });
    };

    BayWindowComponentController.prototype.getImage = function getImage(name, index) {
        var self = this,
            bayWindowTemplateUrls = self.bayWindowTemplateUrls;
        return bayWindowTemplateUrls[index && 1].replace('%s', name);
    };

    BayWindowComponentController.prototype.initBayWindows = function initBayWindows() {
        var self = this,
            spDataService = self.spDataService;

        return spDataService.getPromotionModules().then(function (modulesList) {
            return modulesList.map(function (modules, index) {
                var name = modules.module.moduleTitle;
                return {
                    name: name,
                    link: '',
                    url: self.getImage(name, index),
                    order: index,
                    enabled: true
                };
            });
        });
    };

    BayWindowComponentController.prototype.updateUrl = function updateUrl(item, index) {
        item.url = this.getImage(item.name, index);
    };

    BayWindowComponentController.prototype.add = function add() {
        var self = this,
            linkWindows = self.linkWindows;

        linkWindows.push({
            name: '',
            link: '',
            url: self.getImage(name, linkWindows.length),
            order: linkWindows.length,
            enabled: true
        });
    };

    BayWindowComponentController.prototype.switchPreview = function () {
        var self = this,
            bayWindow = self.bayWindow;
        self.previewWindows = bayWindow.fixed ? self.fixedWindows : self.linkWindows;
    };

    cms.directive('bayWindow', function bayWindowDirectiveFactory() {
        return {
            templateUrl: '/modules/cms/views/jmpromotion/bay.window.directive.html',
            scope: {},
            controllerAs: 'ctrl',
            controller: ['spDataService', BayWindowComponentController]
        };
    });
});