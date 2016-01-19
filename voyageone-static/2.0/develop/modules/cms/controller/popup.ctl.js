/**
 * Created by linanbin on 15/12/7.
 */

define([
    'cms',
    'underscore'
], function (cms, _) {

    cms.constant('popActions', {
            "column_define": {
                "templateUrl": "views/pop/column_define/index.tpl.html",
                "controllerUrl": "modules/cms/views/pop/column_define/index.ctl"
            },
            "new": {
                "templateUrl": "views/pop/new/promotion.tpl.html",
                "controllerUrl": "modules/cms/views/pop/new/promotion.ctl"
            },
            "other": {
                "platform": {
                    "templateUrl": "views/pop/other/platform.tpl.html",
                    "controllerUrl": "modules/cms/views/pop/other/platform.ctl",
                    "controller": 'otherPlatformPopupController as ctrl',
                    "size": 'md'
                },
                "progress": {
                    "templateUrl": "views/pop/other/progress.tpl.html",
                    "controllerUrl": "modules/cms/views/pop/other/progress.ctl"
                }
            },
            "tag": {
                "promotion": {
                    "templateUrl": "views/pop/tag/promotion/index.tpl.html",
                    "controllerUrl": "modules/cms/views/pop/tag/promotion/index.ctl"
                },
                "shop_category": {
                    "templateUrl": "views/pop/tag/shop_category/index.tpl.html",
                    "controllerUrl": "modules/cms/views/pop/tag/shop_category/index.ctl"
                }
            },
            "prop_change": {
                "templateUrl": "views/pop/prop_change/index.tpl.html",
                "controllerUrl": "modules/cms/views/pop/prop_change/index.ctl"
            },
            "import": {
                "templateUrl": "views/pop/import/index.tpl.html",
                "controllerUrl": "modules/cms/views/pop/import/index.ctl"
            },
            "otherDownload": {
                "templateUrl": "views/pop/other/download.tpl.html",
                "controllerUrl": "modules/cms/views/pop/other/download.ctl",
                "controller": 'OtherDownloadCtl',
                "size": 'md'
            },
            "product": {
                "price": {
                    "templateUrl": "views/pop/product/price/history.tpl.html",
                    "controllerUrl": "modules/cms/views/pop/product/price/history.ctl"
                },
                "promotion": {
                    "templateUrl": "views/pop/product/promotion/history.tpl.html",
                    "controllerUrl": "modules/cms/views/pop/product/promotion/history.ctl"
                }
            },
            "system": {
                "category": {
                    "templateUrl": "views/pop/system/category/edit.tpl.html",
                    "controllerUrl": "modules/cms/views/pop/system/category/edit.ctl",
                    "controller": 'popCategorySchemaCtl as ctrl',
                    "backdrop": 'static',
                    "size": 'md'
                }
            },
            "category": {
                "templateUrl": "views/pop/category/index.tpl.html",
                "controllerUrl": "modules/cms/views/pop/category/index.ctl",
                "controller": 'categoryPopupController as ctrl',
                "backdrop": 'static',
                "size": 'lg'
            },
            "feed": {
                "templateUrl": "views/pop/feed/index.tpl.html",
                "controllerUrl": "modules/cms/views/pop/feed/index.ctl",
                "controller": 'feedPropMappingPopupController as ctrl',
                "backdrop": 'static',
                "size": 'lg'
            },
            "feedValue": {
                "templateUrl": "views/pop/feedValue/index.tpl.html",
                "controllerUrl": "modules/cms/views/pop/feedValue/index.ctl",
                "controller": 'feedPropValuePopupController as ctrl',
                "backdrop": 'static',
                "size": 'md'
            },
            "platformMapping": {
                "complex": {
                    "templateUrl": "views/pop/platformMapping/ppComplex.tpl.html",
                    "controllerUrl": "modules/cms/views/pop/platformMapping/ppComplex.ctl",
                    "controller": 'complexMappingPopupController as ctrl',
                    "size": 'md',
                    "backdrop": "static"
                },
                "simple": {
                    list: {},
                    item: {
                        "templateUrl": "views/pop/platformMapping/ppSimple.item.tpl.html",
                        "controllerUrl": "modules/cms/views/pop/platformMapping/ppSimple.item.ctl",
                        "controller": 'simpleItemMappingPopupController as ctrl',
                        "size": 'md',
                        "backdrop": "static"
                    }
                },
                "multiComplex": {
                    "templateUrl": "views/pop/multiComplexMapping/index.tpl.html",
                    "controllerUrl": "modules/cms/views/pop/multiComplexMapping/index.ctl",
                    "controller": 'multiComplexMapping as ctrl',
                    "size": 'lg',
                    "backdrop": "static"
                }
            }
        })
        .controller('popupCtrl', popupCtrl);

    function popupCtrl($scope, $modal, popActions, $q, $translate, alert) {

        function openModel(config, context) {

            if (context) config.resolve = {
                context: function () {
                    return context;
                }
            };

            var defer = $q.defer();
            require([config.controllerUrl], function () {
                defer.resolve($modal.open(config).result);
            });
            return defer.promise;
        }

        $scope.openCustomBaseProperty = openCustomBaseProperty;
        function openCustomBaseProperty(viewSize) {
            $modal.open({
                templateUrl: popActions.column_define.templateUrl,
                controllerUrl: popActions.column_define.controllerUrl,
                size: viewSize,
                resolve: {
                    items: function () {
                        //return data;
                    }
                }
            });
        }

        /**
         * pop出properties变更页面,用于批量更新产品属性
         * @type {openupdateProperties}
         */
        $scope.openUpdateProperties = openUpdateProperties;
        function openUpdateProperties(viewSize, selList, fnInitial) {
            require([popActions.prop_change.controllerUrl], function () {
                if (selList && selList.length) {
                    var modalInstance = $modal.open({
                        templateUrl: popActions.prop_change.templateUrl,
                        controller: 'popPropChangeCtl',
                        size: viewSize,
                        resolve: {
                            productIds: function () {
                                var productIds = [];
                                _.forEach(selList, function (object) {
                                    productIds.push(object.id);
                                });
                                return productIds;
                            }
                        }
                    });

                    // 回调主页面的刷新操作
                    modalInstance.result.then(function () {
                        fnInitial();
                    })

                } else {
                    alert($translate.instant('TXT_COM_MSG_NO_ROWS_SELECT'));
                }
            });
        }

        /**
         * pop出promotion选择页面,用于设置
         * @type {openTagPromotion}
         */
        $scope.openTagPromotion = openTagPromotion;
        function openTagPromotion(viewSize, promotion, selList, fnInitial) {
            require([popActions.tag.promotion.controllerUrl], function () {
                if (selList && selList.length) {
                    var modalInstance = $modal.open({
                        templateUrl: popActions.tag.promotion.templateUrl,
                        controller: 'popTagPromotionCtl',
                        size: viewSize,
                        resolve: {
                            promotion: function () {
                                var productIds = [];
                                _.forEach(selList, function (object) {
                                    productIds.push(object.id);
                                });
                                return {"promotion": promotion, "productIds": productIds};
                            }
                        }
                    });

                    // 回调主页面的刷新操作
                    modalInstance.result.then(function () {
                        fnInitial();
                    })
                } else {
                    alert($translate.instant('TXT_COM_MSG_NO_ROWS_SELECT'));
                }
            });
        }

        $scope.openshop_category = openshop_category;
        function openshop_category(viewSize) {
            $modal.open({
                templateUrl: popActions.tag.shop_category.templateUrl,
                controllerUrl: popActions.tag.shop_category.controllerUrl,
                size: viewSize,
                resolve: {
                    items: function () {
                        //return data;
                    }
                }
            });
        }

        $scope.openHistoryPromotion = openHistoryPromotion;
        function openHistoryPromotion(viewSize, data) {
            require([popActions.product.promotion.controllerUrl], function () {
                $modal.open({
                    templateUrl: popActions.product.promotion.templateUrl,
                    controller: 'popPromotionHistoryCtl',
                    size: viewSize,
                    resolve: {
                        data: function () {
                            return data;
                        }
                    }
                });
            });
        }

        $scope.openHistoryPrice = openHistoryPrice;
        function openHistoryPrice(viewSize, data, type) {
            require([popActions.product.price.controllerUrl], function () {
                $modal.open({
                    templateUrl: popActions.product.price.templateUrl,
                    controller: 'popPriceHistoryCtl',
                    size: viewSize,
                    resolve: {
                        data: function () {
                            return {
                                code: data,
                                type: type
                            };
                        }
                    }
                });
            });
        }

        $scope.openpromotion = openpromotion;
        function openpromotion(viewSize, data) {
            require([popActions.new.controllerUrl], function () {
                $modal.open({
                    templateUrl: popActions.new.templateUrl,
                    controller: 'popNewPromotionCtl',
                    size: viewSize,
                    resolve: {
                        items: function () {
                            return data;
                        }
                    }
                });
            });
        }

        $scope.openOtherProgress = openOtherProgress;
        function openOtherProgress(viewSize) {
            $modal.open({
                templateUrl: popActions.other.progress.templateUrl,
                controllerUrl: popActions.other.progress.controllerUrl,
                size: viewSize,
                resolve: {
                    items: function () {
                        //return data;
                    }
                }
            });
        }

        $scope.openImport = openImport;
        function openImport(viewSize, data) {
            require([popActions.import.controllerUrl], function () {
                $modal.open({
                    templateUrl: popActions.import.templateUrl,
                    controller: 'importCtl',
                    size: viewSize,
                    resolve: {
                        data: function () {
                            return data;
                        }
                    }
                });
            });
        }

        $scope.openOtherPlatform = function (context) {
            return openModel(popActions.other.platform, context);
        };

        $scope.openSystemCategory = function (context) {
            return openModel(popActions.system.category, context);
        };

        $scope.popupNewCategory = function (context) {
            return openModel(popActions.category, context);
        };

        $scope.popupFeed = function (context) {
            return openModel(popActions.feed, context);
        };

        $scope.popupFeedValue = function (context) {
            return openModel(popActions.feedValue, context);
        };

        $scope.ppPlatformMapping = {
            /**
             * Complex Mapping 设定弹出框的上下文参数
             * @typedef {object} ComplexMappingPopupContext
             * @property {string} platformCategoryPath 平台类目路径
             * @property {string} mainCategoryId 主数据类目 ID
             * @property {Field} property 平台属性
             */

            /**
             * 弹出 Complex 属性的值匹配窗
             * @param {ComplexMappingPopupContext} context 上下文参数
             * @returns {Promise.<ComplexMappingBean>}
             */
            complex: function (context) {
                return openModel(popActions.platformMapping.complex, context);
            },

            simple: {

                /**
                 * Simple Mapping List 设定弹出框的上下文参数
                 * @typedef {object} SimpleListMappingPopupContext
                 * @property {string} platformCategoryPath 平台类目路径
                 * @property {string} mainCategoryId 主数据类目 ID
                 * @property {Field} property 平台属性
                 */

                /**
                 * 弹出 Simple 属性的值匹配窗
                 * @param {SimpleListMappingPopupContext} context
                 * @returns {Promise}
                 */
                list: function (context) {
                    return openModel(popActions.platformMapping.simple.list, context);
                },

                /**
                 * Simple Mapping Item 设定弹出框的上下文参数
                 * @typedef {object} SimpleItemMappingPopupContext
                 * @property {string} platformCategoryPath 平台类目路径
                 * @property {string} mainCategoryId 主数据类目 ID
                 * @property {Field} property 平台属性
                 */

                /**
                 * 弹出 Simple 属性的值匹配窗
                 * @param {SimpleItemMappingPopupContext} context
                 * @returns {Promise}
                 */
                item: function (context) {
                    return openModel(popActions.platformMapping.simple.item, context);
                }
            },

            multiComplex: function (context) {
                return openModel(popActions.platformMapping.multiComplex, context);
            }
        };
    }
});