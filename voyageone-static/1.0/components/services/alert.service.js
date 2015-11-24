/**
 * @Name:    alertService.js
 * @Date:    2015/1/30
 *
 * @User:    Edward
 * @Version: 1.0.0
 */

define ([
    "components/app",
    "underscore"
], function (app, _) {

    app.constant ("alertType", {
        "validation_error": "danger",
        "error": "danger",
        "success": "success",
        "warning": "warning"
    });

    // define alertService to use alert message on view.
    app.service ('alertService', ['$rootScope', 'alertType', alertService]);

    // alertService 声明实体
    function alertService($rootScope, alertType) {

        $rootScope.closeAlert = function(index) {
            $rootScope.alerts.splice(index, 1);
        };

        // clear all message.
        this.clearMessage = function () {
            $rootScope.alerts = [];
        };

        /**
         * set validate error info.
         * @param validationErrors
         * @constructor
         */
        this.setValidationErrors = function (validationErrors) {

            if (!_.isArray(validationErrors)) return;

            $rootScope.alerts = _.map(validationErrors, function(err) {
                // TODO 还无法国际化 field
                return {
                    type: alertType.validation_error,
                    msg: err.field + ':' + err.message
                };
            });
        };

        /**
         * set the error info.
         * @param message
         * @constructor
         */
        this.renderErrorMessage = function (message) {
            $rootScope.alerts = [{'type': alertType.error, 'msg': formatMessage(message)}];
        };

        /**
         * set the warning info.
         * @param message
         * @constructor
         */
        this.renderWarningMessage = function (message) {
            $rootScope.alerts = [{'type': alertType.warning, 'msg': formatMessage(message)}];
        };

        /**
         * set the success info.
         * @param message
         * @constructor
         */
        this.renderSuccessMessage = function (message) {
            $rootScope.alerts = [{'type': alertType.success, 'msg': formatMessage(message)}];
        };

        /**
         * edit the message.
         * @param message
         * @returns {string}
         */
        function formatMessage (message) {

            return _.isArray (message) ? _.reduce (message, function (memo, msg) {
                return memo + msg + "<br/>";
            }, "") : message;
        }
    }
});