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
    
    cmsApp.controller ('advanceSearchController', ['$scope', '$rootScope', '$q', '$location', '$routeParams', 'cmsCommonService', 'cmsRoute', 'searchService', 'systemCountry','DTOptionsBuilder','DTColumnBuilder','$translate','$compile',
      function ($scope, $rootScope, $q, $location, $routeParams, commonService, cmsRoute, searchService, systemCountry,DTOptionsBuilder,DTColumnBuilder,$translate,$compile) {

          var _ = require ('underscore');
          var commonUtil = require ('components/util/commonUtil');
          $scope.status = {};
          $scope.search = {};
          $scope.yesOrNo = [{"value":"1","name":"Yes"},{"value":"0","name":"No"}];
          $scope.publishStatus = [{"value":"0","name":"Pending"},{"value":"1","name":"Success"},{"value":"2","name":"Faild"}];

          $scope.dtProductList;
          /**
           * 初始化操作.
           */
          $scope.initialize = function () {
        	  
        	  $scope.status.open = true;
              $scope.search = {};
              doSearchProduct();
          };
          $scope.doSearch = function () {
        	  $scope.dtProductList.dtInstance.reloadData();
          }
          /**
           * 执行检索
           */
        
          function doSearchProduct () {
              var titleHtml = '<input type="checkbox" ng-model="showCase.selectAll" ng-click="showCase.toggleAll(showCase.selectAll, showCase.selected)">';
              $scope.dtProductList = {
                  options: DTOptionsBuilder.newOptions()
                      .withOption('processing', true)
                      .withOption('serverSide', true)
                      .withOption('scrollY', '400px')
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
                        DTColumnBuilder.newColumn('', $translate('CMS_TXT_CODE')).withClass('wtab-xsm text-center').renderWith(function (val, type, row, cell) {
                        	return ('<img class="prodImg" src="' + $rootScope.cmsMaster.imageUrl + row.productImgUrl + '"><br><a ng-controller="navigationController" href="" class="btn-main" ng-href="{{goProductPage(' + row.categoryId + ',' + row.modelId + ',' + row.productId + ')}}">' + row.code + '</a>');
                        }),
                        DTColumnBuilder.newColumn('cnName', $translate('CMS_TXT_NAME')).withClass('wtab-xsm'),
                        DTColumnBuilder.newColumn('isApprovedDescription', $translate('CMS_TXT_IS_APPROVED_DESCRIPTION')).withClass('wtab-xs text-center').renderWith(function (val, type, row, cell) {
                        	return ('<input type="checkbox" '+ (row.isApprovedDescription ? 'checked ' : '') + ' >');
                        }),
                        DTColumnBuilder.newColumn('colorMap', $translate('CMS_TXT_COLOR_MAP')).withClass('wtab-sm'),
                        DTColumnBuilder.newColumn('cnColor', $translate('CMS_TXT_COLOR')).withClass('wtab-sm'),
                        DTColumnBuilder.newColumn('madeInCountry', $translate('CMS_TXT_MADE_IN_COUNTRY')).withClass('wtab-xs'),
                        DTColumnBuilder.newColumn('', $translate('CMS_TXT_MATERIAL_FABRIC')).withClass('wtab-s').renderWith(function (val, type, row, cell) {
                            var materialFabric1 = commonUtil.isNotEmpty(row.materialFabric1) ? row.materialFabric1 : '';
                            var materialFabric2 = commonUtil.isNotEmpty(row.materialFabric2) ? row.materialFabric2 : '';
                            var materialFabric3 = commonUtil.isNotEmpty(row.materialFabric3) ? row.materialFabric3 : '';
                        	return ('<span>'+materialFabric1+'</span><br><span>'+materialFabric2+'</span><br><span">'+materialFabric3+'</span>');
                        }),
                        DTColumnBuilder.newColumn('cnShortDescription', $translate('CMS_TXT_SHORT_DESCRIPTION')).withClass('wtab-sm').renderWith(function (val, type, row, cell) {
                            return ('<span title="'+row.cnShortDescription+'">'+row.cnShortDescription.substring(0,100)+'</span>');
                        }),
                        DTColumnBuilder.newColumn('cnLongDescription', $translate('CMS_TXT_LONG_DESCRIPTION')).withClass('wtab-xsm').renderWith(function (val, type, row, cell) {
                            return ('<span title="'+row.cnLongDescription+'">'+row.cnLongDescription.substring(0,100)+'</span>');
                        }),
                        DTColumnBuilder.newColumn('quantity', $translate('CMS_TXT_QUANTITY')).withClass('wtab-s'),
                        DTColumnBuilder.newColumn('urlKey', $translate('CMS_TXT_URL_KEY')).withClass('wtab-xs text-center'),
                        DTColumnBuilder.newColumn('created', $translate('CMS_TXT_LAST_UPDATED_ON')).withClass('wtab-xsm'),
                        DTColumnBuilder.newColumn('modified', $translate('CMS_TXT_LAST_UPDATED_BY')).withClass('wtab-sm'),
                        DTColumnBuilder.newColumn('referenceMsrp', $translate('CMS_TXT_REFERENCE_MSRP')).withClass('wtab-sm'),
                        DTColumnBuilder.newColumn('referencePrice', $translate('CMS_TXT_REFERENCE_PRICE')).withClass('wtab-xs text-right'),
                        DTColumnBuilder.newColumn('firstSalePrice', $translate('CMS_TXT_FIRST_SALE_PRICE')).withClass('wtab-xs text-right'),
                        DTColumnBuilder.newColumn('cnPrice', $translate('CMS_TXT_CN_PRICE')).withClass('wtab-xs text-right'),
                        DTColumnBuilder.newColumn('effectivePrice', $translate('CMS_TXT_EFFECTIVE_PRICE')).withClass('wtab-xs text-right'),
                        DTColumnBuilder.newColumn('cnPriceRmb', $translate('CMS_TXT_CN_PRICE_RMB')).withClass('wtab-xs text-right'),
                        DTColumnBuilder.newColumn('cnPriceAdjustmentRmb', $translate('CMS_TXT_CN_PRICE_ADJUSTMENT_RMB')).withClass('wtab-xs text-right'),
                        DTColumnBuilder.newColumn('cnPriceFinalRmb', $translate('CMS_TXT_CN_PRICE_FINAL_RMB')).withClass('wtab-xs text-right'),
                        DTColumnBuilder.newColumn('', $translate('CMS_TXT_CN_IS_ON_SALE')).withClass('wtab-xs text-right').renderWith(function (val, type, row, cell) {
                            return ('<input type="checkbox" '+ (row.cnIsOnSale ? 'checked ' : '') + ' >');
                        }),
                        DTColumnBuilder.newColumn('', $translate('CMS_TXT_CN_IS_APPROVED')).withClass('wtab-xs text-center').renderWith(function (val, type, row, cell) {
                            return ('<input type="checkbox" '+ (row.cnIsApproved ? 'checked ' : '') + ' >');
                        }),
                        DTColumnBuilder.newColumn('cnPublishStatus', $translate('CMS_TXT_CN_PUBLISH_STATUS')).withClass('wtab-xs text-center'),
                        DTColumnBuilder.newColumn('cnNumIid', $translate('CMS_TXT_CN_NUMIID')).withClass('wtab-xs text-right'),
                        DTColumnBuilder.newColumn('cnPublishFaildComment', $translate('CMS_TXT_CN_COMMENT')).withClass('wtab-xs text-right'),
                        DTColumnBuilder.newColumn('cnOfficialPriceAdjustmentRmb', $translate('CMS_TXT_CN_OFFICIAL_PRICE_ADJUSTMENT_RMB')).withClass('wtab-xs text-right'),
                        DTColumnBuilder.newColumn('cnOfficialPriceFinalRmb', $translate('CMS_TXT_CN_OFFICIAL_PRICE_FINAL_RMB')).withClass('wtab-xs text-right'),
                        DTColumnBuilder.newColumn('cnOfficialFreeShippingType', $translate('CMS_TXT_CN_OFFICIAL_SHIPPING_TYPE')).withClass('wtab-xs text-right'),
                        DTColumnBuilder.newColumn('', $translate('CMS_TXT_CN_OFFICIAL_IS_ON_SALE')).withClass('wtab-xs ').renderWith(function (val, type, row, cell) {
                            return ('<input type="checkbox" '+ (row.cnOfficialIsOnSale ? 'checked ' : '') + ' >');
                        }),
                        DTColumnBuilder.newColumn('', $translate('CMS_TXT_CN_OFFICIAL_IS_APPROVED')).withClass('wtab-xs text-center').renderWith(function (val, type, row, cell) {
                            return ('<input type="checkbox" '+ (row.cnOfficialIsApproved ? 'checked ' : '') + ' >');
                        }),
                        DTColumnBuilder.newColumn('cnOfficialPrePublishDateTime', $translate('CMS_TXT_CN_OFFICIAL_PRE_PUBLISH_DATE_TIME')).withClass('wtab-xs text-center'),
                        DTColumnBuilder.newColumn('cnOfficialPublishStatus', $translate('CMS_TXT_CN_OFFICIAL_PUBLISH_STATUS')).withClass('wtab-sm'),
                        DTColumnBuilder.newColumn('cnOfficialNumIid', $translate('CMS_TXT_CN_OFFICIAL_NUMIID')).withClass('wtab-xs text-right'),
                        DTColumnBuilder.newColumn('cnOfficialPublishFaildComment', $translate('CMS_TXT_CN_OFFICIAL_COMMENT')).withClass('wtab-xs text-right'),
                        DTColumnBuilder.newColumn('tbPriceAdjustmentRmb', $translate('CMS_TXT_TB_PRICE_ADJUSTMENT_RMB')).withClass('wtab-xs text-right'),
                        DTColumnBuilder.newColumn('tbPriceFinalRmb', $translate('CMS_TXT_TB_PRICE_FINAL_RMB')).withClass('wtab-xs text-right'),
                        DTColumnBuilder.newColumn('tbFreeShippingType', $translate('CMS_TXT_TB_SHIPPING_TYPE')).withClass('wtab-xs ext-right'),
                        DTColumnBuilder.newColumn('', $translate('CMS_TXT_TB_IS_ON_SALE')).withClass('wtab-xs ').renderWith(function (val, type, row, cell) {
                            return ('<input type="checkbox" '+ (row.tbIsOnSale ? 'checked ' : '') + ' >');
                        }),
                        DTColumnBuilder.newColumn('', $translate('CMS_TXT_TB_IS_APPROVED')).withClass('wtab-xs text-center').renderWith(function (val, type, row, cell) {
                            return ('<input type="checkbox" '+ (row.tbIsApproved ? 'checked ' : '') + ' >');
                        }),
                        DTColumnBuilder.newColumn('tbPrePublishDateTime', $translate('CMS_TXT_TB_PRE_PUBLISH_DATE_TIME')).withClass('wtab-xs text-center'),
                        DTColumnBuilder.newColumn('tbPublishStatus', $translate('CMS_TXT_TB_PUBLISH_STATUS')).withClass('wtab-sm'),
                        DTColumnBuilder.newColumn('tbNumIid', $translate('CMS_TXT_TB_NUMIID')).withClass('wtab-xs text-right'),
                        DTColumnBuilder.newColumn('tbPublishFaildComment', $translate('CMS_TXT_TB_COMMENT')).withClass('wtab-xs text-right'),
                        DTColumnBuilder.newColumn('tmPriceAdjustmentRmb', $translate('CMS_TXT_TM_PRICE_ADJUSTMENT_RMB')).withClass('wtab-xs text-right'),
                        DTColumnBuilder.newColumn('tmPriceFinalRmb', $translate('CMS_TXT_TM_PRICE_FINAL_RMB')).withClass('wtab-xs text-right'),
                        DTColumnBuilder.newColumn('tmFreeShippingType', $translate('CMS_TXT_TM_SHIPPING_TYPE')).withClass('wtab-xs text-right'),
                        DTColumnBuilder.newColumn('', $translate('CMS_TXT_TM_IS_ON_SALE')).withClass('wtab-xs ').renderWith(function (val, type, row, cell) {
                            return ('<input type="checkbox" '+ (row.tmIsOnSale ? 'checked ' : '') + ' >');
                        }),
                        DTColumnBuilder.newColumn('', $translate('CMS_TXT_TM_IS_APPROVED')).withClass('wtab-xs text-center').renderWith(function (val, type, row, cell) {
                            return ('<input type="checkbox" '+ (row.tmIsApproved ? 'checked ' : '') + ' >');
                        }),
                        DTColumnBuilder.newColumn('tmPrePublishDateTime', $translate('CMS_TXT_TM_PRE_PUBLISH_DATE_TIME')).withClass('wtab-xs text-center'),
                        DTColumnBuilder.newColumn('tmPublishStatus', $translate('CMS_TXT_TM_PUBLISH_STATUS')).withClass('wtab-sm'),
                        DTColumnBuilder.newColumn('tmNumIid', $translate('CMS_TXT_TM_NUMIID')).withClass('wtab-xs text-right'),
                        DTColumnBuilder.newColumn('tmPublishFaildComment', $translate('CMS_TXT_TM_COMMENT')).withClass('wtab-xs text-right'),
                        DTColumnBuilder.newColumn('tgPriceAdjustmentRmb', $translate('CMS_TXT_TG_PRICE_ADJUSTMENT_RMB')).withClass('wtab-xs text-right'),
                        DTColumnBuilder.newColumn('tgPriceFinalRmb', $translate('CMS_TXT_TG_PRICE_FINAL_RMB')).withClass('wtab-xs text-right'),
                        DTColumnBuilder.newColumn('tgFreeShippingType', $translate('CMS_TXT_TG_SHIPPING_TYPE')).withClass('wtab-xs text-right'),
                        DTColumnBuilder.newColumn('', $translate('CMS_TXT_TG_IS_ON_SALE')).withClass('wtab-xs ').renderWith(function (val, type, row, cell) {
                            return ('<input type="checkbox" '+ (row.tgIsOnSale ? 'checked ' : '') + ' >');
                        }),
                        DTColumnBuilder.newColumn('', $translate('CMS_TXT_TG_IS_APPROVED')).withClass('wtab-xs text-center').renderWith(function (val, type, row, cell) {
                            return ('<input type="checkbox" '+ (row.tgIsApproved ? 'checked ' : '') + ' >');
                        }),
                        DTColumnBuilder.newColumn('tgPrePublishDateTime', $translate('CMS_TXT_TG_PRE_PUBLISH_DATE_TIME')).withClass('wtab-xs text-center'),
                        DTColumnBuilder.newColumn('tgPublishStatus', $translate('CMS_TXT_TG_PUBLISH_STATUS')).withClass('wtab-sm'),
                        DTColumnBuilder.newColumn('tgNumIid', $translate('CMS_TXT_TG_NUMIID')).withClass('wtab-xs text-right'),
                        DTColumnBuilder.newColumn('tgPublishFaildComment', $translate('CMS_TXT_TG_COMMENT')).withClass('wtab-xs text-right'),
                        DTColumnBuilder.newColumn('tgPriceAdjustmentRmb', $translate('CMS_TXT_JD_PRICE_ADJUSTMENT_RMB')).withClass('wtab-xs text-right'),
                        DTColumnBuilder.newColumn('jdPriceFinalRmb', $translate('CMS_TXT_JD_PRICE_FINAL_RMB')).withClass('wtab-xs text-right'),
                        DTColumnBuilder.newColumn('jdFreeShippingType', $translate('CMS_TXT_JD_SHIPPING_TYPE')).withClass('wtab-xs text-right'),
                        DTColumnBuilder.newColumn('', $translate('CMS_TXT_JD_IS_ON_SALE')).withClass('wtab-xs ').renderWith(function (val, type, row, cell) {
                            return ('<input type="checkbox" '+ (row.jdIsOnSale ? 'checked ' : '') + ' >');
                        }),
                        DTColumnBuilder.newColumn('', $translate('CMS_TXT_JD_IS_APPROVED')).withClass('wtab-xs text-center').renderWith(function (val, type, row, cell) {
                            return ('<input type="checkbox" '+ (row.jdIsApproved ? 'checked ' : '') + ' >');
                        }),
                        DTColumnBuilder.newColumn('jdPrePublishDateTime', $translate('CMS_TXT_JD_PRE_PUBLISH_DATE_TIME')).withClass('wtab-xs text-center'),
                        DTColumnBuilder.newColumn('jdPublishStatus', $translate('CMS_TXT_JD_PUBLISH_STATUS')).withClass('wtab-sm'),
                        DTColumnBuilder.newColumn('jdNumIid', $translate('CMS_TXT_JD_NUMIID')).withClass('wtab-xs text-right'),
                        DTColumnBuilder.newColumn('jdPublishFaildComment', $translate('CMS_TXT_JD_COMMENT')).withClass('wtab-xs text-right'),
                        DTColumnBuilder.newColumn('jgPriceAdjustmentRmb', $translate('CMS_TXT_JG_PRICE_ADJUSTMENT_RMB')).withClass('wtab-xs text-right'),
                        DTColumnBuilder.newColumn('jgPriceFinalRmb', $translate('CMS_TXT_JG_PRICE_FINAL_RMB')).withClass('wtab-xs text-right'),
                        DTColumnBuilder.newColumn('jgFreeShippingType', $translate('CMS_TXT_JG_SHIPPING_TYPE')).withClass('wtab-xs text-right'),
                        DTColumnBuilder.newColumn('', $translate('CMS_TXT_JG_IS_ON_SALE')).withClass('wtab-xs ').renderWith(function (val, type, row, cell) {
                            return ('<input type="checkbox" '+ (row.jgIsOnSale ? 'checked ' : '') + ' >');
                        }),
                        DTColumnBuilder.newColumn('', $translate('CMS_TXT_JG_IS_APPROVED')).withClass('wtab-xs text-center').renderWith(function (val, type, row, cell) {
                            return ('<input type="checkbox" '+ (row.jgIsApproved ? 'checked ' : '') + ' >');
                        }),
                        DTColumnBuilder.newColumn('jgPrePublishDateTime', $translate('CMS_TXT_JG_PRE_PUBLISH_DATE_TIME')).withClass('wtab-xs text-center'),
                        DTColumnBuilder.newColumn('jgPublishStatus', $translate('CMS_TXT_JG_PUBLISH_STATUS')).withClass('wtab-sm'),
                        DTColumnBuilder.newColumn('jgNumIid', $translate('CMS_TXT_JG_NUMIID')).withClass('wtab-xs text-right'),
                        DTColumnBuilder.newColumn('jgPublishFaildComment', $translate('CMS_TXT_JG_COMMENT')).withClass('wtab-xs text-right'),

                  ],
                  dtInstance:1
              };
          }
          $scope.doExport = function () {

            	$('#downloadForm').submit();
                          
           };
         
          $scope.doSearchProductData = function (data, draw) {
              if (data.draw === 1) {
            	  initData={};
            	  initData.recordsFiltered = 0;
            	  initData.recordsTotal = 0;
            	  initData.draw= 1;
            	  initData.data={};
                  draw(initData);
                  return;
              }
              
              return searchService.doAdvanceSearch(data,$scope.search)
                  .then (function (data) {
                  $scope.productList = data.data;
                  return draw(data)
              });
          };
      }]);
});