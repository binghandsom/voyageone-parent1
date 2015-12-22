package com.voyageone.batch.oms.job;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.voyageone.batch.core.Enums.TaskControlEnums;
import com.voyageone.batch.core.dao.TaskDao;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.core.util.JaxbUtil;
import com.voyageone.batch.core.util.TaskControlUtils;
import com.voyageone.batch.oms.emum.MagentoType;
import com.voyageone.batch.oms.formbean.MagentoCouponBean;
import com.voyageone.batch.oms.formbean.MagentoOrderBean;
import com.voyageone.batch.oms.formbean.MagentoTradeBean;
import com.voyageone.batch.oms.modelbean.TaobaoOrderBean;
import com.voyageone.batch.oms.modelbean.TaobaoPromotionBean;
import com.voyageone.batch.oms.modelbean.TaobaoTradeBean;
import com.voyageone.batch.oms.service.OrderMagentoImportService;
import com.voyageone.batch.oms.utils.CommonUtil;
import com.voyageone.common.components.issueLog.IssueLog;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Properties;
import com.voyageone.common.configs.ShopConfigs;
import com.voyageone.common.configs.beans.ShopBean;

/**
 * 取独立域名的订单信息定时任务
 * @author James
 *
 */
public class NewOrderMagentoImportJob {
	private Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	TaskDao taskDao;

	@Autowired
	OrderMagentoImportService orderMagentoImportService;

	@Autowired
	IssueLog issueLog;
	
	private String taskCheck;

	private MagentoType magentoType;

	/**
	 * @return the magentoType1
	 */
	public MagentoType getMagentoType() {
		return magentoType;
	}

	/**
	 * @param magentoType1
	 *            the magentoType1 to set
	 */
	public void setMagentoType(MagentoType magentoType) {
		this.magentoType = magentoType;
	}

	/**
	 * @return the taskCheck
	 */
	public String getTaskCheck() {
		return taskCheck;
	}

	/**
	 * @param taskCheck
	 *            the taskCheck to se t
	 */
	public void setTaskCheck(String taskCheck) {
		this.taskCheck = taskCheck;
	}

	private DocumentBuilderFactory factory;

