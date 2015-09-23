package com.voyageone.batch.oms.modelbean;

import java.math.BigDecimal;

public class TaobaoPromotionBean {

	//折扣描述
	protected String description;
	 /**
     * Retrieve the value of the text the user has sent as form data
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the value of the form data text
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
    //折扣名称
	protected String name;
	 /**
     * Retrieve the value of the text the user has sent as form data
     */
    public String getName() {
        return name;
    }

    /**
     * Set the value of the form data text
     */
    public void setName(String name) {
        this.name = name;
    }
    
    //折扣金额
	protected BigDecimal discount;
	 /**
     * Retrieve the value of the text the user has sent as form data
     */
    public BigDecimal getDiscount() {
        return discount;
    }

    /**
     * Set the value of the form data text
     */
    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }
    
    //Id
	protected String id;
	 /**
     * Retrieve the value of the text the user has sent as form data
     */
    public String getId() {
        return id;
    }

    /**
     * Set the value of the form data text
     */
    public void setId(String id) {
        this.id = id;
    }
    
    protected int num;
    
	/**
	 * @return the num
	 */
	public int getNum() {
		return num;
	}

	/**
	 * @param num the num to set
	 */
	public void setNum(int num) {
		this.num = num;
	}
}
