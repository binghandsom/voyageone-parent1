package com.voyageone.bi.task.sup;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyageone.bi.base.BiException;
import com.voyageone.bi.bean.DateBean;
import com.voyageone.bi.commonutils.ConditionUtil.Dimension;
import com.voyageone.bi.dao.DateInfoDao;
import com.voyageone.bi.dao.sqlolap.SqlOlapConstantSales;
import com.voyageone.bi.dao.sqlolap.SqlOlapConstantShopExtend;
import com.voyageone.bi.dao.sqlolap.SqlOlapDaoSupport;
import com.voyageone.bi.dao.sqlolap.SqlOlapSalesUtils;
import com.voyageone.bi.disbean.ChartGridDisBean;
import com.voyageone.bi.disbean.ConditionBean;
import com.voyageone.bi.tranbean.UserInfoBean;


@Service
public class SalesTimeTask {
//	private static Log logger = LogFactory.getLog(SalesTimeTask.class);
	
	private static String TIMELINE_ORG_SQL = 
			"SELECT  \n"
			+ " cur.id id  \n"
			+ ",cur.code code  \n"
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

	// day
	private static String TIMELINE_DAY_CUR_SUB_SQL = "SELECT  \n"
			+ "date.id id \n"
			+ ",date.code code \n"
			+ ",concat(date.month, '-', date.day) name \n"
			+ "#SALES_SUB_SQL#" //
			+ "GROUP BY date.id \n";
	private static String TIMELINE_DAY_MOM_SHOP_SQL = "SELECT  \n"
			+ "date_format(DATE_ADD(STR_TO_DATE(date.id,'%Y%m%d'), INTERVAL 1 DAY),'%Y%m%d') id  \n"
			+ "#SALES_SUB_SQL#" 
			+ "GROUP BY date.id \n";
	private static String TIMELINE_DAY_YOY_SUB_SQL = "SELECT  \n"
			+ "date_format(DATE_ADD(STR_TO_DATE(date.id,'%Y%m%d'), INTERVAL 1 YEAR),'%Y%m%d') id  \n"
			+ "#SALES_SUB_SQL#" 
			+ "GROUP BY date.id \n";
	
