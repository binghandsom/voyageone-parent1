/**
 * @description 美国各平台详情页
 */
define([
    'cms',
    'modules/cms/enums/Carts',
    'modules/cms/directives/platFormStatus.directive'
], function (cms) {

    class usTabController {

        constructor($scope, detailDataService, notify, popups) {
            let self = this;
            self.$scope = $scope;
            self.detailDataService = detailDataService;
            self.notify = notify;
            self.popups = popups;
            self.productInfo = $scope.productInfo;
            self.cartInfo = $scope.cartInfo;
            // 平台信息
            self.platform = {};
            // SKU
            self.selAllSkuFlag = false;
            // 平台状态
            self.platformStatus = this.detailDataService.platformStatus;

            $scope.$on('price.save', function (event, data) {
                //匹配是否为当前平台，或者0：代表全平台刷新
                if (Number(self.cartInfo.value) === Number(data.cartId) || Number(data.cartId) === 0) {
                    self.init();
                }

            });

        }

        init() {
            let self = this;

            self.detailDataService.getProductPlatform({
                cartId: Number(self.cartInfo.value),
                prodId: self.productInfo.productId
            }).then(res => {

                self.platform = res.data.platform;
                self.productComm = res.data.productComm;

                if (!self.platform.pStatus) {
                    self.platform.pStatus = 'Pending';
                }

                // SKU 是否全选
                let flag = true;
                if (self.platform && _.size(self.platform.skus) > 0) {
                    _.each(self.platform.skus, sku => {
                        let isSale = sku.isSale;
                        if (!isSale) {
                            flag = false;
                        }

                        // 去productComm.skus匹配重量和尺码等属性
                        let commonSku = _.find(self.productComm.skus, commSku => {
                            return sku.skuCode == commSku.skuCode;
                        });
                        if (commonSku) {
                            let commAttr = {
                                barcode: commonSku.barcode,
                                clientSize: commonSku.clientSize,
                                size: commonSku.size,
                                weight: commonSku.weight,
                                weightUnit: commonSku.weightUnit
                            };
                            _.extend(sku, commAttr);
                        }
                    });
                }

                self.selAllSkuFlag = flag;

            })
        }

        /**
         * ctrl.popUsCategory({cartId:8,from:ctrl.platform.platform.pCatPath})
         * @param option
         */
        popUsCategory() {
            let self = this;

            if (Number(self.cartInfo.value) !== 5) {

                self.popups.openUsCategory({
                    cartId: 8,
                    from: self.platform.pCatPath
                }).then(res => {
                    self.platform.pCatPath = res.catPath;
                    self.platform.pCatId = res.catId;
                });

            }else{

                self.popups.openAmazonCategory({
                    cartId:5,
                    from:self.platform.pCatPath
                }).then(res => {
                    self.platform.pCatPath = res.catPath;
                    self.platform.pCatId = res.catId;
                });

            }


        }

        // SKU可售选择
        selAllSku() {
            let self = this;
            _.each(self.platform.skus, sku => {
                sku.isSale = self.selAllSkuFlag;
            });
        }

        checkSelAllSku(sku) {
            let self = this,
                isSale = sku.isSale;

            if (!isSale) {
                self.selAllSkuFlag = false;
            } else {
                let notSelOne = _.find(self.platform.skus, sku => {
                    return !sku.isSale;
                });
                self.selAllSkuFlag = !notSelOne;
            }
        }

        save(status) {
            let self = this;
            let platform = angular.copy(self.platform);

            if (status) {
                platform.pStatus = status;
            }
            let parameter = {
                prodId: self.$scope.productInfo.productId,
                data: {
                    platform: platform
                }
            };

            self.detailDataService.updateProductPlatform(parameter).then(res => {
                if (res.data) {
                    self.notify.success("Save success.");
                    self.platform.pStatus = platform.pStatus;
                }
            });
        }

    }

    cms.directive('usTab', function () {
        return {
            restrict: 'E',
            controller: usTabController,
            controllerAs: 'ctrl',
            scope: {
                productInfo: "=productInfo",
                cartInfo: "=cartInfo"
            },
            templateUrl: 'views/usa/product/detail/platform/us-tab.directive.html'
        }
    })

});