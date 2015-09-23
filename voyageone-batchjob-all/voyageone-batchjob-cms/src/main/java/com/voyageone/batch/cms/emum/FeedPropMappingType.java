package com.voyageone.batch.cms.emum;

/**
 * 主数据属性的值映射类型
 * <p>
 * Created by Jonas on 9/2/15.
 */
public enum FeedPropMappingType {

    /**
     * 绑定到第三方品牌的属性上
     */
    FEED(1),

    /**
     * 将主数据的可选值保存到属性上
     */
    OPTIONS(3),

    /**
     * 绑定到 CMS 的属性
     */
    CMS(4),

    /**
     * 将输入的值保存
     */
    VALUE(5);

    private int value;

    public int value() {
        return value;
    }

    FeedPropMappingType(int value) {
        this.value = value;
    }

    public static FeedPropMappingType valueOf(int value) {
        switch (value) {
            case 1:
                return FEED;
            case 3:
                return OPTIONS;
            case 4:
                return CMS;
            case 5:
                return VALUE;
        }
        return null;
    }
}
