package com.voyageone.web2.cms.bean.setting.mapping.feed;

import com.voyageone.service.model.cms.enums.MappingPropType;
import com.voyageone.common.masterdate.schema.field.Field;

/**
 * FeedMapping 属性匹配画面特供数据模型,将画面需要的数据事先找好
 *
 * @author Jonas, 1/5/16.
 * @version 2.0.0
 * @since 2.0.0
 */
public class FieldBean {

    private MappingPropType type;

    private int seq;

    private Field field;

    private int level;

    private String parentId;

    public MappingPropType getType() {
        return type;
    }

    public void setType(MappingPropType type) {
        this.type = type;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}
