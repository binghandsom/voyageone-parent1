/**
 * @Name:    popMoveToModelService.js
 * @Date:    2015/08/26
 * @User:    sky
 * @Version: 1.0.0
 */

define(function (require) {

    var cmsApp = require('modules/cms/cms.module');
    require('components/services/ajax.service');
    cmsApp.service('popMoveToModelService',['$q', 'cmsAction', 'ajaxService',
        function ($q, cmsAction, ajaxService) {

            //通过model查询所属的category
            this.searchCategoryByModel = function (data, scope) {
                return ajaxService.ajaxPost(data, cmsAction.cms_edit_model_doSearchCategoryByModel, scope);
            };

            this.changeModel = function (data, scope) {
                return ajaxService.ajaxPost(data, cmsAction.cms_edit_model_doChangeModel, scope);
            };
        }]);
    return cmsApp;
});