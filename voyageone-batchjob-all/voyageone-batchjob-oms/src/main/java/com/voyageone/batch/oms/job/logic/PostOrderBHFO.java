package com.voyageone.batch.oms.job.logic;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.voyageone.common.components.channelAdvisor.soap.soapenv;
import com.voyageone.common.components.channelAdvisor.bean.orders.*;
import com.voyageone.common.components.channelAdvisor.webservices.APICredentials;
import com.voyageone.common.components.channelAdvisor.webservices.SubmitOrder;
import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import com.voyageone.batch.core.util.JaxbUtil;
import com.voyageone.batch.oms.utils.CommonUtil;
import com.voyageone.common.configs.Properties;
import com.voyageone.common.configs.ThirdPartyConfigs;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.util.StringUtils;

public class PostOrderBHFO extends PostOrderLogic {

	private static String Password;
	private static String DeveloperKey;
	private static String AccountID;
	private static String OrderEmail;
	private static String BHFOPromoCode;
	private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private static String MAPPING_BHFO_API_URL;
	/**
	 * namespace
	 */
	private static String WEBSERVICE_NAMESPACE;

	/**
	 * method
	 */
	private static String WEBSERVICE_SUBMIT = "SubmitOrder";

	private String response;

	@Override
	public String getPostdata() throws Exception {

		int MaxLineItemID = 1;
		APICredentials aPICredentials = new APICredentials();
		aPICredentials.setDeveloperKey(DeveloperKey);
		aPICredentials.setPassword(Password);

		OrderSubmit order = new OrderSubmit();

		order.setClientOrderIdentifier(rv.get("SourceOrderID").toString());
		// jewelry的场合并且email不为空的场合
		if(this.channelId.equalsIgnoreCase(ChannelConfigEnums.Channel.JEWELRY.getId()) 
				&& !StringUtils.isEmpty((String)rv.get("email"))){
			order.setBuyerEmailAddress((String)rv.get("email"));
		}else{
			order.setBuyerEmailAddress(OrderEmail);
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(format.parse(rv.get("OrderDateTime").toString()));
		order.setOrderTimeGMT(calendarToXML(calendar));

		ShippingInfoSubmit ShippingInfo = new ShippingInfoSubmit();
		ShippingInfo.setFirstName(rv.get("ship_name").toString());
		ShippingInfo.setCountryCode("CN");
		ShippingInfo.setCity(rv.get("ship_city").toString());
		ShippingInfo.setAddressLine1(rv.get("ship_address").toString());
		ShippingInfo.setAddressLine2(rv.get("ship_address2").toString());
		ShippingInfo.setPhoneNumberDay(rv.get("ship_phone").toString());
		ShippingInfo.setPhoneNumberEvening(rv.get("ship_phone").toString());
		ShippingInfo.setPostalCode(rv.get("ship_zip").toString());
		ShippingInfo.setRegion(rv.get("ship_state").toString());
		ShippingInfo.setRegionDescription(rv.get("ship_state").toString());
		// jewelry特殊处理
		if(this.channelId.equalsIgnoreCase(ChannelConfigEnums.Channel.JEWELRY.getId())){
			ShippingInfo.setShippingInstructions(rv.get("originSourceOrderId").toString());
		}

		Shipment standardShip = new Shipment();
		standardShip.setShippingCarrier("Standard");
		standardShip.setShippingClass("Shipping");
		ArrayOfShipment shipmentList = new ArrayOfShipment();
		shipmentList.getShipment().add(standardShip);
		ShippingInfo.setShipmentList(shipmentList);
		order.setShippingInfo(ShippingInfo);

		BillingInfo BillingInfo = new BillingInfo();
		BillingInfo.setFirstName(rv.get("ship_name").toString());
		BillingInfo.setCountryCode("CN");
		BillingInfo.setCity(rv.get("ship_city").toString());
		BillingInfo.setAddressLine1(rv.get("ship_address").toString());
		BillingInfo.setAddressLine2(rv.get("ship_address2").toString());
		BillingInfo.setPhoneNumberDay(rv.get("ship_phone").toString());
		BillingInfo.setPhoneNumberEvening(rv.get("ship_phone").toString());
		BillingInfo.setPostalCode(rv.get("ship_zip").toString());
		BillingInfo.setRegion(rv.get("ship_state").toString());
		BillingInfo.setRegionDescription(rv.get("ship_state").toString());
		order.setBillingInfo(BillingInfo);

		OrderCart shoppingCart = new OrderCart();
		// If creating a new cart then the value should be 0
		shoppingCart.setCartID(0);
		// jewelry特殊处理
		if(this.channelId.equalsIgnoreCase(ChannelConfigEnums.Channel.JEWELRY.getId())){
			shoppingCart.setCheckoutSource("Unspecified");
		}else{
			shoppingCart.setCheckoutSource("Site_Checkout");
		}

		List<Map<String, Object>> dtItems=new ArrayList<Map<String,Object>>();
		// BHFO 的CMS 在旧系统所以许要调WEB_service
		if(this.channelId.equalsIgnoreCase(ChannelConfigEnums.Channel.BHFO.getId())){
			StringBuffer buf=new StringBuffer();
			for (Entry<String, Integer> de : productQuantity.entrySet()) {
				buf.append(de.getKey().toString() + ";");
			}
			buf.substring(0, buf.length()-1);
			
			dtItems = postOrderService.getReservationInfo(Properties
					.readValue("PostOrder_BHFO_Get_BHFOSKU_URL") + buf.substring(0, buf.length()-1).toString());
		}
		ArrayOfOrderLineItemItem orderlineList = new ArrayOfOrderLineItemItem();
		Set<Entry<String, Integer>> sets = productQuantity.entrySet();
		for (Entry<String, Integer> de : sets) {
			Integer quantity = de.getValue();
			String SKU = de.getKey().toString();
			OrderLineItemItem orderLine = new OrderLineItemItem();
			if(this.channelId.equalsIgnoreCase(ChannelConfigEnums.Channel.JEWELRY.getId())){
				orderLine.setSKU(SKU);
			}else{
				orderLine.setSKU(search_BHFO_SKU(SKU, dtItems));
			}
			orderLine.setLineItemID(MaxLineItemID++);
			orderLine.setAllowNegativeQuantity(true);
			orderLine.setQuantity(quantity);
			orderLine.setUnitPrice(new BigDecimal(productPrice.get(SKU)).setScale(2, BigDecimal.ROUND_HALF_UP));
			orderLine.setTaxCost(new BigDecimal(0));
			orderLine.setShippingCost(new BigDecimal(0));
			orderLine.setShippingTaxCost(new BigDecimal(0));
			orderLine.setRecyclingFee(new BigDecimal(0));
			orderLine.setGiftWrapCost(new BigDecimal(0));
			orderLine.setGiftWrapTaxCost(new BigDecimal(0));
			// jewelry特殊处理
			if(this.channelId.equalsIgnoreCase(ChannelConfigEnums.Channel.JEWELRY.getId())){
				orderLine.setItemSaleSource("DIRECT_SALE");
			}else{
				orderLine.setItemSaleSource("TMALL_GLOBAL");
			}
			orderlineList.getOrderLineItemItem().add(orderLine);
		}

		shoppingCart.setLineItemSKUList(orderlineList);
		Double dPromoPrice = Double.valueOf(rv.get("revised_discount").toString());
		if (dPromoPrice >= -0.001 || dPromoPrice <= 0.001) {
			ArrayOfOrderLineItemPromo arrayOfOrderLineItemPromo = new ArrayOfOrderLineItemPromo();
			OrderLineItemPromo orderPromo = new OrderLineItemPromo();
			orderPromo.setLineItemType("AdditionalCostOrDiscount");
			orderPromo.setPromoCode(BHFOPromoCode);
			orderPromo.setUnitPrice(new BigDecimal(dPromoPrice / dbRate).setScale(2, BigDecimal.ROUND_HALF_UP));
			arrayOfOrderLineItemPromo.getOrderLineItemPromo().add(orderPromo);
			shoppingCart.setLineItemPromoList(arrayOfOrderLineItemPromo);
		}
		ArrayOfOrderLineItemInvoice arrayOfOrderLineItemInvoice = new ArrayOfOrderLineItemInvoice();

		// Order Shipping Cost Information
		OrderLineItemInvoice orderInvoice = new OrderLineItemInvoice();
		orderInvoice.setLineItemType("Shipping");
		orderInvoice.setUnitPrice(new BigDecimal(dbShipping));
		arrayOfOrderLineItemInvoice.getOrderLineItemInvoice().add(orderInvoice);
		shoppingCart.setLineItemInvoiceList(arrayOfOrderLineItemInvoice);
		order.setShoppingCart(shoppingCart);

		
		OrderStatus orderStatus = new OrderStatus();
		if(this.channelId.equalsIgnoreCase(ChannelConfigEnums.Channel.JEWELRY.getId())){
			orderStatus.setCheckoutStatus("NotVisited");
			orderStatus.setShippingStatus("Unshipped");
			orderStatus.setPaymentStatus("NotSubmitted");
			order.setOrderStatus(orderStatus);
		}else{
			order.setOrderStatus(orderStatus);
		}
		
		
		soapenv soap = new soapenv(aPICredentials, new SubmitOrder(AccountID, order));
		String soapxml = JaxbUtil.convertToXml(soap, "UTF-8");
		return soapxml;
	}

	@Override
	public String postOrder(String postData) throws Exception {
		response = CommonUtil.SOAPHttp(MAPPING_BHFO_API_URL, WEBSERVICE_NAMESPACE + WEBSERVICE_SUBMIT, postData);
		return response;
	}

	@Override
	public void updateData() throws Exception {
		DocumentBuilder db = factory.newDocumentBuilder();
		Document xmldoc = db.parse(new InputSource(new ByteArrayInputStream(response.getBytes("UTF-8"))));
		Element root = xmldoc.getDocumentElement();
		NodeList nodeList = JaxbUtil.selectNodes("Body/SubmitOrderResponse/SubmitOrderResult/ResultData", root);
		if (nodeList.getLength() > 0 && !nodeList.item(0).getTextContent().equals("0")) {
			postOrderService.updateOrder(nodeList.item(0).getTextContent(), rv.get("OrderNumber").toString(), taskName);
		} else {
			throw new Exception("BHFO 返回失败\n" + response);
		}

	}

	private String search_BHFO_SKU(String SKU, List<Map<String, Object>> BHFOSKU) throws Exception {

		for (Map<String, Object> map : BHFOSKU) {
			if (SKU.equalsIgnoreCase(map.get("SKU").toString())) {
				return map.get("BHFOSKU").toString();
			}
		}
		throw new Exception(SKU + " Didn't find bhfo sku");
	}

	private XMLGregorianCalendar calendarToXML(Calendar calendar) {
		XMLGregorianCalendar cal = new XMLGregorianCalendarImpl();
		cal.setYear(calendar.get(Calendar.YEAR));
		cal.setMonth(calendar.get(Calendar.MONTH) + 1);
		cal.setDay(calendar.get(Calendar.DAY_OF_MONTH));
		cal.setHour(calendar.get(Calendar.HOUR_OF_DAY));
		cal.setMinute(calendar.get(Calendar.MINUTE));
		cal.setSecond(calendar.get(Calendar.SECOND));
		cal.setMillisecond(calendar.get(Calendar.MILLISECOND));
		cal.setTimezone(calendar.get(Calendar.ZONE_OFFSET) / 60000);
		return cal;
	}

	@Override
	public void init(String taskName, String channelId) {
		Password = ThirdPartyConfigs.getVal1(channelId, "ca_password");
		DeveloperKey = ThirdPartyConfigs.getVal1(channelId, "ca_developer_key");
		AccountID = ThirdPartyConfigs.getVal1(channelId, "ca_account_id");
		OrderEmail = ThirdPartyConfigs.getVal1(channelId, "ca_order_email");
		BHFOPromoCode = ThirdPartyConfigs.getVal1(channelId, "ca_promo_code");
		MAPPING_BHFO_API_URL = ThirdPartyConfigs.getVal1(channelId, "ca_url_api_order");
		WEBSERVICE_NAMESPACE = ThirdPartyConfigs.getVal1(channelId, "ca_url_namespace");
		this.taskName = taskName;
		this.channelId = channelId;
		// 汇率
		this.dbRate = 1.0;
	}

	@Override
	public void reservationUpdate() throws Exception{
		// TODO Auto-generated method stub
		
	}
}
