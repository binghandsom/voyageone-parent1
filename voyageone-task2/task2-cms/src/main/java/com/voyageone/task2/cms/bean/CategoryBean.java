package com.voyageone.task2.cms.bean;

import java.util.List;

/**
 * CategoryBean
 * Created by zero on 7/28/2015.
 *
 * @author zero
 */
public class CategoryBean {
    private String url_key;
    private String parent_url_key;
    private String c_name;
    private String c_header_title;
    private String c_is_enable_filter;
    private String c_is_visible_on_menu;
    private String c_is_published;
    private String c_is_effective;
    private String ce_seo_title;
    private String ce_seo_description;
    private String ce_seo_keywords;
    private String ce_seo_canonical;
    private String ce_main_category;
    private String ace_amazon_browse_category;
    private String gce_google_category;
    private String pce_price_grabber_category;
    private String cc_cn_name;
    private String cc_cn_header_title;
    private String cc_cn_is_enable_filter;
    private String cc_cn_is_visible_on_menu;
    private String cc_cn_is_publish;
    private String cce_cn_seo_title;
    private String cce_cn_seo_description;
    private String cce_cn_seo_keywords;
    private String cce_hs_code;
    private String cce_hs_code_pu;
    private String cce_size_chart;
    private String cce_main_category;
    private String ctce_tm_category;
    private String cjce_jd_category;
    private String ccps_base_price;
    private String ccps_pricing_factor;
    private String ccps_exchange_rate;
    private String ccps_over_heard_1;
    private String ccps_over_heard_2;
    private String ccps_over_heard_3;
    private String ccps_over_heard_4;
    private String ccps_over_heard_5;
    private String ccps_shipping_compensation;
    private List<ModelBean> modelbeans;

    public List<ModelBean> getModelbeans() {
        return modelbeans;
    }

    public void setModelbeans(List<ModelBean> modelbeans) {
        this.modelbeans = modelbeans;
    }

    public String getUrl_key() {
        return url_key;
    }

    public void setUrl_key(String url_key) {
        this.url_key = url_key;
    }

    public String getParent_url_key() {
        return parent_url_key;
    }

    public void setParent_url_key(String parent_url_key) {
        this.parent_url_key = parent_url_key;
    }

    public String getC_name() {
        return c_name;
    }

    public void setC_name(String c_name) {
        this.c_name = c_name;
    }

    public String getC_header_title() {
        return c_header_title;
    }

    public void setC_header_title(String c_header_title) {
        this.c_header_title = c_header_title;
    }

    public String getC_is_enable_filter() {
        return c_is_enable_filter;
    }

    public void setC_is_enable_filter(String c_is_enable_filter) {
        this.c_is_enable_filter = c_is_enable_filter;
    }

    public String getC_is_visible_on_menu() {
        return c_is_visible_on_menu;
    }

    public void setC_is_visible_on_menu(String c_is_visible_on_menu) {
        this.c_is_visible_on_menu = c_is_visible_on_menu;
    }

    public String getC_is_published() {
        return c_is_published;
    }

    public void setC_is_published(String c_is_published) {
        this.c_is_published = c_is_published;
    }

    public String getC_is_effective() {
        return c_is_effective;
    }

    public void setC_is_effective(String c_is_effective) {
        this.c_is_effective = c_is_effective;
    }

    public String getCe_seo_title() {
        return ce_seo_title;
    }

    public void setCe_seo_title(String ce_seo_title) {
        this.ce_seo_title = ce_seo_title;
    }

    public String getCe_seo_description() {
        return ce_seo_description;
    }

    public void setCe_seo_description(String ce_seo_description) {
        this.ce_seo_description = ce_seo_description;
    }

    public String getCe_seo_keywords() {
        return ce_seo_keywords;
    }

    public void setCe_seo_keywords(String ce_seo_keywords) {
        this.ce_seo_keywords = ce_seo_keywords;
    }

    public String getCe_seo_canonical() {
        return ce_seo_canonical;
    }

    public void setCe_seo_canonical(String ce_seo_canonical) {
        this.ce_seo_canonical = ce_seo_canonical;
    }

    public String getCe_main_category() {
        return ce_main_category;
    }

    public void setCe_main_category(String ce_main_category) {
        this.ce_main_category = ce_main_category;
    }

    public String getAce_amazon_browse_category() {
        return ace_amazon_browse_category;
    }

    public void setAce_amazon_browse_category(String ace_amazon_browse_category) {
        this.ace_amazon_browse_category = ace_amazon_browse_category;
    }

    public String getGce_google_category() {
        return gce_google_category;
    }

    public void setGce_google_category(String gce_google_category) {
        this.gce_google_category = gce_google_category;
    }

    public String getPce_price_grabber_category() {
        return pce_price_grabber_category;
    }

    public void setPce_price_grabber_category(String pce_price_grabber_category) {
        this.pce_price_grabber_category = pce_price_grabber_category;
    }

    public String getCc_cn_name() {
        return cc_cn_name;
    }

    public void setCc_cn_name(String cc_cn_name) {
        this.cc_cn_name = cc_cn_name;
    }

    public String getCc_cn_header_title() {
        return cc_cn_header_title;
    }

