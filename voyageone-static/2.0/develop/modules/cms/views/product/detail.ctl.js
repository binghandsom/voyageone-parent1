/**
 * @Description:
 *
 * @User: linanbin
 * @Version: 2.0.0, 15/12/24
 */

define([
    'cms',
    'underscore',
    'modules/cms/controller/popup.ctl',
    'modules/cms/service/product.detail.service',
    './jd.component.ctl',
    './feed.component.ctl',
    './master.component.ctl',
    './jgj.component.ctl'
], function (cms) {

    return cms.controller('productDetailController', (function () {

        function ProductDetailController($scope,$routeParams, $translate, menuService,productDetailService,confirm) {
            this.scope = $scope;
            this.routeParams = $routeParams;
            this.translate = $translate;
            this.menuService = menuService;
            this.productDetailService = productDetailService;
            this.confirm = confirm;
            this.defaultCartId = 0;
            this.platformTypes = null;
            this.cartData = {};
            this.product = {
                productId : $routeParams.productId,
                productDetails:null,
                translateStatus: 0,
                hsCodeStatus: 0,
                cartData:this.cartData,
                checkFlag:null,
                masterCategory:null,
                lockStatus:null,
                feedInfo:null
            };
        }

        ProductDetailController.prototype = {

            // 获取初始化数据
            initialize: function () {
                var self = this;
                self.menuService.getPlatformType().then(function(resp){
                    self.platformTypes = resp;
                    self.platformTypes.forEach(function(element){
                        self.cartData["_"+element.value] = element;
                    });
                });

                self.productDetailService.getProductInfo({productId: self.routeParams.productId}).then(function (res) {
                    self.product.productDetails = res.data;
                });

                this.defaultCartId =  this.routeParams.cartId != null ? this.routeParams.cartId:0;
            },
            cartIdFilter:function(item){
                return item.value > 20 && item.value < 900;
            },
            cartIdFilter2:function(item){
                return item.value >= 900;
            },
            lockProduct:function(domId){
                var self = this;
                var message = self.product.lockStatus ? "您确定要锁定商品吗？" : "您确定要解锁商品吗？";
                this.confirm(message).result.then(function () {
                    var lock = self.product.lockStatus ? "1" : "0";
                    self.productDetailService.updateLock({prodId:self.product.productId,lock:lock}).then(function(){
                        var notice = self.product.lockStatus ? "商品已锁定" : "商品已接触锁定";
                        $("#".concat(domId)).notify(notice, {className: "success",position:"top right"});
                    });

                },function(){
                    self.product.lockStatus = false;
                });

            }

        };

        return ProductDetailController
    })());
});