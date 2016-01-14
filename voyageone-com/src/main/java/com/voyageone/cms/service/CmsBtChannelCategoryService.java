package com.voyageone.cms.service;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.TypeRef;
import com.voyageone.cms.service.dao.CmsBtChannelCategoryDao;
import com.voyageone.cms.service.dao.mongodb.CmsMtCategoryTreeDao;
import com.voyageone.cms.service.model.CmsBtChannelCategoryModel;
import com.voyageone.cms.service.model.CmsMtCategoryTreeModel;
import com.voyageone.common.util.JacksonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
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
     */
    public List<CmsMtCategoryTreeModel> getCategoriesByChannelId(String channelId) {
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
     * 返回子category列表
     * @param channelId channel Id
     * @return List<CmsMtCategoryTreeModel>
     */
    public List<CmsMtCategoryTreeModel> getFinallyCategoriesByChannelId(String channelId) throws IOException{
        List<CmsMtCategoryTreeModel> result = new ArrayList<>();

        List<CmsBtChannelCategoryModel> mappings = getByChannelId(channelId);
        for (CmsBtChannelCategoryModel mapping : mappings) {
            String catId = mapping.getCatId();
            CmsMtCategoryTreeModel category = cmsMtCategoryTreeDao.selectByCatId(catId);

            if (category.getIsParent() == 1) {

                List<CmsMtCategoryTreeModel> childCategory
                        = JsonPath.parse(JacksonUtil.bean2Json(category)).read("$..children[?(@.isParent == 0)]"
                        , new TypeRef<List<CmsMtCategoryTreeModel>>() {});
                result.addAll(childCategory);
            } else {
                result.add(category);
            }
        }

        return result;
    }

    /**
     * 取得Mapping定义 根据channelId
     */
    public List<CmsBtChannelCategoryModel> getByChannelId(String channelId) {
        return cmsBtChannelCategoryDao.selectbyChannelId(channelId);
    }

    /**
     * 保存Mapping定义
     */
    public void save(CmsBtChannelCategoryModel model) {
        cmsBtChannelCategoryDao.insert(model);
    }

    /**
     * 保存Mapping List定义
     */
    public void saveWithList(List<CmsBtChannelCategoryModel> models) {
        cmsBtChannelCategoryDao.insertWithList(models);
    }
}
