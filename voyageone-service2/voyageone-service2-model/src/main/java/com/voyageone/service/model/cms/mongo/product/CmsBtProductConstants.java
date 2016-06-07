package com.voyageone.service.model.cms.mongo.product;

public interface CmsBtProductConstants {

	/**
	 *  FieldImageType
	 */
	public enum FieldImageType {
		// type: 1-商品图片, 2-包装图片, 3-带角度图片, 4-自定义图片,5-移动端自定义图片,6-PC端自拍商品图,7-APP端自拍商品图,8-吊牌图
		PRODUCT_IMAGE("image1"),
		PACKAGE_IMAGE("image2"),
		ANGLE_IMAGE("image3"),
		CUSTOM_IMAGE("image4"),
		MOBILE_CUSTOM_IMAGE("image5"),
		CUSTOM_PRODUCT_IMAGE("image6"),
		M_CUSTOM_PRODUCT_IMAGE("image7"),
		HANG_TAG_IMAGE("image8");

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

		public static FieldImageType getFieldImageTypeByName(String name) {
			for (FieldImageType c : FieldImageType.values()) {
				if (c.getName().equalsIgnoreCase(name)) {
					return c;
				}
			}
			return null;
		}

	}

}
