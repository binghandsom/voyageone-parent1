/**
 * @Name:    popMoveToModelService.js
 * @Date:    2015/08/26
 * @User:    sky
 * @Version: 1.0.0
 */

define(function (require) {

    var cmsApp = require('modules/cms/cms.module');
    require('components/services/ajax.service');
    cmsApp.service('imgSettingService',['$q', 'cmsAction', 'ajaxService',
        function ($q, cmsAction, ajaxService) {

            //通过model查询所属的category
            this.doUpdateProductImg = function (data, scope) {
                return ajaxService.ajaxPost(data, cmsAction.cms_edit_product_doUpdateProductImg, scope);
            };
            //通过model查询所属的category
            this.doDelProductImg = function (data, scope) {
                return ajaxService.ajaxPost(data, cmsAction.cms_edit_product_doDelProductImg, scope);
            };            

        }]);
    return cmsApp;
});