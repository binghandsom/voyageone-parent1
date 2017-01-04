/**
 * @author rex.wu
 * @date 2016/11/30.
 */
define([
        "cms",
        "./combined-example-page",
        "modules/cms/controller/popup.ctl"
    ], function (cms) {
        cms.controller("CombinedProductEditController", (function () {

            function CombinedProductEditController($scope, context, combinedProductService, $compile, $templateRequest, $document) {
                $scope.vm = {
                    config : {
                        startSupplyChain:false
                    },
                    carts: {},
                    product: {}
                };
                $scope.vm.config.startSupplyChain = context.startSupplyChain == 1;
                $scope.vm.carts = context.carts;
                $scope.equalFlag = false;

                $scope.initialize = function () {
                    combinedProductService.getCombinedProductDetail(context.product).then(function (resp) {
                        $scope.vm.product = resp.data.product == null ? {} : resp.data.product;
                        if ($scope.vm.product != null) {
                            $scope.vm.product.cartId = $scope.vm.product.cartId + "";
                        }
                        _.each($scope.vm.product.skus, function (element) {
                            _.extend(element, {'tempSuitSellingPriceCn': element.suitSellingPriceCn}, {'tempSuitPreferentialPrice': element.suitPreferentialPrice})
                        });
                    });
                };


                $scope.getCombinedProductInfo = function () {
                    var cartId = $scope.vm.product.cartId,
                        numID = $scope.vm.product.numID;

                    if (!cartId || !numID) {
                        return;
                    }
                    combinedProductService.getCombinedProductPlatformDetail({
                        "cartId": cartId,
                        "numID": numID
                    }).then(function (resp) {
                        var product = angular.copy($scope.vm.product);
                        $scope.vm.product = resp.data.product == null ? {} : resp.data.product;
                        // carts集合中cart为string, product为int
                        $scope.vm.product.cartId = $scope.vm.product.cartId == null ? "" : $scope.vm.product.cartId + "";
                        $scope.vm.product._id=product._id;
                        _.each($scope.vm.product.skus, function (element) {
                            _.extend(element, {'tempSuitSellingPriceCn': element.suitSellingPriceCn}, {'tempSuitPreferentialPrice': element.suitPreferentialPrice})
                        });
                    });
                };

                $scope.getSkuDetail = function (sku, skuItem) {
                    if (!skuItem || !skuItem.skuCode) {
                        return;
                    }
                    combinedProductService.getSkuDetail({
                        "skuCode": skuItem.skuCode,
                        "cartId": $scope.vm.product.cartId
                    }).then(function (resp) {
                        _.extend(skuItem, resp.data.skuItem == null ? {} : resp.data.skuItem);
                        // 统计组合套装中国最终售价
                        dynamicSkuPrice(sku);
                    });
                };

                $scope.copySkuItem = function (skuItems) {
                    if (!skuItems || skuItems.length == 0)
                        return;

                    skuItems.push({});
                };

                $scope.deleteSkuItem = function (sku, index) {
                    if (!sku || !sku.skuItems || sku.skuItems.length <= 1) {
                        return;
                    }

                    sku.skuItems.splice(index, 1);
                    dynamicSkuPrice(sku);
                };

                function dynamicSkuPrice(sku) {
                    // 动态统计套装组合SKU【组合套装优惠售价 合计】
                    var tempSuitPreferentialPrice = 0;

                    if (!sku || !sku.skuItems || sku.skuItems.length == 0) {
                        tempSuitPreferentialPrice = 0;
                    } else {
                        _.each(sku.skuItems, function (element) {
                            tempSuitPreferentialPrice += element.preferentialPrice;
                        });
                    }
                    sku.warn = sku.suitPreferentialPrice != tempSuitPreferentialPrice;
                    $scope.equalFlag = sku.warn;
                    sku.tempSuitPreferentialPrice = tempSuitPreferentialPrice;
                    // 动态统计套装组合SKU【组合套装中国最终售价 合计】
                    var suitSellingPriceCn = 0;
                    _.each(sku.skuItems, function (item) {
                        suitSellingPriceCn += item.sellingPriceCn;
                    });
                    sku.tempSuitSkuCode = suitSellingPriceCn;

                }

                $scope.dynamicPrice = function (sku) {
                    dynamicSkuPrice(sku);
                };

                $scope.editSubmit = function (status) {
                    var cartId = $scope.vm.product.cartId;
                    var numId = $scope.vm.product.numID;
                    if (status == 0 && (!cartId || !numId)) { // 暂存
                        alert("组合套装商品暂存，请至少输入平台和商品编码！");
                        return;
                    }
                    combinedProductService.edit(_.extend($scope.vm.product, {"status": status})).then(function () {
                        $scope.$close();
                    });
                };

                /**
                 * 模板展示
                 * @type {{content: string, templateUrl: string, title: string}}
                 */
                $scope.popExamplePage = function () {
                    var body = $document[0].body;

                    $templateRequest('/modules/cms/views/pop/product/combined-example-page.html').then(function (html) {
                        var modal = $(html);
                        var modalChildScope = $scope.$new();

                        modal.appendTo(body);
                        $compile(modal)(modalChildScope);
                    });
                };

            }

            return CombinedProductEditController;

        })());
    }
);