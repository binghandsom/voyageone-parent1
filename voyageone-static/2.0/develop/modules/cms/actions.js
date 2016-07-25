define(function () {

    // 缓存作用域
    var CACHE = {
        NONE: 0,
        ONCE: 1,
        SESSION: 2,
        LOCAL: 3
    };

    // 作为额外缓存关键字的关键字名称
    var KEY = {
        USERNAME: 'username',
        CHANNEL: 'channel'
    };

    /**
     * 生产一个配置好的 action 配置对象
     * @param {string} action Action 名称
     * @param {Array} [cacheWith] 缓存时, 额外追加的缓存关键字, 参见 KEY 对象提供的字段
     * @returns {{url: *, cache: number, cacheWith: *}}
     */
    function session(action, cacheWith) {
        return {
            url: action,
            cache: CACHE.SESSION,
            cacheWith: cacheWith
        };
    }

    return {
        "core": {
            "access": {
                "user": {
                    "logout": "/core/access/user/logout"
                }
            },
            "home": {
                "menu": {
                    "getMenuHeaderInfo": "/core/home/menu/getMenuHeaderInfo",
                    "setLanguage": "/core/home/menu/setLanguage"
                }
            }
        },
        "cms": {
            "home": {
                "$menuService": {
                    "root": "/cms/home/menu/",
                    "getCategoryInfo": session('getCategoryInfo', [KEY.CHANNEL]),
                    "getPlatformType": session('getPlatformType', [KEY.USERNAME, KEY.CHANNEL]),
                    "setPlatformType": "setPlatformType",
                    "getHomeSumData":"getHomeSumData",
                    "getCmsConfig":session('getCmsConfig',[KEY.CHANNEL])
                }
            },
            "search": {
                "$searchAdvanceService2": {
                    "root": "/cms/search/advanceSearch/",
                    "init": "init",
                    "search": "search",
                    "getGroupList": "getGroupList",
                    "getProductList": "getProductList",
                    "exportProducts": "exportProducts",
                    "getCustColumnsInfo": "getCustColumnsInfo",
                    "saveCustColumnsInfo": "saveCustColumnsInfo",
                    "addFreeTag": "addFreeTag",
                    "getCustSearchList": "getCustSearchList"
                },
                "$feedSearchService": {
                    "root": "/cms/search/feed/",
                    "init": "init",
                    "search": "search",
                    "updateFeedStatus": "updateFeedStatus",
                    "doExport": "export",
                    "exportSearch":"exportSearch",
                    "download":"download"
                }
            },
            "group": {
                "$groupDetailService": {
                    "root": "/cms/group/detail/",
                    "init": "init",
                    "getProductList": "getProductList",
                    "setMainProduct": "setMainProduct"
                }
            },
            "product": {
                "$productDetailService": {
                    "root": "/cms/product/detail/",
                    "getProductInfo": "getProductInfo",
                    "updateProductMasterInfo": "updateProductMasterInfo",
                    "updateProductSkuInfo": "updateProductSkuInfo",
                    "updateProductAllInfo": "updateProductAllInfo",
                    "changeCategory": "changeCategory",
                    "revertCategory": "revertCategory",
                    "getProductPlatform": "getProductPlatform",
                    "changePlatformCategory": "changePlatformCategory",
                    "updateProductPlatform": "updateProductPlatform",
                    "updateProductPlatformChk": "updateProductPlatformChk",
                    "updateProductFeed": "updateProductFeed",
                    "getCommonProductInfo": "getCommonProductInfo",
                    "updateCommonProductInfo": "updateCommonProductInfo",
                    "updateLock":"updateLock",
                    "updateProductAtts":"updateProductAtts",
                    "checkCategory":"checkCategory",
                    getChangeMastProductInfo:"getChangeMastProductInfo",
                    setMastProduct:"setMastProduct",
                    delisting:"delisting",
                    delistinGroup:"delistinGroup"
                },
                "productHistoryLogService": {
                    "root": "/cms/product/history/",
                    "getPutOnOffLogList": "getPutOnOffLogList"
                }
            },
            "mapping": {
                "feedMappingService": {
                    "root": "/cms/mapping/feed",
                    "getTopCategories": "getTopCategories",
                    "getFeedCategoryTree": "getFeedCategoryTree",
                    "getMainCategories": "getMainCategories",
                    "setMapping": "setFeedMapping",
                    "extendsMapping": "extendsMapping",
                    "getFieldMapping": "getFieldMapping",
                    "getFeedAttrs": "getFeedAttributes",
                    "saveFieldMapping": "saveFieldMapping",
                    "directMatchOver": "directMatchOver",
                    "getMainMapping": "getMainMapping",
                    "getMappings": "getMappings",
                    "getMappingInfo": "getMappingInfo"
                },
                "platformMappingService": {
                    "root": "/cms/mapping/platform",
                    "getMainCategory": "getMainDataFinalCategoryMap",
                    "getOtherMappingPath": "getOtherMappingCategoryPath",
                    "getPlatformCategories": "getPlatformCategories",
                    "setPlatformMapping": "setPlatformMapping",
                    "getPlatformCategory": "getPlatformCategory",
                    "getPlatformCategorySchema": "getPlatformCategorySchema",
                    "getMainCategorySchema": "getMainCategorySchema",
                    "getDictList": "getDictList",
                    "getPlatformMapping": "getPlatformMapping",
                    "getMappingTypes": "getMappingType",
                    "$saveMapping": "saveMapping",
                    "$saveMatchOverByMainCategory": "saveMatchOverByMainCategory",
                    "getCarts": "getCarts",
                    "getCommonSchema": "getCommonSchema"
                },
                "$dictionaryService": {
                    "root": "/cms/mapping/dictionary",
                    "init": "init",
                    "getDict": "getDict",
                    "getDictList": "getDictList",
                    "getConst": "getConst",
                    "getCustoms": "getCustoms",
                    "setDict": "setDict",
                    "delDict": "delDict",
                    "addDict": "addDict"
                },
                'brandMappingService': {
                	'root': '/cms/mapping/brand',
                	'init': 'init',
                	'searchBrands': 'searchBrands',
                	'searchBrandsByPage': 'searchBrandsByPage',
                	'searchCustBrands': 'searchCustBrands'
                }
            },
            "promotion": {
                "promotionService": {
                    "root": "/cms/promotion/index",
                    "init": "init",
                    "initByPromotionId": "initByPromotionId",
                    "getPromotionList": "getPromotionList",
                    "insertPromotion": "insertPromotion",
                    "updatePromotion": "updatePromotion",
                    "delPromotion": "delPromotion",
                    "exportPromotion": "exportPromotion",
                    getPage: "getPage",
                    getCount: "getCount",
                    getEditModel: "getEditModel",
                    saveEditModel: "saveEditModel",
                    deleteByPromotionId: "deleteByPromotionId",
                    setPromotionStatus:"setPromotionStatus"
                },
                "promotionDetailService": {
                    "root": "/cms/promotion/detail",
                    "getPromotionGroup": "getPromotionGroup",
                    "getPromotionCode": "getPromotionCode",
                    "getPromotionSku": "getPromotionSku",
                    "uploadPromotion": "uploadPromotion",
                    "teJiaBaoInit": "teJiaBaoInit",
                    "updatePromotionProduct": "updatePromotionProduct",
                    "delPromotionModel": "delPromotionModel",
                    "delPromotionCode": "delPromotionCode"
                }
            },
            "jmpromotion": {
                "jmPromotionService": {
                    "root": "/cms/jmpromotion/index",
                    "init": "init",
                    "get": "get",
                    "getListByWhere": "getListByWhere",
                    "getEditModel": "getEditModel",
                    "saveModel": "saveModel",
                    "delete": "delete",
                    "getTagListByPromotionId": "getTagListByPromotionId"
                },
                "jmPromotionProductAddService": {
                    "root": "/cms/jm/promotion/product/",
                    "add": "add",
                    "getPromotionTags": "getPromotionTags"
                },
                "jmPromotionDetailService": {
                    "root": "/cms/jmpromotion/detail",
                    "init": "init",
                    "getPromotionProductInfoListByWhere": "getPromotionProductInfoListByWhere",
                    "getPromotionProductInfoCountByWhere": "getPromotionProductInfoCountByWhere",
                    "delete": "delete",
                    "deleteByPromotionId": "deleteByPromotionId",
                    "deleteByProductIdList": "deleteByProductIdList",
                    "jmNewUpdateAll": "jmNewUpdateAll",
                    "jmNewByProductIdListInfo": "jmNewByProductIdListInfo",
                    "updateDealEndTime": "updateDealEndTime",
                    "updateDealEndTimeAll": "updateDealEndTimeAll",
                    "getProductDetail": "getProductDetail",
                    "updateProductDetail": "updateProductDetail",
                    "updatePromotionProductDetail": "updatePromotionProductDetail",
                    "updateSkuDetail": "updateSkuDetail",
                    "deletePromotionSku": "deletePromotionSku",
                    "getProductMasterData": "getProductMasterData",
                    "updateJM": "updateJM",
                    "batchUpdateDealPrice": "batchUpdateDealPrice",
                    "batchSynchPrice": "batchSynchPrice",
                    "synchAllPrice": "synchAllPrice",
                    "batchCopyDeal": "batchCopyDeal",
                    "copyDealAll": "copyDealAll",
                    "batchDeleteProduct": "batchDeleteProduct",
                    "deleteAllProduct": "deleteAllProduct",
                    "getProductView": "getProductView",
                    "updateDealPrice": "updateDealPrice",
                    updatePromotionProduct:"updatePromotionProduct",
                    updatePromotionProductTag:"updatePromotionProductTag"
                },
                "cmsBtJmPromotionImportTask": {
                    "cmsBtJmPromotionImportTaskService": {
                        "root": "/cms/CmsBtJmPromotionImportTask/index",
                        "getByPromotionId": "getByPromotionId"
                    }
                }
            },
            "cmsBtJmPromotionExportTask": {
                "cmsBtJmPromotionExportTaskService": {
                    "root": "/cms/CmsBtJmPromotionExportTask/index",
                    "getByPromotionId": "getByPromotionId",
                    "addExport": "addExport"
                }
            },
            "cmsMtImageCreate": {
                "cmsMtImageCreateService": {
                    "root": "/cms/imagecreate/index",
                    "getPageByWhere": "getPageByWhere",
                    "getCountByWhere": "getCountByWhere"
                }
            },
            "imageManager": {
                "$CmsMtMasterInfoService": {
                    "root": "/cms/cmsmtmasterinfo/index",
                    "search": "getListByWhere",
                    "addImage": "insert",
                    "saveImage": "update",
                    "deleteImage": "delete",
                    "updateJMImg": "updateJMImg",
                    "getCountByWhere": "getCountByWhere",
                    "loadJmMasterBrand": "loadJmMasterBrand"
                },
                "$CmsMtJmConfigService": {
                    "root": "/cms/cmsmtjmconfig/index",
                    "init": "init",
                    "insert": "insert",
                    "update": "update",
                    "getByKey": "getByKey"
                }
            },
            "task": {
                "taskService": {
                    "root": "/cms/promotion/task",
                    "page": "page"
                },
                "taskBeatService": {
                    "root": "/cms/task/beat",
                    "create": "create",
                    "page": "page",
                    "import": "import",
                    "download": "download",
                    "control": "control",
                    "addNumiid": "addNumiid",
                    "addCode": "addCode",
                    "addCheck": "addCheck",
                    "add": "add",
                    "getTemplates": "getTemplates"
                },
                "taskPriceService": {
                    "root": "/cms/task/price",
                    "getPriceList": "getPriceList",
                    "updateTaskStatus": "updateTaskStatus"
                },
                "taskStockService": {
                    "root": "/cms/promotion/task_stock",
                    "initNewTask": "initNewTask",
                    "saveTask": "saveTask",
                    "delTask": "delTask",
                    "searchStock": "searchStock",
                    "getCommonStockList": "getCommonStockList",
                    "getRealStockList": "getRealStockList",
                    "initNewRecord": "initNewRecord",
                    "getUsableStock": "getUsableStock",
                    "saveNewRecord": "saveNewRecord",
                    "importStockInfo": "importStockInfo",
                    "exportStockInfo": "exportStockInfo",
                    "executeStockSeparation": "executeStockSeparation",
                    "executeStockRevert": "executeStockRevert",
                    "saveRecord": "saveRecord",
                    "delRecord": "delRecord",
                    "getSkuSeparationDetail": "getSkuSeparationDetail",
                    "exportErrorInfo": "exportErrorInfo"
                },
                "taskStockIncrementService": {
                    "root": "/cms/promotion/task_stock_increment",
                    "searchTask": "searchTask",
                    "searchSubTask": "searchSubTask",
                    "saveTask": "saveTask",
                    "delTask": "delTask",
                    "getPlatFormList": "getPlatFormList"
                },
                "taskStockIncrementDetailService": {
                    "root": "/cms/promotion/task_stock_increment_detail",
                    "searchItem": "searchItem",
                    "saveItem": "saveItem",
                    "delItem": "delItem",
                    "importStockInfo": "importStockInfo",
                    "exportStockInfo": "exportStockInfo",
                    "executeStockIncrementSeparation": "executeStockIncrementSeparation"
                }
            },
            "system": {
                "category": {
                    "systemCategoryService": {
                        "root": "/cms/system/category",
                        "getCategoryList": "getCategoryList",
                        "getCategoryDetail": "getCategoryDetail",
                        "updateCategorySchema": "updateCategorySchema",
                        "getNewsCategoryList": session("getNewsCategoryList")
                    },
                    "categorySettingService": {
                        "root": "/cms/system/category/setting",
                        "getMasterSubCategoryList": "getMasterSubCategoryList",
                        "getPlatformSubCategoryList": "getPlatformSubCategoryList"
                    }
                },
                "systemCartService": {
                    "root": "/cms/system/cart",
                    "search": "list",
                    "delete": "delete",
                    "saveOrUpdate": "saveOrUpdate",
                    "save": "save"
                },
                "platform": {
                    "platformService": {
                        "root": "/cms/system/platform",
                        "list": "list"
                    }
                },
                "error": {
                    "$errorListService": {
                        "root": "/cms/system/error",
                        "init": "init",
                        "search": "search",
                        "updateFinishStatus": "updateFinishStatus"
                    }
                },
                "$storeOpService": {
                    "root": "/cms/system/store_operation/",
                    "init": "init",
                    "rePublist": "rePublish",
                    "reUpload": "reUpload",
                    "rePublistPrice": "rePublishPrice",
                    "getHistory": "getHistory"
                }
            },
            "pop": {
                "$addToPromotionService": {
                    "root": "/cms/pop/add_to_promotion",
                    "getPromotionTags": "getPromotionTags",
                    "addToPromotion": "addToPromotion",
                    "checkPromotionTags": "checkPromotionTags"
                },
                "$fieldEditService": {
                    "root": "/cms/pop/field_edit",
                    "getPopOptions": "getPopOptions",
                    "setProductFields": "setProductFields"
                },
                "$promotionHistoryService": {
                    "root": "/cms/pop/history_promotion",
                    "getPromotionHistory": "getPromotionHistory"
                },
                priceLogService: {
                    root: '/cms/price/log',
                    page: 'page',
                    export: 'export'
                },
                statusHistoryService: {
                    root: "/cms/product/statushistory",
                    getPage: "getPage"
                },
                "$addChannelCategoryService": {
                    "root": "/cms/pop/add_to_channel_category",
                    "init": "getChannelCategory",
                    "save": "saveChannelCategory"
                }
            },
            "translation": {
                "translationService": {
                    "root": "/cms/translation/tasks",
                    // "getTasks": "getTasks",
                    // "searchHistoryTasks": "searchHistoryTasks",
                    // "assignTasks": "assignTasks",
                    // "copyFormMainProduct": "copyFormMainProduct",
                    // "saveTask": "saveTask",
                    // "submitTask": "submitTask",
                    // "cancelTask": "cancelTask",
                    // "getFeedAttributes": "getFeedAttributes",
                    "init": "init",
                    "assign": "assign",
                    "save": "save",
                    "search": "search",
                    "get": "get",
                    "submit": "submit"

                }
            },
            "channel": {
                "custom": {
                    "attributeService": {
                        "root": "/cms/channel/custom/prop",
                        "init": "get",
                        "getCatTree": session("getCatTree"),
                        "save": "update",
                        "getCatList": "getCatList"
                    }
                },
                "value": {
                    "attributeValueService": {
                        "root": "/cms/channel/custom/value",
                        "init": "get",
                        "add": "create",
                        "save": "update"
                    }
                },
                "usjoi": {
                    "usjoiService": {
                        "root": "/cms/channel/usjoi/",
                        "getList": "getList",
                        "update": "update",
                        "getCarts": "getCarts",
                        "save": "save",
                        "getCompanys": "getCompanys",
                        "updateCartIds": "updateCartIds",
                        "genKey": "genKey"
                    }
                },
                "tag": {
                    "channelTagService": {
                        "root": "/cms/channel/tag",
                        "init": "init",
                        "save": "saveTag",
                        "del": "delTag",
                        "getTagList": "getTagList"
                    }
                },
                "sellerCat": {
                    "sellerCatService": {
                        "root": "/cms/channel/category/",
                        "init": "getSellerCatConfig",
                        "getCat": "getSellerCat",
                        "addCat": "addSellerCat",
                        "delCat": "removeSellerCat",
                        "updateCat": "updateSellerCat"
                    }
                },
                "imageTemplateService": {
                    "root": "/cms/channel/image_template",
                    "init": "init",
                    "getPage": "getPage",
                    "getCount": "getCount",
                    "save": "save",
                    "delete": "delete",
                    "get": "get",
                    "getTemplateParameter": "getTemplateParameter",
                    "getDownloadUrl": "getDownloadUrl"
                },
                "imageGroupService": {
                    "root": "/cms/channel/image_group",
                    "init": "init",
                    "search": "search",
                    "save": "save",
                    "delete": "delete"
                },
                "imageGroupDetailService": {
                    "root": "/cms/channel/image_group_detail",
                    "init": "init",
                    "search": "search",
                    "save": "save",
                    "saveImage": "saveImage",
                    "saveUploadImage": "saveUploadImage",
                    "delete": "delete",
                    "refresh": "refresh",
                    "move": "move"
                },
                "sizeChart": {
                    "sizeChartService": {
                        "root": "/cms/channel/sizeChartList",
                        "init": "sizeChartInit",
                        "search": "sizeChartSearch",
                        "delete": "sizeChartDelete",
                        "editSave": "sizeChartEditSave"
                    }
                },
                "sizeChartDetail": {
                    "sizeChartDetailService": {
                        "root": "/cms/channel/sizeChartDetail",
                        "init": "sizeChartDetailSearch",
                        "detailSave": "sizeChartDetailSave",
                        "detailSizeMapSave": "sizeChartDetailSizeMapSave"
                    }
                }
            },
            "hsCodeInfo": {
                "hsCodeInfoService": {
                    "root": "/cms/tools/product",
                    "init": "initHsCodeInfo",
                    "search": "searchHsCodeInfo",
                    "get": "getHsCodeInfo",
                    "save": "saveHsCodeInfo",
                    "cancel": "cancelHsCodeInfo"
                }
            }
        }
    };
});