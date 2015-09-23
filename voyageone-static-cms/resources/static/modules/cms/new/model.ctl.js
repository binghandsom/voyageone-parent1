/**
 * @Name:    model.ctl.js
 * @Date:    2015/7/21
 *
 * @User:    Edward
 * @Version: 1.0.0
 */

define (function (require) {
    var cmsApp = require ('modules/cms/cms.module');
    require ('modules/cms/edit/edit.service');
    require ('modules/cms/new/new.service');
    require ('modules/cms/common/common.service');
//    require('modules/oms/directives/tab.directive');
//    require('modules/oms/customer/customerDetail.service');
//    require('modules/oms/customer/customer.service');
//    require('modules/oms/directives/selectLine.directive');
//    require('modules/oms/services/orderDetailInfo.service');
//    require('modules/oms/services/addNewOrder.service');
//    require('modules/oms/popup/popAddNoteCustomer.ctl');

    cmsApp.controller ('newModelController', ['$scope', '$q', '$location', '$routeParams', 'cmsCommonService', 'editCategoryService', 'newModelService', 'cmsRoute',
        function ($scope, $q, $location, $routeParams, commonService, categoryService, newModelService, cmsRoute) {

            var _ = require ('underscore');
            var commonUtil = require ('components/util/commonUtil');

            $scope.parentCategoryId = "";
            $scope.mainCategoryId = "";
            $scope.parentCategoryUrlKey = "";
            $scope.usModelInfo = {};
            $scope.cnBaseModelInfo = {};
            $scope.tmModelInfo = {};
            $scope.jdModelInfo = {};
            $scope.cnPriceSettingInfo = {};

            /**
             * 初始化操作.
             */
            $scope.initialize = function () {

                if (!_.isEmpty ($routeParams.categoryId)) {
                    $scope.parentCategoryId = $routeParams.categoryId;
                    doGetUSCategoryInfo ();
                    doGetCNCategoryInfo ();
                    doGetCategoryCNPriceSettingInfo ();
                }
            };

            /**
             * 如果Parent Category发生变更，则重新迁移到新的
             */
            $scope.changedParentCategory = function () {
                if (_.isEmpty ($scope.usCategoryInfo)
                    && !_.isEmpty ($scope.parentCategoryId)) {
                    doGetUSCategoryInfo ();
                    doGetCNCategoryInfo ();
                    doGetCategoryCNPriceSettingInfo ();
                }
            };

            /**
             * 创建New Category信息,如果创建成功跳转到对应的画面中.
             */
            $scope.doSaveNewModelInfo = function () {

                // 如果US和CN的任意一方Name为空，则替换成不为空的一方
                if (_.isEmpty ($scope.usModelInfo.name)) {
                    $scope.usModelInfo.name = $scope.cnBaseModelInfo.cnName;
                } else if (_.isEmpty ($scope.cnBaseModelInfo.cnName)) {
                    $scope.cnBaseModelInfo.cnName = $scope.usModelInfo.name;
                }

                // 如果US和CN的任意一方size type为空，则替换成不为空的一方
                if (_.isEmpty ($scope.usModelInfo.sizeTypeId)) {
                    $scope.usModelInfo.sizeTypeId = $scope.cnBaseModelInfo.cnSizeTypeId;
                } else if (_.isEmpty ($scope.cnBaseModelInfo.cnSizeTypeId)) {
                    $scope.cnBaseModelInfo.cnSizeTypeId = $scope.usModelInfo.sizeTypeId;
                }

                // 赋值mainCategoryId到US CN Category信息表.
                $scope.usModelInfo.mainCategoryId = $scope.mainCategoryId;
                $scope.cnBaseModelInfo.mainCategoryId = $scope.mainCategoryId;

                // 将所有的cn model信息合并成一个object.
                var cnModelInfo = {};
                cnModelInfo.cnBaseModelInfo = $scope.cnBaseModelInfo;
                //cnModelInfo.tmModelInfo = $scope.tmModelInfo;
                //cnModelInfo.jdModelInfo = $scope.jdModelInfo;

                newModelService.doSaveModelInfo ($scope.parentCategoryId, $scope.usModelInfo, cnModelInfo, $scope.cnPriceSettingInfo)
                    .then (function (data) {
                    if (!_.isEmpty (data.modelId)) {
                        $location.path (commonUtil.returnReallyPath (cmsRoute.cms_edit_model.hash, data.modelId))
                    }
                    // TODO 如果执行 成功但没有返回category Id 如何处理
                });
            };

            /**
             * 取消对New Category的修改编辑.
             */
            $scope.undoSaveNewModelInfo = function () {
                $scope.usModelInfo = angular.copy ($scope.oldUsModelInfo);
                $scope.cnBaseModelInfo = angular.copy ($scope.oldCnBaseModelInfo);
                //$scope.tmModelInfo = angular.copy ($scope.oldCnModelInfo);
                //$scope.jdModelInfo = angular.copy ($scope.oldCnModelInfo);
                $scope.cnPriceSettingInfo = angular.copy ($scope.oldCnPriceSettingInfo);
            };

            /**
             * 设置Url Key当美国名称发生变化时.
             */
            $scope.resetDefaultUrlKey = function () {
                $scope.usModelInfo.urlKey = $scope.parentCategoryUrlKey + '-' + commonUtil.replaceSpaceToHyphen (commonUtil.replaceSpecialChar ($scope.usModelInfo.model, ''));
                $scope.cnBaseModelInfo.urlKey = $scope.usModelInfo.urlKey;
            };

            /**
             * 当一方的URL Key发生变化时调用
             * @param type
             */
            $scope.changeUrlKey = function (type) {

                switch (type) {
                    case "cn":
                        $scope.usModelInfo.urlKey = $scope.cnBaseModelInfo.urlKey;
                        break;
                    case "us":
                        $scope.cnBaseModelInfo.urlKey = $scope.usModelInfo.urlKey;
                        break;
                    default:
                        break;
                }
            };

            /**
             * 检测画面Form内容的有效性.
             * @returns {boolean|FormController.$valid|*|ngModel.NgModelController.$valid|context.ctrl.$valid|rd.$valid}
             */
            //$scope.checkFormIsValid = function () {
            //    $scope.formValid = $scope.formUSCategoryInfo.$valid && $scope.formCNCategoryInfo.$valid && $scope.formCNPriceSettingInfo.$valid;
            //};

            /** US Model Tab的操作 Start **/

            /**
             * 清除US Model's productTypeId
             */
            $scope.clearUSProductTypeId = function () {
                $scope.usModelInfo.productTypeId = "";
            };

            /**
             * 清除US Model's brandId
             */
            $scope.clearUSBrandId = function () {
                $scope.usModelInfo.brandId = "";
            };

            /**
             * 清除US Model's sizeTypeId
             */
            $scope.clearUSSizeTypeId = function () {
                $scope.usModelInfo.sizeTypeId = "";
            };

            /**
             * 清除US Model's amazonBrowseCategoryId
             */
            $scope.clearAmazonBrowseCategoryId = function () {
                $scope.usModelInfo.amazonBrowseCategoryId = "";
            };

            /**
             * 清除US Model's googleCategoryId
             */
            $scope.clearGoogleCategoryId = function () {
                $scope.usModelInfo.googleCategoryId = "";
            };

            /**
             * 清除US Model's priceGrabberCategoryId
             */
            $scope.clearPriceGrabberCategoryId = function () {
                $scope.usModelInfo.priceGrabberCategoryId = "";
            };
            /** US Model Tab的操作 End **/

            /** CN Model Tab的操作 Start **/
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
             * 清除CN Model's sizeChartId
             */
            $scope.clearSizeChartId = function () {
                $scope.cnBaseModelInfo.sizeChartId = "";
                $scope.cnModelChanged ();
            };
            /** CN Model Tab的操作 End **/

            /**
             * 检查CN Model PriceSetting信息是否发生变化.
            /**
             * 清空basePriceId选择内容.
             */
            $scope.clearBasePriceId = function () {
                $scope.cnPriceSettingInfo.basePriceId = '';
            };

            /**
             * 清空overHeard1选择内容.
             */
            $scope.clearOverHeard1 = function () {
                $scope.cnPriceSettingInfo.overHeard1 = '0.00';
            };

            /**
             * 清空overHeard2选择内容.
             */
            $scope.clearOverHeard2 = function () {
                $scope.cnPriceSettingInfo.overHeard2 = '0.00';
            };
            /** CN PriceSetting信息操作 End **/

            /**
             * 取得US Category信息.
             */
            function doGetUSCategoryInfo () {
                categoryService.doGetUSCategoryInfo ($scope.currentCategoryId)
                    .then (function (data) {
                    $scope.usModelInfo = {};
                    $scope.usModelInfo.modelId = "";
                    $scope.usModelInfo.channelId = data.channelId;
                    $scope.usModelInfo.seoTitle = data.seoTitle;
                    $scope.usModelInfo.seoDescription = data.seoDescription;
                    $scope.usModelInfo.seoKeywords = data.seoKeywords;
                    $scope.parentCategoryUrlKey = data.urlKey;
                    //$scope.mainCategoryId = data.mainCategoryId;
                    $scope.oldUsModelInfo = angular.copy ($scope.usModelInfo);
                });
            }

            /**
             * 取得CN Category信息.
             */
            function doGetCNCategoryInfo () {
                categoryService.doGetCNCategoryInfo ($scope.currentCategoryId)
                    .then (function (data) {
                    $scope.cnBaseModelInfo = {};
                    $scope.cnBaseModelInfo.modelId = "";
                    $scope.cnBaseModelInfo.channelId = data.channelId;
                    $scope.cnBaseModelInfo.cnSeoTitle = data.cnSeoTitle;
                    $scope.cnBaseModelInfo.cnSeoDescription = data.cnSeoDescription;
                    $scope.cnBaseModelInfo.cnSeoKeywords = data.cnSeoKeywords;
                    $scope.cnBaseModelInfo.hsCodeId = data.hsCodeId;
                    $scope.cnBaseModelInfo.hsCodePuId = data.hsCodePuId;
                    //$scope.mainCategoryId = data.mainCategoryId;
                    $scope.oldCnBaseModelInfo = angular.copy ($scope.cnBaseModelInfo);
                });
            }

            /**
             * 取得CN Category PriceSetting信息.
             */
            function doGetCategoryCNPriceSettingInfo () {
                categoryService.doGetCategoryCNPriceSettingInfo ($scope.currentCategoryId)
                    .then (function (data) {
                    $scope.cnPriceSettingInfo = data;
                    $scope.cnPriceSettingInfo.modelId = "";
                    $scope.cnPriceSettingInfo.categoryId = "";
                    $scope.cnPriceSettingInfo.modified = "";
                    $scope.oldCnPriceSettingInfo = angular.copy ($scope.cnPriceSettingInfo);
                });
            }
        }]);
});