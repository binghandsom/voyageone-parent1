package com.voyageone.cms.service;

import java.util.List;
import java.util.Map;

import com.voyageone.core.ajax.dt.DtResponse;
import com.voyageone.core.modelbean.UserSessionBean;

import org.springframework.stereotype.Service;

import com.voyageone.cms.formbean.PromotionProduct;
import com.voyageone.cms.formbean.PromotionYearList;
import com.voyageone.cms.modelbean.Promotion;
import com.voyageone.cms.modelbean.RelationPromotionProduct;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Service
public interface PromotionEditService {

	public Promotion getPromotionInfo(Map<String, Object> data);

	public boolean isExistsPromotion(Map<String, Object> data);

	public boolean updatePromotion(Promotion promotion, UserSessionBean user);

	public boolean deletePromotion(Map<String, Object> data);

	public boolean insertPromotion(Promotion promotion,  UserSessionBean user);

	public List<Promotion> getSubPromotionList(Map<String, Object> data);

	public List<PromotionProduct> getPromotionProduct(Map<String, Object> data);

	Map<String, Object> getPromotionMonth(String channelId);

	Map<String, Object> getPromInfo(Map<String, Object> paramMap);

	public boolean updateRelationPromotionProduct(RelationPromotionProduct  relationPromotionProduct);

	Map<String, Object> createPromProductRelation(UserSessionBean user, String[] promotionIdList, String[] productIdArray);

	public List<PromotionYearList> getPromotionList(String channelId);

	public int getPromotionProductCount(Map<String, Object> data);

	public DtResponse<List<PromotionProduct>> getPromotionProductPage(Map<String, Object> data);

	public boolean updateBatchPromotionProduct(Map<String, Object> data);

	public boolean deletePromotionProduct(Map<String, Object> data);

}
