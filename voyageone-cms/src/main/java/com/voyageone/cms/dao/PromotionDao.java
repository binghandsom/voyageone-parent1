package com.voyageone.cms.dao;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.voyageone.cms.formbean.PromotionProduct;
import com.voyageone.cms.formbean.PromotionYearList;
import com.voyageone.cms.modelbean.Promotion;
import com.voyageone.cms.modelbean.RelationPromotionProduct;
import com.voyageone.common.Constants;

/**
 * 
 * Title: PromotionDao Description:商品活动模块
 * 
 * @author: eric
 * @date:2015年8月10日
 *
 */
@Repository
public class PromotionDao extends CmsBaseDao {
	/**
	 * 获取Promotion信息
	 * 
	 * @param data
	 * @return
	 */
	public Promotion getPromotionInfo(Map<String, Object> data) {
		Promotion ret = new Promotion();
		List<Promotion> info = (List) selectList(Constants.DAO_NAME_SPACE_CMS + "cms_get_Promotion", data);
		if (info != null && info.size() > 0) {
			ret = info.get(0);
		}
		return ret;
	}

	/**
	 * 判断是否存在相同的Promotion
	 * 
	 * @param data
	 * @return
	 */
	public Promotion isExistsPromotion(Map<String, Object> data) {
		Promotion ret = null;
		List<Promotion> info = (List) selectList(Constants.DAO_NAME_SPACE_CMS + "cms_get_IsPromotion", data);
		if (info != null && info.size() > 0) {
			ret = info.get(0);
		}
		return ret;

	}

	/**
	 * 更新Promotion信息
	 * 
	 * @param data
	 * @return
	 */
	public boolean updatePromotionInfo(Promotion data) {
		boolean isSuccess = false;
		int ret = updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "cms_update_promotion", data);
		if (ret > 0) {
			isSuccess = true;
		}
		return isSuccess;
	}

	/**
	 * 插入Promotion信息
	 * 
	 * @param data
	 * @return
	 */
	public boolean insertPromotionInfo(Promotion data) {
		boolean isSuccess = false;
		int ret = updateTemplateCms.insert(Constants.DAO_NAME_SPACE_CMS + "cms_insert_Promotion", data);
		if (ret > 0) {
			isSuccess = true;
		}
		return isSuccess;

	}

	/**
	 * 删除Promotion信息
	 * 
	 * @param data
	 * @return
	 */
	public boolean deletePromotionInfo(Map<String, Object> data) {
		boolean isSuccess = false;
		int ret = updateTemplateCms.delete(Constants.DAO_NAME_SPACE_CMS + "cms_delete_Promotion", data);
		if (ret > 0) {
			isSuccess = true;
		}
		return isSuccess;

	}

	/**
	 * 返回List<Promotion>
	 * 
	 * @param data
	 * @return
	 */
	public List<Promotion> getSubPromotionList(Map<String, Object> data) {

		List<Promotion> info = (List) selectList(Constants.DAO_NAME_SPACE_CMS + "cms_get_SubPromotionList", data);

		return info;

	}

	/**
	 * 获取Promotion下的产品
	 * 
	 * @param data
	 * @return
	 */
	public List<PromotionProduct> getPromotionProduct(Map<String, Object> data) {

		List<PromotionProduct> info = (List) selectList(Constants.DAO_NAME_SPACE_CMS + "cms_get_PromotionProduct", data);

		return info;

	}
	/**
	 * 获取PromotionProduct
	 * @param data
	 * @return
	 */
	public PromotionProduct getPromotionProductInfo (Map<String, Object> data) {
		PromotionProduct info = selectOne(Constants.DAO_NAME_SPACE_CMS + "cms_get_PromotionProduct", data);
		return info;
		
	}

	/**
	 * 获取Promotion月份
	 * 
	 * @param parmMap
	 *            <String: channelId; List<String> cartsId>
	 * @return proMotion月份List
	 */
	public List<String> getPromMoth(Map<String, Object> parmMap) {
		return selectList(Constants.DAO_NAME_SPACE_CMS + "cms_get_promotionMonth", parmMap);
	}

	/**
	 * 获取Promotion月份
	 * 
	 * @param parmMap
	 *            <String: channelId; String Month>
	 * @return proMotionInfo List
	 */
	public List<Promotion> getPromInfo(Map<String, Object> parmMap) {
		return selectList(Constants.DAO_NAME_SPACE_CMS + "cms_get_promotionInfo", parmMap);
	}

	public boolean updatePromotionProduct(RelationPromotionProduct  relationPromotionProduct) {
		boolean isSuccess = false;
		int ret = updateTemplateCms.update(Constants.DAO_NAME_SPACE_CMS + "cms_update_promotionProduct", relationPromotionProduct);
		if (ret > 0) {
			isSuccess = true;
		}
		return isSuccess;

	}

	/**
	 * 根据promotionIdList 获取对应的promotion实例
	 * 
	 * @param promotionIdArray
	 *            List<String>
	 * @return int 插入数据条数
	 */
	public List<PromotionProduct> getPromotionInfoById(String[] promotionIdArray) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("promotionIdList", Arrays.asList(promotionIdArray));
		return selectList(Constants.DAO_NAME_SPACE_CMS + "cms_get_promotionInfoById", paramMap);
	}
	/**
	 * 批量删除PromotionProduct
	 * @param data
	 * @return
	 */
    public boolean deletePromotionProduct (Map<String, Object> data) {
    	boolean isSuccess = false;
		int ret = updateTemplateCms.delete(Constants.DAO_NAME_SPACE_CMS + "cms_delete_promotionproduct", data);
		if (ret > 0) {
			isSuccess = true;
		}
		return isSuccess;
    }
	/**
	 * 插入数据到cms_bt_relation_promotion_product表，建立活动和产品之间的关系
	 * 
	 * @param promotionProducts
	 *            <String: channelId; String Month>
	 * @return int 插入数据条数
	 */
	public int createPromotionProductRelation(List<PromotionProduct> promotionProducts) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("promotionProducts", promotionProducts);
		return insert(Constants.DAO_NAME_SPACE_CMS + "cms_create_promotion_product_relation", paramMap);
	}

	/**
	 * getPromotionList
	 * 
	 * @param channelId
	 * @return
	 */
	public List<PromotionYearList> getPromotionList(String channelId) {
		return selectList(Constants.DAO_NAME_SPACE_CMS + "cms_get_PromotionMenuList", channelId);

	}

	/**
	 * getPromotionInfoList
	 * 
	 * @param channelId
	 * @return
	 */
	public List<Promotion> getPromotionInfoList(String channelId) {
		return selectList(Constants.DAO_NAME_SPACE_CMS + "cms_get_PromotionList", channelId);

	}

	public int getPromotionProductCount (Map<String, Object> data) {
		return  selectOne(Constants.DAO_NAME_SPACE_CMS + "cms_get_PromotionProductCount", data);
	}
}
