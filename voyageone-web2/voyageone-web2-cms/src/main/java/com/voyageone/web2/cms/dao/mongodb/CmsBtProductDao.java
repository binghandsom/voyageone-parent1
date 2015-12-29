package com.voyageone.web2.cms.dao.mongodb;

import com.mongodb.*;
import com.voyageone.base.dao.mongodb.BaseMongoDao;
import com.voyageone.base.dao.mongodb.BaseMongoPartDao;
import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.cms.service.model.CmsBtProductModel;
import com.voyageone.cms.service.model.CmsBtProductModel_Field;
import com.voyageone.cms.service.model.CmsBtProductModel_Group_Platform;
import com.voyageone.cms.service.model.CmsBtProductModel_Sku;
import com.voyageone.common.util.DateTimeUtil;
import net.minidev.json.JSONObject;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Repository("web2.cms.CmsBtProductDao")
public class CmsBtProductDao extends BaseMongoPartDao {

    @Override
    public Class getEntityClass() {
        return CmsBtProductModel.class;
    }

    /**
     * 获取商品List 根据GroupId
     */
    public List<CmsBtProductModel> selectProductByGroupId(String channelId, long groupId) {
        String query = "{\"groups.platforms.groupId\":" + groupId + "}";
        return select(query, channelId);
    }

    /**
     * 获取商品Code List 根据CartId（含SKU）
     * @param channelId
     * @param cartId
     * @param pageNo 当前页
     * @param pageSize 页面大小
     *
     */
    public List<CmsBtProductModel> selectProductByCartId(String channelId, String cartId, int pageNo, int pageSize) {
        JomgoQuery where = new JomgoQuery();
        where.setQuery("{\"groups.platforms.cartId\":" + cartId + "}");
        where.setSkip(pageNo * pageSize);
        where.setLimit(pageSize);
        where.setProjection("{\"prodId\":1,\"groups.platforms.cartId.$\":1,\"fields.model\":1,\"fields.code\":1,\"fields.productName\":1,\"batch_update.code_qty\":1,\"fields.salePriceStart\":1,\"fields.salePriceEnd\":1,\"catPath\":1,\"skus\":1}");

        return select(where, channelId);
    }

    /**
     * 获取商品Code 数量 根据CartId（含SKU）
     * @param channelId
     * @param cartId
     *
     */
    public long selectProductByCartIdRecCount(String channelId, String cartId) {
        String query = "{\"groups.platforms.cartId\":" + cartId + "}";

        return countByQuery(query, channelId);
    }

    /**
     * 获取商品Model List 根据CartId
     * @param channelId
     * @param cartId
     * @param pageNo 当前页
     * @param pageSize 页面大小
     *
     */
    public List<CmsBtProductModel> selectModelByCartId(String channelId, String cartId, int pageNo, int pageSize) {
        JomgoQuery where = new JomgoQuery();
//        where.setQuery("{\"groups.platforms.cartId\":" + cartId + ",\"groups.platforms.isMain\":1}");
        where.setQuery("{\"groups.platforms.cartId\":" + cartId + ",\"groups.platforms.isMain\":0}");
        where.setSkip(pageNo * pageSize);
        where.setLimit(pageSize);
        where.setProjection("{\"groups.platforms.cartId.$\":1,\"groups.salePriceStart\":1,\"groups.salePriceEnd\":1,\"prodId\":1,\"fields.model\":1}");

        return select(where, channelId);
    }

    /**
     * 获取商品Model 数量 根据CartId（含SKU）
     * @param channelId
     * @param cartId
     *
     */
    public long selectModelByCartIdRecCount(String channelId, String cartId) {
//        String query = "{\"groups.platforms.cartId\":" + cartId + ",\"groups.platforms.isMain\":1}";
        String query = "{\"groups.platforms.cartId\":" + cartId + ",\"groups.platforms.isMain\":0}";

        return countByQuery(query, channelId);
    }
}
