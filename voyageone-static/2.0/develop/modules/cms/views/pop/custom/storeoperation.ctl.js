/**
 * Created by pwj on 2016/4/19.
 */
define([
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {
    angularAMD.controller('popStoreOperationCtl', function ($scope,context) {
        $scope.pop = {
            header : context.header,
            upLoadFlag: context.upLoadFlag
        }
    });
});