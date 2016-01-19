/**
 * Created by linanbin on 15/12/7.
 */
define([
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {

    angularAMD.controller('popNewPromotionCtl', function ($scope,promotionService,cartList,items) {

        $scope.promotion = {};
        $scope.tejiabao=false;
        $scope.cartList = cartList;

        $scope.initialize  = function () {
            if(items){
                $scope.promotion = _.clone(items);
                if($scope.promotion.tejiabaoId != "0"){
                    $scope.tejiabao=true;
                }
            }
        }

        $scope.ok = function(){

            if(!$scope.tejiabao){
                $scope.promotion.tejiabaoId = "0";
            }
            if(!items) {
                promotionService.insertPromotion($scope.promotion).then(function (res) {

                    $scope.$close();
                }, function (res) {
                })
            }else{
                promotionService.updatePromotion($scope.promotion).then(function (res) {
                    for (key in $scope.promotion) {
                        items[key] = $scope.promotion[key];
                    }
                    $scope.$close();
                }, function (res) {
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