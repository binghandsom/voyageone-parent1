/**
 * Created by 123 on 2016/4/1.
 */
define([
    'angularAMD',
    'underscore',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {

    angularAMD.controller('popImageDetailCtl', function ($scope, context) {
        $scope.vm = {
            picInfo : context
        };
        /**
         * 初始化数据.
         */
        $scope.initialize = function () {

        };
    });
});
