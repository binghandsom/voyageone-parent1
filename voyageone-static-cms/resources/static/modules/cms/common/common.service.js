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
             * 根据不同的国家展示不同的产品信息.(datatable初始化调用)
             * @param type
             * @param columns
             */
            this.doGetProductColumnsAtFirst = function (type, columns) {

                var productAttributes = [];
                var productColumns = [];
                switch (type) {
                    case 'us':
                        productAttributes = userService.getUserConfig().cmsUsProductAttributes;
                        productColumns = getAttributeId($rootScope.cmsMaster.USProductAttributes);
                        break;
                    case 'cn':
                        productAttributes = userService.getUserConfig().cmsCnProductAttributes;
                        productColumns = getAttributeId($rootScope.cmsMaster.CNProductAttributes);
                        break;
                    default :
                        break;
                }

                // 获得US产品的属性列表.
                _.forEach(columns, function (object, index) {

                    // 该店铺需要显示的columns意外的columns不显示
                    if (_.indexOf(productColumns, index.toString()) == -1 && index > 0) {
                        object.notVisible();
                    } else {

                        // 显示用户自己设定的columns
                        if (!_.isEmpty(productAttributes)
                            && _.indexOf(productAttributes, index.toString()) == -1 && index > 0) {
                            object.notVisible();
                        }
                    }
                });
            };

            /**
             * 根据不同的国家展示不同的产品信息.
             * @param type
             * @returns {Array}
             */
            this.doGetProductListByUserConfig = function (type, datatable) {

                var productAttributes = [];
                var productColumns = [];
                switch (type) {
                    case 'us':
                        productAttributes = userService.getUserConfig().cmsUsProductAttributes;
                        productColumns = getAttributeId($rootScope.cmsMaster.USProductAttributes);
                        break;
                    case 'cn':
                        productAttributes = userService.getUserConfig().cmsCnProductAttributes;
                        productColumns = getAttributeId($rootScope.cmsMaster.USProductAttributes);
                        break;
                    default :
                        break;
                }

                // 如果用户设置的要显示的columns
                if (!_.isEmpty(productAttributes)) {

                    datatable.columns().visible(false);
                    // 默认checkbox必须显示.
                    datatable.column(0).visible(true);

                    // 用户选中的columns都要显示.
                    _.forEach(productAttributes, function (value) {
                        datatable.column(parseInt(value)).visible(true);
                    });
                } else {
                    // 默认checkbox必须显示.
                    datatable.column(0).visible(true);

                    // 只显示该店铺需要显示的columns
                    _.forEach(productColumns, function (value) {
                        datatable.column(parseInt(value)).visible(true);
                    })
                }
            };

            /**
             * 获取Attribute的Id列表.
             * @param productAttributes
             * @returns {Array}
             */
            function getAttributeId (productAttributes) {
                var attributeList = [];
                _.forEach(productAttributes, function (attribute) {
                    attributeList.push(attribute.attributeValueId);
                });

                return attributeList
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