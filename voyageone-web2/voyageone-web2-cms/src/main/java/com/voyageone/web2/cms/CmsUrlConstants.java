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

            String GET_FEED_CATEGORY_TREE = "getFeedCategories";

            String GET_MAIN_CATEGORIES = "getMainCategories";
        }
    }

    interface PROMOTION {

        interface LIST {

            String ROOT = "/cms/promotion/list";

            String GET_PROMOTION_LIST = "getPromotionList";

            String INSERT_PROMOTION = "insertPromotion";

            String UPDATE_PROMOTION = "updatePromotion";
        }
        interface DETAIL {

            String ROOT = "/cms/promotion/detail";

            String GET_PROMOTION_GROUP = "getPromotionGroup";

            String GETP_ROMOTION_CODE = "getPromotionCode";

            String GET_PROMOTION_SKU = "getPromotionSku";

            String GET_PROMOTION_UPLOAD = "uploadPromotion";
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
    interface POP {

        interface PROP_CHANGE {

            String ROOT = "/cms/pop/prop_change/";

            String GET_POP_OPTIONS = "getPopOptions";

            String SET_PRODUCT_FIELDS = "setProductFields";
        }

        interface PROM_SELECT {

            String ROOT = "/cms/pop/prom_select/";

            String GET_PROM_TAGS = "getPromotionTags";

            String ADD_TO_PROMOTION = "addToPromotion";
        }

        interface PRICE {

            String ROOT = "/cms/pop/price/";

            String GET_PRICE_HISTORY = "getPriceHistory";
        }

        interface PROMOTION {

            String ROOT = "/cms/pop/promotion/";

            String GET_PROMOTION_HISTORY = "getPromotionHistory";
        }
    }

    interface PRODUCT{

        interface EDIT{

            String ROOT = "/cms/product/edit/";

            String INIT = "doInit";

            String  SUBMIT = "doSubmit";

            String CHANGE_CATEGORY="doChagneCategory";

            String CONFIRM_CHANGE = "doConfirmChange";

            String RESET = "doReset";
        }
    }

}
