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
                    /** Core system used start **/
                    /** for label and title. **/
                    CORE_TXT_LANGUAGE_ZH: '中文',
                    CORE_TXT_LANGUAGE_EN: 'English',
                    CORE_TXT_LOGIN: 'LOGIN',
                    //CORE_TXT_LOGIN_INFORMATION: 'Please Enter Your Information',
                    CORE_TXT_USER_NAME: 'USERNAME/E-MAIL',
                    CORE_TXT_PASSWORD: 'PASSWORD',
                    CORE_TXT_SYSTEM_ANNOUNCEMENT: 'System Announcement',
                    CORE_TXT_COMPANY_ANNOUNCEMENT: 'Company Announcement',
                    CORE_TXT_LOGOUT: 'Logout',
                    CORE_TXT_CHANGE_PASSWORD: 'Change Password',
                    CORE_TXT_PASSWORD_ERR1:'enter a combination of at least six numbers,letters and punctuation marks (like ! and &)',
                    CORE_TXT_PASSWORD_ERR2:'Password does Not match',

                    /** for button **/
                    CORE_BUTTON_SIGN_IN: 'SIGN IN',
                    CORE_BUTTON_LOG_OUT: 'LOG OUT',

                    /** for message dialog **/
                    CORE_TXT_CONFIRM_MESSAGE: 'Confirm',

                    /** Core system used end **/
                    // --------------------------------------------------------- START ---------- USER MANATER	
                    GRID_TITLE_TXT_USER_MANATER_USERNAME:'USERNAME',
                    GRID_TITLE_TXT_USER_MANATER_COMPANY:'COMPANY',
                    GRID_TITLE_TXT_USER_MANATER_FIRST_NAME:'FIRST NAME',
                    GRID_TITLE_TXT_USER_MANATER_LAST_NAME:'LAST NAME',
                    GRID_TITLE_TXT_USER_MANATER_EMAIL:'EMALL',
                    TITLE_TXT_APPLICATION:'APPLICATION',
                    TITLE_TXT_MODULE:'MODULE',
                    TITLE_TXT_ACTIVE:'ACTIVE',
                    TITLE_TXT_ADMIN:'ADMIN',
                    TITLE_TXT_DEFAULT_URL:'DEFAULT_URL',
                    TITLE_TXT_SHOW_IN_MENU:'MENU',
                    TITLE_TXT_MENU_TITLE:'MENU_TITLE',
                    TITLE_TXT_ORDER_BY:'ORDER_BY',
                    GRID_TITLE_TXT_USER_MANATER_SUPERUSER:'SUPERUSER',
                    GRID_TITLE_TXT_USER_MANATER_ROLE:'ROLE',
                    GRID_TITLE_TXT_USER_MANATER_PROPERTY:'PROPERTY',
                    GRID_TITLE_TXT_USER_MANATER_DESCRIPTION:'DESCRIPTION',
                    GRID_TITLE_TXT_USER_MANATER_CONTROLLER:'CONTROLLER',
                    GRID_TITLE_TXT_USER_MANATER_ACTION:'ACTION',
                    GRID_TITLE_TXT_USER_MANATER_VALUE:'VALUE',
                    // --------------------------------------------------------- END ---------- USER MANATER
                    UNDER_LINE: '_',

                    CORE_TXT_ALERT: 'Alert :',
                    CORE_TXT_CONFIRM: 'Confirm :'
                });
        }]);
});
