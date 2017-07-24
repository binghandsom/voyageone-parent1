/**
 * @description 美国产品详情页面
 * @author piao
 */
define([
    'cms',
    'modules/cms/controller/popup.ctl',
    './platform/sn-tab.directive',
    './platform/us-tab.directive',
    './inventory/inventory.directive.ctl',
    './price/price.directive.ctl'
],function (cms) {

    cms.controller('usProductDetailController',class UsProductDetailController{

        constructor($scope, $routeParams, $translate, menuService, confirm){
            this.scope = $scope;
            this.routeParams = $routeParams;
            this.translate = $translate;
            this.menuService = menuService;
            this.confirm = confirm;
            this.platformTypes = null;
            this.product = {
                productId: $routeParams.prodId,
                masterField: null,
                translateStatus: 0,
                hsCodeStatus: 0,
                checkFlag: null,
                masterCategory: null,
                lockStatus: null,
                feedInfo: null,
                autoApprovePrice: null,
                productComm: null
            };
            this.platformStatus = [
                {status:'Pending',display:'Pending'},
                {status:'OnSale', display:'List'},
                {status:'InStock', display:'Delist'}
            ]
        }

        init(){
            let self = this,
                _cartObj = self.routeParams.cartId;

            self.menuService.getPlatformType().then(function (resp) {

                self.platformTypes = _.filter(resp, function (element) {
                    return element.value < 20 && element.value != 0 ;
                });
            });

            self.menuService.getCmsConfig().then(function (resp) {
                self.product.autoApprovePrice = resp.autoApprovePrice[0];
            });

            if (_cartObj) {
                let strArr = _cartObj.split("|");

                if (strArr.length > 1) {
                    self.defaultCartId = strArr[0];
                    self.product.skuBlock = true;
                } else
                    self.defaultCartId = _cartObj;
            }
        }

    });

});