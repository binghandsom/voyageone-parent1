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
            this.codeList = [];
            this.refCode;
            this.searchCode;

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
                self.productDetailService.moveSkuSearch({
                    searchCode: self.searchCode,
                    sourceCode: self.sourceCode
                }).then(function (resp) {
                    self.codeList = resp.data.codeList;
                    self.refCode = "";
                });
            },

            selRefCode: function (productInfo) {
                var self = this;
                self.refCode = productInfo.common.fields.code;
            },

            openImageDetail : function (item) {
                var self = this;
                if (item.common == undefined || item.common.fields == undefined) {
                    return;
                }
                var picList = [];
                for (var attr in item.common.fields) {
                    if (attr.indexOf("images") >= 0) {
                        var image = _.map(item.common.fields[attr], function (entity) {
                            var imageKeyName = "image" + attr.substring(6, 7);
                            return entity[imageKeyName] != null ? entity[imageKeyName] : "";
                        });
                        picList.push(image);
                    }
                }
                self.popups.openImagedetail({'mainPic': picList[0][0], 'picList': picList, 'search': 'master'});
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