package com.voyageone.cms.service.model.feed.mapping;

import com.voyageone.cms.service.model.CmsBtFeedMappingModel;

import java.util.List;

/**
 * feed mapping 中,表示 mapping 中对属性的所有关联关系. 参考 {@link CmsBtFeedMappingModel}
 *
 * @author Jonas, 12/28/15.
 * @version 2.0.0
 * @since 2.0.0
 */
public class Prop {

    private String prop;

    private List<Mapping> mappings;

    private List<Prop> children;

    public String getProp() {
        return prop;
    }

    public void setProp(String prop) {
        this.prop = prop;
    }

    public List<Mapping> getMappings() {
        return mappings;
    }

    public void setMappings(List<Mapping> mappings) {
        this.mappings = mappings;
    }

    public List<Prop> getChildren() {
        return children;
    }

    public void setChildren(List<Prop> children) {
        this.children = children;
    }
}