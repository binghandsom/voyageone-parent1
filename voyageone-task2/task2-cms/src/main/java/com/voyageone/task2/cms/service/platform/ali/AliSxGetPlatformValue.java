package com.voyageone.task2.cms.service.platform.ali;

import com.taobao.top.schema.field.Field;
import com.taobao.top.schema.field.InputField;
import com.taobao.top.schema.field.SingleCheckField;
import com.voyageone.task2.cms.bean.platform.SxData;
import com.voyageone.task2.cms.service.platform.common.SxGetPlatformValueCommon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Created by zhujiaye on 16/2/15.
 */
@Repository
public class AliSxGetPlatformValue {
	@Autowired
	private SxGetPlatformValueCommon sxGetPlatformValueCommon;

	/**
	 * 获取平台的属性的值的入口
	 * @param propId 属性id
	 * @param sxData 商品基本信息
	 */
	public Field getPlatformValue(String propId, SxData sxData, Field field) {

		if ("SpStyleCode".equals(propId)) {
			// 款号
			InputField inputField = (InputField) field;

			// 非达尔文的商品直接用model
			// TODO: 达尔文商品需要先去这个地方检索一下: https://product.tmall.com/futures/futuresItem.htm
			String styleCode = sxData.getProductList().get(0).getCommon().getFields().getModel();
			inputField.setValue(styleCode);

			return inputField;

		} else if ("SpBrand".equals(propId)) {
			// 品牌
			SingleCheckField singleCheckField = (SingleCheckField) field;

			singleCheckField.setValue(
					sxGetPlatformValueCommon.getBrand(sxData.getChannelId(), sxData.getCartId(), sxData.getProductList().get(0).getCommon().getFields().getBrand())
			);

			return singleCheckField;

		} else if ("SpSku".equals(propId)) {
			// Sku

			return null;
		} else {
			// 其他属性的场合, 使用mapping

			return null;
		}

	}


}
