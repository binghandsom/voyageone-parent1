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
public class FormBackorder extends AjaxRequestBean {
	
	private int seq;
	private String order_channel_id;
	private int store_id;
	private String 	store_name;
	private String sku;
	private int active;
	private String created;
	private String created_local;
	private String creater;
	private String modified;
	private String modified_local;
	private String modifier;
    private List<String> orderChannelId;

	public String getStore_name() {
		return store_name;
	}

	public void setStore_name(String store_name) {
		this.store_name = store_name;
	}

	public String getOrder_channel_id() {
		return order_channel_id;
	}

	public void setOrder_channel_id(String order_channel_id) {
		this.order_channel_id = order_channel_id;
	}

	public List<String> getOrderChannelId() {
		return orderChannelId;
	}

	public void setOrderChannelId(List<String> orderChannelId) {
		this.orderChannelId = orderChannelId;
	}

	public String getCreated_local() {
		return created_local;
	}

	public void setCreated_local(String created_local) {
		this.created_local = created_local;
	}

	public String getModified_local() {
		return modified_local;
	}

	public void setModified_local(String modified_local) {
		this.modified_local = modified_local;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public int getStore_id() {
		return store_id;
	}

	public void setStore_id(int store_id) {
		this.store_id = store_id;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public String getModified() {
		return modified;
	}

	public void setModified(String modified) {
		this.modified = modified;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	@Override
	protected String[] getValidateSorts() {
		// TODO Auto-generated method stub
		return null;
	}

}
