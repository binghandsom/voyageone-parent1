package com.voyageone.bi.task.sup;

import java.util.List;



//import com.voyageone.bigdata.ajax.bean.AjaxGetSalesInfoBean;
//import com.voyageone.bigdata.ajax.bean.AjaxGetSalesInfoBean.Result;
//import com.voyageone.bigdata.base.BiException;
//import com.voyageone.bigdata.commonutils.Contants;
//import com.voyageone.bigdata.commonutils.DateTimeUtil;
//import com.voyageone.bigdata.commonutils.SalesUtils;
//import com.voyageone.bigdata.commonutils.StringUtils;
//import com.voyageone.bigdata.dao.SalesInfoDao;
//import com.voyageone.bigdata.dao.UserInfoDao;
//import com.voyageone.bigdata.disbean.ChartGridDisBean;
//import com.voyageone.bigdata.tranbean.SalesCondBean;
//import com.voyageone.bigdata.tranbean.UserInfoBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyageone.bi.base.BiException;
import com.voyageone.bi.commonutils.ConditionUtil.Dimension;
import com.voyageone.bi.dao.sqlolap.SqlOlapConstantSales;
import com.voyageone.bi.dao.sqlolap.SqlOlapDaoSupport;
import com.voyageone.bi.dao.sqlolap.SqlOlapSalesUtils;
import com.voyageone.bi.disbean.ChartGridDisBean;
import com.voyageone.bi.disbean.ConditionBean;
import com.voyageone.bi.tranbean.UserInfoBean;

@Service
public class SalesBrandTask {
	
	private static String BRAND_ORG_SQL = 
			"SELECT  \n"
			+ "cur.id code  \n"
			+ ",'' parent  \n"
			+ ",cur.name name  \n"
			+ SqlOlapConstantSales.SALES_SUB_COLUMN
			+ "FROM (  \n"
			+ "#CUR_SUB_SQL#"
			+ " ) cur \n"
			+ "LEFT JOIN (\n"
			+ "#MOM_SUB_SQL#"
			+ " ) mom ON mom.id=cur.id \n"
			+ "LEFT JOIN (\n"
			+ "#YOY_SUB_SQL#"
			+ " ) yoy ON yoy.id=cur.id \n"
			+ " ORDER BY #ORDER_COLUMN# \n"
			+ " #LIMIT_COLUMN# \n";

	//vt_sales_model
	private static String BRAND_MODEL_SUB_SQL = "SELECT  \n"
			+ "brand.id id \n"
			+ ",brand.code code \n"
			+ ",brand.name name \n"
			+ SqlOlapConstantSales.SALES_SUB_SQL_MODEL
			+ "GROUP BY brand.id \n";
	
	private static String BRAND_MODEL_SQL = 
			BRAND_ORG_SQL.replace("#CUR_SUB_SQL#", BRAND_MODEL_SUB_SQL)
			.replace("#MOM_SUB_SQL#",  BRAND_MODEL_SUB_SQL.replace("#date_condition#", "#mom_date_condition#"))
			.replace("#YOY_SUB_SQL#",  BRAND_MODEL_SUB_SQL.replace("#date_condition#", "#yoy_date_condition#"));

	//vt_sales_product
	private static String BRAND_PRODUCT_SUB_SQL = "SELECT  \n"
			+ "brand.id id \n"
			+ ",brand.code code \n"
			+ ",brand.name name \n"
			+ SqlOlapConstantSales.SALES_SUB_SQL_PRODUCT
			+ "GROUP BY brand.id \n";
	
	private static String BRAND_PRODUCT_SQL = 
			BRAND_ORG_SQL.replace("#CUR_SUB_SQL#", BRAND_PRODUCT_SUB_SQL)
			.replace("#MOM_SUB_SQL#",  BRAND_PRODUCT_SUB_SQL.replace("#date_condition#", "#mom_date_condition#"))
			.replace("#YOY_SUB_SQL#",  BRAND_PRODUCT_SUB_SQL.replace("#date_condition#", "#yoy_date_condition#"));
	
	//vt_sales_sku
	private static String BRAND_SKU_SUB_SQL = "SELECT  \n"
			+ "brand.id id \n"
			+ ",brand.code code \n"
			+ ",brand.name name \n"
			+ SqlOlapConstantSales.SALES_SUB_SQL_SKU
			+ "GROUP BY brand.id \n";
	
	private static String BRAND_SKU_SQL = 
			BRAND_ORG_SQL.replace("#CUR_SUB_SQL#", BRAND_SKU_SUB_SQL)
			.replace("#MOM_SUB_SQL#",  BRAND_SKU_SUB_SQL.replace("#date_condition#", "#mom_date_condition#"))
			.replace("#YOY_SUB_SQL#",  BRAND_SKU_SUB_SQL.replace("#date_condition#", "#yoy_date_condition#"));	
	
	@Autowired
	private SqlOlapDaoSupport sqlOlapDaoSupport;
	
	/**
	 * getTopSalesBrandInfo
	 * @param condition
	 * @param userInfoBean
	 * @return
	 * @throws BiException
	 */
	public List<ChartGridDisBean> getTopSalesBrandInfo(ConditionBean condition, UserInfoBean userInfoBean, int topCount) throws BiException{
		ConditionBean cur_condition = condition.createCopy();
		if (topCount>0) {
			cur_condition.setLimit(String.valueOf(topCount));
		}
		List<ChartGridDisBean> result = getSalesBrandInfo(cur_condition, userInfoBean);
		return result;
	}
	
	/**
	 * getSalesBrandInfo
	 * @param condition
	 * @param userInfoBean
	 * @return
	 * @throws BiException
	 */
	public List<ChartGridDisBean> getSalesBrandInfo(ConditionBean condition, UserInfoBean userInfoBean) throws BiException{
		Dimension dimension = Dimension.Brand;
		String queryStrOrg = BRAND_MODEL_SQL;
		String tableName = SqlOlapSalesUtils.getTableName(condition, dimension);
		 if ("vt_sales_product".equals(tableName)) {
				queryStrOrg = BRAND_PRODUCT_SQL;
		} else if ("vt_sales_sku".equals(tableName)) {
			queryStrOrg = BRAND_SKU_SQL;
		}
		
		// limit
		String limitStr = "";
		if (condition.getLimit() != null && condition.getLimit().length()>0) {
			limitStr = "LIMIT " + condition.getLimit();
		}
		queryStrOrg = queryStrOrg.replaceAll("#LIMIT_COLUMN#", limitStr);
		
		// ORDER_COLUMN
		String sort_column = "amt_n";
		if (condition.getSort_col() !=null && condition.getSort_col().length()>0) {
			sort_column = condition.getSort_col();
		}
		// ORDER_COLUMN ASC
		String sord = "DESC";
		if (condition.getSord() !=null &&  condition.getSord().length()>0) {
			sord = condition.getSord().toUpperCase();
		}
		String sortStr = sort_column + " " +  sord + ", code ASC";
		queryStrOrg = queryStrOrg.replaceAll("#ORDER_COLUMN#", sortStr);
				
		List<ChartGridDisBean> result = SqlOlapSalesUtils.executeSqlAndMomYoy(
				queryStrOrg, 
				tableName,
				condition,
				dimension, 
				sqlOlapDaoSupport);
		return result;
	}
}
