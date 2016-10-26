package com.voyageone.service.impl.cms.promotion;

import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.service.bean.cms.CmsBtPromotionGroupsBean;
import com.voyageone.service.dao.cms.CmsBtPromotionGroupsDao;
import com.voyageone.service.daoext.cms.CmsBtPromotionGroupsDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsBtPromotionGroupsModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author aooer 2016/2/18.
 * @version 2.7.1
 * @since 2.0.0
 */
@Service
public class PromotionModelService extends BaseService {
    private final CmsBtPromotionGroupsDao cmsBtPromotionGroupsDao;
    private final CmsBtPromotionGroupsDaoExt cmsPromotionModelDao;

    @Autowired
    public PromotionModelService(CmsBtPromotionGroupsDaoExt cmsPromotionModelDao, CmsBtPromotionGroupsDao cmsBtPromotionGroupsDao) {
        this.cmsPromotionModelDao = cmsPromotionModelDao;
        this.cmsBtPromotionGroupsDao = cmsBtPromotionGroupsDao;
    }

    public List<Map<String,Object>> getPromotionModelDetailList(Map<String,Object> param) {
        return cmsPromotionModelDao.selectPromotionModelDetailList(param);
    }

    public int getPromotionModelDetailListCnt(Map<String,Object> param){
        return cmsPromotionModelDao.selectPromotionModelDetailListCnt(param);
    }

    @VOTransactional
    public int deleteCmsPromotionModel(CmsBtPromotionGroupsBean model) {
        return cmsPromotionModelDao.deleteCmsPromotionModel(model);
    }

    /**
     * @since 2.7.1
     */
    public List<CmsBtPromotionGroupsModel> getPromotionModelDetailList(Integer promotionId, String channelId) {
        CmsBtPromotionGroupsModel parameter = new CmsBtPromotionGroupsModel();
        parameter.setPromotionId(promotionId);
        parameter.setOrgChannelId(channelId);
        return cmsBtPromotionGroupsDao.selectList(parameter);
    }
}
