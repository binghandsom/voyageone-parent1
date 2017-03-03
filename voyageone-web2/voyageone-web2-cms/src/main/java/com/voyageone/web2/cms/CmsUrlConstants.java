package com.voyageone.web2.cms;

/**
 * 定义所有 CMS WEB2 的地址定义, 同步前端 JS 的 actions.json
 *
 * @author Jonas, 12/9/15
 * @version 2.0.0
 * @since 2.0.0
 */
public interface CmsUrlConstants {

    interface HOME {

        // menu
        interface MENU {

            String ROOT = "/cms/home/menu/";

            String GET_CATE_INFO = "getCategoryInfo";

            String GET_CATE_TYPE = "getPlatformType";

            String SET_CATE_TYPE = "setPlatformType";

            String GetHomeSumData = "getHomeSumData";

            String SumHome="sumHome";

            String GET_CMS_CONFIG = "getCmsConfig";

            String GET_MAIN_CATEGORIES = "getMainCategories";

            String GET_CARTS = "getCarts";

            String GetMenuHeaderInfo="getMenuHeaderInfo";
        }
        interface MODIFY_PASS_WORD {
            String ROOT = "/cms/home/menu/modifyPassword";
            String submit = "submit";
        }
    }

    interface JMPROMOTION {
        interface LIST {
            interface INDEX {
                String ROOT = "/cms/jmpromotion/index";
                String INIT = "init";
                String GET_LIST_BY_WHERE = "getListByWhere";
                String SaveModel = "saveModel";
                String ENCORE = "encore";
                String DELETE = "delete";
                String GET = "get";
                String GetEditModel = "getEditModel";
                String GetEditModelExt = "getEditModelExt";
                String GetTagListByPromotionId = "getTagListByPromotionId";
                String ImportJM = "importJM";
            }

            interface DETAIL {
                String ROOT = "/cms/jmpromotion/detail";
                String INIT = "init";
                String GET_LIST_BY_WHERE = "getListByWhere";
                String GET_PROMOTION_PRODUCT_INFO_LIST_BY_WHERE = "getPromotionProductInfoListByWhere";
                String GetPromotionProductInfoCountByWhere = "getPromotionProductInfoCountByWhere";
                String INSERT = "insert";
                String DELETE = "delete";
                String UPDATE = "update";
                String UPDATEDEAlPRICE = "updateDealPrice";
                String GET = "get";
                String DELETEBYPPROMOTIONID = "deleteByPromotionId";
                String DELETEBYPRODUCTIDLIST = "deleteByProductIdList";
                String JmNewUpdateAll = "jmNewUpdateAll";
                String JmNewByProductIdListInfo = "jmNewByProductIdListInfo";
                String updateDealEndTime = "updateDealEndTime";
                String UpdateDealEndTimeAll = "updateDealEndTimeAll";
                String GET_PRODUCT_DETAIL = "getProductDetail";
                String UPDATE_PRODUCT_DETAIL = "updateProductDetail";
                String UPDATE_PROMOTION_PRODUCT_DETAIL = "updatePromotionProductDetail";
                String UPDATE_SKU_DETAIL = "updateSkuDetail";
                String DELETE_PROMOTION_SKU = "deletePromotionSku";
                String GET_PRODUCT_MASTER_DATA = "getProductMasterData";
                String UpdateJM = "updateJM";
                String BatchUpdateDealPrice = "batchUpdateDealPrice";
                String BatchUpdateSkuDealPrice="batchUpdateSkuDealPrice";
                String BatchSynchPrice = "batchSynchPrice";
                String SynchAllPrice = "synchAllPrice";
                String BatchSynchMallPrice = "batchSynchMallPrice";
                String BatchCopyDeal = "batchCopyDeal";
                String CopyDealAll = "copyDealAll";
                String BatchDeleteProduct = "batchDeleteProduct";
                String DeleteAllProduct = "deleteAllProduct";
                String GetProductView = "getProductView";
                String UpdateDealPrice = "updateDealPrice";
                String UpdatePromotionProduct = "updatePromotionProduct";
                String UpdatePromotionProductTag = "updatePromotionProductTag";
                String UpdatePromotionListProductTag="updatePromotionListProductTag";
                String SelectChangeCountByPromotionId = "selectChangeCountByPromotionId";
                String UpdateRemark="updateRemark";
                String RefreshPrice="refreshPrice";
            }
        }

