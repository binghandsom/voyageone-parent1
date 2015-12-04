/**
 * @Name:    category.ctl.js
 * @Date:    2015/7/16
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

    cmsApp.controller ('newCategoryController', ['$scope', '$q', '$location', '$routeParams', 'cmsCommonService', 'editCategoryService', 'newCategoryService', 'cmsRoute',
        function ($scope, $q, $location, $routeParams, commonService, categoryService, newCategoryService, cmsRoute) {

            var _ = require ('underscore');
            var commonUtil = require ('components/util/commonUtil');

            $scope.parentCategoryId = "";
            $scope.mainCategoryId = "";
            $scope.parentCategoryUrlKey = "";
            $scope.usCategoryInfo = {};
            $scope.cnCategoryInfo = {};
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
            $scope.doSaveNewCategoryInfo = function () {

                // 如果US和CN的任意一方Name为空，则替换成不为空的一方
                if (_.isEmpty ($scope.usCategoryInfo.name)) {
                    $scope.usCategoryInfo.name = $scope.cnCategoryInfo.cnName;
                } else if (_.isEmpty ($scope.cnCategoryInfo.cnName)) {
                    $scope.cnCategoryInfo.cnName = $scope.usCategoryInfo.name;
                }

                // 赋值mainCategoryId到US CN Category信息表.
                $scope.usCategoryInfo.mainCategoryId = $scope.mainCategoryId;
                $scope.cnCategoryInfo.mainCategoryId = $scope.mainCategoryId;

                //if ($scope.formNewUSCategoryInfo.$valid
                //    && $scope.formCNCategoryInfo.$valid
                //    && $scope.formCNPriceSettingInfo.$valid) {

                newCategoryService.doSaveCategoryInfo ($scope.parentCategoryId, $scope.usCategoryInfo, $scope.cnCategoryInfo, $scope.cnPriceSettingInfo)
                    .then (function (data) {
                    if (!_.isEmpty (data.categoryId)) {
                        $location.path (commonUtil.returnReallyPath (cmsRoute.cms_edit_category.hash, data.categoryId))
                    }
                    // TODO 如果执行 成功但没有返回category Id 如何处理
                });
                //}
            };

            /**
             * 取消对New Category的修改编辑.
             */
            $scope.undoSaveNewCategoryInfo = function () {
                $scope.usCategoryInfo = angular.copy ($scope.oldUsCategoryInfo);
                $scope.cnCategoryInfo = angular.copy ($scope.oldCnCategoryInfo);
                $scope.cnPriceSettingInfo = angular.copy ($scope.oldCnPriceSettingInfo);
            };

            /**
             * 设置Url Key当美国名称发生变化时.
             */
            $scope.resetDefaultUrlKey = function () {
                $scope.usCategoryInfo.urlKey = $scope.parentCategoryUrlKey + '-' + commonUtil.replaceSpaceToHyphen (commonUtil.replaceSpecialChar ($scope.usCategoryInfo.name, ''));
                $scope.cnCategoryInfo.urlKey = $scope.usCategoryInfo.urlKey;
            };

            /**
             * 当一方的URL Key发生变化时调用
             * @param type
             */
            $scope.changeUrlKey = function (type) {

                switch (type) {
                    case "cn":
                        $scope.usCategoryInfo.urlKey = $scope.cnCategoryInfo.urlKey;
                        break;
                    case "us":
                        $scope.cnCategoryInfo.urlKey = $scope.usCategoryInfo.urlKey;
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

            /** US Category Tab的操作 Start **/
            /**
             * 清空Main Category选择内容.
             */
            $scope.clearUSMainCategoryId = function () {
                $scope.mainCategoryId = '';
            };

            /**
             * 清空Amazon Category选择内容.
             */
            $scope.clearAmazonBrowseCategoryId = function () {
                $scope.usCategoryInfo.amazonBrowseCategoryId = '';
            };

            /**
             * 清空Google Category选择内容.
             */
            $scope.clearGoogleCategoryId = function () {
                $scope.usCategoryInfo.googleCategoryId = '';
            };

            /**
             * 清空PriceGrabber Category选择内容.
             */
            $scope.clearPriceGrabberCategoryId = function () {
                $scope.usCategoryInfo.priceGrabberCategoryId = '';
            };
            /** US Category Tab的操作 End **/

            /** CN Category Tab的操作 Start **/
            /**
             * 清空HS Code SH选择内容.
             */
            $scope.clearHsCodeId = function () {
                $scope.cnCategoryInfo.hsCodeId = '';
            };

            /**
             * 清空HS Code GZ选择内容.
             */
            $scope.clearHsCodePuId = function () {
                $scope.cnCategoryInfo.hsCodePuId = '';
            };

            /**
             * 清空Main Category选择内容.
             */
            $scope.clearCNMainCategoryId = function () {
                $scope.mainCategoryId = '';
            };

            /**
             * 清空Size Chart选择内容.
             */
            $scope.clearSizeChartId = function () {
                $scope.cnCategoryInfo.sizeChartId = '';
            };
            /** CN Category Tab的操作 End **/


            /** CN Category PriceSetting Tab的操作 Start **/
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
            /** CN Category PriceSetting Tab的操作 End **/
            /**
             * 取得US Category信息.
             */
            function doGetUSCategoryInfo () {
                categoryService.doGetUSCategoryInfo ($scope.currentCategoryId)
                    .then (function (data) {
                    $scope.usCategoryInfo = data;
                    $scope.parentCategoryUrlKey = $scope.usCategoryInfo.urlKey;
                    $scope.usCategoryInfo.categoryId = "";
                    $scope.usCategoryInfo.urlKey = "";
                    $scope.usCategoryInfo.name = "";
                    $scope.usCategoryInfo.headerTitle = "";
                    $scope.usCategoryInfo.isEffective = true;
                    $scope.usCategoryInfo.isEnableFilter = false;
                    $scope.usCategoryInfo.isVisibleOnMenu = false;
                    $scope.usCategoryInfo.isPublished = false;
                    $scope.usCategoryInfo.modified = "";
                    $scope.mainCategoryId = $scope.usCategoryInfo.mainCategoryId;
                    $scope.oldUsCategoryInfo = angular.copy ($scope.usCategoryInfo);
                });
            }

            /**
             * 取得CN Category信息.
             */
            function doGetCNCategoryInfo () {
                categoryService.doGetCNCategoryInfo ($scope.currentCategoryId)
                    .then (function (data) {
                    $scope.cnCategoryInfo = data;
                    $scope.cnCategoryInfo.categoryId = "";
                    $scope.cnCategoryInfo.urlKey = "";
                    $scope.cnCategoryInfo.cnName = "";
                    $scope.cnCategoryInfo.cnHeaderTitle = "";
                    $scope.cnCategoryInfo.cnIsEnableFilter = false;
                    $scope.cnCategoryInfo.cnIsVisibleOnMenu = false;
                    $scope.cnCategoryInfo.cnIsPublished = false;
                    $scope.cnCategoryInfo.modified = "";
                    $scope.oldCnCategoryInfo = angular.copy ($scope.cnCategoryInfo);
                });
            }

            /**
             * 取得CN Category PriceSetting信息.
             */
            function doGetCategoryCNPriceSettingInfo () {
                categoryService.doGetCategoryCNPriceSettingInfo ($scope.currentCategoryId)
                    .then (function (data) {
                    $scope.cnPriceSettingInfo = data;
                    $scope.cnPriceSettingInfo.categoryId = "";
                    $scope.cnPriceSettingInfo.modified = "";
                    $scope.oldCnPriceSettingInfo = angular.copy ($scope.cnPriceSettingInfo);
                });
            }
        }]);
});