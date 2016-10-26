define(['cms'], function (cms) {
    function BayWindowComponentController(spDataService, confirm, notify, $scope) {
        var self = this;
        self.fixedWindows = [];
        self.linkWindows = [];
        self.confirm = confirm;
        self.notify = notify;
        self.spDataService = spDataService;
        self.loadTemplates().then(function () {
            self.loadBayWindow();
        });

        $scope.$on('detail.saved', function () {
            self.loadBayWindow();
        });
    }

    BayWindowComponentController.prototype.moveKeys = {
        up: 'up',
        upToTop: 'upToTop',
        down: 'down',
        downToLast: 'downToLast'
    };

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
                self.fixedWindows = (!bayWindow.fixed || (!bayWindows || !bayWindows.length)) ? _bayWindows : _bayWindows.map(function (_bayWindowItem, index) {
                    return (index === 0) ? bayWindows[0] : angular.merge(_bayWindowItem, bayWindows.find(function (bayWindowItem) {
                        return bayWindowItem.name === _bayWindowItem.name;
                    }));
                });

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

    BayWindowComponentController.prototype.addLinkWindow = function addLinkWindow() {
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

    BayWindowComponentController.prototype.switchPreview = function switchPreview() {
        var self = this,
            bayWindow = self.bayWindow;
        self.previewWindows = bayWindow.fixed ? self.fixedWindows : self.linkWindows;
    };

    BayWindowComponentController.prototype.removeLinkWindow = function removeLinkWindow(item, index) {
        var self = this,
            linkWindows = self.linkWindows;

        self.confirm('TXT_MSG_DELETE_ITEM').then(function () {
            linkWindows.splice(index, 1);
        });
    };

    BayWindowComponentController.prototype.moveLinkWindow = function moveLinkWindow(i, moveKey) {
        var self = this,
            linkWindows = self.linkWindows,
            moveKeys = self.moveKeys,
            temp;

        switch (moveKey) {
            case moveKeys.up:
                if (i === 1)
                    return;
                temp = linkWindows[i];
                linkWindows[i] = linkWindows[i - 1];
                linkWindows[i - 1] = temp;
                return;
            case moveKeys.upToTop:
                if (i === 1)
                    return;
                temp = linkWindows.splice(i, 1);
                linkWindows.splice(1, 0, temp[0]);
                return;
            case moveKeys.down:
                if (i === linkWindows.length - 1)
                    return;
                temp = linkWindows[i];
                linkWindows[i] = linkWindows[i + 1];
                linkWindows[i + 1] = temp;
                return;
            case moveKeys.downToLast:
                if (i === linkWindows.length - 1)
                    return;
                temp = linkWindows.splice(i, 1);
                linkWindows.push(temp[0]);
                return;
        }
    };

    BayWindowComponentController.prototype.saveAll = function saveAll() {
        var self = this,
            spDataService = self.spDataService,
            bayWindow = self.bayWindow,
            notify = self.notify;

        if (self.bayWindowForm.$invalid) {
            notify.warning('TXT_SAVE_ERROR');
            return;
        }

        bayWindow.bayWindows = bayWindow.fixed ? self.fixedWindows : self.linkWindows;

        spDataService.saveBayWindow(bayWindow).then(function () {
            notify.success('TXT_SAVE_SUCCESS');
        });
    };

    cms.directive('bayWindow', function bayWindowDirectiveFactory() {
        return {
            templateUrl: '/modules/cms/views/jmpromotion/bay.window.directive.html',
            scope: {},
            controllerAs: 'ctrl',
            controller: ['spDataService', 'confirm', 'notify', '$scope', BayWindowComponentController]
        };
    });
});