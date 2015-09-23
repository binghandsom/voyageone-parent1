package com.voyageone.batch.oms.modelbean;

public class TradeRateBean {
	// 交易ID	
	private String source_order_id;
	// 评价者昵称
	private String nick;
	// 评价结果,可选值:good(好评),neutral(中评),bad(差评)
	private String result;
	// 评价创建时间
	private String created;
	// 被评价者昵称
	private String rated_nick;
	// 商品标题
	private String item_title;
	// 商品价格
	private double item_price;
	// 评价内容
	private String content;
	// 评价解释
	private String reply;
	// 商品的数字ID
	private long num_iid;
	// 渠道
	private String order_channel_id;
	// 店铺
	private int cart_id;
	/**
	 * @return the source_order_id
	 */
	public String getSource_order_id() {
		return source_order_id;
	}
	/**
	 * @param source_order_id the source_order_id to set
	 */
	public void setSource_order_id(String source_order_id) {
		this.source_order_id = source_order_id;
	}
	/**
	 * @return the nick
	 */
	public String getNick() {
		return nick;
	}
	/**
	 * @param nick the nick to set
	 */
	public void setNick(String nick) {
		this.nick = nick;
	}
	/**
	 * @return the result
	 */
	public String getResult() {
		return result;
	}
	/**
	 * @param result the result to set
	 */
	public void setResult(String result) {
		this.result = result;
	}
	/**
	 * @return the created
	 */
	public String getCreated() {
		return created;
	}
	/**
	 * @param created the created to set
	 */
	public void setCreated(String created) {
		this.created = created;
	}
	/**
	 * @return the rated_nick
	 */
	public String getRated_nick() {
		return rated_nick;
	}
	/**
	 * @param rated_nick the rated_nick to set
	 */
	public void setRated_nick(String rated_nick) {
		this.rated_nick = rated_nick;
	}
	/**
	 * @return the item_title
	 */
	public String getItem_title() {
		return item_title;
	}
	/**
	 * @param item_title the item_title to set
	 */
	public void setItem_title(String item_title) {
		this.item_title = item_title;
	}
	/**
	 * @return the item_price
	 */
	public double getItem_price() {
		return item_price;
	}
	/**
	 * @param item_price the item_price to set
	 */
	public void setItem_price(double item_price) {
		this.item_price = item_price;
	}
	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * @return the reply
	 */
	public String getReply() {
		return reply;
	}
	/**
	 * @param reply the reply to set
	 */
	public void setReply(String reply) {
		this.reply = reply;
	}
	/**
	 * @return the num_iid
	 */
	public long getNum_iid() {
		return num_iid;
	}
	/**
	 * @param num_iid the num_iid to set
	 */
	public void setNum_iid(long num_iid) {
		this.num_iid = num_iid;
	}
	/**
	 * @return the order_channel_id
	 */
	public String getOrder_channel_id() {
		return order_channel_id;
	}
	/**
	 * @param order_channel_id the order_channel_id to set
	 */
	public void setOrder_channel_id(String order_channel_id) {
		this.order_channel_id = order_channel_id;
	}
	/**
	 * @return the cart_id
	 */
	public int getCart_id() {
		return cart_id;
	}
	/**
	 * @param cart_id the cart_id to set
	 */
	public void setCart_id(int cart_id) {
		this.cart_id = cart_id;
	}
	
}
