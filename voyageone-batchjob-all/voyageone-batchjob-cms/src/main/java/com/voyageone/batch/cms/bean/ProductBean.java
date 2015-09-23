package com.voyageone.batch.cms.bean;

import java.util.List;
import com.voyageone.batch.cms.bean.ImageBean;
import com.voyageone.batch.cms.bean.ItemBean;

/**
 * ProductBean
 * Created by zero on 7/28/2015.
 *
 * @author zero
 */
public class ProductBean {
	private String url_key;
	private String model_url_key;
	private String category_url_key;
	private String p_code;
	private String p_name;
	private String p_product_type;
	private String p_color_map_id;
	private String p_color;
	private String p_msrp;
	private String p_made_in_country;
	private String p_material_fabric_1;
	private String p_material_fabric_2;
	private String p_material_fabric_3;
	private String p_image_item_count;
	private String p_image_box_count;
	private String p_image_angle_count;
	private String p_available_time;
	private String p_promotion_tag;
	private String p_is_new_arrival;
	private String p_is_outlets_on_sale;
	private String p_is_reward_eligible;
	private String p_is_discount_eligible;
	private String p_is_phone_order_only;
	private String p_order_limit_count;
	private String p_is_approved_description;
	private String p_is_effective;
	private String pe_abstract;
	private String pe_short_description;
	private String pe_long_description;
	private String pe_accessory;
	private String pe_promotion_tag;
	private String pe_main_category;
	private String ape_amazon_browse_category;
	private String gpe_google_category;
	private String gpe_google_department;
	private String ppe_price_grabber_category;
	private String cp_cn_name;
	private String cp_cn_color;
	private String cpe_cn_abstract;
	private String cpe_cn_short_description;
	private String cpe_cn_long_description;
	private String cpe_display_images;
	private String cpe_reference_msrp;
	private String cpe_reference_price;
	private String cpe_hs_code;
	private String cpe_hs_code_pu;
	private String cpe_main_category;
	private String ctpe_tb_name;
	private String ctpe_tb_attachment_name;
	private String ctpe_tm_name;
	private String ctpe_tm_attachment_name;
	private String ctpe_tg_name;
	private String ctpe_tg_attachment_name;
	private String ctpe_tm_short_description;
	private String ctpe_tm_long_description;
	private String ctpe_tm_category;
	private String cjpe_jd_name;
	private String cjpe_jg_name;
	private String cjpe_jd_short_description;
	private String cjpe_jd_long_description;
	private String cjpe_jd_category;
	private String cpps_base_price;
	private String cpps_pricing_factor;
	private String cpps_exchange_rate;
	private String cpps_over_heard_1;
	private String cpps_over_heard_2;
	private String cpps_over_heard_3;
	private String cpps_over_heard_4;
	private String cpps_over_heard_5;
	private String cpps_shipping_compensation;
	private String ps_price;
	private String cps_cn_price_rmb;
	private List <ImageBean> images;
	private List <ItemBean > itembeans;

	public String getUrl_key() {
		return url_key;
	}

	public void setUrl_key(String url_key) {
		this.url_key = url_key;
	}

	public String getModel_url_key() {
		return model_url_key;
	}

	public void setModel_url_key(String model_url_key) {
		this.model_url_key = model_url_key;
	}

	public String getCategory_url_key() {
		return category_url_key;
	}

	public void setCategory_url_key(String category_url_key) {
		this.category_url_key = category_url_key;
	}

	public String getP_code() {
		return p_code;
	}

	public void setP_code(String p_code) {
		this.p_code = p_code;
	}

	public String getP_name() {
		return p_name;
	}

	public void setP_name(String p_name) {
		this.p_name = p_name;
	}

	public String getP_product_type() {
		return p_product_type;
	}

	public void setP_product_type(String p_product_type) {
		this.p_product_type = p_product_type;
	}

	public String getP_color_map_id() {
		return p_color_map_id;
	}

	public void setP_color_map_id(String p_color_map_id) {
		this.p_color_map_id = p_color_map_id;
	}

	public String getP_color() {
		return p_color;
	}

	public void setP_color(String p_color) {
		this.p_color = p_color;
	}

	public String getP_msrp() {
		return p_msrp;
	}

	public void setP_msrp(String p_msrp) {
		this.p_msrp = p_msrp;
	}

	public String getP_made_in_country() {
		return p_made_in_country;
	}

	public void setP_made_in_country(String p_made_in_country) {
		this.p_made_in_country = p_made_in_country;
	}

	public String getP_material_fabric_1() {
		return p_material_fabric_1;
	}

