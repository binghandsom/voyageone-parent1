/**
 * @User: Jonas
 * @Date: 2015/3/22
 * @Version: 0.0.3
 */

define(function () {

    var cmsModule = angular.module("cmsModule", [
        "ngGrid",
        "mainModule"
    ]);
    
    cmsModule.constant("cmsAction", {
        //james add
    	'cms_edit_category_getCNCategoryInfo':'/cms/edit/category/doGetCNCategoryInfo',
    	'cms_edit_category_getCategoryCNPriceSettingInfo':'/cms/edit/category/doGetCategoryCNPriceSettingInfo',
    	//eric add
     });
    
    return cmsModule;
});