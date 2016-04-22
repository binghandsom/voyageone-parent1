define([
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {
    angularAMD.controller('popPromotionDetailImportCtl', function ($scope, FileUploader, promotionDetailService, context) {
        var parentModel = context;
        $scope.vm = {"messager": ""};
        var uploader = $scope.uploader = new FileUploader({
            url: '/cms/CmsBtJmPromotionImportTask/index/upload'
        });
        $scope.initialize = function () {

        };
        $scope.upload = function () {
            uploader.queue[0].formData = [{"promotionId": parentModel.id}];
            uploader.queue[0].upload();
            $scope.vm.messager = "读入中";
        };
        uploader.onProgressItem = function (fileItem, progress) {
            //console.info('onProgressItem', fileItem, progress);
        };
        uploader.onSuccessItem = function (fileItem, response, status, headers) {
            //console.info('onSuccessItem', fileItem, response, status, headers);
            if (response.data) {
                if (response.data.result) {
                    $scope.$close();
                }
                else {
                    $scope.vm.messager = response.data.msg;
                }
            } else {
                $scope.vm.messager = response.code;
            }
        };
    });
});