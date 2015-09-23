package com.voyageone.batch.oms.job;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.taobao.api.domain.TradeRate;
import com.voyageone.batch.core.Enums.TaskControlEnums;
import com.voyageone.batch.core.dao.TaskDao;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.core.util.TaskControlUtils;
import com.voyageone.batch.oms.modelbean.TradeRateBean;
import com.voyageone.batch.oms.service.TradeRateService;
import com.voyageone.common.components.issueLog.IssueLog;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.components.tmall.TbTraderatesService;
import com.voyageone.common.configs.ShopConfigs;
import com.voyageone.common.configs.Enums.TypeConfigEnums;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.mail.Mail;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.StringUtils;

/**
 * 取得淘宝用户评价内容定时任务
 * 
 * @author James
 *
 */
public class GetTradeRateResultJob {

	private static Log logger = LogFactory.getLog(GetTradeRateResultJob.class);

	@Autowired
	TaskDao taskDao;

	@Autowired
	TradeRateService tradeRateService;
	
	@Autowired
	TbTraderatesService tbTraderatesService;
	
	@Autowired
	IssueLog issueLog;

	private String taskCheck;

	private String yesterday;

	// 可疑的评论 需要mail推送的评论
	private List<TradeRateBean> suspiciousList;

	// 可疑邮件的关键字
	private String ratingKeyword[];
	
	private String badKeyword[];

	public void run() {
		List<TaskControlBean> taskControlList = taskDao.getTaskControlList(taskCheck);
		// 是否可以运行的判断
		if (TaskControlUtils.isRunnable(taskControlList, taskCheck) == false) {
			return;
		}

		String taskID = TaskControlUtils.getTaskId(taskControlList);

		logger.info(taskCheck + "任务开始");

		// 任务监控历史记录添加:启动
		taskDao.insertTaskHistory(taskID, TaskControlEnums.Status.START.getIs());

		try {
			// 用户评价中敏感词
			String temp[]=TypeConfigEnums.MastType.ratingKeyword.getBean().getValue().split(";");
			ratingKeyword = temp[0].split(",");
			badKeyword = temp[1].split(",");
			
			// 包含敏感词汇的评价列表
			suspiciousList = new ArrayList<TradeRateBean>();
			// 获取全部的渠道
			List<ShopBean> shopBeans = ShopConfigs.getShopList();
			// 取得北京时间的前一天
			Calendar now = DateTimeUtil.getCustomCalendar(8);
			now.add(Calendar.DAY_OF_YEAR, -1);
			Date day = now.getTime();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			yesterday = sdf.format(day);

			for (ShopBean shopBean : shopBeans) {
				// 阿里系的场合
				try {
					if ("1".equalsIgnoreCase(shopBean.getPlatform_id())) {
						// 取得评价信息
						getTradeRateInfo(shopBean);
					}
				} catch (Exception e) {
					logger.info(shopBean.getShop_name()+" "+ e.getMessage());
					issueLog.log(e, ErrorType.BatchJob, SubSystem.OMS, taskCheck + shopBean.getShop_name() + "有异常发生");
//					Mail.sendAlert("ITOMS", taskCheck + shopBean.getShop_name() + "有异常发生", e.getMessage());
				}
			}
			// 异常的评价 邮件通知
			String content = getMailContent(suspiciousList, yesterday);
			Mail.sendAlert("ANNOUNCE", "【聚石塔】各店客户评价(" + yesterday + ")", content, suspiciousList.size() > 0);

		} catch (Exception e) {
			try {
				logger.info(e.getMessage());
				Mail.sendAlert("ITOMS", taskCheck + "有异常发生", e.getMessage());
			} catch (MessagingException e1) {
				logger.info(e1.getMessage());
			}
		}

		// 任务监控历史记录添加:结束
		taskDao.insertTaskHistory(taskID, TaskControlEnums.Status.SUCCESS.getIs());
		logger.info(taskCheck + "任务结束");
	}

