package com.voyageone.batch.oms.emum;

public enum MagentoType {

	SPALDING_WEB("SPALDING","25","11","005","100000000"),
	SPALDING_MOB("SPALDING","25","12","005","110000000"),
	SNEAKER_WEB("SNEAKER","25","8","001","800000000"),
	SNEAKER_MOB("SNEAKER","25","9","001","810000000"),
	JUICY("JUICY","25","","004","");

	private	String channelName;	
	private String cartId;
	private String target;
	private String channelId;
	private String targetNum;

	private MagentoType(String channelName, String cartId, String target, String channelId,String targetNum) {
		this.channelName=channelName;
		this.cartId = cartId;
		this.target = target;
		this.channelId = channelId;
		this.targetNum=targetNum;
	}

	/**
	 * @return the channelName
	 */
	public String getChannelName() {
		return channelName;
	}

	/**
	 * @return the cartId
	 */
	public String getCartId() {
		return cartId;
	}

	/**
	 * @return the target
	 */
	public String getTarget() {
		return target;
	}

	/**
	 * @return the channelId
	 */
	public String getChannelId() {
		return channelId;
	}

	/**
	 * @return the targetNum
	 */
	public String getTargetNum() {
		return targetNum;
	}
	
}
