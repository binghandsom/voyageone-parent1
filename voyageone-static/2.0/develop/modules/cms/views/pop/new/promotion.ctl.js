/**
 * Created by linanbin on 15/12/7.
 */
define([
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {

    angularAMD.controller('popNewPromotionCtl', function ($scope,promotionService,items) {

        $scope.promotion = {};
        $scope.tejiabao=false;

        $scope.initialize  = function () {
            if(items){
                $scope.promotion = items
            }
        }

        $scope.ok = function(){

            if(!items) {
                promotionService.insertPromotion($scope.promotion).then(function (res) {
                    $scope.$close();
                }, function (res) {
                    alert("e");
                })
            }else{
                promotionService.updatePromotion($scope.promotion).then(function (res) {
                    $scope.$close();
                }, function (res) {
                    alert("e");
                })
            }
        }
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