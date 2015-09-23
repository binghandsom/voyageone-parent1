package com.voyageone.batch.cms.bean;
import java.util.List;
/**
 * ProductsFeedAttribute
 * Created by zero on 7/28/2015.
 *
 * @author zero
 */
public class ProductsFeedAttribute {
	private String channel_id;
	private List<AttributeBean> attributebeans ;

	public String getChannel_id() {
		return channel_id;
	}

	public void setChannel_id(String channel_id) {
		this.channel_id = channel_id;
	}

	public List<AttributeBean> getAttributebeans() {
		return attributebeans;
	}

	public void setAttributebeans(List<AttributeBean> attributebeans) {
		this.attributebeans = attributebeans;
	}
}
