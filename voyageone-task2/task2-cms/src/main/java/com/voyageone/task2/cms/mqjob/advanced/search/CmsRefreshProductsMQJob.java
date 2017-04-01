package com.voyageone.task2.cms.mqjob.advanced.search;

import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.components.rabbitmq.annotation.VOSubRabbitListener;
import com.voyageone.service.dao.cms.CmsBtRefreshProductTaskDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.fields.cms.CmsBtRefreshProductTaskModelStatus;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.tools.PlatformMappingService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.CmsRefreshProductsMQMessageBody;
import com.voyageone.service.model.cms.CmsBtRefreshProductTaskItemModel;
import com.voyageone.service.model.cms.CmsBtRefreshProductTaskModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.task2.cms.mqjob.TBaseMQCmsSubService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MQ 任务。用于在用户要求对，某类目的全部或单个属性，进行重新计算值之后，重新赋值并触发上新的任务
 * Created by jonas on 2016/11/2.
 *
 * @version 2.9.0
 * @since 2.9.0
 */
@Service
@VOSubRabbitListener
public class CmsRefreshProductsMQJob extends TBaseMQCmsSubService<CmsRefreshProductsMQMessageBody> {

    private final PlatformMappingService platformMappingService;
    private final ProductService productService;
    private final CmsBtRefreshProductTaskDao cmsBtRefreshProductTaskDao;
    private final CmsBtProductDao cmsBtProductDao;
    private final SxProductService sxProductService;

    @Autowired
    public CmsRefreshProductsMQJob(PlatformMappingService platformMappingService, ProductService productService,
                                   CmsBtRefreshProductTaskDao cmsBtRefreshProductTaskDao,
                                   CmsBtProductDao cmsBtProductDao, SxProductService sxProductService) {
        this.platformMappingService = platformMappingService;
        this.productService = productService;
        this.cmsBtRefreshProductTaskDao = cmsBtRefreshProductTaskDao;
        this.cmsBtProductDao = cmsBtProductDao;
        this.sxProductService = sxProductService;
    }

    @Override
    public void onStartup(CmsRefreshProductsMQMessageBody messageMap) throws Exception {
        // 获取参数
        Integer taskId = messageMap.getTaskId();
        CmsBtRefreshProductTaskModel cmsBtRefreshProductTaskModel = cmsBtRefreshProductTaskDao.select(taskId);

        if (cmsBtRefreshProductTaskModel == null) {
            throw new BusinessException("找不到CmsBtRefreshProductTaskModel, taskId=" + taskId);
        }

        // 取商品
        CmsBtRefreshProductTaskItemModel cmsBtRefreshProductTaskItemModel = platformMappingService.popRefreshProduct(cmsBtRefreshProductTaskModel);

        Integer count = 0;
        while (cmsBtRefreshProductTaskItemModel != null) {

            refreshProduct(cmsBtRefreshProductTaskModel, cmsBtRefreshProductTaskItemModel);

            cmsBtRefreshProductTaskItemModel = platformMappingService.popRefreshProduct(cmsBtRefreshProductTaskModel);
            count ++;
        }
        super.count = count;

        cmsBtRefreshProductTaskModel.setStatus(CmsBtRefreshProductTaskModelStatus.COMPLETED);
        cmsBtRefreshProductTaskModel.setModifier("CmsRefreshProductsJobService");
        cmsBtRefreshProductTaskDao.update(cmsBtRefreshProductTaskModel);
    }

    /**
     *
     * @param cmsBtRefreshProductTaskModel
     * @param cmsBtRefreshProductTaskItemModel
     */
    private void refreshProduct(CmsBtRefreshProductTaskModel cmsBtRefreshProductTaskModel, CmsBtRefreshProductTaskItemModel cmsBtRefreshProductTaskItemModel) {
        String channelId = cmsBtRefreshProductTaskModel.getChannelId();
        CmsBtProductModel product = productService.getProductById(channelId, cmsBtRefreshProductTaskItemModel.getProductId());

        // 计算值
        String fieldId = cmsBtRefreshProductTaskModel.getFieldId();
        Integer cartId = cmsBtRefreshProductTaskModel.getCartId();
        Map<String, Object> valueMap = platformMappingService.getValueMap(channelId, cartId, product, null, fieldId);

        // 更新商品
        new ProductUpdater(product, valueMap, cartId, channelId).update();

        // 标记上新
        List<String> cartIdList = new ArrayList<>();
        cartIdList.add(cartId.toString());
        sxProductService.insertSxWorkLoad(product, cartIdList, cmsBtRefreshProductTaskModel.getModifier());
    }

    public class ProductUpdater {
        private CmsBtProductModel product;
        private Map<String, Object> valueMap;
        private Integer cartId;
        private String channelId;

        public ProductUpdater(CmsBtProductModel product, Map<String, Object> valueMap, Integer cartId, String channelId) {
            this.product = product;
            this.valueMap = valueMap;
            this.cartId = cartId;
            this.channelId = channelId;
        }

        public void update() {
            cmsBtProductDao.bulkUpdateWithModel(channelId, getUpdateModelList());
        }

        private List<BulkUpdateModel> getUpdateModelList() {
            List<BulkUpdateModel> bulkUpdateModelList = new ArrayList<>();
            bulkUpdateModelList.add(getUpdateModel());
            return bulkUpdateModelList;
        }

        private BulkUpdateModel getUpdateModel() {
            BulkUpdateModel bulkUpdateModel = new BulkUpdateModel();
            bulkUpdateModel.setQueryMap(getQueryMap());
            bulkUpdateModel.setUpdateMap(getUpdateMap());
            return bulkUpdateModel;
        }

        private Map<String, Object> getUpdateMap() {
            Map<String, Object> updateMap = new HashMap<>();
            valueMap.forEach((key, value) -> updateMap.put(String.format("platforms.P%s.fields.%s", cartId, key), value));
            return updateMap;
        }

        private Map<String, Object> getQueryMap() {
            Map<String, Object> queryMap = new HashMap<>();
            queryMap.put("_id", new ObjectId(product.get_id()));
            return queryMap;
        }
    }
}
