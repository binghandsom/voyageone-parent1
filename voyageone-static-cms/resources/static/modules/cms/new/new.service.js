/**
 * @Name:    new.service.js
 * @Date:    2015/7/16
 *
 * @User:    Edward
 * @Version: 1.0.0
 */


define (function(require) {
    var cmsApp = require('modules/cms/cms.module');

    cmsApp.service ('newCategoryService', ['$q', 'cmsAction', 'ajaxService', 'userService',
        function ($q, cmsAction, ajaxService, userService) {

            /**
             * 新建一个Category，并返回该Category的ID.
             * @param parentCategoryId
             * @param usCategoryInfo
             * @param cnCategoryInfo
             * @param cnPriceSettingInfo
             * @returns {*}
             */
            this.doSaveCategoryInfo = function (parentCategoryId, usCategoryInfo, cnCategoryInfo, cnPriceSettingInfo) {
                var defer = $q.defer();
                var data = {};
                data.parentCategoryId = parentCategoryId;
                data.channelId = userService.getSelChannel();
                data.usCategoryInfo = usCategoryInfo;
                data.cnCategoryInfo = cnCategoryInfo;
                data.cnPriceSettingInfo = cnPriceSettingInfo;

                ajaxService.ajaxPostWithData(data, cmsAction.cms_new_category_doSaveCategoryInfo)
                    .then(function(response) {
                        defer.resolve(response.data);
                    });

                return defer.promise;
            }
        }
    ]);

    cmsApp.service ('newModelService', ['$q', 'cmsAction', 'ajaxService', 'userService',
        function ($q, cmsAction, ajaxService, userService) {

            /**
             * 新建一个Model，并返回该Model的ID.
             * @param parentCategoryId
             * @param usModelInfo
             * @param cnModelInfo
             * @param cnPriceSettingInfo
             * @returns {*}
             */
            this.doSaveModelInfo = function (parentCategoryId, usModelInfo, cnModelInfo, cnPriceSettingInfo) {
                var defer = $q.defer();
                var data = {};
                data.parentCategoryId = parentCategoryId;
                data.channelId = userService.getSelChannel();
                data.usModelInfo = usModelInfo;
                data.cnModelInfo = cnModelInfo;
                data.cnPriceSettingInfo = cnPriceSettingInfo;

                ajaxService.ajaxPostWithData(data, cmsAction.cms_new_model_doSaveModelInfo)
                    .then(function(response) {
                        defer.resolve(response.data);
                    });

                return defer.promise;
            }
        }
    ]);
    cmsApp.service ('newPromotionService', ['$q', 'cmsAction', 'ajaxService', 'userService',
        function ($q, cmsAction, ajaxService, userService) {

            /**
             * 新建一个Promotion
             * @returns {*}
             */
            this.doSavePromotionInfo = function (promotionInfo) {
                var defer = $q.defer();
                var data = {};
               // data.channelId = userService.getSelChannel();
                //data.promotionInfo = promotionInfo;
                promotionInfo.isEffective = true;
                if(promotionInfo.isSelectionOver ==null){
                	promotionInfo.isSelectionOver = false;
                }
                if(promotionInfo.isSignUpOver ==null){
                	promotionInfo.isSignUpOver = false;
                }
                if(promotionInfo.isIsolationStockOver ==null){
                	promotionInfo.isIsolationStockOver = false;
                }
                if(promotionInfo.isActivityOver ==null){
                	promotionInfo.isActivityOver = false;
                }
                ajaxService.ajaxPostWithData(promotionInfo, cmsAction.cms_new_promotion_doSavePromotionInfo)
                    .then(function(response) {
                        defer.resolve(response.data);
                    });

                return defer.promise;
            }
        }
    ]);
});