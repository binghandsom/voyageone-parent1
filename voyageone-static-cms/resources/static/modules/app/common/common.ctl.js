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
    require ('modules/app/systemMenu/cmsNav.ctl');

    mainApp.controller ('commonController', ['$scope', '$route', '$location', 'commonService', 'ngDialog', '$localStorage', '$window', 'commonRoute', 'mainMenu',
        function ($scope, $route, $location, commonService, ngDialog, $localStorage, $window, commonRoute, mainMenu) {

            var commonUtil = require ('components/util/commonUtil');
            // add 'ie' classes to html
            var isIE = !!navigator.userAgent.match (/MSIE/i);
            isIE && angular.element ($window.document.body).addClass ('ie');
            isSmartDevice ($window) && angular.element ($window.document.body).addClass ('smart');
            $scope.userInfo = {};

            // config
            $scope.app = {
                name: 'VoyageOne',
                version: 'Beta 0.0.2',
                // for chart colors
                color: {
                    primary: '#7266ba',
                    info: '#23b7e5',
                    success: '#27c24c',
                    warning: '#fad733',
                    danger: '#f05050',
                    light: '#e8eff0',
                    dark: '#3a3f51',
                    black: '#1c2b36',
                    vo: '#e67e22'
                },
                settings: {
                    themeID: 1,
                    navbarHeaderColor: 'bg-black',
                    navbarCollapseColor: 'bg-black',
                    asideColor: 'bg-dark',
                    headerFixed: true,
                    asideFixed: true,
                    asideFolded: true,
                    asideDock: false,
                    container: false
                }
            };

            // save settings to local storage
            if (angular.isDefined ($localStorage.settings)) {
                $scope.app.settings = $localStorage.settings;
            } else {
                $localStorage.settings = $scope.app.settings;
            }

            $scope.$watch ('app.settings', function () {
                if ($scope.app.settings.asideDock && $scope.app.settings.asideFixed) {
                    // aside dock and fixed must set the header fixed.
                    $scope.app.settings.headerFixed = true;
                }
                // for box layout, add background image
                //$scope.app.settings.container ? angular.element ('html').addClass ('bg') : angular.element ('html').removeClass ('bg');
                // save to local storage
                $localStorage.settings = $scope.app.settings;
            }, true);

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
                    $scope.userInfo.selChannel = userInfo.selChannel;
                    $scope.userInfo.selLanguage = userInfo.selLang;

                    $scope.menuList = userInfo.menuList;
                    $scope.companyList = userInfo.companyList;
                    $scope.channelList = userInfo.channels;
                    $scope.languageList = userInfo.languageList;
                })
                    .then(function() {
                        commonService.doGetMasterInfo($scope.userInfo.selChannel, $scope.userInfo.selMenu, true);
                    });
            };

            /**
             * do logout,then return to login page.
             */
            $scope.doLogout = function () {
                commonService.doLogout ();
            };

            /**
             * set selected language into userService when it has been changed..
             * @param key
             */
            $scope.doSelectLanguage = function (key) {
                commonService.doSelectLanguage (key)
                    .then (function () {
                    $scope.userInfo.selLanguage = key;
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
                    commonService.doGetMasterInfo($scope.userInfo.selChannel, menu, true);
                    if(!_.isEqual($location.path(), response.next)) {
                        commonUtil.goToSystemPage(response.next);
                    } else {
                        $route.reload ();
                    }
                })
            };

            /**
             * change the selected channel.
             * @param channel
             */
            $scope.selectChannel = function (channel) {
                commonService.selectChannel (channel)
                    .then (function (response) {
                    $scope.userInfo.selChannel = channel;
                    commonService.doGetMasterInfo(channel, $scope.userInfo.selMenu, true);
                    if(!_.isEqual($location.path(), response.next)) {
                        commonUtil.goToSystemPage(response.next);
                    } else {
                        $route.reload ();
                    }
                })
            };

            /**
             * 画面上的复合检索.
             */
            $scope.doSearch = function (inputValue) {
                if (commonUtil.isNotEmpty(inputValue)) {
                    switch ($scope.userInfo.selMenu) {
                        case mainMenu.CMS:
                            $location.path(commonUtil.returnReallyPath(commonRoute.cms_search_complex.hash, inputValue));
                            break;
                    }
                }
            };

            $scope.$on ('setNavigation', function () {

                $scope.navigationInfo = commonService.getCurrentNavigationInfo ();

                $scope.$broadcast ('navigationChanged');
            });

            $ (window).scroll (function () {
                var scroll = $ (window).scrollTop ();
                if (scroll > 56) {
                    $ (".app-header").addClass ("addShadow");
                }
                else {
                    $ (".app-header").removeClass ("addShadow");
                }
            });

            function isSmartDevice ($window) {
                // Adapted from http://www.detectmobilebrowsers.com
                var ua = $window['navigator']['userAgent'] || $window['navigator']['vendor'] || $window['opera'];
                // Checks for iOs, Android, Blackberry, Opera Mini, and Windows mobile devices
                return (/iPhone|iPod|iPad|Silk|Android|BlackBerry|Opera Mini|IEMobile/).test (ua);
            }
        }]);
});