	public void CreateMagentoOrder() {

		List<TaskControlBean> taskControlList = taskDao.getTaskControlList(taskCheck);

		// 是否可以运行的判断
		if (TaskControlUtils.isRunnable(taskControlList, taskCheck) == false) {
			return;
		}
		logger.info(taskCheck + "任务开始");
		String taskID = TaskControlUtils.getTaskId(taskControlList);
		// 任务监控历史记录添加:启动
		taskDao.insertTaskHistory(taskID, TaskControlEnums.Status.START.getIs());
		try {
			factory = DocumentBuilderFactory.newInstance();
			factory.setIgnoringElementContentWhitespace(true);

			// 取得上次一运行的时间
			String strLastOrderDate = taskDao.getLastRunTime(taskID);
			// 取得独立域名的URL
			String postUrl = GetCNMagentoOrderURL(magentoType, strLastOrderDate);

			// https
			System.setProperty("javax.net.ssl.trustStore", "/opt/app-shared/voyageone_web/contents/other/third_party/com/trustStore/trustStore");
			logger.info(postUrl);

			// 取得独立域名的订单信息
			String response = CommonUtil.HttpPost("", "UTF-8", postUrl);
			// String response =
			// "<?xml version=\"1.0\" encoding=\"UTF-8\"?><SETIOrders><Response><ResponseCode>1</ResponseCode><ResponseDescription>success</ResponseDescription></Response><Order><OrderNumber>100003111</OrderNumber><OrderDate>08-May-2015 10:32:40 AM</OrderDate><Billing><FullName>常景苏 </FullName><Company></Company><Email>877733222@qq.com</Email><Phone>15263819689</Phone><Address><Street1>东营区</Street1><Street2>石油大学北门，路北往西走200米万家乐专卖店</Street2><City>东营市</City><State>山东省</State><Code>257000</Code><Country>CN</Country></Address></Billing><Shipping><FullName>常景苏 </FullName><Company></Company><Email></Email><Phone>15263819689</Phone><Address><Street1>东营区</Street1><Street2>石油大学北门，路北往西走200米万家乐专卖店</Street2><City>东营市</City><State>山东省</State><Code>257000</Code><Country>CN</Country></Address><Product><Name>NBA经典\"掌控\"室内室外篮球</Name><SKU>74-604y-OneSize</SKU><ItemPrice>220.0000</ItemPrice><Quantity>1.0000</Quantity><Weight></Weight><CustomerText></CustomerText><OrderOption><OptionName>篮球型号</OptionName><SelectedOption>7号球(标准球)</SelectedOption></OrderOption></Product></Shipping><Payment><Generic1><Name>Cnpay</Name><Description>网银在线 </Description><TradeNo>20150507000010000300</TradeNo></Generic1></Payment><Totals><ProductTotal>220.0000</ProductTotal><Tax><TaxAmount>0.0000</TaxAmount></Tax><GrandTotal>220.0000</GrandTotal><ShippingTotal><Total>0.0000</Total><Description>顺丰</Description></ShippingTotal></Totals><Other><IPHostName>61.156.217.67</IPHostName><TotalOrderWeight>0.0000</TotalOrderWeight><GiftMessage></GiftMessage><Comments>要黑色网兜发票类型: 普通发票  发票抬头: 个人 开票内容: 篮球</Comments></Other></Order></SETIOrders>";
			// xml转MagentoTradeBean
			List<MagentoTradeBean> MagentoTrades = GetTradeInfoFromXML(response, magentoType);
			if (MagentoTrades.size() > 0) {
				// MagentoTradeBean 转成 TaobaoTradeBean
				List<TaobaoTradeBean> taobaoTrades = MagentoToTaobao(MagentoTrades, magentoType);
				// 插入order临时表中
				orderMagentoImportService.importNewOrderToHistory(taobaoTrades, taskCheck);
			}

			taskDao.insertTaskHistory(taskID, TaskControlEnums.Status.SUCCESS.getIs());
			logger.info(taskCheck + "任务结束");
		} catch (Exception e) {
			logger.info(e.toString());
			issueLog.log(e, ErrorType.BatchJob, SubSystem.OMS);
			// 任务监控历史记录添加:结束
			taskDao.insertTaskHistory(taskID, TaskControlEnums.Status.ERROR.getIs());
			logger.info(taskCheck + "任务结束");
			return;
		}
	}

