package com.voyageone.web2.cms.model;

import com.voyageone.base.dao.mongodb.model.ChannelPartitionModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gubuchun 15/12/9
 * @version 2.0.0
 */
public class CmsMtCommonPropDefModel extends ChannelPartitionModel {

    private String id;
    private String name;
    private String type;
    private String publish;
    private List<CmsMtCommonPropDefModel_Option> options = new ArrayList<>();

    public CmsMtCommonPropDefModel() {
    }

    public CmsMtCommonPropDefModel(String channelId) {
        super(channelId);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPublish() {
        return publish;
    }

    public void setPublish(String publish) {
        this.publish = publish;
    }

    public List<CmsMtCommonPropDefModel_Option> getOptions() {
        return options;
    }

    public void setOptions(List<CmsMtCommonPropDefModel_Option> options) {
        this.options = options;
    }
}
