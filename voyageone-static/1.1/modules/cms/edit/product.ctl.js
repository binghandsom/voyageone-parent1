/**
 * @Name:    product.ctl.js
 * @Date:    2015/7/28
 *
 * @User:    Edward
 * @Version: 1.0.0
 */


define(function (require) {
    var cmsApp = require('modules/cms/cms.module');
    require('modules/cms/edit/edit.service');
    require('modules/cms/common/common.service');
    require('modules/cms/popup/promotionHistory/popPromotionHistory.ctl');
    require('modules/cms/popup/priceHistory/popPriceHistory.ctl');
    require('modules/cms/popup/imgSetting/imgSetting.ctl');
//    require('modules/oms/directives/tab.directive');
//    require('modules/oms/customer/customerDetail.service');
//    require('modules/oms/customer/customer.service');
//    require('modules/oms/directives/selectLine.directive');
//    require('modules/oms/services/orderDetailInfo.service');
//    require('modules/oms/services/addNewOrder.service');
//    require('modules/oms/popup/popAddNoteCustomer.ctl');

    cmsApp.controller('editProductController', ['$scope', '$rootScope', '$q', '$location', '$routeParams', 'cmsCommonService', 'editProductService', 'cartId', 'userService', 'notify', 'vConfirm','$modal','cmsPopupPages',
        function ($scope, $rootScope, $q, $location, $routeParams, commonService, productService, cartId, userService, notify, vConfirm, $modal, cmsPopupPages) {

            var _ = require('underscore');
            var commonUtil = require('components/util/commonUtil');
            var cmsCommonUtil = require('modules/cms/common/commonUtil');

            $scope.currentProductId = $routeParams.productId;
            $scope.productName = "";
            //$scope.usProductIsChanged = false;
            //$scope.cnBaseProductIsChanged = false;
            //$scope.cnTmProductIsChanged = false;
            //$scope.cnJdProductIsChanged = false;
            //$scope.cnPriceSettingIsChanged = false;
            //$scope.usOfficialPriceInfoIsChanged = false;
            //$scope.usOfficialPriceIsChanged = false;
            //$scope.usAmazonPriceInfoIsChanged = false;
            //$scope.usAmazonPriceIsChanged = false;
            //$scope.usCnBasePriceInfoIsChanged = false;
            //$scope.usCnBasePriceIsChanged = false;
            //$scope.cnBasePriceInfoIsChanged = false;
            //$scope.cnBasePriceIsChanged = false;
            //$scope.cnOfficialPriceInfoIsChanged = false;
            //$scope.cnOfficialPriceIsChanged = false;
            //$scope.cnTaobaoPriceInfoIsChanged = false;
            //$scope.cnTaobaoPriceIsChanged = false;
            //$scope.cnTmallPriceInfoIsChanged = false;
            //$scope.cnTmallPriceIsChanged = false;
            //$scope.cnTmallGPriceInfoIsChanged = false;
            //$scope.cnTmallGPriceIsChanged = false;
            //$scope.cnJDPriceInfoIsChanged = false;
            //$scope.cnJDPriceIsChanged = false;
            //$scope.cnJGPriceInfoIsChanged = false;
            //$scope.cnJGPriceIsChanged = false;
            $scope.currentImageIndex = 0;
            
            $scope.customList = [];

            /**
             * 初始化操作.
             */
            $scope.initialize = function () {

                if (!_.isEmpty($scope.currentProductId)) {
                    doGetUSProductInfo().then(function () {
                        //if (_.isNull($routeParams.categoryId)) {
                        //    $scope.categoryId = $scope.usProductInfo.categoryId;
                        //    $scope.currentModelId = $scope.usProductInfo.modelId;
                        //} else {
                        //}
                        $scope.categoryId = isNaN(parseInt($routeParams.categoryId)) ? 2 : parseInt($routeParams.categoryId);
                        $scope.currentModelId = $routeParams.modelId;
                        doGetNavigationByCategoryModelId();
                        doGetProductSizeAndStock();
                        doGetCNProductInfo();
                        doGetUSProductPriceInfo();
                        doGetCNProductPriceInfo();
                        doGetProductImages();
                        doGetCustomInfo();
                        if ($rootScope.cmsMaster.priceSettingIsActive)
                            doGetCNPriceSettingInfo();

                    });
                }
            };

            /**
             * 执行Effective处理.
             */
            $scope.doUpdateEffective = function () {
                vConfirm('CORE_TXT_MESSAGE_CHANGE_TO_EFFECTIVE','').result.then(function() {
                    var isEffective = $scope.usProductInfo.isEffective;
                    $scope.undoUpdateUSProductInfo();
                    $scope.usProductInfo.isEffective = isEffective;
                    $scope.doUpdateUSProductInfo();
                }, function() {
                    $scope.usProductInfo.isEffective = !$scope.usProductInfo.isEffective;
                })
            };

            /**
             * 更新US Product信息.
             */
            $scope.doUpdateUSProductInfo = function () {

                if (_.isDate ($scope.usProductInfo.availableTime)) {
                    $scope.usProductInfo.availableTime = commonUtil.doFormatDate ($scope.usProductInfo.availableTime);
                }

                productService.doUpdateUSProductInfo($scope.usProductInfo)
                    .then(function () {
                        notify.success("CMS_TXT_MSG_UPDATE_SUCCESS");
                        doGetNavigationByCategoryModelId();
                        doGetUSProductInfo();
                    }, function () {
                        notify.danger("CMS_TXT_MSG_UPDATE_FAILED");
                        doGetUSProductInfo();
                    });
            };

            /**
             * 更新CN Product信息.
             */
            $scope.doUpdateCNProductInfo = function () {

                productService.doUpdateCNProductInfo($scope.cnBaseProductInfo)
                    .then(function () {
                        notify.success("CMS_TXT_MSG_UPDATE_SUCCESS");
                        doGetCNProductInfo();
                    }, function () {
                        notify.danger("CMS_TXT_MSG_UPDATE_FAILED");
                        doGetCNProductInfo();
                    });
            };

            /**
             * 更新TM Product信息.
             */
            $scope.doUpdateTMProductInfo = function () {

                productService.doUpdateCNProductTmallInfo($scope.tmProductInfo)
                    .then(function () {
                        notify.success("CMS_TXT_MSG_UPDATE_SUCCESS");
                        doGetCNProductInfo();
                    }, function () {
                        notify.danger("CMS_TXT_MSG_UPDATE_FAILED");
                        doGetCNProductInfo();
                    });
            };

            /**
             * 更新JD Product信息.
             */
            $scope.doUpdateJDProductInfo = function () {

                productService.doUpdateCNProductJingDongInfo($scope.jdProductInfo)
                    .then(function () {
                        notify.success("CMS_TXT_MSG_UPDATE_SUCCESS");
                        doGetCNProductInfo();
                    }, function () {
                        notify.danger("CMS_TXT_MSG_UPDATE_FAILED");
                        doGetCNProductInfo();
                    });
            };

            /**
             * 更新CN Product Setting信息.
             */
            $scope.doUpdateProductCNPriceSettingInfo = function () {

                productService.doUpdateProductCNPriceSettingInfo($scope.cnPriceSettingInfo)
                    .then(function () {
                        notify.success("CMS_TXT_MSG_UPDATE_SUCCESS");
                        doGetCNPriceSettingInfo();
                    }, function () {
                        notify.danger("CMS_TXT_MSG_UPDATE_FAILED");
                        doGetCNPriceSettingInfo();
                    })
                    .then(function() {
                        doGetUSProductPriceInfo();
                    });
            };

            /**
             * 更新US Official Price信息.
             */
            $scope.doUpdateUSPriceInfo = function () {

                if (_.isDate ($scope.usOfficialPriceInfo.prePublishDatetime)) {
                    $scope.usOfficialPriceInfo.prePublishDatetime = commonUtil.doFormatDate ($scope.usOfficialPriceInfo.prePublishDatetime);
                }

                productService.doUpdateProductOfficialPriceInfo($scope.usOfficialPriceInfo)
                    .then(function () {
                        notify.success("CMS_TXT_MSG_UPDATE_SUCCESS");
                        doGetUSProductPriceInfo();
                    }, function () {
                        notify.danger("CMS_TXT_MSG_UPDATE_FAILED");
                        doGetUSProductPriceInfo();
                    });
            };

            /**
             * 更新US Amazon Price信息.
             */
            $scope.doUpdateUSAmazonPriceInfo = function () {

                if (_.isDate ($scope.usAmazonPriceInfo.prePublishDatetime)) {
                    $scope.usAmazonPriceInfo.prePublishDatetime = commonUtil.doFormatDate ($scope.usAmazonPriceInfo.prePublishDatetime);
                }

                productService.doUpdateProductUSCartPriceInfo($scope.usAmazonPriceInfo)
                    .then(function () {
                        notify.success("CMS_TXT_MSG_UPDATE_SUCCESS");
                        doGetUSProductPriceInfo();
                    }, function () {
                        notify.danger("CMS_TXT_MSG_UPDATE_FAILED");
                        doGetUSProductPriceInfo();
                    });
            };

            /**
             * 更新US的CN Default Price信息.
             */
            $scope.doUpdateCNPriceInfo = function () {

                productService.doUpdateProductCNCartPriceInfo($scope.usCnBasePriceInfo)
                    .then(function () {
                        notify.success("CMS_TXT_MSG_UPDATE_SUCCESS");
                        doGetCNProductPriceInfo();
                    }, function () {
                        notify.danger("CMS_TXT_MSG_UPDATE_FAILED");
                        doGetCNProductPriceInfo();
                    });
            };

            /**
             * 更新CN的Default Price信息.
             */
            $scope.doUpdateCNBasePriceInfo = function () {

                if (_.isDate ($scope.cnBasePriceInfo.prePublishDatetime)) {
                    $scope.cnBasePriceInfo.prePublishDatetime = commonUtil.doFormatDate ($scope.cnBasePriceInfo.prePublishDatetime);
                }

                productService.doUpdateProductCNCartPriceInfo($scope.cnBasePriceInfo)
                    .then(function () {
                        notify.success("CMS_TXT_MSG_UPDATE_SUCCESS");
                        doGetCNProductPriceInfo();
                    }, function () {
                        notify.danger("CMS_TXT_MSG_UPDATE_FAILED");
                        doGetCNProductPriceInfo();
                    });
            };

            /**
             * 更新CN的Official Price信息.
             */
            $scope.doUpdateCNOfficialPriceInfo = function () {

                if (_.isDate ($scope.cnOfficialPriceInfo.prePublishDatetime)) {
                    $scope.cnOfficialPriceInfo.prePublishDatetime = commonUtil.doFormatDate ($scope.cnOfficialPriceInfo.prePublishDatetime);
                }

                productService.doUpdateProductCNCartPriceInfo($scope.cnOfficialPriceInfo)
                    .then(function () {
                        notify.success("CMS_TXT_MSG_UPDATE_SUCCESS");
                        doGetCNProductPriceInfo();
                    }, function () {
                        notify.danger("CMS_TXT_MSG_UPDATE_FAILED");
                        doGetCNProductPriceInfo();
                    });
            };

            /**
             * 更新CN的Taobao Price信息.
             */
            $scope.doUpdateCNTaobaoPriceInfo = function () {

                if (_.isDate ($scope.cnTaobaoPriceInfo.prePublishDatetime)) {
                    $scope.cnTaobaoPriceInfo.prePublishDatetime = commonUtil.doFormatDate ($scope.cnTaobaoPriceInfo.prePublishDatetime);
                }

                productService.doUpdateProductCNCartPriceInfo($scope.cnTaobaoPriceInfo)
                    .then(function () {
                        notify.success("CMS_TXT_MSG_UPDATE_SUCCESS");
                        doGetCNProductPriceInfo();
                    }, function () {
                        notify.danger("CMS_TXT_MSG_UPDATE_FAILED");
                        doGetCNProductPriceInfo();
                    });
            };

            /**
             * 更新CN的Tmall Price信息.
             */
            $scope.doUpdateCNTmallPriceInfo = function () {

                if (_.isDate ($scope.cnTmallPriceInfo.prePublishDatetime)) {
                    $scope.cnTmallPriceInfo.prePublishDatetime = commonUtil.doFormatDate ($scope.cnTmallPriceInfo.prePublishDatetime);
                }

                productService.doUpdateProductCNCartPriceInfo($scope.cnTmallPriceInfo)
                    .then(function () {
                        notify.success("CMS_TXT_MSG_UPDATE_SUCCESS");
                        doGetCNProductPriceInfo();
                    }, function () {
                        notify.danger("CMS_TXT_MSG_UPDATE_FAILED");
                        doGetCNProductPriceInfo();
                    });
            };

            /**
             * 更新CN的TmallG Price信息.
             */
            $scope.doUpdateCNTmallGPriceInfo = function () {

                if (_.isDate ($scope.cnTmallGPriceInfo.prePublishDatetime)) {
                    $scope.cnTmallGPriceInfo.prePublishDatetime = commonUtil.doFormatDate ($scope.cnTmallGPriceInfo.prePublishDatetime);
                }

                productService.doUpdateProductCNCartPriceInfo($scope.cnTmallGPriceInfo)
                    .then(function () {
                        notify.success("CMS_TXT_MSG_UPDATE_SUCCESS");
                        doGetCNProductPriceInfo();
                    }, function () {
                        notify.danger("CMS_TXT_MSG_UPDATE_FAILED");
                        doGetCNProductPriceInfo();
                    });
            };

            /**
             * 更新CN的JD Price信息.
             */
            $scope.doUpdateCNJDPriceInfo = function () {

                if (_.isDate ($scope.cnJDPriceInfo.prePublishDatetime)) {
                    $scope.cnJDPriceInfo.prePublishDatetime = commonUtil.doFormatDate ($scope.cnJDPriceInfo.prePublishDatetime);
                }

                productService.doUpdateProductCNCartPriceInfo($scope.cnJDPriceInfo)
                    .then(function () {
                        notify.success("CMS_TXT_MSG_UPDATE_SUCCESS");
                        doGetCNProductPriceInfo();
                    }, function () {
                        notify.danger("CMS_TXT_MSG_UPDATE_FAILED");
                        doGetCNProductPriceInfo();
                    });
            };

            /**
             * 更新CN的JG Price信息.
             */
            $scope.doUpdateCNJGPriceInfo = function () {

                if (_.isDate ($scope.cnJGPriceInfo.prePublishDatetime)) {
                    $scope.cnJGPriceInfo.prePublishDatetime = commonUtil.doFormatDate ($scope.cnJGPriceInfo.prePublishDatetime);
                }

                productService.doUpdateProductCNCartPriceInfo($scope.cnJGPriceInfo)
                    .then(function () {
                        notify.success("CMS_TXT_MSG_UPDATE_SUCCESS");
                        doGetCNProductPriceInfo();
                    }, function () {
                        notify.danger("CMS_TXT_MSG_UPDATE_FAILED");
                        doGetCNProductPriceInfo();
                    });
            };

            // TODO 设定上传产品图片页面
            /**
             * 弹出product image upload页面.
             */
            $scope.goUploadProductImagePage = function () {
	           	 var modalInstance = $modal.open({
	                 templateUrl: cmsPopupPages.popImgSetting.page,
	                 controller: cmsPopupPages.popImgSetting.controller,
	                 size: 'lg',
	                 resolve: {
	                         productId: function () { return $scope.currentProductId; },
	                         productImages: function () { 
	                        	 data = [];
	                        	 _.each($scope.productImages, function(item){
	                        		 data.push( _.clone(item));
	                        	 })
	                        	 return data; 
	                         },
	                 }
	             });
	
	             modalInstance.result.then(function(){
	            	 doGetUSProductInfo();
	            	 doGetProductImages();
	             })
            };

            $scope.doUpdateCustemData = function(){
                productService.doUpdateCustemData($scope.customList)
                    .then(function () {
                        notify.success("CMS_TXT_MSG_UPDATE_SUCCESS");
                        doGetCustomInfo();
                    }, function () {
                        notify.danger("CMS_TXT_MSG_UPDATE_FAILED");
                        doGetCustomInfo();
                    });
            }
            // TODO
            /**
             * 弹出Display Image页面.
             */
            $scope.goDisplayImagePage = function () {

            };

            //TODO popup price history show page.
            /**
             * 弹出Price History页面.
             * @param type
             */
            $scope.goPriceHistory = function (type) {

                var data = {};
                data.type = type;
                data.productId = $scope.currentProductId;

            };

            // TODO
            /**
             * 迁移到wms Inventory页面.
             */
            $scope.goWmsInventoryPage = function () {

            };
            /** 画面跳转 end **/

            /** 画面编辑数据撤销 start **/

            /**
             * 取消US Product信息的变更.
             */
            $scope.undoUpdateUSProductInfo = function () {
                $scope.usProductInfo = angular.copy($scope.oldUsProductInfo);
                $scope.usProductIsChanged = false;
            };

            /**
             * 取消CN Product信息的变更.
             */
            $scope.undoUpdateCNProductInfo = function () {
                $scope.cnBaseProductInfo = angular.copy($scope.oldCnBaseProductInfo);
                $scope.cnBaseProductIsChanged = false;
            };

            /**
             * 取消TM Product信息的变更.
             */
            $scope.undoUpdateTMProductInfo = function () {
                $scope.tmProductInfo = angular.copy($scope.oldTmProductInfo);
                $scope.cnTmProductIsChanged = false;
            };

            /**
             * 取消JD Product信息的变更.
             */
            $scope.undoUpdateJDProductInfo = function () {
                $scope.jdProductInfo = angular.copy($scope.oldJdProductInfo);
                $scope.cnJdProductIsChanged = false;
            };

            /**
             * 取消CN Price Setting信息的变更.
             */
            $scope.undoUpdateProductCNPriceSettingInfo = function () {
                $scope.cnPriceSettingInfo = angular.copy($scope.oldCnPriceSettingInfo);
                $scope.cnPriceSettingIsChanged = false;
            };

            /**
             * 取消US Price信息的变更.
             */
            $scope.undoUpdateUSPriceInfo = function () {
                $scope.usOfficialPriceInfo = angular.copy($scope.oldUsOfficialPriceInfo);
                $scope.usOfficialPriceInfoIsChanged = false;
                $scope.usOfficialPriceIsChanged = false;
            };

            /**
             * 取消US Amazon Price信息的变更.
             */
            $scope.undoUpdateUSAmazonPriceInfo = function () {
                $scope.usAmazonPriceInfo = angular.copy($scope.oldUsAmazonPriceInfo);
                $scope.usAmazonPriceInfoIsChanged = false;
                $scope.usAmazonPriceIsChanged = false;
            };

            /**
             * 取消CN Default Price信息的变更.
             */
            $scope.undoUpdateCNPriceInfo = function () {
                $scope.usCnBasePriceInfo = angular.copy($scope.oldUsCnBasePriceInfo);
                $scope.usCnBasePriceInfoIsChanged = false;
                $scope.usCnBasePriceIsChanged = false;
            };

            /**
             * 取消CN Default Price信息的变更.
             */
            $scope.undoUpdateCNBasePriceInfo = function () {
                $scope.cnBasePriceInfo = angular.copy($scope.oldCnBasePriceInfo);
                $scope.cnBasePriceInfoIsChanged = false;
                $scope.cnBasePriceIsChanged = false;
            };

            /**
             * 取消CN Official Price信息的变更.
             */
            $scope.undoUpdateCNOfficialPriceInfo = function () {
                $scope.cnOfficialPriceInfo = angular.copy($scope.oldCnOfficialPriceInfo);
                $scope.cnOfficialPriceInfoIsChanged = false;
                $scope.cnOfficialPriceIsChanged = false;
            };

            /**
             * 取消CN Taobao Price信息的变更.
             */
            $scope.undoUpdateCNTaobaoPriceInfo = function () {
                $scope.cnTaobaoPriceInfo = angular.copy($scope.oldCnTaobaoPriceInfo);
                $scope.cnTaobaoPriceInfoIsChanged = false;
                $scope.cnTaobaoPriceIsChanged = false;
            };

            /**
             * 取消CN Tmall Price信息的变更.
             */
            $scope.undoUpdateCNTmallPriceInfo = function () {
                $scope.cnTmallPriceInfo = angular.copy($scope.oldCnTmallPriceInfo);
                $scope.cnTmallPriceInfoIsChanged = false;
                $scope.cnTmallPriceIsChanged = false;
            };

            /**
             * 取消CN TmallG Price信息的变更.
             */
            $scope.undoUpdateCNTmallGPriceInfo = function () {
                $scope.cnTmallGPriceInfo = angular.copy($scope.oldCnTmallGPriceInfo);
                $scope.cnTmallGPriceInfoIsChanged = false;
                $scope.cnTmallGPriceIsChanged = false;
            };

            /**
             * 取消CN JD Price信息的变更.
             */
            $scope.undoUpdateCNJDPriceInfo = function () {
                $scope.cnJDPriceInfo = angular.copy($scope.oldCnJDPriceInfo);
                $scope.cnJDPriceInfoIsChanged = false;
                $scope.cnJDPriceIsChanged = false;
            };

            /**
             * 取消CN JG Price信息的变更.
             */
            $scope.undoUpdateCNJGPriceInfo = function () {
                $scope.cnJGPriceInfo = angular.copy($scope.oldCnJGPriceInfo);
                $scope.cnJGPriceInfoIsChanged = false;
                $scope.cnJGPriceIsChanged = false;
            };

            /**
             * 取消Custem的变更.
             */
            $scope.undoUpdateCustemData = function () {
                $scope.customList = angular.copy($scope.oldCustomList);
                $scope.custemDataIsChanged = false;
            };

            /** 画面编辑数据撤销 end **/

            /** 画面变更数据设置 start **/

            /**
             * 检查US Product Info是否发生变化
             */
            $scope.usProductChanged = function () {
                $scope.usProductIsChanged = !_.isEqual($scope.usProductInfo, $scope.oldUsProductInfo);
            };

            /**
             * 检查CN Base Product Info是否发生变化
             */
            $scope.cnBaseProductChanged = function () {
                $scope.cnBaseProductIsChanged = !_.isEqual($scope.cnBaseProductInfo, $scope.oldCnBaseProductInfo);
            };

            /**
             * 检查TM Product Info是否发生变化
             */
            $scope.tmProductChanged = function () {
                $scope.cnTmProductIsChanged = !_.isEqual($scope.tmProductInfo, $scope.oldTmProductInfo);
            };

            /**
             * 检查JD Product Info是否发生变化
             */
            $scope.jdProductChanged = function () {
                $scope.cnJdProductIsChanged = !_.isEqual($scope.jdProductInfo, $scope.oldJdProductInfo);
            };

            /**
             * 检查CN Price Setting Info是否发生变化
             */
            $scope.cnPriceSettingChanged = function () {
                $scope.cnPriceSettingIsChanged = !_.isEqual($scope.cnPriceSettingInfo, $scope.oldCnPriceSettingInfo);
            };

            /**
             * 检查US Official Price Info是否发生变化
             */
            $scope.usOfficialPriceInfoChanged = function () {
                $scope.usOfficialPriceInfoIsChanged = !_.isEqual($scope.usOfficialPriceInfo, $scope.oldUsOfficialPriceInfo);
            };

            /**
             * 检查US Amazon Price Info是否发生变化
             */
            $scope.usAmazonPriceInfoChanged = function () {
                $scope.usAmazonPriceInfoIsChanged = !_.isEqual($scope.usAmazonPriceInfo, $scope.oldUsAmazonPriceInfo);
            };

            /**
             * 检查US CN Base Price Info是否发生变化
             */
            $scope.usCNBasePriceChanged = function () {
                $scope.usCnBasePriceInfoIsChanged = !_.isEqual($scope.usCnBasePriceInfo, $scope.oldUsCnBasePriceInfo);
            };

            /**
             * 检查CN Base Price Info是否发生变化
             */
            $scope.cnBasePriceInfoChanged = function () {
                $scope.cnBasePriceInfoIsChanged = !_.isEqual($scope.cnBasePriceInfo, $scope.oldCnBasePriceInfo);
            };

            /**
             * 检查CN Official Price Info是否发生变化
             */
            $scope.cnOfficialPriceInfoChanged = function () {
                $scope.cnOfficialPriceInfoIsChanged = !_.isEqual($scope.cnOfficialPriceInfo, $scope.oldCnOfficialPriceInfo);
            };

            /**
             * 检查CN Taobao Price Info是否发生变化
             */
            $scope.cnTaobaoPriceInfoChanged = function () {
                $scope.cnTaobaoPriceInfoIsChanged = !_.isEqual($scope.cnTaobaoPriceInfo, $scope.oldCnTaobaoPriceInfo);
            };

            /**
             * 检查CN Tmall Price Info是否发生变化
             */
            $scope.cnTmallPriceInfoChanged = function () {
                $scope.cnTmallPriceInfoIsChanged = !_.isEqual($scope.cnTmallPriceInfo, $scope.oldCnTmallPriceInfo);
            };

            /**
             * 检查CN TmallG Price Info是否发生变化
             */
            $scope.cnTmallGPriceInfoChanged = function () {
                $scope.cnTmallGPriceInfoIsChanged = !_.isEqual($scope.cnTmallGPriceInfo, $scope.oldCnTmallGPriceInfo);
            };

            /**
             * 检查CN JD Price Info是否发生变化
             */
            $scope.cnJDPriceInfoChanged = function () {
                $scope.cnJDPriceInfoIsChanged = !_.isEqual($scope.cnJDPriceInfo, $scope.oldCnJDPriceInfo);
            };

            /**
             * 检查CN JG Price Info是否发生变化
             */
            $scope.cnJGPriceInfoChanged = function () {
                $scope.cnJGPriceInfoIsChanged = !_.isEqual($scope.cnJGPriceInfo, $scope.oldCnJGPriceInfo);
            };
            /**
             * 检查custemChanged是否发生变化
             */
            $scope.custemChanged = function () {
                $scope.custemDataIsChanged = !_.isEqual($scope.customList, $scope.oldCustomList);
            };
            /** 画面变更数据设置 end **/

            /** 画面操作 start **/

            /**
             * 如果US Product's Code 发生变化，也将替换掉CN Product's Code.
             */
            //$scope.usProductCodeChanged = function () {
            //    $scope.cnBaseProductInfo.code = $scope.usProductInfo.code;
            //    $scope.usProductChanged();
            //};

            /**
             * Clear the US colorMapId.
             */
            $scope.clearUSColorMapId = function () {
                $scope.usProductInfo.colorMapId = '';
                $scope.usProductChanged();
            };

            /**
             * Clear the US madeInCountryId.
             */
            $scope.clearUSMadeInCountryId = function () {
                $scope.usProductInfo.madeInCountryId = '';
                $scope.usProductChanged();
            };

            /**
             * Clear the US materialFabric1Id.
             */
            $scope.clearUSMaterialFabric1Id = function () {
                $scope.usProductInfo.materialFabric1Id = '';
                $scope.usProductChanged();
            };

            /**
             * Clear the US materialFabric2Id.
             */
            $scope.clearUSMaterialFabric2Id = function () {
                $scope.usProductInfo.materialFabric2Id = '';
                $scope.usProductChanged();
            };

            /**
             * Clear the US materialFabric3Id.
             */
            $scope.clearUSMaterialFabric3Id = function () {
                $scope.usProductInfo.materialFabric3Id = '';
                $scope.usProductChanged();
            };

            //$scope.cnProductCodeChanged = function () {
            //    $scope.usProductInfo.code = $scope.cnBaseProductInfo.code;
            //    $scope.cnBaseProductChanged();
            //};

            /**
             * Clear hsCodeId.
             */
            $scope.clearHsCodeId = function () {
                $scope.cnBaseProductInfo.hsCodeId = '';
                $scope.cnBaseProductChanged();
            };

            /**
             * Clear hsCodePuId.
             */
            $scope.clearHsCodePuId = function () {
                $scope.cnBaseProductInfo.hsCodePuId = '';
                $scope.cnBaseProductChanged();
            };

            /**
             * Clear basePriceId.
             */
            $scope.clearBasePriceId = function () {
                $scope.cnPriceSettingInfo.basePriceId = '';
                $scope.cnPriceSettingChanged();
            };

            /**
             * Clear overHeard1.
             */
            $scope.clearOverHeard1 = function () {
                $scope.cnPriceSettingInfo.overHeard1 = '0.00';
                $scope.cnPriceSettingChanged();
            };

            /**
             * Clear overHeard2.
             */
            $scope.clearOverHeard2 = function () {
                $scope.cnPriceSettingInfo.overHeard2 = '0.00';
                $scope.cnPriceSettingChanged();
            };

            /**
             * Clear freeShippingTypeId.
             */
            $scope.clearUSOfficialPriceShippingTypeId = function () {
                $scope.usOfficialPriceInfo.freeShippingTypeId = '';
                $scope.usOfficialPriceInfoChanged();
            };

            /**
             * Clear freeShippingTypeId.
             */
            $scope.clearUSAmazonPriceShippingTypeId = function () {
                $scope.usAmazonPriceInfo.freeShippingTypeId = '';
                $scope.usAmazonPriceInfoChanged ();
            };

            /**
             * Clear freeShippingTypeId.
             */
            $scope.clearCnOfficialPriceShippingTypeId = function () {
                $scope.cnOfficialPriceInfo.freeShippingTypeId = '';
                $scope.cnOfficialPriceInfoChanged();
            };

            /**
             * Clear freeShippingTypeId.
             */
            $scope.clearCnTaobaoPriceShippingTypeId = function () {
                $scope.cnTaobaoPriceInfo.freeShippingTypeId = '';
                $scope.cnTaobaoPriceInfoChanged();
            };

            /**
             * Clear freeShippingTypeId.
             */
            $scope.clearCnTmallPriceShippingTypeId = function () {
                $scope.cnTmallPriceInfo.freeShippingTypeId = '';
                $scope.cnTmallPriceInfoChanged();
            };

            /**
             * Clear freeShippingTypeId.
             */
            $scope.clearCnTmallGPriceShippingTypeId = function () {
                $scope.cnTmallGPriceInfo.freeShippingTypeId = '';
                $scope.cnTmallGPriceInfoChanged();
            };

            /**
             * Clear freeShippingTypeId.
             */
            $scope.clearCnJDPriceShippingTypeId = function () {
                $scope.cnJDPriceInfo.freeShippingTypeId = '';
                $scope.cnJDPriceInfoChanged();
            };

            /**
             * Clear freeShippingTypeId.
             */
            $scope.clearCnJGPriceShippingTypeId = function () {
                $scope.cnJGPriceInfo.freeShippingTypeId = '';
                $scope.cnJGPriceInfoChanged();
            };

            /**
             * US official's isOutletsOnSale has been changed clear the discount.
             */
            $scope.usOfficialIsOutletsOnSaleChanged = function () {
                if (!$scope.usOfficialPriceInfo.isOutletsOnSale) {
                    $scope.usOfficialPriceInfo.discount =  '0.00';
                }
                $scope.usOfficialPriceInfoChanged();
            };

            /**
             * US Amazon's isOutletsOnSale has been changed clear the discount.
             */
            $scope.usAmazonIsOutletsOnSaleChanged = function () {
                if (!$scope.usAmazonPriceInfo.isOutletsOnSale) {
                    $scope.usAmazonPriceInfo.discount =  '0.00';
                }
                $scope.usAmazonPriceInfoChanged();
            };

            /**
             * US CNBase's isOutletsOnSale has been changed clear the discount.
             */
            $scope.usCnBasePriceIsOutletsChanged = function () {
                $scope.usCnBasePriceInfo.cnPriceDiscount = '0.00';
                $scope.usCNBasePriceChanged();
            };

            /**
             * CN Official's isOutletsOnSale has been changed clear the discount.
             */
            $scope.cnOfficialIsOutletsOnSaleChanged = function () {
                if (!$scope.cnOfficialPriceInfo.isOutletsOnSale) {
                    $scope.cnOfficialPriceInfo.cnPriceFinalRmbDiscount =  '0.00';
                }
                $scope.cnOfficialPriceInfoChanged();
            };

            /**
             * CN Taobao's isOutletsOnSale has been changed clear the discount.
             */
            $scope.cnTaobaoIsOutletsOnSaleChanged = function () {
                if (!$scope.cnTaobaoPriceInfo.isOutletsOnSale) {
                    $scope.cnTaobaoPriceInfo.cnPriceFinalRmbDiscount =  '0.00';
                }
                $scope.cnTaobaoPriceInfoChanged();
            };

            /**
             * CN Tmall's isOutletsOnSale has been changed clear the discount.
             */
            $scope.cnTmallIsOutletsOnSaleChanged = function () {
                if (!$scope.cnTmallPriceInfo.isOutletsOnSale) {
                    $scope.cnTmallPriceInfo.cnPriceFinalRmbDiscount =  '0.00';
                }
                $scope.cnTmallPriceInfoChanged();
            };

            /**
             * CN TmallG's isOutletsOnSale has been changed clear the discount.
             */
            $scope.cnTmallGIsOutletsOnSaleChanged = function () {
                if (!$scope.cnTmallGPriceInfo.isOutletsOnSale) {
                    $scope.cnTmallGPriceInfo.cnPriceFinalRmbDiscount =  '0.00';
                }
                $scope.cnTmallGPriceInfoChanged();
            };

            /**
             * CN JD's isOutletsOnSale has been changed clear the discount.
             */
            $scope.cnJDIsOutletsOnSaleChanged = function () {
                if (!$scope.cnJDPriceInfo.isOutletsOnSale) {
                    $scope.cnJDPriceInfo.cnPriceFinalRmbDiscount =  '0.00';
                }
                $scope.cnJDPriceInfoChanged();
            };

            /**
             * CN JG's isOutletsOnSale has been changed clear the discount.
             */
            $scope.cnJGIsOutletsOnSaleChanged = function () {
                if (!$scope.cnJGPriceInfo.isOutletsOnSale) {
                    $scope.cnJGPriceInfo.cnPriceFinalRmbDiscount =  '0.00';
                }
                $scope.cnJGPriceInfoChanged();
            };

            /**
             * 设置当前图片为显示图片.
             */
            $scope.setCurrentImage = function (index) {
                $scope.currentImageIndex = index;
            };

            /**
             * 展示下一张图片.
             */
            $scope.showNextImage = function(){
                $scope.currentImageIndex = ($scope.currentImageIndex < $scope.images.productImages - 1) ? ++$scope.currentImageIndex : 0;
            };

            /**
             * 展示上一张图片.
             */
            $scope.showPreviousImage = function(){
                $scope.currentImageIndex = ($scope.currentImageIndex > 0) ? --$scope.currentImageIndex : $scope.productImages.length -1;
            };
            /** 画面操作 end **/

            /** 金额计算有关操作 start **/

            /**
             * US tempDiscount has been changed, reset the price.
             */
            $scope.usTempDiscountChanged = function (data) {
                var price = _.isUndefined(data.currentPrice) ? data.price : data.currentPrice;
                data.price = cmsCommonUtil.accountPriceByTempDiscount (price, data.tempDiscount);

                usResetProfit(data);
            };

            /**
             * US price has been changed, reset the tempDiscount, profit
             */
            $scope.usPriceChanged = function (data) {

                var tempDiscount = '0.00';
                if (!_.isUndefined(data.currentPrice)) {
                    tempDiscount = cmsCommonUtil.accountTempDiscountByPrice(data.currentPrice, data.price);
                }
                if (!_.isEqual(tempDiscount, data.tempDiscount) && tempDiscount >= 0.00) {
                    data.tempDiscount = tempDiscount;
                }

                usResetProfit(data)
            };

            /**
             * US CnBasePrice's tempDiscount has been changed, reset the price.
             */
            $scope.usCNBasePriceTempDiscountChanged = function () {
                var price = _.isUndefined($scope.usCnBasePriceInfo.currentCnPrice) ? $scope.usCnBasePriceInfo.cnPrice : $scope.usCnBasePriceInfo.currentCnPrice;
                $scope.usCnBasePriceInfo.cnPrice = cmsCommonUtil.accountPriceByTempDiscount (price, $scope.usCnBasePriceInfo.tempDiscount);

                usRestCnBasePrice();
            };

            /**
             * CN BasePrice's cnPrice has been changed, reset the cnPriceRmb, cnPriceFinalRmb, tempDiscount.
             */
            $scope.usCNBasePriceCnPriceChanged = function () {
                var tempDiscount = '0.00';
                if (!_.isUndefined($scope.usCnBasePriceInfo.currentCnPrice)) {
                    tempDiscount = cmsCommonUtil.accountTempDiscountByPrice($scope.usCnBasePriceInfo.currentCnPrice, $scope.usCnBasePriceInfo.cnPrice);
                }
                if (!_.isEqual(tempDiscount, $scope.usCnBasePriceInfo.tempDiscount) && tempDiscount >= 0.00) {
                    $scope.usCnBasePriceInfo.tempDiscount = tempDiscount;
                }

                usRestCnBasePrice();
            };

            /**
             * CN adjustment has been changed, reset the finalPrice.
             */
            //$scope.cnAdjustmentRmbChanged = function (data) {
            //    data.cnPriceFinalRmb = cmsCommonUtil.accountFinalPrice(data.cnPriceRmb, data.cnPriceAdjustmentRmb);
            //
            //    cnResetTempDiscount(data);
            //    //cnRestEffectivePrice(data);
            //};

            /**
             * CN tempDiscount has been changed, reset the price.
             */
            $scope.cnTempDiscountChanged = function (data) {
                //data.cnPriceFinalRmb = cmsCommonUtil.accountPriceByTempDiscount (data.currentCnPriceFinalRmb, data.tempFinalPriceRmbDiscount);
                var price = _.isUndefined(data.currentCnPriceFinalRmb) ? data.cnPriceFinalRmb : data.currentCnPriceFinalRmb;
                data.cnPriceFinalRmb = cmsCommonUtil.accountPriceByTempDiscount (price, data.tempFinalPriceRmbDiscount);

                //cnResetAdjustmentPrice(data);
                cnRestEffectivePrice(data);
            };

            /**
             * CN cnFinalRmb has been changed, reset the tempDiscount,effectivePrice, adjustmentPrice.
             */
            $scope.cnCnFinalRmbChanged = function (data) {

                cnResetTempDiscount(data);
                //cnResetAdjustmentPrice(data);
                cnRestEffectivePrice(data);
            };
            /** 金额计算有关操作 end **/

           
            /**
             * get the category and model navigation info.
             */
            function doGetNavigationByCategoryModelId() {
                commonService.doGetNavigationByCategoryModelId( "2", $scope.categoryId, $scope.currentModelId)
                    .then(function (data) {
                        $scope.navigationCategoryList = data.categoryList;
                        $scope.navigationModelInfo = data.modelInfo;
                        $scope.navigationModelInfo.parentCategoryId = _.last($scope.navigationCategoryList).categoryId;
                    })
            }

            /**
             * get the us product info.
             * @returns {*}
             */
            function doGetUSProductInfo() {

                var defer = $q.defer();

                productService.doGetUSProductInfo($scope.currentProductId)
                    .then(function (data) {
                        $scope.usProductInfo = data;
                        $scope.productName = $scope.usProductInfo.code + ": " + $scope.usProductInfo.name;
                        $scope.oldUsProductInfo = angular.copy($scope.usProductInfo);
                        $scope.usProductIsChanged = false;
                        defer.resolve();
                    });

                return defer.promise;
            }

            /**
             * get the cn product info.
             * @returns {*}
             */
            function doGetCNProductInfo() {

                productService.doGetCNProductInfo($scope.currentProductId)
                    .then(function (data) {
                        $scope.cnBaseProductInfo = data.cnBaseProductInfo;
                        //$scope.productName = $scope.usProductInfo.code + ": " + $scope.usProductInfo.name;
                        $scope.oldCnBaseProductInfo = angular.copy($scope.cnBaseProductInfo);
                        // TODO Show display images.
                        if ($scope.cnBaseProductInfo.displayImages !== null) {
                            $scope.displayImages = $scope.cnBaseProductInfo.displayImages.split(",");
                        }

                        $scope.tmProductInfo = data.tmProductInfo;
                        $scope.oldTmProductInfo = angular.copy($scope.tmProductInfo);

                        $scope.jdProductInfo = data.jdProductInfo;
                        $scope.oldJdProductInfo = angular.copy($scope.jdProductInfo);
                        $scope.cnBaseProductIsChanged = false;
                        $scope.cnTmProductIsChanged = false;
                        $scope.cnJdProductIsChanged = false;
                    });
            }

            /**
             * get cn price setting info.
             */
            function doGetCNPriceSettingInfo() {

                productService.doGetProductCNPriceSettingInfo($scope.currentProductId)
                    .then(function (data) {

                        $scope.cnPriceSettingInfo = data;
                        $scope.oldCnPriceSettingInfo = angular.copy($scope.cnPriceSettingInfo);
                        $scope.cnPriceSettingIsChanged = false;
                    });
            }

            /**
             * get us product price info.
             */
            function doGetUSProductPriceInfo() {

                productService.doGetUSProductPriceInfo($scope.currentProductId)
                    .then(function (data) {

                        angular.forEach(data, function (usCartPriceInfo) {

                            switch (usCartPriceInfo.cartId) {
                                case cartId.US_OFFICIAL:
                                    $scope.usOfficialPriceInfo = usCartPriceInfo;
                                    $scope.oldUsOfficialPriceInfo = angular.copy($scope.usOfficialPriceInfo);
                                    $scope.usOfficialPriceInfoIsChanged = false;
                                    $scope.usOfficialPriceIsChanged = false;
                                    break;
                                case cartId.US_AMAZON:
                                    $scope.usAmazonPriceInfo = usCartPriceInfo;
                                    $scope.oldUsAmazonPriceInfo = angular.copy($scope.usAmazonPriceInfo);
                                    $scope.usAmazonPriceInfoIsChanged = false;
                                    $scope.usAmazonPriceIsChanged = false;
                                    break;
                            }
                        });

                        if (_.isUndefined($scope.usOfficialPriceInfo)) {
                            $scope.usOfficialPriceInfo = defaultPriceInfo (cartId.US_OFFICIAL);
                            $scope.oldUsOfficialPriceInfo = angular.copy($scope.usOfficialPriceInfo);
                        }

                        if (_.isUndefined($scope.usAmazonPriceInfo)) {
                            $scope.usAmazonPriceInfo = defaultPriceInfo (cartId.US_AMAZON);
                            $scope.oldUsAmazonPriceInfo = angular.copy($scope.usAmazonPriceInfo);
                        }
                    });
            }

            /**
             * get cn product price info.
             */
            function doGetCNProductPriceInfo() {

                productService.doGetCNProductPriceInfo($scope.currentProductId)
                    .then(function (data) {

                        angular.forEach(data, function (cnCartPriceInfo) {

                            switch (cnCartPriceInfo.cartId) {
                                case cartId.CN_ALL:
                                    $scope.usCnBasePriceInfo = cnCartPriceInfo;
                                    $scope.oldUsCnBasePriceInfo = angular.copy($scope.usCnBasePriceInfo);
                                    $scope.usCnBasePriceInfoIsChanged = false;
                                    $scope.usCnBasePriceIsChanged = false;
                                    cnCartPriceInfo.cnPriceFinalRmbDiscount = ((cnCartPriceInfo.cnPriceRmb / cnCartPriceInfo.cnPrice) * 100).toFixed(0);
                                    $scope.cnBasePriceInfo = cnCartPriceInfo;
                                    $scope.oldCnBasePriceInfo = angular.copy($scope.cnBasePriceInfo);
                                    $scope.cnBasePriceInfoIsChanged = false;
                                    $scope.cnBasePriceIsChanged = false;
                                    break;
                                case cartId.CN_OFFICIAL:
                                    $scope.cnOfficialPriceInfo = cnCartPriceInfo;
                                    $scope.oldCnOfficialPriceInfo = angular.copy($scope.cnOfficialPriceInfo);
                                    $scope.cnOfficialPriceInfoIsChanged = false;
                                    $scope.cnOfficialPriceIsChanged = false;
                                    break;
                                case cartId.CN_TB:
                                    $scope.cnTaobaoPriceInfo = cnCartPriceInfo;
                                    $scope.oldCnTaobaoPriceInfo = angular.copy($scope.cnTaobaoPriceInfo);
                                    $scope.cnTaobaoPriceInfoIsChanged = false;
                                    $scope.cnTaobaoPriceIsChanged = false;
                                    break;
                                case cartId.CN_TM:
                                    $scope.cnTmallPriceInfo = cnCartPriceInfo;
                                    $scope.oldCnTmallPriceInfo = angular.copy($scope.cnTmallPriceInfo);
                                    $scope.cnTmallPriceInfoIsChanged = false;
                                    $scope.cnTmallPriceIsChanged = false;
                                    break;
                                case cartId.CN_TG:
                                    $scope.cnTmallGPriceInfo = cnCartPriceInfo;
                                    $scope.oldCnTmallGPriceInfo = angular.copy($scope.cnTmallGPriceInfo);
                                    $scope.cnTmallGPriceInfoIsChanged = false;
                                    $scope.cnTmallGPriceIsChanged = false;
                                    break;
                                case cartId.CN_JD:
                                    $scope.cnJDPriceInfo = cnCartPriceInfo;
                                    $scope.oldCnJDPriceInfo = angular.copy($scope.cnJDPriceInfo);
                                    $scope.cnJDPriceInfoIsChanged = false;
                                    $scope.cnJDPriceIsChanged = false;
                                    break;
                                case cartId.CN_JG:
                                    $scope.cnJGPriceInfo = cnCartPriceInfo;
                                    $scope.oldCnJGPriceInfo = angular.copy($scope.cnJGPriceInfo);
                                    $scope.cnJGPriceInfoIsChanged = false;
                                    $scope.cnJGPriceIsChanged = false;
                                    break;
                            }
                        });

                        if (_.isUndefined($scope.usCnBasePriceInfo)) {
                            $scope.usCnBasePriceInfo = defaultPriceInfo (cartId.CN_ALL);
                            $scope.oldUsCnBasePriceInfo = angular.copy($scope.usCnBasePriceInfo);
                            $scope.cnBasePriceInfo = angular.copy($scope.usCnBasePriceInfo);
                            $scope.oldCnBasePriceInfo = angular.copy($scope.usCnBasePriceInfo);
                        }

                        if (_.isUndefined($scope.cnOfficialPriceInfo)) {
                            $scope.cnOfficialPriceInfo = defaultPriceInfo (cartId.CN_OFFICIAL);
                            $scope.oldCnOfficialPriceInfo = angular.copy($scope.cnOfficialPriceInfo);
                        }

                        if (_.isUndefined($scope.cnTaobaoPriceInfo)) {
                            $scope.cnTaobaoPriceInfo = defaultPriceInfo (cartId.CN_TB);
                            $scope.oldCnTaobaoPriceInfo = angular.copy($scope.cnTaobaoPriceInfo);
                        }

                        if (_.isUndefined($scope.cnTmallPriceInfo)) {
                            $scope.cnTmallPriceInfo = defaultPriceInfo (cartId.CN_TM);
                            $scope.oldCnTmallPriceInfo = angular.copy($scope.cnTmallPriceInfo);
                        }

                        if (_.isUndefined($scope.cnTmallGPriceInfo)) {
                            $scope.cnTmallGPriceInfo = defaultPriceInfo (cartId.CN_TG);
                            $scope.oldCnTmallGPriceInfo = angular.copy($scope.cnTmallGPriceInfo);
                        }

                        if (_.isUndefined($scope.cnJDPriceInfo)) {
                            $scope.cnJDPriceInfo = defaultPriceInfo (cartId.CN_JD);
                            $scope.oldCnJDPriceInfo = angular.copy($scope.cnJDPriceInfo);
                        }

                        if (_.isUndefined($scope.cnJGPriceInfo)) {
                            $scope.cnJGPriceInfo = defaultPriceInfo (cartId.CN_JG);
                            $scope.oldCnJGPriceInfo = angular.copy($scope.cnJGPriceInfo);
                        }
                    });
            }

            /**
             * get product images info.
             */
            function doGetProductImages () {
                productService.doGetProductImages ($scope.currentProductId)
                    .then(function (data) {
                        $scope.productImages = data;
                        angular.forEach($scope.productImages, function(productImage) {
                            productImage.imageUrl = $rootScope.cmsMaster.imageUrl + productImage.imageUrl;
                        });
                        $scope.currentImageIndex = 0;
                    })
            }

            /**
             * get product size and stock info.
             */
            function doGetProductSizeAndStock () {
                productService.doGetProductInventory($scope.usProductInfo.code)
                    .then(function (data) {
                        $scope.stockInfo = {};
                        $scope.stockInfo.sizeAndStockList = data.inventoryInfo;
                        $scope.stockInfo.parterInfoList = data.thirdPartnerInfo;
                        $scope.stockInfo.lasterReceivedOn = data.lasterReceivedOn;
                    })
            }
            
            /**
             * get product custom info.
             */
            function doGetCustomInfo () {
                productService.doGetCustomInfo($scope.currentProductId)
                    .then(function (data) {
                        $scope.customList = data;
                        $scope.oldCustomList = angular.copy($scope.customList);
                        $scope.custemDataIsChanged = false;
                    })
            }
            /**
             * default price info at first time.
             * @param cartId
             * @returns {{}}
             */
            function defaultPriceInfo (cartId) {
                var  data = {};
                data.productId = $scope.currentProductId;
                data.channelId = userService.getSelChannel();
                data.cartId = cartId;
                data.isOutletsOnSale = false;
                data.isOnSale = false;
                data.isApproved = false;
                return data;
            }

            /**
             * reset the profit when the price has been changed.
             * @param data
             */
            function usResetProfit (data) {
                var price = isNaN(parseFloat(data.price)) ? 0.00 : parseFloat(data.price);
                var freeShippingFee = _.isEqual(data.freeShippingTypeId, "1") ? $rootScope.cmsMaster.freeShippingFee : 0.00;

                var profit = 0.00;
                switch (data.cartId) {
                    case cartId.US_OFFICIAL:
                        profit = parseFloat(((price - freeShippingFee) - (data.msrp * $rootScope.cmsMaster.priceProfitCoefficient)) / data.msrp * 100).toFixed(2);
                        $scope.usOfficialPriceIsChanged = !_.isEqual(data.price, data.currentPrice);
                        $scope.usOfficialPriceInfoChanged();
                        break;
                    case cartId.US_AMAZON:
                        profit = parseFloat((price * (1.00 - $rootScope.cmsMaster.amazonFee) - $rootScope.cmsMaster.amazonShippingAdjustmentFee - data.msrp * $rootScope.cmsMaster.priceProfitCoefficient) / data.msrp * 100).toFixed(2);
                        $scope.usAmazonPriceIsChanged = !_.isEqual($scope.usAmazonPriceInfo.price, $scope.usAmazonPriceInfo.currentPrice);
                        $scope.usAmazonPriceInfoChanged();
                        break;
                }
                if (!_.isEqual(profit, data.profit) && profit > 0.00) {
                    data.profit = profit.toString();
                }
            }

            /**
             * reset the cnPrice.
             */
            function usRestCnBasePrice () {
                $scope.usCnBasePriceInfo.cnPriceRmb = cmsCommonUtil.accountDefaultPriceByPrice($scope.usCnBasePriceInfo.cnPrice, $scope.cnPriceSettingInfo.pricingFactor);
                //$scope.usCnBasePriceInfo.cnPriceFinalRmb = cmsCommonUtil.accountFinalPrice($scope.usCnBasePriceInfo.cnPriceRmb, $scope.usCnBasePriceInfo.cnPriceAdjustmentRmb);

                $scope.usCnBasePriceIsChanged = !_.isEqual($scope.usCnBasePriceInfo.cnPrice, $scope.usCnBasePriceInfo.currentCnPrice);
                $scope.usCNBasePriceChanged();
            }

            /**
             * reset the temp discount.
             * @param data
             */
            function cnResetTempDiscount (data) {

                var tempDiscount = '0.00';
                if (!_.isUndefined(data.currentCnPriceFinalRmb)) {
                    tempDiscount = cmsCommonUtil.accountTempDiscountByPrice(data.currentCnPriceFinalRmb, data.cnPriceFinalRmb);
                }
                if (!_.isEqual(tempDiscount, data.tempFinalPriceRmbDiscount) && tempDiscount >= 0.00) {
                    data.tempFinalPriceRmbDiscount = tempDiscount;
                }
            }

            /**
             * reset the adjustment price info.
             * @param data
             */
            //function cnResetAdjustmentPrice (data) {
            //
            //    var adjustmentPrice = cmsCommonUtil.accountAdjustmentPrice(data.cnPriceRmb, data.cnPriceFinalRmb);
            //    if (!_.isEqual(adjustmentPrice, data.cnPriceAdjustmentRmb)) {
            //        data.cnPriceAdjustmentRmb = adjustmentPrice;
            //    }
            //}

            /**
             * reset the effective info.
             * @param data
             */
            function cnRestEffectivePrice (data) {

                //data.effectivePrice = cmsCommonUtil.accountEffectivePrice(data.cnPriceFinalRmb
                //    ,$scope.cnPriceSettingInfo.overHeard1
                //    ,$scope.cnPriceSettingInfo.overHeard2
                //    ,$scope.cnPriceSettingInfo.exchangeRate
                //    ,$scope.cnPriceSettingInfo.shippingCompensation);

                switch (data.cartId) {
                    case cartId.CN_ALL:
                        $scope.cnBasePriceIsChanged = !_.isEqual(data.cnPriceFinalRmb, data.currentCnPriceFinalRmb);
                        $scope.cnBasePriceInfoChanged();
                        break;
                    case cartId.CN_OFFICIAL:
                        $scope.cnOfficialPriceIsChanged = !_.isEqual(data.cnPriceFinalRmb, data.currentCnPriceFinalRmb);
                        $scope.cnOfficialPriceInfoChanged();
                        break;
                    case cartId.CN_TB:
                        $scope.cnTaobaoPriceIsChanged = !_.isEqual(data.cnPriceFinalRmb, data.currentCnPriceFinalRmb);
                        $scope.cnTaobaoPriceInfoChanged();
                        break;
                    case cartId.CN_TM:
                        $scope.cnTmallPriceIsChanged = !_.isEqual(data.cnPriceFinalRmb, data.currentCnPriceFinalRmb);
                        $scope.cnTmallPriceInfoChanged();
                        break;
                    case cartId.CN_TG:
                        $scope.cnTmallGPriceIsChanged = !_.isEqual(data.cnPriceFinalRmb, data.currentCnPriceFinalRmb);
                        $scope.cnTmallGPriceInfoChanged();
                        break;
                    case cartId.CN_JD:
                        $scope.cnJDPriceIsChanged = !_.isEqual(data.cnPriceFinalRmb, data.currentCnPriceFinalRmb);
                        $scope.cnJDPriceInfoChanged();
                        break;
                    case cartId.CN_JG:
                        $scope.cnJGPriceIsChanged = !_.isEqual(data.cnPriceFinalRmb, data.currentCnPriceFinalRmb);
                        $scope.cnJGPriceInfoChanged();
                        break;
                }
            };

        }])
});