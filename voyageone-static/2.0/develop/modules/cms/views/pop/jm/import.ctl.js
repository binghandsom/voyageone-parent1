define([
    'angularAMD',
    '../../../controller/popup.ctl.js'
], function (angularAMD) {
    angularAMD.controller('popPromotionDetailImportCtl', function ($scope,FileUploader,promotionDetailService,context) {
        var parentModel=context;
        $scope.vm={"messager":""};
        var uploader = $scope.uploader = new FileUploader({
            url: '/cms/promotion/detail/uploadPromotion'
        });
        $scope.initialize  = function () {

        }
        $scope.upload = function() {
            uploader.queue[0].formData = [{"promotionId": parentModel.id}];
            uploader.queue[0].upload();
            $scope.vm.messager = "读入中";
        }

uploader.onProgressItem = function(fileItem, progress) {
    console.info('onProgressItem', fileItem, progress);
};
uploader.onSuccessItem = function(fileItem, response, status, headers) {
    console.info('onSuccessItem', fileItem, response, status, headers);
    if(response.data){
        if(response.data.fail.length == 0){
            $scope.$close();
        }
        $scope.vm.messager ="读入完毕！成功"+response.data.succeed.length +"条  失败"+response.data.fail.length +"条";
    }else{
        $scope.vm.messager =response.code;
    }
};
});
});