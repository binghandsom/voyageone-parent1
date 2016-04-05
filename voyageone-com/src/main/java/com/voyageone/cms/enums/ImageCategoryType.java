package com.voyageone.cms.enums;

/**
 * Created by jonasvlag on 16/3/7.
 *
 * @version 2.0.0
 * @since 2.0.0
 */
public enum ImageCategoryType {
    Default(0),

    Main(1),

    Beat(2);

    private int val;

    public int getVal() {
        return val;
    }

    ImageCategoryType(int val) {
        this.val = val;
    }

    public ImageCategoryType valueOf(int val) {
        switch (val) {
            case 0:
                return Default;
            case 1:
                return Main;
            case 2:
                return Beat;
            default:
                return null;
        }
    }
}
