package com.voyageone.service.dao.cms.mongo;

import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.BaseMongoDao;
import com.voyageone.service.model.cms.mongo.CmsMtCategoryTreeAllModel;
import com.voyageone.service.model.cms.mongo.CmsMtCategoryTreeAllModel;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class CmsMtCategoryTreeAllDao extends BaseMongoDao<CmsMtCategoryTreeAllModel> {

    public static Map<String, CmsMtCategoryTreeAllModel> cache = new HashMap<>();

    private CmsMtCategoryTreeAllModel getModelFormCache(String key) {
        CmsMtCategoryTreeAllModel result = null;
        if (cache.containsKey(key)) {
            result = cache.get(key);
        }
        return result;
    }

    private void setCache(String key, CmsMtCategoryTreeAllModel cacheObj) {
        if (cacheObj == null) {
            cache.remove(key);
        } else {
            cache.put(key, cacheObj);
        }
    }

    /**
     * 取得 根据CatId
     */
    public CmsMtCategoryTreeAllModel selectByCatId(String catId) {
        CmsMtCategoryTreeAllModel result = getModelFormCache(catId);
        if (result == null) {
            String queryStr = "{\"catId\":\"" + catId + "\"}";
            result = selectOneWithQuery(queryStr);
        }
        return result;
    }

    /**
     * 取得 根据CatId
     */
    public CmsMtCategoryTreeAllModel selectByCatPath(String catPath) {
        String queryStr = "{\"catPath\":\"" + catPath + "\"}";
        return selectOneWithQuery(queryStr);
    }

    /**
     * 插入
     */
    public WriteResult insert(CmsMtCategoryTreeAllModel model) {
        WriteResult reslt = super.insert(model);
        if (reslt.getN() > 0) {
            setCache(model.getCatId(), model);
        }
        return reslt;
    }

    /**
     * 删除
     */
    public WriteResult delete(CmsMtCategoryTreeAllModel model) {
        WriteResult reslt = super.delete(model);
        if (reslt.getN() > 0) {
            setCache(model.getCatId(), null);
        }
        return reslt;
    }

    public WriteResult update(CmsMtCategoryTreeAllModel model) {
        WriteResult reslt = super.update(model);
        if (reslt.getN() > 0) {
            setCache(model.getCatId(), model);
        }
        return reslt;
    }

}
