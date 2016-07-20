package com.voyageone.web2.sdk.api.request;

import com.voyageone.web2.sdk.api.VoApiRequest;
import com.voyageone.web2.sdk.api.exception.ApiRuleException;
import com.voyageone.web2.sdk.api.response.VmsOrderAddResponse;
import com.voyageone.web2.sdk.api.util.RequestUtils;

/**
 *
 * /vms/order/AddOrderInfo
 *
 * Created on 16/07/18.
 * @author jeff.duan
 * @version 1.0
 */
public class VmsOrderAddRequest extends VoApiRequest<VmsOrderAddResponse> {

	@Override
	public String getApiURLPath() {
		return "/vms/order/addOrderInfo";
	}

	/**
	 * channelId(必须)
	 */
	private String channelId;

	/**
	 * reservationId(必须)
	 */
	private String reservationId;

	/**
	 * consolidationOrderId(必须)
	 */
	private String consolidationOrderId;

	/**
	 * orderId(必须)
	 */
	private String orderId;

	/**
	 * clientSku(必须)
	 */
	private String clientSku;

	/**
	 * barcode(必须)
	 */
	private String barcode;

	/**
	 * description(必须)
	 */
	private String description;

	/**
	 * consolidationOrderTime(必须)
	 */
	private Long consolidationOrderTime;

	/**
	 * orderTime(必须)
	 */
	private Long orderTime;

	/**
	 * cartId(必须)
	 */
	private Integer cartId;

	/**
	 * clientMsrp(必须)
	 */
	private Double clientMsrp;

	/**
	 * clientNetPrice(必须)
	 */
	private Double clientNetPrice;

	/**
	 * clientRetailPrice(必须)
	 */
	private Double clientRetailPrice;

	/**
	 * retailPrice(必须)
	 */
	private Double retailPrice;

	@Override
	public void requestCheck() throws ApiRuleException {
		RequestUtils.checkNotEmpty(" channelId", channelId);
		RequestUtils.checkNotEmpty(" reservationId", reservationId);
		RequestUtils.checkNotEmpty(" consolidationOrderId", consolidationOrderId);
		RequestUtils.checkNotEmpty(" consolidationOrderTime", consolidationOrderTime);
		RequestUtils.checkNotEmpty(" orderId", orderId);
		RequestUtils.checkNotEmpty(" orderTime", orderTime);
		RequestUtils.checkNotEmpty(" cartId", cartId);
		RequestUtils.checkNotEmpty(" clientSku", clientSku);
		RequestUtils.checkNotEmpty(" barcode", barcode);
		RequestUtils.checkNotEmpty(" cartId", cartId);
		RequestUtils.checkNotEmpty(" clientMsrp", clientMsrp);
		RequestUtils.checkNotEmpty(" clientNetPrice", clientNetPrice);
		RequestUtils.checkNotEmpty(" clientRetailPrice", clientRetailPrice);
		RequestUtils.checkNotEmpty(" retailPrice", retailPrice);
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getReservationId() {
		return reservationId;
	}

	public void setReservationId(String reservationId) {
		this.reservationId = reservationId;
	}

	public String getConsolidationOrderId() {
		return consolidationOrderId;
	}

	public void setConsolidationOrderId(String consolidationOrderId) {
		this.consolidationOrderId = consolidationOrderId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getClientSku() {
		return clientSku;
	}

	public void setClientSku(String clientSku) {
		this.clientSku = clientSku;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getConsolidationOrderTime() {
		return consolidationOrderTime;
	}

	public void setConsolidationOrderTime(Long consolidationOrderTime) {
		this.consolidationOrderTime = consolidationOrderTime;
	}

	public Long getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Long orderTime) {
		this.orderTime = orderTime;
	}

	public Integer getCartId() {
		return cartId;
	}

	public void setCartId(Integer cartId) {
		this.cartId = cartId;
	}

	public Double getClientMsrp() {
		return clientMsrp;
	}

	public void setClientMsrp(Double clientMsrp) {
		this.clientMsrp = clientMsrp;
	}

	public Double getClientNetPrice() {
		return clientNetPrice;
	}

	public void setClientNetPrice(Double clientNetPrice) {
		this.clientNetPrice = clientNetPrice;
	}

	public Double getClientRetailPrice() {
		return clientRetailPrice;
	}

	public void setClientRetailPrice(Double clientRetailPrice) {
		this.clientRetailPrice = clientRetailPrice;
	}

	public Double getRetailPrice() {
		return retailPrice;
	}

	public void setRetailPrice(Double retailPrice) {
		this.retailPrice = retailPrice;
	}

}