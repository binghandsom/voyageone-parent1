/**
 * Created by linanbin on 15/12/7.
 */
define([
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {

    angularAMD.controller('popPromotionDetailCtl', function ($scope,promotionService, cartList,items,confirm,$translate) {

        $scope.promotion = {};
        $scope.tejiabao=false;
        $scope.cartList = cartList;

        $scope.initialize  = function () {
            if(items){
                $scope.promotion = angular.copy(items);
                if($scope.promotion.tejiabaoId != "0"){
                    $scope.tejiabao=true;
                }
            }else{
                $scope.promotion.tagList=[{"tagId":"","channelId":"","tagName":""}];
            }
        };
        $scope.addTag = function(){
            if($scope.promotion.tagList)
            {
                $scope.promotion.tagList.push({"tagId":"","channelId":"","tagName":""});
            }else{
                $scope.promotion.tagList=[{"tagId":"","channelId":"","tagName":""}];
            }

        };
        $scope.delTag = function(parent,node){
            confirm($translate.instant('TXT_MSG_DELETE_ITEM')).result
                .then(function () {
                    var index;
                    index=_.indexOf(parent,node);
                    if(index >-1 ){
                        parent.splice(index,1);
                    }
                });
        };
        $scope.ok = function(){

            if(!$scope.tejiabao){
                $scope.promotion.tejiabaoId = "0";
            }
            if(!$scope.promotionFrom.$valid || !$scope.promotion.tagList){
                return;
            }
            for(var i=0;i<$scope.promotion.tagList.length;i++){
                if($scope.promotion.tagList[i].tagName == ""){
                    return;
                }
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