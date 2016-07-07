define([
    'vms'
], function (vms) {
    vms.controller('FeedFileUploadController', (function () {

        function FeedFileUploadController($scope, alert, notify, blockUI, FileUploader) {
            this.alert = alert;
            this.notify = notify;
            this.blockUI = blockUI;
            this.uploadItem = null;
            this.uploader = new FileUploader({
                url: "/vms/feed/file_upload/importFeedFile"
            });
        }

        FeedFileUploadController.prototype = {
            upload: function () {
                var main = this;
                var uploadQueue = this.uploader.queue;
                var uploadItem = uploadQueue[uploadQueue.length - 1];
                if (!uploadItem) {
                    return this.alert('TXT_MSG_NO_UPLOAD');
                }
                var uploadIt = function () {
                    uploadItem.onSuccess = function (res) {
                        main.blockUI.stop();
                        if (res.message) {
                            alert(res.message);
                            return;
                        }
                        main.notify.success('TXT_MSG_UPLOAD_SUCCESS');
                    };
                    uploadItem.formData = [];
                    uploadItem.upload();
                    main.blockUI.start();
                };
                uploadIt();
            }

        };
        return FeedFileUploadController;

    }()));
});