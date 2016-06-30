/**
 * Created by linanbin on 15/12/7.
 * Modified by gubuchun on 15/12/16.
 */
define([
    'angularAMD'
], function (angularAMD) {
    angularAMD.controller('popAddToPromotionCtl', function ($scope, $addToPromotionService, $translate, $modalInstance, confirm, notify, context) {

        $scope.vm = {
            promotionInfo: {
                tagId: null,
                productIds: context.productIds,
                promotionId: context.promotion.id,
                cartId: context.promotion.cartId,
                isSelAll: context.isSelAll
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
            if ($scope.vm.promotionInfo.tagId) {
                // 先确认该产品是否已存在同级别的promotion tag
                $addToPromotionService.checkPromotionTags($scope.vm.promotionInfo).then(function (res) {
                    if (res.data.hasTags) {
                        confirm("下列商品已存在同级别的promotion tag，请确认是否覆盖现有tag?<br><br>商品CODE：" + res.data.prodCodeListStr).result.then(function () {
                            $addToPromotionService.addToPromotion($scope.vm.promotionInfo).then(
                                function () {
                                    notify.success ($translate.instant('TXT_MSG_UPDATE_SUCCESS'));
                                    $modalInstance.close('');
                                });
                        });
                    } else {
                        $addToPromotionService.addToPromotion($scope.vm.promotionInfo).then(
                            function () {
                                notify.success ($translate.instant('TXT_MSG_UPDATE_SUCCESS'));
                                $modalInstance.close('');
                            });
                    }
                });
            }
        }
    });
});