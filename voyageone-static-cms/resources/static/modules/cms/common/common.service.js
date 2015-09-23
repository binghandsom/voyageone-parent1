/**
 * @Name:    common.service.js
 * @Date:    2015/6/17
 *
 * @User:    Edward
 * @Version: 1.0.0
 */

define(function(require) {

    var cmsApp = require('modules/cms/cms.module');
    //require('modules/services/master.service');

    cmsApp.service('cmsCommonService', ['$rootScope', '$q', 'cmsAction', 'ajaxService', 'userService', 'DTColumnBuilder', '$translate',
        function($rootScope, $q, cmsAction, ajaxService, userService, DTColumnBuilder, $translate) {

            var _ = require('underscore');
            var commonUtil = require ('components/util/commonUtil');

            /**
             * get the navigation about the category.
             * @param categoryId
             * @returns {*}
             */
            this.doGetNavigationByCategoryId = function (categoryId) {
                var defer = $q.defer ();
                var data = {};
                data.channelId = userService.getSelChannel();
                data.categoryId = categoryId;

                ajaxService.ajaxPostWithData (data, cmsAction.cms_common_navigation_doGetNavigationByCategoryId)
                    .then (function (response) {
                    defer.resolve (response.data);
                });

                return defer.promise;
            };

            /**
             * get the navigation about the category and model.
             * @param categoryId
             * @param modelId
             * @returns {*}
             */
            this.doGetNavigationByCategoryModelId = function (type, categoryId, modelId) {
                var defer = $q.defer ();
                var data = {};
                data.type = type;
                data.channelId = userService.getSelChannel();
                data.categoryId = categoryId;
                data.modelId = modelId;

                ajaxService.ajaxPostWithData (data, cmsAction.cms_common_navigation_doGetNavigationByCategoryModelId)
                    .then (function (response) {
                    defer.resolve (response.data);
                });

                return defer.promise;
            };

            /**
             * 根据不同的国家展示不同的产品信息.
             * @param type
             * @returns {Array}
             */
            this.doGetProductListByUserConfig = function (type, productColumns) {
                switch (type) {
                    case 'us':
                        return getUsProductList(productColumns);
                        break;
                    case 'cn':
                        return getCnProductList(productColumns);
                        break;
                    default :
                        break;
                }
            };

            /**
             * 获取US的产品信息列表.
             * @returns {Array}
             */
            function getUsProductList (productColumns) {

                // 获得US产品的属性列表.
                var usProductAttributes = userService.getUserConfig().cmsUsProductAttributes;

                // 获取被显示的产品columns列表.
                var showUsProductList =[];
                _.forEach(productColumns, function (object, index) {

                    if (_.indexOf(usProductAttributes, (index + 1).toString()) == -1) {
                        object.notVisible();
                    }
                    showUsProductList.push (object);
                });

                return showUsProductList;
            }

            /**
             * 获取CN的产品信息列表.
             * @returns {Array}
             */
            function getCnProductList (productColumns) {

                // 获得US产品的属性列表.
                var cnProductAttributes = userService.getUserConfig().cmsCnProductAttributes;

                // 获取被显示的产品columns列表.
                var showCnProductList = [];
                _.forEach(productColumns, function (object, index) {

                    if (_.indexOf(cnProductAttributes, (index + 1).toString()) == -1) {
                        object.notVisible();
                    }
                    showCnProductList.push (object);
                });

                return showCnProductList;
            }

/*            this.doGetCMSMasterInfo = function (channelId) {
                var defer = $q.defer ();
                var data = {};
                data.channelId = channelId;
                // TODO 以后根据用户当前的选择
                ajaxService.ajaxPostWithData (data, cmsAction.oms_common_master_doGetMasterDataList)
                    .then (function (response) {
                    setMasterInfoToSession (response.data);
                });

                return defer.promise;
            };*/

            /**
             * 将取得的master数据保存在session中.
             * @param data
             */
/*            function setMasterInfoToSession(data) {
                // 设置category menu list.
                //masterService.setCategoryMenuList(data.categoryMenuList);
                $rootScope.cmsCategoryMenuList = data.categoryMenuList;

                // 设置category list.
                //masterService.setCategoryList(data.categoryList);
                $rootScope.cmsCategoryList = data.categoryList;

                // 设置amazon category list.
                //masterService.setAmazonCategoryList(data.amazonCategoryList);
                $rootScope.cmsAmazonCategoryList = data.amazonCategoryList;

                // 设置google category list.
                //masterService.setGoogleCategoryList(data.googleCategoryList);
                $rootScope.cmsGoogleCategoryList = data.googleCategoryList;

                // 设置price grabber category list.
                //masterService.setPriceGrabberCategoryList(data.priceGrabberCategoryList);
                $rootScope.cmsPriceGrabberCategoryList = data.priceGrabberCategoryList;

                // 设置hs code sh list.
                //masterService.setHsCodeShCategoryList(data.hsCodeShList);
                $rootScope.cmsHsCodeShCategoryList = data.hsCodeShList;

                // 设置hs code gz list.
                //masterService.setHsCodeGzCategoryList(data.hsCodeGzList);
                $rootScope.cmsHsCodeGzCategoryList = data.hsCodeGzList;

                // 设置base price list.
                //masterService.setBasePriceList(data.basePriceList);
                $rootScope.cmsBasePriceList = data.basePriceList;

                // 设置percent list.
                //masterService.setPercentList(data.percentList);
                $rootScope.cmsPercentList = data.percentList;

                // 设置product type list.
                //masterService.setProductTypeList(data.productTypeList);
                $rootScope.cmsProductTypeList = data.productTypeList;

                // 设置brand list.
                //masterService.setBrandList(data.brandList);
                $rootScope.cmsBrandList = data.brandList;

                // 设置size type list.
                //masterService.setSizeTypeList(data.sizeTypeList);
                $rootScope.cmsSizeTypeList = data.sizeTypeList;
            }*/


            /**
             * get the property data by typeIdList.
             * @param values
             * @returns {r.promise|promise|qFactory.Deferred.promise|x.ready.promise|fd.g.promise}
             */
            //this.doGetCodeList = function(values) {
            //    var defer = $q.defer();
            //    var data = {};
            //    data.typeIdList = values;
            //    ajaxService.ajaxPostWithData(data, cmsAction.oms_common_service_doGetCode)
            //        .then(function(response) {
            //            defer.resolve(response.data);
            //        });
            //
            //    return defer.promise;
            //};

        }]);

    return cmsApp;
});