package com.voyageone.batch.oms.formbean;

/**
 * MagentoGiftcardBean
 * @author Edward
 *
 */
public class MagentoGiftcardBean {

    // 礼品卡名称
    protected String giftcard_name;
    // 礼品卡金额
    protected String giftcard_fee;
    
	public String getGiftcard_name() {
		return giftcard_name;
	}
	public void setGiftcard_name(String giftcardName) {
		giftcard_name = giftcardName;
	}
	public String getGiftcard_fee() {
		return giftcard_fee;
	}
	public void setGiftcard_fee(String giftcardFee) {
		giftcard_fee = giftcardFee;
	}

}
