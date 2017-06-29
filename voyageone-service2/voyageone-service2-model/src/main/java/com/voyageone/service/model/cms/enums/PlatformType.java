package com.voyageone.service.model.cms.enums;

/**
 * 语言种类
 *
 * @author Edward, 2016/02/04.
 * @version 2.0.0
 * @since 2.0.0
 */
public enum PlatformType {

    OFF_LINE(0),

    TMALL(1),

    JD(2),

    CN_OFFICIAL(3),

    NTES(8),

    JM(4);

    private Integer platformId;

    PlatformType(Integer platformId) {
        this.platformId = platformId;
    }

    public Integer getPlatformId() {
        return this.platformId;
    }

    public void setPlatformId(Integer platformId) {
        this.platformId = platformId;
    }
}
