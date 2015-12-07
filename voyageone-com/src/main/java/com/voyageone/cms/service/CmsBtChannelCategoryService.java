package com.voyageone.cms.service;

import com.voyageone.cms.service.dao.CmsBtChannelCategoryDao;
import com.voyageone.cms.service.dao.mongodb.CmsMtCategoryTreeDao;
import com.voyageone.cms.service.model.CmsBtChannelCategoryModel;
import com.voyageone.cms.service.model.CmsMtCategoryTreeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CmsBtChannelCategoryService {

    @Autowired
    private CmsBtChannelCategoryDao cmsBtChannelCategoryDao;

    @Autowired
    private CmsMtCategoryTreeDao cmsMtCategoryTreeDao;

    /**
     * 取得Category Tree 根据channelId
     * @param channelId
     * @return
     */
    public List<CmsMtCategoryTreeModel> getCategorysByChannelId(String channelId) {
        List<CmsMtCategoryTreeModel> result = new ArrayList<>();

        List<CmsBtChannelCategoryModel> mappings = getByChannelId(channelId);
        for (CmsBtChannelCategoryModel mapping : mappings) {
            String catId = mapping.getCatId();
            CmsMtCategoryTreeModel model = cmsMtCategoryTreeDao.selectByCatId(catId);
            if (model != null) {
                result.add(model);
            }
        }
        return result;
    }

    /**
     * 取得Mapping定义 根据channelId
     * @param channelId
     * @return
     */
    public List<CmsBtChannelCategoryModel> getByChannelId(String channelId) {
        return cmsBtChannelCategoryDao.selectbyChannelId(channelId);
    }

    /**
     * 保存Mapping定义
     * @param model
     */
    public void save(CmsBtChannelCategoryModel model) {
        cmsBtChannelCategoryDao.insert(model);
    }

    /**
     * 保存Mapping List定义
     * @param models
     */
    public void saveWithList(List<CmsBtChannelCategoryModel> models) {
        cmsBtChannelCategoryDao.insertWithList(models);
    }
}
