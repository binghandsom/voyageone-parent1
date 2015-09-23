package com.voyageone.oms.formbean;

import java.util.List;

import com.voyageone.core.ajax.AjaxRequestBean;

/**
 * 画面传入Code取得bean
 * 
 * @author jerry
 *
 */
public class InFormCommonGetCode extends AjaxRequestBean {

	private List<InFormCommonGetCodeItem> typeIdList;
	
	public List<InFormCommonGetCodeItem> getTypeIdList() {
		return typeIdList;
	}

	public void setTypeIdList(List<InFormCommonGetCodeItem> typeIdList) {
		this.typeIdList = typeIdList;
	}

	@Override
	protected String[] getValidateSorts() {
		return new String[]{"noShippedDays", "customerEmail"};
	}
}
