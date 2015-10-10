/**
 * @Name:    categorySearchService
 * @Date:    2015/06/12
 *
 * @User:    Lewis.liu
 * @Version: 1.0.0
 */

define(["modules/cms/cms.module","components/services/ajax.service"],function (cmsServiceModule) {
	
	cmsServiceModule.service("masterCategoryMatchService",["$q", "cmsAction", "ajaxService",
	    function($q, cmsAction, ajaxService) {
			
			this.getAllCategory=function(parmObj){
				
				 var defer = $q.defer();
		            ajaxService.ajaxPost(parmObj, cmsAction.masterCategoryMatch.doGetAllCategory,null)
		                .then(function(response) {
		                    
		                    defer.resolve(response);
		                });
		            return defer.promise;
			};
			
			this.getPlatformInfo=function(categoryId){
				
				 var defer = $q.defer();
		            ajaxService.ajaxPost(categoryId, cmsAction.masterCategoryMatch.doGetPlatformInfo,null)
		                .then(function(response) {
		                    
		                    defer.resolve(response);
		                });
		            return defer.promise;
			};
			
    }]);

});