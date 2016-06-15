/**
 * Created by sofia on 6/14/2016.
 */
define([
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {
    angularAMD.controller('popUpdateProductApprovalCtl', function($scope, $fieldEditService, $translate, $modalInstance, confirm, notify, context) {

        $scope.vm = {
            retInfo: context.resData,
            propertyInfo: context.propertyInfo
        };

        $scope.save = function (updateAll) {
            if (updateAll) {
                $scope.vm.propertyInfo.property.notChkPrice = 1;
            }
            if ($scope.vm.retInfo.startIdx != undefined) {
                $scope.vm.propertyInfo.property.startIdx = $scope.vm.retInfo.startIdx;
            }
            $modalInstance.close();
        }
    });
});