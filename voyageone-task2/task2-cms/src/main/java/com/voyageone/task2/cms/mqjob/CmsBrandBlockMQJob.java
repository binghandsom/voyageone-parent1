package com.voyageone.task2.cms.mqjob;

import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.base.dao.mongodb.JongoUpdate;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.CmsConstants.PlatformStatus;
import com.voyageone.common.masterdate.schema.utils.JsonUtil;
import com.voyageone.service.impl.cms.CmsBtBrandBlockService;
import com.voyageone.service.impl.cms.feed.FeedInfoService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;
import com.voyageone.service.impl.cms.vomq.vomessage.body.CmsBrandBlockMQMessageBody;
import com.voyageone.service.model.cms.CmsBtBrandBlockModel;
import com.voyageone.service.model.cms.mongo.CmsBtOperationLogModel_Msg;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.task2.cms.service.platform.CmsPlatformActiveLogService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.voyageone.common.CmsConstants.PlatformActive.ToInStock;

/**
 * 执行品牌屏蔽（解除屏蔽）的额外操作。如查找品牌相关的商品，并下架锁定这些商品
 * Created by jonas on 9/12/16.
 *
 * @version 2.6.0
 * @since 2.6.0
 */
@Service
@RabbitListener//有引用类CmsPlatformActiveLogService  暂不移动业务代码到service  (queues = CmsMqRoutingKey.CMS_TASK_BRANDBLOCKJOB)
public class CmsBrandBlockMQJob extends TBaseMQCmsService<CmsBrandBlockMQMessageBody> {

    private final FeedInfoService feedInfoService;

    private final ProductService productService;

    private final CmsPlatformActiveLogService platformActiveLogService;

    @Autowired
    public CmsBrandBlockMQJob(FeedInfoService feedInfoService, ProductService productService, CmsPlatformActiveLogService platformActiveLogService) {
        this.feedInfoService = feedInfoService;
        this.productService = productService;
        this.platformActiveLogService = platformActiveLogService;
    }

    @Override
    public void onStartup(CmsBrandBlockMQMessageBody messageBody) throws Exception {

        boolean blocking = messageBody.isBlocking();
        CmsBtBrandBlockModel dataObject = messageBody.getData();

        List<CmsBtOperationLogModel_Msg> failList = new ArrayList<>();

        if (blocking)
            failList = block(dataObject);
        else
            unblock(dataObject);

        if (failList.size() > 0) {
            //写业务错误日志
            String comment = String.format("处理失败件数(%s)",  failList.size());
            cmsSuccessIncludeFailLog(messageBody, comment, failList);
        }
    }

    private void unblock(CmsBtBrandBlockModel brandBlockModel) {
        switch (brandBlockModel.getType()) {
            case CmsBtBrandBlockService.BRAND_TYPE_FEED:
                unblockFeedBrand(brandBlockModel);
                break;
            case CmsBtBrandBlockService.BRAND_TYPE_MASTER:
                unblockMasterBrand(brandBlockModel);
                break;
            case CmsBtBrandBlockService.BRAND_TYPE_PLATFORM:
                // 品牌品牌解除不需要进行额外的处理
                // 因为屏蔽时只做了下架处理，没有做任何其他处理
                break;
        }
    }

    /**
     * 解除主数据品牌的屏蔽，解锁这些品牌的商品
     *
     * @param brandBlockModel 被解锁品牌的数据模型
     */
    private void unblockMasterBrand(CmsBtBrandBlockModel brandBlockModel) {

        String channelId = brandBlockModel.getChannelId();

        List<CmsBtProductModel> productModelList = productService.getList(channelId, JongoQuery.simple("common.fields.brand", brandBlockModel.getBrand()));

        if (productModelList == null || productModelList.isEmpty())
            return;

        for (CmsBtProductModel productModel : productModelList)
            unlockProduct(productModel.getCommon().getFields().getCode(), channelId);
    }

