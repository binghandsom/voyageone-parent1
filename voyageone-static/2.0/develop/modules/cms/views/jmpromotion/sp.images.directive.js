define([
    'cms',
    'modules/cms/controller/popup.ctl'
], function (cms) {

    var IMG_CONFIG = {
        counts: 21
    };

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
            popups = self.popups;

        popups.openImageBatchJmUpload(_.extend(spDataService.commonUpEntity, {
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
    SpImagesDirectiveController.prototype.popImageJmUpload = function (imageName) {
        var self = this,
            spDataService = self.spDataService,
            imgUpEntity = self.imgUpEntity,
            imgUrls = self.imgUrls,
            popups = self.popups;

        popups.openImageJmUpload({
            promotionId: +spDataService.jmPromotionId,
            imageName: imageName
        }).then(function (res) {
            //用于显示
            imgUrls[imageName] = res.templateUrl;
            //用于存储图片名称
            imgUpEntity[imageName] = spDataService.jmPromotionId + "-" + imageName

            //更新
            self.save();
        });
    };

    /**
     * @param saveType    0:暂存    1：提交    2：发布任务
     */
    SpImagesDirectiveController.prototype.save = function (saveType) {
        var self = this,
            notify = self.notify,
            alert = self.alert,
            counts = 0,
            spDataService = self.spDataService;
        spDataService.jmPromotionObj.imageStatus = 2;

        if (saveType == 1) {
            _.each(self.imgUpEntity, function (value, key) {
                if (value && typeof value == 'string' && value.indexOf(key) >= 0)
                    counts++;
            });

            if (counts != IMG_CONFIG) {
                alert("聚美活动图片没有上传完整！");
                return;
            }
        }

        spDataService.savePromotionImages({
            "promotionImages": self.imgUpEntity,
            "brand": self.promotionInfo.brand,
            "saveType": saveType
        }).then(function(){
            notify.success("更新成功!");
            spDataService.jmPromotionObj.imageStatus = 1;

            //刷新页面
            self.init();
        });
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