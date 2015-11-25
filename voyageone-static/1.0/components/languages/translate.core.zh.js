/**
 * @Name:    translate-ch.js
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

            $translateProvider.translations (languageType.zh,
                {
                    /** Core system used start **/
                    /** for label and title. **/
                    CORE_TXT_LANGUAGE_ZH: '中文',
                    CORE_TXT_LANGUAGE_EN: 'English',
                    CORE_TXT_LOGIN: '登录',
                    //CORE_TXT_LOGIN_INFORMATION: '请输入您的登陆信息',
                    CORE_TXT_USER_NAME: '用户名/邮件地址',
                    CORE_TXT_PASSWORD: '密码',
                    CORE_TXT_SYSTEM_ANNOUNCEMENT: '系统公告',
                    CORE_TXT_COMPANY_ANNOUNCEMENT: '公司公告',
                    CORE_TXT_LOGOUT: '退出',
                    CORE_TXT_CHANGE_PASSWORD: '修改密码',
                    CORE_TXT_PASSWORD_ERR1:'请填写密码, 最小长度为 6个字符必须包含字母 数字和符号',
                    CORE_TXT_PASSWORD_ERR2:'两次输入的密码不一致',

                    /** for button **/
                    CORE_BUTTON_SIGN_IN: '登录',
                    CORE_BUTTON_LOG_OUT: '退出',

                    /** for message dialog **/
                    CORE_TXT_CONFIRM_MESSAGE: 'Confirm',

                    /** Core system used end **/
                    // --------------------------------------------------------- START ---------- USER MANATER	
                    GRID_TITLE_TXT_USER_MANATER_USERNAME:'用户名',
                    GRID_TITLE_TXT_USER_MANATER_COMPANY:'公司',
                    GRID_TITLE_TXT_USER_MANATER_FIRST_NAME:'姓',
                    GRID_TITLE_TXT_USER_MANATER_LAST_NAME:'名',
                    GRID_TITLE_TXT_USER_MANATER_EMAIL:'邮箱',
                    TITLE_TXT_APPLICATION:'APPLICATION',
                    TITLE_TXT_MODULE:'MODULE',
                    TITLE_TXT_ACTIVE:'ACTIVE',
                    TITLE_TXT_ADMIN:'管理员',
                    TITLE_TXT_DEFAULT_URL:'默认页',
                    TITLE_TXT_SHOW_IN_MENU:'菜单',
                    TITLE_TXT_MENU_TITLE:'菜单标题',
                    TITLE_TXT_ORDER_BY:'排序',
                    GRID_TITLE_TXT_USER_MANATER_SUPERUSER:'管理员',
                    GRID_TITLE_TXT_USER_MANATER_ROLE:'角色',
                    GRID_TITLE_TXT_USER_MANATER_PROPERTY:'渠道',
                    GRID_TITLE_TXT_USER_MANATER_DESCRIPTION:'类型',
                    GRID_TITLE_TXT_USER_MANATER_CONTROLLER:'CONTROLLER',
                    GRID_TITLE_TXT_USER_MANATER_ACTION:'ACTION',
                    GRID_TITLE_TXT_USER_MANATER_VALUE:'VALUE',
                    // --------------------------------------------------------- END ---------- USER MANATER
                    UNDER_LINE: '_',

                    CORE_TXT_ALERT: '警告：',
                    CORE_TXT_CONFIRM: '确认：'
                });
        }]);
});
