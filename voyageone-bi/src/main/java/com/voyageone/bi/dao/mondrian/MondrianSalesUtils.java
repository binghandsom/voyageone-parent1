package com.voyageone.bi.dao.mondrian;

import java.text.DecimalFormat;
import java.util.List;

import org.olap4j.Cell;

import com.voyageone.bi.commonutils.ConditionUtil;
import com.voyageone.bi.disbean.BaseKpiDisBean;
import com.voyageone.bi.disbean.ChartGridDisBean;
import com.voyageone.bi.disbean.ConditionBean;
import com.voyageone.bi.commonutils.ConditionUtil.Dimension;

public class MondrianSalesUtils {
	
	
	/**
	 * setColumnValue
	 * @param columnName
	 * @param cell
	 * @param chartGridDisBean
	 */
	public static void setColumnValue(String columnName, Cell cell, ChartGridDisBean chartGridDisBean) {
		setColumnValue(columnName, cell.getFormattedValue(), chartGridDisBean);
	}

	/**
	 * setColumnValue
	 * @param columnName
	 * @param value
	 * @param chartGridDisBean
	 */
	public static void setColumnValue(String columnName, String value, ChartGridDisBean chartGridDisBean) {
		String columnNameShort = columnName.replace("[Measures].[", "").replace("]", "");
		setColumnValueShort(columnNameShort, value, chartGridDisBean);
	}
	
