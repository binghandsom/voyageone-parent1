package com.voyageone.wms.modelbean.external;

/**
 * 外部内容：对应 cms_mt_product_type
 * Created by Tester on 5/28/2015.
 *
 * @author Jonas
 */
public class WmsProductTypeBean {
    private int product_type_id;
    private String channel_id;
    private String product_type_short_name;
    private String product_type_name;
    private String lang_id;

    public int getProduct_type_id() {
        return product_type_id;
    }

    public void setProduct_type_id(int product_type_id) {
        this.product_type_id = product_type_id;
    }

    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }

    public String getProduct_type_short_name() {
        return product_type_short_name;
    }

    public void setProduct_type_short_name(String product_type_short_name) {
        this.product_type_short_name = product_type_short_name;
    }

    public String getProduct_type_name() {
        return product_type_name;
    }

    public void setProduct_type_name(String product_type_name) {
        this.product_type_name = product_type_name;
    }

    public String getLang_id() {
        return lang_id;
    }

    public void setLang_id(String lang_id) {
        this.lang_id = lang_id;
    }

}
