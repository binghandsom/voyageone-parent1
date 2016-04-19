package com.voyageone.components.jd.service;

import com.jd.open.api.sdk.domain.category.Category;
import com.jd.open.api.sdk.domain.list.CategoryAttrReadService.CategoryAttr;
import com.jd.open.api.sdk.domain.list.CategoryAttrValueReadService.CategoryAttrValue;
import com.jd.open.api.sdk.request.category.CategorySearchRequest;
import com.jd.open.api.sdk.request.list.CategoryReadFindAttrsByCategoryIdRequest;
import com.jd.open.api.sdk.request.list.CategoryReadFindValuesByAttrIdRequest;
import com.jd.open.api.sdk.response.category.CategorySearchResponse;
import com.jd.open.api.sdk.response.list.CategoryReadFindAttrsByCategoryIdResponse;
import com.jd.open.api.sdk.response.list.CategoryReadFindValuesByAttrIdResponse;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.components.jd.JdBase;
import com.voyageone.components.jd.JdConstants;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 京东类目类 api 调用服务
 * <p/>
 * Created by desmond on 2016/4/8.
 */
@Component
public class JdCategoryService extends JdBase {

	/**
	 * 取得京东类目信息列表
	 *
	 * @param shop ShopBean      店铺信息
	 * @return List<Category>    京东类目列表
	 */
	public List<Category> getCategoryInfo(ShopBean shop) {
		List<Category> categoryList = new ArrayList<>();

		// 京东类目信息取得request（未设置fields，取得全部类目项目）
        CategorySearchRequest request = new CategorySearchRequest();

		try {
			// 调用京东商家类目信息API(360buy.warecats.get)
			CategorySearchResponse response = reqApi(shop, request);

			if (response != null) {
				// 京东返回正常的场合
				if (JdConstants.C_JD_RETURN_SUCCESS_OK.equals(response.getCode())) {
					// 类目信息存在
					for (Category item_cat : response.getCategory()) {
						categoryList.add(item_cat);
					}
				}
			}
		} catch (Exception ex) {
			logger.error("调用京东API获取京东商家类目信息失败 " + "channel_id:" + shop.getOrder_channel_id() + ",cart_id:" + shop.getCart_id());

			throw new BusinessException(shop.getShop_name() + "取得京东商家类目信息失败 " + ex.getMessage());
		}

		return categoryList;
	}

    /**
     * 取得京东类目属性信息列表
     *
     * @param shop ShopBean            店铺信息
     * @param catId String             类目id
     * @param attributeType int        属性类型
     *
     * @return List<CategoryAttr>      京东类目属性列表
     */
    public List<CategoryAttr> getCategoryAttrInfo(ShopBean shop, String catId, int attributeType) {
        List<CategoryAttr> jdCategoryAttrList = new ArrayList<>();

        CategoryReadFindAttrsByCategoryIdRequest request = new CategoryReadFindAttrsByCategoryIdRequest();

        // 类目id
        request.setCid(Long.parseLong(catId));
        // 属性类型（属性类型.1:关键属性 2:不变属性 3:可变属性 4:销售属性）
        request.setAttributeType(attributeType);

        try {
            // 调用京东商家类目属性信息API(jingdong.category.read.findAttrsByCategoryId)
            CategoryReadFindAttrsByCategoryIdResponse response = reqApi(shop, request);

            if (response != null) {
                // 京东返回正常的场合
                if (JdConstants.C_JD_RETURN_SUCCESS_OK.equals(response.getCode()) &&
                        response.getCategoryAttrs() != null) {
                    // 类目信息存在
                    for (CategoryAttr categoryAttr : response.getCategoryAttrs()) {
                        jdCategoryAttrList.add(categoryAttr);
                    }
                }
            }
        } catch (Exception ex) {
            logger.error("调用京东API获取京东类目属性失败 " + "channel_id:" + shop.getOrder_channel_id() + ",cart_id:" + shop.getCart_id() + ",platform_category_id:" + catId);

            throw new BusinessException(shop.getShop_name() + "取得京东商家类目属性信息失败 " + ex.getMessage());
        }

        return jdCategoryAttrList;
    }

    /**
     * 通过类目属性ID获取属性值列表
     *
     * @param shop ShopBean            店铺信息
     * @param categoryAttrId String    类目属性id
     *
     * @return List<CategoryAttrValue> 京东类目属性值列表
     */
    public List<CategoryAttrValue> getCategoryAttrValueInfo(ShopBean shop, Long categoryAttrId) {
        List<CategoryAttrValue> jdCategoryAttrValueList = new ArrayList<>();

        CategoryReadFindValuesByAttrIdRequest request = new CategoryReadFindValuesByAttrIdRequest();

        // 类目属性id
        request.setCategoryAttrId(categoryAttrId);

        try {
            // 调用京东商家类目属性信息API(jingdong.category.read.findValuesByAttrId)
            CategoryReadFindValuesByAttrIdResponse response = reqApi(shop, request);

            if (response != null) {
                // 京东返回正常的场合
                if (JdConstants.C_JD_RETURN_SUCCESS_OK.equals(response.getCode()) &&
                        response.getCategoryAttrValues() != null) {
                    // 类目值信息存在
                    for (CategoryAttrValue categoryAttrValue : response.getCategoryAttrValues()) {
                        jdCategoryAttrValueList.add(categoryAttrValue);
                    }
                }
            }
        } catch (Exception ex) {
            logger.error("调用京东API获取京东类目属性值失败 " + "channel_id:" + shop.getOrder_channel_id() + ",cart_id:" + shop.getCart_id() + ",category_attr_id:" + String.valueOf(categoryAttrId));

            throw new BusinessException(shop.getShop_name() + "取得京东商家类目属性值信息失败 " + ex.getMessage());
        }

        return jdCategoryAttrValueList;
    }
}
