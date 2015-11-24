/**
 * @Name:    app.module.js
 * @Date:    2015/4/30
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

    var mainApp = require ('components/app');

    mainApp.constant ('commonAction', {
        'common_doLogout': '/core/account/login/doLogout',
        'common_doGetUserInfo': '/core/account/login/doGetUserInfo',
        'common_doGetCompany': '/core/account/company/doGetCompany',
        'common_doSelectCompany': '/core/account/company/doSelectCompany',
        'common_doChgPassword': '/core/setting/user/doChgPassword',
        'cms_common_master_doGetMasterDataList': '/cms/common/master/doGetMasterDataList',
        'cms_common_service_doGetPromotionList': '/cms/common/service/doGetPromotionList',
        'core_account_company_doSelectCompany': '/core/account/company/doSelectCompany'
    });

    /**
     * 设定需要在sessionStorage中保存的key.
     */
    mainApp.constant("sessionStorageType", {
        CMS_MASTER_DATA: "cms.masterData",
        //
        CMS_MAIN_CATEGORY_SHOW_PARAM: "cms.mainCategoryShowParam",
        CMS_MAIN_CATEGORY_RETURN_URL: "cms.mainCategoryReturnUrl"
    });

    /**
     * 画面显示那些System Menu.
     */
    mainApp.constant("mainMenu",{
        CMS: 'CMS',
        OMS: 'OMS',
        WMS: 'WMS',
        CORE: 'CORE'
    });

    /**
     * all sale channels.
     */
    mainApp.constant ("cartId", {
        "US_OFFICIAL": "6",
        "US_AMAZON": "5",
        "CN_ALL": "79",
        "CN_OFFICIAL": "25",
        "CN_TB": "21",
        "CN_TM": "20",
        "CN_TG": "23",
        "CN_JD": "24",
        "CN_JG": "26"
    });

    mainApp.constant ("JQ_CONFIG", {
        screenfull: ['libs/jquery/screenfull/dist/screenfull.min.js'],
        chosen: ['/libs/jquery/chosen/chosen.jquery.min.js'],
        slimScroll: ['libs/jquery/slimscroll/jquery.slimscroll.min.js']
    });

    /**
     * 设置页面显示时默认显示哪个地区的数据.
     */
    mainApp.constant("systemCountry", {
        US : 'us',
        CN : 'cn'
    });

    mainApp.controller("navigationController", ['$scope', 'cmsRoute', '$location',
        function($scope, cmsRoute, $location) {

            var commonUtil = require('components/util/commonUtil');
            var commonUrlHeard = '#';

            /** 画面跳转 start **/
            /**
             * 跳转到Category页面.
             * @param categoryId
             */
            $scope.goCategoryPage = function (categoryId) {

                if (commonUtil.isNotEmpty(categoryId)) {
                    return commonUrlHeard + commonUtil.returnReallyPath(cmsRoute.cms_edit_category.hash, categoryId);
                }
            };

            /**
             * 跳转到Category页面（button使用）
             * @param categoryId
             */
            $scope.goCategoryPageForBtn = function (categoryId) {

                if (commonUtil.isNotEmpty(categoryId)) {
                    $location.path(commonUtil.returnReallyPath(cmsRoute.cms_edit_category.hash, categoryId));
                }
            };

            /**
             * 跳转到Model页面.
             * @param categoryId
             * @param modelId
             */
            $scope.goModelPage = function (categoryId, modelId) {

                if (commonUtil.isNotEmpty(categoryId) && commonUtil.isNotEmpty(modelId)) {
                    return commonUrlHeard + commonUtil.returnReallyPathByMoreParam(cmsRoute.cms_edit_model.hash, [categoryId, modelId]);
                }
            };

            /**
             * 跳转到Product页面.
             * @param categoryId
             * @param modelId
             * @param productId
             */
            $scope.goProductPage = function (categoryId, modelId, productId) {

                if (commonUtil.isNotEmpty(categoryId) && commonUtil.isNotEmpty(modelId) && commonUtil.isNotEmpty(productId)) {
                    return commonUrlHeard + commonUtil.returnReallyPathByMoreParam(cmsRoute.cms_edit_product.hash, [categoryId, modelId, productId]);
                }
            };

            /**
             * 跳转到Promotion页面
             * @param promotionId
             */
            $scope.goPromotionPage = function (promotionTypeId, promotionId) {

                if (commonUtil.isNotEmpty(promotionId) && commonUtil.isNotEmpty(promotionTypeId)) {
                    return commonUrlHeard + commonUtil.returnReallyPathByMoreParam(cmsRoute.cms_edit_promotion.hash, [promotionTypeId, promotionId]);
                }
            };

            /**
             * 跳转到Promotion页面.(用于button)
             * @param promotionId
             */
            $scope.goPromotionPageForBtn = function (promotionTypeId, promotionId) {

                if (commonUtil.isNotEmpty(promotionId) && commonUtil.isNotEmpty(promotionTypeId)) {
                    $location.path(commonUtil.returnReallyPathByMoreParam(cmsRoute.cms_edit_promotion.hash, [promotionTypeId, promotionId]));
                }
            };
        }]);

    mainApp.controller ('selectController',['$scope', function ($scope) {

        var commonUtil = require('components/util/commonUtil');

        /**
         * 将当前页的所有数据全部选中，或者全部取消选中.(用于datatable的后端分页)
         * @param objectList
         */
        $scope.selectAll = function (objectList) {

            // 循环处理全选中的数据
            _.forEach(objectList.currentPageIdList, function (object) {
                if(objectList.selectOneFlagList.hasOwnProperty(object.id)) {

                    // 单签页面所有产品选中flag被标示
                    objectList.selectOneFlagList[object.id] = objectList.selectAllFlag;

                    if (objectList.hasOwnProperty('selectIdList')) {
                        if (objectList.selectAllFlag && _.indexOf(objectList.selectIdList, object.id) < 0) {
                            objectList.selectIdList.push(object.id);
                        } else if (!objectList.selectAllFlag && _.indexOf(objectList.selectIdList, object.id) > -1) {
                            objectList.selectIdList.splice(_.indexOf(objectList.selectIdList, object.id), 1);
                        }
                    }
                }
            });

            if (objectList.hasOwnProperty('selectDataList')) {

                _.forEach(objectList.currentPageDataList, function (object) {
                    if (objectList.selectAllFlag && _.indexOf(objectList.selectDataList, object) < 0) {
                        objectList.selectDataList.push(object);
                    } else if (!objectList.selectAllFlag && _.indexOf(objectList.selectDataList, object) > -1) {
                        objectList.selectDataList.splice(_.indexOf(objectList.selectDataList, object), 1);
                    }
                });
            }

        };

        /**
         * 选中当前数据，并将当前数据的Id添加到对应List中，
         * 或者取消选中该数据，并将其数据的Id从List中删除.currentId.(用于datatable的后端分页)
         * @param currentId
         * @param objectList
         */
        $scope.selectOne = function (currentId, objectList) {
            currentId = parseInt(currentId);
            if (objectList.hasOwnProperty('selectIdList')) {
                if (_.indexOf(objectList.selectIdList, currentId) > -1) {
                    objectList.selectIdList.splice(_.indexOf(objectList.selectIdList, currentId), 1);
                } else {
                    objectList.selectIdList.push(currentId);
                }
            }

            if (objectList.hasOwnProperty('selectDataList')) {

                _.forEach(objectList.currentPageDataList, function(object) {

                    if (_.isEqual(object.id, currentId)) {
                        if (_.indexOf(objectList.selectDataList, object) > -1) {
                            objectList.selectDataList.splice(_.indexOf(objectList.selectDataList, object), 1);
                        } else {
                            objectList.selectDataList.push(object);
                        }
                    }
                });
            }

            objectList.selectAllFlag = true;
            _.forEach(objectList.currentPageIdList, function(object) {
                if (!objectList.selectOneFlagList[object.id]) {
                    objectList.selectAllFlag = false;
                }
            })
        };

        /**
         * 选中单个
         * @param currentId
         * @param objectList
         */
        $scope.selectOneByCheckBox = function (currentId, objectList) {
            currentId = parseInt(currentId);
            if (objectList.hasOwnProperty('selectIdList')) {
                if (_.indexOf(objectList.selectIdList, currentId) > -1) {
                    objectList.selectIdList.splice(_.indexOf(objectList.selectIdList, currentId), 1);
                } else {
                    objectList.selectIdList.push(currentId);
                }
            }

            if (objectList.hasOwnProperty('selectDataList')) {

                _.forEach(objectList.selectDataList, function(object) {

                    if (_.isEqual(object.id, currentId)) {
                        if (_.indexOf(objectList.selectDataList, object) > -1) {
                            objectList.selectDataList.splice(_.indexOf(objectList.selectDataList, object), 1);
                        } else {
                            objectList.selectDataList.push(object);
                        }
                    }
                });
            }
        };
    }]);

    mainApp.filter('brDateFilter', function() {
        return function(dateSTR) {
            if(!dateSTR){
                return '';
            }
            dateSTR = dateSTR.substr(0, 4) + '/' + dateSTR.substr(4,2);
            return Date.parse(dateSTR); // No TZ subtraction on this sample
        };
    });
    return mainApp;
});
