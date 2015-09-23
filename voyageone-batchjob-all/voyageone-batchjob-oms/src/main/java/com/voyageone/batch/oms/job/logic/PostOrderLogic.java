package com.voyageone.batch.oms.job.logic;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.mail.MessagingException;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.voyageone.batch.oms.service.PostOrderService;
import com.voyageone.batch.oms.utils.CommonUtil;
import com.voyageone.common.components.issueLog.IssueLog;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Properties;
import com.voyageone.common.mail.Mail;
import com.voyageone.common.util.JsonUtil;

public abstract class PostOrderLogic {

	@Autowired
	PostOrderService postOrderService;

    @Autowired
    protected IssueLog issueLog;
    
	protected Log logger;

	// shipping fee
	protected Double dbShipping = 0.0;
	// discount per SKU
	protected HashMap<String, Double> productDiscount = new HashMap<String, Double>();
	// price per SKU
	protected HashMap<String, Double> productPrice = new HashMap<String, Double>();
	// SKU Quantity
	protected HashMap<String, Integer> productQuantity = new HashMap<String, Integer>();
	// discount by order
	protected Double orderDiscount = 0.0;
	// product price total
	protected Double orderProductTotal = 0.0;
	// order discount total
	protected Double orderDiscountTotal = 0.0;
	// 对美元的汇率
	protected Double dbRate;

	protected DocumentBuilderFactory factory;

	protected Map<String, Object> rv;

	protected String taskName;
	
	protected String channelId;
	
	

	/**
	 * 初始化函数
	 * 
	 * @param taskName
	 */
	abstract public void init(String taskName, String channelId);

	/**
	 * 获取需要推送的数据
	 * 
	 * @return
	 * @throws Exception
	 */
	abstract String getPostdata() throws Exception;

	/**
	 * 把订单信息发给客户
	 * 
	 * @param postData
	 * @return
	 * @throws Exception
	 */
	abstract String postOrder(String postData) throws Exception;

	/**
	 * 客服分配的订单ID更新到数据库中
	 * 
	 * @throws Exception
	 */
	abstract void updateData() throws Exception;

	abstract void reservationUpdate() throws Exception;
	/**
	 * 
	 * @param rv
	 *            数据
	 * @param dbRate
	 *            汇率
	 */
	public PostOrderLogic() {
		logger = LogFactory.getLog(this.getClass());
		factory = DocumentBuilderFactory.newInstance();
		factory.setIgnoringElementContentWhitespace(true);
	}

	public void run(Map<String, Object> rv) {
		try {
			this.rv = rv;
			String OrderNumber = rv.get("OrderNumber").toString();
			// 数据 和价格进行计算
			calculate(OrderNumber);
			// 发送的数据取得
			String postXML = getPostdata();
			if(postXML == null){
				return;
			}
			// post的xml备份
			backupTheXmlFile(OrderNumber, postXML, 0);
			// post
			String response = postOrder(postXML);
			// response的xml备份
			backupTheXmlFile(OrderNumber, response, 1);
			// 查询结果更新到数据库中
			updateData();
			// 更新RsvJuicyOrderNumber
			reservationUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
			issueLog.log(e,ErrorType.BatchJob, SubSystem.OMS);
		}

	}

