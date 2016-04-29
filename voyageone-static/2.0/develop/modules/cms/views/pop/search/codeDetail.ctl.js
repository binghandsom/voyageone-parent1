/**
 * Created by 123 on 2016/4/1.
 */
define([
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {

    angularAMD.controller('popCodeDetailCtl', function ($scope, context) {
        $scope.vm = {
            attsList : context.attsList
        };
        /**
         * 初始化数据.
         */
        $scope.initialize = function () {
        };

    });
});