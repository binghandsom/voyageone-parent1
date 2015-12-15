package com.voyageone.cms.service.model;

import com.voyageone.base.dao.mongodb.model.BaseMongoMap;

/**
 * {@link CmsFeedCategoryModel} 的类目关联关系,
 * @author Jonas, 12/11/15
 * @version 2.0.0
 * @since 2.0.0
 */
public class CmsFeedMappingModel extends BaseMongoMap<String, Object> {

    private final static String MAIN_CATEGORY_ID_PATH = "mainCategoryIdPath";

    private final static String DEFAULT_MAPPING = "defaultMapping";

    private final static String DEFAULT_MAIN = "defaultMain";

    private final static String MATCH_OVER = "matchOver";

    public String getMainCategoryIdPath() {
        return getAttribute(MAIN_CATEGORY_ID_PATH);
    }

    public void setMainCategoryIdPath(String mainCategoryIdPath) {
        setAttribute(MAIN_CATEGORY_ID_PATH, mainCategoryIdPath);
    }

    public int getDefaultMapping() {
        return getAttribute(DEFAULT_MAPPING);
    }

    public void setDefaultMapping(int defaultMapping) {
        setAttribute(DEFAULT_MAPPING, defaultMapping);
    }

    public int getDefaultMain() {
        return getAttribute(DEFAULT_MAIN);
    }

    public void setDefaultMain(int defaultMain) {
        setAttribute(DEFAULT_MAIN, defaultMain);
    }

    public int getMatchOver() {
        return getAttribute(MATCH_OVER);
    }

    public void setMatchOver(int matchOver) {
        setAttribute(MATCH_OVER, matchOver);
    }
}
