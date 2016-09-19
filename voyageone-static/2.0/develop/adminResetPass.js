/**
 * Created by sofia on 2016/8/30.
 */
define(['components/dist/voyageone.angular.com'], function () {
    angular.module('voyageone.admin.adminResetPass', [
        'blockUI',
        'voyageone.angular'
    ]).controller('resetPassController', function ($scope, $http) {
        $scope.submit = function () {
            $http({
                url: '/admin/user/self/forgetPass',
                method: 'post',
                params: {'userAccount': $scope.userAccount}
            }).then(function (res) {
                console.log(res);
            })
        }
    });
    return angular.bootstrap(document, ['voyageone.admin.adminResetPass']);
});