	/**
	 * 计算 单价 运费 折扣
	 * 
	 * @param orderNumber
	 */
	protected void calculate(String orderNumber) {
		// Get Item Details include Discount/Shipping Info
		List<Map<String, Object>> dtOrderDetails = postOrderService.getOrderItems(orderNumber);
		// shipping fee
		dbShipping = 0.0;
		// discount per SKU
		productDiscount = new HashMap<String, Double>();
		// price per SKU
		productPrice = new HashMap<String, Double>();
		// SKU Quantity
		productQuantity = new HashMap<String, Integer>();
		// discount by order
		orderDiscount = 0.0;
		// product price total
		orderProductTotal = 0.0;
		// order discount total
		orderDiscountTotal = 0.0;

		if (dtOrderDetails.size() > 0) {
			for (Map<String, Object> item : dtOrderDetails) {
				// process Shipping Fee
				if ("shipping".equals(item.get("SKU").toString().toLowerCase())) {
					Double dbTemp = Double.valueOf(item.get("PricePerUnit").toString());
					// get USD Shipping Fee
					dbShipping = dbShipping + round(dbTemp / dbRate);

				} else if ("discount".equals(item.get("SKU").toString().toLowerCase())) {
					// process Discount Fee
					String discountName = item.get("Product").toString();
					String[] splitName = discountName.split("[$]");
					Double discount = Double.valueOf(item.get("PricePerUnit").toString());
					// get USD Discount
					discount = round((Math.abs(discount) / dbRate));
					// when Product Discount
					if (splitName.length > 1) {
						if(!productDiscount.containsKey(splitName[1]))
						{
							productDiscount.put(splitName[1], discount);
						}else{
							discount += productDiscount.get(splitName[1]);
							productDiscount.put(splitName[1], discount);
						}
					} else {
						orderDiscount = orderDiscount + discount;
					}
				} else {
					// Product Fee
					Double price = Double.valueOf(item.get("PricePerUnit").toString());
					Integer quantity = Integer.valueOf(item.get("QuantityOrdered").toString());
					price = round(price / dbRate);
					orderProductTotal = orderProductTotal + round(price * quantity);
					if (!productPrice.containsKey(item.get("SKU").toString())) {
						productPrice.put(item.get("SKU").toString(), price);
					}
					if (!productQuantity.containsKey(item.get("SKU").toString())) {
						productQuantity.put(item.get("SKU").toString(), quantity);
					} else {
						quantity += productQuantity.get(item.get("SKU").toString());
						productQuantity.put(item.get("SKU").toString(), quantity);
					}

				}
			}
		}
		// when exist the Quantity of SKU is > 1, calculate the
		// discount of per SKU
		Set<Entry<String, Integer>> sets = productQuantity.entrySet();
		for (Entry<String, Integer> de : sets) {
			Integer quantity = de.getValue();
			String SKU = de.getKey().toString();
			if (quantity > 1 && productDiscount.containsKey(SKU)) {
				Double discount = Double.valueOf(productDiscount.get(SKU).toString());
				Double perDiscount = round(discount / quantity);
				productDiscount.remove(SKU);
				productDiscount.put(SKU, perDiscount);
			}
		}
		// split the discount for all to every product
		if (orderDiscount > 0 && orderProductTotal > 0) {
			for (Entry<String, Double> de : productPrice.entrySet()) {
				String sku = de.getKey().toString();
				Double skuPrice = Double.valueOf((de.getValue().toString()));
				Integer quantity = productQuantity.get(sku);
				Double percentDiscount = round((skuPrice * quantity / orderProductTotal) * orderDiscount);
				if (productDiscount.containsKey(sku)) {
					Double discount = Double.valueOf(productDiscount.get(sku)) + percentDiscount;
					productDiscount.remove(sku);
					productDiscount.put(sku, discount);
				} else {
					productDiscount.put(sku, percentDiscount);
				}
			}
		}

	}

	/**
	 * 四舍五入 保留小数点2位
	 * 
	 * @param value
	 * @return
	 */
	public double round(double value) {
		BigDecimal bg = new BigDecimal(value);
		return bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	public void reservationUpdate(String orderNumber, String clientOrderId, String channelId) throws Exception{
		Map<String,String> postdate = new HashMap<String, String>();
		postdate.put("OrderNumber", orderNumber);
		postdate.put("ClientOrderId", clientOrderId);
		postdate.put("channelId", channelId);
		String json=JsonUtil.getJsonString(postdate);
		String postUrl = Properties.readValue("PostOrder_ReservationUpdate");
		String response = CommonUtil.HttpPost(json, "UTF-8", postUrl);
		if(!("true"+"\n").equalsIgnoreCase(response)){
			throw new Exception("ReservationUpdate fail orderNumber="+orderNumber);
		}
		
	}
	/**
	 * 
	 * @param strXML
	 * @param type
	 */
	private void backupTheXmlFile(String orderNumber, String strXML, int type) {

		String strFolder = Properties.readValue("PostBackup") + File.separator + this.getClass().getName();
		File file = new File(strFolder);
		if (!file.exists() && !file.isDirectory()) {
			file.mkdirs();
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmssSSS");
		java.util.Date date = new java.util.Date();
		String fileName = orderNumber + "_" + sdf.format(date);
		FileWriter fs = null;
		try {
			if (type == 0) {
				fs = new FileWriter(strFolder + File.separator + "post_onestop_" + fileName + ".xml");
			} else {
				fs = new FileWriter(strFolder + File.separator + "ret_onestop_" + fileName + ".xml");
			}
			fs.write(strXML);
			fs.flush();
		} catch (Exception ex) {
			logger.error(ex.toString());
			issueLog.log(ex,ErrorType.BatchJob, SubSystem.OMS);
		} finally {
			try {
				fs.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				issueLog.log(e,ErrorType.BatchJob, SubSystem.OMS);
			}
		}
	}
}
