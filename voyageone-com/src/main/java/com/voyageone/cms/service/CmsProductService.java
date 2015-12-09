package com.voyageone.cms.service;

import com.mongodb.CommandResult;
import com.mongodb.WriteResult;
import com.voyageone.cms.service.dao.mongodb.CmsBtProductDao;
import com.voyageone.cms.service.model.CmsBtFeedInfoModel;
import com.voyageone.cms.service.model.CmsBtProductModel;
import com.voyageone.cms.service.model.CmsBtProductModel_Group_Platform;
import com.voyageone.cms.service.model.CmsBtProductModel_Sku;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class CmsProductService {
    @Autowired
    private CmsBtProductDao cmsBtProductDao;

    /**
     * 获取商品 根据ID获
     */
    public CmsBtProductModel getProductById(String channelId, int prodId) {
        return cmsBtProductDao.selectProductById(channelId, prodId);
    }

    /**
     * 获取商品 根据ID获
     */
    public JSONObject getProductByIdWithJson(String channelId, int prodId) {
        return cmsBtProductDao.selectProductByIdWithJson(channelId, prodId);
    }

    /**
     * 获取商品 根据Code
     */
    public CmsBtProductModel getProductByCode(String channelId, String code) {
        return cmsBtProductDao.selectProductByCode(channelId, code);
    }

    /**
     * 获取商品List 根据GroupId
     */
    public List<CmsBtProductModel> getProductByGroupId(String channelId, int groupId) {
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
}
