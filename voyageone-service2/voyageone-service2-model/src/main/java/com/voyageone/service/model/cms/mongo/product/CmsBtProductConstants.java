package com.voyageone.service.model.cms.mongo.product;

public interface CmsBtProductConstants {

	/**
	 *  FieldImageType
	 */
	enum FieldImageType {
		// type: 1-商品图片, 2-包装图片, 3-带角度图片, 4-自定义图片,5-移动端自定义图片,6-PC端自拍商品图,7-APP端自拍商品图,8-吊牌图
		PRODUCT_IMAGE("image1"),
		PACKAGE_IMAGE("image2"),
		ANGLE_IMAGE("image3"),
		CUSTOM_IMAGE("image4"),
		MOBILE_CUSTOM_IMAGE("image5"),
		CUSTOM_PRODUCT_IMAGE("image6"),
		M_CUSTOM_PRODUCT_IMAGE("image7"),
		HANG_TAG_IMAGE("image8"),
		DURABILITY_TAG_IMAGE("image9");

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

	/**
	 *  Platform_Field_COMM
	 */
	enum Platform_Field_COMM {
	}

//	/**
//	 *  Platform_Field_JM
//	 */
//	enum Platform_Field_JM {
//		/* productNameCn */
//		productNameCn,
//		/* productNameEn */
//		productNameEn,
//		/* productLongName */
//		productLongName,
//		/* productMediumName */
//		productMediumName,
//		/* productShortName */
//		productShortName,
//		/* originCn */
//		originCn,
//		/* beforeDate */
//		beforeDate,
//		/* suitPeople */
//		suitPeople,
//		/* specialExplain */
//		specialExplain,
//		/* searchMetaTextCustom */
//		searchMetaTextCustom,
//	}

	/**
	 *  Platform_SKU_COM
	 */
	enum Platform_SKU_COM {
		/* skuCode(共通) */
		skuCode,
		/* 条形码 */
		barcode,
        /* MSRP售价(中国建议零售价)(共通/分平台) */
        priceMsrp,
		/* 品牌方MSRP售价(中国建议零售价)(共通/分平台) */
		originalPriceMsrp,
		/* 指导价和建议零售价比较状态(XD/XU/空格) */
		priceMsrpFlg,
        /* 销售指导价(共通/分平台) */
        priceRetail,
		/* 确认指导价 */
		confPriceRetail,
		/* 各平台最终零售价(分平台) */
		priceSale,
		/* 指导售价变化状态（U99%/D99%）(分平台) */
		priceChgFlg,
        /* 最终售价与指导售价的变化状态（1:等于指导价/2:比指导价低/3:比指导价高/4:向上击穿警告/5:向下击穿警告）(分平台) */
        priceDiffFlg,
		/* 客户建议零售价 */
		clientMsrpPrice,
		/* 客户成本价 */
		clientNetPrice,
		/* 用于表示该sku是否在该平台销售(默认都是true,画面上不提供编辑) (分平台) */
		isSale,
		/* size(共通) */
		size,
		/* sizeSx(共通) */
		sizeSx,
		/* qty(共通) */
		qty,
		/* weight(共通) */
		weight,
		/* weightUnit(共通) */
		weightUnit
	}

//	/**
//	 *  Platform_SKU_JM
//	 */
//	enum Platform_SKU_JM {
//		/* skuCode(共通) */
//		skuCode,
//		/* 平台级别的指导售价(共通) */
//		priceRetail,
//		/* priceSale(共通) */
//		priceSale,
//		/* 价格变更状态（U/D/XU/XD）(共通) */
//		priceChgFlg,
//		/* 用于表示该sku是否在该平台销售(默认都是true,画面上不提供编辑) (共通) */
//		isSale,
//		/* jmSpuNo */
//		jmSpuNo,
//		/* jmSkuNo */
//		jmSkuNo,
//		/* property */
//		property,
//		/* attribute */
//		attribute,
//		/* size */
//		size,
//	}


}
