package com.voyageone.components.jd.service;

import com.jd.open.api.sdk.domain.category.AttValue;
import com.jd.open.api.sdk.domain.category.Category;
import com.jd.open.api.sdk.domain.list.CategoryAttrReadService.CategoryAttr;
import com.jd.open.api.sdk.domain.list.CategoryAttrValueReadService.CategoryAttrValue;
import com.jd.open.api.sdk.request.category.CategoryAttributeValueSearchRequest;
import com.jd.open.api.sdk.request.category.CategorySearchRequest;
import com.jd.open.api.sdk.request.list.CategoryReadFindAttrsByCategoryIdRequest;
import com.jd.open.api.sdk.request.list.CategoryReadFindValuesByAttrIdRequest;
import com.jd.open.api.sdk.response.category.CategoryAttributeValueSearchResponse;
import com.jd.open.api.sdk.response.category.CategorySearchResponse;
import com.jd.open.api.sdk.response.list.CategoryReadFindAttrsByCategoryIdResponse;
import com.jd.open.api.sdk.response.list.CategoryReadFindValuesByAttrIdResponse;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.components.jd.JdBase;
import com.voyageone.components.jd.JdConstants;
import com.voyageone.components.jd.bean.JdCategoryAttrBean;
import com.voyageone.components.jd.bean.JdCategoryAttrValueBean;
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
	 * @throws BusinessException 业务异常
	 */
	public List<Category> getCategoryInfo(ShopBean shop) throws BusinessException {
		List<Category> categoryList = new ArrayList<>();

		// 京东类目信息取得request（未设置fields，取得全部类目项目）
        CategorySearchRequest request = new CategorySearchRequest();

		// For test only start
		shop.setAppKey("BFA3102EFD4B981E9EEC2BE32DF1E44E");
		shop.setAppSecret("90742900899f49a5acfaf3ec1040a35c");
		// For test only end

		try {
			// 调用京东商家类目信息API(360buy.warecats.get)
//			request.setFields("id,fid,name,index_id,status,lev,isParent");
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
     *        catId String             类目id
     *        attributeType int        类目id
     *
     * @return List<CategoryAttr>      京东类目属性列表
     * @throws BusinessException 业务异常
     */
    public List<CategoryAttr> getCategoryAttrInfo(ShopBean shop, String catId, int attributeType) throws BusinessException {
        List<CategoryAttr> jdCategoryAttrList = new ArrayList<>();

        CategoryReadFindAttrsByCategoryIdRequest request = new CategoryReadFindAttrsByCategoryIdRequest();

        // 类目id
        request.setCid(Long.parseLong(catId));
        // 属性类型（属性类型.1:关键属性 2:不变属性 3:可变属性 4:销售属性）
        request.setAttributeType(attributeType);
        // 需要返回的字段列表(非必须)
//        String fields = "aid,name,is_key_prop,is_color_prop,is_size_prop,is_sale_prop,index_id,status,att_type,input_type,is_req,is_fet,is_nav,cid,group_id";
//        request.setFields(fields);

        // For test only start
        shop.setAppKey("BFA3102EFD4B981E9EEC2BE32DF1E44E");
        shop.setAppSecret("90742900899f49a5acfaf3ec1040a35c");
        // For test only end

        try {
            // 调用京东商家类目属性信息API(jingdong.category.read.findAttrsByCategoryId)
            CategoryReadFindAttrsByCategoryIdResponse response = reqApi(shop, request);

            if (response != null) {
                // 京东返回正常的场合
                if (JdConstants.C_JD_RETURN_SUCCESS_OK.equals(response.getCode())) {
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
     *        categoryAttrId String    类目属性id
     *
     * @return List<CategoryAttrValue> 京东类目属性值列表
     * @throws BusinessException 业务异常
     */
    public List<CategoryAttrValue> getCategoryAttrValueInfo(ShopBean shop, Long categoryAttrId) throws BusinessException {
        List<CategoryAttrValue> jdCategoryAttrValueList = new ArrayList<>();

        CategoryReadFindValuesByAttrIdRequest request = new CategoryReadFindValuesByAttrIdRequest();

        // 类目属性id
        request.setCategoryAttrId(categoryAttrId);
        // 需要返回的字段列表(非必须)
//        String fields = "aid,name,is_key_prop,is_color_prop,is_size_prop,is_sale_prop,index_id,status,att_type,input_type,is_req,is_fet,is_nav,cid,group_id";
//        request.setFields(fields);

        // For test only start
        shop.setAppKey("BFA3102EFD4B981E9EEC2BE32DF1E44E");
        shop.setAppSecret("90742900899f49a5acfaf3ec1040a35c");
        // For test only end

        try {
            // 调用京东商家类目属性信息API(jingdong.category.read.findValuesByAttrId)
            CategoryReadFindValuesByAttrIdResponse response = reqApi(shop, request);

            if (response != null) {
                // 京东返回正常的场合
                if (JdConstants.C_JD_RETURN_SUCCESS_OK.equals(response.getCode())) {
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

//	/**
//	 * 取得京东类目属性信息
//	 *
//	 * @param shop ShopBean            店铺信息
//	 *        cartId String            店铺ID
//	 * @return List<JdCategoryAttr>    京东类目属性（属性值）
//	 * @throws Exception
//	 */
//	public ArrayList<JdCategoryAttrBean> getCategoryAttrInfo(ShopBean shop, String cartId) throws Exception {
//		ArrayList<JdCategoryAttrBean> jdCategoryAttrList = new ArrayList<>();
//
//		CategoryAttributeSearchRequest request = new CategoryAttributeSearchRequest();
//
//		// 类目id
//		request.setCid(cartId);
//		// 是否关键属性（这个地方只能填false）
//		request.setKeyProp("false");
//		// 是否销售属性（这个地方只能false，填true的话只返回销售属性）
//		request.setSaleProp("false");
//		// 需要返回的字段列表
//		String fields = "aid,name,is_key_prop,is_color_prop,is_size_prop,is_sale_prop,index_id,status,att_type,input_type,is_req,is_fet,is_nav,cid,group_id";
//		request.setFields(fields);
////		StringBuilder fields = new StringBuilder();
////		fields.append("id");                    // 类目id
////		fields.append(",fid");                  // 父类目id
////		fields.append(",name");                 // 类目名称 
////		fields.append(",index_id");             // 排序（越小越靠前）
////		fields.append(",status");               // 类目状态（DELETED,UNVALID,VALID
////		fields.append(",lev");                  // 等级（类目分为1、2、3级）
////		fields.append(",isParent");             // 该类目是否为父类目（即：该类目是否还有子类目）
////		request.setFields(fields.toString());
//
//		try {
//			// 调用京东商家类目属性信息API
//			CategoryAttributeSearchResponse response = reqApi(shop, request);
//
//            if (response != null) {
//				// 京东返回正常的场合
//				if (JdConstants.C_JD_RETURN_SUCCESS_OK.equals(response.getCode())) {
//					// 类目信息存在
//					for (CategoryAttributeSearchResponse.Attribute attribute : response.getAttributes()) {
//						JdCategoryAttrBean jdCategoryAttr = new JdCategoryAttrBean();
//						// 属性id
//						jdCategoryAttr.setAid(attribute.getAid());
//						// 属性名
//						jdCategoryAttr.setName(attribute.getName());
//						// 状态
//						jdCategoryAttr.setStatus(attribute.getStatus());
//						// 类目id(叶子)
//						jdCategoryAttr.setCid(attribute.getCid());
//						// 排序
//						jdCategoryAttr.setIndexId(attribute.getIndexId());
//						// 属性类型（1关键属性，2不变属性，3可变属性，4销售属性）
//						jdCategoryAttr.setAttType(attribute.getAttType());
//						// 输入类型（1单选，2多选，3可输入最大50个字符）
//						jdCategoryAttr.setInputType(attribute.getInputType());
//						// 组id
//						jdCategoryAttr.setGroupId(attribute.getGroupId());
//						// 关键属性
//						jdCategoryAttr.setKeyProp(attribute.getKeyProp());
//						// 销售属性
//						jdCategoryAttr.setSaleProp(attribute.getSaleProp());
//						// 是否筛选
//						jdCategoryAttr.setFet(attribute.getFet());
//						// 是否导航
//						jdCategoryAttr.setNav(attribute.getNav());
//						// 是否必填
//						jdCategoryAttr.setReq(attribute.getReq());
//						// 颜色属性
//						jdCategoryAttr.setColorProp(attribute.isColorProp());
//						// 尺码属性
//						jdCategoryAttr.setSizeProp(attribute.isSizeProp());
//						// 可选值
//						jdCategoryAttr.setOptions(attribute.getOptions());
//
//						jdCategoryAttr.setAttrValueList(new ArrayList<JdCategoryAttrValueBean>());
//						jdCategoryAttrList.add(jdCategoryAttr);
//					}
//				}
//			}
//		} catch (Exception ex) {
//			logger.error("调用京东API获取京东类目属性失败 " + "channel_id:" + shop.getOrder_channel_id() + ",cart_id:" + shop.getCart_id());
//
//			throw new Exception(shop.getShop_name() + "取得京东商家类目属性信息失败 " + ex.getMessage());
//		}
//
//		return jdCategoryAttrList;
//	}

	/**
	 * 取得京东类目属性值信息
	 *
	 * @param shop ShopBean
	 *        jdCategoryAttrList List<JdCategoryAttr>
	 * @return List<JdCategoryAttr>
	 * @throws Exception
	 */
	public ArrayList<JdCategoryAttrBean> getCategoryAttrValueInfo(ShopBean shop, ArrayList<JdCategoryAttrBean> jdCategoryAttrList) throws Exception {

		for (JdCategoryAttrBean jdCategoryAttr : jdCategoryAttrList) {

			CategoryAttributeValueSearchRequest request = new CategoryAttributeValueSearchRequest();

			// 属性值Id
			request.setAvs(String.valueOf(jdCategoryAttr.getAid()));
			// 需要返回的字段列表
			String fields = "aid,vid,name,status,index_id,features";
			request.setFields(fields);
//			StringBuilder fields = new StringBuilder();
//			fields.append("id");                    // 类目id
//			fields.append(",fid");                  // 父类目id
//			fields.append(",name");                 // 类目名称 
//			fields.append(",index_id");             // 排序（越小越靠前）
//			fields.append(",status");               // 类目状态（DELETED,UNVALID,VALID
//			fields.append(",lev");                  // 等级（类目分为1、2、3级）
//			fields.append(",isParent");             // 该类目是否为父类目（即：该类目是否还有子类目）
//			request.setFields(fields.toString());

			try {
				// 调用京东商家类目属性值信息API
				CategoryAttributeValueSearchResponse response = reqApi(shop, request);

				if (response != null) {
					// 京东返回正常的场合
					if (JdConstants.C_JD_RETURN_SUCCESS_OK.equals(response.getCode())) {
						// 类目信息存在
						for (AttValue attrValue : response.getAttValues()) {
							JdCategoryAttrValueBean jdCategoryAttrValue = new JdCategoryAttrValueBean();
							// 属性id
							jdCategoryAttrValue.setAid(attrValue.getAid());
							// 属性值id
							jdCategoryAttrValue.setVid(attrValue.getVid());
							// 属性值名字
							jdCategoryAttrValue.setName(attrValue.getName());
							// 状态
							jdCategoryAttrValue.setStatus(attrValue.getStatus());
							// Features
							jdCategoryAttrValue.setFeatures(attrValue.getFeatures());
							// 排序
							jdCategoryAttrValue.setIndexId(attrValue.getIndexId());

							jdCategoryAttr.getAttrValueList().add(jdCategoryAttrValue);
						}
					}
				}
			} catch (Exception ex) {
				logger.error("调用京东API获取京东类目属性值失败 " + "channel_id:" + shop.getOrder_channel_id() + ",cart_id:" + shop.getCart_id());

				throw new Exception(shop.getShop_name() + "取得京东商家类目属性值信息失败 " + ex.getMessage());
			}
		}

		return jdCategoryAttrList;
	}

//	/**
//	 * 辅助方法：在更新商品时，全量更新需要将不更改的值，从 Default Value 中设置到 Valued
//	 */
//	private Field getField(String input_type) {
//		// 对特定字段进行处理
//
//		switch (input_type) {
//			case INPUT:
//				InputField inputField = (InputField) field;
//				inputField.setValue(inputField.getDefaultValue());
//				break;
//			case MULTIINPUT:
//				MultiInputField multiInputField = (MultiInputField) field;
//				multiInputField.setValues(multiInputField.getDefaultValues());
//				break;
//			case MULTICHECK:
//				MultiCheckField multiCheckField = (MultiCheckField) field;
//				multiCheckField.setValues(multiCheckField.getDefaultValuesDO());
//				break;
//			case SINGLECHECK:
//				SingleCheckField singleCheckField = (SingleCheckField) field;
//				singleCheckField.setValue(singleCheckField.getDefaultValue());
//				break;
//			case COMPLEX:
//				ComplexField complexField = (ComplexField) field;
//				complexField.setComplexValue(complexField.getDefaultComplexValue());
//				break;
//			case MULTICOMPLEX:
//				MultiComplexField multiComplexField = (MultiComplexField) field;
//				multiComplexField.setComplexValues(multiComplexField.getDefaultComplexValues());
//				break;
//		}
//	}

}
