/**
 * @Name:    model.ctl.js
 * @Date:    2015/7/17
 *
 * @User:    Edward
 * @Version: 1.0.0
 */

define (function (require) {
    var cmsApp = require ('modules/cms/cms.module');
    require ('modules/cms/edit/edit.service');
    require ('modules/cms/common/common.service');
    require ('modules/cms/service/mainCategory.service');
    require ('modules/cms/popup/setSizeChart/setSizeChart.ctl');
    require ('modules/cms/popup/setProductColumns/popSetProductColumns.ctl');
    require ('modules/cms/popup/addToPromotion/popAddToPromotion.ctl');
    require ('modules/cms/popup/moveToModel/popMoveToModel.ctl');
    require ('modules/cms/popup/setCnProductProperty/popSetCnProductProperty.ctl');
    require ('modules/cms/popup/setCnProductShare/popSetCnProductShare.ctl');
    require ('modules/cms/master/masterPropValueSetting/masterPropValueSetting.ctl');

    cmsApp.controller ('editModelController', ['$scope', '$rootScope', '$q', '$location', '$routeParams', 'cmsCommonService', 'editModelService', 'mainCategoryService'
        , 'mainCategoryLevel', 'cmsRoute', 'systemCountry', 'notify', '$compile', 'DTOptionsBuilder', 'DTColumnBuilder', '$modal', 'cmsPopupPages', '$translate', 'vConfirm',
        function ($scope, $rootScope, $q, $location, $routeParams, cmsCommonService, modelService, mainCategoryService
            , mainCategoryLevel, cmsRoute, systemCountry, notify, $compile, DTOptionsBuilder, DTColumnBuilder, $modal, cmsPopupPages, $translate, vConfirm) {

            var _ = require ('underscore');
            var commonUtil = require ('components/util/commonUtil');
            $scope.modelName = "";

            /**
             * 初始化操作.
             */
            $scope.initialize = function () {

                if (!isNaN(parseInt($routeParams.modelId))) {
                    $scope.currentModelId = $routeParams.modelId;
                    $scope.categoryId = isNaN(parseInt($routeParams.categoryId)) ? 2: parseInt($routeParams.categoryId);

                    doGetNavigationByCategoryId();
                    doGetUSModelInfo ();
                    doGetCNModelInfo ();
                    doGetModelCNPriceSettingInfo ();

                    // TODO 应该从用户设定中取得值
                    $scope.doShowCategoryList('');
                    $scope.doShowProductList('');
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
                        doGetUSCategoryList ();
                        $scope.showCategoryListFlag = systemCountry.US;
                        break;
                    case systemCountry.CN:
                        doGetCNCategoryList ();
                        $scope.showCategoryListFlag = systemCountry.CN;
                        break;
                    default :
                        doGetUSCategoryList ();
                        $scope.showCategoryListFlag = systemCountry.US;
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

            /** US Model信息操作 Start **/
            /**
             * 执行Effective处理.
             */
            $scope.doUpdateEffective = function () {
                vConfirm('CORE_TXT_MESSAGE_CHANGE_TO_EFFECTIVE','').result.then(function() {
                    var isEffective = $scope.usModelInfo.isEffective;
                    $scope.undoUpdateUSModelInfo();
                    $scope.usModelInfo.isEffective = isEffective;
                    $scope.doUpdateUSModelInfo();
                }, function() {
                    $scope.usModelInfo.isEffective = !$scope.usModelInfo.isEffective;
                })
            };

            /**
             * 取消US Model信息的变更.
             */
            $scope.undoUpdateUSModelInfo = function () {
                $scope.usModelInfo = angular.copy ($scope.oldUsModelInfo);
                $scope.usModelIsChanged = false;
            };

            /**
             * 更新US Model信息.
             */
            $scope.doUpdateUSModelInfo = function () {

                //if ($scope.formUSModelInfo.$valid) {

                    modelService.doUpdateUSModelInfo ($scope.currentModelId, $scope.usModelInfo)
                        .then (function () {
                        notify.success("CMS_TXT_MSG_UPDATE_SUCCESS");
                        doGetUSModelInfo ();
                    }, function () {
                        notify.danger("CMS_TXT_MSG_UPDATE_FAILED");
                        doGetUSModelInfo ();
                    });
                //} else {
                //    //TODO 如果validation检测不通过则显示错误信息.
                //}
            };

            /**
             * 清除US Model's productTypeId
             */
            $scope.clearUSProductTypeId = function () {
                $scope.usModelInfo.productTypeId = "";
                $scope.usModelChanged ();
            };

            /**
             * 清除US Model's brandId
             */
            $scope.clearUSBrandId = function () {
                $scope.usModelInfo.brandId = "";
                $scope.usModelChanged ();
            };

            /**
             * 清除US Model's sizeTypeId
             */
            $scope.clearUSSizeTypeId = function () {
                $scope.usModelInfo.sizeTypeId = "";
                $scope.usModelChanged ();
            };

            /**
             * 清除US Model's amazonBrowseCategoryId
             */
            $scope.clearAmazonBrowseCategoryId = function () {
                $scope.usModelInfo.amazonBrowseCategoryId = "";
                $scope.usModelChanged ();
            };

            /**
             * 清除US Model's googleCategoryId
             */
            $scope.clearGoogleCategoryId = function () {
                $scope.usModelInfo.googleCategoryId = "";
                $scope.usModelChanged ();
            };

            /**
             * 清除US Model's priceGrabberCategoryId
             */
            $scope.clearPriceGrabberCategoryId = function () {
                $scope.usModelInfo.priceGrabberCategoryId = "";
                $scope.usModelChanged ();
            };

            /**
             * 清除US Model's mainCategoryId
             */
            $scope.clearUSMainCategoryId = function () {
                $scope.usModelInfo.mainCategoryId = "";
                $scope.usModelChanged ();
            };

            /**
             * 当US Model's IsUnisex发生变化，如果变成No，则修改sizeOffset为空.
             */
            $scope.usModelIsUnisexChanged = function () {
                if (!$scope.usModelInfo.isUnisex) {
                    $scope.usModelInfo.sizeOffset = "";
                }
                $scope.usModelChanged ();
            };

            /**
             * 检查US Model Info是否发生变化
             */
            $scope.usModelChanged = function () {
                $scope.usModelIsChanged = !_.isEqual ($scope.usModelInfo, $scope.oldUsModelInfo);
            };
            /** US Model信息操作 End **/

            /** CN Model信息操作 Start **/
            /**
             * 取消CN Model信息的变更.
             */
            $scope.undoUpdateCNModelInfo = function () {
                $scope.cnBaseModelInfo = angular.copy ($scope.oldCnBaseModelInfo);
                $scope.cnBaseModelIsChanged = false;
            };

            /**
             * 更新CN Model信息.
             */
            $scope.doUpdateCNModelInfo = function () {

                //if ($scope.formCNModelInfo.$valid) {

                    modelService.doUpdateCNModelInfo ($scope.currentModelId, $scope.cnBaseModelInfo)
                        .then (function () {
                        notify.success("CMS_TXT_MSG_UPDATE_SUCCESS");
                        doGetCNModelInfo ();
                    }, function () {
                        notify.danger("CMS_TXT_MSG_UPDATE_FAILED");
                        doGetCNModelInfo ();
                    });
                //} else {
                //    //TODO 如果validation检测不通过则显示错误信息.
                //}
            };

            /**
             * 清除CN Model's cnSizeTypeId
             */
            $scope.clearCNSizeTypeId = function () {
                $scope.cnBaseModelInfo.cnSizeTypeId = "";
                $scope.cnModelChanged ();
            };

            /**
             * 清除CN Model's hsCodeId
             */
            $scope.clearHsCodeId = function () {
                $scope.cnBaseModelInfo.hsCodeId = "";
                $scope.cnModelChanged ();
            };

            /**
             * 清除CN Model's hsCodePuId
             */
            $scope.clearHsCodePuId = function () {
                $scope.cnBaseModelInfo.hsCodePuId = "";
                $scope.cnModelChanged ();
            };

            /**
             * 清除CN Model's mainCategoryId
             */
            $scope.clearCNMainCategoryId = function () {
                $scope.cnBaseModelInfo.mainCategoryId = "";
                $scope.cnModelChanged ();
            };

            /**
             * 清除CN Model's sizeChartId
             */
            $scope.clearSizeChartId = function () {
                $scope.cnBaseModelInfo.sizeChartId = "";
                $scope.cnModelChanged ();
            };

            /**
             * 检查CN Model Info是否发生变化.
             */
            $scope.cnModelChanged = function () {
                $scope.cnBaseModelIsChanged = !_.isEqual ($scope.cnBaseModelInfo, $scope.oldCnBaseModelInfo);
            };

            /**
             * 更新TM Model的信息变化.
             */
            $scope.doUpdateTMModelInfo = function () {

                //if ($scope.formTMModelInfo.$valid) {

                    modelService.doUpdateCNModelTmallInfo ($scope.currentModelId, $scope.tmModelInfo)
                        .then (function () {
                        notify.success("CMS_TXT_MSG_UPDATE_SUCCESS");
                        doGetCNModelInfo ();
                    }, function () {
                        notify.danger("CMS_TXT_MSG_UPDATE_FAILED");
                        doGetCNModelInfo ();
                    });
                //} else {
                //    //TODO 如果validation检测不通过则显示错误信息.
                //}
            };

            /**
             * 取消TM Model信息的变化.
             */
            $scope.undoUpdateTMModelInfo = function () {
                $scope.tmModelInfo = angular.copy ($scope.oldTmModelInfo);
                $scope.cnTmModelIsChanged = false;
            };

            /**
             * 检查TM Model Info是否发生变化.
             */
            $scope.tmModelChanged = function () {
                $scope.cnTmModelIsChanged = !_.isEqual ($scope.tmModelInfo, $scope.oldTmModelInfo);
            };

            /**
             * 更新JD Model的信息变化.
             */
            $scope.doUpdateJDModelInfo = function () {

                //if ($scope.formJDModelInfo.$valid) {

                    modelService.doUpdateCNModelJingDongInfo ($scope.currentModelId, $scope.jdModelInfo)
                        .then (function () {
                        notify.success("CMS_TXT_MSG_UPDATE_SUCCESS");
                        doGetCNModelInfo ();
                    }, function () {
                        notify.danger("CMS_TXT_MSG_UPDATE_FAILED");
                        doGetCNModelInfo ();
                    });
                //} else {
                //    //TODO 如果validation检测不通过则显示错误信息.
                //}
            };

            /**
             * 取消JD Model信息的变化.
             */
            $scope.undoUpdateJDModelInfo = function () {
                $scope.jdModelInfo = angular.copy ($scope.oldJdModelInfo);
                $scope.cnJdModelIsChanged = false;
            };

            /**
             * 检查JD Model Info是否发生变化.
             */
            $scope.jdModelChanged = function () {
                $scope.cnJdModelIsChanged = !_.isEqual ($scope.jdModelInfo, $scope.oldJdModelInfo);
            };

            // TODO
            $scope.goDisplayImagePage = function () {

            };

            /**
             * 跳转到MainCategory设定页面.
             */
            $scope.goMainCategoryPage = function () {
                var data = {};
                data.channelId = $scope.cnBaseModelInfo.channelId;
                data.mainCategoryId = $scope.cnBaseModelInfo.mainCategoryId;
                data.parentLevel = $scope.cnBaseModelInfo.mainParentCategoryTypeId;
                data.parentId = $scope.cnBaseModelInfo.mainParentCategoryId;
                data.currentLevel = mainCategoryLevel.model;
                data.currentId = $scope.cnBaseModelInfo.modelId;
                mainCategoryService.setMainCategoryParam(data);
                
                mainCategoryService.setMainCategoryReturnUrl($location.path());

                $location.path(cmsRoute.cms_masterPropValue_setting.hash);
            };

            // TODO
            $scope.goSizeChartPage = function () {

            };
            /** CN Model信息操作 End **/

            /** CN PriceSetting信息操作 End **/

            /**
             * 检查CN Model PriceSetting信息是否发生变化.
             /**
             * 更新CN Model PriceSetting信息.
             */
            $scope.doUpdateModelCNPriceSettingInfo = function () {

                //if ($scope.formCNPriceSettingInfo.$valid) {

                    modelService.doUpdateModelCNPriceSettingInfo ($scope.currentModelId, $scope.cnPriceSettingInfo)
                        .then (function () {
                        notify.success("CMS_TXT_MSG_UPDATE_SUCCESS");
                        doGetModelCNPriceSettingInfo ();
                        doGetUSProductList ();
                        doGetCNProductList ();
                    }, function () {
                        notify.danger("CMS_TXT_MSG_UPDATE_FAILED");
                        doGetModelCNPriceSettingInfo ();
                        doGetUSProductList ();
                        doGetCNProductList ();
                    });
                //} else {
                //    //TODO 如果validation检测不通过则显示错误信息.
                //}
            };

            /**
             * 取消CN Model PriceSetting信息的变更.
             */
            $scope.undoUpdateModelCNPriceSettingInfo = function () {
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
                $scope.cnPriceSettingInfo.overHeard1 = '';
                $scope.cnPriceSettingChanged ();
            };

            /**
             * 清空overHeard2选择内容.
             */
            $scope.clearOverHeard2 = function () {
                $scope.cnPriceSettingInfo.overHeard2 = '';
                $scope.cnPriceSettingChanged ();
            };
            /** CN PriceSetting信息操作 End **/

            /**
             * 设置US primary category信息.
             * @param modelId
             * @param categoryId
             */
            $scope.doSetUSPrimaryCategory = function ( categoryId) {

                modelService.doUpdateModelPrimaryCategory ($scope.currentModelId, "1", categoryId)
                    .then (function () {
                    notify.success("CMS_TXT_MSG_UPDATE_SUCCESS");
                    doGetUSCategoryList ();
                }, function () {
                    notify.danger("CMS_TXT_MSG_UPDATE_FAILED");
                    doGetUSCategoryList ();
                });
            };

            /**
             * 取消category和model的关联关系.
             * @param modelId
             * @param categoryId
             */
            $scope.doDecouplingCategoryModel = function (categoryId) {

                modelService.doUpdateRemoveCategoryModel ($scope.currentModelId, categoryId)
                    .then (function () {
                    notify.success("CMS_TXT_MSG_UPDATE_SUCCESS");
                    doGetUSCategoryList ();
                    doGetCNCategoryList ();
                }, function () {
                    notify.danger("CMS_TXT_MSG_UPDATE_FAILED");
                    doGetUSCategoryList ();
                    doGetCNCategoryList ();
                });
            };

            /**
             * 设置CN primary category信息.
             * @param modelId
             * @param categoryId
             */
            $scope.doSetCNPrimaryCategory = function (categoryId) {

                modelService.doUpdateModelPrimaryCategory ($scope.currentModelId, "2", categoryId)
                    .then (function () {
                    notify.success("CMS_TXT_MSG_UPDATE_SUCCESS");
                    doGetCNCategoryList ();
                }, function () {
                    notify.danger("CMS_TXT_MSG_UPDATE_FAILED");
                    doGetCNCategoryList ();
                });
            };

            /** 更新datatable的处理 Start **/

            /**
             * 取得US Sub Category List信息.
             * @param data
             * @param draw
             * @returns {*}
             */
            $scope.doGetUSProductList = function (data, draw) {
                if (data.draw === 1 || !$scope.usProductInfo.currentPageIdList) {
                    draw({data: []});
                }
                return modelService.doGetUSProductList (data, $scope.currentModelId)
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
                if (data.draw === 1 || !$scope.cnProductInfo.currentPageIdList) {
                    draw({data: []});
                }
                return modelService.doGetCNProductList (data, $scope.currentModelId)
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

            /** 更新datatable的处理 End **/

            /** popup需要回调函数的处理 Start **/
            $scope.reFreshFunc = {};

            /**
             * 刷新cnProduct信息.
             */
            $scope.reFreshFunc.doRefreshModelInfo = function () {
                doGetCNModelInfo ();
            };

            /**
             * 刷新usProduct信息.
             */
            $scope.reFreshFunc.doRefreshUsProductInfo = function () {
                cmsCommonService.doGetProductListByUserConfig('us', $scope.dtUsProductList.dtInstance.DataTable);
            };

            /**
             * 刷新usProduct信息.
             */
            $scope.reFreshFunc.doRefreshCnProductInfo = function () {
                cmsCommonService.doGetProductListByUserConfig('cn', $scope.dtCnProductList.dtInstance.DataTable);
            };
            /** popup需要回调函数的处理 End **/
            /**
             * 取得US Model信息.
             */
            function doGetUSModelInfo () {
                modelService.doGetUSModelInfo ($scope.currentModelId)
                    .then (function (data) {
                    $scope.usModelInfo = data;
                    $scope.modelName = $scope.usModelInfo.model + ': ' + $scope.usModelInfo.name;
                    $scope.oldUsModelInfo = angular.copy ($scope.usModelInfo);
                    $scope.usModelIsChanged = false;
                });
            }

            /**
             * 取得CN Model信息.
             */
            function doGetCNModelInfo () {
                modelService.doGetCNModelInfo ($scope.currentModelId)
                    .then (function (data) {
                    $scope.cnBaseModelInfo = data.cnBaseModelInfo;
                    $scope.oldCnBaseModelInfo = angular.copy ($scope.cnBaseModelInfo);
                    $scope.cnBaseModelIsChanged = false;

                    $scope.tmModelInfo = data.tmModelInfo;
                    $scope.oldTmModelInfo = angular.copy ($scope.tmModelInfo);
                    $scope.cnTmModelIsChanged = false;

                    $scope.jdModelInfo = data.jdModelInfo;
                    $scope.oldJdModelInfo = angular.copy ($scope.jdModelInfo);
                    $scope.cnJdModelIsChanged = false;


                    if ($scope.cnBaseModelInfo.displayImages !== null) {
                        $scope.displayImages = $scope.cnBaseModelInfo.displayImages.split(",");
                    }
                });
            }

            /**
             * 取得CN Model PriceSetting信息.
             */
            function doGetModelCNPriceSettingInfo () {
                modelService.doGetModelCNPriceSettingInfo ($scope.currentModelId)
                    .then (function (data) {
                    $scope.cnPriceSettingInfo = data;
                    $scope.oldCnPriceSettingInfo = angular.copy ($scope.cnPriceSettingInfo);
                    $scope.cnPriceSettingIsChanged = false;
                });
            }

            /**
             * 取得US Category List信息.
             */
            function doGetUSCategoryList () {
                modelService.doGetUSCategoryList ($scope.currentModelId)
                    .then (function (data) {
                    $scope.usCategoryList = data;
                });
            }

            /**
             * 取得CN Category List信息.
             */
            function doGetCNCategoryList () {
                modelService.doGetCNCategoryList ($scope.currentModelId)
                    .then (function (data) {
                    $scope.cnCategoryList = data;
                });
            }

            /**
             * 取得US Product List信息.
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
                            var rowScope = $scope.$new();
                            rowScope.$row = data;
                            $compile(angular.element(row).contents())(rowScope);
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
                    columns: [
                        DTColumnBuilder.newColumn(null).withTitle(titleHtml).notSortable().withClass('wtab-check').renderWith(function (val, type, row, cell) {
                            var productId = parseInt(row.productId);
                            if (!$scope.usProductInfo.selectOneFlagList.hasOwnProperty(productId)) {
                                $scope.usProductInfo.selectOneFlagList[productId] = false;
                            }
                            return '<input ng-controller="selectController" type="checkbox" ng-model="usProductInfo.selectOneFlagList[$row.productId]" ng-click="selectOne($row.productId, usProductInfo)">';
                        }).notSortable(),
                        DTColumnBuilder.newColumn('', $translate('CMS_TXT_CODE')).withClass('wtab-xs text-center').renderWith(function () {
                            return ('<img class="prodImg" src="{{$root.cmsMaster.imageUrl}}{{$row.productImgUrl}}"><br><a ng-controller="navigationController" href="" class="btn-main" ng-href="{{goProductPage(categoryId,$row.modelId ,$row.productId)}}">{{$row.code}}</a>');
                        }),
                        DTColumnBuilder.newColumn('name', $translate('CMS_TXT_NAME')).withClass('wtab-xsm').notSortable(),
                        DTColumnBuilder.newColumn('displayOrder', $translate('CMS_TXT_DISPLAY_ORDER')).withClass('wtab-xs text-center'),
                        DTColumnBuilder.newColumn('', $translate('CMS_TXT_IS_HIGH_LIGHT_PRODUCT')).withClass('wtab-xs text-center').renderWith(function () {
                            return ('<input type="checkbox" ng-model="$row.isPrimaryProduct" >');
                        }),
                        DTColumnBuilder.newColumn('productType', $translate('CMS_TXT_PRODUCT_TYPE')).withClass('wtab-xs').notSortable(),
                        DTColumnBuilder.newColumn('brand', $translate('CMS_TXT_BRAND')).withClass('wtab-xs').notSortable(),
                        DTColumnBuilder.newColumn('sizeType', $translate('CMS_TXT_SIZE_TYPE')).withClass('wtab-xs').notSortable(),
                        DTColumnBuilder.newColumn('', $translate('CMS_TXT_IS_NEW_ARRIVAL')).withClass('wtab-xs text-center').renderWith(function () {
                            return ('<input type="checkbox" ng-model="$row.isNewArrival" >');
                        }),
                        DTColumnBuilder.newColumn('', $translate('CMS_TXT_IS_REWARD_ELIGIBLE')).withClass('wtab-xs text-center').renderWith(function () {
                            return ('<input type="checkbox" ng-model="$row.isRewardEligible">');
                        }),
                        //DTColumnBuilder.newColumn('', $translate('CMS_TXT_IS_DISCOUNT_ELIGIBLE')).withClass('wtab-xs text-center').renderWith(function () {
                        //    return ('<input type="checkbox" ng-model="$row.isDiscountEligible">');
                        //}),
                        DTColumnBuilder.newColumn('', $translate('CMS_TXT_IS_PHONE_ORDER_ONLY')).withClass('wtab-xs text-center').renderWith(function () {
                            return ('<input type="checkbox" ng-model="$row.isPhoneOrderOnly" >');
                        }),
                        DTColumnBuilder.newColumn('', $translate('CMS_TXT_IS_APPROVED_DESCRIPTION')).withClass('wtab-xs text-center').renderWith(function () {
                            return ('<input type="checkbox" ng-model="$row.isApprovedDescription" >');
                        }),
                        DTColumnBuilder.newColumn('colorMap', $translate('CMS_TXT_COLOR_MAP')).withClass('wtab-xs').notSortable(),
                        DTColumnBuilder.newColumn('color', $translate('CMS_TXT_COLOR')).withClass('wtab-sm').notSortable(),
                        DTColumnBuilder.newColumn('madeInCountry', $translate('CMS_TXT_MADE_IN_COUNTRY')).withClass('wtab-xs').notSortable(),
                        DTColumnBuilder.newColumn('materialFabric', $translate('CMS_TXT_MATERIAL_FABRIC')).withClass('wtab-sm').notSortable(),
                        DTColumnBuilder.newColumn('usAbstract', $translate('CMS_TXT_ABSTRACT')).withClass('wtab-sm').notSortable(),
                        DTColumnBuilder.newColumn('accessory', $translate('CMS_TXT_ACCESSORY')).withClass('wtab-sm').notSortable(),
                        DTColumnBuilder.newColumn('', $translate('CMS_TXT_SHORT_DESCRIPTION')).withClass('wtab-xsm').renderWith(function () {
                            return ('<span title="{{$row.shortDescription}}" ng-bind="$row.shortDescription.substring(0,100)"></span>');
                        }).notSortable(),
                        DTColumnBuilder.newColumn('', $translate('CMS_TXT_LONG_DESCRIPTION')).withClass('wtab-xsm').renderWith(function () {
                            return ('<span title="{{$row.longDescription}}" ng-bind="$row.longDescription.substring(0,100)"></span>');
                        }).notSortable(),
                        DTColumnBuilder.newColumn('orderLimitCount', $translate('CMS_TXT_ORDER_LIMIT_COUNT')).withClass('wtab-xs text-center').notSortable(),
                        DTColumnBuilder.newColumn('promotionTag', $translate('CMS_TXT_PROMOTION_TAG')).withClass('wtab-xs').notSortable(),
                        DTColumnBuilder.newColumn('quantity', $translate('CMS_TXT_QUANTITY')).withClass('wtab-xs text-center'),
                        DTColumnBuilder.newColumn('urlKey', $translate('CMS_TXT_URL_KEY')).withClass('wtab-xsm').notSortable(),
                        DTColumnBuilder.newColumn('created', $translate('CMS_TXT_CREATED_ON')).withClass('wtab-sm'),
                        DTColumnBuilder.newColumn('modified', $translate('CMS_TXT_LAST_UPDATED_ON')).withClass('wtab-sm'),
                        DTColumnBuilder.newColumn('msrp', $translate('CMS_TXT_MSRP')).withClass('wtab-xs text-right'),
                        DTColumnBuilder.newColumn('usOfficialPrice', $translate('CMS_TXT_US_OFFICIAL_PRICE')).withClass('wtab-xs text-right'),
                        //DTColumnBuilder.newColumn('usOfficialFreeShippingType', $translate('CMS_TXT_US_OFFICIAL_SHIPPING_TYPE')).withClass('wtab-xs '),
                        DTColumnBuilder.newColumn('', $translate('CMS_TXT_US_OFFICIAL_IS_ON_SALE')).withClass('wtab-xs text-center').renderWith(function () {
                            return ('<input type="checkbox" ng-model="$row.usOfficialIsOnSale" >');
                        }),
                        DTColumnBuilder.newColumn('', $translate('CMS_TXT_US_OFFICIAL_IS_APPROVED')).withClass('wtab-xs text-center').renderWith(function () {
                            return ('<input type="checkbox" ng-model="$row.usOfficialIsApproved" >');
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
                        //DTColumnBuilder.newColumn('usAmazonFreeShippingType', $translate('CMS_TXT_US_AMAZON_SHIPPING_TYPE')).withClass('wtab-xs '),
                        DTColumnBuilder.newColumn('', $translate('CMS_TXT_US_AMAZON_IS_ON_SALE')).withClass('wtab-xs text-center').renderWith(function () {
                            return ('<input type="checkbox" ng-model="$row.usAmazonIsOnSale" >');
                        }),
                        DTColumnBuilder.newColumn('', $translate('CMS_TXT_US_AMAZON_IS_APPROVED')).withClass('wtab-xs text-center').renderWith(function () {
                            return ('<input type="checkbox" ng-model="$row.usAmazonIsApproved" >');
                        }),
                        DTColumnBuilder.newColumn('usAmazonPrePublishDateTime', $translate('CMS_TXT_US_AMAZON_PRE_PUBLISH_DATE_TIME')).withClass('wtab-xs ').notSortable(),
                        DTColumnBuilder.newColumn('usAmazonSales7Days', $translate('CMS_TXT_US_AMAZON_SALES_7_DAYS')).withClass('wtab-xs text-right'),
                        DTColumnBuilder.newColumn('usAmazonSales7DaysPercent', $translate('CMS_TXT_US_AMAZON_SALES_7_DAYS_PERCENT')).withClass('wtab-xs text-right'),
                        DTColumnBuilder.newColumn('usAmazonSales30Days', $translate('CMS_TXT_US_AMAZON_SALES_30_DAYS')).withClass('wtab-xs text-right'),
                        DTColumnBuilder.newColumn('usAmazonSales30DaysPercent', $translate('CMS_TXT_US_AMAZON_SALES_30_DAYS_PERCENT')).withClass('wtab-xs text-right'),
                        DTColumnBuilder.newColumn('usAmazonSalesInThisYear', $translate('CMS_TXT_US_AMAZON_SALES_THIS_YEAR')).withClass('wtab-xs text-right'),
                        DTColumnBuilder.newColumn('usAmazonSalesInThisYearPercent', $translate('CMS_TXT_US_AMAZON_SALES_THIS_YEAR_PERCENT')).withClass('wtab-xs text-right'),
                        DTColumnBuilder.newColumn('cnPrice', $translate('CMS_TXT_CN_PRICE')).withClass('wtab-xs text-right'),
                        DTColumnBuilder.newColumn('cnPriceRmb', $translate('CMS_TXT_CN_PRICE_RMB')).withClass('wtab-xs text-right'),
                        DTColumnBuilder.newColumn('cnPriceFinalRmb', $translate('CMS_TXT_CN_PRICE_FINAL_RMB')).withClass('wtab-xs text-right'),
                        //DTColumnBuilder.newColumn('cnFreeShippingType', $translate('CMS_TXT_CN_SHIPPING_TYPE')).withClass('wtab-xs '),
                        DTColumnBuilder.newColumn('', $translate('CMS_TXT_CN_IS_ON_SALE')).withClass('wtab-xs text-center').renderWith(function () {
                            return ('<input type="checkbox" ng-model="$row.cnIsOnSale" >');
                        }),
                        DTColumnBuilder.newColumn('', $translate('CMS_TXT_CN_IS_APPROVED')).withClass('wtab-xs text-center').renderWith(function () {
                            return ('<input type="checkbox" ng-model="$row.cnIsApproved" >');
                        }),
                        DTColumnBuilder.newColumn('cnPrePublishDateTime', $translate('CMS_TXT_CN_PRE_PUBLISH_DATE_TIME')).withClass('wtab-sm').notSortable(),
                        DTColumnBuilder.newColumn('cnSales7Days', $translate('CMS_TXT_CN_SALES_7_DAYS')).withClass('wtab-xs text-right'),
                        DTColumnBuilder.newColumn('cnSales7DaysPercent', $translate('CMS_TXT_CN_SALES_7_DAYS_PERCENT')).withClass('wtab-xs text-right'),
                        DTColumnBuilder.newColumn('cnSales30Days', $translate('CMS_TXT_CN_SALES_30_DAYS')).withClass('wtab-xs text-right'),
                        DTColumnBuilder.newColumn('cnSales30DaysPercent', $translate('CMS_TXT_CN_SALES_30_DAYS_PERCENT')).withClass('wtab-xs text-right'),
                        DTColumnBuilder.newColumn('cnSalesInThisYear', $translate('CMS_TXT_CN_SALES_THIS_YEAR')).withClass('wtab-xs text-right'),
                        DTColumnBuilder.newColumn('cnSalesInThisYearPercent', $translate('CMS_TXT_CN_SALES_THIS_YEAR_PERCENT')).withClass('wtab-xs text-right')
                    ],
                    dtInstance: null
                };

                // 设置列的显示/隐藏
                cmsCommonService.doGetProductColumnsAtFirst('us', $scope.dtUsProductList.columns);
            }

            /**
             * 取得CN Product List信息.
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
                            var rowScope = $scope.$new();
                            rowScope.$row = data;
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
                            return ('<img class="prodImg" ng-src="{{$root.cmsMaster.imageUrl}}{{$row.productImgUrl}}"><br><a ng-controller="navigationController" href="" class="btn-main" ng-href="{{goProductPage(categoryId,$row.modelId, $row.productId)}}">{{$row.code}}</a>');
                        }),
                        DTColumnBuilder.newColumn('cnName', $translate('CMS_TXT_NAME')).withClass('wtab-xsm').notSortable(),
                        DTColumnBuilder.newColumn('cnDisplayOrder', $translate('CMS_TXT_DISPLAY_ORDER')).withClass('wtab-xs text-center'),
                        DTColumnBuilder.newColumn('', $translate('CMS_TXT_IS_HIGH_LIGHT_PRODUCT')).withClass('wtab-xs text-center').renderWith(function () {
                            return ('<input type="checkbox" ng-model="$row.cnIsPrimaryProduct" >');
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
                        DTColumnBuilder.newColumn('tbNumIid', $translate('CMS_TXT_TB_NUMIID')).withClass('wtab-xs text-right').renderWith(function () {
                            return ('<a href="" class="btn-main" ng-href="{{$root.cmsMaster.tmallUrl}}{{$row.tbNumIid}}" ng-bind="$row.tbNumIid"></a>');
                        }).notSortable(),
                        DTColumnBuilder.newColumn('tbPublishFaildComment', $translate('CMS_TXT_TB_COMMENT')).withClass('wtab-xs text-right').notSortable(),
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
                        DTColumnBuilder.newColumn('tmNumIid', $translate('CMS_TXT_TM_NUMIID')).withClass('wtab-xs text-right').renderWith(function () {
                            return ('<a href="" class="btn-main" ng-href="{{$root.cmsMaster.tmallUrl}}{{$row.tmNumIid}}" ng-bind="$row.tmNumIid"></a>');
                        }).notSortable(),
                        DTColumnBuilder.newColumn('tmPublishFaildComment', $translate('CMS_TXT_TM_COMMENT')).withClass('wtab-xs text-right').notSortable(),
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
                        DTColumnBuilder.newColumn('tgNumIid', $translate('CMS_TXT_TG_NUMIID')).withClass('wtab-xs text-right').renderWith(function () {
                            return ('<a href="" class="btn-main" ng-href="{{$root.cmsMaster.tmallUrl}}{{$row.tgNumIid}}" ng-bind="$row.tgNumIid"></a>');
                        }).notSortable(),
                        DTColumnBuilder.newColumn('tgPublishFaildComment', $translate('CMS_TXT_TG_COMMENT')).withClass('wtab-xs text-right').notSortable(),
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
                        DTColumnBuilder.newColumn('jdNumIid', $translate('CMS_TXT_JD_NUMIID')).withClass('wtab-xs text-right').notSortable(),
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
                        DTColumnBuilder.newColumn('jgNumIid', $translate('CMS_TXT_JG_NUMIID')).withClass('wtab-xs text-right').notSortable(),
                        DTColumnBuilder.newColumn('jgPublishFaildComment', $translate('CMS_TXT_JG_COMMENT')).withClass('wtab-xs text-right').notSortable(),
                        DTColumnBuilder.newColumn('jgSales7Days', $translate('CMS_TXT_JG_SALES_7_DAYS')).withClass('wtab-xs text-right'),
                        DTColumnBuilder.newColumn('jgSales7DaysPercent', $translate('CMS_TXT_JG_SALES_7_DAYS_PERCENT')).withClass('wtab-xs text-right'),
                        DTColumnBuilder.newColumn('jgSales30Days', $translate('CMS_TXT_JG_SALES_30_DAYS')).withClass('wtab-xs text-right'),
                        DTColumnBuilder.newColumn('jgSales30DaysPercent', $translate('CMS_TXT_JG_SALES_30_DAYS_PERCENT')).withClass('wtab-xs text-right'),
                        DTColumnBuilder.newColumn('jgSalesInThisYear', $translate('CMS_TXT_JG_SALES_THIS_YEAR')).withClass('wtab-xs text-right'),
                        DTColumnBuilder.newColumn('jgSalesInThisYearPercent', $translate('CMS_TXT_JG_SALES_THIS_YEAR_PERCENT')).withClass('wtab-xs text-right')
                    ],
                    dtInstance: null
                };

                // 设置列的显示/隐藏
                cmsCommonService.doGetProductColumnsAtFirst('cn', $scope.dtCnProductList.columns);
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

        }])
});