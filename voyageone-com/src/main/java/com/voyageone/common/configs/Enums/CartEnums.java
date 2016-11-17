package com.voyageone.common.configs.Enums;

/**
 * Created by Jack on 6/6/2017.
 *
 * @version 2.6.0
 * @since 2.0.0
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
         * 天猫国际官网同购
         */
        TT("30"),

        /**
         * Usjoi天猫国际官网同购
         */
        USTT("31"),

		/**
		 * Liking官网
		 */
		LIKING("32"),

		/**
		 * USJOI测试
		 */
		JGT("98"),

		/**
		 * US匠心界
		 */
		USJGJ("928"),
		/**
		 * US悦境
		 */
		USJGY("929"),
		/**
		 * USJOI测试
		 */
		USJGT("998");

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
					return TT;
                case "31":
                    return USTT;
				case "32":
					return LIKING;
				case "928":
					return USJGJ;
				case "929":
					return USJGY;
				default:
					return null;
			}
		}
		public String getId() {
			return id;
		}
		public int getValue()
		{
			return  Integer.parseInt(id);
		}

		/**
		 * 判断给定的店铺是否是同购店
		 *
		 * @param cart 某店铺
		 * @return 是否是同购店
		 * @since 2.6.0
		 */
		public static boolean isSimple(Cart cart) {
			return TT.equals(cart) || USTT.equals(cart);
		}

		/**
		 * 判断给定的店铺是否是京东系
		 *
		 * @param cart 某店铺
		 * @return 是否是京东系
		 * @since 2.6.0
		 */
		public static boolean isJdSeries(Cart cart) {
			return JD.equals(cart) || JG.equals(cart) || JGJ.equals(cart) || JGY.equals(cart);
		}

		/**
		 * 判断给定的店铺的类目是否是共通Schema
		 *
		 * @param cart 某店铺
		 * @return 是否是共通Schema
		 * @since 2.6.0
		 */
		public static boolean isCommonCategorySchema(Cart cart) {
			return isCommonCategorySchema(cart.getId());
		}

		/**
		 * @since 2.10.0
		 */
		public static boolean isCommonCategorySchema(String cartId) {
			return JM.getId().equals(cartId) || TT.getId().equals(cartId) || USTT.getId().equals(cartId) || LIKING.getId().equals(cartId);
		}
    }
}
