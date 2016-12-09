/**
 * @author rex.wu
 * @date 2016/11/24.
 */
define([
        "cms",
        'modules/cms/enums/Carts',
        "modules/cms/controller/popup.ctl"
    ], function (cms,cartsEnums) {
        cms.controller("combinedProductController", (function () {

            function CombinedProductController($scope, combinedProductService, popups, confirm) {
                $scope.vm = {
                    config: {
                        open: true,
                        startSupplyChain: 0
                    },
                    carts: {},
                    cartsEnums:cartsEnums,
                    products: [],
                    productPageOption: {curr: 1,size:10, total: 0, fetch: function(){
                        getProductList();
                    }},
                    statuses: {},
                    platformStatuses: {},
                    searchBean: {
                        cartId: ""
                    }
                };

                /**
                 * 分页处理product数据
                 */
                function getProductList() {
                    var statusesObj = _.pick($scope.vm.searchBean.statuses, function (value, key, object) {
                            return value;
                        }),
                        statuses = _.keys(statusesObj),
                        platformStatusesObj = _.pick($scope.vm.searchBean.platformStatuses, function (value, key, object) {
                            return value;
                        }),
                        platformStatuses = _.keys(platformStatusesObj),
                        copy_searchBean = angular.copy($scope.vm.searchBean);

                    copy_searchBean.statuses = statuses;
                    copy_searchBean.platformStatuses = platformStatuses;
                    combinedProductService.search(_.extend(copy_searchBean, $scope.vm.productPageOption))
                        .then(function (resp) {
                            $scope.vm.products = resp.data.products == null ? [] : resp.data.products;
                            $scope.vm.productPageOption.total = resp.data.total;
                        });
                }

                $scope.initialize = function () {
                    combinedProductService.init().then(function (resp) {
                        $scope.vm.carts = resp.data.carts == null ? {} : resp.data.carts;
                        $scope.vm.statuses = resp.data.statuses == null ? {} : resp.data.statuses;
                        $scope.vm.platformStatuses = resp.data.platformStatuses == null ? {} : resp.data.platformStatuses;
                        $scope.vm.config.startSupplyChain = resp.data.startSupplyChain == null ? 0 : resp.data.startSupplyChain;
                    });
                    getProductList();
                };

                $scope.popNewCombinedProduct = function () {
                    popups.popNewCombinedProduct(_.extend({"carts": $scope.vm.carts}, {"startSupplyChain": $scope.vm.config.startSupplyChain})).then(function () {
                        getProductList();
                    });
                };

                $scope.search = function () {
                    getProductList();
                };

                $scope.clear = function () {
                    $scope.vm.searchBean = {};
                };

                /**
                 *  删除组合套装商品
                 */
                $scope.deleteCombinedProduct = function (product) {
                    if (!product) {
                        return;
                    }

                    confirm("是否确认删除该组合套装商品？").then(function () {
                        combinedProductService.delete(product).then(function () {
                            //$scope.vm.productPageOption.total = $scope.vm.productPageOption.total - 1;
                            if ($scope.vm.products.length == 1 && $scope.vm.productPageOption.curr > 1) {
                                $scope.vm.productPageOption.curr = $scope.vm.productPageOption.curr - 1;
                            }
                            getProductList();
                        });
                    });
                };

                /** 编辑组合套装商品 */
                $scope.popEditCombinedProduct = function (product) {

                    popups.popEditCombinedProduct(_.extend({'product': angular.copy(product)}, {'carts': $scope.vm.carts}, {"startSupplyChain": $scope.vm.config.startSupplyChain})).then(function () {
                        getProductList();
                    });
                };

                /** 组合套装商品上下架 */
                $scope.onOffShelves = function (product, platformStatus) {
                    var confirmMsg = "";

                    if (platformStatus == 0) {
                        confirmMsg = "是否确认将该组合商品下架？";
                    } else if (platformStatus == 1) {
                        confirmMsg = "是否确认将该组合商品上架？";
                    }
                    if (confirmMsg == "") {
                        return;
                    }
                    confirm(confirmMsg).then(function () {
                        combinedProductService.onOffShelves(_.extend(angular.copy(product), {'platformStatus': platformStatus})).then(function () {
                            getProductList();
                        });
                    });

                };

                /** 操作日志 */
                $scope.popCombinedProductLogs = function (product) {
                    popups.popCombinedProductLogs(_.extend({"product": angular.copy(product)}, {
                        "carts": $scope.vm.carts,
                        "statuses": $scope.vm.statuses,
                        "platformStatuses": $scope.vm.platformStatuses
                    }));
                };

                /**
                 * 获取商品平台地址
                 * @param cartId numberId
                 */
                $scope.getPlatFormUrl = function(cartId,numberId){
                    var _cartObj = cartsEnums.valueOf(cartId);

                    if(!numberId)
                        return '';

                    if(cartId != 27)
                        return _cartObj.pUrl + numberId;
                    else
                        return _cartObj.pUrl + numberId + ".html";

                }
            }

            return CombinedProductController;

        })());
    }
);

