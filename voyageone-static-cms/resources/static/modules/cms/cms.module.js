/**
 * @Name:    cms.module.js
 * @Date:    2015/6/16
 *
 * @User:    Edward
 * @Version: 1.0.0
 */

define (function (require) {

    require ('components/services/ajax.service');
    require ('components/services/cookie.service');
    require ('components/services/alert.service');
    require ('components/services/language.service');
    require ('components/services/message.service');
    require ('components/services/permission.service');
    require ('components/services/translate.service');

    var cmsApp = angular.module ('cmsModule', ['mainModule']);
    cmsApp.constant ('cmsAction', {

        /** cms 共通调用的url start **/
        //'oms_common_master_doGetMasterDataList': '/oms/common/master/doGetMasterDataList',
        //'oms_common_service_doGetSKUInfo': '/oms/common/service/doGetSKUInfo',
        'cms_common_navigation_doGetNavigationByCategoryId': '/cms/common/navigation/doGetNavigationByCategoryId',
        'cms_common_navigation_doGetNavigationByCategoryModelId': '/cms/common/navigation/doGetNavigationByCategoryModelId',
        /** edit common's url **/
        'cms_common_edit_doUpdateMainCategory': '/cms/common/edit/doUpdateMainCategory',
        /** edit category's url **/
        'cms_edit_category_doGetCategoryList': '/cms/edit/category/doGetCategoryList',
        'cms_edit_category_doGetUSCategoryInfo': '/cms/edit/category/doGetUSCategoryInfo',
        'cms_edit_category_doGetCNCategoryInfo': '/cms/edit/category/doGetCNCategoryInfo',
        'cms_edit_category_doGetCategoryCNPriceSettingInfo': '/cms/edit/category/doGetCategoryCNPriceSettingInfo',
        'cms_edit_category_doGetUSSubCategoryList': '/cms/edit/category/doGetUSSubCategoryList',
        'cms_edit_category_doGetCNSubCategoryList': '/cms/edit/category/doGetCNSubCategoryList',
        'cms_edit_category_doGetUSModelList': '/cms/edit/category/doGetUSModelList',
        'cms_edit_category_doGetCNModelList': '/cms/edit/category/doGetCNModelList',
        'cms_edit_category_doGetUSProductList': '/cms/edit/category/doGetUSProductList',
        'cms_edit_category_doGetCNProductList': '/cms/edit/category/doGetCNProductList',
        'cms_edit_category_doUpdateUSCategoryInfo': '/cms/edit/category/doUpdateUSCategoryInfo',
        'cms_edit_category_doUpdateCNCategoryInfo': '/cms/edit/category/doUpdateCNCategoryInfo',
        'cms_edit_category_doUpdateCategoryCNPriceSettingInfo': '/cms/edit/category/doUpdateCategoryCNPriceSettingInfo',
        /** new category's url **/
        'cms_new_category_doSaveCategoryInfo': '/cms/new/category/doSaveCategoryInfo',
        /** edit model's url **/
        'cms_edit_model_doGetUSModelInfo': '/cms/edit/model/doGetUSModelInfo',
        'cms_edit_model_doGetCNModelInfo': '/cms/edit/model/doGetCNModelInfo',
        'cms_edit_model_doGetModelCNPriceSettingInfo': '/cms/edit/model/doGetModelCNPriceSettingInfo',
        'cms_edit_model_doGetUSCategoryList': '/cms/edit/model/doGetUSCategoryList',
        'cms_edit_model_doGetCNCategoryList': '/cms/edit/model/doGetCNCategoryList',
        'cms_edit_model_doGetUSProductList': '/cms/edit/model/doGetUSProductList',
        'cms_edit_model_doGetCNProductList': '/cms/edit/model/doGetCNProductList',
        'cms_edit_model_doUpdateUSModelInfo': '/cms/edit/model/doUpdateUSModelInfo',
        'cms_edit_model_doUpdateCNModelInfo': '/cms/edit/model/doUpdateCNModelInfo',
        'cms_edit_model_doUpdateCNModelTmallInfo': '/cms/edit/model/doUpdateCNModelTmallInfo',
        'cms_edit_model_doUpdateCNModelJingDongInfo': '/cms/edit/model/doUpdateCNModelJingDongInfo',
        'cms_edit_model_doUpdateModelCNPriceSettingInfo': '/cms/edit/model/doUpdateModelCNPriceSettingInfo',
        'cms_edit_model_doUpdateModelPrimaryCategory': '/cms/edit/model/doUpdateUSModelPrimaryCategory',
        'cms_edit_model_doUpdateRemoveCategoryModel': '/cms/edit/model/doUpdateRemoveCategoryModel',
        'cms_edit_model_doSearchCategoryByModel': '/cms/edit/model/doSearchCategoryByModel',
        'cms_edit_model_doChangeModel': '/cms/edit/model/doChangeModel',
        /** new model's url **/
        'cms_new_model_doSaveModelInfo': '/cms/new/model/doSaveModelInfo',
        /** edit product's url **/
        'cms_edit_product_doGetUSProductInfo': '/cms/edit/product/doGetUSProductInfo',
        'cms_edit_product_doGetCNProductInfo': '/cms/edit/product/doGetCNProductInfo',
        'cms_edit_product_doGetUSProductPriceInfo': '/cms/edit/product/doGetUSProductPriceInfo',
        'cms_edit_product_doGetCNProductPriceInfo': '/cms/edit/product/doGetCNProductPriceInfo',
        //'cms_edit_product_doGetUSPriceInfoByCardId': '/cms/edit/product/doGetUSPriceInfoByCardId',
        //'cms_edit_product_doGetCNPriceInfoByCardId': '/cms/edit/product/doGetCNPriceInfoByCardId',
        'cms_edit_product_doGetProductCNPriceSettingInfo': '/cms/edit/product/doGetProductCNPriceSettingInfo',
        'cms_edit_product_doGetProductImages': '/cms/edit/product/doGetProductImages',
        'cms_edit_product_doGetProductInventory': '/cms/edit/product/doGetProductInventory',
        //'cms_edit_product_doGetPartnerProductInfo': '/cms/edit/product/doGetPartnerProductInfo',
        'cms_edit_product_doUpdateUSProductInfo': '/cms/edit/product/doUpdateUSProductInfo',
        'cms_edit_product_doUpdateCNProductInfo': '/cms/edit/product/doUpdateCNProductInfo',
        'cms_edit_product_doUpdateCNProductTmallInfo': '/cms/edit/product/doUpdateCNProductTmallInfo',
        'cms_edit_product_doUpdateCNProductJingDongInfo': '/cms/edit/product/doUpdateCNProductJingDongInfo',
        'cms_edit_product_doUpdateProductCNPriceSettingInfo': '/cms/edit/product/doUpdateProductCNPriceSettingInfo',
        'cms_edit_product_doUpdateProductOfficialPriceInfo': '/cms/edit/product/doUpdateProductOfficialPriceInfo',
        //'cms_edit_product_doUpdateProductAmazonPriceInfo': '/cms/edit/product/doUpdateProductAmazonPriceInfo',
        //'cms_edit_product_doUpdateProductCNPriceInfo': '/cms/edit/product/doUpdateProductCNPriceInfo',
        'cms_edit_product_doUpdateProductUSCartPriceInfo': '/cms/edit/product/doUpdateProductUSCartPriceInfo',
        'cms_edit_product_doUpdateProductCNCartPriceInfo': '/cms/edit/product/doUpdateProductCNCartPriceInfo',
        'cms_edit_product_doSetCnProductProperty': '/cms/edit/product/doSetCnProductProperty',
        'cms_edit_product_doSetCnProductShare': '/cms/edit/product/doSetCnProductShare',
        'cms_edit_product_doGetPromotionHistory': '/cms/edit/product/doGetPromotionHistory',
        'cms_edit_product_doGetPriceHistory': '/cms/edit/product/doGetPriceHistory',
        'cms_edit_product_doGetCustomInfo':'/cms/edit/product/doGetCustomInfo',
        'cms_edit_product_doUpdateProductImg':'/cms/edit/product/doUpdateProductImg',
        'cms_edit_product_doDelProductImg':'/cms/edit/product/doDelProductImg',
        
        /** edit promotion's url **/
        'cms_edit_promotion_doGetPromotionInfo': '/cms/edit/promotion/doGetPromotionInfo',
        'cms_edit_promotion_popGetPromMoth': '/cms/edit/promotion/doGetPromMonth',
        'cms_edit_promotion_popGetPromInfo': '/cms/edit/promotion/doGetPromInfo',
        'cms_edit_promotion_popCreatePromProductRelation': '/cms/edit/promotion/doCreatePromProductRelation',
        'cms_edit_promotion_doGetSubPromotionInfo': '/cms/edit/promotion/doGetSubPromotionInfo',
        'cms_edit_promotion_doGetSubPromotionProductInfo': '/cms/edit/promotion/doGetSubPromotionProductInfo',
        'cms_edit_promotion_doUpdatePromotionInfo': '/cms/edit/promotion/doUpdatePromotionInfo',
        /** new promotion's url **/
        'cms_new_promotion_doSavePromotionInfo': '/cms/edit/promotion/doSavePromotionInfo',
        /** popup page's url **/
        'cms_edit_promotion_discount_doUpdatePromotionDiscount': '/cms/edit/promotion/doUpdatePromotionDiscount',
        'cms_edit_promotion_discount_doDeletePromotionProduct': '/cms/edit/promotion/doDeletePromotionProduct',

        /** search url **/
        'cms_search_complex_doSearch': '/cms/search/complex/doSearch',
        'cms_search_advance_doSearch': '/cms/search/advance/doSearch',
        'cms_search_advance_doExport': '/cms/search/advance/doExport',
        /**setSizeChart**/
        'cms_popup_setsizechart_doGetBindedSizeChartList':'/cms/popup/setsizechart/doGetBindedSizeChartList',
        'cms_popup_setsizechart_doGetOtherSizeChartList':'/cms/popup/setsizechart/doGetOtherSizeChartList',
        'cms_popup_setsizechart_doSaveCategorySizeChart':'/cms/popup/setsizechart/doSaveCategorySizeChart',
        'cms_popup_setsizechart_doSaveModelSizeChart':'/cms/popup/setsizechart/doSaveModelSizeChart',
        'cms_popup_setsizechart_doSaveSizeChart':'/cms/popup/setsizechart/doSaveSizeChart',
        'cms_popup_setsizechart_doSaveBindSizeChart':'/cms/popup/setsizechart/doSaveBindSizeChart',
        'cms_popup_setsizechart_doSaveSizeChartDetail':'/cms/popup/setsizechart/doSaveSizeChartDetail',
        'cms_popup_setsizechart_doGetSizeChartModel':'/cms/popup/setsizechart/doGetSizeChartModel',
        'cms_popup_setsizechart_doGetBindSizeChart':'/cms/popup/setsizechart/doGetBindSizeChart',
        'cms_popup_setsizechart_doGetSizeChartDetailInfo':'/cms/popup/setsizechart/doGetSizeChartDetailInfo',
        'cms_popup_setsizechart_doUpdateSizeChartDetail':'/cms/popup/setsizechart/doUpdateSizeChartDetail',

        // popSetProductColumns
        'popSetProductColumns': {
            //'doGetProductColumns': '/cms/popup/setProductColumns/doGetProductColumns',
            'doSaveProductColumns': '/core/setting/user/doUpdateUserConfig'
        },

        //add by lewis start.
        'masterPropSetting':{
            'init':'/cms/masterPropValue/setting/init',
            'search':'/cms/masterPropValue/setting/search',
            'submit':'/cms/masterPropValue/setting/submit',
            'getCategoryNav':'/cms/masterPropValue/setting/getCategoryNav',
            'switchCategory':'/cms/masterPropValue/setting/switchCategory'
        },
        //add by lewis end.

        //add by lewis start.
        'setMtPropValuePopup':{
            'serialize':'/cms/setMasterPropValue/popUp/serialize',
            'deserialize':'/cms/setMasterPropValue/popUp/deserialize',
            'getComplexValues':'/cms/setMasterPropValue/popUp/getComplexValues'
        },
        //add by lewis end.
        
        //add by lewis start 2015-09-01.
        'masterCategoryMatch':{
        	'doGetAllCategory':'/cms/masterCategory/match/getAllCategory',
            'doUpdateCmsCategory':'/cms/masterCategory/match/updateCmsCategory',
            'doGetPlatformInfo':'/cms/masterCategory/match/getPlatformInfo'
        },
        //add by lewis end.
        
     	//add by lewis start 2015-09-10.
        'feedDefaultPropSetting':{
        	'getDefaultProps':'/cms/feedDefaultProperty/setting/getDefaultProps',
            'update':'/cms/feedDefaultProperty/setting/submit'
        },
        //add by lewis end.
        match: {
            props: {
                root: '/cms/feedProp/mapping/',
                getPath: 'getPath',
                getProps: 'getProps',
                getMappings: "getMappings",
                getConst: "getConst",
                getFeedValues: "getFeedValues",
                getPropOptions: "getPropOptions",
                setIgnore: "setIgnore",
                addMapping: "addMapping",
                setMapping: "setMapping",
                delMapping: "delMapping",
                getDefault: "getDefaultValue"
            }
        },
        dict: {
            manage: {
                ROOT: '/cms/dict/manage/',
                DT_GET_DICT : "dtGetDict",
                GET_CONST : "getConst",
                GET_CUSTOMS : "getCustoms",
                SET_DICT : "setDict",
                DEL_DICT : "delDict",
                ADD_DICT : "addDict",
                GET_DICT_LIST : 'getDictList'
            }
        }
    });

    /**
     * mainCategory的显示级别.
     */
    cmsApp.constant ("mainCategoryLevel", {
        "category": "1",
        "model": "2",
        "product": "3"
    });

    /**
     * 设定需要在sessionStorage中保存的key.
     */
    cmsApp.constant("cmsSessionStorageType", {
        CMS_MAIN_CATEGORY_SHOW_PARAM: "cms.mainCategoryShowParam",
        CMS_MAIN_CATEGORY_RETURN_URL: "cms.mainCategoryReturnUrl"
    });

    /**
     * show the date picker.
     */
    cmsApp.controller("datePickerController", ['$scope' ,
        function ($scope) {

            /**
             * 弹出日期控件
             * @param $event
             */
            $scope.openDatePicker = function ($event, name) {
                $event.preventDefault ();
                $event.stopPropagation ();

                switch (name) {
                    case 'avalilabelTime':
                        $scope.avalilabelTime = true;
                        break;
                    case 'usOfficialPublishTime':
                        $scope.usOfficialPublishTime = true;
                        break;
                    case 'usAmazonPublishTime':
                        $scope.usAmazonPublishTime = true;
                        break;
                    case 'cnPricePublishTime':
                        $scope.cnPricePublishTime = true;
                        break;
                    case 'cnOfficialPublishTime':
                        $scope.cnOfficialPublishTime = true;
                        break;
                    case 'cnTaobaoPublishTime':
                        $scope.cnTaobaoPublishTime = true;
                        break;
                    case 'cnTmallPublishTime':
                        $scope.cnTmallPublishTime = true;
                        break;
                    case 'cnTmallGPublishTime':
                        $scope.cnTmallGPublishTime = true;
                        break;
                    case 'cnJDPublishTime':
                        $scope.cnJDPublishTime = true;
                        break;
                    case 'cnJGPublishTime':
                        $scope.cnJGPublishTime = true;
                        break;
                    case 'promotionPreHeadTimeStart':
                        $scope.promotionPreHeadTimeStart = true;
                        break;
                    case 'promotionPreHeadTimeEnd':
                        $scope.promotionPreHeadTimeEnd = true;
                        break;
                    case 'promotionEffectiveTimeStart':
                        $scope.promotionEffectiveTimeStart = true;
                        break;
                    case 'promotionEffectiveTimeEnd':
                        $scope.promotionEffectiveTimeEnd = true;
                        break;
                    case 'cnPublishTime':
                        $scope.cnPublishTime = true;
                        break;
                    default:
                        break;
                }
            };
        }]);
    /**
     * cms的所有popup画面一览.
     */
    cmsApp.constant ("cmsPopupPages", {
        "popPromotionDiscount": {
            "page": "/VoyageOne/modules/cms/popup/promotionDiscount/popPromotionDiscount.tpl.html",
            "controller": "popPromotionDiscountController"
        },
        'popAddToPromotion': {
            'page': '/VoyageOne/modules/cms/popup/addToPromotion/popAddToPromotion.tpl.html',
            'controller': 'popAddToPromotionController'
        },
        'popMoveToModel': {
            'page': '/VoyageOne/modules/cms/popup/moveToModel/popMoveToModel.tpl.html',
            'controller': 'popMoveToModelController'
        },
        'popSetCnProductProperty': {
            'page': '/VoyageOne/modules/cms/popup/setCnProductProperty/popSetCnProductProperty.tpl.html',
            'controller': 'popSetCnProductPropertyController'
        },
        'popSetCnProductShare': {
            'page': '/VoyageOne/modules/cms/popup/setCnProductShare/popSetCnProductShare.tpl.html',
            'controller': 'popSetCnProductShareController'
        },
        'popSetSizeChart': {
            'page': '/VoyageOne/modules/cms/popup/setSizeChart/setSizeChart.tpl.html',
            'controller': 'popSetSizeChartController'
        },
        'popSetSizeChartDetail': {
            'page': '/VoyageOne/modules/cms/popup/setSizeChartDetail/setSizeChartDetail.tpl.html',
            'controller': 'popSetSizeChartDetailController'
        },
        'popNewSizeChartDetail': {
            'page': '/VoyageOne/modules/cms/popup/newSizeChartDetail/newSizeChartDetail.tpl.html',
            'controller': 'popNewSizeChartDetailController'
        },
        'popSetMatchValue': {
            'page': 'modules/cms/popup/setMatchValue/popSetMatchValue.tpl.html',
            'controller': 'SetMatchValueController'
        },
        'popPromotionHistory': {
            'page': '/VoyageOne/modules/cms/popup/promotionHistory/popPromotionHistory.tpl.html',
            'controller': 'popPromotionHistoryController'
        },
        'popPriceHistory': {
            'page': '/VoyageOne/modules/cms/popup/priceHistory/popPriceHistory.tpl.html',
            'controller': 'popPriceHistoryController'
        },
        'popImgSetting': {
            'page': '/VoyageOne/modules/cms/popup/imgSetting/imgSetting.tpl.html',
            'controller': 'imgSettingController'
        },
        'popSetProductColumns': {
            'page': '/VoyageOne/modules/cms/popup/setProductColumns/popSetProductColumns.tpl.html',
            'controller': 'popSetProductColumns'
        },
        //add by lewis start 2015-09-17.
        'popPropValueSetting':{
            'page':'/VoyageOne/modules/cms/popup/propValueSetting/popPropValueSetting.tpl.html',
            'controller':'popPropValueSettingController'
        },
        //add by lewis end.
      //add by lewis start 2015-09-17.
        'popPropValueSettingWarningMsg':{
            'page':'/VoyageOne/modules/cms/popup/warningMessageView/warningMsg.tpl.html',
            'controller':'warningMsgController'
        },
        //add by lewis end.
        
    });

    /**
     * 用于CMS系统弹出popUp画面使用.
     */
    cmsApp.controller('cmsPopupController', ['$scope', 'cmsPopupPages', 'systemCountry', '$modal', 'notify',
        function ($scope, cmsPopupPages, systemCountry, $modal, notify) {
            /**
             * 弹出Add To Promotion页面.
             */
            $scope.openAddToPromotion = function (selectIdList) {
                if (selectIdList.length) {
                    var modalInstance = $modal.open({
                        templateUrl: cmsPopupPages.popAddToPromotion.page,
                        controller: cmsPopupPages.popAddToPromotion.controller,
                        size: 'lg',
                        resolve: {
                            productList: function () {
                                return selectIdList;
                            }
                        }
                    });
                } else {
                    notify.warning("CMS_MSG_NO_PRODUCT_SELECTED");
                }
            };

            /**
             * 弹出Set Product Property页面.
             */
            $scope.openSetCnProductProperty = function (selectIdList){
                if (selectIdList.length) {
                    var modalInstance = $modal.open({
                        templateUrl: cmsPopupPages.popSetCnProductProperty.page,
                        controller: cmsPopupPages.popSetCnProductProperty.controller,
                        size: 'md',
                        resolve: {
                            productIdArray: function () {
                                return selectIdList;
                            }
                        }
                    });
                } else {
                    notify.warning("CMS_MSG_NO_PRODUCT_SELECTED");
                }
            };

            /**
             * 弹出Move To Model页面.
             */
            $scope.openMoveToModel = function (selectDataList) {
                if (selectDataList.length) {
                    var modalInstance = $modal.open({
                        templateUrl: cmsPopupPages.popMoveToModel.page,
                        controller: cmsPopupPages.popMoveToModel.controller,
                        size: 'md',
                        resolve: {
                            productInfoArray: function () {
                                return selectDataList;
                            }
                        }
                });
                } else {
                    notify.warning("CMS_MSG_NO_PRODUCT_SELECTED");
                }
            };

            /**
             * 弹出Set Item Share页面.
             */
            $scope.openSetCnProductShare = function (selectIdList){
                if (selectIdList.length) {
                    var modalInstance = $modal.open({
                        templateUrl: cmsPopupPages.popSetCnProductShare.page,
                        controller: cmsPopupPages.popSetCnProductShare.controller,
                        size: 'md',
                        resolve: {
                            productIdArray: function () {
                                return selectIdList;
                            }
                        }
                    });
                }
            };

            /**
             * 弹出Promotion History页面
             */
            $scope.openPromotionHistory = function (productId, cartId) {
                if (!!productId && !!cartId) {
                    var modalInstance = $modal.open({
                        templateUrl: cmsPopupPages.popPromotionHistory.page,
                        controller: cmsPopupPages.popPromotionHistory.controller,
                        size: 'lg',
                        resolve: {
                            parameters: function () {
                                return {productId: productId, cartId: cartId};
                            }
                        }
                    })
                }
            };

            /**
             * 弹出Price History页面
             */
            $scope.openPriceHistory = function (productId, cartId, priceType) {
                if(!!productId && !!cartId){
                    var modalInstance = $modal.open({
                        templateUrl: cmsPopupPages.popPriceHistory.page,
                        controller: cmsPopupPages.popPriceHistory.controller,
                        size: 'lg',
                        resolve: {
                            parameters: function(){
                                return {productId: productId, cartId: cartId, priceType: priceType};
                            }
                        }
                    });
                }
            };

            /**
             * SetSizeChart画面
             */
            $scope.openSizeChart = function (id, type, sizeChartId, fnInitial, modelInfo) {

                var parameters = {
                    id: id,
                    type: type,
                    sizeChartId: sizeChartId,
                    modelInfo: modelInfo
                };

                var modalInstance = $modal.open({
                    templateUrl: cmsPopupPages.popSetSizeChart.page,
                    controller: cmsPopupPages.popSetSizeChart.controller,
                    size: 'lg',
                    resolve: {
                        parameters: function () { return parameters; }
                    }
                });

                modalInstance.result.then(function(){
                	fnInitial();
                })
            };

            /**
             * 弹出尺码对照表的详细画面.
             */
            $scope.openSizeChartDetail = function (sizeChartId) {

                var parameters = {
                    sizeChartId: sizeChartId
                };

                var modalInstance = $modal.open({
                    templateUrl: cmsPopupPages.popSetSizeChartDetail.page,
                    controller: cmsPopupPages.popSetSizeChartDetail.controller,
                    size: 'lg',
                    resolve: {
                        parameters: function() {
                            return parameters;
                        }
                    }
                });

                modalInstance.result.then(function(){
                })
            };

            /**
             * 打开新建尺码对照的画面.
             */
            $scope.openNewSizeChartDetail = function (fnInitial) {

                var modalInstance = $modal.open({
                    templateUrl: cmsPopupPages.popNewSizeChartDetail.page,
                    controller: cmsPopupPages.popNewSizeChartDetail.controller,
                    size: 'lg'
                });

                modalInstance.result.then(function(newSizeChartId){
                    fnInitial(newSizeChartId);
                });
            };

            /**
             * 打开产品显示和隐藏的效果.
             * @param fhRefreshUs
             * @param fnRefreshCn
             * @param type
             */
            $scope.openSetProductColumns = function (fhRefreshUs, fnRefreshCn, type) {

                var modalInstance = $modal.open({
                    templateUrl: cmsPopupPages.popSetProductColumns.page,
                    controller: cmsPopupPages.popSetProductColumns.controller,
                    size: 'lg',
                    resolve: {
                        parameters: function () {
                            return {type: type};
                        }
                    }
                });
                
                modalInstance.result.then(function(){
                    switch (type) {
                        case 'us':
                            fhRefreshUs();
                            break;
                        case 'cn':
                            fnRefreshCn();
                            break;
                    }
                });
            };
            
            /**
             * 弹出属性设定页面.
             * @param selectObj
             */
            $scope.openPropValueSetting = function (selectObj) {
                    var modalInstance = $modal.open({
                        templateUrl: cmsPopupPages.popPropValueSetting.page,
                        controller: cmsPopupPages.popPropValueSetting.controller,
                        size: 'lg',
                        resolve: {
                            parameters: function(){
                                return {selectObj:selectObj};
                            }
                        }
                    });
            };

        }]);

    return cmsApp;
});
