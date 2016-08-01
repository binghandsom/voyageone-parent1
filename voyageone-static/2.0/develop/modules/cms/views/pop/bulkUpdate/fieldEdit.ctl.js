/**
 * Created by linanbin on 15/12/7.
 * Modified by gubuchun on 15/12/16.
 */
define([
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {

    angularAMD.controller('popFieldEditCtl', function ($scope, $fieldEditService, $translate, $modalInstance, confirm, notify, alert, context) {

        $scope.vm = {
            propertyInfo: {
                property: {},
                productIds: context.productIds,
                isSelAll: context.isSelAll,
                cartId: context.cartId
            },
            properties: [],
            selCnt: context.selCnt
        };

        $scope.initialize = initialize;
        $scope.save = save;

        function initialize() {
            $fieldEditService.getPopOptions().then(function (res) {
                $scope.vm.properties = res.data;
            });
        }

        function save () {
            if ($scope.vm.propertyInfo.property == undefined || $scope.vm.propertyInfo.property == null) {
                alert("未选择变更项目，请选择后再操作。");
                return;
            }
            if ($scope.vm.propertyInfo.property.id == undefined || $scope.vm.propertyInfo.property.id == null) {
                alert("未选择变更项目，请选择后再操作。");
                return;
            }
            if ($scope.vm.propertyInfo.property && $scope.vm.propertyInfo.property.id) {
                if ($scope.vm.propertyInfo.property.id == 'translateStatus') {
                    if ($scope.vm.propertyInfo.property.value == undefined || $scope.vm.propertyInfo.property.value == null) {
                        alert("未设置变更项目，请设置后再操作。");
                        return;
                    }
                    if ($scope.vm.propertyInfo.property.value.value == undefined || $scope.vm.propertyInfo.property.value.value == null) {
                        alert("未设置变更项目，请设置后再操作。");
                        return;
                    }
                    var msg = '是否确认批量修改[翻译状态]？<br>本次操作对象商品数：' + $scope.vm.selCnt + '<br>选择确定，处理将会继续。 选择取消，处理停止';
                    confirm(msg).then(function(){
                        _openBulkUpdate();
                    });
                } else {
                    _openBulkUpdate();
                }
            }

            function _openBulkUpdate() {
                $fieldEditService.setProductFields($scope.vm.propertyInfo).then(function (res) {
                    if (res.data.ecd == null || res.data.ecd == undefined) {
                        alert("提交请求时出现错误");
                        return;
                    }
                    if (res.data.ecd == 1) {
                        alert("未选择商品，请选择后再操作。");
                        return;
                    }
                    if (res.data.ecd == 2) {
                        alert("未设置变更项目，请设置后再操作。");
                        return;
                    }
                    notify.success ($translate.instant('TXT_MSG_UPDATE_SUCCESS'));
                    $modalInstance.close(res);
                });
            }
        }
    });
});