/**
 * @Name:    messageUtil.js
 * @Date:    2015/3/10
 *
 * @User:    Edward
 * @Version: 1.0.0
 */

define(function(require) {

    var messageUtil = {};

    var messageEn = require('components/messages/message_en');
    var messageZh = require('components/messages/message_zh');
    var messageJoiner = ': ';
    var defaultMessage = '';

    messageUtil.messageKeys = require('components/messages/message_key');

    messageUtil.getMessage = function(key, languageType) {

        var _ = require('underscore');
        var languageUtil = require('components/util/languageUtil');
        var message = '';

        switch (languageType) {
            case languageUtil.languageTypes.en:
                message = _.property(key)(messageEn);
                break;
            case languageUtil.languageTypes.zh:
                message = _.property(key)(messageZh);
                break;
            default :
                message = _.property(key)(messageEn);
                break;
        }

        return messageUtil.formatMessage(key, message);
    };

    messageUtil.formatMessage = function(key, value) {

        var message = '';
        if(_.isEmpty(key))
            message = value;
        else if (_.isEmpty(value))
            message = defaultMessage;
        else
            message = key + messageJoiner + value;

        return message;
    };

    // define alertStyle.
    messageUtil.alertStyle = {
        'validation_error': 'errorMes_error',
        'error': 'errorMes_error',
        'success': 'errorMes_success',
        'warning': 'errorMes_warn'
    };

    // define the messageType.
    messageUtil.messageType = {
        // success.
        'messageType_0': 0,
        // validation error.
        'messageType_1': 1,
        // business exception error.
        'messageType_2': 2,
        // session error.
        'messageType_3': 3,
        // token error.
        'messageType_4': 4,
        // system exception error.
        'messageType_5': 5,
        // business warning.
        'messageType_6': 6
    };
    return messageUtil;
});