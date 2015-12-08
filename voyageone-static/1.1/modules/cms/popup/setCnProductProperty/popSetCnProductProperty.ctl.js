/**
 * Created by sky on 2015/08/31
 */

define(function (require) {
    var cmsApp = require('modules/cms/cms.module');
    require ('modules/cms/popup/setCnProductProperty/popSetCnProductProperty.service');
    cmsApp.controller('popSetCnProductPropertyController', ['$scope', 'popSetCnProductPropertyService', 'userService', '$modalInstance', 'productIdArray', 'notify',
        function ($scope, popSetCnProductPropertyService, userService, $modalInstance, productIdArray, notify) {

            var vm = $scope.vm = {};
            vm.productIdList = productIdArray;
            vm.channelId = userService.getSelChannel();

            // 关闭对话框
            $scope.close = closeDialog;
            //修改产品属性
            $scope.changeCnProductProperty = changeCnProductProperty;

            function changeCnProductProperty() {
                popSetCnProductPropertyService.changeCnProductProperty(vm).then(function() {
                    notify.success("CMS_MSG_SET_PRODUCT_PROPERTY_SUCCESS");
                    closeDialog();
                });
            }

            function closeDialog() {
                $modalInstance.dismiss('close');
            }

        }]);

});