package com.voyageone.cms.service.model;

/**
 * {@link CmsFeedCategoryModel} 的类目关联关系,
 * @author Jonas, 12/11/15
 * @version 2.0.0
 * @since 2.0.0
 */
public class CmsFeedMappingModel {

    private String mainCategoryIdPath;

    private int defaultMapping;

    private int defaultMain;

    private int matchOver;

    public String getMainCategoryIdPath() {
        return mainCategoryIdPath;
    }

    public void setMainCategoryIdPath(String mainCategoryIdPath) {
        this.mainCategoryIdPath = mainCategoryIdPath;
    }

    public int getDefaultMapping() {
        return defaultMapping;
    }

    public void setDefaultMapping(int defaultMapping) {
        this.defaultMapping = defaultMapping;
    }

    public int getDefaultMain() {
        return defaultMain;
    }

    public void setDefaultMain(int defaultMain) {
        this.defaultMain = defaultMain;
    }

    public int getMatchOver() {
        return matchOver;
    }

    public void setMatchOver(int matchOver) {
        this.matchOver = matchOver;
    }
}