	//vt_sales_store
	private static String TIMELINE_DAY_SHOP_SQL = 
			TIMELINE_ORG_SQL.replace("#CUR_SUB_SQL#", TIMELINE_DAY_CUR_SUB_SQL.replace("#SALES_SUB_SQL#", SqlOlapConstantSales.SALES_SUB_SQL_SHOP))
			.replace("#MOM_SUB_SQL#",  TIMELINE_DAY_MOM_SHOP_SQL.replace("#SALES_SUB_SQL#", SqlOlapConstantSales.SALES_SUB_SQL_SHOP).replace("#date_condition#", "#mom_date_condition#"))
			.replace("#YOY_SUB_SQL#",  TIMELINE_DAY_YOY_SUB_SQL.replace("#SALES_SUB_SQL#", SqlOlapConstantSales.SALES_SUB_SQL_SHOP).replace("#date_condition#", "#yoy_date_condition#"));
	//vt_sales_model
	private static String TIMELINE_DAY_MODEL_SQL = 
			TIMELINE_ORG_SQL.replace("#CUR_SUB_SQL#", TIMELINE_DAY_CUR_SUB_SQL.replace("#SALES_SUB_SQL#", SqlOlapConstantSales.SALES_SUB_SQL_MODEL))
			.replace("#MOM_SUB_SQL#",  TIMELINE_DAY_MOM_SHOP_SQL.replace("#SALES_SUB_SQL#", SqlOlapConstantSales.SALES_SUB_SQL_MODEL).replace("#date_condition#", "#mom_date_condition#"))
			.replace("#YOY_SUB_SQL#",  TIMELINE_DAY_YOY_SUB_SQL.replace("#SALES_SUB_SQL#", SqlOlapConstantSales.SALES_SUB_SQL_MODEL).replace("#date_condition#", "#yoy_date_condition#"));
	//vt_sales_product
	private static String TIMELINE_DAY_PRODUCT_SQL = 
			TIMELINE_ORG_SQL.replace("#CUR_SUB_SQL#", TIMELINE_DAY_CUR_SUB_SQL.replace("#SALES_SUB_SQL#", SqlOlapConstantSales.SALES_SUB_SQL_PRODUCT))
			.replace("#MOM_SUB_SQL#",  TIMELINE_DAY_MOM_SHOP_SQL.replace("#SALES_SUB_SQL#", SqlOlapConstantSales.SALES_SUB_SQL_PRODUCT).replace("#date_condition#", "#mom_date_condition#"))
			.replace("#YOY_SUB_SQL#",  TIMELINE_DAY_YOY_SUB_SQL.replace("#SALES_SUB_SQL#", SqlOlapConstantSales.SALES_SUB_SQL_PRODUCT).replace("#date_condition#", "#yoy_date_condition#"));
	//vt_sales_sku
	private static String TIMELINE_DAY_SKU_SQL = 
			TIMELINE_ORG_SQL.replace("#CUR_SUB_SQL#", TIMELINE_DAY_CUR_SUB_SQL.replace("#SALES_SUB_SQL#", SqlOlapConstantSales.SALES_SUB_SQL_SKU))
			.replace("#MOM_SUB_SQL#",  TIMELINE_DAY_MOM_SHOP_SQL.replace("#SALES_SUB_SQL#", SqlOlapConstantSales.SALES_SUB_SQL_SKU).replace("#date_condition#", "#mom_date_condition#"))
			.replace("#YOY_SUB_SQL#",  TIMELINE_DAY_YOY_SUB_SQL.replace("#SALES_SUB_SQL#", SqlOlapConstantSales.SALES_SUB_SQL_SKU).replace("#date_condition#", "#yoy_date_condition#"));
	
	
	// month
	private static String TIMELINE_MONTH_CUR_SUB_SQL = "SELECT  \n"
			+ "date.month_id id \n"
			+ ",date.month_code code \n"
			+ ",date.month_code name \n"
			+ "#SALES_SUB_SQL#" //
			+ "GROUP BY date.month_id \n";
	private static String TIMELINE_MONTH_MOM_SHOP_SQL = "SELECT  \n"
			+ "date_format(DATE_ADD(STR_TO_DATE(concat(date.month_id,'01'), '%Y%m%d'), INTERVAL 1 MONTH),'%Y%m') id  \n"
			+ "#SALES_SUB_SQL#" 
			+ "GROUP BY date.month_id \n";
	private static String TIMELINE_MONTH_YOY_SUB_SQL = "SELECT  \n"
			+ "date_format(DATE_ADD(STR_TO_DATE(concat(date.month_id,'01'), '%Y%m%d'), INTERVAL 1 YEAR), '%Y%m') id  \n"
			+ "#SALES_SUB_SQL#" 
			+ "GROUP BY date.month_id \n";
	
