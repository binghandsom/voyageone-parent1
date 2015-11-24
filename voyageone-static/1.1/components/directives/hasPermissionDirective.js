/**
 * @Name:    hasPermissionDirective.js
 * @Date:    2015/1/30
 *
 * @User:    Edward
 * @Version: 1.0.0
 */

define(function(require) {
    var mainApp = require('components/app');
    require('components/services/permissionService');

    // define a directive on mainApp for the permission on page.
    mainApp.directive('hasPermission', ['$location', 'permissionService',
        function($location, permissionService){
            return {
                restrict: 'EA',
                link : function(scope, iElement, iAttr) {

                    function toggleVisibilityBasedOnPermission() {
                        var value = $location.path() + '/' + iAttr.hasPermission.trim();
                        if (permissionService.hasPermission(value)) {
                            $(iElement).show();
                        } else {
                            angular.element(iElement).remove();
                        }
                    }
                    toggleVisibilityBasedOnPermission();
                    scope.$on('permissionsChanged', toggleVisibilityBasedOnPermission);
                }
            }
        }]);

    return mainApp;
});