        interface Images{
            String ROOT = "/cms/jmPromotion/images";
            String INIT = "init";
            String SAVE = "save";
            String GET_IMAGE_FOR_SUIT = "getImageForSuit";
            String GET_IMAGE_TEMPLATE=  "getImageTemplate";
            String DOWNLOAD_SPECIAL_IMAGE_ZIP ="downloadSpecialImageZip";
            String DOWNLOAD_WARES_IMAGE_ZIP ="downloadWaresImageZip";
        }
    }

    interface CmsBtJmPromotionImportTask {
        interface LIST {
            interface INDEX {
                String ROOT = "/cms/CmsBtJmPromotionImportTask/index";
                String GET_BY_PROMOTIONID = "getByPromotionId";
            }
        }
    }

    interface CmsBtJmPromotionExportTask {
        interface LIST {
            interface INDEX {
                String ROOT = "/cms/CmsBtJmPromotionExportTask/index";
                String GET_BY_PROMOTIONID = "getByPromotionId";
                String ADDEXPORT = "addExport";
                String EXPORT_JM_PROMOTION_INFO = "exportJmPromotionInfo";
            }
        }
    }

    interface CMSMTMASTERINFO {
        interface LIST {
            interface INDEX {
                String ROOT = "/cms/cmsmtmasterinfo/index";
                String INIT = "init";
                String GET_LIST_BY_WHERE = "getListByWhere";
                String INSERT = "insert";
                String UPDATE = "update";
                String GET = "get";
                String UPDATEJMIMG = "updateJMImg";
                String GetCountByWhere = "getCountByWhere";
                String LoadJmMasterBrand = "loadJmMasterBrand";
            }
        }
    }

    interface CMSMTJMCONFIG {
        interface LIST {
            interface INDEX {
                String ROOT = "/cms/cmsmtjmconfig/index";
                String INIT = "init";
                String INSERT = "insert";
                String UPDATE = "update";
                String GETBYKEY = "getByKey";

            }
        }
    }

    // 活动管理
    interface PROMOTION {
        interface LIST {
            interface INDEX {

                String ROOT = "/cms/promotion/index";
                String INIT = "init";
                String InitByPromotionId = "initByPromotionId";
                String GetPage = "getPage";
                String GetCount = "getCount";
                String GetEditModel = "getEditModel";
                String SaveEditModel = "saveEditModel";
                String DeleteByPromotionId = "deleteByPromotionId";
                String SetPromotionStatus = "setPromotionStatus";
                String GET_PROMOTION_LIST = "getPromotionList";

                String INSERT_PROMOTION = "insertPromotion";

                String UPDATE_PROMOTION = "updatePromotion";

                String PROMOTION_EXPORT = "exportPromotion";

                String DEL_PROMOTION = "delPromotion";
                String TEST = "test";
            }

            interface DETAIL {

                String ROOT = "/cms/promotion/detail";

                String GetPromotionSkuList="getPromotionSkuList";

                String SaveSkuPromotionPrices="saveSkuPromotionPrices";

                String GET_PROMOTION_GROUP = "getPromotionGroup";

                String GET_PROMOTION_CODE = "getPromotionCode";

                String GET_PROMOTION_SKU = "getPromotionSku";

                String GET_PROMOTION_UPLOAD = "uploadPromotion";

                String TE_JIA_BAO_INIT = "teJiaBaoInit";

                String UPDATE_PROMOTION_PRODUCT = "updatePromotionProduct";

                String DEL_PROMOTION_MODEL = "delPromotionModel";

                String DEL_PROMOTION_CODE = "delPromotionCode";

                String TMALL_JUHUASUAN_EXPORT = "tmallJuhuasuanExport";

