package com.voyageone.wms.formbean;

import com.voyageone.core.ajax.AjaxRequestBean;
import com.voyageone.core.modelbean.ChannelShopBean;
import com.voyageone.core.modelbean.ChannelStoreBean;

import java.util.List;
import java.util.Map;

/**
 * Simple to Introduction  
 * @Package      [com.voyageone.wms.formbean]
 * @ClassName    [FormReservation]
 * @Description  [Reservation bean类]   
 * @Author       [sky]
 * @CreateDate   [20150421]
 * @UpdateUser   [${user}]   
 * @UpdateDate   [${date} ${time}]   
 * @UpdateRemark [说明本次修改内容]
 * @Version      [v1.0]
 */
public class FormReservation extends AjaxRequestBean {

	private String reservation_id;
	private String order_number;
	private String sku;
    private String barcode;
	private String product;
	private String store_name;
	private String store_id;
	private String res_status_name;
	private String res_status_id;
	private String res_note;
	private String modified;
	private String modifier;
	private String order_channel_id;
	private String order_channel_name;
	private String synShipNo;
	private String source_order_id;
	private String order_status;
	private String ship_channel;
	private String ship_channel_id;
	private String isLock;
	private String hasIdCard;
	private String orderDateTime;
	private String orderDateTime_s;
	private String orderDateTime_e;
	private String shop;
	private String cart_id;
	private String itemCode;
	private String description;
	private String inventory_qty;
	private String openRes_qty;
	private String newOrd_qty;
	private int inProcess_qty;
	private String total;
	private String created;
    private String creater;
	private String fromDate;
	private String toDate;
	private List<Map<String, String>> storeInventory;
	private String changeKind;
	private String processContent;
	private List<ChannelStoreBean> companyStoreList;
	private List<ChannelShopBean> companyShopList;
	private List<String> companyOrdChannelList;
	private String image_path;
	private String origin;
	private String transfer_type;
	private String transfer_origin;
    // 创建时间（本地时间）
    private String created_local;
    // 更新时间（本地时间）
    private String modified_local;
    // 下单时间（本地时间）
    private String orderDateTime_local;
	// 允许变更仓库
	private String change_store_flg;
	// 货架名称
	public String  location_name;
	// 品牌方SKU
	public String client_sku;

	public int getInProcess_qty() {
		return inProcess_qty;
	}

	public void setInProcess_qty(int inProcess_qty) {
		this.inProcess_qty = inProcess_qty;
	}

	public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public String getTransfer_type() {
		return transfer_type;
	}

	public void setTransfer_type(String transfer_type) {
		this.transfer_type = transfer_type;
	}

	public String getTransfer_origin() {
		return transfer_origin;
	}

