define([
    'angularAMD',
    'angular',
    'underscore',
    'json!modules/cms/routes.json',
    'json!modules/cms/actions.json',
    'json!modules/cms/translate/en.json',
    'json!modules/cms/translate/zh.json',
    'voyageone-angular-com',
    'voyageone-com',
    'angular-block-ui',
    'angular-ui-bootstrap',
    'angular-ngStorage',
    'angular-route',
    'angular-sanitize',
    'angular-animate',
    'angular-translate',
    'angular-cookies',
    'angular-file-upload',
    'filestyle',
    'notify'
], function (angularAMD, angular, _, cRoutes, cActions, enTranslate, zhTranslate) {

    var mainApp = angular.module('voyageone.cms', [
            'ngRoute',
            'ngAnimate',
            'ngCookies',
            'ngSanitize',
            'pascalprecht.translate',
            'blockUI',
            'voyageone.angular',
            'voyageone.angular.vresources',
            'ui.bootstrap',
            'ngStorage',
            'angularFileUpload'
        ])

        // define
        .constant('$actions', cActions)
        .constant('cActions', cActions)
        .constant('cRoutes', cRoutes)
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
            "channel": {
                "url": "/channel.html"
            },
            "application": {
                "modules": "/modules/",
                "url": "/app.html#/home"
            }
        })

        // router config.
        .config(function ($routeProvider) {
            return _.each(cRoutes, function (module) {
                return $routeProvider.when(module.hash, angularAMD.route(module));
            });
        })

        // translate config.
        .config(function ($translateProvider, cLanguageType) {

            _.forEach(cLanguageType, function (type) {
                $translateProvider.translations(type.name, type.value);
            });
        })

        // menu service.
        .service('menuService', menuService)

        // searchInfo factory.
        //.factory('searchInfoFactory', searchInfoFactory)

        .controller('appCtrl', appCtrl)

        // menu.header.
        .controller('headerCtrl', headerCtrl)

        // menu.breadcrumbs.
        .controller('breadcrumbsCtrl', breadcrumbsCtrl)

        // menu.aside.
        .controller('asideCtrl', asideCtrl);

    /*function searchInfoFactory() {
        var searchInfo = {
            catId: null,
            productStatus: [],
            platformCart: null,
            platformStatus: [],
            labelType: [],
            priceType: null,
            priceStart: null,
            priceEnd: null,
            createTimeStart: null,
            createTimeTo: null,
            publishTimeStart: null,
            publishTimeTo: null,
            compareType: null,
            inventory: null,
            brand: null,
            promotion: null,
            codeList: null,
            sortOneName: null,
            sortOneType: null,
            sortTwoName: null,
            sortTwoType: null,
            sortThreeName: null,
            sortThreeType: null
        };

        return {
            catId: function (value) {
                return value !== undefined ? searchInfo.catId = value : searchInfo.catId;
            },
            productStatus: function (value) {
                return value !== undefined ? searchInfo.productStatus = value : searchInfo.productStatus;
            },
            platformCart: function (value) {
                return value !== undefined ? searchInfo.platformCart = value : searchInfo.platformCart;
            },
            platformStatus: function (value) {
                return value !== undefined ? searchInfo.platformStatus = value : searchInfo.platformStatus;
            },
            labelType: function (value) {
                return value !== undefined ? searchInfo.labelType = value : searchInfo.labelType;
            },
            priceType: function (value) {
                return value !== undefined ? searchInfo.priceType = value : searchInfo.priceType;
            },
            priceStart: function (value) {
                return value !== undefined ? searchInfo.priceStart = value : searchInfo.priceStart;
            },
            priceEnd: function (value) {
                return value !== undefined ? searchInfo.priceEnd = value : searchInfo.priceEnd;
            },
            createTimeStart: function (value) {
                return value !== undefined ? searchInfo.createTimeStart = value : searchInfo.createTimeStart;
            },
            createTimeTo: function (value) {
                return value !== undefined ? searchInfo.createTimeTo = value : searchInfo.createTimeTo;
            },
            publishTimeStart: function (value) {
                return value !== undefined ? searchInfo.publishTimeStart = value : searchInfo.publishTimeStart;
            },
            publishTimeTo: function (value) {
                return value !== undefined ? searchInfo.publishTimeTo = value : searchInfo.publishTimeTo;
            },
            compareType: function (value) {
                return value !== undefined ? searchInfo.compareType = value : searchInfo.compareType;
            },
            inventory: function (value) {
                return value !== undefined ? searchInfo.inventory = value : searchInfo.inventory;
            },
            brand: function (value) {
                return value !== undefined ? searchInfo.brand = value : searchInfo.brand;
            },
            promotion: function (value) {
                return value !== undefined ? searchInfo.promotion = value : searchInfo.promotion;
            },
            codeList: function (value) {
                return value !== undefined ? searchInfo.codeList = value : searchInfo.codeList;
            },
            sortOneName: function (value) {
                return value !== undefined ? searchInfo.sortOneName = value : searchInfo.sortOneName;
            },
            sortOneType: function (value) {
                return value !== undefined ? searchInfo.sortOneType = value : searchInfo.sortOneType;
            },
            sortTwoName: function (value) {
                return value !== undefined ? searchInfo.sortTwoName = value : searchInfo.sortTwoName;
            },
            sortTwoType: function (value) {
                return value !== undefined ? searchInfo.sortTwoType = value : searchInfo.sortTwoType;
            },
            sortThreeName: function (value) {
                return value !== undefined ? searchInfo.sortThreeName = value : searchInfo.sortThreeName;
            },
            sortThreeType: function (value) {
                return value !== undefined ? searchInfo.sortThreeType = value : searchInfo.sortThreeType;
            },
            searchInfo: function (object) {
                return object != undefined ? searchInfo = value : searchInfo;
            }
        }
    }*/

    function appCtrl($scope, $window, translateService) {

        var isIE = !!navigator.userAgent.match(/MSIE/i);
        isIE && angular.element($window.document.body).addClass('ie');
        isSmartDevice($window) && angular.element($window.document.body).addClass('smart');

        // config
        $scope.app = {
            name: 'VoyageOne',
            version: 'Version 2.0.0',
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
            $(window).load(function () {
            });

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

    function menuService($q, ajaxService, cookieService, translateService, cActions) {

        this.getMenuHeaderInfo = getMenuHeaderInfo;
        this.setMenu = setMenu;
        this.clearChannel = clearChannel;
        this.setLanguage = setLanguage;
        this.setCategoryType = setCategoryType;
        this.logout = logout;
        this.getCategoryInfo = getCategoryInfo;
        this.getCategoryType = getCategoryType;
        this.getCategoryTree = getCategoryTree;

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
                    var userlanguage = translateService.getBrowserLanguage();
                    if (!_.isEmpty(cookieService.language()))
                        userlanguage = cookieService.language();
                    else if (!_.isEmpty(data.userInfo.language))
                        userlanguage = data.userInfo.language;
                    translateService.setLanguage(userlanguage);

                    // 设置画面用户显示的语言
                    _.forEach(data.languageList, function (language) {

                        if (_.isEqual(userlanguage, language.add_name2)) {
                            data.userInfo.language = language.add_name1;
                        }
                    });

                    data.userInfo.application = cookieService.application();
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
            ajaxService.post(cActions.core.home.menu.selectLanguage, {"language": language.name})
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
            var defer = $q.defer();
            ajaxService.post(cActions.cms.home.menu.getCategoryInfo)
                .then(function (response) {
                    defer.resolve(response.data);
                });
            return defer.promise;
        }

        /**
         * get categoryTypeList.
         * @returns {*}
         */
        function getCategoryType() {
            var defer = $q.defer();
            ajaxService.post(cActions.cms.home.menu.getCategoryType)
                .then(function (response) {
                    defer.resolve(response.data);
                });
            return defer.promise;
        }

        /**
         * get categoryTree.
         * @returns {*}
         */
        function getCategoryTree() {
            var defer = $q.defer();
            ajaxService.post(cActions.cms.home.menu.getCategoryTree)
                .then(function (response) {
                    defer.resolve(response.data);
                });
            return defer.promise;
        }

        /**
         *
         * set categoryType.
         * @param cTypeId
         * @returns {*}
         */
        function setCategoryType(cType) {
            var defer = $q.defer();
            ajaxService.post(cActions.cms.home.menu.setCategoryType, {"cTypeId": cType.cTypeId, "cartId": cType.cartId})
                .then(function (response) {
                    defer.resolve(response.data);
                });
            return defer.promise;
        }

    }

    function headerCtrl($scope, $window, $location, menuService, cRoutes, cCommonRoutes) {
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
                vm.menuList = data.menuList;
                vm.languageList = data.languageList;
                vm.userInfo = data.userInfo;
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
            })
        }

        /**
         * search by input value.
         */
        function goSearchPage(value) {
            if(value){
                //searchInfoFactory.catId(null);
                //searchInfoFactory.codeList(value);
                //searchInfoFactory.platformCart(23);
                vm.searchValue = "";
                $location.path(cRoutes.search_index_param.url + "2/" + value);
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
                $rootScope.categoryType = data.categoryType;
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

    function asideCtrl($scope, $rootScope, $location, menuService, cRoutes) {

        $scope.menuInfo = {};
        $scope.initialize = initialize;
        $scope.selectCategoryType = selectCategoryType;
        $scope.goSearchPage = goSearchPage;

        function initialize() {
            menuService.getCategoryType().then(function (data) {
                $scope.menuInfo.categoryTypeList = data;
            });
            menuService.getCategoryInfo().then(function (data) {
                $scope.menuInfo.categoryTreeList = data.categoryTreeList;
            });
        }

        /**
         * change your current categoryTYpe.
         * @param cTypeId
         */
        function selectCategoryType(cType) {
            menuService.setCategoryType(cType).then(function (data) {
                $rootScope.categoryType = {cTypeId: cType.cTypeId, cartId: cType.cartId};
                $scope.menuInfo.categoryTreeList = data.categoryTreeList;
            });
        }

        /**
         * 跳转到search页面
         * @param catId
         */
        function goSearchPage(catId) {
            if(catId){
                $location.path(cRoutes.search_index_param.url + "1/" + catId);
            }
        }
    }

    return angularAMD.bootstrap(mainApp);
});