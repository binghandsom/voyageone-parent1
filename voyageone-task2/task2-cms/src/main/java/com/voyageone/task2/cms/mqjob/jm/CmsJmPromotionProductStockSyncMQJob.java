package com.voyageone.task2.cms.mqjob.jm;

import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.util.CommonUtil;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.daoext.cms.CmsBtJmPromotionDaoExt;
import com.voyageone.service.daoext.cms.CmsBtJmPromotionProductDaoExt;
import com.voyageone.service.daoext.cms.CmsBtPromotionCodesDaoExtCamel;
import com.voyageone.service.impl.cms.vomq.vomessage.body.jm.JmPromotionProductStockSyncMQMessageBody;
import com.voyageone.service.model.cms.CmsBtJmPromotionProductModel;
import com.voyageone.service.model.cms.CmsBtPromotionCodesModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.task2.cms.mqjob.TBaseMQCmsService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 从mongo:cms_bt_product_cxxx表取得当前有效活动中的产品的库存数，保存到cms_bt_jm_promotion_product.quantity / cms_bt_promotion_codes.quantity
 * 并计算该活动中的有库存的商品数，保存到cms_bt_jm_promotion.prod_sum （此项只针对聚美平台）
 * <p>
 * 从mongo:cms_bt_product_cxxx表取得当前有效活动中的产品的聚美平台的总销量，保存到cms_bt_jm_promotion_product.sales
 *
 * @author jiangjusheng 2016/10/18.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
@RabbitListener()//queues = CmsMqRoutingKey.CMS_BATCH_JmPromotionProductStockSyncServiceJob
public class CmsJmPromotionProductStockSyncMQJob extends TBaseMQCmsService<JmPromotionProductStockSyncMQMessageBody> {

    @Autowired
    private CmsBtProductDao cmsBtProductDao;
    @Autowired
    private CmsBtJmPromotionProductDaoExt jmPromotionProductDaoExt;
    @Autowired
    private CmsBtJmPromotionDaoExt jmPromotionDaoExt;
    @Autowired
    private CmsBtPromotionCodesDaoExtCamel promotionCodesDaoExtCamel;

    @Override
    public void onStartup(JmPromotionProductStockSyncMQMessageBody messageMap) throws Exception {
        $debug("PromotionProductStockSyncService： start");
        // 取得所有店铺
        List<ShopBean> shopList = Shops.getShopList();
        if (shopList == null || shopList.isEmpty()) {
            $error("PromotionProductStockSyncService 店铺及平台数据不存在！");
            return;
        }
        for (ShopBean shopObj : shopList) {
            syncStockByShop(shopObj);
        }
        $debug("PromotionProductStockSyncService: end");
    }

