package com.voyageone.batch.oms.service;

import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.core.Enums.TaskControlEnums;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.core.util.TaskControlUtils;
import com.voyageone.batch.oms.dao.OrderDao;
import com.voyageone.batch.oms.formbean.OutFormReservationInfo;
import com.voyageone.common.components.issueLog.IssueLog;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Properties;
import com.voyageone.common.util.JsonUtil;
import com.voyageone.common.util.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Simple to Introduction
 * @Package:      [com.voyageone.batch.oms.service]
 * @ClassName:    [ReservationInfoTransferService]
 * @Description:  [发送分配仓库需要的订单信息给C#，接收返回的信息回写oms_bt_order_details表]
 * @Author:       [sky]
 * @CreateDate:   [20150415]
 * @UpdateUser:   [${user}]
 * @UpdateDate:   [${date} ${time}]
 * @UpdateRemark: [说明本次修改内容]
 * @Version:      [v1.0]
 */
@Service
public class ReservationInfoTransferService extends BaseTaskService {

	private static Log logger = LogFactory.getLog(ReservationInfoTransferService.class);
	@Autowired
	OrderDao orderDao;
	@Autowired
	IssueLog issueLog;

	@Override
	public SubSystem getSubSystem() {
		return SubSystem.OMS;
	}

	@Override
	public String getTaskName() {
		return "reservationInfoTransferJob";
	}

	/**
	 * Description reservation信息交互
	 * @param
	 * @return
	 * @throws MessagingException
	 * @create 20150414
	 */
	@Override
	protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {
		try {

			// 允许运行的订单渠道取得
			List<String> orderChannelIdList = TaskControlUtils.getVal1List(taskControlList,TaskControlEnums.Name.order_channel_id);

			// 抽出件数
			String row_count = TaskControlUtils.getVal1(taskControlList,TaskControlEnums.Name.row_count);

			int intRowCount = 1;
			if (!StringUtils.isNullOrBlank2(row_count)) {
				intRowCount = Integer.valueOf(row_count);
			}

			// 获取配置文件keyValue.properties的WS信息

			String nameSpace = Properties.readValue("OMS_WS_RESERVATION_NAMESPACE");
			String method = Properties.readValue("OMS_WS_RESERVATION_METHOD");

			Map<String, String> paramMap = new HashMap<String, String>();
			for (String strChannelId : orderChannelIdList) {
				List<OutFormReservationInfo> reservationList = orderDao.getReservationInfo(strChannelId,intRowCount);
				logger.info("渠道ID为[" +strChannelId + "]需要处理的订单件数："+ reservationList.size());
				if (reservationList != null && reservationList.size() != 0) {
					paramMap.put("channel", strChannelId);
					paramMap.put("order_json", JsonUtil.getJsonString(reservationList));
					// 渠道连接webservice的UR取得
					String endpoint = TaskControlUtils.getVal2(taskControlList,TaskControlEnums.Name.order_channel_id,strChannelId);
					String reservationId = callWS(endpoint, nameSpace, method, paramMap);
					logger.info("****************reservationId**************** : " + reservationId);
					List<Map<String, Object>> reservationIdList = JsonUtil.jsonToMapList(reservationId);
					if (reservationIdList != null && reservationIdList.size() != 0) {
						setReservationIdToOrderDetail(reservationIdList);
					}
				} else {
					logger.info("渠道ID为[" +strChannelId + "]的渠道无需要reservation的订单数据！");
				}
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			issueLog.log("ReservationInfoTransferJob","库存分配失败：" + ex.getMessage(),ErrorType.BatchJob, SubSystem.OMS);
		}
	}

	/**
	 * Description 将C#程序返回的ReservationID写到oms_bt_order_details表的对应记录
	 * @param  reservationId
	 * @return
	 * @create 20150414
	 */
	private void setReservationIdToOrderDetail(List<Map<String, Object>> reservationId) {
		StringBuffer sb = new StringBuffer();
		for (Map<String, Object> map : reservationId) {
			sb = sb.append((String) map.get("order_number"))
					.append(" order_number, ")
					.append((String) map.get("item_number"))
					.append(" item_number, ")
					.append((String) map.get("rsv_id"))
					.append(" rsv_id union all select ");
		}
		String sql = " select " + sb.substring(0, sb.lastIndexOf("union"));
		orderDao.setReservationId(sql);
	}

	/**
	 * Description 调用C#WebService推送Order数据
	 * @param endpoint WebService的URL
	 * @param nameSpace 命名空间
	 * @param method 调用方法名
	 * @param paramMap 方法参数
	 * @return String reservationId
	 * @throws ServiceException
	 * @throws MalformedURLException
	 * @throws RemoteException
	 * @create 20150414
	 */
	private String callWS(String endpoint, String nameSpace, String method, Map<String, String> paramMap) throws ServiceException, MalformedURLException, RemoteException {
		String result;
		org.apache.axis.client.Service service = new org.apache.axis.client.Service();
		org.apache.axis.client.Call call = (org.apache.axis.client.Call) service.createCall();
		call.setTimeout(20000);
		call.setTargetEndpointAddress(new java.net.URL(endpoint));
		call.setOperationName(new QName(nameSpace, method));
		Set<Entry<String, String>> set = paramMap.entrySet();
		Object paramObj[] = new Object[paramMap.size()];
		int i = 0;
		for (Entry<String, String> p : set) {
			String paramName = p.getKey();
			paramObj[i] = p.getValue();
			i++;
			call.addParameter(new QName(nameSpace, paramName),
					org.apache.axis.encoding.XMLType.XSD_STRING,
					javax.xml.rpc.ParameterMode.IN);
		}
		call.setReturnType(org.apache.axis.encoding.XMLType.XSD_STRING);
		call.setUseSOAPAction(true);
		call.setSOAPActionURI(nameSpace + method);
		result = (String) call.invoke(paramObj);
		return result;
	}

}
