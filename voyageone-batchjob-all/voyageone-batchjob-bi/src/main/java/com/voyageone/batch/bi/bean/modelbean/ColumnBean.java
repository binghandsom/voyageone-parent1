package com.voyageone.batch.bi.bean.modelbean;

/**
 * Created by Kylin on 2015/6/5.
 */
public class ColumnBean {

    //平台ID
    private int ecomm_id;
    //商品Code
    private String product_code;
    //平台取得项目名
    private String column_web_name;
    //表项目名分类
    private String column_web_type;
    //表名
    private String cor_table_name;
    //对应表项目名
    private String cor_column_table_name;

    public int getEcomm_id() {
        return ecomm_id;
    }

    public void setEcomm_id(int ecomm_id) {
        this.ecomm_id = ecomm_id;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getColumn_web_name() {
        return column_web_name;
    }

    public void setColumn_web_name(String column_web_name) {
        this.column_web_name = column_web_name;
    }

    public String getColumn_web_type() {
        return column_web_type;
    }

    public void setColumn_web_type(String column_web_type) {
        this.column_web_type = column_web_type;
    }

    public String getCor_table_name() {
        return cor_table_name;
    }

    public void setCor_table_name(String cor_table_name) {
        this.cor_table_name = cor_table_name;
    }

    public String getCor_column_table_name() {
        return cor_column_table_name;
    }

    public void setCor_column_table_name(String cor_column_table_name) {
        this.cor_column_table_name = cor_column_table_name;
    }
}
