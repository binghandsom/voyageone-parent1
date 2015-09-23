package com.voyageone.batch.oms.formbean;

/**
 * MagentoCouponBean
 * @author James
 *
 */
public class MagentoCouponBean {

    // 折扣卷名称
    protected String coupon_name;
    // 折扣卷金额
    protected String coupon_fee;
    
	public String getCoupon_name() {
		return coupon_name;
	}
	public void setCoupon_name(String couponName) {
		coupon_name = couponName;
	}
	public String getCoupon_fee() {
		return coupon_fee;
	}
	public void setCoupon_fee(String couponFee) {
		coupon_fee = couponFee;
	}
}
