/**
 * @Description:
 *
 * @User: linanbin
 * @Version: 2.0.0, 15/12/24
 */

define([
    'cms',
    'underscore',
    'modules/cms/enums/Status',
    'modules/cms/enums/FieldTypes',
    'modules/cms/controller/popup.ctl',
    'modules/cms/service/product.detail.service',
    './jd.component.ctl',
    './tm.component.ctl',
    './feed.component.ctl'
], function (cms, _, Status, FieldTypes) {

    return cms.controller('productDetailController', (function () {

        function ProductDetailController($routeParams, $rootScope, $translate, productDetailService, feedMappingService, notify, confirm, alert , menuService) {

            this.routeParams = $routeParams;
            this.translate = $translate;
            this.$rootScope = $rootScope;
            this.productDetailService = productDetailService;
            this.feedMappingService = feedMappingService;
            this.notify = notify;
            this.confirm = confirm;
            this.alert = alert;
            this.menuService = menuService;
            this.platformTypes = null;
        }

        ProductDetailController.prototype = {

            // 获取初始化数据
            initialize: function () {
                var self = this;
                self.menuService.getPlatformType().then(function(resp){
                    self.platformTypes = resp;
                });
            },
            cartIdFilter:function(item){
                return item.value > 23;
            }

        };

        return ProductDetailController
    })());
});