package com.voyageone.wms.formbean;

/**
 * @author jack
 * FormClientShipmentBean
 */
public class FormClientShipmentBean {

    private String file_name;
    private String ucc128_carton_no;
    private String total_carton_quantity;
    private String article_number;
    private String upc;
    private String sku;
    private String calc_qty;
    private String total_qty;

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getUcc128_carton_no() {
        return ucc128_carton_no;
    }

    public void setUcc128_carton_no(String ucc128_carton_no) {
        this.ucc128_carton_no = ucc128_carton_no;
    }

    public String getTotal_carton_quantity() {
        return total_carton_quantity;
    }

    public void setTotal_carton_quantity(String total_carton_quantity) {
        this.total_carton_quantity = total_carton_quantity;
    }

    public String getArticle_number() {
        return article_number;
    }

    public void setArticle_number(String article_number) {
        this.article_number = article_number;
    }

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getCalc_qty() {
        return calc_qty;
    }

    public void setCalc_qty(String calc_qty) {
        this.calc_qty = calc_qty;
    }

    public String getTotal_qty() {
        return total_qty;
    }

    public void setTotal_qty(String total_qty) {
        this.total_qty = total_qty;
    }
}
