package com.voyageone.cms.service;

import com.mongodb.WriteResult;
import com.voyageone.cms.service.dao.mongodb.CmsBtProductDao;
import com.voyageone.cms.service.model.CmsBtProductModel;
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
     * @param channelId
     * @param prodId
     * @return
     */
    public CmsBtProductModel getProductById(String channelId, int prodId) {
        return cmsBtProductDao.selectProductById(channelId, prodId);
    }

    /**
     * 获取商品 根据ID获
     * @param channelId
     * @param prodId
     * @return
     */
    public JSONObject getProductByIdWithJson(String channelId, int prodId) {
        return cmsBtProductDao.selectProductByIdWithJson(channelId, prodId);
    }

    /**
     * 获取商品 根据Code
     * @param channelId
     * @param code
     * @return
     */
    public CmsBtProductModel getProductByCode(String channelId, String code) {
        return cmsBtProductDao.selectProductByCode(channelId, code);
    }

    /**
     * 获取商品List 根据GroupId
     * @param channelId
     * @param groupId
     * @return
     */
    public List<CmsBtProductModel> getProductByGroupId(String channelId, int groupId) {
        return cmsBtProductDao.selectProductByGroupId(channelId, groupId);
    }

    /**
     * 插入商品
     * @param model
     */
    public WriteResult insert(CmsBtProductModel model) {
        return cmsBtProductDao.insert(model);
    }

    /**
     * 插入商品
     * @param models
     */
    public WriteResult insert(Collection<CmsBtProductModel> models) {
        return cmsBtProductDao.insertWithList(models);
    }

    /**
     * 更新商品
     * @param model
     */
    public WriteResult update(CmsBtProductModel model) {
        return cmsBtProductDao.update(model);
    }

    /**
     * 删除所有商品
     * @param channelId
     * @return
     */
    public WriteResult removeAll(String channelId) {
        return cmsBtProductDao.deleteAll(channelId);
    }
}
