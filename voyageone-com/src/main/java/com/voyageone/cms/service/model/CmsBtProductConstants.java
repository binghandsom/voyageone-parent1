package com.voyageone.cms.service.model;

public interface CmsBtProductConstants {

	/**
	 *  FieldImageType
	 */
	public enum FieldImageType {
		// type: 1-商品图片, 2-包装图片, 4-带角度图片, 3-自定义图片
		PRODUCT_IMAGE("image1"), PACKAGE_IMAGE("image2"), ANGLE_IMAGE("image3"), CUSTOM_IMAGE("image4");

		// 成员变量
		private String name;

		// 构造方法
		private FieldImageType(String name) {
			this.name = name;
		}

		// get set 方法
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

	}

}
