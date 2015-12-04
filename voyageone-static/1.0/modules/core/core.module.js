/**
 * @Name:    module.js
 * @Date:    2015/2/2
 *
 * @User:    Edward
 * @Version: 1.0.0
 */

define (function (require) {

    require ('components/services/ajax.service');
    require ('components/services/cookie.service');
    require ('components/services/alert.service');
    require ('components/services/language.service');
    require ('components/services/message.service');
    require ('components/services/permission.service');
    require ('components/services/translate.service');

    var coreApp = angular.module ('coreModule', ['mainModule']);

    coreApp.constant ('coreAction', {

        'core_account_login_doLogin': '/core/account/login/doLogin',
        'core_account_login_doLogout': '/core/account/login/doLogout',
        'core_account_login_doGetUserInfo': '/core/account/login/doGetUserInfo',
        'core_account_company_doGetCompany': '/core/account/company/doGetCompany',
        'core_account_company_doSelectCompany': '/core/account/company/doSelectCompany',

        'core_menu_home_doGetAnnouncement': '/core/menu/home/doGetAnnouncements',
        'core_menu_home_doGetAnnouncementContent': '/core/menu/home/doGetAnnouncementContent',

        'core_setting_user_doUserInit': '/core/setting/user/doInit',
        'core_setting_user_doGetUserList': '/core/setting/user/getUserList',
        'core_setting_user_doGetUserInfo': '/core/setting/user/getUserInfo',
        'core_setting_user_doUpdateUserInfo': '/core/setting/user/updateUser',
        'core_setting_user_doAddUserInfo': '/core/setting/user/addUser',
        'core_setting_user_doDelUserInfo': '/core/setting/user/delUser',
        'core_setting_user_doAddUserRole': '/core/setting/user/insertRoleInfo',
        'core_setting_user_doUpdateUserRole': '/core/setting/user/updateRoleInfo',
        'core_setting_user_doDelUserRole': '/core/setting/user/delRoleInfo',
        'core_setting_user_doAddUserPermission': '/core/setting/user/insertPermissionInfo',
        'core_setting_user_doUpdateUserPermission': '/core/setting/user/updatePermissionInfo',
        'core_setting_user_doDelUserPermission': '/core/setting/user/delPermissionInfo',

        'core_setting_role_doGetRoleList': '/core/setting/role/getRoleList',
        'core_setting_role_doUpdateRole': '/core/setting/role/updateRole',
        'core_setting_role_doAddRole': '/core/setting/role/addRole',
        'core_setting_role_doUpdateRolePermission': '/core/setting/role/updateRolePermission',
        'core_setting_role_doAddRolePermission': '/core/setting/role/addRolePermission',
        'core_setting_role_doGetRoleInfoById': '/core/setting/role/getRoleInfoById',

        'core_setting_app_doAddApp': '/core/setting/app/addApp',
        'core_setting_app_doAddModule': '/core/setting/app/addModule',
        'core_setting_app_doAddController': '/core/setting/app/addController',
        'core_setting_app_doAddAction': '/core/setting/app/addAction',
        'core_setting_app_doUpdateApp': '/core/setting/app/updateApp',
        'core_setting_app_doUpdateModule': '/core/setting/app/updateModule',
        'core_setting_app_doUpdateController': '/core/setting/app/updateController',
        'core_setting_app_doUpdateAction': '/core/setting/app/updateAction',
        'core_setting_app_doBatchApp': '/core/setting/app/batchAdd'

    });

    /**
     * core的所有popup画面一览.
     */
    coreApp.constant ("corePopupPages", {
        "popAnnouncement": {
            "page": "/VoyageOne/modules/core/popup/popAnnouncement.dialog.tpl.html",
            "controller": "popAnnouncementController"
        }
    });

    return coreApp;
});