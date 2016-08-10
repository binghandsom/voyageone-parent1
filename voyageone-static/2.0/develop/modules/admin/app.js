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
                username: userInfo.name,
                channel: userInfo.channel
            });
        })

        // menu service.
        .service('menuService', menuService)

        .controller('appCtrl', appCtrl)

        // menu.header.
        .controller('headerCtrl', headerCtrl)

        // menu.breadcrumbs.
        .controller('breadcrumbsCtrl', breadcrumbsCtrl)

        // menu.aside.
        .controller('asideCtrl', asideCtrl);

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

    function menuService($q, ajaxService, cookieService, translateService, cActions, $menuService) {

        this.getMenuHeaderInfo = getMenuHeaderInfo;
        this.setMenu = setMenu;
        this.clearChannel = clearChannel;
        this.setLanguage = setLanguage;
        this.setPlatformType = setPlatformType;
        this.logout = logout;
        this.getCategoryInfo = getCategoryInfo;
        this.getPlatformType = getPlatformType;
        this.getCmsConfig = getCmsConfig;

        /**
         * get the system info.
         * includes: systemMenu, languages,
         * @returns {*}
         */
        function getMenuHeaderInfo() {
            var defer = $q.defer();
            ajaxService.post(cActions.core.home.menu.getMenuHeaderInfo)
                .then(function (response) {
                    var data = response.data;

                    // 获取用户语言
                    //var userlanguage = translateService.getBrowserLanguage();
                    //if (!_.isEmpty(cookieService.language()))
                    //    userlanguage = cookieService.language();
                    //else if (!_.isEmpty(data.userInfo.language))
                    userlanguage = data.userInfo.language;

                    // 设置画面用户显示的语言
                    _.forEach(data.languageList, function (language) {

                        if (_.isEqual(userlanguage, language.name)) {
                            data.userInfo.language = language.add_name1;
                            translateService.setLanguage(language.add_name2);
                        }
                    });

                    //data.userInfo.application = cookieService.application();  服务端已经返回
                    defer.resolve(data);
                });
            return defer.promise;
        }

        /**
         * set menu.
         * @param menuTitle
         * @returns {*}
         */
        function setMenu(menuTitle) {
            var defer = $q.defer();
            cookieService.application(menuTitle);
            defer.resolve(menuTitle.toLocaleLowerCase());
            return defer.promise;
        }

        /**
         * clear selected channel
         * @returns {*}
         */
        function clearChannel() {
            var defer = $q.defer();
            cookieService.channel("");
            defer.resolve();
            return defer.promise;
        }

        /**
         * set language.
         * @param language
         * @returns {*}
         */
        function setLanguage(language) {
            var defer = $q.defer();
            ajaxService.post(cActions.core.home.menu.setLanguage, {"language": language.name})
                .then(function () {
                    cookieService.language(language.add_name2);
                    translateService.setLanguage(language.add_name2);
                    defer.resolve(language.add_name1);
                });
            return defer.promise;
        }

        /**
         * clear all cookie.
         * @returns {*}
         */
        function logout() {
            var defer = $q.defer();
            ajaxService.post(cActions.core.access.user.logout)
                .then(function () {
                    cookieService.removeAll();
                    defer.resolve();
                });
            return defer.promise;
        }

        /**
         * get categoryList.
         * @returns {*}
         */
        function getCategoryInfo() {
            return $menuService.getCategoryInfo().then(function (res) {
                return res.data;
            });
        }

        /**
         * get platformShowTypeList.
         * @returns {*}
         */
        function getPlatformType() {
            return $menuService.getPlatformType().then(function (res) {
                return res.data;
            });
        }

        /**
         * set platformType.
         */
        function setPlatformType(cType) {
            return $menuService.setPlatformType({
                "cTypeId": cType.add_name2,
                "cartId": parseInt(cType.value)
            }).then(function (res) {
                return res.data;
            });
        }

        /**cms配置信息，基于session缓存*/
        function getCmsConfig(){
            return $menuService.getCmsConfig().then(function(res){
                return res.data;
            });
        }
    }

    function headerCtrl($scope, $rootScope, $window, $location, menuService, cRoutes, cCommonRoutes) {
        var vm = this;
        vm.menuList = {};
        vm.languageList = {};
        vm.userInfo = {};
        vm.searchValue = "";

        $scope.initialize = initialize;
        $scope.selectChannel = selectChannel;
        $scope.selectMenu = selectMenu;
        $scope.selectLanguage = selectLanguage;
        $scope.goSearchPage = goSearchPage;
        $scope.logout = logout;

        function initialize() {
            menuService.getMenuHeaderInfo().then(function (data) {
                vm.applicationList = data.applicationList;
                vm.languageList = data.languageList;
                vm.userInfo = data.userInfo;
                $rootScope.menuTree = data.menuTree;
                $rootScope.application = data.userInfo.application;
                $rootScope.isTranslator = data.isTranslator;
            });
        }

        /**
         * go to channel selected page.
         */
        function selectChannel() {
            menuService.clearChannel().then(function () {
                $window.location = cCommonRoutes.channel.url;
            });
        }

        /**
         * change to selected menu, and go to new system.
         * @param menu
         */
        function selectMenu(menu) {
            menuService.setMenu(menu.menuTitle).then(function (application) {
                $window.location = cCommonRoutes.application.modules + application + cCommonRoutes.application.url;
            });
        }

        /**
         * change to selected language.
         * @param language
         */
        function selectLanguage(language) {
            menuService.setLanguage(language).then(function (data) {
                vm.userInfo.language = data;
                $window.location.reload();
            })
        }

        /**
         * search by input value.
         */
        function goSearchPage(value) {
            if (value) {
                //searchInfoFactory.catId(null);
                //searchInfoFactory.codeList(value);
                //searchInfoFactory.platformCart(23);
                vm.searchValue = "";
                $location.path(cRoutes.search_advance_param.url + "2/" + encodeURIComponent(value) + '/0/0');
            }
        }

        /**
         * logout.
         */
        function logout() {
            menuService.logout().then(function () {
                $window.location = cCommonRoutes.login.url;
            })
        }
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

    function asideCtrl($scope, $rootScope, $location, menuService, cRoutes, cookieService) {

        $scope.menuInfo = {};
        $scope.initialize = initialize;
        $scope.selectPlatformType = selectPlatformType;
        $scope.goSearchPage = goSearchPage;

        function initialize() {
            menuService.getPlatformType().then(function (data) {
                $scope.menuInfo.platformTypeList = data;
            });
            menuService.getCategoryInfo().then(function (data) {
                $scope.menuInfo.categoryTreeList = data.categoryTreeList;
                $scope.menuInfo.isminimall = data.isminimall;
            });
        }

        /**
         * change your current platformType.
         * @param cType
         */
        function selectPlatformType(cType) {
            menuService.setPlatformType(cType).then(function (data) {
                $rootScope.platformType = {cTypeId: cType.add_name2, cartId: cType.value};
                $scope.menuInfo.categoryTreeList = data.categoryTreeList;
                $rootScope.productUrl = data.productUrl;
            });
        }

        /**
         * 跳转到search页面
         * @param catId:类目名称   影射到高级检索或者feed检索的select默认选中
         * @param type: 1 || 3 = 到高级检索，2 = feed检索
         */
        function goSearchPage(catPath, catId) {
            var catPath = encodeURIComponent(catPath);
            switch ($rootScope.platformType.cTypeId) {
                case "MT": // 已不使用
                    $location.path(cRoutes.search_advance_param.url + "1/" + catPath + "/" + catId);
                    break;
                case "TH":
                    $location.path(cRoutes.feed_product_list_param.url + "1/" + catPath);
                    break;
                default:
                    $location.path(cRoutes.search_advance_param.url + "3/" + $rootScope.platformType.cartId + "/" + catId + "/" + catPath);
                    break;
            }
        }
    }

    return angularAMD.bootstrap(mainApp);
});