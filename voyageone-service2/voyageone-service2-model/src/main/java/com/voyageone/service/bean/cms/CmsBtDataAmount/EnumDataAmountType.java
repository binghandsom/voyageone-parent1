package com.voyageone.service.bean.cms.CmsBtDataAmount;

/**
 * Created by dell on 2016/7/5.
 */
public enum  EnumDataAmountType {
    FeedSum(1, "FeedSum"),
    MasterSum(2, "MasterSum"),
    PlatformPriceSum(3, "PlatformPriceSum"),
    PlatformInfoSum(4, "PlatformInfoSum"),

    //==============================================================================================
    //==================================美国CMS2枚举定义==============================================
    //==============================================================================================

    UsaFeedSum(5, "UsaFeedSum");

    EnumDataAmountType(int id, String name) {
        this.id = id;
        this.name = name;
    }
    private int id;
    private String name;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
