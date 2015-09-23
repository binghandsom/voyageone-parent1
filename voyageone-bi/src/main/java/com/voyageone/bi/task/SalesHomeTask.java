package com.voyageone.bi.task;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyageone.bi.ajax.bean.AjaxSalesHomeBean;
import com.voyageone.bi.base.BiException;
import com.voyageone.bi.commonutils.ConditionUtil;
import com.voyageone.bi.commonutils.Contants;
import com.voyageone.bi.dao.UserInfoDao;
import com.voyageone.bi.dao.mondrian.MondrianSalesUtils;
import com.voyageone.bi.disbean.ConditionBean;
import com.voyageone.bi.disbean.ChartGridDisBean;
import com.voyageone.bi.task.sup.SalesBrandTask;
import com.voyageone.bi.task.sup.SalesCategoryTask;
import com.voyageone.bi.task.sup.SalesProductTask;
import com.voyageone.bi.task.sup.SalesShopTask;
import com.voyageone.bi.task.sup.SalesSkuTask;
import com.voyageone.bi.task.sup.SalesSumTask;
import com.voyageone.bi.task.sup.SalesTimeTask;
import com.voyageone.bi.tranbean.UserInfoBean;

@Service
public class SalesHomeTask {
//	private static Log logger = LogFactory.getLog(SalesHomeTask.class);
	
	@Autowired
	private UserInfoDao userInfoDao;
	// Sum
	@Autowired
	private SalesSumTask salesSumTask;
	// 按时间
	@Autowired
	private SalesTimeTask salesTimeTask;
	// 按销售门店
	@Autowired
	private SalesShopTask salesShopTask;
	// 按品牌
	@Autowired
	private SalesBrandTask salesBrandTask;
	// 按品类
	@Autowired
	private SalesCategoryTask salesCategoryTask;
	// 按产品
	@Autowired
	private SalesProductTask salesProductTask;
	// 按SKU
	@Autowired
	private SalesSkuTask salesSkuTask;
	
	//昨天主要KPI
	public void ajaxSalesHomeYPKData(AjaxSalesHomeBean bean, UserInfoBean userInfoBean) throws BiException{
		// AJAX 返回值
		AjaxSalesHomeBean.Result result = bean.getResponseBean();
		try{
			ConditionBean condition = ConditionUtil.getSalesHomeYPKCondition(bean);
			
			List<ChartGridDisBean> lstTimeLine = salesTimeTask.getSalesTimeLineData(condition, userInfoBean);
			ChartGridDisBean yesterdayKpiBean = new ChartGridDisBean();
			if (lstTimeLine.size()>0) {
				yesterdayKpiBean = lstTimeLine.get(0);
			}
			List<ChartGridDisBean> lstExtendTimeLine = salesTimeTask.getSalesExtendTimeLineData(condition, userInfoBean);
			ChartGridDisBean yesterdayExtendKpiBean = new ChartGridDisBean();
			if (lstExtendTimeLine.size()>0) {
				yesterdayExtendKpiBean = lstExtendTimeLine.get(0);
			}
			MondrianSalesUtils.mergeExtendBean(yesterdayKpiBean, yesterdayExtendKpiBean);
			result.setYesterdayKpi(yesterdayKpiBean);
			
			result.setReqResult(Contants.AJAX_RESULT_OK);
		} catch(Exception e) {
			result.setReqResult(Contants.AJAX_RESULT_FALSE);
			throw new BiException(e, "ajaxSalesHomeYPKData");
		}
	}
	
