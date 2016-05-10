/**
 * Created by 123 on 2016/5/4.
 */
define([
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {

    angularAMD.controller('popImageGroupImgCtl', function ($scope, originUrl) {
        $scope.originUrl = originUrl;
    });
});