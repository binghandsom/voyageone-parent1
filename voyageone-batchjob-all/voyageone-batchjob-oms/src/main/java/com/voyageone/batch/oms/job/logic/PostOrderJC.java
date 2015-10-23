package com.voyageone.batch.oms.job.logic;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.voyageone.batch.core.util.JaxbUtil;
import com.voyageone.batch.oms.bean.OrderBean;
import com.voyageone.batch.oms.bean.OrderItemBean;
import com.voyageone.batch.oms.bean.PaymentBean;
import com.voyageone.batch.oms.bean.TotalsBean;
import com.voyageone.batch.oms.utils.CommonUtil;
import com.voyageone.common.configs.Properties;
import com.voyageone.common.configs.ThirdPartyConfigs;
import com.voyageone.common.configs.beans.ThirdPartyConfigBean;

public class PostOrderJC extends PostOrderLogic {

	private String postUrl;
	private String response;
	private String clientOrderId;
	private List<String> warningOrderNumber = new ArrayList<String>(); // 存放一下异常订单
																		// （sizeID存在的订单）

	@Override
	public String getPostdata() throws Exception {
		OrderBean order = new OrderBean();
		order.setOrderNumber(rv.get("SourceOrderID").toString());
		order.setOrderDate(rv.get("OrderDate").toString());
		// Get SKU/UPC Info

		// 从Reservation库中取得Barcode和ColorId
		StringBuffer buf=new StringBuffer();
		for (Map.Entry<String, Integer> de : productQuantity.entrySet()) {
			buf.append(de.getKey().toString() + ";");
		}
		buf.substring(0, buf.length()-1);
		List<Map<String, Object>> dtItems = postOrderService.getReservationInfo(Properties.readValue("PostOrder_JC_GetBarcodeAndSizeURL") +  buf.substring(0, buf.length()-1).toString());
		List<OrderItemBean> orderItems = new ArrayList<OrderItemBean>();
		if (dtItems.size() > 0 && dtItems.size() == productQuantity.size()) {
            // 把SKU大小写统一一下
            skuConduct(dtItems);
			for (Map<String, Object> item : dtItems) {
				String SKU = item.get("SKU").toString();
                int quantity = productQuantity.get(SKU);
                for (int i=0;i<quantity;i++) {
					OrderItemBean orderItem = new OrderItemBean();
					orderItem.setUpc(item.get("Barcode").toString());
					String[] splits = SKU.split("-");
					orderItem.setProductId(splits[0]);
					orderItem.setColorId(splits[1]);
					// transfer Tmall size to Juicy sizeId by
					if (StringUtils.isEmpty(item.get("sizeId").toString())) {
							throw new Exception("sizeId不存在   OrderNumber=" + rv.get("OrderNumber").toString() + "  SKU=" + item.get("SKU").toString());
					}
					orderItem.setSizeId(item.get("sizeId").toString());
					orderItem.setItemCost(productPrice.get(SKU).toString());
					Double tempDiscount = 0.0;
					// <itemDiscount> is set only when discount
					// exists
					if (productDiscount.containsKey(SKU)) {
						orderItem.setItemDiscount(productDiscount.get(SKU).toString());
						tempDiscount = Double.valueOf(productDiscount.get(SKU));
						orderDiscountTotal = orderDiscountTotal + tempDiscount;
					}
					orderItem.setItemTotal(round((Double.valueOf(productPrice.get(SKU)) - tempDiscount)));
					orderItem.setItemTax(0);
					orderItems.add(orderItem);
				}
			}
		} else {
			throw new Exception("cms数据不整合："+rv.get("SourceOrderID").toString());
		}
		order.setOrderItems(orderItems);
		TotalsBean totals = new TotalsBean();
		totals.setGrandTotal(round(orderProductTotal + dbShipping - orderDiscountTotal));
		totals.setSubTotal(round(orderProductTotal));
		totals.setShippingTotal(round(dbShipping));
		totals.setItemDiscountTotal(round(orderDiscountTotal));
		order.setTotals(totals);
		order.setPayment(new PaymentBean());

		String postXML = JaxbUtil.convertToXml(order);
		return postXML;
	}

	@Override
	public String postOrder(String data) throws Exception {
		response = CommonUtil.HttpPost(data, "UTF-8", postUrl);
		return response;
	}

	@Override
	public void updateData() throws Exception {
		DocumentBuilder db = factory.newDocumentBuilder();
		Document xmldoc = db.parse(new InputSource(new ByteArrayInputStream(response.getBytes("UTF-8"))));
		Element root = xmldoc.getDocumentElement();
		NodeList nodeList = JaxbUtil.selectNodes("Order/OrderNumber", root);
		if (nodeList.getLength() > 0) {
			postOrderService.updateOrder(nodeList.item(0).getTextContent(), rv.get("OrderNumber").toString(), taskName);
			clientOrderId = nodeList.item(0).getTextContent();
		} else {
			throw new Exception("JC订单号取得失败   OrderNumber=" + rv.get("SourceOrderID").toString() + "  response=" + response);
		}
	}

	@Override
	public void init(String taskName, String channelId) {
		postUrl = ThirdPartyConfigs.getVal1("004", "con_url_postOrder") + ThirdPartyConfigs.getVal1("004", "appkey");
		this.taskName = taskName;
		this.channelId = channelId;
		// 汇率
		this.dbRate = Double.valueOf(Properties.readValue("ExchangeRate"));
		this.response = "";
		this.clientOrderId = "";

	}

    private void skuConduct(List<Map<String, Object>> dtItems) throws Exception {

        dtItems.forEach(stringObjectMap -> {
            for (Map.Entry<String, Integer> de : productQuantity.entrySet()) {
                if (de.getKey().equalsIgnoreCase(stringObjectMap.get("SKU").toString())){
                    stringObjectMap.put("SKU", de.getKey());
                    break;
                }
            }
        });
    }
	@Override
	public void reservationUpdate() throws Exception {
		reservationUpdate(rv.get("OrderNumber").toString(), clientOrderId, "004");
	}

}
