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

                String GET_LIST_BY_WHERE = "getListByWhere";

                String INSERT = "insert";

                String UPDATE = "update";
                String GET = "get";
            }

            interface DETAIL {
                String ROOT = "/cms/jmpromotion/detail";

                String INIT = "init";

                String GET_LIST_BY_WHERE = "getListByWhere";
                String GET_PROMOTION_PRODUCT_INFO_LIST_BY_WHERE="getPromotionProductInfoListByWhere";
                String INSERT = "insert";

                String UPDATE = "update";
                String GET = "get";
            }
        }
    }

    // 活动管理
    interface PROMOTION {
        interface LIST {
            interface INDEX {

                String ROOT = "/cms/promotion/index";

                String INIT = "init";

                String GET_PROMOTION_LIST = "getPromotionList";

                String INSERT_PROMOTION = "insertPromotion";

                String UPDATE_PROMOTION = "updatePromotion";

                String PROMOTION_EXPORT = "exportPromotion";
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

                String INIT_NEW_RECORD = "initNewRecord";

                String GET_USABLE_STOCK = "getUsableStock";

                String SAVE_NEW_RECORD = "saveNewRecord";

                String IMPORT_SKU_INFO = "importSkuInfo";

                String IMPORT_STOCK_INFO = "importStockInfo";

                String EXPORT_STOCK_INFO = "exportStockInfo";

                String EXECUTE_STOCK_SEPARATION = "executeStockSeparation";

                String EXECUTE_STOCK_RESTORE = "executeStockRestore";

                String SAVE_RECORD = "saveRecord";

                String DEL_RECORD = "delRecord";

                String GET_SKU_SEPARATION_DETAIL = "getSkuSeparationDetail";

                String EXPORT_ERROR_INFO = "exportErrorInfo";
            }

            interface INCREMENT_STOCK_LIST {

                String ROOT = "/cms/promotion/task_increment_stock_list";

                String SEARCH_TASK = "searchTask";

                String SAVE_TASK = "saveTask";

                String DEL_TASK = "delTask";

                String GET_PLATFORM_LIST = "getPlatFormList";
            }

            interface INCREMENT_STOCK_DETAIL {

                String ROOT = "/cms/promotion/task_increment_stock_detail";

                String SEARCH_ITEM = "searchItem";

                String SAVE_ITEM = "saveItem";

                String DEL_ITEM = "delItem";

                String IMPORT_STOCK_INFO = "importStockInfo";

                String EXPORT_STOCK_INFO = "exportStockInfo";

                String EXECUTE_INCREMENT_STOCK_SEPARATION = "executeIncrementStockSeparation";
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

            String GET_DICT_LIST = "getDictList";

            String GET_PLATFORM_MAPPING = "getPlatformMapping";

            String GET_MAPPING_TYPE = "getMappingType";

            String SAVE_MAPPING = "saveMapping";

            String SAVE_MATCH_OVER_BY_MAIN_CATE = "saveMatchOverByMainCategory";
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
    }

    // 产品编辑
    interface PRODUCT{

        interface DETAIL {

            String ROOT = "/cms/product/detail";

            String GET_PRODUCT_INFO = "getProductInfo";

            String  UPDATE_PRODUCT_MASTER_INFO = "updateProductMasterInfo";

            String  UPDATE_PRODUCT_SKU_INFO = "updateProductSkuInfo";

            String UPDATE_PRODUCT_ALL_INFO = "updateProductAllInfo";

            String CHANGE_CATEGORY = "changeCategory";

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
    }

}
