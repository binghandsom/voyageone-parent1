package com.voyageone.web2.cms.model;

/**
 * 对表 voyageone_cms2.cms_mt_dict 的映射, 没有完整映射所有字段, 后续请自行追加
 *
 * @author Jonas, 1/19/16.
 * @version 2.0.0
 * @since 2.0.0
 */
public class CmsMtDictModel {

    private int id;

    private String order_channel_id;

    private String name;

    private String value;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrder_channel_id() {
        return order_channel_id;
    }

    public void setOrder_channel_id(String order_channel_id) {
        this.order_channel_id = order_channel_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
