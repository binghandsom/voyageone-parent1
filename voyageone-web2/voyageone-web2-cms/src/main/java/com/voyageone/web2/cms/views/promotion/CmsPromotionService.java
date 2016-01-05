package com.voyageone.web2.cms.views.promotion;

import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.cms.dao.CmsBtTagDao;
import com.voyageone.web2.cms.dao.CmsPromotionDao;
import com.voyageone.web2.cms.model.CmsBtPromotionModel;
//import com.voyageone.web2.cms.model.CmsBtTagModel;
import com.voyageone.web2.sdk.api.VoApiDefaultClient;
import com.voyageone.web2.sdk.api.domain.CmsBtTagModel;
import com.voyageone.web2.sdk.api.request.TagAddRequest;
import com.voyageone.web2.sdk.api.response.TagAddResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author james
 * @version 2.0.0, 15/12/11
 */
@Service
public class CmsPromotionService extends BaseAppService {

    @Autowired
    private CmsPromotionDao cmsPromotionDao;

//    @Autowired
//    private CmsBtTagDao cmsBtTagDao;

    @Autowired
    VoApiDefaultClient voApiClient;

    public List<CmsBtPromotionModel> getPromotionList(Map<String, Object> params) {
        return cmsPromotionDao.getPromotionList(params);
    }

    @Transactional
    public int insertPromotion(CmsBtPromotionModel params) {
//        // 更新tag表
//        CmsBtTagModel cmsBtTagModel = new CmsBtTagModel();
//        cmsBtTagModel.setCreater(params.getCreater());
//        cmsBtTagModel.setTagName(params.getPromotionName());
//        cmsBtTagModel.setTagPathName(params.getPromotionName());
//        cmsBtTagModel.setTagType(2);
//        cmsBtTagModel.setTagStatus(0);
//        cmsBtTagModel.setParentTagId(0);
//        cmsBtTagModel.setTagPath("");
//        cmsBtTagModel.setChannelId(params.getChannelId());
//        cmsBtTagDao.insertCmsBtTag(cmsBtTagModel);
//        cmsBtTagModel.setTagPath("-" + cmsBtTagModel.getTagId() + "-");
//        cmsBtTagDao.updateCmsBtTag(cmsBtTagModel);
        // Tag 新追加
        TagAddRequest requestModel = new TagAddRequest(params.getChannelId());
        requestModel.setTagName(params.getPromotionName());
        requestModel.setTagType(2);
        requestModel.setTagStatus(0);
        requestModel.setParentTagId(0);
        requestModel.setSortOrder(0);
        requestModel.setCreater(params.getCreater());
        //SDK取得Product 数据
        TagAddResponse res = voApiClient.execute(requestModel);
        CmsBtTagModel cmsBtTagModel = res.getTag();

        // 把TAGID会写到Promotion表中
        params.setRefTagId(cmsBtTagModel.getTagId());

        // 插入Promotion
        return cmsPromotionDao.insertPromotion(params);
    }

    public int updatePromotion(CmsBtPromotionModel params) {
        return cmsPromotionDao.updatePromotion(params);
    }
}
