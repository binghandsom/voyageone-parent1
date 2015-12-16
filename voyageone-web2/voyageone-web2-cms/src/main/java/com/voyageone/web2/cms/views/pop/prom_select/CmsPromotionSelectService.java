package com.voyageone.web2.cms.views.pop.prom_select;

import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.cms.service.dao.mongodb.CmsBtProductDao;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.cms.dao.CmsBtTagDao;
import com.voyageone.web2.cms.dao.CmsBtTagLogDao;
import com.voyageone.web2.cms.model.CmsBtTagLogModel;
import com.voyageone.web2.cms.model.CmsBtTagModel;
import net.minidev.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gubuchun 15/12/14
 * @version 2.0.0
 */
@Service
public class CmsPromotionSelectService extends BaseAppService {

    @Autowired
    private CmsBtTagDao cmsBtTagDao;

    @Autowired
    private CmsBtProductDao cmsBtProductDao;

    @Autowired
    private CmsBtTagLogDao cmsBtTagLogDao;

    public List<CmsBtTagModel> getPromotionTags(Map<String, Object> params) {
        int promotion_id = (int) params.get("promotionId");
        return this.selectListById(promotion_id);
    }

    public void addToPromotion(Map<String, Object> params, String channelId, String modifier) {
        Object[] productIds = ((JSONArray) params.get("productIds")).toArray();
        int tag_id = (int) params.get("tagId");

        List<String> idList = new ArrayList<>();

        for (Object id : productIds) {
            idList.add(id.toString());
        }
        this.add(idList, channelId, tag_id, modifier);
    }

    /**
     * 获取二级Tag
     */
    public List<CmsBtTagModel> selectListById(int promotionId) {
        return cmsBtTagDao.selectListById(promotionId);
    }

    /**
     * 增加商品的Tag
     */
    public Map<String, Object> add(List<String> prodIds, String channelId, int tagId, String modifier) {
        Map<String, Object> ret = new HashMap<>();
        if (prodIds != null && prodIds.size() <= 500) {
            List<BulkUpdateModel> bulkList = new ArrayList<>();

            List<CmsBtTagLogModel> cmsBtTagLogModelList = new ArrayList<>();

            for (String prodId : prodIds) {
                HashMap<String, Object> updateMap = new HashMap<>();
                updateMap.put("tags", tagId);
                HashMap<String, Object> queryMap = new HashMap<>();
                queryMap.put("prodId", prodId);
                BulkUpdateModel model = new BulkUpdateModel();
                model.setUpdateMap(updateMap);
                model.setQueryMap(queryMap);
                bulkList.add(model);

                CmsBtTagLogModel cmsBtTagLogModel = new CmsBtTagLogModel();
                cmsBtTagLogModel.setTagId(tagId);
                cmsBtTagLogModel.setProductId(prodId);
                cmsBtTagLogModel.setCreater(modifier);
                cmsBtTagLogModelList.add(cmsBtTagLogModel);
            }
            cmsBtProductDao.bulkUpdateWithMap(channelId, bulkList, modifier, "$addToSet");
            cmsBtTagLogDao.insertCmsBtTagLogList(cmsBtTagLogModelList);
            ret.put("result", "success");
        } else {
            ret.put("result", "failed");
            ret.put("errMsg", "商品数量过多");
        }
        return ret;
    }
}
