package com.voyageone.bi.task.sup;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyageone.bi.base.BiException;
import com.voyageone.bi.commonutils.ConditionUtil.Dimension;
import com.voyageone.bi.dao.sqlolap.SqlOlapConstantSales;
import com.voyageone.bi.dao.sqlolap.SqlOlapConstantShopExtend;
import com.voyageone.bi.dao.sqlolap.SqlOlapDaoSupport;
import com.voyageone.bi.dao.sqlolap.SqlOlapSalesUtils;
import com.voyageone.bi.disbean.ChartGridDisBean;
import com.voyageone.bi.disbean.ConditionBean;
import com.voyageone.bi.tranbean.UserInfoBean;


@Service
public class SalesSumTask {

	private static String SUM_ORG_SQL = 
			"SELECT  \n"
			+ "cur.id code  \n"
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

	//vt_sales_store
	private static String SUM_SHOP_SUB_SQL = "SELECT  \n"
			+ "'1' id \n"
			+ SqlOlapConstantSales.SALES_SUB_SQL_SHOP
			+ "\n";
	
	private static String SUM_SHOP_SQL = 
			SUM_ORG_SQL.replace("#CUR_SUB_SQL#", SUM_SHOP_SUB_SQL)
			.replace("#MOM_SUB_SQL#",  SUM_SHOP_SUB_SQL.replace("#date_condition#", "#mom_date_condition#"))
			.replace("#YOY_SUB_SQL#",  SUM_SHOP_SUB_SQL.replace("#date_condition#", "#yoy_date_condition#"));

	//vt_sales_model
	private static String SUM_MODEL_SUB_SQL = "SELECT  \n"
			+ "'1' id \n"
			+ SqlOlapConstantSales.SALES_SUB_SQL_MODEL
			+ "\n";
	
	private static String SUM_MODEL_SQL = 
			SUM_ORG_SQL.replace("#CUR_SUB_SQL#", SUM_MODEL_SUB_SQL)
			.replace("#MOM_SUB_SQL#",  SUM_MODEL_SUB_SQL.replace("#date_condition#", "#mom_date_condition#"))
			.replace("#YOY_SUB_SQL#",  SUM_MODEL_SUB_SQL.replace("#date_condition#", "#yoy_date_condition#"));
	
	//vt_sales_model
	private static String SUM_PRODUCT_SUB_SQL = "SELECT  \n"
			+ "'1' id \n"
			+ SqlOlapConstantSales.SALES_SUB_SQL_PRODUCT
			+ "\n";
	
	private static String SUM_PRODUCT_SQL = 
			SUM_ORG_SQL.replace("#CUR_SUB_SQL#", SUM_PRODUCT_SUB_SQL)
			.replace("#MOM_SUB_SQL#",  SUM_PRODUCT_SUB_SQL.replace("#date_condition#", "#mom_date_condition#"))
			.replace("#YOY_SUB_SQL#",  SUM_PRODUCT_SUB_SQL.replace("#date_condition#", "#yoy_date_condition#"));
	
	//vt_sales_sku
	private static String SUM_SKU_SUB_SQL = "SELECT  \n"
			+ "'1' id \n"
			+ SqlOlapConstantSales.SALES_SUB_SQL_SKU
			+ "\n";
	
	private static String SUM_SKU_SQL = 
			SUM_ORG_SQL.replace("#CUR_SUB_SQL#", SUM_SKU_SUB_SQL)
			.replace("#MOM_SUB_SQL#",  SUM_SKU_SUB_SQL.replace("#date_condition#", "#mom_date_condition#"))
			.replace("#YOY_SUB_SQL#",  SUM_SKU_SUB_SQL.replace("#date_condition#", "#yoy_date_condition#"));		
		
	@Autowired
	private SqlOlapDaoSupport sqlOlapDaoSupport;
	
	/**
	 * getSalesByShops
	 * @param condition
	 * @param userInfoBean
	 * @return
	 * @throws BiException
	 */
	public ChartGridDisBean getSalesSumData(ConditionBean condition, UserInfoBean userInfoBean) throws BiException{
		Dimension dimension = Dimension.None;
		String queryStrOrg = SUM_SHOP_SQL;
		String tableName = SqlOlapSalesUtils.getTableName(condition, dimension);
		if ("vt_sales_model".equals(tableName)) {
			queryStrOrg = SUM_MODEL_SQL;
		} else if ("vt_sales_product".equals(tableName)) {
			queryStrOrg = SUM_PRODUCT_SQL;
		} else if ("vt_sales_sku".equals(tableName)) {
			queryStrOrg = SUM_SKU_SQL;
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
				
		List<ChartGridDisBean> chartGridList = SqlOlapSalesUtils.executeSqlAndMomYoy(
				queryStrOrg, 
				tableName,
				condition,
				dimension, 
				sqlOlapDaoSupport);
		ChartGridDisBean result = new ChartGridDisBean();
		if (chartGridList != null && chartGridList.size()>0) {
			result = chartGridList.get(0);
		}
		
		return result;
	}
	
	private static String SUM_SHOP_EXTEND_ORG_SQL = 
			"SELECT  \n"
			+ "cur.id code  \n"
			+ SqlOlapConstantShopExtend.SALES_SUB_COLUMN
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
	
	//vt_sales_store
	private static String SUM_SHOP_EXTEND_SUB_SQL = "SELECT  \n"
			+ "'1' id \n"
			+ SqlOlapConstantShopExtend.SALES_SUB_SQL_SHOP
			+ "\n";
	
	private static String SUM_SHOP_EXTEND_SQL = 
			SUM_SHOP_EXTEND_ORG_SQL.replace("#CUR_SUB_SQL#", SUM_SHOP_EXTEND_SUB_SQL)
			.replace("#MOM_SUB_SQL#",  SUM_SHOP_EXTEND_SUB_SQL.replace("#date_condition#", "#mom_date_condition#"))
			.replace("#YOY_SUB_SQL#",  SUM_SHOP_EXTEND_SUB_SQL.replace("#date_condition#", "#yoy_date_condition#"));
	
	/**
	 * getSalesSumExtendData
	 * @param condition
	 * @param userInfoBean
	 * @return
	 */
	public ChartGridDisBean getSalesSumExtendData(ConditionBean condition, UserInfoBean userInfoBean)   {
		Dimension dimension = Dimension.None;
		String tableName = SqlOlapSalesUtils.getTableName(condition, dimension);
		ChartGridDisBean result = new ChartGridDisBean();
		if ("vt_sales_store".equals(tableName)) {
			String queryStrOrg = SUM_SHOP_EXTEND_SQL;
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
					
			List<ChartGridDisBean> chartGridList = SqlOlapSalesUtils.executeSqlAndMomYoy(
					queryStrOrg, 
					tableName,
					condition,
					dimension, 
					sqlOlapDaoSupport);
			if (chartGridList != null && chartGridList.size()>0) {
				result = chartGridList.get(0);
			}
		}
		return result;
	}

}