	//vt_sales_store
	private static String TIMELINE_MONTH_SHOP_SQL = 
			TIMELINE_ORG_SQL.replace("#CUR_SUB_SQL#", TIMELINE_MONTH_CUR_SUB_SQL.replace("#SALES_SUB_SQL#", SqlOlapConstantSales.SALES_SUB_SQL_SHOP))
			.replace("#MOM_SUB_SQL#",  TIMELINE_MONTH_MOM_SHOP_SQL.replace("#SALES_SUB_SQL#", SqlOlapConstantSales.SALES_SUB_SQL_SHOP).replace("#date_condition#", "#mom_date_condition#"))
			.replace("#YOY_SUB_SQL#",  TIMELINE_MONTH_YOY_SUB_SQL.replace("#SALES_SUB_SQL#", SqlOlapConstantSales.SALES_SUB_SQL_SHOP).replace("#date_condition#", "#yoy_date_condition#"));
	//vt_sales_model
	private static String TIMELINE_MONTH_MODEL_SQL = 
			TIMELINE_ORG_SQL.replace("#CUR_SUB_SQL#", TIMELINE_MONTH_CUR_SUB_SQL.replace("#SALES_SUB_SQL#", SqlOlapConstantSales.SALES_SUB_SQL_MODEL))
			.replace("#MOM_SUB_SQL#",  TIMELINE_MONTH_MOM_SHOP_SQL.replace("#SALES_SUB_SQL#", SqlOlapConstantSales.SALES_SUB_SQL_MODEL).replace("#date_condition#", "#mom_date_condition#"))
			.replace("#YOY_SUB_SQL#",  TIMELINE_MONTH_YOY_SUB_SQL.replace("#SALES_SUB_SQL#", SqlOlapConstantSales.SALES_SUB_SQL_MODEL).replace("#date_condition#", "#yoy_date_condition#"));
	//vt_sales_product
	private static String TIMELINE_MONTH_PRODUCT_SQL = 
			TIMELINE_ORG_SQL.replace("#CUR_SUB_SQL#", TIMELINE_MONTH_CUR_SUB_SQL.replace("#SALES_SUB_SQL#", SqlOlapConstantSales.SALES_SUB_SQL_PRODUCT))
			.replace("#MOM_SUB_SQL#",  TIMELINE_MONTH_MOM_SHOP_SQL.replace("#SALES_SUB_SQL#", SqlOlapConstantSales.SALES_SUB_SQL_PRODUCT).replace("#date_condition#", "#mom_date_condition#"))
			.replace("#YOY_SUB_SQL#",  TIMELINE_MONTH_YOY_SUB_SQL.replace("#SALES_SUB_SQL#", SqlOlapConstantSales.SALES_SUB_SQL_PRODUCT).replace("#date_condition#", "#yoy_date_condition#"));
	//vt_sales_sku
	private static String TIMELINE_MONTH_SKU_SQL = 
			TIMELINE_ORG_SQL.replace("#CUR_SUB_SQL#", TIMELINE_MONTH_CUR_SUB_SQL.replace("#SALES_SUB_SQL#", SqlOlapConstantSales.SALES_SUB_SQL_SKU))
			.replace("#MOM_SUB_SQL#",  TIMELINE_DAY_MOM_SHOP_SQL.replace("#SALES_SUB_SQL#", SqlOlapConstantSales.SALES_SUB_SQL_SKU).replace("#date_condition#", "#mom_date_condition#"))
			.replace("#YOY_SUB_SQL#",  TIMELINE_MONTH_YOY_SUB_SQL.replace("#SALES_SUB_SQL#", SqlOlapConstantSales.SALES_SUB_SQL_SKU).replace("#date_condition#", "#yoy_date_condition#"));
	
	// year
	private static String TIMELINE_YEAR_CUR_SUB_SQL = "SELECT  \n"
			+ "date.Year id \n"
			+ ",date.Year code \n"
			+ ",date.Year name \n"
			+ "#SALES_SUB_SQL#" //
			+ "GROUP BY date.Year \n";
	private static String TIMELINE_YEAR_MOM_SHOP_SQL = "SELECT  \n"
			+ "date_format(DATE_ADD(STR_TO_DATE(concat(date.Year,'0101'), '%Y%m%d'), INTERVAL 1 YEAR),'%Y') id  \n"
			+ "#SALES_SUB_SQL#" 
			+ "GROUP BY date.Year \n";
	private static String TIMELINE_YEAR_YOY_SUB_SQL = "SELECT  \n"
			+ "date_format(DATE_ADD(STR_TO_DATE(concat(date.Year,'0101'), '%Y%m%d'), INTERVAL 1 YEAR), '%Y') id  \n"
			+ "#SALES_SUB_SQL#" 
			+ "GROUP BY date.Year \n";
	
