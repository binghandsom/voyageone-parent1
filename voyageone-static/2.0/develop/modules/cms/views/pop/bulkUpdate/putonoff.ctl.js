/**
 * Created by sofia on 6/7/2016.
 */
define([
    'angularAMD',
    'modules/cms/enums/Carts',
    'modules/cms/controller/popup.ctl'
], function (angularAMD, Carts) {
    angularAMD.controller('popPutOnOffCtl', function ($scope, $fieldEditService, $translate, $modalInstance, confirm, notify, alert, context) {

        $scope.vm = {
            property: context
        };

        $scope.init = function () {
            if ($scope.vm.property.cartId && $scope.vm.property.cartId > 0) {
                var cartObj = Carts.valueOf(parseInt($scope.vm.property.cartId));
                var cartName = $scope.vm.property.cartId;
                if (cartObj) {
                    cartName = cartObj.desc;
                    if (cartName == null || cartName == '' || cartName == undefined) {
                        cartName = $scope.vm.property.cartId;
                    }
                }
                $scope.vm.titleName = cartName;
            } else {
                $scope.vm.titleName = '全店铺';
            }
        };

        $scope.save = function () {
            if ($scope.vm.property.putFlg == null || $scope.vm.property.putFlg == undefined) {
                alert("没有选择上下架操作，请选择后再操作。");
                return;
            }
            var optName = '';
            if ($scope.vm.property.putFlg == 1) {
                optName = '上架';
            } else if ($scope.vm.property.putFlg == 0) {
                optName = '下架';
            }
            var msg = '';
            if ($scope.vm.property.cartId && $scope.vm.property.cartId > 0) {
                msg = $translate.instant('TXT_CONFIRM_NOW_STORE_PUT_ON', {'cartName': $scope.vm.titleName, 'optName': optName});
            } else {
                msg = $translate.instant('TXT_CONFIRM_ALL_STORE_PUT_ON', {'optName': optName});
            }
            confirm(msg).then(function () {
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
                        // 没有设置上下架操作
                        alert($translate.instant('没有设置上下架操作，请选择后重试'));
                        return;
                    }
                    if (res.data.ecd == 3) {
                        alert($translate.instant('上架操作，选择的商品库存都不大于0'));
                        return;
                    }
                    $modalInstance.close();
                    notify.success($translate.instant('TXT_MSG_UPDATE_SUCCESS'));
                });
            });
        }
    });
});