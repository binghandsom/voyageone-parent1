package com.voyageone.task2.cms;

import com.taobao.top.schema.enums.FieldTypeEnum;

public class CmsConstants {

	// url 去除特殊字符
//	public static final String URL_FORMAT = "%[~@#$%&*_''/‘’^\\()]%";

//	public static final String BACKUP_FEED_FILE = "d:\\productsFeed";
//	public static final String WEB_SERVIES_URI_INSERT = "http://10.0.1.18:8080/VoyageOne_WebService/cms/products/productsFeed";
//	public static final String WEB_SERVIES_URI_UPDATE = "http://10.0.1.18:8080/VoyageOne_WebService/cms/products/productsUpdate";
//	public static final String WEB_SERVIES_URI_ATTRIBUTE = "http://10.0.1.18:8080/VoyageOne_WebService/cms/products/productsAttribute";
//	public static final String WEB_SERVIES_URI_INSERT = "http://121.41.58.229:8889/cms/products/productsFeed";
//	public static final String WEB_SERVIES_URI_UPDATE = "http://121.41.58.229:8889/cms/products/productsUpdate";
//	public static final String WEB_SERVIES_URI_ATTRIBUTE = "http://121.41.58.229:8889/cms/products/productsAttribute";
	public static final String FEED_IO_UPDATEFIELDS_MSRP = "msrp";
	public static final String FEED_IO_UPDATEFIELDS_PRICE = "price";
	public static final String FEED_IO_UPDATEFIELDS_CN_PRICE = "cn_price";
	public static final String FEED_IO_UPDATEFIELDS_CN_PRICE_RMB = "cn_price_rmb";
	public static final String FEED_IO_UPDATEFIELDS_CN_PRICE_FINAL_RMB = "cn_price_final_rmb";
	public static final String FEED_IO_UPDATEFIELDS_LONG_DESCRIPTION ="long_description";
	public static final String FEED_IO_UPDATEFIELDS_IMAGE_URL = "item_image_url";
	public static final String FEED_IO_UPDATEFIELDS_IMAGE_SPLIT = ";";
	public final static String C_PROP_PATH_SPLIT_CHAR = ">";

    // 上新的任务表中的上新状态值（0:未处理）
    public static final int SX_WORKLOAD_PUBLISH_STATUS_INIT = 0;

    // 上新的任务表中的上新状态值（1:已上新）
    public static final int SX_WORKLOAD_PUBLISH_STATUS_OK = 1;

    // 上新的任务表中的上新状态值（2:上新错误）
    public static final int SX_WORKLOAD_PUBLISH_STATUS_ERROR = 2;

    // 从上新的任务表中一次数据抽出最大件数
    public static final int PUBLISH_PRODUCT_RECORD_COUNT_ONCE_HANDLE = 100000;

	/**
	 * 第三方平台的属性类型
	 */
	public final static class PlatformPropType {
		public final static int C_ERROR = 0; // 实际天猫是不存在 ERROR 的， 这里是为了万一有其他的可能性而加的
		public final static int C_INPUT = 1;
		public final static int C_SINGLE_CHECK = 2;
		public final static int C_MULTI_CHECK = 3;
		public final static int C_LABEL = 4;
		public final static int C_COMPLEX = 5;
		public final static int C_MULTI_COMPLEX = 6;

		/**
		 * 根据类型名称，返回值
		 *
		 * @param typeName 类型名称
		 * @return 类型值
		 */
		public static int getValueByName(FieldTypeEnum typeName) {
			if (FieldTypeEnum.INPUT.compareTo(typeName) == 0) {
				return C_INPUT;
			} else if (FieldTypeEnum.SINGLECHECK.compareTo(typeName) == 0) {
				return C_SINGLE_CHECK;
			} else if (FieldTypeEnum.MULTICHECK.compareTo(typeName) == 0) {
				return C_MULTI_CHECK;
			} else if (FieldTypeEnum.LABEL.compareTo(typeName) == 0) {
				return C_LABEL;
			} else if (FieldTypeEnum.COMPLEX.compareTo(typeName) == 0) {
				return C_COMPLEX;
			} else if (FieldTypeEnum.MULTICOMPLEX.compareTo(typeName) == 0) {
				return C_MULTI_COMPLEX;
			} else {
				return C_ERROR;
			}
		}

		/**
		 * 根据类型值，返回类型名称
		 *
		 * @param typeValue 类型值
		 * @return 类型名称
		 */
		public static FieldTypeEnum getNameByValue(int typeValue) {
			if (C_INPUT == typeValue) {
				return FieldTypeEnum.INPUT;
			} else if (C_SINGLE_CHECK == typeValue) {
				return FieldTypeEnum.SINGLECHECK;
			} else if (C_MULTI_CHECK == typeValue) {
				return FieldTypeEnum.MULTICHECK;
			} else if (C_LABEL == typeValue) {
				return FieldTypeEnum.LABEL;
			} else if (C_COMPLEX == typeValue) {
				return FieldTypeEnum.COMPLEX;
			} else if (C_MULTI_COMPLEX == typeValue) {
				return FieldTypeEnum.MULTICOMPLEX;
			} else {
				return null;
			}
		}
	}
}
