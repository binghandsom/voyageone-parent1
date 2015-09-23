/**
 * @Name:    companyController.js
 * @Date:    2015/2/6
 *
 * @User:    Edward
 * @Version: 1.0.0
 */

define (function (require) {

    var coreApp = require ('modules/core/core.module');
    require ('modules/core/account/account.service');
    require ('modules/core/directives/companyStyle.directive');

    coreApp.controller ('companyController', ['$scope', '$location', 'companyService',
        function ($scope, $location, companyService) {

            var commonUtil = require ('components/util/commonUtil');

            // initialize the company list.
            $scope.initialize = function () {
                companyService.doGetCompany ()
                    .then (function (companyList) {
                        $scope.companyList = companyList;
                    });
            };

            // change the selected company.
            $scope.doSelectCompany = function (companyId) {
                companyService.doSelectCompany (companyId.toString ())
                    .then (function () {
                        commonUtil.goToHomePage ();
                    });
            };

        }]);
});
