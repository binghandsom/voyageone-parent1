package com.voyageone.service.impl.cms.jumei2;

import com.voyageone.service.dao.cms.CmsBtJmPromotionDao;
import com.voyageone.service.dao.cms.CmsBtTagDao;
import com.voyageone.service.model.cms.CmsBtJmPromotionModel;
import com.voyageone.service.model.cms.CmsBtTagModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2016/3/18.
 */
@Service
public class CmsBtJmPromotion3Service {

    @Autowired
    CmsBtJmPromotionDao dao;
    @Autowired
    CmsBtTagDao daoCmsBtTag;

    public List<CmsBtTagModel> getTagListByPromotionId(int promotionId) {
        CmsBtJmPromotionModel model = dao.select(promotionId);
        if (model != null && model.getRefTagId() != 0) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("parentTagId", model.getRefTagId());
            map.put("active", 1);
            List<CmsBtTagModel> tagList = daoCmsBtTag.selectList(map);
            return tagList;
        }
        return new ArrayList<>();
    }

    public CmsBtJmPromotionModel get(int id) {
        return dao.select(id);
    }

    /**
     * 更新cms_bt_jm_promotion_model表
     * @param model
     */
    public void update(CmsBtJmPromotionModel model) {
        dao.update(model);
    }
}

