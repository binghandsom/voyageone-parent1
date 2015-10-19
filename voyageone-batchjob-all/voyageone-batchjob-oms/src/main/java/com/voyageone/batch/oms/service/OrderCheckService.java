package com.voyageone.batch.oms.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jd.open.api.sdk.domain.order.OrderSearchInfo;
import com.taobao.api.domain.Trade;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.oms.dao.OrderDao;
import com.voyageone.common.components.issueLog.IssueLog;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.components.jd.JdOrderService;
import com.voyageone.common.components.tmall.TbOrderService;
import com.voyageone.common.configs.ShopConfigs;
import com.voyageone.common.configs.Enums.PlatFormEnums;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.mail.Mail;
import com.voyageone.common.util.DateTimeUtil;

@Service
public class OrderCheckService extends BaseTaskService{

	private static Log logger = LogFactory.getLog(OrderCheckService.class);

	@Autowired
	private OrderDao orderDao;
	
	@Autowired
	TbOrderService tbOrderService;
	
	@Autowired
	JdOrderService jbOrderService;	
	
    @Autowired
    protected IssueLog issueLog;
    
	// 渠道数据
	private ShopBean shopBean;
	
	// 昨天的日期 （北京时间）
	private String yesterday;
	
	private String channelId="";
	
	private String cartId="";
	
	private String taskName="";

	/**
	 * 根据cartId ChannelId 和时间找出改时间段的所有订单
	 * @param dataMap
	 * @return
	 */
	public List<String> getOrderByChannelId(Map<String, Object> dataMap){
		return (List) orderDao.getOrderByChannelId(dataMap);
		
	}

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
	 * @throws Exception
	 */
	@Override
	protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {


		for (TaskControlBean taskControl : taskControlList) {
			if ("card_id".equals(taskControl.getCfg_name())) {
				List<String> omitlist = new ArrayList<String>();
				cartId = taskControl.getCfg_val1();
				
				shopBean = ShopConfigs.getShop(channelId, cartId);
				try{
					// 取得北京时间的前一天
					Calendar now = DateTimeUtil.getCustomCalendar(8);
					now.add(Calendar.DAY_OF_YEAR, -1);
					Date checkdate = now.getTime();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					yesterday = sdf.format(checkdate);
	
					// 取得天猫的昨天的订单列表
					List<String> paytradelist = getYesterdayPayTidList();
					if (paytradelist == null) {
						throw new Exception(shopBean.getShop_name() + "取得历史数据失败，本次漏单检查失败！  各单位注意！！！！！！！！！！！ ERROR ");
					}
					logger.info(shopBean.getShop_name() + "昨日付款订单数:" + paytradelist.size());
					// 没有订单的场合后续也不需要再处理了
					if(paytradelist.size() == 0){
						continue;
					}
					// 取得OMS系统中的订单列表
					List<String> tradlist = getYesterdayTradeList();
	
					logger.info(shopBean.getShop_name() + "OMS系统：昨日订单数:" + tradlist.size());
					logger.info(shopBean.getShop_name() + "OMS系统：昨日订单:" + tradlist);
	
					for (String trade : paytradelist) {
						// 如果昨天的订单列表中不包含该订单，判断为漏单
						if (!tradlist.contains(trade)) {
							omitlist.add(trade);
						}
					}
					// 如果有漏单
					logger.info(shopBean.getShop_name() + "昨日漏单数:" + omitlist.size());
					if (omitlist.size() > 0) {
						logger.info(shopBean.getShop_name() + "有漏单：" + omitlist);
						Mail.sendAlert("ITOMS", shopBean.getShop_name()+"("+shopBean.getComment()+")" + "有漏单发生", shopBean.getShop_name() +"("+shopBean.getComment()+")" + "有漏单发生：" + omitlist, true);
					} else {
						Mail.sendAlert("ITOMS", shopBean.getShop_name() + "("+shopBean.getComment()+")" + "没有漏单发生", shopBean.getShop_name() +"("+shopBean.getComment()+")" + "昨日没有漏单发生");
					}
				}catch(Exception e){
					issueLog.log(e, ErrorType.BatchJob, SubSystem.OMS);
				}
			}
		}
	}

	/**
	 * 取得昨天付款的订单并记录1天前未付款订单的客户信息
	 * @return
	 * @throws Exception
	 */
	private List<String> getYesterdayPayTidList() throws Exception {

		List<String> tid = new ArrayList<String>();
		PlatFormEnums.PlatForm platForm = PlatFormEnums.PlatForm.getValueByID(shopBean.getPlatform_id());
		
		String startdate = DateTimeUtil.getGMTTimeFrom(yesterday,8);
		String enddate = DateTimeUtil.getGMTTimeTo(yesterday,8);
		switch (platForm) {
		case TM:
			List<Trade> tmTrades = tbOrderService.getOrderPage(startdate, enddate, shopBean);
			if (tmTrades != null) {
				for (Trade trade : tmTrades) {
					tid.add(trade.getTid().toString());
				}
			} else {
				tid = null;
			}
			break;
		case JD:
			List<OrderSearchInfo> jdTrades = jbOrderService.getNewOrderPage(startdate, enddate, shopBean);
			if (jdTrades != null) {
				for (OrderSearchInfo trade : jdTrades) {
					tid.add(trade.getOrderId());
				}
			} else {
				tid = null;
			}
			break;
		case CN:
			break;
		}

		if (tid == null) {
			throw new Exception("取得历史数据失败，取得历史数据终止");
		} else {
			logger.info("订单数（" + shopBean.getShop_name() + "）：" + tid.size());
		}

		return tid;
	}

	/**
	 * 取得OMS系统中的Orders基本信息
	 * @return
	 */
	private List<String> getYesterdayTradeList() {

		//把北京时间转成服务器时间
		String startTime = DateTimeUtil.getGMTTimeFrom(yesterday, 8);
		String endTime = DateTimeUtil.getGMTTimeTo(yesterday, 8);

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("order_channel_id", channelId);
		data.put("start_time", startTime);
//		data.put("end_time", endTime);
		data.put("cart_id", cartId);
		return this.getOrderByChannelId(data);
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	
	
}
