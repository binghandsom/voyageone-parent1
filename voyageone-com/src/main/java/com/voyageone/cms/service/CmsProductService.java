package com.voyageone.cms.service;

import com.mongodb.BulkWriteResult;
import com.mongodb.CommandResult;
import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.cms.CmsConstants;
import com.voyageone.cms.service.dao.mongodb.CmsBtProductDao;
import com.voyageone.cms.service.model.*;
import com.voyageone.common.util.StringUtils;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CmsProductService {
    @Autowired
    private CmsBtProductDao cmsBtProductDao;

    /**
     * 获取商品 根据ID获
     */
    public CmsBtProductModel getProductById(String channelId, long prodId) {
        return cmsBtProductDao.selectProductById(channelId, prodId);
    }

    /**
     * 获取商品 根据ID获
     */
    public JSONObject getProductByIdWithJson(String channelId, long prodId) {
        return cmsBtProductDao.selectProductByIdWithJson(channelId, prodId);
    }

    /**
     * 获取商品 根据Code
     */
    public CmsBtProductModel getProductByCode(String channelId, String code) {
        return cmsBtProductDao.selectProductByCode(channelId, code);
    }

    /**
     * 获取商品 根据query
     */
    public CmsBtProductModel getProductWithQuery(String channelId, String query) {
        if (StringUtils.isEmpty(query)) {
            return cmsBtProductDao.selectOne();
        }
        return cmsBtProductDao.selectOneWithQuery(query, channelId);
    }

    /**
     * 获取商品List 根据GroupId
     */
    public List<CmsBtProductModel> getProductByGroupId(String channelId, long groupId) {
        return cmsBtProductDao.selectProductByGroupId(channelId, groupId);
    }

    /**
     * 插入商品
     */
    public WriteResult insert(CmsBtProductModel model) {
        return cmsBtProductDao.insert(model);
    }

    /**
     * 插入商品 依据FeedInfo
     */
    public WriteResult insertByFeed(CmsBtFeedInfoModel feedInfoModel) {
        //TODO 需要增加实现
        return  null;
    }

    /**
     * 插入商品
     */
    public WriteResult insert(Collection<CmsBtProductModel> models) {
        return cmsBtProductDao.insertWithList(models);
    }

    /**
     * 插入商品 依据FeedInfo List
     */
    public WriteResult insertByFeed(Collection<CmsBtFeedInfoModel> models) {
        //TODO 需要增加实现
        return  null;
    }

    /**
     * 更新商品
     */
    public WriteResult update(CmsBtProductModel model) {
        return cmsBtProductDao.update(model);
    }

    /**
     * 删除所有商品
     */
    public CommandResult removeAll(String channelId) {
        return cmsBtProductDao.deleteAll(channelId);
    }

    /**
     *更新Platform
     */
    public WriteResult updateWithPlatform(String channelId, String code, CmsBtProductModel_Group_Platform platformMode) {
        return cmsBtProductDao.updateWithPlatform(channelId, code, platformMode);
    }

    /**
     * 更新SKU
     */
    public WriteResult updateWithSku(String channelId, String code, CmsBtProductModel_Sku skuModel) {
        return cmsBtProductDao.updateWithSku(channelId, code, skuModel);
    }

    /**
     * 批量更新Fields 根据ProdIdList，更新为相同的值
     */
    public BulkWriteResult bulkUpdateFieldsByProdIds(String channelId, List<Long> prodIdList, CmsBtProductModel_Field field, String modifier) {
        return cmsBtProductDao.bulkUpdateFieldsByProdIds(channelId, prodIdList, field, modifier);
    }

    /**
     * 批量更新Fields 根据CodeList，更新为相同的值
     */
    public BulkWriteResult bulkUpdateFieldsByCodes(String channelId, List<String> codeList, CmsBtProductModel_Field field, String modifier) {
        return cmsBtProductDao.bulkUpdateFieldsByCodes(channelId, codeList, field, modifier);
    }

    /**
     * 批量更新Fields 根据CodeList
     */
    public BulkWriteResult bulkUpdateFieldsByProdIds(String channelId, Map<Long, CmsBtProductModel_Field> prodIdFieldMap, String modifier) {
        return cmsBtProductDao.bulkUpdateFieldsByProdIds(channelId, prodIdFieldMap, modifier);
    }

    /**
     * 批量更新Fields 根据CodeList
     */
    public BulkWriteResult bulkUpdateFieldsByCodes(String channelId, Map<String, CmsBtProductModel_Field> codeFieldMap, String modifier) {
        return cmsBtProductDao.bulkUpdateFieldsByCodes(channelId, codeFieldMap, modifier);
    }

    /**
     * 批量更新上新结果 根据CodeList
     */
    public BulkWriteResult bathUpdateWithSXResult(String channelId, int cartId,
                                                  long groupId, List<String> codeList,
                                                  String numIId, String productId,
                                                  String publishTime, String onSalesTime, String instockTime,
                                                  CmsConstants.PlatformStatus status) {

        List<BulkUpdateModel> bulkList = new ArrayList<>();
        for (String code : codeList) {
            HashMap<String, Object> queryMap = new HashMap<>();
            queryMap.put("fields.code", code);
            queryMap.put("groups.platforms.cartId", cartId);

            HashMap<String, Object> updateMap = new HashMap<>();
            if (numIId != null) {
                updateMap.put("groups.platforms.$.numIId", numIId);
            }
            if (productId != null) {
                updateMap.put("groups.platforms.$.productId", productId);
            }
            if (publishTime != null) {
                updateMap.put("groups.platforms.$.publishTime", publishTime);
            }
            if (onSalesTime != null) {
                updateMap.put("groups.platforms.$.onSalesTime", onSalesTime);
            }
            if (instockTime != null) {
                updateMap.put("groups.platforms.$.instockTime", instockTime);
            }
            if (status != null) {
                updateMap.put("groups.platforms.$.platformStatus", status.toString());
            }

            BulkUpdateModel model = new BulkUpdateModel();
            model.setUpdateMap(updateMap);
            model.setQueryMap(queryMap);
            bulkList.add(model);
        }
        return cmsBtProductDao.bulkUpdateWithMap(channelId, bulkList, null, "$set");
    }
}
