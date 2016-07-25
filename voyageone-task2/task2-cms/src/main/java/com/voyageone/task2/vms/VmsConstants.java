package com.voyageone.task2.vms;

public class VmsConstants {

	// 逗号
	public final static char COMMA = ',';
	// utf-8
	public final static String UTF_8 = "utf-8";


	/**
	 * Feed文件导入状态
	 */
	public final static class FeedFileStatus {
		public final static String WAITING_IMPORT = "1";
		public final static String IMPORTING = "2";
		public final static String IMPORT_COMPLETED = "3";
		public final static String IMPORT_ERROR = "4";
		public final static String IMPORT_SYSTEM_ERROR = "5";
	}

	/**
	 * Feed文件上传类型
	 */
	public final static class FeedFileUploadType {
		public final static String ONLINE = "1";
		public final static String FTP = "2";
	}

	/**
	 * ChannelConfig
	 */
	public interface ChannelConfig {

		// 通常ConfigCode
		String COMMON_CONFIG_CODE = "0";

		// CSV分隔符
		String FEED_CSV_SPLIT_SYMBOL = "FEED_CSV_SPLIT_SYMBOL";
		// CSV文件编码
		String FEED_CSV_ENCODE = "FEED_CSV_ENCODE";
		// 每个月出财务报表的日期
		String MAKE_FINANCIAL_REPORT_DAY = "MAKE_FINANCIAL_REPORT_DAY";


	}

	public interface STATUS_VALUE {

		// 物品状态（vms系统用）
		interface PRODUCT_STATUS {
			String OPEN = "1";
			String PACKAGE = "2";
			String SHIPPED = "3";
			String RECEIVED = "5";
			String RECEIVE_ERROR = "6";
			String CANCEL = "7";
		}
	}
}
