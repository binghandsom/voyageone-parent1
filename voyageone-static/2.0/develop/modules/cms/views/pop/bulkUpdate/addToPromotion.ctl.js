/**
 * Created by linanbin on 15/12/7.
 * Modified by gubuchun on 15/12/16.
 */
define([
    'angularAMD'
], function (angularAMD) {
    angularAMD.controller('popAddToPromotionCtl', function ($scope, $addToPromotionService, $translate, $modalInstance, notify, context) {

        $scope.vm = {
            promotionInfo: {
                tagId: null,
                products: context.products,
                productIds: context.productIds,
                promotionId: context.promotion.id,
                cartId: context.promotion.cartId
            },
            promotion: context.promotion,
            subPromotionList: []
        };

        $scope.initialize = function () {

            $addToPromotionService.getPromotionTags($scope.vm.promotion).then(function (res) {
                $scope.vm.subPromotionList = res.data;
            });
        };

        $scope.ok = function () {
            if($scope.vm.promotionInfo.tagId) {
                $addToPromotionService.addToPromotion($scope.vm.promotionInfo).then(
                    function () {
                        notify.success ($translate.instant('TXT_MSG_UPDATE_SUCCESS'));
                        //$scope.$close();
                        $modalInstance.close('');
                    })
            }
        }

    });
});