package com.voyageone.wms.formbean;

import com.voyageone.core.ajax.AjaxRequestBean;

import java.util.List;

/**   
 * Simple to Introduction  
 * @Package      [com.voyageone.wms.formbean]  
 * @ClassName    [FormBackorder]
 * @Description  [Backorder bean类]
 * @Author       [Kylin]
 * @CreateDate   [20150430]
 * @UpdateUser   [sky]
 * @UpdateDate   [20150528]
 * @UpdateRemark [说明本次修改内容]  
 * @Version      [v1.0] 
 */
public class FormReportBean extends AjaxRequestBean {

	private String fromDate;
	private String toDate;
	private String fromDateGMT;
	private String toDateGMT;
	private String store_id;
	private String order_channel_id;
	private String sku;
	private String initInv;
	private String po;
	private String sell;
	private String returns;
	private String wit;
	private String currInv;

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public String getFromDateGMT() {
		return fromDateGMT;
	}

	public void setFromDateGMT(String fromDateGMT) {
		this.fromDateGMT = fromDateGMT;
	}

	public String getToDateGMT() {
		return toDateGMT;
	}

	public void setToDateGMT(String toDateGMT) {
		this.toDateGMT = toDateGMT;
	}

	public String getStore_id() {
		return store_id;
	}

	public void setStore_id(String store_id) {
		this.store_id = store_id;
	}

	public String getOrder_channel_id() {
		return order_channel_id;
	}

	public void setOrder_channel_id(String order_channel_id) {
		this.order_channel_id = order_channel_id;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getInitInv() {
		return initInv;
	}

	public void setInitInv(String initInv) {
		this.initInv = initInv;
	}

	public String getPo() {
		return po;
	}

	public void setPo(String po) {
		this.po = po;
	}

	public String getSell() {
		return sell;
	}

	public void setSell(String sell) {
		this.sell = sell;
	}

	public String getReturns() {
		return returns;
	}

	public void setReturns(String returns) {
		this.returns = returns;
	}

	public String getWit() {
		return wit;
	}

	public void setWit(String wit) {
		this.wit = wit;
	}

	public String getCurrInv() {
		return currInv;
	}

	public void setCurrInv(String currInv) {
		this.currInv = currInv;
	}

	@Override
	protected String[] getValidateSorts() {
		// TODO Auto-generated method stub
		return null;
	}

}