                String TMALL_PROMOTION_EXPORT = "tmallPromotionExport";
                String UpdatePromotionProductTag="updatePromotionProductTag";
            }
        }

        interface TASK {

            interface INDEX {

                String ROOT = "/cms/promotion/task";

                String PAGE = "page";
            }

            interface BEAT {

                String ROOT = "/cms/task/beat";

                String CREATE = "create";

                String PAGE = "page";

                String IMPORT = "import";

                String DOWNLOAD = "download";

                String CONTROL = "control";

                String ADD = "add";

                String ADD_CODE = "addCode";

                String ADD_NUMIID = "addNumiid";

                String ADD_CHECK = "addCheck";

                String GET_TEMPLATES = "getTemplates";
            }

            interface PRICE {

                String ROOT = "/cms/task/price";

                String GET_PRICE_LIST = "getPriceList";

                String UPDATE_TASK_STATUS = "updateTaskStatus";

                String DEL_ALL_PROMOTION_BY_CUSTOM_PROMOTION_ID = "delAllPromotionByCustomPromotionId";

                String REFRESH_ALL_PROMOTION_BY_CUSTOM_PROMOTION_ID = "refreshAllPromotionByCustomPromotionId";
            }

            interface STOCK {

                String ROOT = "/cms/promotion/task_stock";

                String INIT_NEW_TASK = "initNewTask";

                String SAVE_TASK = "saveTask";

                String DEL_TASK = "delTask";

                String SEARCH_STOCK = "searchStock";

                String GET_COMMON_STOCK_LIST = "getCommonStockList";

                String GET_REAL_STOCK_LIST = "getRealStockList";

//                String INIT_NEW_RECORD = "initNewRecord";

                String GET_USABLE_STOCK = "getUsableStock";

                String SAVE_NEW_RECORD = "saveNewRecord";

                String IMPORT_STOCK_INFO = "importStockInfo";

                String EXPORT_STOCK_INFO = "exportStockInfo";

                String EXECUTE_STOCK_SEPARATION = "executeStockSeparation";

                String EXECUTE_STOCK_REVERT = "executeStockRevert";

                String SAVE_RECORD = "saveRecord";

                String DEL_RECORD = "delRecord";

                String GET_SKU_SEPARATION_DETAIL = "getSkuSeparationDetail";

                String EXPORT_ERROR_INFO = "exportErrorInfo";
            }

            interface STOCK_INCREMENT {

                String ROOT = "/cms/promotion/task_stock_increment";

                String SEARCH_TASK = "searchTask";

                String SEARCH_SUB_TASK = "searchSubTask";

                String SAVE_TASK = "saveTask";

                String DEL_TASK = "delTask";

                String GET_PLATFORM_LIST = "getPlatFormList";

            }

            interface STOCK_INCREMENT_DETAIL {

                String ROOT = "/cms/promotion/task_stock_increment_detail";

                String SEARCH_ITEM = "searchItem";

                String SAVE_ITEM = "saveItem";

                String DEL_ITEM = "delItem";

                String IMPORT_STOCK_INFO = "importStockInfo";

                String EXPORT_STOCK_INFO = "exportStockInfo";

                String EXECUTE_STOCK_INCREMENT_SEPARATION = "executeStockIncrementSeparation";
            }
        }
    }

    // 检索
    interface SEARCH {

        interface ADVANCE {

            String ROOT = "/cms/search/advance/";

            String INIT = "init";
            String SEARCH = "search";
            String SEARCH_AUTO_COMPLETE_SOLR = "searchAutoComplete";

            String GET_GROUP_LIST = "getGroupList";
            String GET_PRODUCT_LIST = "getProductList";

            String EXPORT_PRODUCTS = "exportProducts";
            String EXPORT_SERACH = "exportSearch";
            String EXPORT_DOWNLOAD = "exportDownload";

			String GET_SKU_INVENTORY = "getSkuInventory";

        }

        interface FEED {
            String ROOT = "/cms/search/feed";
            String INIT = "init";
            String SEARCH = "search";
            String UPDATE = "updateFeedStatus";
            String EXPORT = "export";
            String REEXPORT = "reExport";
            String EXPORTSEARCH = "exportSearch";
            String DOWNLOAD = "download";
        }
    }

