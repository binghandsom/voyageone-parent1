package com.voyageone.common.configs.Enums;

/**
 * Created by Jack on 6/6/2017.
 */

public class CartEnums {
    /**
     * 对应 ct_cart 表中存在的配置名称
     */
    public enum Cart {
		/**
		 * 天猫
		 */
		TM("20"),

		/**
		 * 淘宝
		 */
		TB("21"),

		/**
		 * 线下
		 */
		OF("22"),

		/**
		 * 天猫国际
		 */
		TG("23"),

		/**
		 * 京东
		 */
		JD("24"),

		/**
		 * 独立域名
		 */
		CN("25"),

		/**
		 * 京东国际
		 */
		JG("26"),

		/**
		 * 聚美优品
		 */
		JM("27"),

		/**
		 * 京东国际 匠心界
		 */
		JGJ("28"),

		/**
		 * 京东国际 悦境
		 */
		JGY("29"),

		/**
		 * 天猫MiniMall
		 */
		TMM("30"),

		/**
		 * US匠心界
		 */
		USJGJ("928"),
		/**
		 * US悦境
		 */
		USJGY("929");

    	private String id;

		Cart(String id) {
			this.id = id;
		}

		public static CartEnums.Cart getValueByID(String id)
		{
			switch(id)
			{
				case "20":
					return TM;
				case "21":
					return TB;
				case "22":
					return OF;
				case "23":
					return TG;
				case "24":
					return JD;
				case "25":
					return CN;
				case "26":
					return JG;
				case "27":
					return JM;
				case "28":
					return JGJ;
				case "29":
					return JGY;
				case "30":
					return TMM;
				default:
					return null;
			}
		}
		public String getId() {
			return id;
		} 
	}
}