    private void syncStockByShop(ShopBean shopObj) {
        // 对每个店铺进行处理
        String channelId = shopObj.getOrder_channel_id();
        String cartIdStr = shopObj.getCart_id();
        $info("channelId=" + channelId + " cartIdStr" + cartIdStr);
        if (StringUtils.trimToNull(shopObj.getApp_url()) == null) {
            $warn("PromotionProductStockSyncService 店铺数据不完整！ channelId=%s, cartId=%s", channelId, cartIdStr);
            return;
        }

        // 先判断该店铺的cms_bt_product_cxxx表是否存在
        boolean exists = cmsBtProductDao.collectionExists(cmsBtProductDao.getCollectionName(channelId));
        if (!exists) {
            $warn("PromotionProductStockSyncService 本店铺对应的cms_bt_product_cxxx表不存在！ channelId=" + channelId);
            return;
        }

        // 验证该店铺的平台配置
        TypeChannelBean cartBean = TypeChannels.getTypeChannelByCode(Constants.comMtTypeChannel.SKU_CARTS_53, channelId, cartIdStr);
        if (cartBean == null) {
            $error("PromotionProductStockSyncService 本店铺无平台数据！ channelId=%s, cartId=%s", channelId, cartIdStr);
            return;
        }

        // 对指定店铺的每个平台进行处理
        int cartId = NumberUtils.toInt(cartIdStr);

        if (CartEnums.Cart.JM.getId().equals(cartIdStr)) {
            // 聚美平台
            // 取得活动下的产品一览
            List<CmsBtJmPromotionProductModel> jmProdList = jmPromotionProductDaoExt.selectValidProductInfo(channelId, cartId);
            if (jmProdList == null || jmProdList.isEmpty()) {
                $warn("JmPromotionProductStockSyncService 本店铺无活动下的产品数据！ channelId=%s, cartId=%s", channelId, cartIdStr);
                return;
            }
            // 过滤出商品code
            List<String> codeList = jmProdList.stream().map(jmProdObj -> jmProdObj.getProductCode()).distinct().filter(prodCode -> prodCode != null && prodCode.length() > 0).collect(Collectors.toList());
            if (codeList == null || codeList.isEmpty()) {
                $warn("JmPromotionProductStockSyncService 本店铺无活动下的有效产品数据！ channelId=%s, cartId=%s", channelId, cartIdStr);
                return;
            }

            // 再取得各产品的库存
            JongoQuery queryObj = new JongoQuery();
            queryObj.setQuery("{common.fields.code:{$in:#}}");
            queryObj.setParameters(codeList);
            queryObj.setProjectionExt("common.fields.code", "common.fields.quantity", "sales.codeSumAll.cartId27");
            List<CmsBtProductModel> prodList = cmsBtProductDao.select(queryObj, channelId);
            if (prodList == null || prodList.isEmpty()) {
                $warn("JmPromotionProductStockSyncService 无指定的产品数据！ channelId=%s, cartId=%s", channelId, cartIdStr);
                return;
            }

            // 更新活动中产品的库存数据
            for (CmsBtJmPromotionProductModel jmPromProd : jmProdList) {
                String prodCode = StringUtils.trimToNull(jmPromProd.getProductCode());
                if (prodCode == null) {
                    $warn("该数据有问题 CmsBtJmPromotionProductModel id=" + jmPromProd.getId());
                    continue;
                }
                for (CmsBtProductModel prodObj : prodList) {
                    if (prodCode.equals(prodObj.getCommonNotNull().getFieldsNotNull().getCode())) {
                        jmPromProd.setQuantity(prodObj.getCommonNotNull().getFieldsNotNull().getQuantity());
                        if (prodObj.getSales() != null) {
                            jmPromProd.setSales(prodObj.getSales().getCodeSumAll(CartEnums.Cart.JM.getValue()));
                        }
                        break;
                    }
                }
            }

            int rs = 0;
            List<List<CmsBtJmPromotionProductModel>> allList = CommonUtil.splitList(jmProdList, 1000);
            for (List<CmsBtJmPromotionProductModel> promList : allList) {
                rs = jmPromotionProductDaoExt.updateProductStockInfo(promList);
                $debug("JmPromotionProductStockSyncService 更新结果=%d, channelId=%s, cartId=%s", rs, channelId, cartIdStr);
            }

            // 计算该活动中的有库存的商品数及库存总数
            rs = jmPromotionDaoExt.updatePromotionProdSumInfo(channelId, cartId);
            $debug("JmPromotionProductStockSyncService 商品数更新结果=%d, channelId=%s, cartId=%s", rs, channelId, cartIdStr);

        } else {
            // 其他平台
            // 取得活动下的产品一览
            List<CmsBtPromotionCodesModel> promProdList = promotionCodesDaoExtCamel.selectValidProductInfo(channelId, cartId);
            if (promProdList == null || promProdList.isEmpty()) {
                $warn("PromotionProductStockSyncService 本店铺无活动下的产品数据！ channelId=%s, cartId=%s", channelId, cartIdStr);
                return;
            }

            // 过滤出商品code
            List<String> codeList = promProdList.stream().map(prodObj -> prodObj.getProductCode()).distinct().filter(prodCode -> prodCode != null && prodCode.length() > 0).collect(Collectors.toList());
            if (codeList == null || codeList.isEmpty()) {
                $warn("PromotionProductStockSyncService 本店铺无活动下的有效产品数据！ channelId=%s, cartId=%s", channelId, cartIdStr);
                return;
            }

            // 再取得各产品的库存
            JongoQuery queryObj = new JongoQuery();
            queryObj.setQuery("{common.fields.code:{$in:#}}");
            queryObj.setParameters(codeList);
            queryObj.setProjectionExt("common.fields.code", "common.fields.quantity");
            List<CmsBtProductModel> prodList = cmsBtProductDao.select(queryObj, channelId);
            if (prodList == null || prodList.isEmpty()) {
                $warn("PromotionProductStockSyncService 无指定的产品数据！ channelId=%s, cartId=%s", channelId, cartIdStr);
                return;
            }

            // 更新活动中产品的库存数据
            for (CmsBtPromotionCodesModel promProd : promProdList) {
                String prodCode = StringUtils.trimToNull(promProd.getProductCode());
                if (prodCode == null) {
                    $warn("该数据有问题 CmsBtPromotionCodesBean id=" + promProd.getId());
                    continue;
                }
                for (CmsBtProductModel prodObj : prodList) {
                    if (prodCode.equals(prodObj.getCommonNotNull().getFieldsNotNull().getCode())) {
                        promProd.setQuantity(prodObj.getCommonNotNull().getFieldsNotNull().getQuantity());
                        break;
                    }
                }
            }
            List<List<CmsBtPromotionCodesModel>> allList = CommonUtil.splitList(promProdList, 1000);
            for (List<CmsBtPromotionCodesModel> promList : allList) {
                int rs = promotionCodesDaoExtCamel.updateProductStockInfo(promList);
                $debug("PromotionProductStockSyncService 更新结果=%d, channelId=%s, cartId=%s", rs, channelId, cartIdStr);
            }
        }
    }
}
