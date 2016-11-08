/**
 * @author piao
 * @description 智能上新操作历史
 * @version V2.9.0
 */

define([
    'cms'
],function(cms){

    cms.controller('intelApproveController',(function(){

        function IntelApproveController(context,$menuService){

            var self = this;
            self.context = context;
            self.$menuService = $menuService;
            //self.statusHistoryService = statusHistoryService;
            self.paging = {
                size: 10,
                fetch: function (pageNum, size) {
                    self.paging.size = size;
                    self.getPage(pageNum);
                }
            };
            self.code = context.code;
        }

        IntelApproveController.prototype.init = function(){
            var self = this,
                $menuService = self.$menuService,
                defaultSelectedCartId = self.context.cartId;

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

                //self.getPage(1);
            });
        };

        IntelApproveController.prototype.getPage = function (pageNumber) {

            var self = this;

/*            self.statusHistoryService.getPage({
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
                self.data = res.data.map(function (row) {
                    var cart = self.cartList.find(function (cart) {
                        return cart.value == row.cartId;
                    });

                    if (cart)
                        row.cartName = cart.label;

                    return row;
                });
                self.paging.total = res.count;
            });*/
        };

        return IntelApproveController;

    })());

});
