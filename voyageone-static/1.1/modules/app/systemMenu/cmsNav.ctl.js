/**
 * Created by edward-pc1 on 2015/8/24.
 */

define (function (require) {

    var mainApp = require ("modules/app/app.module");
    require ('modules/app/systemMenu/systemMenu.service');

    /**
     * cms的左边菜单展示
     */
    mainApp.controller ('cmsNavController', ['$scope', '$route', '$rootScope', '$location',
        function ($scope, $route, $rootScope, $location) {

            var commonUtil = require ('components/util/commonUtil');
            $scope.currentMenu = '';
            $scope.selectedCategory = undefined;
            $scope.selectedPromotion = undefined;

            /**
             * initialize to show the userInfo/companyInfo/menuInfo
             */
            $scope.initialize = function () {
                if ($location.path().indexOf('/cms/edit/category') > -1) {
                    $scope.currentMenu = 'category';
                }
                else if ($location.path().indexOf('/cms/edit/model') > -1) {
                    $scope.currentMenu = 'category';
                }
                else if ($location.path().indexOf('/cms/edit/product') > -1) {
                    $scope.currentMenu = 'category';
                }
                else if ($location.path().indexOf('/cms/edit/promotion') > -1) {
                    $scope.currentMenu = 'promotion';
                }
                else if ($location.path().indexOf('/cms/new') > -1) {
                    $scope.currentMenu = 'new';
                }
                else if ($location.path().indexOf('/cms/search') > -1) {
                    $scope.currentMenu = 'search';
                }
                else if ($location.path().indexOf('/cms/masterCategory') > -1) {
                    $scope.currentMenu = 'masterCategory';
                }
            };
        }]);
});
