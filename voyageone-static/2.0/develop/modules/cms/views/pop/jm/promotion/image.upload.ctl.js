define([
    'cms'
], function (cms) {

    cms.controller('imageUploadCtl', (function () {

        function ImageUploadCtl($uibModalInstance, FileUploader, context) {
            this.$uibModalInstance = $uibModalInstance;
            this.FileUploader = FileUploader;
            this.context = context;
        }

        ImageUploadCtl.prototype.init = function () {
            var self = this,
                uploader = new self.FileUploader({
                    url: '/cms/pop/jmPromotion/upload'
                });


            uploader.onSuccessItem = function (fileItem, response) {

            };

            uploader.onBeforeUploadItem = function (fileItem) {

            };

            this.uploader = uploader;
        };

        ImageUploadCtl.prototype.upload = function () {
            var self = this,
                context = self.context,
                imgRequest = self.uploader.queue[0];

            imgRequest.formData = [{
                promotionId: context.promotionId,
                imageName: context.imageName
            }];
            imgRequest.upload();

        };

        return ImageUploadCtl;

    })());

});