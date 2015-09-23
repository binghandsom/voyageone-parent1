package com.voyageone.batch.ims.modelbean;

import com.voyageone.batch.ims.enums.TmallWorkloadStatus;

/**
 * Created by Leo on 2015/5/26.
 */
public class BrandMapBean {
    String order_channel_id;
    int cart_id;
    int task_id;
    TmallWorkloadStatus task_status;
    int model_id;
    String param;
    String product_ids;

    public String getOrder_channel_id() {
        return order_channel_id;
    }

    public void setOrder_channel_id(String order_channel_id) {
        this.order_channel_id = order_channel_id;
    }

    public int getCart_id() {
        return cart_id;
    }

    public void setCart_id(int cart_id) {
        this.cart_id = cart_id;
    }

    public int getTask_id() {
        return task_id;
    }

    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }

    public TmallWorkloadStatus getTask_status() {
        return task_status;
    }

    public void setTask_status(TmallWorkloadStatus task_status) {
        this.task_status = task_status;
    }

    public int getModel_id() {
        return model_id;
    }

    public void setModel_id(int model_id) {
        this.model_id = model_id;
    }

    public String getProduct_ids() {
        return product_ids;
    }

    public void setProduct_ids(String product_ids) {
        this.product_ids = product_ids;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }
}
