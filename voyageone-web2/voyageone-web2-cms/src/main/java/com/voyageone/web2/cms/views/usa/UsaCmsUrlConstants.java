package com.voyageone.web2.cms.views.usa;

/**
 * 美国CMS控制器Url常量定义
 *
 * @Author rex.wu
 * @Create 2017-07-05 17:11
 */
public class UsaCmsUrlConstants {

    public interface COMMON {
        String ROOT = "/cms/us/common";

        String GET_CHANNEL_CART = "getChannelCarts";

        String GET_FEED_INFO = "getFeedInfo";
    }

    /**
     * USA CMS Feed相关
     */
    public interface FEED {
        String ROOT = "/cms/us/feed";

        String DETAIL = "detail";

        String UPDATE = "update";

        String GET_TOP_MODEL = "getTopModel";

        String SET_PRICE = "setPrice";

        String APPROVE = "approve";

        String LIST = "list";

        String UPDATEONE = "updateOne";
    }

    /**
     * USA CMS 高级检索
     */
    public interface  ADVANCE_SEARCH {
        String ROOT = "/cms/us/advanceSearch";

        String INIT = "init";

        String GET_CUSTOM_COLUMNS = "getCustomColumns";

        String SAVE_CUSTOM_COLUMNS = "saveCustomColumns";

        String SEARCH = "search";

        String UPDATE_PRICE = "updatePrice";

        String LIST_OR_DELIST = "listOrDelist";


    }

    /**
     * USA CMS 高级检索
     */
    public interface  PRODUCT {
        String ROOT = "/cms/usa/product";

        String GET_PRODUCT_PLATFORM = "getProductPlatform";

        String GET_PRODUCT_INFO = "getProductInfo";

        String UPDATE_COMMON_PRODUCT_INFO = "updateCommonProductInfo";

        String GET_ALL_PLATFORMS_PRICE = "getAllPlatformsPrice";

        String UPDATE_ONE_PRICE = "updateOnePrice";

    }

    /**
     * Tag管理
     */
    public interface TAG {
        String ROOT = "/cms/us/tag";

        String INIT = "init";

    }
}
