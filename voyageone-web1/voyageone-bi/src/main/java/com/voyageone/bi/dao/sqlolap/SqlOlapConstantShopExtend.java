package com.voyageone.bi.dao.sqlolap;

public class SqlOlapConstantShopExtend {


	public static String SALES_SUB_COLUMN =
			    ",ifnull(cur.qty_n,0) qty_n  \n"
			+ ",ifnull(mom.qty_n,0) qty_n_l  \n"
			+ ",ifnull(cur.qty_n/mom.qty_n,0) qty_n_l_r  \n"
			+ ",ifnull(cur.qty_n/mom.qty_n-1,0) qty_n_l_r_u  \n"
			+ ",ifnull(yoy.qty_n,0) qty_n_y  \n"
			+ ",ifnull(cur.qty_n/yoy.qty_n,0) qty_n_y_r  \n"
			+ ",ifnull(cur.qty_n/yoy.qty_n-1,0) qty_n_y_r_u  \n"
			
			+ ",ifnull(cur.amt_n,0) amt_n  \n"
			+ ",ifnull(mom.amt_n,0) amt_n_l  \n"
			+ ",ifnull(cur.amt_n/mom.amt_n,0) amt_n_l_r  \n"
			+ ",ifnull(cur.amt_n/mom.amt_n-1,0) amt_n_l_r_u  \n"
			+ ",ifnull(yoy.amt_n,0) amt_n_y  \n"
			+ ",ifnull(cur.amt_n/yoy.amt_n,0) amt_n_y_r  \n"
			+ ",ifnull(cur.amt_n/yoy.amt_n-1,0) amt_n_y_r_u  \n"
			
			+ ",ifnull(cur.order_n,0) order_n  \n"
			+ ",ifnull(mom.order_n,0) order_n_l  \n"
			+ ",ifnull(cur.order_n/mom.order_n,0) order_n_l_r  \n"
			+ ",ifnull(cur.order_n/mom.order_n-1,0) order_n_l_r_u  \n"
			+ ",ifnull(yoy.order_n,0) order_n_y  \n"
			+ ",ifnull(cur.order_n/yoy.order_n,0) order_n_y_r  \n"
			+ ",ifnull(cur.order_n/yoy.order_n-1,0) order_n_y_r_u  \n"
			
			+ ",ifnull(cur.atv_n,0) atv_n \n"
			+ ",ifnull(mom.atv_n,0) atv_n_l  \n"  
			+ ",ifnull(cur.atv_n/mom.atv_n,0) atv_n_l_r  \n"  
			+ ",ifnull(cur.atv_n/mom.atv_n-1,0) atv_n_l_r_u  \n"  
			+ ",ifnull(yoy.atv_n,0) atv_n_y  \n"
			+ ",ifnull(cur.atv_n/yoy.atv_n,0) atv_n_y_r  \n"  
			+ ",ifnull(cur.atv_n/yoy.atv_n-1,0) atv_n_y_r_u  \n"

			+ ",ifnull(cur.pv_n,0) pv_n  \n"
			+ ",ifnull(mom.pv_n,0) pv_n_l  \n"
			+ ",ifnull(cur.pv_n/mom.pv_n,0) pv_n_l_r  \n"
			+ ",ifnull(cur.pv_n/mom.pv_n-1,0) pv_n_l_r_u  \n"
			+ ",ifnull(yoy.pv_n,0) pv_n_y  \n"
			+ ",ifnull(cur.pv_n/yoy.pv_n,0) pv_n_y_r  \n"
			+ ",ifnull(cur.pv_n/yoy.pv_n-1,0) pv_n_y_r_u  \n"

			+ ",ifnull(cur.uv_n,0) uv_n  \n"
			+ ",ifnull(mom.uv_n,0) uv_n_l  \n"
			+ ",ifnull(cur.uv_n/mom.uv_n,0) uv_n_l_r  \n"
			+ ",ifnull(cur.uv_n/mom.uv_n-1,0) uv_n_l_r_u  \n"
			+ ",ifnull(yoy.uv_n,0) uv_n_y  \n"
			+ ",ifnull(cur.uv_n/yoy.uv_n,0) uv_n_y_r  \n"
			+ ",ifnull(cur.uv_n/yoy.uv_n-1,0) uv_n_y_r_u  \n"
			
			+ ",ifnull(cur.tr_n,0) tr_n  \n"
			+ ",ifnull(mom.tr_n,0) tr_n_l  \n"
			+ ",ifnull(cur.tr_n/mom.tr_n,0) tr_n_l_r  \n"
			+ ",ifnull(cur.tr_n/mom.tr_n-1,0) tr_n_l_r_u  \n"
			+ ",ifnull(yoy.tr_n,0) tr_n_y  \n"
			+ ",ifnull(cur.tr_n/yoy.tr_n,0) tr_n_y_r  \n"
			+ ",ifnull(cur.tr_n/yoy.tr_n-1,0) tr_n_y_r_u  \n"
			;
	

	public static String SALES_SUB_SQL_SHOP =
			",sum(num_case_paid) qty_n \n"
			+ ",sum(amt_paid) amt_n \n"
			+ ",sum(num_f_paid) order_n \n"
			+ ",ifnull(sum(amt_paid)/sum(num_case_paid), 0) atv_n \n" 
			+ ",sum(pv) pv_n \n"
			+ ",sum(uv) uv_n \n"
			+ ",avg(cvr_order) tr_n \n"
			+ "FROM #table_name#_extend sale \n"
			+ "INNER JOIN vm_date date ON date.id=sale.date \n"
			+ "INNER JOIN vm_shop shop ON shop.id=sale.shop_id \n"
			+ "INNER JOIN vm_buy_channel buy_channel ON buy_channel.id=sale.buy_channel \n"
			+ "WHERE #date_condition# #condition# \n";
	
}
