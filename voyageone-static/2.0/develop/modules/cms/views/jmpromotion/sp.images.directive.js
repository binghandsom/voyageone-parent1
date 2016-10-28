define([
    'cms',
    'modules/cms/controller/popup.ctl'
], function (cms) {

    function SpImagesDirectiveController(spDataService, popups, notify, alert) {
        this.spDataService = spDataService;
        this.popups = popups;
        this.notify = notify;
        this.alert = alert;
        this.imgUpEntity = {};
        this.imgUrls = {};
    }

    SpImagesDirectiveController.prototype.init = function () {
        var self = this,
            spDataService = self.spDataService;

        spDataService.initPromotionImages().then(function (res) {
            if (res.data.promotionImageUrl)
                self.imgUrls = res.data.promotionImageUrl;
            if (res.data.promotionImagesModel)
                self.imgUpEntity = res.data.promotionImagesModel;
        });

        spDataService.getPromotion().then(function (res) {
            self.promotionInfo = res;
        });

    };

    /**
     * 是否套用模板,只会套用模板供页面显示,不会更新数据
     */
    SpImagesDirectiveController.prototype.canUseTemplate = function () {
        var self = this,
            spDataService = self.spDataService,
            imgUpEntity = self.imgUpEntity;

        if (!imgUpEntity.jmPromotionId)
            return;

        spDataService.getPromotionImgTpl(imgUpEntity).then(function (res) {
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
            brand = promotionInfo.brand ? promotionInfo.brand : '',
            popups = self.popups;

        popups.openImageSuit({brand: brand}).then(function (context) {

            spDataService.getPromotionImgTpl(context).then(function (res) {
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
            popups = self.popups,
            common = angular.copy(spDataService.commonUpEntity);

        popups.openImageBatchJmUpload(_.extend(common, {
            brand: brand
            , imgUpEntity: self.imgUpEntity
        })).then(function () {
            self.init();
        });
    };

    /**
     * 页面单个图片上传弹出框
     * @param imageName  图片类型
     */
    SpImagesDirectiveController.prototype.popImageJmUpload = function (imageType) {
        var self = this,
            spDataService = self.spDataService,
            imgUpEntity = self.imgUpEntity,
            imgUrls = self.imgUrls,
            notify = self.notify,
            popups = self.popups;

        popups.openImageJmUpload({
            promotionId: +spDataService.jmPromotionId,
            imageType: imageType
        }).then(function (res) {
            //用于显示
            imgUrls[imageType] = res.templateUrl;
            //用于存储图片名称
            imgUpEntity[imageType] = res.imageName;

            //更新
            self.save();

            notify.warn("因为图片服务器的延时，如果图片没有显示，请不要慌张，请手动刷新！");
        });
    };

    /**
     * @param saveType    0:暂存    1：提交    2：发布任务      {{ctrlImages.imgChkForm.$valid}}
     */
    SpImagesDirectiveController.prototype.save = function (saveType) {
        var self = this,
            notify = self.notify,
            alert = self.alert,
            spDataService = self.spDataService;

        spDataService.jmPromotionObj.imageStatus = 2;

        if (self.imgChkForm.$invalid) {
            switch (saveType) {
                case 0:
                    alert("聚美专场必传图片没有上传完整！");
                    break;
                case 1:
                    alert("聚美专场必传图片没有上传完整！");
                    return;
                    break;
            }
        }

        if(self.imgUpEntity.saveType == 0)
            self.imgUpEntity.saveType = saveType;
        spDataService.savePromotionImages({
            "promotionImages": self.imgUpEntity,
            "brand": self.promotionInfo.brand,
            "saveType": saveType
        }).then(function () {
            notify.success("更新成功!");
            if (saveType == 1)
                spDataService.jmPromotionObj.imageStatus = 1;

            //刷新页面
            self.init();
        });
    };

    SpImagesDirectiveController.prototype.getImageTimeStamp = function () {
        var imgUpEntity = this.imgUpEntity;

        if (!imgUpEntity.modified)
            return "";

        //return "";
        return "&timeStamp=" + new Date(imgUpEntity.modified).getTime() + 1;
    };

    cms.directive('spImages', [function spImagesDirectiveFactory() {
        return {
            restrict: 'E',
            controller: ['spDataService', 'popups', 'notify', 'alert', SpImagesDirectiveController],
            controllerAs: 'ctrlImages',
            templateUrl: '/modules/cms/views/jmpromotion/sp.images.directive.html'
        }
    }]);
});