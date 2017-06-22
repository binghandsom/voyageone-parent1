/**
 * Created by linanbin on 15/12/7.
 */
define([
    'cms',
    'modules/cms/controller/popup.ctl',
    'modules/cms/views/promotion/task/stock.ctl'
], function (cms) {

    cms.controller('popImageDetailAddCtl', function (data, originUrl, $scope, FileUploader, alert, imageGroupDetailService, notify, blockUI, $compile) {
        $scope.parent = data;
        $scope.imageGroupId = data.imageGroupId;
        $scope.imageType = data.imageType;
        $scope.key = originUrl;
        $scope.originUrl = originUrl;
        $scope.uploadType = "2";
        $scope.disableText = false;
        $scope.uploader = new FileUploader({
            url: '/cms/channel/image_group_detail/saveUploadImage'
        });
        $scope.upload = upload;

        function upload() {
            var uploader = $scope.uploader;
            var uploadQueue = uploader.queue;
            var uploadItem = uploadQueue[uploadQueue.length - 1];
            if (!uploadItem && $scope.uploadType === "1") {
                return alert('TXT_MSG_NO_UPLOAD');
            }

            var uploadIt = function () {
                this.uploadItem = uploadItem;
                uploadItem.onSuccess = function (res) {
                    blockUI.stop();
                    if (res.message) {
                        alert(res.message);
                        return;
                    }
                    notify.success('TXT_MSG_UPDATE_SUCCESS');
                    $scope.$close();
                    $scope.parent.search();
                };
                uploadItem.onError = function (res) {
                    blockUI.stop();
                    if (res.message) {
                        alert(res.message);
                        return;
                    }
                    alert('TXT_MSG_UPDATE_FAIL');
                };
                uploadItem.formData = [{
                    "imageGroupId": $scope.imageGroupId,
                    "key": $scope.key,
                    "originUrl": $scope.originUrl,
                    "imageType": $scope.imageType,
                    "uploadType": $scope.uploadType
                }];
                uploadItem.upload();
                blockUI.start();
            };
            if ($scope.uploadType === "1") {
                uploadIt();
            } else {
                imageGroupDetailService.saveImage({
                    "imageGroupId": $scope.imageGroupId,
                    "key": $scope.key,
                    "originUrl": $scope.originUrl,
                    "imageType": $scope.imageType,
                    "uploadType": $scope.uploadType
                }).then(function () {
                    notify.success('TXT_MSG_UPDATE_SUCCESS');
                    $scope.$close();
                    $scope.parent.search();
                }, function (err) {
                    if (err.displayType === null) {
                        alert('TXT_MSG_UPDATE_FAIL');
                    }
                })
            }
        }

        /**
         * 切换图片上传方式
         */
        $scope.choseImg = function () {
            var type = $scope.uploadType,
                $ = angular.element;

            var _button = $('<input type="file" id="uploader" nv-file-select="" uploader="uploader" accept=".jpg">');

            $('#uploader').parent().html($compile(_button)($scope));

            var show = type === '1';

            $('#uploader').filestyle({
                buttonText: ' &nbsp;浏览文件',
                disabled: !show
            });

            $scope.disableText = show;

            //选中图片
            if(show){
                $scope.uploader.clearQueue();
                $scope.originUrl = null;
            }

        }
    })
});