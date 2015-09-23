package com.voyageone.batch.oms.service;

import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyageone.batch.core.Constants;
import com.voyageone.batch.oms.OmsConstants;
import com.voyageone.batch.oms.dao.OrderDao;
import com.voyageone.common.mail.Mail;
@Service
public class SendSameSourceOrderIdMailService {
		private static Log logger = LogFactory.getLog(SendSameSourceOrderIdMailService.class);
		
		@Autowired
		private OrderDao orderDao;
		
		private final static String MSG = "OMS没有重复订单记录";
		
		public boolean sendSameSourceOrderIdMail() {
			
			boolean isSuccess= true;
			
			List<Map<String,Object>> listMap = orderDao.getSameSourceOrderId();
		
			
			if(listMap.size() == 0) {
				try {
					Mail.sendAlert("ITOMS", OmsConstants.SUBJECT_N, MSG);
					logger.info("邮件发送成功!");
					
				} catch (MessagingException e) {
					logger.info("邮件发送失败！" +e);
					isSuccess = false;
				}
			} else {
				StringBuilder tbody = new StringBuilder();
				
				for(Map<String,Object> map : listMap) {
					
					tbody = tbody.append(String.format(OmsConstants.SAME_ORDERMAIL_ROW, map.get("orderNumber"), map.get("sourceOrderId")));
				}
				//拼接table
				String body = String.format(OmsConstants.SAME_ORDERMAIL_TABLE, OmsConstants.SAME_ORDERMAIL_HEAD, tbody.toString());
				// 邮件正文
				StringBuilder emailContent = new StringBuilder();
				// 拼接邮件正文
				emailContent
						.append(Constants.EMAIL_STYLE_STRING)
						.append(body);
				try {
					Mail.sendAlert("ITOMS", OmsConstants.SUBJECT_Y, emailContent.toString(), true);
					logger.info("邮件发送成功!");
					
				} catch (MessagingException e) {
					logger.info("邮件发送失败！" + e);
					isSuccess = false;
				}
				
			}
			return isSuccess;
		
		}
}
