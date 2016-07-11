package com.voyageone.components.jumei.reponse;

import java.io.Serializable;

/**
 * HtProductAddResponse_Spu
 *
 * @author Ethan Shi,2016/6/12.
 * @version 2.0.0
 * @since 2.0.0
 */
public class HtProductAddResponse_Spu implements Serializable {
    private String partner_spu_no;
    private String jumei_spu_no;
    private String partner_sku_no;
    private String jumei_sku_no;

    public String getPartner_spu_no() {
        return partner_spu_no;
    }

    public void setPartner_spu_no(String partner_spu_no) {
        this.partner_spu_no = partner_spu_no;
    }

    public String getJumei_spu_no() {
        return jumei_spu_no;
    }

    public void setJumei_spu_no(String jumei_spu_no) {
        this.jumei_spu_no = jumei_spu_no;
    }

    public String getPartner_sku_no() {
        return partner_sku_no;
    }

    public void setPartner_sku_no(String partner_sku_no) {
        this.partner_sku_no = partner_sku_no;
    }

    public String getJumei_sku_no() {
        return jumei_sku_no;
    }

    public void setJumei_sku_no(String jumei_sku_no) {
        this.jumei_sku_no = jumei_sku_no;
    }
}
