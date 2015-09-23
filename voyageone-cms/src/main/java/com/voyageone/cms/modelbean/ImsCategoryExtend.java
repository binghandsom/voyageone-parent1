package com.voyageone.cms.modelbean;

/**
 * 对表 voyageone_ims.ims_bt_category_extend 的完整映射
 *
 * Created by Jonas on 9/18/15.
 */
public class ImsCategoryExtend {
    private int id;

    private String channel_id;

    private int main_category_id;

    private int is_set_attr;

    private String created;

    private String creater;

    private String modified;

    private String modifier;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }

    public int getMain_category_id() {
        return main_category_id;
    }

    public void setMain_category_id(int main_category_id) {
        this.main_category_id = main_category_id;
    }

    public int getIs_set_attr() {
        return is_set_attr;
    }

    public void setIs_set_attr(int is_set_attr) {
        this.is_set_attr = is_set_attr;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }
}