	// TimeLine
	public void ajaxGetSalesTimeLineData(AjaxSalesHomeBean bean,
			UserInfoBean userInfoBean) throws BiException {
		AjaxSalesHomeBean.Result result = bean.getResponseBean();
		
		try{
			// TimeLine
			ConditionBean condition = ConditionUtil.getSalesHomeTimeSumL30Condition(bean);
			List<ChartGridDisBean> lstTimeLine = salesTimeTask.getSalesTimeLineData(condition, userInfoBean);
			List<ChartGridDisBean> lstExtendTimeLine = salesTimeTask.getSalesExtendTimeLineData(condition, userInfoBean);
			MondrianSalesUtils.mergeExtendBeanList(lstTimeLine, lstExtendTimeLine);
			result.setTimeLineDisBean(lstTimeLine);

			// TimeLine SUM
			List<ChartGridDisBean> lstTimeLineSum = new ArrayList<ChartGridDisBean>();
			
			// TimeLine L30 SUM
			ChartGridDisBean l30KpiBean = salesSumTask.getSalesSumData(condition, userInfoBean);
			ChartGridDisBean l30ExtendKpiBean = salesSumTask.getSalesSumExtendData(condition, userInfoBean);
			MondrianSalesUtils.mergeExtendBean(l30KpiBean, l30ExtendKpiBean);
			lstTimeLineSum.add(l30KpiBean);
			
			// TimeLine YTD SUM
			condition = ConditionUtil.getSalesHomeTimeSumYtdCondition(bean);
			ChartGridDisBean ytdKpiBean = salesSumTask.getSalesSumData(condition, userInfoBean);
			ChartGridDisBean ytdExtendKpiBean = salesSumTask.getSalesSumExtendData(condition, userInfoBean);
			MondrianSalesUtils.mergeExtendBean(ytdKpiBean, ytdExtendKpiBean);
			lstTimeLineSum.add(ytdKpiBean);
			
			result.setTimeLineSumDisBean(lstTimeLineSum);
			
			result.setReqResult(Contants.AJAX_RESULT_OK);
		} catch(Exception e) {
			result.setReqResult(Contants.AJAX_RESULT_FALSE);
			throw new BiException(e, "ajaxGetSalesInfoByTime");
		}
	}
	
	
	//昨天次要KPI
	public void ajaxSalesHomeCompareData(AjaxSalesHomeBean bean, UserInfoBean userInfoBean) throws BiException{
		// AJAX 返回值
		AjaxSalesHomeBean.Result result = bean.getResponseBean();
		try {
			ConditionBean condition = ConditionUtil.getSalesHomeYPKCondition(bean);
			List<ChartGridDisBean> lstCompareBean = salesShopTask.getSalesByShops(condition, userInfoBean);
			List<ChartGridDisBean> lstExtendCompareBean = salesShopTask.getSalesExtendByShops(condition, userInfoBean);
			MondrianSalesUtils.mergeExtendBeanList(lstCompareBean, lstExtendCompareBean);
			result.setCompareBean(lstCompareBean);
			
			result.setReqResult(Contants.AJAX_RESULT_OK);
		} catch(Exception e) {
			result.setReqResult(Contants.AJAX_RESULT_FALSE);
			throw new BiException(e, "ajaxSalesHomeCompareData");
		}
	}
	
	
	//ajaxGetSalesBrandData KPI
	public void ajaxGetSalesBrandData(AjaxSalesHomeBean bean, UserInfoBean userInfoBean) throws BiException {
		AjaxSalesHomeBean.Result result = bean.getResponseBean();
		try{
			List<List<ChartGridDisBean>> lstBrandDisBean = new ArrayList<List<ChartGridDisBean>>();
			//yesterday
			ConditionBean condition = ConditionUtil.getSalesHomeYPKCondition(bean);
			List<ChartGridDisBean> listYesterdayBean = salesBrandTask.getTopSalesBrandInfo(condition, userInfoBean, 10);
			lstBrandDisBean.add(listYesterdayBean);
			// L30 SUM
			condition = ConditionUtil.getSalesHomeTimeSumL30Condition(bean);
			List<ChartGridDisBean> listL30Bean = salesBrandTask.getTopSalesBrandInfo(condition, userInfoBean, 10);
			lstBrandDisBean.add(listL30Bean);
			// YTD SUM
			condition = ConditionUtil.getSalesHomeTimeSumYtdCondition(bean);
			List<ChartGridDisBean> listYtdBean = salesBrandTask.getTopSalesBrandInfo(condition, userInfoBean, 10);
			lstBrandDisBean.add(listYtdBean);
			
			result.setBrandDisBean(lstBrandDisBean);
			result.setReqResult(Contants.AJAX_RESULT_OK);
		} catch(Exception e) {
			result.setReqResult(Contants.AJAX_RESULT_FALSE);
			throw new BiException(e, "ajaxSalesHomeCompareData");
		}
	}
	
