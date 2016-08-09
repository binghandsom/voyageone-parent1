define([
    'vms'
], function (vms) {
    vms.controller('FeedFileUploadController', (function () {

        function FeedFileUploadController($scope, alert, notify, blockUI, FileUploader) {
            this.alert = alert;
            this.notify = notify;
            this.blockUI = blockUI;
            this.FileUploader = FileUploader;
            this.uploader = new FileUploader({
                url: "/vms/feed/file_upload/uploadFeedFile"
            });
        }

        FeedFileUploadController.prototype = {
            upload: function () {
                var self = this;
                var uploadQueue = self.uploader.queue;
                var uploadItem = uploadQueue[uploadQueue.length - 1];
                if (!uploadItem || document.all.file.value == '') {
                    return this.alert('TXT_MSG_NO_UPLOAD');
                }
                var uploadIt = function () {
                        uploadItem.onSuccess = function (res) {
                        self.blockUI.stop();
                        if (res.message) {
                            self.alert(res.message);
                            return;
                        }
                        self.notify.success('TXT_UPLOAD_FEED_FILE_SUCCESS');
                    };
                    uploadItem.onError = function (res) {
                        self.blockUI.stop();
                        self.alert('TXT_FAIL_TO_UPLOAD_FILE');
                    };
                    uploadItem.formData = [];
                    uploadItem.upload();
                    self.blockUI.start();
                };
                uploadIt();
            },

            download: function () {
                var main = this;
                $.download.post('/vms/feed/file_upload/downSampleFeedFile', {}, this.afterDownload, main);
            },

            afterDownload:function (responseContent, param, context) {
                var res = JSON.parse(responseContent);
                if (res.message != '') {
                    context.alert(res.message);
                }
            }

        };
        return FeedFileUploadController;

    }()));
});