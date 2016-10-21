define(['cms',
    './sp.edit.directive',
    './sp.shelf.directive',
    './sp.images.directive',
    './sp.data.service',
    './sp.product-list.directive',
    './sp.import-list.directivel',
    './sp.export-list.directivel',
    './bay.window.directive'
], function (cms) {

    function SpDetailPageController(spDataService,cActions) {
        this.spDataService = spDataService;
        this.cActions = cActions;
    }

    SpDetailPageController.prototype.loadPromotion = function () {
        var self = this,
            spDataService = self.spDataService;

        spDataService.getPromotion().then(function (promotion) {
            self.promotion = promotion;
        });
    };

    SpDetailPageController.prototype.downloadSpecialImageZip = function () {
        var self = this,
            cActions = self.cActions,
            spDataService = self.spDataService;
        $.download.post(cActions.cms.jmpromotion.JmPromotionImagesService.root + "/" + cActions.cms.jmpromotion.JmPromotionImagesService.downloadSpecialImageZip, {"jmPromotionId": spDataService.jmPromotionId ,"promotionName":self.promotion.name});

    };

    SpDetailPageController.prototype.downloadWaresImageZip = function () {
        var self = this,
            cActions = self.cActions,
            spDataService = self.spDataService;
        $.download.post(cActions.cms.jmpromotion.JmPromotionImagesService.root + "/" + cActions.cms.jmpromotion.JmPromotionImagesService.downloadWaresImageZip, {"jmPromotionId": spDataService.jmPromotionId ,"promotionName":self.promotion.name});
    };

    SpDetailPageController.prototype.downloadJmPromotionInfo = function (type) {
        var self = this,
            cActions = self.cActions,
            spDataService = self.spDataService;

        $.download.post(cActions.cms.cmsBtJmPromotionExportTask.cmsBtJmPromotionExportTaskService.root + "/" + cActions.cms.cmsBtJmPromotionExportTask.cmsBtJmPromotionExportTaskService.exportJmPromotionInfo, {"type":type,"jmPromotionId": spDataService.jmPromotionId,"promotionName":self.promotion.name});

    };

    cms.controller('SpDetailPageController', SpDetailPageController);
});