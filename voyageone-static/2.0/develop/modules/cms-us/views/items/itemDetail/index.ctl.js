define([
    'cms'
], function (cms) {

    cms.controller('itemDetailController', class itemDetailController {
        constructor(popups, $routeParams, itemDetailService) {
            this.code = $routeParams.code;
            this.popups = popups;
            this.itemDetailService = itemDetailService;

            this.feed = {};
            this.init();
        }

        init() {
            let self = this;
            // 根据code加载Feed
            self.itemDetailService.detail({code: "153265-113"}).then((resp) => {
                if (resp.data) {
                    self.feed = resp.data;
                }
            });

        }

        popUsCategory() {
            this.popups.openUsCategory().then(context => {
                console.log('context', context);
            });
        }

    });

});