package com.voyageone.service.dao.cms.mongo;

import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.BaseMongoDao;
import com.voyageone.service.model.cms.mongo.CmsBtSellerCatModel;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CmsBtSellerCatDao extends BaseMongoDao<CmsBtSellerCatModel> {

    public static Map<String, CmsBtSellerCatModel> cache = new HashMap<>();

    private CmsBtSellerCatModel getModelFormCache(String key) {
        CmsBtSellerCatModel result = null;
        if (cache.containsKey(key)) {
            result = cache.get(key);
        }
        return result;
    }

    private void setCache(String key, CmsBtSellerCatModel cacheObj) {
        if (cacheObj == null) {
            cache.remove(key);
        } else {
            cache.put(key, cacheObj);
        }
    }

    /**
     * 取得 根据CatId
     */
    public CmsBtSellerCatModel selectByCatId(String catId) {
        CmsBtSellerCatModel result = getModelFormCache(catId);
        if (result == null) {
            String queryStr = "{\"catId\":\"" + catId + "\"}";
            result = selectOneWithQuery(queryStr);
        }
        return result;
    }

    /**
     * 取得 根据ChannelId, CartId
     */
    public List<CmsBtSellerCatModel> selectByChannelCart(String channelId, int cartId) {

        String queryStr = "{\"channelId\":\"" + channelId + "\"" + ",\"cartId\"" + ":" + cartId + "}";

        List<CmsBtSellerCatModel>  result = select(queryStr);

        return result;
    }


    /**
     * 插入
     */
    public WriteResult insert(CmsBtSellerCatModel model) {
        WriteResult reslt = super.insert(model);
        if (reslt.getN() > 0) {
            setCache(model.getCatId(), model);
        }
        return reslt;
    }

    /**
     * 删除
     */
    public WriteResult delete(CmsBtSellerCatModel model) {
        WriteResult reslt = super.delete(model);
        if (reslt.getN() > 0) {
            setCache(model.getCatId(), null);
        }
        return reslt;
    }

    public WriteResult update(CmsBtSellerCatModel model) {
        WriteResult reslt = super.update(model);
        if (reslt.getN() > 0) {
            setCache(model.getCatId(), model);
        }
        return reslt;
    }

}
