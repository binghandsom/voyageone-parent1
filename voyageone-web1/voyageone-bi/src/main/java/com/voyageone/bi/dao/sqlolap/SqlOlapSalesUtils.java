package com.voyageone.bi.dao.sqlolap;

import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.voyageone.bi.commonutils.ConditionUtil;
import com.voyageone.bi.commonutils.ConditionUtil.Dimension;
import com.voyageone.bi.dao.cache.CacheContext;
import com.voyageone.bi.dao.mondrian.MondrianSalesUtils;
import com.voyageone.bi.disbean.ChartGridDisBean;
import com.voyageone.bi.disbean.ConditionBean;

public class SqlOlapSalesUtils {
	
	//private static final Log logger = LogFactory.getLog(SqlOlapSalesUtils.class);
	
	public static void setColumnValue(String columnName, SqlRowSet rs, ChartGridDisBean chartGridDisBean) {
		String value = rs.getString(columnName);
		MondrianSalesUtils.setColumnValueShort(columnName, value, chartGridDisBean);
	}

	public static String builderSql(String queryStr, ConditionBean condition, String tableName, Dimension dimension) {
		String result = queryStr;
		 
		result = result.replaceAll("#table_name#", tableName);
		
		/**
		 * Date Condition
		 */
		String dateColoumnStr = "date.id";
		switch (condition.getTimeType()) {
	 		case Year:
	 			dateColoumnStr = "date.Year";
	 			break;
	 		case Month:
	 			dateColoumnStr = "date.month_id";
	 			break;
	 		case Day:
	 			dateColoumnStr = "date.id";
	 			break;
 		}
		//current
		String date_conditionStr = dateColoumnStr + " between " +  condition.getTime_start() + " and " + condition.getTime_end();
		result = result.replaceAll("#date_condition#",  date_conditionStr);
		// mom
		ConditionBean lastMomCondition = null;
		if (dimension == Dimension.TimeLine) {
			lastMomCondition = ConditionUtil.getMomConditionBeanTimeLine(condition);
		} else {
			lastMomCondition = ConditionUtil.getMomConditionBean(condition);
		}
		String mom_date_conditionStr = dateColoumnStr + " between " + lastMomCondition.getTime_start() + " and " + lastMomCondition.getTime_end();
		result = result.replaceAll("#mom_date_condition#",  mom_date_conditionStr);
		// yoy
		ConditionBean lastYoyCondition = ConditionUtil.getYoyConditionBean(condition);
		String yoy_date_conditionStr = dateColoumnStr + " between " + lastYoyCondition.getTime_start() + " and " + lastYoyCondition.getTime_end();
		result = result.replaceAll("#yoy_date_condition#",  yoy_date_conditionStr);
		
		//shop condition
		String conditionStr = "";
		String condition_shop_str_tmp = "";
		List<String> condition_shops = condition.getShopIdArray();
		for (String condition_shop : condition_shops) {
			condition_shop_str_tmp =  condition_shop_str_tmp+condition_shop + ",";
		}
		if (condition_shop_str_tmp.length()>0) {
			String condition_shop_str = "shop.id in (" + condition_shop_str_tmp.substring(0, condition_shop_str_tmp.length()-1) + ")";
			conditionStr = conditionStr + "\n AND " + condition_shop_str;
		}
		
		//BuyChannel condition row_BuyChannel
		String condition_buyChannel_str = "";
		String condition_buyChannel_str_tmp = "";
		List<String> condition_buyChannelList = condition.getBuyChannel_idsArray();
		for (String condition_buyChannel : condition_buyChannelList) {
			condition_buyChannel_str_tmp =  condition_buyChannel_str_tmp + condition_buyChannel + ",";
		}
		if (condition_buyChannel_str_tmp.length()>0) {
			condition_buyChannel_str = "buy_channel.id in (" + condition_buyChannel_str_tmp.substring(0, condition_buyChannel_str_tmp.length()-1)+ ")";
			conditionStr = conditionStr + "\n AND " + condition_buyChannel_str;
		}
		
		//brand condition
		String condition_brand_str = "";
		String condition_brand_str_tmp = "";
		List<String> condition_brandList = condition.getBrand_idArray();
		for (String condition_brand : condition_brandList) {
			condition_brand_str_tmp =  condition_brand_str_tmp + condition_brand + ",";
		}
		if (condition_brand_str_tmp.length()>0) {
			condition_brand_str = "brand.id in (" + condition_brand_str_tmp.substring(0, condition_brand_str_tmp.length()-1)+ ")";
			conditionStr = conditionStr + "\n AND " + condition_brand_str;
		}
		
		//category condition
		String condition_category_str = "";
		String condition_category_str_tmp = "";
		List<String> condition_categoryList = condition.getCategory_idArray();
		String level ="1";
		for (String condition_category : condition_categoryList) {
			String[] condition_category_arr = condition_category.split("-");
			String id = condition_category_arr[0];
			level = condition_category_arr[1];
			condition_category_str_tmp =  condition_category_str_tmp + id + ",";
		}
		if (condition_category_str_tmp.length()>0) {
			condition_category_str = "p_category.category" + level + "_id in  (" + condition_category_str_tmp.substring(0, condition_category_str_tmp.length()-1)+ ")";
			conditionStr = conditionStr + "\n AND " + condition_category_str;
		}
		
		//color condition
		String condition_color_str = "";
		String condition_color_str_tmp = "";
		List<String> condition_colorList = condition.getColor_idArray();
		for (String condition_color : condition_colorList) {
			condition_color_str_tmp =  condition_color_str_tmp + condition_color + ",";
		}
		if (condition_color_str_tmp.length()>0) {
			condition_color_str = "color.id in (" + condition_color_str_tmp.substring(0, condition_color_str_tmp.length()-1)+ ")";
			conditionStr = conditionStr + "\n AND " + condition_color_str;
		}
		
		//size condition
		String condition_size_str = "";
		String condition_size_str_tmp = "";
		List<String> condition_sizeList = condition.getSize_idArray();
		for (String condition_size : condition_sizeList) {
			condition_size_str_tmp =  condition_size_str_tmp + condition_size + ",";
		}
		if (condition_size_str_tmp.length()>0) {
			condition_size_str = "size.id in (" + condition_size_str_tmp.substring(0, condition_size_str_tmp.length()-1)+ ")";
			conditionStr = conditionStr + "\n AND " + condition_size_str;
		}

		//Product condition
		String condition_product_str = "";
		String condition_product_str_tmp = "";
		List<String> condition_productList = condition.getProduct_idArray();
		for (String condition_product : condition_productList) {
			condition_product_str_tmp =  condition_product_str_tmp + condition_product + ",";
		}
		if (condition_product_str_tmp.length()>0) {
			condition_product_str = "product.id in (" + condition_product_str_tmp.substring(0, condition_product_str_tmp.length()-1)+ ")";
			conditionStr = conditionStr + "\n AND " + condition_product_str;
		}
		
		//SKU condition
		String condition_sku_product_str = "";
		String condition_sku_product_str_tmp = "";
		List<String> condition_sku_productList = condition.getSku_idArray();
		for (String condition_product : condition_sku_productList) {
			condition_sku_product_str_tmp =  condition_sku_product_str_tmp + condition_product + ",";
		}
		if (condition_sku_product_str_tmp.length()>0) {
			condition_sku_product_str = "sku.id in (" + condition_sku_product_str_tmp.substring(0, condition_sku_product_str_tmp.length()-1)+ ")";
			conditionStr = conditionStr + "\n AND " + condition_sku_product_str;
		}
		
		result = result.replaceAll("#condition#", conditionStr);
		
		return result;
	}
	

