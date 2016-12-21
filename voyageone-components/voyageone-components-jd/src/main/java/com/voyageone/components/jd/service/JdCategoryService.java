package com.voyageone.components.jd.service;

import com.jd.open.api.sdk.domain.category.AttValue;
import com.jd.open.api.sdk.domain.category.Category;
import com.jd.open.api.sdk.request.category.CategoryAttributeSearchRequest;
import com.jd.open.api.sdk.request.category.CategoryAttributeValueSearchRequest;
import com.jd.open.api.sdk.request.category.CategorySearchRequest;
import com.jd.open.api.sdk.request.list.CategoryReadFindByIdRequest;
import com.jd.open.api.sdk.request.list.PopVenderCenerVenderBrandQueryRequest;
import com.jd.open.api.sdk.request.ware.WareAddVenderSellSkuRequest;
import com.jd.open.api.sdk.response.category.CategoryAttributeSearchResponse;
import com.jd.open.api.sdk.response.category.CategoryAttributeValueSearchResponse;
import com.jd.open.api.sdk.response.category.CategorySearchResponse;
import com.jd.open.api.sdk.response.list.CategoryReadFindByIdResponse;
import com.jd.open.api.sdk.response.list.PopVenderCenerVenderBrandQueryResponse;
import com.jd.open.api.sdk.response.list.VenderBrandPubInfo;
import com.jd.open.api.sdk.response.ware.WareAddVenderSellSkuResponse;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.jd.JdBase;
import com.voyageone.components.jd.JdConstants;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
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
        return getCategoryAttrInfo(shop, catId, "false");
    }


    /**
     * 取得京东类目属性信息列表
     *
     * @param shop ShopBean            店铺信息
     * @param catId String             类目id
     * @param isSaleProp String       是否只查询销售属性（这个地方只能填true的话，只返回颜色和尺码）
     *
     * @return List<CategoryAttributeSearchResponse.Attribute> 京东类目属性列表
     */
    public List<CategoryAttributeSearchResponse.Attribute> getCategoryAttrInfo(ShopBean shop, String catId, String isSaleProp) {
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
        request.setSaleProp(isSaleProp);

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
    
    /**
     * 取得京东商家品牌信息
     * @param shop 店铺信息
     * @param brandName 品牌名称
     * @return List<VenderBrandPubInfo> 品牌信息列表
     */
    public List<VenderBrandPubInfo> getCategoryBrandInfo(ShopBean shop, String brandName) {
        List<VenderBrandPubInfo> jdVenderBrandPubInfo = Collections.emptyList();
        // API请求
        PopVenderCenerVenderBrandQueryRequest request = new PopVenderCenerVenderBrandQueryRequest();
        // 设置品牌名称（可模糊查询）
        request.setName(brandName);
        
        try {
            // 调用京东商家品牌信息API
            PopVenderCenerVenderBrandQueryResponse response = reqApi(shop, request);
            // 京东返回正常的场合
            if (JdConstants.C_JD_RETURN_SUCCESS_OK.equals(response.getCode())
                    && response.getBrandList() != null) {
                // 品牌信息存在
                jdVenderBrandPubInfo = response.getBrandList();
            }
        } catch (Exception e) {
            logger.error("调用京东API获取品牌信息失败 [channel_id={}, cart_id={}, brand_name={}]", new Object[]{shop.getOrder_channel_id(), shop.getCart_id(), brandName});
            throw new BusinessException(shop.getShop_name() + "取得京东商家品牌信息失败 " + e.getMessage());
        }
        
        return jdVenderBrandPubInfo;
    }

    /**
     * 添加商家商品销售属性
     *
     * @param shop       店铺信息
     * @param catId      类目id
     * @param idx        属性值index
     * @param attrId     属性id
     * @param attrValue  属性值
     * @param features   属性值特征(如果属性是颜色，必须传入例如:class:#FFFFFF; 尺码的时候不用填)
     * @param status     属性值状态(-1：删除, 0：停用，1：显示，2：隐藏)  默认为1(显示)
     * @return List<AttValue>      京东类目属性值列表
     */
    public boolean addWareVenderSellSku(ShopBean shop, String catId, int idx, String attrId, String attrValue,
                                               String features, String status) {

        WareAddVenderSellSkuRequest request = new WareAddVenderSellSkuRequest();

        // 类目id
        request.setCategory_id(catId);
        // 排序编号,最多3位，不能为负数
        request.setIndex_id(StringUtils.toString(idx));
        // 属性id
        request.setAttribute_id(attrId);
        // 属性值
        request.setAttribute_value(attrValue);
        // 属性值特征(如果属性是颜色，必须传入例如:class:#FFFFFF; 尺码的时候不用填)
        if (!StringUtils.isEmpty(features))  request.setFeatures(features);
        // 属性值状态(-1：删除, 0：停用，1：显示，2：隐藏)  默认为1(显示)
        request.setStatus(StringUtils.isEmpty(status) ? "1" : status);

        try {
            // 调用京东添加商家商品销售属性API(360buy.wares.vendersellsku.add)
            WareAddVenderSellSkuResponse response = reqApi(shop, request);

            if (response != null) {
                // 京东返回正常的场合
                if (JdConstants.C_JD_RETURN_SUCCESS_OK.equals(response.getCode())) {
                    return true;
                }
            }
        } catch (Exception ex) {
            String errMsg = String.format("调用京东API添加商家商品销售属性值失败! [channelId:%s] [cartId:%s] [catId:%s], " +
                    "[attrId:%s] [attrValue:%s] [features:%s] [status:%s] [errMsg:%s]", shop.getOrder_channel_id(), shop.getCart_id(),
                    catId, attrId, attrValue, features, status, ex.getMessage());
            logger.error(errMsg);

            throw new BusinessException(shop.getShop_name() + errMsg);
        }

        return false;
    }

    /**
     * 获取单个类目信息
     *
     * @param shop ShopBean  店铺信息
     * @param catId Long  京东类目Id
     * @return CategoryReadService.Category  类目信息
     */
    public com.jd.open.api.sdk.domain.list.CategoryReadService.Category getCategoryById(ShopBean shop, Long catId, StringBuilder failCause) throws BusinessException {
        CategoryReadFindByIdRequest request = new CategoryReadFindByIdRequest();

        // 类目id
        request.setCid(catId);
        // 需要返回的字段列表
        request.setField("fid,id,lev,name,order,features");

        try {
            // 调用京东获取单个类目信息API(jingdong.category.read.findById)
            CategoryReadFindByIdResponse response = reqApi(shop, request);

            if (response != null) {
                // 京东返回正常的场合
                if (JdConstants.C_JD_RETURN_SUCCESS_OK.equals(response.getCode())) {
                    return response.getCategory();
                } else {
                    // 京东返回失败的场合
                    throw new BusinessException(response.getZhDesc());
                }
            } else {
                // response = null（https://api.jd.com/routerjson）不能访问的可能原因是服务器禁掉了https端口
                // 或app_url,app_key等不正确
                throw new BusinessException("京东根据类目ID获取单个类目信息返回应答为空(response = null)");
            }
        } catch (Exception ex) {
            String errMsg = String.format("调用京东API获取单个类目信息失败! [catId:%s] [errMsg:%s]", StringUtils.toString(catId), ex.getMessage());
            logger.error(errMsg);
            failCause.append(errMsg);
        }

        return null;
    }

}
