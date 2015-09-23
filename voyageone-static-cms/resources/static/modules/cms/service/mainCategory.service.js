/**
 * @Name:    mainCategory.service.js
 * @Date:    2015/7/24
 *
 * @User:    Edward
 * @Version: 1.0.0
 */

define(function (require) {
    var cmsApp = require('modules/cms/cms.module');

    cmsApp.service('mainCategoryService', ['cmsSessionStorageType',
        function (cmsSessionStorageType) {

            this.setMainCategoryParam = function (value) {
                sessionStorage.setItem(cmsSessionStorageType.CMS_MAIN_CATEGORY_SHOW_PARAM, JSON.stringify(value));
            };

            this.getMainCategoryParam = function () {
                if (!_.isUndefined(sessionStorage.getItem(cmsSessionStorageType.CMS_MAIN_CATEGORY_SHOW_PARAM)))
                    return JSON.parse(sessionStorage.getItem(cmsSessionStorageType.CMS_MAIN_CATEGORY_SHOW_PARAM));
                else
                    return null;
            };

            this.setMainCategoryReturnUrl = function (value) {
                sessionStorage.setItem(cmsSessionStorageType.CMS_MAIN_CATEGORY_RETURN_URL, value);
            };

            this.gettMainCategoryReturnUrl = function () {
                if (!_.isUndefined(sessionStorage.getItem(cmsSessionStorageType.CMS_MAIN_CATEGORY_RETURN_URL)))
                    return sessionStorage.getItem(cmsSessionStorageType.CMS_MAIN_CATEGORY_RETURN_URL);
                else
                    return null;
            };


        }]);
});
