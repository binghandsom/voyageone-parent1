package com.voyageone.bi.dao.sqlolap;

public class SqlOlapConstantSales {


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
			+ ",ifnull(cur.atv_n/yoy.atv_n-1,0) atv_n_y_r_u  \n";
	

	public static String SALES_SUB_SQL_SHOP =
			",sum(qty) qty_n \n"
			+ ",sum(amt) amt_n \n"
			+ ",sum(orders) order_n \n"
			+ ",ifnull(sum(amt)/sum(qty), 0) atv_n \n" 
			+ "FROM #schema##table_name# sale \n"
			+ "INNER JOIN #schema#vm_date date ON date.id=sale.date \n"
			+ "INNER JOIN #schema#vm_shop shop ON shop.id=sale.shop_id \n"
			+ "INNER JOIN #schema#vm_buy_channel buy_channel ON buy_channel.id=sale.buy_channel \n"
			+ "WHERE sale.amt<>0 AND #date_condition# #condition# \n";

	public static String SALES_SUB_SQL_MODEL =
			",sum(qty) qty_n \n"
			+ ",sum(amt) amt_n \n"
			+ ",sum(orders) order_n \n"
			+ ",ifnull(sum(amt)/sum(qty), 0) atv_n \n" 
			+ "FROM #schema##table_name# sale \n"
			+ "INNER JOIN #schema#vm_date date ON date.id=sale.date \n"
			+ "INNER JOIN #schema#vm_shop shop ON shop.id=sale.shop_id \n"
			+ "INNER JOIN #schema#vm_buy_channel buy_channel ON buy_channel.id=sale.buy_channel \n"
			+ "INNER JOIN #schema#vm_model model ON model.id=sale.model_id \n"
			+ "INNER JOIN #schema#vm_brand brand ON brand.id=model.brand_id \n"
			+ "WHERE sale.amt<>0 AND #date_condition# #condition# \n";	
	
	public static String SALES_SUB_SQL_PRODUCT =
			",sum(qty) qty_n \n"
			+ ",sum(amt) amt_n \n"
			+ ",sum(orders) order_n \n"
			+ ",ifnull(sum(amt)/sum(qty), 0) atv_n \n" 
			+ "FROM #schema##table_name# sale \n"
			+ "INNER JOIN #schema#vm_date date ON date.id=sale.date \n"
			+ "INNER JOIN #schema#vm_shop shop ON shop.id=sale.shop_id \n"
			+ "INNER JOIN #schema#vm_buy_channel buy_channel ON buy_channel.id=sale.buy_channel \n"
			+ "INNER JOIN #schema#vm_product product ON product.id=sale.product_id \n"
			+ "INNER JOIN #schema#vm_product_category_view p_category ON p_category.product_id=product.id \n" 
			+ "INNER JOIN #schema#vm_category category ON category.id=p_category.category_id  \n"
			+ "INNER JOIN #schema#vm_brand brand ON brand.id=product.brand_id \n"
			+ "WHERE sale.amt<>0 AND #date_condition# #condition# \n";
	
	public static String SALES_SUB_SQL_SKU =
			",sum(qty) qty_n \n"
			+ ",sum(amt) amt_n \n"
			+ ",sum(orders) order_n \n"
			+ ",ifnull(sum(amt)/sum(qty), 0) atv_n \n" 
			+ "FROM #schema##table_name# sale \n"
			+ "INNER JOIN #schema#vm_date date ON date.id=sale.date \n"
			+ "INNER JOIN #schema#vm_shop shop ON shop.id=sale.shop_id \n"
			+ "INNER JOIN #schema#vm_buy_channel buy_channel ON buy_channel.id=sale.buy_channel \n"
			+ "INNER JOIN #schema#vm_product product ON product.id=sale.product_id \n"
			+ "INNER JOIN #schema#vm_product_category_view p_category ON p_category.product_id=product.id \n" 
			+ "INNER JOIN #schema#vm_category category ON category.id=p_category.category_id \n"
			+ "INNER JOIN #schema#vm_brand brand ON brand.id=product.brand_id \n"
			+ "INNER JOIN #schema#vm_sku sku ON sku.id=sale.sku_id \n"
			+ "INNER JOIN #schema#vm_color_define color ON color.id=sku.main_color_id \n"
			+ "INNER JOIN #schema#vm_size size ON size.id=sku.size_id \n"
			+ "WHERE sale.amt<>0 AND #date_condition# #condition# \n";
	
}
