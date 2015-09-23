package com.voyageone.ims.model;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 基于 item 扩展的 temp item。用于通过临时数据获取的完整信息
 *
 * Created by Jonas on 8/13/15.
 */
public class ImsBeatItemTemp extends ImsBeatItem {
    // 扩展
    private String src;

    // 扩展
    private List<String> comments;

    // 扩展
    private String description;

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getComments() {
        return StringUtils.join(comments, "; ");
    }

    public void setComments(String comment) {
        if (comments == null)
            comments = new ArrayList<>();

        comments.add(comment);
    }

    public boolean hasComments() {
        return comments != null && comments.size() > 0;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
