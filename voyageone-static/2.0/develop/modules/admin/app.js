define([
    'angularAMD',
    'angular',
    'underscore',
    'modules/admin/routes',
    'modules/admin/actions',
    'modules/admin/translate/en',
    'modules/admin/translate/zh'
], function (angularAMD, angular, _, routes, actions, enTranslate, zhTranslate) {

    var mainApp = angular.module('voyageone.admin', [
        'ngRoute',
        'ngAnimate',
        'ngCookies',
        'ngSanitize',
        'pascalprecht.translate',
        'blockUI',
        'voyageone.angular',
        'voyageone.angular.vresources',
        'ui.bootstrap',
        'ui.indeterminate',
        'ngStorage',
        'angularFileUpload',
        'localytics.directives',
        'angular-md5',
        'angular-drag'
    ]).constant('cActions', actions)
        .constant('cRoutes', routes)
        .constant('cLanguageType', {
            en: {
                name: "en",
                value: enTranslate
            },
            zh: {
                name: "zh",
                value: zhTranslate
            }
        })
        .constant('cCommonRoutes', {
            "login": {
                "url": "/login.html"
            },
            "application": {
                "modules": "/modules/",
                "url": "/app.html#/home"
            }
        })

        .config(function ($routeProvider, $translateProvider, cLanguageType, $uibModalProvider) {
            // 加载所有的语言配置
            _.each(cLanguageType, function (type) {
                $translateProvider.translations(type.name, type.value);
            });
            // 加载所有的路由配置
            _.each(routes, function (module) {
                console.log(module);
                return $routeProvider.when(module.hash, angularAMD.route(module));
            });
            // 默认设置所有的弹出模态框的背景不能关闭模态框
            $uibModalProvider.options.backdrop = 'static';
        })

        .run(function ($vresources, $localStorage) {
            // 从会话中取出登录和选择渠道存储的数据
            var userInfo = $localStorage.user || {};
            // 传入 register 作为额外的缓存关键字
            $vresources.register(null, actions, {
                username: userInfo.name
            });
        })

        .controller('appCtrl', appCtrl)

        // menu.header.
        .controller('headerCtrl', headerCtrl)

        // menu.breadcrumbs.
        .controller('breadcrumbsCtrl', breadcrumbsCtrl)

    function appCtrl($scope, $window, translateService) {

        var isIE = !!navigator.userAgent.match(/MSIE/i);
        isIE && angular.element($window.document.body).addClass('ie');
        isSmartDevice($window) && angular.element($window.document.body).addClass('smart');

        // config
        $scope.app = {
            name: 'VoyageOne',
            version: 'Version 2.4.0',
            copyRight: 'Copyright © 2016 VoyageOne. All Rights Reserved.',
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
                asideFolded: false,
                container: false
            }
        };

        $scope.$watch('app.settings', function () {
            if ($scope.app.settings.asideFixed) {
                // aside dock and fixed must set the header fixed.
                $scope.app.settings.headerFixed = true;
            }
            // for box layout, add background image
            $scope.app.settings.container ? angular.element('html').addClass('bg') : angular.element('html').removeClass('bg');
        }, true);

        $scope.$watch('$viewContentLoaded', function () {
            $(window).scroll(function () {
                var scroll = $(window).scrollTop();
                if (scroll > 56) {
                    $(".app-header").addClass("addShadow");
                }
                else {
                    $(".app-header").removeClass("addShadow");
                }
            });
        });

        // set de default language
        translateService.setLanguage("");

        function isSmartDevice($window) {
            // Adapted from http://www.detectmobilebrowsers.com
            var ua = $window['navigator']['userAgent'] || $window['navigator']['vendor'] || $window['opera'];
            // Checks for iOs, Android, Blackberry, Opera Mini, and Windows mobile devices
            return (/iPhone|iPod|iPad|Silk|Android|BlackBerry|Opera Mini|IEMobile/).test(ua);
        }
    }

    function headerCtrl($scope, $rootScope, $window, $location, cRoutes, cCommonRoutes) {
        var vm = this;
        vm.languageList = {};
        vm.userInfo = {};
        vm.searchValue = "";



        /**
         * change to selected language.
         * @param language
         */
        // function selectLanguage(language) {
        //     menuService.setLanguage(language).then(function (data) {
        //         vm.userInfo.language = data;
        //         $window.location.reload();
        //     })
        // }

        /**
         * logout.
         */
        // function logout() {
        //     menuService.logout().then(function () {
        //         $window.location = cCommonRoutes.login.url;
        //     })
        // }
    }

    function breadcrumbsCtrl($scope, $rootScope, $location, menuService, cRoutes) {
        var vm = this;
        vm.cid = "";
        vm.navigation = {};

        $scope.initialize = initialize;
        $scope.selectCategory = selectCategory;

        /**
         * initialize
         */
        function initialize() {
            menuService.getCategoryInfo().then(function (data) {
                vm.navigation = data.categoryList;
                // TODO 来至服务器端的session
                $rootScope.platformType = data.platformType;
                $rootScope.imageUrl = data.imageUrl;
                $rootScope.productUrl = data.productUrl;
            });
        }

        /**
         * change to other category page.
         * @param cid
         */
        function selectCategory(cid) {
            $location.path(cRoutes.category.url + cid);
        }
    }

    return angularAMD.bootstrap(mainApp);
});