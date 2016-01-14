/**
 * (c) Copyright Voyageone Corp 2016
 */

package com.voyageone.web2.cms.wsdl.service;

import java.util.Arrays;
import java.util.Map;

import org.apache.commons.beanutils.BeanMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyageone.web2.cms.wsdl.BaseService;
import com.voyageone.web2.cms.wsdl.dao.CmsBtPromotionDao;
import com.voyageone.web2.sdk.api.request.PromotionsDeleteRequest;
import com.voyageone.web2.sdk.api.request.PromotionsGetRequest;
import com.voyageone.web2.sdk.api.request.PromotionsPutRequest;
import com.voyageone.web2.sdk.api.response.PromotionsGetResponse;
import com.voyageone.web2.sdk.api.response.PromotionsPutResponse;

/**
 * @description
 *
 * @author gbb
 */
@Service
public class PromotionService extends BaseService {

	@Autowired
	private CmsBtPromotionDao cmsBtPromotionDao;

	/**
	 * 添加或者修改
	 * 
	 * @param promotionPutRequest
	 * @return
	 */
	public PromotionsPutResponse saveOrUpdate(
			PromotionsPutRequest promotionPutRequest) {
		PromotionsPutResponse response = new PromotionsPutResponse();
		if (cmsBtPromotionDao
				.findById(constructionCondtionMap(promotionPutRequest
						.getCmsBtPromotionModel())) != null) {
			response.setMatchedCount(cmsBtPromotionDao
					.update(promotionPutRequest.getCmsBtPromotionModel()));
		} else {
			response.setInsertedCount(cmsBtPromotionDao
					.insert(promotionPutRequest.getCmsBtPromotionModel()));
		}
		return response;
	}

	/**
	 * 根据条件查询
	 * 
	 * @param promotionGetRequest
	 * @return
	 */
	public PromotionsGetResponse selectByCondition(
			PromotionsGetRequest promotionGetRequest) {
		PromotionsGetResponse response = new PromotionsGetResponse();
		if (promotionGetRequest.getPromotionId()!=null&&promotionGetRequest.getPromotionId().intValue() > 0) {
			response.setCmsBtPromotionModels(Arrays.asList(cmsBtPromotionDao
					.findById(constructionCondtionMap(promotionGetRequest))));
		} else {
			response.setCmsBtPromotionModels(cmsBtPromotionDao
					.findByCondition(constructionCondtionMap(promotionGetRequest)));
		}
		return response;
	}

	/**
	 * 删除
	 * 
	 * @param condtionParams
	 * @return
	 */
	public PromotionsPutResponse deleteById(
			PromotionsDeleteRequest promotionsDeleteRequest) {
		PromotionsPutResponse response = new PromotionsPutResponse();
		response.setRemovedCount(cmsBtPromotionDao
				.deleteById(constructionCondtionMap(promotionsDeleteRequest)));
		return response;
	}

	/**
	 * 构造条件map
	 * 
	 * @param obj
	 * @return
	 */
	private static Map<?, ?> constructionCondtionMap(Object obj) {
		return new BeanMap(obj);
	}
}
