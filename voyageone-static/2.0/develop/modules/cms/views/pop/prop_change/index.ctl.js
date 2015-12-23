/**
 * Created by linanbin on 15/12/7.
 * Modified by gubuchun on 15/12/16.
 */
define([
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {

    angularAMD.controller('popPropChangeCtl', function ($scope, $propChangeService, $translate, notify, productIds) {

        $scope.vm = {
            propertyInfo: {
                property: {},
                productIds: productIds
            },
            properties: []
        };

        $scope.initialize = initialize;
        $scope.save = save;

        function initialize() {
            $propChangeService.getPopOptions().then(function (res) {
                $scope.vm.properties = res.data;
            });
        }

        function save () {
            $propChangeService.setProductFields($scope.vm.propertyInfo).then(function () {
                notify.success ($translate.instant('TXT_COM_UPDATE_SUCCESS'));
                $scope.$close();
            });
        }
    });
});