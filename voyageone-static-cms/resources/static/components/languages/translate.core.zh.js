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
                    CORE_BUTTON_SIGN_IN: '登录',
                    CORE_TXT_TITLE_COMPANY: '公司',
                    CORE_BUTTON_LOG_OUT: '注销',
                    CORE_TXT_TITLE_VOYAGE_ONE: 'Voyage One',
                    CORE_BUTTON_CHANGE_PASSWORD: '修改密码',
                    CORE_BUTTON_OK: '是',
                    CORE_BUTTON_CANCEL: '否',
                    CORE_TXT_ALERT: '警告',
                    CORE_TXT_CONFIRM: '确认',

                    CORE_TXT_MESSAGE_CHANGE_TO_EFFECTIVE : '是否愿意改成有效或者无效 ?',
                    CORE_TXT_MESSAGE_BATCH_DELETE  : '是否愿意进行批量删除操作 ?',
                    UNDER_LINE: '_',

                    CORE_NODATA: '没有找到有效数据',

                    CORE_BTN_ADD: '添加',
                    CORE_BTN_OK: '确定',
                    CORE_BTN_CANCEL: '取消',
                });
        }]);
});
