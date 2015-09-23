/**
 * @Name: categorySearchController
 * @Date: 2015/06/12
 * 
 * @User: Lewis.liu
 * @Version: 1.0.0
 */

define([ "modules/cms/cms.module",
         "modules/cms/cms.route",
		 "modules/cms/masterPropValueSetting/masterPropValueSettingService",
		 "modules/cms/service/mainCategory.service",
		 "modules/cms/edit/edit.service",
		 "modules/cms/masterPropValueSetting/setMtPropValuePopUpController"], 
		 function(cmsModule) {
	
			cmsModule.controller('masterPropValueSettingController', 
					[ '$scope',
					  '$rootScope',
					  'masterPropValueSettingService', 
					  'mainCategoryService',
					  'editMainCategoryService',
					  '$compile', 
					  '$location',
					  'cmsRoute',
					  'cmsPopupPages',
					  'ngDialog',
			
			function(scope,rootScope,vaueSetservice,catagoryServiece,editService, $compile, $location, cmsRoute,cmsPopupPages,ngDialog) {				
				scope.init = function() {
					//需要调用catagoryServiece 取得参数.
					var sessionData = catagoryServiece.getMainCategoryParam();
					
					var parmData ={
						channelId:sessionData.channelId,
						categoryId:sessionData.mainCategoryId,
						parentLevel:sessionData.parentLevel,
						parentLevelValue:sessionData.parentId,
						level:sessionData.currentLevel,
						levelValue:sessionData.currentId
					};
					
					vaueSetservice.doInit(parmData).then(function(response) {
						
						scope.categoryId = parmData.categoryId;
						
						var responseData=response.data;
						//设定平台信息.
						scope.platformInfo = responseData.platformInfo;
						//设置当前类目名称
						scope.currentCategoryName = responseData.currentCategoryName;
						//设置所有顶层类目名称
						scope.categories = responseData.categories;
						
						scope.hiddenInfo = responseData.hiddenInfo;
						
						if (responseData.viewModel) {
							var viewModel = responseData.viewModel;
							
							var htmlView = viewModel.htmlView;
							
							var innerView = $compile(htmlView)(scope);
							
							angular.element("#innerHtml").append(innerView);
							
							scope.propModel = viewModel.propModel;
							
							//设定图片
							setImages(responseData);
							
							scope.isShow=true;
							//设置背景颜色
							setBackgroundColor();
						}
						
					});
					
				};
				
				function setBackgroundColor() {
					//设定必填项背景颜色
					$('li:contains("必须填写")').parents("fieldset").css('background-color', '#f9f2d9');
					$('li:contains("必须填写")').parents("fieldset").find("fieldset").css('background-color', '#f9f2d9');
					$('li:contains("必须填写")').parent().children().filter('li:contains("disableRule(true)")').parents("fieldset").css('background-color', '#FFE4E1');
					$('li:contains("必须填写")').parent().children().filter('li:contains("disableRule(true)")').parents("fieldset").find("fieldset").css('background-color', '#FFE4E1');
				};
				
				scope.search = function(categoryId) {
					
					if (angular.element("#innerHtml")!=undefined) {
						angular.element("#innerHtml").children().remove();
					}
					
					if (scope.propModel) {
						scope.propModel=null;
					}
					
					var parms={
					   categoryId:categoryId,
					   hiddenInfo:scope.hiddenInfo
					};
					
					vaueSetservice.doSearch(parms).then(function(response) {
						
						scope.errMsg=null;
						var responseData=response.data;
						
						//设定平台信息.
						scope.platformInfo = responseData.platformInfo;
						scope.categoryId = categoryId+"";
						var viewModel = responseData.viewModel;
						
						if (responseData.errMsgObj) {
							scope.errMsg = responseData.errMsgObj.errorMessage;
							scope.isShow=false;
							return;
						}
						
						var htmlView = viewModel.htmlView;
						
						var innerView = $compile(htmlView)(scope);
						
						angular.element("#innerHtml").append(innerView);
						
						scope.propModel = viewModel.propModel;
						//设置当前类目名称
						scope.currentCategoryName = responseData.currentCategoryName;
						
						scope.isShow=true;
						//设定背景颜色
						setBackgroundColor();
	
					});

				};

				scope.submit = function() {
					
					var fromData ={
						propModel:scope.propModel,
						hiddenInfo:scope.hiddenInfo
					};
					
					vaueSetservice.doSubmit(fromData).then(function(response) {
						if (response.data==true) {
							
							var returnData =[{
								type:scope.hiddenInfo.level,
								id:scope.hiddenInfo.levelValue,
								channelId:scope.hiddenInfo.channelId,
								mainCategoryId:scope.categoryId,
								platformInfo:scope.platformInfo
							}];
							
							//设定返回对象.
							editService.doUpdateMainCategoryId(returnData,true);
							
						}
					})
					
				};
				
				//弹出页面.
				scope.setValuePopup = function(currentScope,path) {
					currentScope.objPath = path;
					ngDialog.open ({
                        template: cmsPopupPages.setMasterPropValue.page,
                        controller: cmsPopupPages.setMasterPropValue.controller,
                        scope: currentScope
                    });
				};
				
				/**
	             * 展示下一张图片.
	             */
				scope.showNextImage = function(){
					scope.currentImageIndex = (scope.currentImageIndex < scope.images.productImages - 1) ? ++scope.currentImageIndex : 0;
	            };

	            /**
	             * 展示上一张图片.
	             */
	            scope.showPreviousImage = function(){
	            	scope.currentImageIndex = (scope.currentImageIndex > 0) ? --scope.currentImageIndex : scope.productImages.length -1;
	            };
	            
	            /**
	             * 设置当前图片为显示图片.
	             */
	            scope.setCurrentImage = function (index) {
	            	scope.currentImageIndex = index;
	            };
	            
	            function setImages(responseData) {
	            	if (responseData.images.length>0) {
	            		scope.isPhotosShow=true;
					}else {
						scope.isPhotosShow=false;
					}
					//设定图片
					scope.images = responseData.images;
					angular.forEach(scope.images, function(image) {
						 image.imageUrl = rootScope.cmsMaster.imageUrl + image.imageName;
                       });
					scope.currentImageIndex = 0;
				};

			} ]);
});
