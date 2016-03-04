/**
 * (c) Copyright Voyageone Corp 2016
 */

package com.voyageone.web2.cms.wsdl.service;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.web2.cms.wsdl.BaseService;
import com.voyageone.web2.cms.wsdl.dao.CmsBtPromotionDao;
import com.voyageone.web2.cms.wsdl.dao.CmsBtTagDao;
import com.voyageone.web2.sdk.api.domain.CmsBtPromotionModel;
import com.voyageone.web2.sdk.api.exception.ApiException;
import com.voyageone.web2.sdk.api.request.*;
import com.voyageone.web2.sdk.api.response.PromotionsGetResponse;
import com.voyageone.web2.sdk.api.response.PromotionsPutResponse;
import com.voyageone.web2.sdk.api.response.TagAddResponse;
import org.apache.commons.beanutils.BeanMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * product Service
 *
 * @author aooer 16/01/14
 * @version 2.0.0
 * @since. 2.0.0
 */
@Service
public class PromotionService extends BaseService {

    @Autowired
    private CmsBtPromotionDao cmsBtPromotionDao;

    @Autowired
    private CmsBtTagDao cmsBtTagDao;

    @Autowired
    private TagService tagService;

    /**
     * 添加或者修改
     *
     * @param promotionPutRequest Request
     * @return PromotionsPutResponse
     */
    @Transactional
    public PromotionsPutResponse saveOrUpdate(
            PromotionPutRequest promotionPutRequest) {
        promotionPutRequest.check();
        PromotionsPutResponse response = new PromotionsPutResponse();
        if (promotionPutRequest.getCmsBtPromotionModel().getPromotionId() != null) {
            response.setMatchedCount(cmsBtPromotionDao
                    .update(promotionPutRequest.getCmsBtPromotionModel()));
            promotionPutRequest.getCmsBtPromotionModel().getTagList().forEach(cmsBtTagModel -> {
                if(cmsBtTagDao.updateCmsBtTag(cmsBtTagModel) == 0){
                    cmsBtTagModel.setChannelId(promotionPutRequest.getCmsBtPromotionModel().getChannelId());
                    cmsBtTagModel.setParentTagId(promotionPutRequest.getCmsBtPromotionModel().getRefTagId());
                    cmsBtTagModel.setTagType(2);
                    cmsBtTagModel.setTagStatus(0);
                    cmsBtTagModel.setTagPathName(String.format("-%s-%s-", promotionPutRequest.getCmsBtPromotionModel().getPromotionName(), cmsBtTagModel.getTagName()));
                    cmsBtTagModel.setTagPath("");
                    cmsBtTagModel.setCreater(promotionPutRequest.getCmsBtPromotionModel().getModifier());
                    cmsBtTagModel.setModifier(promotionPutRequest.getCmsBtPromotionModel().getModifier());
                    cmsBtTagDao.insertCmsBtTag(cmsBtTagModel);
                    cmsBtTagModel.setTagPath(String.format("-%s-%s-", cmsBtTagModel.getParentTagId(), cmsBtTagModel.getTagId()));
                    cmsBtTagDao.updateCmsBtTag(cmsBtTagModel);
                }
            });
        } else {
            response.setInsertedCount(cmsBtPromotionDao
                    .insert(insertTagsAndGetNewModel(promotionPutRequest.getCmsBtPromotionModel())));
        }
        return response;
    }

    /**
     * @param cmsBtPromotionModel PromotionModel
     * @return CmsBtPromotionModel
     */
    private CmsBtPromotionModel insertTagsAndGetNewModel(CmsBtPromotionModel cmsBtPromotionModel) {
        TagAddRequest requestModel = new TagAddRequest();
        requestModel.setChannelId(cmsBtPromotionModel.getChannelId());
        requestModel.setTagName(cmsBtPromotionModel.getPromotionName());
        requestModel.setTagType(2);
        requestModel.setTagStatus(0);
        requestModel.setParentTagId(0);
        requestModel.setSortOrder(0);
        requestModel.setModifier(cmsBtPromotionModel.getModifier());
        TagAddResponse res = tagService.addTag(requestModel);
        if(!res.getCode().equalsIgnoreCase("0")){
            throw new ApiException(res.getCode(),res.getMessage());
        }
        cmsBtPromotionModel.setRefTagId(res.getTag().getTagId());

        // 子TAG追加
        cmsBtPromotionModel.getTagList().forEach(cmsBtTagModel -> {
            cmsBtTagModel.setChannelId(cmsBtPromotionModel.getChannelId());
            cmsBtTagModel.setParentTagId(res.getTag().getTagId());
            cmsBtTagModel.setTagType(2);
            cmsBtTagModel.setTagStatus(0);
            cmsBtTagModel.setTagPathName(String.format("-%s-%s-", cmsBtPromotionModel.getPromotionName(), cmsBtTagModel.getTagName()));
            cmsBtTagModel.setTagPath("");
            cmsBtTagModel.setCreater(cmsBtPromotionModel.getCreater());
            cmsBtTagModel.setModifier(cmsBtPromotionModel.getCreater());
            cmsBtTagDao.insertCmsBtTag(cmsBtTagModel);
            cmsBtTagModel.setTagPath(String.format("-%s-%s-", res.getTag().getTagId(), cmsBtTagModel.getTagId()));
            cmsBtTagDao.updateCmsBtTag(cmsBtTagModel);
        });
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
        promotionGetRequest.check();
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
//        models.forEach(cmsBtPromotionModel -> {
//            TagsGetRequest request = new TagsGetRequest();
//            request.setParentTagId(cmsBtPromotionModel.getRefTagId());
//            cmsBtPromotionModel.setTagList(tagService.selectList(request).getTags());
//        });
        response.setCmsBtPromotionModels(models);
        response.setTotalCount((long) models.size());
        return response;
    }

    /**
     * 删除
     *
     * @param promotionDeleteRequest Request
     * @return PromotionsPutResponse
     */
    @Transactional
    public PromotionsPutResponse deleteById(
            PromotionDeleteRequest promotionDeleteRequest) {
        promotionDeleteRequest.check();
        PromotionsPutResponse response = new PromotionsPutResponse();
        response.setRemovedCount(cmsBtPromotionDao
                .deleteById(convertCondtionMap(promotionDeleteRequest)));
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
