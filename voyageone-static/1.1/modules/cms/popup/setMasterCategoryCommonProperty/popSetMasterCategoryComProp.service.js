/**
 * @Name:    popSetProductPropertyService.js
 * @Date:    2015/08/31
 * @User:    sky
 * @Version: 1.0.0
 */

define(function (require) {

    var cmsApp = require('modules/cms/cms.module');
    require('components/services/ajax.service');
    cmsApp.service('popSetMasterCategoryComPropService',['$q', 'cmsAction', 'ajaxService',
        function ($q, cmsAction, ajaxService) {

            //获取共通属性选项值.
            this.getOptions = function (parms, scope) {

                return ajaxService.ajaxPost(parms, cmsAction.setMasterCategoryCommonPropertyPopup.getOptions, scope);

            };

            //批量更新
            this.update = function (parms, scope) {

                return ajaxService.ajaxPost(parms, cmsAction.setMasterCategoryCommonPropertyPopup.batchUpdate, scope);

            };

        }]);
    return cmsApp;
});