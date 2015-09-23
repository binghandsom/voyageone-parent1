/**
 * @Name:    popSetItemShareService.js
 * @Date:    2015/09/01
 * @User:    sky
 * @Version: 1.0.0
 */

define(function (require) {

    var cmsApp = require('modules/cms/cms.module');
    require('components/services/ajax.service');
    cmsApp.service('popSetCnProductShareService',['$q', 'cmsAction', 'ajaxService',
        function ($q, cmsAction, ajaxService) {

            //更改上新產品屬性
            this.doSetCnProductShare = function (data, scope) {
                return ajaxService.ajaxPost(data, cmsAction.cms_edit_product_doSetCnProductShare, scope);
            };

        }]);
    return cmsApp;
});