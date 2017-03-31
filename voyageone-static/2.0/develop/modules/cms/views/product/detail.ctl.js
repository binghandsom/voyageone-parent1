/**
 * @Description: 产品详情页主入口
 * @Version: 2.0.0, 15/12/24
 */

define([
    'cms',
    'modules/cms/controller/popup.ctl',
    'modules/cms/service/product.detail.service',
    './feed.component.ctl',
    './sku.component.ctl',
    './master.component.ctl',
    './price.component.ctl',
    './inventory.component.ctl',
    './subpage/tm.sub-page.ctl',
    './subpage/jd.sub-page.ctl',
    './subpage/jm.sub-page.ctl',
    './subpage/gt.sub-page.ctl',
    './subpage/dg.sub-page.ctl',
    './subpage/fx.sub-page.ctl',
    './subpage/lg.sub-page.ctl'
], function (cms) {

    return cms.controller('productDetailController', (function () {

        function ProductDetailController($scope, $routeParams, $translate, menuService, productDetailService, confirm) {
            this.scope = $scope;
            this.routeParams = $routeParams;
            this.translate = $translate;
            this.menuService = menuService;
            this.productDetailService = productDetailService;
            this.confirm = confirm;
            this.platformTypes = null;
            this.product = {
                productId: $routeParams.productId,
                masterField: null,
                translateStatus: 0,
                hsCodeStatus: 0,
                checkFlag: null,
                masterCategory: null,
                lockStatus: null,
                feedInfo: null,
                autoApprovePrice: null,
                productComm : null,
                skuBlock:null     //为了定位到平台详情页的sku区域
            };
        }

        /**获取初始化数据*/
        ProductDetailController.prototype.initialize = function () {
            var self = this,
                _cartObj = self.routeParams.cartId;

            // cartId:928【匠心界】,929【悦境】不显示
            self.menuService.getPlatformType().then(function (resp) {
                self.platformTypes = _.filter(resp, function (element) {
                    return element.value != 21 && element.value >= 20 && element.value <= 928;
                });
            });

            self.menuService.getCmsConfig().then(function (resp) {
                self.product.autoApprovePrice = resp.autoApprovePrice[0];
            });

            if(_cartObj){
                var strArr = _cartObj.split("|");

                if(strArr.length > 1){
                    self.defaultCartId = strArr[0];
                    self.product.skuBlock = true;
                }else
                    self.defaultCartId = _cartObj;
            }

        };

        /**锁定操作*/
        ProductDetailController.prototype.lockProduct = function (domId) {
            var self = this,lock,notice,
                message = self.product.lockStatus ? "您确定要锁定商品吗？" : "您确定要解锁商品吗？";

            this.confirm(message).then(function () {

                lock = self.product.lockStatus ? "1" : "0";

                self.productDetailService.updateLock({
                    prodId: self.product.productId,
                    lock: lock
                }).then(function () {
                    notice = self.product.lockStatus ? "商品已锁定" : "商品已接触锁定";
                    $("#".concat(domId)).notify(notice, {className: "success", position: "right"});
                });

            }, function () {
                self.product.lockStatus = false;
            });
        };

        return ProductDetailController

    })());
});