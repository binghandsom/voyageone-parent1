package com.voyageone.task2.cms.service;

import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.CommonUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.components.jumei.JumeiHtMallService;
import com.voyageone.components.jumei.bean.HtMallSkuPriceUpdateInfo;
import com.voyageone.service.bean.cms.jumei.SkuPriceBean;
import com.voyageone.service.impl.cms.jumei.CmsBtJmPromotionService;
import com.voyageone.service.model.cms.CmsBtImagesModel;
import com.voyageone.task2.base.BaseCronTaskService;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by james on 2016/10/18.
 */
@Service
public class CmsJmMallPriceReductionService extends BaseCronTaskService {
    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "CmsJmMallPriceReductionJob";
    }

    @Autowired
    CmsBtJmPromotionService cmsBtJmPromotionService;

    @Autowired
    JumeiHtMallService jumeiHtMallService;

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {
        List<Integer> jmPids = cmsBtJmPromotionService.selectCloseJmPromotionId();

        if(jmPids != null && jmPids.size()>0){
            ExecutorService es  = Executors.newFixedThreadPool(1);
            jmPids.forEach(integer -> es.execute(()->mallPriceReduction(integer)));
            es.shutdown();
            es.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
        }
    }

    private void mallPriceReduction(Integer jmPid) {
        List<Map<String, Object>> skus = cmsBtJmPromotionService.selectCloseJmPromotionSku(jmPid);
        List<HtMallSkuPriceUpdateInfo> list = new ArrayList<>();
        HtMallSkuPriceUpdateInfo updateData = null;
        //设置请求参数
        for (Map<String, Object> skuPriceBean : skus) {
            updateData = new HtMallSkuPriceUpdateInfo();
            list.add(updateData);
            updateData.setJumei_sku_no(skuPriceBean.get("jm_sku_no").toString());
            updateData.setMall_price(Double.parseDouble(skuPriceBean.get("sale_price").toString()));
            updateData.setMarket_price(Double.parseDouble(skuPriceBean.get("market_price").toString()));
        }

        ShopBean shopBean = Shops.getShop(skus.get(0).get("channel_id").toString(), CartEnums.Cart.JM.getId());
        String errorMsg = "";
        List<List<HtMallSkuPriceUpdateInfo>> pageList = CommonUtil.splitList(list, 15);
        for (List<HtMallSkuPriceUpdateInfo> page : pageList) {
            StringBuffer sb = new StringBuffer();
            try {
                $info(String.format("channelId=%s  Jumei_sku_noList=%s",shopBean.getOrder_channel_id(), page.stream().map(HtMallSkuPriceUpdateInfo::getJumei_sku_no).collect(Collectors.joining(","))));
                if (!jumeiHtMallService.updateMallSkuPrice(shopBean, page, sb)) {
                    errorMsg += sb.toString();
                    $info(errorMsg);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        cmsBtJmPromotionService.updatePromotionStatus(jmPid,getTaskName());
    }
}
