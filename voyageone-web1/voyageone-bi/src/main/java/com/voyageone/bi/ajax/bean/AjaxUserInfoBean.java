package com.voyageone.bi.ajax.bean;

import java.util.List;
import com.voyageone.bi.base.AjaxRequestBean;
import com.voyageone.bi.base.AjaxResponseBean;
import com.voyageone.bi.disbean.PageCmbBoxDisBean;

// 页面初期化用
public class AjaxUserInfoBean extends AjaxRequestBean {

	public class Result extends AjaxResponseBean {		
		
		// 店铺显示内容
		private List<PageCmbBoxDisBean> cmbShops;
		public List<PageCmbBoxDisBean> getCmbShops() {
			return cmbShops;
		}
		public void setCmbShops(List<PageCmbBoxDisBean> cmbShops) {
			this.cmbShops = cmbShops;
		}

		// 渠道显示内容
		private List<PageCmbBoxDisBean> cmbChannels;

		public List<PageCmbBoxDisBean> getCmbChannels() {
			return cmbChannels;
		}

		public void setCmbChannels(List<PageCmbBoxDisBean> cmbChannels) {
			this.cmbChannels = cmbChannels;
		}

		// Category显示内容
		private List<PageCmbBoxDisBean> cmbCategorys;
		public List<PageCmbBoxDisBean> getCmbCategorys() {
			return cmbCategorys;
		}
		public void setCmbCategorys(List<PageCmbBoxDisBean> cmbCategorys) {
			this.cmbCategorys = cmbCategorys;
		}

		// Brand显示内容
		private List<PageCmbBoxDisBean> cmbBrands;
		public List<PageCmbBoxDisBean> getCmbBrands() {
			return cmbBrands;
		}
		public void setCmbBrands(List<PageCmbBoxDisBean> cmbBrands) {
			this.cmbBrands = cmbBrands;
		}

		// Color显示内容
		private List<PageCmbBoxDisBean> cmbColors;
		public List<PageCmbBoxDisBean> getCmbColors() {
			return cmbColors;
		}
		public void setCmbColors(List<PageCmbBoxDisBean> cmbColors) {
			this.cmbColors = cmbColors;
		}
		
		// Size显示内容
		private List<PageCmbBoxDisBean> cmbSizes;
		public List<PageCmbBoxDisBean> getCmbSizes() {
			return cmbSizes;
		}
		public void setCmbSizes(List<PageCmbBoxDisBean> cmbSizes) {
			this.cmbSizes = cmbSizes;
		}
		
		// product显示内容
		private List<PageCmbBoxDisBean> cmbProducts;
		public List<PageCmbBoxDisBean> getCmbProducts() {
			return cmbProducts;
		}
		public void setCmbProducts(List<PageCmbBoxDisBean> cmbProducts) {
			this.cmbProducts = cmbProducts;
		}
		
	}
	
	//产品检索
	private String productQueryStr;
	public String getProductQueryStr() {
		return productQueryStr;
	}
	public void setProductQueryStr(String productQueryStr) {
		this.productQueryStr = productQueryStr;
	}

	// 入力参数检查
	@Override
	public boolean checkInput() {
		// 暂无检查项目
		
		return true;
	}

	@Override
	protected AjaxResponseBean initResponseBean() {
		return new Result();
	}
}
