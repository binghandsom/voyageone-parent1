define([
    'cms'
], function (cms) {

    cms.controller('imageBatchUploadCtl', (function () {

        function ImageBatchUploadCtl($uibModalInstance, FileUploader,blockUI, context) {
            this.$uibModalInstance = $uibModalInstance;
            this.FileUploader = FileUploader;
            this.blockUI = blockUI.instances.get('imgUpload');
            this.context = context;
        }

        ImageBatchUploadCtl.prototype.init = function () {
            var self = this,
                blockUI = self.blockUI,
                $uibModalInstance = self.$uibModalInstance,
                uploader = new self.FileUploader({
                    url: '/cms/pop/jmPromotion/upload'
                });


            uploader.onSuccessItem = function (fileItem, response) {

                blockUI.stop();
                $uibModalInstance.close(response.data);

            };

            this.uploader = uploader;
        };

        ImageBatchUploadCtl.prototype.upload = function () {
            var self = this,
                context = self.context,
                blockUI = self.blockUI,
                imgRequest = self.uploader.queue[0];

            blockUI.start("图片上传中。。。请耐心等待！");

            imgRequest.formData = [{
                promotionId: context.promotionId,
                imageName: context.imageName
            }];
            imgRequest.upload();

        };

        return ImageBatchUploadCtl;

    })());

});