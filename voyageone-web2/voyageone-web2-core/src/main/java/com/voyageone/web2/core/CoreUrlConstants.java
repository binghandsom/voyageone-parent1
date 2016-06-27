package com.voyageone.web2.core;

/**
 * @author Jonas 12/9/15
 * @version 2.0.0
 * @since 2.0.0
 */
public interface CoreUrlConstants {

    interface MENU {

        String ROOT = "/core/home/menu/";

        String GET_MENU_HEADER_INFO = "getMenuHeaderInfo";

        String SET_LANGUAGE = "setLanguage";

        String SET_CHANNEL = "setChannel";
    }

    interface USER {

        String ROOT = "/core/access/user/";

        String LOGIN = "login";

        String VENDOR_LOGIN = "vendorLogin";

        String GET_CHANNEL = "getChannel";

        String SELECT_CHANNEL = "selectChannel";

        String LOGOUT = "logout";
    }
}
