/**
 * @Name:    errorController.js
 * @Date:    2015/2/16
 *
 * @User:    Edward
 * @Version: 1.0.0
 */

define (function (require) {

    var mainApp = require ("modules/app/app.module");
    require ('components/services/message.service');
    require ('modules/app/common/common.service');
    require ('components/services/cookie.service');

    mainApp.controller ('errorController', ['$scope', 'messageService', 'messageKeys', 'commonService', '$translate', 'cookieService',
        function ($scope, messageService, messageKeys, commonService, $translate, cookieService) {

            /**
             * 初始化显示Msg
             */
            $scope.initialize = function () {
                $translate.use (cookieService.getSelLanguage());
                $scope.errorMessage = messageService.getMessageByKey (messageKeys.ID_000001)
            };

            /**
             * 退出系统.
             */
            $scope.doLogout = function () {
                commonService.doLogout ();
            };
        }]);
});