	/**
	 * setColumnValue
	 * @param columnName
	 * @param value
	 * @param chartGridDisBean
	 */
	public static void setColumnValueShort(String columnName, String value, ChartGridDisBean chartGridDisBean) {
		switch (columnName) {
		case "id":
			chartGridDisBean.setId(value);
			break;		
		case "code":
			chartGridDisBean.setValue(value);
			break;
		case "parent":
			chartGridDisBean.setParent(value);
			break;			
		case "name":
			chartGridDisBean.setTitle(value);
			break;
		case "sname":
			chartGridDisBean.setsTitle(value);
			break;
		case "type":
			chartGridDisBean.setType(value);
			break;
		case "qty_n":
			chartGridDisBean.getQty_kpi().setValue(value);
			break;
		case "qty_n_l":
			chartGridDisBean.getQty_kpi().setValue_r(value);
			break;
		case "qty_n_l_r":
			chartGridDisBean.getQty_kpi().setValue_r_rate(value);
			break;
		case "qty_n_l_r_u":
			chartGridDisBean.getQty_kpi().setValue_r_rate_up(value);
			break;
		case "qty_n_y":
			chartGridDisBean.getQty_kpi().setValue_y(value);
			break;
		case "qty_n_y_r":
			chartGridDisBean.getQty_kpi().setValue_y_rate(value);
			break;
		case "qty_n_y_r_u":
			chartGridDisBean.getQty_kpi().setValue_y_rate_up(value);
			break;
		//amt
		case "amt_n":
			chartGridDisBean.getAmt_kpi().setValue(value);
			break;
		case "amt_n_l":
			chartGridDisBean.getAmt_kpi().setValue_r(value);
			break;
		case "amt_n_l_r":
			chartGridDisBean.getAmt_kpi().setValue_r_rate(value);
			break;
		case "amt_n_l_r_u":
			chartGridDisBean.getAmt_kpi().setValue_r_rate_up(value);
			break;
		case "amt_n_y":
			chartGridDisBean.getAmt_kpi().setValue_y(value);
			break;
		case "amt_n_y_r":
			chartGridDisBean.getAmt_kpi().setValue_y_rate(value);
			break;
		case "amt_n_y_r_u":
			chartGridDisBean.getAmt_kpi().setValue_y_rate_up(value);
			break;
		//order
		case "order_n":
			chartGridDisBean.getOrder_kpi().setValue(value);
			break;
		case "order_n_l":
			chartGridDisBean.getOrder_kpi().setValue_r(value);
			break;
		case "order_n_l_r":
			chartGridDisBean.getOrder_kpi().setValue_r_rate(value);
			break;
		case "order_n_l_r_u":
			chartGridDisBean.getOrder_kpi().setValue_r_rate_up(value);
			break;
		case "order_n_y":
			chartGridDisBean.getOrder_kpi().setValue_y(value);
			break;
		case "order_n_y_r":
			chartGridDisBean.getOrder_kpi().setValue_y_rate(value);
			break;
		case "order_n_y_r_u":
			chartGridDisBean.getOrder_kpi().setValue_y_rate_up(value);
			break;
		// atv
		case "atv_n":
			chartGridDisBean.getAtv_kpi().setValue(value);
			break;
		case "atv_n_l":
			chartGridDisBean.getAtv_kpi().setValue_r(value);
			break;
		case "atv_n_l_r":
			chartGridDisBean.getAtv_kpi().setValue_r_rate(value);
			break;
		case "atv_n_l_r_u":
			chartGridDisBean.getAtv_kpi().setValue_r_rate_up(value);
			break;
		case "atv_n_y":
			chartGridDisBean.getAtv_kpi().setValue_y(value);
			break;
		case "atv_n_y_r":
			chartGridDisBean.getAtv_kpi().setValue_y_rate(value);
			break;
		case "atv_n_y_r_u":
			chartGridDisBean.getAtv_kpi().setValue_y_rate_up(value);
			break;
			//pv
		case "pv_n":
			chartGridDisBean.getPv_kpi().setValue(value);
			break;
		case "pv_n_l":
			chartGridDisBean.getPv_kpi().setValue_r(value);
			break;
		case "pv_n_l_r":
			chartGridDisBean.getPv_kpi().setValue_r_rate(value);
			break;
		case "pv_n_l_r_u":
			chartGridDisBean.getPv_kpi().setValue_r_rate_up(value);
			break;
		case "pv_n_y":
			chartGridDisBean.getPv_kpi().setValue_y(value);
			break;
		case "pv_n_y_r":
			chartGridDisBean.getPv_kpi().setValue_y_rate(value);
			break;
		case "pv_n_y_r_u":
			chartGridDisBean.getPv_kpi().setValue_y_rate_up(value);
			break;
		//uv
		case "uv_n":
			chartGridDisBean.getUv_kpi().setValue(value);
			break;
		case "uv_n_l":
			chartGridDisBean.getUv_kpi().setValue_r(value);
			break;
		case "uv_n_l_r":
			chartGridDisBean.getUv_kpi().setValue_r_rate(value);
			break;
		case "uv_n_l_r_u":
			chartGridDisBean.getUv_kpi().setValue_r_rate_up(value);
			break;
		case "uv_n_y":
			chartGridDisBean.getUv_kpi().setValue_y(value);
			break;
		case "uv_n_y_r":
			chartGridDisBean.getUv_kpi().setValue_y_rate(value);
			break;
		case "uv_n_y_r_u":
			chartGridDisBean.getUv_kpi().setValue_y_rate_up(value);
			break;
		//tr
		case "tr_n":
			chartGridDisBean.getTr_kpi().setValue(value);
			break;
		case "tr_n_l":
			chartGridDisBean.getTr_kpi().setValue_r(value);
			break;
		case "tr_n_l_r":
			chartGridDisBean.getTr_kpi().setValue_r_rate(value);
			break;
		case "tr_n_l_r_u":
			chartGridDisBean.getTr_kpi().setValue_r_rate_up(value);
			break;
		case "tr_n_y":
			chartGridDisBean.getTr_kpi().setValue_y(value);
			break;
		case "tr_n_y_r":
			chartGridDisBean.getTr_kpi().setValue_y_rate(value);
			break;
		case "tr_n_y_r_u":
			chartGridDisBean.getTr_kpi().setValue_y_rate_up(value);
			break;
		}
	}

