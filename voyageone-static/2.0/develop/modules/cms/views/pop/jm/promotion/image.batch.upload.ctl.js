define([
    'cms',
    'modules/cms/enums/JmPromotionImage',
], function (cms, JmPromotionImage) {

    cms.controller('imageBatchUploadCtl', (function () {

        /**
         *  promotionId,jmPromotionId,brand 上传时通过extend 加入
         */
        var jmImageEntity = {
            pcEntrance: '',
            appEntrance: '',
            wxShare: '',
            channelFrontCover: '',
            channelMiddleImage: '',
            channelComingSoon: '',
            mobileFocus: '',
            pcHeader: '',
            appHeader: '',
            appChannelEntrance: '',
            appArcCarousel: '',
            appCarousel: '',
            pcCarousel: '',
            pcRow1Cell2: '',
            appRightBigCard: '',
            appLeftBigCard: '',
            appSmallCard: '',
            pcIndexPreheat: '',
            pcIndex: '',
            padCarousel: '',
            padPreheat: '',
            padCard: ''
        };

        function ImageBatchUploadCtl($uibModalInstance, FileUploader, blockUI, context) {
            this.$uibModalInstance = $uibModalInstance;
            this.FileUploader = FileUploader;
            this.blockUI = blockUI.instances.get('imgUpload');
            this.context = context;
            this.files = [];
            this.totols = 0;
        }

        ImageBatchUploadCtl.prototype.init = function () {
            var self = this,
                blockUI = self.blockUI,
                $uibModalInstance = self.$uibModalInstance,
                uploader = new self.FileUploader({
                    url: '/cms/pop/jmPromotion/batchUpload'
                });


            uploader.onSuccessItem = function (fileItem, response) {

                if(response.data){
                    self.files.push(response.data);
                }else
                    self.blockUI.stop();

                if(self.files.length == self.totols){
                    self.blockUI.stop();
                    $uibModalInstance.close();
                }

            };

            this.uploader = uploader;
        };

        ImageBatchUploadCtl.prototype.upload = function () {
            var self = this,
                context = self.context,
                blockUI = self.blockUI,
                data,
                commonData = {
                    promotionId: context.promotionId,
                    jmPromotionId: context.jmPromotionId,
                    brand: context.brand

                };

            if (context.imgUpEntity.jmPromotionId) {
                data = context.imgUpEntity;
            } else {
                data = jmImageEntity;
                _.extend(data,commonData);
            }

            angular.forEach(self.uploader.queue, function (item, index) {
                var filename = item.file.name.split(".")[0];

                var imageType = JmPromotionImage.getImageType(filename).attrName;
                data[imageType] = context.jmPromotionId + "-" + imageType;

            });

            blockUI.start("图片上传中。。。请耐心等待！");

            self.totols = self.uploader.queue.length;

            angular.forEach(self.uploader.queue, function (item, index) {
                var filename = item.file.name.split(".")[0];
                if ((index + 1) == self.uploader.queue.length) {
                    item.formData = [{
                        "promotionImages":JSON.stringify(data),
                        "promotionId": +commonData.jmPromotionId,
                        "imageType": JmPromotionImage.getImageType(filename).attrName
                    }];
                    item.upload();
                } else {
                    item.formData = [{
                        "promotionImages":null,
                        "promotionId": +commonData.jmPromotionId,
                        "imageType": JmPromotionImage.getImageType(filename).attrName
                    }];
                    item.upload();
                }

            });
        };

        return ImageBatchUploadCtl;

    })());

});