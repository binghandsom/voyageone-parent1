/**
 * Created by linanbin on 15/12/7.
 */

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
 * Simple Mapping List 设定弹出框的上下文参数
 * @name SimpleListMappingPopupContext
 * @class
 * @property {string} platformCategoryPath 平台类目路径
 * @property {string} platformCategoryId 平台类目 ID
 * @property {string} mainCategoryId 主数据类目 ID
 * @property {Array} path
 * @property {number} cartId 平台 ID
 * @property {Field} property 平台属性
 * @property {number|null} valueIndex
 */

/**
 * @name SimpleItemMappingPopupContext
 * @class
 * @extends SimpleListMappingPopupContext
 * @property {RuleWord|null} ruleWord
 */

define([
    'cms',
    'underscore',
    'modules/cms/enums/MappingTypes'
], function (cms, _, MappingTypes) {

    cms.constant('popActions', {
            "authority": {
                "new": {
                    "templateUrl": "views/pop/authority/new.tpl.html",
                    "controllerUrl": "modules/cms/views/pop/authority/new.ctl",
                    "controller": 'popAuthorityNewCtl'
                }
            },
        "addattributevalue": {
            "new": {
                "templateUrl": "views/pop/addattributevalue/add.tpl.html",
                "controllerUrl": "modules/cms/views/pop/addattributevalue/add.ctl",
                "controller": 'popAddAttributeValueCtl'
            }
        },
        "addattributevaluenew": {
            "new": {
                "templateUrl": "views/pop/addattributevalue/new.tpl.html",
                "controllerUrl": "modules/cms/views/pop/addattributevalue/new.ctl",
                "controller": 'popAddAttributeValueNewCtl'
            }
        },
        "addattributevaluenews": {
            "new": {
                "templateUrl": "views/pop/addattributevalue/news.tpl.html",
                "controllerUrl": "modules/cms/views/pop/addattributevalue/news.ctl",
                "controller": 'popAddAttributeValueNewsCtl'
            }
        },
            "bulkUpdate": {
                "addToPromotion": {
                    "templateUrl": "views/pop/bulkUpdate/addToPromotion.tpl.html",
                    "controllerUrl": "modules/cms/views/pop/bulkUpdate/addToPromotion.ctl",
                    "controller": 'popAddToPromotionCtl'
                },
                "fieldEdit": {
                    "templateUrl": "views/pop/bulkUpdate/fieldEdit.tpl.html",
                    "controllerUrl": "modules/cms/views/pop/bulkUpdate/fieldEdit.ctl",
                    "controller": 'popFieldEditCtl'
                },
                "category": {
                    "templateUrl": "views/pop/bulkUpdate/masterCategory.tpl.html",
                    "controllerUrl": "modules/cms/views/pop/bulkUpdate/masterCategory.ctl",
                    "controller": 'popCategoryCtl as ctrl',
                    "backdrop": 'static',
                    "size": 'lg'
                }
            },
            "category": {
                "schema": {
                    "templateUrl": "views/pop/category/schema.tpl.html",
                    "controllerUrl": "modules/cms/views/pop/category/schema.ctl",
                    "controller": 'popCategorySchemaCtl as ctrl',
                    "backdrop": 'static',
                    "size": 'md'
                }
            },
            "configuration": {
                "new": {
                    "templateUrl": "views/pop/configuration/new.tpl.html",
                    "controllerUrl": "modules/cms/views/pop/configuration/new.ctl",
                    "controller": 'popConfigurationNewCtl'
                }
            },
            "dictionary": {
                "value": {
                    "templateUrl": "views/pop/dictionary/value.tpl.html",
                    "controllerUrl": "modules/cms/views/pop/dictionary/value.ctl",
                    "controller": "popDictValueCtl"
                },
                "custom": {
                    "templateUrl": "views/pop/dictionary/custom.tpl.html",
                    "controllerUrl": "modules/cms/views/pop/dictionary/custom.ctl",
                    "controller": "popDictCustomCtl"
                }
            },
            "feedMapping": {
                "attribute": {
                    "templateUrl": "views/pop/feedMapping/attribute.tpl.html",
                    "controllerUrl": "modules/cms/views/pop/feedMapping/attribute.ctl",
                    "controller": 'propFeedMappingAttributeController as ctrl',
                    "backdrop": 'static',
                    "size": 'lg'
                },
                "dictValue": {
                    "templateUrl": "views/pop/feedMapping/dictValue.tpl.html",
                    "controllerUrl": "modules/cms/views/pop/feedMapping/dictValue.ctl",
                    "controller": 'propFeedMappingDictValueController as ctrl',
                    "backdrop": 'static',
                    "size": 'md'
                },
                "value": {
                    "templateUrl": "views/pop/feedMapping/value.tpl.html",
                    "controllerUrl": "modules/cms/views/pop/feedMapping/value.ctl",
                    "controller": 'propFeedMappingValueController as ctrl',
                    "backdrop": 'static',
                    "size": 'xlg'
                }
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
                },
                "otherPlatform": {
                    "templateUrl": "views/pop/platformMapping/ppOtherPlatform.tpl.html",
                    "controllerUrl": "modules/cms/views/pop/platformMapping/ppOtherPlatform.ctl",
                    "controller": 'otherPlatformPopupController as ctrl',
                    "size": 'md'
                }
            },
            "field": {
                "customColumn": {
                    "templateUrl": "views/pop/custom/column.tpl.html",
                    "controllerUrl": "modules/cms/views/pop/custom/column.ctl",
                    "controller": 'popFieldColumnCtl'
                },
                "feedDetail": {
                    "templateUrl": "views/pop/field/feedDetail.tpl.html",
                    "controllerUrl": "modules/cms/views/pop/field/feedDetail.ctl",
                    "controller": 'popFieldFeedDetailCtl'
                }
            },
            "file": {
                "import": {
                    "templateUrl": "views/pop/file/import.tpl.html",
                    "controllerUrl": "modules/cms/views/pop/file/import.ctl",
                    "controller": 'popFileImportCtl'
                }
            },
            "history": {
                "price": {
                    "templateUrl": "views/pop/history/price.tpl.html",
                    "controllerUrl": "modules/cms/views/pop/history/price.ctl",
                    "controller": "popPriceHistoryCtl"
                },
                "promotion": {
                    "templateUrl": "views/pop/history/promotion.tpl.html",
                    "controllerUrl": "modules/cms/views/pop/history/promotion.ctl",
                    "controller": "popPromotionHistoryCtl"
                }
            },
            "promotion": {
                "detail": {
                    "templateUrl": "views/pop/promotion/detail.tpl.html",
                    "controllerUrl": "modules/cms/views/pop/promotion/detail.ctl",
                    "controller": 'popPromotionDetailCtl'
                },
                "newBeat": {
                    "templateUrl": "views/pop/promotion/newBeatTask.tpl.html",
                    "controllerUrl": "modules/cms/views/pop/promotion/newBeatTask.ctl",
                    "controller": 'popNewBeatCtl as $ctrl',
                    "size": "md"
                },
                "addBeat": {
                    "templateUrl": "views/pop/promotion/addBeat.tpl.html",
                    "controllerUrl": "modules/cms/views/pop/promotion/addBeat.ctl",
                    "controller": 'popAddBeatCtl as $ctrl',
                    "size": "md"
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

        /**
         * 打开新建权限页面
         * @type {openAuthority}
         */
        $scope.openAuthority = openAuthority;
        function openAuthority(viewSize, data) {
            require([popActions.authority.new.controllerUrl], function () {
                $modal.open({
                    templateUrl: popActions.authority.new.templateUrl,
                    controller: popActions.authority.new.controller,
                    size: viewSize,
                    resolve: {
                        data: function () {
                            return data;
                        }
                    }
                });
            });
        }

        /**
         * 新增属性值
         * @type {openAddattributevalueNew}
         */
        $scope.openAddattributevaluenew = openAddattributevaluenew;
        function openAddattributevaluenew(viewSize, data) {
            require([popActions.addattributevaluenew.new.controllerUrl], function () {
                $modal.open({
                    templateUrl: popActions.addattributevaluenew.new.templateUrl,
                    controller: popActions.addattributevaluenew.new.controller,
                    size: viewSize,
                    resolve: {
                        data: function () {
                            return data;
                        }
                    }
                });
            });
        }
        $scope.openAddattributevaluenews = openAddattributevaluenews;
        function openAddattributevaluenews(viewSize, data) {
            require([popActions.addattributevaluenews.new.controllerUrl], function () {
                $modal.open({
                    templateUrl: popActions.addattributevaluenews.new.templateUrl,
                    controller: popActions.addattributevaluenews.new.controller,
                    size: viewSize,
                    resolve: {
                        data: function () {
                            return data;
                        }
                    }
                });
            });
        }
        $scope.openAddattributevalue = openAddattributevalue;
        function openAddattributevalue(viewSize, data) {
            require([popActions.addattributevalue.new.controllerUrl], function () {
                $modal.open({
                    templateUrl: popActions.addattributevalue.new.templateUrl,
                    controller: popActions.addattributevalue.new.controller,
                    size: viewSize,
                    resolve: {
                        data: function () {
                            return data;
                        }
                    }
                });
            });
        }

        /**
         * pop出promotion选择页面,用于设置
         * @type {openAddToPromotion}
         */
        $scope.openAddToPromotion = openAddToPromotion;
        function openAddToPromotion(viewSize, promotion, selList, fnInitial) {
            require([popActions.bulkUpdate.addToPromotion.controllerUrl], function () {
                if (selList && selList.length) {
                    var modalInstance = $modal.open({
                        templateUrl: popActions.bulkUpdate.addToPromotion.templateUrl,
                        controller: popActions.bulkUpdate.addToPromotion.controller,
                        size: viewSize,
                        resolve: {
                            promotion: function () {
                                //var productIds = [];
                                //_.forEach(selList, function (object) {
                                //    productIds.push(object);
                                //});
                                var productIds = [];
                                _.forEach(selList, function (object) {
                                    productIds.push(object.id);
                                });
                                return {"promotion": promotion, "productIds": productIds, "products": selList};
                            }
                        }
                    });

                    // 回调主页面的刷新操作
                    modalInstance.result.then(function () {
                        fnInitial();
                    })
                } else {
                    alert($translate.instant('TXT_MSG_NO_ROWS_SELECT'));
                }
            });
        }

        /**
         * pop出properties变更页面,用于批量更新产品属性
         * @type {openupdateProperties}
         */
        $scope.openBulkUpdate = openBulkUpdate;
        function openBulkUpdate(viewSize, selList, fnInitial) {
            require([popActions.bulkUpdate.fieldEdit.controllerUrl], function () {
                if (selList && selList.length) {
                    var modalInstance = $modal.open({
                        templateUrl: popActions.bulkUpdate.fieldEdit.templateUrl,
                        controller: popActions.bulkUpdate.fieldEdit.controller,
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
                    alert($translate.instant('TXT_MSG_NO_ROWS_SELECT'));
                }
            });
        }

        /**
         * 打开类目选择页面
         * @param context
         * @returns {*}
         */
        $scope.popupNewCategory = function (context) {
            return openModel(popActions.bulkUpdate.category, context);
        };

        /**
         * 打开类目属性编辑页面
         * @param context
         * @returns {*}
         */
        $scope.openSystemCategory = function (context) {
            return openModel(popActions.category.schema, context);
        };

        /**
         * 打开新增配置页面
         * @type {openConfiguration}
         */
        $scope.openConfiguration = openConfiguration;
        function openConfiguration(viewSize, data) {
            require([popActions.configuration.new.controllerUrl], function () {
                $modal.open({
                    templateUrl: popActions.configuration.new.templateUrl,
                    controller: popActions.configuration.new.controller,
                    size: viewSize,
                    resolve: {
                        data: function () {
                            return data;
                        }
                    }
                });
            });
        }

        /**
         * 添加字典值页面
         * @type {openDictValue}
         */
        $scope.openDictValue = openDictValue;
        function openDictValue(viewSize, fnInitial, $index, data) {
            require([popActions.dictionary.value.controllerUrl], function () {
                var modalInstance = $modal.open({
                    templateUrl: popActions.dictionary.value.templateUrl,
                    controller: popActions.dictionary.value.controller,
                    size: viewSize,
                    resolve: {
                        dictValue: function () {
                            return data;
                        }
                    }
                });

                // 回调主页面的刷新操作
                modalInstance.result.then(function (data) {
                    fnInitial(data, $index);
                })
            });
        }

        /**
         * 添加字典自定义页面
         * @type {openDictCustom}
         */
        $scope.openDictCustom = openDictCustom;
        function openDictCustom(viewSize, fnInitial, $index, data) {
            require([popActions.dictionary.custom.controllerUrl], function () {
                var modalInstance = $modal.open({
                    templateUrl: popActions.dictionary.custom.templateUrl,
                    controller: popActions.dictionary.custom.controller,
                    size: viewSize,
                    resolve: {
                        customValue: function () {
                            return data;
                        }
                    }
                });

                // 回调主页面的刷新操作
                modalInstance.result.then(function (data) {
                    fnInitial(data, $index);
                })
            });
        }

        $scope.popupFeed = function (context) {
            return openModel(popActions.feedMapping.attribute, context);
        };

        $scope.popupFeedValue = function (context) {
            return openModel(popActions.feedMapping.value, context);
        };

        $scope.popupDictValue = function (context) {
            return openModel(popActions.feedMapping.dictValue, context);
        };

        $scope.openOtherPlatform = function (context) {
            return openModel(popActions.platformMapping.otherPlatform, context);
        };

        $scope.ppPlatformMapping = function (context) {

            var last = context.path[0];
            var mapping;
            var config;

            if (_.isNumber(last)) {
                config = popActions.platformMapping.multiComplex.item;
                return openModel(config, context);
            }

            mapping = last.mapping;

            switch (mapping.type) {
                case MappingTypes.SIMPLE_MAPPING:
                    config = popActions.platformMapping.simple.list;
                    break;
                case MappingTypes.COMPLEX_MAPPING:
                    config = popActions.platformMapping.complex;
                    break;
                case MappingTypes.MULTI_COMPLEX_MAPPING:
                    config = popActions.platformMapping.multiComplex.list;
                    break;
                default:
                    throw 'Unknown mapping type: ' + mapping.type;
            }

            return openModel(config, context);
        };

        $scope.ppPlatformMapping.simpleItem = function(context) {
            return openModel(popActions.platformMapping.simple.item, context);
        };

        /**
         * 打开选择base property页面
         * @type {openCustomBaseProperty}
         */
        $scope.openCustomBaseProperty = openCustomBaseProperty;
        function openCustomBaseProperty(viewSize) {
            $modal.open({
                templateUrl: popActions.field.customColumn.templateUrl,
                controllerUrl: popActions.field.customColumn.controllerUrl,
                size: viewSize,
                resolve: {
                    items: function () {
                        //return data;
                    }
                }
            });
        }

        /**
         * 打开翻译使用的第三方属性页面
         * @type {openTranslate}
         */
        $scope.openTranslate = openTranslate;
        function openTranslate(viewSize, data) {
            require([popActions.field.feedDetail.controllerUrl], function () {
                $modal.open({
                    templateUrl: popActions.field.feedDetail.templateUrl,
                    controller: popActions.field.feedDetail.controller,
                    size: viewSize,
                    resolve: {
                        data: function () {
                            return data;
                        }
                    }
                });
            });
        }

        /**
         * 打开导入文件页面
         * @type {openImport}
         */
        $scope.openImport = openImport;
        function openImport(viewSize, data) {
            require([popActions.file.import.controllerUrl], function () {
                $modal.open({
                    templateUrl: popActions.file.import.templateUrl,
                    controller: popActions.file.import.controller,
                    size: viewSize,
                    resolve: {
                        data: function () {
                            return data;
                        }
                    }
                });
            });
        }

        /**
         * 打开promotion历史页面
         * @type {openHistoryPromotion}
         */
        $scope.openHistoryPromotion = openHistoryPromotion;
        function openHistoryPromotion(viewSize, data) {
            require([popActions.history.promotion.controllerUrl], function () {
                $modal.open({
                    templateUrl: popActions.history.promotion.templateUrl,
                    controller: popActions.history.promotion.controller,
                    size: viewSize,
                    resolve: {
                        data: function () {
                            return data;
                        }
                    }
                });
            });
        }

        /**
         * 打开price历史页面
         * @type {openHistoryPrice}
         */
        $scope.openHistoryPrice = openHistoryPrice;
        function openHistoryPrice(viewSize, data, type) {
            require([popActions.history.price.controllerUrl], function () {
                $modal.open({
                    templateUrl: popActions.history.price.templateUrl,
                    controller: popActions.history.price.controller,
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
        /**
         * 打开promotion页面
         * @type {openPromotion}
         */
        $scope.openPromotion = openPromotion;
        function openPromotion(viewSize, cartList, data, fnInitial) {
            require([popActions.promotion.detail.controllerUrl], function () {
                var modalInstance = $modal.open({
                    templateUrl: popActions.promotion.detail.templateUrl,
                    controller: popActions.promotion.detail.controller,
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

        /**
         * 打开promotion页面
         */
        $scope.openTask = function (context) {
            return openModel(popActions.promotion.newBeat, context);
        };

        $scope.popAddBeat = function (context) {
            return openModel(popActions.promotion.addBeat, context);
        };


        //$scope.openshop_category = openshop_category;
        //function openshop_category(viewSize) {
        //    $modal.open({
        //        templateUrl: popActions.tag.shop_category.templateUrl,
        //        controllerUrl: popActions.tag.shop_category.controllerUrl,
        //        size: viewSize,
        //        resolve: {
        //            items: function () {
        //                //return data;
        //            }
        //        }
        //    });
        //}
        //
        //$scope.openOtherProgress = openOtherProgress;
        //function openOtherProgress(viewSize) {
        //    $modal.open({
        //        templateUrl: popActions.other.progress.templateUrl,
        //        controllerUrl: popActions.other.progress.controllerUrl,
        //        size: viewSize,
        //        resolve: {
        //            items: function () {
        //                //return data;
        //            }
        //        }
        //    });
        //}
    }
});