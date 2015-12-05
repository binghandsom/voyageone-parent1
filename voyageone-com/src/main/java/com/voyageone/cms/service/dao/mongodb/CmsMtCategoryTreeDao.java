package com.voyageone.cms.service.dao.mongodb;

import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.BaseMongoDao;
import com.voyageone.cms.service.model.CmsMtCategoryTreeModel;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class CmsMtCategoryTreeDao extends BaseMongoDao {

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

    @Override
    public Class getEntityClass() {
        return CmsMtCategoryTreeModel.class;
    }

    /**
     * select by CatId
     * @param catId
     * @return
     */
    public CmsMtCategoryTreeModel selectByCatId(String catId) {
        CmsMtCategoryTreeModel result = getModelFormCache(catId);
        if (result == null) {
            String queryStr = "{\"catId\":\"" + catId + "\"}";
            result = selectOneWithQuery(queryStr);
        }
        return result;
    }

    public WriteResult insert(CmsMtCategoryTreeModel model) {
        WriteResult reslt = super.insert(model);
        if (reslt.getLastError().getInt("ok") == 1) {
            setCache(model.getCatId(), model);
        }
        return reslt;
    }

    public WriteResult delete(CmsMtCategoryTreeModel model) {
        WriteResult reslt = super.delete(model);
        if (reslt.getLastError().getInt("ok") == 1) {
            setCache(model.getCatId(), null);
        }
        return reslt;
    }

}
