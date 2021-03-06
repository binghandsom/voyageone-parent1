package com.voyageone.task2.cms.bean;

import com.voyageone.base.dao.mongodb.model.ChannelPartitionModel;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by dell on 2015/11/27.
 */
@Document
public class InventoryForCmsBean extends ChannelPartitionModel {
    String code;
    String group_id;
    int qty;

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
}
