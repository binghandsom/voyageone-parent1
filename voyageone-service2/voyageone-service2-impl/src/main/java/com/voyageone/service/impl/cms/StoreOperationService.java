package com.voyageone.service.impl.cms;

import com.voyageone.service.dao.cms.CmsBtStoreOperationHistoryDao;
import com.voyageone.service.dao.cms.CmsBtSxWorkloadDao;
import com.voyageone.service.dao.cms.mongo.CmsBtFeedInfoDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductGroupDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsBtStoreOperationHistoryModel;
import com.voyageone.service.model.cms.CmsBtSxWorkloadModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

/**
 * 店面操作
 * @description
 * @author: holysky.zhao
 * @date: 2016/4/26 14:53
 * COPYRIGHT © 2001 - 2016 VOYAGE ONE GROUP INC. ALL RIGHTS RESERVED.
 */
@Service
public class StoreOperationService extends BaseService{

    @Resource
    CmsBtProductDao productDao;

    @Resource
    CmsBtFeedInfoDao feedInfoDao;

    @Resource
    CmsBtProductGroupDao productGroupDao;

    @Resource
    CmsBtSxWorkloadDao workloadDao;

    @Resource
    CmsBtStoreOperationHistoryDao historyDao;


    public long countProductsThatCanUploaded(String channelId) {

        return productDao.countByFieldStatusEqualApproved(channelId);
    }

    /**
     * 1. 从cms_bt_product_cxxx中获取素有的fields.status为approve的商品code
     * 2. 更新商品code在cms_bt_product_groups_cxxxx表中找到所有的groupId
     * 3. 插入cms_bt_sx_workload表
     * @param channelId
     */
    public void rePublish(String channelId, String creater) {
        List<CmsBtProductModel> products = productDao.selectByFieldStatusEqualApproved(channelId);
        List<String> productCodes = products.stream().map(p -> p.getFields().getCode()).collect(toList());
        List<CmsBtProductGroupModel> groupModels = productGroupDao.selectGroupIdsByProductCode(channelId, productCodes);
        List<CmsBtSxWorkloadModel> models = groupModels.stream().map(groupModel -> {
            CmsBtSxWorkloadModel model = new CmsBtSxWorkloadModel();
            model.setChannelId(channelId);
            model.setCreater(creater);
            model.setCartId(model.getCartId());
            model.setGroup_id(model.getGroupId());
            model.setModifier(creater);
            return model;
        }).collect(toList());
        models.forEach(model->{
            if (!workloadDao.hasUnpublishRecord(model)) {//没有的话才进行插入
                workloadDao.insertSxWorkloadModel(model);
            }

        });

    }

     /**
     * 重新发布价格
      * @param channelId
      * @param channelId
      */
    public void rePublishPrice(String channelId, String creater) {
        //查询priceRetail和priceSale不相等的product
        List<CmsBtProductModel> products = productDao.selectByRetailSalePriceNonEqual(channelId);
        List<String> productCodes = products.stream().map(p -> p.getFields().getCode()).collect(toList());
        List<CmsBtProductGroupModel> groupModels = productGroupDao.selectGroupIdsByProductCode(channelId, productCodes);
        //TODO 取得商品调用product和group的价格区间重新计算 和上面的publish有重复需要考虑设计方式

    }


    /**
     * 重新导入所有feed商品 这里仅仅只是更新标志位,其他逻辑由重新导入job进行
     *
     * @param channelId
     * @param cleanCommonProperties
     * @return
     */
    public boolean reUpload(String channelId, boolean cleanCommonProperties) {

        if (cleanCommonProperties) { //清除数据
            productDao.deleteAll(channelId);
            productGroupDao.deleteAll(channelId);
        }
        feedInfoDao.updateAllUpdFlg(channelId,0);
        return true;
    }

    public List<CmsBtStoreOperationHistoryModel> getHistoryBy(Map<String, Object> params) {
        return historyDao.selectList(params);
    }


}