	/**
	 * @param queryStr
	 * @param condition
	 * @param cubeName
	 * @param dimension
	 * @return
	 */
	public static String builderSql(String queryStr, ConditionBean condition, String cubeName, Dimension dimension) {
		String result = queryStr;
		
		//String cubeName = getSalesCubeName(condition, dimension);
		result = result.replaceAll("#cubeName#", cubeName);
		
		// Date Condition
		String condition_start_time = "";
		String condition_end_time = "[Date]." + CubeUtil.getDateDay(condition.getTime_end());
		if (condition.getTime_start() != null) {
			condition_start_time = "[Date]." + CubeUtil.getDateDay(condition.getTime_start());
		} else {
			condition_start_time = condition_end_time;
		}
		result = result.replaceAll("#start_time#", condition_start_time);
		result = result.replaceAll("#end_time#", condition_end_time);
		
		//shop condition
		String conditionStr = "";
		String condition_shop_str_tmp = "";
		List<String> condition_shops = CubeUtil.getConditonValueId(condition.getShopIdArray());
		for (String condition_shop : condition_shops) {
			condition_shop_str_tmp =  condition_shop_str_tmp + "[Shop].[id]" + condition_shop + ",";
		}
		String condition_shop_str = "{" + condition_shop_str_tmp.substring(0, condition_shop_str_tmp.length()-1) + "}";
		if (dimension == Dimension.TimeLine) {
			conditionStr = conditionStr + condition_shop_str;
		} else if (dimension == Dimension.Shop) {
			result = result.replaceAll("#row_shops#", condition_shop_str);
		} else {
			conditionStr = conditionStr + "*" + condition_shop_str;
		}
		
		//BuyChannel condition row_BuyChannel
		String condition_buyChannel_str = "[BuyChannel].[id].Members";
		String condition_buyChannel_str_tmp = "";
		List<String> condition_buyChannelList = CubeUtil.getConditonValueId(condition.getBuyChannel_idsArray());
		for (String condition_buyChannel : condition_buyChannelList) {
			condition_buyChannel_str_tmp =  condition_buyChannel_str_tmp + "[BuyChannel].[id]" + condition_buyChannel + ",";
		}
		if (condition_buyChannel_str_tmp.length()>0) {
			condition_buyChannel_str = "{" + condition_buyChannel_str_tmp.substring(0, condition_buyChannel_str_tmp.length()-1)+ "}";
		}
		if (dimension == Dimension.BuyChannel) {
			result = result.replaceAll("#row_BuyChannel#",  condition_buyChannel_str);
		} else if (condition_buyChannelList.size()>0) {
			conditionStr = conditionStr + "*" + condition_buyChannel_str;
		}
		
		//brand condition
		String condition_brand_str = "[Brand].[id].Members ";
		String condition_brand_str_tmp = "";
		List<String> condition_brandList = CubeUtil.getConditonValueId(condition.getBrand_idArray());
		for (String condition_brand : condition_brandList) {
			condition_brand_str_tmp =  condition_brand_str_tmp + "[Brand].[id]" + condition_brand + ",";
		}
		if (condition_brand_str_tmp.length()>0) {
			condition_brand_str = "{" + condition_brand_str_tmp.substring(0, condition_brand_str_tmp.length()-1)+ "}";
		}
		if (dimension == Dimension.Brand) {
			result = result.replaceAll("#row_brand#",  condition_brand_str);
		} else {
			if (condition_brandList.size()>0) {
				conditionStr = conditionStr + "*" + condition_brand_str;
			}
		}

		//category condition
		String condition_category_str = "[Category].[category1].Members ";
		String condition_category_str_tmp = "";
		List<String> condition_categoryList = condition.getCategory_idArray();
		String level ="1";
		String isParent = "1";
		for (String condition_category : condition_categoryList) {
			String[] condition_category_arr = condition_category.split("-");
			String code = condition_category_arr[0];
			level = condition_category_arr[1];
			isParent = condition_category_arr[2];
			condition_category_str_tmp =  condition_category_str_tmp + "[Category].[category"+level+"].&[" + code + "],";
		}
		if (condition_category_str_tmp.length()>0) {
			condition_category_str = "{" + condition_category_str_tmp.substring(0, condition_category_str_tmp.length()-1)+ "}";
		}
		if (dimension == Dimension.Category) {
			if (condition_categoryList.size()>0) {
				int newLevel = Integer.parseInt(level);
				if ("1".equals(condition.getCategory_child())) {
					if (condition_categoryList.size()==1 && "0".equals(isParent)) {
						newLevel = Integer.parseInt(level);
					} else {
						newLevel = Integer.parseInt(level) + 1;
					}
				}
				result = result.replaceAll("#row_category#",  "EXISTS([Category].[category"+newLevel+"].Members, " + condition_category_str + ")" );
			} else {
				result = result.replaceAll("#row_category#",  condition_category_str );
			}
		} else {
			if (condition_categoryList.size()>0) {
				conditionStr = conditionStr + "*" + condition_category_str;
			}
		}
		
		//color condition
		String condition_color_str = "[Color].[id].Members ";
		String condition_color_str_tmp = "";
		List<String> condition_colorList = CubeUtil.getConditonValueId(condition.getColor_idArray());
		for (String condition_color : condition_colorList) {
			condition_color_str_tmp =  condition_color_str_tmp + "[Color].[id]" + condition_color + ",";
		}
		if (condition_color_str_tmp.length()>0) {
			condition_color_str = "{" + condition_color_str_tmp.substring(0, condition_color_str_tmp.length()-1)+ "}";
		}
		if (dimension == Dimension.Color) {
			result = result.replaceAll("#row_color#",  condition_color_str);
		} else {
			if (condition_colorList.size()>0) {
				conditionStr = conditionStr + "*" + condition_color_str;
			}
		}
		
		//size condition
		String condition_size_str = "[Size].[id].Members ";
		String condition_size_str_tmp = "";
		List<String> condition_sizeList = CubeUtil.getConditonValueId(condition.getSize_idArray());
		for (String condition_size : condition_sizeList) {
			condition_size_str_tmp =  condition_size_str_tmp + "[Size].[id]" + condition_size + ",";
		}
		if (condition_size_str_tmp.length()>0) {
			condition_size_str = "{" + condition_size_str_tmp.substring(0, condition_size_str_tmp.length()-1)+ "}";
		}
		if (dimension == Dimension.Size) {
			result = result.replaceAll("#row_size#",  condition_size_str);
		} else {
			if (condition_sizeList.size()>0) {
				conditionStr = conditionStr + "*" + condition_size_str;
			}
		}

		//Product condition
		String condition_product_str = "[Product].[id].Members";
		String condition_product_str_tmp = "";
		List<String> condition_productList = CubeUtil.getConditonValueId(condition.getProduct_idArray());
		for (String condition_product : condition_productList) {
			condition_product_str_tmp =  condition_product_str_tmp + "[Product].[id]" + condition_product + ",";
		}
		if (condition_product_str_tmp.length()>0) {
			condition_product_str = "{" + condition_product_str_tmp.substring(0, condition_product_str_tmp.length()-1)+ "}";
		}
		if (dimension == Dimension.Product) {
			result = result.replaceAll("#row_product#",  condition_product_str);
		} else if (dimension == Dimension.SKU) {
		} else {
			if (condition_productList.size()>0) {
				conditionStr = conditionStr + "*" + condition_product_str;
			}
		}
		
		//Sku condition
		String condition_sku_product_str = "[Sku].[id].Members";
		String condition_sku_product_str_tmp = "";
		List<String> condition_sku_productList = CubeUtil.getConditonValueId(condition.getProduct_idArray());
		for (String condition_product : condition_sku_productList) {
			condition_sku_product_str_tmp =  condition_sku_product_str_tmp + "[Sku].[productId]" + condition_product + ",";
		}
		if (condition_sku_product_str_tmp.length()>0) {
			condition_sku_product_str = "{" + condition_sku_product_str_tmp.substring(0, condition_sku_product_str_tmp.length()-1)+ "}";
		}
		if (dimension == Dimension.SKU) {
			result = result.replaceAll("#sku_condition_product#",  "EXISTS([Sku].[id].Members, " + condition_sku_product_str+ ")");
		}
		
		result = result.replaceAll("#condition#", conditionStr);
		
		return result;
	}
	
