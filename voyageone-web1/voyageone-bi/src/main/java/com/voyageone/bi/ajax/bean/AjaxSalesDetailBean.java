package com.voyageone.bi.ajax.bean;

import java.util.List;

import com.voyageone.bi.base.AjaxRequestBean;
import com.voyageone.bi.base.AjaxResponseBean;
import com.voyageone.bi.commonutils.ConditionUtil;
import com.voyageone.bi.commonutils.Contants;
import com.voyageone.bi.commonutils.StringUtils;
import com.voyageone.bi.disbean.ChartGridDisBean;

// 页面初期化用
public class AjaxSalesDetailBean extends AjaxRequestBean {

	public class Result extends AjaxResponseBean {	
		// [时间]销量请求
		private List<ChartGridDisBean> timeLineDisBean;
		public List<ChartGridDisBean> getTimeLineDisBean() {
			return timeLineDisBean;
		}
		public void setTimeLineDisBean(List<ChartGridDisBean> timeLineDisBean) {
			this.timeLineDisBean = timeLineDisBean;
		}

		// [时间]销量请求Sum
		private ChartGridDisBean timeLineSumDisBean;
		public ChartGridDisBean getTimeLineSumDisBean() {
			return timeLineSumDisBean;
		}
		public void setTimeLineSumDisBean(ChartGridDisBean timeLineSumDisBean) {
			this.timeLineSumDisBean = timeLineSumDisBean;
		}
		// [时间]销量请求Pc vs Mobile Sum
		private List<ChartGridDisBean> timeLineSumPcMobileDisBean;
		public List<ChartGridDisBean> getTimeLineSumPcMobileDisBean() {
			return timeLineSumPcMobileDisBean;
		}
		public void setTimeLineSumPcMobileDisBean(
				List<ChartGridDisBean> timeLineSumPcMobileDisBean) {
			this.timeLineSumPcMobileDisBean = timeLineSumPcMobileDisBean;
		}
		
		// [时间]销量请求Shop Sum
		private List<ChartGridDisBean> timeLineSumShopDisBean;
		public List<ChartGridDisBean> getTimeLineSumShopDisBean() {
			return timeLineSumShopDisBean;
		}
		public void setTimeLineSumShopDisBean(
				List<ChartGridDisBean> timeLineSumShopDisBean) {
			this.timeLineSumShopDisBean = timeLineSumShopDisBean;
		}

		// [品牌]销量请求
		private List<ChartGridDisBean> brandDisBean;
		public List<ChartGridDisBean> getBrandDisBean() {
			return brandDisBean;
		}
		public void setBrandDisBean(List<ChartGridDisBean> brandDisBean) {
			this.brandDisBean = brandDisBean;
		}
		
		// [分类]销量请求
		private List<ChartGridDisBean> categoryDisBean;
		public List<ChartGridDisBean> getCategoryDisBean() {
			return categoryDisBean;
		}
		public void setCategoryDisBean(List<ChartGridDisBean> categoryDisBean) {
			this.categoryDisBean = categoryDisBean;
		}
		
		// [款式]销量请求
		private List<ChartGridDisBean> modelDisBean;
		public List<ChartGridDisBean> getModelDisBean() {
			return modelDisBean;
		}
		public void setModelDisBean(List<ChartGridDisBean> modelDisBean) {
			this.modelDisBean = modelDisBean;
		}
		
		// [产品]销量请求
		private List<ChartGridDisBean> productDisBean;
		public List<ChartGridDisBean> getProductDisBean() {
			return productDisBean;
		}
		public void setProductDisBean(List<ChartGridDisBean> productDisBean) {
			this.productDisBean = productDisBean;
		}
		
		// [Color]销量请求
		private List<ChartGridDisBean> colorDisBean;
		public List<ChartGridDisBean> getColorDisBean() {
			return colorDisBean;
		}
		public void setColorDisBean(List<ChartGridDisBean> colorDisBean) {
			this.colorDisBean = colorDisBean;
		}
		
		// [Size]销量请求
		private List<ChartGridDisBean> sizeDisBean;
		public List<ChartGridDisBean> getSizeDisBean() {
			return sizeDisBean;
		}
		public void setSizeDisBean(List<ChartGridDisBean> sizeDisBean) {
			this.sizeDisBean = sizeDisBean;
		}
	}
	
	
	// 店铺
	private String shop_ids = null;
	// 分类
	private String category_ids = null;
	private String category_child = null;