	//vt_sales_store
	private static String TIMELINE_YEAR_SHOP_SQL = 
			TIMELINE_ORG_SQL.replace("#CUR_SUB_SQL#", TIMELINE_YEAR_CUR_SUB_SQL.replace("#SALES_SUB_SQL#", SqlOlapConstantSales.SALES_SUB_SQL_SHOP))
			.replace("#MOM_SUB_SQL#",  TIMELINE_YEAR_MOM_SHOP_SQL.replace("#SALES_SUB_SQL#", SqlOlapConstantSales.SALES_SUB_SQL_SHOP).replace("#date_condition#", "#mom_date_condition#"))
			.replace("#YOY_SUB_SQL#",  TIMELINE_YEAR_YOY_SUB_SQL.replace("#SALES_SUB_SQL#", SqlOlapConstantSales.SALES_SUB_SQL_SHOP).replace("#date_condition#", "#yoy_date_condition#"));
	//vt_sales_model
	private static String TIMELINE_YEAR_MODEL_SQL = 
			TIMELINE_ORG_SQL.replace("#CUR_SUB_SQL#", TIMELINE_YEAR_CUR_SUB_SQL.replace("#SALES_SUB_SQL#", SqlOlapConstantSales.SALES_SUB_SQL_MODEL))
			.replace("#MOM_SUB_SQL#",  TIMELINE_YEAR_MOM_SHOP_SQL.replace("#SALES_SUB_SQL#", SqlOlapConstantSales.SALES_SUB_SQL_MODEL).replace("#date_condition#", "#mom_date_condition#"))
			.replace("#YOY_SUB_SQL#",  TIMELINE_YEAR_YOY_SUB_SQL.replace("#SALES_SUB_SQL#", SqlOlapConstantSales.SALES_SUB_SQL_MODEL).replace("#date_condition#", "#yoy_date_condition#"));
	//vt_sales_product
	private static String TIMELINE_YEAR_PRODUCT_SQL = 
			TIMELINE_ORG_SQL.replace("#CUR_SUB_SQL#", TIMELINE_YEAR_CUR_SUB_SQL.replace("#SALES_SUB_SQL#", SqlOlapConstantSales.SALES_SUB_SQL_PRODUCT))
			.replace("#MOM_SUB_SQL#",  TIMELINE_YEAR_MOM_SHOP_SQL.replace("#SALES_SUB_SQL#", SqlOlapConstantSales.SALES_SUB_SQL_PRODUCT).replace("#date_condition#", "#mom_date_condition#"))
			.replace("#YOY_SUB_SQL#",  TIMELINE_YEAR_YOY_SUB_SQL.replace("#SALES_SUB_SQL#", SqlOlapConstantSales.SALES_SUB_SQL_PRODUCT).replace("#date_condition#", "#yoy_date_condition#"));
	//vt_sales_sku
	private static String TIMELINE_YEAR_SKU_SQL = 
			TIMELINE_ORG_SQL.replace("#CUR_SUB_SQL#", TIMELINE_YEAR_CUR_SUB_SQL.replace("#SALES_SUB_SQL#", SqlOlapConstantSales.SALES_SUB_SQL_SKU))
			.replace("#MOM_SUB_SQL#",  TIMELINE_YEAR_MOM_SHOP_SQL.replace("#SALES_SUB_SQL#", SqlOlapConstantSales.SALES_SUB_SQL_SKU).replace("#date_condition#", "#mom_date_condition#"))
			.replace("#YOY_SUB_SQL#",  TIMELINE_YEAR_YOY_SUB_SQL.replace("#SALES_SUB_SQL#", SqlOlapConstantSales.SALES_SUB_SQL_SKU).replace("#date_condition#", "#yoy_date_condition#"));
	
	
	@Autowired
	private SqlOlapDaoSupport sqlOlapDaoSupport;
	
	@Autowired
	private DateInfoDao dateInfoDao;
	
