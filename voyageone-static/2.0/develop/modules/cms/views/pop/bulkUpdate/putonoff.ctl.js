/**
 * Created by sofia on 6/7/2016.
 */
define([
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {
    angularAMD.controller('popPutOnOffCtl', function ($scope, $fieldEditService, $translate, $modalInstance, confirm, notify, alert, context) {

        $scope.vm = {
            property: context
        };

        $scope.save = function () {
            if ($scope.vm.property.putFlg == null || $scope.vm.property.putFlg == undefined) {
                alert("没有选择上下架操作，请选择后再操作。");
                return;
            }
            var msg = '';
            if ($scope.vm.property.cartId && $scope.vm.property.cartId > 0) {
                msg = $translate.instant('TXT_CONFIRM_NOW_STORE_PUT_ON');
            } else {
                msg = $translate.instant('TXT_CONFIRM_ALL_STORE_PUT_ON');
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
                    $modalInstance.close();
                    notify.success($translate.instant('TXT_MSG_UPDATE_SUCCESS'));
                });
            });
        }
    });
});