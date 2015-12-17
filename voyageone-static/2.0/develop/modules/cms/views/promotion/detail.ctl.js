/**
 * Created by linanbin on 15/12/7.
 */

define([
    'modules/cms/controller/popup.ctl'
], function () {

    return function ($scope,promotionService,promotionDetailService,$routeParams) {
        pageSize = 5;
        $scope.vm={"promotion":{},"groupList":[]};
        $scope.initialize  = function () {
            promotionService.getPromotionList({"promotionId":$routeParams.promotionId}).then(function (res){
                $scope.vm.promotion = res.data[0];
            },function(err){

            });
            $scope.search(1)

        }

        $scope.search = function(pageNo){
            promotionDetailService.getPromotionGroup({"promotionId":$routeParams.promotionId,"start":(pageNo-1)*pageSize,"length":pageSize}).then(function (res){
                $scope.vm.groupList = res.data;
            },function(err){

            })
        }
    };
});
