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

import com.voyageone.bi.dao.sqlolap.SqlOlapConstantSales;
import com.voyageone.bi.dao.sqlolap.SqlOlapDaoSupport;
import com.voyageone.bi.dao.sqlolap.SqlOlapSalesUtils;
import com.voyageone.bi.disbean.ChartGridDisBean;
import com.voyageone.bi.disbean.ConditionBean;
import com.voyageone.bi.commonutils.ConditionUtil.Dimension;
import com.voyageone.bi.tranbean.UserInfoBean;

@Service
public class SalesSkuTask {
	
	private static String SKU_SUB_SQL = "SELECT  \n"
			+ "sku.id id \n"
			+ ",sku.sku_code code \n"
			+ ",sku.product_id parent \n"
			+ ",sku.product_name name \n"
			+ SqlOlapConstantSales.SALES_SUB_SQL_SKU
			+ "GROUP BY sku.id \n";
	
	private static String SKU_SQL = 
			"SELECT  \n"
			+ "cur.id id  \n"
			+ ",cur.code code  \n"
			+ ",cur.parent parent  \n"
			+ ",cur.name name  \n"
			+ SqlOlapConstantSales.SALES_SUB_COLUMN
			+ "FROM (  \n"
			+ SKU_SUB_SQL
			+ " ) cur \n"
			+ "LEFT JOIN (\n"
			+ SKU_SUB_SQL.replace("#date_condition#", "#mom_date_condition#")
			+ " ) mom ON mom.id=cur.id \n"
			+ "LEFT JOIN (\n"
			+ SKU_SUB_SQL.replace("#date_condition#", "#yoy_date_condition#")
			+ " ) yoy ON yoy.id=cur.id \n"
			+ " ORDER BY #ORDER_COLUMN# \n"
			+ " #LIMIT_COLUMN# \n";
	
	@Autowired
	private SqlOlapDaoSupport sqlOlapDaoSupport;

	/**
	 * getTopSalesSkuByProduct
	 * @param condition
	 * @param userInfoBean
	 * @param i
	 * @return
	 */
	public List<ChartGridDisBean> getTopSalesSkuByProduct(ConditionBean condition, UserInfoBean userInfoBean, int topCount) {
		ConditionBean cur_condition = condition.createCopy();
		if (topCount>0) {
			cur_condition.setLimit(String.valueOf(topCount));
		}
		List<ChartGridDisBean> result = getSkuBeanLst(cur_condition, userInfoBean);
		return result;		
	}

	/**
	 * getSkuBeanLst
	 * @param condition
	 * @param userInfoBean
	 * @return
	 */
	public List<ChartGridDisBean> getSkuBeanLst(ConditionBean condition, UserInfoBean userInfoBean) {
		//data condition
		Dimension dimension = Dimension.SKU;
		String queryStrOrg = SKU_SQL;
		
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
			sord = condition.getSord();
		}
		String sortStr = sort_column + " " +  sord + ", code ASC";
		queryStrOrg = queryStrOrg.replaceAll("#ORDER_COLUMN#", sortStr);
		
		String tableName = SqlOlapSalesUtils.getTableName(condition, dimension);
		List<ChartGridDisBean> result = SqlOlapSalesUtils.executeSqlAndMomYoy(
				queryStrOrg, 
				tableName,
				condition,
				dimension, 
				sqlOlapDaoSupport);
		return result;
	}
}
