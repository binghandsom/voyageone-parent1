/**
 * @Name: categorySearchController
 * @Date: 2015/06/12
 * 
 * @User: Lewis.liu
 * @Version: 1.0.0
 */

define([ "modules/cms/cms.module",
         "modules/cms/cms.route",
		 "modules/cms/master/feedDefaultPropSetting/feedDefaultPropSettingService",
		 "modules/cms/popup/propValueSetting/popPropValueSetting.ctl"], 
		 function(cmsModule) {
	
			cmsModule.controller('feedDefaultPropSettingController', 
					[ '$scope',
					  '$rootScope',
					  'cmsAction',
					  'feedDefaultPropSettingService',
					  '$location',
					  'cmsRoute',
					  'notify',
					  '$modal',
					  'cmsPopupPages',
					  'notify',
					  'userService',
			function($scope,$rootScope,cmsAction,settingService,$location,cmsRoute,notify,$modal,cmsPopupPages,notify,userService) {
					
				$scope.inputType = {
						INPUT:1,
						SINGLECHECK:2,
						MULTICHECK:3,
						lABEL:4
					};
						
				$scope.getDefaultProps = function() {
						
						settingService.getDefaultProps().then(
							function(response) {
								
							var resData = response.data;
							$scope.channelId = resData.channelId;
							$scope.defaultPropList = resData.defaultPropList;
							$scope.isUpdate = resData.isUpdate;
							
							for (var i = 0; i < $scope.defaultPropList.length; i++) {
								var item = $scope.defaultPropList[i];
								if (item.channelId!=0) {
									item.ngClass = 'haveDone';
								}
							}
							
					})
					
				};
				
				$scope.submit = function(resList){
					settingService.update(resList).then(
							function(response) {
								notify.success("CMS_TXT_MSG_UPDATE_SUCCESS");
								$scope.isUpdate=true;
								for (var i = 0; i < $scope.defaultPropList.length; i++) {
									var item = $scope.defaultPropList[i];
									if (item.propValue!=null && item.propValue!="") {
										item.ngClass = 'haveDone';
										item.channelId = userService.getSelChannel();
									}else {
										item.ngClass = '';
									}
								}
					})
				};
				
			} ]);
});
