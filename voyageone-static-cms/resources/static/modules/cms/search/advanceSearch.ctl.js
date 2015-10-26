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
    require ('modules/cms/popup/addToPromotion/popAddToPromotion.ctl');
    require ('modules/cms/popup/setCnProductProperty/popSetCnProductProperty.ctl');
    require ('modules/cms/popup/setCnProductShare/popSetCnProductShare.ctl');
    require ('modules/cms/popup/setProductColumns/popSetProductColumns.ctl');
    require ('modules/cms/popup/setMasterCategoryCommonProperty/popSetMasterCategoryComProp.ctl');
    
    cmsApp.controller ('advanceSearchController', ['$scope', '$rootScope', '$q', '$location', '$routeParams', 'cmsCommonService', 'searchService', 'DTOptionsBuilder','DTColumnBuilder','$compile', '$translate', 'cookieService',
      function ($scope, $rootScope, $q, $location, $routeParams, cmsCommonService, searchService, DTOptionsBuilder, DTColumnBuilder,  $compile, $translate, cookieService) {

          var _ = require ('underscore');
          var commonUtil = require ('components/util/commonUtil');

          $scope.dtProductList;
          /**
           * 初始化操作.
           */
          $scope.initialize = function () {

              $scope.status = {};
        	  $scope.status.open = true;

              if(cmsCommonService.filter != null){
                $scope.search = angular.copy(cmsCommonService.filter);
              }else{
                  $scope.search = {};
              }
              $scope.yesOrNo = [{"value":"1","name":"Yes"},{"value":"0","name":"No"}];
              $scope.publishStatus = [{"value":"0","name":"Pending"},{"value":"1","name":"Success"},{"value":"2","name":"Failed"}];
              $scope.token = cookieService.getToken();
              $scope.companyId = cookieService.getSelCompany();

              // 初始化Cn Product 选中信息.
              $scope.cnProductInfo = {
                  selectAllFlag: false,
                  selectOneFlagList: [],
                  selectIdList: [],
                  selectDataList: [],
                  currentPageIdList: [],
                  currentPageDataList:[]
              };

              doSearchProduct();
          };

          /**
           * 检索
           */
          $scope.doSearch = function () {
        	  $scope.dtProductList.dtInstance.reloadData();
          };

          /**
           * 导出数据.
           */
          $scope.doExport = function () {
              $('#downloadForm').submit();
          };

          /**
           * 从数据中取得检索数据.
           * @param data
           * @param draw
           * @returns {*}
           */
          $scope.doSearchProductData = function (data, draw) {
              if (data.draw === 1) {
                  var initData={};
                  initData.recordsFiltered = 0;
                  initData.recordsTotal = 0;
                  initData.draw= 1;
                  initData.data={};
                  draw(initData);
                  if(cmsCommonService.filter == null){
                      return;
                  }else{
                      cmsCommonService.filter = null;
                  }
              }

              return searchService.doAdvanceSearch(data,$scope.search)
                  .then (function (data) {
                  $scope.cnProductInfo.selectAllFlag = true;
                  $scope.cnProductInfo.currentPageIdList = [];
                  $scope.cnProductInfo.currentPageDataList = [];

                  _.forEach(data.data, function (productInfo) {
                      var productId = parseInt(productInfo.productId);

                      $scope.cnProductInfo.currentPageIdList.push({id: productId});
                      $scope.cnProductInfo.currentPageDataList.push({id: productId, code: productInfo.code, name: productInfo.cnName, modelId: productInfo.modelId});

                      if (!$scope.cnProductInfo.selectOneFlagList.hasOwnProperty(productId) || !$scope.cnProductInfo.selectOneFlagList[productId]) {
                          $scope.cnProductInfo.selectAllFlag = false;
                      }
                  });
                  return draw(data);
              });
          };

          /** popup需要回调函数的处理 Start **/
          $scope.reFreshFunc = {};

          /**
           * 刷新usProduct信息.
           */
          $scope.reFreshFunc.doRefreshCnProductInfo = function () {
              cmsCommonService.doGetProductListByUserConfig('cn', $scope.dtProductList.dtInstance.DataTable);
          };
          /** popup需要回调函数的处理 End **/

          /**
           * 执行检索
           */
          function doSearchProduct () {
              var titleHtml = '<input ng-controller="selectController" ng-model="cnProductInfo.selectAllFlag" type="checkbox" ng-click="selectAll(cnProductInfo)">';
              $scope.dtProductList = {
                  options: DTOptionsBuilder.newOptions()
                      .withOption('processing', true)
                      .withOption('serverSide', true)
                      .withOption('scrollY', '400px')
                      .withOption('scrollX', '100%')
                      .withOption('scrollCollapse', true)
                      .withOption('ajax', $scope.doSearchProductData)
                      .withOption('createdRow',  function(row, data, dataIndex) {
                          var rowScope = $scope.$new();
                          rowScope.$row = data;
                          // Recompiling so we can bind Angular directive to the DT
                          $compile(angular.element(row).contents())(rowScope);
                      })
                      .withOption('headerCallback', function(header) {
                          if (!$scope.headerCompiledCn) {
                              // Use this headerCompiled field to only compile header once
                              $scope.headerCompiledCn = true;
                              $compile(angular.element(header).contents())($scope);
                          }
                      })
                      .withDataProp('data')
                      .withPaginationType('full_numbers'),
                  columns: [
                      DTColumnBuilder.newColumn(null).withTitle(titleHtml).notSortable().withClass('wtab-check').renderWith(function (val, type, row, cell) {
                          var productId = parseInt(row.productId);
                          if (!$scope.cnProductInfo.selectOneFlagList.hasOwnProperty(productId)) {
                              $scope.cnProductInfo.selectOneFlagList[productId] = false;
                          }
                          return '<input ng-controller="selectController" type="checkbox" ng-model="cnProductInfo.selectOneFlagList[$row.productId]" ng-click="selectOne($row.productId, cnProductInfo)">';
                      }).notSortable(),
                      DTColumnBuilder.newColumn('', $translate('CMS_TXT_CODE')).withClass('wtab-xs text-center').renderWith(function () {
                          return ('<img class="prodImg" ng-src="{{$root.cmsMaster.imageUrl}}{{$row.productImgUrl}}"><br><a ng-controller="navigationController" href="" class="btn-main" ng-href="{{goProductPage($row.categoryId,$row.modelId, $row.productId)}}">{{$row.code}}</a>');
                      }),
                      DTColumnBuilder.newColumn('cnName', $translate('CMS_TXT_NAME')).withClass('wtab-xsm').notSortable(),
                      DTColumnBuilder.newColumn('cnDisplayOrder', $translate('CMS_TXT_DISPLAY_ORDER')).withClass('wtab-xs text-center'),
                      DTColumnBuilder.newColumn('', $translate('CMS_TXT_IS_HIGH_LIGHT_PRODUCT')).withClass('wtab-xs text-center').renderWith(function () {
                          return ('<input type="checkbox" ng-model="$row.cnIsHightLightProduct" >');
                      }),
                      DTColumnBuilder.newColumn('productType', $translate('CMS_TXT_PRODUCT_TYPE')).withClass('wtab-xs').notSortable(),
                      DTColumnBuilder.newColumn('brand', $translate('CMS_TXT_BRAND')).withClass('wtab-xs').notSortable(),
                      DTColumnBuilder.newColumn('cnSizeType', $translate('CMS_TXT_SIZE_TYPE')).withClass('wtab-xs').notSortable(),
                      DTColumnBuilder.newColumn('', $translate('CMS_TXT_IS_APPROVED_DESCRIPTION')).withClass('wtab-xs text-center').renderWith(function () {
                          return ('<input type="checkbox" ng-model="$row.cnIsApprovedDesciption" >');
                      }),
                      DTColumnBuilder.newColumn('colorMap', $translate('CMS_TXT_COLOR_MAP')).withClass('wtab-xs').notSortable(),
                      DTColumnBuilder.newColumn('cnColor', $translate('CMS_TXT_COLOR')).withClass('wtab-sm').notSortable(),
                      DTColumnBuilder.newColumn('madeInCountry', $translate('CMS_TXT_MADE_IN_COUNTRY')).withClass('wtab-xs').notSortable(),
                      DTColumnBuilder.newColumn('materialFabric', $translate('CMS_TXT_MATERIAL_FABRIC')).withClass('wtab-sm').notSortable(),
                      DTColumnBuilder.newColumn('cnAbstract', $translate('CMS_TXT_ABSTRACT')).withClass('wtab-sm').notSortable(),
                      DTColumnBuilder.newColumn('', $translate('CMS_TXT_SHORT_DESCRIPTION')).withClass('wtab-xsm').renderWith(function () {
                          return ('<span title="{{$row.cnShortDescription}}" ng-bind="$row.cnShortDescription.substring(0, 100)"></span>');
                      }).notSortable(),
                      DTColumnBuilder.newColumn('', $translate('CMS_TXT_LONG_DESCRIPTION')).withClass('wtab-xsm').renderWith(function () {
                          return ('<span title="{{$row.cnLongDescription}}" ng-bind="$row.cnLongDescription.substring(0, 100)"></span>');
                      }).notSortable(),
                      DTColumnBuilder.newColumn('quantity', $translate('CMS_TXT_QUANTITY')).withClass('wtab-xs text-center'),
                      DTColumnBuilder.newColumn('urlKey', $translate('CMS_TXT_URL_KEY')).withClass('wtab-xsm').notSortable(),
                      DTColumnBuilder.newColumn('created', $translate('CMS_TXT_CREATED_ON')).withClass('wtab-sm'),
                      DTColumnBuilder.newColumn('modified', $translate('CMS_TXT_LAST_UPDATED_ON')).withClass('wtab-sm'),
                      DTColumnBuilder.newColumn('referenceMsrp', $translate('CMS_TXT_REFERENCE_MSRP')).withClass('wtab-xs text-right'),
                      DTColumnBuilder.newColumn('referencePrice', $translate('CMS_TXT_REFERENCE_PRICE')).withClass('wtab-xs text-right'),
                      DTColumnBuilder.newColumn('cnPrice', $translate('CMS_TXT_CN_PRICE')).withClass('wtab-xs text-right'),
                      DTColumnBuilder.newColumn('cnPriceRmb', $translate('CMS_TXT_CN_PRICE_RMB')).withClass('wtab-xs text-right'),
                      DTColumnBuilder.newColumn('cnPriceFinalRmb', $translate('CMS_TXT_CN_PRICE_FINAL_RMB')).withClass('wtab-xs text-right'),
                      DTColumnBuilder.newColumn('', $translate('CMS_TXT_CN_IS_ON_SALE')).withClass('wtab-xs text-center').renderWith(function () {
                          return ('<input type="checkbox" ng-model="$row.cnIsOnSale" >');
                      }),
                      DTColumnBuilder.newColumn('', $translate('CMS_TXT_CN_IS_APPROVED')).withClass('wtab-xs text-center').renderWith(function () {
                          return ('<input type="checkbox" ng-model="$row.cnIsApproved" >');
                      }),
                      DTColumnBuilder.newColumn('cnSales7Days', $translate('CMS_TXT_CN_SALES_7_DAYS')).withClass('wtab-xs text-right'),
                      DTColumnBuilder.newColumn('cnSales7DaysPercent', $translate('CMS_TXT_CN_SALES_7_DAYS_PERCENT')).withClass('wtab-xs text-right'),
                      DTColumnBuilder.newColumn('cnSales30Days', $translate('CMS_TXT_CN_SALES_30_DAYS')).withClass('wtab-xs text-right'),
                      DTColumnBuilder.newColumn('cnSales30DaysPercent', $translate('CMS_TXT_CN_SALES_30_DAYS_PERCENT')).withClass('wtab-xs text-right'),
                      DTColumnBuilder.newColumn('cnSalesInThisYear', $translate('CMS_TXT_CN_SALES_THIS_YEAR')).withClass('wtab-xs text-right'),
                      DTColumnBuilder.newColumn('cnSalesInThisYearPercent', $translate('CMS_TXT_CN_SALES_THIS_YEAR_PERCENT')).withClass('wtab-xs text-right'),
                      DTColumnBuilder.newColumn('cnOfficialPriceFinalRmb', $translate('CMS_TXT_CN_OFFICIAL_PRICE_FINAL_RMB')).withClass('wtab-xs text-right'),
                      //DTColumnBuilder.newColumn('cnOfficialFreeShippingType', $translate('CMS_TXT_CN_OFFICIAL_SHIPPING_TYPE')).withClass('wtab-xs '),
                      DTColumnBuilder.newColumn('', $translate('CMS_TXT_CN_OFFICIAL_IS_ON_SALE')).withClass('wtab-xs text-center').renderWith(function () {
                          return ('<input type="checkbox" ng-model="$row.cnOfficialIsOnSale" >');
                      }),
                      DTColumnBuilder.newColumn('', $translate('CMS_TXT_CN_OFFICIAL_IS_APPROVED')).withClass('wtab-xs text-center').renderWith(function () {
                          return ('<input type="checkbox" ng-model="$row.cnOfficialIsApproved" >');
                      }),
                      DTColumnBuilder.newColumn('cnOfficialPrePublishDateTime', $translate('CMS_TXT_CN_OFFICIAL_PRE_PUBLISH_DATE_TIME')).withClass('wtab-sm').notSortable(),
                      DTColumnBuilder.newColumn('cnOfficialSales7Days', $translate('CMS_TXT_CN_OFFICIAL_SALES_7_DAYS')).withClass('wtab-xs text-right'),
                      DTColumnBuilder.newColumn('cnOfficialSales7DaysPercent', $translate('CMS_TXT_CN_OFFICIAL_SALES_7_DAYS_PERCENT')).withClass('wtab-xs text-right'),
                      DTColumnBuilder.newColumn('cnOfficialSales30Days', $translate('CMS_TXT_CN_OFFICIAL_SALES_30_DAYS')).withClass('wtab-xs text-right'),
                      DTColumnBuilder.newColumn('cnOfficialSales30DaysPercent', $translate('CMS_TXT_CN_OFFICIAL_SALES_30_DAYS_PERCENT')).withClass('wtab-xs text-right'),
                      DTColumnBuilder.newColumn('cnOfficialSalesInThisYear', $translate('CMS_TXT_CN_OFFICIAL_SALES_THIS_YEAR')).withClass('wtab-xs text-right'),
                      DTColumnBuilder.newColumn('cnOfficialSalesInThisYearPercent', $translate('CMS_TXT_CN_OFFICIAL_SALES_THIS_YEAR_PERCENT')).withClass('wtab-xs text-right'),
                      DTColumnBuilder.newColumn('tbPriceFinalRmb', $translate('CMS_TXT_TB_PRICE_FINAL_RMB')).withClass('wtab-xs ext-right'),
                      //DTColumnBuilder.newColumn('tbFreeShippingType', $translate('CMS_TXT_TB_SHIPPING_TYPE')).withClass('wtab-xs '),
                      DTColumnBuilder.newColumn('', $translate('CMS_TXT_TB_IS_ON_SALE')).withClass('wtab-xs text-center').renderWith(function () {
                          return ('<input type="checkbox" ng-model="$row.tbIsOnSale" >');
                      }),
                      DTColumnBuilder.newColumn('', $translate('CMS_TXT_TB_IS_APPROVED')).withClass('wtab-xs text-center').renderWith(function () {
                          return ('<input type="checkbox" ng-model="$row.tbIsApproved" >');
                      }),
                      DTColumnBuilder.newColumn('tbPrePublishDateTime', $translate('CMS_TXT_TB_PRE_PUBLISH_DATE_TIME')).withClass('wtab-sm').notSortable(),
                      DTColumnBuilder.newColumn('tbPublishStatus', $translate('CMS_TXT_TB_PUBLISH_STATUS')).withClass('wtab-sm').notSortable(),
                      DTColumnBuilder.newColumn('', $translate('CMS_TXT_TB_NUMIID')).withClass('wtab-xs').renderWith(function () {
                          return ('<a href="" class="btn-main" ng-href="{{$root.cmsMaster.tmallUrl}}{{$row.tbNumIid}}" ng-bind="$row.tbNumIid"></a>');
                      }).notSortable(),
                      DTColumnBuilder.newColumn('tbPublishFaildComment', $translate('CMS_TXT_TB_COMMENT')).withClass('wtab-xs').notSortable(),
                      DTColumnBuilder.newColumn('tbSales7Days', $translate('CMS_TXT_TB_SALES_7_DAYS')).withClass('wtab-xs text-right'),
                      DTColumnBuilder.newColumn('tbSales7DaysPercent', $translate('CMS_TXT_TB_SALES_7_DAYS_PERCENT')).withClass('wtab-xs text-right'),
                      DTColumnBuilder.newColumn('tbSales30Days', $translate('CMS_TXT_TB_SALES_30_DAYS')).withClass('wtab-xs text-right'),
                      DTColumnBuilder.newColumn('tbSales30DaysPercent', $translate('CMS_TXT_TB_SALES_30_DAYS_PERCENT')).withClass('wtab-xs text-right'),
                      DTColumnBuilder.newColumn('tbSalesInThisYear', $translate('CMS_TXT_TB_SALES_THIS_YEAR')).withClass('wtab-xs text-right'),
                      DTColumnBuilder.newColumn('tbSalesInThisYearPercent', $translate('CMS_TXT_TB_SALES_THIS_YEAR_PERCENT')).withClass('wtab-xs text-right'),
                      DTColumnBuilder.newColumn('tmPriceFinalRmb', $translate('CMS_TXT_TM_PRICE_FINAL_RMB')).withClass('wtab-xs text-right'),
                      //DTColumnBuilder.newColumn('tmFreeShippingType', $translate('CMS_TXT_TM_SHIPPING_TYPE')).withClass('wtab-xs '),
                      DTColumnBuilder.newColumn('', $translate('CMS_TXT_TM_IS_ON_SALE')).withClass('wtab-xs text-center').renderWith(function () {
                          return ('<input type="checkbox" ng-model="$row.tmIsOnSale" >');
                      }),
                      DTColumnBuilder.newColumn('', $translate('CMS_TXT_TM_IS_APPROVED')).withClass('wtab-xs text-center').renderWith(function () {
                          return ('<input type="checkbox" ng-model="$row.tmIsApproved" >');
                      }),
                      DTColumnBuilder.newColumn('tmPrePublishDateTime', $translate('CMS_TXT_TM_PRE_PUBLISH_DATE_TIME')).withClass('wtab-sm').notSortable(),
                      DTColumnBuilder.newColumn('tmPublishStatus', $translate('CMS_TXT_TM_PUBLISH_STATUS')).withClass('wtab-sm').notSortable(),
                      DTColumnBuilder.newColumn('', $translate('CMS_TXT_TM_NUMIID')).withClass('wtab-xs').renderWith(function () {
                          return ('<a href="" class="btn-main" ng-href="{{$root.cmsMaster.tmallUrl}}{{$row.tmNumIid}}" ng-bind="$row.tmNumIid"></a>');
                      }).notSortable(),
                      DTColumnBuilder.newColumn('tmPublishFaildComment', $translate('CMS_TXT_TM_COMMENT')).withClass('wtab-xs').notSortable(),
                      DTColumnBuilder.newColumn('tmSales7Days', $translate('CMS_TXT_TM_SALES_7_DAYS')).withClass('wtab-xs text-right'),
                      DTColumnBuilder.newColumn('tmSales7DaysPercent', $translate('CMS_TXT_TM_SALES_7_DAYS_PERCENT')).withClass('wtab-xs text-right'),
                      DTColumnBuilder.newColumn('tmSales30Days', $translate('CMS_TXT_TM_SALES_30_DAYS')).withClass('wtab-xs text-right'),
                      DTColumnBuilder.newColumn('tmSales30DaysPercent', $translate('CMS_TXT_TM_SALES_30_DAYS_PERCENT')).withClass('wtab-xs text-right'),
                      DTColumnBuilder.newColumn('tmSalesInThisYear', $translate('CMS_TXT_TM_SALES_THIS_YEAR')).withClass('wtab-xs text-right'),
                      DTColumnBuilder.newColumn('tmSalesInThisYearPercent', $translate('CMS_TXT_TM_SALES_THIS_YEAR_PERCENT')).withClass('wtab-xs text-right'),
                      DTColumnBuilder.newColumn('tgPriceFinalRmb', $translate('CMS_TXT_TG_PRICE_FINAL_RMB')).withClass('wtab-xs text-right'),
                      //DTColumnBuilder.newColumn('tgFreeShippingType', $translate('CMS_TXT_TG_SHIPPING_TYPE')).withClass('wtab-xs '),
                      DTColumnBuilder.newColumn('', $translate('CMS_TXT_TG_IS_ON_SALE')).withClass('wtab-xs text-center').renderWith(function () {
                          return ('<input type="checkbox" ng-model="$row.tgIsOnSale" >');
                      }),
                      DTColumnBuilder.newColumn('', $translate('CMS_TXT_TG_IS_APPROVED')).withClass('wtab-xs text-center').renderWith(function () {
                          return ('<input type="checkbox" ng-model="$row.tgIsApproved" >');
                      }),
                      DTColumnBuilder.newColumn('tgPrePublishDateTime', $translate('CMS_TXT_TG_PRE_PUBLISH_DATE_TIME')).withClass('wtab-sm').notSortable(),
                      DTColumnBuilder.newColumn('tgPublishStatus', $translate('CMS_TXT_TG_PUBLISH_STATUS')).withClass('wtab-sm').notSortable(),
                      DTColumnBuilder.newColumn('tgNumIid', $translate('CMS_TXT_TG_NUMIID')).withClass('wtab-xs').renderWith(function () {
                          return ('<a href="" class="btn-main" ng-href="{{$root.cmsMaster.tmallUrl}}{{$row.tgNumIid}}" ng-bind="$row.tgNumIid"></a>');
                      }).notSortable(),
                      DTColumnBuilder.newColumn('tgPublishFaildComment', $translate('CMS_TXT_TG_COMMENT')).withClass('wtab-xs').notSortable(),
                      DTColumnBuilder.newColumn('tgSales7Days', $translate('CMS_TXT_TG_SALES_7_DAYS')).withClass('wtab-xs text-right'),
                      DTColumnBuilder.newColumn('tgSales7DaysPercent', $translate('CMS_TXT_TG_SALES_7_DAYS_PERCENT')).withClass('wtab-xs text-right'),
                      DTColumnBuilder.newColumn('tgSales30Days', $translate('CMS_TXT_TG_SALES_30_DAYS')).withClass('wtab-xs text-right'),
                      DTColumnBuilder.newColumn('tgSales30DaysPercent', $translate('CMS_TXT_TG_SALES_30_DAYS_PERCENT')).withClass('wtab-xs text-right'),
                      DTColumnBuilder.newColumn('tgSalesInThisYear', $translate('CMS_TXT_TG_SALES_THIS_YEAR')).withClass('wtab-xs text-right'),
                      DTColumnBuilder.newColumn('tgSalesInThisYearPercent', $translate('CMS_TXT_TG_SALES_THIS_YEAR_PERCENT')).withClass('wtab-xs text-right'),
                      DTColumnBuilder.newColumn('jdPriceFinalRmb', $translate('CMS_TXT_JD_PRICE_FINAL_RMB')).withClass('wtab-xs text-right'),
                      //DTColumnBuilder.newColumn('jdFreeShippingType', $translate('CMS_TXT_JD_SHIPPING_TYPE')).withClass('wtab-xs '),
                      DTColumnBuilder.newColumn('', $translate('CMS_TXT_JD_IS_ON_SALE')).withClass('wtab-xs text-center').renderWith(function () {
                          return ('<input type="checkbox" ng-model="$row.jdIsOnSale" >');
                      }),
                      DTColumnBuilder.newColumn('', $translate('CMS_TXT_JD_IS_APPROVED')).withClass('wtab-xs text-center').renderWith(function () {
                          return ('<input type="checkbox" ng-model="$row.jdIsApproved" >');
                      }),
                      DTColumnBuilder.newColumn('jdPrePublishDateTime', $translate('CMS_TXT_JD_PRE_PUBLISH_DATE_TIME')).withClass('wtab-sm').notSortable(),
                      DTColumnBuilder.newColumn('jdPublishStatus', $translate('CMS_TXT_JD_PUBLISH_STATUS')).withClass('wtab-sm').notSortable(),
                      DTColumnBuilder.newColumn('jdNumIid', $translate('CMS_TXT_JD_NUMIID')).withClass('wtab-xs').notSortable(),
                      DTColumnBuilder.newColumn('jdPublishFaildComment', $translate('CMS_TXT_JD_COMMENT')).withClass('wtab-xs text-right').notSortable(),
                      DTColumnBuilder.newColumn('jdSales7Days', $translate('CMS_TXT_JD_SALES_7_DAYS')).withClass('wtab-xs text-right'),
                      DTColumnBuilder.newColumn('jdSales7DaysPercent', $translate('CMS_TXT_JD_SALES_7_DAYS_PERCENT')).withClass('wtab-xs text-right'),
                      DTColumnBuilder.newColumn('jdSales30Days', $translate('CMS_TXT_JD_SALES_30_DAYS')).withClass('wtab-xs text-right'),
                      DTColumnBuilder.newColumn('jdSales30DaysPercent', $translate('CMS_TXT_JD_SALES_30_DAYS_PERCENT')).withClass('wtab-xs text-right'),
                      DTColumnBuilder.newColumn('jdSalesInThisYear', $translate('CMS_TXT_JD_SALES_THIS_YEAR')).withClass('wtab-xs text-right'),
                      DTColumnBuilder.newColumn('jdSalesInThisYearPercent', $translate('CMS_TXT_JD_SALES_THIS_YEAR_PERCENT')).withClass('wtab-xs text-right'),
                      DTColumnBuilder.newColumn('jgPriceFinalRmb', $translate('CMS_TXT_JG_PRICE_FINAL_RMB')).withClass('wtab-xs text-right'),
                      //DTColumnBuilder.newColumn('jgFreeShippingType', $translate('CMS_TXT_JG_SHIPPING_TYPE')).withClass('wtab-xs '),
                      DTColumnBuilder.newColumn('', $translate('CMS_TXT_JG_IS_ON_SALE')).withClass('wtab-xs text-center').renderWith(function () {
                          return ('<input type="checkbox" ng-model="$row.jgIsOnSale" >');
                      }),
                      DTColumnBuilder.newColumn('', $translate('CMS_TXT_JG_IS_APPROVED')).withClass('wtab-xs text-center').renderWith(function () {
                          return ('<input type="checkbox" ng-model="$row.jgIsApproved" >');
                      }),
                      DTColumnBuilder.newColumn('jgPrePublishDateTime', $translate('CMS_TXT_JG_PRE_PUBLISH_DATE_TIME')).withClass('wtab-sm').notSortable(),
                      DTColumnBuilder.newColumn('jgPublishStatus', $translate('CMS_TXT_JG_PUBLISH_STATUS')).withClass('wtab-sm').notSortable(),
                      DTColumnBuilder.newColumn('jgNumIid', $translate('CMS_TXT_JG_NUMIID')).withClass('wtab-xs').notSortable(),
                      DTColumnBuilder.newColumn('jgPublishFaildComment', $translate('CMS_TXT_JG_COMMENT')).withClass('wtab-xs').notSortable(),
                      DTColumnBuilder.newColumn('jgSales7Days', $translate('CMS_TXT_JG_SALES_7_DAYS')).withClass('wtab-xs text-right'),
                      DTColumnBuilder.newColumn('jgSales7DaysPercent', $translate('CMS_TXT_JG_SALES_7_DAYS_PERCENT')).withClass('wtab-xs text-right'),
                      DTColumnBuilder.newColumn('jgSales30Days', $translate('CMS_TXT_JG_SALES_30_DAYS')).withClass('wtab-xs text-right'),
                      DTColumnBuilder.newColumn('jgSales30DaysPercent', $translate('CMS_TXT_JG_SALES_30_DAYS_PERCENT')).withClass('wtab-xs text-right'),
                      DTColumnBuilder.newColumn('jgSalesInThisYear', $translate('CMS_TXT_JG_SALES_THIS_YEAR')).withClass('wtab-xs text-right'),
                      DTColumnBuilder.newColumn('jgSalesInThisYearPercent', $translate('CMS_TXT_JG_SALES_THIS_YEAR_PERCENT')).withClass('wtab-xs text-right')
                  ],
                  dtInstance:null
              };

              // 设置列的显示/隐藏
              cmsCommonService.doGetProductColumnsAtFirst('cn', $scope.dtProductList.columns);
          }
      }]);
});