	/**
	 * 取得用户评价信息的主函数
	 * @param shopBean
	 * @return
	 * @throws Exception
	 */
	private String getTradeRateInfo(ShopBean shopBean) throws Exception {

		String ret = "";
		// 取得昨天评价信息
		List<TradeRate> traderatelist = tbTraderatesService.getTradeRateList(yesterday, yesterday, shopBean);
		if (traderatelist == null) {
			throw new Exception(shopBean.getShop_name() + "取得评价信息失败，各单位注意！！！！！！！！！！！ ERROR");
		}
		List<TradeRateBean> tradeRates = taobaobean2TradeRateBean(traderatelist, shopBean);
		logger.info(String.format("昨日%s评价信息数:%d", shopBean.getShop_name(), traderatelist.size()));
		// 聚石塔外：插入昨天取得评价信息,
		tradeRateService.inseretTradeRateList(tradeRates);

		return ret;
	}

	/**
	 * 把天猫的返回的bean转成我们自己的bean 
	 * @param taobaolist
	 * @param shopBean
	 * @return
	 */
	private List<TradeRateBean> taobaobean2TradeRateBean(List<TradeRate> taobaolist, ShopBean shopBean) {
		List<TradeRateBean> tradeRates = new ArrayList<TradeRateBean>();
		for (TradeRate taobaoBean : taobaolist) {
			TradeRateBean tradeRate = new TradeRateBean();
			tradeRate.setSource_order_id(taobaoBean.getTid().toString());
			tradeRate.setNick(taobaoBean.getNick());
			tradeRate.setResult(taobaoBean.getResult());
			tradeRate.setCreated(DateTimeUtil.getGMTTime(taobaoBean.getCreated(), 8));
			tradeRate.setRated_nick(taobaoBean.getRatedNick());
			tradeRate.setItem_title(taobaoBean.getItemTitle());
			tradeRate.setItem_price(Double.parseDouble(taobaoBean.getItemPrice()));
			tradeRate.setContent(taobaoBean.getContent());
			tradeRate.setReply(taobaoBean.getReply());
			tradeRate.setNum_iid(taobaoBean.getNumIid());
			tradeRate.setCart_id(Integer.parseInt(shopBean.getCart_id()));
			tradeRate.setOrder_channel_id(shopBean.getOrder_channel_id());
			tradeRates.add(tradeRate);

			// 检查是否是可疑评论
			String content = tradeRate.getContent();
			if (content.length() > 30 || StringUtils.containstr(content, ratingKeyword, badKeyword)) {
				suspiciousList.add(tradeRate);
			}
		}
		return tradeRates;

	}
	
	/**
	 * @param taskCheck
	 *            the taskCheck to set
	 */
	public void setTaskCheck(String taskCheck) {
		this.taskCheck = taskCheck;
	}

	/**
	 * 生产邮件的内容
	 * @param reviewList
	 * @param yesterday
	 * @return
	 */
	private String getMailContent(List<TradeRateBean> reviewList, String yesterday) {

		StringBuffer sb = new StringBuffer();

		sb.append("各位");
		sb.append("<br>");
		sb.append("以下信息是各店昨天(" + yesterday + ") 所获得的客户评价");
		sb.append("<br>");

		sb.append("抽取条件:评价字数大于30汉字，评价中有[差评],[假],[高仿],[客服]等关键字");

		sb.append("<table border=\"1\" style=\"border-collapse:collapse\">");
		sb.append("<tr bgcolor=\"#CCCCCC\">");
		sb.append("<td align=center>店铺名</td>");
		sb.append("<td align=center>订单编号</td>");
		sb.append("<td align=center>订单价格</td>");
		sb.append("<td align=center>评价内容</td>");
		sb.append("</tr>");

		for (int i = 0; i < reviewList.size(); i++) {
			TradeRateBean bean = reviewList.get(i);
			if (isBadReview(bean.getContent())) {
				sb.append("<tr bgcolor=\"#DC143C\">");
			} else {
				sb.append("<tr>");
			}

			sb.append("<td>");
			sb.append(bean.getRated_nick());
			sb.append("</td>");
			sb.append("<td>");
			sb.append(bean.getSource_order_id());
			sb.append("</td>");
			sb.append("<td>");
			sb.append(bean.getItem_price());
			sb.append("</td>");
			sb.append("<td>");
			sb.append(bean.getContent());
			sb.append("</td>");
			sb.append("</tr>");
		}
		sb.append("</table>");

		return sb.toString();
	}

	/**
	 * 是否是警告
	 * @param content
	 * @return
	 */
	private Boolean isBadReview(String content) {

		boolean ret = false;

		if (content != null && content.length() > 0) {
			return StringUtils.containstr(content, badKeyword);
		} else {
			ret = false;
		}

		return ret;

	}
}
