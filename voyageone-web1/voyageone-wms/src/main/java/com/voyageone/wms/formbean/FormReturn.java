package com.voyageone.wms.formbean;

import com.voyageone.core.ajax.AjaxRequestBean;

import java.util.List;
import java.util.Map;

/**   
 * Simple to Introduction  
 * @Package      [com.voyageone.wms.formbean]  
 * @ClassName    [FormReturn]
 * @Description  [Return bean类]
 * @Author       [sky]   
 * @CreateDate   [20150421]   
 * @UpdateUser   [${user}]   
 * @UpdateDate   [${date} ${time}]   
 * @UpdateRemark [说明本次修改内容]  
 * @Version      [v1.0] 
 */
public class FormReturn extends AjaxRequestBean {
	
	private String return_id;
	private String return_session_id;
	private String order_channel_id;
	private String store_id;
	private String store_name;
	private String res_id;
	private String res_status;
	private String res_status_name;
	private String barCode;
	private String sku;
	private String size;
	private String product_name;
	private String order_num;
	private String condition;
	private String condition_id;
	private String reason;
	private String reason_name;
	private String notes;
	private String received_from;
	private String received_from_id;
	private String tracking_no;
	private String return_status;
	private String return_status_id;
	private String status_name;
	private String active;
	private String created;
	private String created_local;
	private String createTime_s;
	private String createTime_e;
	private String creater;
	private String modified;
	private String modified_local;
	private String modifier;
	private String updateTime_s;
	private String updateTime_e;
	private String user;
	private String return_type;
	private String syn_flg;
	private String close_day_flg;
	private String lang;
	private String return_session_status;
	private List<String> orderChannelId;
	private Map<String, Integer> typeIdMap;
	private String reservationLog_notes;
	private String returnType_name;
	private String label_type;
	private String location_name;
	private String Upc;
    private List<String> store_id_list;
    private List<String> condition_id_list;

	public String getReservationLog_notes() {
		return reservationLog_notes;
	}

	public void setReservationLog_notes(String reservationLog_notes) {
		this.reservationLog_notes = reservationLog_notes;
	}

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
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

	public Map<String, Integer> getTypeIdMap() {
		return typeIdMap;
	}

	public void setTypeIdMap(Map<String, Integer> typeIdMap) {
		this.typeIdMap = typeIdMap;
	}

	public String getReturn_session_status() {
		return return_session_status;
	}

