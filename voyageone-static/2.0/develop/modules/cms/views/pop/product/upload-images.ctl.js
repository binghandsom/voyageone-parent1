/**
 * @description 产品详情页 商品图片上传
 * @author Piao
 */
define([
    'cms'
], function (cms) {

    cms.controller('uploadImagesController', (function () {

        function UploadImagesCtl(context, popups) {
            this.context = context;
            this.popups = popups;
        }

        UploadImagesCtl.prototype.init = function () {
            var self = this;

            self.platform = self.context.platform;
        };

        //openImageSetting
        UploadImagesCtl.prototype.openProImageSetting = function (imageType) {
            var self = this,
                context = self.context,
                popups = self.popups;

            popups.openImageSetting({
                productId: context.productId,
                cartId: context.cartId,
                imageType: imageType
            }).then(function (context) {
                console.log(context);
            });

        };

        return UploadImagesCtl;

    })());

});
