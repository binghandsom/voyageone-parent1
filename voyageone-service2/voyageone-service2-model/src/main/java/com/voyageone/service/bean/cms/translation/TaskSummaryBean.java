package com.voyageone.service.bean.cms.translation;

/**
 * Created by Ethan Shi on 2016/6/28.
 *
 * @author Ethan Shi
 * @version 2.2.0
 * @since 2.2.0
 */
public class TaskSummaryBean {

    /**
     * 未分配总数
     */
    private int unassginedCount;

    /**
     * 已分配但未完成总数
     */
    private int imcompeleteCount;

    /**
     * 完成翻译总商品数.
     */
    private int compeleteCount;

    /**
     * 个人完成翻译商品数.
     */
    private int userCompeleteCount;

    public int getUnassginedCount() {
        return unassginedCount;
    }

    public void setUnassginedCount(int unassginedCount) {
        this.unassginedCount = unassginedCount;
    }

    public int getImcompeleteCount() {
        return imcompeleteCount;
    }

    public void setImcompeleteCount(int imcompeleteCount) {
        this.imcompeleteCount = imcompeleteCount;
    }

    public int getCompeleteCount() {
        return compeleteCount;
    }

    public void setCompeleteCount(int compeleteCount) {
        this.compeleteCount = compeleteCount;
    }

    public int getUserCompeleteCount() {
        return userCompeleteCount;
    }

    public void setUserCompeleteCount(int userCompeleteCount) {
        this.userCompeleteCount = userCompeleteCount;
    }
}
