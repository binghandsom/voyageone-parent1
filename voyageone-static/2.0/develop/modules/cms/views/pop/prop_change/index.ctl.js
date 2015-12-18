/**
 * Created by linanbin on 15/12/7.
 * Modified by gubuchun on 15/12/16.
 */
define([
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {

    angularAMD.controller('popPropChangeCtl', function ($scope, $propChangeService, data) {

        $scope.promotion = {};
        $scope.tejiabao = false;

        $scope.initialize = function () {
            if (data) {
                $scope.promotion = items
            }
        };
    });
});