package com.voyageone.bi.ajax.bean;

import java.util.List;

import com.voyageone.bi.base.AjaxRequestBean;
import com.voyageone.bi.base.AjaxResponseBean;
import com.voyageone.bi.commonutils.ConditionUtil;
import com.voyageone.bi.commonutils.Contants;
import com.voyageone.bi.commonutils.StringUtils;
import com.voyageone.bi.disbean.ChartGridDisBean;

// 页面初期化用
public class AjaxSalesHomeBean extends AjaxRequestBean {

	public class Result extends AjaxResponseBean {	
		//昨天主要KPI
		private ChartGridDisBean yesterdayKpi = null;
		public ChartGridDisBean getYesterdayKpi() {
			return yesterdayKpi;
		}
		public void setYesterdayKpi(ChartGridDisBean yesterdayKpi) {
			this.yesterdayKpi = yesterdayKpi;
		}
		
		// [时间]销量请求
		private List<ChartGridDisBean> timeLineDisBean;
		public List<ChartGridDisBean> getTimeLineDisBean() {
			return timeLineDisBean;
		}
		public void setTimeLineDisBean(List<ChartGridDisBean> timeLineDisBean) {
			this.timeLineDisBean = timeLineDisBean;
		}

		// [时间]销量请求Sum
		private List<ChartGridDisBean> timeLineSumDisBean;
		public List<ChartGridDisBean> getTimeLineSumDisBean() {
			return timeLineSumDisBean;
		}
		public void setTimeLineSumDisBean(List<ChartGridDisBean> timeLineSumDisBean) {
			this.timeLineSumDisBean = timeLineSumDisBean;
		}
		
		//Compare Stores
		private List<ChartGridDisBean> compareBean;
		public List<ChartGridDisBean> getCompareBean() {
			return compareBean;
		}
		public void setCompareBean(List<ChartGridDisBean> compareBean) {
			this.compareBean = compareBean;
		}

		// [品牌]销量请求
		private List<List<ChartGridDisBean>> brandDisBean;
		public List<List<ChartGridDisBean>> getBrandDisBean() {
			return brandDisBean;
		}
		public void setBrandDisBean(List<List<ChartGridDisBean>> brandDisBean) {
			this.brandDisBean = brandDisBean;
		}

		// [分类]销量请求
		private List<List<ChartGridDisBean>> categoryDisBean;
		public List<List<ChartGridDisBean>> getCategoryDisBean() {
			return categoryDisBean;
		}
		public void setCategoryDisBean(List<List<ChartGridDisBean>> categoryDisBean) {
			this.categoryDisBean = categoryDisBean;
		}

		// [产品]销量请求
		private List<List<ChartGridDisBean>> productDisBean;
		public List<List<ChartGridDisBean>> getProductDisBean() {
			return productDisBean;
		}
		public void setProductDisBean(List<List<ChartGridDisBean>> productDisBean) {
			this.productDisBean = productDisBean;
		}
	}
	
	// 时间
	private String time = null;
	// 店铺（TM，TG等）
	private String shop_ids = null;
	// sort
	private String sort_col = null;
	private String sord = null;
	//
	private String productTimeType = "0";
	
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	
	public String getShop_ids() {
		return shop_ids;
	}

	public void setShop_ids(String shop_ids) {
		this.shop_ids = shop_ids;
	}
	
	public String getSort_col() {
		return sort_col;
	}
	public void setSort_col(String sort_col) {
		this.sort_col = sort_col;
	}
	
	public String getSord() {
		return sord;
	}
	public void setSord(String sord) {
		this.sord = sord;
	}
	
	public String getProductTimeType() {
		return productTimeType;
	}
	public void setProductTimeType(String productTimeType) {
		this.productTimeType = productTimeType;
	}
	
	// 入力参数检查
	@Override
	public boolean checkInput() {
		// 时间
		if (StringUtils.isEmpty(time)) {
			AjaxResponseBean result = getResponseBean();
			result.setReqResult(Contants.AJAX_RESULT_FALSE);
			result.setReqResultInfo("date is needed");
			return false;
		}
		
		String msg = ConditionUtil.checkEndDate(time);
		if (msg != null) {
			AjaxResponseBean result = getResponseBean();
			result.setReqResult(Contants.AJAX_RESULT_FALSE);
			result.setReqResultInfo(msg);
			return false;
		}
		
		msg = ConditionUtil.checkStartDate(time);
		if (msg != null) {
			AjaxResponseBean result = getResponseBean();
			result.setReqResult(Contants.AJAX_RESULT_FALSE);
			result.setReqResultInfo(msg);
			return false;
		}
		
		// 门店
		if (shop_ids == null || shop_ids.length()==0) {
			AjaxResponseBean result = getResponseBean();
			result.setReqResult(Contants.AJAX_RESULT_FALSE);
			result.setReqResultInfo("shop is needed");
			return false;
		}
		return true;
	}

	@Override
	protected AjaxResponseBean initResponseBean() {
		return new Result();
	}
}
