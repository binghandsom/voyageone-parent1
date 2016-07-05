define([
    'cms', 'underscore'
], function (cms, _) {
    cms.controller('PriceLogPopupController', (function () {

        function PriceLogPopupController($menuService, priceLogService, context) {

            var self = this;

            self.skuList = context.skuList;
            self.code = context.code;
            self.selected = context.selected;

            $menuService.getPlatformType().then(function (res) {
                self.cartList = _.filter(res.data, function (item) {
                    return item.value >= 20 && item.value < 900;
                });
            }).then(function () {

            });
        }

        return PriceLogPopupController;

    }()));
});