    // 匹配关系设置
    interface MAPPING {

        interface DICTIONARY {

            String ROOT = "/cms/mapping/dictionary";

            String INIT = "init";

            String GET_DICT = "getDict";

            String GET_CONST = "getConst";

            String GET_CUSTOMS = "getCustoms";

            String SET_DICT = "setDict";

            String DEL_DICT = "delDict";

            String ADD_DICT = "addDict";

            String GET_DICT_LIST = "getDictList";
        }

        /**
         * 品牌映射
         */
        interface BRAND {
            /**
             * 根路径
             */
            String ROOT = "/cms/mapping/brand";

            /**
             * 页面初始化
             */
            String INIT = "init";

            /**
             * 检索品牌映射关系
             */
            String SEARCH_BRANDS = "searchBrands";

            /**
             * 检索客户的品牌
             */
            String SEARCH_CUST_BRANDS = "searchCustBrands";

            /**
             * 检索已匹配的品牌
             */
            String SEARCH_MATCHED_BRANDS = "searchMatchedBrands";

            /**
             * 添加新匹配的品牌
             */
            String ADD_NEW_BRAND_MAPPING = "addNewBrandMapping";

            /**
             * 添加或更新匹配的品牌数据
             */
            String ADD_OR_UPDATE_BRAND_MAPPING = "addOrUpdateBrandMapping";

            /**
             * 同步平台品牌数据
             */
            String SYNCHRONIZE_PLATFORM_BRANDS = "synchronizePlatformBrands";

            /**
             * 取得平台品牌数据同步时间
             */
            String GET_SYNCHRONIZE_TIME = "getSynchronizedTime";
        }
    }

    // 系统设置
    interface SYSTEM {

        interface CATEGORY {

            String ROOT = "/cms/system/category";

            String GET_CATEGORY_LIST = "getCategoryList";

            String GET_CATEGORY_DETAIL = "getCategoryDetail";

            String UPDATE_CATEGORY_SCHEMA = "updateCategorySchema";

            String GET_NEW_CATEGORY_LIST = "getNewsCategoryList";
        }

        interface ERROR {

            String ROOT = "/cms/system/error";

            String INIT = "init";

            String SEARCH = "search";

            String UPDATE_FINISH_STATUS = "updateFinishStatus";
        }
        interface MQ_ERROR {

            String ROOT = "/cms/system/mqError";

            String INIT = "init";

            String SEARCH = "search";
        }

        interface CACHE {

            String ROOT = "/cms/system/cache";
            String INIT = "init";
            String CLEAR = "clear";
        }

        interface MQ {

            String ROOT = "/cms/system/mq";
            String INIT = "init";
            String SEND = "send";
        }
    }

    // popup页面
    interface POP {

        interface FIELD_EDIT {
            String ROOT = "/cms/pop/field_edit/";
            String GET_POP_OPTIONS = "getPopOptions";
            String SET_PRODUCT_FIELDS = "setProductFields";
            String DLD_PRODUCT_PROCESALE = "dldUnProcCode4PriceSale";
            String INTELLIGENT_PUBLISH = "intelligentPublish";
            String BULK_SET_CATEGORY = "bulkSetCategory";
            String BULK_SET_PLATFORM_FIELDS = "bulkSetPlatformFields";
            String GET_PLATFROM_POP_OPTIONS = "getPlatfromPopOptions";
        }

        interface ADD_TO_PROMOTION {

            String ROOT = "/cms/pop/add_to_promotion/";

            String GET_PROM_TAGS = "getPromotionTags";
            String CHECK_PROM_TAGS = "checkPromotionTags";
            String ADD_TO_PROMOTION = "addToPromotion";
        }
        interface  AddProductToPromotion
        {
            String ROOT = "/cms/pop/add_product_to_promotion/";
            String Save = "save";
            String Init="init";
        }

        interface PRICE_LOG {

            String ROOT = "/cms/price/log";

            String PAGE = "page";

            String EXPORT = "export";
        }

