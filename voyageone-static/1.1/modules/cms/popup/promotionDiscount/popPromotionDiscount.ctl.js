/**
 * @Name:    popPromotionDiscountController
 * @Date:    2015/8/21
 *
 * @User:    Eric
 * @Version: 1.0.0
 */

define (function (require) {
    var cmsApp = require ('modules/cms/cms.module');
    require ('modules/cms/popup/promotionDiscount/popPromotionDiscount.service');

    cmsApp.controller ('popPromotionDiscountController',['$scope', 'popPromotionDiscountService', 'userService', '$modalInstance', 'productList' , 'promotionId', 'notify',
        function ($scope, popPromotionDiscountService, userService, $modalInstance, productList, promotionId, notify) {

            //关闭对话框
            $scope.close = closeDialog;
            $scope.productIdArray = [];
            $scope.productIdArray = productList;
            $scope.promotionId = promotionId;

            /**
             * 更新Promotion的Discount信息.
             */
            $scope.updatePromotionProduct =  function () {
                var data = {
                    productIdList:$scope.productIdArray,
                    promotionId:$scope.promotionId,
                    channelId:userService.getSelChannel(),
                    discountPercent:$scope.discountPercent
                };
                popPromotionDiscountService.doUpdatePromotionDiscount(data) .then (function () {
                    notify.success("CMS_TXT_MSG_UPDATE_SUCCESS");
                    $modalInstance.close("");
                })
            };

            /**
             * 关闭窗口，并初始化该页面输入内容.
             */
            function closeDialog() {
                $modalInstance.dismiss('close');
            }
        }])
});
