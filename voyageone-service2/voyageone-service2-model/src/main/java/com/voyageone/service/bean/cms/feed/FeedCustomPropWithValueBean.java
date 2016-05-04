package com.voyageone.service.bean.cms.feed;

import java.util.List;
import java.util.Map;

/**
 * 需要显示在店铺中的自定义的属性
 * 注意点:
 * 	1. 关于类目(feed_cat_path)
 * 		如果feed_cat_path为0(代表空类目), 那么就说明这个属性是针对整个店铺的所有类目的共通属性
 * 		如果不为0, 那么就是只有指定的类目才会显示这个属性
 * 	2. 关于翻译的值
 * 		场合1:
 * 			如果有一个类目A, 他的属性A1的翻译为"A11"
 * 			并且, 有个类目为0(代表着空类目), 他也有个属性, 也叫A1, 他的翻译为"A10"
 * 			那么, 当遇到一个商品是A类目的话, A1属性的名称的翻译就是A11
 * 			如果一个商品是B类目的话, 那么A1属性的名称翻译就是"A10"
 * 		场合2:
 * 			如果有一个类目A, 他的属性A1的翻译值为空
 * 			并且, 有个类目为0(代表着空类目), 他也有个属性, 也叫A1, 他的翻译为"A10"
 * 			那么, 当遇到一个商品是A类目的话, 就不需要显示A1这个属性了, 因为他的属性名称的翻译是空的
 * 			如果一个商品是B类目的话, 那么A1属性就是需要显示的, 并且显示的翻译就是"A10"
 * Created by zhujiaye on 16/2/26.
 */
public class FeedCustomPropWithValueBean {
	private int id;							// 序号
	private String channel_id;				// channel id
	private String feed_cat_path;			// 类目名称
	private String feed_prop_original;		// 属性名称
	private String feed_prop_translation;	// 属性名称的翻译
	private String display_order;			// 显示的顺序
	private Map<String, List<String>> mapPropValue;	// 属性值的翻译
													// (不会保存在数据库里, 临时存放为了用起来方便的)
													// (翻译的话, 使用列表里第一个值就可以了, 显示时候用这个)
													// (翻译里的第二个值开始, 一般都是参考用的. 翻译的时候, 如果第一个值是空, 那就用第二个值)

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

	public String getFeed_cat_path() {
		return feed_cat_path;
	}

	public void setFeed_cat_path(String feed_cat_path) {
		this.feed_cat_path = feed_cat_path;
	}

	public String getFeed_prop_original() {
		return feed_prop_original;
	}

	public void setFeed_prop_original(String feed_prop_original) {
		this.feed_prop_original = feed_prop_original;
	}

	public String getFeed_prop_translation() {
		return feed_prop_translation;
	}

	public void setFeed_prop_translation(String feed_prop_translation) {
		this.feed_prop_translation = feed_prop_translation;
	}

	public String getDisplay_order() {
		return display_order;
	}

	public void setDisplay_order(String display_order) {
		this.display_order = display_order;
	}

	public Map<String, List<String>> getMapPropValue() {
		return mapPropValue;
	}

	public void setMapPropValue(Map<String, List<String>> mapPropValue) {
		this.mapPropValue = mapPropValue;
	}
}
