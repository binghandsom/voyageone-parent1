package com.voyageone.cms;

public interface CmsConstants {

	public static final class Category {

		public static final String CATEGORY_ISPUBLISHED_YES = "1";

		public static final String CATEGORY_ISPUBLISHED_NO = "0";

		public static final String CATEGORY_PUBLISHSTATUS = "0";

		public static final String PLATFORM_TM_ID = "1";

		public static final String PLATFORM_JD_ID = "2";

		public static final String MAIN_CATEGORY_TYPE = "1";

		public static final String HISTORY_PRICE_SETTING_TYPE_CATEGORY = "1";

	}

	public static final class CartId {

		public static final String US_OFFICIAL_CARTID = "6";

		public static final String US_AMAZONE_CARTID = "5";

		public static final String CN_OFFICIAL_CARTID = "25";

	}

	public static final class IDType {

		public static final int TYPE_CATEGORY = 1;

		public static final int TYPE_MODEL = 2;

		public static final int TYPE_PRODUCT = 3;

		public static final int TYPE_ALL = 0;

	}

	public static final class PromotionType {

		public static final String TYPE_YEAR = "1";

		public static final String TYPE_MONTH = "2";

		public static final String TYPE_PROMOTIONINFO = "3";

	}

	public static final class CountryType {

		public static final String TYPE_US = "1";

		public static final String TYPE_CN = "2";

	}
	
	public static final class AttributeType {

		public static final int BasePrice = 1;

		public static final int PercentList = 2;
		
		public static final int ShippingList = 3;
		
		public static final int USSaleChannels = 4;
		
		public static final int CNSaleChannels = 5;
		
		public static final int SystemCountry = 6;
		
		public static final int USProductAttributes = 7;
		
		public static final int CNProductAttributes = 8;
	}	
	
	public static final class PropKey {
		public static final String CSVPATH = "CSV_Path";
	}
	
	public static final class Format {
		public static final String CSVNAME = "Product%s";
	}
}
