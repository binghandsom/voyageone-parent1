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
        bigImageUrl: 'http://image.voyageone.com.cn/is/image/sneakerhead/✓?wid=2200&hei=2200'
    };

    cms.controller('uploadImagesController', (function () {

        function UploadImagesCtl(context, $uibModalInstance, popups, $productDetailService, confirm, notify, $rootScope, alert) {
            this.context = context;
            this.uibModalInstance = $uibModalInstance;
            this.popups = popups;
            this.confirm = confirm;
            this.notify = notify;
            this.$rootScope = $rootScope;
            this.productDetailService = $productDetailService;
            this.copyImages = [
                {
                    value: 'image1',
                    name: '品牌方商品图'
                }, {
                    value: 'image6',
                    name: 'PC端自拍商品图'
                }, {
                    value: 'image7',
                    name: 'APP端自拍商品图'
                }, {
                    value: 'image2',
                    name: '包装图'
                }, {
                    value: 'image3',
                    name: '角度图'
                }, {
                    value: 'image4',
                    name: 'PC端自定义图'
                }, {
                    value: 'image5',
                    name: 'APP端自定义图'
                }, {
                    value: 'image8',
                    name: '吊牌图'
                }, {
                    value: 'image9',
                    name: '耐久性标签图'
                }
            ];
            this.alert = alert;
        }

        UploadImagesCtl.prototype.init = function () {
            var self = this;

            self.currentImage = self.$rootScope.imageUrl.replace('%s', self.context.platform.images1[0].image1);

            self.images1 = self.context.platform.images1;
            self.platform = self.context.platform;
        };

        UploadImagesCtl.prototype.getPlatForm = function (upEntity) {
            return this.productDetailService.getProductPlatform(upEntity);
        };

        /**
         * 触发图片上传
         * @param imageType 图片类型
         */
        UploadImagesCtl.prototype.openProImageSetting = function (imageType) {
            var self = this,
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

        UploadImagesCtl.prototype.goDetail = function () {
            var self = this,
                args = self.currentImage.split("/");

            if (args.length == 0)
                return;

            window.open(mConfig.bigImageUrl.replace("✓", args[args.length - 1]));
        };

        UploadImagesCtl.prototype.sortImg = function (imagesType) {
            var self = this, context = self.context,
                imgSource = self.platform[imagesType];

            if (!imagesType || imgSource.length < 2)
                return;

            self.productDetailService.restorePlatFromImg({
                cartId: context.cartId,
                prodId: context.productId,
                imagesType: imagesType,
                images: imgSource
            }).then(function () {
                self.getPlatForm({
                    cartId: context.cartId,
                    prodId: context.productId
                }).then(function (res) {
                    self.platform = res.data.platform;
                    self.notify.success("排序成功！");
                });
            });

        };

        UploadImagesCtl.prototype.simpleImgDown = function (imgName, $event) {

            var jq = angular.element,
                _aTag;

            imgName = mConfig.bigImageUrl.replace("✓", imgName);
            _aTag = jq('<a download>').attr({'href': imgName});

            jq('body').append(_aTag);
            _aTag[0].click();
            _aTag.remove();

            $event.stopPropagation();

        };

        UploadImagesCtl.prototype.copyToOtherImg = function (image, targetImg) {
            var self = this,
                imageType = Object.keys(image)[0],
                platform = self.context.platform,
                imageName = image[imageType];

            self.confirm("您确认要复制到【" + targetImg.name + "】吗?").then(function () {


                var _orgImg = _.find(platform[imageType.replace('image', 'images')], function (item) {
                    return item[imageType] === imageName;
                });

                var jsonStr = angular.toJson(_orgImg).replace(/image\d{1}/g, targetImg.value);
                _target = platform[targetImg.value.replace('image', 'images')];

                if (!_target) {
                    _target = platform[targetImg.value.replace('image', 'images')] = [];
                }

                var isExit = _.some(_target, function (item) {
                    return item[targetImg.value] === imageName;
                });

                if (isExit) {
                    self.alert(targetImg.name + "上已经存在该图片。");
                    return;
                }

                _target.push(angular.fromJson(jsonStr));

                self.notify.success('复制成功。');

            });
        };

        UploadImagesCtl.prototype.close = function(){
            var self = this;

            //删除品牌图
            delete self.platform.images1;

            self.uibModalInstance.dismiss();
        };

        return UploadImagesCtl;

    })());

});