        interface PRICE_CONFIRM {

            String ROOT = "/cms/price/confirm";

            String PAGE = "page";
        }

        interface PROMOTION {

            String ROOT = "/cms/pop/history_promotion/";

            String GET_PROMOTION_HISTORY = "getPromotionHistory";

			String GET_UNDUE_PROMOTION = "getUnduePromotion";
        }

        interface IMAGE_SETTING {

            String ROOT = "/cms/pop/image_setting/";

            String UPLOAD_IMAGE = "uploadImage";

            String UPLOAD_IMAGES = "uploadImages";
        }

        interface ADD_TO_CHANNEL_CATEGORY {
            String ROOT = "/cms/pop/add_to_channel_category";
            String GET_CHANNEL_CATEGORY_INFO = "getChannelCategory";
            String SAVE_CHANNEL_CATEGORY_INFO = "saveChannelCategory";
        }

        interface JM_IMAGE_UPLOAD{

            String ROOT = "/cms/pop/jmPromotion/";

            String UPLOAD = "upload";

            String BATCH_UPLOAD = "batchUpload";
        }
    }

    // 产品编辑
    interface PRODUCT {

        interface DETAIL {
            String ROOT = "/cms/product/detail";

            String SaveCartSkuPrice="saveCartSkuPrice";

            String SetCartSkuIsSale="setCartSkuIsSale";

            String GetCalculateCartMsrp="getCalculateCartMsrp";

            String GetProductPriceSales="getProductPriceSales";

            String DoAppSwitch="doAppSwitch";

            String DoTranslateStatus="doTranslateStatus";

            String GET_PRODUCT_INFO = "getProductInfo";

            String UPDATE_PRODUCT_MASTER_INFO = "updateProductMasterInfo";

            String UPDATE_PRODUCT_SKU_INFO = "updateProductSkuInfo";

            String UPDATE_PRODUCT_ALL_INFO = "updateProductAllInfo";

            String UPDATE_PRODUCT_FEED = "updateProductFeed";

            String CHANGE_CATEGORY = "changeCategory";

            String REFRESH_PRODUCT_CATEGORY = "refreshProductCategory";

            String GET_PRODUCT_PLATFORM = "getProductPlatform";

            String UPDATE_PRODUCT_PLATFORM = "updateProductPlatform";

            String UPDATE_PRODUCT_PLATFORM_CHK = "updateProductPlatformChk";

            String CHANGE_PLATFORM_CATEGORY = "changePlatformCategory";

            String GET_COMMON_PRODUCTINFO = "getCommonProductInfo";

            String GET_COMMON_PRODUCT_SKU_INFO = "getCommonProductSkuInfo";

            String GET_MAIN_CATEGORY_INFO = "getMainCategoryInfo";

            String UPDATE_COMMON_PRODUCTINFO = "updateCommonProductInfo";

            String UPDATE_LOCK = "updateLock";

            String UPDATE_FEED_ATTS = "updateProductAtts";

            String CHECK_CATEGORY = "checkCategory";

            String GetChangeMastProductInfo = "getChangeMastProductInfo";

            String SetMastProduct = "setMastProduct";

            String Delisting = "delisting";

            String DelistinGroup = "delistinGroup";

            String HsCodeChg = "hsCodeChg";

            String CopyProperty = "copyProperty";

            String CopyCommonProperty = "copyCommonProperty";

            String GET_PLATFORM_CATEGORIES = "getPlatformCategories";

            String PriceConfirm = "priceConfirm";

            String UPDATE_SKUPRICE = "updateSkuPrice";

            String RESET_TM_PRODUCT = "resetTmProduct";

            String MOVE_CODE_INIT_CHECK = "moveCodeInitCheck";

            String MOVE_CODE_INIT = "moveCodeInit";

            String MOVE_CODE_SEARCH = "moveCodeSearch";

            String MOVE_CODE_PREVIEW = "moveCodePreview";

            String MOVE_CODE = "moveCode";

            String MOVE_SKU_INIT_CHECK = "moveSkuInitCheck";

            String MOVE_SKU_INIT = "moveSkuInit";

