package com.voyageone.batch.cms;

public class CmsConstants {

	// url 去除特殊字符
	public static final String URL_FORMAT = "%[~@#$%&*_''/‘’^\\()]%";

	public static final String BACKUP_FEED_FILE = "d:\\productsFeed";
//	public static final String WEB_SERVIES_URI_INSERT = "http://10.0.1.18:8080/VoyageOne_WebService/cms/products/productsFeed";
//	public static final String WEB_SERVIES_URI_UPDATE = "http://10.0.1.18:8080/VoyageOne_WebService/cms/products/productsUpdate";
//	public static final String WEB_SERVIES_URI_ATTRIBUTE = "http://10.0.1.18:8080/VoyageOne_WebService/cms/products/productsAttribute";
	public static final String WEB_SERVIES_URI_INSERT = "http://121.41.58.229:8889/cms/products/productsFeed";
	public static final String WEB_SERVIES_URI_UPDATE = "http://121.41.58.229:8889/cms/products/productsUpdate";
	public static final String WEB_SERVIES_URI_ATTRIBUTE = "http://121.41.58.229:8889/cms/products/productsAttribute";
	public static final String FEED_IO_UPDATEFIELDS_MSRP = "msrp";
	public static final String FEED_IO_UPDATEFIELDS_PRICE = "price";
	public static final String FEED_IO_UPDATEFIELDS_CN_PRICE = "cn_price";
	public static final String FEED_IO_UPDATEFIELDS_CN_PRICE_RMB = "cn_price_rmb";
	public static final String FEED_IO_UPDATEFIELDS_LONG_DESCRIPTION ="long_description";
	public static final String FEED_IO_UPDATEFIELDS_IMAGE_URL = "item_image_url" ;
	public static final String FEED_IO_UPDATEFIELDS_IMAGE_SPLIT = ";";
}
