/**
 * Created by linanbin on 15/12/7.
 * Modified by gubuchun on 15/12/16.
 */
define([
    'angularAMD'
], function (angularAMD) {
    angularAMD.controller('popTagPromotionCtl', function ($scope, $tagPromotionService, promotion) {

        $scope.vm = {
            promotionInfo: {
                tagId: promotion.tagId,
                productIds: []
            },
            promotion: {},
            subPromotionList: []
        };

        $scope.initialize = function () {
            $scope.vm.promotion = promotion.promotion;
            $scope.vm.promotionInfo.productIds = promotion.productIds;

            $tagPromotionService.getPromotionTags($scope.vm.promotion).then(function (res) {
                $scope.vm.subPromotionList = res.data;
            });
        };

        $scope.ok = function () {
            $tagPromotionService.addToPromotion($scope.vm.promotionInfo).then(
                function () {
                    $scope.$close();
                })
        }

    });
});