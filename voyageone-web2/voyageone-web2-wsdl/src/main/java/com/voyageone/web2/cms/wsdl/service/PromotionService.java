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
import org.springframework.util.Assert;

/**
 * @description promotion sdk服务
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
	 * @param promotionsPutRequest put请求参数
	 * @return put响应
	 */
	public PromotionsPutResponse saveOrUpdate(
			PromotionsPutRequest promotionsPutRequest) {
		validatePutParameters(promotionsPutRequest);
		PromotionsPutResponse response = new PromotionsPutResponse();
		CmsBtPromotionModel cmsBtPromotionModel=promotionsPutRequest
				.getCmsBtPromotionModel();
		if(cmsBtPromotionModel.getPromotionId()>0){
			response.setModifiedCount(cmsBtPromotionDao
					.update(cmsBtPromotionModel));
		}else {
			response.setInsertedCount(cmsBtPromotionDao
					.insert(insertTagsAndGetNewModel(cmsBtPromotionModel)));
		}
		return response;
	}

	/**
	 * 添加标签并设置标签id到model
	 * @param cmsBtPromotionModel 模型
	 * @return set TagId后的模型
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
	 * @param promotionsGetRequest 查询条件
	 * @return 查询结果
	 */
	public PromotionsGetResponse selectByCondition(
			PromotionsGetRequest promotionsGetRequest) {
		validateGetParameters(promotionsGetRequest);
		PromotionsGetResponse response = new PromotionsGetResponse();
		if (promotionsGetRequest.getPromotionId() > 0) {
			response.setCmsBtPromotionModels(Arrays.asList(cmsBtPromotionDao
					.findById(constructionCondtionMap(promotionsGetRequest))));
		} else {
			response.setCmsBtPromotionModels(cmsBtPromotionDao
					.findByCondition(constructionCondtionMap(promotionsGetRequest)));
		}
		return response;
	}


	/**
	 * 删除
	 * 
	 * @param promotionsDeleteRequest 删除参数
	 * @return 删除结果
	 */
	public PromotionsPutResponse deleteById(
			PromotionsDeleteRequest promotionsDeleteRequest) {
		validateDeleteParameters(promotionsDeleteRequest);
		PromotionsPutResponse response = new PromotionsPutResponse();
		response.setRemovedCount(cmsBtPromotionDao
				.deleteById(constructionCondtionMap(promotionsDeleteRequest)));
		return response;
	}

	/**
	 * 构造条件map
	 * 
	 * @param obj 对象
	 * @return beanMap of Map
	 */
	private static Map<?, ?> constructionCondtionMap(Object obj) {
		return new BeanMap(obj);
	}

	/**
	 * Put参数校验
	 * @param promotionsPutRequest Put参数
	 */
	private void validatePutParameters(PromotionsPutRequest promotionsPutRequest){
		Assert.notNull(promotionsPutRequest,"request不能为null");
		Assert.notNull(promotionsPutRequest.getCmsBtPromotionModel(),"cmsBtPromotionModel不能为null");
	}

	/**
	 * Get参数校验
	 * @param promotionGetRequest Get参数
	 */
	private void validateGetParameters(PromotionsGetRequest promotionGetRequest){
		Assert.notNull(promotionGetRequest,"request不能为null");
	}

	/**
	 * Del参数校验
	 * @param promotionsDeleteRequest Del参数
	 */
	private void validateDeleteParameters(PromotionsDeleteRequest promotionsDeleteRequest){
		Assert.notNull(promotionsDeleteRequest.getPromotionId(),"promotionId不能为null");
	}
}
