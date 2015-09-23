define (function (require) {

    var cmsApp = require ('modules/cms/cms.module');

    cmsApp.service('categoryService',['$q', 'ajaxService', 'cmsAction',
    function($q, ajaxService, cmsAction) {
	
        this.doGetCNCategoryInfo = function (data,scope) {
            var defer = $q.defer ();
            ajaxService.ajaxPost (data, cmsAction.cms_edit_category_getCNCategoryInfo, scope)
                .then (function (response) {
                defer.resolve (response);
            });
            return defer.promise;
        };
        this.doGetCategoryCNPriceSettingInfo = function (data,scope) {
            var defer = $q.defer ();
            ajaxService.ajaxPost (data, cmsAction.cms_edit_category_getCategoryCNPriceSettingInfo, scope)
                .then (function (response) {
                defer.resolve (response);
            });
            return defer.promise;
        };
    }]);
});