	public List<TaobaoTradeBean> MagentoToTaobao(List<MagentoTradeBean> MagentoTrades, MagentoType magentoType) {
		List<TaobaoTradeBean> ret = new ArrayList<TaobaoTradeBean>();
		for (MagentoTradeBean item : MagentoTrades) {
			TaobaoTradeBean data = new TaobaoTradeBean();
			// 订单编号
			data.setTid(item.getTid());
			// 购买人昵称
			data.setBuyer_nick(item.getBuyer_nick());
			// 成交时间
			data.setCreated(item.getCreatedTime());
			// 支付时间
			data.setPay_time(item.getCreatedTime());
			data.setModified(item.getCreatedTime());
			// 收件人名字
			data.setReceiver_name(item.getReceiver_name());
			// 收件地邮编
			data.setReceiver_zip(item.getReceiver_zip());
			// 收件地省份
			data.setReceiver_state(item.getReceiver_state());
			// 收件地城市
			data.setReceiver_city(item.getReceiver_city());
			// 收件地区
			data.setReceiver_district(item.getReceiver_district());
			// 收件地具体地址
			data.setReceiver_address(item.getReceiver_address());
			// 收件人手机
			data.setReceiver_mobile(item.getReceiver_mobile());
			// 收件人座机
			data.setReceiver_phone(item.getReceiver_mobile());
			// 买家电子邮件
			data.setBuyer_email(item.getBuyer_email());
			// 买家留言
			data.setBuyer_message(item.getOther_comments());
			// 支付宝交易编号
			data.setAlipay_no(item.getPayment_no());
			// 邮费
			data.setPost_fee(item.getShipping_fee());
			// 物流方式
			data.setShipping_type(item.getShipping_type());
			// 实付金额
			data.setPayment(item.getPay_fee());
			// 商品金额
			data.setTotal_fee(item.getTotal_fee());
			// 使用积分
			data.setPoint_fee(item.getPoint_fee());
			// 订单来源
			data.setTarget(magentoType.getTarget());
			// 订单信息XML
			data.setOrderXML("");
			// 订单信息
			List<TaobaoOrderBean> order = new ArrayList<TaobaoOrderBean>();
			for (MagentoOrderBean magentoOrder : item.getOrder()) {
				TaobaoOrderBean t = new TaobaoOrderBean();

				// 商家编码
				t.setOuter_sku_id(magentoOrder.getOuter_sku_id());
				// 数量
				t.setNum(magentoOrder.getNum());
				// 价格
				t.setPrice(magentoOrder.getPrice());
				// 商品名称
				t.setTitle(magentoOrder.getTitle());
				// 出货仓库：0-美国 0以外-中国
				t.setChina_stock(magentoOrder.getChina_stock());
				order.add(t);
			}
			data.setOrder(order);
			// 折扣活动信息
			List<TaobaoPromotionBean> promotion = new ArrayList<TaobaoPromotionBean>();
			for (MagentoCouponBean magentoCoupon : item.getCoupon()) {
				TaobaoPromotionBean p = new TaobaoPromotionBean();
				p.setName(magentoCoupon.getCoupon_name());
				p.setDiscount(BigDecimal.valueOf(Double.parseDouble(magentoCoupon.getCoupon_fee())));
				promotion.add(p);
			}
			data.setPromotion(promotion);
			// 支付方式
			data.setPaymentMethod(item.getPayment_name());
			// 买家支付宝账号
			// data.setbuyer_alipay_no;
			// 订单cartId
			data.setCartId(magentoType.getCartId());
			// 订单channelId
			data.setChannelId(magentoType.getChannelId());
			// 发票信息
			data.setInvoice_info(item.getInvoiceInfo());
			ret.add(data);
		}
		return ret;

	}