	public void setReturn_session_status(String return_session_status) {
		this.return_session_status = return_session_status;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getCreateTime_s() {
		return createTime_s;
	}

	public void setCreateTime_s(String createTime_s) {
		this.createTime_s = createTime_s;
	}

	public String getCreateTime_e() {
		return createTime_e;
	}

	public void setCreateTime_e(String createTime_e) {
		this.createTime_e = createTime_e;
	}
	
	public String getCondition_id() {
		return condition_id;
	}

	public void setCondition_id(String condition_id) {
		this.condition_id = condition_id;
	}

	public String getReceived_from_id() {
		return received_from_id;
	}

	public void setReceived_from_id(String received_from_id) {
		this.received_from_id = received_from_id;
	}

	public String getReturn_status_id() {
		return return_status_id;
	}

	public void setReturn_status_id(String return_status_id) {
		this.return_status_id = return_status_id;
	}
	
	public String getClose_day_flg() {
		return close_day_flg;
	}

	public void setClose_day_flg(String close_day_flg) {
		this.close_day_flg = close_day_flg;
	}

	public void setReturn_type(String return_type) {
		this.return_type = return_type;
	}

	public void setSyn_flg(String syn_flg) {
		this.syn_flg = syn_flg;
	}

	public String getReturn_type() {
		return return_type;
	}

	public String getSyn_flg() {
		return syn_flg;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getUser() {
		return user;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setReason_name(String reason_name) {
		this.reason_name = reason_name;
	}

	public void setStatus_name(String status_name) {
		this.status_name = status_name;
	}

	public void setUpdateTime_s(String updateTime_s) {
		this.updateTime_s = updateTime_s;
	}

	public void setUpdateTime_e(String updateTime_e) {
		this.updateTime_e = updateTime_e;
	}

	public String getReason_name() {
		return reason_name;
	}

	public String getStatus_name() {
		return status_name;
	}

	public String getUpdateTime_s() {
		return updateTime_s;
	}

	public String getUpdateTime_e() {
		return updateTime_e;
	}

	public void setReturn_id(String return_id) {
		this.return_id = return_id;
	}

	public void setReturn_session_id(String return_session_id) {
		this.return_session_id = return_session_id;
	}

	public void setOrder_channel_id(String order_channel_id) {
		this.order_channel_id = order_channel_id;
	}

	public void setStore_id(String store_id) {
		this.store_id = store_id;
	}

	public void setStore_name(String store_name) {
		this.store_name = store_name;
	}

	public void setRes_id(String res_id) {
		this.res_id = res_id;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public void setOrder_num(String order_num) {
		this.order_num = order_num;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public void setReceived_from(String received_from) {
		this.received_from = received_from;
	}

	public void setTracking_no(String tracking_no) {
		this.tracking_no = tracking_no;
	}

	public void setReturn_status(String return_status) {
		this.return_status = return_status;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public void setModified(String modified) {
		this.modified = modified;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public String getReturn_id() {
		return return_id;
	}

	public String getReturn_session_id() {
		return return_session_id;
	}

	public String getOrder_channel_id() {
		return order_channel_id;
	}

	public String getStore_id() {
		return store_id;
	}

	public String getStore_name() {
		return store_name;
	}

	public String getRes_id() {
		return res_id;
	}

	public String getSku() {
		return sku;
	}

	public String getOrder_num() {
		return order_num;
	}

	public String getCondition() {
		return condition;
	}

	public String getReason() {
		return reason;
	}

	public String getNotes() {
		return notes;
	}

	public String getReceived_from() {
		return received_from;
	}

	public String getTracking_no() {
		return tracking_no;
	}

	public String getReturn_status() {
		return return_status;
	}

	public String getActive() {
		return active;
	}

	public String getCreated() {
		return created;
	}

	public String getCreater() {
		return creater;
	}

	public String getModified() {
		return modified;
	}

	public String getModifier() {
		return modifier;
	}

	public List<String> getOrderChannelId() {
		return orderChannelId;
	}

	public void setOrderChannelId(List<String> orderChannelId) {
		this.orderChannelId = orderChannelId;
	}

	public String getReturnType_name() {
		return returnType_name;
	}

	public void setReturnType_name(String returnType_name) {
		this.returnType_name = returnType_name;
	}

	public String getLabel_type() {
		return label_type;
	}

	public void setLabel_type(String label_type) {
		this.label_type = label_type;
	}

	public String getLocation_name() {
		return location_name;
	}

	public void setLocation_name(String location_name) {
		this.location_name = location_name;
	}

	public String getRes_status() {
		return res_status;
	}

	public void setRes_status(String res_status) {
		this.res_status = res_status;
	}

	public String getRes_status_name() {
		return res_status_name;
	}

	public void setRes_status_name(String res_status_name) {
		this.res_status_name = res_status_name;
	}

	public String getUpc() {
		return Upc;
	}

	public void setUpc(String upc) {
		Upc = upc;
	}

    public List<String> getStore_id_list() {
        return store_id_list;
    }

    public void setStore_id_list(List<String> store_id_list) {
        this.store_id_list = store_id_list;
    }

    public List<String> getCondition_id_list() {
        return condition_id_list;
    }

    public void setCondition_id_list(List<String> condition_id_list) {
        this.condition_id_list = condition_id_list;
    }

    @Override
	protected String[] getValidateSorts() {
		// TODO Auto-generated method stub
		return null;
	}
}
