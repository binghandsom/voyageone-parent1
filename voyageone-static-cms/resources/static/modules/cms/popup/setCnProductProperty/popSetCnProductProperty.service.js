/**
 * @Name:    popSetProductPropertyService.js
 * @Date:    2015/08/31
 * @User:    sky
 * @Version: 1.0.0
 */

define(function (require) {

    var cmsApp = require('modules/cms/cms.module');
    require('components/services/ajax.service');
    cmsApp.service('popSetCnProductPropertyService',['$q', 'cmsAction', 'ajaxService',
        function ($q, cmsAction, ajaxService) {

            //更改產品屬性
            this.changeCnProductProperty = function (data, scope) {
                return ajaxService.ajaxPost(data, cmsAction.cms_edit_product_doSetCnProductProperty, scope);
            };

        }]);
    return cmsApp;
});