package com.voyageone.batch.oms.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import com.voyageone.common.configs.Enums.CartEnums;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.core.util.JaxbUtil;
import com.voyageone.batch.oms.utils.CommonUtil;
import com.voyageone.common.components.channelAdvisor.bean.orders.PaymentInfo;
import com.voyageone.common.components.channelAdvisor.bean.orders.PaymentInfoUpdateSubmit;
import com.voyageone.common.components.channelAdvisor.soap.UpdateOrderListResponseSoap;
import com.voyageone.common.components.channelAdvisor.soap.UpdateOrderStatusSoap;
import com.voyageone.common.components.channelAdvisor.webservices.APICredentials;
import com.voyageone.common.components.channelAdvisor.webservices.ArrayOfOrderUpdateSubmit;
import com.voyageone.common.components.channelAdvisor.webservices.OrderStatusUpdateSubmit;
import com.voyageone.common.components.channelAdvisor.webservices.OrderUpdateSubmit;
import com.voyageone.common.components.channelAdvisor.webservices.UpdateOrderList;
import com.voyageone.common.components.issueLog.IssueLog;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Properties;
import com.voyageone.common.configs.ThirdPartyConfigs;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;

@Service
public class UpdateOrderStatusService extends BaseTaskService {

	private static Log logger = LogFactory.getLog(UpdateOrderStatusService.class);

	@Autowired
	private PostOrderService postOrderService;

	private String Password;
	private String DeveloperKey;
	private String AccountID;
	private String MAPPING_BHFO_API_URL;
	private String WEBSERVICE_NAMESPACE;
	private APICredentials aPICredentials;

	@Autowired
	protected IssueLog issueLog;

	private String taskName = "";

	@Override
	public SubSystem getSubSystem() {
		return SubSystem.OMS;
	}

	@Override
	public String getTaskName() {
		return taskName;
	}

	/**
	 * 漏单检查主函数
	 * 
	 * @throws Exception
	 */
	@Override
	protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {

		for (TaskControlBean taskControl : taskControlList) {
			if ("channel_id".equals(taskControl.getCfg_name())) {
				try {
					String channelId = taskControl.getCfg_val1();
					Password = ThirdPartyConfigs.getVal1(channelId, "ca_password");
					DeveloperKey = ThirdPartyConfigs.getVal1(channelId, "ca_developer_key");
					AccountID = ThirdPartyConfigs.getVal1(channelId, "ca_account_id");
					MAPPING_BHFO_API_URL = ThirdPartyConfigs.getVal1(channelId, "ca_url_api_order");
					WEBSERVICE_NAMESPACE = ThirdPartyConfigs.getVal1(channelId, "ca_url_namespace");
					sendCA(channelId);
				} catch (Exception e) {
					issueLog.log(e, ErrorType.BatchJob, SubSystem.OMS);
				}
			}
		}
	}

	public void sendCA(String channelId) throws Exception {
		aPICredentials = new APICredentials();
		aPICredentials.setDeveloperKey(DeveloperKey);
		aPICredentials.setPassword(Password);
		List<Map<String, Object>> orders = postOrderService.getApproveOrdersToChanneladvisor(channelId);

		for (Map<String, Object> order : orders) {
			// 生产要推送的XML
			String postXml = getXML(order,channelId);
			logger.info(postXml);
			// 备份
			backupTheXmlFile(order.get("OrderNumber").toString(), postXml, 0);

			// 推送
			String response = postOrder(postXml);
			logger.info(response);
			// 推送结果备份
			backupTheXmlFile(order.get("OrderNumber").toString(), response, 1);

			updateSendFlg(order.get("OrderNumber").toString(), response);

		}
	}

	public String postOrder(String postData) throws Exception {
		String response = CommonUtil.SOAPHttp(MAPPING_BHFO_API_URL, WEBSERVICE_NAMESPACE + "UpdateOrderList", postData);
		return response;
	}

	public void updateSendFlg(String orderNumber, String xml) {

		UpdateOrderListResponseSoap updateOrderListResponse = JaxbUtil.converyToJavaBean(xml, UpdateOrderListResponseSoap.class);
		if ("Success".equals(updateOrderListResponse.getBody().getUpdateOrderListResponse().getUpdateOrderListResult().getStatus().value())) {
			postOrderService.updateOrderSendflg(orderNumber);
		}
	}

	private String getXML(Map<String, Object> order, String channelId) {
		UpdateOrderList updateOrderList = new UpdateOrderList();
		updateOrderList.setAccountID(AccountID);

		OrderUpdateSubmit orderUpdateSubmit = new OrderUpdateSubmit();
		orderUpdateSubmit.setOrderID(Integer.parseInt(order.get("clientOrderId").toString()));

		OrderStatusUpdateSubmit orderStatusUpdateSubmit = new OrderStatusUpdateSubmit();
		orderStatusUpdateSubmit.setCheckoutPaymentStatus("CheckoutSamePaymentCleared");
		orderStatusUpdateSubmit.setShippingStatus("Unshipped");

		orderUpdateSubmit.setOrderStatusUpdate(orderStatusUpdateSubmit);
		
		if(channelId.equals(ChannelConfigEnums.Channel.JEWELRY.getId())){
		// paymentinfo 设定
			PaymentInfoUpdateSubmit paymentInfo =new PaymentInfoUpdateSubmit();
			if (CartEnums.Cart.JM.getId().equals(order.get("cartId").toString())) {
				paymentInfo.setPaymentType("Jumei");
			}else{
				paymentInfo.setPaymentType("AliPay");
			}
			paymentInfo.setPaymentTransactionID(order.get("payNo").toString());
			paymentInfo.setMerchantReferenceNumber(order.get("originSourceOrderId").toString());
			paymentInfo.setCreditCardLast4("");
			paymentInfo.setPayPalID("");
			orderUpdateSubmit.setPaymentInfo(paymentInfo);
		}
		
		ArrayOfOrderUpdateSubmit arrayOfOrderUpdateSubmit = new ArrayOfOrderUpdateSubmit();
		arrayOfOrderUpdateSubmit.getOrderUpdateSubmit().add(orderUpdateSubmit);
		updateOrderList.setUpdateOrderSubmitList(arrayOfOrderUpdateSubmit);
		

		UpdateOrderStatusSoap soap = new UpdateOrderStatusSoap(aPICredentials, updateOrderList);
		String soapxml = JaxbUtil.convertToXml(soap, "UTF-8");

		return soapxml;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

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
				fs = new FileWriter(strFolder + File.separator + "post_channeladvisor_" + fileName + ".xml");
			} else {
				fs = new FileWriter(strFolder + File.separator + "ret_channeladvisor_" + fileName + ".xml");
			}
			fs.write(strXML);
			fs.flush();
		} catch (Exception ex) {
			logger.error(ex.toString());
			issueLog.log(ex, ErrorType.BatchJob, SubSystem.OMS);
		} finally {
			try {
				fs.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				issueLog.log(e, ErrorType.BatchJob, SubSystem.OMS);
			}
		}
	}

}
