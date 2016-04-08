package com.voyageone.task2.cms.service.platform.ali;

import com.taobao.api.ApiException;
import com.taobao.top.schema.exception.TopSchemaException;
import com.taobao.top.schema.factory.SchemaReader;
import com.taobao.top.schema.factory.SchemaWriter;
import com.taobao.top.schema.field.Field;
import com.voyageone.task2.cms.bean.platform.SxData;
import com.voyageone.components.tmall.service.TbProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhujiaye on 16/2/14.
 */
@Repository
public class AliSxProduct {
	@Autowired
	private TbProductService tbProductService;
	@Autowired
	private AliSxGetPlatformValue aliSxGetPlatformValue;

	/**
	 * 去天猫(天猫国际)上去寻找是否有存在这个product
	 * @param sxData 商品信息
	 * @return 返回的天猫productId (如果没有找到, 返回null)
	 */
	public Long getProductIdFromTmall(SxData sxData){
		Long productId = 0L;

		// 获取匹配产品规则
		List<Field> fieldList;
		try {
			String strXml = tbProductService.getProductMatchSchema(Long.parseLong(sxData.getPlatformCategoryId()), sxData.getShopBean());

			fieldList = SchemaReader.readXmlForList(strXml);
		} catch (ApiException e) {
			// 调用API异常
			return productId;
		} catch (TopSchemaException e) {
			// 解析XML异常
			return productId;
		}

		// 设置值
		List<Field> fieldListSearch = new ArrayList<>();
		for (Field field : fieldList) {
			String propId = getPropIdByTmallPropId(field.getId());

			fieldListSearch.add(aliSxGetPlatformValue.getPlatformValue(propId, sxData, field));
		}

		// 匹配产品
		try {
			String strPropXml = SchemaWriter.writeParamXmlString(fieldListSearch);
			String[] strProductList = tbProductService.matchProduct(Long.parseLong(sxData.getPlatformCategoryId()), strPropXml, sxData.getShopBean());
			if (strProductList != null && strProductList.length > 0) {
				productId = Long.parseLong(strProductList[0]);
			}
		} catch (TopSchemaException e) {
			// 解析XML异常
			return productId;
		} catch (ApiException e) {
			// 调用API异常
			return productId;
		}

		return productId;
	}

	/**
	 * 特殊属性id的匹配
	 * TODO: 特殊属性id的匹配, 暂时这里写一下, 之后要移到数据库里
	 * @param tmallPropId 天猫上的属性id
	 * @return 转换过的共通的属性id (非特殊属性id, 直接原样返回)
	 */
	private String getPropIdByTmallPropId(String tmallPropId) {
		switch (tmallPropId) {
			case "prop_13021751": {
				// 款号
				return "SpStyleCode";
			}
			case "prop_20000": {
				// 品牌
				return "SpBrand";
			}
			default: {
				// 非特殊, 直接原样返回
				return tmallPropId;
			}
		}

	}

}