	/**
	 * Get CN Magento Order's URL.
	 * 
	 * @param magentoType
	 * @param strLastOrderDate
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String GetCNMagentoOrderURL(MagentoType magentoType, String strLastOrderDate)
			throws UnsupportedEncodingException {

		logger.info("获得中国 Magento取得订单URL开始。");
		ShopBean shopBean = ShopConfigs.getShop(magentoType.getChannelId(), magentoType.getCartId());
		String strGetMagnetoOrdeUrl = shopBean.getApp_url()+Properties.readValue(magentoType.getChannelName() + "_MAGENTO_GETORDER_URL")
				.toString();

		// Get CN Magento setifunction
		String strSetIFcuntion = Properties.readValue(magentoType.getChannelName() + "_MAGENTO_GETORDER_SETIFUNCTION")
				.toString();

		// Get CN Magento setiuser
		String strSetIUser = shopBean.getAppSecret();

		// Get CN Magento password
		String strPassword = shopBean.getSessionKey();

		// Get SystemDate of yesterday

		Object[] fmtargs = { strSetIFcuntion, strSetIUser, strPassword,
				java.net.URLEncoder.encode(strLastOrderDate, "utf-8") };

		String smsContent = MessageFormat.format(strGetMagnetoOrdeUrl, fmtargs);
		logger.info("中国Magento取订单URL：" + smsContent);
		logger.info("获得中国 Magento取得订单URL结束。");
		return smsContent;
	}

	/**
	 * Deal With CN.Magento's Order XML.
	 * 
	 * @param strXML
	 * @return
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws SAXException
	 * @throws UnsupportedEncodingException
	 */
	public List<MagentoTradeBean> GetTradeInfoFromXML(String strXML, MagentoType magentoType) throws Exception {

		List<MagentoTradeBean> result = new ArrayList<MagentoTradeBean>();

		logger.info("中国Magento订单XML From :" + strXML);
		DocumentBuilder db = factory.newDocumentBuilder();
		Document xmldoc = db.parse(new InputSource(new ByteArrayInputStream(strXML.getBytes("UTF-8"))));
		Element root = xmldoc.getDocumentElement();
		NodeList nResponseDescription = JaxbUtil.selectNodes("//SETIOrders/Response/ResponseDescription", root);

		// SAXReader saxReader = new SAXReader();
		// Document doc = saxReader.read(new StringReader(strXML));
		// Element root = doc.getRootElement();
		// root.at
		if (nResponseDescription == null || !"success".equals(nResponseDescription.item(0).getTextContent())) {
			logger.error("取得中国Magento订单失败。");
			return null;
		} else {
			NodeList orders = JaxbUtil.selectNodes("//SETIOrders/Order", root);
			logger.info("取得中国Magento的XML中,订单总件数：" + orders.getLength());
			//
			for (int i = 0; i < orders.getLength(); i++) {
				MagentoTradeBean trade = new MagentoTradeBean();
				Node eOrder = orders.item(i);
				String tid = JaxbUtil.selectNode("OrderNumber", eOrder).getTextContent();
				if (CheckTradIsExists(tid, magentoType)) {
					continue;
				}

				trade.setTid(tid);
				logger.info("从取得中国Magento的XML中，处理的订单编号：" + tid);
				// OrderDate
				trade.setCreatedTime(JaxbUtil.selectNode("OrderDate", eOrder).getTextContent());

				// Get Billing List Info
				NodeList lstBilling = JaxbUtil.selectNodes("Billing", eOrder);
				// Buyer_nick
				trade.setBuyer_nick(JaxbUtil.selectNode("FullName", lstBilling.item(0)).getTextContent());
				// buyer_company
				trade.setBuyer_company(JaxbUtil.selectNode("Company", lstBilling.item(0)).getTextContent());
				// buyer_phone
				// trade.setBuyer_phone(lstBilling.get(0).element("Phone").getText());
				trade.setBuyer_phone(JaxbUtil.selectNode("Company", lstBilling.item(0)).getTextContent());
				// buyer_email
				trade.setBuyer_email(JaxbUtil.selectNode("Email", lstBilling.item(0)).getTextContent());
				// Get Billing Address
				NodeList lstBillingAddress = JaxbUtil.selectNodes("Address", lstBilling.item(0));
				// buyer_address
				trade.setBuyer_district(JaxbUtil.selectNode("Street1", lstBillingAddress.item(0)).getTextContent());
				try{
					trade.setBuyer_address(JaxbUtil.selectNode("Street2", lstBillingAddress.item(0)).getTextContent());
				}catch(Exception e){
					trade.setBuyer_address("");
				}

				// buyer_city
				trade.setBuyer_city(JaxbUtil.selectNode("City", lstBillingAddress.item(0)).getTextContent());
				// buyer_state
				trade.setBuyer_state(JaxbUtil.selectNode("State", lstBillingAddress.item(0)).getTextContent());
				// buyer_zip
				trade.setBuyer_zip(JaxbUtil.selectNode("Code", lstBillingAddress.item(0)).getTextContent());
				// buyer_country
				trade.setBuyer_country(JaxbUtil.selectNode("Country", lstBillingAddress.item(0)).getTextContent());
				// Get Shipping List Info
				NodeList lstShipping = JaxbUtil.selectNodes("Shipping", eOrder);
				// receiver_name
				trade.setReceiver_name(JaxbUtil.selectNode("FullName", lstShipping.item(0)).getTextContent());
				// receiver_company
				trade.setReceiver_company(JaxbUtil.selectNode("Company", lstShipping.item(0)).getTextContent());
				// receiver_mobile
				trade.setReceiver_mobile(JaxbUtil.selectNode("Phone", lstShipping.item(0)).getTextContent());
				// receiver_email
				trade.setReceiver_email(JaxbUtil.selectNode("Email", lstShipping.item(0)).getTextContent());
				// Get Shipping Address
				NodeList lstShippingAddress = JaxbUtil.selectNodes("Address", lstShipping.item(0));
				// receiver_address
				trade.setReceiver_district(JaxbUtil.selectNode("Street1", lstShippingAddress.item(0)).getTextContent());
				try{
					trade.setReceiver_address(JaxbUtil.selectNode("Street2", lstShippingAddress.item(0)).getTextContent());
				}catch(Exception e){
					trade.setReceiver_address("");
				}
				// receiver_city
				trade.setReceiver_city(JaxbUtil.selectNode("City", lstShippingAddress.item(0)).getTextContent());
				// receiver_state
				trade.setReceiver_state(JaxbUtil.selectNode("State", lstShippingAddress.item(0)).getTextContent());
				// receiver_zip
				trade.setReceiver_zip(JaxbUtil.selectNode("Code", lstShippingAddress.item(0)).getTextContent());
				// receiver_country
				trade.setReceiver_country(JaxbUtil.selectNode("Country", lstShippingAddress.item(0)).getTextContent());
				// Get Products List
				NodeList products = JaxbUtil.selectNodes("Product", lstShipping.item(0));
				for (int j = 0; j < products.getLength(); j++) {
					MagentoOrderBean order = new MagentoOrderBean();
					Node eProduct = products.item(j);
					// title
					order.setTitle(JaxbUtil.selectNode("Name", eProduct).getTextContent());
					// outer_sku_id
					order.setOuter_sku_id(JaxbUtil.selectNode("SKU", eProduct).getTextContent());
					// price
					order.setPrice(JaxbUtil.selectNode("ItemPrice", eProduct).getTextContent());
					// num
					int Quantity = Float.valueOf(JaxbUtil.selectNode("Quantity", eProduct).getTextContent()).intValue();
					order.setNum(String.valueOf(Quantity));
					// size
					String[] ItemCodeAndSize = GetItemCodeAndSizeFromSKU(JaxbUtil.selectNode("SKU", eProduct)
							.getTextContent());
					if (ItemCodeAndSize != null && ItemCodeAndSize.length == 2) {
						order.setSize(ItemCodeAndSize[1]);
					} else {
						order.setSize("0");
					}
					// weight
					order.setWeight(GetWeight(JaxbUtil.selectNode("Weight", eProduct).getTextContent()));
					trade.getOrder().add(order);
				}
				//
				// Get Payment Info

				NodeList lstGeneric1 = JaxbUtil.selectNodes("Payment/Generic1", eOrder);
				// payment_name
				trade.setPayment_name(JaxbUtil.selectNode("Name", lstGeneric1.item(0)).getTextContent());
				// payment_no
				trade.setPayment_no(JaxbUtil.selectNode("TradeNo", lstGeneric1.item(0)).getTextContent());
				// payment_description
				trade.setPayment_description(JaxbUtil.selectNode("Description", lstGeneric1.item(0)).getTextContent());
				// payment_time
				trade.setPayment_time("");
				//
				// Get Totals List Info
				NodeList lstTotals = JaxbUtil.selectNodes("Totals", eOrder);
				// total_fee
				trade.setTotal_fee(JaxbUtil.selectNode("ProductTotal", lstTotals.item(0)).getTextContent());
				// pay_fee
				trade.setPay_fee(JaxbUtil.selectNode("GrandTotal", lstTotals.item(0)).getTextContent());
				// tax_fee
				trade.setTax_fee(GetPrice(JaxbUtil.selectNode("Tax/TaxAmount", lstTotals.item(0)).getTextContent()));
				// Get ShippingTotal List Info
				trade.setShipping_fee(GetPrice(JaxbUtil.selectNode("ShippingTotal/Total", lstTotals.item(0))
						.getTextContent()));
				// shipping_type
				trade.setShipping_type(JaxbUtil.selectNode("ShippingTotal/Description", lstTotals.item(0))
						.getTextContent());
				// Get Coupon List Info
				NodeList coupons = JaxbUtil.selectNodes("Coupon", eOrder);
				for (int j = 0; j < coupons.getLength(); j++) {
					MagentoCouponBean couponBean = new MagentoCouponBean();
					Node coupon = coupons.item(j);
					couponBean.setCoupon_name(JaxbUtil.selectNode("Name", coupon).getTextContent());
					// oupon_fee
					String strCouponFee = GetPrice(JaxbUtil.selectNode("Total", coupon).getTextContent());
					couponBean.setCoupon_fee(strCouponFee.substring(strCouponFee.indexOf("-") + 1));
					trade.getCoupon().add(couponBean);
				}
				// other_iphost
				trade.setOther_iphost(JaxbUtil.selectNode("Other/IPHostName", eOrder).getTextContent());
				// other_totalweight
				trade.setOther_totalweight(GetWeight(JaxbUtil.selectNode("Other/TotalOrderWeight", eOrder)
						.getTextContent()));
				// other_giftmessage
				trade.setOther_giftmessage(JaxbUtil.selectNode("Other/GiftMessage", eOrder).getTextContent());
				// other_comments
				trade.setOther_comments(JaxbUtil.selectNode("Other/Comments", eOrder).getTextContent());
				result.add(trade);
			}
		}
		return result;
	}

