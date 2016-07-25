define([
    'cms'
], function (cms) {
    cms.controller('ProductStatusPopupController', (function ProductStatusPopupControllerWrapper() {

        function ProductStatusPopupController(context, statusHistoryService, $menuService) {

            var self = this;
            var defaultSelectedCartId = context.cartId;

            self.statusHistoryService = statusHistoryService;

            self.paging = {
                size: 10,
                fetch: function (pageNum, size) {
                    self.paging.size = size;
                    self.getPage(pageNum);
                }
            };

            self.code = context.code;

            $menuService.getPlatformType().then(function (resp) {

                var cartList = _.filter(resp.data, function (item) {
                    return item.value >= 20;
                });
                var defaultCart = {value: '', label: 'Select...'};

                cartList = _.map(cartList, function (item) {
                    return {value: item.value, label: item.name};
                });

                cartList.unshift(defaultCart);

                if (!defaultSelectedCartId)
                    self.selectedCart = cartList[1];
                else
                    self.selectedCart = _.find(cartList, function (cart) {
                        return cart.value === defaultSelectedCartId;
                    });

                self.cartList = cartList;

                self.getPage(1);
            });
        }

        ProductStatusPopupController.prototype.getPage = function (pageNumber) {

            var self = this;

            self.statusHistoryService.getPage({
                pageIndex: pageNumber,
                pageRowCount: self.paging.size,
                orderBy: {
                    modified: "desc"
                },
                parameters: {
                    code: self.code,
                    cartId: self.selectedCart.value
                }
            }).then(function (resp) {
                var res = resp.data;
                self.data = res.data;
                self.paging.total = res.count;
            });
        };

        return ProductStatusPopupController;

    })());
});