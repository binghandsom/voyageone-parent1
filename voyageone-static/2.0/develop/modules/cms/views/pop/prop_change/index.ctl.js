/**
 * Created by linanbin on 15/12/7.
 * Modified by gubuchun on 15/12/16.
 */
define([
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {

    angularAMD.controller('popPropChangeCtl', function ($scope, $propChangeService, selList) {

        $scope.vm = {
            propertyInfo: {
                property: {},
                productIds: selList,
                value: {}
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
                $scope.$close();
            });
        }
    });
});