	public void setP_material_fabric_1(String p_material_fabric_1) {
		this.p_material_fabric_1 = p_material_fabric_1;
	}

	public String getP_material_fabric_2() {
		return p_material_fabric_2;
	}

	public void setP_material_fabric_2(String p_material_fabric_2) {
		this.p_material_fabric_2 = p_material_fabric_2;
	}

	public String getP_material_fabric_3() {
		return p_material_fabric_3;
	}

	public void setP_material_fabric_3(String p_material_fabric_3) {
		this.p_material_fabric_3 = p_material_fabric_3;
	}

	public String getP_image_item_count() {
		return p_image_item_count;
	}

	public void setP_image_item_count(String p_image_item_count) {
		this.p_image_item_count = p_image_item_count;
	}

	public String getP_image_box_count() {
		return p_image_box_count;
	}

	public void setP_image_box_count(String p_image_box_count) {
		this.p_image_box_count = p_image_box_count;
	}

	public String getP_image_angle_count() {
		return p_image_angle_count;
	}

	public void setP_image_angle_count(String p_image_angle_count) {
		this.p_image_angle_count = p_image_angle_count;
	}

	public String getP_available_time() {
		return p_available_time;
	}

	public void setP_available_time(String p_available_time) {
		this.p_available_time = p_available_time;
	}

	public String getP_promotion_tag() {
		return p_promotion_tag;
	}

	public void setP_promotion_tag(String p_promotion_tag) {
		this.p_promotion_tag = p_promotion_tag;
	}

	public String getP_is_new_arrival() {
		return p_is_new_arrival;
	}

	public void setP_is_new_arrival(String p_is_new_arrival) {
		this.p_is_new_arrival = p_is_new_arrival;
	}

	public String getP_is_outlets_on_sale() {
		return p_is_outlets_on_sale;
	}

	public void setP_is_outlets_on_sale(String p_is_outlets_on_sale) {
		this.p_is_outlets_on_sale = p_is_outlets_on_sale;
	}

	public String getP_is_reward_eligible() {
		return p_is_reward_eligible;
	}

	public void setP_is_reward_eligible(String p_is_reward_eligible) {
		this.p_is_reward_eligible = p_is_reward_eligible;
	}

	public String getP_is_discount_eligible() {
		return p_is_discount_eligible;
	}

	public void setP_is_discount_eligible(String p_is_discount_eligible) {
		this.p_is_discount_eligible = p_is_discount_eligible;
	}

	public String getP_is_phone_order_only() {
		return p_is_phone_order_only;
	}

	public void setP_is_phone_order_only(String p_is_phone_order_only) {
		this.p_is_phone_order_only = p_is_phone_order_only;
	}

	public String getP_order_limit_count() {
		return p_order_limit_count;
	}

	public void setP_order_limit_count(String p_order_limit_count) {
		this.p_order_limit_count = p_order_limit_count;
	}

	public String getP_is_approved_description() {
		return p_is_approved_description;
	}

	public void setP_is_approved_description(String p_is_approved_description) {
		this.p_is_approved_description = p_is_approved_description;
	}

	public String getP_is_effective() {
		return p_is_effective;
	}

	public void setP_is_effective(String p_is_effective) {
		this.p_is_effective = p_is_effective;
	}

	public String getPe_abstract() {
		return pe_abstract;
	}

	public void setPe_abstract(String pe_abstract) {
		this.pe_abstract = pe_abstract;
	}

	public String getPe_short_description() {
		return pe_short_description;
	}

	public void setPe_short_description(String pe_short_description) {
		this.pe_short_description = pe_short_description;
	}

	public String getPe_long_description() {
		return pe_long_description;
	}

	public void setPe_long_description(String pe_long_description) {
		this.pe_long_description = pe_long_description;
	}

	public String getPe_accessory() {
		return pe_accessory;
	}

	public void setPe_accessory(String pe_accessory) {
		this.pe_accessory = pe_accessory;
	}

	public String getPe_promotion_tag() {
		return pe_promotion_tag;
	}

	public void setPe_promotion_tag(String pe_promotion_tag) {
		this.pe_promotion_tag = pe_promotion_tag;
	}

	public String getPe_main_category() {
		return pe_main_category;
	}

	public void setPe_main_category(String pe_main_category) {
		this.pe_main_category = pe_main_category;
	}

	public String getApe_amazon_browse_category() {
		return ape_amazon_browse_category;
	}

	public void setApe_amazon_browse_category(String ape_amazon_browse_category) {
		this.ape_amazon_browse_category = ape_amazon_browse_category;
	}

