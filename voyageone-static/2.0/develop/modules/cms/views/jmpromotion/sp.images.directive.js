define([
    'cms',
    'modules/cms/controller/popup.ctl'
], function (cms) {

    function SpImagesDirectiveController(spDataService, popups) {
        this.spDataService = spDataService;
        this.popups = popups;
        this.imgUpEntity = {};
        this.imgUrls = {};
    }

    SpImagesDirectiveController.prototype.init = function () {
        var self = this,
            spDataService = self.spDataService;

        spDataService.initPromotionImages().then(function(res){
            console.log("res",res);
        });
    };

    SpImagesDirectiveController.prototype.popImageSuit = function () {
        var self = this,
            popups = self.popups;

        popups.openImageSuit({}).then(function () {

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
            spDataService = self.spDataService;

        spDataService.savePromotionImages({
            "promotionImages":self.imgUpEntity
        });
    };


    cms.directive('spImages', [function spImagesDirectiveFactory() {
        return {
            restrict: 'E',
            controller: ['spDataService', 'popups', SpImagesDirectiveController],
            controllerAs: 'ctrlImages',
            templateUrl: '/modules/cms/views/jmpromotion/sp.images.directive.html'
        }
    }]);
});