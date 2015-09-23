package com.voyageone.batch.oms.formbean;

/**
 * MagentoOrderBean
 * @author Edward
 *
 */
public class MagentoOrderBean {

	// 产品SKU
	protected String outer_sku_id;
	// 产品Size
	protected String size;
	// 数量
	protected String num;
	// 价格
	protected String price;
	// 商品名称
	protected String title;
	// 商品重量
	protected String weight;
	// 出货仓库：0-美国  0以外-中国
	protected int china_stock;
	
	public void setOuter_sku_id(String outerSkuId) {
		outer_sku_id = outerSkuId;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}	
	public int getChina_stock() {
		return china_stock;
	}
	public void setChina_stock(int chinaStock) {
		china_stock = chinaStock;
	}
	public String getOuter_sku_id() {
		return outer_sku_id;
	}
}
