package com.voyageone.oms.formbean;

import com.voyageone.core.ajax.AjaxRequestBean;

/**
 * 画面传入退款信息
 * 
 * @author jacky
 *
 */
public class InFormOrderdetailRefunds extends AjaxRequestBean {
	
	private OutFormOrderdetailRefunds refundInfo;

	public OutFormOrderdetailRefunds getRefundInfo() {
		return refundInfo;
	}

	public void setRefundInfo(OutFormOrderdetailRefunds refundInfo) {
		this.refundInfo = refundInfo;
	}

	@Override
	protected String[] getValidateSorts() {
		return new String[]{"noShippedDays", "customerEmail"};
	}
}
