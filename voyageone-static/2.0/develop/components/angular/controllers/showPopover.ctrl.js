/**
 * @Description:
 * 显示html的popover的共同方法
 * @User: linanbin
 * @Version: 2.0.0, 15/12/14
 */
angular.module("voyageone.angular.controllers").controller("showPopoverCtrl", function ($scope,$searchAdvanceService2,$promotionHistoryService) {

    $scope.templateAction = {
        "promotionDetailPopover":{
            templateUrl: 'promotionDetailTemplate.html',
            title: 'Title'
        },
        "advanceSkuPopover":{
            templateUrl: 'advanceSkuTemplate.html',
            title: 'Title'
        }
    };

    $scope.showInfo = showInfo;
    $scope.popoverAdvanceSku = popoverAdvanceSku;
    $scope.popoverPromotionDetail = popoverPromotionDetail;
    $scope.getCartQty = getCartQty;

    function showInfo(values) {
        if (values == undefined || values == '') {
            return '';
        }
        var tempHtml = "";
        if (values instanceof Array) {
            angular.forEach(values, function (data, index) {
                tempHtml += data;
                if (index !== values.length) {
                    tempHtml += "<br>";
                }
            });
        }
        else if(values.isUseComplexTemplate == true){
            $scope.dynamicPopover = {
                type: values.type,
                value1: values.value,
                value2: values.value2,
                value3: values.value3,
                templateUrl: 'dynamicPopoverTemplate.html'
            };
        }else {
            tempHtml += values;
        }
        return tempHtml;
    }

    /**
     * 高级检索   显示sku
     */
    function popoverAdvanceSku(code, skus , entity){

        if(entity.isOpen){
            entity.isOpen = false;
            return;
        }
        entity.isOpen = true;

        $searchAdvanceService2.getSkuInventory(code).then(function(resp) {
            var skuDetails = [],
                skuInventories = resp.data;
            _.forEach(skus, function(sku) {
                var inventory = null;
                _.forEach(skuInventories, function(skuInventory) {
                    if (skuInventory.sku.toLowerCase() == sku.skuCode.toLowerCase()) {
                        inventory = skuInventory.qtyChina;
                        return false;
                    }
                });
                skuDetails.push({
                    skuCode: sku.skuCode,
                    size: sku.size,
                    inventory: inventory
                });
            });

            $scope.advanceSku = skuDetails;
        });

    }

    /**
     * 高级线索   显示活动详情
     */
    function popoverPromotionDetail(code,entity){

        if(entity.isOpen){
            entity.isOpen = false;
            return;
        }
        entity.isOpen = true;

        $promotionHistoryService.getUnduePromotion({code: code}).then(function(resp) {
            $scope.promotionDetail = resp.data;
        });

    }

    /**
     * code对应平台的库存
     * @param code
     * @param codeCartQty
     */
    function getCartQty(codeCartQty){
        $scope.codeCartQty = codeCartQty;
    }
});
