package com.voyageone.task2.cms.bean;
import java.util.Map;

/**
 * ProductsFeedUpdate
 * Created by zero on 7/28/2015.
 *
 * @author zero
 */
public class ProductsFeedUpdate {
	private String channel_id;
	private String code;
	private String product_url_key;
	private String barcode;
	private Map<String, String> updatefields;

	public String getChannel_id() {
		return channel_id;
	}

	public void setChannel_id(String channel_id) {
		this.channel_id = channel_id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getProduct_url_key() {
		return product_url_key;
	}

	public void setProduct_url_key(String product_url_key) {
		this.product_url_key = product_url_key;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public Map<String, String> getUpdatefields() {
		return updatefields;
	}

	public void setUpdatefields(Map<String, String> updatefields) {
		this.updatefields = updatefields;
	}
}
