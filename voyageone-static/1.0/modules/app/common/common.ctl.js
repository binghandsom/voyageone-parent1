/**
 * @Name:    masterController.js
 * @Date:    2015/2/14
 *
 * @User:    Edward
 * @Version: 1.0.0
 */

define (function (require) {

    var mainApp = require ("modules/app/app.module");
    //var mainModule = require ('components/app');
    require ('modules/app/common/common.service');
    require ('modules/app/popup/popChgPassword.ctl');

    mainApp.controller ('commonController', ['$scope', '$route', '$location', 'commonService', 'omsSessionStorageType','ngDialog',
        function ($scope, $route, $location, commonService, omsSessionStorageType,ngDialog) {

            var commonUtil = require ('components/util/commonUtil');
            $scope.userInfo = {};

            /**
             * initialize to show the userInfo/companyInfo/menuInfo
             */
            $scope.initialize = function () {

                // get the userInfo.
                commonService.doGetUserInfo ()
                    .then (function (userInfo) {
                    $scope.userInfo.userName = userInfo.userName;
                    $scope.userInfo.loginTime = userInfo.loginTime;
                    $scope.userInfo.selMenu = userInfo.selMenu;//cookieService.getMenu();
                    $scope.userInfo.selCompanyId = userInfo.selCompanyId;

                    $scope.menuList = userInfo.menuList;
                    $scope.companyList = userInfo.companyList;
                });
            };

            /**
             * do logout,then return to login page.
             */
            $scope.doLogout = function () {
                commonService.doLogout ();
            };

            /**
             * set selected company into userService when it has been changed..
             */
            $scope.doSelectCompany = function () {
                commonService.doSelectCompany ($scope.userInfo.selCompanyId)
                    .then (function (next) {
                    $location.path (next);
                    $scope.initialize();
                });
            };

            /**
             * set selected language into userService when it has been changed..
             * @param key
             */
            $scope.doSelectLanguage = function (key) {
                commonService.doSelectLanguage (key)
                    .then (function () {
                    $route.reload ();
                });
            };

            /**
             * change the selected menu.
             * @param menu
             */
            $scope.selectMenu = function (menu) {
                commonService.selectMenu (menu)
                    .then (function () {
                    $scope.userInfo.selMenu = menu;
                })
                    .then (function () {
                    // TODO:这样写太丑了，以后要想个办法改掉。现在先这个吧。
                    // 检索条件
                    //var SESSION_STORAGE_ORDER_SEARCH = 'orders.search';
                    // 检索条件删除
                    sessionStorage.removeItem (omsSessionStorageType.ORDER_DETAIL_INFO_LIST);
                    sessionStorage.removeItem (omsSessionStorageType.ORDER_DETAIL_BEFORE_PAGE_URL);
                    sessionStorage.removeItem (omsSessionStorageType.ORDER_NEW_CUSTOMER_INFO);
                    sessionStorage.removeItem (omsSessionStorageType.ORDER_NEW_BEFORE_PAGE_URL);
                    sessionStorage.removeItem (omsSessionStorageType.ORDER_SEARCH_SELECT_CONDITION);
                    sessionStorage.removeItem (omsSessionStorageType.ORDER_SEARCH_SELECT_CONDITION_FLAG);
                })
            };

            $scope.doChgPassword = function(){
            	$scope.register={};
            	$scope.message="";
                ngDialog.open ({
                    template: '/VoyageOne/modules/app/popup/popChgPassword.dialog.tpl.html',
                    controller: 'popChgPasswordController',
                    scope: $scope
                });
            };
            $scope.$on ('setNavigation', function () {

                $scope.navigationInfo = commonService.getCurrentNavigationInfo ();

                $scope.$broadcast ('navigationChanged');
            });

        }]);
});