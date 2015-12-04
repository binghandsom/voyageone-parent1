/**
 * @Name:    permissionService.js
 * @Date:    2015/1/30
 *
 * @User:    Edward
 * @Version: 1.0.0
 */
define (function (require) {

    var mainApp = require ('components/app');

    // define a factory on mainApp for save the permissions of login user.
    mainApp.service ('permissionService', ['$rootScope',
        function ($rootScope) {

            var permissionList = [];

            /**
             * set the action permissions.
             * @param values
             */
            this.setPermissions = function (values) {
                permissionList = values;
                $rootScope.$broadcast ('permissionsChanged');
            };

            /**
             * check the permission has been in action permissions.
             * @param permission
             * @returns {boolean|*}
             */
            this.hasPermission = function (permission) {
                return checkPermission (permission);
            };

            /**
             * check the page's permissions.
             * @param permission
             * @returns {boolean|*}
             */
            function checkPermission (permission) {
                var _ = require (['underscore']);
                return _.contains (permissionList, permission.trim ());
            }
        }]);
});