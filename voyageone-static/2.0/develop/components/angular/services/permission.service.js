(function() {
    /**
     * @Date:    2015-11-19 14:26:43
     * @User:    Jonas
     * @Version: 0.2.0
     */
    angular.module("voyageone.angular.services.permission", []).service("permissionService", PermissionService);
    function PermissionService($rootScope) {
        this.$rootScope = $rootScope;
        this.permissions = [];
    }
    PermissionService.prototype = {
        /**
         * set the action permissions.
         * @param permissions
         */
        setPermissions: function(permissions) {
            this.permissions = permissions;
            this.$rootScope.$broadcast("permissionsChanged");
        },
        /**
         * check the permission has been in action permissions.
         * @param permission
         * @returns {boolean|*}
         */
        has: function(permission) {
            return _.contains(this.permissions, permission.trim());
        }
    };
})();