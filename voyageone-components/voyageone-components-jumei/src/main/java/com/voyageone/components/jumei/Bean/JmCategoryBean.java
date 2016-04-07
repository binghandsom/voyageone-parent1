package com.voyageone.components.jumei.Bean;

/**
 * 分类结构。
 *
 * Created on 2015-12-14
 *
 * @author chuanyu.liang
 * @version 2.0.0
 * @since 2.0.0
 */
public class JmCategoryBean extends JmBaseBean {

    private Integer category_id;

    private String name;

    private String level;

    private Integer parent_category_id;

    public Integer getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Integer category_id) {
        this.category_id = category_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Integer getParent_category_id() {
        return parent_category_id;
    }

    public void setParent_category_id(Integer parent_category_id) {
        this.parent_category_id = parent_category_id;
    }
}