    /**
     * 解除 Feed 品牌的屏蔽，解锁这些品牌商品对应的主数据商品，并修改 Feed 数据的状态
     *
     * @param brandBlockModel 被解锁品牌的数据模型
     */
    private void unblockFeedBrand(CmsBtBrandBlockModel brandBlockModel) {

        String channelId = brandBlockModel.getChannelId();

        List<CmsBtFeedInfoModel> feedInfoModelList = feedInfoService.getList(channelId, JongoQuery.simple("brand", brandBlockModel.getBrand()));

        if (feedInfoModelList == null || feedInfoModelList.isEmpty())
            return;

        for (CmsBtFeedInfoModel feedInfoModel : feedInfoModelList) {

            String code = feedInfoModel.getCode();

            CmsBtProductModel productModel = productService.getProductByCode(channelId, code);

            // 如果有主数据商品，就把 feed 标记为导入成功
            // 否则，就标记为新商品
            // 如果有主数据商品，还需要额外解锁这些商品
            // 因为屏蔽时，会锁定它们
            if (productModel == null) {
                feedInfoModel.setUpdFlg(CmsConstants.FeedUpdFlgStatus.New);
            } else {
                feedInfoModel.setUpdFlg(CmsConstants.FeedUpdFlgStatus.Succeed);
                unlockProduct(code, channelId);
            }

            feedInfoService.updateFeedInfo(feedInfoModel);
        }
    }

    private List<CmsBtOperationLogModel_Msg> block(CmsBtBrandBlockModel brandBlockModel) {
        List<CmsBtOperationLogModel_Msg> result = new ArrayList<>();
        switch (brandBlockModel.getType()) {
            case CmsBtBrandBlockService.BRAND_TYPE_FEED:
                result = blockFeedBrand(brandBlockModel);
                break;
            case CmsBtBrandBlockService.BRAND_TYPE_MASTER:
                result = blockMasterBrand(brandBlockModel);
                break;
            case CmsBtBrandBlockService.BRAND_TYPE_PLATFORM:
                result = blockPlatformBrand(brandBlockModel);
                break;
        }

        return result;
    }

    /**
     * 屏蔽平台品牌，同时下架这些品牌的商品
     *
     * @param brandBlockModel 屏蔽品牌的数据模型
     */
    private List<CmsBtOperationLogModel_Msg> blockPlatformBrand(CmsBtBrandBlockModel brandBlockModel) {

        List<CmsBtOperationLogModel_Msg> failList = new ArrayList<>();

        String channelId = brandBlockModel.getChannelId();

        List<CmsBtProductModel> productModelList = productService.getList(channelId,
                JongoQuery.simple("platforms.P" + brandBlockModel.getCartId() + ".pBrandId", brandBlockModel.getBrand()));

        if (productModelList == null || productModelList.isEmpty()) {
            return failList;
        }

        OffShelfHelper offShelfHelper = new OffShelfHelper(channelId);

        productModelList.forEach(offShelfHelper::addProduct);

        try {
            offShelfHelper.offThem();
        } catch (BlockBrandOffShelfFailException e) {
            logIssue(e.getCause());
            CmsBtOperationLogModel_Msg errorInfo = new CmsBtOperationLogModel_Msg();
            errorInfo.setSkuCode(JsonUtil.bean2Json(offShelfHelper.getCodeList()));
            errorInfo.setMsg(e.getMessage());
            failList.add(errorInfo);
        }

        return failList;
    }

    /**
     * 屏蔽主数据品牌，同时下架这些品牌的商品。并最终锁定这些商品
     *
     * @param brandBlockModel 屏蔽品牌的数据模型
     */
    private List<CmsBtOperationLogModel_Msg> blockMasterBrand(CmsBtBrandBlockModel brandBlockModel) {

        List<CmsBtOperationLogModel_Msg> failList = new ArrayList<>();

        String channelId = brandBlockModel.getChannelId();

        List<CmsBtProductModel> productModelList = productService.getList(channelId, JongoQuery.simple("common.fields.brand", brandBlockModel.getBrand()));

        if (productModelList == null || productModelList.isEmpty())
            return failList;

        for (CmsBtProductModel productModel : productModelList) {
            OffShelfHelper offShelfHelper = new OffShelfHelper(channelId);
            offShelfHelper.addProduct(productModel);
            try {
                offShelfHelper.offThem();
            } catch (BlockBrandOffShelfFailException e) {
                logIssue(e.getCause());
                CmsBtOperationLogModel_Msg errorInfo = new CmsBtOperationLogModel_Msg();
                errorInfo.setSkuCode(JsonUtil.bean2Json(offShelfHelper.getCodeList()));
                errorInfo.setMsg(e.getMessage());
                failList.add(errorInfo);
                continue;
            }
            // 在下架完成之后
            // 锁定商品
            lockProduct(productModel.getCommon().getFields().getCode(), channelId);
        }

        return failList;
    }

