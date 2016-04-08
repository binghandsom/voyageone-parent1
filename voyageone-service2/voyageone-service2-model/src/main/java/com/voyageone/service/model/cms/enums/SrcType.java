package com.voyageone.service.model.cms.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * 表示 feed mapping 时,其关联值的类型. 参考 {@link com.voyageone.cms.service.model.feed.mapping.Mapping}
 *
 * @author Jonas, 12/28/15.
 * @version 2.0.0
 * @since 2.0.0
 */
public enum SrcType {
    text,
    propFeed,
    propMain,
    optionMain,
    optionPlatform,
    dict;

    @JsonCreator
    public static SrcType fromJson(String srcType) {
        return SrcType.valueOf(srcType);
    }
}