	/**
	 * getSalesCubeName
	 * @param condition
	 * @param dimension
	 * @return
	 */
	public static String getSalesCubeName(ConditionBean condition, Dimension dimension) {
		String result = "sales_store";
		if (dimension == Dimension.SKU) {
			result = "sales_sku";
		} else if (condition.getColor_idArray().size() > 0
				|| condition.getSize_idArray().size() > 0
				|| dimension == Dimension.Color
				|| dimension == Dimension.Size
				) {
			result = "sales_sku";
		} else if (condition.getCategory_idArray().size() > 0
				|| condition.getBrand_idArray().size() > 0
				|| condition.getProduct_idArray().size() > 0
				|| dimension == Dimension.Category
				|| dimension == Dimension.Brand
				|| dimension == Dimension.Product
				) {
			result = "sales_product";
		}
		return result;
	}
	
	/**
	 * getSalesExtendCubeName
	 * @param condition
	 * @param dimension
	 * @return
	 */
	public static String getSalesExtendCubeName(ConditionBean condition, Dimension dimension) {
		String result = "sales_store_extend";
		if (condition.getColor_idArray().size() > 0
				|| condition.getSize_idArray().size() > 0
				|| dimension == Dimension.Color
				|| dimension == Dimension.Size
				) {
			result = "sales_sku_extend";
		} else if (condition.getCategory_idArray().size() > 0
				|| condition.getBrand_idArray().size() > 0
				|| condition.getProduct_idArray().size() > 0
				|| dimension == Dimension.Category
				|| dimension == Dimension.Brand
				|| dimension == Dimension.Product
				) {
			result = "sales_product_extend";
		}
		return result;
	}
	