	/**
	 * Get Price With Format '0.00'.
	 * 
	 * @param strPrice
	 * @return
	 */
	public static String GetPrice(String strPrice) {

		DecimalFormat dFormat = new DecimalFormat("0.00");
		if (strPrice == null || strPrice.trim().length() == 0) {
			return "0.00";
		} else {
			return dFormat.format(Double.valueOf(strPrice));
		}
	}

	/**
	 * Get Weight With Format '0.00'
	 * 
	 * @param strWeight
	 * @return
	 */
	public static String GetWeight(String strWeight) {

		DecimalFormat dFormat = new DecimalFormat("0.00");
		if (strWeight == null || strWeight.trim().length() == 0) {
			return "0.00";
		} else {
			return dFormat.format(Double.valueOf(strWeight));
		}
	}

	/**
	 * Get ItemCode And Size From SKU.
	 * 
	 * @param strSKU
	 * @return
	 */
	public static String[] GetItemCodeAndSizeFromSKU(String strSKU) {

		String[] ItemCodeAndSize = new String[] { "", "" };

		if (strSKU == null || strSKU.trim().length() == 0) {
			return null;
		} else {
			int position = strSKU.lastIndexOf("-");
			ItemCodeAndSize[0] = strSKU.substring(0, position);
			ItemCodeAndSize[1] = strSKU.substring(position + 1);
		}

		return ItemCodeAndSize;
	}

	/**
	 * 该条tid是否已存在
	 * 
	 * @param tid
	 * @param magentoType
	 * @return true 存在 false 不存在
	 */
	public boolean CheckTradIsExists(String tid, MagentoType magentoType) {
		List<Map<String, Object>> data = orderMagentoImportService.getOrderInfoByTid(tid, magentoType.getCartId(),
				magentoType.getChannelId());
		if (data.size() > 0) {
			return true;
		} else {
			return false;
		}
	}
}
