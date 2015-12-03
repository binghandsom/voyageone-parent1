package com.voyageone.wms.formbean;

import com.voyageone.core.ajax.AjaxRequestBean;

import java.util.List;
import java.util.Map;

/**   
 * Simple to Introduction  
 * @Package      [com.voyageone.wms.formbean]  
 * @ClassName    [FormStockTake]
 * @Description  [StockTake bean类]
 * @Author       [sky]   
 * @CreateDate   [20150518]
 * @UpdateUser   [${user}]   
 * @UpdateDate   [${date} ${time}]   
 * @UpdateRemark [说明本次修改内容]  
 * @Version      [v1.0] 
 */
public class FormStocktake extends AjaxRequestBean {
	private String user;
	private String lang;
	private String order_channel_id;
	private List<String> orderChannelId;
	private Map<String, Integer> typeIdMap;
	private int stocktake_id;
	private String stocktake_name;
	private String stocktake_status;
	private String stocktake_statusName;
	private int store_id;
	private String store_name;
	private String creater;
	private String created;
	private String created_local;
	private String modified;
	private String modified_local;
	private String modifiedTime_s;
	private String modifiedTime_e;
	private String syn_flg;
	private int active;
	private String stocktakeType;
	private String sessionName;
	private String sectionName;
	private String sectionStatus;
	private String sectionStatusName;
	private int location_id;
	private String location_name;
	private String sessionStatus;
	private String sessionStatusName;
	private int stocktake_detail_id;
	private String code;
	private String sku;
	private String upc;
	private String size;
	private String productName;
	private int stocktake_qty;
	private int stocktake_qty_offset;
	private String isFixed;
	private int seq;
	private int inventory;

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
	public String getIsFixed() {
		return isFixed;
	}

	public void setIsFixed(String isFixed) {
		this.isFixed = isFixed;
	}

	public int getStocktake_qty_offset() {
		return stocktake_qty_offset;
	}

	public void setStocktake_qty_offset(int stocktake_qty_offset) {
		this.stocktake_qty_offset = stocktake_qty_offset;
	}

	public String getSessionName() {
		return sessionName;
	}

	public void setSessionName(String sessionName) {
		this.sessionName = sessionName;
	}

	public int getStocktake_qty() {
		return stocktake_qty;
	}

	public void setStocktake_qty(int stocktake_qty) {
		this.stocktake_qty = stocktake_qty;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getUpc() {
		return upc;
	}

	public void setUpc(String upc) {
		this.upc = upc;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getStocktake_detail_id() {
		return stocktake_detail_id;
	}

	public void setStocktake_detail_id(int stocktake_detail_id) {
		this.stocktake_detail_id = stocktake_detail_id;
	}

	public String getSectionStatusName() {
		return sectionStatusName;
	}

	public void setSectionStatusName(String sectionStatusName) {
		this.sectionStatusName = sectionStatusName;
	}

	public String getSessionStatusName() {
		return sessionStatusName;
	}

	public void setSessionStatusName(String sessionStatusName) {
		this.sessionStatusName = sessionStatusName;
	}

	public String getSessionStatus() {
		return sessionStatus;
	}

	public void setSessionStatus(String sessionStatus) {
		this.sessionStatus = sessionStatus;
	}

	public int getLocation_id() {
		return location_id;
	}

	public void setLocation_id(int location_id) {
		this.location_id = location_id;
	}

	public String getLocation_name() {
		return location_name;
	}

	public void setLocation_name(String location_name) {
		this.location_name = location_name;
	}

	public String getSectionName() {
		return sectionName;
	}

	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}

	public String getSectionStatus() {
		return sectionStatus;
	}

	public void setSectionStatus(String sectionStatus) {
		this.sectionStatus = sectionStatus;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getStocktakeType() {
		return stocktakeType;
	}

	public void setStocktakeType(String stocktakeType) {
		this.stocktakeType = stocktakeType;
	}

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}

	public String getSyn_flg() {
		return syn_flg;
	}

	public void setSyn_flg(String syn_flg) {
		this.syn_flg = syn_flg;
	}

	public String getOrder_channel_id() {
		return order_channel_id;
	}

	public void setOrder_channel_id(String order_channel_id) {
		this.order_channel_id = order_channel_id;
	}

	public int getStocktake_id() {
		return stocktake_id;
	}

	public void setStocktake_id(int stocktake_id) {
		this.stocktake_id = stocktake_id;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getModifiedTime_s() {
		return modifiedTime_s;
	}

	public void setModifiedTime_s(String modifiedTime_s) {
		this.modifiedTime_s = modifiedTime_s;
	}

	public String getModifiedTime_e() {
		return modifiedTime_e;
	}

	public void setModifiedTime_e(String modifiedTime_e) {
		this.modifiedTime_e = modifiedTime_e;
	}

	public String getStocktake_name() {
		return stocktake_name;
	}

	public void setStocktake_name(String stocktake_name) {
		this.stocktake_name = stocktake_name;
	}

	public String getStocktake_statusName() {
		return stocktake_statusName;
	}

	public void setStocktake_statusName(String stocktake_statusName) {
		this.stocktake_statusName = stocktake_statusName;
	}

	public int getStore_id() {
		return store_id;
	}

	public void setStore_id(int store_id) {
		this.store_id = store_id;
	}

	public String getStore_name() {
		return store_name;
	}

	public void setStore_name(String store_name) {
		this.store_name = store_name;
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

	public Map<String, Integer> getTypeIdMap() {
		return typeIdMap;
	}

	public void setTypeIdMap(Map<String, Integer> typeIdMap) {
		this.typeIdMap = typeIdMap;
	}

	public List<String> getOrderChannelId() {
		return orderChannelId;
	}

	public void setOrderChannelId(List<String> orderChannelId) {
		this.orderChannelId = orderChannelId;
	}

	public String getStocktake_status() {
		return stocktake_status;
	}

	public void setStocktake_status(String stocktake_status) {
		this.stocktake_status = stocktake_status;
	}

	public int getInventory() {
		return inventory;
	}

	public void setInventory(int inventory) {
		this.inventory = inventory;
	}

	@Override
	protected String[] getValidateSorts() {
		// TODO Auto-generated method stub
		return null;
	}
}
