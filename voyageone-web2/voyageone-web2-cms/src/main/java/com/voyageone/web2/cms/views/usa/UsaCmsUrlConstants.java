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
    }

    /**
     * USA CMS 高级检索
     */
    public interface  PRODUCT {
        String ROOT = "/cms/usa/product";

        String GET_PRODUCT_INFO = "getProductInfo";

    }
}
