/**
 * @Description:
 *
 * @User: linanbin
 * @Version: 2.0.0, 15/12/24
 */

define([
    'cms',
    'modules/cms/controller/popup.ctl',
    'modules/cms/service/product.detail.service',
    './jd.component.ctl',
    './feed.component.ctl',
    './sku.component.ctl',
    './master.component.ctl',
    './jgj.component.ctl',
    './gw.component.ctl',
    './dl.component.ctl',
    './price.component.ctl',
    './dl.component.ctl',
    './inventory.component.ctl',
    './jd.sub-page.ctl',
    './jm.sub-page.ctl',
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

            self.menuService.getPlatformType().then(function (resp) {
                self.platformTypes = _.filter(resp, function (element) {
                    return element.value >= 20;
                });
            });

            self.menuService.getCmsConfig().then(function (resp) {
                self.product.autoApprovePrice = resp.autoApprovePrice[0];
            });

/*            if(_cartObj){
                var strArr = _cartObj.split("|");

                if(strArr.length > 1){
                    self.defaultCartId = strArr[0];
                    self.product.skuBlock = true;
                }else
                    self.defaultCartId = _cartObj;
            }*/

            self.defaultCartId = 27;

        };

        /**锁定操作*/
        ProductDetailController.prototype.lockProduct = function (domId) {
            var self = this,
                message = self.product.lockStatus ? "您确定要锁定商品吗？" : "您确定要解锁商品吗？";

            this.confirm(message).then(function () {

                var lock = self.product.lockStatus ? "1" : "0";

                self.productDetailService.updateLock({
                    prodId: self.product.productId,
                    lock: lock
                }).then(function () {
                    var notice = self.product.lockStatus ? "商品已锁定" : "商品已接触锁定";
                    $("#".concat(domId)).notify(notice, {className: "success", position: "right"});
                });

            }, function () {
                self.product.lockStatus = false;
            });
        };

        return ProductDetailController

    })());
});