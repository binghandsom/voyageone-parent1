package com.voyageone.web2.cms;

/**
 * 定义所有 CMS WEB2 的地址定义, 同步前端 JS 的 actions.json
 * @author Jonas, 12/9/15
 * @version 2.0.0
 * @since 2.0.0
 */
public interface CmsUrlConstants {

    interface MENU {

        String ROOT = "/cms/home/menu/";

        String GET_CATE_INFO = "getCategoryInfo";

        String GET_CATE_TYPE = "getCategoryType";

        String SET_CATE_TYPE = "setCategoryType";
    }

    interface MAPPING {

        interface FEED {

            String ROOT = "/cms/setting/feedMapping";

            String GET_FEED_CATEGORY_TREE = "feedCategoryTree";

            String GET_MAIN_CATEGORIES = "mainCategories";
        }
    }
    interface PROMOTION {

        interface LIST {

            String ROOT = "/cms/promotion/list";

            String GET_PROMOTION_LIST = "getPromotionList";

            String INSERT_PROMOTION = "insertPromotion";

            String UPDATE_PROMOTION = "updatePromotion";
        }
    }

    interface PROP {

        interface CHANGE {

            String ROOT = "/cms/pop/prop_change/";

            String GET_POP_OPTIONS = "getPopOptions";

            String SET_PRODUCT_FIELDS = "setProductFields";
        }
    }

    interface PROM {

        interface SELECT {

            String ROOT = "/cms/pop/prom_select/";

            String GET_PROM_TAGS = "getPromotionTags";

            String SET_PROM_TAG = "setPromotionTag";
        }
    }
    interface SEARCH {

        interface INDEX {

            String ROOT = "/cms/search/index/";

            String INIT = "init";

            String SEARCH = "search";

            String GET_GROUP_LIST = "getGroupList";

            String GET_PRODUCT_LIST = "getProductList";
        }
    }
}
