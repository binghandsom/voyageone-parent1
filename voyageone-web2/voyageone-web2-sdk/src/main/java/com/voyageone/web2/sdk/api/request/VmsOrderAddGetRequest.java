package com.voyageone.web2.sdk.api.request;

import com.voyageone.web2.sdk.api.VoApiRequest;
import com.voyageone.web2.sdk.api.exception.ApiRuleException;
import com.voyageone.web2.sdk.api.response.ProductForWmsGetResponse;
import com.voyageone.web2.sdk.api.response.VmsOrderAddGetResponse;
import com.voyageone.web2.sdk.api.util.RequestUtils;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * /vms/order/AddOrderInfo
 *
 * Created on 16/07/18.
 * @author jeff.duan
 * @version 1.0
 */
public class VmsOrderAddGetRequest extends VoApiRequest<VmsOrderAddGetResponse> {

	@Override
	public String getApiURLPath() {
		return "/vms/order/addOrderInfo";
	}

	/**
	 * channelId
	 */
	private String channelId;

	/**
	 * reservationId
	 */
	private String reservationId;

	/**
	 * consolidationOrderId
	 */
	private String consolidationOrderId;

	/**
	 * orderId
	 */
	private String orderId;

	/**
	 * clientSku
	 */
	private String clientSku;

	/**
	 * barcode
	 */
	private String barcode;

	/**
	 * description
	 */
	private String description;

	/**
	 * consolidationOrderTime
	 */
	private Date consolidationOrderTime;

	/**
	 * orderTime
	 */
	private Date orderTime;

	/**
	 * cartId
	 */
	private Integer cartId;

	/**
	 * clientMsrp
	 */
	private BigDecimal clientMsrp;

	/**
	 * clientNetPrice
	 */
	private BigDecimal clientNetPrice;

	/**
	 * clientRetailPrice
	 */
	private BigDecimal clientRetailPrice;

	/**
	 * msrp
	 */
	private BigDecimal msrp;

	/**
	 * retailPrice
	 */
	private BigDecimal retailPrice;

	/**
	 * salePrice
	 */
	private BigDecimal salePrice;

	public VmsOrderAddGetRequest() {

	}

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
		RequestUtils.checkNotEmpty(" msrp", msrp);
		RequestUtils.checkNotEmpty(" retailPrice", retailPrice);
		RequestUtils.checkNotEmpty(" salePrice", salePrice);
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

	public Date getConsolidationOrderTime() {
		return consolidationOrderTime;
	}

	public void setConsolidationOrderTime(Date consolidationOrderTime) {
		this.consolidationOrderTime = consolidationOrderTime;
	}

	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}

	public Integer getCartId() {
		return cartId;
	}

	public void setCartId(Integer cartId) {
		this.cartId = cartId;
	}

	public BigDecimal getClientMsrp() {
		return clientMsrp;
	}

	public void setClientMsrp(BigDecimal clientMsrp) {
		this.clientMsrp = clientMsrp;
	}

	public BigDecimal getClientNetPrice() {
		return clientNetPrice;
	}

	public void setClientNetPrice(BigDecimal clientNetPrice) {
		this.clientNetPrice = clientNetPrice;
	}

	public BigDecimal getClientRetailPrice() {
		return clientRetailPrice;
	}

	public void setClientRetailPrice(BigDecimal clientRetailPrice) {
		this.clientRetailPrice = clientRetailPrice;
	}

	public BigDecimal getMsrp() {
		return msrp;
	}

	public void setMsrp(BigDecimal msrp) {
		this.msrp = msrp;
	}

	public BigDecimal getRetailPrice() {
		return retailPrice;
	}

	public void setRetailPrice(BigDecimal retailPrice) {
		this.retailPrice = retailPrice;
	}

	public BigDecimal getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
	}
}