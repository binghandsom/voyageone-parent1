package com.voyageone.cms.service.model;

/**
 * 属性值的翻译对照表
 * 注意点:
 *	1. 关于prop_id
 *		如果prop_id是0, 那么就说明这个翻译的内容, 在整个店铺里任何地方都能使用这个翻译的内容
 *		如果prop_id不是0, 那么就说明这个翻译的内容, 只有在翻译这个属性的场合在会起作用
 *	2. 关于翻译的值
 *		feed_value_translation为空的数据将会被无视, 如果共通的有过翻译的话, 那就使用共通的翻译
 *		同样的一个feed_value_original, 使用的优先顺是:
 *			a. 指定类目和属性的 (prop_id不为0, 并且prop_id对应的CmsBtFeedCustomPropValueModel的catPath不为0 的记录)
 *			b. 未指定类目的共通属性的值的翻译 (prop_id不为0, 并且prop_id对应的CmsBtFeedCustomPropValueModel的catPath为0 的记录)
 *			c. 未指定类目未指定属性的值的翻译 (prop_id为0的记录)
 * Created by zhujiaye on 16/2/26.
 */
public class CmsBtFeedCustomPropValueModel {
	private int id;							// 序号
	private String channel_id;				// channel id
	private int prop_id;					// CmsBtFeedCustomPropValueMode的id
	private String feed_value_original;		// 属性值原文
	private String feed_value_translation;	// 属性值的翻译

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getChannel_id() {
		return channel_id;
	}

	public void setChannel_id(String channel_id) {
		this.channel_id = channel_id;
	}

	public int getProp_id() {
		return prop_id;
	}

	public void setProp_id(int prop_id) {
		this.prop_id = prop_id;
	}

	public String getFeed_value_original() {
		return feed_value_original;
	}

	public void setFeed_value_original(String feed_value_original) {
		this.feed_value_original = feed_value_original;
	}

	public String getFeed_value_translation() {
		return feed_value_translation;
	}

	public void setFeed_value_translation(String feed_value_translation) {
		this.feed_value_translation = feed_value_translation;
	}

}