	public static String getSchemaName() {
		return "";
	}
	/**
	 * getSalesCubeName
	 * @param condition
	 * @param dimension
	 * @return
	 */
	public static String getTableName(ConditionBean condition, Dimension dimension) {
		String result = "vt_sales_store";
		if (dimension == Dimension.SKU) {
			result = "vt_sales_sku";
		} else if (condition.getColor_idArray().size() > 0
				|| condition.getSize_idArray().size() > 0
				|| dimension == Dimension.Color
				|| dimension == Dimension.Size
				) {
			result = "vt_sales_sku";
		} else if (condition.getCategory_idArray().size() > 0
				|| condition.getProduct_idArray().size() > 0
				|| dimension == Dimension.Category
				|| dimension == Dimension.Product
				) {
			result = "vt_sales_product";
		} else if (condition.getBrand_idArray().size() > 0
				|| dimension == Dimension.Brand
				|| dimension == Dimension.Model
				) {
			result = "vt_sales_model";
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
		String result = "vt_sales_store_extend";
		if (condition.getColor_idArray().size() > 0
				|| condition.getSize_idArray().size() > 0
				|| dimension == Dimension.Color
				|| dimension == Dimension.Size
				) {
			result = "vt_sales_sku_extend";
		} else if (condition.getCategory_idArray().size() > 0
				|| condition.getBrand_idArray().size() > 0
				|| condition.getProduct_idArray().size() > 0
				|| dimension == Dimension.Category
				|| dimension == Dimension.Brand
				|| dimension == Dimension.Product
				) {
			result = "vt_sales_product_extend";
		}
		return result;
	}
	
	public static List<ChartGridDisBean> executeSqlAndMomYoy(
			String queryStrOrg, 
			String tableName, 
			ConditionBean condition,
			Dimension dimension, 
			SqlOlapDaoSupport sqlOlapDaoSupport) {
		// current
		String queryStr = builderSql(queryStrOrg, condition, tableName, dimension);
		String key = queryStr;
		@SuppressWarnings("unchecked")
		List<ChartGridDisBean> result = (List<ChartGridDisBean>) CacheContext.getInstance().get(key);
        if (result != null) {
            return result;
        }
		result = sqlOlapDaoSupport.queryWithBeanList(queryStr);
		if (result.size()>0) {
			CacheContext.getInstance().addOrUpdateCache(queryStr, result);
		}
		return result;
	}

}
