/**
 * Created by linanbin on 15/12/7.
 */
define([
    'angularAMD',
    'modules/cms/controller/popup.ctl',
    'modules/cms/views/promotion/task/stock.ctl'
], function (angularAMD) {

    angularAMD.controller('popImageDetailAddCtl', function ($scope,FileUploader,confirm,alert,cActions,data,originUrl,imageGroupDetailService,notify,blockUI) {
        $scope.parent = data;
        $scope.key = originUrl;
        $scope.originUrl = originUrl;
        $scope.imageType = "2";

        $scope.vm={"messager":""};
        var uploader = $scope.uploader = new FileUploader({
            url: '/cms/channel/image_group_detail/saveUploadImage'
        });

        $scope.upload = function() {
            var uploadQueue = uploader.queue;
            var uploadItem = uploadQueue[uploadQueue.length - 1];
            if (!uploadItem &&  $scope.imageType == "1") {
                return alert('TXT_MSG_NO_UPLOAD');
            }
            var uploadIt = function () {
                this.uploadItem = uploadItem;
                uploadItem.onSuccess = function (res) {
                    blockUI.stop();
                    if (res.message) {
                        $scope.vm.messager = res.message;
                        alert(res.message);
                        return;
                    }
                    notify.success('TXT_MSG_INSERT_SUCCESS');
                    $scope.$close();
                    $scope.parent.search();
                };
                uploadItem.onError = function (res) {
                    blockUI.stop();
                    if (res.message) {
                        $scope.vm.messager = res.message;
                        alert(res.message);
                        return;
                    }
                    alert('TXT_MSG_INSERT_FAIL');
                };
                uploadItem.formData = [{
                    "key": $scope.key,
                    "originUrl": $scope.originUrl,
                    "imageType": $scope.imageType
                }];
                uploadItem.upload();
                $scope.vm.messager = "reading...";
                blockUI.start();
            };
            if ($scope.imageType == "1") {
                uploadIt();
            } else {
                imageGroupDetailService.saveImage({
                    "key": $scope.key,
                    "originUrl": $scope.originUrl,
                    "imageType": $scope.imageType
                }).then(function (res) {
                    notify.success('TXT_MSG_INSERT_SUCCESS');
                    $scope.$close();
                    $scope.parent.search();
                }, function (err) {
                    if (err.displayType == null) {
                        alert('TXT_MSG_INSERT_FAIL');
                    }
                })
            }
        }

        uploader.onProgressItem = function(fileItem, progress) {
        };
    })
});