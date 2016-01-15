/**
 * (c) Copyright Voyageone Corp 2016
 */

package com.voyageone.web2.cms.wsdl.service;

import java.util.Arrays;
import java.util.Map;

import com.voyageone.web2.sdk.api.domain.CmsBtPromotionModel;
import com.voyageone.web2.sdk.api.domain.CmsBtTagModel;
import com.voyageone.web2.sdk.api.request.TagAddRequest;
import com.voyageone.web2.sdk.api.response.TagAddResponse;
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

	@Autowired
	private TagService tagService;

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
					.insert(insertTagsAndGetNewModel(promotionPutRequest.getCmsBtPromotionModel())));
		}
		return response;
	}

	/**
	 *
	 * @param cmsBtPromotionModel
	 * @return
     */
	private CmsBtPromotionModel insertTagsAndGetNewModel(CmsBtPromotionModel cmsBtPromotionModel){
		TagAddRequest requestModel = new TagAddRequest();
		requestModel.setChannelId(cmsBtPromotionModel.getChannelId());
		requestModel.setTagName(cmsBtPromotionModel.getPromotionName());
		requestModel.setTagType(2);
		requestModel.setTagStatus(0);
		requestModel.setParentTagId(0);
		requestModel.setSortOrder(0);
		requestModel.setCreater(cmsBtPromotionModel.getCreater());
		TagAddResponse res =tagService.addTag(requestModel);
		cmsBtPromotionModel.setRefTagId(res.getTag().getTagId());
		return cmsBtPromotionModel;
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
