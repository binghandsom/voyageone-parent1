package com.voyageone.bi.task.sup;

import java.util.ArrayList;
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
public class SalesShopTask {
	
	private static String SHOP_ORG_SQL = 
			"SELECT  \n"
			+ "cur.id code  \n"
			+ ",'' parent  \n"
			+ ",cur.name name  \n"
			+ ",cur.name_cns sname  \n"
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
	private static String SHOP_SUB_SQL = "SELECT  \n"
			+ "shop.id id \n"
			+ ",shop.code code \n"
			+ ",shop.name name \n"
			+ ",shop.name_cns name_cns \n"
			+ SqlOlapConstantSales.SALES_SUB_SQL_SHOP
			+ "GROUP BY shop.id \n";
	
	private static String SHOP_SQL = 
			SHOP_ORG_SQL.replace("#CUR_SUB_SQL#", SHOP_SUB_SQL)
			.replace("#MOM_SUB_SQL#",  SHOP_SUB_SQL.replace("#date_condition#", "#mom_date_condition#"))
			.replace("#YOY_SUB_SQL#",  SHOP_SUB_SQL.replace("#date_condition#", "#yoy_date_condition#"));
	
	//vt_sales_product
	private static String SHOP_MODEL_SUB_SQL = "SELECT  \n"
			+ "shop.id id \n"
			+ ",shop.code code \n"
			+ ",shop.name name \n"
			+ ",shop.name_cns name_cns \n"
			+ SqlOlapConstantSales.SALES_SUB_SQL_MODEL
			+ "GROUP BY shop.id \n";
	
	private static String SHOP_MODEL_SQL = 
			SHOP_ORG_SQL.replace("#CUR_SUB_SQL#", SHOP_MODEL_SUB_SQL)
			.replace("#MOM_SUB_SQL#",  SHOP_MODEL_SUB_SQL.replace("#date_condition#", "#mom_date_condition#"))
			.replace("#YOY_SUB_SQL#",  SHOP_MODEL_SUB_SQL.replace("#date_condition#", "#yoy_date_condition#"));
	
	//vt_sales_product
	private static String SHOP_PRODUCT_SUB_SQL = "SELECT  \n"
			+ "shop.id id \n"
			+ ",shop.code code \n"
			+ ",shop.name name \n"
			+ ",shop.name_cns name_cns \n"
			+ SqlOlapConstantSales.SALES_SUB_SQL_PRODUCT
			+ "GROUP BY shop.id \n";
	
	private static String SHOP_PRODUCT_SQL = 
			SHOP_ORG_SQL.replace("#CUR_SUB_SQL#", SHOP_PRODUCT_SUB_SQL)
			.replace("#MOM_SUB_SQL#",  SHOP_PRODUCT_SUB_SQL.replace("#date_condition#", "#mom_date_condition#"))
			.replace("#YOY_SUB_SQL#",  SHOP_PRODUCT_SUB_SQL.replace("#date_condition#", "#yoy_date_condition#"));
	
	//vt_sales_sku
	private static String SHOP_SKU_SUB_SQL = "SELECT  \n"
			+ "shop.id id \n"
			+ ",shop.code code \n"
			+ ",shop.name name \n"
			+ ",shop.name_cns name_cns \n"
			+ SqlOlapConstantSales.SALES_SUB_SQL_SKU
			+ "GROUP BY shop.id \n";
	
	private static String SHOP_SKU_SQL = 
			SHOP_ORG_SQL.replace("#CUR_SUB_SQL#", SHOP_SKU_SUB_SQL)
			.replace("#MOM_SUB_SQL#",  SHOP_SKU_SUB_SQL.replace("#date_condition#", "#mom_date_condition#"))
			.replace("#YOY_SUB_SQL#",  SHOP_SKU_SUB_SQL.replace("#date_condition#", "#yoy_date_condition#"));	
	

	@Autowired
	private SqlOlapDaoSupport sqlOlapDaoSupport;
	

	/**
	 * getSalesByShops
	 * @param condition
	 * @param userInfoBean
	 * @return
	 * @throws BiException
	 */
	public List<ChartGridDisBean> getSalesByShops(ConditionBean condition, UserInfoBean userInfoBean) throws BiException{
		Dimension dimension = Dimension.Shop;
		String queryStrOrg = SHOP_SQL;
		String tableName = SqlOlapSalesUtils.getTableName(condition, dimension);
		if ("vt_sales_model".equals(tableName)) {
			queryStrOrg = SHOP_MODEL_SQL;
		} else if ("vt_sales_product".equals(tableName)) {
			queryStrOrg = SHOP_PRODUCT_SQL;
		} else if ("vt_sales_sku".equals(tableName)) {
			queryStrOrg = SHOP_SKU_SQL;
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
	
	
	private static String SHOP_EXTEND_ORG_SQL = 
			"SELECT  \n"
			+ "cur.id code  \n"
			+ ",'' parent  \n"
			+ ",cur.name name  \n"
			+ ",cur.name_cns sname  \n"
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
	private static String SHOP_EXTEND_SUB_SQL = "SELECT  \n"
			+ "shop.id id \n"
			+ ",shop.code code \n"
			+ ",shop.name name \n"
			+ ",shop.name_cns name_cns \n"
			+ SqlOlapConstantShopExtend.SALES_SUB_SQL_SHOP
			+ "GROUP BY shop.id \n";
	
	private static String SHOP_EXTEND_SQL = 
			SHOP_EXTEND_ORG_SQL.replace("#CUR_SUB_SQL#", SHOP_EXTEND_SUB_SQL)
			.replace("#MOM_SUB_SQL#",  SHOP_EXTEND_SUB_SQL.replace("#date_condition#", "#mom_date_condition#"))
			.replace("#YOY_SUB_SQL#",  SHOP_EXTEND_SUB_SQL.replace("#date_condition#", "#yoy_date_condition#"));
	
	/**
	 * getSalesExtendByShops
	 * @param condition
	 * @param userInfoBean
	 * @return
	 * @throws BiException
	 */
	public List<ChartGridDisBean>getSalesExtendByShops(ConditionBean condition, UserInfoBean userInfoBean) throws BiException {
		Dimension dimension = Dimension.Shop;
		String tableName = SqlOlapSalesUtils.getTableName(condition, dimension);
		List<ChartGridDisBean> result = new ArrayList<>();
		if ("vt_sales_store".equals(tableName)) {
			String queryStrOrg = SHOP_EXTEND_SQL;
			
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
					
			result = SqlOlapSalesUtils.executeSqlAndMomYoy(
					queryStrOrg, 
					tableName,
					condition,
					dimension, 
					sqlOlapDaoSupport);
		}		
		return result;
	}

}
