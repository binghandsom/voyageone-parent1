/**
 * Created by linanbin on 15/12/7.
 * Modified by gubuchun on 15/12/16.
 */
define([
    'angularAMD'
], function (angularAMD) {
    angularAMD.controller('popTagPromotionCtl', function ($scope, $tagPromotionService, $translate, $modalInstance, notify, promotion) {

        $scope.vm = {
            promotionInfo: {
                tagPath: null,
                productIds: promotion.productIds
            },
            promotion: promotion.promotion,
            subPromotionList: []
        };

        $scope.initialize = function () {

            $tagPromotionService.getPromotionTags($scope.vm.promotion).then(function (res) {
                $scope.vm.subPromotionList = res.data;
            });
        };

        $scope.ok = function () {
            $tagPromotionService.addToPromotion($scope.vm.promotionInfo).then(
                function () {
                    notify.success ($translate.instant('TXT_COM_UPDATE_SUCCESS'));
                    //$scope.$close();
                    $modalInstance.close('');
                })
        }

    });
});