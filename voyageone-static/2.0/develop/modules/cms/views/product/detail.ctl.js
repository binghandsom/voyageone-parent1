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
    './tm.component.ctl',
    './feed.component.ctl',
    './master.component.ctl'
], function (cms) {

    return cms.controller('productDetailController', (function () {

        function ProductDetailController($routeParams, $translate, menuService) {

            this.routeParams = $routeParams;
            this.translate = $translate;
            this.menuService = menuService;
            this.defaultCartId = 0;
            this.platformTypes = null;
            this.cartData = {};
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

                this.defaultCartId =  this.routeParams.cartId != null ? this.routeParams.cartId:0;
            },
            cartIdFilter:function(item){
                return item.value > 23 && item.value < 900;
            }

        };

        return ProductDetailController
    })());
});