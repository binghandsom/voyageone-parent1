/**
 * Created by linanbin on 15/12/7.
 */
define([
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {

    angularAMD.controller('popFieldFeedDetailCtl', function () {

        //$scope.vm={"messager":""};
        //var uploader = $scope.uploader = new FileUploader({
        //    url: '/cms/promotion/detail/uploadPromotion'
        //});
        //
        //$scope.initialize  = function () {
        //
        //}
        //$scope.upload = function(){
        //    uploader.queue[0].formData = [{"promotionId":data}];
        //    uploader.queue[0].upload();
        //    $scope.vm.messager ="读入中";
        //}
        //
        //uploader.onProgressItem = function(fileItem, progress) {
        //    console.info('onProgressItem', fileItem, progress);
        //};
        //
        //uploader.onSuccessItem = function(fileItem, response, status, headers) {
        //    console.info('onSuccessItem', fileItem, response, status, headers);
        //    $scope.vm.messager ="读入完毕！成功"+response.data.succeed.length +"条  失败"+response.data.fail.length +"条";
        //};
    });

    //return function ($scope,promotionService) {
    //
    //    $scope.promotion = {};
    //    $scope.name = "123";
    //
    //    $scope.initialize  = function () {
    //        alert("a");
    //    }
    //
    //    $scope.ok = function(){
    //        alert("e");
    //    }
    //
    //};
});