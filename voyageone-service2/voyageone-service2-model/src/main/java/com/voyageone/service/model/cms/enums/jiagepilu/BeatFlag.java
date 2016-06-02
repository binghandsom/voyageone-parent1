package com.voyageone.service.model.cms.enums.jiagepilu;

/**
 * 价格披露 2 的状态码
 * Created by jonasvlag on 16/3/1.
 *
 * @version 2.0.0
 * @since 2.0.0
 */
public enum BeatFlag {

    STOP(10),

    BEATING(20),

    SUCCESS(30),

    FAIL(40),

    REVERT(50),

    RE_SUCCESS(60),

    RE_FAIL(70),

    CANT_BEAT(-1);

    private final int flag;

    BeatFlag(int flag) {
        this.flag = flag;
    }

    public int getFlag() {
        return flag;
    }

    public static BeatFlag valueOf(int flag) {
        for (BeatFlag f : values())
            if (f.getFlag() == flag) return f;
        return null;
    }
}
