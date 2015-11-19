/**
 * @Name: categorySearchController
 * @Date: 2015/06/12
 * 
 * @User: Lewis.liu
 * @Version: 1.0.0
 */

define([ "modules/cms/cms.module",
         "modules/cms/cms.route",
		 "modules/cms/master/masterPropValueSetting/masterPropValueSettingService",
		 "modules/cms/service/mainCategory.service",
		 "modules/cms/edit/edit.service",
		 "modules/cms/popup/propValueSetting/popPropValueSetting.ctl"], 
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
					  'vConfirm',
					  '$modal',
					  '$timeout',
					  '$interval',
					  '$anchorScroll',
			
			function(scope,rootScope,vaueSetservice,catagoryServiece,editService, $compile, $location, cmsRoute,cmsPopupPages,ngDialog,vConfirm,$modal,$timeout,$interval,$anchorScroll) {
						
				/**
				 * 递归用对象
				 */
				scope.model = {};
				
				/**
				 * 初始化处理
				 */
				scope.init = function() {
					scope.isReady = true;
					scope.isSave = true;
					scope.eligiblyModels=null;
					//需要调用catagoryServiece 取得参数.
					scope.session = catagoryServiece.getMainCategoryParam();
					var parmData ={
						channelId:scope.session.channelId,
						categoryId:scope.session.mainCategoryId,
						parentLevel:scope.session.parentLevel,
						parentLevelValue:scope.session.parentId,
						level:scope.session.currentLevel,
						levelValue:scope.session.currentId
					};
					if(parmData.categoryId!=null && parmData.categoryId!=''){

						scope.isNew=false;
						vaueSetservice.doInit(parmData).then(function(response) {

							scope.categoryId = parmData.categoryId;

							scope.orgCategoryId = parmData.categoryId;

							var responseData=response.data;

							if (responseData==0) {

								scope.isReady=false;

								scope.sec = 10;
								scope.cur = $interval(function(){
									scope.sec--;
									if (scope.sec==0) {
										$interval.cancel(scope.cur);
										scope.doGoBack();
									}
								},1000);

								return;
							}
							//获取上新时错误信息.
							scope.tmallErrMsgs = responseData.tmallErrMsgs;

							var requireModels ={};
							for(var modelName in responseData.propModels){
								var model = responseData.propModels[modelName];
								if(model.require==1){
									requireModels[modelName] = model;
								}else{
									if(!scope.eligiblyModels){
										scope.eligiblyModels={};
									}
									scope.eligiblyModels[modelName] = model;
								}
							}
							//绑定
							scope.tabs = [
								{title:'必填项',models:requireModels},
								{title:'可选项'}
							];
							//设定平台信息.
							scope.platformInfo = responseData.platformInfo;

							//设置当前类目名称
							scope.currentCategoryName = responseData.currentCategoryName;

							scope.hiddenInfo = responseData.hiddenInfo;

							//设定图片
							setImages(responseData);

							scope.isError=false;

						});
					}else{
						scope.isNew=true;
						vaueSetservice.doInit(parmData).then(function(response) {

							var responseData=response.data;

							if (responseData.images) {

								//设定图片
								setImages(responseData);

								scope.isError=false;
							}
						});
						//取得导航列表.
						if (!scope.categories) {
							vaueSetservice.getCategoryNav().then(function(response) {
								//设置所有顶层类目名称
								scope.categories = response.data.categories;
							});
						}
					}
				};

				/**
				 * 选择tab.
				 * @param index
				 */
				scope.selectTab = function(index){

					scope.tabs[index].active=true;

					$location.hash('top');

					$anchorScroll();

					if(index==1 && !scope.tabs[index].models){
						scope.tabs[index].models = scope.eligiblyModels;
					}

				};

				/**
				 * 绑定可选项数据.
				 * @param tab
				 */
				scope.showEligiblyModels = function(tab){

					if(!tab.models){
						tab.models = scope.eligiblyModels;
					}
				};
				/**
				 * 查询处理
				 */
				scope.search = function(categoryId) {
					if(scope.tabs){
						scope.tabs[0].active=true;
					}
					scope.isNew=false;
					scope.isSave = false;
					scope.propModels=null;
					scope.eligiblyModels=null;

					var hiddenInfo = {
						channelId:scope.session.channelId,
						level:scope.session.currentLevel,
						levelValue:scope.session.currentId
					};

					var parms={
					   categoryId:categoryId,
					   hiddenInfo:hiddenInfo
					};
					
					vaueSetservice.doSearch(parms).then(function(response) {
						
						scope.errMsg=null;
						
						var responseData=response.data;
						
						if (responseData.errMsgObj) {
							scope.errMsg = responseData.errMsgObj.errorMessage;
							scope.isError=true;
							return;
						}

						var requireModels ={};
						for(var modelName in responseData.propModels){
							var model = responseData.propModels[modelName];
							if(model.require==1){

								requireModels[modelName] = model;

							}else{
								if(scope.eligiblyModels==null){
									scope.eligiblyModels={};
								}
								scope.eligiblyModels[modelName] = model;

							}
						}
						scope.tabs = [
							{title:'必填项',models:requireModels},
							{title:'可选项'}
						];
						//设定平台信息.
						scope.platformInfo = responseData.platformInfo;
						
						scope.categoryId = categoryId+"";
						
						//设置当前类目名称
						scope.currentCategoryName = responseData.currentCategoryName;
						
						scope.isError=false;
						
						if (scope.orgCategoryId==categoryId) {
							
							scope.isSave = true;
							
						}
						
					});

				};
				
				/**
				 * 提交处理.
				 */
	            scope.submit = function () {

	                vConfirm('CMS_MSG_MASTER_PROP_VALUE_SETTING_CATEGORY_SAVE_COMFIRM','').result.then(function() {

						var saveModels=scope.tabs[0].models;

						if(scope.tabs[1].models){
							for(var modelName in scope.tabs[1].models){
								saveModels[modelName] =   scope.tabs[1].models[modelName];
							}
						}else{
							for(var modelName in scope.eligiblyModels){
								saveModels[modelName] =   scope.eligiblyModels[modelName];
							}
						}

						var fromData ={
							propModels:saveModels,
							hiddenInfo:{
								channelId:scope.session.channelId,
								level:scope.session.currentLevel,
								levelValue:scope.session.currentId
							}
						};
						
						vaueSetservice.doSubmit(fromData).then(function(response) {
							if (response.data==true) {
								
								var returnData =[{
									type:scope.session.currentLevel,
									id:scope.session.currentId+"",
									channelId:scope.session.channelId,
									mainCategoryId:scope.categoryId,
									platformInfo:scope.platformInfo
								}];
								
								//设定返回对象.
								editService.doUpdateMainCategoryId(returnData,true);
								
							}
						})
						
	                }, function() {
	                	
	                })
	            };

				/**
				 * 类目切换.
				 */
				scope.switchCategory = function () {
					var confrimMsg={
						id:'CMS_MSG_MASTER_PROP_VALUE_SETTING_CATEGORY_SWITCH_COMFIRM',
						values:{categoryName:scope.currentCategoryName}
					};
					vConfirm(confrimMsg).result.then(function() {

						var fromData ={
							level:scope.session.currentLevel,
							levelValue:scope.session.currentId
						};
						vaueSetservice.doSwitchCategory(fromData).then(function(response) {

							var returnData =[{
								type:scope.session.currentLevel,
								id:scope.session.currentId+"",
								channelId:scope.session.channelId,
								mainCategoryId:scope.categoryId+"",
								platformInfo:scope.platformInfo
							}];
							//设定返回对象.
							editService.doUpdateMainCategoryId(returnData,true);
						});

					}, function() {

					})
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
	            
				/**
				 * 添加一行.
				 */
				scope.addItem = function(list){
					var newItem ={};
					angular.copy(list[list.length-1], newItem);
					vaueSetservice.clearItem(newItem);
					list.push(newItem);
				};
				
				/**
				 * 删除一行.
				 */
				scope.removeItem = function(list,$index){
					if (list.length>1) {
						list.splice($index, 1);
					}else {
						vaueSetservice.clearItem(list[$index]);
					}
				};
				
				/**
				 * 返回上一页
				 */
				scope.doGoBack = function(){
					if(scope.cur)
						$interval.cancel(scope.cur);
					$location.path (catagoryServiece.gettMainCategoryReturnUrl());
				}
				
				/**
				 * 页面渲染完成后加载导航并设置必填项背景颜色
				 */
				scope.$on('ngRepeatFinished', function (ngRepeatFinishedEvent) {
			        //设置必填项背景颜色
					vaueSetservice.setBackgroundColor();
					//取得导航列表.
					if (!scope.categories) {
						vaueSetservice.getCategoryNav().then(function(response) {
							//设置所有顶层类目名称
							scope.categories = response.data.categories;
						});
					}
				});
				
				/**
				 * 设定图片
				 */
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
				
				/**
				 * 清除属性值.
				 */
	            scope.clearValue = function (model) {
		            model.propValue = null;
	            };
	            
	            /**
	             * 回复原始类目.
	             */
	            scope.revertCategory = function(){
	            	scope.search(scope.orgCategoryId);
	            };


			} ]);
			
			//监视页面加载完成命令
			cmsModule.directive('onFinishRenderFilters', function ($timeout) {
			    return {
			        restrict: 'A',
			        link: function(scope, element, attr) {
			            if (scope.$last === true) {
			                $timeout(function() {
			                    scope.$emit('ngRepeatFinished');
			                });
			            }
			        }
			    };
			});
			
});
