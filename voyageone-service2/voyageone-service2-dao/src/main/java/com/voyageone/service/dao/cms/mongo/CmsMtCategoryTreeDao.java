package com.voyageone.service.dao.cms.mongo;

import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.BaseMongoDao;
import com.voyageone.service.model.cms.mongo.CmsMtCategoryTreeModel;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class CmsMtCategoryTreeDao extends BaseMongoDao<CmsMtCategoryTreeModel> {

    public static Map<String, CmsMtCategoryTreeModel> cache = new HashMap<>();

    private CmsMtCategoryTreeModel getModelFormCache(String key) {
        CmsMtCategoryTreeModel result = null;
        if (cache.containsKey(key)) {
            result = cache.get(key);
        }
        return result;
    }

    private void setCache(String key, CmsMtCategoryTreeModel cacheObj) {
        if (cacheObj == null) {
            cache.remove(key);
        } else {
            cache.put(key, cacheObj);
        }
    }

    /**
     * 取得 根据CatId
     */
    public CmsMtCategoryTreeModel selectByCatId(String catId) {
        CmsMtCategoryTreeModel result = getModelFormCache(catId);
        if (result == null) {
            String queryStr = "{\"catId\":\"" + catId + "\"}";
            result = selectOneWithQuery(queryStr);
        }
        return result;
    }

    /**
     * 插入
     */
    public WriteResult insert(CmsMtCategoryTreeModel model) {
        WriteResult reslt = super.insert(model);
        if (reslt.getN() > 0) {
            setCache(model.getCatId(), model);
        }
        return reslt;
    }

    /**
     * 删除
     */
    public WriteResult delete(CmsMtCategoryTreeModel model) {
        WriteResult reslt = super.delete(model);
        if (reslt.getN() > 0) {
            setCache(model.getCatId(), null);
        }
        return reslt;
    }

    public WriteResult update(CmsMtCategoryTreeModel model) {
        WriteResult reslt = super.update(model);
        if (reslt.getN() > 0) {
            setCache(model.getCatId(), model);
        }
        return reslt;
    }

}