            String MOVE_SKU_SEARCH = "moveSkuSearch";

            String MOVE_SKU_PREVIEW = "moveSkuPreview";

            String MOVE_SKU = "moveSku";

            String RESTORE_IMG = "restoreImg";

			String GET_SKU_STOCK_INFO = "getSkuStockInfo";

			String GetProductIdByCode="getProductIdByCode";

			String RESTORE_PLATFORM_IMG = "restorePlatformImg";
        }

        interface HISTORY {
            String ROOT = "/cms/product/history";
            String GET_PUTONOFF_LOG_LIST = "getPutOnOffLogList";


        }

        interface StatusHistory {
            String ROOT = "/cms/product/statushistory";
            String GetPage = "getPage";
        }

        interface CombinedProduct {
            String ROOT = "cms/combined/product";
            String INIT = "init";
            String SEARCH = "search";
            String GET_COMBINED_PRODUCT_PLATFORM_DETAIL = "getCombinedProductPlatformDetail";
            String GET_SKU_DETAIL = "getSkuDetail";
            String ADD = "add";
            String DELETE = "delete";
            String GET_COMBINED_PRODUCT_DETAIL = "getCombinedProductDetail";
            String EDIT = "edit";
            String ON_OFF_SHELVES = "onOffShelves";
            String GET_OPERATE_LOGS = "getOperateLogs";
            String BATCH_GET_SKU_DETAIL = "batchGetSkuDetail";
        }
    }
    interface ProductTop {
        String ROOT = "/cms/producttop";
        String Init="init";
        String GetPage = "getPage";
        String GetCount = "getCount";
        String GetTopList="getTopList";
        String AddTopProduct="addTopProduct";
        String SaveTopProduct="saveTopProduct";
    }
    // 商品编辑
    interface GROUP {

        interface DETAIL {
            String ROOT = "/cms/group/detail";

            String INIT = "init";

            String GET_PRODUCT_LIST = "getProductList";

            String SET_MAIN_PRODUCT = "setMainProduct";
        }
    }

    interface TRANSLATION {

        interface TASKS {

            String ROOT = "/cms/translation/tasks";

            String INIT = "init";

            String SEARCH = "search";

            String ASSIGN = "assign";

            String SAVE = "save";

            String SUBMIT = "submit";

            String GET = "get";
        }
    }

    interface CHANNEL {

        interface CUSTOM_PROP {

            String ROOT = "/cms/channel/custom/prop";

            String INIT = "get";

            String SAVE = "update";

            String GET_CAT_TREE = "getCatTree";

            String GET_CAT_LIST = "getCatList";
        }

        interface CUSTOM_VALUE {

            String ROOT = "/cms/channel/custom/value";

            String INIT = "get";

            String ADD = "create";

            String SAVE = "update";
        }

        interface CUSTOM_TRANSLATE {

            String ROOT = "/cms/channel/customTranslate/value";

            String INIT = "init";

            String ADD = "create";

            String SAVE = "update";
        }

        interface CHANNEL_TAG {
            String ROOT = "/cms/channel/tag";
            String INIT_CHANNEL_TAG = "init";
            String SAVE_CHANNEL_TAG = "saveTag";
            String DEL_CHANNEL_TAG = "delTag";
            String GET_TAG_LIST = "getTagList";
        }

        interface CHANNEL_IMAGE_GROUP {
            String ROOT = "/cms/channel/image_group";
            String INIT_CHANNEL_IMAGE_GROUP = "init";
            String SEARCH_CHANNEL_IMAGE_GROUP = "search";
            String SAVE_CHANNEL_IMAGE_GROUP = "save";
            String DELETE_CHANNEL_IMAGE_GROUP = "delete";
            String GetNoMatchSizeImageGroupList="getNoMatchSizeImageGroupList";
        }

        interface CHANNEL_IMAGE_TEMPLATE {
            String ROOT = "/cms/channel/image_template";
            String Init = "init";
            String GetPage = "getPage";
            String GetCount = "getCount";
            String Save = "save";
            String Delete = "delete";
            String Get = "get";
            String GetDownloadUrl = "getDownloadUrl";
            String GetTemplateParameter = "getTemplateParameter";
        }

