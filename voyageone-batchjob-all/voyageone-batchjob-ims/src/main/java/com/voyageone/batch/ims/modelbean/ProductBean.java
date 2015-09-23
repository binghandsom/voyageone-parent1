package com.voyageone.batch.ims.modelbean;

/**
 * Created by Leo on 2015/5/27.
 */
public class ProductBean {
    private String channel_id;
    private int cart_id;
    private String code;
    private String num_iid;
    private String product_code;
    private int main_product_flg;
    private int product_type;

    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }

    public int getCart_id() {
        return cart_id;
    }

    public void setCart_id(int cart_id) {
        this.cart_id = cart_id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNum_iid() {
        return num_iid;
    }

    public void setNum_iid(String num_iid) {
        this.num_iid = num_iid;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public int getMain_product_flg() {
        return main_product_flg;
    }

    public void setMain_product_flg(int main_product_flg) {
        this.main_product_flg = main_product_flg;
    }

    public int getProduct_type() {
        return product_type;
    }

    public void setProduct_type(int product_type) {
        this.product_type = product_type;
    }
}
