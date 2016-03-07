package com.voyageone.task2.cms.bean;

/**
 * ImageBean
 * Created by zero on 7/28/2015.
 *
 * @author zero
 */
public class ImageBean {
	private String image_type;
	private String image;
	private String image_url;
	private String image_name;
	private String display_order;

	public String getImage_type() {
		return image_type;
	}

	public void setImage_type(String image_type) {
		this.image_type = image_type;
	}

	public String getImage() {
		return image;
	}

	public String getImage_url() {
		return image_url;
	}

	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getImage_name() {
		return image_name;
	}

	public void setImage_name(String image_name) {
		this.image_name = image_name;
	}

	public String getDisplay_order() {
		return display_order;
	}

	public void setDisplay_order(String display_order) {
		this.display_order = display_order;
	}
}
