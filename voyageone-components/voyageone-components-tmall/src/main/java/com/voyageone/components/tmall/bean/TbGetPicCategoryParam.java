package com.voyageone.components.tmall.bean;

/**
 * 图片分类的查询参数
 *
 * Created by Jonas on 8/10/15.
 */
public class TbGetPicCategoryParam {

    private Long parentId;

    private Long pictureCategoryId;

    private String pictureCategoryName;

    private String type;

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getPictureCategoryId() {
        return pictureCategoryId;
    }

    public void setPictureCategoryId(Long pictureCategoryId) {
        this.pictureCategoryId = pictureCategoryId;
    }

    public String getPictureCategoryName() {
        return pictureCategoryName;
    }

    public void setPictureCategoryName(String pictureCategoryName) {
        this.pictureCategoryName = pictureCategoryName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