	// 品牌
	private String brand_ids = null;
	// 颜色
	private String color_ids = null;
	// 尺寸
	private String size_ids = null;
	// 产品
	private String product_ids = null;
	// 产品
	private String product_codes = null;
	// 时间
	private String time_type = null;
	private String time_start = null;
	private String time_end = null;
	
	// sort
	private String sort_col = null;
	private String sord = null;
	
	public String getShop_ids() {
		return shop_ids;
	}
	public void setShop_ids(String shop_ids) {
		this.shop_ids = shop_ids;
	}
	
	public String getCategory_ids() {
		return category_ids;
	}
	public void setCategory_ids(String category_ids) {
		this.category_ids = category_ids;
	}

	public String getCategory_child() {
		return category_child;
	}
	public void setCategory_child(String category_child) {
		this.category_child = category_child;
	}
	
	public String getBrand_ids() {
		return brand_ids;
	}
	public void setBrand_ids(String brand_ids) {
		this.brand_ids = brand_ids;
	}

	public String getColor_ids() {
		return color_ids;
	}
	public void setColor_ids(String color_ids) {
		this.color_ids = color_ids;
	}

	public String getSize_ids() {
		return size_ids;
	}
	public void setSize_ids(String size_ids) {
		this.size_ids = size_ids;
	}
	
	public String getProduct_ids() {
		return product_ids;
	}
	public void setProduct_ids(String product_ids) {
		this.product_ids = product_ids;
	}
	public String getProduct_codes() {
		return product_codes;
	}
	public void setProduct_codes(String product_codes) {
		this.product_codes = product_codes;
	}

	public String getTime_type() {
		return time_type;
	}
	public void setTime_type(String time_type) {
		this.time_type = time_type;
	}

	public String getTime_start() {
		return time_start;
	}
	public void setTime_start(String time_start) {
		this.time_start = time_start;
	}

	public String getTime_end() {
		return time_end;
	}
	public void setTime_end(String time_end) {
		this.time_end = time_end;
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

	// 入力参数检查
	@Override
	public boolean checkInput() {
		// Shop
		if (shop_ids == null || shop_ids.length()==0) {
			AjaxResponseBean result = getResponseBean();
			result.setReqResult(Contants.AJAX_RESULT_FALSE);
			result.setReqResultInfo("shop is needed");
			return false;
		}
		
		// 时间
		if (StringUtils.isEmpty(time_type)) {
			AjaxResponseBean result = getResponseBean();
			result.setReqResult(Contants.AJAX_RESULT_FALSE);
			result.setReqResultInfo("DateType is needed");
			return false;
		}
		if (StringUtils.isEmpty(time_start)) {
			AjaxResponseBean result = getResponseBean();
			result.setReqResult(Contants.AJAX_RESULT_FALSE);
			result.setReqResultInfo("start date is needed");
			return false;
		}

		String msg = ConditionUtil.checkStartDate(time_start);
		if (msg != null) {
			AjaxResponseBean result = getResponseBean();
			result.setReqResult(Contants.AJAX_RESULT_FALSE);
			result.setReqResultInfo(msg);
			return false;
		}
		if (StringUtils.isEmpty(time_end)) {
			AjaxResponseBean result = getResponseBean();
			result.setReqResult(Contants.AJAX_RESULT_FALSE);
			result.setReqResultInfo("end date is needed");
			return false;
		}
		msg = ConditionUtil.checkEndDate(time_end);
		if (msg != null) {
			AjaxResponseBean result = getResponseBean();
			result.setReqResult(Contants.AJAX_RESULT_FALSE);
			result.setReqResultInfo(msg);
			return false;
		}
		
		msg =ConditionUtil.checkCategoryLevel(category_ids);
		if (msg != null) {
			AjaxResponseBean result = getResponseBean();
			result.setReqResult(Contants.AJAX_RESULT_FALSE);
			result.setReqResultInfo(msg);
			return false;
		}
		
		return true;
	}

	@Override
	protected AjaxResponseBean initResponseBean() {
		return new Result();
	}
	
}
