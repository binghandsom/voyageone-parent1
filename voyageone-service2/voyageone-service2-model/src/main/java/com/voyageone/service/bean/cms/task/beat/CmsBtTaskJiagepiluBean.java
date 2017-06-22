package com.voyageone.service.bean.cms.task.beat;

import com.voyageone.service.model.cms.CmsBtTaskJiagepiluModel;
import com.voyageone.service.model.cms.enums.jiagepilu.BeatFlag;
import com.voyageone.service.model.cms.enums.jiagepilu.ImageStatus;

/**
 * 价格披露Model扩展Bean
 *
 * @Author rex.wu
 * @Create 2017-06-22 16:13
 */
public class CmsBtTaskJiagepiluBean extends CmsBtTaskJiagepiluModel {

    private BeatFlag SynFlagEnum;

    private ImageStatus ImageStatusEnum;

    public BeatFlag getSynFlagEnum() {
        return BeatFlag.valueOf(synFlag);
    }

    public void setSynFlagEnum(BeatFlag synFlagEnum) {
        SynFlagEnum = synFlagEnum;
    }

    public ImageStatus getImageStatusEnum() {
        return imageStatus == null ? ImageStatus.None : ImageStatus.valueOf(imageStatus);
    }

    public void setImageStatusEnum(ImageStatus imageStatusEnum) {
        ImageStatusEnum = imageStatusEnum;
    }
}
