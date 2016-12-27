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

                // 初始化，加载组合商品详情
                $scope.initialize = function () {
                    combinedProductService.getCombinedProductDetail(context.product).then(function (resp) {
                        if (resp.data.product == null) {
                            alert("查询不到组合商品信息！");
                        } else {
                            $scope.vm.product = resp.data.product;
                            $scope.vm.product.cartId = $scope.vm.product.cartId + "";
                            _.each($scope.vm.product.skus, function (skuBean) {
                                skuBean.tempSuitSellingPriceCn = skuBean.suitSellingPriceCn;
                                skuBean.tempSuitPreferentialPrice = skuBean.suitPreferentialPrice;
                            });
                        }
                    });
                };

                // 从平台获取组合商品详情
                $scope.getCombinedProductInfo = function () {
                    var cartId = $scope.vm.product.cartId;
                    var numID = $scope.vm.product.numID;

                    if (!cartId || !numID) {
                        alert("请确认平台和商品编码是否填写完整！");
                        return;
                    }

                    combinedProductService.getCombinedProductPlatformDetail({
                            "cartId": cartId,
                            "numID": numID
                        }).then(function (resp) {
                        $scope.vm.config.showFlag = true;
                        $scope.vm.product = resp.data.product == null ? {} : resp.data.product;
                        $scope.vm.product.cartId = $scope.vm.product.cartId == null ? "" : $scope.vm.product.cartId + "";
                        if(resp.data.product != null) {
                            // 记录此套装中sku
                            var skuCodes = new Array();
                            _.each($scope.vm.product.skus, function (skuBean, index, list) {
                                _.each(skuBean.skuItems, function (skuItem, index, list) {
                                    skuCodes.push(skuItem.skuCode);
                                });
                            });
                            var parameter = {};
                            parameter.skuCodes = skuCodes;
                            parameter.cartId = $scope.vm.product.cartId;
                            combinedProductService.batchGetSkuDetail(parameter).then(function (response) {
                                var skuItems = response.data == null ? [] : response.data;
                                _.each($scope.vm.product.skus, function (skuBean, index, list) {
                                    _.each(skuBean.skuItems, function (skuItem, index, list) {
                                        var targetSkuItem = _.find(skuItems, function (target) {
                                            return target.skuCode == skuItem.skuCode;
                                        });
                                        if (!targetSkuItem) {
                                            alert("SKU:" + skuItem.skuCode + "查询不到具体信息！");
                                        }else {
                                            skuItem.code = targetSkuItem.code;
                                            skuItem.productName = targetSkuItem.productName;
                                            skuItem.sellingPriceCn = targetSkuItem.sellingPriceCn;
                                        }
                                    });
                                    dynamicSkuPrice(skuBean);
                                });
                            });
                        }

                    });
                };

                function dynamicSkuPrice(sku) {
                    // 动态统计套装组合SKU【组合套装优惠售价 合计】
                    var tempSuitPreferentialPrice = 0,
                        tempSuitSellingPriceCn = 0;
                    /*if (!sku || !sku.skuItems || sku.skuItems.length == 0) {*/
                    if (sku && sku.skuItems && sku.skuItems.length > 0) {
                        _.each(sku.skuItems, function (skuItem) {
                            tempSuitPreferentialPrice += skuItem.preferentialPrice;
                            tempSuitSellingPriceCn += skuItem.sellingPriceCn;
                        });
                    }
                    sku.warn = sku.suitPreferentialPrice != tempSuitPreferentialPrice;
                    sku.tempSuitPreferentialPrice = tempSuitPreferentialPrice;
                    sku.tempSuitSellingPriceCn = tempSuitSellingPriceCn;
                    // 每次sku.warn改变后，判断所有SKU的warn值，来决定提交按钮是否disabled
                    var flag = false;
                    _.each($scope.vm.product.skus, function (skuBean) {
                        flag = flag || sku.warn;
                    });
                    $scope.equalFlag = flag;
                }

                // 新增实际SKU
                $scope.copySkuItem = function (skuItems) {
                    if (!skuItems || skuItems.length == 0) {
                        return;
                    }
                    skuItems.push({});
                };
                // 删减实际SKU
                $scope.deleteSkuItem = function (sku, index) {
                    if (!sku || !sku.skuItems || sku.skuItems.length <= 1) {
                        return;
                    }
                    sku.skuItems.splice(index, 1);
                    dynamicSkuPrice(sku);
                };

                // 根据skuCode获取SKU详情
                $scope.getSkuDetail = function (sku, skuItem) {
                    if (!skuItem || !skuItem.skuCode) {
                        return;
                    }
                    combinedProductService.getSkuDetail({
                            "skuCode": skuItem.skuCode,
                            "cartId": $scope.vm.product.cartId
                        }).then(function (resp) {
                        if(resp.data.skuItem == null) {
                            alert("查询不到SKU信息！");
                        }else {
                            _.extend(skuItem, resp.data.skuItem);
                            dynamicSkuPrice(sku);
                        }
                    });
                };

                // 改变实际SKU价格
                $scope.changeSkuItemPrice = function (sku) {
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