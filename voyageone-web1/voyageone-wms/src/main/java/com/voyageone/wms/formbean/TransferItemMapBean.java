package com.voyageone.wms.formbean;

import com.voyageone.wms.modelbean.TransferBean;
import com.voyageone.wms.modelbean.TransferItemBean;

/**
 * 包含映射关系的 TransferItem Bean
 * Created by Tester on 1/23/2016.
 * @author Jack
 */
public class TransferItemMapBean extends TransferItemBean {
    public String order_channel_id;

    public String getOrder_channel_id() {
        return order_channel_id;
    }

    public void setOrder_channel_id(String order_channel_id) {
        this.order_channel_id = order_channel_id;
    }
}
