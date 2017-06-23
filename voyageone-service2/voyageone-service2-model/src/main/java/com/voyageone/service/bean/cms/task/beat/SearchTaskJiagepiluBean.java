package com.voyageone.service.bean.cms.task.beat;

import com.voyageone.service.model.cms.CmsBtTaskJiagepiluModel;
import com.voyageone.service.model.cms.enums.jiagepilu.BeatFlag;
import com.voyageone.service.model.cms.enums.jiagepilu.ImageStatus;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 检索价格披露任务商品条件Bean
 *
 * @Author rex.wu
 * @Create 2017-06-22 17:11
 */
public class SearchTaskJiagepiluBean extends CmsBtTaskJiagepiluModel {

    public List<String> numIidOrCodes;
    public String errorMsg;
    public List<String> beatFlags;
    public List<Integer> beatFlagVals;
    public List<String> imageStatuses;
    public List<Integer> imageStatusVals;

    private int size;
    private int curr;
    private int start;

    // 状态枚举处理
    public void parseEnum() {
        if (CollectionUtils.isNotEmpty(beatFlags)) {
            beatFlagVals = new ArrayList<>();
            for (String flag : beatFlags) {
                BeatFlag beatFlag = BeatFlag.valueOf(flag);
                if (beatFlag != null) {
                    beatFlagVals.add(Integer.valueOf(beatFlag.getFlag()));
                }
            }
            this.beatFlagVals = beatFlagVals;
        }
        if (CollectionUtils.isNotEmpty(imageStatuses)) {
            imageStatusVals = new ArrayList<>();
            for (String status : imageStatuses) {
                ImageStatus imageStatus = ImageStatus.valueOf(status);
                if (imageStatus != null) {
                    imageStatusVals.add(Integer.valueOf(imageStatus.getId()));
                }
            }
            this.imageStatusVals = imageStatusVals;
        }
    }

    // 分页参数处理
    public void handlePage() {
        if (this.curr <= 0) {
            this.curr = 1;
        }
        if (this.size <= 0) {
            this.size = 10;
        }
        this.start = (this.curr - 1) * size;
    }

    public List<String> getNumIidOrCodes() {
        return numIidOrCodes;
    }

    public void setNumIidOrCodes(List<String> numIidOrCodes) {
        this.numIidOrCodes = numIidOrCodes;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public List<String> getBeatFlags() {
        return beatFlags;
    }

    public void setBeatFlags(List<String> beatFlags) {
        this.beatFlags = beatFlags;
    }

    public List<Integer> getBeatFlagVals() {
        return beatFlagVals;
    }

    public void setBeatFlagVals(List<Integer> beatFlagVals) {
        this.beatFlagVals = beatFlagVals;
    }

    public List<String> getImageStatuses() {
        return imageStatuses;
    }

    public void setImageStatuses(List<String> imageStatuses) {
        this.imageStatuses = imageStatuses;
    }

    public List<Integer> getImageStatusVals() {
        return imageStatusVals;
    }

    public void setImageStatusVals(List<Integer> imageStatusVals) {
        this.imageStatusVals = imageStatusVals;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getCurr() {
        return curr;
    }

    public void setCurr(int curr) {
        this.curr = curr;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }
}
