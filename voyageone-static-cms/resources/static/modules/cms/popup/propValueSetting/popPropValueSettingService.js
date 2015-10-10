/**
 * @Name:    categorySearchService
 * @Date:    2015/06/12
 *
 * @User:    Lewis.liu
 * @Version: 1.0.0
 */

define(["modules/cms/cms.module",
        "components/services/ajax.service"],
   function (cmsServiceModule) {
	
	cmsServiceModule.service("popPropValueSettingService",
			["$q", 
			 "cmsAction", 
			 "ajaxService",
			 
	    function($q, cmsAction, ajaxService) {
			
			this.serialize=function(formData){
				
				 var defer = $q.defer();
		            ajaxService.ajaxPost(formData, cmsAction.setMtPropValuePopup.serialize,null)
		                .then(function(response) {
		                    
		                    defer.resolve(response);
		                });
		            return defer.promise;
			};
			
	        this.deserialize = function (formData) {
	            var defer = $q.defer();
	            ajaxService.ajaxPost(formData, cmsAction.setMtPropValuePopup.deserialize,null)
	                .then(function(response) {
	                    
	                    defer.resolve(response);
	                });
	            return defer.promise;
	        };
	        
	        this.getComplexValues = function (formData) {
	            var defer = $q.defer();
	            ajaxService.ajaxPost(formData, cmsAction.setMtPropValuePopup.getComplexValues,null)
	                .then(function(response) {
	                    
	                    defer.resolve(response);
	                });
	            return defer.promise;
	        };

    }]);

});