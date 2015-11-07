/**
 * @Name:    complex.ctl.js
 * @Date:    2015/8/12
 *
 * @User:    Edward
 * @Version: 1.0.0
 */
define (function (require) {
    var cmsApp = require ('modules/cms/cms.module');
    require ('modules/cms/common/common.service');
    require ('modules/cms/search/search.service');
    require ('modules/cms/service/mainCategory.service');
    
    cmsApp.controller ('searchComplexController', ['$scope', '$rootScope', '$q', '$location', '$routeParams', 'cmsCommonService', 'cmsRoute', 'searchService', 'systemCountry','DTOptionsBuilder','DTColumnBuilder','$translate','$compile',
      function ($scope, $rootScope, $q, $location, $routeParams, commonService, cmsRoute, searchService, systemCountry,DTOptionsBuilder,DTColumnBuilder,$translate,$compile) {

          var _ = require ('underscore');
          var commonUtil = require ('components/util/commonUtil');

          $scope.searchValue = '';

          /**
           * 初始化操作.
           */
          $scope.initialize = function () {
              if (commonUtil.isNotEmpty($routeParams.key)) {
                  $scope.searchValue = $routeParams.key.replace("%2f","/");
              }
              $scope.doSearchCategory ();
              $scope.doSearchModel();
              $scope.doSearchProduct ();
          };
          
          $scope.doSearch = function () {
              if (commonUtil.isNotEmpty($scope.searchValue)) {
            	  $scope.dtCategoryList.dtInstance.reloadData();
            	  $scope.dtModelList.dtInstance.reloadData();
            	  $scope.dtProductList.dtInstance.reloadData();
              }
          };
          /**
           * 点击画面上go按钮后的操作.
           */

          $scope.doSearchCategory = function () {
              var titleHtml = '<input type="checkbox" ng-model="showCase.selectAll" ng-click="showCase.toggleAll(showCase.selectAll, showCase.selected)">';
              $scope.dtCategoryList = {
                  options: DTOptionsBuilder.newOptions()
                      .withOption('processing', true)
                      .withOption('serverSide', true)
                      .withOption('scrollY', '600px')
                      .withOption('scrollX', '100%')
                      .withOption('scrollCollapse', true)
                      .withOption('ajax', $scope.doSearchCategoryData)
                      .withOption('createdRow',  function(row, data, dataIndex) {
                          // Recompiling so we can bind Angular directive to the DT
                          $compile(angular.element(row).contents())($scope);
                      })
                      .withDataProp('data')
                      .withPaginationType('full_numbers'),
                  columns: [
                        DTColumnBuilder.newColumn('', $translate('CMS_TXT_NAME')).withClass('wtab-xs text-center').renderWith(function (val, type, row, cell) {
                            return ('<a ng-controller="navigationController" href="" class="btn-main" ng-href="{{goCategoryPage('+row.categoryId+')}}" >'+row.name+'</a>');
                        }),
                        DTColumnBuilder.newColumn('subCategoryCount', $translate('CMS_TXT_SUB_CATEGORY_COUNT')).withClass('wtab-s text-center'),
                        DTColumnBuilder.newColumn('modelCount', $translate('CMS_TXT_MODEL_COUNT')).withClass('wtab-xsm text-center'),
                        DTColumnBuilder.newColumn('', $translate('CMS_TXT_PARENT_CATEGORY')).withClass('wtab-xsm text-center').renderWith(function (val, type, row, cell) {
                            return ('<a ng-controller="navigationController" href="" class="btn-main" ng-href="{{goCategoryPage('+row.parentCategoryId+')}}" >'+row.parentCategoryName+'</a>');
                        }),
                        DTColumnBuilder.newColumn('', $translate('CMS_TXT_IS_EFFECTIVE')).withClass('wtab-xsm text-center').renderWith(function (val, type, row, cell) {
                            return ('<input type="checkbox" '+ (row.isEffective ? 'checked ' : '') + ' >');
                        }),
                        DTColumnBuilder.newColumn('created', $translate('CMS_TXT_CREATED_ON')).withClass('wtab-xsm text-center'),
                        DTColumnBuilder.newColumn('modified', $translate('CMS_TXT_LAST_UPDATED_ON')).withClass('wtab-xsm text-center')
                        
                  ],
                  dtInstance:1
              };
          };

          $scope.doSearchCategoryData = function(data, draw) {
              if (data.draw === 1) {
            	  var initData={};
            	  initData.recordsFiltered = 0;
            	  initData.recordsTotal = 0;
            	  initData.draw= 1;
            	  initData.data={};
                  draw(initData);
                  if (!commonUtil.isNotEmpty($scope.searchValue)) {
  					return;
  				  }
              }
              return searchService.doSearch(data, $scope.searchValue,1)
                  .then (function (data) {
                  $scope.categoryList = data.data;
                  return draw(data)
              });
          };
      
          $scope.doSearchModel = function () {
              var titleHtml = '<input type="checkbox" ng-model="showCase.selectAll" ng-click="showCase.toggleAll(showCase.selectAll, showCase.selected)">';
              $scope.dtModelList = {
                  options: DTOptionsBuilder.newOptions()
                      .withOption('processing', true)
                      .withOption('serverSide', true)
                      .withOption('scrollY', '600px')
                      .withOption('scrollX', '100%')
                      .withOption('scrollCollapse', true)
                      .withOption('ajax', $scope.doSearchModelData)
                      .withOption('createdRow',  function(row, data, dataIndex) {
                          // Recompiling so we can bind Angular directive to the DT
                          $compile(angular.element(row).contents())($scope);
                      })
                      .withDataProp('data')
                      .withPaginationType('full_numbers'),
                  columns: [
                      DTColumnBuilder.newColumn('', $translate('CMS_TXT_MODEL')).withClass('wtab-sm text-center').renderWith(function (val, type, row, cell) {
                          return ('<a ng-controller="navigationController" href="" class="btn-main" ng-href="{{goModelPage('+row.primaryCategoryId+','+row.modelId+')}}" >'+row.model+'</a>');
                      }),
                      DTColumnBuilder.newColumn('name', $translate('CMS_TXT_NAME')).withClass('wtab-xsm'),
                      DTColumnBuilder.newColumn('productCount', $translate('CMS_TXT_PRODUCT_COUNT')).withClass('wtab-xs text-center'),
                      DTColumnBuilder.newColumn('', $translate('CMS_TXT_PRIMARY_CATEGORY')).withClass('wtab-xsm').renderWith(function (val, type, row, cell) {
                          return ($scope.getParentHtm(row));
                      }),
                      DTColumnBuilder.newColumn('', $translate('CMS_TXT_PRIMARY_PRODUCT')).withClass('wtab-sm text-center').renderWith(function (val, type, row, cell) {
                          if(commonUtil.isNotEmpty(row.primaryProductId)){
                              return ('<img class="prodImg" src="' + $rootScope.cmsMaster.imageUrl + row.imageName + '"><br><a ng-controller="navigationController" href="" class="btn-main" ng-href="{{goProductPage(' + row.primaryCategoryId + ',' + row.modelId + ',' + row.primaryProductId + ')}}">' + row.primaryProductCode + '</a>');
                          }else{
                              return "";
                          }
                      }),
                      DTColumnBuilder.newColumn('created', $translate('CMS_TXT_CREATED_ON')).withClass('wtab-sm'),
                      DTColumnBuilder.newColumn('modified', $translate('CMS_TXT_LAST_UPDATED_ON')).withClass('wtab-sm'),
                      DTColumnBuilder.newColumn('', $translate('CMS_BTN_SET_PROPERTY')).withClass('wtab-sm').renderWith(function (val, type, row, cell) {
                          return ('<button ng-controller="goMainPage" ype="button" ng-click="goMainCategoryPage('+row.modelId+')" class="btn btn-primary btn-sm fa fa-equalizer fa-building ng-scope" translate="CMS_TXT_SET_PROPERTY"></button>');
                      })
                  ],
                  dtInstance:1
              };
          };
          
          $scope.doSearchModelData = function(data, draw) {
              if (data.draw === 1) {
            	  var initData={};
            	  initData.recordsFiltered = 0;
            	  initData.recordsTotal = 0;
            	  initData.draw= 1;
            	  initData.data={};
                  draw(initData);
					if (!commonUtil.isNotEmpty($scope.searchValue)) {
						return;
					}
              }
              return searchService.doSearch(data, $scope.searchValue,2)
                  .then (function (data) {
                  $scope.modelList = data.data;
                  return draw(data)
              });
          };
          
          $scope.doSearchProduct = function () {
              var titleHtml = '<input type="checkbox" ng-model="showCase.selectAll" ng-click="showCase.toggleAll(showCase.selectAll, showCase.selected)">';
              $scope.dtProductList = {
                  options: DTOptionsBuilder.newOptions()
                      .withOption('processing', true)
                      .withOption('serverSide', true)
                      .withOption('scrollY', '600px')
                      .withOption('scrollX', '100%')
                      .withOption('scrollCollapse', true)
                      .withOption('ajax', $scope.doSearchProductData)
                      .withOption('createdRow',  function(row, data, dataIndex) {
                          // Recompiling so we can bind Angular directive to the DT
                          $compile(angular.element(row).contents())($scope);
                      })
                      .withDataProp('data')
                      .withPaginationType('full_numbers'),
                  columns: [
                      DTColumnBuilder.newColumn('', $translate('CMS_TXT_CODE')).withClass('wtab-xs text-center').renderWith(function (val, type, row, cell) {
                          return ('<img class="prodImg" src="' + $rootScope.cmsMaster.imageUrl + row.imageName + '"><br><a ng-controller="navigationController" href="" class="btn-main" ng-href="{{goProductPage(' + row.primaryCategoryId + ',' + row.modelId + ',' + row.productId + ')}}">' + row.code + '</a>');
                      }),
                      DTColumnBuilder.newColumn('name', $translate('CMS_TXT_NAME')).withClass('wtab-xsm'),
                      DTColumnBuilder.newColumn('color', $translate('CMS_TXT_COLOR')).withClass('wtab-sm'),
                      DTColumnBuilder.newColumn('', $translate('CMS_TXT_MODEL')).withClass('wtab-xs').renderWith(function (val, type, row, cell) {
                          return ('<a  ng-controller="navigationController" href="" class="btn-main" ng-href="{{goModelPage('+row.primaryCategoryId+','+row.modelId+')}}" >'+'<span title="'+row.modelName+'">'+row.model+'</span>'+'</a>');
                      }),
                      DTColumnBuilder.newColumn('', $translate('CMS_TXT_PRIMARY_CATEGORY')).withClass('wtab-xsm').renderWith(function (val, type, row, cell) {
                          return ($scope.getParentHtm(row));
                      }),
                      DTColumnBuilder.newColumn('quantity', $translate('CMS_TXT_QUANTITY')).withClass('wtab-xs text-center'),
                      DTColumnBuilder.newColumn('created', $translate('CMS_TXT_CREATED_ON')).withClass('wtab-sm'),
                      DTColumnBuilder.newColumn('modified', $translate('CMS_TXT_LAST_UPDATED_ON')).withClass('wtab-sm'),
                      DTColumnBuilder.newColumn('', $translate('CMS_BTN_SET_PROPERTY')).withClass('wtab-sm').renderWith(function (val, type, row, cell) {
                          return ('<button ng-controller="goMainPage" ype="button" ng-click="goMainCategoryPage('+row.modelId+')" class="btn btn-primary btn-sm fa fa-equalizer fa-building ng-scope" translate="CMS_TXT_SET_PROPERTY"></button>');
                      })
                  ],
                  dtInstance:1
              };
          };
          
          $scope.doSearchProductData = function (data, draw) {
              if (data.draw === 1) {
            	  var initData={};
            	  initData.recordsFiltered = 0;
            	  initData.recordsTotal = 0;
            	  initData.draw= 1;
            	  initData.data={};
                  draw(initData);
                  if (!commonUtil.isNotEmpty($scope.searchValue)) {
					return;
				  }
              }
              return searchService.doSearch(data, $scope.searchValue,3)
                  .then (function (data) {
                  $scope.productList = data.data;
                  return draw(data)
              });
          };
          
          $scope.getParentHtm = function (row) {
        	  var str = "";
        	  _.each(row.parentCategoryPath, function(item,i){
                  str += '<a ng-controller="navigationController" href="" class="btn-main" ng-href="{{goCategoryPage('+item.categoryId+')}}">'+item.name+'</a>';
                  if(i != row.parentCategoryPath.length - 1)
                  {
                	  str += ' <span>&nbsp;>>&nbsp;</span>'
                  }
        	  });
        	  return str;
          }
         
      }]);

    /**
     * 用于跳转到主类目属性设置页面.
     * 为了应付20151107快速对应
     */
    cmsApp.controller ('goMainPage',['$scope', '$location', 'searchService','mainCategoryService','cmsRoute', 'mainCategoryLevel',
        function($scope, $location, searchService, mainCategoryService, cmsRoute, mainCategoryLevel) {

            /**
             * 跳转到MainCategory设定页面.
             */
            $scope.goMainCategoryPage = function (modelId) {

                searchService.doGetCNModelInfo(modelId.toString()).then(function(modelInfo) {

                    var data = {};

                    data.channelId = modelInfo.cnBaseModelInfo.channelId;
                    data.mainCategoryId = modelInfo.cnBaseModelInfo.mainCategoryId;
                    data.parentLevel = modelInfo.cnBaseModelInfo.mainParentCategoryTypeId;
                    data.parentId = modelInfo.cnBaseModelInfo.mainParentCategoryId;
                    data.currentLevel = mainCategoryLevel.model;
                    data.currentId = modelInfo.cnBaseModelInfo.modelId;
                    mainCategoryService.setMainCategoryParam(data);

                    mainCategoryService.setMainCategoryReturnUrl($location.path());

                    $location.path(cmsRoute.cms_masterPropValue_setting.hash);
                });
            };
        }]);
});