package com.voyageone.cms.service.impl;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.core.MessageConstants;
import com.voyageone.core.ajax.AjaxResponseBean;
import com.voyageone.core.ajax.dt.DtResponse;
import com.voyageone.core.modelbean.UserSessionBean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyageone.cms.dao.PromotionDao;
import com.voyageone.cms.formbean.PromotionProduct;
import com.voyageone.cms.formbean.PromotionYearList;
import com.voyageone.cms.modelbean.Promotion;
import com.voyageone.cms.modelbean.RelationPromotionProduct;
import com.voyageone.cms.service.PromotionEditService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * Title: PromotionEditServiceImpl Description:商品活动模块
 * 
 * @author:eric
 * @date:2015年8月10日
 * 
 *
 */
@Service
public class PromotionEditServiceImpl implements PromotionEditService {
	@Autowired
	private PromotionDao promotionDao;

	private static Log logger = LogFactory.getLog(PromotionEditServiceImpl.class);

	/**
	 * 获取Promotion信息
	 * 
	 * @param data
	 * @return
	 */
	@Override
	public Promotion getPromotionInfo(Map<String, Object> data) {
		Promotion promotionInfo = promotionDao.getPromotionInfo(data);
		return promotionInfo;
	}

	/**
	 * 判断Promotion是否存在
	 * 
	 * @param data
	 * @return
	 */
	@Override
	public boolean isExistsPromotion(Map<String, Object> data) {
		boolean isSuccess = false;
		Promotion promotionInfo = promotionDao.isExistsPromotion(data);
		if (promotionInfo != null) {
			isSuccess = true;
		}
		return isSuccess;
	}

	/**
	 * 对Promotion信息进行更新
	 * 
	 * @param promotion
	 * @return
	 */
	@Override
	public boolean updatePromotion(Promotion promotion, UserSessionBean user) {
		promotion.setModifier(user.getUserName());
		boolean isSuccess = promotionDao.updatePromotionInfo(promotion);
		return isSuccess;
	}

	/**
	 * 删除Promotion信息
	 * 
	 * @param data
	 * @return
	 */
	@Override
	public boolean deletePromotion(Map<String, Object> data) {
		boolean isSuccess = promotionDao.deletePromotionInfo(data);
		return isSuccess;

	}

	/**
	 * 插入Promotion信息
	 * 
	 * @param promotion
	 * @return
	 */
	@Override
	public boolean insertPromotion(Promotion promotion, UserSessionBean user) {
		promotion.setCreater(user.getUserName());
		promotion.setModifier(user.getUserName());
		boolean isSuccess = promotionDao.insertPromotionInfo(promotion);
		return isSuccess;
	}

	/**
	 * 获取Promotion下的SubPromotion
	 */
	@Override
	public List<Promotion> getSubPromotionList(Map<String, Object> data) {
		List<Promotion> ret = promotionDao.getSubPromotionList(data);
		return ret;
	}

	/**
	 * 获取Promotion下的Product
	 * 
	 * @param data
	 * @return
	 */
	@Override
	public List<PromotionProduct> getPromotionProduct(Map<String, Object> data) {
		List<PromotionProduct> ret = promotionDao.getPromotionProduct(data);
		return ret;
	}

	/**
	 * 获取分页的PromotionProduct
	 * 
	 * @param data
	 * @return
	 */
	@Override
	public DtResponse<List<PromotionProduct>> getPromotionProductPage(Map<String, Object> data) {

		DtResponse<List<PromotionProduct>> dtResponse = new DtResponse<List<PromotionProduct>>();

		List<PromotionProduct> ret = promotionDao.getPromotionProduct(data);
        
		dtResponse.setData(ret);

		int count = promotionDao.getPromotionProductCount(data);

		dtResponse.setRecordsTotal(count);

		dtResponse.setRecordsFiltered(count);
		return dtResponse;

	}

	@Override
	public List<PromotionYearList> getPromotionList(String channelId) {
		List<PromotionYearList> promotionList = promotionDao.getPromotionList(channelId);
		return promotionList;
	}

	/**
	 * 获取Promotion的月份
	 * 
	 * @param channelId
	 *            渠道Id
	 */

	@Override
	public Map<String, Object> getPromotionMonth(String channelId) {
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("channelId", channelId);
		try {

			paramMap.put("cartsId", getCartIdList("US"));
			resultMap.put("promMothListUS", promotionDao.getPromMoth(paramMap));

			paramMap.put("cartsId", getCartIdList("CH"));
			resultMap.put("promMothListCH", promotionDao.getPromMoth(paramMap));

			return resultMap;
		} catch (Exception e) {
			logger.error(e);
			throw new BusinessException(MessageConstants.ComMsg.SYSTEM_ERR);
		}
	}

