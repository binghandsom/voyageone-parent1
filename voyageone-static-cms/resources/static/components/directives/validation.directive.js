/**
 * @Name:    validation.directive.js
 * @Date:    2015/6/26
 *
 * @User:    Edward
 * @Version: 1.0.0
 */

define(function(require) {
    var mainApp = require('components/app');
    require ('modules/app/services/user.service');
    require ('components/services/language.service');

    // define a directive on mainApp for the permission on page.
    mainApp.directive('vaCheck', ['$location', 'userService', 'languageType',
        function($location, userService, languageType){

            var message_en = {
                "required": "This field is required",
                "maxlength": "This field requires a max length of @maxlength",
                "currency": "Please input the right format: 2, 2.0, 2.00",
                "urlKey": "Please input the right format: includes letter, number and -",
                "number": "Please input the right format: 1, 11, 0",
                "date": "Please input valid date"
            };

            var message_zh = {
                "required": "该栏不能为空",
                "maxlength": "该栏的输入长度不能超过@maxlength",
                "currency": "请输入正确的格式: 2, 2.0, 2.00",
                "urlKey": "请输入正确的格式: 只包括英数字和-",
                "number": "请输入正确的格式: 1, 11, 0",
                "date": "请输入有效的日期"
            };

            var validationType = {
                "required": "required",
                "maxlength": "maxlength",
                "currency": "currency",
                "urlKey": "urlKey",
                "number": "number",
                "date": "date"
            };

            return {
                restrict: 'EA',
                link : function(scope, iElement, iAttr) {

                    var objMessage = getMessageObject();
                    var message = "";

                    switch (iAttr.checkType) {
                        case validationType.required:
                            message = objMessage.required;
                            break;
                        case validationType.maxlength:
                            message = objMessage.maxlength.replace("@maxlength",iAttr.checkParam);
                            break;
                        case validationType.currency:
                            message = objMessage.currency;
                            break;
                        case validationType.urlKey:
                            message = objMessage.urlKey;
                            break;
                        case validationType.number:
                            message = objMessage.number;
                            break;
                        case validationType.date:
                            message = objMessage.date;
                            break;
                        default :
                            break;
                    }

                    $(iElement).addClass("text-danger");
                    $(iElement)[0].innerHTML = message;

                }
            };

            /**
             * 根据用户设定的language返回validation类型.
             * @returns {{required: string, maxlength: string}}
             */
            function getMessageObject() {
                switch (userService.getSelLanguage()) {
                    case languageType.en:
                        return message_en;
                        break;
                    case languageType.zh:
                        return message_zh;
                        break;
                    default :
                        return message_en;
                        break;
                }
            }
        }]);

    return mainApp;
});