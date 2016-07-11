define([
    'cms', 'underscore'
], function (cms, _) {
    cms.controller('PriceLogPopupController', (function () {

        function PriceLogPopupController($menuService, priceLogService, context, cActions) {

            var self = this;
            var skuList = _.map(context.skuList, function (skuObj) {
                return {value: skuObj.skuCode, label: skuObj.skuCode};
            });
            var selected = context.selected;
            var defaultSku = {value: '', label: 'Select...'};
            var serviceActions = cActions.cms.pop.priceLogService;

            skuList.unshift(defaultSku);
            if (!selected.sku)
                selected.sku = defaultSku;
            else
                selected.sku = _.find(skuList, function (sku) {
                    return sku.value === selected.sku;
                });

            self.exportAction = serviceActions.root + '/' + serviceActions.export;
            self.skuList = skuList;
            self.code = context.code;
            self.selected = selected;
            self.priceLogService = priceLogService;
            self.isOnlyShowMsrp = false;

            self.paging = {
                size: 10,
                fetch: function (pageNum, size) {
                    self.paging.size = size;
                    self.getData(pageNum - 1);
                }
            };

            $menuService.getPlatformType().then(function (res) {
                var cartList = _.filter(res.data, function (item) {
                    return item.value >= 20;
                });
                var defaultCart = {value: '', label: 'Select...'};

                cartList = _.map(cartList, function (item) {
                    return {value:item.value, label:item.name};
                });

                cartList.unshift(defaultCart);

                if (!selected.cart)
                    selected.cart = cartList[1];
                else
                    selected.cart = _.find(cartList, function (cart) {
                        return cart.value === selected.cart;
                    });

                self.cartList = cartList;
            }).then(function () {
                self.getData(0);
            });
        }

        PriceLogPopupController.prototype.getCartName = function (cartId) {

            var self = this;

            var cart = _.find(self.cartList, function (cart) {
                return cart.value == cartId;
            });

            return (!cart || !cart.value) ? "" : cart.label;
        };

        PriceLogPopupController.prototype.export = function () {

            var self = this;

            $.download.post(self.exportAction, {
                sku: self.selected.sku.value,
                code: self.code,
                cart: self.selected.cart.value
            });
        };

        PriceLogPopupController.prototype.reset = function () {

            var self = this;

            self.selected.sku = self.skuList[0];
            self.selected.cart = self.cartList[0];

            self.getData(0);
        };

        PriceLogPopupController.prototype.getData = function (pageIndex) {

            var self = this;
            var size = self.paging.size;

            self.priceLogService.page({
                sku: self.selected.sku.value,
                code: self.code,
                cart: self.selected.cart.value,
                offset: pageIndex * size,
                limit: size
            }).then(function (response) {

                var result = response.data;
                self.data = result.data;
                self.paging.total = result.count;

            });
        };

        return PriceLogPopupController;

    }()));
});