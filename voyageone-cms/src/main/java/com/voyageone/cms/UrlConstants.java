package com.voyageone.cms;

public interface UrlConstants {
    String URL_CMS_EDIT_CATEGORY = "/cms/edit/category";
    String URL_CMS_EDIT_MODEL = "/cms/edit/model";
    String URL_CMS_EDIT_PRODUCT = "/cms/edit/product";
    String URL_CMS_EDIT_SETSIZECHART = "/cms/popup/setsizechart";
    String URL_CMS_COMMON = "/cms/common";
    String URL_CMS_SEARCH = "/cms/search";
    String URL_CMS_EDIT_PROMOTION = "/cms/edit/promotion";
    String URL_CMS_EDIT_SYSTEMSETTING = "/cms/edit/systemsetting";

    interface FeedPropMapping {

        String ROOT = "/cms/feedProp/mapping/";

        String GET_PATH = "getPath";

        String GET_PROPS = "getProps";

        String GET_MAPPINGS = "getMappings";

        String GET_CONST = "getConst";

        String GET_FEED_VALUES = "getFeedValues";

        String GET_PROP_OPTIONS = "getPropOptions";

        String SET_IGNORE = "setIgnore";

        String ADD_MAPPING = "addMapping";

        String SET_MAPPING = "setMapping";

        String DEL_MAPPING = "delMapping";

        String GET_DEFAULT = "getDefaultValue";
    }

    interface DictManage {

        String ROOT = "/cms/dict/manage/";

        String DT_GET_DICT = "dtGetDict";

        String GET_CONST = "getConst";

        String GET_CUSTOMS = "getCustoms";

        String SET_DICT = "setDict";

        String DEL_DICT = "delDict";

        String ADD_DICT = "addDict";

        String GET_DICT_LIST = "getDictList";
    }
}