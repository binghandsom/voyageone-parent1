/**
 * Created by sky on 2015/09/01
 */

define(function (require) {
    var cmsApp = require('modules/cms/cms.module');
    require ('modules/cms/popup/setCnProductShare/popSetCnProductShare.service');
    cmsApp.controller('popSetCnProductShareController', ['$scope', 'popSetCnProductShareService', 'userService', '$modalInstance', 'productIdArray', 'notify', '$filter',
        function ($scope, popSetCnProductShareService, userService, $modalInstance, productIdArray, notify, $filter) {

            var commonUtil = require('components/util/commonUtil');
            var vm = $scope.vm = {};

            vm.productIdList = productIdArray;
            vm.channelId = userService.getSelChannel();

            // 关闭对话框
            $scope.close = closeDialog;
            $scope.doSetCnProductShare = doSetCnProductShare;
            $scope.changeIsOutletsOnSale = changeIsOutletsOnSale;

            function doSetCnProductShare() {
                if (_.isDate (vm.prePublishDatetime)) {
                    vm.prePublishDatetime = commonUtil.doFormatDate (vm.prePublishDatetime);
                }
                popSetCnProductShareService.doSetCnProductShare(vm).then(function() {
                    notify("CMS_TXT_PRODUCT_APPROVED_SUCCESS");
                    clearVm();
                });
            }

            function closeDialog() {
                $modalInstance.dismiss('close');
            }

            function clearVm(){
                vm = $scope.vm = {};
                vm.productIdList = productIdArray;
                vm.channelId = userService.getSelChannel();
            }

            function changeIsOutletsOnSale () {
                if(vm.isOutletsOnSale != '1') {
                    vm.discount = '';
                }
            }

        }]);

});