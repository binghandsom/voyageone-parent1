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
                },
                "dictionary": {
                    "value": {
                        "templateUrl": "views/pop/system/dictionary/value.tpl.html",
                        "controllerUrl": "modules/cms/views/pop/system/dictionary/value.ctl",
                        "controller": "popDictValueController as ctrl"
                    },
                    "custom": {
                        "templateUrl": "views/pop/system/dictionary/custom.tpl.html",
                        "controllerUrl": "modules/cms/views/pop/system/dictionary/custom.ctl",
                        "controller": "popDictCustomController as ctrl"
                    }
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
                    list: {
                        "templateUrl": "views/pop/platformMapping/ppSimple.list.tpl.html",
                        "controllerUrl": "modules/cms/views/pop/platformMapping/ppSimple.list.ctl",
                        "controller": 'simpleListMappingPopupController as ctrl',
                        "size": 'lg',
                        "backdrop": "static"
                    },
                    item: {
                        "templateUrl": "views/pop/platformMapping/ppSimple.item.tpl.html",
                        "controllerUrl": "modules/cms/views/pop/platformMapping/ppSimple.item.ctl",
                        "controller": 'simpleItemMappingPopupController as ctrl',
                        "size": 'lg',
                        "backdrop": "static"
                    }
                },
                "multiComplex": {
                    list: {
                        "templateUrl": "views/pop/platformMapping/ppMultiComplex.list.tpl.html",
                        "controllerUrl": "modules/cms/views/pop/platformMapping/ppMultiComplex.list.ctl",
                        "controller": 'multiComplexMappingPopupController as ctrl',
                        "size": 'md',
                        "backdrop": "static"
                    },

                    item: {
                        "templateUrl": "views/pop/platformMapping/ppMultiComplex.item.tpl.html",
                        "controllerUrl": "modules/cms/views/pop/platformMapping/ppMultiComplex.item.ctl",
                        "controller": 'multiComplexItemMappingPopupController as ctrl',
                        "size": 'md',
                        "backdrop": "static"
                    }
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

        $scope.openDictValue = openDictValue;
        function openDictValue(viewSize) {
            require([popActions.system.dictionary.value.controllerUrl], function () {
                var modalInstance = $modal.open({
                    templateUrl: popActions.system.dictionary.value.templateUrl,
                    controller: popActions.system.dictionary.value.controller,
                    size: viewSize,
                    resolve: {
                        promotion: function () {
                            //var productIds = [];
                            //_.forEach(selList, function (object) {
                            //    productIds.push(object.id);
                            //});
                            //return {"promotion": promotion, "productIds": productIds};
                        }
                    }
                });

                // 回调主页面的刷新操作
                //modalInstance.result.then(function () {
                //    fnInitial();
                //})
            });
        }

        $scope.openDictCustom = openDictCustom;
        function openDictCustom(viewSize) {
            require([popActions.system.dictionary.custom.controllerUrl], function () {
                var modalInstance = $modal.open({
                    templateUrl: popActions.system.dictionary.custom.templateUrl,
                    controller: popActions.system.dictionary.custom.controller,
                    size: viewSize,
                    resolve: {
                        promotion: function () {
                            //var productIds = [];
                            //_.forEach(selList, function (object) {
                            //    productIds.push(object.id);
                            //});
                            //return {"promotion": promotion, "productIds": productIds};
                        }
                    }
                });

                // 回调主页面的刷新操作
                //modalInstance.result.then(function () {
                //    fnInitial();
                //})
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
        function openpromotion(viewSize, cartList, data, fnInitial) {
            require([popActions.new.controllerUrl], function () {
                var modalInstance = $modal.open({
                    templateUrl: popActions.new.templateUrl,
                    controller: 'popNewPromotionCtl',
                    size: viewSize,
                    resolve: {
                        items: function () {
                            return data;
                        },
                        cartList: function () {
                            return cartList;
                        }
                    }
                });

                modalInstance.result.then(function () {
                    if (fnInitial) {
                        fnInitial();
                    }

                })
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

        /**
         * @class
         * @name Field
         * @property {string} type
         * @property {object[]} options
         * @property {object[]} rules
         * @property {string} name
         * @property {string} id
         */

        /**
         * @typedef {object} FieldBean
         */

        /**
         * @typedef {object} FeedPropMappingPopupContext
         * @property {string} feedCategoryPath
         * @property {string} mainCategoryPath
         * @property {Field} field
         * @property {FieldBean} bean
         */

        /**
         * @param {FeedPropMappingPopupContext} context
         * @returns {Promise}
         */
        $scope.popupFeed = function (context) {
            return openModel(popActions.feed, context);
        };

        $scope.popupFeedValue = function (context) {
            return openModel(popActions.feedValue, context);
        };

        $scope.ppPlatformMapping = {

            /**
             * 弹出 Complex 属性的值匹配窗
             * @param {SimpleListMappingPopupContext} context 上下文参数
             * @returns {Promise.<MultiComplexMappingBean>}
             */
            complex: function (context) {
                return openModel(popActions.platformMapping.complex, context);
            },

            simple: {

                /**
                 * Simple Mapping List 设定弹出框的上下文参数
                 * @name SimpleListMappingPopupContext
                 * @class
                 * @property {string} platformCategoryPath 平台类目路径
                 * @property {string} platformCategoryId 平台类目 ID
                 * @property {string} mainCategoryId 主数据类目 ID
                 * @property {number} cartId 平台 ID
                 * @property {Field} property 平台属性
                 * @property {number|null} valueIndex
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
                 * @name SimpleItemMappingPopupContext
                 * @class
                 * @extends SimpleListMappingPopupContext
                 * @property {RuleWord|null} ruleWord
                 */

                /**
                 * 弹出 Simple 属性的值匹配窗
                 * @param {SimpleItemMappingPopupContext} context
                 * @returns {Promise.<RuleWord>}
                 */
                item: function (context) {
                    return openModel(popActions.platformMapping.simple.item, context);
                }
            },

            multiComplex: {

                list: function (context) {
                    return openModel(popActions.platformMapping.multiComplex.list, context);
                },

                item: function (context) {
                    return openModel(popActions.platformMapping.multiComplex.item, context);
                }
            }
        };
    }
});