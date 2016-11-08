/**
 * @author piao
 * @description 智能上新操作历史
 * @version V2.9.0
 */

define([
    'cms'
],function(cms){

    cms.controller('intelApproveController',(function(){

        function IntelApproveController(context, $menuService, statusHistoryService){

            var self = this;
            self.context = context;
            self.$menuService = $menuService;
            self.statusHistoryService = statusHistoryService;
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
                $menuService = self.$menuService;

            $menuService.getPlatformType().then(function (resp) {

                var cartList = _.filter(resp.data, function (item) {
                    return item.value >= 20;
                });

                _.find(cartList, function (cart) {
                	if (cart.value === self.context.cartId) {
                		self.selectedCart = cart.name;
                		return;
                	}
                });

                self.getPage(1);
            });
        };

        IntelApproveController.prototype.getPage = function (pageNumber) {

            var self = this;

            self.statusHistoryService.getPage({
                pageIndex: pageNumber,
                pageRowCount: self.paging.size,
                orderBy: {
                    modified: "desc"
                },
                parameters: {
                    code: self.code,
                    cartId: self.context.cartId,
                    operationType: 13
                }
            }).then(function (resp) {
                var res = resp.data;
                self.data = res.data.map(function (row) {
                    row.cartName = self.selectedCart;
                    return row;
                });
                self.paging.total = res.count;
            });
        };

        return IntelApproveController;

    })());

});
