package com.voyageone.task2.cms.service.jumei;

import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.CommonUtil;
import com.voyageone.components.jumei.JumeiHtMallService;
import com.voyageone.components.jumei.bean.HtMallSkuPriceUpdateInfo;
import com.voyageone.service.daoext.cms.CmsBtJmPromotionDaoExt;
import com.voyageone.service.impl.cms.jumei.CmsBtJmPromotionService;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 聚美活动开始 把商城的价格设置为活动价
 * Created by jiangjusheng on 2016/10/18.
 */
@Service
public class JmMallPromotionPriceSyncService extends BaseTaskService {

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "CmsJmMallPromotionPriceSyncJob";
    }

    @Autowired
    private CmsBtJmPromotionDaoExt jmPromotionDaoExt;
    @Autowired
    private CmsBtJmPromotionService cmsBtJmPromotionService;
    @Autowired
    private JumeiHtMallService jumeiHtMallService;

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {
        // 先找到即将要开始的活动
        List<Integer> jmPids = jmPromotionDaoExt.selectJmPromotionBegin();

        if (jmPids != null && jmPids.size() > 0) {
            ExecutorService es = Executors.newFixedThreadPool(1);
            jmPids.forEach(integer -> es.execute(() -> mallPriceSync(integer)));
            es.shutdown();
            es.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
        }
    }

    // 更新商城中的商品价格为活动价
    private void mallPriceSync(Integer jmPid) {
        // 找到该活动下所有sku
        List<Map<String, Object>> skus = cmsBtJmPromotionService.selectCloseJmPromotionSku(jmPid);
        List<HtMallSkuPriceUpdateInfo> list = new ArrayList<>();
        HtMallSkuPriceUpdateInfo updateData = null;
        // 设置请求参数
        for (Map<String, Object> skuPriceBean : skus) {
            String channelId = skuPriceBean.get("channel_id").toString();
            ShopBean shopBean = Shops.getShop(channelId, CartEnums.Cart.JM.getId());
            if (shopBean == null) {
                $error("JmMallPromotionPriceSyncService 店铺及平台数据不存在！ channelId=%s, skuinfo=%s", channelId, skuPriceBean.toString());
                continue;
            }
            updateData = new HtMallSkuPriceUpdateInfo();
            list.add(updateData);
            updateData.setJumei_sku_no(skuPriceBean.get("jm_sku_no").toString());
            updateData.setMall_price(Double.parseDouble(skuPriceBean.get("market_price").toString()));
        }

        ShopBean shopBean = Shops.getCartShopList(CartEnums.Cart.JM.getId()).get(0);

        List<List<HtMallSkuPriceUpdateInfo>> pageList = CommonUtil.splitList(list, 20);
        for (List<HtMallSkuPriceUpdateInfo> page : pageList) {
            StringBuffer sb = new StringBuffer();
            try {
                if (!jumeiHtMallService.updateMallSkuPrice(shopBean, page, sb)) {
                    $error(sb.toString());
                }
            } catch (Exception e) {
                $error(String.format("更新价格时异常 jmPromId=%d, errmsg=%s", jmPid, e.getMessage()), e);
            }
        }
    }
}
