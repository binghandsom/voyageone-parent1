define([
    'cms', 'underscore'
], function (cms, _) {
    cms.controller('PriceLogPopupController', (function () {

        function PriceLogPopupController($menuService, priceLogService, context) {

            var self = this;
            var skuList = _.map(context.skuList, function (skuObj) {
                return {value: skuObj.skuCode};
            });
            var selected = context.selected;
            var defaultSku = {value: ''};

            skuList.unshift(defaultSku);
            if (!selected.sku)
                selected.sku = defaultSku;
            else
                selected.sku = _.find(skuList, function (sku) {
                    return sku.value === selected.sku;
                });

            self.skuList = skuList;
            self.code = context.code;
            self.selected = selected;
            self.priceLogService = priceLogService;

            self.paging = {
                size: 10,
                fetch: function () {
                    console.log(arguments);
                    self.getData();
                }
            };

            $menuService.getPlatformType().then(function (res) {
                self.cartList = _.filter(res.data, function (item) {
                    return item.value >= 20 && item.value < 900;
                });
            }).then(function () {
                self.getData(0);
            });
        }

        PriceLogPopupController.prototype.getData = function (pageIndex) {

            var self = this;
            var size = self.paging.size;

            self.priceLogService.page({
                sku: self.selected.sku.value,
                code: self.code,
                cart: self.selected.cart,
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