	/**
	 * mergeChartGridDisBean
	 * @param chartGridDisBeanCur
	 * @param chartGridDisBeanMom
	 * @param chartGridDisBeanYoy
	 */
	public static void mergeChartGridDisBean(
			ChartGridDisBean chartGridDisBeanCur,
			ChartGridDisBean chartGridDisBeanMom,
			ChartGridDisBean chartGridDisBeanYoy) {
		// qty
		mergeChartGridDisBean(chartGridDisBeanCur.getQty_kpi(),
													chartGridDisBeanMom.getQty_kpi(),
													chartGridDisBeanYoy.getQty_kpi());
		// amt
		mergeChartGridDisBean(chartGridDisBeanCur.getAmt_kpi(),
													chartGridDisBeanMom.getAmt_kpi(),
													chartGridDisBeanYoy.getAmt_kpi());

		// order
		mergeChartGridDisBean(chartGridDisBeanCur.getOrder_kpi(),
													chartGridDisBeanMom.getOrder_kpi(),
													chartGridDisBeanYoy.getOrder_kpi());
		// atv
		mergeChartGridDisBean(chartGridDisBeanCur.getAtv_kpi(),
													chartGridDisBeanMom.getAtv_kpi(),
													chartGridDisBeanYoy.getAtv_kpi());
		//PV
		mergeChartGridDisBean(chartGridDisBeanCur.getPv_kpi(),
													chartGridDisBeanMom.getPv_kpi(),
													chartGridDisBeanYoy.getPv_kpi());
		//UV
		mergeChartGridDisBean(chartGridDisBeanCur.getUv_kpi(),
													chartGridDisBeanMom.getUv_kpi(),
													chartGridDisBeanYoy.getUv_kpi());
		//TR
		mergeChartGridDisBean(chartGridDisBeanCur.getTr_kpi(),
													chartGridDisBeanMom.getTr_kpi(),
													chartGridDisBeanYoy.getTr_kpi());
	}
	
	/**
	 * mergeChartGridDisBean
	 * @param cbean
	 * @param mbean
	 * @param ybean
	 */
	public static void mergeChartGridDisBean(
			BaseKpiDisBean cbean,
			BaseKpiDisBean mbean, 
			BaseKpiDisBean ybean) {
		
		DecimalFormat df1 = new DecimalFormat("####.##");
		cbean.setValue_r(mbean.getValue());
		cbean.setValue_y(ybean.getValue());
		
		double cValue = Double.parseDouble(cbean.getValue());
		double rValue = Double.parseDouble(mbean.getValue());
		double yValue = Double.parseDouble(ybean.getValue());
		
		if (rValue != 0 && cValue != 0) {
			double value_r_rate = cValue / rValue;
			cbean.setValue_r_rate(df1.format(value_r_rate));
			cbean.setValue_r_rate_up(df1.format(value_r_rate - 1));
		}
		
		if (yValue != 0 && cValue != 0) {
			double value_y_rate = cValue / rValue;
			cbean.setValue_y_rate(df1.format(value_y_rate));
			cbean.setValue_y_rate_up(df1.format(value_y_rate - 1));
		}
	}
	