    /**
     * 屏蔽 Feed 品牌，同时下架这些品牌的商品对应的平台商品。并最终锁定所有的平台商品
     *
     * @param brandBlockModel 屏蔽品牌的数据模型
     */
    private List<CmsBtOperationLogModel_Msg> blockFeedBrand(CmsBtBrandBlockModel brandBlockModel) {

        List<CmsBtOperationLogModel_Msg> failList = new ArrayList<>();

        String channelId = brandBlockModel.getChannelId();

        List<CmsBtFeedInfoModel> feedInfoModelList = feedInfoService.getList(channelId, JongoQuery.simple("brand", brandBlockModel.getBrand()));

        if (feedInfoModelList == null || feedInfoModelList.isEmpty())
            return failList;

        // 先更新所有的 feed upFlg 为 7，标记为黑名单商品
        // 之后，在过滤记录那些已经有 master 数据的商品 code
        // 在获取了 cart id 集合之后，统一通知下架
        for (CmsBtFeedInfoModel feedInfoModel : feedInfoModelList) {

            feedInfoModel.setUpdFlg(CmsConstants.FeedUpdFlgStatus.FeedBlackList);

            feedInfoService.updateFeedInfo(feedInfoModel);

            String code = feedInfoModel.getCode();

            CmsBtProductModel productModel = productService.getProductByCode(channelId, code);

            if (productModel == null)
                continue;

            OffShelfHelper offShelfHelper = new OffShelfHelper(channelId);
            offShelfHelper.addProduct(productModel);
            try {
                offShelfHelper.offThem();
            } catch (BlockBrandOffShelfFailException e) {
                // 下架失败
                // 跳过锁定
                logIssue(e.getCause());
                CmsBtOperationLogModel_Msg errorInfo = new CmsBtOperationLogModel_Msg();
                errorInfo.setSkuCode(JsonUtil.bean2Json(offShelfHelper.getCodeList()));
                errorInfo.setMsg(e.getMessage());
                failList.add(errorInfo);
                continue;
            }
            // 在下架完成之后
            // 锁定商品
            lockProduct(feedInfoModel.getCode(), channelId);
        }

        return failList;
    }

    private void lockProduct(String code, String channelId) {
        productService.updateFirstProduct(new JongoUpdate()
                .setQuery("{\"common.fields.code\":\"" + code + "\"}")
                .setUpdate("{$set:{\"lock\":\"1\", \"comment\":\"Feed 品牌加入黑名单，所有商品不能上架\"}}"), channelId);
    }

    private void unlockProduct(String code, String channelId) {
        productService.updateFirstProduct(new JongoUpdate()
                .setQuery("{\"common.fields.code\":\"" + code + "\"}")
                .setUpdate("{$set:{\"lock\":\"0\", \"comment\":\"\"}}"), channelId);
    }

    private class OffShelfHelper {

        private Map<String, Object> mqParams = new HashMap<>(6, 1f);

        private Set<String> codeList = new HashSet<>();

        private Set<Integer> cartIdList = new HashSet<>();

        OffShelfHelper(String channelId) {
            mqParams.put("channelId", channelId);
            mqParams.put("creater", CmsMqRoutingKey.CMS_BRAND_BLOCK);
            mqParams.put("activeStatus", ToInStock.name());
            mqParams.put("cartIdList", cartIdList);
            mqParams.put("comment", "Feed 品牌黑名单下架");
            mqParams.put("codeList", codeList);
        }

        private void addCartId(Integer cartId) {
            cartIdList.add(cartId);
        }

        private void addCode(String code) {
            codeList.add(code);
        }

        void addProduct(CmsBtProductModel productModel) {
            productModel.getPlatforms().forEach((k, platform) -> {
                if (PlatformStatus.OnSale.equals(platform.getpStatus())) {
                    addCode(productModel.getCommon().getFields().getCode());
                    addCartId(platform.getCartId());
                }
            });
        }

        Set<String> getCodeList() {
            return codeList;
        }

        void offThem() throws BlockBrandOffShelfFailException {

            if (cartIdList.isEmpty() || codeList.isEmpty())
                return;
            try {
                for (Integer cartId : cartIdList) {
                    mqParams.remove("cartIdList", cartIdList);
                    mqParams.put("cartId", cartId);
                    platformActiveLogService.setProductOnSaleOrInStock(mqParams);
                }
            } catch (Exception e) {
                throw new BlockBrandOffShelfFailException(e);
            }
        }
    }

    private class BlockBrandOffShelfFailException extends Exception {
        BlockBrandOffShelfFailException(Exception e) {
            super(e);
        }
    }
}
