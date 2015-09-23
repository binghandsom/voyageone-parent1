/**
 * @Name:    defaultController.js
 * @Date:    2015/2/3
 *
 * @User:    Edward
 * @Version: 1.0.0
 */

define (function (require) {

    var mainApp = require ("modules/app/app.module");
    require ('modules/app/common/common.service');

    mainApp.controller ('indexController', ['$scope', 'commonService',
        function ($scope, commonService) {

            $scope.initialize = function () {
                commonService.doLogout ();
            };
        }]);
});