	/**
	 * getSalesTimeLineData
	 * @param condition
	 * @param userInfoBean
	 * @return
	 * @throws BiException
	 */
	public List<ChartGridDisBean> getSalesTimeLineData(ConditionBean condition, UserInfoBean userInfoBean) throws BiException{
		Dimension dimension = Dimension.TimeLine;
		String queryStrOrg = "";
		String tableName = SqlOlapSalesUtils.getTableName(condition, dimension);
		switch(condition.getTimeType()) {
		case Year:
			queryStrOrg = TIMELINE_YEAR_SHOP_SQL;
			if ("vt_sales_model".equals(tableName)) {
				queryStrOrg = TIMELINE_YEAR_MODEL_SQL;
			} else if ("vt_sales_product".equals(tableName)) {
				queryStrOrg = TIMELINE_YEAR_PRODUCT_SQL;
			} else if ("vt_sales_sku".equals(tableName)) {
				queryStrOrg = TIMELINE_YEAR_SKU_SQL;
			}
			break;
		case Month:
			queryStrOrg = TIMELINE_MONTH_SHOP_SQL;
			if ("vt_sales_model".equals(tableName)) {
				queryStrOrg = TIMELINE_MONTH_MODEL_SQL;
			} else if ("vt_sales_product".equals(tableName)) {
				queryStrOrg = TIMELINE_MONTH_PRODUCT_SQL;
			} else if ("vt_sales_sku".equals(tableName)) {
				queryStrOrg = TIMELINE_MONTH_SKU_SQL;
			}
			break;
		case Day:
			queryStrOrg = TIMELINE_DAY_SHOP_SQL;
			if ("vt_sales_model".equals(tableName)) {
				queryStrOrg = TIMELINE_DAY_MODEL_SQL;
			} else if ("vt_sales_product".equals(tableName)) {
				queryStrOrg = TIMELINE_DAY_PRODUCT_SQL;
			} else if ("vt_sales_sku".equals(tableName)) {
				queryStrOrg = TIMELINE_DAY_SKU_SQL;
			}
			break;
		}
		
		// limit
		queryStrOrg = queryStrOrg.replaceAll("#LIMIT_COLUMN#", "");
		
		String sortStr = "code ASC";
		queryStrOrg = queryStrOrg.replaceAll("#ORDER_COLUMN#", sortStr);
		
		List<ChartGridDisBean> chartGridDisBeanList = SqlOlapSalesUtils.executeSqlAndMomYoy(
				queryStrOrg, 
				tableName,
				condition,
				dimension, 
				sqlOlapDaoSupport);

		List<ChartGridDisBean> result = mergeTimeLineData(condition, chartGridDisBeanList);
		return result;
	}
	
	
	private static String TIMELINE_EXTEND_ORG_SQL = 
			"SELECT  \n"
			+ " cur.id id  \n"
			+ ",cur.code code  \n"
			+ ",cur.name name  \n"
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

	// day
	private static String TIMELINE_EXTEND_DAY_CUR_SUB_SQL = "SELECT  \n"
			+ "date.id id \n"
			+ ",date.code code \n"
			+ ",concat(date.month, '-', date.day) name \n"
			+ "#SALES_SUB_SQL#" //
			+ "GROUP BY date.id \n";
	private static String TIMELINE_EXTEND_DAY_MOM_SHOP_SQL = "SELECT  \n"
			+ "date_format(DATE_ADD(STR_TO_DATE(date.id,'%Y%m%d'), INTERVAL 1 DAY),'%Y%m%d') id  \n"
			+ "#SALES_SUB_SQL#" 
			+ "GROUP BY date.id \n";
	private static String TIMELINE_EXTEND_DAY_YOY_SUB_SQL = "SELECT  \n"
			+ "date_format(DATE_ADD(STR_TO_DATE(date.id,'%Y%m%d'), INTERVAL 1 YEAR),'%Y%m%d') id  \n"
			+ "#SALES_SUB_SQL#" 
			+ "GROUP BY date.id \n";
	
