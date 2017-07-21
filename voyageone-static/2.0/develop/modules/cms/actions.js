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
                    "getHomeSumData": "getHomeSumData",
                    "getCmsConfig": session('getCmsConfig', [KEY.CHANNEL]),
                    getMenuHeaderInfo: "getMenuHeaderInfo"
                },
                "$modifyPassWordService": {
                    "root": "/cms/home/menu/modifyPassword/",
                    "save": "save"
                }
            },
            "search": {
                "$searchAdvanceService2": {
                    "root": "/cms/search/advanceSearch/",
                    "init": "init",
                    "search": "search",
                    "searchAutoComplete": "searchAutoComplete",
                    "getGroupList": "getGroupList",
                    "getProductList": "getProductList",
                    "exportProducts": "exportProducts",
                    "exportSearch": "exportSearch",
                    "exportDownload": "exportDownload",
                    "getCustColumnsInfo": "getCustColumnsInfo",
                    "saveCustColumnsInfo": "saveCustColumnsInfo",
                    "addFreeTag": "addFreeTag",
                    "getCustSearchList": "getCustSearchList",
                    "getSkuInventory": "getSkuInventory"
                },
                "$searchAdvanceSolrService": {
                    "root": "/cms/search/advanceSearchSolr/",
                    "init": "init",
                    "search": "search",
                    "searchAutoComplete": "searchAutoComplete",
                    "getGroupList": "getGroupList",
                    "getProductList": "getProductList",
                    "exportProducts": "exportProducts",
                    "exportSearch": "exportSearch",
                    "exportDownload": "exportDownload",
                    "getCustColumnsInfo": "getCustColumnsInfo",
                    "saveCustColumnsInfo": "saveCustColumnsInfo",
                    "addFreeTag": "addFreeTag",
                    "getCustSearchList": "getCustSearchList",
                    "getSkuInventory": "getSkuInventory"
                },
                "$feedSearchService": {
                    "root": "/cms/search/feed/",
                    "init": "init",
                    "search": "search",
                    "updateFeedStatus": "updateFeedStatus",
                    "doExport": "export",
                    "exportSearch": "exportSearch",
                    "download": "download",
                    "updateMainCategory": "updateMainCategory",
                    "batchUpdateMainCategory": "batchUpdateMainCategory"
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
                    root: "/cms/product/detail/",
                    setCartSkuIsSale: "setCartSkuIsSale",
                    getCalculateCartMsrp: "getCalculateCartMsrp",
                    saveCartSkuPrice: "saveCartSkuPrice",
                    getProductPriceSales: "getProductPriceSales",
                    getProductInfo: "getProductInfo",
                    updateProductMasterInfo: "updateProductMasterInfo",
                    updateProductSkuInfo: "updateProductSkuInfo",
                    updateProductAllInfo: "updateProductAllInfo",
                    changeCategory: "changeCategory",
                    refreshProductCategory: "refreshProductCategory",
                    revertCategory: "revertCategory",
                    getProductPlatform: "getProductPlatform",
                    changePlatformCategory: "changePlatformCategory",
                    updateProductPlatform: "updateProductPlatform",
                    updateProductPlatformChk: "updateProductPlatformChk",
                    updateProductFeed: "updateProductFeed",
                    getCommonProductInfo: "getCommonProductInfo",
                    getCommonProductSkuInfo: "getCommonProductSkuInfo",
                    updateCommonProductInfo: "updateCommonProductInfo",
                    updateLock: "updateLock",
                    doAppSwitch: "doAppSwitch",
                    doTranslateStatus: "doTranslateStatus",
                    updateProductAtts: "updateProductAtts",
                    checkCategory: "checkCategory",
                    getChangeMastProductInfo: "getChangeMastProductInfo",
                    setMastProduct: "setMastProduct",
                    delisting: "delisting",
                    delistinGroup: "delistinGroup",
                    hsCodeChg: "hsCodeChg",
                    copyProperty: "copyProperty",
                    copyCommonProperty: "copyCommonProperty",
                    resetTmProduct: "resetTmProduct",
                    getMainCategoryInfo: "getMainCategoryInfo",
                    priceConfirm: "priceConfirm",
                    getPlatformCategories: {url: "getPlatformCategories", cache: CACHE.LOCAL},
                    updateSkuPrice: "updateSkuPrice",
                    moveCodeInitCheck: "moveCodeInitCheck",
                    moveCodeInit: "moveCodeInit",
                    moveCodeSearch: "moveCodeSearch",
                    moveCodePreview: "moveCodePreview",
                    moveCode: "moveCode",
                    moveSkuInitCheck: "moveSkuInitCheck",
                    moveSkuInit: "moveSkuInit",
                    moveSkuSearch: "moveSkuSearch",
                    moveSkuPreview: "moveSkuPreview",
                    moveSku: "moveSku",
                    getSkuStockInfo: 'getSkuStockInfo',
                    restoreImg: "restoreImg",
                    restorePlatFromImg: "restorePlatformImg",
                    doAppSwitch: "doAppSwitch",
                    doTranslateStatus: "doTranslateStatus",
                    getProductIdByCode: "getProductIdByCode",
                    upperLowerFrame: "upperLowerFrame",
                    lockPlatForm: "lockPlatForm",
                    updateGroupPlatform: "updateGroupPlatform",
                    updateOriginalTitleCn: "updateOriginalTitleCn"
                },
                "productHistoryLogService": {
                    "root": "/cms/product/history/",
                    "getPutOnOffLogList": "getPutOnOffLogList"
                },
                "combinedProductService": {
                    "root": "/cms/combined/product",
                    "init": "init",
                    "search": "search",
                    "getCombinedProductPlatformDetail": "getCombinedProductPlatformDetail",
                    "add": "add",
                    "getSkuDetail": "getSkuDetail",
                    "delete": "delete",
                    "getCombinedProductDetail": "getCombinedProductDetail",
                    "edit": "edit",
                    "onOffShelves": "onOffShelves",
                    "getOperateLogs": "getOperateLogs",
                    "batchGetSkuDetail": "batchGetSkuDetail"
                },
                productTopService: {
                    "root": "/cms/producttop",
                    "init": "init",
                    "getPage": "getPage",
                    "getCount": "getCount",
                    "getTopList": "getTopList",
                    "addTopProduct": "addTopProduct",
                    "saveTopProduct": "saveTopProduct"
                }
            },
            "mapping": {
                // 原功能已删除
                // 但内部的 action 被其他功能调用, 所以暂时保留
                // 具体的 action 指向其他根地址
                "feedMappingService": {
                    root: "/cms",
                    getMainCategories: {
                        root: "/cms/home/menu/",
                        url: "getMainCategories",
                        cache: CACHE.LOCAL
                    }
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
                    'searchCustBrands': 'searchCustBrands',
                    'searchMatchedBrands': 'searchMatchedBrands',
                    'addNewBrandMapping': 'addOrUpdateBrandMapping',
                    'getSynchronizedTime': 'getSynchronizedTime',
                    'synchronizePlatformBrands': 'synchronizePlatformBrands'
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
                    "getPromotionExportTask": "getPromotionExportTask",
                    "downloadPromotionExport": "downloadPromotionExport",
                    getPage: "getPage",
                    getCount: "getCount",
                    getEditModel: "getEditModel",
                    saveEditModel: "saveEditModel",
                    deleteByPromotionId: "deleteByPromotionId",
                    setPromotionStatus: "setPromotionStatus",
                    getPromotionSimpleList: "getPromotionSimpleList",
                    test: "test"
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
                    "delPromotionCode": "delPromotionCode",
                    "tmallJuhuasuanExport": "tmallJuhuasuanExport",
                    "tmallPromotionExport": "tmallPromotionExport",
                    "getPromotionSkuList": "getPromotionSkuList",
                    "saveSkuPromotionPrices": "saveSkuPromotionPrices",
                    "updatePromotionProductTag": "updatePromotionProductTag",
                    "updatePromotionListProductTag":"updatePromotionListProductTag",
                    "addPromotionByGroup": "addPromotionByGroup"
                }
            },
            "jmpromotion": {
                "jmPromotionService": {
                    "root": "/cms/jmpromotion/index",
                    "init": "init",
                    "get": "get",
                    "getJmPromList": "getJmPromList",
                    "getJmPromCount": "getJmPromCount",
                    "getListByWhere": "getListByWhere",
                    "getEditModel": "getEditModel",
                    "getEditModelExt": "getEditModelExt",
                    "saveModel": "saveModel",
                    "encore": "encore",
                    "delete": "delete",
                    "getTagListByPromotionId": "getTagListByPromotionId",
                    getBayWindow: 'getBayWindow',
                    saveBayWindow: 'saveBayWindow'
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
                    "batchSynchMallPrice": "batchSynchMallPrice",
                    "synchAllPrice": "synchAllPrice",
                    "batchCopyDeal": "batchCopyDeal",
                    "copyDealAll": "copyDealAll",
                    "batchDeleteProduct": "batchDeleteProduct",
                    "deleteAllProduct": "deleteAllProduct",
                    "getProductView": "getProductView",
                    "updateDealPrice": "updateDealPrice",
                    "refreshPrice": "refreshPrice",
                    "updateRemark": "updateRemark",
                    "updatePromotionListProductTag": "updatePromotionListProductTag",
                    "batchUpdateSkuDealPrice": "batchUpdateSkuDealPrice",
                    updatePromotionProduct: "updatePromotionProduct",
                    updatePromotionProductTag: "updatePromotionProductTag",
                    getPromotionTagModules: 'getPromotionTagModules',
                    savePromotionTagModules: 'savePromotionTagModules',
                    getPromotionProducts: 'getPromotionProducts',
                    saveProductSort: 'saveProductSort',
                    "setJmPromotionStepStatus": 'setJmPromotionStepStatus',
                    getJmTemplateUrls: {url: 'getJmTemplateUrls', cache: CACHE.LOCAL}
                },
                "JmPromotionImagesService": {
                    "root": "/cms/jmPromotion/images",
                    "init": "init",
                    "save": "save",
                    "getImageForSuit": "getImageForSuit",
                    "getImageTemplate": "getImageTemplate",
                    "downloadSpecialImageZip": "downloadSpecialImageZip",
                    "downloadWaresImageZip": "downloadWaresImageZip"
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
                    "addExport": "addExport",
                    "exportJmPromotionInfo": "exportJmPromotionInfo"
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
                    "updateTaskStatus": "updateTaskStatus",
                    "refreshAllPromotionByCustomPromotionId": "refreshAllPromotionByCustomPromotionId",
                    "delAllPromotionByCustomPromotionId": "delAllPromotionByCustomPromotionId"
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
                        "getNewsCategoryList": session("getNewsCategoryList", [KEY.CHANNEL])
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
                    },
                    "$mqErrorListService": {
                        "root": "/cms/system/mqError",
                        "init": "init",
                        "search": "search"
                    }
                },
                "$storeOpService": {
                    "root": "/cms/system/store_operation/",
                    "init": "init",
                    "rePublist": "rePublish",
                    "reUpload": "reUpload",
                    "rePublistPrice": "rePublishPrice",
                    "getHistory": "getHistory"
                },
                "$valueChannelService": {
                    "root": "/cms/system/valueChannel/",
                    "addHsCodes": "addHsCode",
                    "addEtkHsCode": "addEtkHsCode",
                    "updateKaoLaNumiid":"updateKaoLaNumiid"
                }
            },
            "pop": {
                "addProductToPromotionService": {
                    "root": "/cms/pop/add_product_to_promotion",
                    "init": "init",
                    "save": "save"
                },
                "$fieldEditService": {
                    root: "/cms/pop/field_edit",
                    getPopOptions: "getPopOptions",
                    setProductFields: "setProductFields",
                    dldUnProcCode4PriceSale: "dldUnProcCode4PriceSale",
                    bulkSetCategory: "bulkSetCategory",
                    bulkSetPlatformFields: "bulkSetPlatformFields",
                    getPlatfromPopOptions: "getPlatfromPopOptions",
                    intelligentPublish: "intelligentPublish",
                    bulkLockProducts: "bulkLockProducts",
                    bulkConfClientMsrp: "bulkConfClientMsrp"
                },
                "$promotionHistoryService": {
                    "root": "/cms/pop/history_promotion",
                    "getPromotionHistory": "getPromotionHistory",
                    "getUnduePromotion": "getUnduePromotion"
                },
                priceLogService: {
                    root: '/cms/price/log',
                    page: 'page',
                    export: 'export'
                },
                priceConfirmLogService: {
                    root: '/cms/price/confirm',
                    page: 'page'
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
                        root: "/cms/channel/custom/prop",
                        init: "get",
                        getCatTree: session("getCatTree", [KEY.CHANNEL]),
                        save: "update",
                        getCatList: "getCatList"
                    },
                    "attributeService2": {
                        root: "/cms/channel/custProp",
                        search: "search",
                        doSetCustomshIsDispPlay: "doSetCustomshIsDispPlay",
                        doUpdateEntity: "doUpdateEntity",
                        doSetSort: "doSetSort",
                        delete: "delete",
                        saveFeed: "saveFeed",
                        createFeed: "createFeed",
                        init: 'init'
                    },
                    "attrTranslateService": {
                        root: "/cms/channel/customTranslate/value",
                        init: "init",
                        create: "create",
                        update: "update"
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
                        "updateCat": "updateSellerCat",
                        "sortableCat": "sortableCat"
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
                    "delete": "delete",
                    "getNoMatchSizeImageGroupList": "getNoMatchSizeImageGroupList"
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
                        "editSave": "sizeChartEditSave",
                        "getNoMatchList": "getNoMatchList",
                        "getListImageGroupBySizeChartId": "getListImageGroupBySizeChartId",
                        getProductSizeChartList: "getProductSizeChartList"
                    }
                },
                "sizeChartDetail": {
                    "sizeChartDetailService": {
                        "root": "/cms/channel/sizeChartDetail",
                        "init": "sizeChartDetailSearch",
                        "detailSave": "sizeChartDetailSave",
                        "detailSizeMapSave": "sizeChartDetailSizeMapSave"
                    }
                },
                "MtChannelConfig": {
                    "cmsMTChannelConfigService": {
                        "root": "/cms/mt/channel/config",
                        "search": "search",
                        "saveList": "saveList"
                    }
                },
                "FeedConfig": {
                    "cmsFeedConfigService": {
                        "root": "/cms/channel/feedConfig",
                        "search": "search",
                        "save": "save",
                        "export": "export",
                        "import": "import",
                        "delete": "delete",
                        "saveFeed": "saveFeed",
                        "createFeed": "createFeed"
                    }
                }
            },
            "channelConfig": {
                "channelConfigService": {
                    "root": "/cms/channel/config",
                    "init": "init",
                    "loadByChannel": "loadByChannel",
                    "addChannelConfig": "addChannelConfig",
                    "loadChannelConfigDetail": "loadChannelConfigDetail",
                    "editChannelConfig": "editChannelConfig",
                    "delChannelConfig": "delChannelConfig"
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
            },
            "rePriceService": {
                "root": "/cms/tools/reprice/",
                "getChannelList": {url: "getChannelList", cache: CACHE.LOCAL},
                "getPlatformList": "getPlatformList",
                "getCartList": "getCartList",
                "getPlatformCategoryList": {url: "getPlatformCategoryList", cache: CACHE.LOCAL},
                "setUpdateFlg": "setUpdateFlg"
            },
            "platformMappingService": {
                root: "/cms/platform/mapping/",
                page: "page",
                get: "get",
                save: "save",
                delete: "delete",
                getCommonSchema: {
                    url: "getCommonSchema",
                    cache: CACHE.LOCAL
                },
                getFeedCustomProps: {
                    url: "getFeedCustomProps",
                    cache: CACHE.SESSION,
                    cacheWith: [KEY.CHANNEL]
                },
                refreshProducts: 'refreshProducts',
                // 原 platform mapping 的功能已删除
                // 原 platformMappingService 的以下两个 action 被其他内容调用
                // 所以暂时寄存在新的 platformMappingService 下
                getPlatformCategories: {
                    root: "/cms/product/detail/",
                    url: "getPlatformCategories",
                    cache: CACHE.LOCAL,
                    cacheWith: [KEY.CHANNEL]
                },
                getCarts: {
                    root: "/cms/home/menu/",
                    url: "getCarts",
                    cache: CACHE.LOCAL,
                    cacheWith: [KEY.CHANNEL]
                }
            },
            "blackBrandService": {
                root: "/cms/channel/black_brand/",
                init: "init",
                list: "searchBlackBrand",
                update: "updateBlackBrand"
            },
            //主品牌匹配页面
            "masterBrandService": {
                root: "/cms/tools/common/",
                search: "getMasterBrandInfo"
            },
            //主品牌管理页面
            "masterBrandApplicationService": {
                root: "/cms/maintain/common/",
                init: "initMasterBrandInfo",
                search: "getMasterBrandInfo",
                reviewed: "reviewedMasterBrandInfo",
                edit: "editMasterBrandInfo",
                mappingSearch: "mappingMasterBrandInfoToPlatform"
            },
            // 货架模板管理
            "shelvesTemplateService": {
                root: "/cms/shelves/template",
                init: "init",
                add: "add",
                edit: "edit",
                delete: "delete",
                search: "search",
                detail: "detail"
            },
            // 货架管理
            shelvesService: {
                root: "/cms/shelves/detail",
                search: "search",
                createShelves: "createShelves",
                updateShelves: "updateShelves",
                updateProductSort: "updateProductSort",
                addProduct: "addProduct",
                getShelvesInfo: "getShelvesInfo",
                removeProduct: "removeProduct",
                clearProduct: "clearProduct",
                deleteShelves: "deleteShelves",
                releaseImage: "releaseImage",
                getShelvesHtml: "getShelvesHtml"
            },
            "biReportService": {
                "root": "/cms/biReport/download/",
                "init": "init",
                getPage: "getPage",
                getCount: "getCount",
                biRepDownload: "biRepDownload",
                createXlsFile: "createXlsFile",
                getDownloadTaskList: "getDownloadTaskList",
                createXlsFileTask: "createXlsFileTask",
                get_channel_list: "get_channel_list",
                deleteTask: "deleteTask"
            },
            commonService: {
                "root": "/cms/us/common",
                "getChannelCarts": "getChannelCarts",
                "getFeedInfo": "getFeedInfo"
            },
            itemDetailService: {
                root: '/cms/us/feed',
                detail: "detail",
                update: "update",
                getTopModel: "getTopModel",
                updateOne: "updateOne",
                setPrice: "setPrice",
                list: "list",
                approve: "approve"
            },
            advanceSearch: {
                root: "/cms/us/advanceSearch",
                init: "init",
                search: "search",
                getCustomColumns: "getCustomColumns",
                saveCustomColumns: "saveCustomColumns",
                updatePrice: "updatePrice",
                listOrDelist: "listOrDelist"
            },
            usTagService: {
                root: "/cms/us/tag",
                init: "init"
            },
            $usProductDetailService: {
                root: '/cms/usa/product',
                getProductInfo: 'getProductInfo',
                updateOnePrice:'updateOnePrice',
                getAllPlatformsPrice:'getAllPlatformsPrice'
            }
        }
    };
});