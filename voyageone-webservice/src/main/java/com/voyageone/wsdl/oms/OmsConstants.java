package com.voyageone.wsdl.oms;

import java.util.HashMap;
import java.util.Map;

public class OmsConstants {
	/**
	 * 订单地址国家信息_中国
	 */
	public static final String ADDRESS_COUNTRY_CHINA = "中国";
	
	/**
	 * 差价订单
	 */
	public static final String PRICES_GAP = "price-difference-001";
	
	/**
	 * 订单来源（阿里巴巴）
	 */
	public static final String ORDER_PLATFORM_ID_ALIBABA = "1";
	
	/**
	 * targetId  sneakerhead旗舰店
	 */
	public static String TARGET_SNEAKERHEAD_TMALL = "1";
	/**
	 * targetId  sneakerhead淘宝店
	 */
	public static String TARGET_SNEAKERHEAD_TAOBAO = "2";
	/**
	 * targetId  sneakerhead海外旗舰店
	 */
	public static String TARGET_SNEAKERHEAD_TMALLG = "3";
	/**
	 * targetId  京东
	 */
	public static String TARGET_JD = "4";
	/**
	 * targetId  PortAmerican海外专营店
	 */
	public static String TARGET_PA_TMALLG = "5";
	/**
	 * targetId  斯伯丁官方旗舰店
	 */
	public static String TARGET_SPALDING_TMALL = "6";
	/**
	 * targetId  JuicyCouture海外旗舰店
	 */
	public static String TARGET_JC_TMALLG = "7";
	/**
	 * targetId  BHFO海外旗舰店
	 */
	public static String TARGET_BHFO_TMALLG = "13";
	/**
	 * targetId  京东国际
	 */
	public static String TARGET_JDG = "14";
	
	/**
	 * targetId  paymentName 对应
	 */
	public static Map<String, String> TARGET_PAYMENT_NAME = new HashMap<String, String>();
	
	/**
	 * targetId  MarketName 对应
	 */
	public static Map<String, String> TARGET_MARKET_NAME = new HashMap<String, String>();
	
	static {
		TARGET_PAYMENT_NAME.put(TARGET_SNEAKERHEAD_TMALL, "Alipay");
		TARGET_PAYMENT_NAME.put(TARGET_SNEAKERHEAD_TAOBAO, "AlipayTaobao");
		TARGET_PAYMENT_NAME.put(TARGET_SNEAKERHEAD_TMALLG, "AlipayInterNational");
		TARGET_PAYMENT_NAME.put(TARGET_JD, "Jdpay");
		TARGET_PAYMENT_NAME.put(TARGET_JDG, "JdpayInterNational");
		
		TARGET_MARKET_NAME.put(TARGET_SNEAKERHEAD_TMALL, "Tmall");
		TARGET_MARKET_NAME.put(TARGET_SNEAKERHEAD_TAOBAO, "Taobao");
		TARGET_MARKET_NAME.put(TARGET_SNEAKERHEAD_TMALLG, "TmallG");
		TARGET_MARKET_NAME.put(TARGET_JD, "jd");
		TARGET_MARKET_NAME.put(TARGET_JDG, "jg");
	}
}