	//vt_sales_store
	private static String TIMELINE_EXTEND_DAY_SHOP_SQL = 
			TIMELINE_EXTEND_ORG_SQL.replace("#CUR_SUB_SQL#", TIMELINE_EXTEND_DAY_CUR_SUB_SQL.replace("#SALES_SUB_SQL#", SqlOlapConstantShopExtend.SALES_SUB_SQL_SHOP))
			.replace("#MOM_SUB_SQL#",  TIMELINE_EXTEND_DAY_MOM_SHOP_SQL.replace("#SALES_SUB_SQL#", SqlOlapConstantShopExtend.SALES_SUB_SQL_SHOP).replace("#date_condition#", "#mom_date_condition#"))
			.replace("#YOY_SUB_SQL#",  TIMELINE_EXTEND_DAY_YOY_SUB_SQL.replace("#SALES_SUB_SQL#", SqlOlapConstantShopExtend.SALES_SUB_SQL_SHOP).replace("#date_condition#", "#yoy_date_condition#"));
	
	
	// month
	private static String TIMELINE_EXTEND_MONTH_CUR_SUB_SQL = "SELECT  \n"
			+ "date.month_id id \n"
			+ ",date.month_code code \n"
			+ ",date.month_code name \n"
			+ "#SALES_SUB_SQL#" //
			+ "GROUP BY date.month_id \n";
	private static String TIMELINE_EXTEND_MONTH_MOM_SHOP_SQL = "SELECT  \n"
			+ "date_format(DATE_ADD(STR_TO_DATE(concat(date.month_id,'01'), '%Y%m%d'), INTERVAL 1 MONTH),'%Y%m') id  \n"
			+ "#SALES_SUB_SQL#" 
			+ "GROUP BY date.month_id \n";
	private static String TIMELINE_EXTEND_MONTH_YOY_SUB_SQL = "SELECT  \n"
			+ "date_format(DATE_ADD(STR_TO_DATE(concat(date.month_id,'01'), '%Y%m%d'), INTERVAL 1 YEAR), '%Y%m') id  \n"
			+ "#SALES_SUB_SQL#" 
			+ "GROUP BY date.month_id \n";
	
	//vt_sales_store
	private static String TIMELINE_EXTEND_MONTH_SHOP_SQL = 
			TIMELINE_EXTEND_ORG_SQL.replace("#CUR_SUB_SQL#", TIMELINE_EXTEND_MONTH_CUR_SUB_SQL.replace("#SALES_SUB_SQL#", SqlOlapConstantShopExtend.SALES_SUB_SQL_SHOP))
			.replace("#MOM_SUB_SQL#",  TIMELINE_EXTEND_MONTH_MOM_SHOP_SQL.replace("#SALES_SUB_SQL#", SqlOlapConstantShopExtend.SALES_SUB_SQL_SHOP).replace("#date_condition#", "#mom_date_condition#"))
			.replace("#YOY_SUB_SQL#",  TIMELINE_EXTEND_MONTH_YOY_SUB_SQL.replace("#SALES_SUB_SQL#", SqlOlapConstantShopExtend.SALES_SUB_SQL_SHOP).replace("#date_condition#", "#yoy_date_condition#"));

	// year
	private static String TIMELINE_EXTEND_YEAR_CUR_SUB_SQL = "SELECT  \n"
			+ "date.Year id \n"
			+ ",date.Year code \n"
			+ ",date.Year name \n"
			+ "#SALES_SUB_SQL#" //
			+ "GROUP BY date.Year \n";
	private static String TIMELINE_EXTEND_YEAR_MOM_SHOP_SQL = "SELECT  \n"
			+ "date_format(DATE_ADD(STR_TO_DATE(concat(date.Year,'0101'), '%Y%m%d'), INTERVAL 1 YEAR),'%Y') id  \n"
			+ "#SALES_SUB_SQL#" 
			+ "GROUP BY date.Year \n";
	private static String TIMELINE_EXTEND_YEAR_YOY_SUB_SQL = "SELECT  \n"
			+ "date_format(DATE_ADD(STR_TO_DATE(concat(date.Year,'0101'), '%Y%m%d'), INTERVAL 1 YEAR), '%Y') id  \n"
			+ "#SALES_SUB_SQL#" 
			+ "GROUP BY date.Year \n";
	
