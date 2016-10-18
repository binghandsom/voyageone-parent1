define([
    'cms',
    'modules/cms/controller/popup.ctl'
], function (cms) {

    var ImgTabConfig = {
      rootPath : 'http://image.sneakerhead.com/is/image/sneakerhead/'
    };

    function SpImagesDirectiveController(spDataService, popups) {
        this.spDataService = spDataService;
        this.popups = popups;
        this.imgUpEntity = {};
    }

    SpImagesDirectiveController.prototype.init = function () {

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
            popups = self.popups;

        popups.openImageJmUpload({
            promotionId: +spDataService.jmPromotionId,
            imageName: imageName
        }).then(function(res){
            imgUpEntity[imageName] = ImgTabConfig.rootPath + res.imageName;
            console.log(imgUpEntity[imageName]);
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