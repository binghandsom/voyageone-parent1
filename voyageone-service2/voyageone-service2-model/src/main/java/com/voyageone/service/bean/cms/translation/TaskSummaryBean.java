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
    private long unassginedCount;

    /**
     * 已分配但未完成总数
     */
    private long incompleteCount;

    /**
     * 完成翻译总商品数.
     */
    private long completeCount;

    /**
     * 个人完成翻译商品数.
     */
    private long userCompleteCount;

    public long getUnassginedCount() {
        return unassginedCount;
    }

    public void setUnassginedCount(long unassginedCount) {
        this.unassginedCount = unassginedCount;
    }

    public long getIncompleteCount() {
        return incompleteCount;
    }

    public void setIncompleteCount(long incompleteCount) {
        this.incompleteCount = incompleteCount;
    }

    public long getCompleteCount() {
        return completeCount;
    }

    public void setCompleteCount(long completeCount) {
        this.completeCount = completeCount;
    }

    public long getUserCompleteCount() {
        return userCompleteCount;
    }

    public void setUserCompleteCount(long userCompleteCount) {
        this.userCompleteCount = userCompleteCount;
    }
}
