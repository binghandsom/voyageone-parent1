/**
 * @Name:    indexService.js
 * @Date:    2015/2/13
 *
 * @User:    Edward
 * @Version: 1.0.0
 */

define (function (require) {

    var mainApp = require ("modules/app/app.module");
    require ('modules/app/services/user.service');
    require ('modules/app/services/master.service');
    require ('components/services/ajax.service');

    mainApp.service ('commonService', ['$rootScope', '$q', 'ajaxService', 'userService', 'cookieService', 'commonAction', 'masterService', 'mainMenu',
        function ($rootScope, $q, ajaxService, userService, cookieService, commonAction, masterService, mainMenu) {

            var commonUtil = require ('components/util/commonUtil');
            var _ = require ('underscore');

            /**
             * get userInfo  from server.
             * @returns {r.promise|promise|qFactory.Deferred.promise|x.ready.promise|fd.g.promise}
             */
            this.doGetUserInfo = function () {
                var defer = $q.defer ();

                ajaxService.ajaxPostOnlyByUrl (commonAction.common_doGetUserInfo)
                    .then (function (response) {
                    var userInfo = response.data.userInfo;
                    userService.setUserInfo (userInfo);
                    userInfo.loginTime = cookieService.getLoginTime ();
                    userInfo.selMenu = userService.getSelMenu ();
                    userInfo.selChannel = userService.getSelChannel ();
                    userInfo.selLanguage = userService.getSelLanguage ();
                    userService.setCompanyInfo (userInfo.companyList);
                    defer.resolve (userInfo);
                });
                return defer.promise;
            };

            /**
             * try get user info using ajax ( always remoute not local cache )
             */
            this.tryGetUserInfo = function() {
              return ajaxService
              .ajaxPostOnlyByUrl(commonAction.common_doGetUserInfo)
              .then(function (response) {
                return response.data.userInfo;
              });
            };

            /**
             * logout clear hte cookie, and init the userInfo.
             * @returns {r.promise|promise|qFactory.Deferred.promise|x.ready.promise|fd.g.promise}
             */
            this.doLogout = function () {
                var defer = $q.defer ();

                ajaxService.ajaxPostOnlyByUrl (commonAction.common_doLogout)
                    .then (function () {
                    sessionStorage.clear ();
                    userService.init ();
                    cookieService.init ();
                    defer.resolve ('');
                })
                    .then (function () {
                    commonUtil.goToLoginPage ();
                });
                return defer.promise;
            };

            /**
             * update the selected language to server.
             * @param language
             * @returns {r.promise|promise|qFactory.Deferred.promise|x.ready.promise|fd.g.promise}
             */
            this.doSelectLanguage = function (language) {
                var defer = $q.defer ();
                userService.setSelLanguage (language)
                    .then (function () {
                    defer.resolve ('');
                });
                return defer.promise;
            };

            /**
             * change menu to userInfo.
             * @param menu
             */
            this.selectMenu = function (menu) {
                var defer = $q.defer ();
                var data = {};
                data.id = userService.getSelCompany();
                data.channelId = userService.getSelChannel();
                data.systemName = menu;
                this.doSelectCompany(data).then(function(response) {
                    userService.setSelMenu (menu);
                    defer.resolve (response);
                });
                return defer.promise;
            };

            /**
             * change channel to userInfo.
             * @param channel
             */
            this.selectChannel = function (channel) {
                var defer = $q.defer ();
                var data = {};
                data.id = userService.getSelCompany();
                data.channelId = channel;
                data.systemName = userService.getSelMenu();
                this.doSelectCompany(data).then(function(response) {
                    userService.setSelChannel (channel);
                    defer.resolve (response);
                });
                return defer.promise;
            };

            /**
             * return this page's navigationList.
             * @returns {*}
             */
            this.getCurrentNavigationInfo = function () {

                return userService.getCurrentNavigationInfo ();
            };

            /**
             * 取得系统级别的master数据.
             * @param channelId
             * @param systemName
             * @param getFromSessionFlag
             */
            this.doGetMasterInfo = function (channelId, systemName, getFromSessionFlag) {
                switch (systemName) {
                    case mainMenu.CMS:
                        this.doGetCMSMasterInfo(channelId, getFromSessionFlag);
                        break;
                    default:
                        break;
                }
            };

            /**
             * set the selected company.
             * @param data
             */
            this.doSelectCompany = function (data) {
                var defer = $q.defer ();
                ajaxService.ajaxPostWithData (data, commonAction.core_account_company_doSelectCompany)
                    .then (function (response) {
                    defer.resolve (response);
                });
                return defer.promise;
            };

            /**
             * 取得CMS使用的Master数据.
             * @param channelId
             * @returns {*}
             */
            this.doGetCMSMasterInfo = function (channelId, getFromSessionFlag) {

                // 从session中取得master数据.
                if (!_.isEmpty(masterService.getCmsMasterData()) && getFromSessionFlag) {

                    // 将master数据导入$rootScope.
                    setMasterInfoToSession (masterService.getCmsMasterData());
                }
                // 从服务器端取得master数据.
                else {
                    var data = {};
                    data.channelId = channelId;
                    // TODO 以后根据用户当前的选择
                    ajaxService.ajaxPostWithData (data, commonAction.cms_common_master_doGetMasterDataList)
                        .then (function (response) {
                        //将CMS的master数据保存到session中.
                        masterService.setCmsMasterData(response.data);

                        // 将master数据导入$rootScope.
                        setMasterInfoToSession (response.data);
                    });
                }
            }

            /**
             * 将取得的master数据保存在session中.
             * @param data
             */
            function setMasterInfoToSession(data) {
                $rootScope.cmsMaster = data;

                angular.forEach(data.masterValue, function(masterInfo) {

                    switch (masterInfo.typeId.toString()) {
                        case "1":
                            $rootScope.cmsMaster.imageUrl = masterInfo.value;
                            break;
                        case "2":
                            $rootScope.cmsMaster.displayImagesCount = masterInfo.value;
                            break;
                        case "3":
                            $rootScope.cmsMaster.usSaleChannels = commonUtil.splitByChar(masterInfo.value, ',');
                            break;
                        case "4":
                            $rootScope.cmsMaster.cnSaleChannels = commonUtil.splitByChar(masterInfo.value, ',');
                            break;
                        case "5":
                            $rootScope.cmsMaster.defaultPriceDifference = masterInfo.value;
                            break;
                        case "6":
                            $rootScope.cmsMaster.freeShippingFee = masterInfo.value;
                            break;
                        case "7":
                            $rootScope.cmsMaster.priceProfitCoefficient = masterInfo.value;
                            break;
                        case "8":
                            $rootScope.cmsMaster.amazonFee = masterInfo.value;
                            break;
                        case "9":
                            $rootScope.cmsMaster.amazonShippingAdjustmentFee = masterInfo.value;
                            break;
                        case "10":
                            $rootScope.cmsMaster.defaultShowCountryInfo = masterInfo.value;
                            break;
                        case "11":
                            $rootScope.cmsMaster.priceSettingIsActive = _.isEqual(masterInfo.value, '1') ? true : false;
                            break;
                        case "12":
                            $rootScope.cmsMaster.tmallUrl = masterInfo.value;
                            break;
                        //case "14":
                        //    $rootScope.cmsMaster.usProductColumns = masterInfo.value != "" ? masterInfo.value.split(',') : [];
                        //    break;
                        //case "15":
                        //    $rootScope.cmsMaster.cnProductColumns = masterInfo.value != "" ? masterInfo.value.split(',') : [];
                        //    break;

                    }
                });

            }
        }]);
});