        interface CHANNEL_IMAGE_GROUP_DETAIL {
            String ROOT = "/cms/channel/image_group_detail";
            String INIT_CHANNEL_IMAGE_GROUP_DETAIL = "init";
            String SAVE_CHANNEL_IMAGE_GROUP_DETAIL = "save";
            String SEARCH_CHANNEL_IMAGE_GROUP_DETAIL = "search";
            String SAVE_UPLOAD_IMAGE_CHANNEL_IMAGE_GROUP_DETAIL = "saveUploadImage";
            String SAVE_IMAGE_CHANNEL_IMAGE_GROUP_DETAIL = "saveImage";
            String DELETE_CHANNEL_IMAGE_GROUP_DETAIL = "delete";
            String REFRESH_CHANNEL_IMAGE_GROUP_DETAIL = "refresh";
            String MOVE_CHANNEL_IMAGE_GROUP_DETAIL = "move";
        }

        interface LISTING {
            interface SIZE_CHART {
                String ROOT = "/cms/channel/sizeChartList";
                String INIT_SIZE_CHART = "sizeChartInit";
                String DELETE_SIZE_CHART = "sizeChartDelete";
                String SEARCH_SIZE_CHART = "sizeChartSearch";
                String SAVE_EDIT_SIZE_CHART = "sizeChartEditSave";
                String GetNoMatchList="getNoMatchList";
                String GetListImageGroupBySizeChartId="getListImageGroupBySizeChartId";
                String GetProductSizeChartList="getProductSizeChartList";
            }

            interface SIZE_CHART_DETAIL {
                String ROOT = "/cms/channel/sizeChartDetail";
                String SEARCH_DETAIL_SIZE_CHART = "sizeChartDetailSearch";
                String SAVE_DETAIL_SIZE_CHART = "sizeChartDetailSave";
                String SAVE_DETAIL_SIZE_MAP_CHART = "sizeChartDetailSizeMapSave";
            }
        }

        interface SELLER_CAT {

            String ROOT = "/cms/channel/category";

            String GET_SELLER_CAT = "getSellerCat";

            String ADD_SELLER_CAT = "addSellerCat";

            String REMOVE_SELLER_CAT = "removeSellerCat";

            String UPDATE_SELLER_CAT = "updateSellerCat";

            String GET_SELLER_CAT_CONFIG = "getSellerCatConfig";

            String SORTABLE_CART =  "sortableCat";
        }

        interface BLACK_BRAND {
            String ROOT = "/cms/channel/black_brand";

            String SEARCH_BLACK_BRAND = "searchBlackBrand";

            String UPDATE_BLACK_BRAND = "updateBlackBrand";
        }

        interface CONFIG {
            String ROOT = "/cms/channel/config";
            String INIT = "init";
            String LOAD_BY_CHANNEL = "loadByChannel";
            String ADD_CHANNEL_CONFIG = "addChannelConfig";
            String LOAD_CHANNEL_CONFIG_DETAIL = "loadChannelConfigDetail";
            String EDIT_CHANNEL_CONFIG = "editChannelConfig";
            String DEL_CHANNEL_CONFIG = "delChannelConfig";
        }
        interface MtChannelConfig {
            String ROOT = "/cms/mt/channel/config";
            String Search = "search";
            String saveList="saveList";
        }
        interface FEED_CONFIG {
            String ROOT = "/cms/channel/feedConfig";
            String SEARCH = "search";
            String SAVE ="save";
            String EXPORT ="export";
            String IMPORT ="import";
            String DELETE = "delete";
            String SAVE_FEED ="saveFeed";
            String CREATE_FEED ="createFeed";
        }
        interface CUSTOM {
            String ROOT = "/cms/channel/custProp";
            String SEARCH = "search";
            String SET_CUSTOMSH_IS_DISPPLAY ="doSetCustomshIsDispPlay";
            String UPDATE_ENTITY ="doUpdateEntity";
            String SET_SORT ="doSetSort";
            String DELETE = "delete";
            String INIT ="init";
            String CREATE_FEED ="createFeed";
        }
    }

