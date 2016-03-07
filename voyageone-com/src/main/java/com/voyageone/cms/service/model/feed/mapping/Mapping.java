package com.voyageone.cms.service.model.feed.mapping;

import com.voyageone.cms.enums.SrcType;
import com.voyageone.cms.bean.Condition;

import java.util.List;

/**
 * feed mapping 中,具体表示属性与值之间关系. 参考 {@link Prop}
 *
 * @author Jonas, 12/28/15.
 * @version 2.0.0
 * @since 2.0.0
 */
public class Mapping {

    private List<Condition> condition;

    private SrcType type;

    private String val;

    public List<Condition> getCondition() {
        return condition;
    }

    public void setCondition(List<Condition> condition) {
        this.condition = condition;
    }

    public SrcType getType() {
        return type;
    }

    public void setType(SrcType type) {
        this.type = type;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }
}