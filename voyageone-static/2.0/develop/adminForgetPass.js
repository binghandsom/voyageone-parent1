/**
 * Created by sofia on 2016/8/30.
 */
define(['components/dist/common'], function () {
    angular.module('voyageone.admin.adminForgetPass', [
        'blockUI',
        'voyageone.angular'
    ]).controller('forgetPassController', function ($scope, $ajax) {
        $scope.submit = function () {
            $ajax.post('/admin/user/self/forgetPass', {
                userAccount: $scope.userAccount
            }).then(function (res) {
                if (res.data) {
                    console.log(res);
                }
            })
        }
    });
    return angular.bootstrap(document, ['voyageone.admin.adminForgetPass']);
});