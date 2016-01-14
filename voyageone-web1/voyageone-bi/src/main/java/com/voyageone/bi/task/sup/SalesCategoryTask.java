package com.voyageone.bi.task.sup;

import java.util.List;

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
public class SalesCategoryTask {

	private static String CATEGORY_ORG_SQL = 
			"SELECT  \n"
			+ "CONCAT(cur.id, '-', cur.level, '-', cur.isParent) code  \n"
			+ ",'' parent  \n"
			+ ",cur.name name  \n"
			+ ",cur.isParent type  \n"
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

	//vt_sales_product
	private static String CATEGORY_PRODUCT_SUB_SQL = "SELECT  \n"
			+ "p_category.category#cat_level#_id id \n"
			+ ",p_category.category#cat_level#_name name \n"
			+ ",p_category.category#cat_level#_level level \n"
			+ ",p_category.is_parent#cat_level# isParent \n"
			+ SqlOlapConstantSales.SALES_SUB_SQL_PRODUCT
			+ "GROUP BY p_category.category#cat_level#_id \n";
	
	private static String CATEGORY_PRODUCT_SQL = 
			CATEGORY_ORG_SQL.replace("#CUR_SUB_SQL#", CATEGORY_PRODUCT_SUB_SQL)
			.replace("#MOM_SUB_SQL#",  CATEGORY_PRODUCT_SUB_SQL.replace("#date_condition#", "#mom_date_condition#"))
			.replace("#YOY_SUB_SQL#",  CATEGORY_PRODUCT_SUB_SQL.replace("#date_condition#", "#yoy_date_condition#"));
	
	//vt_sales_sku
	private static String CATEGORY_SKU_SUB_SQL = "SELECT  \n"
			+ "p_category.category#cat_level#_id id \n"
			+ ",p_category.category#cat_level#_name name \n"
			+ ",p_category.category#cat_level#_level level \n"
			+ ",p_category.is_parent#cat_level# isParent \n"
			+ SqlOlapConstantSales.SALES_SUB_SQL_SKU
			+ "GROUP BY p_category.category#cat_level#_id \n";
	
	private static String CATEGORY_SKU_SQL = 
			CATEGORY_ORG_SQL.replace("#CUR_SUB_SQL#", CATEGORY_SKU_SUB_SQL)
			.replace("#MOM_SUB_SQL#",  CATEGORY_SKU_SUB_SQL.replace("#date_condition#", "#mom_date_condition#"))
			.replace("#YOY_SUB_SQL#",  CATEGORY_SKU_SUB_SQL.replace("#date_condition#", "#yoy_date_condition#"));	

	@Autowired
	private SqlOlapDaoSupport sqlOlapDaoSupport;

	/**
	 * getTopSalesCategoryInfo
	 * @param condition
	 * @param userInfoBean
	 * @return
	 * @throws BiException
	 */
	public List<ChartGridDisBean> getTopSalesCategoryInfo(ConditionBean condition, UserInfoBean userInfoBean, int topCount) throws BiException{
		ConditionBean cur_condition = condition.createCopy();
		if (topCount>0) {
			cur_condition.setLimit(String.valueOf(topCount));
		}
		List<ChartGridDisBean> result = getCategoryBeanLst(cur_condition, userInfoBean);
		return result;
	}
	
	/**
	 * getCategoryBeanLst
	 * @param condition
	 * @param userInfoBean
	 * @return
	 * @throws BiException
	 */
	public List<ChartGridDisBean> getCategoryBeanLst(ConditionBean condition, UserInfoBean userInfoBean) throws BiException{
		Dimension dimension = Dimension.Category;
		String queryStrOrg = CATEGORY_PRODUCT_SQL;
		String tableName = SqlOlapSalesUtils.getTableName(condition, dimension);
		if ("vt_sales_sku".equals(tableName)) {
			queryStrOrg = CATEGORY_SKU_SQL;
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
		
		//cat_level
		condition.getCategory_idArray();
		List<String> condition_categoryList = condition.getCategory_idArray();
		String cat_level ="1";
		for (String condition_category : condition_categoryList) {
			String[] condition_category_arr = condition_category.split("-");
			String parent_cat_level = condition_category_arr[1];
			String isParent = condition_category_arr[2];
			if (isParent != null && "1".equals(isParent)) {
				try {
					cat_level = String.valueOf(Integer.parseInt(parent_cat_level) + 1);
				} catch(Exception ex) {
					ex.printStackTrace();
				}
			} else if (isParent != null && "0".equals(isParent)) {
				cat_level = parent_cat_level;
			}
		}
		queryStrOrg = queryStrOrg.replaceAll("#cat_level#", cat_level);

		List<ChartGridDisBean> result = SqlOlapSalesUtils.executeSqlAndMomYoy(
				queryStrOrg, 
				tableName,
				condition,
				dimension, 
				sqlOlapDaoSupport);
		return result;
	}

}
