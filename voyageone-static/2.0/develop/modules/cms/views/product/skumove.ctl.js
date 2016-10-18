/**
 * Created by sofia on 2016/10/10.
 */
define([
    'cms',
    'modules/cms/service/product.detail.service',
    'modules/cms/controller/popup.ctl'
], function (cms) {
    cms.controller('SkuMoveController', (function () {
        function SkuMoveController(productDetailService, notify, confirm, alert, popups) {
            this.sourceCode;
            this.skuList = [];
            this.skuListView;
            this.includeJM;
            this.destGroupType = "new";

            this.show = false;
            this.popups = popups;
            this.showView = false;

            this.productDetailService = productDetailService;
            this.notify = notify;
            this.confirm = confirm;
            this.alert = alert;
        }

        SkuMoveController.prototype = {
            init: function () {
                var self = this;
                var moveSkuInfo = JSON.parse(window.sessionStorage.getItem('moveSkuInfo'));
                if (moveSkuInfo) {
                    self.sourceCode = moveSkuInfo.sourceCode;
                    _.each(moveSkuInfo.skuList, function (sku) {
                        if(sku.isChecked) {
                            self.skuList.push(sku.skuCode);
                        }
                    });
                    self.skuListView = self.skuList.join(', ');
                }
                self.productDetailService.moveSkuInit({
                    sourceCode: self.sourceCode
                }).then(function (resp) {
                    self.includeJM = resp.data.includeJM;
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
                        item.value == 'select' ? self.show = true : self.show = false;
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
        return SkuMoveController;
    })())
});