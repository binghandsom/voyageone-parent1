/**
 * @Name:    categorySearchService
 * @Date:    2015/06/12
 *
 * @User:    Lewis.liu
 * @Version: 1.0.0
 */

define(["modules/cms/cms.module","components/services/ajax.service"],function (cmsServiceModule) {
	
	cmsServiceModule.service("feedDefaultPropSettingService",["$q", "cmsAction", "ajaxService",
	    function($q, cmsAction, ajaxService) {
			
			this.getDefaultProps=function(){
				
				 var defer = $q.defer();
		            ajaxService.ajaxPost({}, cmsAction.feedDefaultPropSetting.getDefaultProps,null)
		                .then(function(response) {
		                    
		                    defer.resolve(response);
		                });
		            return defer.promise;
			};
			
			this.update=function(dataList){
				
				 var defer = $q.defer();
		            ajaxService.ajaxPost(dataList, cmsAction.feedDefaultPropSetting.update,null)
		                .then(function(response) {
		                    
		                    defer.resolve(response);
		                });
		            return defer.promise;
			};
			
    }]);

});