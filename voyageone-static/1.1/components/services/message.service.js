/**
 * @Name:    message.service.js
 * @Date:    2015/4/29
 *
 * @User:    Edward
 * @Version: 1.0.0
 */

define(function (require) {

    var mainApp = require('components/app');
    require("components/services/alert.service");
    require("components/services/cookie.service");
    require("components/services/language.service");
    require ('components/services/permission.service');

    var messageType = {
        // success.
        "messageType_0": 0,
        // validation error.
        "messageType_1": 1,
        // business exception error.
        "messageType_2": 2,
        // session error.
        "messageType_3": 3,
        // token error.
        "messageType_4": 4,
        // system exception error.
        "messageType_5": 5,
        // business warning.
        "messageType_6": 6
    };

    var messageKeys = {

        ID_200002: 'ID_200002',
        ID_200003: 'ID_200003',
        ID_200004: 'ID_200004',
        ID_200005: 'ID_200005',
        ID_200006: 'ID_200006',
        ID_200007: 'ID_200007',
        ID_000001: 'ID_000001',
        ID_000002: 'ID_000002',
        ID_000003: 'ID_000003',
        ID_000004: 'ID_000004',
        ID_000005: 'ID_000005'

    };

    var statusCodes = {
        success: 200,
        validError: 900,
        error: 500,
        unauthorized: 401
    };

    function messageService(
        languageType,
        alertService,
        cookieService,
        permissionService,
        commonRoute,
        vAlert,
        $location
    ) {

        var _ = require("underscore");
        var messageEn = require("components/messages/message.en");
        var messageZh = require("components/messages/message.zh");
        var commonUtil = require('components/util/commonUtil');
        var messageJoiner = ": ";
        var defaultMessage = "";
        var that = this;

        this.formatMessage = function (key, value) {
            var message = "";
            if (_.isEmpty(key))
                message = value;
            else if (_.isEmpty(value))
                message = defaultMessage;
            else
                message = key + messageJoiner + value;

            return message;
        };

        this.getMessage = function (key, language) {

            var _ = require("underscore");
            var message = "";

            switch (language) {
                case languageType.en:
                    message = _.property(key)(messageEn);
                    break;
                case languageType.zh:
                    message = _.property(key)(messageZh);
                    break;
                default :
                    message = _.property(key)(messageEn);
                    break;
            }

            return this.formatMessage(key, message);
        };

        this.getMessageByKey = function (key) {

            var _ = require("underscore");
            var message = "";

            if (!_.isEmpty(key)) {
                message = this.getMessage(key, cookieService.getSelLanguage());
            }

            return message;
        };

        this.alertMessage = function (key) {
            var message = this.getMessage(key, cookieService.getSelLanguage());
            alertService.renderWarningMessage(message);
        };

        /**
         * check messageType.
         */
        this.checkMessageType = function (res) {

            var messageTypeId = res.messageType;
            var message = that.formatMessage(res.messageCode, res.message);

            // clear all message.
            alertService.clearMessage();

            switch (messageTypeId) {
                case messageType.messageType_0:
                    if (!_.isEmpty(message))
                        alertService.renderSuccessMessage(message);
                    permissionService.setPermissions(res.permissions);

                    return statusCodes.success;

                case messageType.messageType_1:

                    return statusCodes.validError;

                case messageType.messageType_2:
                    alertService.renderErrorMessage(message);

                    return statusCodes.error;

                case messageType.messageType_3:
                    cookieService.removeAll();
                    commonUtil.goToLoginPage();
                    return statusCodes.unauthorized;

                case messageType.messageType_4:
                    (vAlert || alert)(message);

                    return statusCodes.error;

                case messageType.messageType_5:
                    cookieService.removeAll();
                    //commonUtil.goToErrorPage ();
                    $location.path(commonRoute.common_error_index.hash);

                    return statusCodes.error;

                case messageType.messageType_6:
                    alertService.renderWarningMessage(message);

                    return statusCodes.success;
            }

            return 0;
        };
    }

    mainApp
        .constant("messageType", messageType)
        .constant("messageKeys", messageKeys)
        .constant("statusCodes", statusCodes)

        .service("messageService", ["languageType", "alertService", "cookieService", "permissionService", "commonRoute",
            "vAlert", "$location", messageService]);
});
