/**
 * @Name:    translate-en.js
 * @Date:    2015/1/30
 *
 * @User:    Edward
 * @Version: 1.0.0
 */

define (function (require) {
    var mainApp = require ('components/app');
    require ('components/services/language.service');

    mainApp.config (["$translateProvider", "languageType",
        function ($translateProvider, languageType) {

            $translateProvider.translations (languageType.en,
                {
                    CORE_BUTTON_SIGN_IN: 'Sign In',
                    CORE_TXT_TITLE_COMPANY: 'Company',
                    CORE_BUTTON_LOG_OUT: 'Log Out',
                    CORE_TXT_TITLE_VOYAGE_ONE: 'Voyage One',
                    CORE_BUTTON_CHANGE_PASSWORD: 'Change Password',
                    CORE_BUTTON_OK: 'YES',
                    CORE_BUTTON_CANCEL: 'NO',
                    CORE_TXT_ALERT: 'Alert',
                    CORE_TXT_CONFIRM: 'Confirm',

                    CORE_TXT_MESSAGE_CHANGE_TO_EFFECTIVE: 'Do you want to change the effective or uneffective ?',
                    CORE_TXT_MESSAGE_BATCH_DELETE : 'Do you want to batch delete operation ? ',

                    UNDER_LINE: '_',

                    CORE_NODATA: 'No data available in table',

                    CORE_BTN_ADD: 'ADD',
                    CORE_BTN_OK: 'OK',
                    CORE_BTN_CANCEL: 'CANCEL',
                });
        }]);
});
