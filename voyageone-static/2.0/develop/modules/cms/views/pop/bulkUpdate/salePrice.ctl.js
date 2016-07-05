/**
 * Created by sofia on 6/7/2016.
 */
define([
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {
    angularAMD.controller('popSalePriceCtl', function ($scope, $fieldEditService, $translate, $modalInstance, $filter, confirm, notify, alert, context) {

        $scope.vm = {
            property: context.property,
            cartId: context.property.cartId.toString(),
            cartList: context.cartList,
            _optStatus: false,
            priceTypeId: 0,
            isRoundUp: true
        };

        $scope.save = function () {
            // 检查输入
            if ($scope.vm.priceTypeId == 0) {
                alert("未选择基准价格，请选择后再操作。");
                return;
            }
            if ($scope.vm.optType == undefined || $scope.vm.optType == '') {
                alert("未选择表达式，请选择后再操作。");
                return;
            }
            if ($scope.vm.optType != '=' && ($scope.vm.priceValue == undefined || $scope.vm.priceValue == '')) {
                alert("未填写价格，请填写后再操作。");
                return;
            }
            if ($scope.vm.priceTypeId == 4 && ($scope.vm.priceValue == undefined || $scope.vm.priceValue == '')) {
                alert("未填写价格，请填写后再操作。");
                return;
            }
            $scope.vm.property.priceType = $scope.vm.priceType;
            $scope.vm.property.optionType = $scope.vm.optType;
            $scope.vm.property.priceValue = $scope.vm.priceValue;
            if ($scope.vm.isRoundUp) {
                $scope.vm.property.isRoundUp = '1';
            } else {
                $scope.vm.property.isRoundUp = '0';
            }

            confirm($translate.instant('TXT_BULK_SETSALEPRICE')).result.then(function () {
                $fieldEditService.setProductFields($scope.vm.property).then(function (res) {
                    if (res.data.ecd == null || res.data.ecd == undefined) {
                        alert($translate.instant('TXT_COMMIT_ERROR'));
                        return;
                    }
                    if (res.data.ecd == 1) {
                        // 未选择商品
                        alert($translate.instant('未选择商品，请选择后再操作'));
                        return;
                    }
                    if (res.data.ecd == 2) {
                        // 低于指导价
                        alert("商品[code=" + res.data.prodCode + ", sku=" + res.data.skuCode + "]的最终售价[" + $filter('number')(res.data.priceSale, 2) + "]低于指导价[" + $filter('number')(res.data.priceLimit, 2) + "]，请重新输入。");
                        return;
                    }
                    if (res.data.ecd == 3) {
                        // 大于阀值
                        alert("商品[code=" + res.data.prodCode + ", sku=" + res.data.skuCode + "]的最终售价[" + $filter('number')(res.data.priceSale, 2) + "]超过阈值[" + $filter('number')(res.data.priceLimit, 2) + "]，请重新输入。");
                        return;
                    }
                    $modalInstance.close();
                    notify.success($translate.instant('TXT_MSG_UPDATE_SUCCESS'));
                });
            });
        };

        $scope.chkOptionType = function () {
            if ($scope.vm.optType == '') {
                $scope.vm._opeText = '';
            } else if ($scope.vm.optType == '=') {
                $scope.vm._opeText = '';
            } else {
                $scope.vm._opeText = $scope.vm.optType;
            }
        };

        $scope.chkPriceType = function (priceTypeVal, typeTxt) {
            $scope.vm.priceTypeId = priceTypeVal;
            if (priceTypeVal == 4) {
                $scope.vm._optStatus = true;
                $scope.vm.optType = '=';
                $scope.vm._opeText = '';
                $scope.vm._typeText = '';
            } else {
                $scope.vm._optStatus = false;
                $scope.vm.optType = '';
                $scope.vm._opeText = '';
                $scope.vm._typeText = $translate.instant(typeTxt);
            }
        };
    });
});