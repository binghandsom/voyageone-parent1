/**
 * @Name: categorySearchController
 * @Date: 2015/06/12
 * 
 * @User: Lewis.liu
 * @Version: 1.0.0
 */

define([ "modules/cms/cms.module",
         "modules/cms/cms.route",
		 "modules/cms/master/masterCategoryMatch/masterCategoryMatchService",
		 "modules/cms/service/mainCategory.service",
		 "modules/cms/edit/edit.service"], 
		 function(cmsModule) {
	
			cmsModule.controller('masterCategoryMatchController', 
					[ '$scope',
					  '$rootScope',
					  'cmsAction',
					  'masterCategoryMatchService',
					  'editMainCategoryService',
					  '$location',
					  'cmsRoute',
					  'notify',
			function($scope,$rootScope,cmsAction,matchService,editService,$location,cmsRoute,notify) {
					$scope.initialize = function() {
						
						matchService.getAllCategory({}).then(
							function(response) {
								
							$scope.resData = response.data;
							
							var categories = [];

							var cmsCategoryModels = $scope.resData.cmsCategoryModels;

							buildModel(cmsCategoryModels,categories);

							//获取cms类目
							$scope.cmsCategoryList = categories;
							//获取所有主类目
							$scope.masterCategoryList = $scope.resData.masterCategoryList;
					})
					
				};
				function buildModel(cmsCategoryModels,categories){
					if(cmsCategoryModels.length<1){
						return;
					}
					for(var i=0;i<cmsCategoryModels.length;i++){
						var model = cmsCategoryModels[i];
						if(model.mainCategoryId==-1){
							model.isMatch=true;
						}
						if (model.mainCategoryId >0) {
							model.inheritClass = 'super-category fa fa-star';
							model.isPropMatch = true;
						}

						if (model.mainCategoryId == 0 && model.mainCategoryPath!=null) {
							model.isExtend=true;
							model.inheritClass = 'sub-category fa fa-long-arrow-up';
							model.isPropMatch = false;
						}
						categories.push(model);
						buildModel(model.children,categories);
					}

				};
				$scope.setMasterCategory=function(item){
					
					matchService.getPlatformInfo({categoryId:item.categoryId}).then(function(response){
						
						$scope.selectScope.cmsCategory.platformInfo = response.data.platformInfo;
						
						$scope.selectScope.cmsCategory.mainCategoryId = item.categoryId;
						$scope.selectScope.cmsCategory.mainCategoryPath = item.categoryPath;
						$scope.selectScope.cmsCategory.isSave = true;
						$scope.selectScope.cmsCategory.isMatch = false;
						$scope.selectScope.cmsCategory.inheritClass = 'super-category fa fa-star';


						var curCategory = $scope.selectScope.cmsCategory;
						if(curCategory.isExtend){
							curCategory.isExtend=false;
						}
						setSubCategoryPath(curCategory,item.categoryPath,item.categoryId);

					})
				};

				function setSubCategoryPath(category,parentPath,parentCategoryId){

					if(category.children.length<1){
						return;
					}

					for (var i = 0; i < category.children.length; i++) {
						var subCategory = category.children[i];
						if (subCategory.mainCategoryId<1) {

							if(subCategory.mainCategoryId==0){
								subCategory.mainCategoryPath = parentPath;
								if(parentPath==null){
									subCategory.isExtend=false;
									subCategory.inheritClass = '';
								}else{
									if(subCategory.isSave){
										subCategory.isSave=false;
									}
									subCategory.extendMainCategoryId = parentCategoryId;
									subCategory.isExtend=true;
									subCategory.inheritClass = 'sub-category fa fa-long-arrow-up';
								}
							}

							setSubCategoryPath(subCategory,parentPath,parentCategoryId);
						}
					}
				}
				
				$scope.showMasterCategoryDiv = function($this,$event){
					$scope.isClick = true;
					$scope.selectScope = $this;
					var $target = $($event.target);
					
					if (!$target.is('button')){
						$target = $target.closest('button');
					} 
					
					if (($($scope.curtarget)[0]==$target[0]) && $scope.showMainCategory) {
						$scope.showMainCategory = false;
					}else{
						$scope.curtarget = $target[0];
						$scope.showMainCategory = true;
						var myOffset = {};
						myOffset.left = $target[0].offsetParent.offsetLeft;
						myOffset.top = $target[0].offsetParent.offsetTop;
						$("#master-category-match").css({left:myOffset.left+30, top:(myOffset.top+126), right:"", bottom:"" });
						$("#master-category-match").show();
					}
				};
				
				$scope.save = function(category){
					var mainCategoryId=null;
					if(category.isSave){
						mainCategoryId = category.mainCategoryId;
					}else if(category.isExtend){
						mainCategoryId = category.extendMainCategoryId;
						category.mainCategoryId = mainCategoryId;
					}
					var parmData =[{
							type:'1',
							id:category.categoryId,
							channelId:category.channelId,
							mainCategoryId:mainCategoryId,
							platformInfo:category.platformInfo
						}];
					
					//更新cms类目.
					editService.doUpdateMainCategoryId(parmData,false).then(
							function(respose){

								category.isPropMatch = true;

								if(category.isSave){
									category.isSave = false;
								}

								if(category.isExtend) {
									category.inheritClass = 'super-category fa fa-star';
									category.isExtend = false;
								}

								notify.success("CMS_TXT_MSG_UPDATE_SUCCESS");
					});
				};
				
				$scope.saveAll = function(allCategory){
					var saveItemList = [];
					
					for (var i = 0; i < allCategory.length; i++) {
						
						if (allCategory[i].isSave) {
							var item={};
							item.type = 1;
							item.id = allCategory[i].categoryId;
							item.channelId = allCategory[i].channelId;
							if(allCategory[i].mainCategoryId!=0){
								item.mainCategoryId = allCategory[i].mainCategoryId;
							}else{
								item.mainCategoryId = null;
							}
							item.platformInfo = allCategory[i].platformInfo;
							saveItemList.push(item);
						}
					}
					
					//更新cms类目.
					editService.doUpdateMainCategoryId(saveItemList,false).then(function(respose){
						for (var i = 0; i < allCategory.length; i++) {
							if (allCategory[i].isSave) {
								if(allCategory[i].mainCategoryId>0){
									allCategory[i].isPropMatch = true;;
								}
								allCategory[i].isSave = false;
							}
						}

						notify.success("CMS_TXT_MSG_UPDATE_SUCCESS");
					});
				};

				$scope.filterPropDisMatchCategory = function($event){

					if ($scope.filterByProp) {

						$scope.filterByProp = false;

						var cmsCategorise = $scope.allCategory;

						//获取cms类目
						$scope.cmsCategoryList = cmsCategorise;

						$event.target.innerText = " 属性匹配未完成类目";
					}else {
						$scope.allCategory = $scope.cmsCategoryList;
						var filterCategoryList = [];
						$scope.filterByProp = true;
						for (var i = 0; i < $scope.cmsCategoryList.length; i++) {

							if ($scope.cmsCategoryList[i].mainCategoryId > 0 && $scope.cmsCategoryList[i].propMatchStatus==0) {
								filterCategoryList.push($scope.cmsCategoryList[i]);
							}
						}
						//获取cms类目
						$scope.cmsCategoryList = filterCategoryList;
						$event.target.innerText = " 全部类目";
					}
				}
				
				document.onclick = function(e){
					if ($scope.isClick) {
						$scope.isClick=false;
					}else {
						$("#master-category-match").hide();
					}
				 };
				 
				 $scope.change = function(category){
					 if(category.isExtend){
						 category.isExtend=false;
					 }
					 category.isSave = true;
					 if (category.isMatch) {
						 if(category.mainCategoryId > 0){
							 category.mainCategoryId = -1;
							 category.mainCategoryPath = null;
							 category.inheritClass = "";
							 var topNodeCat = getTopNOde(category.parentCategoryId);
							 if(topNodeCat!=null){
								 setSubCategoryPath(topNodeCat,topNodeCat.mainCategoryPath,topNodeCat.mainCategoryId);
							 }else{
								 setSubCategoryPath(category,category.mainCategoryPath,category.mainCategoryId);
							 }
						 }else if(category.mainCategoryId==0){
							 category.mainCategoryId = -1;
							 category.mainCategoryPath = null;
							 category.inheritClass = "";
						 }

					}else if(category.mainCategoryId==-1){
						category.mainCategoryId = 0;
						 if(category.parentCategoryId>0){
							 var topNodeCat = getTopNOde(category.parentCategoryId);
							 if(topNodeCat!=null){
								 setSubCategoryPath(topNodeCat,topNodeCat.mainCategoryPath,topNodeCat.mainCategoryId);
							 }
						 }
					}

				 };

				function getTopNOde(parentCategoryId){

					for(var i=0;i< $scope.cmsCategoryList.length;i++){
						var category = $scope.cmsCategoryList[i];
						if(category.categoryId===parentCategoryId){
							if(category.mainCategoryId>0){
								return category;
							}else{
								return getTopNOde(category.parentCategoryId);
							}
						}
					}
					return null;
				};
				 
				 $scope.filterDisMatchCategory = function($event){


					 if ($scope.filterByCategory) {

						 var cmsCategorise = $scope.allCategory;
						 
						 $scope.filterByCategory = false;

						//获取cms类目
						$scope.cmsCategoryList = cmsCategorise;
						$event.target.innerText = " 未匹配类目";
					}else {
						 $scope.allCategory = $scope.cmsCategoryList;
						var filterCategoryList = [];
						$scope.filterByCategory = true;
						for (var i = 0; i < $scope.cmsCategoryList.length; i++) {
							
							if ($scope.cmsCategoryList[i].mainCategoryId == 0) {
								filterCategoryList.push($scope.cmsCategoryList[i]);
							}
						}
						//获取cms类目
						$scope.cmsCategoryList = filterCategoryList;
						$event.target.innerText = " 全部类目";
					}
					 
				 };
				 
				 $scope.propertyMatch = function(cmsCategory){
					 
					 $location.path(cmsRoute.cms_feed_prop_match.path(cmsCategory.mainCategoryId));
				 };
				    
			} ]);
});
