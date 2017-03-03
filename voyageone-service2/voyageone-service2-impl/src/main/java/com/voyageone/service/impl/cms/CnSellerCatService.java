package com.voyageone.service.impl.cms;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.components.cnn.service.CnnCatalogService;
import com.voyageone.service.model.cms.mongo.CmsBtSellerCatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2016/9/23.
 */
@Service
public class CnSellerCatService {
    @Autowired
    MongoSequenceService commSequenceMongoService;

    @Autowired
    private CnnCatalogService cnnCatalogService;

    public Map<String, String> addSellerCat(String channelId, String parentCId, String catName, String urlKey, ShopBean shopBean, int index) {
        String catId = Long.toString(commSequenceMongoService.getNextSequence(MongoSequenceService.CommSequenceName.CMS_BT_CnShopCategory_ID));
        try {
            cnnCatalogService.addCatalog(shopBean, catName,catId,parentCId);
        }
        catch (Exception e) {
            throw new BusinessException("创建类目失败， 请再尝试一下。" + e.getMessage());
        }

        Map<String, String> resultMap = new HashMap<String, String>();
        resultMap.put("catId", catId);
        resultMap.put("urlKey", "");
        return resultMap;
    }

    public void  updateSellerCat(CmsBtSellerCatModel currentNode, ShopBean shopBean, int index)
    {
        try {
            cnnCatalogService.updateCatalog(shopBean, currentNode.getCatId(),currentNode.getCatName());
        } catch (Exception e) {
            throw new BusinessException("更新类目失败， 请再尝试一下。" + e.getMessage());
        }
    }

    public void  deleteSellerCat(CmsBtSellerCatModel currentNode, ShopBean shopBean)
    {
        try {
            cnnCatalogService.deleteCatalog(shopBean, currentNode.getCatId());
        }catch (Exception e) {
            throw new BusinessException("删除类目失败， 请再尝试一下。" + e.getMessage());
        }
    }

    public void resetAllCatalog(List<CmsBtSellerCatModel> cmsBtSellerCatModels, ShopBean shopBean){
        try {
            cnnCatalogService.resetAllCatalog(shopBean,cmsBtSellerCatModels);
        } catch (Exception e) {
            throw new BusinessException("重置所有店铺内分类失败， 请再尝试一下。" + e.getMessage());
        }
    }
}
