package com.voyageone.task2.vms;

import java.util.HashMap;
import java.util.Map;

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
	 * 财务报表状态
	 */
	public final static class FinancialReportStatus {
		public final static String UNCONFIRMED = "0";
		public final static String CONFIRMED = "1";
	}

	/**
	 * 数据统计名称
	 */
	public final static class DataAmount {
		public final static String NEW_ORDER_COUNT = "NEW_ORDER_COUNT";
		public final static String NEW_SKU_COUNT = "NEW_SKU_COUNT";
		public final static String RECEIVE_ERROR_SHIPMENT_COUNT = "RECEIVE_ERROR_SHIPMENT_COUNT";
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

		// shipment状态
		interface SHIPMENT_STATUS {
			String OPEN = "1";
			String SHIPPED = "3";
			String ARRIVED = "4";
			String RECEIVED = "5";
			String RECEIVE_ERROR = "6";
		}
	}
}
