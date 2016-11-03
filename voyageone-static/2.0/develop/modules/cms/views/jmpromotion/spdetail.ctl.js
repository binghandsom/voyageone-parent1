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

    function SpDetailPageController(spDataService, cActions, cmsBtJmPromotionExportTaskService, notify, confirm) {
        this.spDataService = spDataService;
        this.cActions = cActions;
        this.notify = notify;
        this.confirm = confirm;
        this.cmsBtJmPromotionExportTaskService = cmsBtJmPromotionExportTaskService;
    }

    SpDetailPageController.prototype.skipUrl = function (res) {
        var self = this,
            confirm = self.confirm;
        confirm("是否要跳转的新建的返场活动。").then(function () {
            window.location.href = "#/jmpromotion/detail/" + res.data.promotionId + "/" + res.data.jmPromotionId;
        });
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
        var model = {templateType: 0, cmsBtJmPromotionId: this.promotion.id};
        this.cmsBtJmPromotionExportTaskService.addExport(model).then(function (res) {
            // $scope.searchExport();
        }, function (res) {

        });
    };
    SpDetailPageController.prototype.downloadSpecialImageZip = function () {
        var self = this,
            cActions = self.cActions,
            confirm = self.confirm,
            spDataService = self.spDataService;

        confirm("请确认活动图片效果后，再行导出。").then(function(){
            $.download.post(cActions.cms.jmpromotion.JmPromotionImagesService.root + "/" + cActions.cms.jmpromotion.JmPromotionImagesService.downloadSpecialImageZip,
                {
                    "jmPromotionId": spDataService.jmPromotionId,
                    "promotionName": self.promotion.name
                });

        });

    };

    SpDetailPageController.prototype.downloadWaresImageZip = function () {
        var self = this,
            cActions = self.cActions,
            confirm = self.confirm,
            spDataService = self.spDataService;

        confirm("请确认活动图片效果后，再行导出。").then(function(){
            $.download.post(cActions.cms.jmpromotion.JmPromotionImagesService.root + "/" + cActions.cms.jmpromotion.JmPromotionImagesService.downloadWaresImageZip, {
                "jmPromotionId": spDataService.jmPromotionId,
                "promotionName": self.promotion.name
            });
        });

    };

    SpDetailPageController.prototype.downloadJmPromotionInfo = function (type) {
        var self = this,
            cActions = self.cActions,
            notify = self.notify,
            spDataService = self.spDataService;

        notify.warning("准备图片下载中，请留意浏览器左下角！");

        $.download.post(cActions.cms.cmsBtJmPromotionExportTask.cmsBtJmPromotionExportTaskService.root + "/" + cActions.cms.cmsBtJmPromotionExportTask.cmsBtJmPromotionExportTaskService.exportJmPromotionInfo, {
            "type": type,
            "jmPromotionId": spDataService.jmPromotionId,
            "promotionName": self.promotion.name
        });

    };

    cms.controller('SpDetailPageController', SpDetailPageController);
});