    public void setCc_cn_header_title(String cc_cn_header_title) {
        this.cc_cn_header_title = cc_cn_header_title;
    }

    public String getCc_cn_is_enable_filter() {
        return cc_cn_is_enable_filter;
    }

    public void setCc_cn_is_enable_filter(String cc_cn_is_enable_filter) {
        this.cc_cn_is_enable_filter = cc_cn_is_enable_filter;
    }

    public String getCc_cn_is_visible_on_menu() {
        return cc_cn_is_visible_on_menu;
    }

    public void setCc_cn_is_visible_on_menu(String cc_cn_is_visible_on_menu) {
        this.cc_cn_is_visible_on_menu = cc_cn_is_visible_on_menu;
    }

    public String getCc_cn_is_publish() {
        return cc_cn_is_publish;
    }

    public void setCc_cn_is_publish(String cc_cn_is_publish) {
        this.cc_cn_is_publish = cc_cn_is_publish;
    }

    public String getCce_cn_seo_title() {
        return cce_cn_seo_title;
    }

    public void setCce_cn_seo_title(String cce_cn_seo_title) {
        this.cce_cn_seo_title = cce_cn_seo_title;
    }

    public String getCce_cn_seo_description() {
        return cce_cn_seo_description;
    }

    public void setCce_cn_seo_description(String cce_cn_seo_description) {
        this.cce_cn_seo_description = cce_cn_seo_description;
    }

    public String getCce_cn_seo_keywords() {
        return cce_cn_seo_keywords;
    }

    public void setCce_cn_seo_keywords(String cce_cn_seo_keywords) {
        this.cce_cn_seo_keywords = cce_cn_seo_keywords;
    }

    public String getCce_hs_code() {
        return cce_hs_code;
    }

    public void setCce_hs_code(String cce_hs_code) {
        this.cce_hs_code = cce_hs_code;
    }

    public String getCce_hs_code_pu() {
        return cce_hs_code_pu;
    }

    public void setCce_hs_code_pu(String cce_hs_code_pu) {
        this.cce_hs_code_pu = cce_hs_code_pu;
    }

    public String getCce_size_chart() {
        return cce_size_chart;
    }

    public void setCce_size_chart(String cce_size_chart) {
        this.cce_size_chart = cce_size_chart;
    }

    public String getCce_main_category() {
        return cce_main_category;
    }

    public void setCce_main_category(String cce_main_category) {
        this.cce_main_category = cce_main_category;
    }

    public String getCtce_tm_category() {
        return ctce_tm_category;
    }

    public void setCtce_tm_category(String ctce_tm_category) {
        this.ctce_tm_category = ctce_tm_category;
    }

    public String getCjce_jd_category() {
        return cjce_jd_category;
    }

    public void setCjce_jd_category(String cjce_jd_category) {
        this.cjce_jd_category = cjce_jd_category;
    }

    public String getCcps_base_price() {
        return ccps_base_price;
    }

    public void setCcps_base_price(String ccps_base_price) {
        this.ccps_base_price = ccps_base_price;
    }

    public String getCcps_pricing_factor() {
        return ccps_pricing_factor;
    }

    public void setCcps_pricing_factor(String ccps_pricing_factor) {
        this.ccps_pricing_factor = ccps_pricing_factor;
    }

    public String getCcps_exchange_rate() {
        return ccps_exchange_rate;
    }

    public void setCcps_exchange_rate(String ccps_exchange_rate) {
        this.ccps_exchange_rate = ccps_exchange_rate;
    }

    public String getCcps_over_heard_1() {
        return ccps_over_heard_1;
    }

    public void setCcps_over_heard_1(String ccps_over_heard_1) {
        this.ccps_over_heard_1 = ccps_over_heard_1;
    }

    public String getCcps_over_heard_2() {
        return ccps_over_heard_2;
    }

    public void setCcps_over_heard_2(String ccps_over_heard_2) {
        this.ccps_over_heard_2 = ccps_over_heard_2;
    }

    public String getCcps_over_heard_3() {
        return ccps_over_heard_3;
    }

    public void setCcps_over_heard_3(String ccps_over_heard_3) {
        this.ccps_over_heard_3 = ccps_over_heard_3;
    }

    public String getCcps_over_heard_4() {
        return ccps_over_heard_4;
    }

    public void setCcps_over_heard_4(String ccps_over_heard_4) {
        this.ccps_over_heard_4 = ccps_over_heard_4;
    }

    public String getCcps_over_heard_5() {
        return ccps_over_heard_5;
    }

    public void setCcps_over_heard_5(String ccps_over_heard_5) {
        this.ccps_over_heard_5 = ccps_over_heard_5;
    }

    public String getCcps_shipping_compensation() {
        return ccps_shipping_compensation;
    }

    public void setCcps_shipping_compensation(String ccps_shipping_compensation) {
        this.ccps_shipping_compensation = ccps_shipping_compensation;
    }

    /**
     * 通过 URL KEY 比对
     */
    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof CategoryBean)) return super.equals(obj);

        CategoryBean categoryBean = (CategoryBean) obj;

        return getUrl_key().equals(categoryBean.getUrl_key());
    }
}