	//ajaxGetSalesCategoryData KPI
	public void ajaxGetSalesCategoryData(AjaxSalesHomeBean bean, UserInfoBean userInfoBean) throws BiException {
		AjaxSalesHomeBean.Result result = bean.getResponseBean();
		try{
			List<List<ChartGridDisBean>> lstCategoryDisBean = new ArrayList<List<ChartGridDisBean>>();
			
			//yesterday
			ConditionBean condition = ConditionUtil.getSalesHomeYPKCondition(bean);
			List<ChartGridDisBean> listYesterdayBean = salesCategoryTask.getTopSalesCategoryInfo(condition, userInfoBean, 10);
			lstCategoryDisBean.add(listYesterdayBean);
			// L30 SUM
			condition = ConditionUtil.getSalesHomeTimeSumL30Condition(bean);
			List<ChartGridDisBean> listL30Bean = salesCategoryTask.getTopSalesCategoryInfo(condition, userInfoBean, 10);
			lstCategoryDisBean.add(listL30Bean);
			// YTD SUM
			condition = ConditionUtil.getSalesHomeTimeSumYtdCondition(bean);
			List<ChartGridDisBean> listYtdBean = salesCategoryTask.getTopSalesCategoryInfo(condition, userInfoBean, 10);
			lstCategoryDisBean.add(listYtdBean);
			
			result.setCategoryDisBean(lstCategoryDisBean);
			result.setReqResult(Contants.AJAX_RESULT_OK);
		} catch(Exception e) {
			result.setReqResult(Contants.AJAX_RESULT_FALSE);
			throw new BiException(e, "ajaxSalesHomeCompareData");
		}
	}
	
	//ajaxGetSalesProductData KPI
	public void ajaxGetSalesProductData(AjaxSalesHomeBean bean, UserInfoBean userInfoBean) throws BiException {
		AjaxSalesHomeBean.Result result = bean.getResponseBean();
		try{
			List<List<ChartGridDisBean>> lstProductDisBean = new ArrayList<List<ChartGridDisBean>>();
			List<ChartGridDisBean> listYeardayBean = null;
			List<ChartGridDisBean> listL30Bean = null;
			List<ChartGridDisBean> listYtdBean = null;
			
			int productTimeType = Integer.parseInt(bean.getProductTimeType());
			switch(productTimeType) {
			case 0:
				//yesterday
				ConditionBean condition = ConditionUtil.getSalesHomeYPKCondition(bean);
				listYeardayBean = getSalesProductAndSku(condition, userInfoBean, 10);
				break;
			case 1:
				// L30 SUM
				condition = ConditionUtil.getSalesHomeTimeSumL30Condition(bean);
				listL30Bean = getSalesProductAndSku(condition, userInfoBean, 10);
				break;
			case 2:
				// YTD SUM
				condition = ConditionUtil.getSalesHomeTimeSumYtdCondition(bean);
				listYtdBean = getSalesProductAndSku(condition, userInfoBean, 10);
				break;
			}
			lstProductDisBean.add(listYeardayBean);
			lstProductDisBean.add(listL30Bean);
			lstProductDisBean.add(listYtdBean);
			
			result.setProductDisBean(lstProductDisBean);
			result.setReqResult(Contants.AJAX_RESULT_OK);
		} catch(Exception e) {
			result.setReqResult(Contants.AJAX_RESULT_FALSE);
			throw new BiException(e, "ajaxSalesHomeCompareData");
		}
	}
	
	private List<ChartGridDisBean> getSalesProductAndSku(ConditionBean condition, UserInfoBean userInfoBean, int topCount) throws BiException {
		List<ChartGridDisBean> listProductBean = salesProductTask.getTopSalesProductInfo(condition, userInfoBean, topCount);
		String product_ids = "";
		for (ChartGridDisBean productBean :listProductBean) {
			product_ids = product_ids + productBean.getId() + ",";
		}
		List<ChartGridDisBean>  listSkuBean = null;
		if (product_ids.length()>0) {
			condition.setProduct_ids(product_ids.substring(0, product_ids.length()-1));
			listSkuBean = salesSkuTask.getSkuBeanLst(condition, userInfoBean);
		}
		List<ChartGridDisBean> result = null;
		if (listSkuBean != null && listSkuBean.size()>0) {
			result =new ArrayList<ChartGridDisBean>();
			for (ChartGridDisBean productBean:listProductBean) {
				productBean.setType("product");
				result.add(productBean);
				for (ChartGridDisBean skuBean:listSkuBean) {
					skuBean.setType("sku");
					if (productBean.getId().equals(skuBean.getParent())) {
						result.add(skuBean);
					}
				}
			}
		} else {
			for (ChartGridDisBean productBean:listProductBean) {
				productBean.setType("product");
			}
			result = listProductBean;
		}
		return result;
	}
	

}
