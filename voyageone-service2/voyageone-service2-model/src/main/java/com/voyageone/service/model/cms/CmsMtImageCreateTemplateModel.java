package com.voyageone.service.model.cms;

import com.voyageone.base.dao.mysql.BaseModel;

public class CmsMtImageCreateTemplateModel extends BaseModel {

    /**
     * 渠道id
     */
    private String channelId;
    /**
     * 模板名字
     */
    private String name;
    /**
     * 模板内容
     */
    private String content;


    /**
     * 渠道id
     */
    public String getChannelId() {

        return this.channelId;
    }

    public void setChannelId(String channelId) {
        if (channelId != null) {
            this.channelId = channelId;
        } else {
            this.channelId = "";
        }

    }


    /**
     * 模板名字
     */
    public String getName() {

        return this.name;
    }

    public void setName(String name) {
        if (name != null) {
            this.name = name;
        } else {
            this.name = "";
        }

    }


    /**
     * 模板内容
     */
    public String getContent() {

        return this.content;
    }

    public void setContent(String content) {
        if (content != null) {
            this.content = content;
        } else {
            this.content = "";
        }

    }


}