	//vt_sales_store
	private static String TIMELINE_EXTEND_YEAR_SHOP_SQL = 
			TIMELINE_EXTEND_ORG_SQL.replace("#CUR_SUB_SQL#", TIMELINE_EXTEND_YEAR_CUR_SUB_SQL.replace("#SALES_SUB_SQL#", SqlOlapConstantShopExtend.SALES_SUB_SQL_SHOP))
			.replace("#MOM_SUB_SQL#",  TIMELINE_EXTEND_YEAR_MOM_SHOP_SQL.replace("#SALES_SUB_SQL#", SqlOlapConstantShopExtend.SALES_SUB_SQL_SHOP).replace("#date_condition#", "#mom_date_condition#"))
			.replace("#YOY_SUB_SQL#",  TIMELINE_EXTEND_YEAR_YOY_SUB_SQL.replace("#SALES_SUB_SQL#", SqlOlapConstantShopExtend.SALES_SUB_SQL_SHOP).replace("#date_condition#", "#yoy_date_condition#"));

	/**
	 * getSalesExtendTimeLineData
	 * @param condition
	 * @param userInfoBean
	 * @return
	 * @throws BiException
	 */
	public List<ChartGridDisBean> getSalesExtendTimeLineData(ConditionBean condition, UserInfoBean userInfoBean) throws BiException{
		List<ChartGridDisBean> result = new ArrayList<ChartGridDisBean>();
		Dimension dimension = Dimension.TimeLine;
		String queryStrOrg = "";
		String tableName = SqlOlapSalesUtils.getTableName(condition, dimension);
		if ("vt_sales_store".equals(tableName)) {
			switch(condition.getTimeType()) {
			case Year:
				queryStrOrg = TIMELINE_EXTEND_YEAR_SHOP_SQL;
				break;
			case Month:
				queryStrOrg = TIMELINE_EXTEND_MONTH_SHOP_SQL;
				break;
			case Day:
				queryStrOrg = TIMELINE_EXTEND_DAY_SHOP_SQL;
				break;
			}
			// limit
			queryStrOrg = queryStrOrg.replaceAll("#LIMIT_COLUMN#", "");
			
			String sortStr = "code ASC";
			queryStrOrg = queryStrOrg.replaceAll("#ORDER_COLUMN#", sortStr);
			
			List<ChartGridDisBean> chartGridDisBeanList = SqlOlapSalesUtils.executeSqlAndMomYoy(
					queryStrOrg, 
					tableName,
					condition,
					dimension, 
					sqlOlapDaoSupport);

			result = mergeTimeLineData(condition, chartGridDisBeanList);
		}
		return result;
	}
	
	/**
	 * mergeTimeLineData
	 * @param condition
	 * @param chartGridDisBeanList
	 * @return
	 */
	private List<ChartGridDisBean> mergeTimeLineData(ConditionBean condition, List<ChartGridDisBean> chartGridDisBeanList) {
		List<DateBean> dataList = dateInfoDao.getDataList(condition.getTimeType(),
				Integer.parseInt(condition.getTime_start()),
				Integer.parseInt(condition.getTime_end()));

		List<ChartGridDisBean> result = new ArrayList<ChartGridDisBean>();
		for (DateBean dateBean : dataList) {
			ChartGridDisBean curBean = null;
			for (ChartGridDisBean bean : chartGridDisBeanList) {
				if (bean.getId().equals(String.valueOf(dateBean.id))) {
					curBean = bean;
					break;
				}
			}
			if (curBean == null) {
				curBean = new ChartGridDisBean();
				curBean.setId(String.valueOf(dateBean.id));
				switch(condition.getTimeType()) {
				case Year:
					curBean.setValue(dateBean.code);
					curBean.setTitle(dateBean.code);
					break;
				case Month:
					curBean.setValue(dateBean.code);
					curBean.setTitle(dateBean.code);
					break;
				case Day:
					curBean.setValue(dateBean.code);
					// ",concat(date.month, '-', date.day) name \n"
					curBean.setTitle("" + dateBean.month + '-' + dateBean.day);
					break;
				}
			}
			curBean.setType(condition.getTimeType().toString());
			result.add(curBean);
		}
		return result;
	}


}
