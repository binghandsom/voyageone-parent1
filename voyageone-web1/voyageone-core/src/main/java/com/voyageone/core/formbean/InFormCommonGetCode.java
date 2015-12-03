package com.voyageone.core.formbean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.Email;

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
