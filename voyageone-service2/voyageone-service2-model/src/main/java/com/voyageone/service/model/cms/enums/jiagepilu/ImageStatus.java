package com.voyageone.service.model.cms.enums.jiagepilu;

/**
 * 价格披露时, 图片的状态
 * Created by jonasvlag on 16/5/23.
 *
 * @version 2.0.0
 * @since 2.0.0
 */
public enum ImageStatus {

    /**
     * 无图或未知, 该项为默认状态
     */
    None(0),

    /**
     * 已发取图请求, 任务等待中。
     * <p>注意: 该状态原为异步使用, 但现在 Job 使用同步接口取图, 所以不存在等待状态。暂不使用。</p>
     */
    Waiting(5),

    /**
     * 取图返回结果为错误
     */
    Error(10),

    /**
     * 取图成功, 图片已存在
     */
    Exists(15);

    private int id;

    ImageStatus(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static ImageStatus valueOf(int id) {
        for (ImageStatus imageStatus : values()) {
            if (imageStatus.getId() == id)
                return imageStatus;
        }
        return null;
    }
}
