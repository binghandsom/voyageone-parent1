package com.voyageone.batch.ims.enums;

/**
 * ims_bt_cart_activity_beat_icon 的 beat_flg 字段，标记任务的阶段状态
 *
 * Created by Jonas on 7/23/15.
 */
public enum BeatFlg {

    /**
     * 最初的设置阶段
     */
    Startup(0),

    /**
     * 等待执行打标
     */
    Waiting(1),

    /**
     * 打标成功
     */
    Passed(2),

    /**
     * 打标失败
     */
    Fail(3),

    /**
     * 还原成功
     */
    Revert(4),

    /**
     * 还原失败
     */
    RevertFail(5),

    /**
     * 等待取消
     */
    Cancel(10),

    /**
     * 取消完成
     */
    Canceled(11),

    /**
     * 取消失败
     */
    CancelFail(12);

    private int value;

    public int value() {
        return value;
    }

    BeatFlg(int value) {
        this.value = value;
    }

    public static BeatFlg valueOf(int value) {
        switch (value) {
            case 0:
                return Startup;
            case 1:
                return Waiting;
            case 2:
                return Passed;
            case 3:
                return Fail;
            case 4:
                return Revert;
            case 5:
                return RevertFail;
            case 10:
                return Cancel;
            case 11:
                return Canceled;
            case 12:
                return CancelFail;
            default:
                return null;
        }
    }
}