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

    function SpDetailPageController(spDataService,cActions,cmsBtJmPromotionExportTaskService,notify) {
        this.spDataService = spDataService;
        this.cActions = cActions;
        this.notify = notify;
        this.cmsBtJmPromotionExportTaskService=cmsBtJmPromotionExportTaskService;
    }

    SpDetailPageController.prototype.loadPromotion = function () {
        var self = this,
            spDataService = self.spDataService;

        spDataService.getPromotion().then(function (promotion) {
            self.promotion = promotion;
            spDataService.jmPromotionObj = promotion;
        });
    };
    SpDetailPageController.prototype.addExport = function () {
        console.log(this.promotion);
        var model = {templateType: 0, cmsBtJmPromotionId:this.promotion.id};
        this.cmsBtJmPromotionExportTaskService.addExport(model).then(function (res) {
           // $scope.searchExport();
        }, function (res) {

        });
    };
    SpDetailPageController.prototype.downloadSpecialImageZip = function () {
        var self = this,
            cActions = self.cActions,
            notify = self.notify,
            spDataService = self.spDataService;

        notify.success("准备图片下载中，请留意浏览器右下角！");

        $.download.post(cActions.cms.jmpromotion.JmPromotionImagesService.root + "/" + cActions.cms.jmpromotion.JmPromotionImagesService.downloadSpecialImageZip,
            {"jmPromotionId": spDataService.jmPromotionId ,
              "promotionName":self.promotion.name});

    };

    SpDetailPageController.prototype.downloadWaresImageZip = function () {
        var self = this,
            cActions = self.cActions,
            notify = self.notify,
            spDataService = self.spDataService;

        notify.success("准备图片下载中，请留意浏览器右下角！");

        $.download.post(cActions.cms.jmpromotion.JmPromotionImagesService.root + "/" + cActions.cms.jmpromotion.JmPromotionImagesService.downloadWaresImageZip, {"jmPromotionId": spDataService.jmPromotionId ,"promotionName":self.promotion.name});
    };

    SpDetailPageController.prototype.downloadJmPromotionInfo = function (type) {
        var self = this,
            cActions = self.cActions,
            notify = self.notify,
            spDataService = self.spDataService;

        notify.success("准备图片下载中，请留意浏览器右下角！");

        $.download.post(cActions.cms.cmsBtJmPromotionExportTask.cmsBtJmPromotionExportTaskService.root + "/" + cActions.cms.cmsBtJmPromotionExportTask.cmsBtJmPromotionExportTaskService.exportJmPromotionInfo, {"type":type,"jmPromotionId": spDataService.jmPromotionId,"promotionName":self.promotion.name});

    };

    cms.controller('SpDetailPageController', SpDetailPageController);
});