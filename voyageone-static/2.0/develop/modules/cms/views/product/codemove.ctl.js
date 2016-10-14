/**
 * Created by sofia on 2016/10/13.
 */
define([
    'cms',
    'modules/cms/service/product.detail.service',
    'modules/cms/controller/popup.ctl'
], function (cms) {
    cms.controller('CodeMoveController', (function () {
        function CodeMoveController(productDetailService, notify, confirm, alert, popups) {
            this.cartId;
            this.cartName;
            this.prodId;
            this.productCode;
            this.sourceGroupId;
            this.sourceGroupName;

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
                self.productDetailService.moveCodeInit({
                    productCode: self.productCode,
                    cartId: self.cartId
                }).then(function (resp) {
                    self.sourceGroupId = resp.data.sourceGroupId;
                    self.sourceGroupName = resp.data.sourceGroupName;
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

        // CodeMoveController.$inject = ['productDetailService', 'notify','confirm', 'alert'];
        return CodeMoveController;
    })())
});