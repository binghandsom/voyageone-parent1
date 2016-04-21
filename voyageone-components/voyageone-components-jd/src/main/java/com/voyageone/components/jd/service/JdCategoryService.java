package com.voyageone.components.jd.service;

import com.jd.open.api.sdk.domain.category.AttValue;
import com.jd.open.api.sdk.domain.category.Category;
import com.jd.open.api.sdk.request.category.CategoryAttributeSearchRequest;
import com.jd.open.api.sdk.request.category.CategoryAttributeValueSearchRequest;
import com.jd.open.api.sdk.request.category.CategorySearchRequest;
import com.jd.open.api.sdk.response.category.CategoryAttributeSearchResponse;
import com.jd.open.api.sdk.response.category.CategoryAttributeValueSearchResponse;
import com.jd.open.api.sdk.response.category.CategorySearchResponse;
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
     *
     * @return List<CategoryAttributeSearchResponse.Attribute> 京东类目属性列表
     */
    public List<CategoryAttributeSearchResponse.Attribute> getCategoryAttrInfo(ShopBean shop, String catId) {
        List<CategoryAttributeSearchResponse.Attribute> jdCategoryAttrList = new ArrayList<>();

        CategoryAttributeSearchRequest request = new CategoryAttributeSearchRequest();

        // 类目id
        request.setCid(catId);
        // 需要返回的字段列表
        String fields = "aid,name,cid,is_key_prop,is_sale_prop,is_color_prop,is_size_prop,index_id,status,att_type,input_type,is_req,is_fet,is_nav";
        request.setFields(fields);
        // 是否关键属性（这个地方只能填false）
        request.setKeyProp("false");
        // 是否销售属性（这个地方只能填true的话，只返回颜色和尺码）
        request.setSaleProp("false");

        try {
            // 调用京东商家类目属性信息API(360buy.ware.get.attribute)
            CategoryAttributeSearchResponse response = reqApi(shop, request);

            if (response != null) {
                // 京东返回正常的场合
                if (JdConstants.C_JD_RETURN_SUCCESS_OK.equals(response.getCode()) &&
                        response.getAttributes() != null) {
                    // 类目信息存在
                    jdCategoryAttrList = response.getAttributes();
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
     * @param categoryAttrId long      类目属性id
     *
     * @return List<AttValue>      京东类目属性值列表
     */
    public List<AttValue> getCategoryAttrValueInfo(ShopBean shop, Long categoryAttrId) {
        List<AttValue> jdCategoryAttrValueList = new ArrayList<>();

        CategoryAttributeValueSearchRequest request = new CategoryAttributeValueSearchRequest();

        // 类目属性id
        request.setAvs(String.valueOf(categoryAttrId));
        // 需要返回的字段列表
        String fields = "aid,vid,name,status,index_id,features";
        request.setFields(fields);

        try {
            // 调用京东商家类目属性信息API(jingdong.category.read.findValuesByAttrId)
            CategoryAttributeValueSearchResponse response = reqApi(shop, request);

            if (response != null) {
                // 京东返回正常的场合
                if (JdConstants.C_JD_RETURN_SUCCESS_OK.equals(response.getCode()) &&
                        response.getAttValues() != null) {
                    // 类目值信息存在
                    jdCategoryAttrValueList = response.getAttValues();
                }
            }
        } catch (Exception ex) {
            logger.error("调用京东API获取京东类目属性值失败 " + "channel_id:" + shop.getOrder_channel_id() + ",cart_id:" + shop.getCart_id() + ",category_attr_id:" + String.valueOf(categoryAttrId));

            throw new BusinessException(shop.getShop_name() + "取得京东商家类目属性值信息失败 " + ex.getMessage());
        }

        return jdCategoryAttrValueList;
    }
}
