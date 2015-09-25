/**
 * @Name: categorySearchController
 * @Date: 2015/06/12
 * 
 * @User: Lewis.liu
 * @Version: 1.0.0
 */

define([ "modules/cms/cms.module",
         "modules/cms/cms.route",
		 "modules/cms/masterCategoryMatch/masterCategoryMatchService",
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
							
							var cmsCategorise = $scope.resData.cmsCategoryList;
							
							for (var i = 0; i < cmsCategorise.length; i++) {
								if (cmsCategorise[i].mainCategoryId==-1) {
									cmsCategorise[i].isMatch = true;
								}
								if (cmsCategorise[i].mainCategoryId >0) {
									cmsCategorise[i].inheritClass = 'super-category fa fa-star';
									cmsCategorise[i].isPropMatch = true;
								}
								
								if (cmsCategorise[i].mainCategoryId == 0 && cmsCategorise[i].mainCategoryPath!=null) {
									cmsCategorise[i].inheritClass = 'sub-category fa fa-long-arrow-up';
									cmsCategorise[i].isPropMatch = false;
								}
							}
							
							//获取cms类目
							$scope.cmsCategoryList = cmsCategorise;
							//获取所有主类目
							$scope.masterCategoryList = $scope.resData.masterCategoryList;
					})
					
				};
				
				$scope.setMasterCategory=function(item){
					
					matchService.getPlatformInfo({categoryId:item.categoryId}).then(function(response){
						
						$scope.selectScope.cmsCategory.platformInfo = response.data.platformInfo;
						
						$scope.selectScope.cmsCategory.mainCategoryId = item.categoryId;
						$scope.selectScope.cmsCategory.mainCategoryPath = item.categoryPath;
						$scope.selectScope.cmsCategory.isSave = true;
						$scope.selectScope.cmsCategory.isMatch = false;
						$scope.selectScope.cmsCategory.inheritClass = 'super-category fa fa-star';
						var keyCmsPath = $scope.selectScope.cmsCategory.cmsCategoryPath;
						var keyLength = keyCmsPath.length;
						var cmsCategoryList = $scope.selectScope.$parent.cmsCategoryList;
						for (var i = 0; i < cmsCategoryList.length; i++) {
							if (cmsCategoryList[i].mainCategoryId==0 && (cmsCategoryList[i].cmsCategoryPath.substring(0,keyLength))==keyCmsPath) {
								cmsCategoryList[i].mainCategoryPath = item.categoryPath;
								cmsCategoryList[i].inheritClass = 'sub-category fa fa-long-arrow-up';
							}
						}
						
					})
					
				};
				
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
					if(category.mainCategoryId!=0){
						mainCategoryId = category.mainCategoryId;
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
						category.isSave = false;
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
				
				document.onclick = function(e){
					if ($scope.isClick) {
						$scope.isClick=false;
					}else {
						$("#master-category-match").hide();
					}
				 };
				 
				 $scope.change = function(category){
					 category.isSave = true;
					 if (category.isMatch) {
						 category.mainCategoryId = -1;
					}else {
						category.mainCategoryId = 0;
					}
					category.mainCategoryPath = null;
					category.inheritClass = "";
					 
				 };
				 
				 $scope.filterDisMatchCategory = function($event){
					 
					 var cmsCategorise = $scope.resData.cmsCategoryList;
					 
					 if ($scope.filter) {
						 
						 $scope.filter = false;
							
						for (var i = 0; i < cmsCategorise.length; i++) {
							if (cmsCategorise[i].mainCategoryId==-1) {
								cmsCategorise[i].isMatch = true;
							}
							if (cmsCategorise[i].mainCategoryId >0) {
								cmsCategorise[i].inheritClass = 'super-category fa fa-star';
							}
							
							if (cmsCategorise[i].mainCategoryId == 0 && cmsCategorise[i].mainCategoryPath!=null) {
								cmsCategorise[i].inheritClass = 'sub-category fa fa-long-arrow-up';
							}
						}
						//获取cms类目
						$scope.cmsCategoryList = cmsCategorise;
						$event.target.innerText = " 未匹配类目";
					}else {
						var filterCategoryList = [];
						$scope.filter = true;
						for (var i = 0; i < cmsCategorise.length; i++) {
							
							if (cmsCategorise[i].mainCategoryId == 0) {
								filterCategoryList.push(cmsCategorise[i]);
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
