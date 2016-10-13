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
                    "getCmsConfig": session('getCmsConfig', [KEY.CHANNEL])
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
                    "exportSearch": "exportSearch",
                    "exportDownload": "exportDownload",
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
                    "exportSearch": "exportSearch",
                    "download": "download"
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
                    "updateLock": "updateLock",
                    "updateProductAtts": "updateProductAtts",
                    "checkCategory": "checkCategory",
                    "getChangeMastProductInfo": "getChangeMastProductInfo",
                    "setMastProduct": "setMastProduct",
                    "delisting": "delisting",
                    "delistinGroup": "delistinGroup",
                    "hsCodeChg": "hsCodeChg",
                    "copyProperty": "copyProperty",
                    "copyCommonProperty": "copyCommonProperty",
                    priceConfirm:"priceConfirm",
                    getPlatformCategories: {url: "getPlatformCategories", cache: CACHE.LOCAL},
                    updateSkuPrice:"updateSkuPrice"
                },
                "productHistoryLogService": {
                    "root": "/cms/product/history/",
                    "getPutOnOffLogList": "getPutOnOffLogList"
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
                    getPage: "getPage",
                    getCount: "getCount",
                    getEditModel: "getEditModel",
                    saveEditModel: "saveEditModel",
                    deleteByPromotionId: "deleteByPromotionId",
                    setPromotionStatus: "setPromotionStatus"
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
                    "saveSkuPromotionPrices":"saveSkuPromotionPrices"
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
                    updatePromotionProduct: "updatePromotionProduct",
                    updatePromotionProductTag: "updatePromotionProductTag"
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
                },
                "$valueChannelService": {
                    "root": "/cms/system/valueChannel/",
                    "addHsCodes": "addHsCode"
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
                    "setProductFields": "setProductFields",
                    "dldUnProcCode4PriceSale": "dldUnProcCode4PriceSale"
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
                        "root": "/cms/channel/custom/prop",
                        "init": "get",
                        "getCatTree": session("getCatTree", [KEY.CHANNEL]),
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
                    "delete": "delete",
                    "getNoMatchSizeImageGroupList":"getNoMatchSizeImageGroupList"
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
                        "getNoMatchList":"getNoMatchList",
                        "getListImageGroupBySizeChartId":"getListImageGroupBySizeChartId"
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
            "blackBrandService":{
                root:"/cms/channel/black_brand/",
                list:"searchBlackBrand",
                update:"updateBlackBrand"
            },
            //主品牌匹配页面
            "masterBrandService":{
                root:"/cms/tools/common/",
                search:"getMasterBrandInfo"
            },
            //主品牌管理页面
            "masterBrandApplicationService":{
                root:"/cms/maintain/common/",
                init:"initMasterBrandInfo",
                search:"getMasterBrandInfo",
                reviewed:"reviewedMasterBrandInfo",
                edit:"editMasterBrandInfo",
                mappingSearch:"mappingMasterBrandInfoToPlatform"
            }
        }
    };
});