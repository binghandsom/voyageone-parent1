package com.voyageone.components.jumei.Bean;

/**
 * 货币信息结构。
 *
 * Created on 2015-12-14
 *
 * @author chuanyu.liang
 * @version 2.0.0
 * @since 2.0.0
 */
public class JmCurrencyBean extends JmBaseBean {

    //货币符号.
    private String area_currency_symbol;

    //地区编码.
    private Integer area_code;

    //外汇名称.
    private String area_currency_name;

    public String getArea_currency_symbol() {
        return area_currency_symbol;
    }

    public void setArea_currency_symbol(String area_currency_symbol) {
        this.area_currency_symbol = area_currency_symbol;
    }

    public Integer getArea_code() {
        return area_code;
    }

    public void setArea_code(Integer area_code) {
        this.area_code = area_code;
    }

    public String getArea_currency_name() {
        return area_currency_name;
    }

    public void setArea_currency_name(String area_currency_name) {
        this.area_currency_name = area_currency_name;
    }
}
