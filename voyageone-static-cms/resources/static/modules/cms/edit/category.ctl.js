/**
 * @Name:    category.ctl.js
 * @Date:    2015/6/16
 *
 * @User:    Edward
 * @Version: 1.0.0
 */

define (function (require) {
    var cmsApp = require ('modules/cms/cms.module');
    require ('modules/cms/edit/edit.service');
    require ('modules/cms/common/common.service');
    require ('modules/cms/service/mainCategory.service');

    require ('modules/cms/popup/addToPromotion/popAddToPromotion.ctl');
    require ('modules/cms/popup/setSizeChart/setSizeChart.ctl');
    require ('modules/cms/popup/setCnProductProperty/popSetCnProductProperty.ctl');
    require ('modules/cms/popup/setCnProductShare/popSetCnProductShare.ctl');
    require ('modules/cms/popup/setProductColumns/popSetProductColumns.ctl');
    require ('modules/cms/masterPropValueSetting/setMtPropValuePopUpController');

    cmsApp.controller ('editCategoryController', ['$scope', '$rootScope', '$q', '$location', '$routeParams', '$translate', '$compile', 'cmsCommonService'
        , 'editCategoryService', 'cmsRoute', 'mainCategoryService', 'mainCategoryLevel', 'notify', 'ngDialog', 'cmsPopupPages', 'systemCountry',
        'DTOptionsBuilder', 'DTColumnBuilder', '$modal', 'vConfirm', 'userService', 'commonService',
        function ($scope, $rootScope, $q, $location, $routeParams, $translate, $compile, cmsCommonService
            , categoryService, cmsRoute, mainCategoryService, mainCategoryLevel, notify, ngDialog, cmsPopupPages, systemCountry
            , DTOptionsBuilder, DTColumnBuilder, $modal, vConfirm, userService, commonService) {

            var _ = require ('underscore');
            var commonUtil = require ('components/util/commonUtil');

            //var bind = function(fn, me){ return function(){ return fn.apply(me, arguments); }; };

            $scope.categoryName = "";

            /**
             * 初始化操作.
             */
            $scope.initialize = function () {

                if (!isNaN(parseInt($routeParams.categoryId))) {
                    $scope.currentCategoryId = $routeParams.categoryId;
                    $scope.categoryId = isNaN(parseInt($routeParams.categoryId)) ? 2: parseInt($routeParams.categoryId);

                    doGetNavigationByCategoryId();
                    doGetUSCategoryInfo ();
                    doGetCNCategoryInfo ();
                    doGetCategoryCNPriceSettingInfo ();

                    $scope.doShowProductList('');
                    $scope.doShowCategoryList('');
                    $scope.doShowModelList('');
                }
            };

            /**
             * 显示不同国家的sub category list.
             * @param country
             */
            $scope.doShowCategoryList = function (country) {

                // 根据用户的设定显示不同的
                switch (country) {
                    case systemCountry.US:
                        doGetUSSubCategoryList ();
                        $scope.showCategoryListFlag = systemCountry.US;
                        break;
                    case systemCountry.CN:
                        doGetCNSubCategoryList ();
                        $scope.showCategoryListFlag = systemCountry.CN;
                        break;
                    default :
                        doGetUSSubCategoryList ();
                        $scope.showCategoryListFlag = systemCountry.US;
                        break;
                }
            };

            /**
             * 显示不同国家的model list.
             * @param country
             */
            $scope.doShowModelList = function (country){

                // 根据用户的设定显示不同的
                switch (country) {
                    case systemCountry.US:
                        doGetUSModelList ();
                        $scope.showModelListFlag = systemCountry.US;
                        break;
                    case systemCountry.CN:
                        doGetCNModelList ();
                        $scope.showModelListFlag = systemCountry.CN;
                        break;
                    default :
                        doGetUSModelList ();
                        $scope.showModelListFlag = systemCountry.US;
                        break;
                }
            };

            /**
             * 显示不同国家的product list.
             * @param country
             */
            $scope.doShowProductList = function (country) {

                // 初始化Us Product 选中信息.
                $scope.usProductInfo = {
                    selectAllFlag: false,
                    selectOneFlagList: [],
                    selectIdList: [],
                    selectDataList: [],
                    currentPageIdList: [],
                    currentPageDataList:[]
                };

                // 初始化Cn Product 选中信息.
                $scope.cnProductInfo = {
                    selectAllFlag: false,
                    selectOneFlagList: [],
                    selectIdList: [],
                    selectDataList: [],
                    currentPageIdList: [],
                    currentPageDataList:[]
                };

                // 根据用户的设定显示不同的
                switch (country) {
                    case systemCountry.US:
                        $scope.dtUsProductList.dtInstance.reloadData();
                        $scope.showProductListFlag = systemCountry.US;
                        break;
                    case systemCountry.CN:
                        $scope.dtCnProductList.dtInstance.reloadData();
                        $scope.showProductListFlag = systemCountry.CN;
                        break;
                    default :
                        doGetUSProductList ();
                        doGetCNProductList ();
                        $scope.showProductListFlag = systemCountry.US;
                        break;
                }
            };

            /**
             * 跳转到New Category页面
             */
            $scope.doAddCategory = function () {
                $location.path (commonUtil.returnReallyPath (cmsRoute.cms_new_category_withCategoryId.hash, $scope.currentCategoryId));
            };

            /**
             * 跳转到New Model页面.
             */
            $scope.doAddModel = function () {
                $location.path (commonUtil.returnReallyPath (cmsRoute.cms_new_model_withCategoryId.hash, $scope.currentCategoryId));
            };

            /** US Category Tab的操作 Start **/
            /**
             * 执行Effective处理.
             */
            $scope.doUpdateEffective = function () {
                vConfirm('CORE_TXT_MESSAGE_CHANGE_TO_EFFECTIVE','').result.then(function() {
                    var isEffective = $scope.usCategoryInfo.isEffective;
                    $scope.undoUpdateUSCategoryInfo();
                    $scope.usCategoryInfo.isEffective = isEffective;
                    $scope.doUpdateUSCategoryInfo();
                }, function() {
                    $scope.usCategoryInfo.isEffective = !$scope.usCategoryInfo.isEffective;
                })
            };
            /**
             * 更新US Category信息.
             */
            $scope.doUpdateUSCategoryInfo = function () {

                categoryService.doUpdateUSCategoryInfo ($scope.currentCategoryId, $scope.usCategoryInfo)
                    .then (function () {
                    notify.success("CMS_TXT_MSG_UPDATE_SUCCESS");
                    commonService.doGetCMSMasterInfo(userService.getSelChannel(), false);
                    doGetNavigationByCategoryId();
                    doGetUSCategoryInfo ();
                }, function () {
                    notify.danger("CMS_TXT_MSG_UPDATE_FAILED");
                    doGetUSCategoryInfo ();
                });
            };

            /**
             * 取消US Category信息的变更.
             */
            $scope.undoUpdateUSCategoryInfo = function () {
                $scope.usCategoryInfo = angular.copy ($scope.oldUsCategoryInfo);
                $scope.usCategoryIsChanged = false;
            };

            /**
             * 检查US Category Info是否发生变化
             */
            $scope.usCategoryChanged = function () {
                $scope.usCategoryIsChanged = !_.isEqual ($scope.usCategoryInfo, $scope.oldUsCategoryInfo);
            };

            /**
             * 清空Main Category选择内容.
             */
            $scope.clearUSMainCategoryId = function () {
                $scope.usCategoryInfo.mainCategoryId = '';
                $scope.usCategoryChanged ();
            };

            /**
             * 清空Amazon Category选择内容.
             */
            $scope.clearAmazonBrowseCategoryId = function () {
                $scope.usCategoryInfo.amazonBrowseCategoryId = '';
                $scope.usCategoryChanged ();
            };

            /**
             * 清空Google Category选择内容.
             */
            $scope.clearGoogleCategoryId = function () {
                $scope.usCategoryInfo.googleCategoryId = '';
                $scope.usCategoryChanged ();
            };

            /**
             * 清空PriceGrabber Category选择内容.
             */
            $scope.clearPriceGrabberCategoryId = function () {
                $scope.usCategoryInfo.priceGrabberCategoryId = '';
                $scope.usCategoryChanged ();
            };
            /** US Category Tab的操作 End **/

            /** CN Category Tab的操作 Start **/
            /**
             * 更新CN Category信息.
             */
            $scope.doUpdateCNCategoryInfo = function () {

                categoryService.doUpdateCNCategoryInfo ($scope.currentCategoryId, $scope.cnCategoryInfo)
                    .then (function () {
                    notify.success("CMS_TXT_MSG_UPDATE_SUCCESS");
                    doGetCNCategoryInfo ();
                }, function () {
                    notify.danger("CMS_TXT_MSG_UPDATE_FAILED");
                    doGetCNCategoryInfo ();
                });
            };

            /**
             * 取消CN Category信息的变更.
             */
            $scope.undoUpdateCNCategoryInfo = function () {
                $scope.cnCategoryInfo = angular.copy ($scope.oldCnCategoryInfo);
                $scope.cnCategoryIsChanged = false;
            };

            /**
             * 检查CN Category Info是否发生变化
             */
            $scope.cnCategoryChanged = function () {
                $scope.cnCategoryIsChanged = !_.isEqual ($scope.cnCategoryInfo, $scope.oldCnCategoryInfo);
            };

            /**
             * 清空HS Code SH选择内容.
             */
            $scope.clearHsCodeId = function () {
                $scope.cnCategoryInfo.hsCodeId = '';
                $scope.cnCategoryChanged ();
            };

            /**
             * 清空HS Code GZ选择内容.
             */
            $scope.clearHsCodePuId = function () {
                $scope.cnCategoryInfo.hsCodePuId = '';
                $scope.cnCategoryChanged ();
            };

            /**
             * 清空Main Category选择内容.
             */
            $scope.clearCNMainCategoryId = function () {
                $scope.cnCategoryInfo.mainCategoryId = '';
                $scope.cnCategoryChanged ();
            };

            /**
             * 清空Size Chart选择内容.
             */
            $scope.clearSizeChartId = function () {
                $scope.cnCategoryInfo.sizeChartId = '';
                $scope.cnCategoryChanged ();
            };

            /**
             * 跳转到MainCategory设定页面.
             */
            $scope.goMainCategoryPage = function () {
                var data = {};
                data.channelId = $scope.cnCategoryInfo.channelId;
                data.mainCategoryId = $scope.cnCategoryInfo.mainCategoryId;
                data.parentLevel = $scope.cnCategoryInfo.mainParentCategoryTypeId;
                data.parentId = $scope.cnCategoryInfo.mainParentCategoryId;
                data.currentLevel = mainCategoryLevel.category;
                data.currentId = $scope.cnCategoryInfo.categoryId;
                mainCategoryService.setMainCategoryParam(data);
                
                mainCategoryService.setMainCategoryReturnUrl($location.path());

                $location.path(cmsRoute.cms_masterPropValue_setting.hash);
            };
            /** CN Category Tab的操作 End **/

            /** CN Category PriceSetting Tab的操作 Start **/
            /**
             * 更新CN Category PriceSetting信息.
             */
            $scope.doUpdateCategoryCNPriceSettingInfo = function () {

                categoryService.doUpdateCategoryCNPriceSettingInfo ($scope.currentCategoryId, $scope.cnPriceSettingInfo)
                    .then (function () {
                    notify.success("CMS_TXT_MSG_UPDATE_SUCCESS");
                    doGetCategoryCNPriceSettingInfo ();
                    doGetUSProductList();
                    doGetCNProductList();
                }, function () {
                    notify.danger("CMS_TXT_MSG_UPDATE_FAILED");
                    doGetCategoryCNPriceSettingInfo ();
                    doGetUSProductList();
                    doGetCNProductList();
                });
            };

            /**
             * 取消CN Category PriceSetting信息的变更.
             */
            $scope.undoUpdateCategoryCNPriceSettingInfo = function () {
                $scope.cnPriceSettingInfo = angular.copy ($scope.oldCnPriceSettingInfo);
                $scope.cnPriceSettingIsChanged = false;
            };

            /**
             * 检查CN PriceSetting Info是否发生变化
             */
            $scope.cnPriceSettingChanged = function () {
                $scope.cnPriceSettingIsChanged = !_.isEqual ($scope.cnPriceSettingInfo, $scope.oldCnPriceSettingInfo);
            };

            /**
             * 清空basePriceId选择内容.
             */
            $scope.clearBasePriceId = function () {
                $scope.cnPriceSettingInfo.basePriceId = '';
                $scope.cnPriceSettingChanged ();
            };

            /**
             * 清空overHeard1选择内容.
             */
            $scope.clearOverHeard1 = function () {
                $scope.cnPriceSettingInfo.overHeard1 = '0.00';
                $scope.cnPriceSettingChanged ();
            };

            /**
             * 清空overHeard2选择内容.
             */
            $scope.clearOverHeard2 = function () {
                $scope.cnPriceSettingInfo.overHeard2 = '0.00';
                $scope.cnPriceSettingChanged ();
            };
            /** CN Category PriceSetting Tab的操作 End **/

            /** 更新datable的处理 Start **/
            /**
             * 更新US Category信息.
             * @param usCategoryInfo
             */
            $scope.doBatchUpdateUSCategory = function (usCategoryInfo) {

                categoryService.doUpdateUSCategoryInfo ($scope.currentCategoryId, usCategoryInfo)
                    .then (function () {
                    notify.success("CMS_TXT_MSG_UPDATE_SUCCESS");
                    doGetUSSubCategoryList();
                }, function () {
                    notify.danger("CMS_TXT_MSG_UPDATE_FAILED");
                    doGetUSSubCategoryList();
                });
            };

            /**
             * 更新CN Category信息.
             * @param cnCategoryInfo
             */
            $scope.doBatchUpdateCNCategory = function (cnCategoryInfo) {

                categoryService.doUpdateCNCategoryInfo ($scope.currentCategoryId, cnCategoryInfo)
                    .then (function () {
                    notify.success("CMS_TXT_MSG_UPDATE_SUCCESS");
                    doGetCNSubCategoryList();
                }, function () {
                    notify.danger("CMS_TXT_MSG_UPDATE_FAILED");
                    doGetCNSubCategoryList();
                });
            };

            /**
             * 取得US Sub Category List信息.
             * @param data
             * @param draw
             * @returns {*}
             */
            $scope.doGetUSProductList = function (data, draw) {
                if (data.draw === 1 || !$scope.usProductList) {
                    draw({data: []});
                }
                return categoryService.doGetUSProductList (data, $scope.currentCategoryId)
                    .then (function (data) {
                    $scope.usProductInfo.selectAllFlag = true;
                    $scope.usProductInfo.currentPageIdList = [];
                    $scope.usProductInfo.currentPageDataList = [];

                    _.forEach(data.data, function (productInfo) {
                        var productId = parseInt(productInfo.productId);

                        $scope.usProductInfo.currentPageIdList.push({id: productId});
                        $scope.usProductInfo.currentPageDataList.push({id: productId, code: productInfo.code, name: productInfo.name});

                        if (!$scope.usProductInfo.selectOneFlagList.hasOwnProperty(productId) || !$scope.usProductInfo.selectOneFlagList[productId]) {
                            $scope.usProductInfo.selectAllFlag = false;
                        }
                    });
                    return draw(data)
                });
            };

            /**
             * 取得CN Category List信息.
             * @param data
             * @param draw
             * @returns {*}
             */
            $scope.doGetCNProductList = function (data, draw) {
                if (data.draw === 1 || !$scope.cnProductList) {
                    draw({data: []});
                }
                return categoryService.doGetCNProductList (data, $scope.currentCategoryId)
                    .then (function (data) {
                    $scope.cnProductInfo.selectAllFlag = true;
                    $scope.cnProductInfo.currentPageIdList = [];
                    $scope.cnProductInfo.currentPageDataList = [];

                    _.forEach(data.data, function (productInfo) {
                        var productId = parseInt(productInfo.productId);

                        $scope.cnProductInfo.currentPageIdList.push({id: productId});
                        $scope.cnProductInfo.currentPageDataList.push({id: productId, code: productInfo.code, name: productInfo.cnName});

                        if (!$scope.cnProductInfo.selectOneFlagList.hasOwnProperty(productId) || !$scope.cnProductInfo.selectOneFlagList[productId]) {
                            $scope.cnProductInfo.selectAllFlag = false;
                        }
                    });
                    return draw(data)
                });
            };
            /** 更新datable的处理 End **/

            /** popup需要回调函数的处理 Start **/
            $scope.reFreshFunc = {};

            /**
             * 刷新cnCategory信息.
             */
            $scope.reFreshFunc.doRefreshCnCategoryInfo = function () {
                doGetCNCategoryInfo ();
            };

            /**
             * 刷新usProduct信息.
             */
            $scope.reFreshFunc.doRefreshUsProductInfo = function () {
                doGetUSProductList ();
            };

            /**
             * 刷新usProduct信息.
             */
            $scope.reFreshFunc.doRefreshCnProductInfo = function () {
                doGetCNProductList ();
            };
            /** popup需要回调函数的处理 End **/

            /**
             * 取得US Category信息.
             */
            function doGetUSCategoryInfo () {
                categoryService.doGetUSCategoryInfo ($scope.currentCategoryId)
                    .then (function (data) {
                    $scope.usCategoryInfo = data;
                    $scope.categoryName = angular.copy (data.name);
                    $scope.oldUsCategoryInfo = angular.copy ($scope.usCategoryInfo);
                    $scope.usCategoryIsChanged = false;
                });
            }

            /**
             * 取得CN Category信息.
             */
            function doGetCNCategoryInfo () {
                categoryService.doGetCNCategoryInfo ($scope.currentCategoryId)
                    .then (function (data) {
                    $scope.cnCategoryInfo = data;
                    $scope.oldCnCategoryInfo = angular.copy ($scope.cnCategoryInfo);
                    $scope.cnCategoryIsChanged = false;
                });
            }

            /**
             * 取得CN Category PriceSetting信息.
             */
            function doGetCategoryCNPriceSettingInfo () {
                categoryService.doGetCategoryCNPriceSettingInfo ($scope.currentCategoryId)
                    .then (function (data) {
                    $scope.cnPriceSettingInfo = data;
                    $scope.oldCnPriceSettingInfo = angular.copy ($scope.cnPriceSettingInfo);
                    $scope.cnPriceSettingIsChanged = false;
                });
            }

            /**
             * 取得US Sub Category List信息.
             */
            function doGetUSSubCategoryList () {
                categoryService.doGetUSSubCategoryList ($scope.currentCategoryId)
                    .then (function (data) {
                    $scope.usSubCategoryList = data;
                });
            }

            /**
             * 取得CN Sub Category List信息.
             */
            function doGetCNSubCategoryList () {
                categoryService.doGetCNSubCategoryList ($scope.currentCategoryId)
                    .then (function (data) {
                    $scope.cnSubCategoryList = data;
                });
            }

            /**
             * 取得US Model List信息.
             */
            function doGetUSModelList () {
                categoryService.doGetUSModelList ($scope.currentCategoryId)
                    .then (function (data) {
                    $scope.usModelList = data;
                });
            }

            /**
             * 取得CN Model List信息.
             */
            function doGetCNModelList () {
                categoryService.doGetCNModelList ($scope.currentCategoryId)
                    .then (function (data) {
                    $scope.cnModelList = data;
                });
            }

            /**
             * 取得US Category List信息.
             */
            function doGetUSProductList () {
                var titleHtml = '<input ng-controller="selectController" ng-model="usProductInfo.selectAllFlag" type="checkbox" ng-click="selectAll(usProductInfo)">';
                $scope.dtUsProductList = {
                    options: DTOptionsBuilder.newOptions()
                        .withOption('processing', true)
                        .withOption('serverSide', true)
                        .withOption('scrollY', '400px')
                        .withOption('scrollX', '100%')
                        .withOption('scrollCollapse', true)
                        .withOption('ajax', $scope.doGetUSProductList)
                        .withOption('createdRow',  function(row, data, dataIndex) {
                            $compile(angular.element(row).contents())($scope);
                        })
                        .withOption('headerCallback', function(header) {
                            if (!$scope.headerCompiledUs) {
                                // Use this headerCompiled field to only compile header once
                                $scope.headerCompiledUs = true;
                                $compile(angular.element(header).contents())($scope);
                            }
                        })
                        .withDataProp('data')
                        .withPaginationType('full_numbers'),
                    columns: [DTColumnBuilder.newColumn(null).withTitle(titleHtml).notSortable().withClass('wtab-check').renderWith(function (val, type, row, cell) {
                        var productId = parseInt(row.productId);
                        if (!$scope.usProductInfo.selectOneFlagList.hasOwnProperty(productId)) {
                            $scope.usProductInfo.selectOneFlagList[productId] = false;
                        }
                        return '<input ng-controller="selectController" type="checkbox" ng-model="usProductInfo.selectOneFlagList['+productId+']" ng-click="selectOne(' + productId + ', usProductInfo)">';
                    })],
                    dtInstance: null
                };

                // 设置product属性的显示/非显示
                var usProductColumns = [
                    DTColumnBuilder.newColumn('', $translate('CMS_TXT_CODE')).withClass('wtab-xs text-center').renderWith(function (val, type, row, cell) {
                        return ('<img class="prodImg" src="' + $rootScope.cmsMaster.imageUrl + row.productImgUrl + '"><br><a ng-controller="navigationController" href="" class="btn-main" ng-href="{{goProductPage(' + $scope.categoryId + ',' + row.modelId + ',' + row.productId + ')}}">' + row.code + '</a>');
                    }),
                    DTColumnBuilder.newColumn('name', $translate('CMS_TXT_NAME')).withClass('wtab-xsm'),
                    DTColumnBuilder.newColumn('displayOrder', $translate('CMS_TXT_DISPLAY_ORDER')).withClass('wtab-xs text-center'),
                    DTColumnBuilder.newColumn('', $translate('CMS_TXT_IS_HIGH_LIGHT_PRODUCT')).withClass('wtab-xs text-center').renderWith(function (val, type, row, cell) {
                        return ('<input type="checkbox" '+ (row.isHighLightProduct ? 'checked ' : '') + ' >');
                    }),
                    DTColumnBuilder.newColumn('productType', $translate('CMS_TXT_PRODUCT_TYPE')).withClass('wtab-xs'),
                    DTColumnBuilder.newColumn('brand', $translate('CMS_TXT_BRAND')).withClass('wtab-xs'),
                    DTColumnBuilder.newColumn('sizeType', $translate('CMS_TXT_SIZE_TYPE')).withClass('wtab-xs'),
                    DTColumnBuilder.newColumn('', $translate('CMS_TXT_IS_NEW_ARRIVAL')).withClass('wtab-xs text-center').renderWith(function (val, type, row, cell) {
                        return ('<input type="checkbox" '+ (row.isNewArrival ? 'checked ' : '') + ' >');
                    }),
                    DTColumnBuilder.newColumn('', $translate('CMS_TXT_IS_REWARD_ELIGIBLE')).withClass('wtab-xs text-center').renderWith(function (val, type, row, cell) {
                        return ('<input type="checkbox" '+ (row.isRewardEligible ? 'checked ' : '') + ' >');
                    }),
                    DTColumnBuilder.newColumn('', $translate('CMS_TXT_IS_DISCOUNT_ELIGIBLE')).withClass('wtab-xs text-center').renderWith(function (val, type, row, cell) {
                        return ('<input type="checkbox" '+ (row.isDiscountEligible ? 'checked ' : '') + ' >');
                    }),
                    DTColumnBuilder.newColumn('', $translate('CMS_TXT_IS_PHONE_ORDER_ONLY')).withClass('wtab-xs text-center').renderWith(function (val, type, row, cell) {
                        return ('<input type="checkbox" '+ (row.isPhoneOrderOnly ? 'checked ' : '') + ' >');
                    }),
                    DTColumnBuilder.newColumn('', $translate('CMS_TXT_IS_APPROVED_DESCRIPTION')).withClass('wtab-xs text-center').renderWith(function (val, type, row, cell) {
                        return ('<input type="checkbox" '+ (row.isApprovedDescription ? 'checked ' : '') + ' >');
                    }),
                    DTColumnBuilder.newColumn('colorMap', $translate('CMS_TXT_COLOR_MAP')).withClass('wtab-xs'),
                    DTColumnBuilder.newColumn('color', $translate('CMS_TXT_COLOR')).withClass('wtab-sm'),
                    DTColumnBuilder.newColumn('madeInCountry', $translate('CMS_TXT_MADE_IN_COUNTRY')).withClass('wtab-xs'),
                    DTColumnBuilder.newColumn('', $translate('CMS_TXT_MATERIAL_FABRIC')).withClass('wtab-sm').renderWith(function (val, type, row, cell) {
                        return ('<span>'+row.materialFabric+'</span>');
                    }),
                    DTColumnBuilder.newColumn('usAbstract', $translate('CMS_TXT_ABSTRACT')).withClass('wtab-sm'),
                    DTColumnBuilder.newColumn('accessory', $translate('CMS_TXT_ACCESSORY')).withClass('wtab-sm'),
                    DTColumnBuilder.newColumn('', $translate('CMS_TXT_SHORT_DESCRIPTION')).withClass('wtab-xsm').renderWith(function (val, type, row, cell) {
                        var shortDescription = commonUtil.isNotEmpty(row.shortDescription) ? row.shortDescription : '';
                        return ('<span title="'+shortDescription+'">'+shortDescription.substring(0,100)+'</span>');
                    }),
                    DTColumnBuilder.newColumn('', $translate('CMS_TXT_LONG_DESCRIPTION')).withClass('wtab-xsm').renderWith(function (val, type, row, cell) {
                        var longDescription = commonUtil.isNotEmpty(row.longDescription) ? row.longDescription : '';
                        return ('<span title="'+longDescription+'">'+longDescription.substring(0,100)+'</span>');
                    }),
                    DTColumnBuilder.newColumn('orderLimitCount', $translate('CMS_TXT_ORDER_LIMIT_COUNT')).withClass('wtab-xs text-center'),
                    DTColumnBuilder.newColumn('promotionTag', $translate('CMS_TXT_PROMOTION_TAG')).withClass('wtab-xs'),
                    DTColumnBuilder.newColumn('quantity', $translate('CMS_TXT_QUANTITY')).withClass('wtab-xs text-center'),
                    DTColumnBuilder.newColumn('urlKey', $translate('CMS_TXT_URL_KEY')).withClass('wtab-xsm'),
                    DTColumnBuilder.newColumn('created', $translate('CMS_TXT_CREATED_ON')).withClass('wtab-sm'),
                    DTColumnBuilder.newColumn('modified', $translate('CMS_TXT_LAST_UPDATED_ON')).withClass('wtab-sm'),
                    DTColumnBuilder.newColumn('msrp', $translate('CMS_TXT_MSRP')).withClass('wtab-xs text-right'),

                    DTColumnBuilder.newColumn('usOfficialPrice', $translate('CMS_TXT_US_OFFICIAL_PRICE')).withClass('wtab-xs text-right'),
                    DTColumnBuilder.newColumn('usOfficialFreeShippingType', $translate('CMS_TXT_US_OFFICIAL_SHIPPING_TYPE')).withClass('wtab-xs '),
                    DTColumnBuilder.newColumn('', $translate('CMS_TXT_US_OFFICIAL_IS_ON_SALE')).withClass('wtab-xs text-center').renderWith(function (val, type, row, cell) {
                        return ('<input type="checkbox" '+ (row.usOfficialIsOnSale ? 'checked ' : '') + ' >');
                    }),
                    DTColumnBuilder.newColumn('', $translate('CMS_TXT_US_OFFICIAL_IS_APPROVED')).withClass('wtab-xs text-center').renderWith(function (val, type, row, cell) {
                        return ('<input type="checkbox" '+ (row.usOfficialIsApproved ? 'checked ' : '') + ' >');
                    }),
                    DTColumnBuilder.newColumn('usOfficialSneakerRXSales7Days', $translate('CMS_TXT_US_OFFICIAL_SNEAKER_RX_SALES_7_DAYS')).withClass('wtab-xs text-right'),
                    DTColumnBuilder.newColumn('usOfficialSneakerRXSales7DaysPercent', $translate('CMS_TXT_US_OFFICIAL_SNEAKER_RX_SALES_7_DAYS_PERCENT')).withClass('wtab-xs text-right'),
                    DTColumnBuilder.newColumn('usOfficialSneakerRXSales30Days', $translate('CMS_TXT_US_OFFICIAL_SNEAKER_RX_SALES_30_DAYS')).withClass('wtab-xs text-right'),
                    DTColumnBuilder.newColumn('usOfficialSneakerRXSales30DaysPercent', $translate('CMS_TXT_US_OFFICIAL_SNEAKER_RX_SALES_30_DAYS_PERCENT')).withClass('wtab-xs text-right'),
                    DTColumnBuilder.newColumn('usOfficialSneakerRXSalesInThisYear', $translate('CMS_TXT_US_OFFICIAL_SNEAKER_RX_SALES_THIS_YEAR')).withClass('wtab-xs text-right'),
                    DTColumnBuilder.newColumn('usOfficialSneakerRXSalesInThisYearPercent', $translate('CMS_TXT_US_OFFICIAL_SNEAKER_RX_SALES_THIS_YEAR_PERCENT')).withClass('wtab-xs text-right'),
                    DTColumnBuilder.newColumn('usOfficialSneakerSales7Days', $translate('CMS_TXT_US_OFFICIAL_SNEAKER_SALES_7_DAYS')).withClass('wtab-xs text-right'),
                    DTColumnBuilder.newColumn('usOfficialSneakerSales7DaysPercent', $translate('CMS_TXT_US_OFFICIAL_SNEAKER_SALES_7_DAYS_PERCENT')).withClass('wtab-xs text-right'),
                    DTColumnBuilder.newColumn('usOfficialSneakerSales30Days', $translate('CMS_TXT_US_OFFICIAL_SNEAKER_SALES_30_DAYS')).withClass('wtab-xs text-right'),
                    DTColumnBuilder.newColumn('usOfficialSneakerSales30DaysPercent', $translate('CMS_TXT_US_OFFICIAL_SNEAKER_SALES_30_DAYS_PERCENT')).withClass('wtab-xs text-right'),
                    DTColumnBuilder.newColumn('usOfficialSneakerSalesInThisYear', $translate('CMS_TXT_US_OFFICIAL_SNEAKER_SALES_THIS_YEAR')).withClass('wtab-xs text-right'),
                    DTColumnBuilder.newColumn('usOfficialSneakerSalesInThisYearPercent', $translate('CMS_TXT_US_OFFICIAL_SNEAKER_SALES_THIS_YEAR_PERCENT')).withClass('wtab-xs text-right'),
                    DTColumnBuilder.newColumn('usOfficialSneakerWSSales7Days', $translate('CMS_TXT_US_OFFICIAL_SNEAKER_WS_SALES_7_DAYS')).withClass('wtab-xs text-right'),
                    DTColumnBuilder.newColumn('usOfficialSneakerWSSales7DaysPercent', $translate('CMS_TXT_US_OFFICIAL_SNEAKER_WS_SALES_7_DAYS_PERCENT')).withClass('wtab-xs text-right'),
                    DTColumnBuilder.newColumn('usOfficialSneakerWSSales30Days', $translate('CMS_TXT_US_OFFICIAL_SNEAKER_WS_SALES_30_DAYS')).withClass('wtab-xs text-right'),
                    DTColumnBuilder.newColumn('usOfficialSneakerWSSales30DaysPercent', $translate('CMS_TXT_US_OFFICIAL_SNEAKER_WS_SALES_30_DAYS_PERCENT')).withClass('wtab-xs text-right'),
                    DTColumnBuilder.newColumn('usOfficialSneakerWSSalesInThisYear', $translate('CMS_TXT_US_OFFICIAL_SNEAKER_WS_SALES_THIS_YEAR')).withClass('wtab-xs text-right'),
                    DTColumnBuilder.newColumn('usOfficialSneakerWSSalesInThisYearPercent', $translate('CMS_TXT_US_OFFICIAL_SNEAKER_WS_SALES_THIS_YEAR_PERCENT')).withClass('wtab-xs text-right'),
                    DTColumnBuilder.newColumn('usOfficialSneakerMobileSales7Days', $translate('CMS_TXT_US_OFFICIAL_SNEAKER_MOBILE_SALES_7_DAYS')).withClass('wtab-xs text-right'),
                    DTColumnBuilder.newColumn('usOfficialSneakerMobileSales7DaysPercent', $translate('CMS_TXT_US_OFFICIAL_SNEAKER_MOBILE_SALES_7_DAYS_PERCENT')).withClass('wtab-xs text-right'),
                    DTColumnBuilder.newColumn('usOfficialSneakerMobileSales30Days', $translate('CMS_TXT_US_OFFICIAL_SNEAKER_MOBILE_SALES_30_DAYS')).withClass('wtab-xs text-right'),
                    DTColumnBuilder.newColumn('usOfficialSneakerMobileSales30DaysPercent', $translate('CMS_TXT_US_OFFICIAL_SNEAKER_MOBILE_SALES_30_DAYS_PERCENT')).withClass('wtab-xs text-right'),
                    DTColumnBuilder.newColumn('usOfficialSneakerMobileSalesInThisYear', $translate('CMS_TXT_US_OFFICIAL_SNEAKER_MOBILE_SALES_THIS_YEAR')).withClass('wtab-xs text-right'),
                    DTColumnBuilder.newColumn('usOfficialSneakerMobileSalesInThisYearPercent', $translate('CMS_TXT_US_OFFICIAL_SNEAKER_MOBILE_SALES_THIS_YEAR_PERCENT')).withClass('wtab-xs text-right'),
                    DTColumnBuilder.newColumn('usAmazonPrice', $translate('CMS_TXT_US_AMAZON_PRICE')).withClass('wtab-xs text-right'),
                    DTColumnBuilder.newColumn('usAmazonFreeShippingType', $translate('CMS_TXT_US_AMAZON_SHIPPING_TYPE')).withClass('wtab-xs '),
                    DTColumnBuilder.newColumn('', $translate('CMS_TXT_US_AMAZON_IS_ON_SALE')).withClass('wtab-xs text-center').renderWith(function (val, type, row, cell) {
                        return ('<input type="checkbox" '+ (row.usAmazonIsOnSale ? 'checked ' : '') + ' >');
                    }),
                    DTColumnBuilder.newColumn('', $translate('CMS_TXT_US_AMAZON_IS_APPROVED')).withClass('wtab-xs text-center').renderWith(function (val, type, row, cell) {
                        return ('<input type="checkbox" '+ (row.usAmazonIsApproved ? 'checked ' : '') + ' >');
                    }),
                    DTColumnBuilder.newColumn('usAmazonPrePublishDateTime', $translate('CMS_TXT_US_AMAZON_PRE_PUBLISH_DATE_TIME')).withClass('wtab-xs '),
                    DTColumnBuilder.newColumn('usAmazonSales7Days', $translate('CMS_TXT_US_AMAZON_SALES_7_DAYS')).withClass('wtab-xs text-right'),
                    DTColumnBuilder.newColumn('usAmazonSales7DaysPercent', $translate('CMS_TXT_US_AMAZON_SALES_7_DAYS_PERCENT')).withClass('wtab-xs text-right'),
                    DTColumnBuilder.newColumn('usAmazonSales30Days', $translate('CMS_TXT_US_AMAZON_SALES_30_DAYS')).withClass('wtab-xs text-right'),
                    DTColumnBuilder.newColumn('usAmazonSales30DaysPercent', $translate('CMS_TXT_US_AMAZON_SALES_30_DAYS_PERCENT')).withClass('wtab-xs text-right'),
                    DTColumnBuilder.newColumn('usAmazonSalesInThisYear', $translate('CMS_TXT_US_AMAZON_SALES_THIS_YEAR')).withClass('wtab-xs text-right'),
                    DTColumnBuilder.newColumn('usAmazonSalesInThisYearPercent', $translate('CMS_TXT_US_AMAZON_SALES_THIS_YEAR_PERCENT')).withClass('wtab-xs text-right'),
                    DTColumnBuilder.newColumn('cnPrice', $translate('CMS_TXT_CN_PRICE')).withClass('wtab-xs text-right'),
                    DTColumnBuilder.newColumn('cnPriceRmb', $translate('CMS_TXT_CN_PRICE_RMB')).withClass('wtab-xs text-right'),
                    DTColumnBuilder.newColumn('cnPriceFinalRmb', $translate('CMS_TXT_CN_PRICE_FINAL_RMB')).withClass('wtab-xs text-right'),
                    DTColumnBuilder.newColumn('cnFreeShippingType', $translate('CMS_TXT_CN_SHIPPING_TYPE')).withClass('wtab-xs '),
                    DTColumnBuilder.newColumn('', $translate('CMS_TXT_CN_IS_ON_SALE')).withClass('wtab-xs text-center').renderWith(function (val, type, row, cell) {
                        return ('<input type="checkbox" '+ (row.cnIsOnSale ? 'checked ' : '') + ' >');
                    }),
                    DTColumnBuilder.newColumn('', $translate('CMS_TXT_CN_IS_APPROVED')).withClass('wtab-xs text-center').renderWith(function (val, type, row, cell) {
                        return ('<input type="checkbox" '+ (row.cnIsApproved ? 'checked ' : '') + ' >');
                    }),
                    DTColumnBuilder.newColumn('cnPrePublishDateTime', $translate('CMS_TXT_CN_PRE_PUBLISH_DATE_TIME')).withClass('wtab-sm'),
                    DTColumnBuilder.newColumn('cnSales7Days', $translate('CMS_TXT_CN_SALES_7_DAYS')).withClass('wtab-xs text-right'),
                    DTColumnBuilder.newColumn('cnSales7DaysPercent', $translate('CMS_TXT_CN_SALES_7_DAYS_PERCENT')).withClass('wtab-xs text-right'),
                    DTColumnBuilder.newColumn('cnSales30Days', $translate('CMS_TXT_CN_SALES_30_DAYS')).withClass('wtab-xs text-right'),
                    DTColumnBuilder.newColumn('cnSales30DaysPercent', $translate('CMS_TXT_CN_SALES_30_DAYS_PERCENT')).withClass('wtab-xs text-right'),
                    DTColumnBuilder.newColumn('cnSalesInThisYear', $translate('CMS_TXT_CN_SALES_THIS_YEAR')).withClass('wtab-xs text-right'),
                    DTColumnBuilder.newColumn('cnSalesInThisYearPercent', $translate('CMS_TXT_CN_SALES_THIS_YEAR_PERCENT')).withClass('wtab-xs text-right')
                ];
                $scope.dtUsProductList.columns = _.union($scope.dtUsProductList.columns, cmsCommonService.doGetProductListByUserConfig('us', usProductColumns));
            }

            /**
             * 取得CN Category List信息.
             */
            function doGetCNProductList () {
                var titleHtml = '<input ng-controller="selectController" ng-model="cnProductInfo.selectAllFlag" type="checkbox" ng-click="selectAll(cnProductInfo)">';
                $scope.dtCnProductList = {
                    options: DTOptionsBuilder.newOptions()
                        .withOption('processing', true)
                        .withOption('serverSide', true)
                        .withOption('scrollY', '400px')
                        .withOption('scrollX', '100%')
                        .withOption('scrollCollapse', true)
                        .withOption('ajax', $scope.doGetCNProductList)
                        .withOption('createdRow',  function(row, data, dataIndex) {
                            $compile(angular.element(row).contents())($scope);
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
                            return '<input ng-controller="selectController" type="checkbox" ng-model="cnProductInfo.selectOneFlagList['+productId+']" ng-click="selectOne(' + productId + ', cnProductInfo)">';
                        })
                    ],
                    dtInstance: null
                };

                var cnProductColumns = [
                    DTColumnBuilder.newColumn('', $translate('CMS_TXT_CODE')).withClass('wtab-xs text-center').renderWith(function (val, type, row, cell) {
                        return ('<img class="prodImg" src="' + $rootScope.cmsMaster.imageUrl + row.productImgUrl + '"><br><a ng-controller="navigationController" href="" class="btn-main" ng-href="{{goProductPage(' + $scope.categoryId + ',' + row.modelId + ',' + row.productId + ')}}">' + row.code + '</a>');
                    }),
                    DTColumnBuilder.newColumn('cnName', $translate('CMS_TXT_NAME')).withClass('wtab-xsm'),
                ];
                $scope.dtCnProductList.columns = _.union($scope.dtCnProductList.columns, cmsCommonService.doGetProductListByUserConfig('cn', cnProductColumns));
            }

            /**
             * get the category and model navigation info.
             */
            function doGetNavigationByCategoryId() {
                cmsCommonService.doGetNavigationByCategoryModelId( "1", $scope.categoryId, null)
                    .then(function (data) {
                        $scope.navigationCategoryList = data;
                    })
            }
        }]);



});
