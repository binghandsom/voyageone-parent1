package com.voyageone.task2.cms.mqjob;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.CommonUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.components.jumei.JumeiHtMallService;
import com.voyageone.components.jumei.bean.HtMallSkuPriceUpdateInfo;
import com.voyageone.service.impl.cms.jumei.CmsBtJmPromotionService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.CmsJmMallPromotionPriceSyncMQMessageBody;
import com.voyageone.service.model.cms.mongo.CmsBtOperationLogModel_Msg;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by james on 2017/1/23.
 */
@Service
@RabbitListener()
public class CmsJmMallPromotionPriceSyncMQJob extends TBaseMQCmsService<CmsJmMallPromotionPriceSyncMQMessageBody> {

    private final CmsBtJmPromotionService cmsBtJmPromotionService;

    private final JumeiHtMallService jumeiHtMallService;

    @Autowired
    public CmsJmMallPromotionPriceSyncMQJob(CmsBtJmPromotionService cmsBtJmPromotionService, JumeiHtMallService jumeiHtMallService) {
        this.cmsBtJmPromotionService = cmsBtJmPromotionService;
        this.jumeiHtMallService = jumeiHtMallService;
    }

    @Override
    public void onStartup(CmsJmMallPromotionPriceSyncMQMessageBody messageBody) throws Exception {
        Integer jmPid = messageBody.getJmPromotionId();
        List<String> productCodes = messageBody.getProductCodes();
        super.count = productCodes.size();

        String channelId = messageBody.getChannelId();
        ShopBean shopBean = Shops.getShop(channelId, CartEnums.Cart.JM.getId());
        if (shopBean == null) {
            $error("JmMallPromotionPriceSyncService 店铺及平台数据不存在！ channelId=%s", channelId);

            throw new BusinessException(String.format("JmMallPromotionPriceSyncService 店铺及平台数据不存在！ channelId=%s", channelId));
        }

        // 找到该活动下所有sku
        List<Map<String, Object>> skus = cmsBtJmPromotionService.selectCloseJmPromotionSku(jmPid);
        List<HtMallSkuPriceUpdateInfo> list = new ArrayList<>();
        HtMallSkuPriceUpdateInfo updateData;
        // 设置请求参数
        for (Map<String, Object> skuPriceBean : skus) {
            if (ListUtils.isNull(productCodes) || productCodes.contains(skuPriceBean.get("product_code").toString())) {
                updateData = new HtMallSkuPriceUpdateInfo();
                list.add(updateData);
                updateData.setJumei_sku_no(skuPriceBean.get("jm_sku_no").toString());
                updateData.setMall_price(Double.parseDouble(skuPriceBean.get("deal_price").toString()));
            }
        }

        List<CmsBtOperationLogModel_Msg> failList = new ArrayList<>();
        List<List<HtMallSkuPriceUpdateInfo>> pageList = CommonUtil.splitList(list, 20);
        for (List<HtMallSkuPriceUpdateInfo> page : pageList) {
            StringBuffer sb = new StringBuffer();
            try {
                if (!jumeiHtMallService.updateMallSkuPrice(shopBean, page, sb)) {
                    $error(sb.toString());

                    CmsBtOperationLogModel_Msg errorInfo = new CmsBtOperationLogModel_Msg();
                    StringBuffer sbSku = new StringBuffer();
                    page.stream().forEach(skuInfo -> sbSku.append(skuInfo.getJumei_sku_no()).append(","));
                    errorInfo.setSkuCode(sbSku.toString());
                    errorInfo.setMsg(sb.toString());
                    failList.add(errorInfo);
                }
            } catch (Exception e) {
                $error(sb.toString());
                CmsBtOperationLogModel_Msg errorInfo = new CmsBtOperationLogModel_Msg();
                StringBuffer sbSku = new StringBuffer();
                page.stream().forEach(skuInfo -> sbSku.append(skuInfo.getJumei_sku_no()).append(","));
                errorInfo.setSkuCode(sbSku.toString());
                errorInfo.setMsg(String.format("更新价格时异常 jmPromId=%d, errmsg=%s", jmPid, e.getMessage()));
                failList.add(errorInfo);
            }
        }

        if (failList.size() > 0) {
            //写业务错误日志
            String comment = String.format("处理总件数(%s), 处理失败件数(%s)", productCodes.size(), failList.size());
            cmsSuccessIncludeFailLog(messageBody, comment, failList);
        }
    }
}
