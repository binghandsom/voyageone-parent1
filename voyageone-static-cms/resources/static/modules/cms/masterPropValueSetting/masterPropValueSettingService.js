/**
 * @Name:    categorySearchService
 * @Date:    2015/06/12
 *
 * @User:    Lewis.liu
 * @Version: 1.0.0
 */

define(["modules/cms/cms.module","components/services/ajax.service"],function (cmsServiceModule) {
	
	cmsServiceModule.service("masterPropValueSettingService",["$q", "cmsAction", "ajaxService",
	    function($q, cmsAction, ajaxService) {
			
			this.doInit=function(formData){
				
				 var defer = $q.defer();
		            ajaxService.ajaxPost(formData, cmsAction.masterPropSetting.init,null)
		                .then(function(response) {
		                    
		                    defer.resolve(response);
		                });
		            return defer.promise;
			};
			
	        this.doSearch = function (formData) {
	            var defer = $q.defer();
	            ajaxService.ajaxPost(formData, cmsAction.masterPropSetting.search,null)
	                .then(function(response) {
	                    
	                    defer.resolve(response);
	                });
	            return defer.promise;
	        };
	        
	        this.doSubmit = function (formData) {
	            var defer = $q.defer();
	            ajaxService.ajaxPost(formData, cmsAction.masterPropSetting.submit,null)
	                .then(function(response) {
	                    
	                    defer.resolve(response);
	                });
	            return defer.promise;
	        };

    }]);

});