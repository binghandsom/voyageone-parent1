package com.voyageone.batch.ims.enums;

/**
 * 对应 ims_bt_pic_category 的 type。表示目录的用途
 *
 * Created by Jonas on 8/20/15.
 */
public enum ImsPicCategoryType {
    Default(0),

    Main(1),

    Beat(2);

    private int val;

    public int getVal() {
        return val;
    }

    ImsPicCategoryType(int val) {
        this.val = val;
    }

    public ImsPicCategoryType valOf(int val) {
        switch (val) {
            case 0:
                return Default;
            case 1:
                return Main;
            case 2:
                return Beat;
        }
        return null;
    }
}
