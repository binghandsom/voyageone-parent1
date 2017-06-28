/**
 * 统一管理弹出框的定义信息
 * Created by LinAn.Bin on 15/12/7.
 */

define([
    'cms',
    'underscore',
    'modules/cms/enums/MappingTypes'
], function (cms, _, MappingTypes) {

    angular.module('com.voyageone.popups', []).constant('popActions', {
        "modifyPass": {
            "templateUrl": "views/pop/modifyPass/modifyPass.tpl.html",
            "controllerUrl": "modules/cms/views/pop/modifyPass/modifyPass.ctl",
            "controller": 'ModifyPassController as ctrl',
            "size": 'md'
        },
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
            "platformFieldEdit": {
                "templateUrl": "views/pop/bulkUpdate/platformFieldEdit.tpl.html",
                "controllerUrl": "modules/cms/views/pop/bulkUpdate/platformFieldEdit.ctl",
                "controller": 'popPlatformFieldEditCtl',
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
            "categoryMul": {
                "templateUrl": "views/pop/bulkUpdate/categoryMul.tpl.html",
                "controllerUrl": "modules/cms/views/pop/bulkUpdate/categoryMul.ctl",
                "controller": 'popCategoryMulCtl as ctrl',
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
                "size": '600'
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
            },
            "platformBrandSetting": {
                "templateUrl": "views/pop/channel/platformBrandSetting.tpl.html",
                "controllerUrl": "modules/cms/views/pop/channel/platformBrandSetting.ctl",
                "controller": 'PlatformBrandSettingController as ctrl',
                "backdrop": 'static',
                "size": 'lg'
            },
            "brandMappingConfirm": {
                "templateUrl": "views/pop/channel/brandMappingConfirm.tpl.html",
                "controllerUrl": "modules/cms/views/pop/channel/brandMappingConfirm.ctl",
                "controller": 'BrandMappingConfirmController as ctrl',
                "backdrop": 'static',
                "size": 'md'
            },
            "newChannelConfig": {
                "templateUrl": "views/pop/channel/channelConfigAdd.tpl.html",
                "controllerUrl": "modules/cms/views/pop/channel/channelConfigAdd.ctl",
                "controller": "channelConfigAddController as ctrl"
            },
            "editChannelConfig": {
                "templateUrl": "views/pop/channel/channelConfigEdit.tpl.html",
                "controllerUrl": "modules/cms/views/pop/channel/channelConfigEdit.ctl",
                "controller": "channelConfigEditController as ctrl"
            },
            "importFeedConfig": {
                "templateUrl": "views/pop/channel/feedConfigSet.tpl.html",
                "controllerUrl": "modules/cms/views/pop/channel/feedConfigSet.ctl",
                "controller": "feedConfigSetController"
            }
        },
        "custom": {
            "newAttribute": {
                "templateUrl": "views/pop/custom/newAttribute.tpl.html",
                "controllerUrl": "modules/cms/views/pop/custom/newAttribute.ctl",
                "controller": 'popAddAttributeValueCtl as ctrl',
                "size": 'md'
            },
            "newValue": {
                "templateUrl": "views/pop/custom/newValue.tpl.html",
                "controllerUrl": "modules/cms/views/pop/custom/newValue.ctl",
                "controller": 'popAddAttributeValueNewCtl as ctrl',
                "backdrop": 'static',
                "size": 'md'
            },
            "addValue": {
                "templateUrl": "views/pop/custom/addValue.tpl.html",
                "controllerUrl": "modules/cms/views/pop/custom/addValue.ctl",
                "controller": 'popAddValueCtl as ctrl'
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
            },
            "columnForDownLoad": {
                "templateUrl": "views/pop/custom/columnForDownload.tpl.html",
                "controllerUrl": "modules/cms/views/pop/custom/columnForDownload.ctl",
                "controller": 'popColumnForDownloadCtl',
                "size": 'lg'
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
        "platformMapping": {
            "propertyMapping": {
                "templateUrl": "views/pop/platformMapping/propertyMapping.tpl.html",
                "controllerUrl": "modules/cms/views/pop/platformMapping/propertyMapping.ctl",
                "controller": 'propertyMappingController as ctrl',
                "size": 'lg'
            },
            "propertySetting": {
                "templateUrl": "views/pop/platformMapping/propertySetting.tpl.html",
                "controllerUrl": "modules/cms/views/pop/platformMapping/propertySetting.ctl",
                "controller": 'propertySettingController as ctrl',
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
                "controller": "PriceLogPopupController as ctrl",
                "size": "lg"
            },
            price_confirm: {
                templateUrl: "views/pop/history/price.confirm.log.tpl.html",
                controllerUrl: "modules/cms/views/pop/history/price.confirm.log.controller",
                controller: "PriceConfirmLogController as ctrl",
                size: "lg"
            },
            "promotion": {
                "templateUrl": "views/pop/history/promotion.tpl.html",
                "controllerUrl": "modules/cms/views/pop/history/promotion.ctl",
                "controller": "popPromotionHistoryCtl"
            },
            "putOnOff": {
                "templateUrl": "views/pop/history/putOnOff.tpl.html",
                "controllerUrl": "modules/cms/views/pop/history/putOnOff.ctl",
                "controller": "PutOnOffController as ctrl",
                "size": "lg"
            },
            "productStatus": {
                "templateUrl": "views/pop/history/productStatus.tpl.html",
                "controllerUrl": "modules/cms/views/pop/history/productStatus.ctl",
                "controller": "ProductStatusPopupController as ctrl",
                "size": "lg"
            },
            "intelApprove": {
                "templateUrl": "views/pop/history/intelApprove.tpl.html",
                "controllerUrl": "modules/cms/views/pop/history/intelApprove.ctl",
                "controller": "intelApproveController as ctrl",
                "size": "lg"
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
            },
            "setSkuPrice": {
                "templateUrl": "views/pop/promotion/setSkuPrice.tpl.html",
                "controllerUrl": "modules/cms/views/pop/promotion/setSkuPrice.ctl",
                "controller": 'setSkuPriceCtl as ctrl',
                "size": "lg"
            },
            "tagModify": {
                "templateUrl": "views/pop/promotion/detail/tagmodify.tpl.html",
                "controllerUrl": "modules/cms/views/pop/promotion/detail/tagmodify.ctl",
                "controller": 'TagModifyCtl'
            }
        },
        "jumei": {
            "promotion": {
                "imageSuit": {
                    "templateUrl": "views/pop/jm/promotion/image.suit.tpl.html",
                    "controllerUrl": "modules/cms/views/pop/jm/promotion/image.suit.ctl",
                    "controller": 'imageSuitCtl as ctrl',
                    "size": 'lg'
                },
                "imageUpload": {
                    "templateUrl": "views/pop/jm/promotion/image.upload.tpl.html",
                    "controllerUrl": "modules/cms/views/pop/jm/promotion/image.upload.ctl",
                    "controller": 'imageUploadCtl as ctrl'
                },
                "imageBatchUpload": {
                    "templateUrl": "views/pop/jm/promotion/image.batch.upload.tpl.html",
                    "controllerUrl": "modules/cms/views/pop/jm/promotion/image.batch.upload.ctl",
                    "controller": 'imageBatchUploadCtl as ctrl',
                    "size": "lg"
                }
            },
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
                    "backdrop": "static",
                    "size": 'lg'
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
                    "controller": 'popDealExtensionCtl',
                    "size": 'md'
                },
                "priceModify": {
                    "templateUrl": "views/pop/jm/pricemodify.tpl.html",
                    "controllerUrl": "modules/cms/views/pop/jm/pricemodify.ctl",
                    "controller": 'popPriceModifyCtl',
                    "size": '600',
                    "controller": 'popPriceModifyCtl'
                },
                "tagModify": {
                    "templateUrl": "views/pop/jm/tagmodify.tpl.html",
                    "controllerUrl": "modules/cms/views/pop/jm/tagmodify.ctl",
                    "controller": 'popTagModifyCtl'
                },
                "encore": {
                    "templateUrl": "views/pop/jm/promotionEncore.tpl.html",
                    "controllerUrl": "modules/cms/views/pop/jm/promotionEncore.ctl",
                    "controller": 'popJMPromotionEncoreCtl',
                    "backdrop": "static",
                    "size": 'lg'
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
                "controller": 'popCodeDetailCtl',
                "backdrop": 'static',
                "size": 'md'
            },
            "joinPromotion": {
                "templateUrl": "views/pop/search/joinPromotion.tpl.html",
                "controllerUrl": "modules/cms/views/pop/search/joinPromotion.ctl",
                "controller": 'joinPromotionCtl as ctrl',
                "backdrop": 'static',
                "size": 'lg'
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
            },
            "mqErrorList": {
                "templateUrl": "views/pop/system/mqErrorList.tpl.html",
                "controllerUrl": "modules/cms/views/pop/system/mqErrorList.ctl",
                "controller": 'popMqErrorListCtl'
            }
        },
        "maintain": {
            "masterBrandCheck": {
                "templateUrl": "views/pop/maintain/masterBrandCheck.tpl.html",
                "controllerUrl": "modules/cms/views/pop/maintain/masterBrandCheck.ctl",
                "controller": 'masterBrandCheckCtl as ctrl',
                "size": "lg"
            },
            "masterBrandMapDetail": {
                "templateUrl": "views/pop/maintain/masterBrandMapDetail.tpl.html",
                "controllerUrl": "modules/cms/views/pop/maintain/masterBrandMapDetail.ctl",
                "controller": 'masterBrandMapDetailCtl as ctrl',
                "size": "lg"
            },
            "masterBrandEdit": {
                "templateUrl": "views/pop/maintain/masterBrandEdit.tpl.html",
                "controllerUrl": "modules/cms/views/pop/maintain/masterBrandEdit.ctl",
                "controller": 'masterBrandEditCtl as ctrl'
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
                },
                "delConfirm": {
                    "templateUrl": "views/pop/store/listing/delConfirm.tpl.html",
                    "controllerUrl": "modules/cms/views/pop/store/listing/delConfirm.ctl",
                    "controller": 'popDelConfirmCtl as ctrl'
                }
            }
        },
        "image": {
            "upload": {
                "templateUrl": "views/pop/image/imgSetting.tpl.html",
                "controllerUrl": "modules/cms/views/pop/image/imgSetting.ctl",
                "controller": 'popImgSettingCtl as ctrl',
                "size": 'lg'
            }
        },
        "product": {
            "switchMain": {
                "templateUrl": "views/pop/product/switchMain.tpl.html",
                "controllerUrl": "modules/cms/views/pop/product/switchMain.ctl",
                "controller": 'SwitchMainController as ctrl',
                "size": 'lg'
            },
            "productOffLine": {
                "templateUrl": "views/pop/product/productOffLine.tpl.html",
                "controllerUrl": "modules/cms/views/pop/product/productOffLine.ctl",
                "controller": 'ProductOffLineController as ctrl',
                "size": 'lg'
            },
            "hsCodeChange": {
                "templateUrl": "views/pop/product/hsCodeChange.tpl.html",
                "controllerUrl": "modules/cms/views/pop/product/hsCodeChange.ctl",
                "controller": 'HsCodeChangeController as ctrl'
            },
            "approveConfirm": {
                "templateUrl": "views/pop/product/approveConfirm.tpl.html",
                "controllerUrl": "modules/cms/views/pop/product/approveConfirm.ctl",
                "controller": 'ApproveConfirmController as ctrl'
            },
            "skuMoveConfirm": {
                "templateUrl": "views/pop/product/sku_move_confirm.tpl.html",
                "controllerUrl": "modules/cms/views/pop/product/sku_move_confirm.ctl",
                "controller": 'SkuMoveConfirmController as ctrl'
            },
            "moveResult": {
                "templateUrl": "views/pop/product/move_result.tpl.html",
                "controllerUrl": "modules/cms/views/pop/product/move_result.ctl",
                "controller": 'MoveResultController as ctrl',
                "size": 'sm'
            },
            "combinedProductNew": {
                "templateUrl": "views/pop/product/combined-product-new.tpl.html",
                "controllerUrl": "modules/cms/views/pop/product/combined-product-new.ctl",
                "controller": 'CombinedProductNewController as ctrl',
                "size": 'lg'
            },
            "combinedProductEdit": {
                "templateUrl": "views/pop/product/combined-product-edit.tpl.html",
                "controllerUrl": "modules/cms/views/pop/product/combined-product-edit.ctl",
                "controller": 'CombinedProductEditController as ctrl',
                "size": 'lg'
            },
            "combinedProductLogs": {
                "templateUrl": "views/pop/product/combined-product-logs.tpl.html",
                "controllerUrl": "modules/cms/views/pop/product/combined-product-logs.ctl",
                "controller": 'CombinedProductLogsController as ctrl',
                "size": 'lg'
            },
            "uploadImages": {
                "templateUrl": "views/pop/product/upload-images.tpl.html",
                "controllerUrl": "modules/cms/views/pop/product/upload-images.ctl",
                "controller": 'uploadImagesController as ctrl'
            },
            "loadAttribute": {
                "templateUrl": "views/pop/product/loadAttribute.tpl.html",
                "controllerUrl": "modules/cms/views/pop/product/loadAttribute.ctl",
                "controller": 'loadAttributeController as ctrl'
            }
        },
        "group":{
            "editGroup": {
                "templateUrl": "views/pop/group/editGroup.tpl.html",
                "controllerUrl": "modules/cms/views/pop/group/editGroup.ctl",
                "controller": 'editGroupController as ctrl',
                "size":"lg"
            }
        },
        "shelves": {
            "shelvesTemplateAdd": {
                "templateUrl": "views/pop/shelves/shelves-template-add.tpl.html",
                "controllerUrl": "modules/cms/views/pop/shelves/shelves-template-add.ctl",
                "controller": 'ShelvesTemplateAddController as ctrl'
            },
            "shelvesTemplateEdit": {
                "templateUrl": "views/pop/shelves/shelves-template-edit.tpl.html",
                "controllerUrl": "modules/cms/views/pop/shelves/shelves-template-edit.ctl",
                "controller": 'ShelvesTemplateEditController as ctrl'
            },
            newShelves: {
                templateUrl: "views/pop/shelves/new-shelves.tpl.html",
                controllerUrl: "modules/cms/views/pop/shelves/new-shelves.ctl",
                controller: 'NewShelvesPopupController as $ctrl',
                size: 'md'
            }
        },
        confirmProductRefresh: {
            templateUrl: "views/pop/platformMapping/confirmProductRefresh.html",
            controllerUrl: "modules/cms/views/pop/platformMapping/confirmProductRefresh.controller",
            controller: 'ConfirmProductRefreshController as $ctrl',
            size: 'md'
        },
        mqSkuCodeError: {
            templateUrl: "views/pop/error/mqSkuCodeError.html",
            controllerUrl: "modules/cms/views/pop/error/mqSkuCodeError.ctl",
            controller: 'mqSkuCodeErrorController as ctrl',
            size: 'md'
        }
    }).controller('popupCtrl', function popupCtrl($scope, $uibModal, popActions, $q) {

        function openModal(config, context, contextIsResolve) {

            config.resolve = contextIsResolve ? context : {
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
        $scope.openAuthority = function openAuthority(viewSize, data) {
            popActions.authority.new.size = viewSize;
            openModal(popActions.authority.new, {
                data: function () {
                    return data;
                }
            }, true);
        };

        /**
         * 新增属性值
         */
        $scope.openAddattributevaluenew = function openAddattributevaluenew(viewSize, data) {
            popActions.addattributevaluenew.new.size = viewSize;
            openModal(popActions.addattributevaluenew.new, {
                data: function () {
                    return data;
                }
            }, true);
        };

        $scope.openAddattributevaluenews = function openAddattributevaluenews(viewSize, data) {
            popActions.addattributevaluenews.new.size = viewSize;
            openModal(popActions.addattributevaluenews.new, {
                data: function () {
                    return data;
                }
            }, true);
        };

        $scope.openAddattributevalue = function openAddattributevalue(viewSize, data) {
            popActions.addattributevalue.new.size = viewSize;
            openModal(popActions.addattributevalue.new, {
                data: function () {
                    return data;
                }
            }, true);
        };

        /**
         * pop出promotion选择页面,用于设置
         * @type {openAddToPromotion}
         */
        $scope.openAddToPromotion = function openAddToPromotion(promotion, selList, context) {
            var productIds = [];
            var selAllFlg = 0;
            if (context && context.isSelAll) {
                // 全选
                selAllFlg = 1;
            } else {
                if (selList && selList.length) {
                    _.forEach(selList, function (object) {
                        productIds.push(object.id);
                    });
                }
            }
            return openModal(popActions.bulkUpdate.addToPromotion, {
                "promotion": promotion,
                "productIds": productIds,
                "products": selList,
                "isSelAll": selAllFlg
            });
        };

        /**
         * pop出properties变更页面,用于批量更新产品属性
         */
        $scope.openFieldEdit = function openFieldEdit(selList, context) {
            var params = null;
            if (context) {
                if (context.isSelAll) {
                    // 全选
                    params = context;
                } else {
                    var productIds = [];
                    if (selList && selList.length) {
                        _.forEach(selList, function (object) {
                            productIds.push(object.code);
                        });
                    }
                    params = context;
                    params.productIds = productIds;
                }
            } else {
                params = {};
            }
            return openModal(popActions.bulkUpdate.fieldEdit, params);
        };

        /**
         * 打开类目选择页面
         * @param context
         * @returns {*}
         */
        $scope.popupNewCategory = function popupNewCategory(context) {
            return openModal(popActions.bulkUpdate.category, context);
        };

        /**
         * 打开类目选择页面
         * @param context
         * @returns {*}
         */
        $scope.popupPlatformPopOptions = function popupPlatformPopOptions(context) {

            return openModal(popActions.bulkUpdate.platformFieldEdit, context);
        };
        /**
         * 打开多选类目选择页面
         * @param context
         * @returns {*}
         */
        $scope.popCategoryMul = function popCategoryMul(context) {
            return openModal(popActions.bulkUpdate.categoryMul, context);
        };

        /**
         * 打开新的类目选择页面
         * @param context
         * @returns {*}
         */
        $scope.popupCategoryNew = function popupCategoryNew(context) {
            return openModal(popActions.bulkUpdate.categoryNew, context);
        };

        /**
         * 打开类目属性编辑页面
         * @param context
         * @returns {*}
         */
        $scope.openSystemCategory = function openSystemCategory(context) {
            return openModal(popActions.category.schema, context);
        };

        /**
         * 打开添加自定义属性编辑页面
         * @param context
         * @returns {*}
         */
        $scope.openAddAttribute = function openAddAttribute(context) {
            return openModal(popActions.custom.newAttribute, context);
        };

        /**
         * 新增advance查询页,参加聚美活动弹出
         * */
        $scope.openAddJMActivity = function openAddJMActivity(promotion, selList, context) {
            if (context && context.isSelAll) {
                // 全选
                return openModal(popActions.search.joinJM, {promotion: promotion, products: [], 'isSelAll': 1});
            } else {
                if (selList && selList.length) {
                    return openModal(popActions.search.joinJM, {promotion: promotion, products: selList});
                }
            }
        };

        /**
         * 打开添加自定义属性-值，编辑页面
         * @param context
         * @returns {*}
         */
        $scope.openAddNewValue = function openAddNewValue(context) {
            return openModal(popActions.custom.newValue, context);
        };

        $scope.openAddAttrValue = function openAddNewValue(context) {
            return openModal(popActions.custom.addValue, context);
        };

        /**
         * 打开新增配置页面
         * @type {openConfiguration}
         */
        $scope.openConfiguration = function openConfiguration(viewSize, data) {
            popActions.configuration.new.size = viewSize;
            openModal(popActions.configuration.new, {
                data: function () {
                    return data;
                }
            }, true);
        };

        /**
         * 添加字典值页面
         * @type {openDictValue}
         */
        $scope.openDictValue = function openDictValue(viewSize, fnInitial, $index, data) {
            popActions.dictionary.value.size = viewSize;
            openModal(popActions.dictionary.value, {
                dictValue: function () {
                    return data;
                }
            }, true).then(function (data) {
                fnInitial(data, $index);
            });
        };

        /**
         * 添加字典自定义页面
         * @type {openDictCustom}
         */
        $scope.openDictCustom = function openDictCustom(viewSize, fnInitial, $index, data) {
            popActions.dictionary.custom.size = viewSize;
            openModal(popActions.dictionary.custom, {
                customValue: function () {
                    return data;
                }
            }, true).then(function (data) {
                fnInitial(data, $index);
            });
        };

        $scope.openOtherPlatform = function openOtherPlatform(context) {
            return openModal(popActions.platformMapping.otherPlatform, context);
        };

        /**
         * 打开选择base property页面
         * @type {openCustomBaseProperty}
         */
        $scope.openCustomBaseProperty = function openCustomBaseProperty(viewSize) {
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
        };

        /**
         * 打开翻译使用的第三方属性页面
         * @type {openTranslate}
         */
        $scope.openTranslate = function openTranslate(viewSize, data) {
            popActions.field.feedDetail.size = viewSize;
            openModal(popActions.field.feedDetail, {
                data: function () {
                    return data;
                }
            }, true);
        };

        /**
         * 打开导入文件页面
         * @type {openImport}
         */
        $scope.openImport = function openImport(viewSize, data, fnInitial) {
            popActions.file.import.size = viewSize;
            openModal(popActions.file.import, {
                data: function () {
                    return data;
                }
            }, true).then(function () {
                if (fnInitial) {
                    fnInitial();
                }
            });
        };

        /**
         * 打开promotion历史页面
         * @type {openHistoryPromotion}
         */
        $scope.openHistoryPromotion = function openHistoryPromotion(viewSize, code, selectedCart) {
            popActions.history.promotion.size = viewSize;
            openModal(popActions.history.promotion, {
                data: function () {
                    return {
                        code: code,
                        cartId: selectedCart
                    };
                }
            }, true);
        };

        //产品详情页，弹出历史纪录的上下架历史纪录
        $scope.openHistoryPutOnOff = function openHistoryPutOnOff(code, cartId, cartList) {
            return openModal(popActions.history.putOnOff, {code: code, cartId: cartId, cartList: cartList});
        };

        //全店操作页面中，操作按钮弹出
        $scope.openNewOpp = function openNewOpp(header, upLoadFlag) {
            return openModal(popActions.custom.storeoperation, {header: header, upLoadFlag: upLoadFlag});
        };

        //全店操作页面中，确认操作按钮弹出
        $scope.openConfirmOpp = function openConfirmOpp(header, upLoadFlag) {
            return openModal(popActions.custom.confirmstoreopp, {header: header, upLoadFlag: upLoadFlag});
        };

        /**
         * 打开price历史页面
         */
        $scope.openHistoryPrice = function openHistoryPrice(code, skuList, selectedSku, selectedCart) {
            return openModal(popActions.history.price, {
                skuList: skuList,
                code: code,
                selected: {
                    sku: selectedSku,
                    cart: selectedCart
                }
            });
        };

        $scope.openHistoryPriceConfirm = function openHistoryPriceConfirm(code, skuList, selectedSku, selectedCart) {
            return openModal(popActions.history.price_confirm, {
                skuList: skuList,
                code: code,
                selected: {
                    sku: selectedSku,
                    cart: selectedCart
                }
            });
        };

        /**
         * 打开promotion页面
         * @type {openPromotion}
         */
        $scope.openPromotion = function openPromotion(viewSize, cartList, data, fnInitial) {
            popActions.promotion.detail.size = viewSize;
            openModal(popActions.promotion.detail, {
                items: function () {
                    return data;
                },
                cartList: function () {
                    return cartList;
                }
            }, true).then(function () {
                if (fnInitial) {
                    fnInitial();
                }
            });
        };

        /**
         * 打开promotion页面
         * @type {openPromotion}
         */
        $scope.openImageSetting = function openImageSetting(context) {
            return openModal(popActions.image.upload, context);
        };

        /**
         * 打开promotion页面
         */
        $scope.openNewBeatTask = function openNewBeatTask(context) {
            return openModal(popActions.promotion.newBeat, context);
        };

        $scope.popAddBeat = function popAddBeat(context) {
            return openModal(popActions.promotion.addBeat, context);
        };

        /**
         * 打开新建库存隔离任务
         * @type {openMrbStock}
         */
        $scope.openMrbStock = function openMrbStock(viewSize, data) {
            popActions.promotion.newMrbStock.size = viewSize;
            openModal(popActions.promotion.newMrbStock, {
                data: function () {
                    return data;
                }
            }, true);
        };

        /**
         * 导入库存隔离数据
         * @type {openImportStock}
         */
        $scope.openImportStock = function openImportStock(viewSize, data) {
            popActions.file.stock.size = viewSize;
            openModal(popActions.file.stock, {
                data: function () {
                    return data;
                }
            }, true);
        };

        /**
         * 新增一个sku的库存隔离
         */
        $scope.openNewMrbStockSku = function openNewMrbStockSku(viewSize, data) {
            popActions.promotion.newMrbStockSku.size = viewSize;
            openModal(popActions.promotion.newMrbStockSku, {
                data: function () {
                    return data;
                }
            }, true);
        };

        /**
         * 显示该sku的库存隔离明细
         */
        $scope.openSkuMrbStockDetail = function openSkuMrbStockDetail(viewSize, taskId, cartId, data) {
            popActions.promotion.skuMrbStockDetail.size = viewSize;
            openModal(popActions.promotion.skuMrbStockDetail, {
                taskId: function () {
                    return taskId;
                },
                cartId: function () {
                    return cartId;
                },
                data: function () {
                    return data;
                }
            }, true);
        };

        /**
         * 新增增量隔离库存任务
         */
        $scope.openAddMrbStockIncrement = function openAddMrbStockIncrement(viewSize, data) {
            popActions.promotion.addMrbStockIncrement.size = viewSize;
            openModal(popActions.promotion.addMrbStockIncrement, {
                data: function () {
                    return data;
                }
            }, true);
        };

        /**
         * sku价格刷新
         */
        $scope.openSetSkuPrice = function openSetSkuPrice(context) {
            return openModal(popActions.promotion.setSkuPrice, context);
        };

        /**
         * 新增feed查询页图片弹出
         * */
        $scope.openImagedetail = function openImagedetail(context) {
            return openModal(popActions.search.imagedetail, context);
        };

        /**
         * 新增feed查询页code弹出
         * */
        $scope.openCodeDetail = function openCodeDetail(context) {
            return openModal(popActions.search.codeDetail, context);
        };

        /**
         * advance查询，添加店铺内分类edit弹出
         */
        $scope.openAddChannelCategoryEdit = function openAddChannelCategoryEdit(selList, cartId, context) {
            var productIds = [], data;
            _.forEach(selList, function (object) {
                productIds.push(object.code);
            });
            if (context && context.isSelAll) {
                data = {
                    "productIds": [],
                    "cartId": cartId,
                    'isSelAll': context.isSelAll,
                    "searchInfo": context.searchInfo
                };
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

            if (context && context.isQuery) {
                data.isQuery = "1";
            } else {
                data.isQuery = "0";
            }

            return openModal(popActions.bulkUpdate.addChannelCategory, data);
        };

        /**
         * 新增ChannelList页,设置操作弹出
         * */
        $scope.openChannelSetting = function openChannelSetting(context) {
            return openModal(popActions.system.channelsetting, context);
        };

        /**
         * 新增CartList页,设置操作弹出
         * */
        $scope.openChannelEdit = function openChannelEdit(context) {
            return openModal(popActions.system.channeledit, context);
        };

        $scope.openCartEdit = function openCartEdit(context) {
            return openModal(popActions.system.cartList, context);
        };

        /**
         *新增店铺管理-Listing-sizechart页,新增操作弹出
         */
        $scope.openSizeChartAdd = function openSizeChartAdd(context) {
            return openModal(popActions.store.listing.sizechart, context);
        };

        /**
         * 新增店铺管理-Listing-sizechart页,设置操作弹出
         * */
        $scope.openSizeChartSetting = function openSizeChartSetting(context) {
            return openModal(popActions.store.listing.sizechart, context);
        };

        /**
         * 新增店铺管理-Listing-sizechartimport页,倒入操作弹出
         * */
        $scope.openSizeChartImport = function openSizeChartImport(context) {
            return openModal(popActions.store.listing.sizechartimport, context);
        };

        /**
         * 新增店铺管理-Listing-imagetemplate页,设置操作弹出
         * */
        $scope.openImgTplEditing = function openImgTplEditing(context) {
            return openModal(popActions.store.listing.imagetemplate, context);
        };

        /**
         * 新增店铺管理-Listing-imagetemplate预览图片页,设置操作弹出
         * */
        $scope.openImgTplPreview = function openImgTplPreview(context) {
            return openModal(popActions.store.listing.imagetemplatepreview, context);
        };

        /**
         * 新增店铺管理-Listing-imagegroup页,add操作弹出
         * */
        $scope.openImgGroupAdd = function openImgGroupAdd(data) {
            return openModal(popActions.store.listing.imagegroupadd, {
                data: function () {
                    return data;
                }
            }, true);
        };

        /**
         * 图片和尺码表确认框
         */
        $scope.openTemplateConfirm = function openTemplateConfirm(context) {
            return openModal(popActions.store.listing.delConfirm, context);
        };

        /**
         * 新增店铺管理-Listing-imagegroup页,预览查看图片操作弹出
         * */
        $scope.openImgGroupListImg = function openImgGroupListImg(originUrl) {
            openModal(popActions.store.listing.imagegroupimg, {
                originUrl: function () {
                    return originUrl;
                }
            }, true);
        };

        /**
         * 新增店铺管理-Listing-imagegroup_detail页,add操作弹出
         * */
        $scope.openImgGroupDetail = function openImgGroupDetail(data, originUrl) {
            openModal(popActions.store.listing.imagedetailadd, {
                data: function () {
                    return data;
                },
                originUrl: function () {
                    return originUrl;
                }
            }, true);
        };

        /**
         * 弹出自定义属性列
         * @type {openCustomColumn}
         */
        $scope.openCustomColumn = function openCustomColumn(viewSize, fnInitial) {
            popActions.custom.column.size = viewSize;
            openModal(popActions.custom.column, null, true).then(function () {
                if (fnInitial) {
                    fnInitial();
                }
            });
        };

        //TagList一览中，新增标签
        $scope.openNewTag = function openNewTag(context) {
            return openModal(popActions.custom.addtaglist, context);
        };

        $scope.openJmPromotionDefaultSetting = function openJmPromotionDefaultSetting(context) {
            return openModal(popActions.jumei.jmPromotionDefaultSetting.batch, context);
        };

        //聚美一览中，deal延期
        $scope.openDealExtension = function openDealExtension(context) {
            return openModal(popActions.jumei.jmPromotionDetail.dealExtension, context);
        };

        //聚美一览中，price
        $scope.openPriceModify = function openPriceModify(context) {
            return openModal(popActions.jumei.jmPromotionDetail.priceModify, context);
        };

        // 修改非聚美活动商品tag
        $scope.openTagModify = function (context) {
            return openModal(popActions.promotion.tagModify, context);
        };
        //聚美一览中，price
        $scope.openJMTagModify = function (context) {
            return openModal(popActions.jumei.jmPromotionDetail.tagModify, context);
        };
        //聚美图片管理中，追加按钮
        $scope.openJmImageSetting = function openJmImageSetting(context) {
            return openModal(popActions.jumei.jmImageManage.imageSetting, context);
        };

        $scope.openJmProductDetail = function openJmProductDetail(context) {
            return openModal(popActions.jumei.jmProductDetail.detail, context);
        };

        $scope.openJmPromotionDetail = function openJmPromotionDetail(context, fnInitial) {
            popActions.jumei.jmPromotionDetail.detail.size = 'lg';
            openModal(popActions.jumei.jmPromotionDetail.detail, context).then(function () {
                if (fnInitial) {
                    fnInitial();
                }
            });
        };

        $scope.openJmPromotionEncore = function openJmPromotionEncore(context, fnInitial, ctrl) {
            popActions.jumei.jmPromotionDetail.encore.size = 'lg';
            openModal(popActions.jumei.jmPromotionDetail.encore, context).then(function (res) {
                if (fnInitial) {
                    fnInitial(res, ctrl);
                }
            });
        };

        $scope.openJmPromotionProductImport = function openJmPromotionProductImport(context, fnInitial) {
            openModal(popActions.jumei.jmPromotionDetail.import, context).then(function () {
                if (fnInitial) {
                    fnInitial();
                }
            });
        };

        $scope.openFeedConfigImport = function openFeedConfigImport(context) {
            return openModal(popActions.channel.importFeedConfig, context);
        };

        $scope.openCategorySetting = function openCategorySetting(context) {
            return openModal(popActions.channel.categorySetting, context);
        };

        $scope.openNewCategory = function openNewCategory(context) {
            return openModal(popActions.channel.newCategory, context);
        };

        $scope.openPlatformMappingSetting = function openPlatformMappingSetting(context) {
            return openModal(popActions.channel.platformBrandSetting, context);
        };

        $scope.openPlatformMappingConfirm = function openPlatformMappingConfirm(context) {
            return openModal(popActions.channel.brandMappingConfirm, context);
        };

        //打开高级查询页的通用设置，上下架
        $scope.openPutOnOff = function openPutOnOff(context) {
            return openModal(popActions.bulkUpdate.putOnOff, context);
        };

        //打开高级查询页的搜索条件，自由标签
        $scope.openFreeTag = function openFreeTag(context) {
            return openModal(popActions.bulkUpdate.freeTag, context);
        };

        //打开高级查询页的共通设置，最终售价
        $scope.openSalePrice = function openSalePrice(context) {
            return openModal(popActions.bulkUpdate.salePrice, context);
        };

        //打开高级查询页的共通设置，上新审批
        $scope.openUpdateApproval = function openUpdateApproval(context) {
            return openModal(popActions.bulkUpdate.updateProductApproval, context);
        };

        /**切换主类目*/
        $scope.openSwitchMain = function openSwitchMain(context) {
            return openModal(popActions.product.switchMain, context);
        };

        /**产品下线*/
        $scope.openProductOffLine = function openProductOffLine(context) {
            return openModal(popActions.product.productOffLine, context);
        };

        $scope.openProductStatusHistory = function openProductStatusHistory(code, cartId) {
            return openModal(popActions.history.productStatus, {
                cartId: cartId,
                code: code
            });
        };

        /**税号改变 hsCodeChange*/
        $scope.openHsCodeChange = function openHsCodeChange(context) {
            return openModal(popActions.product.hsCodeChange, context);
        };

        /**属性匹配*/
        $scope.openPropertyMapping = function openPropertyMapping(fieldMapping, searchInfo) {
            return openModal(popActions.platformMapping.propertyMapping, _.extend(fieldMapping, searchInfo));
        };

        /**属性编辑*/
        $scope.openPropertySetting = function openPropertySetting(context) {
            return openModal(popActions.platformMapping.propertySetting, context);
        };

        /**上新价格确认*/
        $scope.openApproveConfirm = function openApproveConfirm(context) {
            return openModal(popActions.product.approveConfirm, context);
        };

        /**
         * 聚美选择套图
         */
        $scope.openImageSuit = function openImageSuit(context) {
            return openModal(popActions.jumei.promotion.imageSuit, context)
        };

        /**
         * 聚美活动图片上传
         */
        $scope.openImageJmUpload = function openImageJmUpload(context) {
            return openModal(popActions.jumei.promotion.imageUpload, context)
        };

        /**
         * 聚美活动图片批量上传
         */
        $scope.openImageBatchJmUpload = function openImageBatchJmUpload(context) {
            return openModal(popActions.jumei.promotion.imageBatchUpload, context)
        };

        /**
         * 高级检索加入活动
         */
        $scope.openJoinPromotion = function openJoinPromotion(context) {
            return openModal(popActions.search.joinPromotion, context)
        };

        /**移动SKU的结果确认*/
        $scope.openMoveResult = function openMoveResult(context) {
            return openModal(popActions.product.moveResult, context);
        };

        /**新增店铺配置(channelconfig)*/
        $scope.newChannelConfig = function newChannelConfig(context) {
            return openModal(popActions.channel.newChannelConfig, context);
        };

        /**编辑店铺配置*/
        $scope.editChannelConfig = function editChannelConfig(context) {
            return openModal(popActions.channel.editChannelConfig, context);
        };

        /** 智能上新操作历史 */
        $scope.openIntelApprove = function openIntelApprove(code, cartId) {
            return openModal(popActions.history.intelApprove, {code: code, cartId: cartId});
        };

        $scope.confirmProductRefresh = function confirmProductRefresh(field, mappingInfo) {
            return openModal(popActions.confirmProductRefresh, {
                field: field,
                mappingInfo: mappingInfo
            });
        };

        /**新建货架模板*/
        $scope.shelvesTemplateAdd = function (context) {
            return openModal(popActions.shelves.shelvesTemplateAdd, context)
        };

        $scope.shelvesTemplateEdit = function (context) {
            return openModal(popActions.shelves.shelvesTemplateEdit, context)
        };

        $scope.popNewShelves = function popNewShelves(context) {
            return openModal(popActions.shelves.newShelves, context)
        };

        /**移动SKU确认*/
        $scope.openSKUMoveConfirm = function openSKUMoveConfirm(context) {
            return openModal(popActions.product.skuMoveConfirm, context);
        };

        /**移动SKU的结果确认*/
        $scope.openMoveResult = function openMoveResult(context) {
            return openModal(popActions.product.moveResult, context);
        };

        /**组合商品*/
        $scope.popNewCombinedProduct = function (context) {
            return openModal(popActions.product.combinedProductNew, context)
        };

        $scope.popEditCombinedProduct = function (context) {
            return openModal(popActions.product.combinedProductEdit, context)
        };

        $scope.popCombinedProductLogs = function (context) {
            return openModal(popActions.product.combinedProductLogs, context);
        };

        /**
         * 打开新建权限配置页面
         */
        $scope.openModifyPass = function openModifyPass(context) {
            return openModal(popActions.modifyPass, context);
        };

        /**产品详情页图片上传*/
        $scope.openUploadImages = function openUploadImages(context) {
            return openModal(popActions.product.uploadImages, context);
        };

        /**
         * MQ:sku-code 错误列表
         */
        $scope.openMqSkuCodeError = function openMqSkuCodeError(context) {
            return openModal(popActions.mqSkuCodeError, context);
        };

        /**
         * 产品详情页更新部分属性
         */
        $scope.openLoadAttribute = function openLoadAttribute(context) {
            return openModal(popActions.product.loadAttribute, context);
        };

        $scope.openEditGroup = function(context){
            return openModal(popActions.group.editGroup, context);
        }

        /**
         * 自定义下载选择列模态框
         */
        $scope.openColumnForDownLoad = function (context) {
            return openModal(popActions.custom.columnForDownLoad, context);
        };

    }).factory('popups', function ($controller, $rootScope) {

        var popupScope = $rootScope.$new();

        popupScope.$controller = $controller('popupCtrl', {$scope: popupScope});

        return popupScope;
    });
});
