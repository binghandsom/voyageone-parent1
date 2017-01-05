define([
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {
    angularAMD.controller('feedConfigSetController', function ($scope, FileUploader,$uibModalInstance) {
        var uploader = $scope.uploader = new FileUploader({
            url: '/cms/channel/feedConfig/import'
        });
        $scope.upload = function () {
            uploader.queue[0].upload();
        };
        uploader.onSuccessItem = function () {
            $uibModalInstance.close();
        };
    });
});