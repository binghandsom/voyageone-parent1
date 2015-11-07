/**
 * @Name:    promotion.ctl.js
 * @Date:    2015/8/13
 *
 * @User:    Eric
 * @Version: 1.0.0
 */

define(function (require) {
    var cmsApp = require('modules/cms/cms.module');
    require('modules/cms/edit/edit.service');
    require('modules/cms/common/common.service');
    require('modules/cms/popup/promotionDiscount/popPromotionDiscount.ctl');
    require('modules/cms/popup/addToPromotion/popAddToPromotion.ctl');
    //require ('modules/cms/popup/popPromotionDiscount.ctl');
    //require ('modules/cms/directives/popAddToPromotion/popPromotionDiscount');
//    require('modules/oms/directives/tab.directive');
//    require('modules/oms/customer/customerDetail.service');
//    require('modules/oms/customer/customer.service');
//    require('modules/oms/directives/selectLine.directive');
//    require('modules/oms/services/orderDetailInfo.service');
//    require('modules/oms/services/addNewOrder.service');
//    require('modules/oms/popup/popAddNoteCustomer.ctl');


    cmsApp.controller('editPromotionController', ['$scope', '$rootScope', '$q', '$location', '$routeParams', 'cmsCommonService', 'editPromotionService', 'cartId', 'userService','ngDialog', 'cmsPopupPages', 'cmsRoute','notify','$modal',
                                                  'DTOptionsBuilder', 'DTColumnBuilder','$translate', '$compile', 'vConfirm',
        function ($scope, $rootScope, $q, $location, $routeParams, commonService, promotionService, cartId, userService, ngDialog, cmsPopupPages, cmsRoute,notify,$modal,DTOptionsBuilder, DTColumnBuilder,$translate, $compile, vConfirm) {

            var _ = require('underscore');
            var commonUtil = require('components/util/commonUtil');

            $scope.promotionName = '';
            $scope.status = {};
            //$scope.PromotionProductListData = {};
            //$scope.selectedPromotionProductList = [];
            /**
             * 初始化操作.
             */
            $scope.initialize = function () {
                 
                if (!isNaN(parseInt($routeParams.promotionId))) {
                	$scope.promotionId = $routeParams.promotionId;
                    $scope.promotionType = $routeParams.promotionType;

                    // 初始化Us Product 选中信息.
                    $scope.usProductInfo = {
                        selectAllFlag: false,
                        selectOneFlagList: [],
                        selectIdList: [],
                        currentPageIdList: []
                    };

                    if (_.isEqual($routeParams.promotionType, '3')) {
                        doGetPromotionInfo();
                        doGetPromotionProductDataTable();
                    } else if (_.isEqual($routeParams.promotionType, '1')
                        || _.isEqual($routeParams.promotionType, '2')){
                        $scope.promotionName = $routeParams.promotionId;
                        doGetSubPromotionInfo();
                    }
                }
            };

            /**
             * 跳转到New Promotion页面.
             */
            $scope.doAddPromotion = function () {
                $location.path(cmsRoute.cms_new_promotion);
            };

            /**
             * 判断Promotion是否发生变更.
             */
            $scope.promotionChanged = function () {
                $scope.prmotionIsChanged = !_.isEqual($scope.promotionInfo, $scope.oldPromotionInfo);
            };

            /**
             * 清除选中的cartId.
             */
            $scope.clearCartId = function () {
                $scope.promotionInfo.cartId = '';
                $scope.promotionChanged();
            };

            /**
             * 执行Effective处理.
             */
            $scope.doUpdateEffective = function () {
                vConfirm('CORE_TXT_MESSAGE_CHANGE_TO_EFFECTIVE','').result.then(function() {
                    var isEffective = $scope.promotionInfo.isEffective;
                    $scope.undoUpdatePromotionInfo();
                    $scope.promotionInfo.isEffective = isEffective;
                    $scope.doUpdatePromotionInfo();
                }, function() {
                    $scope.promotionInfo.isEffective = !$scope.promotionInfo.isEffective;
                })
            };

            /**
             * 撤销画面的编辑.
             */
            $scope.undoUpdatePromotionInfo = function () {
                $scope.promotionInfo = angular.copy($scope.oldPromotionInfo);
                $scope.prmotionIsChanged = false;
            };

            /**
             * 更新Promotion信息.
             */
            $scope.doUpdatePromotionInfo = function ()  {
                var promotionInfo = angular.copy($scope.promotionInfo);
                promotionInfo.preheatDateStart = commonUtil.doFormatDate(promotionInfo.preheatDateStart);
                promotionInfo.preheatDateEnd = commonUtil.doFormatDate(promotionInfo.preheatDateEnd);
                promotionInfo.effectiveDateStart = commonUtil.doFormatDate(promotionInfo.effectiveDateStart);
                promotionInfo.effectiveDateEnd = commonUtil.doFormatDate(promotionInfo.effectiveDateEnd);
                promotionService.doUpdatePromotionInfo(promotionInfo)
                    .then (function () {
                    notify.success("CMS_TXT_MSG_UPDATE_SUCCESS");
                    $scope.prmotionIsChanged = false;
                    doGetPromotionInfo();
                }, function () {
                    notify.danger("CMS_TXT_MSG_UPDATE_FAILED");
                    doGetPromotionInfo();
                });
            };

            /**
             * 删除被选中的数据.
             */
            $scope.doDeletePromotionProduct = function () {
                if (!$scope.promotionInfo.isActivityOver && $scope.promotionInfo.isEffective) {
                    if ($scope.usProductInfo.selectIdList.length) {
                        var data = {productIdList:$scope.usProductInfo.selectIdList , promotionId:$scope.promotionId}
                        vConfirm('CORE_TXT_MESSAGE_BATCH_DELETE','').result.then(function() {
                            promotionService.doDeletePromotionProduct(data) .then (function (res) {
                                if(res){
                                    notify.success("CMS_TXT_MSG_DELETE_SUCCESS");
                                    $scope.dtPromotionProductList.dtInstance.reloadData();
                                } else {
                                    notify.success("CMS_TXT_MSG_DELETE_FAILED");
                                }
                            })
                        })
                    } else {
                        notify.warning("CMS_MSG_NO_PRODUCT_SELECTED");
                    }
                } else {
                    notify.warning('CMS_MSG_ACTIVITY_OVER_OR_UNEFFECTIVE_PROMOTION');
                }
            };

            /**
             * Discount画面
             */
            $scope.openPromotionDiscount = function () {
                if (!$scope.promotionInfo.isActivityOver && $scope.promotionInfo.isEffective) {
                    if ($scope.usProductInfo.selectIdList.length) {
                        var modalInstance = $modal.open({
                            templateUrl: cmsPopupPages.popPromotionDiscount.page,
                            controller: cmsPopupPages.popPromotionDiscount.controller,
                            size: 'md',
                            resolve: {
                                productList: function () { return $scope.usProductInfo.selectIdList; },
                                promotionId: function () { return $scope.promotionId; }
                            }
                        });

                        modalInstance.result.then(function(){
                            $scope.dtPromotionProductList.dtInstance.reloadData();
                        })
                    } else {
                        notify.warning("CMS_MSG_NO_PRODUCT_SELECTED");
                    }
                } else {
                    notify.warning('CMS_MSG_ACTIVITY_OVER_OR_UNEFFECTIVE_PROMOTION');
                }
            };

            /**
             * 分页PromotionProduct
             */
            $scope.doGetPromotionProductList = function (data, draw) {

                if (data.draw === 1 || !$scope.usProductInfo.currentPageIdList) {
                    draw({data: []})
                }
                return promotionService.doGetSubPromotionProductInfo (data, $scope.promotionId)
                    .then (function (data) {
                    $scope.usProductInfo.selectAllFlag = true;
                    $scope.usProductInfo.currentPageIdList = [];

                    _.forEach(data.data, function (productInfo) {
                        var productId = parseInt(productInfo.productId);

                        $scope.usProductInfo.currentPageIdList.push({id: productId});

                        if (!$scope.usProductInfo.selectOneFlagList.hasOwnProperty(productId) || !$scope.usProductInfo.selectOneFlagList[productId]) {
                            $scope.usProductInfo.selectAllFlag = false;
                        }
                    });
                    return draw(data)
                });
            };

            /**
             * 获取PromotionInfo
             */
            function doGetPromotionInfo  () {
                promotionService.doGetPromotionInfo($scope.promotionId)
                    .then (function (data) {
                    $scope.promotionInfo = data;
                    $scope.oldPromotionInfo = angular.copy($scope.promotionInfo);
                    $scope.promotionName = angular.copy($scope.promotionInfo.name);
                });
            }

            /**
             * 获取sub promotion的product信息.
             */
            function doGetSubPromotionProductInfo  () {
                promotionService.doGetSubPromotionProductInfo($scope.promotionId)
                    .then (function (data) {
                    $scope.promotionProductList = data;
                    $scope.status.open = $scope.promotionProductList.length ? false : true;

                });
            }

            /**
             * datatable的product分页数据取得.
             */
            function doGetPromotionProductDataTable () {
                var titleHtml = '<input ng-controller="selectController" ng-model="usProductInfo.selectAllFlag" type="checkbox" ng-click="selectAll(usProductInfo)">';
                $scope.dtPromotionProductList = {
                     options: DTOptionsBuilder.newOptions()
                         .withOption('processing', true)
                         .withOption('serverSide', true)
                         .withOption('scrollY', '400px')
                         .withOption('scrollX', '100%')
                         .withOption('scrollCollapse', false)
                         .withOption('ajax', $scope.doGetPromotionProductList)
                         .withOption('createdRow',  function(row, data, dataIndex) {
                             // Recompiling so we can bind Angular directive to the DT
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
                     columns: [
                         DTColumnBuilder.newColumn(null).withTitle(titleHtml).notSortable().withClass('wtab-check').renderWith(function (val, type, row, cell) {
                             var productId = parseInt(row.productId);
                             if (!$scope.usProductInfo.selectOneFlagList.hasOwnProperty(productId)) {
                                 $scope.usProductInfo.selectOneFlagList[productId] = false;
                             }
                             return '<input ng-controller="selectController" type="checkbox" ng-model="usProductInfo.selectOneFlagList['+productId+']" ng-click="selectOne(' + productId + ', usProductInfo)">';
                         }),
                         //DTColumnBuilder.newColumn('').withTitle(titleHtml).withClass('wtab-xs text-center').renderWith(function (val, type, row, cell) {
                         //    //$scope.selected[row.id] = false;
                         //    return '<input ng-controller="selectController" type="checkbox" '+ (_.indexOf($scope.selectedPromotionProductList, parseInt(row.productId)) > -1 ? 'checked' : '') +' ng-click="selectOne('+ row.productId +', selectedPromotionProductList)">';}),
                         DTColumnBuilder.newColumn('', $translate('CMS_TXT_CODE')).withClass('wtab-xs text-center').renderWith(function (val, type, row, cell) {
                             return ('<img class="prodImg" src="' + $rootScope.cmsMaster.imageUrl + row.imageName + '"><br><a ng-controller="navigationController" href="" class="btn-main" ng-href="{{goProductPage(' + row.categoryId + ',' + row.modelId + ',' + row.productId + ')}}">' + row.code + '</a>');
                         }),
                         DTColumnBuilder.newColumn('name', $translate('CMS_TXT_NAME')).withClass('wtab-xsm'),
                         DTColumnBuilder.newColumn('price', $translate('CMS_TXT_PRICE')).withClass('wtab-xxs text-right'),
                         DTColumnBuilder.newColumn('discountPercent', $translate('CMS_TXT_DISCOUNT')).withClass('wtab-xs text-center'),
                         DTColumnBuilder.newColumn('discountSalePrice', $translate('CMS_TXT_OUTLETS_SALE_PRICE')).withClass('wtab-xxs text-right'),
                         DTColumnBuilder.newColumn('quantity', $translate('CMS_TXT_QUANTITY')).withClass('wtab-xs text-center'),
                         DTColumnBuilder.newColumn('salesQuantity', $translate('CMS_TXT_SALES_QUANTITY_IN_ACTIVITY')).withClass('wtab-xs text-center'),
                         DTColumnBuilder.newColumn('salesPercent', $translate('CMS_TXT_SALES_PERCENT_IN_ACTIVITY')).withClass('wtab-xs text-center'),
                         DTColumnBuilder.newColumn('created', $translate('CMS_TXT_CREATED_ON')).withClass('wtab-sm'),
                         DTColumnBuilder.newColumn('modified', $translate('CMS_TXT_LAST_UPDATED_ON')).withClass('wtab-sm'),
                       
                     ],
                     	dtInstance: null
                 };
            }

            /**
             * 获取sub promotion信息.
             */
            function doGetSubPromotionInfo  () {
                promotionService.doGetSubPromotionInfo($scope.promotionType, $scope.promotionId)
                    .then (function (data) {
                    $scope.subPromotionList = data;
                    $scope.status.open = $scope.subPromotionList.length ? false : true;
                });
            }

        }])
});