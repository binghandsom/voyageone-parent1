/**
 * 统一管理弹出框的定义信息
 * Created by LinAn.Bin on 15/12/7.
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
                "controller": 'popAddToPromotionCtl',
                "backdrop": 'static',
                "size": 'md'
            },
            "fieldEdit": {
                "templateUrl": "views/pop/bulkUpdate/fieldEdit.tpl.html",
                "controllerUrl": "modules/cms/views/pop/bulkUpdate/fieldEdit.ctl",
                "controller": 'popFieldEditCtl',
                "backdrop": 'static',
                "size": 'md'
            },
            "category": {
                "templateUrl": "views/pop/bulkUpdate/masterCategory.tpl.html",
                "controllerUrl": "modules/cms/views/pop/bulkUpdate/masterCategory.ctl",
                "controller": 'popCategoryCtl as ctrl',
                "backdrop": 'static',
                "size": 'lg'
            },
            "categoryNew": {
                "templateUrl": "views/pop/bulkUpdate/masterCategoryNew.tpl.html",
                "controllerUrl": "modules/cms/views/pop/bulkUpdate/masterCategoryNew.ctl",
                "controller": 'popCategoryNewCtl as ctrl',
                "backdrop": 'static',
                "size": 'lg'
            },
            "addChannelCategory": {
                "templateUrl": "views/pop/search/addChannelCategory.tpl.html",
                "controllerUrl": "modules/cms/views/pop/search/addChannelCategory.ctl",
                "controller": 'popAddChannelCategoryCtrl as ctrl'
            },
            "putOnOff": {
                "templateUrl": "views/pop/bulkUpdate/putonoff.tpl.html",
                "controllerUrl": "modules/cms/views/pop/bulkUpdate/putonoff.ctl",
                "controller": 'popPutOnOffCtl as ctrl',
                "size": 'md'
            },
            "freeTag": {
                "templateUrl": "views/pop/bulkUpdate/freetag.tpl.html",
                "controllerUrl": "modules/cms/views/pop/bulkUpdate/freetag.ctl",
                "controller": 'popFreeTagCtl as ctrl',
                "size": 'lg'
            },
            "salePrice": {
                "templateUrl": "views/pop/bulkUpdate/salePrice.tpl.html",
                "controllerUrl": "modules/cms/views/pop/bulkUpdate/salePrice.ctl",
                "controller": 'popSalePriceCtl',
                "size": 'md'
            },
            "updateProductApproval": {
                "templateUrl": "views/pop/bulkUpdate/updateProductApproval.tpl.html",
                "controllerUrl": "modules/cms/views/pop/bulkUpdate/updateProductApproval.ctl",
                "controller": 'popUpdateProductApprovalCtl as ctrl',
                "size": 'md'
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
        "channel": {
            "categorySetting": {
                "templateUrl": "views/pop/channel/categorySetting.tpl.html",
                "controllerUrl": "modules/cms/views/pop/channel/categorySetting.ctl",
                "controller": 'categorySettingCtl as ctrl'
            },
            "newCategory": {
                "templateUrl": "views/pop/channel/newCategory.tpl.html",
                "controllerUrl": "modules/cms/views/pop/channel/newCategory.ctl",
                "controller": 'newCategoryCtl as ctrl'
            }
        },
        "custom": {
            "newAttribute": {
                "templateUrl": "views/pop/custom/newAttribute.tpl.html",
                "controllerUrl": "modules/cms/views/pop/custom/newAttribute.ctl",
                "controller": 'popAddAttributeValueCtl as ctrl',
                "backdrop": 'static',
                "size": 'md'
            },
            "newValue": {
                "templateUrl": "views/pop/custom/newValue.tpl.html",
                "controllerUrl": "modules/cms/views/pop/custom/newValue.ctl",
                "controller": 'popAddAttributeValueNewCtl as ctrl',
                "backdrop": 'static',
                "size": 'md'
            },
            "column": {
                "templateUrl": "views/pop/custom/column.tpl.html",
                "controllerUrl": "modules/cms/views/pop/custom/column.ctl",
                "controller": 'popCustomColumnCtl'
            },
            "addtaglist": {
                "templateUrl": "views/pop/custom/addtaglist.tpl.html",
                "controllerUrl": "modules/cms/views/pop/custom/addtaglist.ctl",
                "controller": 'popAddTagListCtl'
            },
            "storeoperation": {
                "templateUrl": "views/pop/custom/storeoperation.tpl.html",
                "controllerUrl": "modules/cms/views/pop/custom/storeoperation.ctl",
                "controller": 'popStoreOperationCtl'
            },
            "confirmstoreopp": {
                "templateUrl": "views/pop/custom/confirmstoreopp.tpl.html",
                "controllerUrl": "modules/cms/views/pop/custom/confirmstoreopp.ctl",
                "controller": 'popConfirmStoreOppCtl'
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
            },
            "stock": {
                "templateUrl": "views/pop/file/stockImport.tpl.html",
                "controllerUrl": "modules/cms/views/pop/file/stockImport.ctl",
                "controller": 'popFileStockImportCtl'
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
                "controller": 'popNewBeatCtl as ctrl',
                "size": "md"
            },
            "addBeat": {
                "templateUrl": "views/pop/promotion/addBeat.tpl.html",
                "controllerUrl": "modules/cms/views/pop/promotion/addBeat.ctl",
                "controller": 'popAddBeatCtl as ctrl',
                "size": "md"
            },
            "newMrbStock": {
                "templateUrl": "views/pop/promotion/newMrbStock.tpl.html",
                "controllerUrl": "modules/cms/views/pop/promotion/newMrbStock.ctl",
                "controller": 'popNewMrbStockCtl'
            },
            "newMrbStockSku": {
                "templateUrl": "views/pop/promotion/newMrbStockSku.tpl.html",
                "controllerUrl": "modules/cms/views/pop/promotion/newMrbStockSku.ctl",
                "controller": 'popNewMrbStockSkuCtl as ctrl',
                "size": "md"
            },
            "skuMrbStockDetail": {
                "templateUrl": "views/pop/promotion/skuMrbStockDetail.tpl.html",
                "controllerUrl": "modules/cms/views/pop/promotion/skuMrbStockDetail.ctl",
                "controller": 'popSkuMrbStockDetailCtl as ctrl'
            },
            "addMrbStockIncrement": {
                "templateUrl": "views/pop/promotion/addStockIncrement.tpl.html",
                "controllerUrl": "modules/cms/views/pop/promotion/addStockIncrement.ctl",
                "controller": 'popAddStockIncrementCtl'
            }
        },
        "jumei": {
            "jmPromotionDefaultSetting": {
                "batch": {
                    "templateUrl": "views/pop/jm/promotiondefaultsetting.tpl.html",
                    "controllerUrl": "modules/cms/views/pop/jm/promotiondefaultsetting.ctl",
                    "controller": 'popJmPromotionDefaultSettingCtl as ctrl',
                    "size": 'md',
                    "backdrop": "static"
                }
            },
            "jmProductDetail": {
                "detail": {
                    "templateUrl": "views/pop/jm/productdetail.tpl.html",
                    "controllerUrl": "modules/cms/views/pop/jm/productdetail.ctl",
                    "controller": 'popJmProductDetailController as ctrl',
                    "size": 'lg',
                    "backdrop": "static"
                }
            },
            "jmPromotionDetail": {
                "detail": {
                    "templateUrl": "views/pop/jm/promotiondetail.tpl.html",
                    "controllerUrl": "modules/cms/views/pop/jm/promotiondetail.ctl",
                    "controller": 'popJMPromotionDetailCtl',
                    "size": 'md',
                    "backdrop": "static"
                },
                "import": {
                    "templateUrl": "views/pop/jm/import.tpl.html",
                    "controllerUrl": "modules/cms/views/pop/jm/import.ctl",
                    "controller": 'popPromotionDetailImportCtl',
                    "size": 'md',
                    "backdrop": "static"
                },
                "dealExtension": {
                    "templateUrl": "views/pop/jm/dealextension.tpl.html",
                    "controllerUrl": "modules/cms/views/pop/jm/dealextension.ctl",
                    "controller": 'popDealExtensionCtl'
                },
                "priceModify": {
                    "templateUrl": "views/pop/jm/pricemodify.tpl.html",
                    "controllerUrl": "modules/cms/views/pop/jm/pricemodify.ctl",
                    "controller": 'popPriceModifyCtl'
                }
            },
            "jmImageManage": {
                "imageSetting": {
                    "templateUrl": "views/pop/jm/imagesetting.tpl.html",
                    "controllerUrl": "modules/cms/views/pop/jm/imagesetting.ctl",
                    "controller": 'popImageSettingCtl as ctrl'
                }
            }
        },
        "search": {
            "joinJM": {
                "templateUrl": "views/pop/search/joinJM.tpl.html",
                "controllerUrl": "modules/cms/views/pop/search/joinJM.ctl",
                "controller": 'popJoinJMCtl as ctrl',
                "backdrop": 'static',
                "size": 'md'
            },
            "imagedetail": {
                "templateUrl": "views/pop/search/imagedetail.tpl.html",
                "controllerUrl": "modules/cms/views/pop/search/imagedetail.ctl",
                "controller": 'popImageDetailCtl'
            },
            "codeDetail": {
                "templateUrl": "views/pop/search/codeDetail.tpl.html",
                "controllerUrl": "modules/cms/views/pop/search/codeDetail.ctl",
                "controller": 'popCodeDetailCtl'
            }

        },
        "system": {
            "channelsetting": {
                "templateUrl": "views/pop/system/channelsetting.tpl.html",
                "controllerUrl": "modules/cms/views/pop/system/channelsetting.ctl",
                "controller": 'popChannelSettingCtl'
            },
            "channeledit": {
                "templateUrl": "views/pop/system/channeledit.tpl.html",
                "controllerUrl": "modules/cms/views/pop/system/channeledit.ctl",
                "controller": 'popChannelEditCtl'
            },
            "cartList": {
                "templateUrl": "views/pop/system/cartList.tpl.html",
                "controllerUrl": "modules/cms/views/pop/system/cartList.ctl",
                "controller": 'popCartListCtl'
            },
            "channelList": {
                "templateUrl": "views/pop/system/channelList.tpl.html",
                "controllerUrl": "modules/cms/views/pop/system/channelList.ctl",
                "controller": 'popChannelListCtl'
            }
        },
        "store": {
            "listing": {
                "sizechart": {
                    "templateUrl": "views/pop/store/listing/sizechart.tpl.html",
                    "controllerUrl": "modules/cms/views/pop/store/listing/sizechart.ctl",
                    "controller": 'popSizeChartCtl'
                },
                "sizechartimport": {
                    "templateUrl": "views/pop/store/listing/sizechartimport.tpl.html",
                    "controllerUrl": "modules/cms/views/pop/store/listing/sizechartimport.ctl",
                    "controller": 'popSizeChartImportCtl'
                },
                "imagetemplate": {
                    "templateUrl": "views/pop/store/listing/imagetemplate.tpl.html",
                    "controllerUrl": "modules/cms/views/pop/store/listing/imagetemplate.ctl",
                    "controller": 'popImageTemplateCtl'
                },
                "imagetemplatepreview": {
                    "templateUrl": "views/pop/store/listing/imagetemplatepreview.tpl.html",
                    "controllerUrl": "modules/cms/views/pop/store/listing/imagetemplatepreview.ctl",
                    "controller": 'popImageTemplatePreviewCtl'
                },
                "imagegroupadd": {
                    "templateUrl": "views/pop/store/listing/imagegroupadd.tpl.html",
                    "controllerUrl": "modules/cms/views/pop/store/listing/imagegroupadd.ctl",
                    "controller": 'popImageGroupAddCtl as ctrl'
                },
                "imagegroupimg": {
                    "templateUrl": "views/pop/store/listing/imagegroupimg.tpl.html",
                    "controllerUrl": "modules/cms/views/pop/store/listing/imagegroupimg.ctl",
                    "controller": 'popImageGroupImgCtl'
                },
                "imagedetailadd": {
                    "templateUrl": "views/pop/store/listing/imagedetailadd.tpl.html",
                    "controllerUrl": "modules/cms/views/pop/store/listing/imagedetailadd.ctl",
                    "controller": 'popImageDetailAddCtl'
                }
            }
        },
        "image": {
            "upload": {
                "templateUrl": "views/pop/image/imgSetting.tpl.html",
                "controllerUrl": "modules/cms/views/pop/image/imgSetting.ctl",
                "controller": 'popImgSettingCtl'
            }
        }
    }).controller('popupCtrl', function popupCtrl($scope, $uibModal, popActions, $q) {

        function openModel(config, context) {

            // if (context)
            config.resolve = {
                context: function () {
                    return context;
                }
            };

            var defer = $q.defer();
            require([config.controllerUrl], function () {

                defer.resolve($uibModal.open(config).result);
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
                $uibModal.open({
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
         */
        $scope.openAddattributevaluenew = openAddattributevaluenew;
        function openAddattributevaluenew(viewSize, data) {
            require([popActions.addattributevaluenew.new.controllerUrl], function () {
                $uibModal.open({
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
                $uibModal.open({
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
                $uibModal.open({
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
        $scope.openAddToPromotion = function (promotion, selList, context) {
            var productIds = [];
            if (context && context.isSelAll) {
                // 全选
            } else {
                if (selList && selList.length) {
                    _.forEach(selList, function (object) {
                        productIds.push(object.id);
                    });
                }
            }
            return openModel(popActions.bulkUpdate.addToPromotion, {
                "promotion": promotion,
                "productIds": productIds,
                "products": selList
            });
        };

        /**
         * pop出properties变更页面,用于批量更新产品属性
         */
        $scope.openFieldEdit = function (selList, context) {
            var productIds = [];
            var params = null;
            if (context && context.isSelAll) {
                // 全选
                params = {"productIds": productIds, 'isSelAll': 1};
            } else {
                if (selList && selList.length) {
                    _.forEach(selList, function (object) {
                        productIds.push(object.code);
                    });
                }
                params = {"productIds": productIds};
            }
            return openModel(popActions.bulkUpdate.fieldEdit, params);
        };

        //$scope.openBulkUpdate = openBulkUpdate;
        //function openBulkUpdate(selList, fnInitial) {
        //    require([popActions.bulkUpdate.fieldEdit.controllerUrl], function () {
        //        if (selList && selList.length) {
        //            var modalInstance = $uibModal.open({
        //                templateUrl: popActions.bulkUpdate.fieldEdit.templateUrl,
        //                controller: popActions.bulkUpdate.fieldEdit.controller,
        //                size: 'md',
        //                resolve: {
        //                    productIds: function () {
        //                        var productIds = [];
        //                        _.forEach(selList, function (object) {
        //                            productIds.push(object.id);
        //                        });
        //                        return productIds;
        //                    }
        //                }
        //            });
        //
        //            // 回调主页面的刷新操作
        //            modalInstance.result.then(function () {
        //                fnInitial();
        //            })
        //
        //        } else {
        //            alert($translate.instant('TXT_MSG_NO_ROWS_SELECT'));
        //        }
        //    });
        //}

        /**
         * 打开类目选择页面
         * @param context
         * @returns {*}
         */
        $scope.popupNewCategory = function (context) {
            return openModel(popActions.bulkUpdate.category, context);
        };

        /**
         * 打开新的类目选择页面
         * @param context
         * @returns {*}
         */
        $scope.popupCategoryNew = function (context) {
            return openModel(popActions.bulkUpdate.categoryNew, context);
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
         * 打开添加自定义属性编辑页面
         * @param context
         * @returns {*}
         */
        $scope.openAddAttribute = function (context) {
            return openModel(popActions.custom.newAttribute, context);
        };

        // function openAddToPromotion(viewSize, promotion, selList, fnInitial) {
        //    require([popActions.bulkUpdate.addToPromotion.controllerUrl], function () {
        //        if (selList && selList.length) {
        //            var modalInstance = $uibModal.open({
        //                templateUrl: popActions.bulkUpdate.addToPromotion.templateUrl,
        //                controller: popActions.bulkUpdate.addToPromotion.controller,
        //                size: viewSize,
        //                resolve: {
        //                    promotion: function () {
        //                        //var productIds = [];
        //                        //_.forEach(selList, function (object) {
        //                        //    productIds.push(object);
        //                        //});
        //                        var productIds = [];
        //                        _.forEach(selList, function (object) {
        //                            productIds.push(object.id);
        //                        });
        //                        return {"promotion": promotion, "productIds": productIds, "products": selList};
        //                    }
        //                }
        //            });
        //
        //            // 回调主页面的刷新操作
        //            modalInstance.result.then(function () {
        //                fnInitial();
        //            })
        //        } else {
        //            alert($translate.instant('TXT_MSG_NO_ROWS_SELECT'));
        //        }
        //    });
        //}

        /**
         * 新增advance查询页,参加聚美活动弹出
         * */
        $scope.openAddJMActivity = function (promotion, selList, context) {
            if (context && context.isSelAll) {
                // 全选
                return openModel(popActions.search.joinJM, {promotion: promotion, products: [], 'isSelAll': 1});
            } else {
                if (selList && selList.length) {
                    return openModel(popActions.search.joinJM, {promotion: promotion, products: selList});
                }
            }
        };

        /**
         * 打开添加自定义属性-值，编辑页面
         * @param context
         * @returns {*}
         */
        $scope.openAddNewValue = function (context) {
            return openModel(popActions.custom.newValue, context);
        };

        /**
         * 打开新增配置页面
         * @type {openConfiguration}
         */
        $scope.openConfiguration = openConfiguration;
        function openConfiguration(viewSize, data) {
            require([popActions.configuration.new.controllerUrl], function () {
                $uibModal.open({
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
                var modalInstance = $uibModal.open({
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
                var modalInstance = $uibModal.open({
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

        $scope.ppPlatformMapping.simpleItem = function (context) {
            return openModel(popActions.platformMapping.simple.item, context);
        };

        /**
         * 打开选择base property页面
         * @type {openCustomBaseProperty}
         */
        $scope.openCustomBaseProperty = openCustomBaseProperty;
        function openCustomBaseProperty(viewSize) {
            $uibModal.open({
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
                $uibModal.open({
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
        function openImport(viewSize, data, fnInitial) {
            require([popActions.file.import.controllerUrl], function () {
                var modalInstance = $uibModal.open({
                    templateUrl: popActions.file.import.templateUrl,
                    controller: popActions.file.import.controller,
                    size: viewSize,
                    resolve: {
                        data: function () {
                            return data;
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
         * 打开promotion历史页面
         * @type {openHistoryPromotion}
         */
        $scope.openHistoryPromotion = openHistoryPromotion;
        function openHistoryPromotion(viewSize, data) {
            require([popActions.history.promotion.controllerUrl], function () {
                $uibModal.open({
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

        //全店操作页面中，操作按钮弹出
        $scope.openNewOpp = function (header, upLoadFlag) {
            return openModel(popActions.custom.storeoperation, {header: header, upLoadFlag: upLoadFlag});
        };
        //全店操作页面中，确认操作按钮弹出
        $scope.openConfirmOpp = function (header, upLoadFlag) {
            return openModel(popActions.custom.confirmstoreopp, {header: header, upLoadFlag: upLoadFlag});
        };
        /**
         * 打开price历史页面
         * @type {openHistoryPrice}
         */
        $scope.openHistoryPrice = openHistoryPrice;
        function openHistoryPrice(viewSize, data, type) {
            require([popActions.history.price.controllerUrl], function () {
                $uibModal.open({
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
                var modalInstance = $uibModal.open({
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
         * @type {openPromotion}
         */
        $scope.openImageSetting = function (context) {
            return openModel(popActions.image.upload, context);
        };
        //function openImageSetting(viewSize, product, imageType, fnInitial) {
        //    require([popActions.image.setting.controllerUrl], function () {
        //        var modalInstance = $uibModal.open({
        //            templateUrl: popActions.image.setting.templateUrl,
        //            controller: popActions.image.setting.controller,
        //            size: viewSize,
        //            resolve: {
        //                product: function () {
        //                    return product;
        //                },
        //                imageType: function () {
        //                    return imageType;
        //                }
        //            }
        //        });
        //
        //        modalInstance.result.then(function (object) {
        //            if (fnInitial) {
        //                fnInitial(object);
        //            }
        //
        //        })
        //    });
        //}
        /**
         * 打开promotion页面
         */
        $scope.openNewBeatTask = function (context) {
            return openModel(popActions.promotion.newBeat, context);
        };

        $scope.popAddBeat = function (context) {
            return openModel(popActions.promotion.addBeat, context);
        };

        /**
         * 打开新建库存隔离任务
         * @type {openMrbStock}
         */
        $scope.openMrbStock = openMrbStock;
        function openMrbStock(viewSize, data) {
            require([popActions.promotion.newMrbStock.controllerUrl], function () {
                $uibModal.open({
                    templateUrl: popActions.promotion.newMrbStock.templateUrl,
                    controller: popActions.promotion.newMrbStock.controller,
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
         * 导入库存隔离数据
         * @type {openImportStock}
         */
        $scope.openImportStock = openImportStock;
        function openImportStock(viewSize, data) {
            require([popActions.file.stock.controllerUrl], function () {
                $uibModal.open({
                    templateUrl: popActions.file.stock.templateUrl,
                    controller: popActions.file.stock.controller,
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
         * 新增一个sku的库存隔离
         */
        $scope.openNewMrbStockSku = openNewMrbStockSku;
        function openNewMrbStockSku(viewSize, data) {
            require([popActions.promotion.newMrbStockSku.controllerUrl], function () {
                $uibModal.open({
                    templateUrl: popActions.promotion.newMrbStockSku.templateUrl,
                    controller: popActions.promotion.newMrbStockSku.controller,
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
         * 显示该sku的库存隔离明细
         */
        $scope.openSkuMrbStockDetail = openSkuMrbStockDetail;
        function openSkuMrbStockDetail(viewSize, taskId, cartId, data) {
            require([popActions.promotion.skuMrbStockDetail.controllerUrl], function () {
                $uibModal.open({
                    templateUrl: popActions.promotion.skuMrbStockDetail.templateUrl,
                    controller: popActions.promotion.skuMrbStockDetail.controller,
                    size: viewSize,
                    resolve: {
                        taskId: function () {
                            return taskId;
                        },
                        cartId: function () {
                            return cartId;
                        },
                        data: function () {
                            return data;
                        }
                    }
                });
            });
        }

        /**
         * 新增增量隔离库存任务
         */
        $scope.openAddMrbStockIncrement = openAddMrbStockIncrement;
        function openAddMrbStockIncrement(viewSize, data) {
            require([popActions.promotion.addMrbStockIncrement.controllerUrl], function () {
                $uibModal.open({
                    templateUrl: popActions.promotion.addMrbStockIncrement.templateUrl,
                    controller: popActions.promotion.addMrbStockIncrement.controller,
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
         * 新增feed查询页图片弹出
         * */
        $scope.openImagedetail = function (context) {
            return openModel(popActions.search.imagedetail, context);
        };

        /**
         * 新增feed查询页code弹出
         * */
        $scope.openCodeDetail = function (context) {
            return openModel(popActions.search.codeDetail, context);
        };

        /**
         * advance查询，添加店铺内分类edit弹出
         */
        $scope.openAddChannelCategoryEdit = function (selList, cartId, context) {
            var productIds = [], data;
            _.forEach(selList, function (object) {
                productIds.push(object.code);
            });
            if (context && context.isSelAll) {
                data = {"productIds": [], "cartId": cartId};
            } else if (selList.length > 0 && selList[0].plateSchema) {
                data = {
                    "productIds": productIds,
                    "cartId": selList[0].cartId,
                    "selectedIds": selList[0].selectedIds,
                    plateSchema: true
                };
            } else {
                data = {"productIds": productIds, "cartId": cartId};
            }
            return openModel(popActions.bulkUpdate.addChannelCategory, data);
        };

        /**
         * 新增ChannelList页,设置操作弹出
         * */
        $scope.openChannelSetting = function (context) {
            return openModel(popActions.system.channelsetting, context);
        };

        /**
         * 新增CartList页,设置操作弹出
         * */
        $scope.openChannelEdit = function (context) {
            return openModel(popActions.system.channeledit, context);
        };

        $scope.openCartEdit = function (context) {
            return openModel(popActions.system.cartList, context);
        };
        /**
         *新增店铺管理-Listing-sizechart页,新增操作弹出
         */
        $scope.openSizeChartAdd = function (context) {
            return openModel(popActions.store.listing.sizechart, context);
        };
        /**
         * 新增店铺管理-Listing-sizechart页,设置操作弹出
         * */
        $scope.openSizeChartSetting = function (context) {
            return openModel(popActions.store.listing.sizechart, context);
        };
        /**
         * 新增店铺管理-Listing-sizechartimport页,倒入操作弹出
         * */
        $scope.openSizeChartImport = function (context) {
            return openModel(popActions.store.listing.sizechartimport, context);
        };
        /**
         * 新增店铺管理-Listing-imagetemplate页,设置操作弹出
         * */
        $scope.openImgTplEditing = function (context) {
            return openModel(popActions.store.listing.imagetemplate, context);
        };
        /**
         * 新增店铺管理-Listing-imagetemplate预览图片页,设置操作弹出
         * */
        $scope.openImgTplPreview = function (context) {
            return openModel(popActions.store.listing.imagetemplatepreview, context);
        };
        /**
         * 新增店铺管理-Listing-imagegroup页,add操作弹出
         * */
        $scope.openImgGroupAdd = openImgGroupAdd;
        function openImgGroupAdd(data) {
            require([popActions.store.listing.imagegroupadd.controllerUrl], function () {
                $uibModal.open({
                    templateUrl: popActions.store.listing.imagegroupadd.templateUrl,
                    controller: popActions.store.listing.imagegroupadd.controller,
                    resolve: {
                        data: function () {
                            return data;
                        }
                    }
                });
            });
        }


        /**
         * 新增店铺管理-Listing-imagegroup页,预览查看图片操作弹出
         * */
        $scope.openImgGroupListImg = openImgGroupListImg;
        function openImgGroupListImg(originUrl) {
            require([popActions.store.listing.imagegroupimg.controllerUrl], function () {
                $uibModal.open({
                    templateUrl: popActions.store.listing.imagegroupimg.templateUrl,
                    controller: popActions.store.listing.imagegroupimg.controller,
                    resolve: {
                        originUrl: function () {
                            return originUrl;
                        }
                    }
                });
            });
        }


        /**
         * 新增店铺管理-Listing-imagegroup_detail页,add操作弹出
         * */
        $scope.openImgGroupDetail = openImgGroupDetail;
        function openImgGroupDetail(data, originUrl) {
            require([popActions.store.listing.imagedetailadd.controllerUrl], function () {
                $uibModal.open({
                    templateUrl: popActions.store.listing.imagedetailadd.templateUrl,
                    controller: popActions.store.listing.imagedetailadd.controller,
                    resolve: {
                        data: function () {
                            return data;
                        },
                        originUrl: function () {
                            return originUrl;
                        }
                    }
                });
            });
        }

        /**
         * 弹出自定义属性列
         * @type {openCustomColumn}
         */
        $scope.openCustomColumn = openCustomColumn;
        function openCustomColumn(viewSize, fnInitial) {
            require([popActions.custom.column.controllerUrl], function () {
                var modalInstance = $uibModal.open({
                    templateUrl: popActions.custom.column.templateUrl,
                    controller: popActions.custom.column.controller,
                    size: viewSize
                });

                modalInstance.result.then(function () {
                    if (fnInitial) {
                        fnInitial();
                    }

                })
            });
        }

        //TagList一览中，新增标签
        $scope.openNewTag = function (context) {
            return openModel(popActions.custom.addtaglist, context);
        };
        $scope.openJmPromotionDefaultSetting = function (context) {
            return openModel(popActions.jumei.jmPromotionDefaultSetting.batch, context);
        };

        //聚美一览中，deal延期
        $scope.openDealExtension = function (context) {
            return openModel(popActions.jumei.jmPromotionDetail.dealExtension, context);
        };
        //聚美一览中，price
        $scope.openPriceModify = function (context) {
            return openModel(popActions.jumei.jmPromotionDetail.priceModify, context);
        };
        //聚美图片管理中，追加按钮
        $scope.openJmImageSetting = function (context) {
            return openModel(popActions.jumei.jmImageManage.imageSetting, context);
        };

        $scope.openJmProductDetail = function (context) {
            return openModel(popActions.jumei.jmProductDetail.detail, context);
        };

        $scope.openJmPromotionDetail = function (context) {
            return openModel(popActions.jumei.jmPromotionDetail.detail, context);
        };

        $scope.openJmPromotionProductImport = openJmPromotionProductImport;
        function openJmPromotionProductImport(context, fnInitial) {
            require([popActions.jumei.jmPromotionDetail.import.controllerUrl], function () {
                var modalInstance = $uibModal.open({
                    templateUrl: popActions.jumei.jmPromotionDetail.import.templateUrl,
                    controller: popActions.jumei.jmPromotionDetail.import.controller,
                    resolve: {
                        context: function () {
                            return context;
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

        $scope.openCategorySetting = function (context) {
            return openModel(popActions.channel.categorySetting, context);
        };

        $scope.openNewCategory = function (context) {
            return openModel(popActions.channel.newCategory, context);
        };

        //打开高级查询页的通用设置，上下架
        $scope.openPutOnOff = function (context) {
            return openModel(popActions.bulkUpdate.putOnOff, context);
        };

        //打开高级查询页的搜索条件，自由标签
        $scope.openFreeTag = function (context) {
            return openModel(popActions.bulkUpdate.freeTag, context);
        };

        //打开高级查询页的共通设置，最终售价
        $scope.openSalePrice = function (context) {
            return openModel(popActions.bulkUpdate.salePrice, context);
        };

        //打开高级查询页的共通设置，上新审批
        $scope.openUpdateApproval = function (context) {
            return openModel(popActions.bulkUpdate.updateProductApproval, context);
        };

        //$scope.openJmPromotionProductImport = function (context,fnInitial) {
        //   var  promise=openModel(popActions.jumei.jmPromotionDetail.import, context);
        //    promise.then(function success(data) {
        //        data.then(function () {
        //            if (fnInitial) {
        //                fnInitial();
        //            }
        //        })
        //    }, function error(msg) {
        //        console.error(msg);
        //    });
        //
        //};
    });
});