	/**
	 * mergeChartGridDisBeanList
	 * @param lstBeanCur
	 * @param lstBeanMom
	 * @param lstBeanYoy
	 */
	public static void mergeChartGridDisBeanList(
			List<ChartGridDisBean> lstBeanCur,
			List<ChartGridDisBean> lstBeanMom, 
			List<ChartGridDisBean> lstBeanYoy) {
		
		for (ChartGridDisBean beanCur : lstBeanCur) {
			String valueCur = beanCur.getValue();
			ChartGridDisBean chartGridDisBeanMom = new ChartGridDisBean();
			ChartGridDisBean chartGridDisBeanYoy = new ChartGridDisBean();

			for (ChartGridDisBean beanMom : lstBeanMom) {
				if (valueCur != null && valueCur.equals(beanMom.getValue())) {
					chartGridDisBeanMom = beanMom;
					break;
				}
			}
			
			for (ChartGridDisBean beanYoy : lstBeanYoy) {
				if (valueCur != null && valueCur.equals(beanYoy.getValue())) {
					chartGridDisBeanYoy = beanYoy;
					break;
				}
			}
			mergeChartGridDisBean(
					beanCur, 
					chartGridDisBeanMom,
					chartGridDisBeanYoy);
		}
	}
	
	/**
	 * executeSqlAndMomYoy
	 * @param queryStrOrg
	 * @param cubeName
	 * @param condition
	 * @param dimension
	 * @param mondrianDaoSupport
	 * @return
	 */
	public static List<ChartGridDisBean> executeSqlAndMomYoy(
			String queryStrOrg, String cubeName, ConditionBean condition,
			Dimension dimension, MondrianDaoSupport mondrianDaoSupport) {
		// current
		String queryStr = MondrianSalesUtils.builderSql(queryStrOrg, condition, cubeName, dimension);
		List<ChartGridDisBean> lstBeanCur = mondrianDaoSupport.queryWithBeanList(queryStr);

		// mom
		ConditionBean lastMomCondition = ConditionUtil.getMomConditionBean(condition);
		queryStr = MondrianSalesUtils.builderSql(queryStrOrg, lastMomCondition, cubeName, dimension);
		List<ChartGridDisBean> lstBeanMom = mondrianDaoSupport.queryWithBeanList(queryStr);

		// yoy
		ConditionBean lastYoyCondition = ConditionUtil.getYoyConditionBean(condition);
		queryStr = MondrianSalesUtils.builderSql(queryStrOrg, lastYoyCondition, cubeName, dimension);
		List<ChartGridDisBean> lstBeanYoy = mondrianDaoSupport.queryWithBeanList(queryStr);

		// merge
		MondrianSalesUtils.mergeChartGridDisBeanList(lstBeanCur, lstBeanMom, lstBeanYoy);
		return lstBeanCur;
	}
	
	public static void mergeExtendBeanList(List<ChartGridDisBean> lstBean, List<ChartGridDisBean> lstExtendBean) {
		for (ChartGridDisBean bean : lstBean) {
			String valueCur = bean.getValue();
			for (ChartGridDisBean beanExtend : lstExtendBean) {
				if (valueCur != null && valueCur.equals(beanExtend.getValue())) {
					mergeExtendBean(bean, beanExtend);
					break;
				}
			}
		}
	}
	public static void mergeExtendBean(ChartGridDisBean bean, ChartGridDisBean beanExtend) {
		bean.setPv_kpi(beanExtend.getPv_kpi());
		bean.setUv_kpi(beanExtend.getUv_kpi());
		bean.setTr_kpi(beanExtend.getTr_kpi());
	}

}
