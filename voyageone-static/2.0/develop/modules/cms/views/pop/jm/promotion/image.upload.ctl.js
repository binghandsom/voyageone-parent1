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
                    url: '/cms/pop/image_setting/uploadImage'
                });


            uploader.onSuccessItem = function (fileItem, response) {

                if (response.data) {
                    response.data.imageType = self.context.imageType.replace("image", "images");
                    fileItem.uploaded = true;
                    fileItem.message = null;
                    self.files.push(response.data);
                } else {
                    fileItem.message = "可能由于网络原因上传异常，请再试";
                    self.blockUI.stop();
                    //fileItem.uploaded = true;
                }

                if (self.files.length == self.vm.total) {
                    self.blockUI.stop();
                    self.uibModalInstance.close(self.files);
                }


            };

            uploader.onBeforeUploadItem = function (fileItem) {

                if (fileItem._file.size > 5 * 1024 * 1024) {
                    self.alert(fileItem._file.name + "的图片大小大于5M");
                    fileItem.message = fileItem._file.name + "的图片大小大于5M";
                    self.vm.reUpload = true;
                    self.blockUI.stop();
                    throw "图片过大错误!";
                }

                if (fileItem.uploaded)
                    throw "已上传!";

                var _idx = fileItem._idx;
                if (_idx > 0) {
                    var preItem = self.uploader.queue[_idx - 1];
                    if (preItem.message != null) {
                        self.blockUI.stop();
                        self.vm.reUpload = true;
                        fileItem.message = "等待上传！";
                        throw "上一张图片上传错误!";
                    }
                }
            };

            this.uploader = uploader;
        };

        ImageUploadCtl.prototype.upload = function () {
            var self = this;

            //记录要上传的总数
            self.vm.total = self.uploader.queue.length;

            if (self.uploader.queue.length != 0) {

                self.blockUI.start("图片上传中。。。请耐心等待！");

                var data = [{
                    "productId": self.context.productId,
                    "imageType": self.context.imageType
                }];

                angular.forEach(self.uploader.queue, function (item, index) {
                    item._idx = index;
                    item.formData = data;
                    item.upload();
                });

            }
        };

        return ImageUploadCtl;

    })());

});