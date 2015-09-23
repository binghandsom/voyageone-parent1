package com.voyageone.batch.oms.modelbean;

public class TaobaoOrderBean {

	// 商家编码
	protected String outer_sku_id;
	// 数量
	protected String num;
	// 商家ID
	protected String outer_iid;
	// 商品ID
	protected String num_iid;
	// 价格
	protected String price;
	// 商品名称
	protected String title;
    // 商品类型
    protected String type;
	// 出货仓库：0-美国  0以外-中国
	protected int china_stock;
	// 子订单ID
    protected String oid;
    
    protected String discount_fee;

    public String getOuter_sku_id() {
        return outer_sku_id;
    }
    
    public void setOuter_sku_id(String outer_sku_id) {
        this.outer_sku_id = outer_sku_id;
    }

    public String getNum() {
        return num;
    }
    
    public void setNum(String num) {
        this.num = num;
    }

    public String getOuter_iid() {
        return outer_iid;
    }
    
    public void setOuter_iid(String outer_iid) {
        this.outer_iid = outer_iid;
    }

    public String getNum_iid() {
        return num_iid;
    }
    
    public void setNum_iid(String num_iid) {
        this.num_iid = num_iid;
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
    
    public int getChina_stock() {
        return china_stock;
    }
    
    public void setChina_stock(int china_stock) {
        this.china_stock = china_stock;
    }

    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }

	/**
	 * @return the oid
	 */
	public String getOid() {
		return oid;
	}

	/**
	 * @param oid the oid to set
	 */
	public void setOid(String oid) {
		this.oid = oid;
	}

	/**
	 * @return the discount_fee
	 */
	public String getDiscount_fee() {
		return discount_fee;
	}

	/**
	 * @param discountFee the discount_fee to set
	 */
	public void setDiscount_fee(String discountFee) {
		discount_fee = discountFee;
	}
    
}