	public String getGpe_google_category() {
		return gpe_google_category;
	}

	public void setGpe_google_category(String gpe_google_category) {
		this.gpe_google_category = gpe_google_category;
	}

	public String getGpe_google_department() {
		return gpe_google_department;
	}

	public void setGpe_google_department(String gpe_google_department) {
		this.gpe_google_department = gpe_google_department;
	}

	public String getPpe_price_grabber_category() {
		return ppe_price_grabber_category;
	}

	public void setPpe_price_grabber_category(String ppe_price_grabber_category) {
		this.ppe_price_grabber_category = ppe_price_grabber_category;
	}

	public String getCp_cn_name() {
		return cp_cn_name;
	}

	public void setCp_cn_name(String cp_cn_name) {
		this.cp_cn_name = cp_cn_name;
	}

	public String getCp_cn_color() {
		return cp_cn_color;
	}

	public void setCp_cn_color(String cp_cn_color) {
		this.cp_cn_color = cp_cn_color;
	}

	public String getCpe_cn_abstract() {
		return cpe_cn_abstract;
	}

	public void setCpe_cn_abstract(String cpe_cn_abstract) {
		this.cpe_cn_abstract = cpe_cn_abstract;
	}

	public String getCpe_cn_short_description() {
		return cpe_cn_short_description;
	}

	public void setCpe_cn_short_description(String cpe_cn_short_description) {
		this.cpe_cn_short_description = cpe_cn_short_description;
	}

	public String getCpe_cn_long_description() {
		return cpe_cn_long_description;
	}

	public void setCpe_cn_long_description(String cpe_cn_long_description) {
		this.cpe_cn_long_description = cpe_cn_long_description;
	}

	public String getCpe_display_images() {
		return cpe_display_images;
	}

	public void setCpe_display_images(String cpe_display_images) {
		this.cpe_display_images = cpe_display_images;
	}

	public String getCpe_reference_msrp() {
		return cpe_reference_msrp;
	}

	public void setCpe_reference_msrp(String cpe_reference_msrp) {
		this.cpe_reference_msrp = cpe_reference_msrp;
	}

	public String getCpe_reference_price() {
		return cpe_reference_price;
	}

	public void setCpe_reference_price(String cpe_reference_price) {
		this.cpe_reference_price = cpe_reference_price;
	}

	public String getCpe_hs_code() {
		return cpe_hs_code;
	}

	public void setCpe_hs_code(String cpe_hs_code) {
		this.cpe_hs_code = cpe_hs_code;
	}

	public String getCpe_hs_code_pu() {
		return cpe_hs_code_pu;
	}

	public void setCpe_hs_code_pu(String cpe_hs_code_pu) {
		this.cpe_hs_code_pu = cpe_hs_code_pu;
	}

	public String getCpe_main_category() {
		return cpe_main_category;
	}

	public void setCpe_main_category(String cpe_main_category) {
		this.cpe_main_category = cpe_main_category;
	}

	public String getCtpe_tb_name() {
		return ctpe_tb_name;
	}

	public void setCtpe_tb_name(String ctpe_tb_name) {
		this.ctpe_tb_name = ctpe_tb_name;
	}

	public String getCtpe_tb_attachment_name() {
		return ctpe_tb_attachment_name;
	}

	public void setCtpe_tb_attachment_name(String ctpe_tb_attachment_name) {
		this.ctpe_tb_attachment_name = ctpe_tb_attachment_name;
	}

	public String getCtpe_tm_name() {
		return ctpe_tm_name;
	}

	public void setCtpe_tm_name(String ctpe_tm_name) {
		this.ctpe_tm_name = ctpe_tm_name;
	}

	public String getCtpe_tm_attachment_name() {
		return ctpe_tm_attachment_name;
	}

	public void setCtpe_tm_attachment_name(String ctpe_tm_attachment_name) {
		this.ctpe_tm_attachment_name = ctpe_tm_attachment_name;
	}

	public String getCtpe_tg_name() {
		return ctpe_tg_name;
	}

	public void setCtpe_tg_name(String ctpe_tg_name) {
		this.ctpe_tg_name = ctpe_tg_name;
	}

	public String getCtpe_tg_attachment_name() {
		return ctpe_tg_attachment_name;
	}

	public void setCtpe_tg_attachment_name(String ctpe_tg_attachment_name) {
		this.ctpe_tg_attachment_name = ctpe_tg_attachment_name;
	}

	public String getCtpe_tm_short_description() {
		return ctpe_tm_short_description;
	}

