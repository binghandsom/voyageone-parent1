/**
 * @author piao
 * @description 价格一览
 * @version V2.9.0
 */
define([
    'cms',
    'modules/cms/directives/platFormStatus.directive'
], function (cms) {

    function productPriceCtl($scope, $element, $attrs, $productDetailService, $rootScope, alert, notify, confirm) {
        var self = this;
        self.$scope = $scope;
        self.$element = $element;
        self.$attrs = $attrs;
        self.sales = {};
        self.vm = {
            selectSales: "codeSumAll",
            productPriceList: [],
            model: {},
            priceMsrp:"",
            priceSale:""
        };
        self.$productDetailService = $productDetailService;
        self.$rootScope = $rootScope;
        self.alert = alert;
        self.notify = notify;
        self.confirm = confirm;
        self.productInfo = self.$scope.productInfo;
    }

    productPriceCtl.prototype.init = function () {
        var self = this,
            productInfo = self.productInfo,
            vm = self.vm;

        self.$productDetailService.getProductPriceSales(productInfo.productId).then(function (resp) {
            vm.productPriceList = resp.data.productPriceList;
            vm.model = resp.data;
            self.sales = resp.data.sales;
            self.selectSalesOnChange();
            vm.productPriceList.forEach(function (element) {
                if (element.checked == 2) {
                    element.isSale = true;
                }
            });
        });
    };

    productPriceCtl.prototype.selectSalesOnChange = function () {
        var self = this,
            vm = self.vm,
            cartSales = self.sales[vm.selectSales];

        if (cartSales) {
            _.each(vm.productPriceList, function (element) {
                element.saleQty = cartSales["cartId" + element.cartId];
            });
        }
        else {
            _.each(vm.productPriceList, function (element) {
                element.saleQty = 0;
            });
        }
    };

    productPriceCtl.prototype.isSaleOnChange = function (item) {
        var _state = item.isSale,
            self = this,
            productInfo = self.productInfo;

        self.$productDetailService.setCartSkuIsSale({
            prodId: productInfo.productId,
            cartId: item.cartId,
            isSale: item.isSale
        }).then(function () {
            notify.success("更新成功 ！");
            //更新平台页面
            productInfo.masterCategory = new Date().getTime();
        }, function () {
            item.isSale = !_state;
        });

    };

    productPriceCtl.prototype.calculateCartMsrpClick = function () {
        var self = this,
            vm = self.vm,
            productInfo = self.productInfo;

        self.$productDetailService.getCalculateCartMsrp(productInfo.productId).then(function (resp) {
            _.each(vm.productPriceList, function (element) {
                var msrpInfo = _.find(resp.data, function (d) {
                    return d.cartId == element.cartId
                });
                if (msrpInfo && element.autoSyncPriceMsrp != "1") {
                    element.priceMsrp = msrpInfo.msrp;
                }
            });

        });
    };

    productPriceCtl.prototype.saveCartSkuPriceClick = function (item) {
        var self = this,
            upEntity = {
                prodId: self.productInfo.productId,
                cartId: item.cartId
            };

        if (item.priceMsrp)
            upEntity.priceMsrp = item.priceMsrp;//中国建议售价

        if (item.priceSale)
            upEntity.priceSale = item.priceSale;//中国最终售价

        if (item.autoSyncPriceMsrp == "2" && (item.priceMsrp < item.priceSale || item.priceMsrp < item.priceSaleEd)) {
            self.confirm("建议售价不能低于指导价和最终售价，是否强制保存？").then(function () {
                self.saveCartSkuPrice(upEntity, item);
            });
        } else {
            self.saveCartSkuPrice(upEntity, item);
        }
    };

    productPriceCtl.prototype.saveCartSkuPrice = function (para, item) {
        var self = this;

        self.$productDetailService.saveCartSkuPrice(para).then(function () {

            if (para.priceMsrp > 0) {
                item.priceMsrpSt = para.priceMsrp;
                item.priceMsrpEd = para.priceMsrp;
            }

            if (para.priceSale > 0) {
                item.priceSaleSt = para.priceSale;
                item.priceSaleEd = para.priceSale;
            }

            item.priceMsrp = undefined;
            item.priceSale = undefined;

            //更新页面数据
            self.init();
            //更新子页面数据
            self.$scope.productInfo.masterCategory = new Date().getTime();

            self.notify.success("保存成功")

        });
    };
    
    productPriceCtl.prototype.setAllCartPrice = function () {
        var self = this;
        var priceMsrp = self.vm.priceMsrp;
        var priceSale = self.vm.priceSale;
        if (priceMsrp > 0 && priceSale > 0) {

            self.vm.productPriceList.forEach(function (element) {
                element.priceMsrp = priceMsrp;
                element.priceSale = priceSale;
            });
        }
    };

    cms.directive("priceSchema", function ($productDetailService, $rootScope, alert, notify, confirm) {
        return {
            restrict: "E",
            controller: ['$scope', '$element', '$attrs', '$productDetailService', '$rootScope', 'alert', 'notify', 'confirm', productPriceCtl],
            controllerAs: 'ctrl',
            templateUrl: "views/product/price.component.tpl.html",
            scope: {
                productInfo: "=productInfo"
            }
        };
    });
});