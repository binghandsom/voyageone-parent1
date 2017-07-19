/**
 * @author tony-piao
 * 京东 & 聚美 产品概述（schema）
 */
define([
    'cms',
    'modules/cms/enums/Carts',
    'modules/cms/service/product.detail.service',
    'modules/cms/directives/platFormStatus.directive',
    'modules/cms/directives/noticeTip.directive',
    'modules/cms/directives/contextMenu.directive'
], function (cms, carts) {

    const mConfig = {
        bigImageUrl: 'http://image.voyageone.com.cn/is/image/sneakerhead/✓?wid=2200&hei=2200',
        newImageUrl: 'http://image.voyageone.com.cn/is/image/sneakerhead/✓?fmt=jpg&scl=1&qlt=100'
    };


    class SnTabController{

        constructor(productDetailService, sizeChartService, $rootScope, systemCategoryService, alert, notify, confirm, $localStorage){

        }

        init(){
            console.log('init');
        }

    }

    cms.directive('snTab', function () {
        return {
            restrict: 'E',
            controller: ['productDetailService', 'sizeChartService', '$rootScope', 'systemCategoryService', 'alert', 'notify', 'confirm', '$localStorage', SnTabController],
            controllerAs: 'ctrl',
            scope: {
                productInfo: "=productInfo",
                cartInfo: "=cartInfo"
            },
            templateUrl: 'views/usa/product/detail/platform/sn-tab.tpl.html'
        }
    })
});
