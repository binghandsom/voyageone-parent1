package com.voyageone.web2.cms;

/**
 * 定义所有 CMS WEB2 的地址定义, 同步前端 JS 的 actions.json
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
        }

    }

    interface JMPROMOTION {
        interface LIST {
            interface INDEX {
                String ROOT = "/cms/jmpromotion/index";
                String INIT = "init";
                String GET_LIST_BY_WHERE = "selectListByWhere";
                String SaveModel = "saveModel";
                String DELETE = "delete";
                String GET = "get";
                String GetEditModel="getEditModel";
                String GetTagListByPromotionId="getTagListByPromotionId";
                String ImportJM="importJM";

            }

            interface DETAIL {
                String ROOT = "/cms/jmpromotion/detail";

                String INIT = "init";

                String GET_LIST_BY_WHERE = "selectListByWhere";
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
                String BatchUpdateDealPrice="batchUpdateDealPrice";
                String BatchSynchPrice="batchSynchPrice";
                String SynchAllPrice="synchAllPrice";
                String BatchCopyDeal="batchCopyDeal";
                String CopyDealAll="copyDealAll";
                String BatchDeleteProduct="batchDeleteProduct";
                String DeleteAllProduct="deleteAllProduct";
                String GetProductView="getProductView";
                String UpdateDealPrice="updateDealPrice";
            }
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
            }
        }
    }

    interface CMSMTMASTERINFO {
        interface LIST {
            interface INDEX {
                String ROOT = "/cms/cmsmtmasterinfo/index";
                String INIT = "init";
                String GET_LIST_BY_WHERE = "selectListByWhere";
                String INSERT = "insert";
                String UPDATE = "update";
                String GET = "get";
                String UPDATEJMIMG = "updateJMImg";
                String GetCountByWhere = "selectCountByWhere";
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
                String GETBYKEY = "selectByKey";

            }
        }
    }

    // 活动管理
    interface PROMOTION {
        interface LIST {
            interface INDEX {

                String ROOT = "/cms/promotion/index";
                String INIT = "init";
                String InitByPromotionId="initByPromotionId";
                String GET_PROMOTION_LIST = "getPromotionList";

                String INSERT_PROMOTION = "insertPromotion";

                String UPDATE_PROMOTION = "updatePromotion";

                String PROMOTION_EXPORT = "exportPromotion";

                String DEL_PROMOTION = "delPromotion";
            }

            interface DETAIL {

                String ROOT = "/cms/promotion/detail";

                String GET_PROMOTION_GROUP = "getPromotionGroup";

                String GET_PROMOTION_CODE = "getPromotionCode";

                String GET_PROMOTION_SKU = "getPromotionSku";

                String GET_PROMOTION_UPLOAD = "uploadPromotion";

                String TE_JIA_BAO_INIT = "teJiaBaoInit";

                String UPDATE_PROMOTION_PRODUCT = "updatePromotionProduct";

                String DEL_PROMOTION_MODEL = "delPromotionModel";

                String DEL_PROMOTION_CODE = "delPromotionCode";
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
            }

            interface FILE {

                String ROOT = "/cms/promotion/file";

//                String GET_CODE_FILE = "getCodeFile";
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

            String GET_GROUP_LIST = "getGroupList";

            String GET_PRODUCT_LIST = "getProductList";

            String EXPORT_PRODUCTS = "exportProducts";
        }

        interface FEED {
            String ROOT = "/cms/search/feed";
            String INIT = "init";
            String SEARCH = "search";
            String UPDATE = "updateFeedStatus";
        }
    }

    // 匹配关系设置
    interface MAPPING {

        interface FEED {

            String ROOT = "/cms/mapping/feed";

            String GET_TOP_CATEGORIES = "getTopCategories";

            String GET_FEED_CATEGORY_TREE = "getFeedCategoryTree";

            String GET_MAIN_CATEGORIES = "getMainCategories";

            String SET_MAPPING = "setFeedMapping";

            String EXTENDS_MAPPING = "extendsMapping";

            String GET_FIELD_MAPPING = "getFieldMapping";

            String GET_FEED_ATTRS = "getFeedAttributes";

            String SAVE_FIELD_MAPPING = "saveFieldMapping";

            String DIRECT_MATCH_OVER = "directMatchOver";

            String GET_MAIN_MAPPING = "getMainMapping";

            String GET_MAPPINGS = "getMappings";

            String GET_MAPPING_INFO = "getMappingInfo";
        }

        interface PLATFORM {

            String ROOT = "/cms/mapping/platform";

            String GET_MAIN_CATEGORY = "getMainDataFinalCategoryMap";

            String GET_OTHER_MAPPING_PATH = "getOtherMappingCategoryPath";

            String GET_PLATFORM_CATEGORIES = "getPlatformCategories";

            String GET_PLATFORM_CATEGORY = "getPlatformCategory";

            String SET_PLATFORM_MAPPING = "setPlatformMapping";

            String GET_MAIN_CATEGORY_SCHEMA = "getMainCategorySchema";

            String GET_PLATFORM_CATEGORY_SCHEMA = "getPlatformCategorySchema";

            String GET_DICT_LIST = "getDictList";

            String GET_PLATFORM_MAPPING = "getPlatformMapping";

            String GET_MAPPING_TYPE = "getMappingType";

            String SAVE_MAPPING = "saveMapping";

            String SAVE_MATCH_OVER_BY_MAIN_CATE = "saveMatchOverByMainCategory";

            String GET_CARTS = "getCarts";

            String GET_COMMON_SCHEMA = "getCommonSchema";
        }

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
    }

    // 系统设置
    interface SYSTEM {

        interface CATEGORY {

            String ROOT = "/cms/system/category";

            String GET_CATEGORY_LIST = "getCategoryList";

            String GET_CATEGORY_DETAIL = "getCategoryDetail";

            String UPDATE_CATEGORY_SCHEMA = "updateCategorySchema";
        }

        interface ERROR {

            String ROOT = "/cms/system/error";

            String INIT = "init";

            String SEARCH = "search";

            String UPDATE_FINISH_STATUS = "updateFinishStatus";
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
        }

        interface ADD_TO_PROMOTION {

            String ROOT = "/cms/pop/add_to_promotion/";

            String GET_PROM_TAGS = "getPromotionTags";
            String CHECK_PROM_TAGS = "checkPromotionTags";
            String ADD_TO_PROMOTION = "addToPromotion";
        }

        interface PRICE {

            String ROOT = "/cms/pop/history_price/";

            String GET_PRICE_HISTORY = "getPriceHistory";
        }

        interface PROMOTION {

            String ROOT = "/cms/pop/history_promotion/";

            String GET_PROMOTION_HISTORY = "getPromotionHistory";
        }

        interface IMAGE_SETTING {

            String ROOT = "/cms/pop/image_setting/";

            String UPLOAD_IMAGE = "uploadImage";
        }
        interface ADD_TO_CHANNEL_CATEGORY {
            String ROOT = "/cms/pop/add_to_channel_category/";
            String GET_CHANNEL_CATEGORY_INFO = "getChannelCategory";
            String SAVE_CHANNEL_CATEGORY_INFO = "saveChannelCategory";
        }
    }

    // 产品编辑
    interface PRODUCT{

        interface DETAIL {

            String ROOT = "/cms/product/detail";

            String GET_PRODUCT_INFO = "getProductInfo";

            String  UPDATE_PRODUCT_MASTER_INFO = "updateProductMasterInfo";

            String  UPDATE_PRODUCT_SKU_INFO = "updateProductSkuInfo";

            String UPDATE_PRODUCT_ALL_INFO = "updateProductAllInfo";

            String UPDATE_PRODUCT_FEED = "updateProductFeed";

            String CHANGE_CATEGORY = "changeCategory";

            String GET_PRODUCT_PLATFORM ="getProductPlatform";

            String UPDATE_PRODUCT_PLATFORM ="updateProductPlatform";

            String UPDATE_PRODUCT_PLATFORM_CHK ="updateProductPlatformChk";

            String CHANGE_PLATFORM_CATEGORY ="changePlatformCategory";

        }

    }

    // 商品编辑
    interface GROUP{

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

            String GET_TASKS = "getTasks";

            String SEARCH_HISTORY_TASKS = "searchHistoryTasks";

            String ASSIGN_TASKS = "assignTasks";

            String COPY_FORM_MAIN_PRODUCT = "copyFormMainProduct";

            String SAVE_TASK = "saveTask";

            String SUBMIT_TASK = "submitTask";

            String CANCEL_TASK = "cancelTask";

            String GET_FEED_ATTRIBUTES = "getFeedAttributes";
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

        interface CHANNEL_TAG {
            String ROOT = "/cms/channel/tag";
            String INIT_CHANNEL_TAG = "init";
            String SAVE_CHANNEL_TAG = "saveTag";
            String DEL_CHANNEL_TAG = "delTag";
            String GET_TAG_LIST = "getTagList";
        }
        interface CHANNEL_IMAGE_GROUP {
            String ROOT="/cms/channel/image_group";
            String INIT_CHANNEL_IMAGE_GROUP = "init";
            String SEARCH_CHANNEL_IMAGE_GROUP = "search";
            String SAVE_CHANNEL_IMAGE_GROUP = "save";
            String DELETE_CHANNEL_IMAGE_GROUP = "delete";
        }
        interface CHANNEL_IMAGE_TEMPLATE {
            String ROOT = "/cms/channel/image_template";
            String Init = "init";
            String GetPage = "selectPage";
            String GetCount = "getCount";
            String Save = "save";
            String Delete = "delete";
            String Get="get";
            String GetDownloadUrl="getDownloadUrl";
            String GetTemplateParameter="getTemplateParameter";
        }
        interface CHANNEL_IMAGE_GROUP_DETAIL {
            String ROOT="/cms/channel/image_group_detail";
            String INIT_CHANNEL_IMAGE_GROUP_DETAIL = "init";
            String SAVE_CHANNEL_IMAGE_GROUP_DETAIL = "save";
            String SEARCH_CHANNEL_IMAGE_GROUP_DETAIL = "search";
            String SAVE_UPLOAD_IMAGE_CHANNEL_IMAGE_GROUP_DETAIL = "saveUploadImage";
            String SAVE_IMAGE_CHANNEL_IMAGE_GROUP_DETAIL = "saveImage";
            String DELETE_CHANNEL_IMAGE_GROUP_DETAIL = "delete";
            String REFRESH_CHANNEL_IMAGE_GROUP_DETAIL = "refresh";
            String MOVE_CHANNEL_IMAGE_GROUP_DETAIL = "move";
        }
        interface LISTING{
            interface SIZE_CHART{
                String ROOT="/cms/channel/sizeChartList";
                String INIT_SIZE_CHART = "sizeChartInit";
                String DELETE_SIZE_CHART="sizeChartDelete";
                String SEARCH_SIZE_CHART = "sizeChartSearch";
                String SAVE_EDIT_SIZE_CHART="sizeChartEditSave";
            }
            interface SIZE_CHART_DETAIL{
                String ROOT="/cms/channel/sizeChartDetail";
                String SEARCH_DETAIL_SIZE_CHART="sizeChartDetailSearch";
                String SAVE_DETAIL_SIZE_CHART="sizeChartDetailSave";
                String SAVE_DETAIL_SIZE_MAP_CHART ="sizeChartDetailSizeMapSave";
            }
        }

        interface SELLER_CAT {

            String ROOT = "/cms/channel/category";

            String GET_SELLER_CAT = "getSellerCat";

            String ADD_SELLER_CAT = "addSellerCat";

            String REMOVE_SELLER_CAT = "removeSellerCat";

            String UPDATE_SELLER_CAT = "updateSellerCat";

            String GET_SELLER_CAT_CONFIG = "getSellerCatConfig";
        }

    }

    interface ImageCreate {
        String ROOT = "/cms/imagecreate/index";
        String Upload = "upload";
        String GetPageByWhere = "selectPageByWhere";
        String GetCountByWhere = "selectCountByWhere";
        String DownloadExcel = "downloadExcel";
        String DownloadImportErrorExcel = "downloadImportErrorExcel";
    }
    // hsCode编辑
    interface TOOLS{
        interface PRODUCT {

            String ROOT = "/cms/tools/product";

            String INIT_HS_CODE_INFO = "initHsCodeInfo";

            String SEARCH_HS_CODE_INFO = "searchHsCodeInfo";

            String GET_HS_CODE_INFO = "getHsCodeInfo";

            String SAVE_HS_CODE_INFO = "saveHsCodeInfo";

            String CANCEL_HS_CODE_INFO = "cancelHsCodeInfo";
        }
    }
}
