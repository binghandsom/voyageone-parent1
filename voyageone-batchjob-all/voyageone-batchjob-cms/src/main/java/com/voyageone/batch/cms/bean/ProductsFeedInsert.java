package com.voyageone.batch.cms.bean;
import java.util.List;
/**
 * ProductsFeedInsert
 * Created by zero on 7/28/2015.
 *
 * @author zero
 */
public class ProductsFeedInsert {

	private String channel_id;

	private List<CategoryBean> categorybeans;

	/**
	 * @return the channel_id
	 */
	public String getChannel_id() {
		return channel_id;
	}

	/**
	 * @param channel_id the channel_id to set
	 */
	public void setChannel_id(String channel_id) {
		this.channel_id = channel_id;
	}

	/**
	 * @return the categorybeans
	 */
	public List<CategoryBean> getCategorybeans() {
		return categorybeans;
	}

	/**
	 * @param categorybeans the categorybeans to set
	 */
	public void setCategorybeans(List<CategoryBean> categorybeans) {
		this.categorybeans = categorybeans;
	}
 }
