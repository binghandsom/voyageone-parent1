package com.voyageone.cms.service;

import com.voyageone.cms.service.dao.mongodb.CmsBtProductDao;
import com.voyageone.cms.service.model.CmsBtProductModel;
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
    public void insert(CmsBtProductModel model) {
        cmsBtProductDao.insert(model);
    }

    /**
     * 插入商品
     * @param models
     */
    public void insert(Collection<CmsBtProductModel> models) {
        cmsBtProductDao.insertWithList(models);
    }

    /**
     * 更新商品
     * @param model
     */
    public void update(CmsBtProductModel model) {
        cmsBtProductDao.update(model);
    }

}
