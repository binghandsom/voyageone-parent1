/**
 * Created by sofia on 2016/10/13.
 */
define([
    'cms',
    'modules/cms/controller/popup.ctl'
], function (cms) {
    cms.controller('CodeMoveController', (function (productDetailService, notify, confirm, alert) {
        function CodeMoveController(popups) {
            this.cartId;
            this.cartName;
            this.prodId;
            this.productCode;

            this.show = false;
            this.popups = popups;
            this.showView = false;

            this.productDetailService = productDetailService;
            this.notify = notify;
            this.confirm = confirm;
            this.alert = alert;
        }

        CodeMoveController.prototype = {
            init: function () {
                var self = this;
                var moveCodeInfo = JSON.parse(window.sessionStorage.getItem('moveCodeInfo'));
                if (moveCodeInfo) {
                    self.cartId = moveCodeInfo.cartId;
                    self.cartName = moveCodeInfo.cartName;
                    self.productCode = moveCodeInfo.productCode;
                }
                productDetailService.moveCodeInit({
                    prodId: scope.productInfo.productId
                }).then(function (resp) {
                    scope.skuList = resp.data.skuList;
                });
            },
            search: function () {
                var self = this;
                console.log(self.type);
            },
            ifShow: function (item) {
                var self = this;
                switch (item.type) {
                    case 'selectGroup':
                        item.value == 1 ? self.show = true : self.show = false;
                        break;
                    case 'buildView':
                        self.showView = true;
                }

            },
            move: function (type) {
                var self = this;
                self.popups.openSKUMoveConfirm(type);
            }

        };
        return CodeMoveController;
    })())
});