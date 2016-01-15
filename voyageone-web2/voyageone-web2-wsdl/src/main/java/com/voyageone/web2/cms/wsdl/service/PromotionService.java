/**
 * (c) Copyright Voyageone Corp 2016
 */

package com.voyageone.web2.cms.wsdl.service;

import com.voyageone.web2.cms.wsdl.BaseService;
import com.voyageone.web2.cms.wsdl.dao.CmsBtPromotionDao;
import com.voyageone.web2.sdk.api.domain.CmsBtPromotionModel;
import com.voyageone.web2.sdk.api.request.PromotionsDeleteRequest;
import com.voyageone.web2.sdk.api.request.PromotionsGetRequest;
import com.voyageone.web2.sdk.api.request.PromotionsPutRequest;
import com.voyageone.web2.sdk.api.request.TagAddRequest;
import com.voyageone.web2.sdk.api.response.PromotionsGetResponse;
import com.voyageone.web2.sdk.api.response.PromotionsPutResponse;
import com.voyageone.web2.sdk.api.response.TagAddResponse;
import org.apache.commons.beanutils.BeanMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * product Service
 *
 * @author binbin.gao 16/01/14
 * @version 2.0.0
 * @since. 2.0.0
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
	 * @param promotionPutRequest Request
	 * @return PromotionsPutResponse
	 */
	public PromotionsPutResponse saveOrUpdate(
			PromotionsPutRequest promotionPutRequest) {
		//事物处理？？?
		PromotionsPutResponse response = new PromotionsPutResponse();
		if (promotionPutRequest.getCmsBtPromotionModel().getPromotionId() > 0) {
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
	 * @param cmsBtPromotionModel PromotionModel
	 * @return CmsBtPromotionModel
     */
	private CmsBtPromotionModel insertTagsAndGetNewModel(CmsBtPromotionModel cmsBtPromotionModel){
		TagAddRequest requestModel = new TagAddRequest();
		requestModel.setChannelId(cmsBtPromotionModel.getChannelId());
		requestModel.setTagName(cmsBtPromotionModel.getPromotionName());
		requestModel.setTagType(2);
		requestModel.setTagStatus(0);
		requestModel.setParentTagId(0);
		requestModel.setSortOrder(0);
		requestModel.setModifier(cmsBtPromotionModel.getModifier());
		TagAddResponse res = tagService.addTag(requestModel);
		cmsBtPromotionModel.setRefTagId(res.getTag().getTagId());
		return cmsBtPromotionModel;
	}

	/**
	 * 根据条件查询
	 * 
	 * @param promotionGetRequest Request
	 * @return PromotionsGetResponse
	 */
	public PromotionsGetResponse selectByCondition(
			PromotionsGetRequest promotionGetRequest) {
		PromotionsGetResponse response = new PromotionsGetResponse();
		List<CmsBtPromotionModel> models = new ArrayList<>();
		if (promotionGetRequest.getPromotionId() != null && promotionGetRequest.getPromotionId() > 0) {
			CmsBtPromotionModel model = cmsBtPromotionDao.findById(convertCondtionMap(promotionGetRequest));
			if (model != null) {
				models.add(model);
			}

		} else {
			List<CmsBtPromotionModel> modelsTmp = cmsBtPromotionDao.findByCondition(convertCondtionMap(promotionGetRequest));
			if (modelsTmp != null) {
				models.addAll(modelsTmp);
			}
		}
		response.setCmsBtPromotionModels(models);
		response.setTotalCount((long)models.size());
		return response;
	}

	/**
	 * 删除
	 * 
	 * @param promotionsDeleteRequest Request
	 * @return PromotionsPutResponse
	 */
	public PromotionsPutResponse deleteById(
			PromotionsDeleteRequest promotionsDeleteRequest) {
		PromotionsPutResponse response = new PromotionsPutResponse();
		response.setRemovedCount(cmsBtPromotionDao
				.deleteById(convertCondtionMap(promotionsDeleteRequest)));
		return response;
	}

	/**
	 * 构造条件map
	 * 
	 * @param obj input
	 * @return Map
	 */
	private static Map<?, ?> convertCondtionMap(Object obj) {
		return new BeanMap(obj);
	}
}