	/**
	 * 根据月份获取所有的活动
	 * 
	 * @param paramMap
	 *            channelId:渠道； cartLocation: 店铺区域（US，CH）; promMonth: 活动月份
	 */
	@Override
	public Map<String, Object> getPromInfo(Map<String, Object> paramMap) {
		Map<String, Object> resultMap = new HashMap<>();
		try {
			String cartLocation = (String) paramMap.get("cartLocation");
			paramMap.put("cartsId", getCartIdList(cartLocation));
			if ("US".equals(cartLocation)) {
				resultMap.put("promInfoListUS", promotionDao.getPromInfo(paramMap));
			} else if ("CH".equals(cartLocation)) {
				resultMap.put("promInfoListCH", promotionDao.getPromInfo(paramMap));
			}
			return resultMap;
		} catch (Exception e) {
			logger.error(e);
			throw new BusinessException(MessageConstants.ComMsg.SYSTEM_ERR);
		}
	}

	/**
	 * 建立产品和活动之间的关系
	 * 
	 * @param promotionIdArray
	 *            promotionId的集合，productId数组
	 * @param productIdArray
	 *            productId 数组
	 */
	@Override
	public Map<String, Object> createPromProductRelation(UserSessionBean user, String[] promotionIdArray, String[] productIdArray) {
		Map<String, Object> resultMap = new HashMap<>();
		int insertCount = 0;
		try {
			List<PromotionProduct> promotionProductList = promotionDao.getPromotionInfoById(promotionIdArray);
			List<PromotionProduct> promotionProductParamList = new ArrayList<>();
			for (PromotionProduct promotionProduct : promotionProductList) {
				for (String productId : productIdArray) {
					PromotionProduct promotionProductTemp = new PromotionProduct();
					promotionProductTemp.setPromotionId(promotionProduct.getPromotionId());
					promotionProductTemp.setPromotionYear(promotionProduct.getPromotionYear());
					promotionProductTemp.setPromotionMonth(promotionProduct.getPromotionMonth());
					promotionProductTemp.setChannelId(promotionProduct.getChannelId());
					promotionProductTemp.setProductId(productId);
					promotionProductTemp.setModifier(user.getUserName());
					promotionProductParamList.add(promotionProductTemp);
				}
			}
			promotionDao.createPromotionProductRelation(promotionProductParamList);
			resultMap.put("successFlg", true);
			return resultMap;
		} catch (Exception e) {
			logger.error(e);
			throw new BusinessException(MessageConstants.ComMsg.SYSTEM_ERR);
		}
	}

	private List<Integer> getCartIdList(String place) {
		List<Integer> catIds = new ArrayList<>();
		if ("US".equals(place)) {
			catIds.add(5);
			catIds.add(6);
		} else if ("CH".equals(place)) {
			catIds.add(20);
			catIds.add(21);
			catIds.add(22);
			catIds.add(23);
			catIds.add(24);
			catIds.add(25);
			catIds.add(26);
		}
		return catIds;
	}

	/**
	 * 更新RelationPromotionProduct
	 */
	@Override
	public boolean updateRelationPromotionProduct(RelationPromotionProduct relationPromotionProduct) {
		boolean isSuccess = promotionDao.updatePromotionProduct(relationPromotionProduct);
		return isSuccess;
	}

	@Override
	public boolean updateBatchPromotionProduct(Map<String, Object> data) {
		boolean isSuccess = false;
		List<Integer> productList = (List<Integer>) data.get("productIdList");
		String promotionId = (String) data.get("promotionId");
		String channelId = (String) data.get("channelId");
		String discountPercent = (String) data.get("discountPercent");
		Double discountPercentDouble = (100 - Double.parseDouble(discountPercent)) / 100;
		if (productList != null && productList.size() > 0) {
			for (Integer productId : productList) {
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("productId", productId);
				param.put("promotionId", promotionId);
				param.put("channelId", channelId);
				PromotionProduct product = promotionDao.getPromotionProductInfo(param);
				String price = product.getPrice();
				RelationPromotionProduct relationPromotionProduct = new RelationPromotionProduct();
				if (price != null) {
					Double priceDouble = Double.parseDouble(price);
					Double discountSalePrice = priceDouble * discountPercentDouble;
					relationPromotionProduct.setDiscountSalePrice(String.valueOf(discountSalePrice));
				}
				relationPromotionProduct.setCurrentPrice(price);
				relationPromotionProduct.setProductId(productId);
				relationPromotionProduct.setPromotionId(Integer.parseInt(promotionId));
				relationPromotionProduct.setDiscountPercent(discountPercent);
				isSuccess = updateRelationPromotionProduct(relationPromotionProduct);
			}
		}
		return isSuccess;

	}

	/**
	 * 批量删除
	 * 
	 * @param data
	 * @return
	 */
	@Override
	public boolean deletePromotionProduct(Map<String, Object> data) {
		boolean isSuccess = promotionDao.deletePromotionProduct(data);
		return isSuccess;
	}

	/**
	 * 获取PromotionProductCount条数
	 */
	@Override
	public int getPromotionProductCount(Map<String, Object> data) {
		int count = promotionDao.getPromotionProductCount(data);
		return count;
	}
}
