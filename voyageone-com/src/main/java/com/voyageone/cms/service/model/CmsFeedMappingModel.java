package com.voyageone.cms.service.model;

import com.voyageone.base.dao.mongodb.model.BaseMongoMap;

/**
 * {@link CmsFeedCategoryModel} 的类目关联关系,
 * @author Jonas, 12/11/15
 * @version 2.0.0
 * @since 2.0.0
 */
public class CmsFeedMappingModel extends BaseMongoMap<String, Object> {

//    private String mainCategoryPath;
//
//    private int defaultMapping;
//
//    private int defaultMain;
//
//    private int matchOver;

    public String getMainCategoryPath() {
        return getAttribute("mainCategoryPath");
    }

    public void setMainCategoryPath(String mainCategoryPath) {
        setAttribute("mainCategoryPath", mainCategoryPath);
    }

    public int getDefaultMapping() {
        return getAttribute("defaultMapping");
    }

    public void setDefaultMapping(int defaultMapping) {
        setAttribute("defaultMapping", defaultMapping);
    }

    public int getDefaultMain() {
        return getAttribute("defaultMain");
    }

    public void setDefaultMain(int defaultMain) {
        setAttribute("defaultMain", defaultMain);
    }

    public int getMatchOver() {
        return getAttribute("matchOver");
    }

    public void setMatchOver(int matchOver) {
        setAttribute("matchOver", matchOver);
    }
}
