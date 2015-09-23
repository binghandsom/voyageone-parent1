package com.voyageone.bi.ajax.bean;

import com.voyageone.bi.base.AjaxRequestBean;
import com.voyageone.bi.base.AjaxResponseBean;
import com.voyageone.bi.bean.BillBean;
import com.voyageone.bi.commonutils.ConditionUtil;
import com.voyageone.bi.commonutils.Contants;
import com.voyageone.bi.commonutils.StringUtils;
import com.voyageone.common.util.DateTimeUtil;

import java.util.List;

// 页面初期化用
public class AjaxFinancialBillBean extends AjaxRequestBean {

	public class Result extends AjaxResponseBean {	
		// 税费账单请求
		private List<BillBean> taxBillBean;
		public List<BillBean> getTaxBillBean() {
			return taxBillBean;
		}
		public void setTaxBillBean(List<BillBean> taxBillBean) {
			this.taxBillBean = taxBillBean;
		}

		// 邮费账单请求
		private List<BillBean> transpotationBillBean;
		public List<BillBean> getTranspotationBillBean() {
			return transpotationBillBean;
		}
		public void setTranspotationBillBean(List<BillBean> transpotationBillBean) {
			this.transpotationBillBean = transpotationBillBean;
		}

		private int tax_page;
		private int tax_page_size;
		private int tax_record_count;
		private int transpotation_page;
		private int transpotation_page_size;
		private int transpotation_record_count;

		private String report_file_path;
		private String report_file_name;

		public int getTax_page() {
			return tax_page;
		}

		public void setTax_page(int tax_page) {
			this.tax_page = tax_page;
		}

		public int getTax_page_size() {
			return tax_page_size;
		}

		public void setTax_page_size(int tax_page_size) {
			this.tax_page_size = tax_page_size;
		}

		public int getTax_record_count() {
			return tax_record_count;
		}

		public void setTax_record_count(int tax_record_count) {
			this.tax_record_count = tax_record_count;
		}

		public int getTranspotation_page() {
			return transpotation_page;
		}

		public void setTranspotation_page(int transpotation_page) {
			this.transpotation_page = transpotation_page;
		}

		public int getTranspotation_page_size() {
			return transpotation_page_size;
		}

		public void setTranspotation_page_size(int transpotation_page_size) {
			this.transpotation_page_size = transpotation_page_size;
		}

		public int getTranspotation_record_count() {
			return transpotation_record_count;
		}

		public void setTranspotation_record_count(int transpotation_record_count) {
			this.transpotation_record_count = transpotation_record_count;
		}

		public String getReport_file_path() {
			return report_file_path;
		}

		public void setReport_file_path(String report_file_path) {
			this.report_file_path = report_file_path;
		}

		public String getReport_file_name() {
			return report_file_name;
		}

		public void setReport_file_name(String report_file_name) {
			this.report_file_name = report_file_name;
		}
	}

	// 店铺
	private String shop_ids = null;
	// 分类
	private String invoice_num = null;
	// 分类
	private String file_name = null;

	// 时间
	private String time_start;
	private String time_end;
	
	// sort
	private String sort_col;
	private String sord;

	private int tax_page;
	private int tax_page_size;
	private int tax_record_count;
	private int transpotation_page;
	private int transpotation_page_size;
	private int transpotation_record_count;

	private String web_root_path;

	private String web_file_path;

	private String report_file_path;
	private String report_file_name;

	public String getShop_ids() {
		return shop_ids;
	}
	public void setShop_ids(String shop_ids) {
		this.shop_ids = shop_ids;
	}

	public String getInvoice_num() {
		return invoice_num;
	}

	public void setInvoice_num(String invoice_num) {
		this.invoice_num = invoice_num;
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

	public String getFile_name() {
		return file_name;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	public int getTax_page() {
		return tax_page;
	}

	public void setTax_page(int tax_page) {
		this.tax_page = tax_page;
	}

	public int getTax_page_size() {
		return tax_page_size;
	}

	public void setTax_page_size(int tax_page_size) {
		this.tax_page_size = tax_page_size;
	}

	public int getTax_record_count() {
		return tax_record_count;
	}

	public void setTax_record_count(int tax_record_count) {
		this.tax_record_count = tax_record_count;
	}

	public int getTranspotation_page() {
		return transpotation_page;
	}

	public void setTranspotation_page(int transpotation_page) {
		this.transpotation_page = transpotation_page;
	}

	public int getTranspotation_page_size() {
		return transpotation_page_size;
	}

	public void setTranspotation_page_size(int transpotation_page_size) {
		this.transpotation_page_size = transpotation_page_size;
	}

	public int getTranspotation_record_count() {
		return transpotation_record_count;
	}

	public void setTranspotation_record_count(int transpotation_record_count) {
		this.transpotation_record_count = transpotation_record_count;
	}

	public String getWeb_root_path() {
		return web_root_path;
	}

	public void setWeb_root_path(String web_root_path) {
		this.web_root_path = web_root_path;
	}

	public String getWeb_file_path() {
		return web_file_path;
	}

	public void setWeb_file_path(String web_file_path) {
		this.web_file_path = web_file_path;
	}

	public String getReport_file_path() {
		return report_file_path;
	}

	public void setReport_file_path(String report_file_path) {
		this.report_file_path = report_file_path;
	}

	public String getReport_file_name() {
		return report_file_name;
	}

	public void setReport_file_name(String report_file_name) {
		this.report_file_name = report_file_name;
	}

	// 入力参数检查
	@Override
	public boolean checkInput() {

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

		if(DateTimeUtil.getDate().before(DateTimeUtil.parse(time_start, DateTimeUtil.DEFAULT_DATE_FORMAT))){
			AjaxResponseBean result = getResponseBean();
			result.setReqResult(Contants.AJAX_RESULT_FALSE);
			result.setReqResultInfo("start date can not be earlier than current day");
			return false;
		}

		if(DateTimeUtil.getDate().before(DateTimeUtil.parse(time_end, DateTimeUtil.DEFAULT_DATE_FORMAT))){
			AjaxResponseBean result = getResponseBean();
			result.setReqResult(Contants.AJAX_RESULT_FALSE);
			result.setReqResultInfo("end date can not be earlier than current day");
			return false;
		}

		if(DateTimeUtil.parse(time_end, DateTimeUtil.DEFAULT_DATE_FORMAT).before(DateTimeUtil.parse(time_start, DateTimeUtil.DEFAULT_DATE_FORMAT))){
			AjaxResponseBean result = getResponseBean();
			result.setReqResult(Contants.AJAX_RESULT_FALSE);
			result.setReqResultInfo("end date can not be earlier than start day");
			return false;
		}

		return true;
	}

	@Override
	protected AjaxResponseBean initResponseBean() {
		return new Result();
	}
	
}
