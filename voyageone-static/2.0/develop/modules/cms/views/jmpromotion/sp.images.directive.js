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

    /**
     * 是否套用模板,只会套用模板供页面显示,不会更新数据
     */
    SpImagesDirectiveController.prototype.canUseTemplate = function(){
        var self = this,
            spDataService = self.spDataService,
            imgUpEntity = self.imgUpEntity;

        spDataService.getPromotionImgTpl(imgUpEntity).then(function(res){
            self.imgUrls = res.promotionImageUrl;
            self.imgUpEntity = res.promotionImagesModel;
        });

    };

    /**
     * 页面选择套图弹出框
     */
    SpImagesDirectiveController.prototype.popImageSuit = function () {
        var self = this,
            promotionInfo = self.promotionInfo,
            spDataService = self.spDataService,
            imgUpEntity = self.imgUpEntity,
            brand = promotionInfo.brand ? promotionInfo.brand : '',
            popups = self.popups;

        popups.openImageSuit({brand:brand}).then(function (context) {

            spDataService.getPromotionImgTpl(context).then(function(res){
                self.imgUrls = res.promotionImageUrl;
                self.imgUpEntity = res.promotionImagesModel;
            });
        });
    };

    /**
     *  页面批量导入弹出框
     */
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

    /**
     * 页面单个图片上传弹出框
     * @param imageName  图片类型
     */
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