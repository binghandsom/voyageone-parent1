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
		public final static String IMPORT_COMPLETED = "2";
		public final static String IMPORT_WITH_ERROR = "3";
	}

	/**
	 * ChannelConfig
	 */
	public interface ChannelConfig {

		// 通常ConfigCode
		String COMMON_CONFIG_CODE = "0";

		// CSV分隔符
		String CSV_SPLIT_SYMBOL = "CSV_SPLIT_SYMBOL";
		// CSV文件编码
		String CSV_ENCODE = "CSV_ENCODE";


	}
}
