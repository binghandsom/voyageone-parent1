package com.voyageone.bi.task.sup;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyageone.bi.base.BiException;
import com.voyageone.bi.commonutils.ConditionUtil.Dimension;
import com.voyageone.bi.dao.sqlolap.SqlOlapConstantShopExtend;
import com.voyageone.bi.dao.sqlolap.SqlOlapDaoSupport;
import com.voyageone.bi.dao.sqlolap.SqlOlapSalesUtils;
import com.voyageone.bi.disbean.ChartGridDisBean;
import com.voyageone.bi.disbean.ConditionBean;
import com.voyageone.bi.tranbean.UserInfoBean;


@Service
public class SalesBuyChannelTask {
	
	
	private static String BUYCHANNEL_ORG_SQL = 
			"SELECT  \n"
			+ "cur.id code  \n"
			+ ",'' parent  \n"
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

	//vt_sales_store
	private static String BUYCHANNEL_SHOP_SUB_SQL = "SELECT  \n"
			+ "buy_channel.id id \n"
			+ ",buy_channel.code code \n"
			+ ",buy_channel.name name \n"
			+ SqlOlapConstantShopExtend.SALES_SUB_SQL_SHOP
			+ "  AND buy_channel.id <> 0 \n"
			+ "GROUP BY buy_channel.id \n";
	
	private static String BUYCHANNEL_SHOP_SQL = 
			BUYCHANNEL_ORG_SQL.replace("#CUR_SUB_SQL#", BUYCHANNEL_SHOP_SUB_SQL)
			.replace("#MOM_SUB_SQL#",  BUYCHANNEL_SHOP_SUB_SQL.replace("#date_condition#", "#mom_date_condition#"))
			.replace("#YOY_SUB_SQL#",  BUYCHANNEL_SHOP_SUB_SQL.replace("#date_condition#", "#yoy_date_condition#"));

	@Autowired
	private SqlOlapDaoSupport sqlOlapDaoSupport;
	

	
	/**
	 * getSalesBrandInfo
	 * @param condition
	 * @param userInfoBean
	 * @return
	 * @throws BiException
	 */
	public List<ChartGridDisBean> getSalesByBuyChannel(ConditionBean condition, UserInfoBean userInfoBean) throws BiException{
		Dimension dimension = Dimension.BuyChannel;
		String tableName = SqlOlapSalesUtils.getTableName(condition, dimension);
		List<ChartGridDisBean> result = new ArrayList<ChartGridDisBean>();
		if ("vt_sales_store".equals(tableName)) {
			String queryStrOrg = BUYCHANNEL_SHOP_SQL;
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
