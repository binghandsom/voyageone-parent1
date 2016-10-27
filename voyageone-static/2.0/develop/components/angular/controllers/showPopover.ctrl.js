/**
 * @Description:
 * 显示html的popover的共同方法
 * @User: linanbin
 * @Version: 2.0.0, 15/12/14
 */
angular.module("voyageone.angular.controllers").controller("showPopoverCtrl", function ($scope) {

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
        } else {
            tempHtml += values;
        }
        return tempHtml;
    }

    /**
     * 高级检索   显示sku
     */
    function popoverAdvanceSku(){
        var advanceSku = {};


       // advanceSku.test = "young king young boss";

        $scope.advanceSku = advanceSku;
    }

    /**
     * 高级线索   显示活动详情
     */
    function popoverPromotionDetail(){
        var promotionDetail = {};

        $scope.promotionDetail = promotionDetail;
    }
});
