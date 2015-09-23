package com.voyageone.bi.disbean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConditionBean {
	
	public enum DateType {
		Year("Year"), Month("Month"), Day("Day");
        // 成员变量
        private String name;

        // 构造方法
        private DateType(String name) {
            this.name = name;
        }

		public String getName() {
			return name;
		}
	}
	
	// 店铺
	private String shop_ids = null;
	// 销售渠道
	private String buyChannel_ids = null;
	// 分类
	private String category_ids = null;
	// 分类
	private String category_child = null;

	// 品牌
	private String brand_ids = null;
	// 颜色
	private String color_ids = null;
	// 尺寸
	private String size_ids = null;
	// 产品
	private String product_ids = null;
	// SKU
	private String sku_ids = null;

	// 时间
	private String time_type = null;
	private String time_start = null;
	private String time_end = null;
	
	// sort
	private String sort_col = null;
	private String sord = null;
	
	//limit 
	private String limit = null;
	
	
	public String getShop_ids() {
		return shop_ids;
	}
	public void setShop_ids(String shop_ids) {
		this.shop_ids = shop_ids;
	}
	public List<String> getShopIdArray() {
		List<String> result = new ArrayList<String>();
		if (shop_ids != null && !"".equals(shop_ids.trim())) {
			result = Arrays.asList(shop_ids.split(","));
		}
		return result;
	}

	public String getBuyChannel_ids() {
		return buyChannel_ids;
	}
	public void setBuyChannel_ids(String buyChannel_ids) {
		this.buyChannel_ids = buyChannel_ids;
	}
	public List<String> getBuyChannel_idsArray() {
		List<String> result = new ArrayList<String>();
		if (buyChannel_ids != null && !"".equals(buyChannel_ids.trim())) {
			result = Arrays.asList(buyChannel_ids.split(","));
		}
		return result;
	}
	
	public String getCategory_ids() {
		return category_ids;
	}
	public void setCategory_ids(String category_ids) {
		this.category_ids = category_ids;
	}
	public List<String> getCategory_idArray() {
		List<String> result = new ArrayList<String>();
		if (category_ids != null && !"".equals(category_ids.trim())) {
			result = Arrays.asList(category_ids.split(","));
		}
		return result;
	}

	public String getCategory_child() {
		return category_child;
	}
	public void setCategory_child(String category_child) {
		this.category_child = category_child;
	}

	public String getBrand_ids() {
		return brand_ids;
	}
	public void setBrand_ids(String brand_ids) {
		this.brand_ids = brand_ids;
	}
	public List<String> getBrand_idArray() {
		List<String> result = new ArrayList<String>();
		if (brand_ids != null && !"".equals(brand_ids.trim())) {
			result = Arrays.asList(brand_ids.split(","));
		}
		return result;
	}

	public String getColor_ids() {
		return color_ids;
	}
	public void setColor_ids(String color_ids) {
		this.color_ids = color_ids;
	}
	public List<String> getColor_idArray() {
		List<String> result = new ArrayList<String>();
		if (color_ids != null && !"".equals(color_ids.trim())) {
			result = Arrays.asList(color_ids.split(","));
		}
		return result;
	}

	public String getSize_ids() {
		return size_ids;
	}
	public void setSize_ids(String size_ids) {
		this.size_ids = size_ids;
	}
	public List<String> getSize_idArray() {
		List<String> result = new ArrayList<String>();
		if (size_ids != null && !"".equals(size_ids.trim())) {
			result = Arrays.asList(size_ids.split(","));
		}
		return result;
	}
	
	public String getProduct_ids() {
		return product_ids;
	}
	public void setProduct_ids(String product_ids) {
		this.product_ids = product_ids;
	}
	public List<String> getProduct_idArray() {
		List<String> result = new ArrayList<String>();
		if (product_ids != null && !"".equals(product_ids.trim())) {
			result = Arrays.asList(product_ids.split(","));
		}
		return result;
	}
	
	public String getSku_ids() {
		return sku_ids;
	}
	public void setSku_ids(String sku_ids) {
		this.sku_ids = sku_ids;
	}
	public List<String> getSku_idArray() {
		List<String> result = new ArrayList<String>();
		if (sku_ids != null && !"".equals(sku_ids.trim())) {
			result = Arrays.asList(sku_ids.split(","));
		}
		return result;
	}

	public String getTime_type() {
		return time_type;
	}
	public void setTime_type(String time_type) {
		this.time_type = time_type;
	}
	public DateType getTimeType() {
		DateType result = DateType.Day;
		if (this.time_type != null) {
			result = DateType.valueOf(this.time_type);
		} 
		return result;
	}

	public String getTime_start() {
		return time_start;
	}
	public void setTime_start(String time_start) {
		this.time_start = time_start;
	}

	public String getTime_end() {
		return time_end;
	}
	public void setTime_end(String time_end) {
		this.time_end = time_end;
	}
	
	public String getSort_col() {
		return sort_col;
	}
	public void setSort_col(String sort_col) {
		this.sort_col = sort_col;
	}
	public String getSord() {
		return sord;
	}
	public void setSord(String sord) {
		this.sord = sord;
	}
	public String getLimit() {
		return limit;
	}
	public void setLimit(String limit) {
		this.limit = limit;
	}
	
	public ConditionBean createCopy() {
		ConditionBean result = new ConditionBean();
		result.setShop_ids(shop_ids);
		result.setBuyChannel_ids(buyChannel_ids);
		result.setCategory_ids(category_ids);
		result.setCategory_child(category_child);
		result.setBrand_ids(brand_ids);
		result.setColor_ids(color_ids);
		result.setSize_ids(size_ids);
		result.setProduct_ids(product_ids);
		result.setSku_ids(sku_ids);
		result.setTime_type(time_type);
		result.setTime_start(time_start);
		result.setTime_end(time_end);
		result.setSort_col(sort_col);
		result.setSord(sord);
		result.setLimit(limit);
		return result;
	}
}
