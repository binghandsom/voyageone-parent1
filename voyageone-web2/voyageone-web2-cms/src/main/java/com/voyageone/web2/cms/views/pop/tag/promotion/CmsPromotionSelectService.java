package com.voyageone.web2.cms.views.pop.tag.promotion;

import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.cms.service.dao.mongodb.CmsBtProductDao;
import com.voyageone.common.util.CommonUtil;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.cms.dao.CmsBtTagDao;
import com.voyageone.web2.cms.dao.CmsBtTagLogDao;
import com.voyageone.web2.cms.model.CmsBtTagLogModel;
import com.voyageone.web2.cms.model.CmsBtTagModel;
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
        int tag_id = (int) params.get("refTagId");
        return this.selectListByParentTagId(tag_id);
    }

    public Map<String, Object> addToPromotion(Map<String, Object> params, String channelId, String modifier) {
        String tag_path = params.get("tagPath").toString();

        return this.add((ArrayList<Long>)params.get("productIds"), channelId, tag_path, modifier);
    }

    /**
     * 获取二级Tag
     */
    public List<CmsBtTagModel> selectListByParentTagId(int parentTagId) {
        return cmsBtTagDao.selectListByParentTagId(parentTagId);
    }

    /**
     * 增加商品的Tag
     */
    public Map<String, Object> add(List<Long> prodIds, String channelId, String tagPath, String modifier) {
        Map<String, Object> ret = new HashMap<>();
        if (prodIds != null && prodIds.size() <= 500) {
            List<BulkUpdateModel> bulkList = new ArrayList<>();

            List<CmsBtTagLogModel> cmsBtTagLogModelList = new ArrayList<>();

            for (Long prodId : prodIds) {
                HashMap<String, Object> updateMap = new HashMap<>();
                updateMap.put("tags", tagPath);
                HashMap<String, Object> queryMap = new HashMap<>();
                queryMap.put("prodId", prodId);
                BulkUpdateModel model = new BulkUpdateModel();
                model.setUpdateMap(updateMap);
                model.setQueryMap(queryMap);
                bulkList.add(model);

                CmsBtTagLogModel cmsBtTagLogModel = new CmsBtTagLogModel();
                cmsBtTagLogModel.setTagId((int) tagPath.charAt(tagPath.length() - 2));
                cmsBtTagLogModel.setProductId(prodId);
                cmsBtTagLogModel.setComment("insert");
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

    /**
     * 删除商品的Tag
     */
    public Map<String, Object> remove(List<Long> prodIds, String channelId, String tagPath, String modifier) {
        Map<String, Object> ret = new HashMap<>();
        if (prodIds != null && prodIds.size() <= 500) {
            List<BulkUpdateModel> bulkList = new ArrayList<>();

            List<CmsBtTagLogModel> cmsBtTagLogModelList = new ArrayList<>();

            for (Long prodId : prodIds) {
                HashMap<String, Object> updateMap = new HashMap<>();
                updateMap.put("tags", tagPath);
                HashMap<String, Object> queryMap = new HashMap<>();
                queryMap.put("prodId", prodId);
                BulkUpdateModel model = new BulkUpdateModel();
                model.setUpdateMap(updateMap);
                model.setQueryMap(queryMap);
                bulkList.add(model);

                CmsBtTagLogModel cmsBtTagLogModel = new CmsBtTagLogModel();
                cmsBtTagLogModel.setTagId((int) tagPath.charAt(tagPath.length() - 2));
                cmsBtTagLogModel.setProductId(prodId);
                cmsBtTagLogModel.setComment("delete");
                cmsBtTagLogModel.setCreater(modifier);
                cmsBtTagLogModelList.add(cmsBtTagLogModel);
            }
            cmsBtProductDao.bulkUpdateWithMap(channelId, bulkList, modifier, "$pull");
            cmsBtTagLogDao.insertCmsBtTagLogList(cmsBtTagLogModelList);
            ret.put("result", "success");
        } else {
            ret.put("result", "failed");
            ret.put("errMsg", "商品数量过多");
        }
        return ret;
    }
}
