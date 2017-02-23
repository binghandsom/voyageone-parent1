/**
 * @description 产品详情页 商品图片上传
 *
 * image1 品牌方商品图
 * image2 包装图
 * image3 角度图
 * image4 PC端自定义图
 * image5 APP端自定义图
 * image6  PC端自拍商品图
 * image7  APP端自拍商品图
 * image8 吊牌图
 * image9 耐久性标签图
 *
 * @author Piao
 */
define([
    'cms'
], function (cms) {

    var mConfig = {
        bigImageUrl: 'http://image.sneakerhead.com/is/image/sneakerhead/✓?wid=2200&hei=2200'
    };

    cms.controller('uploadImagesController', (function () {

        function UploadImagesCtl(context, $uibModalInstance, popups, $productDetailService, confirm, notify, $rootScope) {
            this.context = context;
            this.uibModalInstance = $uibModalInstance;
            this.popups = popups;
            this.confirm = confirm;
            this.notify = notify;
            this.$rootScope = $rootScope;
            this.productDetailService = $productDetailService;
        }

        UploadImagesCtl.prototype.init = function () {
            var self = this;

            self.currentImage = self.$rootScope.imageUrl.replace('%s', self.context.platform.images1[0].image1);

            self.platform = self.context.platform;
        };

        UploadImagesCtl.prototype.getPlatForm = function (upEntity) {
            var self = this,
                context = self.context;
            return self.productDetailService.getProductPlatform({
                cartId: context.cartId,
                prodId: context.productId
            })
        };

        /**
         * 触发图片上传
         * @param imageType 图片类型
         */
        UploadImagesCtl.prototype.openProImageSetting = function (imageType) {
            var self = this,
                productDetailService = self.productDetailService,
                context = self.context,
                popups = self.popups;

            popups.openImageSetting({
                productId: context.productId,
                cartId: context.cartId,
                imageType: imageType
            }).then(function () {
                self.getPlatForm({
                    cartId: context.cartId,
                    prodId: context.productId
                }).then(function (res) {
                    self.platform = res.data.platform;
                });

            });

        };

        UploadImagesCtl.prototype.restorePlatformImg = function (imagesType, imageName, $event) {
            var self = this,
                productDetailService = self.productDetailService,
                pictures = self.platform[imagesType],
                _rmIndex = 0;

            if (!imagesType)
                return;

            self.confirm("您确认要删除该图片吗？").then(function () {
                _.each(pictures, function (ele, index) {
                    if (ele[imagesType.replace('images', 'image')] === imageName) {
                        _rmIndex = index;
                    }
                });

                pictures.splice(_rmIndex, 1);

                productDetailService.restorePlatFromImg({
                    prodId: self.context.productId,
                    cartId: self.context.cartId,
                    imagesType: imagesType,
                    images: pictures
                }).then(function () {
                    self.getPlatForm({
                        cartId: self.context.cartId,
                        prodId: self.context.productId
                    }).then(function (res) {
                        self.platform = res.data.platform;
                        self.notify.success("删除成功！");
                    });
                });

            });

            $event.stopPropagation();
        };

        UploadImagesCtl.prototype.complete = function () {
            var self = this;

            //删除品牌图
            delete self.platform.images1;

            self.uibModalInstance.close(self.platform);
        };

        /**控制对应图片类型tab的显示*/
        UploadImagesCtl.prototype.canDisplay = function (imageType) {
            if (!this.context.showArr || this.context.showArr.length == 0)
                return true;

            return this.context.showArr.indexOf(imageType) >= 0;
        };

        UploadImagesCtl.prototype.goDetail = function(){
            var self = this,
                args = self.currentImage.split("/");

            if (args.length == 0)
                return;

            window.open(mConfig.bigImageUrl.replace("✓", args[args.length - 1]));
        };

        return UploadImagesCtl;

    })());

});
