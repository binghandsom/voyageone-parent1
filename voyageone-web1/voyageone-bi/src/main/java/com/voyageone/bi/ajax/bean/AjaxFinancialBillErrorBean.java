package com.voyageone.bi.ajax.bean;

import java.util.List;

import com.voyageone.bi.base.AjaxRequestBean;
import com.voyageone.bi.base.AjaxResponseBean;
import com.voyageone.bi.bean.BillErrorBean;
import com.voyageone.bi.bean.BillErrorExplanationBean;

// 页面初期化用
public class AjaxFinancialBillErrorBean extends AjaxRequestBean {

	public class Result extends AjaxResponseBean {	
		// 税费账单请求
		private List<BillErrorBean> billErrorBean;

		public List<BillErrorBean> getBillErrorBean() {
			return billErrorBean;
		}

		public void setBillErrorBean(List<BillErrorBean> billErrorBean) {
			this.billErrorBean = billErrorBean;
		}

		// 税费账单请求
		private List<BillErrorExplanationBean> billErrorExplanationBean;

		public List<BillErrorExplanationBean> getBillErrorExplanationBean() {
			return billErrorExplanationBean;
		}

		public void setBillErrorExplanationBean(List<BillErrorExplanationBean> billErrorExplanationBean) {
			this.billErrorExplanationBean = billErrorExplanationBean;
		}

		private int error_page;
		private int error_page_size;
		private int error_record_count;
		private int explanation_page;
		private int explanation__page_size;
		private int explanation_record_count;

		public int getError_page() {
			return error_page;
		}

		public void setError_page(int error_page) {
			this.error_page = error_page;
		}

		public int getError_page_size() {
			return error_page_size;
		}

		public void setError_page_size(int error_page_size) {
			this.error_page_size = error_page_size;
		}

		public int getError_record_count() {
			return error_record_count;
		}

		public void setError_record_count(int error_record_count) {
			this.error_record_count = error_record_count;
		}

		public int getExplanation_page() {
			return explanation_page;
		}

		public void setExplanation_page(int explanation_page) {
			this.explanation_page = explanation_page;
		}

		public int getExplanation__page_size() {
			return explanation__page_size;
		}

		public void setExplanation__page_size(int explanation__page_size) {
			this.explanation__page_size = explanation__page_size;
		}

		public int getExplanation_record_count() {
			return explanation_record_count;
		}

		public void setExplanation_record_count(int explanation_record_count) {
			this.explanation_record_count = explanation_record_count;
		}
	}

	// 店铺
	private String shop_ids = null;
	// 分类
	private String invoice_num = null;
	private String tracking_no = null;
	private String main_waybill_num;
	private String pay_in_warrant_num;
	private String error_type_id;
	
	// sort
	private String sort_col;
	private String sord;

	private int error_page;
	private int error_page_size;
	private int error_record_count;

	private String explanation_error_status;
	private int explanation_page;
	private int explanation__page_size;
	private int explanation_record_count;
	
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

	public String getTracking_no() {
		return tracking_no;
	}

	public void setTracking_no(String tracking_no) {
		this.tracking_no = tracking_no;
	}

	public String getMain_waybill_num() {
		return main_waybill_num;
	}

	public void setMain_waybill_num(String main_waybill_num) {
		this.main_waybill_num = main_waybill_num;
	}

	public String getPay_in_warrant_num() {
		return pay_in_warrant_num;
	}

	public void setPay_in_warrant_num(String pay_in_warrant_num) {
		this.pay_in_warrant_num = pay_in_warrant_num;
	}

	public String getError_type_id() {
		return error_type_id;
	}

	public void setError_type_id(String error_type_id) {
		this.error_type_id = error_type_id;
	}

	public int getError_page() {
		return error_page;
	}

	public void setError_page(int error_page) {
		this.error_page = error_page;
	}

	public int getError_page_size() {
		return error_page_size;
	}

	public void setError_page_size(int error_page_size) {
		this.error_page_size = error_page_size;
	}

	public int getError_record_count() {
		return error_record_count;
	}

	public void setError_record_count(int error_record_count) {
		this.error_record_count = error_record_count;
	}

	// 入力参数检查
	@Override
	public boolean checkInput() {

		return true;
	}

	@Override
	protected AjaxResponseBean initResponseBean() {
		return new Result();
	}

	public String getExplanation_error_status() {
		return explanation_error_status;
	}

	public void setExplanation_error_status(String explanation_error_status) {
		this.explanation_error_status = explanation_error_status;
	}

	public int getExplanation_page() {
		return explanation_page;
	}

	public void setExplanation_page(int explanation_page) {
		this.explanation_page = explanation_page;
	}

	public int getExplanation__page_size() {
		return explanation__page_size;
	}

	public void setExplanation__page_size(int explanation__page_size) {
		this.explanation__page_size = explanation__page_size;
	}

	public int getExplanation_record_count() {
		return explanation_record_count;
	}

	public void setExplanation_record_count(int explanation_record_count) {
		this.explanation_record_count = explanation_record_count;
	}
}
