/**
 * (c) Copyright Voyageone Corp 2016
 */

package com.voyageone.service.impl.cms.promotion;

import com.voyageone.service.bean.cms.CmsTagInfoBean;
import com.voyageone.service.dao.cms.CmsBtPromotionDao;
import com.voyageone.service.dao.cms.CmsBtPromotionLogDao;
import com.voyageone.service.dao.cms.CmsBtTagDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.TagService;
import com.voyageone.service.model.cms.CmsBtPromotionLogModel;
import com.voyageone.service.model.cms.CmsBtPromotionModel;
import com.voyageone.service.model.cms.CmsBtTagModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author aooer 16/01/14
 * @version 2.0.0
 */
@Service
public class PromotionService extends BaseService {

    @Autowired
    private CmsBtPromotionDao cmsBtPromotionDao;

    @Autowired
    private CmsBtTagDao cmsBtTagDao;

    @Autowired
    private TagService tagService;

    @Autowired
    private CmsBtPromotionLogDao cmsBtPromotionLogDao;

    /**
     * 添加或者修改
     */
    @Transactional
    public int saveOrUpdate(CmsBtPromotionModel cmsBtPromotionModel) {
        int result;
        if (cmsBtPromotionModel.getPromotionId() != null) {
            result = cmsBtPromotionDao.update(cmsBtPromotionModel);
            cmsBtPromotionModel.getTagList().forEach(cmsBtTagModel -> {
                if(cmsBtTagDao.updateCmsBtTag(cmsBtTagModel) == 0){
                    cmsBtTagModel.setChannelId(cmsBtPromotionModel.getChannelId());
                    cmsBtTagModel.setParentTagId(cmsBtPromotionModel.getRefTagId());
                    cmsBtTagModel.setTagType(2);
                    cmsBtTagModel.setTagStatus(0);
                    cmsBtTagModel.setTagPathName(String.format("-%s-%s-", cmsBtPromotionModel.getPromotionName(), cmsBtTagModel.getTagName()));
                    cmsBtTagModel.setTagPath("");
                    cmsBtTagModel.setCreater(cmsBtPromotionModel.getModifier());
                    cmsBtTagModel.setModifier(cmsBtPromotionModel.getModifier());
                    cmsBtTagDao.insertCmsBtTag(cmsBtTagModel);
                    cmsBtTagModel.setTagPath(String.format("-%s-%s-", cmsBtTagModel.getParentTagId(), cmsBtTagModel.getTagId()));
                    cmsBtTagDao.updateCmsBtTag(cmsBtTagModel);
                }
            });
        } else {
            result = cmsBtPromotionDao.insert(insertTagsAndGetNewModel(cmsBtPromotionModel));
        }
        return result;
    }

    /**
     * insertTagsAndGetNewModel
     */
    private CmsBtPromotionModel insertTagsAndGetNewModel(CmsBtPromotionModel cmsBtPromotionModel) {
        CmsTagInfoBean requestModel = new CmsTagInfoBean();
        requestModel.setChannelId(cmsBtPromotionModel.getChannelId());
        requestModel.setTagName(cmsBtPromotionModel.getPromotionName());
        requestModel.setTagType(2);
        requestModel.setTagStatus(0);
        requestModel.setParentTagId(0);
        requestModel.setSortOrder(0);
        requestModel.setModifier(cmsBtPromotionModel.getModifier());
        //Tag追加
        int refTagId = tagService.addTag(requestModel);
        cmsBtPromotionModel.setRefTagId(refTagId);

        // 子TAG追加
        cmsBtPromotionModel.getTagList().forEach(cmsBtTagModel -> {
            cmsBtTagModel.setChannelId(cmsBtPromotionModel.getChannelId());
            cmsBtTagModel.setParentTagId(refTagId);
            cmsBtTagModel.setTagType(2);
            cmsBtTagModel.setTagStatus(0);
            cmsBtTagModel.setTagPathName(String.format("-%s-%s-", cmsBtPromotionModel.getPromotionName(), cmsBtTagModel.getTagName()));
            cmsBtTagModel.setTagPath("");
            cmsBtTagModel.setCreater(cmsBtPromotionModel.getCreater());
            cmsBtTagModel.setModifier(cmsBtPromotionModel.getCreater());
            cmsBtTagDao.insertCmsBtTag(cmsBtTagModel);
            cmsBtTagModel.setTagPath(String.format("-%s-%s-", refTagId, cmsBtTagModel.getTagId()));
            cmsBtTagDao.updateCmsBtTag(cmsBtTagModel);
        });
        return cmsBtPromotionModel;
    }

    /**
     * 根据PromotionId查询
     *
     * @param promotionId int
     * @return CmsBtPromotionModel
     */
    public CmsBtPromotionModel getByPromotionId(int promotionId) {
        Map<String, Object> params = new HashMap<>();
        params.put("promotionId", promotionId);
        return cmsBtPromotionDao.selectById(params);
    }

    /**
     * 根据PromotionId查询
     *
     * @param promotionId int
     * @return CmsBtPromotionModel
     */
    public CmsBtPromotionModel getByPromotionIdOrgChannelId(int promotionId, String orgChannelId) {
        Map<String, Object> params = new HashMap<>();
        params.put("promotionId", promotionId);
        params.put("orgChannelId",orgChannelId);
        return cmsBtPromotionDao.selectById(params);
    }

    /**
     * 根据条件查询
     *
     * @param params Map
     * @return List<CmsBtPromotionModel>
     */
    public List<CmsBtPromotionModel> getByCondition(Map<String, Object> params) {
        return cmsBtPromotionDao.selectByCondition(params);
    }

    /**
     * 删除
     */
    @Transactional
    public int delete(CmsBtPromotionModel cmsBtPromotionModel) {
        Map<String, Object> param = new HashMap<>();
        param.put("promotionId", cmsBtPromotionModel.getPromotionId());
        param.put("modifier", cmsBtPromotionModel.getModifier());

        // 删除对应的tag
        CmsBtTagModel cmsBtTagModel = new CmsBtTagModel();
        cmsBtTagModel.setParentTagId(cmsBtPromotionModel.getRefTagId());
        cmsBtTagModel.setTagId(cmsBtPromotionModel.getRefTagId());
        cmsBtTagDao.deleteCmsBtTagByParentTagId(cmsBtTagModel);
        cmsBtTagDao.deleteCmsBtTagByTagId(cmsBtTagModel);

        return cmsBtPromotionDao.deleteById(param);
    }

    public Map<String, Object> getPromotionLogMap(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        List<CmsBtPromotionLogModel> promotionList = cmsBtPromotionLogDao.selectPromotionLog(params);
        int count = cmsBtPromotionLogDao.selectPromotionLogCnt(params);
        result.put("list", promotionList);
        result.put("total", count);
        return result;
    }
}
