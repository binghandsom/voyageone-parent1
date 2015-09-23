package com.voyageone.batch.bi.spider.constants.data;


public interface DataSearchConstants {

	//商品情况数据的场合(最近7天)TM
	public static final int TYPE_PRODUCT_SHOW_TM = 11;
	//店铺情况数据的场合(最近7天)TM
	public static final int TYPE_STORE_SHOW_TM = 12;

	//商品情况数据的场合(初始化)JD
	public static final int TYPE_PRODUCT_SHOW_JD = 21;
	//店铺情况数据的场合(初始化)JD
	public static final int TYPE_PRODUCT_SHOW_JD_PC = 22;
	//商品情况数据的场合(最近7天)JD
	public static final int TYPE_PRODUCT_SHOW_JD_MOBILE = 23;
	//店铺情况数据的场合(最近7天)JD
	public static final int TYPE_STORE_SHOW_JD = 24;

	//商品情况数据的场合(最近7天)
	public static final String COLUMN_CONDITION_SHOP = "vt_sales_shop";
	//店铺情况数据的场合(最近7天)
	public static final String COLUMN_CONDITION_PRODUCT = "vt_sales_product";

	//店铺情况数据的场合(最近7天)
	public static final String COLUMN_TYPE_PRODUCT = "resultData_summary";
	//店铺情况数据的场合(最近7天)
	public static final String COLUMN_TYPE_PRODUCT_PC = "resultData_summary_pc";
	//店铺情况数据的场合(最近7天)
	public static final String COLUMN_TYPE_PRODUCT_MOBILE = "resultData_summary_mobile";

	public static final int THREAD_SLEEP = 900;
	public static final int THREAD_SLEEP_INPUT = 200;
	public static final int THREAD_SLEEP_LOGIN = 5000;
	public static final int THREAD_SLEEP_RELOAD = 400000;

	public static final String KEY_JD_TABLE_COLUMN = "cor_column_table_name";
	public static final String KEY_JD_WEB_TYPE = "column_web_type";
	public static final String KEY_JD_WEB_COLUMN = "column_web_name";

	public static final int CASE_TM = 100;
	public static final int CASE_JD = 200;

	public static final String JD_COLUMN = "column";

	public static final String JD_VALUE = "value";

	public static final int ECOMM_TM = 1;
	public static final int ECOMM_TB = 2;
	public static final int ECOMM_OF = 3;
	public static final int ECOMM_TG = 4;
	public static final int ECOMM_JD = 5;
	public static final int ECOMM_CN = 6;
	public static final int ECOMM_JG = 7;
	public static final int ECOMM_JM = 8;
}
