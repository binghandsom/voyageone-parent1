/**
 * @Name: edit.service.js
 * @Date: 2015/6/23
 * 
 * @User: Edward
 * @Version: 1.0.0
 */

define(function (require) {
    var cmsApp = require('modules/cms/cms.module');

    cmsApp.service('editCategoryService', ['$q', 'cmsAction', 'ajaxService', 'userService',
        function ($q, cmsAction, ajaxService, userService) {

            /**
			 * 取得当前category的美国信息.
			 * 
			 * @param categoryId
			 * @returns {*}
			 */
            this.doGetUSCategoryInfo = function (categoryId) {
                var defer = $q.defer();
                var data = {};
                // data.typeIdList = values;
                data.channelId = userService.getSelChannel();
                data.categoryId = categoryId;
                // TODO 以后根据用户当前的选择
                ajaxService.ajaxPostWithData(data, cmsAction.cms_edit_category_doGetUSCategoryInfo)
                    .then(function (response) {
                    defer.resolve(response.data);
                });

                return defer.promise;
            };

            /**
			 * 取得当前category的中国信息.
			 * 
			 * @param categoryId
			 * @returns {*}
			 */
            this.doGetCNCategoryInfo = function (categoryId) {
                var defer = $q.defer();
                var data = {};
                // data.typeIdList = values;
                data.channelId = userService.getSelChannel();
                data.categoryId = categoryId;
                // TODO 以后根据用户当前的选择
                ajaxService.ajaxPostWithData(data, cmsAction.cms_edit_category_doGetCNCategoryInfo)
                    .then(function (response) {
                    defer.resolve(response.data);
                });

                return defer.promise;
            };

            /**
			 * 取得当前category的price setting信息.
			 * 
			 * @param categoryId
			 * @returns {*}
			 */
            this.doGetCategoryCNPriceSettingInfo = function (categoryId) {
                var defer = $q.defer();
                var data = {};
                // data.typeIdList = values;
                data.channelId = userService.getSelChannel();
                data.categoryId = categoryId;
                // TODO 以后根据用户当前的选择
                ajaxService.ajaxPostWithData(data, cmsAction.cms_edit_category_doGetCategoryCNPriceSettingInfo)
                    .then(function (response) {
                    defer.resolve(response.data);
                });

                return defer.promise;
            };

            /**
			 * 取得当前category的US Sub Categories信息.
			 * 
			 * @param categoryId
			 * @returns {*}
			 */
            this.doGetUSSubCategoryList = function (categoryId) {
                var defer = $q.defer();
                var data = {};
                // data.typeIdList = values;
                data.channelId = userService.getSelChannel();
                data.categoryId = categoryId;
                // TODO 以后根据用户当前的选择
                ajaxService.ajaxPostWithData(data, cmsAction.cms_edit_category_doGetUSSubCategoryList)
                    .then(function (response) {
                    defer.resolve(response.data);
                });

                return defer.promise;
            };

            /**
			 * 取得当前category的CN Sub Categories信息.
			 * 
			 * @param categoryId
			 * @returns {*}
			 */
            this.doGetCNSubCategoryList = function (categoryId) {
                var defer = $q.defer();
                var data = {};
                // data.typeIdList = values;
                data.channelId = userService.getSelChannel();
                data.categoryId = categoryId;
                // TODO 以后根据用户当前的选择
                ajaxService.ajaxPostWithData(data, cmsAction.cms_edit_category_doGetCNSubCategoryList)
                    .then(function (response) {
                    defer.resolve(response.data);
                });

                return defer.promise;
            };

            /**
			 * 取得当前category的US Models信息.
			 * 
			 * @param categoryId
			 * @returns {*}
			 */
            this.doGetUSModelList = function (categoryId) {
                var defer = $q.defer();
                var data = {};
                // data.typeIdList = values;
                data.channelId = userService.getSelChannel();
                data.categoryId = categoryId;
                // TODO 以后根据用户当前的选择
                ajaxService.ajaxPostWithData(data, cmsAction.cms_edit_category_doGetUSModelList)
                    .then(function (response) {
                    defer.resolve(response.data);
                });

                return defer.promise;
            };

            /**
			 * 取得当前category的CN Models信息.
			 * 
			 * @param categoryId
			 * @returns {*}
			 */
            this.doGetCNModelList = function (categoryId) {
                var defer = $q.defer();
                var data = {};
                // data.typeIdList = values;
                data.channelId = userService.getSelChannel();
                data.categoryId = categoryId;
                // TODO 以后根据用户当前的选择
                ajaxService.ajaxPostWithData(data, cmsAction.cms_edit_category_doGetCNModelList)
                    .then(function (response) {
                    defer.resolve(response.data);
                });

                return defer.promise;
            };

            /**
             * 取得当前category的US Products信息.
             * @param data
             * @param categoryId
             * @returns {*}
             */
            this.doGetUSProductList = function (data, categoryId) {
                var defer = $q.defer();
                data.param = {
                    channelId: userService.getSelChannel(),
                    categoryId: categoryId
                };
                // TODO 以后根据用户当前的选择
                ajaxService.ajaxPostWithData(data, cmsAction.cms_edit_category_doGetUSProductList)
                    .then(function (response) {
                    defer.resolve(response.data);
                });

                return defer.promise;
            };

            /**
             * 取得当前category的CN Products信息.
             * @param data
             * @param categoryId
             * @returns {*}
             */
            this.doGetCNProductList = function (data, categoryId) {
                var defer = $q.defer();
                data.param = {
                    channelId: userService.getSelChannel(),
                    categoryId: categoryId
                };
                // TODO 以后根据用户当前的选择
                ajaxService.ajaxPostWithData(data, cmsAction.cms_edit_category_doGetCNProductList)
                    .then(function (response) {
                    defer.resolve(response.data);
                });

                return defer.promise;
            };

            /**
			 * 更新US Category的信息.
			 * 
			 * @param categoryId
			 * @param usCategoryInfo
			 * @returns {*}
			 */
            this.doUpdateUSCategoryInfo = function (categoryId, usCategoryInfo) {
                var defer = $q.defer();
                // var data = {};
                // data.typeIdList = values;
                // data.categoryId = categoryId;
                // data.channelId = userService.getSelChannel();
                // data.usCategoryInfo = usCategoryInfo;

                ajaxService.ajaxPostWithData(usCategoryInfo, cmsAction.cms_edit_category_doUpdateUSCategoryInfo)
                    .then(function (response) {
                    defer.resolve(response.data);
                });

                return defer.promise;
            };

            /**
			 * 更新CN Category的信息.
			 * 
			 * @param categoryId
			 * @param cnCategoryInfo
			 * @returns {*}
			 */
            this.doUpdateCNCategoryInfo = function (categoryId, cnCategoryInfo) {
                var defer = $q.defer();
                // var data = {};
                // data.typeIdList = values;
                // data.categoryId = categoryId;
                // data.channelId = userService.getSelChannel();
                // data.cnCategoryInfo = cnCategoryInfo;

                ajaxService.ajaxPostWithData(cnCategoryInfo, cmsAction.cms_edit_category_doUpdateCNCategoryInfo)
                    .then(function (response) {
                    defer.resolve(response.data);
                });

                return defer.promise;
            };

            /**
			 * 更新CN PriceSetting的信息.
			 * 
			 * @param categoryId
			 * @param cnPriceSettingInfo
			 * @returns {*}
			 */
            this.doUpdateCategoryCNPriceSettingInfo = function (categoryId, cnPriceSettingInfo) {
                var defer = $q.defer();
                // var data = {};
                // data.typeIdList = values;
                // data.categoryId = categoryId;
                // data.channelId = userService.getSelChannel();
                // data.cnPriceSettingInfo = cnPriceSettingInfo;

                ajaxService.ajaxPostWithData(cnPriceSettingInfo, cmsAction.cms_edit_category_doUpdateCategoryCNPriceSettingInfo)
                    .then(function (response) {
                    defer.resolve(response.data);
                });

                return defer.promise;
            };
        }
    ]);

    cmsApp.service('editModelService', ['$q', 'cmsAction', 'ajaxService', 'userService',
        function ($q, cmsAction, ajaxService, userService) {

            /**
			 * 取得US Model信息.
			 * 
			 * @param modelId
			 * @returns {*}
			 */
            this.doGetUSModelInfo = function (modelId) {
                var defer = $q.defer();
                var data = {};
                // data.typeIdList = values;
                data.channelId = userService.getSelChannel();
                data.modelId = modelId;
                // TODO 以后根据用户当前的选择
                ajaxService.ajaxPostWithData(data, cmsAction.cms_edit_model_doGetUSModelInfo)
                    .then(function (response) {
                    defer.resolve(response.data);
                });

                return defer.promise;
            };

            /**
			 * 取得CN Model信息.
			 * 
			 * @param modelId
			 * @returns {*}
			 */
            this.doGetCNModelInfo = function (modelId) {
                var defer = $q.defer();
                var data = {};
                // data.typeIdList = values;
                data.channelId = userService.getSelChannel();
                data.modelId = modelId;
                // TODO 以后根据用户当前的选择
                ajaxService.ajaxPostWithData(data, cmsAction.cms_edit_model_doGetCNModelInfo)
                    .then(function (response) {
                    defer.resolve(response.data);
                });

                return defer.promise;
            };

            /**
			 * 取得当前model的price setting信息.
			 * 
			 * @param modelId
			 * @returns {*}
			 */
            this.doGetModelCNPriceSettingInfo = function (modelId) {
                var defer = $q.defer();
                var data = {};
                // data.typeIdList = values;
                data.channelId = userService.getSelChannel();
                data.modelId = modelId;
                // TODO 以后根据用户当前的选择
                ajaxService.ajaxPostWithData(data, cmsAction.cms_edit_model_doGetModelCNPriceSettingInfo)
                    .then(function (response) {
                    defer.resolve(response.data);
                });

                return defer.promise;
            };

            /**
			 * 取得US Category的信息.
			 * 
			 * @param modelId
			 * @returns {*}
			 */
            this.doGetUSCategoryList = function (modelId) {
                var defer = $q.defer();
                var data = {};
                // data.typeIdList = values;
                data.channelId = userService.getSelChannel();
                data.modelId = modelId;
                // TODO 以后根据用户当前的选择
                ajaxService.ajaxPostWithData(data, cmsAction.cms_edit_model_doGetUSCategoryList)
                    .then(function (response) {
                    defer.resolve(response.data);
                });

                return defer.promise;
            };

            /**
			 * 取得CN Category的信息.
			 * 
			 * @param modelId
			 * @returns {*}
			 */
            this.doGetCNCategoryList = function (modelId) {
                var defer = $q.defer();
                var data = {};
                // data.typeIdList = values;
                data.channelId = userService.getSelChannel();
                data.modelId = modelId;
                // TODO 以后根据用户当前的选择
                ajaxService.ajaxPostWithData(data, cmsAction.cms_edit_model_doGetCNCategoryList)
                    .then(function (response) {
                    defer.resolve(response.data);
                });

                return defer.promise;
            };

            /**
             * 取得US Product的信息.
             * @param data
             * @param modelId
             * @returns {*}
             */
            this.doGetUSProductList = function (data, modelId) {
                var defer = $q.defer();
                data.param = {
                    channelId: userService.getSelChannel(),
                    modelId: modelId
                };
                // TODO 以后根据用户当前的选择
                ajaxService.ajaxPostWithData(data, cmsAction.cms_edit_model_doGetUSProductList)
                    .then(function (response) {
                    defer.resolve(response.data);
                });

                return defer.promise;
            };

            /**
             * 取得CN Product的信息.
             * @param data
             * @param modelId
             * @returns {*}
             */
            this.doGetCNProductList = function (data, modelId) {
                var defer = $q.defer();
                data.param = {
                    channelId: userService.getSelChannel(),
                    modelId: modelId
                };
                // TODO 以后根据用户当前的选择
                ajaxService.ajaxPostWithData(data, cmsAction.cms_edit_model_doGetCNProductList)
                    .then(function (response) {
                    defer.resolve(response.data);
                });

                return defer.promise;
            };

            /**
			 * 更新Us Model的信息.
			 * 
			 * @param modelId
			 * @param usModelInfo
			 * @returns {*}
			 */
            this.doUpdateUSModelInfo = function (modelId, usModelInfo) {
                var defer = $q.defer();
                // var data = {};
                // data.typeIdList = values;
                // data.modelId = modelId;
                // data.channelId = userService.getSelChannel();
                // data.usModelInfo = usModelInfo;

                ajaxService.ajaxPostWithData(usModelInfo, cmsAction.cms_edit_model_doUpdateUSModelInfo)
                    .then(function (response) {
                    defer.resolve(response.data);
                });

                return defer.promise;
            };

            /**
			 * 更新CN Model的信息.
			 * 
			 * @param modelId
			 * @param cnBaseModelInfo
			 * @returns {*}
			 */
            this.doUpdateCNModelInfo = function (modelId, cnBaseModelInfo) {
                var defer = $q.defer();
                // var data = {};
                // data.typeIdList = values;
                // data.modelId = modelId;
                // data.channelId = userService.getSelChannel();
                // data.cnBaseModelInfo = cnBaseModelInfo;

                ajaxService.ajaxPostWithData(cnBaseModelInfo, cmsAction.cms_edit_model_doUpdateCNModelInfo)
                    .then(function (response) {
                    defer.resolve(response.data);
                });

                return defer.promise;
            };

            /**
			 * 更新TM Model的信息.
			 * 
			 * @param modelId
			 * @param tmModelInfo
			 * @returns {*}
			 */
            this.doUpdateCNModelTmallInfo = function (modelId, tmModelInfo) {
                var defer = $q.defer();
                // var data = {};
                // data.typeIdList = values;
                // data.modelId = modelId;
                // data.channelId = userService.getSelChannel();
                // data.tmModelInfo = tmModelInfo;

                ajaxService.ajaxPostWithData(tmModelInfo, cmsAction.cms_edit_model_doUpdateCNModelTmallInfo)
                    .then(function (response) {
                    defer.resolve(response.data);
                });

                return defer.promise;
            };

            /**
			 * 更新TM Model的信息.
			 * 
			 * @param modelId
			 * @param jdModelInfo
			 * @returns {*}
			 */
            this.doUpdateCNModelJingDongInfo = function (modelId, jdModelInfo) {
                var defer = $q.defer();
                // var data = {};
                // data.typeIdList = values;
                // data.modelId = modelId;
                // data.channelId = userService.getSelChannel();
                // data.jdModelInfo = jdModelInfo;

                ajaxService.ajaxPostWithData(jdModelInfo, cmsAction.cms_edit_model_doUpdateCNModelJingDongInfo)
                    .then(function (response) {
                    defer.resolve(response.data);
                });

                return defer.promise;
            };

            /**
			 * 更新CN PriceSetting的信息.
			 * 
			 * @param modelId
			 * @param cnPriceSettingInfo
			 * @returns {*}
			 */
            this.doUpdateModelCNPriceSettingInfo = function (modelId, cnPriceSettingInfo) {
                var defer = $q.defer();
                // var data = {};
                // data.typeIdList = values;
                // data.modelId = modelId;
                // data.channelId = userService.getSelChannel();
                // data.cnPriceSettingInfo = cnPriceSettingInfo;

                ajaxService.ajaxPostWithData(cnPriceSettingInfo, cmsAction.cms_edit_model_doUpdateModelCNPriceSettingInfo)
                    .then(function (response) {
                    defer.resolve(response.data);
                });

                return defer.promise;
            };

            /**
			 * 更新该Model被选中的Primary Category信息.
			 * 
			 * @param modelId
			 * @param typeId
			 * @param categoryId
			 * @returns {*}
			 */
            this.doUpdateModelPrimaryCategory = function (modelId, typeId, categoryId) {
                var defer = $q.defer();
                var data = {};
                // data.typeIdList = values;
                data.modelId = modelId;
                data.channelId = userService.getSelChannel();
                data.typeId = typeId;
                data.categoryId = categoryId;

                ajaxService.ajaxPostWithData(data, cmsAction.cms_edit_model_doUpdateModelPrimaryCategory)
                    .then(function (response) {
                    defer.resolve(response.data);
                });

                return defer.promise;
            };

            /**
			 * 解除该Category和Model的关联关系.
			 * 
			 * @param modelId
			 * @param categoryId
			 * @returns {*}
			 */
            this.doUpdateRemoveCategoryModel = function (modelId, categoryId) {
                var defer = $q.defer();
                var data = {};
                // data.typeIdList = values;
                data.modelId = modelId;
                data.channelId = userService.getSelChannel();
                data.categoryId = categoryId;

                ajaxService.ajaxPostWithData(data, cmsAction.cms_edit_model_doUpdateRemoveCategoryModel)
                    .then(function (response) {
                    defer.resolve(response.data);
                });

                return defer.promise;
            }
        }
    ]);

    cmsApp.service('editProductService', ['$q', 'cmsAction', 'ajaxService', 'userService',
        function ($q, cmsAction, ajaxService, userService) {

            /**
			 * 取得US Product信息.
			 * 
			 * @param productId
			 * @returns {*}
			 */
            this.doGetUSProductInfo = function (productId) {
                var defer = $q.defer();
                var data = {};
                // data.typeIdList = values;
                data.channelId = userService.getSelChannel();
                data.productId = productId;
                // TODO 以后根据用户当前的选择
                ajaxService.ajaxPostWithData(data, cmsAction.cms_edit_product_doGetUSProductInfo)
                    .then(function (response) {
                    defer.resolve(response.data);
                });

                return defer.promise;
            };

            /**
			 * 取得CN Product信息.
			 * 
			 * @param productId
			 * @returns {*}
			 */
            this.doGetCNProductInfo = function (productId) {
                var defer = $q.defer();
                var data = {};
                // data.typeIdList = values;
                data.channelId = userService.getSelChannel();
                data.productId = productId;
                // TODO 以后根据用户当前的选择
                ajaxService.ajaxPostWithData(data, cmsAction.cms_edit_product_doGetCNProductInfo)
                    .then(function (response) {
                    defer.resolve(response.data);
                });

                return defer.promise;
            };

            /**
			 * 取得US Price信息.
			 * 
			 * @param productId
			 * @returns {*}
			 */
            this.doGetUSProductPriceInfo = function (productId) {
                var defer = $q.defer();
                var data = {};
                // data.typeIdList = values;
                data.channelId = userService.getSelChannel();
                data.productId = productId;
                // TODO 以后根据用户当前的选择
                ajaxService.ajaxPostWithData(data, cmsAction.cms_edit_product_doGetUSProductPriceInfo)
                    .then(function (response) {
                    defer.resolve(response.data);
                });

                return defer.promise;
            };

            /**
			 * 取得CN Price信息.
			 * 
			 * @param productId
			 * @returns {*}
			 */
            this.doGetCNProductPriceInfo = function (productId) {
                var defer = $q.defer();
                var data = {};
                // data.typeIdList = values;
                data.channelId = userService.getSelChannel();
                data.productId = productId;
                // TODO 以后根据用户当前的选择
                ajaxService.ajaxPostWithData(data, cmsAction.cms_edit_product_doGetCNProductPriceInfo)
                    .then(function (response) {
                    defer.resolve(response.data);
                });

                return defer.promise;
            };

            /**
			 * 取得US Price信息.
			 * 
			 * @param productId
			 * @param cartId(5:amazon,
			 *            6:official)
			 * @returns {*}
			 */
            this.doGetUSPriceInfoByCardId = function (productId, cartId) {
                var defer = $q.defer();
                var data = {};
                // data.typeIdList = values;
                data.channelId = userService.getSelChannel();
                data.productId = productId;
                data.cartId = cartId;
                // TODO 以后根据用户当前的选择
                ajaxService.ajaxPostWithData(data, cmsAction.cms_edit_product_doGetUSPriceInfoByCardId)
                    .then(function (response) {
                    defer.resolve(response.data);
                });

                return defer.promise;
            };

            /**
			 * 取得CN Price信息.
			 * 
			 * @param productId
			 * @param cartId(79:CN,
			 *            20:Tmall, 21:Taobao, 23:TmallG, 24:JD, 26:JG,
			 *            25:Official)
			 * @returns {*}
			 */
            this.doGetCNPriceInfoByCardId = function (productId, cartId) {
                var defer = $q.defer();
                var data = {};
                // data.typeIdList = values;
                data.channelId = userService.getSelChannel();
                data.productId = productId;
                data.cartId = cartId;
                // TODO 以后根据用户当前的选择
                ajaxService.ajaxPostWithData(data, cmsAction.cms_edit_product_doGetCNPriceInfoByCardId)
                    .then(function (response) {
                    defer.resolve(response.data);
                });

                return defer.promise;
            };

            /**
			 * 取得当前product的price setting信息.
			 * 
			 * @param productId
			 * @returns {*}
			 */
            this.doGetProductCNPriceSettingInfo = function (productId) {
                var defer = $q.defer();
                var data = {};
                // data.typeIdList = values;
                data.channelId = userService.getSelChannel();
                data.productId = productId;
                // TODO 以后根据用户当前的选择
                ajaxService.ajaxPostWithData(data, cmsAction.cms_edit_product_doGetProductCNPriceSettingInfo)
                    .then(function (response) {
                    defer.resolve(response.data);
                });

                return defer.promise;
            };

            /**
			 * 取得该Product的图片信息.
			 * 
			 * @param productId
			 * @returns {*}
			 */
            this.doGetProductImages = function (productId) {
                var defer = $q.defer();
                var data = {};
                // data.typeIdList = values;
                data.channelId = userService.getSelChannel();
                data.productId = productId;
                // TODO 以后根据用户当前的选择
                ajaxService.ajaxPostWithData(data, cmsAction.cms_edit_product_doGetProductImages)
                    .then(function (response) {
                    defer.resolve(response.data);
                });

                return defer.promise;

            };

            /**
			 * 取得该Product的size及stock信息.
			 * 
			 * @param code
			 * @returns {*}
			 */
            this.doGetProductInventory = function (code) {
                var defer = $q.defer();
                var data = {};
                // data.typeIdList = values;
                data.channelId = userService.getSelChannel();
                data.code = code;
                // TODO 以后根据用户当前的选择
                ajaxService.ajaxPostWithData(data, cmsAction.cms_edit_product_doGetProductInventory)
                    .then(function (response) {
                    defer.resolve(response.data);
                });

                return defer.promise;

            };

            this.doGetCustomInfo = function (productId){
            	 var defer = $q.defer();
                 var data = {};
                 // data.typeIdList = values;
                 data.channelId = userService.getSelChannel();
                 data.productId = productId;
                 // TODO 以后根据用户当前的选择
                 ajaxService.ajaxPostWithData(data, cmsAction.cms_edit_product_doGetCustomInfo)
                     .then(function (response) {
                     defer.resolve(response.data);
                 });

                 return defer.promise;
            }
            
            /**
			 * 更新US Product信息.
			 * 
			 * @param usProductInfo
			 * @returns {*}
			 */
            this.doUpdateUSProductInfo = function (usProductInfo) {
                var defer = $q.defer();
                // var data = {};
                // //data.typeIdList = values;
                // data.modelId = modelId;
                // data.channelId = userService.getSelChannel ();
                // data.categoryId = categoryId;

                ajaxService.ajaxPostWithData(usProductInfo, cmsAction.cms_edit_product_doUpdateUSProductInfo)
                    .then(function (response) {
                    defer.resolve(response.data);
                });

                return defer.promise;
            };

            /**
			 * 更新CN Product信息.
			 * 
			 * @param cnBaseProductInfo
			 * @returns {*}
			 */
            this.doUpdateCNProductInfo = function (cnBaseProductInfo) {
                var defer = $q.defer();
                // var data = {};
                // //data.typeIdList = values;
                // data.modelId = modelId;
                // data.channelId = userService.getSelChannel ();
                // data.categoryId = categoryId;

                ajaxService.ajaxPostWithData(cnBaseProductInfo, cmsAction.cms_edit_product_doUpdateCNProductInfo)
                    .then(function (response) {
                    defer.resolve(response.data);
                });

                return defer.promise;
            };

            /**
			 * 更新CN TMall Product信息.
			 * 
			 * @param tmProductInfo
			 * @returns {*}
			 */
            this.doUpdateCNProductTmallInfo = function (tmProductInfo) {
                var defer = $q.defer();
                // var data = {};
                // //data.typeIdList = values;
                // data.modelId = modelId;
                // data.channelId = userService.getSelChannel ();
                // data.categoryId = categoryId;

                ajaxService.ajaxPostWithData(tmProductInfo, cmsAction.cms_edit_product_doUpdateCNProductTmallInfo)
                    .then(function (response) {
                    defer.resolve(response.data);
                });

                return defer.promise;
            };

            /**
			 * 更新CN JingDong Product信息.
			 * 
			 * @param jdProductInfo
			 * @returns {*}
			 */
            this.doUpdateCNProductJingDongInfo = function (jdProductInfo) {
                var defer = $q.defer();
                // var data = {};
                // //data.typeIdList = values;
                // data.modelId = modelId;
                // data.channelId = userService.getSelChannel ();
                // data.categoryId = categoryId;

                ajaxService.ajaxPostWithData(jdProductInfo, cmsAction.cms_edit_product_doUpdateCNProductJingDongInfo)
                    .then(function (response) {
                    defer.resolve(response.data);
                });

                return defer.promise;
            };

            /**
			 * 更新CN Price Setting信息.
			 * 
			 * @param cnPriceSettingInfo
			 * @returns {*}
			 */
            this.doUpdateProductCNPriceSettingInfo = function (cnPriceSettingInfo) {
                var defer = $q.defer();
                // var data = {};
                // //data.typeIdList = values;
                // data.modelId = modelId;
                // data.channelId = userService.getSelChannel ();
                // data.categoryId = categoryId;

                ajaxService.ajaxPostWithData(cnPriceSettingInfo, cmsAction.cms_edit_product_doUpdateProductCNPriceSettingInfo)
                    .then(function (response) {
                    defer.resolve(response.data);
                });

                return defer.promise;
            };

            /**
			 * 更新US Official Price 信息.
			 * 
			 * @param usOfficialPriceInfo
			 * @returns {*}
			 */
            this.doUpdateProductOfficialPriceInfo = function (usOfficialPriceInfo) {
                var defer = $q.defer();
                // var data = {};
                // //data.typeIdList = values;
                // data.modelId = modelId;
                // data.channelId = userService.getSelChannel ();
                // data.categoryId = categoryId;

                ajaxService.ajaxPostWithData(usOfficialPriceInfo, cmsAction.cms_edit_product_doUpdateProductOfficialPriceInfo)
                    .then(function (response) {
                    defer.resolve(response.data);
                });

                return defer.promise;
            };

            /**
			 * 更新US Price 信息.
			 * 
			 * @param usPriceInfo
			 * @returns {*}
			 */
            this.doUpdateProductUSCartPriceInfo = function (usPriceInfo) {
                var defer = $q.defer();
                // var data = {};
                // //data.typeIdList = values;
                // data.modelId = modelId;
                // data.channelId = userService.getSelChannel ();
                // data.categoryId = categoryId;

                ajaxService.ajaxPostWithData(usPriceInfo, cmsAction.cms_edit_product_doUpdateProductUSCartPriceInfo)
                    .then(function (response) {
                    defer.resolve(response.data);
                });

                return defer.promise;
            };

            /**
			 * 更新CN Price 信息.
			 * 
			 * @param cnPriceInfo
			 * @returns {*}
			 */
            this.doUpdateProductCNCartPriceInfo = function (cnPriceInfo) {
                var defer = $q.defer();
                // var data = {};
                // //data.typeIdList = values;
                // data.modelId = modelId;
                // data.channelId = userService.getSelChannel ();
                // data.categoryId = categoryId;

                ajaxService.ajaxPostWithData(cnPriceInfo, cmsAction.cms_edit_product_doUpdateProductCNCartPriceInfo)
                    .then(function (response) {
                    defer.resolve(response.data);
                });

                return defer.promise;
            };

            this.doUpdateCustemData = function (custemList) {
                var defer = $q.defer();
                // var data = {};
                // //data.typeIdList = values;
                // data.modelId = modelId;
                // data.channelId = userService.getSelChannel ();
                // data.categoryId = categoryId;

                ajaxService.ajaxPostWithData(custemList, cmsAction.cms_edit_product_doUpdateCustem)
                    .then(function (response) {
                        defer.resolve(response.data);
                    });

                return defer.promise;
            };
        }
    ]);

    cmsApp.service('editPromotionService', ['$q', 'cmsAction', 'ajaxService', 'userService',
          function ($q, cmsAction, ajaxService, userService) {

              /**
               * 取得US Promotion信息.
               *
               * @param promotionId
               * @returns {*}
               */
              this.doGetPromotionInfo = function (promotionId) {
                  var defer = $q.defer();
                  var data = {};
                  data.channelId = userService.getSelChannel();
                  data.promotionId = promotionId;
                  ajaxService.ajaxPostWithData(data, cmsAction.cms_edit_promotion_doGetPromotionInfo)
                      .then(function (response) {
                          defer.resolve(response.data);
                      });

                  return defer.promise;
              };

              /**
               * 获取SubPromotionInfo
               * @param promotionType
               * @param promotionId
               * @returns {*}
               */
              this.doGetSubPromotionInfo = function (promotionType, promotionId) {
                  var defer = $q.defer();
                  var data = {};
                  data.channelId = userService.getSelChannel();
                  data.promotionType = promotionType;
                  data.promotionId = promotionId;
                  ajaxService.ajaxPostWithData(data, cmsAction.cms_edit_promotion_doGetSubPromotionInfo)
                      .then(function (response) {
                          defer.resolve(response.data);
                      });

                  return defer.promise;
              };

              /**
               * 获取SubPromotionProductInfo
               */
              this.doGetSubPromotionProductInfo = function (data,promotionId) {
                  var defer = $q.defer();
                  data.param = {
                          channelId: userService.getSelChannel(),
                          promotionId: promotionId
                      };
                  
                  ajaxService.ajaxPostWithData(data, cmsAction.cms_edit_promotion_doGetSubPromotionProductInfo)
                      .then(function (response) {
                          defer.resolve(response.data);
                      });

                  return defer.promise;
              };
              /**
               * 更新Promotion
               */
              this.doUpdatePromotionInfo = function (promotion) {
                  var defer = $q.defer();

                  ajaxService.ajaxPostWithData(promotion, cmsAction.cms_edit_promotion_doUpdatePromotionInfo)
                      .then(function (response) {
                          defer.resolve(response.data);
                      });

                  return defer.promise;
              };
              /**
               * 批量删除PromotionProduct
               */
              this.doDeletePromotionProduct = function (promotion) {
                  var defer = $q.defer();

                  ajaxService.ajaxPostWithData(promotion, cmsAction.cms_edit_promotion_discount_doDeletePromotionProduct)
                      .then(function (response) {
                          defer.resolve(response.data);
                      });

                  return defer.promise;
              };
          }
      ]);

    cmsApp.service('editMainCategoryService', ['$q', '$location', 'cmsAction', 'ajaxService', 'userService', 'mainCategoryService',
        function ($q, $location, cmsAction, ajaxService, userService, mainCategoryService) {

            this.doUpdateMainCategoryId = function (data,isLocation) {
                var defer = $q.defer();

                ajaxService.ajaxPostWithData(data, cmsAction.cms_common_edit_doUpdateMainCategory)
                    .then(function (response) {
                        // defer.resolve(response.data);
                    	if(isLocation){
                    		$location.path (mainCategoryService.gettMainCategoryReturnUrl());
                    	}else{
                    		defer.resolve(response.data);
                    	}
                        
                    });

                return defer.promise;
            };
        }
    ]);
});