    interface ImageCreate {
        String ROOT = "/cms/imagecreate/index";
        String Upload = "upload";
        String GetPageByWhere = "getPageByWhere";
        String GetCountByWhere = "getCountByWhere";
        String DownloadExcel = "downloadExcel";
        String DownloadImportErrorExcel = "downloadImportErrorExcel";
    }

    interface TOOLS {
        // hsCode编辑
        interface PRODUCT {
            String ROOT = "/cms/tools/product/";

            String INIT_HS_CODE_INFO = "initHsCodeInfo";

            String SEARCH_HS_CODE_INFO = "searchHsCodeInfo";

            String GET_HS_CODE_INFO = "getHsCodeInfo";

            String SAVE_HS_CODE_INFO = "saveHsCodeInfo";

            String CANCEL_HS_CODE_INFO = "cancelHsCodeInfo";
        }
        interface COMMON {
            String ROOT = "/cms/tools/common/";
            String SEARCH_MASTER_BRAND_INFO = "getMasterBrandInfo";
        }
    }

    interface CMS_TOOLS_REPRICE {
        String ROOT = "/cms/tools/reprice/";
        String GET_CHANNEL_LIST = "getChannelList";
        String GET_PLATFORM_LIST = "getPlatformList";
        String GET_CART_LIST = "getCartList";
        String GET_PLATFORM_CATEGORY_LIST = "getPlatformCategoryList";
        String SET_UPDATE_FLG = "setUpdateFlg";
    }

    interface PLATFORM_MAPPING {
        String ROOT = "/cms/platform/mapping/";
        String PAGE = "page";
        String GET = "get";
        String SAVE = "save";
        String DELETE = "delete";
        String GET_COMMONSCHEMA = "getCommonSchema";
        String GET_FEEDCUSTOMPROPS = "getFeedCustomProps";
    }

    interface MAINTAIN_SETTING {
        interface COMMON {
            interface BRAND_ADMINISTRATION{
                String ROOT="/cms/maintain/common/";
                String MASTER_BRAND_APPLICATION_INIT="initMasterBrandInfo";
                String MASTER_BRAND_APPLICATION_SEARCH="getMasterBrandInfo";
                String MASTER_BRAND_APPLICATION_REVIEWED="reviewedMasterBrandInfo";
                String MASTER_BRAND_APPLICATION_EDIT="editMasterBrandInfo";
                String MASTER_BRAND_APPLICATION_PLATFORM_SEARCH="mappingMasterBrandInfoToPlatform";
            }
        }
    }

    interface SHELVES {
        interface TEMPLATE {
            String ROOT = "/cms/shelves/template";
            String INIT = "init";
            String ADD = "add";
            String EDIT = "edit";
            String DELETE = "delete";
            String SEARCH = "search";
            String DETAIL = "detail";
        }
        interface DETAIL {
            String ROOT = "/cms/shelves/detail";
            String SEARCH = "search";
            String CREATE_SHELVES= "createShelves";
            String UPDATE_SHELVES= "updateShelves";
            String UPDATE_PRODUCT_SORT = "updateProductSort";
            String ADD_PRODUCT = "addProduct";
            String EXPORT_APP_IMAGE ="exportAppImage";
            String GET_SHELVES_INFO = "getShelvesInfo";
            String GET_SHELVES_HTML = "getShelvesHtml";
        }
    }
    //bi_report
    interface BIREPORT {
        interface LIST {
            interface DOWNLOAD {
                String ROOT = "/cms/biReport/download";
                String INIT = "init";
                String BIREPDOWNLOAD = "biRepDownload";
                String DOWNLOADTASKLIST = "getDownloadTaskList";
                String CREATEXLSFILETASK = "createXlsFileTask";
                String GETCOUNT = "getCount";
                String GETPAGE = "getPage";
                String GET_CHANNEL_LIST = "get_channel_list";
                String DELETETASK = "deleteTask";
            }
        }
    }
}

