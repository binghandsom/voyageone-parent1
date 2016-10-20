define([
    'cms',
    'modules/cms/controller/popup.ctl'
], function (cms) {

    function SpImagesDirectiveController(spDataService, popups,notify) {
        this.spDataService = spDataService;
        this.popups = popups;
        this.notify = notify;
        this.imgUpEntity = {};
        this.imgUrls = {};
    }

    SpImagesDirectiveController.prototype.init = function () {
        var self = this,
            spDataService = self.spDataService;

        spDataService.initPromotionImages().then(function(res){

            self.imgUrls = res.data.promotionImageUrl;
            self.imgUpEntity = res.data.promotionImagesModel;
        });

        spDataService.getPromotion().then(function(res){
            self.promotionInfo = res;
        });

    };

    SpImagesDirectiveController.prototype.popImageSuit = function () {
        var self = this,
            promotionInfo = self.promotionInfo,
            spDataService = self.spDataService,
            brand = promotionInfo.brand ? promotionInfo.brand : '',
            popups = self.popups;

        popups.openImageSuit({brand:brand}).then(function (context) {
            spDataService.getPromotionImgTpl(context).then(function(res){
                self.imgUrls = res.promotionImageUrl;
                self.imgUpEntity = res.promotionImagesModel;
            });
        });
    };

    SpImagesDirectiveController.prototype.popImageBatchUpload = function () {
        var self = this,
            promotionInfo = self.promotionInfo,
            spDataService = self.spDataService,
            brand = promotionInfo ? promotionInfo.brand : '',
            popups = self.popups;

        popups.openImageBatchJmUpload(_.extend(spDataService.commonUpEntity,{brand:brand
            ,imgUpEntity:self.imgUpEntity})).then(function (context) {

        });
    };


    SpImagesDirectiveController.prototype.popImageJmUpload = function(imageName){
        var self = this,
            spDataService = self.spDataService,
            imgUpEntity = self.imgUpEntity,
            imgUrls = self.imgUrls,
            popups = self.popups;

        popups.openImageJmUpload({
            promotionId: +spDataService.jmPromotionId,
            imageName: imageName
        }).then(function(res){
            //用于显示
            imgUrls[imageName] = res.templateUrl;
            //用于存储图片名称
            imgUpEntity[imageName] =  spDataService.jmPromotionId + "-" + imageName
        });
    };

    SpImagesDirectiveController.prototype.save = function(){
        var self = this,
            notify = self.notify,
            spDataService = self.spDataService;

        spDataService.savePromotionImages({
            "promotionImages":self.imgUpEntity,
            "brand":self.promotionInfo.brand
        }).then(function(){
            notify.success("更新成功!");
        });
    };


    cms.directive('spImages', [function spImagesDirectiveFactory() {
        return {
            restrict: 'E',
            controller: ['spDataService', 'popups', 'notify',SpImagesDirectiveController],
            controllerAs: 'ctrlImages',
            templateUrl: '/modules/cms/views/jmpromotion/sp.images.directive.html'
        }
    }]);
});