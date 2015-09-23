/**
 * @Name:    userService.js
 * @Date:    2015/1/30
 *
 * @User:    Edward
 * @Version: 1.0.0
 */

define (function (require) {

    var mainApp = require ("modules/app/app.module");
    require ('components/services/translate.service');
    require ('components/services/cookie.service');

    mainApp.service ('userService', ['$q', '$rootScope', '$location', 'cookieService', 'translateService',
        function ($q, $rootScope, $location, cookieService, translateService) {

            var _ = require ('underscore');
            var CURRENT_NAVIGATION_INFO = 'core.currentNavigationInfo';
            var userInfo = {
                'isLogin': false,
                'userName': '',
                'selCompanyId': '',
                'selLanguage': '',
                'selMenu': '',
                'selChannel': '',
                'menuList': [],
                'navigationList': [],
                'pagePermissionList': [],
                'languageList': [],
                'userConfig': [],

                // 可用仓库
                // [{store_id:number,store_name:string}]
                'stores': [],
                // 用户可用店铺
                // [{shop_id:string,shop_name:string}]
                'shops': [],
                // 用户可用渠道
                // [{id:string,name:string}]
                'channels': [],
                // 硬件信息
                'hardware': []
            };
            var companyInfo = [];
            var currentNavigation = [];
            var exceptPermissionUrl = ["/app/common/error", "/core/menu/home"];

            /**
             * initialize the userInfo  and companyInfo.
             */
            this.init = function () {
                this.setSelLanguage (cookieService.getSelLanguage ());
                userInfo = {
                    'isLogin': false,
                    'userName': '',
                    'selCompanyId': '',
                    'selLanguage': '',
                    'selMenu': '',
                    'selChannel': '',
                    'menuList': [],
                    'navigationList': [],
                    'pagePermissionList': [],
                    'languageList': [],
                    'userConfig': [],

                    // 可用仓库
                    // [{store_id:number,store_name:string}]
                    'stores': [],
                    // 用户可用店铺
                    // [{shop_id:string,shop_name:string}]
                    'shops': [],
                    // 用户可用渠道
                    // [{id:string,name:string}]
                    'channels': [],
                    // 硬件信息
                    'hardware': []
                };
                companyInfo = [];
                sessionStorage.setItem (CURRENT_NAVIGATION_INFO, JSON.stringify (currentNavigation));
            };

            /**
             * set userInfo.
             * @param object
             */
            this.setUserInfo = function (object) {
                this.setIsLogin (object.isLogin);
                this.setUserName (object.userName);
                this.setSelCompany (object.selCompanyId);
                this.setSelLanguage (object.selLang);
                this.setSelMenu (object.selMenu);
                this.setSelChannel (object.selChannel);
                this.setUserConfig (object.cmsProductFields);
                var userInfo = getUserInfo ();
                userInfo.menuList = formatMenuList (object.menuList);
                userInfo.pagePermissionList = object.pagePermissionList;
                userInfo.navigationList = object.navigationList;
                userInfo.shops = object.shops;
                userInfo.stores = object.stores;
                userInfo.channels = object.channels;
                userInfo.hardware = object.hardware;
                userInfo.languageList = object.languageList;
            };

            /**
             * set companyInfo.
             * @param object
             */
            this.setCompanyInfo = function (object) {
                companyInfo = object;
            };

            /**
             * set the navigation info for current page.
             * @param object
             */
            this.setCurrentNavigationInfo = function () {
                _.each (getUserInfo ().navigationList, function (navigationInfo) {
                    if ($location.path ().indexOf(navigationInfo.permissionUrl) > -1) {
                        sessionStorage.setItem (CURRENT_NAVIGATION_INFO, JSON.stringify (navigationInfo.navigationList));
                    }
                });
            };

            /**
             * get the navigation info from sessionStorage.
             * @returns {*}
             */
            this.getCurrentNavigationInfo = function () {
                if (_.isNull (sessionStorage.getItem (CURRENT_NAVIGATION_INFO))) {
                    return currentNavigation;
                } else {
                    return JSON.parse (sessionStorage.getItem (CURRENT_NAVIGATION_INFO));
                }
            };

            /**
             * return true when the user has been login.else return false.
             * @returns {boolean}
             */
            this.isLogin = function () {
                return getUserInfo ().isLogin;
            };

            /**
             * set the value to userInfo.isLogin and cookie.
             * @param value
             */
            this.setIsLogin = function (value) {
                var userInfo = getUserInfo ();
                userInfo.isLogin = value;
                if (value) {
                    cookieService.setLoginTime (_.now ());
                }
                //sessionStorage.setItem(USER_INFO, JSON.stringify(userInfo));
            };

            /**
             * return the userInfo's userName.
             * @returns {string}
             */
            this.getUserName = function () {
                return getUserInfo ().userName;
            };

            /**
             * set the userInfo's userName.
             * @param value
             */
            this.setUserName = function (value) {
                var userInfo = getUserInfo ();
                if (!_.isEmpty (value))
                    userInfo.userName = value;
            };

            /**
             * return true when userInfo.selCompany > 0 else return false.
             * @returns {boolean}
             */
            this.isSelCompany = function () {
                return !_.isEmpty (getUserInfo ().selCompanyId.toString ());
            };

            /**
             * return the userInfo's selected companyId.
             * @returns {string}
             */
            this.getSelCompany = function () {
                return getUserInfo ().selCompanyId;
            };

            /**
             * set the userInfo's selected companyId.
             * @param value
             */
            this.setSelCompany = function (value) {
                var userInfo = getUserInfo ();
                if (!_.isEmpty (value.toString ())) {
                    userInfo.selCompanyId = value;
                    cookieService.setSelCompany (value);
                }
            };

            /**
             * return the userInfo's selected language.
             * @returns {string}
             */
            this.getSelLanguage = function () {
                return getUserInfo ().selLanguage;
            };

            /**
             * update the selected language,and set the value to userInfo,cookie.
             * @param value
             * @returns {r.promise|promise|qFactory.Deferred.promise|x.ready.promise|fd.g.promise}
             */
            this.setSelLanguage = function (value) {
                var defer = $q.defer ();
                var userInfo = getUserInfo ();
                value = translateService.setLanguage (value);
                if (!_.isEmpty (value)) {
                    userInfo.selLanguage = value;
                    cookieService.setSelLanguage (value);
                }
                defer.resolve (value);
                return defer.promise;
            };

            /**
             * get the userInfo's selMenu.
             * @returns {string}
             */
            this.getSelMenu = function () {
                //setDefaultSelMenu ();
                return getUserInfo ().selMenu;
            };

            /**
             * set userInfo's selMenu.
             * @param value
             */
            this.setSelMenu = function (value) {
                var userInfo = getUserInfo ();
                userInfo.selMenu = value;
            };

            /**
             * get userInfo's selChannel.
             * @returns {string|*}
             */
            this.getSelChannel = function () {
                return getUserInfo ().selChannel;
            };

            /**
             * set userInfo's selChannel.
             * @param value
             */
            this.setSelChannel = function (value) {
                var userInfo = getUserInfo ();
                if (!_.isEmpty (value.toString ())) {
                    userInfo.selChannel = value;
                    cookieService.setSelChannel (value);
                }
            };

            /**
             * get userInfo's user config.
             * @returns {Array|*}
             */
            this.getUserConfig = function () {
                return getUserInfo().userConfig;
            };

            /**
             * set userInfo's userConfig.
             * @param value
             */
            this.setUserConfig = function (value) {
                var userInfo = getUserInfo ();
                if (!_.isEmpty (value.toString ())) {
                    userInfo.userConfig = value;
                }
            };

            /**
             * return userInfo's pagePermissionList.
             * @returns {*}
             */
            this.getPagePermissionList = function () {
                return getUserInfo ().pagePermissionList;
            };

            /**
             * return true when userInfo.pagePermissionList is empty,
             * or userInfo.pagePermissionList has contains the value,
             * else return false.
             * @param value
             * @returns {boolean}
             */
            this.hasPagePermission = function (value) {
                var pathList = value.split ('/');
                var checkValue = '/' + pathList[1] + '/' + pathList[2] + '/' + pathList[3];
                var userInfo = getUserInfo ();
                if (_.isEmpty (userInfo.pagePermissionList))
                    return true;
                else {
                    var pagePermissionList = userInfo.pagePermissionList;
                    if (this.isLogin ()
                        && this.isSelCompany ()
                        && (_.contains (pagePermissionList, checkValue)
                        || _.contains (exceptPermissionUrl, checkValue)))
                        return true;
                    else
                        return false;

                }
            };

            /**
             * return userInfo's navigationList.
             * @returns {*}
             */
            this.getNavigationList = function () {
                return _.isEmpty (getUserInfo ().navigationList) ? [] : getUserInfo ().navigationList;
            };

            /**
             * set navigationList.
             * @param values
             */
            this.setNavigationList = function (values) {
                var userInfo = getUserInfo ();
                userInfo.navigationList = values;
            };

            /**
             * return the userInfo.
             * @returns {{userName: string, isLogin: boolean, selCompany: number, selLanguage: string, menuList: Array, pagePermissionList: Array, token: string}}
             */
            function getUserInfo () {
                return userInfo;
            }

            /**
             * return default selMenu.
             * @returns {string|*}
             */
            function setDefaultSelMenu () {
                var defaultMenuName = getUserInfo ().menuList[0].menuName;
                var userInfo = getUserInfo ();

                if (_.isEmpty (userInfo.selMenu))
                    userInfo.selMenu = defaultMenuName;
            }

            /**
             * check menu has subMenuList,and set the  hasSubMenuList.
             * @param menuList
             * @returns {*}
             */
            function formatMenuList (menuList) {
                _.each (menuList, function (menuInfo) {
                    menuInfo.hasSubMenuList = false;
                    if (!_.isEmpty (menuInfo.subMenuList)) {
                        menuInfo.hasSubMenuList = true;
                        formatMenuList (menuInfo.subMenuList);
                    }
                });
                return menuList;
            }

            /**
             * 获取用户可用的店铺
             */
            this.getShops = function () {
                return userInfo.shops;
            };

            /**
             * 获取用户可用的仓库
             */
            this.getStores = function () {
                return userInfo.stores;
            };

            /**
             * 获取用户可用的渠道
             */
            this.getChannels = function () {
                return userInfo.channels;
            };

            /**
             * 获取一组硬件的配置信息
             * @returns [{Object}]
             */
            this.getHardware = function () {
                return userInfo.hardware;
            };
        }]);
});