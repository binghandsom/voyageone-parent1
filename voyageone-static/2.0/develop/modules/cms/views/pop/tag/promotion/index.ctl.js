/**
 * Created by linanbin on 15/12/7.
 * Modified by gubuchun on 15/12/16.
 */
define([
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {
    angularAMD.controller('popTagPromotionCtl', function ($scope, $tagPromotionService, promotion) {

        $scope.vm = {
            "promotionInfo": {
                "tagId": null
            },
            "promotion": null,
            "subPromotionList": null,
            "groupList": [],
            "productList": []
        };

        $scope.initialize = function () {
            $scope.vm.promotion = promotion;

            $tagPromotionService.getPromotionTags(promotion).then(function (res) {
                $scope.vm.subPromotionList = res.data;
            });
        };

        $scope.ok = function () {
            $tagPromotionService.addToPromotion($scope.vm.promotion).then(
                function (res) {
                    $scope.$close();
                }, function (res) {
                    alert("e");
                })
        }

    });
});