	public void setTransfer_origin(String transfer_origin) {
		this.transfer_origin = transfer_origin;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getImage_path() {
		return image_path;
	}

	public void setImage_path(String image_path) {
		this.image_path = image_path;
	}

	public List<ChannelStoreBean> getCompanyStoreList() {
        return companyStoreList;
    }

    public void setCompanyStoreList(List<ChannelStoreBean> companyStoreList) {
        this.companyStoreList = companyStoreList;
    }

	public List<ChannelShopBean> getCompanyShopList() {
		return companyShopList;
	}

	public void setCompanyShopList(List<ChannelShopBean> companyShopList) {
		this.companyShopList = companyShopList;
	}

	public List<String> getCompanyOrdChannelList() {
		return companyOrdChannelList;
	}

	public void setCompanyOrdChannelList(List<String> companyOrdChannelList) {
		this.companyOrdChannelList = companyOrdChannelList;
	}

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

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public List<Map<String, String>> getStoreInventory() {
		return storeInventory;
	}

	public void setStoreInventory(List<Map<String, String>> storeInventory) {
		this.storeInventory = storeInventory;
	}

	public String getInventory_qty() {
		return inventory_qty;
	}

	public void setInventory_qty(String inventory_qty) {
		this.inventory_qty = inventory_qty;
	}

	public String getOpenRes_qty() {
		return openRes_qty;
	}

	public void setOpenRes_qty(String openRes_qty) {
		this.openRes_qty = openRes_qty;
	}

	public String getNewOrd_qty() {
		return newOrd_qty;
	}

	public void setNewOrd_qty(String newOrd_qty) {
		this.newOrd_qty = newOrd_qty;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public void setRes_status_id(String res_status_id) {
		this.res_status_id = res_status_id;
	}

	public void setShip_channel_id(String ship_channel_id) {
		this.ship_channel_id = ship_channel_id;
	}

	public void setOrderDateTime_s(String orderDateTime_s) {
		this.orderDateTime_s = orderDateTime_s;
	}

	public void setOrderDateTime_e(String orderDateTime_e) {
		this.orderDateTime_e = orderDateTime_e;
	}

	public void setCart_id(String cart_id) {
		this.cart_id = cart_id;
	}

	public String getRes_status_id() {
		return res_status_id;
	}

	public String getShip_channel_id() {
		return ship_channel_id;
	}

	public String getOrderDateTime_s() {
		return orderDateTime_s;
	}

	public String getOrderDateTime_e() {
		return orderDateTime_e;
	}

	public String getCart_id() {
		return cart_id;
	}

	public void setOrder_channel_name(String order_channel_name) {
		this.order_channel_name = order_channel_name;
	}

	public void setSynShipNo(String synShipNo) {
		this.synShipNo = synShipNo;
	}

	public void setSource_order_id(String source_order_id) {
		this.source_order_id = source_order_id;
	}

	public void setOrder_status(String order_status) {
		this.order_status = order_status;
	}

	public void setShip_channel(String ship_channel) {
		this.ship_channel = ship_channel;
	}

	public void setIsLock(String isLock) {
		this.isLock = isLock;
	}

	public void setHasIdCard(String hasIdCard) {
		this.hasIdCard = hasIdCard;
	}

	public void setOrderDateTime(String orderDateTime) {
		this.orderDateTime = orderDateTime;
	}

	public void setShop(String shop) {
		this.shop = shop;
	}

	public String getOrder_channel_name() {
		return order_channel_name;
	}

	public String getSynShipNo() {
		return synShipNo;
	}

	public String getSource_order_id() {
		return source_order_id;
	}

	public String getOrder_status() {
		return order_status;
	}

	public String getShip_channel() {
		return ship_channel;
	}

	public String getIsLock() {
		return isLock;
	}

	public String getHasIdCard() {
		return hasIdCard;
	}

	public String getOrderDateTime() {
		return orderDateTime;
	}

	public String getShop() {
		return shop;
	}

	public String getReservation_id() {
		return reservation_id;
	}

	public String getOrder_number() {
		return order_number;
	}

	public String getSku() {
		return sku;
	}

	public String getProduct() {
		return product;
	}

	public String getStore_name() {
		return store_name;
	}

	public String getStore_id() {
		return store_id;
	}

	public String getRes_note() {
		return res_note;
	}

	public String getModified() {
		return modified;
	}

	public String getModifier() {
		return modifier;
	}

	public String getOrder_channel_id() {
		return order_channel_id;
	}

	public void setReservation_id(String reservation_id) {
		this.reservation_id = reservation_id;
	}

	public void setOrder_number(String order_number) {
		this.order_number = order_number;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public void setStore_name(String store_name) {
		this.store_name = store_name;
	}

	public void setStore_id(String store_id) {
		this.store_id = store_id;
	}

	public void setRes_note(String res_note) {
		this.res_note = res_note;
	}

	public String getRes_status_name() {
		return res_status_name;
	}

	public void setRes_status_name(String res_status_name) {
		this.res_status_name = res_status_name;
	}

	public void setModified(String modified) {
		this.modified = modified;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public void setOrder_channel_id(String order_channel_id) {
		this.order_channel_id = order_channel_id;
	}

	public String getChangeKind() {
		return changeKind;
	}

	public void setChangeKind(String changeKind) {
		this.changeKind = changeKind;
	}

	public String getProcessContent() {
		return processContent;
	}

	public void setProcessContent(String processContent) {
		this.processContent = processContent;
	}

	public String getOrderDateTime_local() {
		return orderDateTime_local;
	}

	public void setOrderDateTime_local(String orderDateTime_local) {
		this.orderDateTime_local = orderDateTime_local;
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

	public String getChange_store_flg() {
		return change_store_flg;
	}

	public void setChange_store_flg(String change_store_flg) {
		this.change_store_flg = change_store_flg;
	}

	public String getLocation_name() {
		return location_name;
	}

	public void setLocation_name(String location_name) {
		this.location_name = location_name;
	}

	public String getClient_sku() {
		return client_sku;
	}

	public void setClient_sku(String client_sku) {
		this.client_sku = client_sku;
	}

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    @Override
	protected String[] getValidateSorts() {
		// TODO Auto-generated method stub
		return null;
	}

}
