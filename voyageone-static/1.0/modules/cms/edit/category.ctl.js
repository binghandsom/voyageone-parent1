/**
 * @Name:    companyController.js
 * @Date:    2015/2/6
 *
 * @User:    Edward
 * @Version: 1.0.0
 */

define (function (require) {

    var cmsApp = require ('modules/cms/cms.module');
    require ('modules/cms/edit/category.service');

    cmsApp.controller ('categoryController', ['$scope', '$location', 'categoryService',
        function ($scope, $location, categoryService) {

            var commonUtil = require ('components/util/commonUtil');

            // initialize the company list.
            $scope.initialize = function () {
            	categoryService.doGetCategoryCNPriceSettingInfo ({"categoryId":"3","channelId":"005"},$scope)
                    .then (function (companyList) {
                        $scope.companyList = companyList;
                    });
            };

        }]);
});