	public void setCtpe_tm_short_description(String ctpe_tm_short_description) {
		this.ctpe_tm_short_description = ctpe_tm_short_description;
	}

	public String getCtpe_tm_long_description() {
		return ctpe_tm_long_description;
	}

	public void setCtpe_tm_long_description(String ctpe_tm_long_description) {
		this.ctpe_tm_long_description = ctpe_tm_long_description;
	}

	public String getCtpe_tm_category() {
		return ctpe_tm_category;
	}

	public void setCtpe_tm_category(String ctpe_tm_category) {
		this.ctpe_tm_category = ctpe_tm_category;
	}

	public String getCjpe_jd_name() {
		return cjpe_jd_name;
	}

	public void setCjpe_jd_name(String cjpe_jd_name) {
		this.cjpe_jd_name = cjpe_jd_name;
	}

	public String getCjpe_jg_name() {
		return cjpe_jg_name;
	}

	public void setCjpe_jg_name(String cjpe_jg_name) {
		this.cjpe_jg_name = cjpe_jg_name;
	}

	public String getCjpe_jd_short_description() {
		return cjpe_jd_short_description;
	}

	public void setCjpe_jd_short_description(String cjpe_jd_short_description) {
		this.cjpe_jd_short_description = cjpe_jd_short_description;
	}

	public String getCjpe_jd_long_description() {
		return cjpe_jd_long_description;
	}

	public void setCjpe_jd_long_description(String cjpe_jd_long_description) {
		this.cjpe_jd_long_description = cjpe_jd_long_description;
	}

	public String getCjpe_jd_category() {
		return cjpe_jd_category;
	}

	public void setCjpe_jd_category(String cjpe_jd_category) {
		this.cjpe_jd_category = cjpe_jd_category;
	}

	public String getCpps_base_price() {
		return cpps_base_price;
	}

	public void setCpps_base_price(String cpps_base_price) {
		this.cpps_base_price = cpps_base_price;
	}

	public String getCpps_pricing_factor() {
		return cpps_pricing_factor;
	}

	public void setCpps_pricing_factor(String cpps_pricing_factor) {
		this.cpps_pricing_factor = cpps_pricing_factor;
	}

	public String getCpps_exchange_rate() {
		return cpps_exchange_rate;
	}

	public void setCpps_exchange_rate(String cpps_exchange_rate) {
		this.cpps_exchange_rate = cpps_exchange_rate;
	}

	public String getCpps_over_heard_1() {
		return cpps_over_heard_1;
	}

	public void setCpps_over_heard_1(String cpps_over_heard_1) {
		this.cpps_over_heard_1 = cpps_over_heard_1;
	}

	public String getCpps_over_heard_2() {
		return cpps_over_heard_2;
	}

	public void setCpps_over_heard_2(String cpps_over_heard_2) {
		this.cpps_over_heard_2 = cpps_over_heard_2;
	}

	public String getCpps_over_heard_3() {
		return cpps_over_heard_3;
	}

	public void setCpps_over_heard_3(String cpps_over_heard_3) {
		this.cpps_over_heard_3 = cpps_over_heard_3;
	}

	public String getCpps_over_heard_4() {
		return cpps_over_heard_4;
	}

	public void setCpps_over_heard_4(String cpps_over_heard_4) {
		this.cpps_over_heard_4 = cpps_over_heard_4;
	}

	public String getCpps_over_heard_5() {
		return cpps_over_heard_5;
	}

	public void setCpps_over_heard_5(String cpps_over_heard_5) {
		this.cpps_over_heard_5 = cpps_over_heard_5;
	}

	public String getCpps_shipping_compensation() {
		return cpps_shipping_compensation;
	}

	public void setCpps_shipping_compensation(String cpps_shipping_compensation) {
		this.cpps_shipping_compensation = cpps_shipping_compensation;
	}

	public String getPs_price() {
		return ps_price;
	}

	public void setPs_price(String ps_price) {
		this.ps_price = ps_price;
	}

	public String getCps_cn_price_rmb() {
		return cps_cn_price_rmb;
	}

	public void setCps_cn_price_rmb(String cps_cn_price_rmb) {
		this.cps_cn_price_rmb = cps_cn_price_rmb;
	}

	public List<ImageBean> getImages() {
		return images;
	}

	public void setImages(List<ImageBean> images) {
		this.images = images;
	}

	public List<ItemBean> getItembeans() {
		return itembeans;
	}

	public void setItembeans(List<ItemBean> itembeans) {
		this.itembeans = itembeans;
	}
}
